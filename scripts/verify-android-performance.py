#!/usr/bin/env python3
"""Collect and validate the Android runtime performance proxy contract.

This is intentionally an emulator/runtime measurement, not a replacement for
Android Macrobenchmark or OEM/device certification.  ``--collect`` records
``am start -W`` startup samples and ``dumpsys gfxinfo`` frame statistics.  The
report uses ``MEASURED`` only when the platform returns all required fields;
missing adb/emulator data is ``UNMEASURED`` and cannot become a green SLO.
"""

from __future__ import annotations

import argparse
import csv
import json
import io
import math
import os
import platform
import re
import subprocess
import sys
import time
from datetime import datetime, timezone
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_REPORT = ROOT / "build/reports/android-runtime/performance.json"
STARTUP_SAMPLES = 5
STARTUP_WARMUP_SAMPLES = 1
STARTUP_BUDGET_MS = 2_000.0
FRAME_PROXY_BUDGET_MS = 32.0
# An idle post-settle window can contain very few frames; below this count the
# nearest-rank p95 approximates the worst single frame, so the report must say so.
FRAME_MIN_RECOMMENDED_SAMPLES = 30
FRAME_SETTLE_SECONDS = 1.0


def fail(message: str) -> "NoReturn":
    print(f"android performance: FAIL: {message}", file=sys.stderr)
    raise SystemExit(1)


def require(condition: bool, message: str) -> None:
    if not condition:
        fail(message)


def run_adb(adb: str, serial: str, *args: str, timeout: float = 30.0) -> subprocess.CompletedProcess[str]:
    return subprocess.run(
        [adb, "-s", serial, *args],
        check=False,
        capture_output=True,
        text=True,
        timeout=timeout,
    )


def parse_millis(output: str, label: str) -> float | None:
    # `am start -W` reports bare milliseconds (e.g. `TotalTime: 351`), while
    # gfxinfo percentiles append `ms`; accept both forms but normalize to ms.
    match = re.search(rf"(?mi)^\s*{re.escape(label)}:\s*([0-9]+(?:\.[0-9]+)?)\s*(?:ms)?\s*$", output)
    return float(match.group(1)) if match else None


def parse_int(output: str, label: str) -> int | None:
    match = re.search(rf"(?mi)^\s*{re.escape(label)}:\s*([0-9]+)", output)
    return int(match.group(1)) if match else None


def parse_percentile(output: str, percentile: int) -> float | None:
    # Android's human-readable gfxinfo output is stable across API 29-35, but
    # keep the parser permissive for localized spacing and decimal values.
    match = re.search(
        rf"(?mi)^\s*{percentile}(?:th|st|nd|rd) percentile:\s*([0-9]+(?:\.[0-9]+)?)\s*ms\s*$",
        output,
    )
    return float(match.group(1)) if match else None


def parse_framestats_durations(output: str) -> list[float]:
    """Return frame durations from Android's machine-readable PROFILEDATA.

    Human-readable ``gfxinfo`` percentiles are useful as a fallback, but they
    are rounded into coarse buckets (and API versions differ in their
    formatting).  API 29+ also exposes a CSV ``PROFILEDATA`` section.  The
    UI-facing frame duration is the time from the intended vsync to the
    completed frame; GPU completion is deliberately not used because the
    goldfish emulator can report a synthetic 4.95s GPU queue while the UI
    timeline is still valid.  Invalid/unfinished rows are skipped instead of
    being converted into a green measurement.
    """
    marker = "---PROFILEDATA---"
    if marker not in output:
        return []
    section = output.split(marker, 1)[1]
    lines = [line.strip() for line in section.splitlines() if line.strip()]
    if not lines:
        return []
    try:
        rows = csv.DictReader(io.StringIO("\n".join(lines)))
    except csv.Error:
        return []
    durations: list[float] = []
    for row in rows:
        try:
            intended = float(row.get("IntendedVsync", "0"))
            completed = float(row.get("FrameCompleted", "0"))
        except (TypeError, ValueError):
            continue
        if intended <= 0 or completed <= intended:
            continue
        duration = (completed - intended) / 1_000_000.0
        if math.isfinite(duration) and 0.0 < duration <= 10_000.0:
            durations.append(duration)
    return durations


def nearest_rank_percentile(samples: list[float], percentile: int) -> float | None:
    if not samples:
        return None
    ordered = sorted(samples)
    index = min(len(ordered) - 1, max(0, (len(ordered) * percentile + 99) // 100 - 1))
    return ordered[index]


def finite(value: object, name: str) -> float:
    require(isinstance(value, (int, float)) and not isinstance(value, bool), f"{name} must be numeric")
    result = float(value)
    require(math.isfinite(result) and result >= 0, f"{name} must be finite and non-negative")
    return result


def write_json(path: Path, report: dict[str, Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(report, ensure_ascii=False, indent=2, sort_keys=True) + "\n", encoding="utf-8")


def collect(args: argparse.Namespace, report_path: Path) -> int:
    adb = str(args.adb)
    serial = str(args.serial)
    package = str(args.package)
    activity = str(args.activity)
    component = f"{package}/{activity}"
    timestamp = datetime.now(timezone.utc).isoformat().replace("+00:00", "Z")
    base: dict[str, Any] = {
        "schema": "stylish-ui.android-runtime-performance.v1",
        "scope": "Android API 35 emulator runtime performance proxy",
        "not_a_production_device_slo": True,
        "scopeGuard": {
            "startupSource": "adb shell am start -W",
            "frameSource": "adb shell dumpsys gfxinfo",
            "frameMetricIsProxy": True,
            "macrobenchmarkRequiredForRelease": True,
            "talkbackVoiceOverOemUnmeasured": True,
            "emulatorOnly": True,
            "measuredBuildTypeIsDebug": True,
            "sampleAppScope": (
                "minimal single-screen consumer (StylishTheme + paginated StylishDataTable); "
                "not the component catalog"
            ),
        },
        "environment": {
            "serial": serial,
            "package": package,
            "activity": activity,
            "buildType": str(args.build_type),
            "os": platform.system(),
            "arch": platform.machine(),
            "recordedAt": timestamp,
            "revision": os.environ.get("GITHUB_SHA", "local"),
        },
        "budgets": {
            "startupP95Millis": STARTUP_BUDGET_MS,
            "frameProxyP95Millis": FRAME_PROXY_BUDGET_MS,
        },
    }

    try:
        state = run_adb(adb, serial, "get-state")
    except (OSError, subprocess.TimeoutExpired) as error:
        base["status"] = "UNMEASURED"
        base["reason"] = f"adb unavailable: {error}"
        write_json(report_path, base)
        if args.require:
            fail(base["reason"])
        print("android performance: UNMEASURED (adb unavailable; no SLO claim)")
        return 0
    if state.returncode != 0 or state.stdout.strip() != "device":
        base["status"] = "UNMEASURED"
        base["reason"] = "emulator/device is not online"
        write_json(report_path, base)
        if args.require:
            fail(base["reason"])
        print("android performance: UNMEASURED (device unavailable; no SLO claim)")
        return 0

    startup_samples: list[float] = []
    startup_raw: list[str] = []
    # One unrecorded-in-p95 warmup launch absorbs first-launch-after-install
    # work (install-time dexopt/verification) so recorded samples represent
    # steady cold-process starts. The warmup value itself stays in the report.
    startup_warmup: list[float] = []
    for _ in range(max(0, args.startup_warmup_samples)):
        run_adb(adb, serial, "shell", "am", "force-stop", package)
        try:
            warmed = run_adb(adb, serial, "shell", "am", "start", "-W", "-n", component, timeout=45.0)
        except subprocess.TimeoutExpired:
            continue
        warm_millis = parse_millis(warmed.stdout + warmed.stderr, "TotalTime")
        if warmed.returncode == 0 and warm_millis is not None:
            startup_warmup.append(warm_millis)
        time.sleep(0.2)
    for _ in range(STARTUP_SAMPLES):
        run_adb(adb, serial, "shell", "am", "force-stop", package)
        try:
            started = run_adb(adb, serial, "shell", "am", "start", "-W", "-n", component, timeout=45.0)
        except subprocess.TimeoutExpired as error:
            startup_raw.append(f"timeout: {error}")
            continue
        startup_raw.append(started.stdout + started.stderr)
        measured = parse_millis(started.stdout + started.stderr, "TotalTime")
        if started.returncode == 0 and measured is not None:
            startup_samples.append(measured)
        time.sleep(0.2)

    # Let the first composition/measure/layout settle before starting the
    # interaction window. Reset twice: the first reset clears frames from the
    # startup samples, the settle interval absorbs late initial work, and the
    # second reset makes the reported window explicit and reproducible.
    run_adb(adb, serial, "shell", "dumpsys", "gfxinfo", package, "reset")
    time.sleep(args.frame_settle_seconds)
    run_adb(adb, serial, "shell", "dumpsys", "gfxinfo", package, "reset")
    run_adb(adb, serial, "shell", "input", "keyevent", "KEYCODE_TAB")
    time.sleep(1.0)
    try:
        gfx = run_adb(adb, serial, "shell", "dumpsys", "gfxinfo", package, "framestats", timeout=45.0)
        gfx_output = gfx.stdout + gfx.stderr
    except subprocess.TimeoutExpired as error:
        gfx_output = f"timeout: {error}"
    total_frames = parse_int(gfx_output, "Total frames rendered")
    janky_frames = parse_int(gfx_output, "Janky frames")
    frame_samples = parse_framestats_durations(gfx_output)
    frame_p95 = nearest_rank_percentile(frame_samples, 95) or parse_percentile(gfx_output, 95)
    frame_deadline_misses = sum(1 for value in frame_samples if value > FRAME_PROXY_BUDGET_MS)
    frame_small_sample = len(frame_samples) < FRAME_MIN_RECOMMENDED_SAMPLES
    startup_sorted = sorted(startup_samples)
    startup_p95 = startup_sorted[min(len(startup_sorted) - 1, max(0, (len(startup_sorted) * 95 + 99) // 100 - 1))] if startup_sorted else None
    startup_measured = len(startup_samples) == STARTUP_SAMPLES and startup_p95 is not None
    frame_measured = total_frames is not None and janky_frames is not None and frame_p95 is not None
    status = "PASS" if startup_measured and frame_measured and startup_p95 <= STARTUP_BUDGET_MS and frame_p95 <= FRAME_PROXY_BUDGET_MS else "UNMEASURED"
    if startup_measured and frame_measured and status != "PASS":
        status = "FAIL"
    base.update(
        {
            "status": status,
            "startup": {
                "status": "MEASURED" if startup_measured else "UNMEASURED",
                "kind": "cold-process start (am force-stop + am start -W TotalTime)",
                "samplesMillis": startup_samples,
                "sampleCount": len(startup_samples),
                "requiredSamples": STARTUP_SAMPLES,
                "warmupSamplesExcludedFromP95": len(startup_warmup),
                "warmupSampleMillis": startup_warmup,
                "statistic": "nearestRankP95",
                "statisticNote": "with 5 samples the nearest-rank p95 equals the worst observed cold start",
                "p95Millis": startup_p95,
                "rawAmStartOutputCaptured": len(startup_raw),
            },
            "frameTimeProxy": {
                "status": "MEASURED" if frame_measured else "UNMEASURED",
                "totalFrames": total_frames,
                "jankyFrames": janky_frames,
                "p95Millis": frame_p95,
                "samplesMillis": frame_samples,
                "sampleCount": len(frame_samples),
                "budgetDeadlineMillis": FRAME_PROXY_BUDGET_MS,
                "deadlineMissCount": frame_deadline_misses,
                "minMillis": min(frame_samples) if frame_samples else None,
                "medianMillis": sorted(frame_samples)[len(frame_samples) // 2] if frame_samples else None,
                "maxMillis": max(frame_samples) if frame_samples else None,
                "minimumRecommendedSamples": FRAME_MIN_RECOMMENDED_SAMPLES,
                "smallSampleCaveat": (
                    f"only {len(frame_samples)} frames in the window; nearest-rank p95 approximates "
                    "the worst single frame and is not a stable distribution estimate"
                    if frame_small_sample
                    else None
                ),
                "windowProtocol": (
                    "double gfxinfo reset around a settle interval excludes startup frames; the "
                    "reported window covers one KEYCODE_TAB interaction plus idle until the "
                    "framestats dump"
                ),
                "metric": (
                    "gfxinfo framestats FrameCompleted-IntendedVsync nearest-rank p95"
                    if frame_samples
                    else "gfxinfo human-readable frame percentile fallback"
                ),
                "settleSecondsBeforeMeasurement": args.frame_settle_seconds,
            },
            "reason": None if status in {"PASS", "FAIL"} else "required platform fields were not returned",
        }
    )
    write_json(report_path, base)
    if args.require and status != "PASS":
        fail(f"runtime performance status is {status}; report is honest but not a green SLO: {report_path}")
    print(
        f"android performance: {status} (startup p95={startup_p95}, frame proxy p95={frame_p95}; "
        "emulator proxy only, not production device SLO)"
    )
    return 0


def verify(args: argparse.Namespace, report_path: Path) -> int:
    require(report_path.is_file(), f"missing report: {report_path}")
    try:
        report = json.loads(report_path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        fail(f"invalid report: {error}")
    require(isinstance(report, dict), "report root must be an object")
    require(report.get("schema") == "stylish-ui.android-runtime-performance.v1", "unexpected schema")
    require(report.get("scope") == "Android API 35 emulator runtime performance proxy", "unexpected scope")
    require(report.get("not_a_production_device_slo") is True, "production device scope guard missing")
    guard = report.get("scopeGuard")
    require(isinstance(guard, dict) and guard.get("frameMetricIsProxy") is True, "frame proxy scope guard missing")
    require(guard.get("macrobenchmarkRequiredForRelease") is True, "macrobenchmark requirement missing")
    require(guard.get("emulatorOnly") is True, "emulator-only scope guard missing")
    environment = report.get("environment")
    require(
        isinstance(environment, dict)
        and isinstance(environment.get("buildType"), str)
        and bool(environment["buildType"]),
        "environment.buildType is required to distinguish debug proxy from release SLO",
    )
    status = report.get("status")
    require(status in {"PASS", "FAIL", "UNMEASURED"}, "status must be PASS, FAIL, or UNMEASURED")
    budgets = report.get("budgets")
    require(isinstance(budgets, dict), "budgets must be an object")
    startup = report.get("startup")
    frame = report.get("frameTimeProxy")
    require(isinstance(startup, dict) and isinstance(frame, dict), "startup/frame sections are required")
    if status == "PASS":
        require(startup.get("status") == "MEASURED", "PASS startup must be measured")
        require(frame.get("status") == "MEASURED", "PASS frame proxy must be measured")
        startup_p95 = finite(startup.get("p95Millis"), "startup.p95Millis")
        frame_p95 = finite(frame.get("p95Millis"), "frameTimeProxy.p95Millis")
        require(startup_p95 <= finite(budgets.get("startupP95Millis"), "budgets.startupP95Millis"), "startup p95 exceeds budget")
        require(frame_p95 <= finite(budgets.get("frameProxyP95Millis"), "budgets.frameProxyP95Millis"), "frame proxy p95 exceeds budget")
    elif args.require:
        fail(f"required measured PASS but report status is {status}")
    print(f"android performance report: {status} (emulator proxy; no production device SLO claim)")
    return 0


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--collect", action="store_true", help="collect from adb and write a report")
    parser.add_argument("--verify", action="store_true", help="verify an existing report")
    parser.add_argument("--require", action="store_true", help="fail unless a measured PASS report exists")
    parser.add_argument("--adb", default=os.environ.get("ADB", "adb"))
    parser.add_argument("--serial", default=os.environ.get("ANDROID_SERIAL", "emulator-5554"))
    parser.add_argument("--package", default="com.segnities007.stylishui.androidruntime")
    parser.add_argument("--activity", default=".MainActivity")
    parser.add_argument(
        "--frame-settle-seconds",
        type=float,
        default=FRAME_SETTLE_SECONDS,
        help="idle interval before the measured frame window (default: 1s)",
    )
    parser.add_argument(
        "--startup-warmup-samples",
        type=int,
        default=STARTUP_WARMUP_SAMPLES,
        help="unrecorded warmup launches after install before recorded startup samples (default: 1)",
    )
    parser.add_argument(
        "--build-type",
        default="debug",
        help="buildType recorded in the report scope guard (default: debug)",
    )
    parser.add_argument("--report", type=Path, default=DEFAULT_REPORT)
    args = parser.parse_args()
    require(args.frame_settle_seconds >= 0, "--frame-settle-seconds must be non-negative")
    require(args.startup_warmup_samples >= 0, "--startup-warmup-samples must be non-negative")
    report = args.report if args.report.is_absolute() else ROOT / args.report
    if args.collect:
        return collect(args, report)
    if args.verify or not args.collect:
        return verify(args, report)
    return 0


if __name__ == "__main__":
    main()

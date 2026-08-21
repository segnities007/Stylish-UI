#!/usr/bin/env python3
"""Create and validate a bounded Wasm production bundle evidence report.

The report is intentionally byte based and scoped to the packaged production
artifact.  It is not a browser frame-time, startup, memory, or network
performance claim.  A committed baseline gives every run a deterministic
comparison point; the report labels a single CI run as baseline-relative and
does not pretend that it is a long-term trend until hosted artifacts are
retained across runs.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import platform
import sys
from datetime import datetime, timezone
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_BASELINE = ROOT / "docs/wasm-bundle-baseline.json"
DEFAULT_OUTPUT = ROOT / "website-wasm/build/ci-evidence/wasm-bundle-size.json"
DEFAULT_HISTORY = ROOT / "website-wasm/build/ci-evidence/wasm-bundle-history.json"


def fail(message: str) -> "NoReturn":
    print(f"wasm bundle evidence: FAIL: {message}", file=sys.stderr)
    raise SystemExit(1)


def require(condition: bool, message: str) -> None:
    if not condition:
        fail(message)


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as source:
        for block in iter(lambda: source.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def load_object(path: Path) -> dict[str, Any]:
    display = path.relative_to(ROOT) if path.is_relative_to(ROOT) else path
    require(path.is_file(), f"missing {display}")
    try:
        value = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        fail(f"invalid JSON in {path}: {error}")
    require(isinstance(value, dict), f"{path} root must be an object")
    return value


def json_write(path: Path, value: dict[str, Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2, sort_keys=True) + "\n", encoding="utf-8")


def verify_report(path: Path) -> int:
    report = load_object(path)
    require(report.get("schema") == "stylish-ui.wasm-bundle-evidence.v1", "unexpected report schema")
    require(report.get("scope") == "Wasm production bundle byte budget", "unexpected report scope")
    require(report.get("not_a_runtime_slo") is True, "runtime scope guard is missing")
    require(report.get("status") in {"PASS", "FAIL"}, "status must be PASS or FAIL")
    budgets = report.get("budgets")
    current = report.get("current")
    baseline = report.get("baseline")
    delta = report.get("deltaFromBaseline")
    require(isinstance(budgets, dict) and isinstance(current, dict), "budgets/current objects are required")
    require(isinstance(baseline, dict) and isinstance(delta, dict), "baseline/delta objects are required")
    for section, fields in (("budgets", ("jsBytes", "wasmBytes")), ("current", ("jsBytes", "wasmBytes")), ("baseline", ("jsBytes", "wasmBytes"))):
        values = {"budgets": budgets, "current": current, "baseline": baseline}[section]
        for field in fields:
            value = values.get(field)
            require(isinstance(value, int) and value > 0, f"{section}.{field} must be positive integer")
    require(current["jsBytes"] <= budgets["jsBytes"] and current["wasmBytes"] <= budgets["wasmBytes"], "current artifact exceeds budget")
    require(delta.get("jsBytes") == current["jsBytes"] - baseline["jsBytes"], "JS delta is inconsistent")
    require(delta.get("wasmBytes") == current["wasmBytes"] - baseline["wasmBytes"], "Wasm delta is inconsistent")
    for field in ("jsSha256", "wasmSha256"):
        require(isinstance(current.get(field), str) and len(current[field]) == 64, f"current.{field} is missing")
    history = report.get("history")
    require(isinstance(history, dict) and history.get("trendClaimAllowed") is False, "history trend scope must remain conservative")
    require(report.get("status") == "PASS", "report status is not PASS")
    print("wasm bundle evidence report: PASS (byte budget only; no runtime/trend SLO claim)")
    return 0


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--js", type=Path, help="production JavaScript bundle")
    parser.add_argument("--wasm", type=Path, help="optimized Kotlin Wasm bundle")
    parser.add_argument("--verify-report", type=Path, help="validate an existing evidence report")
    parser.add_argument("--baseline", type=Path, default=DEFAULT_BASELINE)
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT)
    parser.add_argument("--history", type=Path, default=DEFAULT_HISTORY)
    parser.add_argument("--commit", default=os.environ.get("GITHUB_SHA", "local"))
    args = parser.parse_args()

    if args.verify_report is not None:
        report_path = args.verify_report if args.verify_report.is_absolute() else ROOT / args.verify_report
        return verify_report(report_path)
    require(args.js is not None and args.wasm is not None, "--js and --wasm are required when creating evidence")

    js = args.js if args.js.is_absolute() else ROOT / args.js
    wasm = args.wasm if args.wasm.is_absolute() else ROOT / args.wasm
    baseline_path = args.baseline if args.baseline.is_absolute() else ROOT / args.baseline
    output = args.output if args.output.is_absolute() else ROOT / args.output
    history_path = args.history if args.history.is_absolute() else ROOT / args.history
    require(js.is_file() and js.stat().st_size > 0, f"missing/empty JS bundle: {js}")
    require(wasm.is_file() and wasm.stat().st_size > 0, f"missing/empty Wasm bundle: {wasm}")

    baseline = load_object(baseline_path)
    require(baseline.get("schema") == "stylish-ui.wasm-bundle-baseline.v1", "unexpected baseline schema")
    budgets = baseline.get("budgets")
    require(isinstance(budgets, dict), "baseline.budgets must be an object")
    js_budget = budgets.get("jsBytes")
    wasm_budget = budgets.get("wasmBytes")
    require(isinstance(js_budget, int) and js_budget > 0, "baseline.budgets.jsBytes must be positive")
    require(isinstance(wasm_budget, int) and wasm_budget > 0, "baseline.budgets.wasmBytes must be positive")
    baseline_sizes = baseline.get("baseline")
    require(isinstance(baseline_sizes, dict), "baseline.baseline must be an object")
    baseline_js = baseline_sizes.get("jsBytes")
    baseline_wasm = baseline_sizes.get("wasmBytes")
    require(isinstance(baseline_js, int) and baseline_js > 0, "baseline.baseline.jsBytes must be positive")
    require(isinstance(baseline_wasm, int) and baseline_wasm > 0, "baseline.baseline.wasmBytes must be positive")

    current_js = js.stat().st_size
    current_wasm = wasm.stat().st_size
    now = datetime.now(timezone.utc).isoformat().replace("+00:00", "Z")
    record = {
        "revision": str(args.commit),
        "source": "production-webpack-artifact",
        "recordedAt": now,
        "jsBytes": current_js,
        "wasmBytes": current_wasm,
        "jsSha256": sha256(js),
        "wasmSha256": sha256(wasm),
    }
    result: dict[str, Any] = {
        "schema": "stylish-ui.wasm-bundle-evidence.v1",
        "scope": "Wasm production bundle byte budget",
        "not_a_runtime_slo": True,
        "status": "PASS" if current_js <= js_budget and current_wasm <= wasm_budget else "FAIL",
        "environment": {
            "os": platform.system(),
            "arch": platform.machine(),
            "revision": str(args.commit),
        },
        "budgets": {"jsBytes": js_budget, "wasmBytes": wasm_budget},
        "baseline": {
            "revision": baseline.get("revision", "committed-baseline"),
            "jsBytes": baseline_js,
            "wasmBytes": baseline_wasm,
        },
        "current": record,
        "deltaFromBaseline": {
            "jsBytes": current_js - baseline_js,
            "wasmBytes": current_wasm - baseline_wasm,
        },
        "history": {
            "kind": "committed-baseline-plus-current",
            "measuredHostedRuns": 0 if str(args.commit) == "local" else 1,
            "trendClaimAllowed": False,
            "note": "A retained artifact from a prior hosted run is required for a multi-run trend claim.",
        },
    }

    # Keep a bounded per-run history artifact. If a workflow downloads a prior
    # artifact into this path, it is preserved; a clean checkout contains only
    # the committed baseline and the current run, which remains explicitly
    # baseline-relative rather than a fabricated historical series.
    prior: list[dict[str, Any]] = []
    if history_path.is_file():
        try:
            old = json.loads(history_path.read_text(encoding="utf-8"))
            if isinstance(old, dict) and isinstance(old.get("records"), list):
                prior = [item for item in old["records"] if isinstance(item, dict)][-49:]
        except (OSError, json.JSONDecodeError):
            prior = []
    history = {
        "schema": "stylish-ui.wasm-bundle-history.v1",
        "scope": "Wasm production bundle byte budget",
        "not_a_runtime_slo": True,
        "records": prior + [record],
        "trendClaimAllowed": len(prior) >= 1,
    }
    result["history"]["measuredHostedRuns"] = sum(
        1 for item in history["records"] if item.get("source") == "production-webpack-artifact" and item.get("revision") != "local"
    )
    json_write(output, result)
    json_write(history_path, history)
    if result["status"] != "PASS":
        fail(
            f"bundle budget exceeded: JS={current_js}/{js_budget} bytes, "
            f"Wasm={current_wasm}/{wasm_budget} bytes"
        )
    print(
        "wasm bundle evidence: PASS "
        f"(JS {current_js} bytes, Wasm {current_wasm} bytes; "
        f"baseline deltas {current_js - baseline_js}/{current_wasm - baseline_wasm}; "
        "byte-budget only, no runtime SLO claim)"
    )
    return 0


if __name__ == "__main__":
    main()

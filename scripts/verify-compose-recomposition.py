#!/usr/bin/env python3
"""Validate the Linux JVM Compose recomposition harness artifact.

The validator intentionally rejects interpretation as a frame/device SLO. A missing
artifact is a skip for local static audits, while a malformed or over-budget artifact
fails. Hosted CI should run it after ``jvmTest`` with report generation enabled.
"""

from __future__ import annotations

import argparse
import json
import math
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_REPORT = ROOT / "build/reports/performance/compose-recomposition.json"


def fail(message: str) -> "NoReturn":
    print(f"compose recomposition report: FAIL: {message}", file=sys.stderr)
    raise SystemExit(1)


def require(condition: bool, message: str) -> None:
    if not condition:
        fail(message)


def number(value: object, field: str) -> float:
    require(isinstance(value, (int, float)) and not isinstance(value, bool), f"{field} must be numeric")
    result = float(value)
    require(math.isfinite(result) and result >= 0, f"{field} must be finite and non-negative")
    return result


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--report", type=Path, default=DEFAULT_REPORT)
    parser.add_argument("--require", action="store_true")
    args = parser.parse_args()
    report_path = args.report if args.report.is_absolute() else ROOT / args.report
    if not report_path.is_file():
        if args.require:
            display = report_path.relative_to(ROOT) if report_path.is_relative_to(ROOT) else report_path
            fail(f"missing {display}")
        print("compose recomposition report: SKIP (no JVM harness artifact)")
        return 0
    try:
        report = json.loads(report_path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        fail(f"invalid JSON: {error}")
    require(isinstance(report, dict), "root must be an object")
    require(report.get("schema") == "stylish-ui.compose-recomposition.v1", "unexpected schema")
    require(report.get("scope") == "Linux JVM Compose UI test recomposition harness", "unexpected scope")
    require(report.get("not_a_frame_or_device_slo") is True, "scope guard missing")
    protocol = report.get("protocol")
    require(isinstance(protocol, dict), "protocol must be an object")
    iterations = protocol.get("updateIterations")
    require(isinstance(iterations, int) and iterations >= 3, "updateIterations must be >= 3")
    require(protocol.get("countMetric") == "successfulSideEffect", "successful composition metric missing")
    updates = report.get("updateSuccessfulCompositions")
    require(isinstance(updates, int) and updates == iterations, "update composition count mismatch")
    samples = report.get("updateMillis")
    require(isinstance(samples, list) and len(samples) == iterations, "update sample count mismatch")
    values = [number(value, "updateMillis") for value in samples]
    sorted_values = sorted(values)
    index = min(len(values) - 1, max(0, (len(values) * 95 + 99) // 100 - 1))
    p95 = number(report.get("updateP95Millis"), "updateP95Millis")
    require(abs(p95 - sorted_values[index]) <= 0.002, "update p95 summary is inconsistent")
    budget = number(report.get("recompositionBudget"), "recompositionBudget")
    require(updates <= budget, "recomposition budget exceeded")
    require(report.get("status") == "PASS", "report status is not PASS")
    print("compose recomposition report: PASS (JVM harness; count/update proxy only, no device/frame SLO)")
    return 0


if __name__ == "__main__":
    main()

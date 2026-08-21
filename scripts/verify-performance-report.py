#!/usr/bin/env python3
"""Validate the machine-readable Linux algorithmic performance report.

The report is deliberately a *bounded algorithmic* signal.  This verifier refuses to
interpret it as a frame-time, heap, startup, or device SLO.  A report is accepted only
when the measurement protocol and every required workload are present, so a missing
workload cannot silently turn into a green release artifact.
"""

from __future__ import annotations

import argparse
import json
import math
import sys
from pathlib import Path


EXPECTED_WORKLOADS = {
    "dataTable-10k-sort": 5_000,
    "tree-100k-flatten": 5_000,
    "chart-100k-downsample": 2_000,
}


def fail(message: str) -> "NoReturn":
    print(f"performance report: FAIL: {message}", file=sys.stderr)
    raise SystemExit(1)


def require(condition: bool, message: str) -> None:
    if not condition:
        fail(message)


def finite_number(value: object, field: str) -> float:
    require(isinstance(value, (int, float)) and not isinstance(value, bool), f"{field} must be numeric")
    result = float(value)
    require(math.isfinite(result), f"{field} must be finite")
    return result


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--report",
        default="build/reports/performance/algorithmic-budgets.json",
        help="path to the generated report",
    )
    args = parser.parse_args()
    report_path = Path(args.report)
    require(report_path.is_file(), f"missing report: {report_path}")
    try:
        report = json.loads(report_path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        fail(f"invalid JSON: {exc}")

    require(isinstance(report, dict), "root must be an object")
    require(report.get("schemaVersion") == 1, "schemaVersion must be 1")
    require(report.get("scope") == "Linux JVM deterministic algorithmic smoke", "unexpected scope")
    require(report.get("not_a_frame_or_heap_slo") is True, "scope guard is missing")

    protocol = report.get("protocol")
    require(isinstance(protocol, dict), "protocol must be an object")
    warmups = protocol.get("warmupIterations")
    iterations = protocol.get("measurementIterations")
    require(isinstance(warmups, int) and warmups >= 1, "warmupIterations must be >= 1")
    require(isinstance(iterations, int) and iterations >= 3, "measurementIterations must be >= 3")
    require(protocol.get("statistic") == "p95Millis", "p95 statistic is required")
    require(protocol.get("budgetRule") == "p95Millis <= budgetMillis", "budget rule is not explicit")

    environment = report.get("environment")
    require(isinstance(environment, dict), "environment must be an object")
    for field in ("javaVersion", "os", "arch", "revision"):
        require(isinstance(environment.get(field), str) and environment[field], f"environment.{field} is missing")

    measurements = report.get("measurements")
    require(isinstance(measurements, list), "measurements must be an array")
    by_name: dict[str, dict[str, object]] = {}
    for index, measurement in enumerate(measurements):
        require(isinstance(measurement, dict), f"measurement {index} must be an object")
        name = measurement.get("name")
        require(isinstance(name, str) and name, f"measurement {index} has no name")
        require(name not in by_name, f"duplicate workload: {name}")
        by_name[name] = measurement
        require(measurement.get("unit") == "ms", f"{name} unit must be ms")
        samples = measurement.get("samplesMillis")
        require(isinstance(samples, list), f"{name}.samplesMillis must be an array")
        require(len(samples) == iterations, f"{name} sample count must equal measurementIterations")
        sample_values = [finite_number(value, f"{name}.samplesMillis") for value in samples]
        require(all(value >= 0 for value in sample_values), f"{name} samples cannot be negative")
        budget = finite_number(measurement.get("budgetMillis"), f"{name}.budgetMillis")
        require(budget == EXPECTED_WORKLOADS.get(name), f"unexpected budget for {name}")
        minimum = finite_number(measurement.get("minMillis"), f"{name}.minMillis")
        median = finite_number(measurement.get("medianMillis"), f"{name}.medianMillis")
        p95 = finite_number(measurement.get("p95Millis"), f"{name}.p95Millis")
        require(abs(minimum - min(sample_values)) <= 0.002, f"{name} min summary is inconsistent")
        sorted_values = sorted(sample_values)
        expected_median = sorted_values[(len(sorted_values) - 1) // 2]
        require(abs(median - expected_median) <= 0.002, f"{name} median summary is inconsistent")
        p95_index = min(len(sorted_values) - 1, max(0, (len(sorted_values) * 95 + 99) // 100 - 1))
        require(abs(p95 - sorted_values[p95_index]) <= 0.002, f"{name} p95 summary is inconsistent")
        require(p95 <= budget, f"{name} p95 {p95}ms exceeds {budget}ms")
        require(measurement.get("status") == "PASS", f"{name} status is not PASS")

    require(set(by_name) == set(EXPECTED_WORKLOADS), "required workload set is incomplete or has extras")
    print(
        "performance report: PASS "
        f"({len(by_name)} workloads, p95 protocol, budget-only; not a device/frame/heap SLO)"
    )
    return 0


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""Turn Compose compiler metrics into a bounded, reviewable stability report.

This is a compiler-stability proxy, not a device frame-time or heap benchmark.
"""

from __future__ import annotations

import argparse
import json
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_METRICS = ROOT / "build/compose-metrics/jvm/main/io_github_segnities007:Stylish-UI-module.json"


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--metrics", default=str(DEFAULT_METRICS))
    parser.add_argument("--output", default="build/reports/performance/compose-metrics.json")
    args = parser.parse_args()
    metrics_path = Path(args.metrics)
    if not metrics_path.is_file():
        print(f"Compose metrics: SKIP (missing {metrics_path})")
        return 0
    data = json.loads(metrics_path.read_text())
    total = int(data.get("totalComposables", 0))
    skippable = int(data.get("skippableComposables", 0))
    unknown = int(data.get("unknownStableArguments", 0))
    unstable = int(data.get("knownUnstableArguments", 0))
    total_classes = int(data.get("totalClasses", 0))
    stable_classes = int(data.get("effectivelyStableClasses", 0))
    ratio = skippable / total if total else 0.0
    stability_ratio = stable_classes / total_classes if total_classes else 0.0
    checks = {
        "strongSkipping": data.get("featureFlags", {}).get("StrongSkipping") is True,
        "skippableRatioAtLeast80Percent": ratio >= 0.80,
        # The headless Tree/Transfer/Chart renderer overloads intentionally
        # accept generic List/Set payloads; the compiler reports three extra
        # unknown arguments. Keep a bounded regression budget rather than
        # pretending those generic collection types are device-stable.
        "unknownStableArgumentsAtMost110": unknown <= 110,
        "knownUnstableArgumentsAtMost100": unstable <= 100,
        "effectivelyStableClassesAtLeast90Percent": stability_ratio >= 0.90,
    }
    report = {
        "schema": "stylish-ui.compose-metrics.v1",
        "scope": "jvm-main compiler stability proxy",
        "source": str(metrics_path.relative_to(ROOT) if metrics_path.is_absolute() else metrics_path),
        "metrics": data,
        "derived": {
            "skippableRatio": round(ratio, 6),
            "effectivelyStableClassRatio": round(stability_ratio, 6),
        },
        "checks": checks,
        "status": "PASS" if all(checks.values()) else "FAIL",
        "limitations": ["not a frame-time benchmark", "not a heap/memory benchmark", "not a device SLO"],
    }
    output = ROOT / args.output
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(report, indent=2, sort_keys=True) + "\n")
    if not all(checks.values()):
        print(f"Compose metrics: FAIL ({output})")
        return 1
    print(f"Compose metrics: PASS (skippable={ratio:.1%}, stableClasses={stability_ratio:.1%})")
    print(f"Compose metrics evidence: {output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

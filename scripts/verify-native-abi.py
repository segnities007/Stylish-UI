#!/usr/bin/env python3
"""Build and verify target-specific Kotlin/Native KLib ABI snapshots."""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import subprocess
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
TARGETS = ("iosArm64", "iosSimulatorArm64")
REPORT_DIR = ROOT / "build/reports/native-abi"


def find_klib_tool() -> Path:
    configured = os.environ.get("KLIB_BIN")
    if configured and Path(configured).is_file():
        return Path(configured)
    konan = Path.home() / ".konan"
    candidates = sorted(konan.glob("kotlin-native-prebuilt-*/bin/klib"))
    if not candidates:
        raise FileNotFoundError("Kotlin/Native klib tool not found under ~/.konan; set KLIB_BIN")
    return candidates[-1]


def sha256(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--build", action="store_true", help="build both KLib targets before dumping ABI")
    args = parser.parse_args()
    if args.build:
        subprocess.run(
            ["./gradlew", "iosArm64Klib", "iosSimulatorArm64Klib", "--no-daemon"],
            cwd=ROOT,
            check=True,
        )
    tool = find_klib_tool()
    REPORT_DIR.mkdir(parents=True, exist_ok=True)
    entries = []
    for target in TARGETS:
        candidates = sorted((ROOT / "build/libs").glob(f"Stylish-UI-{target}Main-*.klib"))
        if not candidates:
            print(f"Native ABI: FAIL (missing {target} KLib)")
            return 1
        klib = candidates[-1]
        snapshot = REPORT_DIR / f"{target}.abi"
        with snapshot.open("w") as output:
            subprocess.run([str(tool), "dump-abi", str(klib)], stdout=output, check=True)
        text = snapshot.read_text()
        declaration_lines = sum(1 for line in text.splitlines() if line and not line.startswith("//"))
        if declaration_lines < 10:
            print(f"Native ABI: FAIL ({target} snapshot is unexpectedly small)")
            return 1
        entries.append(
            {
                "target": target,
                "klib": str(klib.relative_to(ROOT)),
                "snapshot": str(snapshot.relative_to(ROOT)),
                "klibSha256": sha256(klib),
                "snapshotSha256": sha256(snapshot),
                "declarationLines": declaration_lines,
            }
        )
    manifest = {
        "schema": "stylish-ui.native-abi.v1",
        "tool": str(tool),
        "targets": entries,
        "status": "PASS",
        "limitations": ["KLib ABI snapshot only", "not iOS simulator runtime", "not VoiceOver evidence"],
    }
    (REPORT_DIR / "manifest.json").write_text(json.dumps(manifest, indent=2, sort_keys=True) + "\n")
    print(f"Native ABI: PASS ({len(entries)} target snapshots)")
    print(f"Native ABI evidence: {REPORT_DIR}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

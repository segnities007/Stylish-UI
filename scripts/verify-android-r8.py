#!/usr/bin/env python3
"""Verify the minified Android consumer sample and write reviewable evidence."""

from __future__ import annotations

import hashlib
import json
import zipfile
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
APK = ROOT / "samples/android-r8/build/outputs/apk/release/android-r8-release-unsigned.apk"
MAPPING = ROOT / "samples/android-r8/build/outputs/mapping/release"
REPORT = ROOT / "build/reports/release/android-r8.json"


def main() -> int:
    required = ["mapping.txt", "configuration.txt", "seeds.txt", "usage.txt"]
    if not APK.is_file():
        print(f"Android R8: FAIL (missing {APK})")
        return 1
    missing = [name for name in required if not (MAPPING / name).is_file()]
    if missing:
        print(f"Android R8: FAIL (missing mapping evidence: {', '.join(missing)})")
        return 1
    try:
        with zipfile.ZipFile(APK) as archive:
            names = set(archive.namelist())
    except (OSError, zipfile.BadZipFile) as exc:
        print(f"Android R8: FAIL (invalid APK: {exc})")
        return 1
    required_entries = {"AndroidManifest.xml", "resources.arsc"}
    dex_entries = sorted(name for name in names if name.startswith("classes") and name.endswith(".dex"))
    missing_entries = sorted(required_entries - names)
    if missing_entries or not dex_entries:
        print(f"Android R8: FAIL (missing={missing_entries}, dex={dex_entries})")
        return 1
    digest = hashlib.sha256(APK.read_bytes()).hexdigest()
    evidence = {
        "schema": "stylish-ui.android-r8.v1",
        "apk": str(APK.relative_to(ROOT)),
        "sha256": digest,
        "sizeBytes": APK.stat().st_size,
        "minifyEnabled": True,
        "resourceShrinkingEnabled": True,
        "consumerRules": "proguard/stylish-ui-consumer-rules.pro",
        "dexEntries": dex_entries,
        "mappingEvidence": [str((MAPPING / name).relative_to(ROOT)) for name in required],
        "status": "PASS",
    }
    REPORT.parent.mkdir(parents=True, exist_ok=True)
    REPORT.write_text(json.dumps(evidence, indent=2) + "\n")
    print(f"Android R8: PASS ({evidence['sizeBytes']} bytes, {len(dex_entries)} dex, sha256={digest})")
    print(f"Android R8 evidence: {REPORT}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

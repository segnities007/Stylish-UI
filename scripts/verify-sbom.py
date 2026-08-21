#!/usr/bin/env python3
"""Validate the generated CycloneDX SBOM without trusting its producer.

License classification is informational: ``allowed`` covers permissive
licenses, ``review`` flags copyleft families that must never ship unreviewed,
and ``missing`` marks artifacts whose POM chain exposes no license.  The
release gate fails on structural errors and on ``--require-clean`` with any
``review`` hits; ``missing`` is reported but does not block, because the
Kotlin/Compose dependency graph contains metadata that is not always
available through the resolved POM chain.
"""

from __future__ import annotations

import argparse
import json
import re
from pathlib import Path


ALLOWED_MARKERS = ("apache", "mit", "bsd", "isc", "cc0", "public domain", "bouncy castle")
REVIEW_MARKERS = ("lgpl", "gpl", "mpl", "eclipse public", "cddl", "epl")


def license_status(component: dict) -> str:
    licenses = component.get("licenses")
    if not isinstance(licenses, list) or not licenses:
        return "missing"
    names = []
    for entry in licenses:
        license_data = entry.get("license") if isinstance(entry, dict) else None
        if not isinstance(license_data, dict):
            return "missing"
        name = str(license_data.get("id") or license_data.get("name") or "").strip().lower()
        if not name:
            return "missing"
        names.append(name)
    joined = " ".join(names)
    if any(marker in joined for marker in REVIEW_MARKERS):
        return "review"
    if not any(marker in joined for marker in ALLOWED_MARKERS):
        return "review"
    return "allowed"


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("path", nargs="?", default="build/reports/release/sbom.json")
    parser.add_argument("--require-clean", action="store_true", help="fail on missing or review licenses")
    args = parser.parse_args()
    path = Path(args.path)
    if not path.is_file():
        print(f"SBOM missing: {path}")
        return 1
    try:
        bom = json.loads(path.read_text())
    except (OSError, json.JSONDecodeError) as exc:
        print(f"SBOM unreadable: {exc}")
        return 1
    errors: list[str] = []
    if bom.get("bomFormat") != "CycloneDX" or bom.get("specVersion") != "1.5":
        errors.append("bomFormat/specVersion must be CycloneDX 1.5")
    components = bom.get("components")
    if not isinstance(components, list) or not components:
        errors.append("components must be a non-empty array")
        components = []
    purls: set[str] = set()
    statuses = {"allowed": 0, "review": 0, "missing": 0}
    for index, component in enumerate(components):
        if not isinstance(component, dict):
            errors.append(f"component[{index}] is not an object")
            continue
        purl = component.get("purl")
        if not isinstance(purl, str) or not re.fullmatch(r"pkg:maven/[^/]+/[^@]+@[^@]+", purl):
            errors.append(f"component[{index}] has invalid Maven purl")
        elif purl in purls:
            errors.append(f"duplicate purl: {purl}")
        else:
            purls.add(purl)
        if component.get("type") != "library":
            errors.append(f"component[{index}] type must be library")
        hashes = component.get("hashes")
        properties = component.get("properties") or []
        metadata_only = any(
            isinstance(item, dict)
            and item.get("name") == "stylish:artifact"
            and item.get("value") == "metadata-only"
            for item in properties
        )
        if not metadata_only and (not isinstance(hashes, list) or not any(
            isinstance(item, dict)
            and item.get("alg") == "SHA-256"
            and isinstance(item.get("content"), str)
            and re.fullmatch(r"[0-9a-f]{64}", item["content"])
            for item in hashes or []
        )):
            errors.append(f"component[{index}] missing SHA-256 hash")
        status = license_status(component)
        statuses[status] += 1
    if errors:
        print("SBOM: FAIL")
        print("\n".join(f"- {error}" for error in errors[:20]))
        return 1
    if args.require_clean and statuses["review"]:
        print(
            "SBOM: FAIL (copyleft license requires legal review: "
            f"allowed={statuses['allowed']} review={statuses['review']} missing={statuses['missing']})"
        )
        return 1
    print(
        "SBOM: PASS (CycloneDX 1.5, "
        f"components={len(components)}, allowed={statuses['allowed']}, "
        f"review={statuses['review']}, missing={statuses['missing']})"
    )
    if statuses["review"] or statuses["missing"]:
        print("SBOM license status: REVIEW_REQUIRED (release must not claim verified until reviewed)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

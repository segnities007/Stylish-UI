#!/usr/bin/env python3
"""Fail-closed structural validation for the public support/lifecycle policy.

This proves that the policy is explicit and machine-readable. It does not pretend that
response-time targets, on-call ownership, or rollback drills have happened in this repo.
"""

from __future__ import annotations

import json
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
POLICY = ROOT / "docs/support-policy.json"


def fail(message: str) -> None:
    print(f"SUPPORT POLICY VIOLATION: {message}", file=sys.stderr)
    raise SystemExit(1)


def main() -> int:
    if not POLICY.is_file():
        fail(f"missing {POLICY.relative_to(ROOT)}")
    try:
        data = json.loads(POLICY.read_text(encoding="utf-8"))
    except json.JSONDecodeError as error:
        fail(f"invalid JSON: {error}")

    if data.get("schemaVersion") != 1:
        fail("schemaVersion must be 1")
    if data.get("policyStatus") != "defined-not-operationally-proven":
        fail("policyStatus must distinguish policy from operational evidence")
    if data.get("artifact") != "io.github.segnities007:stylish-ui":
        fail("artifact coordinates are incorrect")
    required_platforms = {"android", "jvm-desktop", "wasm-web", "ios"}
    if set(data.get("platforms", [])) != required_platforms:
        fail(f"platforms must be exactly {sorted(required_platforms)}")

    window = data.get("supportWindow", {})
    if window.get("stable") != "latest-and-previous-minor":
        fail("stable support window must cover latest and previous minor")
    if window.get("security") != "latest-and-previous-minor":
        fail("security support window must cover latest and previous minor")
    if not isinstance(window.get("eolNoticeBusinessDays"), int) or window["eolNoticeBusinessDays"] < 30:
        fail("EOL notice must be at least 30 business days")

    severity = data.get("severity", {})
    expected_days = {"S0": 1, "S1": 2, "S2": 5, "S3": 10}
    for level, days in expected_days.items():
        entry = severity.get(level)
        if not isinstance(entry, dict) or entry.get("firstHumanResponseBusinessDays") != days:
            fail(f"{level} response target must be {days} business day(s)")
        if not entry.get("releaseAction"):
            fail(f"{level} must define a release action")

    deprecation = data.get("deprecation", {})
    if deprecation.get("minimumMinorReleasesBeforeRemoval") < 2:
        fail("deprecation must provide at least two minor releases")
    for key in ("requiresMigrationGuide", "requiresApiDumpAndApiCheck", "preOneMajorExceptionMustBeDocumented"):
        if deprecation.get(key) is not True:
            fail(f"deprecation.{key} must be true")

    incident = data.get("incident", {})
    for field in ("impact", "firstBadVersion", "platform", "reproduction", "workaround", "owner"):
        if field not in incident.get("requiredFields", []):
            fail(f"incident required field missing: {field}")
    for field in ("rootCause", "detectionGap", "regressionTest", "preventionGate"):
        if field not in incident.get("requiredFollowUp", []):
            fail(f"incident follow-up missing: {field}")
    if incident.get("secretsPolicy") != "never-store-credentials-or-user-data":
        fail("incident secrets policy must prohibit credentials and user data")

    evidence = data.get("evidence", {})
    for artifact in ("checksum", "sbom", "license-review", "api-abi", "platform-matrix"):
        if artifact not in evidence.get("requiredReleaseInputs", []):
            fail(f"release evidence input missing: {artifact}")
    if evidence.get("operationalEvidenceStatus") != "not-yet-observed-in-this-repository":
        fail("operational evidence status must not overclaim")

    print("Support policy: PASS (machine-readable policy present; operational targets remain unproven)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

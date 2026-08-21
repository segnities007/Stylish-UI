#!/usr/bin/env python3
"""Validate the retained packaged Wasm UI workflow artifact."""

from __future__ import annotations

import argparse
import json
from datetime import datetime
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_ARTIFACT = ROOT / "website-wasm/build/ci-evidence/wasm-ui-e2e.json"
REQUIRED_STEPS = {"catalog-loaded", "category-filter", "search", "theme-toggle", "keyboard-focus"}


def fail(message: str) -> "NoReturn":
    raise SystemExit(f"wasm UI E2E artifact: FAIL: {message}")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--artifact", type=Path, default=DEFAULT_ARTIFACT)
    parser.add_argument("--require", action="store_true", help="fail when the per-run artifact is absent")
    args = parser.parse_args()
    artifact = args.artifact if args.artifact.is_absolute() else ROOT / args.artifact
    if not artifact.is_file():
        if args.require:
            fail(f"missing {artifact.relative_to(ROOT)}")
        print("wasm UI E2E artifact: SKIP (no per-run artifact present)")
        return 0

    try:
        data = json.loads(artifact.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        fail(str(error))
    if data.get("schema") != "stylish-ui.wasm-ui-e2e.v1":
        fail("unexpected schema")
    if not str(data.get("baseUrl", "")).startswith("http://127.0.0.1:"):
        fail("artifact must describe a local deterministic test URL")
    raw_steps = data.get("steps")
    if not isinstance(raw_steps, list) or not raw_steps:
        fail("steps must be a non-empty list")
    if any(
        not isinstance(step, dict)
        or not isinstance(step.get("name"), str)
        or not step.get("name")
        or not isinstance(step.get("assertion"), str)
        or not step.get("assertion")
        for step in raw_steps
    ):
        fail("every step must contain a non-empty name and assertion")
    steps = {step["name"] for step in raw_steps}
    if steps != REQUIRED_STEPS:
        fail(f"workflow steps mismatch: {sorted(steps)}")
    console_errors = data.get("consoleErrors")
    if not isinstance(console_errors, list):
        fail("consoleErrors must be a list")
    if console_errors:
        fail("consoleErrors is not empty")
    viewport = data.get("viewport") or {}
    if viewport.get("width") != 1440 or viewport.get("height") != 1000:
        fail(f"unexpected viewport: {viewport}")
    if viewport.get("deviceScaleFactor") != 1:
        fail(f"unexpected deviceScaleFactor: {viewport.get('deviceScaleFactor')}")
    if viewport.get("localeOverride") != "ja-JP":
        fail("locale override is not recorded")
    completed_at = data.get("completedAt")
    if not isinstance(completed_at, str) or not completed_at:
        fail("completedAt is missing")
    try:
        datetime.fromisoformat(completed_at.replace("Z", "+00:00"))
    except ValueError:
        fail("completedAt is not ISO-8601")
    screenshot = artifact.with_suffix(".png")
    if not screenshot.is_file() or screenshot.stat().st_size == 0:
        fail(f"missing screenshot beside artifact: {screenshot.name}")
    print(f"wasm UI E2E artifact: PASS ({artifact.relative_to(ROOT)})")
    return 0


if __name__ == "__main__":
    main()

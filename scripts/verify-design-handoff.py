#!/usr/bin/env python3
"""Validate the deterministic designer handoff package and its Figma interop contract.

This is deliberately an offline verifier. It proves that the repository's semantic JSON can be
exported into a reviewable, typed interchange package and that repeated exports are byte-stable.
It does not pretend to prove a real Figma file was imported or approved.
"""

from __future__ import annotations

import hashlib
import json
import subprocess
import sys
import tempfile
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "docs/tokens/stylish-ui.tokens.json"
EXPORTER = ROOT / "scripts/export-design-tokens.py"
FIGMA_SCHEMA = "stylish-ui.figma-variable-handoff.v1"
EXPECTED_MODES = {
    "light": "Light",
    "dark": "Dark",
    "highContrastLight": "High Contrast Light",
    "highContrastDark": "High Contrast Dark",
}
EXPECTED_ARTIFACTS = {
    "manifest.json",
    "stylish-ui.tokens.handoff.json",
    "stylish-ui.tokens.css",
    "stylish-ui.tokens.figma.variables.json",
}


def fail(message: str) -> None:
    raise SystemExit(f"Design handoff: FAIL ({message})")


def load(path: Path) -> dict[str, Any]:
    try:
        value = json.loads(path.read_text())
    except (OSError, json.JSONDecodeError) as error:
        fail(f"invalid JSON {path}: {error}")
    if not isinstance(value, dict):
        fail(f"JSON document must be an object: {path}")
    return value


def sha256(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def run_export(output: Path) -> None:
    result = subprocess.run(
        [sys.executable, str(EXPORTER), "--output-dir", str(output.relative_to(ROOT))],
        cwd=ROOT,
        check=False,
        capture_output=True,
        text=True,
    )
    if result.returncode:
        fail(result.stderr.strip() or result.stdout.strip() or "exporter failed")


def validate(output: Path) -> tuple[str, ...]:
    if set(path.name for path in output.iterdir()) != EXPECTED_ARTIFACTS:
        fail(f"artifact set changed: {[path.name for path in output.iterdir()]}")

    source_sha = sha256(SOURCE)
    manifest = load(output / "manifest.json")
    handoff = load(output / "stylish-ui.tokens.handoff.json")
    figma = load(output / "stylish-ui.tokens.figma.variables.json")

    if manifest.get("sourceSha256") != source_sha:
        fail("manifest source hash does not match token source")
    if handoff.get("sourceSha256") != source_sha or figma.get("sourceSha256") != source_sha:
        fail("handoff source hash does not match token source")
    if manifest.get("status") != "PASS":
        fail("manifest status is not PASS")
    if manifest.get("figmaInterop") != {
        "schema": FIGMA_SCHEMA,
        "nativeExport": False,
        "adapterRequired": True,
    }:
        fail("manifest does not declare the explicit Figma interchange boundary")
    if set(manifest.get("artifacts", [])) != EXPECTED_ARTIFACTS - {"manifest.json"}:
        fail("manifest artifact list is incomplete")

    source = load(SOURCE)
    source_modes = source.get("modes")
    if source_modes != list(EXPECTED_MODES):
        fail(f"source modes changed without updating the adoption contract: {source_modes}")
    if handoff.get("modes") != source_modes:
        fail("normalized handoff modes differ from source")
    source_tokens = handoff.get("tokens")
    if not isinstance(source_tokens, list) or len(source_tokens) < 20:
        fail("normalized handoff has too few tokens")
    if manifest.get("tokenCount") != len(source_tokens):
        fail("manifest tokenCount differs from normalized handoff")

    paths = [token.get("path") for token in source_tokens]
    names = [token.get("name") for token in source_tokens]
    if any(not isinstance(path, str) or not path for path in paths):
        fail("token paths must be non-empty strings")
    if len(paths) != len(set(paths)) or len(names) != len(set(names)):
        fail("token paths or CSS names are duplicated")
    if any(not isinstance(name, str) or not name.startswith("--stylish-") for name in names):
        fail("normalized token names must be stable --stylish-* properties")

    collection = figma.get("collection")
    if figma.get("schema") != FIGMA_SCHEMA or figma.get("format") != "interchange":
        fail("Figma artifact schema/format is not explicit")
    if figma.get("nativeFigmaExport") is not False:
        fail("Figma artifact must not claim to be a native Figma export")
    if not isinstance(collection, dict) or collection.get("id") != "stylish-ui":
        fail("Figma collection identity is missing")
    modes = collection.get("modes")
    if modes != [{"id": key.lower().replace(" ", "-"), "name": value} for key, value in EXPECTED_MODES.items()]:
        fail("Figma mode IDs/names are not stable")
    variables = collection.get("variables")
    if not isinstance(variables, list) or len(variables) != len(source_tokens):
        fail("Figma variable count does not match normalized handoff")
    variable_paths = {variable.get("path") for variable in variables}
    if variable_paths != set(paths):
        fail("Figma variable paths do not match normalized handoff")
    mode_ids = {mode["id"] for mode in modes}
    for variable in variables:
        if variable.get("id") != "stylish." + variable.get("path", ""):
            fail(f"unstable Figma variable ID: {variable.get('path')}")
        if set(variable.get("valuesByMode", {})) != mode_ids:
            fail(f"mode coverage missing for {variable.get('path')}")
        if variable.get("resolvedType") not in {"COLOR", "FLOAT", "STRING"}:
            fail(f"unsupported Figma resolved type: {variable.get('resolvedType')}")
        for value in variable["valuesByMode"].values():
            if value.get("kind") == "alias":
                if not value.get("external") or not value.get("target"):
                    fail(f"alias must identify an explicit external foundation: {variable.get('path')}")
            elif value.get("kind") == "literal":
                if "value" not in value:
                    fail(f"literal value missing: {variable.get('path')}")
            else:
                fail(f"unknown Figma value kind: {variable.get('path')}")

    aliases = sorted(
        value["target"]
        for variable in variables
        for value in variable["valuesByMode"].values()
        if value.get("kind") == "alias"
    )
    if figma.get("externalAliases") != sorted(set(aliases)):
        fail("external alias inventory is not canonical")

    css = (output / "stylish-ui.tokens.css").read_text()
    for name in names:
        if f"  {name}:" not in css:
            fail(f"CSS handoff is missing {name}")

    return tuple(sorted(path.name for path in output.iterdir()))


def main() -> int:
    with tempfile.TemporaryDirectory(prefix="stylish-ui-handoff-") as first_dir, tempfile.TemporaryDirectory(
        prefix="stylish-ui-handoff-"
    ) as second_dir:
        first = Path(first_dir)
        second = Path(second_dir)
        # The exporter resolves paths relative to ROOT; use stable in-tree temporary paths for it.
        first_rel = ROOT / "build/reports/.handoff-verification-1"
        second_rel = ROOT / "build/reports/.handoff-verification-2"
        try:
            first_rel.mkdir(parents=True, exist_ok=True)
            second_rel.mkdir(parents=True, exist_ok=True)
            run_export(first_rel)
            run_export(second_rel)
            validate(first_rel)
            validate(second_rel)
            for name in EXPECTED_ARTIFACTS:
                if (first_rel / name).read_bytes() != (second_rel / name).read_bytes():
                    fail(f"export is not deterministic: {name}")
        finally:
            for path in (first_rel, second_rel):
                if path.exists():
                    for child in path.iterdir():
                        child.unlink()
                    path.rmdir()
        print(f"Design handoff: PASS ({len(load(ROOT / 'docs/tokens/stylish-ui.tokens.json'))} top-level groups; deterministic Figma interop)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

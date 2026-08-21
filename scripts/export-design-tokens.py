#!/usr/bin/env python3
"""Export the semantic token contract into deterministic designer handoff artifacts."""

from __future__ import annotations

import argparse
import hashlib
import json
import re
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "docs/tokens/stylish-ui.tokens.json"

# The JSON token contract is intentionally portable, while this additional artifact is an
# explicit interchange format for a Figma/Token Studio adapter. It is not a native Figma API
# export (native exports contain file-specific IDs); stable IDs here make diffs reviewable.
FIGMA_SCHEMA = "stylish-ui.figma-variable-handoff.v1"
FIGMA_MODE_NAMES = {
    "light": "Light",
    "dark": "Dark",
    "highContrastLight": "High Contrast Light",
    "highContrastDark": "High Contrast Dark",
}


def flatten(value: Any, prefix: tuple[str, ...] = ()) -> list[tuple[str, dict[str, Any]]]:
    if isinstance(value, dict) and "value" in value and "type" in value:
        return [(".".join(prefix), value)]
    result: list[tuple[str, dict[str, Any]]] = []
    if isinstance(value, dict):
        for key, child in value.items():
            result.extend(flatten(child, prefix + (key,)))
    return result


def css_name(path: str) -> str:
    return "--stylish-" + re.sub(r"[^a-z0-9]+", "-", path.lower()).strip("-")


def css_value(token: str, token_type: str) -> str:
    reference = re.fullmatch(r"\{([^}]+)\}", token)
    if reference:
        return "var(--" + re.sub(r"[^a-z0-9]+", "-", reference.group(1).lower()).strip("-") + ")"
    if token == "instant":
        return "0ms"
    if token_type == "dimension" and token.endswith("dp"):
        # CSS has no dp unit; the Web adapter's baseline maps 1dp to 1px and
        # may scale the custom property at the host boundary.
        return token[:-2] + "px"
    return token


def slug(value: str) -> str:
    return re.sub(r"[^a-z0-9]+", "-", value.lower()).strip("-")


def figma_mode_id(mode: str) -> str:
    """Return the stable mode key used by the handoff, independent of Figma file IDs."""
    return slug(mode)


def figma_resolved_type(token_type: str) -> str:
    if token_type == "color":
        return "COLOR"
    if token_type in {"dimension", "duration"}:
        return "FLOAT"
    return "STRING"


def figma_literal(token: str, token_type: str) -> dict[str, Any]:
    """Convert a DTCG literal into a typed, unit-preserving interchange value."""
    if token_type == "dimension" and token.endswith("dp"):
        return {"kind": "literal", "unit": "dp", "value": float(token[:-2])}
    if token_type == "duration" and token.endswith("ms"):
        return {"kind": "literal", "unit": "ms", "value": float(token[:-2])}
    return {"kind": "literal", "value": token}


def figma_value(token: str, token_type: str) -> dict[str, Any]:
    reference = re.fullmatch(r"\{([^}]+)\}", token)
    if reference:
        return {
            "kind": "alias",
            "target": reference.group(1),
            # The target is resolved by the host's Material/Figma foundation collection.
            "external": True,
        }
    return figma_literal(token, token_type)


def figma_handoff(document: dict[str, Any], tokens: list[tuple[str, dict[str, Any]]], source_sha: str) -> dict[str, Any]:
    modes = document.get("modes", [])
    mode_records = [
        {"id": figma_mode_id(str(mode)), "name": FIGMA_MODE_NAMES.get(str(mode), str(mode))}
        for mode in modes
    ]
    variables = []
    external_aliases: set[str] = set()
    for path, data in tokens:
        token_type = str(data["type"])
        value = figma_value(str(data["value"]), token_type)
        if value["kind"] == "alias":
            external_aliases.add(str(value["target"]))
        variables.append(
            {
                "id": "stylish." + path,
                "name": path.replace(".", "/"),
                "path": path,
                "resolvedType": figma_resolved_type(token_type),
                "description": f"Stylish UI semantic token: {path}",
                "valuesByMode": {
                    mode["id"]: value for mode in mode_records
                },
            }
        )
    return {
        "schema": FIGMA_SCHEMA,
        "format": "interchange",
        "nativeFigmaExport": False,
        "source": "docs/tokens/stylish-ui.tokens.json",
        "sourceSha256": source_sha,
        "collection": {
            "id": "stylish-ui",
            "name": "Stylish UI",
            "modes": mode_records,
            "variables": variables,
        },
        "externalAliases": sorted(external_aliases),
        "notes": [
            "Stable IDs are intended for an adapter and reviewable diffs; Figma file IDs are assigned on import.",
            "All mode values are semantic aliases or literals from the JSON contract; platform palette resolution remains host-owned.",
        ],
    }


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output-dir", default="build/reports/tokens")
    args = parser.parse_args()
    source_bytes = SOURCE.read_bytes()
    document = json.loads(source_bytes)
    tokens = flatten(document)
    if len(tokens) < 20:
        raise SystemExit(f"token export unexpectedly small: {len(tokens)}")
    output = ROOT / args.output_dir
    output.mkdir(parents=True, exist_ok=True)
    source_sha = hashlib.sha256(source_bytes).hexdigest()
    normalized = {
        "schema": "stylish-ui.token-handoff.v1",
        "source": "docs/tokens/stylish-ui.tokens.json",
        "sourceSha256": source_sha,
        "modes": document.get("modes", []),
        "tokens": [
            {"path": path, "name": css_name(path), "type": data["type"], "value": data["value"]}
            for path, data in tokens
        ],
    }
    (output / "stylish-ui.tokens.handoff.json").write_text(json.dumps(normalized, indent=2, sort_keys=True) + "\n")
    (output / "stylish-ui.tokens.figma.variables.json").write_text(
        json.dumps(figma_handoff(document, tokens, source_sha), indent=2, sort_keys=True) + "\n"
    )
    lines = [
        "/* Generated from docs/tokens/stylish-ui.tokens.json; do not edit. */",
        f"/* source-sha256: {source_sha} */",
        ":root {",
    ]
    for path, data in tokens:
        lines.append(f"  {css_name(path)}: {css_value(str(data['value']), str(data['type']))}; /* {data['type']} {path} */")
    lines.append("}")
    (output / "stylish-ui.tokens.css").write_text("\n".join(lines) + "\n")
    manifest = {
        "schema": "stylish-ui.token-handoff-manifest.v1",
        "source": "docs/tokens/stylish-ui.tokens.json",
        "sourceSha256": source_sha,
        "tokenCount": len(tokens),
        "modes": document.get("modes", []),
        "artifacts": [
            "stylish-ui.tokens.handoff.json",
            "stylish-ui.tokens.css",
            "stylish-ui.tokens.figma.variables.json",
        ],
        "figmaInterop": {
            "schema": FIGMA_SCHEMA,
            "nativeExport": False,
            "adapterRequired": True,
        },
        "status": "PASS",
    }
    (output / "manifest.json").write_text(json.dumps(manifest, indent=2, sort_keys=True) + "\n")
    print(f"Design token handoff: PASS ({len(tokens)} tokens, sha256={source_sha})")
    print(f"Design token artifacts: {output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

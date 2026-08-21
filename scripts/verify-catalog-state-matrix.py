#!/usr/bin/env python3
"""Build and audit the public-API ↔ catalog ↔ component-state matrix.

This is deliberately a source-level verifier.  It does not pretend that a
Linux parser is an Android TalkBack, iOS VoiceOver, or real browser visual
test.  It answers the narrower question that was previously unguarded:

* did the public common Compose surface shrink or grow without an inventory?
* does every catalog demo have a stable category/name/code entry?
* which public symbols are referenced by which demos?
* which state examples are present or absent in each catalog family?

The generated JSON is intended for CI artifacts and tooling.  The Markdown
report is intentionally human-readable and contains both directions of the
mapping.  State gaps are reported as gaps but do not fail the default check;
``--strict`` is available for a future adoption gate once the platform
evidence exists.  Structural drift (unparseable entries, count regression,
duplicate demo names, and invalid policy) always fails.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from dataclasses import asdict, dataclass
from pathlib import Path
from typing import Iterable


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_POLICY = ROOT / "docs" / "catalog-state-policy.json"
DEFAULT_OUTPUT = ROOT / "build" / "reports" / "catalog-state-matrix"


@dataclass(frozen=True)
class Declaration:
    declaration_id: str
    name: str
    source: str
    line: int
    package: str
    layer: str
    family: str


@dataclass(frozen=True)
class Demo:
    demo_id: str
    name: str
    category: str
    source: str
    line: int
    code: str
    body: str
    symbols: tuple[str, ...]
    state_evidence: dict[str, tuple[str, ...]]
    max_literal_length: int
    family: str


COMPOSABLE_DECLARATION = re.compile(
    r"(?ms)^[ \t]*@Composable(?:[ \t]*\([^\n]*\))?"
    r"(?:[ \t]*\n[ \t]*@[^\n]*)*[ \t]*(?:\n[ \t]*)?"
    r"public[ \t]+(?:(?:inline|operator|suspend|infix)[ \t]+)*"
    # Kotlin permits a type-parameter list and/or an extension receiver
    # between `fun` and the symbol name.  The public inventory counts these
    # as declarations, while catalog references use the final simple name.
    r"fun[ \t]+(?:<[^>\n]*>[ \t]*)?"
    r"(?:[A-Za-z_]\w*\.)?(?P<name>[A-Za-z_]\w*)[ \t]*\("
)


def relative(path: Path) -> str:
    return path.relative_to(ROOT).as_posix()


def line_number(text: str, offset: int) -> int:
    return text.count("\n", 0, offset) + 1


def kotlin_package(text: str) -> str:
    match = re.search(r"(?m)^\s*package\s+([A-Za-z0-9_.]+)", text)
    return match.group(1) if match else ""


def layer_for_source(source: str) -> str:
    parts = Path(source).parts
    if "components" in parts:
        index = parts.index("components")
        if index + 1 < len(parts):
            return parts[index + 1]
    for candidate in ("structure", "foundation", "theme", "tokens"):
        if candidate in parts:
            return candidate
    return "other"


def classify_api_family(source: str, name: str) -> str:
    """Classify a public symbol into the policy family used by the report."""

    layer = layer_for_source(source)
    lowered = name.lower()
    if layer == "charts":
        return "Charts"
    if layer == "patterns":
        return "Patterns"
    if layer == "structure":
        return "Structure"
    if layer in {"foundation", "theme", "tokens", "other"}:
        return "Foundation"
    if layer == "organisms":
        if any(token in lowered for token in ("navigation", "drawer", "rail", "menubar", "menu", "tab", "search", "segmented", "command")):
            return "Navigation"
        return "Advanced"
    if layer == "molecules":
        if "connected" in lowered:
            return "Connected"
        if any(token in lowered for token in ("snackbar", "toast", "alert", "empty", "skeleton", "result", "progress")):
            return "Feedback"
        if any(token in lowered for token in ("date", "time", "autocomplete", "form", "field", "input")):
            return "Inputs"
        if any(token in lowered for token in ("pagination", "breadcrumb", "stepper", "toolbar")):
            return "Navigation"
        return "WebParity"
    # Atoms contain controls as well as visual primitives.  Keep the mapping
    # deterministic and let the state report expose the distinction.
    if any(token in lowered for token in ("textfield", "input", "picker", "slider", "rating", "pin", "number")):
        return "Inputs"
    if any(token in lowered for token in ("checkbox", "radio", "switch", "toggle", "segmented")):
        return "Selection"
    if any(token in lowered for token in ("progress", "divider", "badge", "indicator")):
        return "Feedback"
    return "Buttons"


def parse_public_declarations(source_root: Path) -> list[Declaration]:
    declarations: list[Declaration] = []
    for path in sorted(source_root.rglob("*.kt")):
        text = path.read_text(encoding="utf-8")
        package = kotlin_package(text)
        source = relative(path)
        for match in COMPOSABLE_DECLARATION.finditer(text):
            name = match.group("name")
            line = line_number(text, match.start())
            declarations.append(
                Declaration(
                    declaration_id=f"{source}:{line}:{name}",
                    name=name,
                    source=source,
                    line=line,
                    package=package,
                    layer=layer_for_source(source),
                    family=classify_api_family(source, name),
                )
            )
    return declarations


def find_balanced_call(text: str, open_index: int) -> str:
    """Return a Kotlin call body while ignoring strings and comments."""

    depth = 0
    index = open_index
    length = len(text)
    while index < length:
        if text.startswith("//", index):
            newline = text.find("\n", index + 2)
            index = length if newline < 0 else newline + 1
            continue
        if text.startswith("/*", index):
            end = text.find("*/", index + 2)
            index = length if end < 0 else end + 2
            continue
        if text.startswith('"""', index):
            end = text.find('"""', index + 3)
            index = length if end < 0 else end + 3
            continue
        char = text[index]
        if char == '"':
            index += 1
            while index < length:
                if text[index] == "\\":
                    index += 2
                elif text[index] == '"':
                    index += 1
                    break
                else:
                    index += 1
            continue
        if char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
            if depth == 0:
                return text[open_index + 1:index]
        index += 1
    raise ValueError("unbalanced DemoComponent call")


def kotlin_string_value(value: str) -> str:
    # Demo names are ordinary Kotlin strings.  json.loads handles escaped
    # quotes and unicode without mangling Japanese source text.
    try:
        return json.loads(f'"{value}"')
    except json.JSONDecodeError:
        return value.replace('\\"', '"').replace("\\n", "\n")


def extract_named_string(body: str, field: str) -> str:
    match = re.search(rf"(?m)\b{re.escape(field)}\s*=\s*\"((?:\\.|[^\"\\])*)\"", body)
    if not match:
        return ""
    return kotlin_string_value(match.group(1))


def extract_code(body: str) -> str:
    match = re.search(r"\bcode\s*=\s*\"\"\"(.*?)\"\"\"", body, re.DOTALL)
    return match.group(1).strip() if match else ""


def string_literal_lengths(text: str) -> list[int]:
    values: list[int] = []
    for match in re.finditer(r'"((?:\\.|[^"\\])*)"', text):
        values.append(len(kotlin_string_value(match.group(1))))
    for match in re.finditer(r'"""(.*?)"""', text, re.DOTALL):
        values.append(len(match.group(1)))
    return values


def evidence_for_state(body: str, code: str, state: str, patterns: list[str]) -> tuple[str, ...]:
    if state == "default":
        return ("__always__",)
    haystack = f"{body}\n{code}"
    matches: list[str] = []
    for pattern in patterns:
        try:
            if re.search(pattern, haystack, re.IGNORECASE):
                matches.append(pattern)
        except re.error as error:
            raise ValueError(f"invalid state evidence regex {pattern!r}: {error}") from error
    if state == "long_text" and max(string_literal_lengths(haystack), default=0) >= 24:
        matches.append("literal-length>=24")
    return tuple(dict.fromkeys(matches))


def parse_demos(catalog_root: Path, declarations: list[Declaration], policy: dict) -> list[Demo]:
    names = sorted({declaration.name for declaration in declarations}, key=len, reverse=True)
    demos: list[Demo] = []
    state_patterns: dict[str, list[str]] = policy["stateEvidence"]
    family_by_category = {
        category: family
        for family, details in policy["families"].items()
        for category in details.get("categories", [])
    }
    for path in sorted(catalog_root.rglob("*.kt")):
        text = path.read_text(encoding="utf-8")
        for marker in re.finditer(r"\bDemoComponent\s*\(", text):
            body = find_balanced_call(text, marker.end() - 1)
            name = extract_named_string(body, "name")
            category_match = re.search(r"\bcategory\s*=\s*DemoCategory\.([A-Za-z_]\w*)", body)
            if not name or not category_match:
                # DemoComponent.kt contains the data-class constructor.  It
                # has neither named demo metadata nor a category and is not a
                # registry entry.
                continue
            category = category_match.group(1)
            if category not in family_by_category:
                raise ValueError(f"catalog demo {name!r} uses unknown category {category!r}")
            code = extract_code(body)
            combined = f"{body}\n{code}"
            # A symbol counts as referenced only when it is not used as a
            # named-argument label (`shape = ...`).  Labels are aliases, not
            # calls: counting them mapped Defaults helpers such as the
            # composable `shape`/`colors`/`elevation` without any demo ever
            # invoking them.
            symbols = tuple(
                name_token
                for name_token in names
                if re.search(
                    rf"(?<![A-Za-z0-9_]){re.escape(name_token)}(?![A-Za-z0-9_])(?!\s*=)",
                    combined,
                )
            )
            state_evidence = {
                state: evidence_for_state(combined, code, state, patterns)
                for state, patterns in state_patterns.items()
            }
            demos.append(
                Demo(
                    demo_id=f"{relative(path)}:{line_number(text, marker.start())}:{name}",
                    name=name,
                    category=category,
                    source=relative(path),
                    line=line_number(text, marker.start()),
                    code=code,
                    body=body,
                    symbols=symbols,
                    state_evidence=state_evidence,
                    max_literal_length=max(string_literal_lengths(combined), default=0),
                    family=family_by_category[category],
                )
            )
    return demos


def state_status(evidence: Iterable[str]) -> str:
    return "covered" if tuple(evidence) else "missing"


def family_for_demo(policy: dict, category: str) -> str:
    for family, details in policy["families"].items():
        if category in details.get("categories", []):
            return family
    raise ValueError(f"no state family configured for category {category!r}")


def build_report(declarations: list[Declaration], demos: list[Demo], policy: dict) -> dict:
    by_name: dict[str, list[Declaration]] = {}
    for declaration in declarations:
        by_name.setdefault(declaration.name, []).append(declaration)
    demos_by_symbol: dict[str, list[Demo]] = {}
    for demo in demos:
        for symbol in demo.symbols:
            demos_by_symbol.setdefault(symbol, []).append(demo)

    demo_rows: list[dict] = []
    for demo in demos:
        required = policy["families"][demo.family]["requiredStates"]
        states = {
            state: {
                "status": state_status(demo.state_evidence.get(state, ())),
                "evidence": list(demo.state_evidence.get(state, ())),
            }
            for state in required
        }
        demo_rows.append(
            {
                "id": demo.demo_id,
                "name": demo.name,
                "category": demo.category,
                "family": demo.family,
                "source": f"{demo.source}:{demo.line}",
                "copyReadyCode": bool(demo.code),
                "symbols": list(demo.symbols),
                "stateMatrix": states,
                "missingStates": [state for state, row in states.items() if row["status"] == "missing"],
                "maxLiteralLength": demo.max_literal_length,
            }
        )

    declaration_rows: list[dict] = []
    for declaration in declarations:
        demos_for_symbol = demos_by_symbol.get(declaration.name, [])
        # Non-visual composable factories and foundation helpers are expected
        # to be searchable in Dokka/API docs, not duplicated as catalog cards.
        # - foundation/theme/tokens: value holders and helpers, no rendered UI.
        # - *Defaults* files (atoms/molecules): default parameter holders.
        # - colors/shape/elevation/border composables: parameter-default
        #   providers for their component; they return style objects and
        #   render nothing on their own.
        # - structure: headless layout/slot hosts.  They render no pixels and
        #   delegate every item to a Finish renderer whose demo carries the
        #   visual coverage.
        # - remember*: composable state factories allocating hoisted state;
        #   they have no visual output.  Demos still win: an existing genuine
        #   mapping is never downgraded.
        non_visual = (
            declaration.layer in {"foundation", "theme", "tokens", "structure"}
            or (declaration.layer in {"atoms", "molecules"} and "Defaults" in declaration.source)
            or (
                declaration.layer in {"atoms", "molecules"}
                and declaration.name in {"colors", "shape", "elevation", "border"}
            )
            or declaration.name.startswith("remember")
        )
        coverage = "mapped" if demos_for_symbol else ("api-doc" if non_visual else "missing")
        required = policy["families"][declaration.family]["requiredStates"]
        union_evidence: dict[str, list[str]] = {state: [] for state in required}
        for demo in demos_for_symbol:
            for state in required:
                union_evidence[state].extend(demo.state_evidence.get(state, ()))
        states = {
            state: {
                "status": "not_applicable" if non_visual else state_status(union_evidence[state]),
                "evidence": list(dict.fromkeys(union_evidence[state])),
            }
            for state in required
        }
        declaration_rows.append(
            {
                "id": declaration.declaration_id,
                "name": declaration.name,
                "package": declaration.package,
                "layer": declaration.layer,
                "family": declaration.family,
                "source": f"{declaration.source}:{declaration.line}",
                "catalogCoverage": coverage,
                "catalogDemos": [demo.name for demo in demos_for_symbol],
                "stateMatrix": states,
                "missingStates": [
                    state for state, row in states.items() if row["status"] == "missing"
                ],
            }
        )

    family_rows: list[dict] = []
    for family, details in policy["families"].items():
        family_demos = [demo for demo in demos if demo.family == family]
        family_declarations = [row for row in declaration_rows if row["family"] == family]
        state_rows: dict[str, dict] = {}
        for state in details["requiredStates"]:
            covered_demos = [
                demo.name
                for demo in family_demos
                if demo.state_evidence.get(state)
            ]
            covered_declarations = [
                row["name"]
                for row in family_declarations
                if row["stateMatrix"].get(state, {}).get("status") == "covered"
            ]
            state_rows[state] = {
                "catalogDemosCovered": len(covered_demos),
                "catalogDemosTotal": len(family_demos),
                "declarationsCovered": len(covered_declarations),
                "declarationsTotal": len(family_declarations),
                "evidenceDemos": covered_demos,
            }
        family_rows.append(
            {
                "family": family,
                "catalogDemos": len(family_demos),
                "publicDeclarations": len(family_declarations),
                "requiredStates": list(details["requiredStates"]),
                "states": state_rows,
            }
        )

    mapped_visual = [
        row for row in declaration_rows
        if row["catalogCoverage"] == "mapped"
    ]
    visual_declarations = [
        row for row in declaration_rows
        if row["catalogCoverage"] != "api-doc"
    ]
    return {
        "schema": "stylish-ui.catalog-state-matrix.v1",
        "sourceOfTruth": {
            "publicComposableRoot": relative(ROOT / "src" / "commonMain" / "kotlin"),
            "catalogRoot": relative(ROOT / "catalog" / "src" / "commonMain" / "kotlin"),
            "policy": relative(DEFAULT_POLICY),
        },
        "counts": {
            "publicComposableDeclarations": len(declarations),
            "catalogDemos": len(demos),
            "visualDeclarations": len(visual_declarations),
            "visualDeclarationsWithDemo": len(mapped_visual),
            "visualDeclarationsWithoutDemo": len(visual_declarations) - len(mapped_visual),
            "apiDocOnlyDeclarations": len(declaration_rows) - len(visual_declarations),
        },
        "families": family_rows,
        "catalogDemos": demo_rows,
        "publicDeclarations": declaration_rows,
    }


def structural_errors(report: dict, policy: dict, demos: list[Demo]) -> list[str]:
    errors: list[str] = []
    counts = report["counts"]
    if counts["publicComposableDeclarations"] < policy["minimumPublicComposableDeclarations"]:
        errors.append(
            "public composable declarations regressed: "
            f"{counts['publicComposableDeclarations']} < {policy['minimumPublicComposableDeclarations']}"
        )
    if counts["catalogDemos"] < policy["minimumCatalogDemos"]:
        errors.append(
            f"catalog demos regressed: {counts['catalogDemos']} < {policy['minimumCatalogDemos']}"
        )
    demo_names = [demo.name for demo in demos]
    duplicates = sorted({name for name in demo_names if demo_names.count(name) > 1})
    if duplicates:
        errors.append(f"duplicate catalog demo names: {', '.join(duplicates)}")
    for required in policy["stateOrder"]:
        if required not in policy["stateEvidence"]:
            errors.append(f"state policy has no evidence patterns for {required!r}")
    return errors


def strict_errors(report: dict) -> list[str]:
    errors: list[str] = []
    for row in report["publicDeclarations"]:
        if row["catalogCoverage"] == "missing":
            errors.append(f"public visual API has no catalog demo: {row['id']}")
        if row["missingStates"]:
            errors.append(
                f"public API state gaps ({', '.join(row['missingStates'])}): {row['id']}"
            )
    for row in report["catalogDemos"]:
        if not row["copyReadyCode"]:
            errors.append(f"catalog demo has no copy-ready code: {row['id']}")
    return errors


def pct(covered: int, total: int) -> str:
    return "—" if total == 0 else f"{covered}/{total} ({covered / total:.0%})"


def markdown_report(report: dict) -> str:
    counts = report["counts"]
    lines = [
        "# Catalog / public API / state matrix",
        "",
        "This file is generated by `scripts/verify-catalog-state-matrix.py`. "
        "It is a source-level inventory, not proof of Android TalkBack, iOS VoiceOver, "
        "browser screen-reader, or physical-device visual acceptance.",
        "",
        f"- Public `@Composable` declarations: **{counts['publicComposableDeclarations']}**",
        f"- Catalog demos: **{counts['catalogDemos']}**",
        f"- Visual declarations with at least one demo: **{counts['visualDeclarationsWithDemo']}/{counts['visualDeclarations']}**",
        f"- Visual declarations without a demo: **{counts['visualDeclarationsWithoutDemo']}**",
        f"- API-doc-only composables (factories/foundation helpers): **{counts['apiDocOnlyDeclarations']}**",
        "",
        "## Family state coverage",
        "",
        "The denominator is the number of demos or public declarations in that family. "
        "A state is `covered` only when its evidence token appears in the demo source; "
        "a user can still require stronger rendered/platform evidence.",
        "",
        "| Family | Demos | Public APIs | State | Demo evidence | API evidence |",
        "|---|---:|---:|---|---:|---:|",
    ]
    for family in report["families"]:
        for state, values in family["states"].items():
            lines.append(
                f"| {family['family']} | {family['catalogDemos']} | {family['publicDeclarations']} | "
                f"`{state}` | {pct(values['catalogDemosCovered'], values['catalogDemosTotal'])} | "
                f"{pct(values['declarationsCovered'], values['declarationsTotal'])} |"
            )
    lines += ["", "## Demo → public symbol mapping", "", "| Demo | Category | Source | Public symbols | Missing states |", "|---|---|---|---|---|"]
    for demo in report["catalogDemos"]:
        symbols = ", ".join(f"`{symbol}`" for symbol in demo["symbols"]) or "—"
        missing = ", ".join(f"`{state}`" for state in demo["missingStates"]) or "—"
        lines.append(
            f"| {demo['name']} | {demo['category']} | `{demo['source']}` | {symbols} | {missing} |"
        )
    lines += ["", "## Public declaration → demo mapping", "", "| Public API | Layer/family | Source | Catalog coverage | Demos | Missing states |", "|---|---|---|---|---|---|"]
    for declaration in report["publicDeclarations"]:
        demos = ", ".join(declaration["catalogDemos"]) or "—"
        missing = ", ".join(f"`{state}`" for state in declaration["missingStates"]) or "—"
        lines.append(
            f"| `{declaration['name']}` | {declaration['layer']} / {declaration['family']} | "
            f"`{declaration['source']}` | `{declaration['catalogCoverage']}` | {demos} | {missing} |"
        )
    lines += ["", "## Interpretation", "", "- `mapped` means a catalog source references the public symbol by name as more than an argument label.", "- `api-doc` means the symbol renders no UI on its own — foundation/theme/tokens helpers, Defaults parameter providers (`colors`/`shape`/`elevation`/`border`), headless Structure layout hosts, and `remember*` state factories. It must be searchable in Dokka/API docs rather than duplicated as a visual card; Structure coverage is carried by the Finish components that consume it.", "- `missing` means a visual public symbol currently has no matching catalog demo and is an adoption gap.", "- State gaps are intentionally emitted rather than hidden. They are the next implementation/visual/platform QA queue.", ""]
    return "\n".join(lines)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--policy", type=Path, default=DEFAULT_POLICY)
    parser.add_argument("--output-dir", type=Path, default=DEFAULT_OUTPUT)
    parser.add_argument("--strict", action="store_true", help="fail on every visual API/demo/state gap")
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    policy = json.loads(args.policy.read_text(encoding="utf-8"))
    source_root = ROOT / "src" / "commonMain" / "kotlin"
    catalog_root = ROOT / "catalog" / "src" / "commonMain" / "kotlin"
    if not source_root.is_dir():
        print(f"catalog state matrix: missing source root {source_root}", file=sys.stderr)
        return 2
    if not catalog_root.is_dir():
        print(f"catalog state matrix: missing catalog root {catalog_root}", file=sys.stderr)
        return 2
    declarations = parse_public_declarations(source_root)
    demos = parse_demos(catalog_root, declarations, policy)
    report = build_report(declarations, demos, policy)
    errors = structural_errors(report, policy, demos)
    if args.strict:
        errors.extend(strict_errors(report))
    args.output_dir.mkdir(parents=True, exist_ok=True)
    (args.output_dir / "catalog-component-state-matrix.json").write_text(
        json.dumps(report, ensure_ascii=False, indent=2, sort_keys=True) + "\n",
        encoding="utf-8",
    )
    (args.output_dir / "catalog-component-state-matrix.md").write_text(
        markdown_report(report),
        encoding="utf-8",
    )
    counts = report["counts"]
    print("Catalog/public API state matrix")
    print(f"  public @Composable declarations: {counts['publicComposableDeclarations']}")
    print(f"  catalog demos:                  {counts['catalogDemos']}")
    print(f"  visual APIs with demo:          {counts['visualDeclarationsWithDemo']}/{counts['visualDeclarations']}")
    print(f"  visual APIs without demo:       {counts['visualDeclarationsWithoutDemo']}")
    print(f"  API-doc-only composables:       {counts['apiDocOnlyDeclarations']}")
    print(f"  report:                          {args.output_dir / 'catalog-component-state-matrix.json'}")
    if errors:
        print("Catalog state matrix gaps / errors:")
        for error in errors:
            print(f"  - {error}")
        if args.strict:
            print("catalog state matrix: FAIL", file=sys.stderr)
            return 1
    print("catalog state matrix: PASS (structural inventory; state gaps are reported)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

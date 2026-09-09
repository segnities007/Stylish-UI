#!/usr/bin/env python3
"""Check Kotlin consumer sources against a checked-in design-harness import contract.

This is a source architecture check, not a security sandbox or Kotlin symbol resolver.
Run the Kotlin compiler and interaction tests as the accompanying acceptance gates.
"""
import argparse
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DEFAULT = ROOT / "catalog/src/commonMain/kotlin/com/segnities007/stylishui/catalog/harness"
PACKAGE = "com.segnities007.stylishui"
STRICT_SYMBOLS = {
    PACKAGE + ".components.patterns.StylishScreen",
    PACKAGE + ".components.models.StylishScreenDocument",
    PACKAGE + ".components.models.StylishScreenElement",
    PACKAGE + ".components.models.StylishScreenEvent",
    PACKAGE + ".components.models.StylishContentState",
    PACKAGE + ".components.models.StylishTextRole",
    PACKAGE + ".theme.StylishTheme",
}
IMPORT = re.compile(r"^\s*import\s+([\w.*]+)(?:\s+as\s+(\w+))?", re.M)
MATERIAL = re.compile(r"\b(?:androidx|org\.jetbrains)\.compose\.material(?:3)?\b")
REFERENCE = re.compile(r"\b(?:androidx|org\.jetbrains|com)\.[\w.]+")
SCREEN = PACKAGE + ".components.patterns.StylishScreen"


def code_only(source):
    """Strip comments and literal contents, retaining line breaks and template expressions."""
    # Nested Kotlin block comments are handled separately from strings.
    result, i, depth = [], 0, 0
    while i < len(source):
        if depth:
            if source.startswith("/*", i):
                depth += 1
                i += 2
            elif source.startswith("*/", i):
                depth -= 1
                i += 2
            else:
                result.append("\n" if source[i] == "\n" else " ")
                i += 1
        elif source.startswith("/*", i):
            depth = 1
            i += 2
        elif source.startswith("//", i):
            end = source.find("\n", i)
            i = len(source) if end < 0 else end
        elif source[i] in ('"', "'"):
            delimiter = '"""' if source.startswith('"""', i) else source[i]
            i += len(delimiter)
            while i < len(source) and not source.startswith(delimiter, i):
                if source.startswith("${", i):
                    # Preserve template bodies for qualified reference checks.
                    start, balance = i + 2, 1
                    i = start
                    while i < len(source) and balance:
                        balance += (source[i] == "{") - (source[i] == "}")
                        i += 1
                    result.append(code_only(source[start:i - 1]))
                elif source[i] == "\\" and len(delimiter) == 1:
                    i += 2
                else:
                    result.append("\n" if source[i] == "\n" else " ")
                    i += 1
            i += len(delimiter)
        else:
            result.append(source[i])
            i += 1
    return "".join(result)


def allowed(symbol, strict):
    if symbol.startswith(("kotlin.", "kotlinx.coroutines.", "androidx.compose.runtime.")):
        return True
    if strict:
        return any(symbol == name or symbol.startswith(name + ".") for name in STRICT_SYMBOLS)
    return symbol.startswith((
        PACKAGE + ".", "androidx.compose.foundation.", "androidx.compose.ui.",
        "org.jetbrains.compose.resources.",
    ))


def audit(source, strict=True):
    code = code_only(source)
    failures = []
    imports = list(IMPORT.finditer(code))
    for match in imports:
        symbol = match.group(1)
        if "*" in symbol or not allowed(symbol, strict):
            failures.append("non-allowlisted import: " + symbol)
    body = IMPORT.sub("", code)
    body = re.sub(r"^\s*package\s+[\w.]+", "", body, flags=re.M)
    for match in REFERENCE.finditer(body):
        if not allowed(match.group(), strict):
            failures.append("non-allowlisted qualified reference: " + match.group())
    if MATERIAL.search(code):
        failures.append("Material UI reference: use Stylish UI")
    names = {"StylishScreen", SCREEN}
    names.update(m.group(2) for m in imports if m.group(1) == SCREEN and m.group(2))
    renders_screen = any(re.search(r"(?<![\w.])" + re.escape(name) + r"\s*\(", body) for name in names)
    return failures, renders_screen


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("roots", nargs="*", type=Path)
    parser.add_argument("--profile", choices=("screen", "material-free"), default="screen")
    args = parser.parse_args()
    roots = args.roots or [DEFAULT]
    missing = [str(root) for root in roots if not root.exists()]
    if missing:
        parser.error("missing source roots: " + ", ".join(missing))
    files = sorted({p for root in roots for p in (
        [root] if root.is_file() else root.rglob("*.kt")
    ) if p.suffix == ".kt"})
    failures, screens = [], 0
    for path in files:
        issues, renders = audit(path.read_text(encoding="utf-8"), args.profile == "screen")
        failures.extend(str(path) + ": " + issue for issue in issues)
        screens += renders
    if not files:
        failures.append("No Kotlin consumer files found")
    if args.profile == "screen" and not screens:
        failures.append("No StylishScreen invocation: a constrained consumer must render a screen")
    for failure in failures:
        print(failure, file=sys.stderr)
    print(f"design harness {args.profile}: {len(files)} Kotlin files, {screens} screen files")
    return 1 if failures else 0


if __name__ == "__main__":
    raise SystemExit(main())

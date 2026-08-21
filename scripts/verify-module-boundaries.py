#!/usr/bin/env python3
"""Verify the physical Gradle module graph and virtual source-layer boundaries.

The library intentionally keeps the styled component layers in one multiplatform
module for now while the headless Foundation/Structure slices are physically
extracted.  This check complements ``verify-architecture.sh`` with a small,
deterministic audit that runs on Linux without Gradle, Kotlin, or a network:

- the settings project set and an allowlisted Gradle project dependency graph;
- required canary edges (a consumer cannot silently drop its extraction dep);
- a layered direction matrix (edges must point strictly downward);
- a duplicate-package guard: modules that own the same FQCN package must never
  be co-consumed on one classpath (Android D8 duplicate-class hazard);
- package/path agreement for every Kotlin file plus per-module import rules.
"""

from __future__ import annotations

import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
LIBRARY_PACKAGE = "com.segnities007.stylishui"

# Physical source roots per logical module owner. Shared by the package-path
# audit and the duplicate-package (D8 duplicate-class) guard.
MODULE_SOURCE_ROOTS = {
    "library": ROOT / "src",
    "foundation": ROOT / "foundation/src",
    "structure": ROOT / "structure/src",
    "catalog": ROOT / "catalog/src",
    "website": ROOT / "website/src",
    "android-r8": ROOT / "samples/android-r8/src",
    "android-runtime": ROOT / "samples/android-runtime/src",
    "foundation-consumer": ROOT / "samples/foundation-consumer/src",
    "migration-consumer": ROOT / "samples/migration-consumer/src",
    "structure-consumer": ROOT / "samples/structure-consumer/src",
    "adapters": ROOT / "samples/adapters/src",
}

# Logical owner -> Gradle module path, so package ownership can be compared
# against declared Gradle edges.
OWNER_MODULE = {
    "library": ":",
    "foundation": ":foundation",
    "structure": ":structure",
    "catalog": ":catalog",
    "website": ":website",
    "android-r8": ":samples:android-r8",
    "android-runtime": ":samples:android-runtime",
    "foundation-consumer": ":samples:foundation-consumer",
    "migration-consumer": ":samples:migration-consumer",
    "structure-consumer": ":samples:structure-consumer",
    "adapters": ":samples:adapters",
}

# Permitted project edges per build file. Anything not listed here is a
# violation even if the layered direction below would allow it.
EDGE_RULES = {
    "foundation/build.gradle.kts": set(),
    "structure/build.gradle.kts": set(),
    "catalog/build.gradle.kts": {":"},
    "website/build.gradle.kts": {":catalog"},
    "samples/android-r8/build.gradle.kts": {":"},
    "samples/android-runtime/build.gradle.kts": {":"},
    "samples/foundation-consumer/build.gradle.kts": {":foundation"},
    "samples/migration-consumer/build.gradle.kts": {":foundation", ":structure"},
    "samples/structure-consumer/build.gradle.kts": {":structure"},
    "samples/adapters/build.gradle.kts": {":foundation"},
}

# Edges that MUST exist: every extraction keeps at least one compile-time
# canary, so a silently dropped dependency cannot hollow out the boundary.
REQUIRED_EDGES = {
    "catalog/build.gradle.kts": {":"},
    "website/build.gradle.kts": {":catalog"},
    "samples/android-r8/build.gradle.kts": {":"},
    "samples/android-runtime/build.gradle.kts": {":"},
    "samples/foundation-consumer/build.gradle.kts": {":foundation"},
    "samples/migration-consumer/build.gradle.kts": {":foundation", ":structure"},
    "samples/structure-consumer/build.gradle.kts": {":structure"},
}

# Layered direction policy (Finish -> Structure -> Foundation, consumers on top).
# An edge u -> v is legal only when rank(u) > rank(v); the published root is
# additionally special-cased to zero sibling edges while its binary-compatibility
# copies exist (see check_module_graph).
LAYER_RANK = {
    ":foundation": 0,
    ":structure": 1,
    ":": 2,
    ":catalog": 3,
    ":website": 4,
    ":samples:adapters": 5,
    ":samples:android-r8": 5,
    ":samples:android-runtime": 5,
    ":samples:foundation-consumer": 5,
    ":samples:migration-consumer": 5,
    ":samples:structure-consumer": 5,
}


def module_path(relative_build: str) -> str:
    """Return the Gradle module path implied by a build-file location."""

    parts = [part for part in relative_build.split("/") if part != "build.gradle.kts"]
    return ":" + ":".join(parts) if parts else ":"


def fail(message: str, failures: list[str]) -> None:
    failures.append(message)
    print(f"MODULE BOUNDARY VIOLATION: {message}", file=sys.stderr)


def read(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def project_edges(path: Path) -> list[str]:
    """Return Gradle project dependency paths declared by one build file.

    Matches both positional ``project(":x")`` and named-argument
    ``project(path = ":x")`` call shapes.
    """

    return re.findall(
        r"(?:api|implementation|compileOnly|runtimeOnly|testImplementation)"
        r"\s*\(\s*project\(\s*(?:path\s*=\s*)?\"([^\"]+)\"",
        read(path),
    )


def source_files(root: Path) -> list[Path]:
    return sorted(root.rglob("*.kt")) if root.is_dir() else []


def package_for(path: Path) -> str | None:
    match = re.search(r"^\s*package\s+([A-Za-z0-9_.]+)\s*$", read(path), re.MULTILINE)
    return match.group(1) if match else None


def package_path(path: Path) -> str | None:
    """Return the package implied by the path below a Kotlin source root."""

    parts = path.parts
    try:
        marker = parts.index("kotlin")
    except ValueError:
        return None
    relative = parts[marker + 1 : -1]
    return ".".join(relative) if relative else None


def code_lines(text: str) -> list[str]:
    """Remove comment-only lines before scanning cross-module references."""

    lines: list[str] = []
    in_block = False
    for raw in text.splitlines():
        line = raw
        if in_block:
            end = line.find("*/")
            if end < 0:
                continue
            line = line[end + 2 :]
            in_block = False
        while "/*" in line:
            start = line.find("/*")
            end = line.find("*/", start + 2)
            if end < 0:
                line = line[:start]
                in_block = True
                break
            line = line[:start] + line[end + 2 :]
        line = line.split("//", 1)[0]
        if line.strip():
            lines.append(line)
    return lines


def check_module_graph(failures: list[str]) -> int:
    settings = read(ROOT / "settings.gradle.kts")
    declared = set(re.findall(r'include\("([^\"]+)"\)', settings))
    expected = {
        ":foundation",
        ":structure",
        ":website",
        ":catalog",
        ":samples:android-r8",
        ":samples:android-runtime",
        ":samples:foundation-consumer",
        ":samples:migration-consumer",
        ":samples:structure-consumer",
        ":samples:adapters",
    }
    if declared != expected:
        fail(
            f"settings.gradle.kts project set changed: expected {sorted(expected)}, got {sorted(declared)}",
            failures,
        )

    edge_count = 0
    for relative, allowed in EDGE_RULES.items():
        path = ROOT / relative
        if not path.is_file():
            fail(f"missing module build file: {relative}", failures)
            continue
        edges = project_edges(path)
        edge_count += len(edges)
        unexpected = sorted(set(edges) - allowed)
        if unexpected:
            fail(f"{relative} has forbidden project dependencies: {unexpected}", failures)

    # Required canary edges must actually be present. An allowlist alone cannot
    # notice a consumer quietly dropping its dependency on the extracted module.
    for relative, required in REQUIRED_EDGES.items():
        path = ROOT / relative
        if not path.is_file():
            continue  # already reported above
        missing = sorted(required - set(project_edges(path)))
        if missing:
            fail(f"{relative} is missing required project dependencies: {missing}", failures)

    # Global layered direction: every declared edge must point strictly
    # downward in LAYER_RANK (upper layers consume lower layers). This catches
    # cycles and upward edges even when both endpoints happen to be allowlisted.
    for relative in EDGE_RULES:
        path = ROOT / relative
        if not path.is_file():
            continue
        source = module_path(relative)
        for target in project_edges(path):
            if source not in LAYER_RANK or target not in LAYER_RANK:
                fail(
                    f"{relative}: unknown layer rank for edge {source} -> {target}",
                    failures,
                )
                continue
            if LAYER_RANK[source] <= LAYER_RANK[target]:
                fail(
                    f"{relative}: dependency direction violation "
                    f"({source} rank {LAYER_RANK[source]} -> {target} rank {LAYER_RANK[target]})",
                    failures,
                )

    # The published root module intentionally has no runtime dependency on any
    # sibling while binary-compatibility copies remain in its artifact; embedding
    # both would create duplicate JVM/Android/Wasm classes. A future major
    # release can replace the copies with api dependencies.
    root_build = read(ROOT / "build.gradle.kts")
    root_edges = re.findall(r"\bproject\(\s*\"([^\"]+)\"\s*\)", root_build)
    unexpected_root_edges = sorted(set(root_edges))
    if unexpected_root_edges:
        fail(
            "root library build.gradle.kts has forbidden sibling project dependencies: "
            f"{unexpected_root_edges}",
            failures,
        )

    print(f"module graph: {len(declared) + 1} Gradle modules, {edge_count} allowed project edges")
    return edge_count


def check_source_boundaries(failures: list[str]) -> tuple[int, int]:
    files_checked = 0
    package_errors = 0
    for owner, source_root in MODULE_SOURCE_ROOTS.items():
        for path in source_files(source_root):
            files_checked += 1
            actual = package_for(path)
            implied = package_path(path)
            if actual is None:
                fail(f"{owner}: missing package declaration: {path.relative_to(ROOT)}", failures)
                package_errors += 1
            elif implied and actual != implied:
                fail(
                    f"{owner}: package/path mismatch ({actual} != {implied}): {path.relative_to(ROOT)}",
                    failures,
                )
                package_errors += 1

            lines = code_lines(read(path))
            if owner == "foundation":
                # Foundation is the lowest physical module. It may expose only
                # framework-neutral contracts and must not acquire a rendered or
                # host application dependency during the migration.
                forbidden = (
                    ".components",
                    ".structure",
                    ".theme",
                    ".tokens",
                    "android.",
                    "androidx.",
                    "UIKit",
                    "Foundation.",
                    "org.jetbrains.compose",
                )
                if any(line.lstrip().startswith(f"import {prefix}") for line in lines for prefix in forbidden):
                    fail(f"foundation module must remain framework-neutral: {path.relative_to(ROOT)}", failures)
            elif owner == "structure":
                # Structure owns arrangement and slots, not the Stylish visual
                # system. It may use Compose layout primitives, but it must
                # not couple to the styled published root, theme, or catalog.
                forbidden = (
                    f"{LIBRARY_PACKAGE}.components",
                    f"{LIBRARY_PACKAGE}.theme",
                    f"{LIBRARY_PACKAGE}.tokens",
                    f"{LIBRARY_PACKAGE}.catalog",
                    "android.",
                    "UIKit",
                    "Foundation.",
                    "org.jetbrains.compose.material",
                )
                if any(line.lstrip().startswith(f"import {prefix}") for line in lines for prefix in forbidden):
                    fail(f"structure module must remain headless and style-free: {path.relative_to(ROOT)}", failures)
            elif owner == "library":
                forbidden = (".catalog", ".website", ".androidruntime", ".r8sample")
                if any(f"{LIBRARY_PACKAGE}{suffix}" in line for line in lines for suffix in forbidden):
                    fail(f"published library source references a host/catalog package: {path.relative_to(ROOT)}", failures)
            elif owner == "catalog":
                forbidden = (".website", ".androidruntime", ".r8sample", ".catalog")
                if any(f"{LIBRARY_PACKAGE}{suffix}" in line for line in lines for suffix in forbidden[:-1]):
                    fail(f"catalog source references a host/sample package: {path.relative_to(ROOT)}", failures)
            elif owner == "foundation-consumer":
                if any("com.segnities007.stylishui.components." in line for line in lines):
                    fail(
                        f"foundation consumer must not depend on rendered component packages: "
                        f"{path.relative_to(ROOT)}",
                        failures,
                    )
            elif owner == "migration-consumer":
                # The migration canary must keep proving that BOTH extracted
                # artifacts are adoptable without the styled root publication.
                forbidden = (
                    f"{LIBRARY_PACKAGE}.components",
                    f"{LIBRARY_PACKAGE}.theme",
                    f"{LIBRARY_PACKAGE}.tokens",
                    f"{LIBRARY_PACKAGE}.catalog",
                )
                if any(line.lstrip().startswith(f"import {prefix}") for line in lines for prefix in forbidden):
                    fail(
                        f"migration consumer must stay on the extracted artifacts only: "
                        f"{path.relative_to(ROOT)}",
                        failures,
                    )
            elif owner == "website":
                # The Wasm sibling module was removed on 2026-08-21; keep the
                # guard so a reintroduced host cannot import this one.
                if any(f"{LIBRARY_PACKAGE}.websitewasm." in line for line in lines):
                    fail(f"website host must not depend on removed wasm hosts: {path.relative_to(ROOT)}", failures)
    print(f"source boundaries: {files_checked} Kotlin files checked, {package_errors} package errors")
    return files_checked, package_errors


def declared_project_edges() -> dict[str, set[str]]:
    """Collect every declared Gradle project edge keyed by source module."""

    edges: dict[str, set[str]] = {}
    for relative in EDGE_RULES:
        path = ROOT / relative
        if path.is_file():
            targets = project_edges(path)
            if targets:
                edges.setdefault(module_path(relative), set()).update(targets)
    root_targets = re.findall(r"\bproject\(\s*\"([^\"]+)\"\s*\)", read(ROOT / "build.gradle.kts"))
    if root_targets:
        edges.setdefault(":", set()).update(root_targets)
    return edges


def owned_packages() -> dict[str, set[str]]:
    """Map each logical owner to the set of Kotlin packages it declares."""

    packages: dict[str, set[str]] = {}
    for owner, source_root in MODULE_SOURCE_ROOTS.items():
        found = {
            package
            for package in (package_for(path) for path in source_files(source_root))
            if package
        }
        if found:
            packages[owner] = found
    return packages


def check_duplicate_packages(failures: list[str]) -> int:
    """Fail when same-package modules could land on one runtime classpath.

    Android's D8/R8 aborts on duplicate classes, so two modules that own the
    same FQCN must never be consumed together. The root publication keeps
    intentional binary-compatibility copies of the packages that physically
    moved to ``:foundation`` / ``:structure``; those pairs are safe exactly
    because no module may declare edges to both members of such a pair.
    """

    edges = declared_project_edges()
    packages = owned_packages()
    owners = sorted(packages)
    hazards = 0
    shared_pairs: list[tuple[str, str]] = []
    for index, first in enumerate(owners):
        for second in owners[index + 1 :]:
            overlap = sorted(packages[first] & packages[second])
            if not overlap:
                continue
            shared_pairs.append((first, second))
            left = OWNER_MODULE[first]
            right = OWNER_MODULE[second]
            problems: list[str] = []
            if right in edges.get(left, set()) or left in edges.get(right, set()):
                problems.append(f"direct project edge between {left} and {right}")
            for consumer, targets in edges.items():
                if consumer in (left, right):
                    continue
                if left in targets and right in targets:
                    problems.append(f"{consumer} consumes both {left} and {right}")
            if problems:
                hazards += 1
                fail(
                    "duplicate-class hazard: "
                    f"{left} and {right} share package(s) {overlap}: " + "; ".join(problems),
                    failures,
                )
    if shared_pairs and not hazards:
        detail = ", ".join(f"{a}~{b}" for a, b in shared_pairs)
        print(
            f"duplicate packages: {detail} "
            "(intentional compatibility copies; no co-consumption edges)"
        )
    return hazards


def main() -> int:
    failures: list[str] = []
    check_module_graph(failures)
    check_source_boundaries(failures)
    check_duplicate_packages(failures)
    if failures:
        print(f"module boundary check: FAIL ({len(failures)} violation(s))", file=sys.stderr)
        return 1
    print("module boundary check: PASS")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

#!/usr/bin/env python3
"""Guard the cross-platform headless model/layout/renderer boundary.

This verifier is intentionally dependency-free. It checks that the contract consumed by native
renderers has no Compose/platform imports and that the main stateful organisms expose the same
reducer abstraction. It is a structural guard, not a substitute for runtime accessibility or
platform rendering acceptance.
"""

from __future__ import annotations

import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
CONTRACT = ROOT / "foundation/src/commonMain/kotlin/com/segnities007/stylishui/foundation/headless/StylishHeadless.kt"
LEGACY_CONTRACT = ROOT / "src/commonMain/kotlin/com/segnities007/stylishui/foundation/headless/StylishHeadless.kt"


def fail(message: str, failures: list[str]) -> None:
    failures.append(message)
    print(f"HEADLESS ARCHITECTURE VIOLATION: {message}", file=sys.stderr)


def main() -> int:
    failures: list[str] = []
    if LEGACY_CONTRACT.exists():
        legacy_text = LEGACY_CONTRACT.read_text(encoding="utf-8")
        if "Binary-compatibility copy" not in legacy_text:
            fail(
                "root headless compatibility copy must declare its migration boundary: "
                f"{LEGACY_CONTRACT.relative_to(ROOT)}",
                failures,
            )
        for symbol in ("StylishReducer", "StylishViewport", "StylishRenderPlan", "StylishRenderer"):
            if symbol not in legacy_text:
                fail(f"root compatibility copy is missing {symbol}", failures)
    if not CONTRACT.is_file():
        fail(f"missing headless contract: {CONTRACT.relative_to(ROOT)}", failures)
    else:
        text = CONTRACT.read_text(encoding="utf-8")
        forbidden_import_prefixes = (
            "import androidx.",
            "import android.",
            "import UIKit",
            "import Foundation.",
            "import com.segnities007.stylishui.components.",
            "import com.segnities007.stylishui.structure.",
        )
        for line in text.splitlines():
            stripped = line.strip()
            if stripped == "@Composable":
                fail("Compose annotation in framework-neutral contract", failures)
            for prefix in forbidden_import_prefixes:
                if stripped.startswith(prefix):
                    fail(f"Compose/platform or rendered-layer import in contract: {prefix}", failures)
        required = (
            "StylishReducer",
            "StylishViewport",
            "StylishLayoutRect",
            "StylishRenderNode",
            "StylishRenderPlan",
            "StylishLayoutEngine",
            "StylishRenderer",
        )
        for symbol in required:
            if symbol not in text:
                fail(f"headless contract is missing {symbol}", failures)

    component_files = {
        "tree": ROOT / "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishTree.kt",
        "transfer": ROOT / "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishTransferUpload.kt",
        "table": ROOT / "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishDataTable.kt",
        "chart": ROOT / "src/commonMain/kotlin/com/segnities007/stylishui/components/charts/StylishMultiSeriesLineChart.kt",
    }
    reducer_symbols = {
        "tree": "StylishTreeStateReducer",
        "transfer": "StylishTransferStateReducer",
        "table": "StylishDataTableStateReducer",
        "chart": "StylishChartStateReducer",
    }
    for name, path in component_files.items():
        if not path.is_file():
            fail(f"missing controlled component source: {path.relative_to(ROOT)}", failures)
            continue
        text = path.read_text(encoding="utf-8")
        if "foundation.headless.StylishReducer" not in text:
            fail(f"{name} does not consume shared StylishReducer", failures)
        if reducer_symbols[name] not in text:
            fail(f"{name} does not expose {reducer_symbols[name]}", failures)

    tree_engine = ROOT / "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishTreeEngine.kt"
    if not tree_engine.is_file():
        fail("missing tree layout engine", failures)
    else:
        text = tree_engine.read_text(encoding="utf-8")
        for symbol in ("StylishLayoutEngine", "StylishRenderPlan", "StylishTreeLayoutInput"):
            if symbol not in text:
                fail(f"tree layout engine is missing {symbol}", failures)

    test_path = ROOT / "src/commonTest/kotlin/com/segnities007/stylishui/components/StylishHeadlessArchitectureTest.kt"
    if not test_path.is_file():
        fail("missing common headless architecture tests", failures)

    if failures:
        print(f"headless architecture check: FAIL ({len(failures)} violation(s))", file=sys.stderr)
        return 1
    print("headless architecture: PASS (framework-neutral contract, shared reducers, tree plan)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

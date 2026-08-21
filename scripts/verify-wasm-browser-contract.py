#!/usr/bin/env python3
"""Check that Wasm browser evidence is described honestly.

This is deliberately a static contract. It does not start Gradle, Chrome, or
an HTTP server. The repository has a browser-executed Kotlin test for
deterministic shared data logic and a dependency-free Chrome accessibility
workflow for the packaged catalog. The contract keeps hosted-CI verification
and native accessibility limitations explicit.
"""

from __future__ import annotations

import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
CI = ROOT / ".github/workflows/ci.yml"
DOC = ROOT / "docs/wasm-browser-acceptance.md"
SMOKE = ROOT / "src/wasmJsTest/kotlin/com/segnities007/stylishui/WasmBrowserQualitySmokeTest.kt"
UI_E2E = ROOT / "scripts/wasm-ui-e2e.mjs"
UI_E2E_ARTIFACT = ROOT / "scripts/verify-wasm-ui-e2e-artifact.py"
UI_E2E_RUNNER = ROOT / "scripts/run-wasm-ui-e2e.sh"
WASM_BUILD = ROOT / "website-wasm/build.gradle.kts"


def fail(message: str) -> "NoReturn":
    print(f"wasm browser contract: FAIL: {message}", file=sys.stderr)
    raise SystemExit(1)


def require_file(path: Path) -> None:
    if not path.is_file():
        fail(f"missing {path.relative_to(ROOT)}")


def require_text(path: Path, text: str) -> None:
    if text not in path.read_text(encoding="utf-8"):
        fail(f"missing {text!r} in {path.relative_to(ROOT)}")


def browser_sources() -> list[Path]:
    return sorted(
        path
        for path in ROOT.glob("**/src/wasmJsTest/**/*.kt")
        if ".gradle" not in path.parts and "build" not in path.parts
    )


def main() -> int:
    for path in (CI, DOC, SMOKE, UI_E2E, UI_E2E_ARTIFACT, UI_E2E_RUNNER, WASM_BUILD):
        require_file(path)

    ci = CI.read_text(encoding="utf-8")
    doc = DOC.read_text(encoding="utf-8")
    smoke = SMOKE.read_text(encoding="utf-8")
    ui_e2e = UI_E2E.read_text(encoding="utf-8")
    ui_e2e_runner = UI_E2E_RUNNER.read_text(encoding="utf-8")
    wasm_build = WASM_BUILD.read_text(encoding="utf-8")

    for text in ("assembleWasmProductionSite", "processedResources/wasmJs/main", "wasmJsBrowserProductionWebpack"):
        if text not in wasm_build:
            fail(f"Wasm build is missing deployable-site marker {text!r}")
    require_text(DOC, "assembleWasmProductionSite")
    require_text(DOC, "processed Compose resources")

    # These markers prove that the recipe can execute the browser target and
    # build a bounded artifact, not that a user can complete a rendered flow.
    for text in ("wasmJsBrowserTest", "xvfb-run", "setup-chrome@v1"):
        if text not in ci:
            fail(f"CI is missing required browser pipeline marker {text!r}")
    # The runner owns Chrome/server lifecycle. Keep these settings in one
    # executable recipe instead of duplicating them in the workflow YAML.
    for text in ("--window-size=1440,1000", "--force-device-scale-factor=1", "--lang=ja-JP"):
        if text not in ui_e2e_runner:
            fail(f"UI workflow runner is missing deterministic browser setting {text!r}")
    for text in ('test "$js_kib" -le 700', 'test "$wasm_kib" -le 10000'):
        if text not in ci:
            fail(f"CI is missing Wasm bundle budget {text!r}")

    require_text(DOC, "Status: UI_E2E_WORKFLOW_IMPLEMENTED_CI_PENDING")
    for text in (
        "wasm-ui-e2e.mjs",
        "wasm-ui-e2e-evidence",
        "wasm-ui-e2e.json",
        "wasm-ui-e2e.png",
        "verify-wasm-ui-e2e-artifact.py",
        "run-wasm-ui-e2e.sh",
    ):
        if text not in ci and text not in doc and text not in ui_e2e_runner:
            fail(f"UI workflow/artifact marker missing: {text!r}")

    for text in (
        "Accessibility.getFullAXTree",
        "Input.dispatchMouseEvent",
        "Input.insertText",
        "Page.captureScreenshot",
        "consoleErrors",
    ):
        if text not in ui_e2e:
            fail(f"UI workflow is missing required evidence marker {text!r}")

    # Keep the pure shared-function smoke distinct from the real accessibility
    # workflow, so their evidence scopes cannot be conflated.
    if "Run browser pipeline test (not UI/DOM E2E)" not in ci:
        fail("shared Wasm smoke lost its bounded non-UI label")
    if "Run packaged Wasm UI accessibility workflow" not in ci:
        fail("CI does not execute the packaged UI accessibility workflow")
    if "run-wasm-ui-e2e.sh" not in ci:
        fail("CI does not execute the packaged UI workflow runner")
    for text in (
        "python3 -m http.server",
        "--remote-debugging-port=",
        "node scripts/wasm-ui-e2e.mjs",
        "verify-wasm-ui-e2e-artifact.py --require",
        "trap cleanup EXIT",
    ):
        if text not in ui_e2e_runner:
            fail(f"UI workflow runner is missing lifecycle marker {text!r}")

    sources = browser_sources()
    if not sources:
        fail("no src/**/wasmJsTest source exists; add a pipeline test or document the gap")

    if "@Test" not in smoke or "resolveStylishDataTableRows" not in smoke:
        fail("the current browser smoke test no longer proves deterministic table execution")

    # Kotlin wasmJsTest remains a shared-logic smoke. The packaged UI workflow
    # is intentionally kept in a separate Node/CDP source with its own artifact.
    ui_markers = re.compile(
        r"runComposeUiTest|createComposeRule|onNode\b|onNodeWith|document\.|window\.|playwright|webdriver",
        re.IGNORECASE,
    )
    ui_sources = [path for path in sources if ui_markers.search(path.read_text(encoding="utf-8"))]
    if ui_sources:
        names = ", ".join(str(path.relative_to(ROOT)) for path in ui_sources)
        fail(f"UI/DOM source detected ({names}); update the acceptance contract and artifact rules")

    # Guard public reports against accidental completion language.
    public_docs = [ROOT / "README.md", ROOT / "docs/comparison-report.md", ROOT / "docs/quality-audit.md"]
    forbidden = re.compile(r"(?:Wasm|browser).{0,40}(?:E2E|DOM).{0,20}(?:complete|完了|成功)", re.IGNORECASE)
    for path in public_docs:
        if forbidden.search(path.read_text(encoding="utf-8")):
            fail(f"overclaiming browser E2E language in {path.relative_to(ROOT)}")

    print("wasm browser contract: PASS")
    print("  browser task: present")
    print("  Chrome/Xvfb pipeline: present")
    print("  deterministic shared-data smoke: present")
    print("  UI accessibility workflow: IMPLEMENTED (hosted CI artifact pending)")
    print("  bundle budgets: JS <= 700 KiB, Kotlin Wasm <= 10 MiB")
    return 0


if __name__ == "__main__":
    main()

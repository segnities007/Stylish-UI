#!/usr/bin/env python3
"""Audit the source-level accessibility contract for interactive composites.

This is intentionally a Linux-friendly source audit.  It proves that the
shared implementation keeps the semantics and focus hooks needed by the
runtime tests; it does not pretend to certify TalkBack, VoiceOver, or a
particular browser screen reader.  Keeping the checks executable prevents a
visual refactor from silently deleting keyboard and collection semantics.
"""

from __future__ import annotations

import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]


def fail(message: str) -> "NoReturn":
    print(f"accessibility contract: FAIL: {message}", file=sys.stderr)
    raise SystemExit(1)


def source(relative: str) -> str:
    path = ROOT / relative
    if not path.is_file():
        fail(f"missing {relative}")
    return path.read_text(encoding="utf-8")


def require_all(relative: str, markers: tuple[str, ...]) -> None:
    text = source(relative)
    missing = [marker for marker in markers if marker not in text]
    if missing:
        fail(f"{relative} is missing: {', '.join(missing)}")


def main() -> int:
    require_all(
        "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishTree.kt",
        (
            "StylishTreeState",
            "focusedId",
            "FocusRequester",
            "focusRequester",
            "onPreviewKeyEvent",
            "Key.DirectionRight",
            "Key.DirectionLeft",
            "selected = node.id == selectedId",
            "stateDescription",
        ),
    )
    require_all(
        "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishTransferUpload.kt",
        (
            "StylishTransferState",
            "rememberStylishFocusRequesters",
            "stylishRovingFocus",
            "CollectionInfo",
            "collectionInfo",
            "CollectionItemInfo",
            "collectionItemInfo",
            "Role.Checkbox",
        ),
    )
    require_all(
        "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishMenubar.kt",
        (
            "FocusRequester",
            "focusRequester",
            "requestFocus()",
            "onPreviewKeyEvent",
            "Key.DirectionLeft",
            "Key.DirectionRight",
            "Key.Escape",
            "selected = expandedIndex == index",
            "stateDescription",
        ),
    )
    require_all(
        "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishContextMenu.kt",
        (
            "stylish_context_menu",
            "contentDescription",
            "Role.Button",
            "onDismissRequest",
        ),
    )
    require_all(
        "src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishCommandPalette.kt",
        (
            "inputFocusRequester",
            "focusRequester(inputFocusRequester)",
            "stylish_command_palette_input",
            "Key.DirectionDown",
            "Key.DirectionUp",
            "Key.Enter",
            "Key.Escape",
            "Role.Button",
            "this.selected = selected",
        ),
    )

    android_script = source("scripts/verify-android-runtime.sh")
    for marker in (
        "uiautomator dump",
        "screencap -p",
        "build-fingerprint.txt",
        "manifest.json",
        "ui-after-tab.xml",
        "KEYCODE_TAB",
        "verify-android-performance.py",
        "performance.json",
        "state page=1 size=10",
        "ID, row 1",
        "Name, row 1",
    ):
        if marker not in android_script:
            fail(f"Android runtime evidence is missing {marker!r}")

    wasm_script = source("scripts/wasm-ui-e2e.mjs")
    for marker in (
        "Accessibility.getFullAXTree",
        "Input.dispatchKeyEvent",
        "keyboard-focus",
        "consoleErrors",
        "Page.captureScreenshot",
    ):
        if marker not in wasm_script:
            fail(f"Wasm UI evidence is missing {marker!r}")

    docs = source("docs/accessibility-contract.md")
    if not re.search(r"TalkBack.*VoiceOver|VoiceOver.*TalkBack", docs, re.DOTALL):
        fail("accessibility contract must distinguish native screen-reader evidence")
    if "Linux-verifiable invariants" not in docs:
        fail("accessibility contract must declare its Linux evidence scope")

    print("accessibility contract: PASS (semantics/focus/source and evidence hooks present)")
    return 0


if __name__ == "__main__":
    main()

#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

python3 - <<'PY'
import json
from pathlib import Path

root = Path.cwd()
tokens = json.loads((root / "docs/tokens/stylish-ui.tokens.json").read_text())
dimensions = (root / "src/commonMain/kotlin/com/segnities007/stylishui/tokens/StylishDimensions.kt").read_text()
shapes = (root / "src/commonMain/kotlin/com/segnities007/stylishui/tokens/StylishShapes.kt").read_text()
animation = (root / "src/commonMain/kotlin/com/segnities007/stylishui/tokens/StylishAnimationTokens.kt").read_text()
interaction = (root / "src/commonMain/kotlin/com/segnities007/stylishui/foundation/StylishInteractionPolicy.kt").read_text()

expected = {
    "space.none": ("spacingNone", "0.dp", dimensions),
    "space.xs": ("spacingXs", "4.dp", dimensions),
    "space.sm": ("spacingSm", "8.dp", dimensions),
    "space.md": ("spacingMd", "12.dp", dimensions),
    "space.lg": ("spacingLg", "16.dp", dimensions),
    "space.xl": ("spacingXl", "20.dp", dimensions),
    "space.xxl": ("spacingXxl", "24.dp", dimensions),
    "space.section": ("sectionSpacing", "32.dp", dimensions),
    "shape.small": ("small", "6.dp", shapes),
    "shape.medium": ("medium", "12.dp", shapes),
    "shape.large": ("large", "20.dp", shapes),
    "shape.extraLarge": ("extraLarge", "28.dp", shapes),
    "motion.short": ("durationShort", "180", animation),
    "motion.medium": ("durationMedium", "300", animation),
    "motion.long": ("durationLong", "500", animation),
}

for path, (property_name, kotlin_value, source) in expected.items():
    section, key = path.split(".")
    value = tokens[section][key]["value"]
    expected_json = kotlin_value[:-3] + "dp" if kotlin_value.endswith(".dp") else kotlin_value + "ms"
    if value != expected_json:
        raise SystemExit(f"token export mismatch: {path} JSON={value!r} expected={expected_json!r}")
    value_is_present = f"= {kotlin_value}" in source
    if section == "shape":
        value_is_present = f"RoundedCornerShape({kotlin_value})" in source
    if f"public val {property_name}:" not in source or not value_is_present:
        raise SystemExit(f"runtime token missing or changed without export update: {path}")

interaction_expected = {
    "interaction.minimumTarget": ("minimumTarget", "iconButtonMinSize", "48.dp"),
    "interaction.focusRingWidth": ("focusRingWidth", "focusRingWidth", "2.dp"),
}
for path, (property_name, dimension_name, kotlin_value) in interaction_expected.items():
    section, key = path.split(".")
    value = tokens[section][key]["value"]
    if value != kotlin_value[:-3] + "dp":
        raise SystemExit(f"token export mismatch: {path} JSON={value!r} expected={kotlin_value[:-3] + 'dp'!r}")
    if f"public val {dimension_name}: Dp = {kotlin_value}" not in dimensions:
        raise SystemExit(f"runtime dimension token missing or changed without export update: {path}")
    if f"public val {property_name}: Dp = DefaultStylishDimensions.{dimension_name}" not in interaction:
        raise SystemExit(f"runtime interaction token must reference dimensions.{dimension_name}: {path}")

if tokens["motion"]["reducedMotion"]["value"] != "instant":
    raise SystemExit("token export mismatch: motion.reducedMotion must remain instant")
if "isStylishReducedMotionEnabled" not in (root / "src/commonMain/kotlin/com/segnities007/stylishui/foundation/VisibilityState.kt").read_text():
    raise SystemExit("reduced-motion runtime contract is missing")

print("Stylish UI token contract: PASS")
PY

#!/usr/bin/env bash
set -euo pipefail

# Static, Linux-friendly audit of the evidence described by the quality docs.
# This intentionally does not invoke Gradle, a browser, or any platform tool:
# it prevents documentation from claiming a runtime gate that the repository
# does not actually contain or that was not recorded as an artifact.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

fail() {
  echo "quality evidence check: $*" >&2
  exit 1
}

require_file() {
  [[ -f "$1" ]] || fail "missing file: $1"
}

require_text() {
  local file="$1"
  local text="$2"
  # grep (not ripgrep) keeps this audit portable on minimal Linux hosts.
  grep -F -q -- "$text" "$file" || fail "missing '$text' in $file"
}

require_file docs/comparison-report.md
require_file docs/quality-audit.md
require_file src/wasmJsTest/kotlin/com/segnities007/stylishui/WasmBrowserQualitySmokeTest.kt
require_file src/jvmTest/kotlin/com/segnities007/stylishui/visual/GoldenTest.kt
require_file src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt
require_file docs/visual-regression-matrix.md
require_file scripts/verify-visual-matrix.sh
require_file src/jvmTest/kotlin/com/segnities007/stylishui/components/AccessibilityLayoutSmokeTest.kt
require_file src/jvmTest/kotlin/com/segnities007/stylishui/components/CompositeAccessibilitySmokeTest.kt
require_file src/commonTest/kotlin/com/segnities007/stylishui/components/StylishDataTableEngineTest.kt
require_file docs/accessibility-contract.md
require_file docs/performance-contract.md
require_file src/jvmTest/kotlin/com/segnities007/stylishui/performance/PerformanceBudgetTest.kt
require_file src/jvmTest/kotlin/com/segnities007/stylishui/performance/ComposeRecompositionBudgetTest.kt
require_file docs/component-state-matrix.md
require_file docs/tokens/figma-mapping.md
require_file docs/wasm-browser-acceptance.md
require_file scripts/wasm-ui-e2e.mjs
require_file scripts/verify-wasm-ui-e2e-artifact.py
require_file scripts/run-wasm-ui-e2e.sh
require_file docs/support-policy.md
require_file docs/public-component-contracts.md
require_file scripts/verify-component-contracts.sh
require_file scripts/verify-performance-contract.sh
require_file scripts/verify-wasm-bundle-evidence.py
require_file scripts/verify-android-performance.py
require_file scripts/verify-compose-recomposition.py
require_file docs/wasm-bundle-baseline.json
require_file scripts/check-semantic-tokens.sh
require_file scripts/verify-sbom.py
require_file scripts/verify-android-r8.py
require_file scripts/export-design-tokens.py
require_file scripts/verify-design-handoff.py
require_file scripts/verify-compose-metrics.py
require_file scripts/verify-native-abi.py
require_file scripts/verify-motion-contract.sh
require_file scripts/verify-module-boundaries.py
require_file scripts/verify-token-literals.sh
require_file scripts/verify-accessibility-contract.py
require_file scripts/verify-android-runtime.sh
require_file scripts/verify-release-evidence.py
require_file docs/android-runtime-acceptance.md
require_file docs/release-evidence.md
require_file src/jvmTest/resources/golden/light-golden.png
require_file src/jvmTest/resources/golden/dark-golden.png

# The browser test must remain an executable test source, while this audit
# records its current scope: deterministic shared data logic, not UI/DOM E2E.
require_text src/wasmJsTest/kotlin/com/segnities007/stylishui/WasmBrowserQualitySmokeTest.kt '@Test'
require_text src/wasmJsTest/kotlin/com/segnities007/stylishui/WasmBrowserQualitySmokeTest.kt 'resolveStylishDataTableRows'
if grep -nE 'runComposeUiTest|onNode|document\.|window\.' \
  src/wasmJsTest/kotlin/com/segnities007/stylishui/WasmBrowserQualitySmokeTest.kt >/dev/null; then
  fail 'Wasm smoke scope changed; update the browser evidence wording before merging'
fi

# Verify the CI recipe is present, without treating its presence as a run.
require_text .github/workflows/ci.yml 'wasmJsBrowserTest'
require_text .github/workflows/ci.yml 'xvfb-run'
require_text .github/workflows/ci.yml 'setup-chrome'
require_text .github/workflows/ci.yml 'verify-android-runtime.sh'
require_text .github/workflows/ci.yml 'verify-release-evidence.py'

# Visual evidence is deliberately bounded and skipped on CI today.
require_text src/jvmTest/kotlin/com/segnities007/stylishui/visual/GoldenTest.kt 'skipOnCi'
require_text src/jvmTest/kotlin/com/segnities007/stylishui/visual/GoldenTest.kt '393.dp, 900.dp'
bash scripts/verify-visual-matrix.sh >/dev/null
bash scripts/verify-performance-contract.sh >/dev/null
bash scripts/check-semantic-tokens.sh >/dev/null
bash scripts/verify-token-literals.sh >/dev/null
python3 scripts/verify-accessibility-contract.py >/dev/null
python3 scripts/verify-design-handoff.py >/dev/null
bash scripts/verify-motion-contract.sh >/dev/null
python3 scripts/verify-module-boundaries.py >/dev/null
python3 scripts/verify-wasm-browser-contract.py >/dev/null
python3 scripts/verify-wasm-ui-e2e-artifact.py >/dev/null
if [[ -f build/reports/release/sbom.json ]]; then
  python3 scripts/verify-sbom.py >/dev/null
  python3 scripts/verify-release-evidence.py >/dev/null
fi
if [[ -f build/reports/release/android-r8.json ]]; then
  python3 scripts/verify-android-r8.py >/dev/null
fi
if [[ -f build/reports/tokens/manifest.json ]]; then
  python3 scripts/export-design-tokens.py >/dev/null
fi
if [[ -f build/compose-metrics/jvm/main/io_github_segnities007:Stylish-UI-module.json ]]; then
  python3 scripts/verify-compose-metrics.py >/dev/null
fi
if [[ -f build/reports/performance/compose-recomposition.json ]]; then
  python3 scripts/verify-compose-recomposition.py >/dev/null
fi
if [[ -f website-wasm/build/ci-evidence/wasm-bundle-size.json ]]; then
  python3 scripts/verify-wasm-bundle-evidence.py --verify-report website-wasm/build/ci-evidence/wasm-bundle-size.json >/dev/null
fi
if [[ -f build/reports/android-runtime/performance.json ]]; then
  python3 scripts/verify-android-performance.py --verify --report build/reports/android-runtime/performance.json >/dev/null
fi
if [[ -f build/reports/native-abi/manifest.json ]]; then
  python3 scripts/verify-native-abi.py >/dev/null
fi

# The additional Linux matrix smoke checks image structure and can emit PNG artifacts,
# but this static audit intentionally does not claim that the JVM test was executed.
require_text src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt 'fontScale in listOf(1f, 2f)'
require_text src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt 'highContrast in listOf(false, true)'
require_text src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt 'WRITE_VISUAL_MATRIX'

# Current Linux JVM accessibility evidence covers RTL and a 2x density smoke;
# it is not a substitute for device or screen-reader testing.
require_text src/jvmTest/kotlin/com/segnities007/stylishui/components/AccessibilityLayoutSmokeTest.kt 'LayoutDirection.Rtl'
require_text src/jvmTest/kotlin/com/segnities007/stylishui/components/AccessibilityLayoutSmokeTest.kt 'Density(1f, 2f)'

# The common DataTable test is a bounded-result functional regression. The
# separate PerformanceBudgetTest provides algorithmic timing smoke evidence;
# neither is a frame-time, heap, or device SLO.
require_text src/commonTest/kotlin/com/segnities007/stylishui/components/StylishDataTableEngineTest.kt '10_000'
require_text src/jvmTest/kotlin/com/segnities007/stylishui/performance/PerformanceBudgetTest.kt 'this is not a frame-time or heap SLO'
if grep -nE 'measureTime|nanoTime|frame|memory|recomposition|benchmark' \
  src/commonTest/kotlin/com/segnities007/stylishui/components/StylishDataTableEngineTest.kt >/dev/null; then
  fail 'performance test gained timing claims; document and gate an explicit SLO first'
fi

# Guard against the superseded, unsubstantiated acceptance claim.
if grep -nE 'Linux受入ゲートは[[:space:]]*100/100|Linux受入は[[:space:]]*100点' docs/comparison-report.md >/dev/null; then
  fail 'comparison report still declares a Linux 100/100 gate'
fi
require_text docs/comparison-report.md 'Linux受入ゲートやGAFA採用度を100/100とは判定しません'
require_text docs/quality-audit.md '実行ログ・スクリーンショットartifact・性能履歴'
require_text docs/quality-audit.md 'Wasm browser UI accessibility workflowのHosted CI artifactが未取得'
require_text docs/accessibility-contract.md 'TalkBack/VoiceOver'
require_text docs/android-runtime-acceptance.md 'UIAutomator'
require_text docs/performance-contract.md 'frame'
require_text docs/component-state-matrix.md 'Remaining adoption evidence'
require_text docs/tokens/figma-mapping.md 'open adoption gate'
require_text docs/tokens/stylish-ui.tokens.json 'highContrastLight'
require_text docs/public-component-contracts.md '220/220'
require_text docs/public-component-contracts.md '194/194'
require_text docs/public-component-contracts.md '`testTag` coverage is intentionally reported'

echo 'quality evidence check: PASS (static scope/evidence consistency; no runtime tests executed)'

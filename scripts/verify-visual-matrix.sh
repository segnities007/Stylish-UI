#!/usr/bin/env bash
set -euo pipefail

# Source-level, Linux-friendly guard for the visual matrix. This does not
# pretend to be a pixel renderer; it prevents a future edit from silently
# reducing the release scenarios while host-font-dependent goldens are local.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

fail() {
  echo "visual matrix check: $*" >&2
  exit 1
}

test -f src/jvmTest/kotlin/com/segnities007/stylishui/visual/GoldenTest.kt || \
  fail "GoldenTest.kt is missing"
test -f docs/visual-regression-matrix.md || fail "matrix documentation is missing"
test -f .github/workflows/ci.yml || fail "CI workflow is missing"

for required in \
  'visualRegressionMatrixGoldens' \
  'visualRegressionMatrixContractIsComplete' \
  'VisualState.Disabled' \
  'VisualState.Loading' \
  'VisualState.Error' \
  'VisualState.Empty' \
  'VisualState.LongText' \
  'LayoutDirection.Rtl' \
  'Density(1f, scenario.fontScale)' \
  'listOf(393, 320)' \
  'listOf(1f, 2f)' \
  'assertEquals(96, visualMatrix.size)'; do
  grep -F -q -- "$required" src/jvmTest/kotlin/com/segnities007/stylishui/visual/GoldenTest.kt || \
    fail "missing matrix contract: $required"
done

grep -F -q -- '2 themes × 2 layout directions × 2 widths × 2 font scales × 6 content states' \
  docs/visual-regression-matrix.md || fail "matrix cardinality is not documented"

for ci_marker in 'WRITE_VISUAL_MATRIX: '\''1'\''' 'visual-matrix-evidence'; do
  grep -F -q -- "$ci_marker" .github/workflows/ci.yml || fail "CI visual artifact marker is missing: $ci_marker"
done

echo 'visual matrix check: PASS (96 deterministic scenarios declared; no runtime rendering executed)'

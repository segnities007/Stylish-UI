#!/usr/bin/env bash
set -euo pipefail

# Linux-friendly contract audit for deterministic performance evidence. This does
# not claim frame-time, heap, or device performance; it only verifies that the
# bounded algorithmic smoke test, report path, and honest scope are wired together.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

fail() {
  echo "performance contract check: $*" >&2
  exit 1
}

require_file() {
  [[ -f "$1" ]] || fail "missing file: $1"
}

require_text() {
  local file="$1"
  local text="$2"
  grep -F -q -- "$text" "$file" || fail "missing '$text' in $file"
}

TEST_FILE="src/jvmTest/kotlin/com/segnities007/stylishui/performance/PerformanceBudgetTest.kt"
DOC_FILE="docs/performance-contract.md"
CI_FILE=".github/workflows/ci.yml"
REPORT_VALIDATOR="scripts/verify-performance-report.py"
WASM_BUNDLE_VALIDATOR="scripts/verify-wasm-bundle-evidence.py"
ANDROID_PERFORMANCE_VALIDATOR="scripts/verify-android-performance.py"
COMPOSE_RECOMPOSITION_VALIDATOR="scripts/verify-compose-recomposition.py"

require_file "$TEST_FILE"
require_file "$DOC_FILE"
require_file "$CI_FILE"
require_file "$REPORT_VALIDATOR"
require_file "$WASM_BUNDLE_VALIDATOR"
require_file "$ANDROID_PERFORMANCE_VALIDATOR"
require_file "$COMPOSE_RECOMPOSITION_VALIDATOR"
require_file "docs/wasm-bundle-baseline.json"

for marker in \
  'dataTable-10k-sort' \
  'tree-100k-flatten' \
  'chart-100k-downsample' \
  'WRITE_PERFORMANCE_REPORT' \
  'build/reports/performance/algorithmic-budgets.json' \
  'MEASUREMENT_ITERATIONS' \
  'p95Millis' \
  'schemaVersion'; do
  require_text "$TEST_FILE" "$marker"
done

for marker in \
  'verify-wasm-bundle-evidence.py' \
  'wasm-bundle-size.json' \
  'wasm-bundle-history.json' \
  'verify-compose-recomposition.py' \
  'WRITE_RECOMPOSITION_REPORT'; do
  require_text "$CI_FILE" "$marker"
done
require_text scripts/verify-android-runtime.sh 'verify-android-performance.py'
require_text scripts/verify-android-runtime.sh 'performance.json'

for marker in \
  '--report build/reports/performance/algorithmic-budgets.json' \
  'Verify algorithmic performance report'; do
  require_text "$CI_FILE" "$marker"
done

require_text "$CI_FILE" 'algorithmic-performance-evidence'
require_text "$CI_FILE" 'build/reports/performance/'
require_text "$CI_FILE" "WRITE_PERFORMANCE_REPORT: '1'"
require_text "$CI_FILE" 'Upload algorithmic performance evidence'
require_text "$DOC_FILE" 'not a frame-time or heap SLO'
require_text "$DOC_FILE" 'algorithmic smoke'
require_text "$DOC_FILE" 'p95Millis'
require_text "$DOC_FILE" 'warmupIterations'
require_text "$DOC_FILE" 'frame-proxy'
require_text "$DOC_FILE" 'wasm-bundle-history.json'
require_text "$DOC_FILE" 'trendClaimAllowed=false'

# Prevent a future edit from turning this bounded smoke into an unqualified
# production-performance claim without updating the documented contract.
if grep -En 'frame.?time SLO is met|heap SLO is met|device performance: PASS|60fps: PASS' \
  "$TEST_FILE" "$DOC_FILE" "$ANDROID_PERFORMANCE_VALIDATOR" "$WASM_BUNDLE_VALIDATOR" >/dev/null; then
  fail 'unqualified frame/heap/device performance claim detected'
fi

echo 'performance contract check: PASS (algorithmic evidence scope is wired and bounded)'

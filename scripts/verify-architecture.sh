#!/usr/bin/env bash
set -euo pipefail

# Enforces the dependency direction documented in AGENTS.md. This is intentionally a small,
# dependency-free guard so it runs on every contributor machine and in CI before compilation.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC="$ROOT/src/commonMain/kotlin/com/segnities007/stylishui"
violations=0

check_forbidden_imports() {
  local label="$1"
  local dir="$2"
  local pattern="$3"
  if [[ ! -d "$SRC/$dir" ]]; then
    return
  fi
  while IFS= read -r file; do
    echo "ARCHITECTURE VIOLATION [$label]: $file"
    grep -En "$pattern" "$file" || true
    violations=$((violations + 1))
  done < <(grep -Erl --include='*.kt' "$pattern" "$SRC/$dir" || true)
}

# Atomic Design direction: upper layers may depend on lower layers only.
check_forbidden_imports "atoms -> molecules/organisms/patterns" "components/atoms" \
  'import com\.segnities007\.stylishui\.components\.(molecules|organisms|patterns)'
check_forbidden_imports "molecules -> organisms/patterns" "components/molecules" \
  'import com\.segnities007\.stylishui\.components\.(organisms|patterns)'
check_forbidden_imports "organisms -> patterns" "components/organisms" \
  'import com\.segnities007\.stylishui\.components\.patterns'

# Models are data-only contracts. They must not pull rendered components into the shared model
# layer, otherwise a seemingly harmless state model change can create an architectural cycle.
check_forbidden_imports "models -> rendered components" "components/models" \
  'import com\.segnities007\.stylishui\.components\.(atoms|molecules|organisms|patterns|charts)'

# Visual-completeness direction: Foundation and Structure must remain headless.
check_forbidden_imports "structure -> components" "structure" \
  'import com\.segnities007\.stylishui\.components\.(atoms|molecules|organisms|patterns)'
check_forbidden_imports "foundation -> components/structure" "foundation" \
  'import com\.segnities007\.stylishui\.(components|structure)'
check_forbidden_imports "tokens -> components/structure" "tokens" \
  'import com\.segnities007\.stylishui\.(components|structure)'
check_forbidden_imports "theme -> components/structure" "theme" \
  'import com\.segnities007\.stylishui\.(components|structure)'

if (( violations > 0 )); then
  echo "Found $violations architecture violation(s)."
  exit 1
fi

echo "Architecture dependency direction: OK"

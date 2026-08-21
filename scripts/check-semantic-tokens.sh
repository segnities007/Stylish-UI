#!/usr/bin/env bash
set -euo pipefail

# Static contract for the highest-impact shared interaction defaults. Component-specific spacing
# is intentionally not rejected here: migrating every historical preview and component-specific
# size in one change would create a noisy visual diff. Shared interaction policy and target
# helpers, however, must derive geometry/motion from semantic tokens.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
FOUNDATION="$ROOT/src/commonMain/kotlin/com/segnities007/stylishui/foundation"
violations=0

check_no_raw_geometry_or_motion() {
  local file="$1"
  local matches
  matches="$(grep -En '[0-9]+(\.[0-9]+)?\.dp|[0-9]+\.[0-9]+f' "$file" || true)"
  if [[ -n "$matches" ]]; then
    echo "SEMANTIC TOKEN VIOLATION: shared default contains a raw geometry/motion literal: $file"
    echo "$matches"
    violations=$((violations + 1))
  fi
}

check_no_raw_geometry_or_motion "$FOUNDATION/StylishInteractionPolicy.kt"
check_no_raw_geometry_or_motion "$FOUNDATION/InteractiveTarget.kt"

if (( violations > 0 )); then
  echo "Found $violations semantic-token violation(s). Use DefaultStylishDimensions or DefaultStylishAnimationTokens."
  exit 1
fi

echo "Semantic token contract: OK (shared interaction defaults use tokens)"

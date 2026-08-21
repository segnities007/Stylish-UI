#!/usr/bin/env bash
set -euo pipefail

# Every shared animation must read the same reduced-motion policy. This is a
# source-level guard: it proves that the policy is reachable from each animated
# component, but it does not certify an OS accessibility setting or device frame.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

fail() {
  echo "motion contract check: $*" >&2
  exit 1
}

mapfile -t animated_files < <(
  grep -Erl --include='*.kt' \
    'AnimatedVisibility|AnimatedContent|animate(Color|Dp|Float|Int|Value)AsState|animateContentSize|\.animateTo\(' \
    src/commonMain/kotlin | sort
)

(( ${#animated_files[@]} > 0 )) || fail 'no shared animation sources found'

for file in "${animated_files[@]}"; do
  if ! grep -Eq 'isStylishReducedMotionEnabled|reducedMotion' "$file"; then
    fail "animated source does not reference reduced-motion policy: $file"
  fi
done

for file in \
  src/commonMain/kotlin/com/segnities007/stylishui/components/atoms/StylishFab.kt \
  src/commonMain/kotlin/com/segnities007/stylishui/components/atoms/StylishExtendedFab.kt \
  src/commonMain/kotlin/com/segnities007/stylishui/components/patterns/StylishHeader.kt \
  src/commonMain/kotlin/com/segnities007/stylishui/components/patterns/StylishFooter.kt; do
  grep -Eq 'if \(reducedMotion\)' "$file" || fail "visibility transition is not snap-guarded: $file"
done

grep -Eq 'reduced-motion' docs/accessibility-contract.md \
  || fail 'accessibility contract does not document reduced-motion behavior'

echo "motion contract check: PASS (${#animated_files[@]} animated source files policy-guarded)"

#!/usr/bin/env bash
set -euo pipefail

# Narrow, deterministic guard for literals that most often leak across themes.
# Preview-only examples are explicitly allowlisted; production components must
# use the active color scheme. QR defaults are intentionally black/white because
# a QR encoder's contrast contract is independent of the UI theme.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

violations=0
while IFS=: read -r file line text; do
  case "$file" in
    src/commonMain/kotlin/com/segnities007/stylishui/structure/ConnectedToggleRow.kt|\
    src/commonMain/kotlin/com/segnities007/stylishui/components/molecules/StylishCarousel.kt|\
    src/commonMain/kotlin/com/segnities007/stylishui/components/organisms/StylishQrCode.kt)
      continue
      ;;
    *)
      echo "TOKEN LITERAL VIOLATION: $file:$line:$text"
      violations=$((violations + 1))
      ;;
  esac
done < <(
  grep -rEn 'Color\.(Black|White)|Color\(0x' \
    src/commonMain/kotlin/com/segnities007/stylishui/components \
    src/commonMain/kotlin/com/segnities007/stylishui/structure || true
)

if (( violations > 0 )); then
  echo "Found $violations theme-sensitive color literal(s); use MaterialTheme/StylishTheme tokens or document a narrow adapter exception." >&2
  exit 1
fi

echo "Token literal contract: PASS (theme-sensitive colors are tokenized or explicitly allowlisted)"

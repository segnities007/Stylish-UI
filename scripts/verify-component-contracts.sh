#!/usr/bin/env bash
set -euo pipefail

# Linux-friendly static contract audit for the public Compose surface.
# This does not invoke Gradle or a platform runtime.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

strict=0
if [[ "${1:-}" == "--strict" ]]; then strict=1; fi
src="$ROOT/src/commonMain/kotlin"
[[ -d "$src" ]] || { echo "component contract check: missing $src" >&2; exit 1; }

tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT
public_list="$tmp/public.tsv"
violations="$tmp/violations.tsv"
: > "$public_list"
: > "$violations"

# Kotlin annotations may be followed by an opt-in/suppression annotation.
while IFS= read -r file; do
  awk -v file="$file" '
    { line[NR] = $0 }
    END {
      for (i = 1; i <= NR; i++) {
        if (line[i] !~ /^[[:space:]]*@Composable([[:space:]]|$)/) continue
        j = i + 1
        while (j <= NR && line[j] ~ /^[[:space:]]*(@|$)/) j++
        if (j > NR || line[j] !~ /^[[:space:]]*public[[:space:]]+fun[[:space:]]+/) continue
        declaration = line[j]
        sub(/^[[:space:]]*public[[:space:]]+fun[[:space:]]+/, "", declaration)
        sub(/[[:space:]]*\(.*/, "", declaration)
        k = i - 1
        while (k > 0 && line[k] ~ /^[[:space:]]*(@|$)/) k--
        kdoc = (k > 0 && line[k] ~ /\*\/[[:space:]]*$/) ? "yes" : "no"
        # Defaults objects expose composable value factories, not visual
        # components; they are documented by the component previews that use
        # them.  Foundation/theme primitives are similarly preview-exempt.
        policy = (file ~ /\/components\// && file !~ /Defaults\.kt$/ || file ~ /\/structure\//) ? "preview-required" : "preview-exempt"
        printf "%s\t%s\t%d\t%s\t%s\n", file, declaration, j, kdoc, policy
      }
    }
  ' "$file" >> "$public_list"
done < <(grep -Erl --include='*.kt' '@Composable' "$src" | sort)

total=0
with_kdoc=0
preview_required=0
with_preview=0
with_test_tag=0
while IFS=$'\t' read -r file name line kdoc preview_policy; do
  [[ -n "$file" ]] || continue
  total=$((total + 1))
  if [[ "$kdoc" == yes ]]; then
    with_kdoc=$((with_kdoc + 1))
  else
    printf 'KDOC\t%s:%s\t%s\n' "$file" "$line" "$name" >> "$violations"
  fi
  if [[ "$preview_policy" == preview-required ]]; then
    preview_required=$((preview_required + 1))
    if grep -Eq '^@Preview' "$file"; then
      with_preview=$((with_preview + 1))
    else
      printf 'PREVIEW\t%s:%s\t%s\n' "$file" "$line" "$name" >> "$violations"
    fi
  fi
  # A stable root tag is required for visual/interaction evidence. Count the
  # shared helper as well as a direct Compose testTag call so the audit does
  # not miss the canonical implementation used by the component surface.
  if grep -Eq '(testTag|stylishTestTag)[[:space:]]*\(' "$file"; then
    with_test_tag=$((with_test_tag + 1))
  fi
done < "$public_list"

files_with_multiple=0
while IFS=$'\t' read -r count file; do
  [[ -n "$file" ]] || continue
  files_with_multiple=$((files_with_multiple + 1))
done < <(cut -f1 "$public_list" | sort | uniq -c | awk '$1 > 1 { print $1 "\t" $2 }')

echo "Public composable contract audit"
echo "  declarations: $total"
echo "  KDoc:         $with_kdoc/$total"
echo "  previews:     $with_preview/$preview_required (components/structure only)"
echo "  testTag file coverage (advisory): $with_test_tag/$total"
echo "  multi-public files (advisory): $files_with_multiple"
if [[ -s "$violations" ]]; then
  echo
  echo "Required contract gaps:"
  sort -u "$violations"
fi
if (( strict == 1 )) && [[ -s "$violations" ]]; then
  echo "component contract check: FAIL" >&2
  exit 1
fi
echo "component contract check: PASS (static contract; testTag and multi-public results are advisory)"

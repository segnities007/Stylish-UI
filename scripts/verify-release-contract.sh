#!/usr/bin/env bash
set -euo pipefail

# Gradle-free release contract audit. This verifies that a checkout contains the
# policy and provenance inputs required before a release is allowed to claim
# GAFA adoption readiness. It deliberately does not claim that a report was
# generated or that a device/native gate passed.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

required_files=(
  LICENSE
  SECURITY.md
  CONTRIBUTING.md
  version.properties
  api/jvm/Stylish-UI.api
  docs/support-policy.md
  docs/support-policy.json
  docs/adapter-contract.md
  docs/sbom-license-policy.md
  docs/android-shrink-and-abi.md
  proguard/stylish-ui-consumer-rules.pro
  scripts/verify-sbom.py
  scripts/verify-android-r8.py
  scripts/export-design-tokens.py
  scripts/verify-design-handoff.py
  scripts/verify-compose-metrics.py
  scripts/verify-native-abi.py
  scripts/verify-module-boundaries.py
  scripts/verify-motion-contract.sh
  scripts/verify-token-literals.sh
  scripts/verify-accessibility-contract.py
  scripts/verify-android-runtime.sh
  scripts/verify-release-evidence.py
  scripts/verify-support-policy.py
  docs/android-runtime-acceptance.md
  docs/release-evidence.md
  samples/android-r8/build.gradle.kts
)

for file in "${required_files[@]}"; do
  test -f "$file" || { echo "missing release contract input: $file" >&2; exit 1; }
done

grep -q 'Apache License' LICENSE || { echo 'LICENSE must identify Apache License 2.0' >&2; exit 1; }
grep -qi 'vulnerability reporting' SECURITY.md || { echo 'security reporting policy missing' >&2; exit 1; }
grep -q 'SBOM' docs/sbom-license-policy.md || { echo 'SBOM policy missing' >&2; exit 1; }
grep -q 'generateSbom' build.gradle.kts .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'SBOM generation is not wired to Gradle and CI' >&2
  exit 1
}
grep -q -- '--require-clean' .github/workflows/release-please.yml || {
  echo 'release workflow must fail closed on unresolved licenses' >&2
  exit 1
}
grep -q 'assembleRelease' .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'Android R8 consumer sample is not wired to CI/release' >&2
  exit 1
}
grep -q 'export-design-tokens.py' .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'design token handoff is not wired to CI/release' >&2
  exit 1
}
grep -q 'verify-design-handoff.py' .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'design token/Figma interchange validation is not wired to CI/release' >&2
  exit 1
}
grep -q 'verify-compose-metrics.py' .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'Compose stability metrics are not wired to CI/release' >&2
  exit 1
}
grep -q 'verify-release-evidence.py' .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'release evidence index is not wired to CI/release' >&2
  exit 1
}
grep -q 'verify-support-policy.py' build.gradle.kts .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'machine-readable support policy is not wired to build and CI/release' >&2
  exit 1
}
grep -q ':samples:adapters:jvmTest' build.gradle.kts || {
  echo 'adapter contract tests are not wired to the root verification lifecycle' >&2
  exit 1
}
grep -q 'verify-native-abi.py' .github/workflows/ci.yml || {
  echo 'Native ABI snapshot is not wired to the iOS CI job' >&2
  exit 1
}
grep -q 'verify-accessibility-contract.py' .github/workflows/ci.yml .github/workflows/release-please.yml || {
  echo 'Accessibility source/evidence contract is not wired to CI/release' >&2
  exit 1
}
grep -q '未確認\|未検証\|未実行' docs/android-shrink-and-abi.md docs/sbom-license-policy.md docs/support-policy.md || {
  echo 'release policies must distinguish evidence from policy' >&2
  exit 1
}

echo 'Release contract inputs: PASS (policy/provenance inputs present; no platform gate claimed)'

#!/usr/bin/env bash
set -euo pipefail

# Single Linux acceptance command. Keeping the gate in one script prevents a local
# verification run from accidentally omitting browser, API, or architecture checks.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

git diff --check
bash scripts/verify-architecture.sh
python3 scripts/test_design_harness.py
python3 scripts/verify-design-harness.py

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$ROOT/.gradle-ci}" \
  ./gradlew check apiCheck assemble \
  --no-daemon --max-workers=1 \
  -Djava.net.preferIPv4Stack=true \
  -Dkotlin.incremental=false \
  -Dkotlin.compiler.execution.strategy=in-process

echo "Linux quality gate: PASS"

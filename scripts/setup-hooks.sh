#!/usr/bin/env bash
# Installs the repository git hooks (core.hooksPath -> .githooks).
#
# The pre-push hook blocks pushes that re-introduce commits already
# merged into main (the squash-merge conflict trap, see AGENTS.md rule 3).
#
# Usage: scripts/setup-hooks.sh
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
HOOKS_DIR="$REPO_ROOT/.githooks"

git config core.hooksPath "$HOOKS_DIR"
echo "git hooks installed: $HOOKS_DIR (pre-push)"

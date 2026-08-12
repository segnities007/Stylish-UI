#!/usr/bin/env bash
# Creates a feature branch from the LATEST origin/main.
#
# Why this exists: squash-merged branches are unusable as branch bases —
# they inherit commits that already exist in main, so GitHub reports
# conflicts even when the content is identical (AGENTS.md rule 3).
# This script is the single entry point for creating feature branches:
# it fetches origin, verifies main is up to date, and creates the branch
# from origin/main — never from the current branch.
#
# Usage:
#   scripts/new-branch.sh <branch-name>
#
# Examples:
#   scripts/new-branch.sh feat/components-slider
#   scripts/new-branch.sh fix/nav-bar-insets
set -euo pipefail

if [ "$#" -ne 1 ]; then
  echo "usage: $0 <branch-name>" >&2
  exit 1
fi

BRANCH="$1"

# Reject creating branches from stale bases.
if git rev-parse --verify "$BRANCH" >/dev/null 2>&1; then
  echo "error: branch '$BRANCH' already exists" >&2
  exit 1
fi

git fetch origin

LOCAL_MAIN=$(git rev-parse --short main 2>/dev/null || echo "none")
REMOTE_MAIN=$(git rev-parse --short origin/main)

if [ "$LOCAL_MAIN" != "$REMOTE_MAIN" ]; then
  echo "note: local main ($LOCAL_MAIN) differs from origin/main ($REMOTE_MAIN); using origin/main"
fi

git checkout -b "$BRANCH" origin/main
echo
echo "branch '$BRANCH' created from origin/main ($REMOTE_MAIN)"

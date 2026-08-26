# Agent Guidelines

This document provides instructions for AI agents and automated tools working on the Stylish UI project.

## Project Overview

Stylish UI is an Android Compose design system library published to Maven Central.

- **Group ID**: `io.github.segnities007`
- **Artifact ID**: `stylish-ui`
- **License**: Apache License 2.0

## Architecture: Atomic Design

Components follow Atomic Design. Dependencies flow **one way** — upper layers may import lower layers, never the reverse.

```
patterns → organisms → molecules → atoms → foundation / theme / tokens
```

| Layer | Package | Definition | May import |
|---|---|---|---|
| **atoms** | `components.atoms` | Single interactive element. Composes no other Stylish components. | foundation, theme, tokens |
| **molecules** | `components.molecules` | Composition of atoms and/or M3 primitives into a reusable UI unit. | atoms, foundation, theme, tokens |
| **organisms** | `components.organisms` | Composition of multiple molecules (or molecules + atoms) into a complete UI section. | molecules, atoms, foundation, theme, tokens |
| **patterns** | `components.patterns` | Page-level layout / scaffold that assembles organisms and molecules. | organisms, molecules, atoms, foundation, theme, tokens |

Supporting layers (not components):

- **foundation** (`foundation/`) — geometry, outlines, interaction helpers
- **theme** (`theme/`) — colors, typography, chart colors
- **tokens** (`tokens/`) — dimension constants
- **models** (`components/models/`) — immutable data classes consumed by any layer

### Rules for adding or moving components

1. A component that composes **zero** Stylish components is an **atom**.
2. A component that composes **only atoms** (or M3 primitives) is a **molecule**.
3. A component that composes **any molecule** is at least an **organism**.
4. A component that assembles organisms into a page-level layout is a **pattern**.
5. Never import from a layer above your own.

### Design quality gate

Every component must satisfy the design checklist in [DESIGN.md](DESIGN.md) — **Clear, Simple, Modern**. Review it before creating or modifying any UI component.

## Architecture: The Three Layers (Visual Completeness)

Orthogonal to Atomic Design (which measures **composition** complexity), every
piece of Stylish UI sits on a second axis: **visual completeness** — how much
of the look has been decided. This axis has three layers, and dependency flows
**one way** (`Finish → Structure → Foundation`); a layer may import only the
layers to its right.

```
Finish  →  Structure  →  Foundation
(styled)   (headless)    (material)
```

| Layer | Package | Definition | Decides visuals? |
|---|---|---|---|
| **Foundation** (基礎) | `foundation/`, `tokens/`, `theme/` | Primitive material and rules: geometry computation, dimension tokens, color/typography, interaction logic. Contains **no `@Composable` that renders UI**. | — (no UI) |
| **Structure** (構造) | `structure/` | Headless components: layout, slots, semantics, and behavior, but **no visual styling** — no colors, elevation, corner-radius values, or animation. Computes connection geometry and delegates each item's rendering to a slot lambda. | ❌ |
| **Finish** (仕上げ) | `components/` (atoms~patterns) | Styled components wearing the Stylish look (color, elevation, corner radius, animation, haptics). Consumes a Structure component (or Foundation directly, for atoms). | ✅ |

### How the two axes combine

Atomic Design (atom / molecule / organism / pattern) organizes the **Finish**
layer inside `components/`. The three-layer axis is independent of it: a
composable is simultaneously a molecule (composition axis) **and** Finish
(visual-completeness axis). The Structure layer mirrors the same compositions
but headless. `components/models/` holds immutable data classes consumed by any
layer.

### Layer judgment rules

Use these mechanical rules to decide where new code belongs:

1. **Foundation** — it computes, holds values, or decides logic, and renders
   no pixels. A function that takes positions or values and returns geometry,
   colors, or booleans (e.g. `connectedShape`, `connectedOutline`,
   `isActionable`, `StylishDimensions`) is Foundation.
   *Test: does it render pixels or make a visual decision? No → Foundation.*
2. **Structure** — it is a `@Composable` that lays out children, provides
   slots, and sets semantics/behavior, but makes **no** visual decision (no
   color, elevation, corner-radius value, or animation). It delegates each
   item's rendering to a slot lambda, passing the computed connection
   geometry.
   *Test: does it decide any visual property? No — but it lays out and has
   slots → Structure.*
3. **Finish** — it applies the Stylish look (color, elevation, corner radius,
   animation, haptics), typically by consuming a Structure component and
   supplying a styled item renderer.
   *Test: does it apply the Stylish look? Yes → Finish.*

### The Connected exemplar

The Card family is the reference implementation of the Structure/Finish split:
a layout computes connection geometry per index and delegates rendering through
the `StylishConnectedCardItemContent` slot, whose default
(`DefaultStylishConnectedCardItem`) is the Finish renderer. **New Structure
components must follow this delegation pattern; new Finish components must
consume a Structure component rather than re-implementing layout.**

## Component Conventions

### One file, one concern

Each file contains exactly **one** public composable (or one cohesive group such as a typealias + its default implementation). Do not put unrelated components in the same file.

### Previews live with the component

Every public `@Composable` must have at least one `@Preview` in the **same file**. Previews are `private` and serve as executable documentation.

```kotlin
@Preview(name = "Descriptive name", showBackground = true, widthDp = 393)
@Composable
private fun MyComponentPreview() {
    StylishTheme(darkTheme = false) {
        MyComponent(/* realistic sample data */)
    }
}
```

### Parameter ordering

Follow this order consistently across all components:

1. Primary content parameters (e.g. `title`, `items`, `options`)
2. `modifier: Modifier = Modifier`
3. Style / appearance parameters (colors, text styles, shapes)
4. Behaviour parameters (`enabled`, `maxLines`, overflow)
5. Slot lambdas last (`leadingContent`, `trailingContent`, `content`)

### KDoc on public API

Every public composable, function, and data class must have a KDoc comment describing its purpose. Document non-obvious parameters. AI agents rely on KDoc to understand contracts without reading the implementation.

## Development Workflow

1. **Always work on a branch** — never push directly to `main`.
2. **One branch per PR — discard after merge.** Squash merge creates a new
   commit on `main` with no link to the original branch. Reusing a
   squash-merged branch causes recurring merge conflicts because Git cannot
   recognise that the work is already in `main`. After a PR is merged,
   delete the branch and create a fresh one from the latest `main`.
3. **Create branches ONLY from `origin/main`** — never from an existing
   branch. A branch created from a merged branch inherits commits that are
   already in `main`, which GitHub flags as conflicts even if the content
   is identical. Concretely:
   - Always start with `git checkout -b <branch> origin/main` (after
     `git fetch origin`), never `git checkout -b <branch>` from the
     current branch.
   - Before opening a PR, verify the branch contains no already-merged
     commits: `git log --oneline origin/main..HEAD` must show only new
     work. If it shows old commit titles, rebase them away with
     `git rebase --onto origin/main <old-base-sha>`.
   - Use `scripts/new-branch.sh <branch-name>` as the single entry point
     for creating feature branches (it fetches, verifies, and creates
     from `origin/main`).
   - **Local pre-push guard (automatic):** the repository's `pre-push` git
     hook (`.githooks/pre-push`, installed via `scripts/setup-hooks.sh`,
     which `new-branch.sh` runs automatically) blocks any push whose
     branch does not contain the latest `origin/main` as an ancestor —
     i.e. branches created from squash-merged or otherwise stale bases.
     The same check runs in CI (`check-branch-base` job). If the hook
     blocks a push, rebase the new work onto main
     (`git rebase --onto origin/main <first-new-commit>^`) and
     `git push --force-with-lease`, or recreate the branch with
     `scripts/new-branch.sh`. To bypass once: `git push --no-verify`
     (only after verifying the push is intentional).
4. **Open a Pull Request** for all changes.
5. **Use Conventional Commits** for all commit messages and PR titles.
   GitHub auto-generates the PR title from the branch name (e.g.
   `docs/foo` → "Docs/foo"). Set the PR title manually to
   `<type>(<scope>): <subject>` format with a lowercase subject (e.g.
   `docs(components): enrich KDoc across all public API`).
6. **Use Squash merge** when merging PRs.
7. **Do not modify release-related files** unless explicitly instructed:
   - `version.properties`
   - `.release-please-manifest.json`
   - `CHANGELOG.md` (managed by Release Please)

## Conventional Commits

Use the following format for PR titles and commit messages:

```
<type>(<scope>): <subject>
```

Allowed types:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Code style changes
- `refactor`: Refactoring
- `perf`: Performance improvement
- `test`: Tests
- `build`: Build system
- `ci`: CI configuration
- `chore`: Other changes
- `revert`: Revert a previous change

Example scopes:

- `components`
- `structure`
- `foundation`
- `theme`
- `tokens`
- `docs`
- `ci`

## Before Submitting

- Run `./gradlew jvmTest` locally
- Run `./gradlew assemble` locally
- Run `./gradlew apiCheck` locally (verifies public ABI has not changed unexpectedly)
- If the public API changed intentionally, run `./gradlew apiDump` to update the reference dump
- Ensure tests pass
- Do not modify `version.properties` or release-related files

## Prohibited Actions

AI agents must NOT perform the following actions:

- Create or merge release PRs
- Modify `version.properties` directly
- Push directly to `main`
- Publish to Maven Central
- Change GitHub repository settings
- Update GitHub Secrets

## Release Process

Releases are handled by Release Please. Human maintainers must review and merge release PRs.

## Testing

Always run tests before considering a change complete:

```bash
./gradlew jvmTest
./gradlew assemble
./gradlew apiCheck
```

## Documentation

When adding new public components or APIs, update the README.md with usage examples.

## Semantic Decomposition (MANDATORY)

UI コードは**意味単位の関数**に分割すること。1関数 = 1つの意味的責務。

- **80行ルール**: 関数は 80 行を超えてはならない。超えたら即座に分解する。
  - `scripts/verify-composable-size.py` が `check` で失敗を検出する（ベースライン・ラチェット方式: 既存違反は `scripts/composable-size-baseline.txt` に登録、**新規違反のみ失敗**。減らすのは自由、増やすのは禁止、しきい値の緩和や `size:allow` の乱用は禁止）。
- **画面 Composable = オーケストレーター**: 状態 + コールバック + パーツ組み立てのみに専念する。描画ロジックを書かない。
- **切り出し単位（役割名の private 関数）**:
  - `FooHeader` / `FooTopBar` — ヘッダー
  - `FooFloatingAction` / `FooFab` — FAB
  - `LazyListScope.fooItems(...)` — リスト本体（読み込み中/空/項目列の分岐も含む）
  - `FooCard` / `FooItem` — 1項目
  - `FooDialog` — ダイアログ（入力状態はダイアログ内に閉じ込める）
  - `FooSection` — その他の意味的セクション
- **状態ホルダー**: 関連する状態が 3 つ以上並ぶなら `XxxState` クラス + `rememberXxxState()` に集約する。
- **テーマ色は `colorScheme` から**: `Color.White` / `Color.Black` 固定は禁止（ダイナミック カラーを壊す）。
- **リファクタリングは挙動を変えない**: 純粋な切り出し・再配置のみ。検証はコンパイル + 既存テスト。

違反したままコミットしないこと。`check` が失敗したら分解して解消する。

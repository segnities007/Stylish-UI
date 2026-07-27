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
3. **Open a Pull Request** for all changes.
4. **Use Conventional Commits** for all commit messages and PR titles.
   GitHub auto-generates the PR title from the branch name (e.g.
   `docs/foo` → "Docs/foo"), which **fails the `pr-title-check` CI**.
   Always set the PR title manually to `<type>(<scope>): <subject>`
   format with a lowercase subject (e.g.
   `docs(components): enrich KDoc across all public API`).
5. **Use Squash merge** when merging PRs.
6. **Do not modify release-related files** unless explicitly instructed:
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

# Agent Guidelines

This document provides instructions for AI agents and automated tools working on the Stylish UI project.

## Project Overview

Stylish UI is an Android Compose design system library published to Maven Central.

- **Group ID**: `io.github.segnities007`
- **Artifact ID**: `stylish-ui`
- **License**: Apache License 2.0

## Development Workflow

1. **Always work on a branch** — never push directly to `main`.
2. **Open a Pull Request** for all changes.
3. **Use Conventional Commits** for all commit messages and PR titles.
4. **Use Squash merge** when merging PRs.
5. **Do not modify release-related files** unless explicitly instructed:
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

- Run `./gradlew test` locally
- Run `./gradlew assemble` locally
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
./gradlew test
./gradlew assemble
```

## Documentation

When adding new public components or APIs, update the README.md with usage examples.

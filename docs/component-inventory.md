# Stylish UI component inventory

This inventory is the Linux/common target contract. The Gradle `checkComponentInventory` task
guards the minimum source inventory so a refactor cannot silently remove a UI family.

| Family | Source files (current) | Scope |
|---|---:|---|
| Atoms | 43 | buttons, fields, selection controls, feedback primitives, avatar/badge, progress |
| Molecules | 54 | connected groups, lists, date/time, table, pagination, empty/loading states |
| Organisms | 21 | navigation, dialogs, search, DataTable, advanced interaction primitives |
| Patterns | 11 | scaffold, app bars, adaptive layouts, page sections |
| Charts | 7 | line/bar/pie, multi-series, formatting/math/drawing helpers |
| Headless structure | 20+ | connected layouts and `DataTableLayout` slot contracts |

## API quality contract

Every new public component must provide:

1. A `Modifier` entry point and stable `testTag` where a root node exists.
2. Theme-driven defaults through `StylishTheme`, with explicit color/shape/typography overrides.
3. Slots for content that applications commonly replace (icons, labels, empty/loading/error states).
4. Semantics for action, selected/expanded/disabled state, and keyboard behavior where applicable.
5. KDoc with a minimal usage example and an entry in the catalog or a documented reason for omission.

Run the Linux quality gate with:

```bash
./gradlew checkComponentInventory check jvmTest apiCheck
```

The inventory guard complements, rather than replaces, API compatibility and visual golden tests.

# Public component contract audit

`scripts/verify-component-contracts.sh` is the Linux-friendly source audit for
the common Compose API. It is wired into the Gradle `check` lifecycle as
`checkComponentContracts`, but the script can also be run directly without a
Gradle daemon:

```bash
bash scripts/verify-component-contracts.sh --strict
```

## Current evidence

The audit currently finds 220 public `@Composable` declarations:

| Contract | Result | Interpretation |
| --- | ---: | --- |
| KDoc immediately preceding every public composable | 220/220 | Required gate; passes |
| Preview in every component/structure source file | 194/194 | Required gate; passes |
| stable root tag file coverage (`testTag` or `stylishTestTag`) | 101/220 | Advisory for non-rendering/slot/state APIs; all tagged roots use the shared `stylishTestTag` namespace |
| Multiple public composables in one file | 29 files | Advisory; cohesive defaults/variants and state factories are allowed |

The preview rule intentionally excludes foundation, theme, and `*Defaults.kt`
files. Those files expose headless modifiers, state/value factories, or theme
defaults; their behavior is exercised by the styled component previews and
tests rather than by a standalone visual preview.

## Remaining adoption work

`testTag` coverage is intentionally reported rather than made a hard failure. Root-tag coverage is
intentionally reported rather than made a hard failure:
the existing API includes static layout primitives, slots, and state factories
that do not own a rendered root. Rendered components now use
`Modifier.stylishTestTag("component_name")`, which normalizes the identifier to
the portable `stylish_` namespace while preserving the caller's modifier chain.
Direct `Modifier.testTag` remains supported for components whose tag is
dynamic (for example a row keyed by a stable item id). The verifier counts
both forms and keeps the non-rendering exceptions visible instead of claiming
that a state factory has a UI node.

The next promotion gate is declaration-level classification: each public
declaration must be marked as `rendered-root`, `slot/scope`, or
`state/value-factory`; `rendered-root` entries require a stable tag and a
semantics contract appropriate to their role. This avoids forcing meaningless
tags onto layout scopes and keeps test selectors deterministic across Android,
JVM, Wasm, and future native adapters.

Likewise, the multi-public-file list is an architectural review queue, not a
blind one-function-per-file rewrite. Variant families and a defaults object are
cohesive; unrelated public components must be split when touched. The audit
does not claim that this queue is complete.

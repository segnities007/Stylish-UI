# Token audit — `docs/tokens/**` vs `scripts/export-design-tokens.py`

Audit date: 2026-08-21. Auditor: Lane D (release-evidence & supply-chain audit).
Scope: consistency of naming, values, and layers between the committed token
contract (`docs/tokens/stylish-ui.tokens.json`, plus README/figma-mapping) and
the exporter logic (`scripts/export-design-tokens.py`), cross-checked against
the Kotlin runtime token sources.

## Verdict

**PASS with findings.** The exporter faithfully reflects the JSON contract
(25 tokens, deterministic output, correct CSS/Figma transformations), and every
JSON value that has a Kotlin runtime counterpart matches it exactly. Three
schema-level inconsistencies and two coverage gaps are listed below; none block
the handoff pipeline today because the exporter special-cases them, but they
should be resolved before external tooling consumes the JSON strictly.

## Evidence chain

| Layer | File | Role |
|---|---|---|
| Runtime source of truth | `src/commonMain/.../tokens/StylishDimensions.kt`, `StylishAnimationTokens.kt`, `tokens/StylishShapes.kt`, `foundation/StylishInteractionPolicy.kt` | Kotlin defaults |
| Interchange contract | `docs/tokens/stylish-ui.tokens.json` | designer handoff source |
| Exporter | `scripts/export-design-tokens.py` | derives handoff/CSS/Figma artifacts |
| Generated output | `build/reports/tokens/{stylish-ui.tokens.handoff.json, stylish-ui.tokens.css, stylish-ui.tokens.figma.variables.json, manifest.json}` | uploaded by CI (`design-token-handoff`) |

Re-export executed during this audit:
`python3 scripts/export-design-tokens.py --output-dir <tmp>` →
`PASS (25 tokens, sha256=96bc1a45297d11688fede275aea9a6080238764fed8a6bbb3dcb404e36674977)`.

## Token inventory check (25 = 7 color + 8 space + 4 shape + 4 motion + 2 interaction)

Matches `manifest.json.tokenCount=25` and the README claim ("25 tokens across
four modes"). The `flatten()` guard (`len(tokens) < 20`) is satisfied.

## Value parity vs Kotlin runtime

| JSON token | Value | Kotlin counterpart | Match |
|---|---|---|---|
| `space.none…section` | 0/4/8/12/16/20/24/32 dp | `spacingNone/Xs/Sm/Md/Lg/Xl/Xxl/Xxxl` | ✅ |
| `shape.small/medium/large/extraLarge` | 6/12/20/28 dp | `StylishShapes` defaults | ✅ |
| `motion.short/medium/long` | 180/300/500 ms | `durationShort/Medium/Long` | ✅ |
| `motion.reducedMotion` | `instant` | runtime uses `snap()`/0 ms under `isStylishReducedMotionEnabled()` | ✅ semantics |
| `interaction.minimumTarget` | 48 dp | `StylishInteractionPolicy.minimumTarget` → `iconButtonMinSize = 48.dp` | ✅ |
| `interaction.focusRingWidth` | 2 dp | `StylishDimensions.focusRingWidth = 2.dp` | ✅ |
| `color.*` (7) | `{material.*}` aliases | host-owned `ColorScheme` resolution | ✅ by design |

## Naming / transformation checks

- `css_name()`: path lowercased, non-alphanumerics → `-`, prefixed `--stylish-`.
  Deterministic but **lossy**: camelCase is flattened (`color.onSurface` →
  `--stylish-color-onsurface`, `shape.extraLarge` → `--stylish-shape-extralarge`,
  `interaction.focusRingWidth` → `--stylish-interaction-focusringwidth`). This
  convention is implemented consistently but is not written down in
  `docs/tokens/README.md` — documentation gap, minor.
- `css_value()`: `{ref}` → `var(--ref-dashed)`; `dp` → `px` (documented 1dp
  baseline); `instant` → `0ms`. Verified against generated CSS line-by-line.
- Figma handoff: mode IDs `slug(mode)` produce `light`, `dark`,
  `highcontrastlight`, `highcontrastdark` — matches `figma-mapping.md`.
  Variable names are slash-delimited paths; IDs stable `stylish.<path>`;
  `{material.*}` correctly emitted as `externalAliases` with `external: true`.
- Mode list `[light, dark, highContrastLight, highContrastDark]` is identical in
  the JSON contract, `FIGMA_MODE_NAMES`, README, and figma-mapping. ✅

## Findings (diffs / inconsistencies)

1. **Type mismatch on `motion.reducedMotion`** — declared `"type": "cubicBezier"`
   with literal value `"instant"`. DTCG-style cubic-bezier tokens expect a
   4-number array; only the exporter's hard-coded special case
   (`token == "instant"` → `0ms`, STRING resolved type in Figma handoff) makes
   this valid. A strict DTCG parser would reject the file. Recommend either
   `"type": "duration", "value": "0ms"` or documenting the extension.
2. **Space-scale naming divergence** — JSON uses `space.section` (32dp) where
   Kotlin names the same step `spacingXxxl` (with `sectionSpacing` as an alias);
   Kotlin's `spacingXxs` (2dp) has no JSON entry. Mapping should be recorded in
   `figma-mapping.md` or the JSON extended.
3. **Motion coverage gap** — Kotlin exposes `durationEmphasized = 350ms` and
   three easing curves (`FastOutSlowIn`, `CubicBezier(0.2,0,0,1)`,
   `LinearOutSlowIn`) that have no JSON representation. If the JSON is intended
   as the complete designer contract, these are missing; if intentionally
   semantic-only, state the subset policy in the README.
4. **CSS artifact does not resolve colors standalone** — all `--stylish-color-*`
   values reference undefined `var(--material-*)`. Host-owned by design and
   already noted in `figma-mapping.md`; restated here so Web adopters do not
   mistake the CSS file for a drop-in theme.
5. Minor: exporter writes `status: "PASS"` unconditionally into
   `manifest.json`; the real gate is `verify-design-handoff.py` (double-export
   byte equality, hash/mode/alias checks). Acceptable, but the manifest field is
   producer-asserted, not independently verified at generation time.

## CI wiring

`ci.yml` runs `export-design-tokens.py` (default `build/reports/tokens`) and
uploads exactly that directory as `design-token-handoff`. Path verified correct.
No retention-days is set on any upload in either workflow (GitHub default,
90 days on hosted public repos unless overridden by org settings).

## What this audit does NOT claim

No Figma file exists behind these artifacts (`nativeFigmaExport: false` is
correct); no pixel-parity or platform rendering claim is made. Designer-owner
approval remains an open adoption gate, as stated in `docs/tokens/README.md`.

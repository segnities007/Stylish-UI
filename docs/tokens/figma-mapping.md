# Figma handoff mapping

This is the stable name mapping a Figma library or Token Studio export must use when connecting to
the Stylish UI token contract. It is intentionally a mapping specification, not a claim that a
Figma file has already been synchronized.

| Figma variable collection | Variable prefix | Runtime owner |
|---|---|---|
| Color / Semantic | `stylish.color.*` | `ColorScheme` and `StylishComponentColors` |
| Spacing | `stylish.space.*` | `StylishDimensions.spacing*` |
| Shape | `stylish.shape.*` | `StylishShapes` |
| Motion | `stylish.motion.*` | `StylishAnimationTokens` |
| Interaction | `stylish.interaction.*` | `StylishInteractionPolicy` |

Required Figma modes are `Light`, `Dark`, `High Contrast Light`, and `High Contrast Dark`. RTL is a layout direction rather
than a color mode and must be represented by a mirrored component state in the component library.
Every component variable must alias a semantic variable; raw hex colors, arbitrary spacing, and
component-local motion values are not valid handoff output.

## Repository-side interchange artifact

`python3 scripts/export-design-tokens.py` emits
`build/reports/tokens/stylish-ui.tokens.figma.variables.json`. This is a deliberately small,
reviewable interchange format with schema `stylish-ui.figma-variable-handoff.v1`:

- `collection.modes` uses stable IDs (`light`, `dark`, `highcontrastlight`, and
  `highcontrastdark`) and human-readable names.
- Each variable has a stable `stylish.<path>` ID, a slash-delimited Figma name, a typed
  `resolvedType`, and a value for every mode.
- References such as `{material.surface}` are explicit external aliases. The host/Figma
  foundation collection must resolve them; the exporter never silently substitutes a color.
- Dimensions and durations retain their source unit (`dp` or `ms`) instead of being flattened into
  an ambiguous number.

Run `python3 scripts/verify-design-handoff.py` to verify source hash, mode coverage, alias
inventory, CSS parity, and byte-for-byte deterministic output. The generated JSON is an adapter
input, not a claim of a synchronized Figma file. A real adoption review still requires an
export-from-Figma diff, component anatomy/state approval, and a design owner sign-off.

Before adoption, a design owner must attach an export diff showing JSON ↔ Figma parity and approve
the component anatomy/state matrix. Until that artifact exists, Figma synchronization remains an
open adoption gate.

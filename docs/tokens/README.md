# Token contract

`stylish-ui.tokens.json` is the portable designer-handoff contract for Stylish UI. It is
deliberately semantic: component APIs consume `StylishTheme`, never raw palette values or
arbitrary dimensions. The Kotlin token classes under `src/commonMain/.../tokens` are the runtime
implementation and remain the source of truth for default values; this JSON is the interchange
format for design tools and code generators.

When changing a token:

1. Update the Kotlin token and its KDoc.
2. Update this JSON and the component/state matrix that depends on it.
3. Add or update a light/dark, RTL, 200% font-scale, and reduced-motion evidence case.
4. Record the change in the migration notes when it changes visual output or API behavior.

The semantic color contract supports `Light`, `Dark`, `High Contrast Light`, and `High Contrast
Dark` modes. `StylishTheme(highContrast = true)` selects the deterministic high-contrast roles;
hosts may inject a branded high-contrast `ColorScheme` without changing component APIs.

The schema intentionally does not encode platform-specific implementation details. A Web or
SwiftUI adapter maps these semantic names to CSS custom properties or native environment values.

`scripts/verify-token-contract.sh` checks the exported spacing, shape, motion, and interaction
defaults against the Kotlin runtime and verifies that the reduced-motion semantic remains wired
to the runtime accessibility policy. `scripts/check-semantic-tokens.sh` additionally prevents the
shared interaction policy and target helper from reintroducing raw geometry/motion literals;
they must reference the runtime token objects. These are source-contract gates, not proof of
Figma sync or pixel parity on every platform.

`python3 scripts/export-design-tokens.py` produces a deterministic handoff package under
`build/reports/tokens/`: a normalized JSON suitable for Token Studio ingestion, CSS custom-property
aliases for Web adapters, a typed Figma-variable interchange JSON, and a manifest containing the
source SHA-256, mode list, and token count. The Figma artifact is deliberately marked
`nativeFigmaExport: false`: it contains stable review IDs and semantic aliases for an import adapter,
not file-specific IDs from a real Figma document. Dimension values are emitted as valid CSS `px`
custom properties using the documented 1dp baseline; native adapters continue to consume the
original JSON `dp` values.

Run `python3 scripts/verify-design-handoff.py` before handing the package to a designer. The gate
re-exports twice into isolated directories and fails on a source-hash mismatch, duplicate token
path/name, missing mode value, unsupported variable type, unresolved external alias declaration,
missing CSS property, or byte-level nondeterminism. This makes a designer review diffable and
repeatable without claiming that the package has already been imported into Figma.
The 2026-08-21 Linux run exported 25 tokens across four modes. The package is uploaded by CI, but it
does not claim that a real Figma file is synchronized; the Figma import/export diff and design-owner
approval remain a separate adoption gate. The current verifier proves the repository-side handoff
contract only; it cannot produce a Figma file, Figma Code Connect mapping, or designer approval on
Linux.

# Headless model / layout / renderer contract

Stylish-UI now exposes one framework-neutral boundary for controlled state and native rendering:

```text
host store ──actions──► StylishReducer ──immutable state──► headless layout engine
                                                              │
                                                              ▼
                                                       StylishRenderPlan
                                                              │
                                     Compose / Web / SwiftUI / Desktop renderer
```

The canonical contract lives in
`foundation/src/commonMain/kotlin/com/segnities007/stylishui/foundation/headless/StylishHeadless.kt` and
contains no Compose, Android View, UIKit, or DOM dependency. The root artifact temporarily carries
a binary-compatibility copy at `src/commonMain/.../foundation/headless/StylishHeadless.kt`; this is
intentional and guarded, so existing imports do not disappear while consumers migrate to the
physical `:foundation` artifact. The duplicate is scheduled for removal only in a major release.

## Contract

- `StylishReducer<S, A>` is the replayable state transition boundary. A host owns persistence,
  lifecycle, effects, and cancellation; the component reducer only returns a new immutable state.
- `StylishViewport` is the normalized host viewport. Layout engines receive pixels and a shared
  LTR/RTL direction, so a native renderer can convert from its own density without changing the
  component model.
- `StylishLayoutEngine<I>` converts a model input into deterministic geometry and semantics.
- `StylishRenderPlan` and `StylishRenderNode` carry stable identity, bounds, role, label, state
  description, selection, enabled state, and child identity. They contain no platform view type.
- `StylishRenderer<T>` is the final host boundary. Each platform maps a plan into its native
  primitives while preserving node identity and semantic state.

The first production integration is `StylishTreeLayoutEngine`. It consumes visible tree rows and
returns a plan with a root `Tree` node, `TreeItem` rows, expanded/collapsed state, selected state,
stable ids, focus identity, and deterministic LTR/RTL indentation. The existing Compose
`StylishTree` remains the reference renderer, so this contract is additive and source-compatible.

The same reducer contract is now exposed by:

- `StylishTreeStateReducer`
- `StylishTransferStateReducer`
- `StylishDataTableStateReducer`
- `StylishChartStateReducer`

Their existing `state.reduce(action)` extensions delegate to those shared reducers, preserving
source compatibility while allowing a SwiftUI/Web/Desktop host store to use the same transition
object.

## Acceptance and limits

`checkHeadlessArchitecture` and `scripts/verify-headless-architecture.py` enforce the dependency
boundary. `StylishHeadlessArchitectureTest` proves reducer replay, invalid viewport normalization,
rectangle hit testing, tree plan semantics, focus identity, and RTL layout. These checks prove the
common contract; they do not certify VoiceOver/TalkBack, browser screen readers, native gesture
feel, or device-specific typography. Those remain platform acceptance gates in
`docs/accessibility-contract.md` and `docs/release-evidence.md`.

Physical Gradle extraction into `foundation`, `structure`, and `components` artifacts is still
tracked separately. The current one-library publication remains guarded by the virtual import
boundary and the new Compose-free contract, avoiding a high-risk ABI-breaking split before native
consumer artifacts and migration tooling exist.

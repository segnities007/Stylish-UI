# Accessibility contract

Stylish-UI's automated accessibility contract is intentionally split from native
screen-reader certification. The JVM Compose UI smoke suite verifies the
semantics tree and layout invariants that can be reproduced on Linux; it does
not claim to emulate TalkBack, VoiceOver, keyboard focus traversal in a native
window, or browser assistive technology.

## Linux-verifiable invariants

- RTL composition, a high-contrast theme, and a 200% density/font-scale smoke case keep primary
  controls discoverable.
- Long localized labels retain a visible node, click action, checkbox role,
  selected state, state description, and a `RequestFocus` semantics action.
- Transfer rows apply `stylishInteractiveTarget()` in addition to `focusable()`
  and `clickable()`, preserving the 48 dp minimum target contract for custom
  content. Each pane exposes collection/collection-item semantics, and the
  shared roving-focus policy covers Up/Down/Home/End/Enter/Space while the
  focused key is restored when a virtualized row is recomposed.
- Tree rows expose selected/expanded state and a controlled `focusedId`; the
  renderer restores that id through a `FocusRequester` after expansion. Menu
  bars expose expanded/selected state, handle Escape and directional keys, and
  restore the top-level menu button after popup dismissal. Command palettes
  place initial focus in the query field, skip disabled commands during arrow
  navigation, and expose selected command semantics.
- QR output exposes a localized content description and its matrix dimensions as
  `stateDescription`.
- Charts and navigation retain their content descriptions and selected state in
  RTL.
- Splitter resize handles are focusable, expose a bounded progress range and
  `SetProgress` semantics action, announce the current percentage, and respond
  to directional/Home/End keys. The vertical direction uses a `Column` so
  ratios divide height rather than accidentally dividing width.

These invariants are covered by
`src/jvmTest/kotlin/com/segnities007/stylishui/components/AccessibilityLayoutSmokeTest.kt`.
The source-level regression gate is
`scripts/verify-accessibility-contract.py`; Android API 35 UIAutomator is a
separate executable evidence recipe (`scripts/verify-android-runtime.sh`).

## Motion policy (reduced-motion)

Every shared animation source is required to read `isStylishReducedMotionEnabled()` (or an
equivalent `reducedMotion` value). Visibility transitions in FAB, extended FAB, header, and
footer snap to a zero-duration fade when the policy is enabled; charts, popovers, accordions,
toasts, skeletons, and selection transitions use the same policy for their duration or spec.
`scripts/verify-motion-contract.sh` enforces this source-level invariant. It does not claim that
an Android, iOS, desktop, or browser operating-system setting has been exercised on a device.

The deterministic visual-resilience supplement is covered by
`src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt`.
It checks the 320dp high-contrast on/off, 320dp RTL/LTR and 100%/200% font-scale combinations, long copy,
and default/loading/disabled/error/empty states through image dimensions and
stable pixel-structure assertions. It is a Linux test contract; it does not replace
native screen-reader evidence.

## Explicitly not certified here

Native TalkBack/VoiceOver announcements, rotor behavior, Dynamic Type text
reflow on iOS, Android accessibility service output, browser screen-reader
output, and hardware keyboard focus order require platform-specific devices or
automation. They remain adoption evidence items until tested on the target
platforms and recorded separately.

# Stylish UI design harness

## Contract

Stylish UI is the application-facing design system. Compose Runtime, UI and Foundation
remain the execution framework, and Material3 remains an internal implementation dependency.
No new renderer, binary-compatible Material3 fork, or universal Material3 API parity is claimed.

Two API tiers are deliberate:

| Tier | Allowed application code | Guarantee |
|---|---|---|
| Constrained screen | `StylishScreen`, its document/state/event types, theme and runtime state | Closed element vocabulary; rendering belongs to the library |
| Flexible components | Stylish components, theme, Compose layout/graphics/resources | No direct Material UI imports; custom slot semantics remain the host's responsibility |

A typealias is a source migration convenience for existing color/state parameters, not an
independent implementation. It preserves existing binary signatures. Applications that must
never name a Material type can import the corresponding `Stylish*` alias from `components.models`.
No published API was removed.

## Constrained screen

`StylishScreenDocument` validates nonblank unique identities and visible labels. Its elements
are Text, Entry, Action, Input and Toggle. The host cannot supply a renderer, custom modifier,
color or animation through this API. Lists retain stable keys. Input and Toggle are controlled:
the host updates its state in response to Edit/Toggle events. Activate carries the action ID.
Error retry emits Retry. Empty content documents render the standard localized empty state.

The screen composes the existing ModernScreen and Header implementation. It owns header
clearance, system-bar clearance and the keyboard viewport. Actions are scrollable document
elements, not overlaid floating surfaces. The strict API therefore cannot occlude the last
item with a host-supplied FAB. Android still requires the Activity to enable edge-to-edge.

The host owns navigation, storage, permission requests, localization data and business
validation. This API does not itself save a form or perform a network request.

## Flexible Material-free UI

| Material application usage | Stylish entry |
|---|---|
| MaterialTheme | StylishTheme; colorScheme, typography, dimensions, shapes, animation |
| Text / Icon / Surface | StylishText / StylishIcon / StylishSurface |
| Image / Row / Column / Box | StylishImage / StylishRow / StylishColumn / StylishBox |
| ListItem | StylishListItem |
| Button / Card / Chip | Existing StylishButton / StylishCard / StylishChip |
| TextField / OutlinedTextField | Existing StylishFilledTextField / StylishOutlinedTextField |
| Checkbox / Switch / RadioButton / Slider | Existing Stylish controls |
| Dialog / Sheet / Snackbar | Existing StylishAlertDialog / StylishBottomSheet / StylishSnackbarHost |
| Drawer / top bar / navigation | Existing Stylish navigation and app-bar components |
| State and color parameter types | Stylish-prefixed compatibility aliases in components.models |
| State factories | rememberStylishSnackbarHostState / DrawerState / ModalBottomSheetState / DatePickerState / TimePickerState |

This mapping is a family inventory, not proof of every Material overload or interaction.
For custom surface colors, pass a matching content color when the color is not a theme role.
StylishText inherits the enclosing content color by default, so button/surface colors propagate.

## Executable source policy

```bash
python3 scripts/verify-design-harness.py path/to/constrained/screens
python3 scripts/verify-design-harness.py --profile material-free path/to/flexible/ui
python3 scripts/test_design_harness.py
```

The screen profile allows named screen/model/theme imports and Kotlin/Compose Runtime.
The material-free profile additionally allows Stylish components, Foundation/UI and Compose
resources. Wildcard imports and unknown external rendering imports fail. Fully qualified
Material references and aliased Material imports fail. An empty source set fails; the screen
profile also requires at least one real StylishScreen invocation.

The checker is an architecture aid, not a security sandbox or full Kotlin symbol resolver.
It does not prove behavior of local functions, reflection, generated code outside the scanned
roots, or all resolved transitive calls. Apply it to the entire designated UI source set and
also compile/test that consumer. The closed screen model provides the stronger API guarantee.

The default checked consumer is catalog's `harness/SettingsScreen.kt`; it compiles as part of
the catalog and is interactive in the gallery. Root Gradle `check` runs the consumer policy and
positive/negative checker tests. CI must run these checks even when platform setup fails.

## Verification and remaining coverage

`DesignHarnessTest` exercises model rejection/snapshotting, light/dark controlled input,
toggle/action dispatch, disabled/loading action semantics, error retry and content transition,
and rendered text inheriting its surface content color.
These are JVM Compose interaction tests, not physical-device verification.

Local verification on 2026-09-09 (Linux):

- `GRADLE_USER_HOME=$PWD/.gradle-local ./gradlew check apiCheck assemble
  --no-daemon --max-workers=1 --console=plain` passed in 4m 46s:
  477 actionable tasks (149 executed, 328 up-to-date).
- `apiDump` and the seven `DesignHarnessTest` tests passed.
- The full root JVM suite passed: 221 tests, no failures, errors or skips.
- `wasmJsBrowserTest` passed: 107 common tests in Chrome Headless, no failures,
  errors or skips. This is not browser interaction coverage of the new screen renderer.
- Source-policy checks for both consumer profiles and five checker regression tests passed.
- Architecture, module boundaries, component contracts, composable size and catalog
  inventory checks passed. The catalog report still lists 11 visual APIs without demos;
  its structural PASS does not mean complete visual/state coverage.
- JVM API compatibility checks passed; KLIB API checks and iOS simulator execution
  were skipped on this host and are not claimed as verified.

The strict vocabulary currently does not cover navigation destinations, modal stacks,
image/media content, date/time controls, charts or arbitrary grids. Those remain available
through flexible components and are not declared constrained-screen equivalents yet.
IME/inset behavior, TalkBack/VoiceOver, 200% font scale and browser input need platform runs.
The prior architectural audit's root/physical-module migration and legacy function-size
checker issues are not resolved by this application-facing API.

Do not mark a new element family complete merely because its name exists: add its validated
model, exhaustive renderer, state/event tests, consumer example and applicable platform evidence.

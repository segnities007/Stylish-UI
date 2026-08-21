# Platform adapter examples

This page is the copy-ready D-7.3 sample for Navigation, Flow/state, image loading,
file picking/upload boundaries, and QR encoding. The common contract is implemented in
[`samples/adapters`](../samples/adapters). It intentionally contains no Android, iOS,
Desktop, browser, network, or decoder dependency: the host owns those APIs and only maps
their result to `StylishAdapterResult`.

The examples below are integration shapes, not claims that an OS picker or a screen reader
has been executed on Linux. Every asynchronous adapter must preserve four outcomes:
success, classified failure, user cancellation, and permission denial where the platform
requires permission.

## Common lifecycle

```text
host source / OS API
        │
        ▼
platform adapter (permission + lifecycle + decoding + transport)
        │  StylishAdapterResult<T>
        ▼
immutable screen state
        │
        ▼
Stylish component (controlled rendering + intent callbacks only)
```

Never launch an adapter from a composable body. Start it from a host event or an explicitly
lifecycle-bound effect, cancel it with the host lifecycle, and redact paths, URLs, tokens,
and file contents from logs.

## Navigation

`StylishNavigationReducer` is a deterministic model adapter. Android Navigation Compose,
SwiftUI `NavigationStack`, a desktop router, or a Web history adapter can use the same
intent/state contract while retaining ownership of deep links, saved state, back gestures,
and analytics.

```kotlin
val next = StylishNavigationReducer.dispatch(
    state = screen.navigation,
    intent = StylishNavigationIntent.Navigate(route = "settings"),
)
when (next) {
    is StylishAdapterResult.Success -> viewModel.setNavigation(next.value)
    is StylishAdapterResult.Failure -> viewModel.showError(next.error)
    StylishAdapterResult.Cancelled -> Unit
}
```

The current route is derived from the host back stack. Do not optimistically toggle
`selected` in the component before navigation succeeds. Do not put query tokens or private
route parameters in a content description.

## Flow / observable state

The sample's `StylishFlowAdapter` takes a `kotlinx.coroutines.flow.Flow`, emits `Loading`,
`Content`, `Empty`, or classified `Error`, and returns a cancellable `Job`. A host should
provide a lifecycle-bound scope (`viewModelScope`, `lifecycleScope`, or an equivalent
Desktop/Web/Apple owner):

```kotlin
private fun observeState() {
    StylishFlowAdapter(repository.items).collect(viewModelScope) { state ->
        // Map to the screen's immutable state. The component never collects Flow itself.
        viewModel.setItemsState(state)
    }
}
```

Cancellation is rethrown as `CancellationException`, so a disposed scope does not turn a
normal cancellation into an error banner. A second subscription is a host bug; retain one
`Job` per screen owner and cancel it before replacing it. For stale cache data, map the
first emission to `StylishStreamState.Content(value, stale = true)` in the host.

## Image loading

The image adapter returns only validated, portable metadata. A platform decoder owns bytes,
memory pressure, cache eviction, and cancellation. The `source` value is an opaque stable
cache key, not a loggable credential-bearing URL.

```kotlin
val result = imageAdapter.load(StylishImageRequest(source = avatar.id, contentDescription = avatar.name))
when (result) {
    is StylishAdapterResult.Success -> imageState = ImageState.Ready(result.value)
    is StylishAdapterResult.Failure -> imageState = ImageState.Error(result.error)
    StylishAdapterResult.Cancelled -> imageState = ImageState.Placeholder
}
```

Android can map Coil/`ImageDecoder`, iOS can map `CGImage`/`AsyncImage`, Desktop can map its
image loader, and Web can map `ImageBitmap`/`createImageBitmap`. None of those platform
types should cross the common UI API.

## File picker and upload

Permission is an explicit input to the picker contract. The host requests permission first,
then launches the picker only after `Granted`. Android normally maps Storage Access Framework,
iOS maps `UIDocumentPickerViewController`, Desktop maps its native chooser, and Web maps
`showOpenFilePicker` or drag-and-drop.

```kotlin
val request = StylishFilePickerRequest(
    mimeTypes = listOf("image/*", "application/pdf"),
    allowMultiple = true,
    permission = permissionState,
)
when (val result = filePicker.pick(request)) {
    is StylishAdapterResult.Success -> uploadStore.replaceFiles(result.value)
    is StylishAdapterResult.Failure -> uploadStore.showError(result.error)
    StylishAdapterResult.Cancelled -> uploadStore.dismissPicker()
}
```

`validatePickedFiles` rejects missing stable IDs, invalid sizes, and multiple files when
`allowMultiple` is false; duplicate IDs are collapsed deterministically. Upload progress,
retry, and transport cancellation belong to the host store and are fed into
`StylishTransfer`/`StylishUpload` as controlled state. The UI must never read a local path,
open a stream, or upload a file by itself.

## QR encoding

The QR component renders a boolean matrix, while the host chooses the QR implementation and
error-correction policy:

```kotlin
when (val result = qrEncoder.encode(StylishQrRequest(payload = deepLink))) {
    is StylishAdapterResult.Success -> StylishQrCode(matrix = result.value.rows())
    is StylishAdapterResult.Failure -> StylishResult(title = "QR unavailable")
    StylishAdapterResult.Cancelled -> Unit
}
```

`validateQrResult` enforces a non-empty payload, supported error correction (`L`, `M`, `Q`,
or `H`), and a square matrix. Native camera scanning, clipboard/share permission, and image
export are separate adapters and must not be inferred from this encoder contract.

## Host mapping table

| Concern | Android | iOS / SwiftUI | Desktop | Web | Common contract |
|---|---|---|---|---|---|
| Navigation | Navigation Compose / SavedState | `NavigationStack` / scene state | router/history | History API / router | `StylishNavigationState` + intents |
| Observable state | `Flow` + lifecycle scope | `AsyncSequence`/observable bridge | coroutine scope | store subscription | `StylishStreamState` |
| Image | Coil / `ImageDecoder` | `CGImage` / `AsyncImage` | desktop decoder | `createImageBitmap` | `StylishImageAdapter` |
| Files | Storage Access Framework | document picker | native chooser | File API/drop | `StylishFilePickerAdapter` |
| QR | host QR encoder | host QR encoder | host QR encoder | host QR encoder | `StylishQrEncoderAdapter` |

The host adapter is the correct place for platform permission dialogs, lifecycle disposal,
network auth, retry/backoff, and exception mapping. The common contract proves the state and
failure taxonomy; it does not prove that every OS implementation or physical assistive
technology has been run.

## Contract test matrix

The Linux JVM test `StylishAdapterContractTest` covers the deterministic part of every
adapter:

| Adapter | Success | Failure | Cancellation | Permission boundary |
|---|---:|---:|---:|---:|
| Navigation | yes | invalid route | no-op/back policy | n/a |
| Flow | content/empty | transport exception | `CancellationException` is not error | n/a |
| Image | validated metadata | decode/invalid metadata | result passthrough | n/a |
| File picker | stable identity/dedup | invalid metadata | explicit cancelled result | denied/restricted/unknown |
| QR | square matrix | invalid payload/matrix | explicit cancelled result | host share/camera is separate |

OS-level picker permission dialogs, VoiceOver/TalkBack, native decoder behavior, and real
network transports require platform-hosted evidence and are deliberately not counted as
Linux proof.

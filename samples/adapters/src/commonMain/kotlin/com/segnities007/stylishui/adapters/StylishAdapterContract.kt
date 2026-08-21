package com.segnities007.stylishui.adapters

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/** Stable error categories shared by every host adapter. */
public enum class StylishAdapterErrorCode {
    /** The host has not granted the permission required for the operation. */
    PermissionDenied,

    /** The request or returned payload failed the cross-platform contract. */
    InvalidRequest,

    /** A decoder could not turn the source into a usable image or QR matrix. */
    DecodeFailed,

    /** A remote or local transport failed and may be retried. */
    Transport,

    /** The host does not support the requested operation. */
    Unsupported,

    /** An error that cannot be classified at the adapter boundary. */
    Unknown,
}

/** A redacted, machine-classifiable adapter error. Do not put paths, tokens, or credentials in [message]. */
public data class StylishAdapterError(
    public val code: StylishAdapterErrorCode,
    public val message: String,
    public val retryable: Boolean = false,
)

/** The explicit outcome of a host-owned asynchronous operation. */
public sealed interface StylishAdapterResult<out T> {
    /** The operation completed successfully. */
    public data class Success<T>(public val value: T) : StylishAdapterResult<T>

    /** The operation failed without leaking platform-specific exceptions to the UI. */
    public data class Failure(public val error: StylishAdapterError) : StylishAdapterResult<Nothing>

    /** The user or host cancelled the operation; cancellation is not an error. */
    public data object Cancelled : StylishAdapterResult<Nothing>
}

/** Permission state supplied by the host before a picker, camera, or share operation starts. */
public enum class StylishPermissionStatus {
    Unknown,
    Granted,
    Denied,
    Restricted,
}

/** A navigation route and back-stack state owned by the host router. */
public data class StylishNavigationState(
    public val currentRoute: String,
    public val backStack: List<String> = listOf(currentRoute),
    public val restored: Boolean = false,
)

/** User intent emitted by a Stylish navigation component. */
public sealed interface StylishNavigationIntent {
    /** Navigate to a route selected by the user. */
    public data class Navigate(public val route: String) : StylishNavigationIntent

    /** Handle a host back action. */
    public data object Back : StylishNavigationIntent

    /** Re-select an already selected destination without changing the route. */
    public data class Reselect(public val route: String) : StylishNavigationIntent

    /** Apply a deep link; the host may replace the existing stack. */
    public data class DeepLink(public val route: String) : StylishNavigationIntent

    /** Restore a previously persisted stack after process or scene recreation. */
    public data class Restore(public val backStack: List<String>) : StylishNavigationIntent
}

/**
 * A pure navigation adapter example. Real Android/iOS/Web routers can translate the returned
 * state into their own back-stack APIs; Stylish never owns a platform navigator.
 */
public fun interface StylishNavigationAdapter {
    /** Applies one intent to [state] without mutating the caller's state. */
    public fun dispatch(
        state: StylishNavigationState,
        intent: StylishNavigationIntent,
    ): StylishAdapterResult<StylishNavigationState>
}

/** Deterministic navigation implementation used by samples and contract tests. */
public object StylishNavigationReducer : StylishNavigationAdapter {
    override fun dispatch(
        state: StylishNavigationState,
        intent: StylishNavigationIntent,
    ): StylishAdapterResult<StylishNavigationState> {
        val current = normalizeNavigationState(state)
        if (current is StylishAdapterResult.Failure) return current
        val validState = (current as StylishAdapterResult.Success).value
        return when (intent) {
            StylishNavigationIntent.Back -> {
                if (validState.backStack.size <= 1) {
                    StylishAdapterResult.Success(validState)
                } else {
                    val stack = validState.backStack.dropLast(1)
                    StylishAdapterResult.Success(
                        validState.copy(currentRoute = stack.last(), backStack = stack, restored = false),
                    )
                }
            }
            is StylishNavigationIntent.Navigate ->
                navigate(validState, intent.route, replaceStack = false)
            is StylishNavigationIntent.Reselect ->
                if (intent.route == validState.currentRoute) {
                    StylishAdapterResult.Success(validState.copy(restored = false))
                } else {
                    navigate(validState, intent.route, replaceStack = false)
                }
            is StylishNavigationIntent.DeepLink -> navigate(validState, intent.route, replaceStack = true)
            is StylishNavigationIntent.Restore -> restore(validState, intent.backStack)
        }
    }
}

private fun navigate(
    state: StylishNavigationState,
    route: String,
    replaceStack: Boolean,
): StylishAdapterResult<StylishNavigationState> {
    val normalizedRoute = route.trim()
    if (normalizedRoute.isBlank()) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "route must not be blank"),
        )
    }
    val stack = if (replaceStack) listOf(normalizedRoute) else state.backStack + normalizedRoute
    return StylishAdapterResult.Success(
        state.copy(currentRoute = normalizedRoute, backStack = stack, restored = false),
    )
}

private fun restore(
    state: StylishNavigationState,
    backStack: List<String>,
): StylishAdapterResult<StylishNavigationState> {
    val stack = backStack.map(String::trim).filter(String::isNotBlank)
    if (stack.isEmpty()) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "restored back stack must not be empty"),
        )
    }
    return StylishAdapterResult.Success(
        state.copy(currentRoute = stack.last(), backStack = stack, restored = true),
    )
}

private fun normalizeNavigationState(state: StylishNavigationState): StylishAdapterResult<StylishNavigationState> {
    val stack = state.backStack.map(String::trim).filter(String::isNotBlank)
    if (stack.isEmpty() || state.currentRoute.trim().isBlank()) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "navigation state must contain a route"),
        )
    }
    return StylishAdapterResult.Success(
        state.copy(currentRoute = stack.last(), backStack = stack),
    )
}

/** Explicit loading/content/empty/error state emitted by [StylishFlowAdapter]. */
public sealed interface StylishStreamState<out T> {
    /** The subscription has started and no value has arrived yet. */
    public data object Loading : StylishStreamState<Nothing>

    /** A value from the source; [stale] identifies a cached value. */
    public data class Content<T>(public val value: T, public val stale: Boolean = false) : StylishStreamState<T>

    /** The source completed without emitting a value. */
    public data class Empty(public val message: String? = null) : StylishStreamState<Nothing>

    /** The source failed; [retryable] is a host policy hint. */
    public data class Error(public val error: StylishAdapterError) : StylishStreamState<Nothing>
}

/**
 * Lifecycle-neutral Flow bridge. The host supplies a lifecycle-bound [CoroutineScope] and owns
 * cancellation by cancelling the returned [Job]. The adapter emits loading before collection and
 * converts non-cancellation exceptions into a classified error.
 */
public class StylishFlowAdapter<T>(private val flow: Flow<T>) {
    /** Starts one subscription and returns its idempotently cancellable job. */
    public fun collect(
        scope: CoroutineScope,
        onState: (StylishStreamState<T>) -> Unit,
    ): Job = scope.launch {
        onState(StylishStreamState.Loading)
        var emitted = false
        var failed = false
        flow
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                failed = true
                onState(
                    StylishStreamState.Error(
                        StylishAdapterError(
                            code = StylishAdapterErrorCode.Transport,
                            message = throwable.message ?: "state source failed",
                            retryable = true,
                        ),
                    ),
                )
            }
            .collect { value ->
                emitted = true
                onState(StylishStreamState.Content(value))
            }
        if (!emitted && !failed) onState(StylishStreamState.Empty())
    }
}

/** A host-neutral image request; byte decoding and caching stay in the host adapter. */
public data class StylishImageRequest(
    public val source: String,
    public val contentDescription: String? = null,
)

/** Metadata returned after a host decoder validates an image. */
public data class StylishImageResource(
    public val key: String,
    public val mimeType: String,
    public val widthPx: Int,
    public val heightPx: Int,
)

/** Host-owned image decoder/cache boundary. */
public fun interface StylishImageAdapter {
    /** Loads and validates [request], without exposing platform decoder types to Stylish. */
    public suspend fun load(request: StylishImageRequest): StylishAdapterResult<StylishImageResource>
}

/** Returns a classified validation failure for malformed image metadata. */
public fun validateImageResource(resource: StylishImageResource): StylishAdapterResult<StylishImageResource> {
    if (resource.key.isBlank() || resource.mimeType.isBlank() || resource.widthPx <= 0 || resource.heightPx <= 0) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "image metadata is incomplete"),
        )
    }
    return StylishAdapterResult.Success(resource)
}

/** File picker request with the permission decision made by the host. */
public data class StylishFilePickerRequest(
    public val mimeTypes: List<String> = listOf("*/*"),
    public val allowMultiple: Boolean = true,
    public val permission: StylishPermissionStatus = StylishPermissionStatus.Unknown,
)

/** A validated, stable file identity returned by a host picker. */
public data class StylishPickedFile(
    public val id: String,
    public val displayName: String,
    public val mimeType: String,
    public val sizeBytes: Long,
)

/** Android SAF, iOS document picker, Desktop chooser, or Web drop adapter boundary. */
public fun interface StylishFilePickerAdapter {
    /** Starts a picker only after the host has resolved permission. */
    public suspend fun pick(request: StylishFilePickerRequest): StylishAdapterResult<List<StylishPickedFile>>
}

/** Validates the permission and identity boundary before a picker result reaches UI state. */
public fun validatePickedFiles(
    request: StylishFilePickerRequest,
    files: List<StylishPickedFile>,
): StylishAdapterResult<List<StylishPickedFile>> {
    if (request.permission != StylishPermissionStatus.Granted) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(
                StylishAdapterErrorCode.PermissionDenied,
                "file permission was not granted",
                retryable = true,
            ),
        )
    }
    if (!request.allowMultiple && files.size > 1) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "multiple files are not allowed"),
        )
    }
    val invalid = files.firstOrNull {
        it.id.isBlank() || it.displayName.isBlank() || it.mimeType.isBlank() || it.sizeBytes < 0
    }
    if (invalid != null) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "file identity or metadata is invalid"),
        )
    }
    val deduplicated = files.distinctBy(StylishPickedFile::id)
    return StylishAdapterResult.Success(deduplicated)
}

/** QR encoding request. The encoder, not the UI, owns QR standard/error-correction details. */
public data class StylishQrRequest(
    public val payload: String,
    public val errorCorrection: Char = 'M',
)

/** A square QR module matrix in row-major order. */
public data class StylishQrMatrix(
    public val size: Int,
    public val modules: List<Boolean>,
) {
    init {
        require(size > 0) { "size must be positive" }
        require(modules.size == size * size) { "modules must contain size × size values" }
    }

    /** Returns the dark/light module at [row] and [column]. */
    public operator fun get(row: Int, column: Int): Boolean = modules[row * size + column]

    /** Converts the flat matrix to the rows expected by [StylishQrCode]. */
    public fun rows(): List<List<Boolean>> = modules.chunked(size)
}

/** Host encoder boundary for Android/iOS/Desktop/Web QR libraries. */
public fun interface StylishQrEncoderAdapter {
    /** Encodes [request] or returns a classified failure. */
    public suspend fun encode(request: StylishQrRequest): StylishAdapterResult<StylishQrMatrix>
}

/** Validates an encoder result without claiming a platform QR implementation. */
public fun validateQrResult(
    request: StylishQrRequest,
    result: StylishQrMatrix,
): StylishAdapterResult<StylishQrMatrix> {
    if (request.payload.isBlank() || request.errorCorrection !in setOf('L', 'M', 'Q', 'H')) {
        return StylishAdapterResult.Failure(
            StylishAdapterError(StylishAdapterErrorCode.InvalidRequest, "QR request is invalid"),
        )
    }
    return runCatching { result.size; StylishAdapterResult.Success(result) }
        .getOrElse {
            StylishAdapterResult.Failure(
                StylishAdapterError(StylishAdapterErrorCode.DecodeFailed, "QR matrix is invalid"),
            )
        }
}

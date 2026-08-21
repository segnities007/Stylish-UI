package com.segnities007.stylishui.components.models

import androidx.compose.runtime.Immutable

/**
 * The shared state machine for data-backed Stylish components.
 *
 * Components should model loading, empty, error, and content as explicit states instead of
 * scattering nullable values and booleans across their APIs. The model is UI-framework agnostic,
 * so a Flow, SwiftUI observable object, or web state store can own it and map it into the same
 * rendering contract.
 */
@Immutable
public sealed interface StylishContentState<out T> {
    /** A request is in progress and content is not yet available. */
    @Immutable
    public data object Loading : StylishContentState<Nothing>

    /** The request completed successfully but contains no items. */
    @Immutable
    public data class Empty(public val message: String? = null) : StylishContentState<Nothing>

    /** The request failed and may optionally expose a retry action to the host. */
    @Immutable
    public data class Error(
        public val message: String,
        public val retryLabel: String? = null,
    ) : StylishContentState<Nothing>

    /** The request completed successfully with a value. */
    @Immutable
    public data class Content<T>(public val value: T) : StylishContentState<T>
}

/** Maps only the content branch while preserving the state contract. */
public inline fun <T, R> StylishContentState<T>.mapContent(transform: (T) -> R): StylishContentState<R> =
    when (this) {
        StylishContentState.Loading -> StylishContentState.Loading
        is StylishContentState.Empty -> this
        is StylishContentState.Error -> this
        is StylishContentState.Content -> StylishContentState.Content(transform(value))
    }

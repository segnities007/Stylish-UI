package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * Creates and remembers a [SwipeToDismissBoxState] for use with
 * [StylishSwipeToDismissBox].
 *
 * This is the Finish-layer wrapper around the Material 3
 * [rememberSwipeToDismissBoxState], exposing the non-deprecated signature
 * ([positionalThreshold] instead of the legacy `confirmValueChange` overload)
 * so callers never touch the deprecated M3 API surface. The settle animation
 * uses [StylishTheme.animation] tokens — [com.segnities007.stylishui.tokens.StylishAnimationTokens.durationMedium]
 * duration with [com.segnities007.stylishui.tokens.StylishAnimationTokens.defaultEasing] —
 * so the dismiss motion matches the rest of the Stylish motion language.
 *
 * @param initialValue The initial settled value of the state. Defaults to
 *   [SwipeToDismissBoxValue.Settled].
 * @param positionalThreshold The distance (as a fraction of the total swipe
 *   distance) after which the swipe settles into a dismissed or settled
 *   target. Defaults to
 *   [SwipeToDismissBoxDefaults.positionalThreshold] (56 dp).
 *
 * @see StylishSwipeToDismissBox
 * @see rememberStylishDismissState
 */
@Composable
public fun rememberStylishSwipeToDismissBoxState(
    initialValue: SwipeToDismissBoxValue = SwipeToDismissBoxValue.Settled,
    positionalThreshold: (totalDistance: Float) -> Float =
        SwipeToDismissBoxDefaults.positionalThreshold,
): SwipeToDismissBoxState {
    return rememberSwipeToDismissBoxState(
        initialValue = initialValue,
        positionalThreshold = positionalThreshold,
    )
}

/**
 * Convenience alias for [rememberStylishSwipeToDismissBoxState] with a
 * shorter name. Returns a [SwipeToDismissBoxState] whose settle animation
 * is configured with [StylishTheme.animation] tokens.
 *
 * Use [SwipeToDismissBoxState.isDismissed] to check whether the content
 * has been dismissed, [SwipeToDismissBoxState.dismissOffset] for the
 * current swipe offset in pixels, and [SwipeToDismissBoxState.progress]
 * / [SwipeToDismissBoxState.currentValue] for the animation progress
 * and settled direction.
 *
 * @param initialValue The initial settled value of the state. Defaults to
 *   [SwipeToDismissBoxValue.Settled].
 * @param positionalThreshold The distance (as a fraction of the total swipe
 *   distance) after which the swipe settles into a dismissed or settled
 *   target. Defaults to
 *   [SwipeToDismissBoxDefaults.positionalThreshold] (56 dp).
 *
 * @see rememberStylishSwipeToDismissBoxState
 * @see StylishSwipeToDismissBox
 */
@Composable
public fun rememberStylishDismissState(
    initialValue: SwipeToDismissBoxValue = SwipeToDismissBoxValue.Settled,
    positionalThreshold: (totalDistance: Float) -> Float =
        SwipeToDismissBoxDefaults.positionalThreshold,
): SwipeToDismissBoxState = rememberStylishSwipeToDismissBoxState(
    initialValue = initialValue,
    positionalThreshold = positionalThreshold,
)

/**
 * Whether this dismiss state has settled into a dismissed value
 * (either [SwipeToDismissBoxValue.StartToEnd] or
 * [SwipeToDismissBoxValue.EndToStart]).
 *
 * @see rememberStylishDismissState
 */
public val SwipeToDismissBoxState.isDismissed: Boolean
    get() = currentValue != SwipeToDismissBoxValue.Settled

/**
 * A swipe-to-dismiss container that reveals [backgroundContent] as the user
 * swipes [content] to either side, then settles or dismisses based on the
 * swipe distance.
 *
 * This is the Finish-layer wrapper around the Material 3 [SwipeToDismissBox]:
 * it forwards every parameter unchanged and keeps the M3 signature, so
 * callers can use the familiar `backgroundContent` / `content` slot contract
 * without touching experimental or deprecated M3 overloads. The component is
 * intentionally unstyled — the surface, shape, and colors of both the
 * background and the foreground content are owned by the caller, matching
 * Material's guidance that swipe-to-dismiss backgrounds are item-specific
 * (e.g. a red destructive background). Use it together with
 * [rememberStylishSwipeToDismissBoxState].
 *
 * @param state The [SwipeToDismissBoxState] driving this container. Create it
 *   with [rememberStylishSwipeToDismissBoxState].
 * @param backgroundContent A composable rendered behind [content], revealed
 *   as the content is swiped away. Receives [RowScope].
 * @param modifier Modifier applied to the container [Box] root.
 * @param enableDismissFromStartToEnd Whether swiping from start to end can
 *   dismiss. Defaults to `true`.
 * @param enableDismissFromEndToStart Whether swiping from end to start can
 *   dismiss. Defaults to `true`.
 * @param gesturesEnabled Whether pointer gestures are active. When `false`,
 *   the content cannot be swiped (e.g. while another gesture is in
 *   progress). Defaults to `true`.
 * @param onDismiss Called when the content settles into a dismissed state,
 *   receiving the dismissed direction. Defaults to no-op.
 * @param content The foreground content that can be swiped. Receives
 *   [RowScope].
 *
 * @see rememberStylishSwipeToDismissBoxState
 * @see SwipeToDismissBox
 */
@Composable
public fun StylishSwipeToDismissBox(
    state: SwipeToDismissBoxState,
    backgroundContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true,
    gesturesEnabled: Boolean = true,
    onDismiss: (SwipeToDismissBoxValue) -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    SwipeToDismissBox(
        state = state,
        backgroundContent = backgroundContent,
        modifier = modifier,
        enableDismissFromStartToEnd = enableDismissFromStartToEnd,
        enableDismissFromEndToStart = enableDismissFromEndToStart,
        gesturesEnabled = gesturesEnabled,
        onDismiss = onDismiss,
        content = content,
    )
}

@Preview(name = "Stylish swipe to dismiss box", showBackground = true, widthDp = 393)
@Composable
private fun StylishSwipeToDismissBoxPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val state = rememberStylishSwipeToDismissBoxState()
            StylishSwipeToDismissBox(
                state = state,
                backgroundContent = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.error)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError,
                        )
                    }
                },
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.stylishComponentColors.groupedContainer,
                ) {
                    Text(
                        "スワイプして削除",
                        Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A snackbar styled with the Stylish design language — rounded corners
 * from [StylishTheme.dimensions.connectedCornerRadius] and theme-aware
 * container/content colors.
 *
 * Use together with [StylishSnackbarHost] to display transient
 * messages at the bottom of the screen.
 *
 * @param snackbarData The data driving this snackbar's message,
 *   action label, and duration.
 * @param modifier Modifier applied to the [Snackbar] root.
 * @param shape Corner shape. Defaults to [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param containerColor Background color.
 * @param contentColor Text color for the message.
 * @param actionColor Text color for the action button.
 *
 * @see StylishSnackbarHost
 */
@Composable
public fun StylishSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    actionColor: Color = MaterialTheme.colorScheme.inversePrimary,
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier.stylishTestTag("snackbar").semantics { liveRegion = LiveRegionMode.Polite },
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionColor = actionColor,
    )
}

/**
 * A slot-based snackbar styled with the Stylish design language, for
 * composing the message and action content directly instead of driving the
 * snackbar from a [SnackbarData]. Carries the same styling as the
 * [SnackbarData] overload: rounded corners from
 * [StylishTheme.dimensions.connectedCornerRadius] and theme-aware
 * container/content colors.
 *
 * @param modifier Modifier applied to the [Snackbar] root.
 * @param action Optional composable shown as the snackbar's action area,
 *   typically a [TextButton]. When `null`, no action is displayed.
 * @param dismissAction Optional composable shown as the dismiss affordance,
 *   typically an icon button. When `null`, no dismiss affordance is
 *   displayed.
 * @param actionOnNewLine When `true`, [action] is placed on a separate line
 *   below the message. Defaults to `false`.
 * @param shape Corner shape. Defaults to [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param containerColor Background color.
 * @param contentColor Text color for the message.
 * @param actionColor Text color applied to [action].
 * @param content The snackbar message content.
 *
 * @see StylishSnackbarHost
 */
@Composable
public fun StylishSnackbar(
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    dismissAction: (@Composable () -> Unit)? = null,
    actionOnNewLine: Boolean = false,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    actionColor: Color = MaterialTheme.colorScheme.inversePrimary,
    content: @Composable () -> Unit,
) {
    Snackbar(
        modifier = modifier.stylishTestTag("snackbar_host").semantics { liveRegion = LiveRegionMode.Polite },
        action = action,
        dismissAction = dismissAction,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionColor,
        content = content,
    )
}

/**
 * A [SnackbarHost] that renders each snackbar with [StylishSnackbar].
 * Place this inside a
 * [com.segnities007.stylishui.components.patterns.StylishScaffold]'s
 * `snackbarHost` slot.
 *
 * @param hostState The [SnackbarHostState] that queues snackbar
 *   messages.
 * @param modifier Modifier applied to the host.
 * @param shape Corner shape forwarded to [StylishSnackbar].
 * @param containerColor Background color forwarded to [StylishSnackbar].
 * @param contentColor Content color forwarded to [StylishSnackbar].
 * @param actionColor Action color forwarded to [StylishSnackbar].
 *
 * @see StylishSnackbar
 */
@Composable
public fun StylishSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    actionColor: Color = MaterialTheme.colorScheme.inversePrimary,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.stylishTestTag("snackbar_action").semantics { liveRegion = LiveRegionMode.Polite },
    ) { data ->
        StylishSnackbar(
            snackbarData = data,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            actionColor = actionColor,
        )
    }
}

private class PreviewSnackbarVisuals(
    override val message: String = "保存しました",
    override val actionLabel: String? = "元に戻す",
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals

private class PreviewSnackbarData(
    override val visuals: SnackbarVisuals = PreviewSnackbarVisuals(),
) : SnackbarData {
    override fun performAction() {}
    override fun dismiss() {}
}

@Preview(name = "Stylish snackbar", showBackground = true, widthDp = 393)
@Composable
private fun StylishSnackbarPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishSnackbar(snackbarData = PreviewSnackbarData())
        }
    }
}

@Preview(name = "Stylish snackbar host", showBackground = true, widthDp = 393, heightDp = 240)
@Composable
private fun StylishSnackbarHostPreview() {
    StylishTheme(darkTheme = false) {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(PreviewSnackbarVisuals())
        }
        Surface(Modifier.padding(20.dp)) {
            StylishSnackbarHost(hostState = hostState)
        }
    }
}

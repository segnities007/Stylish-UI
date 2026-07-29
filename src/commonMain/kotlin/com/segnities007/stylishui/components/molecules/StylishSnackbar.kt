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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A snackbar styled with the Stylish design language — rounded corners
 * from [StylishTheme.dimensions.connectedCornerRadius], a hairline
 * outline, and theme-aware container/content colors.
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
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionColor = actionColor,
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
        modifier = modifier,
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
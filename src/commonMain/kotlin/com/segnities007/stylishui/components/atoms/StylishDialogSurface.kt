package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A modal dialog container that enters with a combined scale-and-fade
 * animation. Wraps content in a full-width [Card] inside a platform
 * [Dialog] whose properties default to
 * `DialogProperties(usePlatformDefaultWidth = false)`, so the surface
 * stretches to the available width minus [horizontalPadding].
 *
 * The entrance animation scales from 92 % to 100 % over
 * [StylishTheme.animation.durationShort] with a quadratic ease-in,
 * while alpha fades from 0 to 1 over the same duration. Set
 * [animate] to `false` to skip the animation entirely (e.g. during
 * UI tests or when the caller manages its own transition).
 *
 * Dismiss behaviour (back press, tap outside) is controlled through
 * [properties]: pass `DialogProperties(dismissOnBackPress = false)`
 * or `DialogProperties(dismissOnClickOutside = false)` to opt out of
 * either dismissal path. [onDismiss] is invoked for whichever
 * dismissals remain enabled.
 *
 * The card is padded by [windowInsets] (defaults to
 * [WindowInsets.safeDrawing]) so its content never collides with the
 * system bars; pass `WindowInsets(0)` to disable this behaviour.
 *
 * @param onDismiss Called when the user taps outside the dialog or
 *   presses the system back button (per [properties]).
 * @param modifier Modifier applied to the root [Card], before the
 *   built-in full-width, horizontal-padding, and entrance-animation
 *   modifiers.
 * @param animate When `true` (default), the dialog plays the
 *   scale-and-fade entrance animation. When `false`, the dialog
 *   appears immediately with no transition.
 * @param shape Shape of the card surface. Defaults to
 *   `RoundedCornerShape` with
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param containerColor Background color of the card. Defaults to
 *   `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param horizontalPadding Horizontal margin between the dialog edges
 *   and the screen edges. Defaults to 16 dp.
 * @param contentColor Default content color inside the card. When
 *   `null` (default), the color scheme's default content color is
 *   used.
 * @param properties The platform [DialogProperties]. Defaults to
 *   `DialogProperties(usePlatformDefaultWidth = false)` so the dialog
 *   spans the full available width. Use `dismissOnBackPress` and
 *   `dismissOnClickOutside` (constructor parameters of
 *   [DialogProperties]) to control dismissal.
 * @param windowInsets Insets padded around the card so its content
 *   stays clear of system bars. Defaults to [WindowInsets.safeDrawing].
 *   Pass `WindowInsets(0)` to disable.
 * @param content Content rendered inside the card's [ColumnScope].
 *   Callers are responsible for their own internal padding.
 */
@Composable
public fun StylishDialogSurface(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color? = null,
    horizontalPadding: Dp = 16.dp,
    contentColor: Color? = null,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    content: @Composable ColumnScope.() -> Unit,
) {
    val effectiveAnimate = animate && !isStylishReducedMotionEnabled()
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val dialogScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.92f,
        animationSpec = tween(
            durationMillis = StylishTheme.animation.durationShort,
            easing = { it * it },
        ),
        label = "dialogScale",
    )
    val dialogAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = StylishTheme.animation.durationShort),
        label = "dialogAlpha",
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = properties,
    ) {
        Card(
            modifier = modifier.stylishTestTag("dialog_surface")
                .windowInsetsPadding(windowInsets)
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .then(
                    if (effectiveAnimate) {
                        Modifier.graphicsLayer {
                            scaleX = dialogScale
                            scaleY = dialogScale
                            alpha = dialogAlpha
                        }
                    } else {
                        Modifier
                    },
                ),
            shape = shape,
            colors = if (contentColor != null) {
                CardDefaults.cardColors(
                    containerColor = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
                    contentColor = contentColor,
                )
            } else {
                CardDefaults.cardColors(
                    containerColor = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
                )
            },
            content = content,
        )
    }
}

@Preview(name = "Stylish dialog surface", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishDialogSurfacePreview() {
    StylishTheme(darkTheme = false) {
        StylishDialogSurface({}) {
            Text("ダイアログ", Modifier.padding(24.dp), style = MaterialTheme.typography.titleLarge)
        }
    }
}

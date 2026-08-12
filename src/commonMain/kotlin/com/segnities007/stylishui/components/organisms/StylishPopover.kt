package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import kotlin.math.roundToInt

/**
 * A popover — anchored floating content for arbitrary use (filters,
 * calendars, help text) — the MUI Popover / Radix Popover counterpart.
 *
 * Unlike [com.segnities007.stylishui.components.atoms.StylishDropdownMenu],
 * a popover is not menu-shaped, does not auto-close when its content is
 * tapped, and shows no scrim: it is dismissed only through
 * [onExpandedChange] (typically by tapping outside, which triggers the
 * popup's dismiss request). Content is laid out in a [ColumnScope] on a
 * rounded, outlined, elevated surface sized to [width], centered
 * horizontally under the anchor and placed [offset] below it.
 *
 * The anchor is rendered by this composable inside an internal [Box].
 * The popup enters with a short fade + scale animation
 * ([StylishTheme.animation.durationShort]) that snaps instead of tweening
 * when the platform requests reduced motion (see
 * [isStylishReducedMotionEnabled]).
 *
 * **Anchoring limitation:** the popup position is computed from the
 * anchor's bounds while the popup is positioned relative to its enclosing
 * box, so the anchor must not be nested inside transformed containers
 * (e.g. parents that change the coordinate space, such as `graphicsLayer`
 * scale/rotation or scroll-transformed layouts) or the popup will drift
 * off the anchor.
 *
 * ```kotlin
 * var expanded by remember { mutableStateOf(false) }
 * StylishPopover(
 *     expanded = expanded,
 *     onExpandedChange = { expanded = it },
 *     anchor = {
 *         Button(onClick = { expanded = !expanded }) {
 *             Text("フィルター")
 *         }
 *     },
 * ) {
 *     Text("カテゴリ", style = MaterialTheme.typography.titleSmall)
 *     Text("写真のみ表示", style = MaterialTheme.typography.bodyMedium)
 * }
 * ```
 *
 * ## Testing
 *
 * The popup surface carries the default test tag `stylish_popover` for
 * UI tests. Callers can override it by passing their own
 * `Modifier.testTag(...)` in [modifier].
 *
 * @param expanded Whether the popover is currently shown.
 * @param onExpandedChange Called with `false` when the user requests
 *   dismissal (e.g. tapping outside); the caller should also toggle
 *   [expanded] when the anchor is tapped.
 * @param modifier Modifier applied to the [Box] that wraps [anchor].
 * @param anchor The trigger composable the popover is anchored to,
 *   rendered inside the wrapping [Box].
 * @param shape Shape of the popup surface. Defaults to
 *   [RoundedCornerShape] with
 *   [DefaultStylishDimensions.connectedCornerRadius] (12 dp).
 * @param containerColor Background color of the popup surface. Defaults
 *   to `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param contentColor Default content color inside the popup. Defaults
 *   to `MaterialTheme.colorScheme.onSurface`.
 * @param contentPadding Padding around the content inside the popup.
 *   Defaults to the Stylish control paddings (16 dp horizontal, 12 dp
 *   vertical).
 * @param tonalElevation Tonal elevation of the popup surface. Defaults
 *   to [DefaultStylishDimensions.floatingElevation] (2 dp).
 * @param offset Vertical gap between the anchor's bottom edge and the
 *   popup's top edge. Defaults to 8 dp.
 * @param width Width of the popup surface. Defaults to 280 dp.
 * @param properties [PopupProperties] for further customization of the
 *   popup behavior. Defaults to a focusable popup.
 * @param content The popup content, laid out in a [ColumnScope].
 *
 * @see com.segnities007.stylishui.components.atoms.StylishDropdownMenu
 */
@Composable
public fun StylishPopover(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    anchor: @Composable () -> Unit,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = DefaultStylishDimensions.controlPadding,
        vertical = DefaultStylishDimensions.controlVerticalPadding,
    ),
    tonalElevation: Dp = DefaultStylishDimensions.floatingElevation,
    offset: Dp = 8.dp,
    width: Dp = 280.dp,
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    var anchorBounds by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val density = LocalDensity.current
    val popupWidthPx = with(density) { width.toPx() }
    val offsetPx = with(density) { offset.toPx() }
    val reducedMotion = isStylishReducedMotionEnabled()
    val animationDuration = StylishTheme.animation.durationShort
    val enterProgress = remember { Animatable(0f) }
    LaunchedEffect(expanded) {
        if (expanded) {
            enterProgress.animateTo(
                targetValue = 1f,
                animationSpec = if (reducedMotion) {
                    snap()
                } else {
                    tween(animationDuration)
                },
            )
        }
    }
    Box(
        modifier = modifier.onGloballyPositioned { anchorBounds = it },
    ) {
        anchor()
        anchorBounds?.let { bounds ->
            if (expanded) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(
                        x = ((bounds.size.width - popupWidthPx) / 2).roundToInt(),
                        y = (bounds.size.height + offsetPx).roundToInt(),
                    ),
                    onDismissRequest = { onExpandedChange(false) },
                    properties = properties,
                ) {
                    Surface(
                        modifier = Modifier
                            .width(width)
                            .testTag("stylish_popover")
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
                            .graphicsLayer {
                                val p = enterProgress.value
                                alpha = p
                                scaleX = if (reducedMotion) 1f else 0.95f + 0.05f * p
                                scaleY = if (reducedMotion) 1f else 0.95f + 0.05f * p
                            },
                        shape = shape,
                        color = containerColor,
                        contentColor = contentColor,
                        tonalElevation = tonalElevation,
                    ) {
                        Column(
                            modifier = Modifier.padding(contentPadding),
                            content = content,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Stylish popover", showBackground = true, widthDp = 393)
@Composable
private fun StylishPopoverPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            var expanded by remember { mutableStateOf(true) }
            var notifications by remember { mutableStateOf(true) }
            StylishPopover(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                anchor = {
                    Button(onClick = { expanded = !expanded }) {
                        Text("フィルター")
                    }
                },
            ) {
                Text("絞り込み条件", style = MaterialTheme.typography.titleSmall)
                Text(
                    "カテゴリと状態で表示を絞り込みます",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = StylishTheme.dimensions.itemSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "通知を受け取る",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Switch(
                        checked = notifications,
                        onCheckedChange = { notifications = it },
                    )
                }
            }
        }
    }
}

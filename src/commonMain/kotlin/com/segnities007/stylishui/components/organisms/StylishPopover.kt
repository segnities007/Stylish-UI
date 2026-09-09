package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
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
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.foundation.stylishFloatingEnterTransition
import com.segnities007.stylishui.foundation.stylishFloatingExitTransition
import com.segnities007.stylishui.theme.StylishElevationLevel
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.containerColor
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
 * The popup uses the standard Stylish floating fade + slide animation and
 * remains hosted until its exit transition completes.
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
 *   [StylishTheme.shapes.connectedCornerRadius] (12 dp).
 * @param containerColor Background color of the popup surface. Defaults
 *   to the [StylishElevationLevel.Raised] container color.
 * @param contentColor Default content color inside the popup. Defaults
 *   to `MaterialTheme.colorScheme.onSurface`.
 * @param contentPadding Padding around the content inside the popup.
 *   Defaults to the Stylish control paddings (16 dp horizontal, 12 dp
 *   vertical).
 * @param tonalElevation Tonal elevation of the popup surface. Defaults
 *   to [StylishTheme.dimensions.floatingElevation] (2 dp).
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
    shape: Shape = RoundedCornerShape(StylishTheme.shapes.connectedCornerRadius),
    containerColor: Color = StylishElevationLevel.Raised.containerColor(),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StylishTheme.dimensions.controlPadding,
        vertical = StylishTheme.dimensions.controlVerticalPadding,
    ),
    tonalElevation: Dp = StylishTheme.dimensions.floatingElevation,
    offset: Dp = 8.dp,
    width: Dp = 280.dp,
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    var anchorBounds by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val density = LocalDensity.current
    val popupWidthPx = with(density) { width.toPx() }
    val offsetPx = with(density) { offset.toPx() }
    val visibilityState = remember { MutableTransitionState(false) }
    LaunchedEffect(expanded) {
        visibilityState.targetState = expanded
    }
    val popupVisible = expanded || visibilityState.currentState || visibilityState.targetState
    Box(
        modifier = modifier.onGloballyPositioned { anchorBounds = it },
    ) {
        anchor()
        anchorBounds?.let { bounds ->
            if (popupVisible) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(
                        x = ((bounds.size.width - popupWidthPx) / 2).roundToInt(),
                        y = (bounds.size.height + offsetPx).roundToInt(),
                    ),
                    onDismissRequest = { onExpandedChange(false) },
                    properties = properties,
                ) {
                    AnimatedVisibility(
                        visibleState = visibilityState,
                        enter = stylishFloatingEnterTransition(StylishFloatingSlideDirection.Down),
                        exit = stylishFloatingExitTransition(StylishFloatingSlideDirection.Down),
                    ) {
                        Surface(
                            modifier = Modifier
                                .width(width)
                                .testTag("stylish_popover")
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape),
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

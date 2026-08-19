package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Placement of a [StylishTooltip] relative to its anchor.
 *
 * Controls where the tooltip popup appears in relation to the anchor
 * content that triggers it.
 *
 * @see StylishTooltip
 */
public enum class TooltipPlacement {
    /** Tooltip appears above the anchor, centered horizontally. */
    Top,

    /** Tooltip appears below the anchor, centered horizontally. */
    Bottom,

    /** Tooltip appears to the start (left in LTR) of the anchor, centered vertically. */
    Start,

    /** Tooltip appears to the end (right in LTR) of the anchor, centered vertically. */
    End,
}

/**
 * A tooltip that shows contextual information when the anchor is hovered
 * or focused — the Material Design 3 tooltip counterpart.
 *
 * The [content] is the anchor that triggers the tooltip. Hovering over
 * the anchor (on pointer devices) or focusing it (via keyboard navigation)
 * shows the tooltip after [delayMillis]. The tooltip is positioned
 * according to [placement] relative to the anchor and animated with a
 * fade transition that respects the platform's reduced-motion preference
 * (see [isStylishReducedMotionEnabled]).
 *
 * The tooltip surface carries the test tag `stylish_tooltip` for UI
 * tests; callers can override it by passing their own
 * `Modifier.testTag(...)` in [modifier]. The tooltip text is exposed to
 * screen readers via `contentDescription` semantics.
 *
 * ```kotlin
 * StylishTooltip(
 *     text = "Add a new item",
 *     placement = TooltipPlacement.Bottom,
 * ) {
 *     IconButton(onClick = { }) {
 *         Icon(Icons.Default.Add, contentDescription = null)
 *     }
 * }
 * ```
 *
 * @param text The tooltip text content, shown inside the popup surface
 *   and exposed to screen readers via semantics.
 * @param modifier Modifier applied to the anchor wrapper [Box].
 * @param containerColor Background color of the tooltip surface.
 *   Defaults to `MaterialTheme.colorScheme.inverseSurface`.
 * @param contentColor Text color of the tooltip. Defaults to
 *   `MaterialTheme.colorScheme.inverseOnSurface`.
 * @param shape Shape of the tooltip surface. Defaults to
 *   [RoundedCornerShape] with 4 dp.
 * @param textStyle Text style for the tooltip text. Defaults to
 *   `MaterialTheme.typography.bodySmall`.
 * @param placement Placement of the tooltip relative to the anchor.
 *   Defaults to [TooltipPlacement.Bottom].
 * @param delayMillis Delay in milliseconds before the tooltip appears
 *   after the anchor is hovered or focused. Defaults to 500.
 * @param content The anchor content that triggers the tooltip.
 *
 * @see TooltipPlacement
 */
@Composable
public fun StylishTooltip(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    shape: Shape = RoundedCornerShape(4.dp),
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    placement: TooltipPlacement = TooltipPlacement.Bottom,
    delayMillis: Int = 500,
    content: @Composable () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    var showTooltip by remember { mutableStateOf(false) }
    var anchorBounds by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val gapPx = with(density) { 8.dp.toPx() }
    val reducedMotion = isStylishReducedMotionEnabled()
    val animationDuration = StylishTheme.animation.durationShort

    LaunchedEffect(hovered, focused) {
        if (hovered || focused) {
            delay(delayMillis.toLong())
            if (hovered || focused) showTooltip = true
        } else {
            showTooltip = false
        }
    }

    val popupOffset = remember(anchorBounds, tooltipSize, placement, gapPx) {
        val anchor = anchorBounds ?: return@remember IntOffset.Zero
        when (placement) {
            TooltipPlacement.Bottom -> IntOffset(
                x = (anchor.size.width - tooltipSize.width) / 2,
                y = (anchor.size.height + gapPx).toInt(),
            )
            TooltipPlacement.Top -> IntOffset(
                x = (anchor.size.width - tooltipSize.width) / 2,
                y = (-tooltipSize.height - gapPx).toInt(),
            )
            TooltipPlacement.Start -> IntOffset(
                x = (-tooltipSize.width - gapPx).toInt(),
                y = (anchor.size.height - tooltipSize.height) / 2,
            )
            TooltipPlacement.End -> IntOffset(
                x = (anchor.size.width + gapPx).toInt(),
                y = (anchor.size.height - tooltipSize.height) / 2,
            )
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { anchorBounds = it }
            .onFocusEvent { state -> focused = state.isFocused }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> hovered = true
                            PointerEventType.Exit -> hovered = false
                            else -> Unit
                        }
                    }
                }
            },
    ) {
        content()

        AnimatedVisibility(
            visible = showTooltip,
            enter = fadeIn(
                animationSpec = if (reducedMotion) snap() else tween(animationDuration),
            ),
            exit = fadeOut(
                animationSpec = if (reducedMotion) snap() else tween(animationDuration),
            ),
        ) {
            Popup(
                alignment = Alignment.TopStart,
                offset = popupOffset,
            ) {
                Surface(
                    modifier = Modifier
                        .onGloballyPositioned { tooltipSize = it.size }
                        .testTag("stylish_tooltip")
                        .semantics(mergeDescendants = true) {
                            contentDescription = text
                        },
                    shape = shape,
                    color = containerColor,
                    contentColor = contentColor,
                    tonalElevation = 0.dp,
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(
                            horizontal = StylishTheme.dimensions.inlineSpacing,
                            vertical = StylishTheme.dimensions.spacingXxs,
                        ),
                        style = textStyle,
                    )
                }
            }
        }
    }
}

@Preview(name = "Stylish tooltip — bottom", showBackground = true, widthDp = 393)
@Composable
private fun StylishTooltipBottomPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishTooltip(
                text = "Add a new item",
                placement = TooltipPlacement.Bottom,
            ) {
                Button(onClick = {}) {
                    Text("ホバーでツールチップ")
                }
            }
        }
    }
}

@Preview(name = "Stylish tooltip — top", showBackground = true, widthDp = 393)
@Composable
private fun StylishTooltipTopPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishTooltip(
                text = "追加",
                placement = TooltipPlacement.Top,
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}

@Preview(name = "Stylish tooltip — end", showBackground = true, widthDp = 393)
@Composable
private fun StylishTooltipEndPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishTooltip(
                text = "補足説明",
                placement = TooltipPlacement.End,
            ) {
                Text("テキストにツールチップ", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

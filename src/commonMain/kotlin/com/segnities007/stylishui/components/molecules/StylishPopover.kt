package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.math.roundToInt

/**
 * Placement of a [StylishPopover] relative to its trigger anchor.
 *
 * The placement determines which side of the anchor the popover appears on
 * and how it is aligned. For example, [Bottom] centers the popover
 * horizontally below the anchor, while [BottomStart] aligns the popover's
 * start edge with the anchor's start edge below it.
 */
public enum class PopoverPlacement {
    /** Above the anchor, centered horizontally. */
    Top,

    /** Below the anchor, centered horizontally. */
    Bottom,

    /** To the start (left in LTR) of the anchor, centered vertically. */
    Start,

    /** To the end (right in LTR) of the anchor, centered vertically. */
    End,

    /** Above the anchor, aligned to the start edge. */
    TopStart,

    /** Above the anchor, aligned to the end edge. */
    TopEnd,

    /** Below the anchor, aligned to the start edge. */
    BottomStart,

    /** Below the anchor, aligned to the end edge. */
    BottomEnd,
}

/**
 * A popover — a click-triggered floating panel for rich content (forms,
 * tables, filters, inline editing) — the MUI Popover / Radix Popover
 * counterpart.
 *
 * Unlike [com.segnities007.stylishui.components.atoms.StylishDropdownMenu],
 * a popover is not menu-shaped, does not auto-close when its content is
 * tapped, and shows no scrim: it is dismissed only through
 * [onExpandedChange] or [onDismissRequest] (typically by tapping outside
 * or pressing back). The popover is non-modal and anchored to the
 * [trigger] element.
 *
 * The [trigger] is rendered by this composable inside an internal [Box].
 * The popup enters with a short fade + scale animation
 * ([StylishTheme.animation.durationShort]) that is skipped when the
 * platform requests reduced motion (see [isStylishReducedMotionEnabled]).
 *
 * **Anchoring limitation:** the popup position is computed from the
 * trigger's bounds while the popup is positioned relative to the root,
 * so the trigger must not be nested inside transformed containers
 * (e.g. parents that change the coordinate space, such as `graphicsLayer`
 * scale/rotation or scroll-transformed layouts) or the popup will drift
 * off the anchor.
 *
 * ```kotlin
 * var expanded by remember { mutableStateOf(false) }
 * StylishPopover(
 *     trigger = {
 *         Button(onClick = { expanded = !expanded }) {
 *             Text("フィルター")
 *         }
 *     },
 *     expanded = expanded,
 *     onExpandedChange = { expanded = it },
 *     placement = PopoverPlacement.Bottom,
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
 * @param trigger The anchor element that opens the popover when clicked.
 *   Rendered inside the wrapping [Box].
 * @param content The popover content shown in the floating panel.
 * @param modifier Modifier applied to the [Box] that wraps [trigger].
 * @param expanded Whether the popover is currently shown.
 * @param onExpandedChange Called with the new expanded state when the
 *   user toggles the popover. The caller should update [expanded]
 *   accordingly.
 * @param placement Where the popover appears relative to the trigger.
 *   Defaults to [PopoverPlacement.Bottom].
 * @param offset Offset from the computed anchor position. Defaults to
 *   `DpOffset(0.dp, 4.dp)` (4 dp below the anchor for [PopoverPlacement.Bottom]).
 * @param containerColor Background color of the popup surface. Defaults
 *   to `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param contentColor Default content color inside the popup. Defaults
 *   to `MaterialTheme.colorScheme.onSurface`.
 * @param shape Shape of the popup surface. Defaults to [RoundedCornerShape]
 *   with [StylishTheme.dimensions.floatingCornerRadius].
 * @param border Optional [BorderStroke] drawn around the popup surface.
 *   Defaults to a hairline border using
 *   [StylishTheme.dimensions.outlineWidth] and
 *   `MaterialTheme.colorScheme.outlineVariant`.
 * @param tonalElevation Tonal elevation of the popup surface. Defaults
 *   to 4 dp.
 * @param shadowElevation Shadow elevation of the popup surface. Defaults
 *   to [StylishTheme.dimensions.floatingElevation].
 * @param onDismissRequest Called when the user requests dismissal (e.g.
 *   tapping outside or pressing back). Defaults to calling
 *   `onExpandedChange(false)`.
 *
 * @see com.segnities007.stylishui.components.atoms.StylishDropdownMenu
 * @see PopoverPlacement
 */
@Composable
public fun StylishPopover(
    trigger: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    placement: PopoverPlacement = PopoverPlacement.Bottom,
    offset: DpOffset = DpOffset(0.dp, 4.dp),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    width: Dp? = null,
    onDismissRequest: () -> Unit = { onExpandedChange(false) },
) {
    var triggerBounds by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val density = LocalDensity.current
    val offsetPx = with(density) { IntOffset(offset.x.toPx().roundToInt(), offset.y.toPx().roundToInt()) }
    val reducedMotion = isStylishReducedMotionEnabled()
    val animationDuration = StylishTheme.animation.durationShort

    val positionProvider = remember(placement, offsetPx) {
        PopoverPopupPositionProvider(
            placement = placement,
            offset = offsetPx,
        )
    }

    Box(
        modifier = modifier.onGloballyPositioned { triggerBounds = it },
    ) {
        trigger()
        triggerBounds?.let { bounds ->
            if (expanded) {
                val triggerPosition = bounds.positionInRoot()
                val triggerSize = IntSize(bounds.size.width, bounds.size.height)
                val anchorBounds = IntRect(
                    triggerPosition.x.roundToInt(),
                    triggerPosition.y.roundToInt(),
                    triggerPosition.x.roundToInt() + triggerSize.width,
                    triggerPosition.y.roundToInt() + triggerSize.height,
                )

                Popup(
                    popupPositionProvider = positionProvider,
                    onDismissRequest = onDismissRequest,
                    properties = PopupProperties(focusable = true),
                ) {
                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn(tween(animationDuration)) + scaleIn(
                            initialScale = if (reducedMotion) 1f else 0.95f,
                            animationSpec = tween(animationDuration),
                        ),
                        exit = fadeOut(tween(animationDuration)) + scaleOut(
                            targetScale = if (reducedMotion) 1f else 0.95f,
                            animationSpec = tween(animationDuration),
                        ),
                    ) {
                        Surface(
                            modifier = Modifier
                                .testTag("stylish_popover")
                                .then(if (width != null) Modifier.width(width) else Modifier),
                            shape = shape,
                            color = containerColor,
                            contentColor = contentColor,
                            tonalElevation = tonalElevation,
                            shadowElevation = shadowElevation,
                            border = border,
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

/**
 * A [PopupPositionProvider] that positions a popover relative to an anchor
 * based on [PopoverPlacement].
 *
 * The provider computes the popup's position by:
 * 1. Determining the anchor edge based on [placement]
 * 2. Aligning the popup according to the placement (center, start, or end)
 * 3. Applying the [offset] to the final position
 *
 * @param placement Where the popover appears relative to the anchor.
 * @param offset Offset in pixels from the computed position.
 */
private class PopoverPopupPositionProvider(
    private val placement: PopoverPlacement,
    private val offset: IntOffset,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val anchorCenterX = (anchorBounds.left + anchorBounds.right) / 2
        val anchorCenterY = (anchorBounds.top + anchorBounds.bottom) / 2

        val (x, y) = when (placement) {
            PopoverPlacement.Top -> {
                val x = anchorCenterX - popupContentSize.width / 2
                val y = anchorBounds.top - popupContentSize.height
                x to y
            }
            PopoverPlacement.Bottom -> {
                val x = anchorCenterX - popupContentSize.width / 2
                val y = anchorBounds.bottom
                x to y
            }
            PopoverPlacement.Start -> {
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.left - popupContentSize.width
                } else {
                    anchorBounds.right
                }
                val y = anchorCenterY - popupContentSize.height / 2
                x to y
            }
            PopoverPlacement.End -> {
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.right
                } else {
                    anchorBounds.left - popupContentSize.width
                }
                val y = anchorCenterY - popupContentSize.height / 2
                x to y
            }
            PopoverPlacement.TopStart -> {
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.left
                } else {
                    anchorBounds.right - popupContentSize.width
                }
                val y = anchorBounds.top - popupContentSize.height
                x to y
            }
            PopoverPlacement.TopEnd -> {
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.right - popupContentSize.width
                } else {
                    anchorBounds.left
                }
                val y = anchorBounds.top - popupContentSize.height
                x to y
            }
            PopoverPlacement.BottomStart -> {
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.left
                } else {
                    anchorBounds.right - popupContentSize.width
                }
                val y = anchorBounds.bottom
                x to y
            }
            PopoverPlacement.BottomEnd -> {
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.right - popupContentSize.width
                } else {
                    anchorBounds.left
                }
                val y = anchorBounds.bottom
                x to y
            }
        }

        return IntOffset(x + offset.x, y + offset.y)
    }
}

@Preview(name = "Popover on button (bottom placement)", showBackground = true, widthDp = 393)
@Composable
private fun StylishPopoverBottomPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            var expanded by remember { mutableStateOf(true) }
            StylishPopover(
                trigger = {
                    Button(onClick = { expanded = !expanded }) {
                        Text("フィルター")
                    }
                },
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp).width(240.dp),
                    ) {
                        Text("絞り込み条件", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "カテゴリと状態で表示を絞り込みます",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placement = PopoverPlacement.Bottom,
            )
        }
    }
}

@Preview(name = "Popover with form content", showBackground = true, widthDp = 393)
@Composable
private fun StylishPopoverFormPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            var expanded by remember { mutableStateOf(true) }
            var notifications by remember { mutableStateOf(true) }
            StylishPopover(
                trigger = {
                    Button(onClick = { expanded = !expanded }) {
                        Text("設定")
                    }
                },
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp).width(260.dp),
                    ) {
                        Text("通知設定", style = MaterialTheme.typography.titleSmall)
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
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placement = PopoverPlacement.Bottom,
            )
        }
    }
}

@Preview(name = "Popover with list content", showBackground = true, widthDp = 393)
@Composable
private fun StylishPopoverListPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            var expanded by remember { mutableStateOf(true) }
            StylishPopover(
                trigger = {
                    Button(onClick = { expanded = !expanded }) {
                        Text("メニュー")
                    }
                },
                content = {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp).width(200.dp),
                    ) {
                        Text(
                            "編集",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            "コピー",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        HorizontalDivider()
                        Text(
                            "削除",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placement = PopoverPlacement.Bottom,
            )
        }
    }
}

@Preview(name = "Popover with different placements", showBackground = true, widthDp = 393, heightDp = 600)
@Composable
private fun StylishPopoverPlacementsPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            var expanded by remember { mutableStateOf(true) }
            StylishPopover(
                trigger = {
                    Button(onClick = { expanded = !expanded }) {
                        Text("TopStart")
                    }
                },
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp).width(180.dp),
                    ) {
                        Text("TopStart配置", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "アンカーの左上に表示されます",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placement = PopoverPlacement.TopStart,
            )
        }
    }
}

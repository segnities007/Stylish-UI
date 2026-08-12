package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.stylishFocusRing
import com.segnities007.stylishui.foundation.stylishStateLayer
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * The default chip renderer used by connected-chip layouts when no custom
 * [ConnectedChipItemContent] is supplied.
 *
 * This is the Finish-layer rendering: it dresses a chip in the Stylish look —
 * animated selection colors (180 ms), `Role.Tab` semantics with the `selected`
 * state, a hairline connected outline, interactive elevation, and a haptic
 * pulse on tap. Items whose [StylishConnectedChipItem.onClick] is `null` or
 * whose [StylishConnectedChipItem.enabled] is `false` are non-interactive and
 * lose their elevation. Disabled chips drop the selection colors entirely:
 * they render with the `surfaceVariant` container and `onSurfaceVariant`
 * content colors without animation, and are marked with the `disabled()`
 * semantic flag.
 *
 * @param item The [StylishConnectedChipItem] data for the chip.
 * @param modifier A modifier carrying layout constraints from the parent
 *   layout. Applied to the [Surface] root.
 * @param shape The connected [Shape] for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides draw outline
 *   borders.
 * @param outlineCorners The [ConnectedCorners] indicating which corners are
 *   rounded.
 * @param labelMaxLines Maximum lines for the chip label.
 * @param labelOverflow Overflow strategy for the chip label.
 * @param labelStyle Text style for the chip label.
 * @param selectedContainerColor Background color of a selected chip.
 * @param selectedContentColor Content color of a selected chip.
 * @param unselectedContainerColor Background color of an unselected chip.
 * @param unselectedContentColor Content color of an unselected chip.
 * @param contentPadding Inner padding of the chip.
 * @param contentSpacing Gap between the leading slot, label, and trailing slot.
 * @param interactionSource The [MutableInteractionSource] used to observe
 *   press/hover/focus interactions for the state layer. When `null`, an
 *   internal one is remembered per chip.
 *
 * Focus: actionable chips draw the web "focus-visible ring"
 * ([Modifier.stylishFocusRing]) around [shape] while the chip holds keyboard
 * or focus-ring focus.
 *
 * @see StylishConnectedChipRow
 * @see StylishConnectedChipColumn
 * @see StylishConnectedChipGrid
 */
@Composable
public fun DefaultStylishConnectedChip(
    item: StylishConnectedChipItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
    labelMaxLines: Int,
    labelOverflow: TextOverflow,
    labelStyle: TextStyle,
    selectedContainerColor: Color,
    selectedContentColor: Color,
    unselectedContainerColor: Color,
    unselectedContentColor: Color,
    contentPadding: PaddingValues,
    contentSpacing: Dp,
    interactionSource: MutableInteractionSource? = null,
) {
    val haptic = LocalHapticFeedback.current
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val actionable = isActionable(
        enabled = item.enabled,
        hasClickAction = item.onClick != null,
    )
    val containerColor: Color
    val contentColor: Color
    if (item.enabled) {
        if (isStylishReducedMotionEnabled()) {
            containerColor = if (item.selected) selectedContainerColor else unselectedContainerColor
            contentColor = if (item.selected) selectedContentColor else unselectedContentColor
        } else {
            val animatedContainerColor by animateColorAsState(
                targetValue = if (item.selected) selectedContainerColor else unselectedContainerColor,
                animationSpec = tween(StylishTheme.animation.durationShort),
                label = "chipContainer",
            )
            val animatedContentColor by animateColorAsState(
                targetValue = if (item.selected) selectedContentColor else unselectedContentColor,
                animationSpec = tween(StylishTheme.animation.durationShort),
                label = "chipContent",
            )
            containerColor = animatedContainerColor
            contentColor = animatedContentColor
        }
    } else {
        containerColor = MaterialTheme.colorScheme.surfaceVariant
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                selected = item.selected
                role = Role.Tab
                if (!item.enabled) disabled()
            }
            .then(
                if (actionable) {
                    Modifier.clickable(
                        interactionSource = resolvedInteractionSource,
                        indication = null,
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        item.onClick?.invoke()
                    }
                        .stylishFocusRing(resolvedInteractionSource, shape)
                } else {
                    Modifier
                },
            )
            .then(
                if (actionable) {
                    Modifier
                        .clip(shape)
                        .stylishStateLayer(resolvedInteractionSource)
                } else {
                    Modifier
                },
            )
            .connectedOutline(outlineEdges, outlineCorners),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        Row(
            Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(contentSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.content != null) {
                item.content(this)
            } else {
                item.leadingContent?.invoke(this)
                Text(
                    item.label,
                    style = labelStyle,
                    maxLines = labelMaxLines,
                    overflow = labelOverflow,
                )
                item.trailingContent?.invoke(this)
            }
        }
    }
}

@Preview(name = "Stylish connected chip", showBackground = true, widthDp = 393)
@Composable
private fun DefaultStylishConnectedChipPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            DefaultStylishConnectedChip(
                item = StylishConnectedChipItem("選択中", {}, selected = true),
                modifier = Modifier,
                shape = connectedShape(ConnectedCorners.Standalone),
                outlineEdges = ConnectedEdges.All,
                outlineCorners = ConnectedCorners.Standalone,
                labelMaxLines = 1,
                labelOverflow = TextOverflow.Ellipsis,
                labelStyle = MaterialTheme.typography.labelLarge,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                unselectedContainerColor = MaterialTheme.stylishComponentColors.groupedContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                contentSpacing = 6.dp,
            )
        }
    }
}

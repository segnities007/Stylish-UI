package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A horizontally connected row of selectable chips with animated
 * selection-state color transitions, suited for category filters or
 * tab-like selectors.
 *
 * Outline edges and corner radii are computed automatically from each item's
 * index so that the first chip rounds only its leading corners, the last chip
 * rounds only its trailing corners, and middle chips have square corners with
 * shared vertical outlines. When [fillWidth] is `false` (the default) the row
 * scrolls horizontally; when `true`, every chip receives equal weight and the
 * row fills the available width. Tapping an actionable chip triggers a haptic
 * feedback pulse and animates the container/content colors over 180 ms. Chips
 * are assigned `Role.Tab` semantics with the `selected` state reflected from
 * [StylishConnectedChipItem.selected]. Items whose
 * [StylishConnectedChipItem.onClick] is `null` or whose
 * [StylishConnectedChipItem.enabled] is `false` are non-interactive and lose
 * their elevation.
 *
 * @param items The list of [StylishConnectedChipItem] data objects that
 *   describe each chip's label, selection state, click action, and optional
 *   leading/trailing slot content.
 * @param spacing The horizontal gap between adjacent chips. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param fillWidth When `true`, chips share the available width equally
 *   instead of scrolling. Defaults to `false`.
 * @param labelMaxLines Maximum number of lines for the chip label text.
 *   Defaults to 1.
 * @param labelOverflow The [TextOverflow] strategy for the chip label when it
 *   exceeds [labelMaxLines]. Defaults to [TextOverflow.Ellipsis].
 * @param labelStyle The [TextStyle] applied to each chip's label. Defaults to
 *   [MaterialTheme.typography.labelLarge].
 * @param selectedContainerColor The background color of a selected chip.
 *   Defaults to [MaterialTheme.colorScheme.primary].
 * @param selectedContentColor The content color of a selected chip. Defaults
 *   to [MaterialTheme.colorScheme.onPrimary].
 * @param unselectedContainerColor The background color of an unselected chip.
 *   Defaults to [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param unselectedContentColor The content color of an unselected chip.
 *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param contentPadding The inner padding of each chip. Defaults to 14 dp
 *   horizontal and 10 dp vertical.
 * @param contentSpacing The horizontal gap between the leading slot, label,
 *   and trailing slot inside each chip. Defaults to 6 dp.
 *
 * @see StylishConnectedButtonRow
 */
@Composable
public fun StylishConnectedChipRow(
    items: List<StylishConnectedChipItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    fillWidth: Boolean = false,
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color = MaterialTheme.stylishComponentColors.groupedContainer,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing: Dp = 6.dp,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = if (fillWidth) modifier.fillMaxWidth()
        else modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            val actionable = isActionable(
                enabled = item.enabled,
                hasClickAction = item.onClick != null,
            )
            val containerColor by animateColorAsState(
                targetValue = if (item.selected) selectedContainerColor else unselectedContainerColor,
                animationSpec = tween(180),
                label = "chipContainer",
            )
            val contentColor by animateColorAsState(
                targetValue = if (item.selected) selectedContentColor else unselectedContentColor,
                animationSpec = tween(180),
                label = "chipContent",
            )
            Surface(
                modifier = Modifier
                    .let { if (fillWidth) it.weight(1f) else it }
                    .semantics {
                        selected = item.selected
                        role = Role.Tab
                    }
                    .then(
                        if (actionable) {
                            Modifier.clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                item.onClick?.invoke()
                            }
                        } else {
                            Modifier
                        },
                    )
                    .connectedOutline(
                        edges = connectedRowEdges(index, items.size),
                        corners = corners,
                    ),
                shape = connectedShape(corners),
                color = containerColor,
                contentColor = contentColor,
                shadowElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
            ) {
                Row(
                    Modifier.padding(contentPadding),
                    horizontalArrangement = Arrangement.spacedBy(contentSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
}

@Preview(name = "Connected chip row", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedChipRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedChipRow(
                listOf(
                    StylishConnectedChipItem("すべて", {}, selected = true) {
                        Icon(Icons.Default.Check, null)
                    },
                    StylishConnectedChipItem("仕事", {}),
                    StylishConnectedChipItem("個人", {}),
                    StylishConnectedChipItem("アイデア", {}),
                ),
            )
        }
    }
}

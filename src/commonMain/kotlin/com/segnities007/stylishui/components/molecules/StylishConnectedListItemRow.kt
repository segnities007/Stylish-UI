package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A horizontally connected group of list items that share outlines and corner
 * radii, typically used for side-by-side summary tiles or horizontal
 * navigation sections.
 *
 * Each item receives equal weight within the row and stretches to the tallest
 * sibling via [IntrinsicSize.Min]. Outline edges and corner radii are computed
 * automatically from each item's index: the first item rounds only its
 * leading corners, the last item rounds only its trailing corners, and middle
 * items have square corners with shared vertical outlines. Each item displays
 * a headline, an optional supporting text line, additional supporting lines,
 * and optional leading/trailing slot content. Items whose
 * [StylishConnectedListItem.onClick] is `null` and whose
 * [StylishConnectedListItem.onLongClick] is `null`, or whose
 * [StylishConnectedListItem.enabled] is `false`, are rendered without
 * elevation and do not respond to interaction. Long-click actions trigger
 * haptic feedback before invoking the callback. Items with an [onClick]
 * action are assigned `Role.Button` semantics; disabled items are marked
 * with the `disabled()` semantic flag.
 *
 * @param items The list of [StylishConnectedListItem] data objects that
 *   describe each row's headline, supporting text, click/long-click actions,
 *   enabled state, and slot content.
 * @param spacing The horizontal gap between adjacent items. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param headlineMaxLines Maximum number of lines for the headline text.
 *   Defaults to [Int.MAX_VALUE] (unlimited).
 * @param headlineOverflow The [TextOverflow] strategy for the headline when it
 *   exceeds [headlineMaxLines]. Defaults to [TextOverflow.Ellipsis].
 * @param headlineStyle The [TextStyle] applied to each item's headline.
 *   Defaults to [MaterialTheme.typography.titleMedium].
 * @param supportingTextMaxLines Maximum number of lines for supporting text
 *   and supporting lines. Defaults to [Int.MAX_VALUE] (unlimited).
 * @param supportingTextOverflow The [TextOverflow] strategy for supporting
 *   text. Defaults to [TextOverflow.Ellipsis].
 * @param supportingTextStyle The [TextStyle] applied to supporting text and
 *   supporting lines. Defaults to [MaterialTheme.typography.bodyMedium].
 * @param containerColor The background color of each item surface. When
 *   `null`, defaults to [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param contentColor The content color of each item surface. When `null`,
 *   defaults to [MaterialTheme.colorScheme.onSurface] for enabled items or
 *   [MaterialTheme.colorScheme.onSurfaceVariant] for disabled items.
 * @param horizontalPadding The horizontal padding inside each item. Defaults
 *   to 16 dp.
 * @param verticalPadding The vertical padding inside each item. Defaults to
 *   14 dp.
 *
 * @see StylishConnectedListItemColumn
 * @see StylishConnectedListItemGrid
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun StylishConnectedListItemRow(
    items: List<StylishConnectedListItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    headlineMaxLines: Int = Int.MAX_VALUE,
    headlineOverflow: TextOverflow = TextOverflow.Ellipsis,
    headlineStyle: TextStyle = MaterialTheme.typography.titleMedium,
    supportingTextMaxLines: Int = Int.MAX_VALUE,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    containerColor: Color? = null,
    contentColor: Color? = null,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 14.dp,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            val actionable = isActionable(
                enabled = item.enabled,
                hasClickAction = item.onClick != null,
                hasLongClickAction = item.onLongClick != null,
            )
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .then(
                        if (actionable) {
                            Modifier
                                .combinedClickable(
                                    onClick = { item.onClick?.invoke() },
                                    onLongClick = item.onLongClick?.let {
                                        {
                                            haptic.performHapticFeedback(
                                                HapticFeedbackType.LongPress,
                                            )
                                            it()
                                        }
                                    },
                                )
                                .semantics { role = Role.Button }
                        } else {
                            Modifier
                        },
                    )
                    .semantics {
                        if (!item.enabled) disabled()
                    }
                    .connectedOutline(
                        edges = connectedRowEdges(index, items.size),
                        corners = corners,
                    ),
                shape = connectedShape(corners),
                color = containerColor ?: MaterialTheme.stylishComponentColors.groupedContainer,
                contentColor = contentColor ?: if (item.enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                shadowElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
            ) {
                Row(
                    Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item.leadingContent?.invoke(this)
                    Column(Modifier.weight(1f)) {
                        Text(
                            item.headline,
                            style = headlineStyle,
                            maxLines = headlineMaxLines,
                            overflow = headlineOverflow,
                        )
                        item.supportingText?.let {
                            Text(
                                it,
                                style = supportingTextStyle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = supportingTextMaxLines,
                                overflow = supportingTextOverflow,
                            )
                        }
                        item.supportingLines.forEach { line ->
                            Text(
                                line,
                                style = supportingTextStyle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = supportingTextMaxLines,
                                overflow = supportingTextOverflow,
                            )
                        }
                    }
                    item.trailingContent?.invoke(this)
                }
            }
        }
    }
}

@Preview(name = "Connected list item row", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedListItemRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedListItemRow(
                listOf(
                    StylishConnectedListItem(
                        headline = "テーマ",
                        supportingText = "システム",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Palette, null) },
                    ),
                    StylishConnectedListItem(
                        headline = "通知",
                        supportingText = "オン",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Notifications, null) },
                    ),
                ),
            )
        }
    }
}

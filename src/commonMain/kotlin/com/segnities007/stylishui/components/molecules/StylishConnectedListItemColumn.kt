package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.structure.ConnectedListItemColumn
import com.segnities007.stylishui.structure.ConnectedListItemContent
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A vertically connected group of list items that share outlines and corner
 * radii, typically used for grouped settings sections or navigation menus.
 *
 * This is the Finish-layer component: it supplies the Stylish list-item
 * rendering ([DefaultStylishConnectedListItem]) to the headless Structure
 * layout [ConnectedListItemColumn], which owns arrangement and connection
 * geometry. Outline edges and corner radii are computed automatically from each
 * item's index: the first item rounds only its top corners, the last item
 * rounds only its bottom corners, and middle items have square corners. Each
 * item displays a headline, optional supporting text, additional supporting
 * lines, and optional leading/trailing slot content. Items whose
 * [StylishConnectedListItem.onClick] and [StylishConnectedListItem.onLongClick]
 * are both `null`, or whose [StylishConnectedListItem.enabled] is `false`, are
 * rendered without elevation and do not respond to interaction. Pass a custom
 * [listItem] to override the Stylish rendering while keeping the connected
 * geometry.
 *
 * @param items The list of [StylishConnectedListItem] data objects that
 *   describe each row's headline, supporting text, click/long-click actions,
 *   enabled state, and slot content.
 * @param spacing The vertical gap between adjacent items. Defaults to
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
 *   defaults to an enabled/disabled-aware color.
 * @param horizontalPadding The horizontal padding inside each item. Defaults
 *   to 16 dp.
 * @param verticalPadding The vertical padding inside each item. Defaults to
 *   14 dp.
 * @param contentSpacing The horizontal gap between the leading slot, text
 *   block, and trailing slot inside each item. Defaults to
 *   [StylishTheme.dimensions.itemSpacing] (8 dp).
 * @param listItem A composable lambda that renders a single item. Receives
 *   the item data, a modifier (including fill-max-width), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *   Defaults to [DefaultStylishConnectedListItem], dressed in the Stylish look
 *   with the text, color, padding, and spacing parameters above.
 *
 * @see ConnectedListItemColumn
 * @see StylishConnectedListItemRow
 * @see StylishConnectedListItemGrid
 * @see DefaultStylishConnectedListItem
 */
@Composable
public fun StylishConnectedListItemColumn(
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
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    listItem: ConnectedListItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedListItem(
            item, itemModifier, shape, edges, corners, headlineMaxLines, headlineOverflow,
            headlineStyle, supportingTextMaxLines, supportingTextOverflow, supportingTextStyle,
            containerColor, contentColor, horizontalPadding, verticalPadding, contentSpacing,
        )
    },
) {
    ConnectedListItemColumn(items, modifier, spacing, listItem = listItem)
}

@Preview(name = "Connected list items", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedListItemColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedListItemColumn(
                listOf(
                    StylishConnectedListItem(
                        headline = "テーマ",
                        supportingText = "システム設定を使用",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Palette, null) },
                        trailingContent = {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                        },
                    ),
                    StylishConnectedListItem(
                        headline = "通知",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Notifications, null) },
                        trailingContent = { Switch(true, {}) },
                    ),
                ),
            )
        }
    }
}

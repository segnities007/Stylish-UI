package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.structure.ConnectedListItemContent
import com.segnities007.stylishui.structure.ConnectedListItemRow
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontally connected group of list items that share outlines and corner
 * radii, typically used for side-by-side summary tiles or horizontal
 * navigation sections.
 *
 * This is the Finish-layer component: it supplies the Stylish list-item
 * rendering ([DefaultStylishConnectedListItem]) to the headless Structure
 * layout [ConnectedListItemRow], which owns arrangement and connection
 * geometry. Each item receives equal weight within the row and stretches to the
 * tallest sibling. Outline edges and corner radii are computed automatically
 * from each item's index. Each item displays a headline, optional supporting
 * text, additional supporting lines, and optional leading/trailing slot
 * content. Items whose [StylishConnectedListItem.onClick] and
 * [StylishConnectedListItem.onLongClick] are both `null`, or whose
 * [StylishConnectedListItem.enabled] is `false`, are rendered without elevation
 * and do not respond to interaction. Pass a custom [listItem] to override the
 * Stylish rendering while keeping the connected geometry.
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
 *   defaults to an enabled/disabled-aware color.
 * @param horizontalPadding The horizontal padding inside each item. Defaults
 *   to 16 dp.
 * @param verticalPadding The vertical padding inside each item. Defaults to
 *   14 dp.
 * @param contentSpacing The horizontal gap between the leading slot, text
 *   block, and trailing slot inside each item. Defaults to
 *   [StylishTheme.dimensions.itemSpacing] (8 dp).
 * @param interactionSource The [MutableInteractionSource] forwarded to each
 *   actionable item to observe press/hover/focus interactions for the state
 *   layer. When `null`, each item remembers its own. Pass a shared source to
 *   observe group-level interaction, e.g. to trigger a state update from a
 *   parent.
 * @param listItem A composable lambda that renders a single item. Receives
 *   the item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedListItem], dressed
 *   in the Stylish look with the text, color, padding, and spacing parameters
 *   above.
 *
 * @see ConnectedListItemRow
 * @see StylishConnectedListItemColumn
 * @see StylishConnectedListItemGrid
 * @see DefaultStylishConnectedListItem
 */
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
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    interactionSource: MutableInteractionSource? = null,
    listItem: ConnectedListItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedListItem(
            item, itemModifier, shape, edges, corners, headlineMaxLines, headlineOverflow,
            headlineStyle, supportingTextMaxLines, supportingTextOverflow, supportingTextStyle,
            containerColor, contentColor, horizontalPadding, verticalPadding, contentSpacing,
            interactionSource = interactionSource,
        )
    },
) {
    ConnectedListItemRow(items, modifier, spacing, listItem = listItem)
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

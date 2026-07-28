package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
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
import com.segnities007.stylishui.structure.ConnectedListItemGrid
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A grid of connected list items laid out in equal-width cells across a fixed
 * number of columns, with shared outlines and corner radii — suited for
 * settings dashboards or grouped navigation tiles.
 *
 * This is the Finish-layer component: it supplies the Stylish list-item
 * rendering ([DefaultStylishConnectedListItem]) to the headless Structure
 * layout [ConnectedListItemGrid], which owns arrangement and connection
 * geometry. Items are chunked into rows of [columns] items; within each row
 * every item receives equal weight and stretches to the tallest sibling.
 * Outline edges are drawn on all four sides of every cell; corner radii are
 * computed automatically from each item's absolute index so that only the four
 * outer corners of the entire grid are rounded. Each item displays a headline,
 * optional supporting text, additional supporting lines, and optional
 * leading/trailing slot content. Items whose [StylishConnectedListItem.onClick]
 * and [StylishConnectedListItem.onLongClick] are both `null`, or whose
 * [StylishConnectedListItem.enabled] is `false`, are rendered without elevation
 * and do not respond to interaction.
 *
 * @param items The list of [StylishConnectedListItem] data objects that
 *   describe each cell's headline, supporting text, click/long-click actions,
 *   enabled state, and slot content.
 * @param columns The number of equal-width columns in the grid. Must be
 *   greater than zero.
 * @param spacing The gap between adjacent items both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing]
 *   (3 dp).
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
 *
 * @see ConnectedListItemGrid
 * @see StylishConnectedListItemRow
 * @see StylishConnectedListItemColumn
 * @see DefaultStylishConnectedListItem
 */
@Composable
public fun StylishConnectedListItemGrid(
    items: List<StylishConnectedListItem>,
    columns: Int,
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
    ConnectedListItemGrid(items, columns, modifier, spacing) { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedListItem(
            item, itemModifier, shape, edges, corners, headlineMaxLines, headlineOverflow,
            headlineStyle, supportingTextMaxLines, supportingTextOverflow, supportingTextStyle,
            containerColor, contentColor, horizontalPadding, verticalPadding,
        )
    }
}

@Preview(name = "Connected list item grid", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedListItemGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedListItemGrid(
                items = listOf(
                    StylishConnectedListItem(
                        headline = "テーマ",
                        supportingText = "システム設定",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Palette, null) },
                    ),
                    StylishConnectedListItem(
                        headline = "通知",
                        supportingText = "オン",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Notifications, null) },
                    ),
                    StylishConnectedListItem(
                        headline = "全般",
                        supportingText = "言語・地域",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Settings, null) },
                    ),
                ),
                columns = 2,
            )
        }
    }
}

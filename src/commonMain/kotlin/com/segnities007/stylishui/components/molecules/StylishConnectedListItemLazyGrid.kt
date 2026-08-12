package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.segnities007.stylishui.structure.ConnectedListItemContent
import com.segnities007.stylishui.structure.ConnectedListItemLazyGrid
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A lazily-composed grid of connected list items laid out in equal-width cells across a fixed
 * number of columns, with shared outlines and corner radii — suited for scrollable settings
 * dashboards or grouped navigation tiles.
 *
 * This is the Finish-layer component: it supplies the Stylish list-item rendering
 * ([DefaultStylishConnectedListItem]) to the headless Structure layout [ConnectedListItemLazyGrid],
 * which owns arrangement and connection geometry. Only the visible rows are composed, while
 * the connection geometry is computed exactly as in the eager [StylishConnectedListItemGrid]:
 * items are chunked into rows of [columns] items, within each row every item receives equal
 * weight, corner radii are computed from each item's absolute index so that only the four
 * outer corners of the entire grid are rounded, and a partially filled final row stretches to
 * fill the full row width. Each item displays a headline, optional supporting text, additional
 * supporting lines, and optional leading/trailing slot content. Items whose
 * [StylishConnectedListItem.onClick] and [StylishConnectedListItem.onLongClick] are both
 * `null`, or whose [StylishConnectedListItem.enabled] is `false`, are rendered without
 * elevation and do not respond to interaction. Pass a custom [listItem] to override the
 * Stylish rendering while keeping the connected geometry.
 *
 * @param items The list of [StylishConnectedListItem] data objects that describe each cell's
 *   headline, supporting text, click/long-click actions, enabled state, and slot content.
 * @param columns The number of equal-width columns in the grid. Must be greater than zero.
 * @param spacing The gap between adjacent items both horizontally and vertically. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding [PaddingValues] applied around the entire lazy grid. Defaults to
 *   [DefaultStylishDimensions.controlPadding] horizontal padding.
 * @param listState [LazyGridState] controlling scroll position. Defaults to
 *   [rememberLazyGridState]. Supply a hoisted state to observe scroll offset or
 *   programmatically scroll.
 * @param headlineMaxLines Maximum number of lines for the headline text. Defaults to
 *   [Int.MAX_VALUE] (unlimited).
 * @param headlineOverflow The [TextOverflow] strategy for the headline when it exceeds
 *   [headlineMaxLines]. Defaults to [TextOverflow.Ellipsis].
 * @param headlineStyle The [TextStyle] applied to each item's headline. Defaults to
 *   [MaterialTheme.typography.titleMedium].
 * @param supportingTextMaxLines Maximum number of lines for supporting text and supporting
 *   lines. Defaults to [Int.MAX_VALUE] (unlimited).
 * @param supportingTextOverflow The [TextOverflow] strategy for supporting text. Defaults to
 *   [TextOverflow.Ellipsis].
 * @param supportingTextStyle The [TextStyle] applied to supporting text and supporting lines.
 *   Defaults to [MaterialTheme.typography.bodyMedium].
 * @param containerColor The background color of each item surface. When `null`, defaults to
 *   [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param contentColor The content color of each item surface. When `null`, defaults to an
 *   enabled/disabled-aware color.
 * @param horizontalPadding The horizontal padding inside each item. Defaults to 16 dp.
 * @param verticalPadding The vertical padding inside each item. Defaults to 14 dp.
 * @param contentSpacing The horizontal gap between the leading slot, text block, and trailing
 *   slot inside each item. Defaults to [StylishTheme.dimensions.itemSpacing] (8 dp).
 * @param listItem A composable lambda that renders a single cell. Receives the item data, a
 *   modifier (fill-max-width within an equal-weight cell), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners]. Defaults to
 *   [DefaultStylishConnectedListItem], dressed in the Stylish look with the text, color,
 *   padding, and spacing parameters above.
 *
 * @see ConnectedListItemLazyGrid
 * @see StylishConnectedListItemGrid
 * @see StylishConnectedListItemLazyColumn
 * @see DefaultStylishConnectedListItem
 */
@Composable
public fun StylishConnectedListItemLazyGrid(
    items: List<StylishConnectedListItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = DefaultStylishDimensions.controlPadding),
    listState: LazyGridState = rememberLazyGridState(),
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
    ConnectedListItemLazyGrid(items, columns, modifier, spacing, contentPadding, listState, listItem = listItem)
}

@Preview(name = "Connected list items lazy grid", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishConnectedListItemLazyGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedListItemLazyGrid(
                items = listOf(
                    StylishConnectedListItem(
                        headline = "Civic",
                        supportingText = "セダン",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Palette, null) },
                    ),
                    StylishConnectedListItem(
                        headline = "Accord",
                        supportingText = "セダン",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Notifications, null) },
                    ),
                    StylishConnectedListItem(
                        headline = "Prius",
                        supportingText = "ハイブリッド",
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Settings, null) },
                    ),
                    StylishConnectedListItem(
                        headline = "Model 3",
                        supportingText = "EV",
                        onClick = {},
                    ),
                    StylishConnectedListItem(
                        headline = "Leaf",
                        supportingText = "EV",
                        onClick = {},
                    ),
                    StylishConnectedListItem(
                        headline = "Golf",
                        supportingText = "ハッチバック",
                        onClick = {},
                    ),
                    StylishConnectedListItem(
                        headline = "Passat",
                        supportingText = "セダン",
                        onClick = {},
                    ),
                    StylishConnectedListItem(
                        headline = "Corolla",
                        supportingText = "セダン",
                        onClick = {},
                    ),
                    StylishConnectedListItem(
                        headline = "Outback",
                        supportingText = "SUV",
                        onClick = {},
                    ),
                    StylishConnectedListItem(
                        headline = "Fit",
                        supportingText = "コンパクト",
                        onClick = {},
                    ),
                ),
                columns = 2,
            )
        }
    }
}

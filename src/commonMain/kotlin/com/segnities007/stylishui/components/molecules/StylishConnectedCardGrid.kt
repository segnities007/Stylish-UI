package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardGrid
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A grid of connected cards laid out in equal-width cells across a fixed
 * number of columns, with shared outlines and corner radii — suited for
 * statistics dashboards or tile galleries.
 *
 * This is the Finish-layer component: it supplies the Stylish card rendering
 * ([DefaultStylishConnectedCardItem]) to the headless Structure layout
 * [ConnectedCardGrid], which owns arrangement and connection geometry. Items
 * are chunked into rows of [columns] cards; within each row every card receives
 * equal weight and stretches to the tallest sibling. Outline edges are drawn on
 * all four sides of every cell; corner radii are computed automatically from
 * each item's absolute index so that only the four outer corners of the entire
 * grid are rounded. When the final row has fewer items than [columns], the
 * remaining cards stretch via equal weight to fill the full row width. Pass a
 * custom [card] to override the Stylish rendering while keeping the connected
 * geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects that
 *   describe each card's title, supporting text, click actions, and slot
 *   content.
 * @param columns The number of equal-width columns in the grid. Must be
 *   greater than zero.
 * @param spacing The gap between adjacent cards both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param containerColor Background color of each card. When `null`, defaults
 *   to [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param contentColor Content color of each card. When `null`, defaults to an
 *   enabled/disabled-aware color.
 * @param disabledContainerColor Background color of each card when its
 *   [StylishConnectedCardItem.enabled] is `false`.
 * @param disabledContentColor Content color of each card when its
 *   [StylishConnectedCardItem.enabled] is `false`.
 * @param titleStyle The [TextStyle] applied to each card's title.
 * @param supportingTextStyle The [TextStyle] applied to each card's supporting
 *   text.
 * @param titleMaxLines Maximum number of lines for each card's title. Defaults
 *   to 1.
 * @param titleOverflow The [TextOverflow] strategy for each card's title.
 *   Defaults to [TextOverflow.Ellipsis].
 * @param supportingTextMaxLines Maximum number of lines for each card's
 *   supporting text. Defaults to 1.
 * @param supportingTextOverflow The [TextOverflow] strategy for each card's
 *   supporting text. Defaults to [TextOverflow.Ellipsis].
 * @param minHeight Minimum height of each card body. Defaults to
 *   [DefaultStylishDimensions.cardMinHeight] (77 dp).
 * @param horizontalPadding Horizontal padding inside each card. Defaults to
 *   [DefaultStylishDimensions.controlPadding] (16 dp).
 * @param verticalPadding Vertical padding inside each card. Defaults to
 *   [DefaultStylishDimensions.controlVerticalPadding] (12 dp).
 * @param contentSpacing Horizontal gap between slots inside each card.
 *   Defaults to [StylishTheme.dimensions.itemSpacing] (8 dp).
 * @param titleSpacing Vertical gap between title and supporting text inside
 *   each card. Defaults to [StylishTheme.dimensions.inlineSpacing] (4 dp).
 * @param card A composable lambda that renders a single card. Receives the
 *   item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedCardItem], which
 *   delegates to the [StylishConnectedCard] atom with the styling parameters
 *   above.
 *
 * @see ConnectedCardGrid
 * @see StylishConnectedCardRow
 * @see StylishConnectedCardColumn
 * @see DefaultStylishConnectedCardItem
 */
@Composable
public fun StylishConnectedCardGrid(
    items: List<StylishConnectedCardItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    containerColor: Color? = null,
    contentColor: Color? = null,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextMaxLines: Int = 1,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    minHeight: Dp = DefaultStylishDimensions.cardMinHeight,
    horizontalPadding: Dp = DefaultStylishDimensions.controlPadding,
    verticalPadding: Dp = DefaultStylishDimensions.controlVerticalPadding,
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    titleSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    card: StylishConnectedCardItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedCardItem(
            item, itemModifier, shape, edges, corners,
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            titleStyle = titleStyle,
            supportingTextStyle = supportingTextStyle,
            titleMaxLines = titleMaxLines,
            titleOverflow = titleOverflow,
            supportingTextMaxLines = supportingTextMaxLines,
            supportingTextOverflow = supportingTextOverflow,
            minHeight = minHeight,
            horizontalPadding = horizontalPadding,
            verticalPadding = verticalPadding,
            contentSpacing = contentSpacing,
            titleSpacing = titleSpacing,
        )
    },
) {
    ConnectedCardGrid(items, columns, modifier, spacing, card = card)
}

@Preview(name = "Connected card grid", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCardGrid(
                items = List(5) { index ->
                    StylishConnectedCardItem("項目 ${index + 1}", "補足情報")
                },
                columns = 2,
            )
        }
    }
}

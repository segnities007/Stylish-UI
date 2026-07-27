package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardGrid
import com.segnities007.stylishui.theme.StylishTheme

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
 * @param card A composable lambda that renders a single card. Receives the
 *   item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedCardItem], which
 *   delegates to the [StylishConnectedCard] atom.
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
    card: StylishConnectedCardItemContent = ::DefaultStylishConnectedCardItem,
) {
    ConnectedCardGrid(items, columns, modifier, spacing, card)
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

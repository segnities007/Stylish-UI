package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedGridCorners
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless grid of connected list items laid out in equal-width cells across
 * a fixed number of columns (Structure layer).
 *
 * Items are chunked into rows of [columns] items. Within each row every item
 * receives equal weight and stretches to the tallest sibling. Outline edges are
 * drawn on all four sides of every cell; corner radii are computed
 * automatically from each item's absolute index so that only the four outer
 * corners of the entire grid are rounded. When the final row has fewer items
 * than [columns], the remaining items stretch via equal weight to fill the full
 * row width. The actual rendering is delegated to the [listItem] lambda, which
 * receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, animation, semantics, or click handling. It is the headless
 * backbone that the Stylish Finish counterpart `StylishConnectedListItemGrid`
 * consumes by supplying a styled [listItem]. Supply your own [listItem] to
 * render a custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedListItem] data objects describing
 *   each cell.
 * @param columns The number of equal-width columns in the grid. Must be greater
 *   than zero.
 * @param spacing The gap between adjacent items both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param listItem A composable lambda that renders a single cell. Receives the
 *   item data, a modifier (including weight and fill-max-height), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedListItemRow
 * @see ConnectedListItemColumn
 * @see ConnectedListItemContent
 */
@Composable
public fun ConnectedListItemGrid(
    items: List<StylishConnectedListItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    listItem: ConnectedListItemContent,
) {
    require(columns > 0) { "columns must be greater than zero" }
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.chunked(columns)
            .forEachIndexed { rowIndex, rowItems ->
                Row(
                    Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    rowItems.forEachIndexed { columnIndex, item ->
                        val index = rowIndex * columns + columnIndex
                        val corners = connectedGridCorners(index, items.size, columns)
                        listItem(
                            item,
                            Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            connectedShape(corners),
                            ConnectedEdges.All,
                            corners,
                        )
                    }
                }
            }
    }
}

@Preview(name = "Headless connected list item grid", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedListItemGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedListItemGrid(
                items = List(3) { index -> StylishConnectedListItem("Item ${index + 1}", supportingText = "Detail") },
                columns = 2,
            ) { item, itemModifier, shape, edges, corners ->
                Surface(
                    itemModifier.connectedOutline(edges, corners),
                    shape = shape,
                    color = Color.Transparent,
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(item.headline)
                        item.supportingText?.let { Text(it) }
                    }
                }
            }
        }
    }
}

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedGridCorners
import com.segnities007.stylishui.foundation.connectedGridEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless grid of connected chips laid out in equal-width cells across a
 * fixed number of columns (Structure layer).
 *
 * Items are chunked into rows of [columns] chips. Within each row every chip
 * receives equal weight and stretches to the tallest sibling. Outline edges are
 * drawn on all four sides of every cell; corner radii are computed
 * automatically from each item's absolute index so that only the four outer
 * corners of the entire grid are rounded. When the final row has fewer items
 * than [columns], the remaining chips stretch via equal weight to fill the full
 * row width. The actual rendering is delegated to the [chip] lambda, which
 * receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, or animation. It is the headless backbone that the Stylish Finish
 * counterpart `StylishConnectedChipGrid` consumes by supplying a styled [chip].
 * Supply your own [chip] to render a custom skin over the same connected
 * geometry.
 *
 * @param items The list of [StylishConnectedChipItem] data objects describing
 *   each chip.
 * @param columns The number of equal-width columns in the grid. Must be greater
 *   than zero.
 * @param spacing The gap between adjacent chips both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param cornerRadius The radius applied to the outer corners. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param chip A composable lambda that renders a single chip. Receives the item
 *   data, a modifier (including weight and fill-max-height), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedChipRow
 * @see ConnectedChipColumn
 * @see ConnectedChipItemContent
 */
@Composable
public fun ConnectedChipGrid(
    items: List<StylishConnectedChipItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    chip: ConnectedChipItemContent,
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
                        chip(
                            item,
                            Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            connectedShape(corners, cornerRadius),
                            connectedGridEdges(index, items.size, columns),
                            corners,
                        )
                    }
                }
            }
    }
}

@Preview(name = "Headless connected chip grid", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedChipGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedChipGrid(
                items = List(4) { index -> StylishConnectedChipItem("Tag ${index + 1}") },
                columns = 2,
            ) { item, itemModifier, shape, edges, corners ->
                Surface(
                    itemModifier.connectedOutline(edges, corners),
                    shape = shape,
                    color = Color.Transparent,
                ) {
                    Text(item.label, Modifier.padding(8.dp))
                }
            }
        }
    }
}

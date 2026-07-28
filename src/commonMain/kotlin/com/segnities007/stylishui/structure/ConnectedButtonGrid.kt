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
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedGridCorners
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless grid of connected buttons laid out in equal-width cells across a
 * fixed number of columns (Structure layer).
 *
 * Items are chunked into rows of [columns] buttons. Within each row every
 * button receives equal weight and stretches to the tallest sibling. Outline
 * edges are drawn on all four sides of every cell; corner radii are computed
 * automatically from each item's absolute index so that only the four outer
 * corners of the entire grid are rounded. When the final row has fewer items
 * than [columns], the remaining buttons stretch via equal weight to fill the
 * full row width. The actual rendering is delegated to the [button] lambda,
 * which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, animation, or click handling. It is the headless backbone that the
 * Stylish Finish counterpart `StylishConnectedButtonGrid` consumes by supplying
 * a styled [button]. Supply your own [button] to render a custom skin over the
 * same connected geometry.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects describing
 *   each button.
 * @param columns The number of equal-width columns in the grid. Must be greater
 *   than zero.
 * @param cornerRadius The radius applied to the outer corners. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The gap between adjacent buttons both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param button A composable lambda that renders a single button. Receives the
 *   item data, a modifier (including weight and fill-max-height), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedButtonRow
 * @see ConnectedButtonColumn
 * @see ConnectedButtonItemContent
 */
@Composable
public fun ConnectedButtonGrid(
    items: List<StylishConnectedButtonItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    button: ConnectedButtonItemContent,
) {
    require(columns > 0) { "columns must be greater than zero" }
    Column(
        modifier = modifier,
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
                        button(
                            item,
                            Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            connectedShape(corners, cornerRadius),
                            ConnectedEdges.All,
                            corners,
                        )
                    }
                }
            }
    }
}

@Preview(name = "Headless connected button grid", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedButtonGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedButtonGrid(
                items = List(4) { StylishConnectedButtonItem(onClick = {}) { Text("Action") } },
                columns = 2,
            ) { item, itemModifier, shape, edges, corners ->
                Surface(
                    itemModifier.connectedOutline(edges, corners),
                    shape = shape,
                    color = Color.Transparent,
                ) {
                    Row(Modifier.padding(12.dp), content = item.content)
                }
            }
        }
    }
}

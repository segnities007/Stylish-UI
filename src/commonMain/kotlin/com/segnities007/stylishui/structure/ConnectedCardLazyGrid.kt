package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key as runtimeKey
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedGridCorners
import com.segnities007.stylishui.foundation.connectedGridEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless lazily-composed grid of connected cards laid out in equal-width cells across a
 * fixed number of columns (Structure layer).
 *
 * Unlike [ConnectedCardGrid], only the visible rows are composed and rendered: each grid row
 * of [columns] cards becomes a single full-span item of a [LazyVerticalGrid]. The full [items]
 * list is known up front, so the connection geometry is computed exactly as in the eager grid:
 * within each row every card receives equal weight, corner radii are computed from each item's
 * absolute index so that only the four outer corners of the entire grid are rounded, and a
 * partially filled final row stretches to fill the full row width. The actual rendering is
 * delegated to the [card] lambda, which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors, elevation, or
 * animation. It is the headless backbone that the Stylish Finish counterpart
 * `StylishConnectedCardLazyGrid` consumes by supplying a styled [card]. Supply your own [card]
 * to render a custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects describing each card.
 * @param columns The number of equal-width columns in the grid. Must be greater than zero.
 * @param spacing The gap between adjacent cards both horizontally and vertically. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding [PaddingValues] applied around the entire lazy grid. Defaults to
 *   [StylishTheme.dimensions.controlPadding] horizontal padding.
 * @param key Optional stable and unique key factory used to preserve item state across moves.
 * @param listState [LazyGridState] controlling scroll position. Defaults to
 *   [rememberLazyGridState]. Supply a hoisted state to observe scroll offset or
 *   programmatically scroll.
 * @param card A composable lambda that renders a single card. Receives the item data, a
 *   modifier (fill-max-width within an equal-weight cell), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedCardGrid
 * @see ConnectedCardRow
 * @see ConnectedCardItemContent
 */
@Composable
public fun ConnectedCardLazyGrid(
    items: List<StylishConnectedCardItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = StylishTheme.dimensions.controlPadding),
    key: ((StylishConnectedCardItem) -> Any)? = null,
    listState: LazyGridState = rememberLazyGridState(),
    card: ConnectedCardItemContent,
) {
    require(columns > 0) { "columns must be greater than zero" }
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.chunked(columns)
            .forEachIndexed { rowIndex, rowItems ->
                val rowKey = key?.invoke(rowItems.first()) ?: rowIndex
                item(span = { GridItemSpan(columns) }, key = rowKey) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                    ) {
                        rowItems.forEachIndexed { columnIndex, item ->
                            val index = rowIndex * columns + columnIndex
                            val corners = connectedGridCorners(index, items.size, columns)
                            runtimeKey(key?.invoke(item) ?: index) {
                                Box(Modifier.weight(1f)) {
                                    card(
                                        item,
                                        Modifier.fillMaxWidth(),
                                        connectedShape(corners, StylishTheme.dimensions.connectedCornerRadius),
                                        connectedGridEdges(index, items.size, columns),
                                        corners,
                                    )
                                }
                            }
                        }
                    }
                }
            }
    }
}

@Preview(name = "Headless lazy connected card grid", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun ConnectedCardLazyGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedCardLazyGrid(
                items = List(6) { index ->
                    StylishConnectedCardItem("Card ${index + 1}", "Detail")
                },
                columns = 2,
            ) { item, itemModifier, shape, edges, corners ->
                Surface(
                    itemModifier.connectedOutline(edges, corners),
                    shape = shape,
                    color = Color.Transparent,
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(item.title)
                        Text(item.supportingText)
                    }
                }
            }
        }
    }
}

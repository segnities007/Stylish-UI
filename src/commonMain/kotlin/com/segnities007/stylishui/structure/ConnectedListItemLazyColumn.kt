package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedColumnCorners
import com.segnities007.stylishui.foundation.connectedColumnEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A headless lazily-composed vertically connected list-item layout (Structure layer).
 *
 * Each item fills the full available width, but unlike [ConnectedListItemColumn] the items
 * are emitted into a [LazyColumn] so that only the visible rows are composed and rendered.
 * Because the full [items] list is known up front, the connection geometry (outline edges and
 * corner radii) is computed exactly as in the eager layout: the first item rounds only its
 * top corners, the last item rounds only its bottom corners, and middle items have square
 * corners with shared horizontal outlines. The actual rendering is delegated to the [listItem]
 * lambda, which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors, elevation, or
 * animation. It is the headless backbone that the Stylish Finish counterpart
 * `StylishConnectedListItemLazyColumn` consumes by supplying a styled [listItem]. Supply your
 * own [listItem] to render a custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedListItem] data objects describing each item.
 * @param spacing The vertical gap between adjacent items. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding [PaddingValues] applied around the entire lazy list. Defaults to
 *   [DefaultStylishDimensions.controlPadding] horizontal padding.
 * @param listState [LazyListState] controlling scroll position. Defaults to
 *   [rememberLazyListState]. Supply a hoisted state to observe scroll offset or
 *   programmatically scroll.
 * @param listItem A composable lambda that renders a single item. Receives the item data, a
 *   modifier (fill-max-width), the connected [Shape], the outline [ConnectedEdges], and the
 *   outline [ConnectedCorners].
 *
 * @see ConnectedListItemColumn
 * @see ConnectedListItemGrid
 * @see ConnectedListItemContent
 */
@Composable
public fun ConnectedListItemLazyColumn(
    items: List<StylishConnectedListItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = DefaultStylishDimensions.controlPadding),
    listState: LazyListState = rememberLazyListState(),
    listItem: ConnectedListItemContent,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        itemsIndexed(items) { index, item ->
            val corners = connectedColumnCorners(index, items.size)
            listItem(
                item,
                Modifier.fillMaxWidth(),
                connectedShape(corners, StylishTheme.dimensions.connectedCornerRadius),
                connectedColumnEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless lazy connected list item column", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun ConnectedListItemLazyColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedListItemLazyColumn(
                items = List(8) { index ->
                    StylishConnectedListItem("Item ${index + 1}", supportingText = "Detail")
                },
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

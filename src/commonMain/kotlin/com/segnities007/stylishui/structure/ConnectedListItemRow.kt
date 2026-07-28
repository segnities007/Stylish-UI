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
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless horizontally connected list-item layout (Structure layer).
 *
 * Each item receives equal weight within the row and stretches to the tallest
 * sibling via [IntrinsicSize.Min]. Outline edges and corner radii are computed
 * automatically from each item's index: the first item rounds only its leading
 * corners, the last item rounds only its trailing corners, and middle items
 * have square corners with shared vertical outlines. The actual rendering is
 * delegated to the [listItem] lambda, which receives the pre-computed
 * connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, animation, semantics, or click handling. It is the headless
 * backbone that the Stylish Finish counterpart `StylishConnectedListItemRow`
 * consumes by supplying a styled [listItem]. Supply your own [listItem] to
 * render a custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedListItem] data objects describing
 *   each row.
 * @param spacing The horizontal gap between adjacent items. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param listItem A composable lambda that renders a single row. Receives the
 *   item data, a modifier (including weight and fill-max-height), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedListItemColumn
 * @see ConnectedListItemGrid
 * @see ConnectedListItemContent
 */
@Composable
public fun ConnectedListItemRow(
    items: List<StylishConnectedListItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    listItem: ConnectedListItemContent,
) {
    Row(
        modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            listItem(
                item,
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                connectedShape(corners),
                connectedRowEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected list item row", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedListItemRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedListItemRow(
                listOf(
                    StylishConnectedListItem("Theme", supportingText = "System"),
                    StylishConnectedListItem("Notifications", supportingText = "On"),
                ),
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

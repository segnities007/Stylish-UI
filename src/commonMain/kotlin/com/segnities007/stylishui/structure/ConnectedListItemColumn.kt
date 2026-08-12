package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

/**
 * A headless vertically connected list-item layout (Structure layer).
 *
 * Each item fills the full available width. Outline edges and corner radii are
 * computed automatically from each item's index: the first item rounds only its
 * top corners, the last item rounds only its bottom corners, and middle items
 * have square corners with shared horizontal outlines. The actual rendering is
 * delegated to the [listItem] lambda, which receives the pre-computed
 * connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, or animation. It is the headless backbone that the Stylish Finish
 * counterpart `StylishConnectedListItemColumn` consumes by supplying a styled
 * [listItem]. Supply your own [listItem] to render a custom skin over the same
 * connected geometry.
 *
 * @param items The list of [StylishConnectedListItem] data objects describing
 *   each item.
 * @param spacing The vertical gap between adjacent items. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param cornerRadius The radius applied to the outer corners. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param listItem A composable lambda that renders a single item. Receives the
 *   item data, a modifier (fill-max-width), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedListItemRow
 * @see ConnectedListItemGrid
 * @see ConnectedListItemContent
 */
@Composable
public fun ConnectedListItemColumn(
    items: List<StylishConnectedListItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    listItem: ConnectedListItemContent,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedColumnCorners(index, items.size)
            listItem(
                item,
                Modifier.fillMaxWidth(),
                connectedShape(corners, cornerRadius),
                connectedColumnEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected list item column", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedListItemColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedListItemColumn(
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

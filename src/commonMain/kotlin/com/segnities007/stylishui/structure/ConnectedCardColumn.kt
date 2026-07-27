package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.connectedColumnCorners
import com.segnities007.stylishui.foundation.connectedColumnEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless vertically connected card layout (Structure layer).
 *
 * Outline edges and corner radii are computed automatically from each item's
 * index: the first card rounds only its top corners, the last card rounds only
 * its bottom corners, and middle cards have square corners with shared
 * horizontal outlines. The actual rendering is delegated to the [card] lambda,
 * which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual decisions — no colors, elevation, or
 * animation. It is the headless backbone that the Stylish Finish counterpart
 * `StylishConnectedCardColumn` consumes by supplying a styled [card]. Supply
 * your own [card] to render a custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects describing
 *   each card.
 * @param spacing The vertical gap between adjacent cards. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param card A composable lambda that renders a single card. Receives the item
 *   data, a modifier, the connected [Shape], the outline [ConnectedEdges], and
 *   the outline [ConnectedCorners].
 *
 * @see ConnectedCardRow
 * @see ConnectedCardGrid
 * @see ConnectedCardItemContent
 */
@Composable
public fun ConnectedCardColumn(
    items: List<StylishConnectedCardItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    card: ConnectedCardItemContent,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedColumnCorners(index, items.size)
            card(
                item,
                Modifier,
                connectedShape(corners),
                connectedColumnEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected card column", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedCardColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedCardColumn(
                listOf(
                    StylishConnectedCardItem("Today", "3 events"),
                    StylishConnectedCardItem("Tomorrow", "1 event"),
                ),
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

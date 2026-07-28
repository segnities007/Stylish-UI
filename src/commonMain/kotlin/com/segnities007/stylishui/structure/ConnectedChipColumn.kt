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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.foundation.connectedColumnCorners
import com.segnities007.stylishui.foundation.connectedColumnEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless vertically connected chip layout (Structure layer).
 *
 * Outline edges and corner radii are computed automatically from each item's
 * index so that the first chip rounds only its top corners, the last chip
 * rounds only its bottom corners, and middle chips have square corners with
 * shared horizontal outlines. Every chip fills the full available width. The
 * actual rendering is delegated to the [chip] lambda, which receives the
 * pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * selection animation, semantics, or click handling. It is the headless
 * backbone that the Stylish Finish counterpart `StylishConnectedChipColumn`
 * consumes by supplying a styled [chip]. Supply your own [chip] to render a
 * custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedChipItem] data objects describing
 *   each chip.
 * @param spacing The vertical gap between adjacent chips. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param chip A composable lambda that renders a single chip. Receives the item
 *   data, a modifier (fill-max-width), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedChipRow
 * @see ConnectedChipGrid
 * @see ConnectedChipItemContent
 */
@Composable
public fun ConnectedChipColumn(
    items: List<StylishConnectedChipItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    chip: ConnectedChipItemContent,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedColumnCorners(index, items.size)
            chip(
                item,
                Modifier.fillMaxWidth(),
                connectedShape(corners),
                connectedColumnEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected chip column", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedChipColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedChipColumn(
                listOf(
                    StylishConnectedChipItem("All", selected = true),
                    StylishConnectedChipItem("Work"),
                    StylishConnectedChipItem("Personal"),
                ),
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

package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless horizontally connected chip layout (Structure layer).
 *
 * Outline edges and corner radii are computed automatically from each item's
 * index so that the first chip rounds only its leading corners, the last chip
 * rounds only its trailing corners, and middle chips have square corners with
 * shared vertical outlines. When [fillWidth] is `false` (the default) the row
 * scrolls horizontally; when `true`, every chip receives equal weight and the
 * row fills the available width. The actual rendering is delegated to the
 * [chip] lambda, which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * selection animation, semantics, or click handling. It is the headless
 * backbone that the Stylish Finish counterpart `StylishConnectedChipRow`
 * consumes by supplying a styled [chip]. Supply your own [chip] to render a
 * custom skin over the same connected geometry.
 *
 * @param items The list of [StylishConnectedChipItem] data objects describing
 *   each chip.
 * @param spacing The horizontal gap between adjacent chips. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param fillWidth When `true`, chips share the available width equally instead
 *   of scrolling. Defaults to `false`.
 * @param chip A composable lambda that renders a single chip. Receives the item
 *   data, a modifier, the connected [Shape], the outline [ConnectedEdges], and
 *   the outline [ConnectedCorners].
 *
 * @see ConnectedChipColumn
 * @see ConnectedChipGrid
 * @see ConnectedChipItemContent
 */
@Composable
public fun ConnectedChipRow(
    items: List<StylishConnectedChipItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    fillWidth: Boolean = false,
    chip: ConnectedChipItemContent,
) {
    Row(
        modifier = if (fillWidth) modifier.fillMaxWidth()
        else modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            chip(
                item,
                Modifier.let { if (fillWidth) it.weight(1f) else it },
                connectedShape(corners),
                connectedRowEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected chip row", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedChipRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedChipRow(
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

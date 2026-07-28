package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.foundation.connectedColumnCorners
import com.segnities007.stylishui.foundation.connectedColumnEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless vertically connected button layout (Structure layer).
 *
 * Each button fills the full available width. Outline edges and corner radii
 * are computed automatically from each item's index: the first button rounds
 * only its top corners, the last button rounds only its bottom corners, and
 * middle buttons have square corners. The actual rendering is delegated to the
 * [button] lambda, which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, animation, or click handling. It is the headless backbone that the
 * Stylish Finish counterpart `StylishConnectedButtonColumn` consumes by
 * supplying a styled [button]. Supply your own [button] to render a custom skin
 * over the same connected geometry.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects describing
 *   each button.
 * @param cornerRadius The radius applied to the outer corners. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The vertical gap between adjacent buttons. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param button A composable lambda that renders a single button. Receives the
 *   item data, a modifier (fill-max-width), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedButtonRow
 * @see ConnectedButtonGrid
 * @see ConnectedButtonItemContent
 */
@Composable
public fun ConnectedButtonColumn(
    items: List<StylishConnectedButtonItem>,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    button: ConnectedButtonItemContent,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedColumnCorners(index, items.size)
            button(
                item,
                Modifier.fillMaxWidth(),
                connectedShape(corners, cornerRadius),
                connectedColumnEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected button column", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedButtonColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedButtonColumn(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("Export") },
                    StylishConnectedButtonItem(onClick = {}) { Text("Delete") },
                ),
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

package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
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
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless horizontally connected button layout (Structure layer).
 *
 * Each button occupies an equal weight within the row and stretches to the
 * tallest sibling via [IntrinsicSize.Min]. Outline edges and corner radii are
 * computed automatically from each item's index: the first button rounds only
 * its leading corners, the last button rounds only its trailing corners, and
 * middle buttons have square corners. The actual rendering is delegated to the
 * [button] lambda, which receives the pre-computed connection geometry.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, or animation. It is the headless backbone that the Stylish Finish
 * counterpart `StylishConnectedButtonRow` consumes by supplying a styled
 * [button]. Supply your own [button] to render a custom skin over the same
 * connected geometry.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects describing
 *   each button.
 * @param cornerRadius The radius applied to the outer corners. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The horizontal gap between adjacent buttons. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param button A composable lambda that renders a single button. Receives the
 *   item data, a modifier (including weight and fill-max-height), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see ConnectedButtonColumn
 * @see ConnectedButtonGrid
 * @see ConnectedButtonItemContent
 */
@Composable
public fun ConnectedButtonRow(
    items: List<StylishConnectedButtonItem>,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    button: ConnectedButtonItemContent,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            button(
                item,
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                connectedShape(corners, cornerRadius),
                connectedRowEdges(index, items.size),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected button row", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedButtonRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedButtonRow(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("Edit") },
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

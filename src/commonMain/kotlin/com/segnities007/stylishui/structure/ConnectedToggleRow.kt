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
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless horizontally connected toggle row layout (Structure layer).
 *
 * Arranges a list of generic items as a segmented toggle control where at most
 * one item is selected at a time. Each item occupies an equal weight within the
 * row and stretches to the tallest sibling via [IntrinsicSize.Min]. Outline
 * edges and corner radii are computed automatically from each item's index: the
 * first button rounds only its leading corners, the last button rounds only its
 * trailing corners, and middle buttons have square corners. The actual rendering
 * is delegated to the [toggle] lambda, which receives the pre-computed
 * connection geometry and selection state.
 *
 * This component makes **no** visual or interactive decisions — no colors,
 * elevation, or animation. It is the headless backbone that the Stylish Finish
 * counterpart `StylishConnectedToggleRow` consumes by supplying a styled
 * [toggle]. Supply your own [toggle] to render a custom skin over the same
 * connected geometry.
 *
 * @param T The type of each item in the row.
 * @param items The list of items to display as toggle segments.
 * @param selectedIndex The index of the currently selected item, or `null` if
 *   no item is selected.
 * @param onSelectedChange Callback invoked with the index of the tapped item.
 *   The callback receives the new index regardless of whether it matches the
 *   current selection; consumers decide whether to allow deselection.
 * @param modifier The modifier applied to the root [Row].
 * @param cornerRadius The radius applied to the outer corners of the first and
 *   last items. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The horizontal gap between adjacent items. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param toggle A composable lambda that renders a single toggle item. Receives
 *   the item data, its index, whether it is currently selected, a modifier
 *   (including weight and fill-max-height), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners].
 *
 * @see StylishConnectedToggleRow
 */
@Composable
public fun <T> ConnectedToggleRow(
    items: List<T>,
    selectedIndex: Int?,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    toggle: @Composable (
        item: T,
        index: Int,
        isSelected: Boolean,
        modifier: Modifier,
        shape: Shape,
        edges: ConnectedEdges,
        corners: ConnectedCorners,
    ) -> Unit,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            toggle(
                item,
                index,
                index == selectedIndex,
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

@Preview(name = "Headless connected toggle row", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedToggleRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            ConnectedToggleRow(
                items = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                selectedIndex = 2,
                onSelectedChange = {},
            ) { _, index, isSelected, itemModifier, shape, edges, corners ->
                Surface(
                    itemModifier.connectedOutline(edges, corners),
                    shape = shape,
                    color = if (isSelected) Color.Black else Color.Transparent,
                    contentColor = if (isSelected) Color.White else Color.Black,
                ) {
                    Row(Modifier.padding(12.dp)) {
                        Text("Day $index")
                    }
                }
            }
        }
    }
}

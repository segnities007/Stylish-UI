package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.structure.ConnectedButtonGrid
import com.segnities007.stylishui.structure.ConnectedButtonItemContent
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A grid of connected buttons laid out in equal-width cells across a fixed
 * number of columns, with shared outlines and corner radii.
 *
 * This is the Finish-layer component: it supplies the Stylish button rendering
 * ([DefaultStylishConnectedButton]) to the headless Structure layout
 * [ConnectedButtonGrid], which owns arrangement and connection geometry. Items
 * are chunked into rows of [columns] buttons; within each row every button
 * receives equal weight and stretches to the tallest sibling. Outline edges are
 * drawn on all four sides of every cell; corner radii are computed
 * automatically from each item's absolute index so that only the four outer
 * corners of the entire grid are rounded. When the final row has fewer items
 * than [columns], the remaining buttons stretch via equal weight to fill the
 * full row width. Items whose [StylishConnectedButtonItem.onClick] is `null` or
 * whose [StylishConnectedButtonItem.enabled] is `false` are rendered in a
 * disabled state and do not respond to interaction. Pass a custom [button] to
 * override the Stylish rendering while keeping the connected geometry.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects that
 *   describe each button's content, click action, colors, and enabled state.
 * @param columns The number of equal-width columns in the grid. Must be
 *   greater than zero.
 * @param cornerRadius The radius applied to the outer corners of the grid.
 *   Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The gap between adjacent buttons both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding The inner padding of each button. Defaults to
 *   12 dp horizontal and 12 dp vertical.
 * @param defaultColors The [ButtonColors] used for every item whose
 *   [StylishConnectedButtonItem.colors] is `null`.
 * @param button A composable lambda that renders a single button. Receives
 *   the item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedButton], dressed
 *   in the Stylish look with [cornerRadius], [contentPadding], and
 *   [defaultColors].
 *
 * @see ConnectedButtonGrid
 * @see StylishConnectedButtonRow
 * @see StylishConnectedButtonColumn
 * @see DefaultStylishConnectedButton
 */
@Composable
public fun StylishConnectedButtonGrid(
    items: List<StylishConnectedButtonItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
    defaultColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    button: ConnectedButtonItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedButton(
            item, itemModifier, shape, edges, corners, cornerRadius, contentPadding, defaultColors,
        )
    },
) {
    ConnectedButtonGrid(items, columns, modifier, cornerRadius, spacing, button)
}

@Preview(name = "Connected button grid", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedButtonGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedButtonGrid(
                items = listOf(
                    StylishConnectedButtonItem({}, leadingContent = {
                        Icon(Icons.Default.Add, null)
                    }) { Text("追加\n") },
                    StylishConnectedButtonItem({}, leadingContent = {
                        Icon(Icons.Default.Edit, null)
                    }) { Text("編集") },
                    StylishConnectedButtonItem({}, leadingContent = {
                        Icon(Icons.Default.Edit, null)
                    }) { Text("編集") },
                    StylishConnectedButtonItem({}) { Text("その他") },
                ),
                columns = 2,
            )
        }
    }
}

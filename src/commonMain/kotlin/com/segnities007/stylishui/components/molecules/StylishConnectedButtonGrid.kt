package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedGridCorners
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A grid of connected buttons laid out in equal-width cells across a fixed
 * number of columns, with shared outlines and corner radii.
 *
 * Items are chunked into rows of [columns] buttons. Within each row every
 * button receives equal weight and stretches to the tallest sibling. Outline
 * edges are drawn on all four sides of every cell; corner radii are computed
 * automatically from each item's absolute index so that only the four outer
 * corners of the entire grid are rounded. If the final row has fewer items
 * than [columns], invisible spacers preserve column alignment. Items whose
 * [StylishConnectedButtonItem.onClick] is `null` or whose
 * [StylishConnectedButtonItem.enabled] is `false` are rendered in a disabled
 * state and do not respond to interaction.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects that
 *   describe each button's content, click action, colors, and enabled state.
 * @param columns The number of equal-width columns in the grid. Must be
 *   greater than zero.
 * @param cornerRadius The radius applied to the outer corners of the grid.
 *   Defaults to 12 dp.
 * @param spacing The gap between adjacent buttons both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding The inner padding of each button. Defaults to
 *   12 dp horizontal and 12 dp vertical.
 * @param defaultColors The [ButtonColors] used for every item whose
 *   [StylishConnectedButtonItem.colors] is `null`. Defaults to a grouped
 *   container background ([MaterialTheme.stylishComponentColors.groupedContainer])
 *   with [MaterialTheme.colorScheme.onSurface] content.
 *
 * @see StylishConnectedButtonRow
 * @see StylishConnectedButtonColumn
 */
@Composable
public fun StylishConnectedButtonGrid(
    items: List<StylishConnectedButtonItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
    defaultColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
) {
    require(columns > 0) { "columns must be greater than zero" }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.chunked(columns)
            .forEachIndexed { rowIndex, rowItems ->
                Row(
                    Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    rowItems.forEachIndexed { columnIndex, item ->
                        val index = rowIndex * columns + columnIndex
                        val corners = connectedGridCorners(index, items.size, columns)
                        val actionable = isActionable(
                            enabled = item.enabled,
                            hasClickAction = item.onClick != null,
                        )
                        Button(
                            onClick = { item.onClick?.invoke() },
                            enabled = actionable,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .heightIn(min = 52.dp)
                                .connectedOutline(
                                    edges = ConnectedEdges.All,
                                    corners = corners,
                                    cornerRadius = cornerRadius,
                                ),
                            shape = connectedShape(
                                corners,
                                cornerRadius = cornerRadius,
                            ),
                            colors = item.colors ?: defaultColors,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = StylishTheme.dimensions.interactiveElevation,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp,
                            ),
                            contentPadding = contentPadding,
                        ) {
                            StylishButtonSlot(item.leadingContent, Alignment.CenterStart)
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                content = item.content,
                            )
                            StylishButtonSlot(item.trailingContent, Alignment.CenterEnd)
                        }
                    }
                    if (rowItems.size == columns) {
                        repeat(columns - rowItems.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
    }
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

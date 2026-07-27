package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A horizontally connected group of buttons that share outlines and corner
 * radii to form a segmented-control appearance.
 *
 * Each button occupies an equal weight within the row and stretches to the
 * tallest sibling via [IntrinsicSize.Min]. Outline edges and corner radii are
 * computed automatically from each item's index: the first button rounds only
 * its leading corners, the last button rounds only its trailing corners, and
 * middle buttons have square corners with shared vertical outlines. Items
 * whose [StylishConnectedButtonItem.onClick] is `null` or whose
 * [StylishConnectedButtonItem.enabled] is `false` are rendered in a disabled
 * state and do not respond to interaction.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects that
 *   describe each button's content, click action, colors, and enabled state.
 * @param cornerRadius The radius applied to the outer corners of the first and
 *   last buttons. Defaults to 12 dp.
 * @param spacing The horizontal gap between adjacent buttons. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding The inner padding of each button. Defaults to
 *   12 dp horizontal and 12 dp vertical.
 * @param defaultColors The [ButtonColors] used for every item whose
 *   [StylishConnectedButtonItem.colors] is `null`. Defaults to a grouped
 *   container background ([MaterialTheme.stylishComponentColors.groupedContainer])
 *   with [MaterialTheme.colorScheme.onSurface] content.
 *
 * @see StylishConnectedButtonColumn
 * @see StylishConnectedButtonGrid
 */
@Composable
public fun StylishConnectedButtonRow(
    items: List<StylishConnectedButtonItem>,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
    defaultColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
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
                        edges = connectedRowEdges(index, items.size),
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
    }
}

@Preview(name = "Connected button row", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedButtonRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedButtonRow(
                items = listOf(
                    StylishConnectedButtonItem(
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Edit, null) },
                    ) { Text("編集") },
                    StylishConnectedButtonItem(
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Delete, null) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        ),
                    ) { Text("削除") },
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

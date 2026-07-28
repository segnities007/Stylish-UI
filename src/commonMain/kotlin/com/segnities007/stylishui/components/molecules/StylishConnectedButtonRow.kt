package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.segnities007.stylishui.structure.ConnectedButtonRow
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A horizontally connected group of buttons that share outlines and corner
 * radii to form a segmented-control appearance.
 *
 * This is the Finish-layer component: it supplies the Stylish button rendering
 * ([DefaultStylishConnectedButton]) to the headless Structure layout
 * [ConnectedButtonRow], which owns arrangement and connection geometry. Each
 * button occupies an equal weight within the row and stretches to the tallest
 * sibling. Outline edges and corner radii are computed automatically from each
 * item's index. Items whose [StylishConnectedButtonItem.onClick] is `null` or
 * whose [StylishConnectedButtonItem.enabled] is `false` are rendered in a
 * disabled state and do not respond to interaction.
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
 *   [StylishConnectedButtonItem.colors] is `null`.
 *
 * @see ConnectedButtonRow
 * @see StylishConnectedButtonColumn
 * @see StylishConnectedButtonGrid
 * @see DefaultStylishConnectedButton
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
    ConnectedButtonRow(items, modifier, cornerRadius, spacing) { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedButton(
            item, itemModifier, shape, edges, corners, cornerRadius, contentPadding, defaultColors,
        )
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

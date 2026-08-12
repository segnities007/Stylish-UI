package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
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
import com.segnities007.stylishui.structure.ConnectedButtonColumn
import com.segnities007.stylishui.structure.ConnectedButtonItemContent
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A vertically connected group of buttons that share outlines and corner
 * radii to form a segmented-control appearance.
 *
 * This is the Finish-layer component: it supplies the Stylish button rendering
 * ([DefaultStylishConnectedButton]) to the headless Structure layout
 * [ConnectedButtonColumn], which owns arrangement and connection geometry. Each
 * button fills the full available width. Outline edges and corner radii are
 * computed automatically from each item's index. Leading and trailing slots are
 * given a minimum width of 40 dp so that icons align consistently across rows.
 * Items whose [StylishConnectedButtonItem.onClick] is `null` or whose
 * [StylishConnectedButtonItem.enabled] is `false` are rendered in a disabled
 * state and do not respond to interaction. Pass a custom [button] to override
 * the Stylish rendering while keeping the connected geometry.
 *
 * @param items The list of [StylishConnectedButtonItem] data objects that
 *   describe each button's content, click action, colors, and enabled state.
 * @param cornerRadius The radius applied to the outer corners of the first and
 *   last buttons. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The vertical gap between adjacent buttons. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding The inner padding of each button. Defaults to
 *   16 dp horizontal and 12 dp vertical.
 * @param defaultColors The [ButtonColors] used for every item whose
 *   [StylishConnectedButtonItem.colors] is `null`.
 * @param button A composable lambda that renders a single button. Receives
 *   the item data, a modifier (including fill-max-width), the connected
 *   [Shape], the outline [ConnectedEdges], and the outline [ConnectedCorners].
 *   Defaults to [DefaultStylishConnectedButton], dressed in the Stylish look
 *   with [cornerRadius], [contentPadding], [defaultColors], and a 40 dp slot
 *   minimum width.
 *
 * @see ConnectedButtonColumn
 * @see StylishConnectedButtonRow
 * @see StylishConnectedButtonGrid
 * @see DefaultStylishConnectedButton
 */
@Composable
public fun StylishConnectedButtonColumn(
    items: List<StylishConnectedButtonItem>,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    defaultColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    button: ConnectedButtonItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedButton(
            item, itemModifier, shape, edges, corners, cornerRadius, contentPadding, defaultColors,
            slotMinWidth = 40.dp,
        )
    },
) {
    ConnectedButtonColumn(items, modifier, cornerRadius, spacing, button)
}

@Preview(name = "Connected buttons", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedButtonColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedButtonColumn(
                items = listOf(
                    StylishConnectedButtonItem(
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.FileDownload, null) },
                        trailingContent = {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                        },
                    ) { Text("書き出す") },
                    StylishConnectedButtonItem(
                        onClick = {},
                        leadingContent = { Icon(Icons.Default.Delete, null) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        ),
                    ) { Text("削除する") },
                    StylishConnectedButtonItem(
                        onClick = {},
                        enabled = false,
                    ) { Text("利用できません") },
                ),
            )
        }
    }
}

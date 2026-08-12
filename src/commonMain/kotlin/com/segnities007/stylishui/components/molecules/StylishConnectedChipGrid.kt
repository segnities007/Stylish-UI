package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.structure.ConnectedChipGrid
import com.segnities007.stylishui.structure.ConnectedChipItemContent
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A grid of connected selectable chips laid out in equal-width cells across a
 * fixed number of columns, with animated selection-state color transitions —
 * suited for tag clouds, category pickers, or multi-select filter grids.
 *
 * This is the Finish-layer component: it supplies the Stylish chip rendering
 * ([DefaultStylishConnectedChip]) to the headless Structure layout
 * [ConnectedChipGrid], which owns arrangement and connection geometry. Items
 * are chunked into rows of [columns] chips; within each row every chip receives
 * equal weight and stretches to the tallest sibling. Outline edges are drawn on
 * all four sides of every cell; corner radii are computed automatically from
 * each item's absolute index so that only the four outer corners of the entire
 * grid are rounded. Tapping an actionable chip triggers a haptic feedback pulse
 * and animates the container/content colors over 180 ms. Chips are assigned
 * `Role.Tab` semantics with the `selected` state reflected from
 * [StylishConnectedChipItem.selected]. Pass a custom [chip] to override the
 * Stylish rendering while keeping the connected geometry.
 *
 * @param items The list of [StylishConnectedChipItem] data objects that
 *   describe each chip's label, selection state, click action, and optional
 *   leading/trailing slot content.
 * @param columns The number of equal-width columns in the grid. Must be
 *   greater than zero.
 * @param spacing The gap between adjacent chips both horizontally and
 *   vertically. Defaults to [StylishTheme.dimensions.connectedSpacing]
 *   (3 dp).
 * @param labelMaxLines Maximum number of lines for the chip label text.
 *   Defaults to 1.
 * @param labelOverflow The [TextOverflow] strategy for the chip label when it
 *   exceeds [labelMaxLines]. Defaults to [TextOverflow.Ellipsis].
 * @param labelStyle The [TextStyle] applied to each chip's label. Defaults to
 *   [MaterialTheme.typography.labelLarge].
 * @param selectedContainerColor The background color of a selected chip.
 *   Defaults to [MaterialTheme.colorScheme.primary].
 * @param selectedContentColor The content color of a selected chip. Defaults
 *   to [MaterialTheme.colorScheme.onPrimary].
 * @param unselectedContainerColor The background color of an unselected chip.
 *   Defaults to [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param unselectedContentColor The content color of an unselected chip.
 *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param contentPadding The inner padding of each chip. Defaults to 14 dp
 *   horizontal and 10 dp vertical.
 * @param contentSpacing The horizontal gap between the leading slot, label,
 *   and trailing slot inside each chip. Defaults to 6 dp.
 * @param chip A composable lambda that renders a single chip. Receives the
 *   item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedChip], dressed in
 *   the Stylish look with the label, color, and padding parameters above.
 *
 * @see ConnectedChipGrid
 * @see StylishConnectedChipRow
 * @see StylishConnectedChipColumn
 * @see DefaultStylishConnectedChip
 */
@Composable
public fun StylishConnectedChipGrid(
    items: List<StylishConnectedChipItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color = MaterialTheme.stylishComponentColors.groupedContainer,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing: Dp = 6.dp,
    chip: ConnectedChipItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedChip(
            item, itemModifier, shape, edges, corners, labelMaxLines, labelOverflow, labelStyle,
            selectedContainerColor, selectedContentColor, unselectedContainerColor,
            unselectedContentColor, contentPadding, contentSpacing,
        )
    },
) {
    ConnectedChipGrid(items, columns, modifier, spacing, chip = chip)
}

@Preview(name = "Connected chip grid", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedChipGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedChipGrid(
                items = listOf(
                    StylishConnectedChipItem("すべて", {}, selected = true) {
                        Icon(Icons.Default.Check, null)
                    },
                    StylishConnectedChipItem("仕事", {}),
                    StylishConnectedChipItem("個人", {}),
                    StylishConnectedChipItem("アイデア", {}),
                    StylishConnectedChipItem("旅行", {}),
                ),
                columns = 2,
            )
        }
    }
}

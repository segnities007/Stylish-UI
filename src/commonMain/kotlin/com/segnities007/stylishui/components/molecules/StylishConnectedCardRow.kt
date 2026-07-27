package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardRow
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontally connected group of cards that share outlines and corner
 * radii, typically used for side-by-side numeric summaries or KPI tiles.
 *
 * This is the Finish-layer component: it supplies the Stylish card rendering
 * ([DefaultStylishConnectedCardItem]) to the headless Structure layout
 * [ConnectedCardRow], which owns arrangement and connection geometry. Each card
 * receives equal weight within the row and stretches to the tallest sibling.
 * Outline edges and corner radii are computed automatically from each item's
 * index: the first card rounds only its leading corners, the last card rounds
 * only its trailing corners, and middle cards have square corners with shared
 * vertical outlines. Pass a custom [card] to override the Stylish rendering
 * while keeping the connected geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects that
 *   describe each card's title, supporting text, click actions, and slot
 *   content.
 * @param spacing The horizontal gap between adjacent cards. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param card A composable lambda that renders a single card. Receives the
 *   item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedCardItem], which
 *   delegates to the [StylishConnectedCard] atom.
 *
 * @see ConnectedCardRow
 * @see StylishConnectedCardColumn
 * @see StylishConnectedCardGrid
 * @see DefaultStylishConnectedCardItem
 */
@Composable
public fun StylishConnectedCardRow(
    items: List<StylishConnectedCardItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    card: StylishConnectedCardItemContent = ::DefaultStylishConnectedCardItem,
) {
    ConnectedCardRow(items, modifier, spacing, card)
}

@Preview(name = "Connected card row", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCardRow(
                listOf(
                    StylishConnectedCardItem("12", "メモ"),
                    StylishConnectedCardItem("3", "お気に入り"),
                ),
            )
        }
    }
}

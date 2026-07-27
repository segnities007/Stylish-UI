package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardColumn
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A vertically connected group of cards that share outlines and corner radii,
 * typically used for grouped settings sections or stacked informational cards.
 *
 * This is the Finish-layer component: it supplies the Stylish card rendering
 * ([DefaultStylishConnectedCardItem]) to the headless Structure layout
 * [ConnectedCardColumn], which owns arrangement and connection geometry.
 * Outline edges and corner radii are computed automatically from each item's
 * index: the first card rounds only its top corners, the last card rounds only
 * its bottom corners, and middle cards have square corners with shared
 * horizontal outlines. Pass a custom [card] to override the Stylish rendering
 * while keeping the connected geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects that
 *   describe each card's title, supporting text, click actions, and slot
 *   content.
 * @param spacing The vertical gap between adjacent cards. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param card A composable lambda that renders a single card. Receives the
 *   item data, a modifier, the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners]. Defaults to
 *   [DefaultStylishConnectedCardItem], which delegates to the
 *   [StylishConnectedCard] atom.
 *
 * @see ConnectedCardColumn
 * @see StylishConnectedCardRow
 * @see StylishConnectedCardGrid
 * @see DefaultStylishConnectedCardItem
 */
@Composable
public fun StylishConnectedCardColumn(
    items: List<StylishConnectedCardItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    card: StylishConnectedCardItemContent = ::DefaultStylishConnectedCardItem,
) {
    ConnectedCardColumn(items, modifier, spacing, card)
}

@Preview(name = "Connected card column", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCardColumn(
                listOf(
                    StylishConnectedCardItem("今日", "3件の予定"),
                    StylishConnectedCardItem("明日", "1件の予定"),
                    StylishConnectedCardItem("今週", "8件の予定"),
                ),
            )
        }
    }
}

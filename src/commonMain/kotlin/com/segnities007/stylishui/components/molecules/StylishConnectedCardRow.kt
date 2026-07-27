package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedRowEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontally connected group of cards that share outlines and corner
 * radii, typically used for side-by-side numeric summaries or KPI tiles.
 *
 * Each card receives equal weight within the row and stretches to the tallest
 * sibling via [IntrinsicSize.Min]. Outline edges and corner radii are computed
 * automatically from each item's index: the first card rounds only its leading
 * corners, the last card rounds only its trailing corners, and middle cards
 * have square corners with shared vertical outlines. The actual card rendering
 * is delegated to the [card] lambda, which receives the pre-computed connection
 * geometry so that custom implementations preserve the connected appearance.
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
    Row(
        modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            card(
                item,
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                connectedShape(corners),
                connectedRowEdges(index, items.size),
                corners,
            )
        }
    }
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

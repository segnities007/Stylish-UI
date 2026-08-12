package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardLazyColumn
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A lazily-composed vertical group of connected cards that share outlines and corner radii,
 * suited for long scrollable card lists.
 *
 * This is the Finish-layer component: it supplies the Stylish card rendering
 * ([DefaultStylishConnectedCardItem]) to the headless Structure layout
 * [ConnectedCardLazyColumn], which owns arrangement and connection geometry. Only the visible
 * cards are composed, while the connection geometry is computed exactly as in the eager
 * [StylishConnectedCardColumn]: the first card rounds only its top corners, the last card
 * rounds only its bottom corners, and middle cards have square corners with shared horizontal
 * outlines. Pass a custom [card] to override the Stylish rendering while keeping the connected
 * geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects that describe each card's
 *   title, supporting text, click actions, and slot content.
 * @param spacing The vertical gap between adjacent cards. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding [PaddingValues] applied around the entire lazy list. Defaults to
 *   [DefaultStylishDimensions.controlPadding] horizontal padding.
 * @param listState [LazyListState] controlling scroll position. Defaults to
 *   [rememberLazyListState]. Supply a hoisted state to observe scroll offset or
 *   programmatically scroll.
 * @param card A composable lambda that renders a single card. Receives the item data, a
 *   modifier (fill-max-width), the connected [Shape], the outline [ConnectedEdges], and the
 *   outline [ConnectedCorners]. Defaults to [DefaultStylishConnectedCardItem], which delegates
 *   to the [com.segnities007.stylishui.components.atoms.StylishConnectedCard] atom.
 *
 * @see ConnectedCardLazyColumn
 * @see StylishConnectedCardColumn
 * @see StylishConnectedCardLazyGrid
 * @see DefaultStylishConnectedCardItem
 */
@Composable
public fun StylishConnectedCardLazyColumn(
    items: List<StylishConnectedCardItem>,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = DefaultStylishDimensions.controlPadding),
    listState: LazyListState = rememberLazyListState(),
    card: StylishConnectedCardItemContent = ::DefaultStylishConnectedCardItem,
) {
    ConnectedCardLazyColumn(items, modifier, spacing, contentPadding, listState, card = card)
}

@Preview(name = "Connected card lazy column", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishConnectedCardLazyColumnPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCardLazyColumn(
                items = listOf(
                    StylishConnectedCardItem("Civic", "セダン"),
                    StylishConnectedCardItem("Accord", "セダン"),
                    StylishConnectedCardItem("Prius", "ハイブリッド"),
                    StylishConnectedCardItem("Model 3", "EV"),
                    StylishConnectedCardItem("Leaf", "EV"),
                    StylishConnectedCardItem("Golf", "ハッチバック"),
                    StylishConnectedCardItem("Passat", "セダン"),
                    StylishConnectedCardItem("Corolla", "セダン"),
                    StylishConnectedCardItem("Outback", "SUV"),
                    StylishConnectedCardItem("Fit", "コンパクト"),
                ),
            )
        }
    }
}

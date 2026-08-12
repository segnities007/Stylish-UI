package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardLazyGrid
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A lazily-composed grid of connected cards laid out in equal-width cells across a fixed
 * number of columns, with shared outlines and corner radii — suited for scrollable
 * statistics dashboards or tile galleries.
 *
 * This is the Finish-layer component: it supplies the Stylish card rendering
 * ([DefaultStylishConnectedCardItem]) to the headless Structure layout [ConnectedCardLazyGrid],
 * which owns arrangement and connection geometry. Only the visible rows are composed, while
 * the connection geometry is computed exactly as in the eager [StylishConnectedCardGrid]: items
 * are chunked into rows of [columns] cards, within each row every card receives equal weight,
 * corner radii are computed from each item's absolute index so that only the four outer corners
 * of the entire grid are rounded, and a partially filled final row stretches to fill the full
 * row width. Pass a custom [card] to override the Stylish rendering while keeping the connected
 * geometry.
 *
 * @param items The list of [StylishConnectedCardItem] data objects that describe each card's
 *   title, supporting text, click actions, and slot content.
 * @param columns The number of equal-width columns in the grid. Must be greater than zero.
 * @param spacing The gap between adjacent cards both horizontally and vertically. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding [PaddingValues] applied around the entire lazy grid. Defaults to
 *   [DefaultStylishDimensions.controlPadding] horizontal padding.
 * @param listState [LazyGridState] controlling scroll position. Defaults to
 *   [rememberLazyGridState]. Supply a hoisted state to observe scroll offset or
 *   programmatically scroll.
 * @param card A composable lambda that renders a single card. Receives the item data, a
 *   modifier (fill-max-width within an equal-weight cell), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners]. Defaults to
 *   [DefaultStylishConnectedCardItem], which delegates to the
 *   [com.segnities007.stylishui.components.atoms.StylishConnectedCard] atom.
 *
 * @see ConnectedCardLazyGrid
 * @see StylishConnectedCardGrid
 * @see StylishConnectedCardLazyColumn
 * @see DefaultStylishConnectedCardItem
 */
@Composable
public fun StylishConnectedCardLazyGrid(
    items: List<StylishConnectedCardItem>,
    columns: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = DefaultStylishDimensions.controlPadding),
    listState: LazyGridState = rememberLazyGridState(),
    card: StylishConnectedCardItemContent = ::DefaultStylishConnectedCardItem,
) {
    ConnectedCardLazyGrid(items, columns, modifier, spacing, contentPadding, listState, card = card)
}

@Preview(name = "Connected card lazy grid", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishConnectedCardLazyGridPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCardLazyGrid(
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
                columns = 2,
            )
        }
    }
}

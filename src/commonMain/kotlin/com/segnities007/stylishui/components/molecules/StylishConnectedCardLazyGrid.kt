package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.structure.ConnectedCardLazyGrid
import com.segnities007.stylishui.theme.StylishTheme

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
 *   [StylishTheme.dimensions.controlPadding] horizontal padding.
 * @param key Optional stable and unique key factory used to preserve item state across moves.
 * @param listState [LazyGridState] controlling scroll position. Defaults to
 *   [rememberLazyGridState]. Supply a hoisted state to observe scroll offset or
 *   programmatically scroll.
 * @param containerColor Background color of each card. When `null`, defaults
 *   to [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param contentColor Content color of each card. When `null`, defaults to an
 *   enabled/disabled-aware color.
 * @param disabledContainerColor Background color of each card when its
 *   [StylishConnectedCardItem.enabled] is `false`.
 * @param disabledContentColor Content color of each card when its
 *   [StylishConnectedCardItem.enabled] is `false`.
 * @param titleStyle The [TextStyle] applied to each card's title.
 * @param supportingTextStyle The [TextStyle] applied to each card's supporting
 *   text.
 * @param titleMaxLines Maximum number of lines for each card's title. Defaults
 *   to 1.
 * @param titleOverflow The [TextOverflow] strategy for each card's title.
 *   Defaults to [TextOverflow.Ellipsis].
 * @param supportingTextMaxLines Maximum number of lines for each card's
 *   supporting text. Defaults to 1.
 * @param supportingTextOverflow The [TextOverflow] strategy for each card's
 *   supporting text. Defaults to [TextOverflow.Ellipsis].
 * @param minHeight Minimum height of each card body. Defaults to
 *   [StylishTheme.dimensions.cardMinHeight] (77 dp).
 * @param horizontalPadding Horizontal padding inside each card. Defaults to
 *   [StylishTheme.dimensions.controlPadding] (16 dp).
 * @param verticalPadding Vertical padding inside each card. Defaults to
 *   [StylishTheme.dimensions.controlVerticalPadding] (12 dp).
 * @param contentSpacing Horizontal gap between slots inside each card.
 *   Defaults to [StylishTheme.dimensions.itemSpacing] (8 dp).
 * @param titleSpacing Vertical gap between title and supporting text inside
 *   each card. Defaults to [StylishTheme.dimensions.inlineSpacing] (4 dp).
 * @param card A composable lambda that renders a single card. Receives the item data, a
 *   modifier (fill-max-width within an equal-weight cell), the connected [Shape], the outline
 *   [ConnectedEdges], and the outline [ConnectedCorners]. Defaults to
 *   [DefaultStylishConnectedCardItem], which delegates to the
 *   [com.segnities007.stylishui.components.atoms.StylishConnectedCard] atom with the styling
 *   parameters above.
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
    contentPadding: PaddingValues = PaddingValues(horizontal = StylishTheme.dimensions.controlPadding),
    key: ((StylishConnectedCardItem) -> Any)? = null,
    listState: LazyGridState = rememberLazyGridState(),
    containerColor: Color? = null,
    contentColor: Color? = null,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextMaxLines: Int = 1,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    minHeight: Dp = StylishTheme.dimensions.cardMinHeight,
    horizontalPadding: Dp = StylishTheme.dimensions.controlPadding,
    verticalPadding: Dp = StylishTheme.dimensions.controlVerticalPadding,
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    titleSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    card: StylishConnectedCardItemContent = { item, itemModifier, shape, edges, corners ->
        DefaultStylishConnectedCardItem(
            item, itemModifier, shape, edges, corners,
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            titleStyle = titleStyle,
            supportingTextStyle = supportingTextStyle,
            titleMaxLines = titleMaxLines,
            titleOverflow = titleOverflow,
            supportingTextMaxLines = supportingTextMaxLines,
            supportingTextOverflow = supportingTextOverflow,
            minHeight = minHeight,
            horizontalPadding = horizontalPadding,
            verticalPadding = verticalPadding,
            contentSpacing = contentSpacing,
            titleSpacing = titleSpacing,
        )
    },
) {
    ConnectedCardLazyGrid(
        items = items,
        columns = columns,
        modifier = modifier,
        spacing = spacing,
        contentPadding = contentPadding,
        key = key,
        listState = listState,
        card = card,
    )
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

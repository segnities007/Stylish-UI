package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
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
import com.segnities007.stylishui.structure.ConnectedCardRow
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

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
 *   [DefaultStylishDimensions.cardMinHeight] (77 dp).
 * @param horizontalPadding Horizontal padding inside each card. Defaults to
 *   [DefaultStylishDimensions.controlPadding] (16 dp).
 * @param verticalPadding Vertical padding inside each card. Defaults to
 *   [DefaultStylishDimensions.controlVerticalPadding] (12 dp).
 * @param contentSpacing Horizontal gap between slots inside each card.
 *   Defaults to [StylishTheme.dimensions.itemSpacing] (8 dp).
 * @param titleSpacing Vertical gap between title and supporting text inside
 *   each card. Defaults to [StylishTheme.dimensions.inlineSpacing] (4 dp).
 * @param card A composable lambda that renders a single card. Receives the
 *   item data, a modifier (including weight and fill-max-height), the
 *   connected [Shape], the outline [ConnectedEdges], and the outline
 *   [ConnectedCorners]. Defaults to [DefaultStylishConnectedCardItem], which
 *   delegates to the [StylishConnectedCard] atom with the styling parameters
 *   above.
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
    minHeight: Dp = DefaultStylishDimensions.cardMinHeight,
    horizontalPadding: Dp = DefaultStylishDimensions.controlPadding,
    verticalPadding: Dp = DefaultStylishDimensions.controlVerticalPadding,
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
    ConnectedCardRow(items, modifier, spacing, card = card)
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

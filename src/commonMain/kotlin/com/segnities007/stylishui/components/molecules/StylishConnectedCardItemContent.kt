package com.segnities007.stylishui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges

/**
 * A composable function type used by connected-card layout components to
 * render a single card item.
 *
 * Connected-card layouts ([StylishConnectedCardRow], [StylishConnectedCardColumn],
 * [StylishConnectedCardGrid]) compute the outline edges, corners, and shape for
 * each position in the group, then pass that geometry into this lambda. Custom
 * implementations should apply the supplied [shape], [outlineEdges], and
 * [outlineCorners] to preserve the connected appearance while freely choosing
 * the card's inner UI.
 *
 * @param item The [StylishConnectedCardItem] data for the card being rendered.
 * @param modifier A modifier carrying layout constraints (e.g. weight,
 *   fill-max-height) determined by the parent layout. Must be applied to the
 *   card's root composable.
 * @param shape The connected [Shape] with only the appropriate outer corners
 *   rounded for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides of this card
 *   should draw outline borders to connect with adjacent siblings.
 * @param outlineCorners The [ConnectedCorners] indicating which corners of
 *   this card are rounded.
 *
 * @see DefaultStylishConnectedCardItem
 */
public typealias StylishConnectedCardItemContent = @Composable (
    item: StylishConnectedCardItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
) -> Unit

/**
 * The default card renderer used by connected-card layouts when no custom
 * [StylishConnectedCardItemContent] is supplied.
 *
 * Delegates to the [StylishConnectedCard] atom, forwarding all connection
 * geometry (shape, outline edges, outline corners) and item data including
 * title, supporting text, click/long-click actions, enabled state, and
 * leading/trailing slot content.
 *
 * @param item The [StylishConnectedCardItem] data for the card being rendered.
 * @param modifier A modifier carrying layout constraints from the parent
 *   layout. Applied to the [StylishConnectedCard] root.
 * @param shape The connected [Shape] for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides draw outline
 *   borders.
 * @param outlineCorners The [ConnectedCorners] indicating which corners are
 *   rounded.
 *
 * @see StylishConnectedCardItemContent
 * @see StylishConnectedCardRow
 * @see StylishConnectedCardColumn
 * @see StylishConnectedCardGrid
 */
@Composable
public fun DefaultStylishConnectedCardItem(
    item: StylishConnectedCardItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
) {
    StylishConnectedCard(
        title = item.title,
        supportingText = item.supportingText,
        onClick = item.onClick,
        onLongClick = item.onLongClick,
        enabled = item.enabled,
        modifier = modifier,
        shape = shape,
        outlineEdges = outlineEdges,
        outlineCorners = outlineCorners,
        leadingContent = item.leadingContent,
        trailingContent = item.trailingContent,
    )
}

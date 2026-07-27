package com.segnities007.stylishui.structure

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges

/**
 * The rendering contract for a single item within a headless connected-card
 * layout (Structure layer).
 *
 * A connected-card layout ([ConnectedCardRow], [ConnectedCardColumn],
 * [ConnectedCardGrid]) computes the outline edges, corners, and shape for each
 * position in the group, then invokes this lambda to render the item. The
 * lambda owns **all** visual decisions — surface, colors, elevation, content —
 * while the layout owns geometry and arrangement. This separation is what makes
 * the layout headless: it can drive any visual treatment (the Stylish Finish
 * default `DefaultStylishConnectedCardItem`, or a fully custom skin) without
 * changing structure.
 *
 * @param item The [StylishConnectedCardItem] data for the card being rendered.
 * @param modifier Layout constraints (e.g. weight, fill-max-height) determined
 *   by the parent layout. Must be applied to the item's root composable.
 * @param shape The connected [Shape] with only the appropriate outer corners
 *   rounded for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides of this card
 *   should draw outline borders to connect with adjacent siblings.
 * @param outlineCorners The [ConnectedCorners] indicating which corners of this
 *   card are rounded.
 *
 * @see ConnectedCardRow
 * @see ConnectedCardColumn
 * @see ConnectedCardGrid
 */
public typealias ConnectedCardItemContent = @Composable (
    item: StylishConnectedCardItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
) -> Unit

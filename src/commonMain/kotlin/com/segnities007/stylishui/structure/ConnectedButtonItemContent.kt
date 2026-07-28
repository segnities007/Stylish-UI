package com.segnities007.stylishui.structure

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges

/**
 * The rendering contract for a single item within a headless connected-button
 * layout (Structure layer).
 *
 * A connected-button layout ([ConnectedButtonRow], [ConnectedButtonColumn],
 * [ConnectedButtonGrid]) computes the outline edges, corners, and shape for
 * each position in the group, then invokes this lambda to render the item. The
 * lambda owns **all** visual and interactive decisions — surface, colors,
 * elevation, click handling, content — while the layout owns geometry and
 * arrangement. This separation is what makes the layout headless.
 *
 * @param item The [StylishConnectedButtonItem] data for the button being
 *   rendered.
 * @param modifier Layout constraints (e.g. weight, fill-max-height) determined
 *   by the parent layout. Must be applied to the item's root composable.
 * @param shape The connected [Shape] with only the appropriate outer corners
 *   rounded for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides of this
 *   button should draw outline borders to connect with adjacent siblings.
 * @param outlineCorners The [ConnectedCorners] indicating which corners of this
 *   button are rounded.
 *
 * @see ConnectedButtonRow
 * @see ConnectedButtonColumn
 * @see ConnectedButtonGrid
 */
public typealias ConnectedButtonItemContent = @Composable (
    item: StylishConnectedButtonItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
) -> Unit

package com.segnities007.stylishui.structure

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges

/**
 * The rendering contract for a single item within a headless connected-chip
 * layout (Structure layer).
 *
 * A connected-chip layout ([ConnectedChipRow], [ConnectedChipColumn],
 * [ConnectedChipGrid]) computes the outline edges, corners, and shape for each
 * position in the group, then invokes this lambda to render the item. The
 * lambda owns **all** visual and interactive decisions — surface, selection
 * colors, animation, semantics, click handling — while the layout owns geometry
 * and arrangement. This separation is what makes the layout headless.
 *
 * @param item The [StylishConnectedChipItem] data for the chip being rendered.
 * @param modifier Layout constraints determined by the parent layout (e.g.
 *   weight when `fillWidth` is used). Must be applied to the item's root
 *   composable.
 * @param shape The connected [Shape] with only the appropriate outer corners
 *   rounded for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides of this chip
 *   should draw outline borders to connect with adjacent siblings.
 * @param outlineCorners The [ConnectedCorners] indicating which corners of this
 *   chip are rounded.
 *
 * @see ConnectedChipRow
 * @see ConnectedChipColumn
 * @see ConnectedChipGrid
 */
public typealias ConnectedChipItemContent = @Composable (
    item: StylishConnectedChipItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
) -> Unit

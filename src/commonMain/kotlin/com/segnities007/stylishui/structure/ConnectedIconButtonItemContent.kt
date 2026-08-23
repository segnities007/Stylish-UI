package com.segnities007.stylishui.structure

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.components.models.StylishConnectedIconButtonItem
import com.segnities007.stylishui.foundation.ConnectedCorners

/**
 * Rendering contract for one item in [ConnectedIconButtonRow].
 *
 * The Structure layer computes the position-aware shape and arrangement. The
 * Finish layer decides how an item looks and how it handles interaction.
 * Connected icon buttons deliberately do not pass outline edges: the default
 * control uses color, spacing, and clipping rather than a border to express
 * the connection.
 *
 * @param item Data describing the icon action.
 * @param modifier Layout constraints supplied by the parent row.
 * @param shape Position-aware shape for this item.
 * @param corners Outer-corner metadata for custom renderers.
 */
public typealias ConnectedIconButtonItemContent = @Composable (
    item: StylishConnectedIconButtonItem,
    modifier: Modifier,
    shape: Shape,
    corners: ConnectedCorners,
) -> Unit

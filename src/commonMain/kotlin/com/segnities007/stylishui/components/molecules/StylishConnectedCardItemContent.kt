package com.segnities007.stylishui.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges

/**
 * Content used by connected-card layouts to render an item.
 *
 * The layout supplies the modifier and connection geometry, allowing custom card
 * implementations to preserve the connected appearance while choosing any UI.
 */
public typealias StylishConnectedCardItemContent = @Composable (
    item: StylishConnectedCardItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
) -> Unit

/** The default renderer used by connected-card layouts. */
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

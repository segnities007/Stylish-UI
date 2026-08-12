package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.structure.ConnectedCardItemContent
import com.segnities007.stylishui.theme.StylishTheme

/**
 * The Finish-layer alias for the headless connected-card rendering contract.
 *
 * This is an alias of the Structure-layer [ConnectedCardItemContent], kept
 * under its historical name so existing call sites and imports continue to
 * compile. Connected-card layouts ([StylishConnectedCardRow],
 * [StylishConnectedCardColumn], [StylishConnectedCardGrid]) compute the outline
 * edges, corners, and shape for each position in the group, then pass that
 * geometry into a lambda of this type. The default Finish rendering is
 * [DefaultStylishConnectedCardItem].
 *
 * @see ConnectedCardItemContent
 * @see DefaultStylishConnectedCardItem
 */
public typealias StylishConnectedCardItemContent = ConnectedCardItemContent

/**
 * The default card renderer used by connected-card layouts when no custom
 * [StylishConnectedCardItemContent] is supplied.
 *
 * This is the Finish-layer rendering: it delegates to the [StylishConnectedCard]
 * atom, forwarding all connection geometry (shape, outline edges, outline
 * corners) and item data including title, supporting text, click/long-click
 * actions, enabled state, and leading/trailing slot content, dressed in the
 * Stylish look.
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
        content = item.content,
    )
}

@Preview(name = "Stylish connected card item", showBackground = true, widthDp = 393)
@Composable
private fun DefaultStylishConnectedCardItemPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            DefaultStylishConnectedCardItem(
                item = StylishConnectedCardItem(
                    title = "12",
                    supportingText = "メモ",
                ),
                modifier = Modifier,
                shape = connectedShape(ConnectedCorners.Standalone),
                outlineEdges = ConnectedEdges.All,
                outlineCorners = ConnectedCorners.Standalone,
            )
        }
    }
}

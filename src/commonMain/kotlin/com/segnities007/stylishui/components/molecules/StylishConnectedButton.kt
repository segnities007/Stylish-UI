package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * The default button renderer used by connected-button layouts when no custom
 * [ConnectedButtonItemContent] is supplied.
 *
 * This is the Finish-layer rendering: it dresses a button in the Stylish look —
 * grouped-container colors, interactive elevation, and a 52 dp minimum height
 * — and wires tap interaction. Items whose
 * [StylishConnectedButtonItem.onClick] is `null` or whose
 * [StylishConnectedButtonItem.enabled] is `false` are rendered in a disabled
 * state and do not respond to interaction.
 *
 * The renderer delegates to the Material 3 [Button], which owns its
 * [androidx.compose.foundation.interaction.MutableInteractionSource]
 * internally; no `interactionSource` parameter is exposed here. Observe
 * button interaction by hoisting state into [StylishConnectedButtonItem]
 * data instead.
 *
 * @param item The [StylishConnectedButtonItem] data for the button.
 * @param modifier A modifier carrying layout constraints from the parent
 *   layout. Applied to the [Button] root.
 * @param shape The connected [Shape] for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides draw outline
 *   borders.
 * @param outlineCorners The [ConnectedCorners] indicating which corners are
 *   rounded.
 * @param cornerRadius The radius used for the outline's outer-corner arcs.
 * @param contentPadding The inner padding of the button.
 * @param defaultColors The [ButtonColors] used when [StylishConnectedButtonItem.colors]
 *   is `null`.
 * @param slotMinWidth The minimum width reserved for the leading/trailing slots
 *   so icons align consistently. Defaults to 24 dp (rows/grids); columns use
 *   40 dp.
 * The outline geometry parameters are retained for the connected renderer
 * contract and custom skins; this default skin intentionally does not draw a
 * border. Supply a custom [ConnectedButtonItemContent] when an outline is a
 * deliberate part of a product-specific treatment.
 *
 * @see StylishConnectedButtonRow
 * @see StylishConnectedButtonColumn
 * @see StylishConnectedButtonGrid
 */
@Suppress("UNUSED_PARAMETER")
@Composable
public fun DefaultStylishConnectedButton(
    item: StylishConnectedButtonItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
    cornerRadius: Dp,
    contentPadding: PaddingValues,
    defaultColors: ButtonColors,
    slotMinWidth: Dp = 24.dp,
) {
    val actionable = isActionable(
        enabled = item.enabled,
        hasClickAction = item.onClick != null,
    )
    Button(
        onClick = { item.onClick?.invoke() },
        enabled = actionable,
        modifier = modifier.heightIn(min = 52.dp),
        shape = shape,
        colors = item.colors ?: defaultColors,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = StylishTheme.dimensions.interactiveElevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        contentPadding = contentPadding,
    ) {
        StylishButtonSlot(item.leadingContent, Alignment.CenterStart, slotMinWidth)
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = item.content,
        )
        StylishButtonSlot(item.trailingContent, Alignment.CenterEnd, slotMinWidth)
    }
}

@Preview(name = "Stylish connected button", showBackground = true, widthDp = 393)
@Composable
private fun DefaultStylishConnectedButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            DefaultStylishConnectedButton(
                item = StylishConnectedButtonItem(onClick = {}) { Text("編集") },
                modifier = Modifier,
                shape = connectedShape(ConnectedCorners.Standalone),
                outlineEdges = ConnectedEdges.None,
                outlineCorners = ConnectedCorners.Standalone,
                cornerRadius = 12.dp,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                defaultColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    }
}

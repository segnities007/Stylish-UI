package com.segnities007.stylishui.structure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedIconButtonItem
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Headless horizontal layout for connected icon buttons.
 *
 * The row computes the position-aware shape for each item and delegates
 * rendering to [button]. It owns no colors, borders, elevation, ripples, or
 * animation. The small gap is intentional: it keeps each hit target distinct
 * while the shared outer clipping preserves the Connected UI silhouette.
 *
 * @param items Items to arrange. An empty list produces no layout.
 * @param modifier Modifier applied to the row.
 * @param cornerRadius Radius for the outer corners of the group.
 * @param joinedCornerRadius Radius for corners facing a neighboring item.
 * @param spacing Gap between neighboring items.
 * @param button Finish-layer renderer for each item.
 */
@Composable
public fun ConnectedIconButtonRow(
    items: List<StylishConnectedIconButtonItem>,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.iconButtonMinSize / 2,
    joinedCornerRadius: Dp = StylishTheme.dimensions.joinedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    button: ConnectedIconButtonItemContent,
) {
    if (items.isEmpty()) return

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val corners = connectedRowCorners(index, items.size)
            button(
                item,
                Modifier.fillMaxHeight(),
                connectedShape(
                    corners = corners,
                    cornerRadius = cornerRadius,
                    joinedCornerRadius = joinedCornerRadius,
                ),
                corners,
            )
        }
    }
}

@Preview(name = "Headless connected icon button row", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedIconButtonRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            ConnectedIconButtonRow(
                items = List(4) {
                    StylishConnectedIconButtonItem(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "設定",
                        onClick = {},
                    )
                },
            ) { _, itemModifier, itemShape, _ ->
                Surface(
                    modifier = itemModifier.width(48.dp),
                    shape = itemShape,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                ) {}
            }
        }
    }
}

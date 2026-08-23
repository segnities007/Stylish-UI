package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedIconButtonItem
import com.segnities007.stylishui.structure.ConnectedIconButtonItemContent
import com.segnities007.stylishui.structure.ConnectedIconButtonRow
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontally connected group of icon buttons.
 *
 * The group is clipped by [shape] (a circle by default) while each icon keeps
 * its own position-aware Connected shape. The narrow gap maintains separate
 * hit targets and the default renderer has exactly one Material ripple per
 * item. No outline is drawn: the connection is communicated by the shared
 * silhouette, tonal colors, spacing, and elevation.
 *
 * Pass a rounded rectangle or another [Shape] when the surrounding layout
 * needs a different outer contour. The default [CircleShape] produces a
 * circle for one item and a pill-like connected control for multiple items.
 *
 * @param items Icon actions to render.
 * @param modifier Modifier applied to the group.
 * @param shape Outer clip for the complete group.
 * @param spacing Gap between neighboring item hit targets.
 * @param cornerRadius Radius for the group's outer item corners.
 * @param joinedCornerRadius Radius for corners facing a neighboring item.
 * @param button Optional custom item renderer. The default renderer uses one
 *   Material [IconButton] indication and no border.
 */
@Composable
public fun StylishConnectedIconButtonRow(
    items: List<StylishConnectedIconButtonItem>,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    cornerRadius: Dp = StylishTheme.dimensions.iconButtonMinSize / 2,
    joinedCornerRadius: Dp = StylishTheme.dimensions.joinedCornerRadius,
    button: ConnectedIconButtonItemContent = { item, itemModifier, itemShape, _ ->
        DefaultStylishConnectedIconButton(item, itemModifier, itemShape)
    },
) {
    ConnectedIconButtonRow(
        items = items,
        modifier = modifier.clip(shape),
        cornerRadius = cornerRadius,
        joinedCornerRadius = joinedCornerRadius,
        spacing = spacing,
        button = button,
    )
}

@Composable
private fun DefaultStylishConnectedIconButton(
    item: StylishConnectedIconButtonItem,
    modifier: Modifier,
    shape: Shape,
) {
    val containerColor = if (item.active) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val contentColor = if (item.active) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val effectiveContentColor = if (item.enabled) contentColor else contentColor.copy(alpha = 0.38f)

    Surface(
        modifier = modifier
            .sizeIn(
                minWidth = StylishTheme.dimensions.iconButtonMinSize,
                minHeight = StylishTheme.dimensions.iconButtonMinSize,
            )
            .then(if (!item.enabled) Modifier.semantics { disabled() } else Modifier),
        shape = shape,
        color = containerColor,
        shadowElevation = if (item.enabled) {
            StylishTheme.dimensions.interactiveElevation
        } else {
            0.dp
        },
    ) {
        // Keep the indication in exactly one Material component. Wrapping
        // IconButton with a second interactive surface creates two ripples.
        IconButton(
            onClick = item.onClick,
            enabled = item.enabled,
            shape = RectangleShape,
        ) {
            Icon(
                imageVector = item.imageVector,
                contentDescription = item.contentDescription,
                tint = effectiveContentColor,
            )
        }
    }
}

@Preview(name = "Connected icon buttons - four items", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedIconButtonRowPreview() {
    StylishTheme(darkTheme = false) {
        StylishConnectedIconButtonRow(
            items = listOf(
                StylishConnectedIconButtonItem(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "設定1",
                    onClick = {},
                ),
                StylishConnectedIconButtonItem(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "設定2",
                    onClick = {},
                ),
                StylishConnectedIconButtonItem(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "設定3",
                    onClick = {},
                ),
                StylishConnectedIconButtonItem(
                    imageVector = Icons.Default.Search,
                    contentDescription = "検索",
                    onClick = {},
                    active = true,
                ),
            ),
        )
    }
}

package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A circular icon button rendered on an outlined surface with a
 * subtle shadow. Supports an [active] state that inverts the color
 * scheme to primary/onPrimary, suitable for toggle-style controls.
 *
 * When [active] is `true`, the container defaults to
 * `MaterialTheme.colorScheme.primary` and the content color to
 * `onPrimary`. When inactive, the container defaults to
 * `surfaceContainerHighest` and the content to `onSurfaceVariant`.
 * Explicit [containerColor] / [contentColor] overrides always win.
 * When [enabled] is `false`, the shadow elevation drops to zero and
 * the inner [IconButton] rejects clicks.
 *
 * @param imageVector Icon drawn inside the button when [iconContent]
 *   is `null`.
 * @param contentDescription Accessibility label for [imageVector].
 * @param onClick Called when the button is tapped.
 * @param enabled When `false`, the button ignores pointer input and
 *   the shadow elevation is removed.
 * @param active When `true`, the button renders in the primary color
 *   scheme to indicate a selected/toggled state.
 * @param containerColor Background color override. When `null`,
 *   resolved from [active] (see above).
 * @param contentColor Content tint override. When `null`, resolved
 *   from [active] (see above).
 * @param shape Shape of the surface. Defaults to [CircleShape].
 * @param minWidth Minimum width of the tappable surface.
 *   Defaults to 48 dp.
 * @param minHeight Minimum height of the tappable surface.
 *   Defaults to 48 dp.
 * @param iconContent Optional slot that replaces the default [Icon].
 *   When `null` (default), [imageVector] and [contentDescription]
 *   are used instead.
 *
 * @see StylishRoundedIconButton
 * @see StylishFab
 */
@Composable
public fun StylishIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    active: Boolean = false,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    minWidth: Dp = 48.dp,
    minHeight: Dp = 48.dp,
    iconContent: (@Composable () -> Unit)? = null,
) {
    val resolvedContainerColor = containerColor ?: if (active) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val resolvedContentColor = contentColor ?: if (active) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier.sizeIn(minWidth = minWidth, minHeight = minHeight),
        shape = shape ?: CircleShape,
        color = resolvedContainerColor,
        border = BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
        shadowElevation = if (enabled) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            iconContent?.invoke() ?: Icon(imageVector, contentDescription, tint = resolvedContentColor)
        }
    }
}

@Preview(name = "Icon button default", showBackground = true)
@Composable
private fun StylishIconButtonPreview() {
    MaterialTheme {
        StylishIconButton(Icons.Default.Search, "検索", {})
    }
}

@Preview(name = "Icon button active", showBackground = true)
@Composable
private fun StylishIconButtonActivePreview() {
    MaterialTheme {
        StylishIconButton(
            imageVector = Icons.Default.Search,
            contentDescription = "検索",
            onClick = {},
            active = true,
        )
    }
}

@Preview(name = "Icon button disabled", showBackground = true)
@Composable
private fun StylishIconButtonDisabledPreview() {
    MaterialTheme {
        StylishIconButton(
            imageVector = Icons.Default.Search,
            contentDescription = "検索",
            onClick = {},
            enabled = false,
        )
    }
}

package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
 * A circular floating action button that floats above content with
 * tonal and shadow elevation (floatingElevation, 2 dp). Renders a
 * single icon inside a [CircleShape] surface with a hairline outline
 * border.
 *
 * Use for the primary action of a screen (e.g. "create new item").
 * When [iconContent] is provided it replaces the default [Icon]
 * rendered from [imageVector] and [contentDescription], allowing
 * arbitrary composable content such as an animated icon or a badge.
 * When [enabled] is `false`, the inner [IconButton] rejects clicks
 * and Material applies its disabled alpha.
 *
 * @param imageVector Icon drawn inside the button when [iconContent]
 *   is `null`.
 * @param contentDescription Accessibility label for [imageVector].
 * @param onClick Called when the button is tapped.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders with Material's disabled treatment.
 * @param containerColor Background color of the surface. Defaults to
 *   `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param contentColor Default tint for content inside the surface.
 *   Defaults to `MaterialTheme.colorScheme.onSurface`.
 * @param shape Shape of the surface. Defaults to [CircleShape].
 * @param size Diameter of the circular surface. Defaults to 56 dp.
 * @param border Border stroke around the surface. Defaults to a
 *   hairline of [StylishTheme.dimensions.outlineWidth] (0.4 dp)
 *   using `MaterialTheme.colorScheme.outlineVariant`.
 * @param iconContent Optional slot that replaces the default [Icon].
 *   When `null` (default), [imageVector] and [contentDescription]
 *   are used instead.
 *
 * @see StylishIconButton
 * @see StylishRoundedIconButton
 */
@Composable
public fun StylishFab(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    size: Dp = 56.dp,
    border: BorderStroke? = null,
    iconContent: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier.size(size),
        shape = shape ?: CircleShape,
        color = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        border = border ?: BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
        tonalElevation = StylishTheme.dimensions.floatingElevation,
        shadowElevation = StylishTheme.dimensions.floatingElevation,
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            iconContent?.invoke() ?: Icon(imageVector, contentDescription)
        }
    }
}

@Preview(name = "FAB default", showBackground = true)
@Composable
private fun StylishFabPreview() {
    MaterialTheme {
        StylishFab(Icons.Default.Add, "追加", {})
    }
}

@Preview(name = "FAB disabled", showBackground = true)
@Composable
private fun StylishFabDisabledPreview() {
    MaterialTheme {
        StylishFab(
            imageVector = Icons.Default.Add,
            contentDescription = "追加",
            onClick = {},
            enabled = false,
        )
    }
}

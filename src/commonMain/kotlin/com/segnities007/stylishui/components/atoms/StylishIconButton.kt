package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishInteractiveSurface
import com.segnities007.stylishui.foundation.rememberStylishInteractionSource
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A circular icon button rendered on a tonal surface with a subtle
 * shadow. It has no outline by default. Supports an [active] state that inverts the color
 * scheme to primary/onPrimary, suitable for toggle-style controls.
 *
 * When [active] is `true`, the container defaults to
 * `MaterialTheme.colorScheme.primary` and the content color to
 * `onPrimary`. When inactive, the container defaults to
 * `surfaceContainerHighest` and the content to `onSurfaceVariant`.
 * Explicit [containerColor] / [contentColor] overrides always win.
 * When [enabled] is `false`, the shadow elevation drops to zero, the
 * content is dimmed to 38 % opacity, the button is announced as
 * disabled via semantics, and the inner [IconButton] rejects clicks.
 * When [enabled], a Material-style state layer (see
 * [Modifier.stylishStateLayer]) darkens the surface on hover and press,
 * and a primary-colored focus ring (see [Modifier.stylishFocusRing]) is
 * drawn while the button holds keyboard focus.
 *
 * @param imageVector Icon drawn inside the button when [iconContent]
 *   is `null`.
 * @param contentDescription Accessibility label for [imageVector].
 *   Defaults to `null` (no label); provide one whenever the action is
 *   not otherwise described on screen.
 * @param onClick Called when the button is tapped.
 * @param enabled When `false`, the button ignores pointer input, the
 *   shadow elevation is removed, the content is dimmed to 38 %
 *   opacity, and the button is announced as disabled.
 * @param active When `true`, the button renders in the primary color
 *   scheme to indicate a selected/toggled state.
 * @param containerColor Background color override. When `null`,
 *   resolved from [active] (see above).
 * @param contentColor Content tint override. When `null`, resolved
 *   from [active] (see above). When [enabled] is `false`, the
 *   resolved content color is dimmed to 38 % opacity.
 * @param shape Shape of the surface. Defaults to [CircleShape].
 * @param border Optional border stroke drawn around the surface. Defaults to
 *   `null`; pass an explicit stroke when an outlined treatment is intentional.
 * @param minWidth Minimum width of the tappable surface.
 *   Defaults to [StylishTheme.dimensions.iconButtonMinSize].
 * @param minHeight Minimum height of the tappable surface.
 *   Defaults to [StylishTheme.dimensions.iconButtonMinSize].
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param iconContent Optional slot that replaces the default [Icon].
 *   When `null` (default), [imageVector] and [contentDescription]
 *   are used instead. When provided, [contentColor] is not applied
 *   automatically — the slot is responsible for its own tinting.
 *
 * @see StylishRoundedIconButton
 * @see StylishFab
 */
@Composable
public fun StylishIconButton(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    active: Boolean = false,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    border: BorderStroke? = null,
    minWidth: Dp = StylishTheme.dimensions.iconButtonMinSize,
    minHeight: Dp = StylishTheme.dimensions.iconButtonMinSize,
    interactionSource: MutableInteractionSource? = null,
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
    val effectiveContentColor = if (enabled) {
        resolvedContentColor
    } else {
        resolvedContentColor.copy(alpha = 0.38f)
    }
    val resolvedInteractionSource = rememberStylishInteractionSource(interactionSource)
    val resolvedShape = shape ?: CircleShape

    Surface(
        modifier = modifier
            .stylishTestTag("icon_button")
            .sizeIn(minWidth = minWidth, minHeight = minHeight)
            .then(
                if (!enabled) {
                    Modifier.semantics { disabled() }
                } else {
                    Modifier
                },
            )
            .then(if (enabled) Modifier.stylishInteractiveSurface(resolvedInteractionSource, resolvedShape) else Modifier),
        shape = resolvedShape,
        color = resolvedContainerColor,
        border = border,
        shadowElevation = if (enabled) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = resolvedInteractionSource,
        ) {
            iconContent?.invoke() ?: Icon(imageVector, contentDescription, tint = effectiveContentColor)
        }
    }
}

@Preview(name = "Icon button default", showBackground = true)
@Composable
private fun StylishIconButtonPreview() {
    StylishTheme(darkTheme = false) {
        StylishIconButton(Icons.Default.Search, "検索", {})
    }
}

@Preview(name = "Icon button active", showBackground = true)
@Composable
private fun StylishIconButtonActivePreview() {
    StylishTheme(darkTheme = false) {
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
    StylishTheme(darkTheme = false) {
        StylishIconButton(
            imageVector = Icons.Default.Search,
            contentDescription = "検索",
            onClick = {},
            enabled = false,
        )
    }
}

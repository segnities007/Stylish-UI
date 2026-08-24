package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.stylishFocusRing
import com.segnities007.stylishui.foundation.stylishStateLayer
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A pill-shaped icon button rendered on a wide rounded-rectangle
 * surface. Behaves like [StylishIconButton] but uses a horizontally
 * elongated shape, making it a good fit for toolbars or action rows
 * where a wider tap target is desirable.
 *
 * The [active] / [enabled] color and elevation logic mirrors
 * [StylishIconButton]: active inverts to primary/onPrimary, and
 * disabling removes the shadow. When [enabled], a Material-style state
 * layer (see [Modifier.stylishStateLayer]) darkens the surface on
 * hover and press, and a primary-colored focus ring (see
 * [Modifier.stylishFocusRing]) is drawn while the button holds keyboard
 * focus. When [enabled] is `false`, the icon
 * is rendered inside a plain [Box] instead of an [IconButton], so
 * no ripple or click handling is attached; the box is announced as
 * disabled and carries the [contentDescription] via semantics.
 *
 * @param imageVector Icon drawn inside the button when [iconContent]
 *   is `null`.
 * @param contentDescription Accessibility label for [imageVector].
 *   Defaults to `null` (no label); provide one whenever the action is
 *   not otherwise described on screen.
 * @param onClick Called when the button is tapped. Ignored when
 *   [enabled] is `false`.
 * @param enabled When `false`, the button ignores pointer input,
 *   the shadow elevation is removed, the icon is rendered in a
 *   non-interactive [Box], and the button is announced as disabled.
 * @param active When `true`, the button renders in the primary color
 *   scheme to indicate a selected/toggled state.
 * @param containerColor Background color override. When `null`,
 *   resolved from [active]: `primary` if active, otherwise
 *   `surfaceContainerHighest`.
 * @param contentColor Content tint override. When `null`, resolved
 *   from [active]: `onPrimary` if active, otherwise
 *   `onSurfaceVariant`.
 * @param shape Shape of the surface. Defaults to
 *   `RoundedCornerShape` with
 *   [StylishTheme.dimensions.floatingCornerRadius].
 * @param border Optional border stroke drawn around the surface. Defaults to
 *   `null`; pass an explicit stroke when an outlined treatment is intentional.
 * @param minWidth Minimum width of the tappable surface.
 *   Defaults to [StylishTheme.dimensions.roundedIconButtonMinWidth].
 * @param minHeight Minimum height of the tappable surface.
 *   Defaults to 48 dp.
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param iconContent Optional slot that replaces the default [Icon].
 *   When `null` (default), [imageVector] and [contentDescription]
 *   are used instead. When provided, [contentColor] is not applied
 *   automatically — the slot is responsible for its own tinting.
 *
 * @see StylishIconButton
 * @see StylishFab
 */
@Composable
public fun StylishRoundedIconButton(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    active: Boolean = false,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    border: BorderStroke? = null,
    minWidth: Dp = StylishTheme.dimensions.roundedIconButtonMinWidth,
    minHeight: Dp = 48.dp,
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
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .stylishTestTag("rounded_icon_button")
            .sizeIn(minWidth = minWidth, minHeight = minHeight)
            .then(
                if (enabled) {
                    Modifier.stylishStateLayer(
                        interactionSource = resolvedInteractionSource,
                        shape = shape,
                    )
                } else {
                    Modifier
                },
            )
            .then(
                if (enabled) {
                    Modifier.stylishFocusRing(
                        interactionSource = resolvedInteractionSource,
                        shape = shape,
                    )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        color = resolvedContainerColor,
        border = border,
        shadowElevation = if (enabled) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        if (enabled) {
            IconButton(onClick = onClick, interactionSource = resolvedInteractionSource) {
                iconContent?.invoke() ?: Icon(imageVector, contentDescription, tint = resolvedContentColor)
            }
        } else {
            Box(
                Modifier
                    .sizeIn(minWidth = minWidth, minHeight = minHeight)
                    .semantics {
                        disabled()
                        if (contentDescription != null) {
                            this.contentDescription = contentDescription
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                iconContent?.invoke() ?: Icon(imageVector, contentDescription, tint = resolvedContentColor)
            }
        }
    }
}

@Preview(name = "Rounded icon button default", showBackground = true)
@Composable
private fun StylishRoundedIconButtonPreview() {
    StylishTheme(darkTheme = false) {
        StylishRoundedIconButton(Icons.Default.Add, "追加", {})
    }
}

@Preview(name = "Rounded icon button active", showBackground = true)
@Composable
private fun StylishRoundedIconButtonActivePreview() {
    StylishTheme(darkTheme = false) {
        StylishRoundedIconButton(
            imageVector = Icons.Default.Add,
            contentDescription = "追加",
            onClick = {},
            active = true,
        )
    }
}

@Preview(name = "Rounded icon button disabled", showBackground = true)
@Composable
private fun StylishRoundedIconButtonDisabledPreview() {
    StylishTheme(darkTheme = false) {
        StylishRoundedIconButton(
            imageVector = Icons.Default.Add,
            contentDescription = "追加",
            onClick = {},
            enabled = false,
        )
    }
}

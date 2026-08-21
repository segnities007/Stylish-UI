package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A filled icon button with a primary-container background, wrapping
 * the Material 3 [FilledIconButton] with the theme's default colors.
 *
 * The filled variant is the high-emphasis icon button; use it for the
 * primary action of a toolbar or image list. [content] should typically
 * be an [Icon] sized 24 x 24 dp; the button enforces a 48 x 48 dp
 * minimum touch target.
 *
 * @param onClick Called when the button is tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param shape Shape of the button's container. Defaults to
 *   [IconButtonDefaults.filledShape].
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.filledIconButtonColors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishFilledTonalIconButton
 * @see StylishOutlinedIconButton
 */
@Composable
public fun StylishFilledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.filledShape,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.stylishTestTag("filled_icon_button"),
        enabled = enabled,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * A filled tonal icon button with a secondary-container background,
 * wrapping the Material 3 [FilledTonalIconButton] with the theme's
 * default colors.
 *
 * The filled tonal variant is the medium-emphasis icon button, a middle
 * ground between [StylishFilledIconButton] and
 * [StylishOutlinedIconButton]; use it when the action needs slightly
 * more emphasis than an outline provides. [content] should typically
 * be an [Icon] sized 24 x 24 dp; the button enforces a 48 x 48 dp
 * minimum touch target.
 *
 * @param onClick Called when the button is tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param shape Shape of the button's container. Defaults to
 *   [IconButtonDefaults.filledShape].
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.filledTonalIconButtonColors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishFilledIconButton
 * @see StylishOutlinedIconButton
 */
@Composable
public fun StylishFilledTonalIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.filledShape,
    colors: IconButtonColors = IconButtonDefaults.filledTonalIconButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier.stylishTestTag("filled_tonal_icon_button"),
        enabled = enabled,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * An outlined icon button with a hairline border, wrapping the
 * Material 3 [OutlinedIconButton] with the theme's default colors.
 *
 * The outlined variant separates the action from its surroundings with
 * a border; use it when the action needs more visual separation than a
 * bare icon button. [content] should typically be an [Icon] sized
 * 24 x 24 dp; the button enforces a 48 x 48 dp minimum touch target.
 *
 * @param onClick Called when the button is tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param shape Shape of the button's container and border. Defaults to
 *   [IconButtonDefaults.outlinedShape].
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.outlinedIconButtonColors].
 * @param border Border drawn around the container. Defaults to
 *   [IconButtonDefaults.outlinedIconButtonBorder] resolved from
 *   [enabled]. Pass `null` for no border.
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishFilledIconButton
 * @see StylishFilledTonalIconButton
 */
@Composable
public fun StylishOutlinedIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.outlinedShape,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
    border: BorderStroke? = IconButtonDefaults.outlinedIconButtonBorder(enabled),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier.stylishTestTag("outlined_icon_button"),
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        content = content,
    )
}

@Preview(name = "Stylish filled icon button", showBackground = true, widthDp = 393)
@Composable
private fun StylishFilledIconButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishFilledIconButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "追加")
            }
        }
    }
}

@Preview(name = "Stylish filled tonal icon button", showBackground = true, widthDp = 393)
@Composable
private fun StylishFilledTonalIconButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishFilledTonalIconButton(onClick = {}) {
                Icon(Icons.Default.Edit, contentDescription = "編集")
            }
        }
    }
}

@Preview(name = "Stylish outlined icon button", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedIconButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishOutlinedIconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "検索")
            }
        }
    }
}

@Preview(name = "Stylish icon button variants", showBackground = true, widthDp = 393)
@Composable
private fun StylishIconButtonVariantsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishFilledIconButton(onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp))
                }
                StylishFilledTonalIconButton(onClick = {}) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(24.dp))
                }
                StylishOutlinedIconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

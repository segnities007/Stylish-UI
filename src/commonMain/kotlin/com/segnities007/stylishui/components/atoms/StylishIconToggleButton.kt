package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A standard icon toggle button that switches between a checked and an
 * unchecked state, wrapping the Material 3 [IconToggleButton] with the
 * theme's default colors.
 *
 * Use this for single-select actions that stay active, such as a
 * favorite or bookmark toggle. [content] should typically be an [Icon]
 * that changes with [checked] (e.g. filled vs. outlined); the button
 * enforces a 48 x 48 dp minimum touch target.
 *
 * @param checked Whether the button is currently toggled on.
 * @param onCheckedChange Called with the new state when the button is
 *   tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.iconToggleButtonColors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param shape Shape of the button's container. Defaults to
 *   [IconButtonDefaults.standardShape].
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishFilledIconToggleButton
 * @see StylishFilledTonalIconToggleButton
 * @see StylishOutlinedIconToggleButton
 */
@Composable
public fun StylishIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = IconButtonDefaults.standardShape,
    content: @Composable () -> Unit,
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.stylishTestTag("icon_toggle_button"),
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
        shape = shape,
        content = content,
    )
}

/**
 * A filled icon toggle button with a primary-container background,
 * wrapping the Material 3 [FilledIconToggleButton] with the theme's
 * default colors.
 *
 * Use this for high-emphasis toggle actions such as a pinned item.
 * [content] should typically be an [Icon] that changes with [checked];
 * the button enforces a 48 x 48 dp minimum touch target.
 *
 * @param checked Whether the button is currently toggled on.
 * @param onCheckedChange Called with the new state when the button is
 *   tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param shape Shape of the button's container. Defaults to
 *   [IconButtonDefaults.filledShape].
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.filledIconToggleButtonColors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishIconToggleButton
 * @see StylishFilledTonalIconToggleButton
 * @see StylishOutlinedIconToggleButton
 */
@Composable
public fun StylishFilledIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.filledShape,
    colors: IconToggleButtonColors = IconButtonDefaults.filledIconToggleButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.stylishTestTag("filled_icon_toggle_button"),
        enabled = enabled,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * A filled tonal icon toggle button with a secondary-container
 * background, wrapping the Material 3 [FilledTonalIconToggleButton]
 * with the theme's default colors.
 *
 * Use this for medium-emphasis toggle actions. [content] should
 * typically be an [Icon] that changes with [checked]; the button
 * enforces a 48 x 48 dp minimum touch target.
 *
 * @param checked Whether the button is currently toggled on.
 * @param onCheckedChange Called with the new state when the button is
 *   tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param shape Shape of the button's container. Defaults to
 *   [IconButtonDefaults.filledShape].
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.filledTonalIconToggleButtonColors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishIconToggleButton
 * @see StylishFilledIconToggleButton
 * @see StylishOutlinedIconToggleButton
 */
@Composable
public fun StylishFilledTonalIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.filledShape,
    colors: IconToggleButtonColors = IconButtonDefaults.filledTonalIconToggleButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    FilledTonalIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.stylishTestTag("filled_tonal_icon_toggle_button"),
        enabled = enabled,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
        content = content,
    )
}

/**
 * An outlined icon toggle button with a hairline border, wrapping the
 * Material 3 [OutlinedIconToggleButton] with the theme's default
 * colors.
 *
 * Use this for toggle actions that need visual separation from their
 * surroundings. [content] should typically be an [Icon] that changes
 * with [checked]; the button enforces a 48 x 48 dp minimum touch
 * target.
 *
 * @param checked Whether the button is currently toggled on.
 * @param onCheckedChange Called with the new state when the button is
 *   tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in the disabled color scheme.
 * @param shape Shape of the button's container and border. Defaults to
 *   [IconButtonDefaults.outlinedShape].
 * @param colors Colors used in each state. Defaults to
 *   [IconButtonDefaults.outlinedIconToggleButtonColors].
 * @param border Border drawn around the container. Defaults to
 *   [IconButtonDefaults.outlinedIconToggleButtonBorder] resolved from
 *   [enabled] and [checked]. Pass `null` for no border.
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param content The content of the button, typically an [Icon].
 *
 * @see StylishIconToggleButton
 * @see StylishFilledIconToggleButton
 * @see StylishFilledTonalIconToggleButton
 */
@Composable
public fun StylishOutlinedIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.outlinedShape,
    colors: IconToggleButtonColors = IconButtonDefaults.outlinedIconToggleButtonColors(),
    border: BorderStroke? = IconButtonDefaults.outlinedIconToggleButtonBorder(enabled, checked),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    OutlinedIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.stylishTestTag("outlined_icon_toggle_button"),
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        content = content,
    )
}

@Preview(name = "Stylish icon toggle button", showBackground = true, widthDp = 393)
@Composable
private fun StylishIconToggleButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var checked by remember { mutableStateOf(true) }
            StylishIconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                Icon(
                    if (checked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "お気に入り",
                )
            }
        }
    }
}

@Preview(name = "Stylish icon toggle button variants", showBackground = true, widthDp = 393)
@Composable
private fun StylishIconToggleButtonVariantsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishIconToggleButton(checked = true, onCheckedChange = {}) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
                StylishFilledIconToggleButton(checked = true, onCheckedChange = {}) {
                    Icon(Icons.Default.Star, contentDescription = null)
                }
                StylishFilledTonalIconToggleButton(checked = true, onCheckedChange = {}) {
                    Icon(Icons.Default.Star, contentDescription = null)
                }
                StylishOutlinedIconToggleButton(checked = true, onCheckedChange = {}) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
            }
        }
    }
}

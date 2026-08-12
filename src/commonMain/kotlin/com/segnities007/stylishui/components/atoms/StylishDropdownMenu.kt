package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A dropdown menu that shows a list of choices on a temporary surface,
 * wrapping the Material 3 [DropdownMenu] with Stylish defaults.
 *
 * Populate the menu with [StylishDropdownMenuItem] (or any other
 * content) and anchor it to a trigger by placing both inside the same
 * composable. Control [expanded] and dismiss via [onDismissRequest];
 * the typical pattern is to dismiss when the anchor is tapped again,
 * an item is selected, or the user taps outside.
 *
 * @param expanded Whether the menu is currently shown.
 * @param onDismissRequest Called when the user requests dismissal,
 *   such as by tapping outside the menu bounds.
 * @param modifier Modifier applied to the menu content.
 * @param offset [DpOffset] from the original position of the menu.
 *   Defaults to [DpOffset.Zero].
 * @param properties [PopupProperties] for further customization of the
 *   popup behavior. Defaults to a focusable popup.
 * @param shape Shape of the menu surface. Defaults to
 *   [MaterialTheme.shapes.extraSmall].
 * @param containerColor Background color of the menu. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainer].
 * @param content The menu content, typically one or more
 *   [StylishDropdownMenuItem]s, laid out in a [ColumnScope].
 *
 * @see StylishDropdownMenuItem
 */
@Composable
public fun StylishDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset.Zero,
    properties: PopupProperties = PopupProperties(focusable = true),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset,
        properties = properties,
        shape = shape,
        containerColor = containerColor,
        content = content,
    )
}

/**
 * A single selectable row inside a [StylishDropdownMenu], wrapping the
 * Material 3 [DropdownMenuItem].
 *
 * @param text The item's label, typically a [Text] composable.
 * @param onClick Called when the item is tapped. The caller is
 *   responsible for dismissing the enclosing menu and applying the
 *   selection.
 * @param modifier Modifier applied to the menu item.
 * @param leadingIcon Optional icon shown before the label.
 * @param trailingIcon Optional content shown after the label (an icon
 *   or a keyboard-shortcut hint such as `Text("Ctrl+K")`).
 * @param enabled When `false`, the item ignores pointer input and
 *   renders visually disabled.
 *
 * @see StylishDropdownMenu
 */
@Composable
public fun StylishDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    DropdownMenuItem(
        text = text,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
    )
}

@Preview(name = "Stylish dropdown menu", showBackground = true, widthDp = 393, heightDp = 400)
@Composable
private fun StylishDropdownMenuPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Box {
                StylishDropdownMenu(expanded = true, onDismissRequest = {}) {
                    StylishDropdownMenuItem(
                        text = { Text("項目を選択") },
                        onClick = {},
                        leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
                    )
                    StylishDropdownMenuItem(text = { Text("編集") }, onClick = {})
                    StylishDropdownMenuItem(text = { Text("削除") }, onClick = {}, enabled = false)
                }
            }
        }
    }
}

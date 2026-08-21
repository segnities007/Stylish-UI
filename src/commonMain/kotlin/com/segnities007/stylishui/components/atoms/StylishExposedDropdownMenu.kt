package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A box that anchors an exposed dropdown menu to a text field, wrapping
 * the Material 3 [ExposedDropdownMenuBox].
 *
 * Place a text field (with [ExposedDropdownMenuBoxScope.menuAnchor])
 * and a [StylishExposedDropdownMenu] inside [content]. The box tracks
 * the field's bounds, positions the menu below it, and constrains the
 * menu's width to the field and its height to the visible window.
 *
 * @param expanded Whether the menu is currently shown.
 * @param onExpandedChange Called when the menu's expansion state
 *   changes (e.g. the anchor is tapped or the menu is dismissed).
 * @param modifier Modifier applied to the box root.
 * @param content The box content: typically a text field and a
 *   [StylishExposedDropdownMenu], in [ExposedDropdownMenuBoxScope] for
 *   access to [ExposedDropdownMenuBoxScope.menuAnchor].
 *
 * @see StylishExposedDropdownMenu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishExposedDropdownMenuBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ExposedDropdownMenuBoxScope.() -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier.stylishTestTag("exposed_dropdown_menu_box"),
        content = content,
    )
}

/**
 * The menu of a [StylishExposedDropdownMenuBox], wrapping the Material 3
 * [ExposedDropdownMenuDefaults.ExposedDropdownMenu].
 *
 * Must be called inside the [content] of [StylishExposedDropdownMenuBox]
 * so the box can constrain the menu's width to the anchor and its
 * height to the visible window. Populate it with [StylishDropdownMenuItem]
 * (or any other content) and control [expanded] /
 * [onDismissRequest] like a regular dropdown menu.
 *
 * @param expanded Whether the menu is currently shown.
 * @param onDismissRequest Called when the user requests dismissal,
 *   such as by tapping outside the menu bounds.
 * @param modifier Modifier applied to the menu content.
 * @param scrollState A [ScrollState] used by the menu's content for
 *   vertical scrolling. Defaults to a remembered scroll state.
 * @param matchAnchorWidth Whether the menu's width should be
 *   constrained to match the anchor's width. Defaults to `true`.
 * @param shape Shape of the menu surface. Defaults to
 *   [MenuDefaults.shape].
 * @param containerColor Background color of the menu. Defaults to
 *   [MenuDefaults.containerColor].
 * @param tonalElevation Tonal elevation of the menu surface. Defaults
 *   to [MenuDefaults.TonalElevation].
 * @param shadowElevation Shadow elevation below the menu. Defaults to
 *   [MenuDefaults.ShadowElevation].
 * @param border Border drawn around the menu container. Defaults to
 *   `null` (no border).
 * @param content The menu content, typically one or more
 *   [StylishDropdownMenuItem]s, laid out in a [ColumnScope].
 *
 * @see StylishExposedDropdownMenuBox
 * @see StylishDropdownMenu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ExposedDropdownMenuBoxScope.StylishExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchAnchorWidth: Boolean = true,
    shape: Shape = MenuDefaults.shape,
    containerColor: Color = MenuDefaults.containerColor,
    tonalElevation: Dp = MenuDefaults.TonalElevation,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.stylishTestTag("exposed_dropdown_menu"),
        scrollState = scrollState,
        matchAnchorWidth = matchAnchorWidth,
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish exposed dropdown menu", showBackground = true, widthDp = 393, heightDp = 400)
@Composable
private fun StylishExposedDropdownMenuPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var expanded by remember { mutableStateOf(true) }
            var selected by remember { mutableStateOf("カテゴリ") }
            StylishExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                TextField(
                    value = selected,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("カテゴリ") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                )
                StylishExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    listOf("カテゴリ", "編集", "削除").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selected = option
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

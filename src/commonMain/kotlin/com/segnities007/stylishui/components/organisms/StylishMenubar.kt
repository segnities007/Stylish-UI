package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A keyboard-friendly menubar built from standard Compose menu primitives.
 *
 * @param menus Top-level menus and their actions.
 * @param modifier Modifier applied to the menubar row.
 */
@Composable
public fun StylishMenubar(menus: List<StylishMenu>, modifier: Modifier = Modifier) {
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    var focusAfterDismiss by remember { mutableStateOf<Int?>(null) }
    val menuFocusRequesters = remember(menus.size) { List(menus.size) { FocusRequester() } }
    fun dismissMenu(focusIndex: Int? = expandedIndex) {
        focusAfterDismiss = focusIndex
        expandedIndex = null
    }
    LaunchedEffect(expandedIndex) {
        if (expandedIndex == null) {
            focusAfterDismiss?.let { index ->
                menuFocusRequesters.getOrNull(index)?.requestFocus()
                focusAfterDismiss = null
            }
        }
    }
    Row(modifier.stylishTestTag("menubar").semantics { contentDescription = "Menu bar" }) {
        menus.forEachIndexed { index, menu ->
            Box {
                TextButton(
                    onClick = {
                        if (expandedIndex == index) dismissMenu(index) else expandedIndex = index
                    },
                    modifier = Modifier
                        .focusRequester(menuFocusRequesters[index])
                        .onPreviewKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                        when (event.key) {
                            Key.Enter, Key.Spacebar -> {
                                expandedIndex = if (expandedIndex == index) null else index
                                true
                            }
                            Key.DirectionDown, Key.DirectionUp -> { expandedIndex = index; true }
                            Key.Escape -> { dismissMenu(index); true }
                            Key.DirectionLeft -> { expandedIndex = (index - 1).coerceAtLeast(0); true }
                            Key.DirectionRight -> { expandedIndex = (index + 1).coerceAtMost(menus.lastIndex); true }
                            else -> false
                        }
                    }
                    .semantics {
                        contentDescription = menu.label
                        selected = expandedIndex == index
                        stateDescription = if (expandedIndex == index) "Expanded" else "Collapsed"
                    },
                ) { Text(menu.label) }
                DropdownMenu(
                    expanded = expandedIndex == index,
                    onDismissRequest = { dismissMenu(index) },
                    modifier = Modifier.semantics { contentDescription = menu.label },
                ) {
                    menu.items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.label) },
                            onClick = { item.onClick(); dismissMenu(index) },
                            enabled = item.enabled,
                            modifier = Modifier.semantics {
                                contentDescription = item.label
                                role = androidx.compose.ui.semantics.Role.Button
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Stylish menubar", showBackground = true, widthDp = 393)
@Composable
private fun StylishMenubarPreview() {
    StylishTheme(darkTheme = false) {
        StylishMenubar(
            menus = listOf(
                StylishMenu("File", listOf(StylishMenuItem("Open", {}))),
                StylishMenu("Edit", listOf(StylishMenuItem("Copy", {}))),
            ),
        )
    }
}

package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Controlled context menu primitive.
 *
 * @param expanded Whether the menu is currently visible.
 * @param onDismissRequest Called when the menu should be closed.
 * @param items Actions displayed in the menu.
 * @param modifier Modifier applied to the anchor/content container.
 * @param content Composable content that anchors the menu.
 */
@Composable
public fun StylishContextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<StylishMenuItem>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier.testTag("stylish_context_menu_anchor")) {
        content()
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.testTag("stylish_context_menu").semantics {
                contentDescription = "Context menu"
            },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.label) },
                    onClick = { item.onClick(); onDismissRequest() },
                    enabled = item.enabled,
                    modifier = Modifier.semantics {
                        contentDescription = item.label
                        role = Role.Button
                    },
                )
            }
        }
    }
}

@Preview(name = "Stylish context menu", showBackground = true)
@Composable
private fun StylishContextMenuPreview() {
    StylishTheme(darkTheme = false) {
        StylishContextMenu(
            expanded = true,
            onDismissRequest = {},
            items = listOf(
                StylishMenuItem("Edit", {}),
                StylishMenuItem("Delete", {}),
            ),
        ) { Text("Open context menu") }
    }
}

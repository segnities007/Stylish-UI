package com.segnities007.stylishui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.segnities007.stylishui.components.organisms.StylishColorPicker
import com.segnities007.stylishui.components.organisms.StylishContextMenu
import com.segnities007.stylishui.components.organisms.StylishMenuItem
import com.segnities007.stylishui.components.organisms.StylishTree
import com.segnities007.stylishui.components.organisms.StylishTreeNode
import com.segnities007.stylishui.components.organisms.StylishTransfer
import com.segnities007.stylishui.components.organisms.StylishTransferItem
import com.segnities007.stylishui.theme.StylishTheme
import androidx.compose.ui.graphics.Color
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class AdvancedComponentsSmokeTest {
    @Test
    fun treeAndTransferRender() = runComposeUiTest {
        var selectedKeys by mutableStateOf<Set<Any>>(emptySet())
        setContent {
            StylishTheme(darkTheme = false) {
                StylishTree(
                    nodes = listOf(StylishTreeNode("root", "Root", "root", listOf(StylishTreeNode("child", "Child", "child")))),
                )
                StylishTransfer(
                    available = listOf(
                        StylishTransferItem("a", "A", "Alpha"),
                        StylishTransferItem("b", "B", "Beta"),
                    ),
                    selectedKeys = selectedKeys,
                    onSelectedKeysChange = { selectedKeys = it },
                )
            }
        }
        onNodeWithText("Root").assertIsDisplayed()
        onNodeWithText("Alpha").assertIsDisplayed()
        onNodeWithText("Beta").assertIsDisplayed()
        onNodeWithText("Alpha").performClick()
        onNodeWithText("Beta").performClick()
        onNodeWithContentDescription("Move to Selected").performClick()
        onNodeWithText("Alpha").assertIsDisplayed()
        onNodeWithText("Beta").assertIsDisplayed()
    }

    @Test
    fun colorAndContextMenuRender() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishColorPicker(Color.Red, onColorChange = {})
                StylishContextMenu(
                    expanded = true,
                    onDismissRequest = {},
                    items = listOf(StylishMenuItem("Action", onClick = {})),
                ) { Text("Anchor") }
            }
        }
        onNodeWithText("Action").assertIsDisplayed()
        onNodeWithText("Anchor").assertIsDisplayed()
    }
}

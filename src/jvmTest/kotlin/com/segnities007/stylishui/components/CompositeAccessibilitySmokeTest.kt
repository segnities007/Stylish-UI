package com.segnities007.stylishui.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.organisms.StylishCommandItem
import com.segnities007.stylishui.components.organisms.StylishCommandPalette
import com.segnities007.stylishui.components.organisms.StylishMenu
import com.segnities007.stylishui.components.organisms.StylishMenuItem
import com.segnities007.stylishui.components.organisms.StylishMenubar
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals

/** Linux-reproducible semantics checks for popup and command composites. */
@OptIn(ExperimentalTestApi::class)
class CompositeAccessibilitySmokeTest {

    @Test
    fun menubarPublishesCollapsedStateAndOpensAnAccessibleAction() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishMenubar(
                    menus = listOf(StylishMenu("File", listOf(StylishMenuItem("Open", {})))),
                )
            }
        }

        val file = onNodeWithContentDescription("File")
        file.assertIsDisplayed()
        assertEquals("Collapsed", file.fetchSemanticsNode().config[SemanticsProperties.StateDescription])
        file.performClick()
        onNodeWithText("Open").assertIsDisplayed()
    }

    @Test
    fun commandPalettePublishesInputAndSelectedItemSemantics() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishCommandPalette(
                    expanded = true,
                    onDismiss = {},
                    query = "",
                    onQueryChange = {},
                    items = listOf(StylishCommandItem("Open", {})),
                )
            }
        }

        onNodeWithTag("stylish_command_palette_input").assertIsDisplayed()
        val item = onNodeWithTag("stylish_command_palette_item_0")
        item.assertIsDisplayed()
        assertEquals(true, item.fetchSemanticsNode().config[SemanticsProperties.Selected])
    }
}

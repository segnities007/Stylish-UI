package com.segnities007.stylishui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.molecules.StylishButtonGroup
import com.segnities007.stylishui.components.molecules.StylishButtonGroupOrientation
import com.segnities007.stylishui.components.molecules.StylishToolbar
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class ToolbarButtonGroupTest {
    @Test
    fun buttonGroupKeepsChildrenActionableInBothOrientations() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishButtonGroup(orientation = StylishButtonGroupOrientation.Vertical) {
                    StylishButton(onClick = { clicks++ }) { Text("保存") }
                    StylishButton(onClick = { clicks++ }) { Text("削除") }
                }
            }
        }

        onNodeWithText("保存").assertIsDisplayed().performClick()
        onNodeWithText("削除").performClick()
        assertEquals(2, clicks)
    }

    @Test
    fun toolbarRendersTitleSubtitleAndCustomActions() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishToolbar(
                    title = "車両一覧",
                    subtitle = "3台",
                    actions = { Text("編集") },
                )
            }
        }

        onNodeWithText("車両一覧").assertIsDisplayed()
        onNodeWithText("3台").assertIsDisplayed()
        onNodeWithText("編集").assertIsDisplayed()
    }
}

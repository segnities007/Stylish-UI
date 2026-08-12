package com.segnities007.stylishui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishCard
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishSwitch
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.organisms.StylishConnectedSegmentedControl
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class InteractionTest {

    @Test
    fun buttonClickInvokesCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishButton(onClick = { clicks++ }) { Text("ボタン") }
            }
        }
        onNodeWithText("ボタン").performClick()
        kotlin.test.assertEquals(1, clicks)
    }

    @Test
    fun disabledButtonDoesNotInvokeCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishButton(onClick = { clicks++ }, enabled = false) { Text("無効ボタン") }
            }
        }
        onNodeWithText("無効ボタン").assertIsNotEnabled()
        onNodeWithText("無効ボタン").performClick()
        kotlin.test.assertEquals(0, clicks)
    }

    @Test
    fun switchToggleInvokesCallback() = runComposeUiTest {
        var checked = false
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSwitch(checked = checked, onCheckedChange = { checked = it })
            }
        }
        onNode(androidx.compose.ui.test.hasClickAction()).performClick()
        kotlin.test.assertTrue(checked)
    }

    @Test
    fun chipClickInvokesCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishChip(label = "チップ", onClick = { clicks++ })
            }
        }
        onNodeWithText("チップ").performClick()
        kotlin.test.assertEquals(1, clicks)
    }

    @Test
    fun cardClickInvokesCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishCard(title = "カード", onClick = { clicks++ })
            }
        }
        onNodeWithText("カード").performClick()
        kotlin.test.assertEquals(1, clicks)
    }

    @Test
    fun connectedButtonRowClickInvokesItemCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedButtonRow(
                    items = listOf(
                        StylishConnectedButtonItem(onClick = { clicks++ }) { Text("連結ボタン") },
                    ),
                )
            }
        }
        onNodeWithText("連結ボタン").performClick()
        kotlin.test.assertEquals(1, clicks)
    }

    @Test
    fun disabledConnectedChipDoesNotInvokeCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedChipRow(
                    items = listOf(
                        StylishConnectedChipItem("無効チップ", onClick = { clicks++ }, enabled = false),
                    ),
                )
            }
        }
        onNodeWithText("無効チップ").assertIsNotEnabled()
        onNodeWithText("無効チップ").performClick()
        kotlin.test.assertEquals(0, clicks)
    }

    @Test
    fun navigationBarItemClickInvokesCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                StylishNavigationBar(
                    items = listOf(
                        StylishNavigationItem(Icons.Default.Home, "ホーム", onClick = { clicks++ }),
                    ),
                )
            }
        }
        onNodeWithText("ホーム").performClick()
        kotlin.test.assertEquals(1, clicks)
    }

    @Test
    fun segmentedControlSelectionInvokesCallback() = runComposeUiTest {
        var selected = "a"
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedSegmentedControl(
                    options = listOf(
                        StylishSegmentedOption("a", "A"),
                        StylishSegmentedOption("b", "B"),
                    ),
                    selectedValue = selected,
                    onSelected = { selected = it },
                )
            }
        }
        onNodeWithText("B").performClick()
        kotlin.test.assertEquals("b", selected)
    }

    @Test
    fun enabledChipIsEnabled() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishChip(label = "有効チップ", onClick = {})
            }
        }
        onNodeWithText("有効チップ").assertIsEnabled()
    }
}

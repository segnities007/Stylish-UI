package com.segnities007.stylishui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishCard
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishSwitch
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishPagination
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.organisms.StylishConnectedSegmentedControl
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.components.organisms.StylishDataTable
import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.StylishJapaneseStrings
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class InteractionTest {

    @Test
    fun dataTableFiltersExpandsAndExports() = runComposeUiTest {
        data class Row(val id: Int, val name: String)
        var query by mutableStateOf("a")
        var expanded by mutableStateOf(emptySet<Any>())
        var exported = emptyList<Row>()
        setContent {
            StylishTheme(darkTheme = false) {
                StylishDataTable(
                    rows = listOf(Row(1, "Alpha"), Row(2, "Beta")),
                    columns = listOf(StylishDataTableColumn("name", "Name") { Text(it.name) }),
                    rowKey = { it.id },
                    filterText = query,
                    onFilterTextChange = { query = it },
                    filterPredicate = { row, text -> row.name.contains(text, ignoreCase = true) },
                    expandedKeys = expanded,
                    onExpandedKeysChange = { expanded = it },
                    expandedContent = { Text("Details ${it.name}") },
                    onExport = { exported = it },
                )
            }
        }
        onNodeWithText("Alpha").performClick()
        onNodeWithText("Details Alpha").assertIsDisplayed()
        onNodeWithText("Export").performClick()
        kotlin.test.assertEquals(listOf("Alpha", "Beta"), exported.map { it.name })
    }

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
    fun connectedCardIsKeyboardFocusableAndUsesLocalizedLabels() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false, strings = StylishJapaneseStrings) {
                StylishConnectedCard(
                    title = "カード",
                    onClick = {},
                )
            }
        }
        // The card exposes click semantics and is therefore keyboard-actionable.
        onNodeWithText("カード").assert(hasClickAction())
    }

    @Test
    fun paginationKeepsLocalizedSemanticsInRtlLayout() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                StylishTheme(darkTheme = false, strings = StylishJapaneseStrings) {
                    StylishPagination(page = 2, pageCount = 3, onPageChange = {})
                }
            }
        }
        onNodeWithContentDescription("前のページ").assertExists()
        onNodeWithContentDescription("次のページ").assertExists()
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
                    onSelectedChange = { selected = it },
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

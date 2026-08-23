package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import com.segnities007.stylishui.components.atoms.StylishExtendedFab
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.atoms.StylishFormTextField
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.molecules.StylishConnectedCardColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.molecules.StylishEmptyState
import com.segnities007.stylishui.components.organisms.StylishConnectedSegmentedControl
import com.segnities007.stylishui.components.organisms.StylishDialogActions
import com.segnities007.stylishui.components.patterns.StylishFooter
import com.segnities007.stylishui.components.patterns.StylishHeader
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class ComponentSmokeTest {

    @Test
    fun sectionTitleRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSectionTitle("テスト見出し")
            }
        }
        onNodeWithText("テスト見出し").assertIsDisplayed()
    }

    @Test
    fun connectedCardRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedCard(
                    title = "カードタイトル",
                    supportingText = "補足テキスト",
                )
            }
        }
        onNodeWithText("カードタイトル").assertIsDisplayed()
        onNodeWithText("補足テキスト").assertIsDisplayed()
    }

    @Test
    fun fabHonorsParentAlignmentThroughVisibilityAnimation() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Box(Modifier.size(200.dp)) {
                    StylishFab(
                        imageVector = Icons.Default.Add,
                        contentDescription = "追加",
                        onClick = {},
                        modifier = Modifier.align(Alignment.BottomEnd),
                    )
                }
            }
        }

        val bounds = onNodeWithTag("stylish_fab").fetchSemanticsNode().boundsInRoot
        assertEquals(144f, bounds.left)
        assertEquals(144f, bounds.top)
    }

    @Test
    fun extendedFabHonorsParentAlignmentThroughVisibilityAnimation() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Box(Modifier.size(240.dp)) {
                    StylishExtendedFab(
                        text = "追加",
                        icon = Icons.Default.Add,
                        contentDescription = "追加",
                        onClick = {},
                        modifier = Modifier
                            .width(100.dp)
                            .align(Alignment.BottomEnd),
                    )
                }
            }
        }

        val bounds = onNodeWithTag("stylish_extended_fab").fetchSemanticsNode().boundsInRoot
        assertEquals(140f, bounds.left)
        assertTrue(bounds.top > 0f)
    }

    @Test
    fun headerHonorsParentAlignmentThroughVisibilityAnimation() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Box(Modifier.size(240.dp)) {
                    StylishHeader(
                        title = { Text("ヘッダー") },
                        modifier = Modifier
                            .width(100.dp)
                            .align(Alignment.BottomEnd),
                    )
                }
            }
        }

        val bounds = onNodeWithText("ヘッダー").fetchSemanticsNode().boundsInRoot
        assertTrue(bounds.left >= 140f)
        assertTrue(bounds.top > 0f)
    }

    @Test
    fun footerHonorsParentAlignmentThroughVisibilityAnimation() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Box(Modifier.size(240.dp)) {
                    StylishFooter(
                        content = { Text("フッター") },
                        modifier = Modifier
                            .width(100.dp)
                            .align(Alignment.BottomEnd),
                    )
                }
            }
        }

        val bounds = onNodeWithText("フッター").fetchSemanticsNode().boundsInRoot
        assertTrue(bounds.left >= 140f)
        assertTrue(bounds.top > 0f)
    }

    @Test
    fun formTextFieldRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishFormTextField(
                    value = "",
                    onValueChange = {},
                    label = "ラベル",
                    placeholder = "プレースホルダー",
                )
            }
        }
        onNodeWithText("ラベル").assertIsDisplayed()
    }

    @Test
    fun connectedButtonRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedButtonRow(
                    items = listOf(
                        StylishConnectedButtonItem(onClick = {}) { Text("ボタン1") },
                        StylishConnectedButtonItem(onClick = {}) { Text("ボタン2") },
                    ),
                )
            }
        }
        onNodeWithText("ボタン1").assertIsDisplayed()
        onNodeWithText("ボタン2").assertIsDisplayed()
    }

    @Test
    fun connectedCardRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedCardRow(
                    items = listOf(
                        StylishConnectedCardItem("項目A", "説明A"),
                        StylishConnectedCardItem("項目B", "説明B"),
                    ),
                )
            }
        }
        onNodeWithText("項目A").assertIsDisplayed()
        onNodeWithText("項目B").assertIsDisplayed()
    }

    @Test
    fun connectedChipRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedChipRow(
                    items = listOf(
                        StylishConnectedChipItem("チップ1", onClick = {}),
                        StylishConnectedChipItem("チップ2", onClick = {}),
                    ),
                )
            }
        }
        onNodeWithText("チップ1").assertIsDisplayed()
        onNodeWithText("チップ2").assertIsDisplayed()
    }

    @Test
    fun connectedCardColumnRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedCardColumn(
                    items = listOf(
                        StylishConnectedCardItem(title = "アイテム1", onClick = {}),
                        StylishConnectedCardItem(title = "アイテム2", onClick = {}),
                    ),
                )
            }
        }
        onNodeWithText("アイテム1").assertIsDisplayed()
        onNodeWithText("アイテム2").assertIsDisplayed()
    }

    @Test
    fun emptyStateRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishEmptyState(
                    icon = Icons.Default.Search,
                    title = "データなし",
                    description = "まだ何もありません",
                )
            }
        }
        onNodeWithText("データなし").assertIsDisplayed()
        onNodeWithText("まだ何もありません").assertIsDisplayed()
    }

    @Test
    fun segmentedControlRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedSegmentedControl(
                    options = listOf(
                        StylishSegmentedOption("a", "オプションA"),
                        StylishSegmentedOption("b", "オプションB"),
                    ),
                    selectedValue = "a",
                    onSelectedChange = {},
                )
            }
        }
        onNodeWithText("オプションA").assertIsDisplayed()
        onNodeWithText("オプションB").assertIsDisplayed()
    }

    @Test
    fun dialogActionsRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Column {
                    StylishDialogActions(
                        confirmLabel = "確認",
                        cancelLabel = "キャンセル",
                        onConfirm = {},
                        onCancel = {},
                    )
                }
            }
        }
        onNodeWithText("確認").assertIsDisplayed()
        onNodeWithText("キャンセル").assertIsDisplayed()
    }
}

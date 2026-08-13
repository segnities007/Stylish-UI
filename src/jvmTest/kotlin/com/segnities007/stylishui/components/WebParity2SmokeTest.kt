package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.atoms.StylishCode
import com.segnities007.stylishui.components.atoms.StylishVisuallyHidden
import com.segnities007.stylishui.components.molecules.StylishAlert
import com.segnities007.stylishui.components.molecules.StylishAlertVariant
import com.segnities007.stylishui.components.molecules.StylishAutocomplete
import com.segnities007.stylishui.components.molecules.StylishDescriptions
import com.segnities007.stylishui.components.molecules.StylishDescriptionItem
import com.segnities007.stylishui.components.molecules.StylishResult
import com.segnities007.stylishui.components.molecules.StylishResultVariant
import com.segnities007.stylishui.components.molecules.StylishToastData
import com.segnities007.stylishui.components.molecules.StylishToastHost
import com.segnities007.stylishui.components.molecules.StylishToastVariant
import com.segnities007.stylishui.components.molecules.rememberStylishToastHostState
import com.segnities007.stylishui.components.organisms.StylishPopconfirm
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class WebParity2SmokeTest {

    @Test
    fun alertRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAlert(
                    title = "お知らせ",
                    message = "新しいバージョンがあります。",
                    variant = StylishAlertVariant.Info,
                )
            }
        }
        onNodeWithText("新しいバージョンがあります。").assertIsDisplayed()
    }

    @Test
    fun toastHostRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                val hostState = rememberStylishToastHostState()
                hostState.toasts.add(StylishToastData("保存しました", StylishToastVariant.Success))
                StylishToastHost(hostState)
            }
        }
        onNodeWithText("保存しました").assertIsDisplayed()
    }

    @Test
    fun resultRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishResult(
                    title = "完了しました",
                    description = "処理が正常に終了しました。",
                    variant = StylishResultVariant.Success,
                )
            }
        }
        onNodeWithText("完了しました").assertIsDisplayed()
    }

    @Test
    fun popconfirmRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Column {
                    StylishPopconfirm(
                        expanded = true,
                        onExpandedChange = {},
                        anchor = { Text("トリガー") },
                        title = "削除しますか?",
                        confirmLabel = "削除",
                        onConfirm = {},
                    )
                }
            }
        }
        waitForIdle()
    }

    @Test
    fun descriptionsRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishDescriptions(
                    items = listOf(
                        StylishDescriptionItem("車両名", "Stylish Car"),
                        StylishDescriptionItem("年式", "2026"),
                    ),
                )
            }
        }
        onNodeWithText("Stylish Car").assertIsDisplayed()
    }

    @Test
    fun autocompleteRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAutocomplete(
                    value = "",
                    onValueChange = {},
                    options = listOf("Stylish UI", "Kotlin"),
                    label = "検索",
                )
            }
        }
        onNodeWithText("検索").assertIsDisplayed()
    }

    @Test
    fun codeRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishCode("val x = 1")
            }
        }
        onNodeWithText("val x = 1").assertIsDisplayed()
    }

    @Test
    fun visuallyHiddenKeepsContentInTree() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishVisuallyHidden {
                    Text("スクリーンリーダー専用")
                }
            }
        }
        onNodeWithText("スクリーンリーダー専用", useUnmergedTree = true).assertExists()
    }
}

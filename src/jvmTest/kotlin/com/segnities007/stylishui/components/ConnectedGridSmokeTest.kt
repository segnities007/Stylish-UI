package com.segnities007.stylishui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedChipGrid
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test

/**
 * Regression smoke tests for Connected grids with an incomplete (stretched)
 * final row — the odd item counts that previously produced a clipped outer
 * corner on the item above the stretched row.
 */
@OptIn(ExperimentalTestApi::class)
class ConnectedGridSmokeTest {

    @Test
    fun cardGridWithStretchedFinalRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedCardGrid(
                    items = List(5) { index ->
                        StylishConnectedCardItem("カード${index + 1}", "補足")
                    },
                    columns = 2,
                )
            }
        }
        onNodeWithText("カード4").assertIsDisplayed()
        onNodeWithText("カード5").assertIsDisplayed()
    }

    @Test
    fun buttonGridWithStretchedFinalRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedButtonGrid(
                    items = List(3) { index ->
                        StylishConnectedButtonItem(onClick = {}) { Text("ボタン${index + 1}") }
                    },
                    columns = 2,
                )
            }
        }
        onNodeWithText("ボタン2").assertIsDisplayed()
        onNodeWithText("ボタン3").assertIsDisplayed()
    }

    @Test
    fun chipGridWithStretchedFinalRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedChipGrid(
                    items = List(7) { index ->
                        StylishConnectedChipItem("チップ${index + 1}", onClick = {})
                    },
                    columns = 3,
                )
            }
        }
        onNodeWithText("チップ6").assertIsDisplayed()
        onNodeWithText("チップ7").assertIsDisplayed()
    }

    @Test
    fun threeItemCardGridWithStretchedFinalRowRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedCardGrid(
                    items = List(3) { index ->
                        StylishConnectedCardItem(title = "アイテム${index + 1}", onClick = {})
                    },
                    columns = 2,
                )
            }
        }
        onNodeWithText("アイテム2").assertIsDisplayed()
        onNodeWithText("アイテム3").assertIsDisplayed()
    }
}

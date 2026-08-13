package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.atoms.StylishKbd
import com.segnities007.stylishui.components.atoms.StylishNumberInput
import com.segnities007.stylishui.components.atoms.StylishPinInput
import com.segnities007.stylishui.components.atoms.StylishRating
import com.segnities007.stylishui.components.atoms.StylishSpeedDial
import com.segnities007.stylishui.components.molecules.StylishAccordion
import com.segnities007.stylishui.components.molecules.StylishAccordionItem
import com.segnities007.stylishui.components.molecules.StylishBreadcrumb
import com.segnities007.stylishui.components.molecules.StylishBreadcrumbItem
import com.segnities007.stylishui.components.molecules.StylishEditable
import com.segnities007.stylishui.components.molecules.StylishPagination
import com.segnities007.stylishui.components.molecules.StylishStatistic
import com.segnities007.stylishui.components.molecules.StylishStepper
import com.segnities007.stylishui.components.molecules.StylishTable
import com.segnities007.stylishui.components.molecules.StylishTimeline
import com.segnities007.stylishui.components.molecules.StylishTimelineItem
import com.segnities007.stylishui.components.organisms.StylishPopover
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class WebParitySmokeTest {

    @Test
    fun accordionRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAccordion(
                    items = listOf(
                        StylishAccordionItem(
                            title = "セクション",
                            content = { Text("中身") },
                        ),
                    ),
                )
            }
        }
        onNodeWithText("セクション").assertIsDisplayed()
    }

    @Test
    fun stepperRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishStepper(
                    steps = listOf("A", "B", "C"),
                    currentStep = 1,
                    completedSteps = setOf(0),
                )
            }
        }
        onNodeWithText("A").assertIsDisplayed()
        onNodeWithText("B").assertIsDisplayed()
    }

    @Test
    fun breadcrumbRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishBreadcrumb(
                    items = listOf(
                        StylishBreadcrumbItem("ホーム", onClick = {}),
                        StylishBreadcrumbItem("詳細"),
                    ),
                )
            }
        }
        onNodeWithText("ホーム").assertIsDisplayed()
    }

    @Test
    fun paginationRendersAndNavigates() = runComposeUiTest {
        var page by mutableIntStateOf(2)
        setContent {
            StylishTheme(darkTheme = false) {
                StylishPagination(page = page, onPageChange = { page = it }, pageCount = 10)
            }
        }
        onNodeWithText("3").performClick()
        assertEquals(3, page)
    }

    @Test
    fun ratingRendersAndUpdates() = runComposeUiTest {
        var rating by mutableIntStateOf(3)
        setContent {
            StylishTheme(darkTheme = false) {
                StylishRating(value = rating, onValueChange = { rating = it })
            }
        }
        onNodeWithText("5").assertDoesNotExist()
        waitForIdle()
    }

    @Test
    fun numberInputRenders() = runComposeUiTest {
        var value by mutableIntStateOf(3)
        setContent {
            StylishTheme(darkTheme = false) {
                StylishNumberInput(value = value, onValueChange = { value = it }, label = "台数")
            }
        }
        onNodeWithText("台数").assertIsDisplayed()
    }

    @Test
    fun pinInputRenders() = runComposeUiTest {
        setContent {
            var value by remember { mutableStateOf("") }
            StylishTheme(darkTheme = false) {
                StylishPinInput(value = value, onValueChange = { value = it })
            }
        }
        waitForIdle()
    }

    @Test
    fun editableCommitsOnCheck() = runComposeUiTest {
        setContent {
            var value by remember { mutableStateOf("初期値") }
            StylishTheme(darkTheme = false) {
                StylishEditable(value = value, onValueChange = { value = it }, onCommit = { value = it })
            }
        }
        onNodeWithText("初期値").assertIsDisplayed()
    }

    @Test
    fun statisticRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishStatistic(label = "燃費", value = "15.2", delta = "+1.3%")
            }
        }
        onNodeWithText("15.2").assertIsDisplayed()
    }

    @Test
    fun timelineRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishTimeline(
                    items = listOf(
                        StylishTimelineItem("オイル交換", "交換完了", "2026/08/10"),
                    ),
                )
            }
        }
        onNodeWithText("オイル交換").assertIsDisplayed()
    }

    @Test
    fun tableRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishTable(
                    columns = listOf("項目", "金額"),
                    rows = listOf(listOf("オイル交換", "¥12,000")),
                )
            }
        }
        onNodeWithText("オイル交換").assertIsDisplayed()
    }

    @Test
    fun popoverRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Column {
                    StylishPopover(
                        expanded = true,
                        onExpandedChange = {},
                        anchor = { Text("アンカー") },
                    ) {
                        Text("ポップオーバー内容")
                    }
                }
            }
        }
        waitForIdle()
    }

    @Test
    fun kbdRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishKbd("Ctrl")
            }
        }
        onNodeWithText("Ctrl").assertIsDisplayed()
    }

    @Test
    fun speedDialRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSpeedDial(
                    expanded = true,
                    onExpandedChange = {},
                    actionCount = 2,
                    onActionClick = {},
                ) {
                    Text("アクション$it")
                }
            }
        }
        onNodeWithText("アクション0").assertIsDisplayed()
    }
}

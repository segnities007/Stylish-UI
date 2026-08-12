package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.components.atoms.StylishDropdownMenu
import com.segnities007.stylishui.components.atoms.StylishDropdownMenuItem
import com.segnities007.stylishui.components.atoms.StylishRangeSlider
import com.segnities007.stylishui.components.atoms.StylishSlider
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.molecules.StylishConnectedCardLazyColumn
import com.segnities007.stylishui.components.molecules.StylishListItem
import com.segnities007.stylishui.components.organisms.StylishAlertDialog
import com.segnities007.stylishui.components.organisms.StylishSearchBar
import com.segnities007.stylishui.components.patterns.StylishTopAppBar
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.StylishAnimationTokens
import com.segnities007.stylishui.tokens.StylishDimensions
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class, ExperimentalMaterial3Api::class)
class NewComponentsSmokeTest {

    @Test
    fun sliderRenders() = runComposeUiTest {
        var value by mutableFloatStateOf(0.5f)
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSlider(value = value, onValueChange = { value = it })
            }
        }
        waitForIdle()
    }

    @Test
    fun rangeSliderRenders() = runComposeUiTest {
        setContent {
            var range by remember { mutableStateOf(0.2f..0.8f) }
            StylishTheme(darkTheme = false) {
                StylishRangeSlider(value = range, onValueChange = { range = it })
            }
        }
        waitForIdle()
    }

    @Test
    fun avatarRendersInitials() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAvatar(initials = "SM")
            }
        }
        onNodeWithText("SM", substring = true).assertIsDisplayed()
    }

    @Test
    fun dropdownMenuExpandedRendersItems() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                Column {
                    StylishDropdownMenu(expanded = true, onDismissRequest = {}) {
                        StylishDropdownMenuItem(text = { Text("メニュー項目") }, onClick = {})
                    }
                }
            }
        }
        waitForIdle()
    }

    @Test
    fun listItemRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishListItem(
                    headline = "リスト見出し",
                    supportingText = "リスト説明",
                )
            }
        }
        onNodeWithText("リスト見出し").assertIsDisplayed()
        onNodeWithText("リスト説明").assertIsDisplayed()
    }

    @Test
    fun alertDialogComposesWithoutCrash() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAlertDialog(
                    onDismissRequest = {},
                    title = { Text("ダイアログタイトル") },
                    text = { Text("ダイアログ本文") },
                    confirmButton = { Text("確認") },
                )
            }
        }
        waitForIdle()
    }

    @Test
    fun searchBarRenders() = runComposeUiTest {
        setContent {
            var query by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }
            StylishTheme(darkTheme = false) {
                StylishSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {},
                    active = active,
                    onActiveChange = { active = it },
                ) {
                    Text("検索結果")
                }
            }
        }
        waitForIdle()
    }

    @Test
    fun topAppBarRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishTopAppBar(title = { Text("トップバー") })
            }
        }
        onNodeWithText("トップバー").assertIsDisplayed()
    }

    @Test
    fun lazyConnectedCardColumnRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishConnectedCardLazyColumn(
                    items = List(5) { index ->
                        StylishConnectedCardItem(title = "遅延項目 $index")
                    },
                )
            }
        }
        onNodeWithText("遅延項目 0").assertIsDisplayed()
    }

    @Test
    fun barChartRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                SimpleBarChart(
                    contentDescriptionPrefix = "棒グラフ",
                    emptyLabel = "データなし",
                    data = listOf(
                        BarChartData("A", 10f),
                        BarChartData("B", 20f),
                    ),
                )
            }
        }
        waitForIdle()
    }

    @Test
    fun lineChartRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                SimpleLineChart(
                    contentDescriptionPrefix = "折れ線グラフ",
                    emptyLabel = "データなし",
                    data = listOf(
                        LineChartData("A", 10f),
                        LineChartData("B", 20f),
                    ),
                )
            }
        }
        waitForIdle()
    }
}

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class ThemeAndTokensTest {

    @Test
    fun darkThemeRendersCoreComponents() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = true) {
                Column {
                    Text("ダークテーマ")
                    StylishAvatar(initials = "DK")
                    StylishListItem(headline = "項目")
                }
            }
        }
        onNodeWithText("ダークテーマ").assertIsDisplayed()
    }

    @Test
    fun dimensionTokensPropagateThroughTheme() = runComposeUiTest {
        setContent {
            StylishTheme(
                darkTheme = false,
                dimensions = StylishDimensions(sectionSpacing = 60.dp),
            ) {
                var readBack by remember { mutableStateOf("") }
                readBack = "${StylishTheme.dimensions.sectionSpacing.value}"
                Text(readBack)
            }
        }
        onNodeWithText("60.0").assertIsDisplayed()
    }

    @Test
    fun animationTokensPropagateThroughTheme() = runComposeUiTest {
        setContent {
            StylishTheme(
                darkTheme = false,
                animation = StylishAnimationTokens(durationShort = 250),
            ) {
                var readBack by remember { mutableStateOf("") }
                readBack = "${StylishTheme.animation.durationShort}"
                Text(readBack)
            }
        }
        onNodeWithText("250").assertIsDisplayed()
    }

    @Test
    fun dynamicColorFallsBackToStaticSchemeOnJvm() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false, dynamicColor = true) {
                Text("ダイナミックカラー")
            }
        }
        onNodeWithText("ダイナミックカラー").assertIsDisplayed()
    }
}

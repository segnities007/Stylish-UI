package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLayoutDirection
import com.segnities007.stylishui.components.molecules.StylishPagination
import com.segnities007.stylishui.components.organisms.StylishQrCode
import com.segnities007.stylishui.components.organisms.StylishShortNavigationBar
import com.segnities007.stylishui.components.organisms.StylishShortNavigationBarItem
import com.segnities007.stylishui.components.organisms.StylishTransfer
import com.segnities007.stylishui.components.organisms.StylishTransferItem
import com.segnities007.stylishui.components.charts.StylishAreaChart
import com.segnities007.stylishui.components.charts.StylishAreaPoint
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Regression checks for accessibility contracts that are easy to break while
 * polishing visuals: RTL composition, collapsed navigation labels, and text
 * scaling. These are intentionally small smoke tests rather than screenshot
 * tests so they remain stable across host font rasterizers.
 */
@OptIn(ExperimentalTestApi::class)
class AccessibilityLayoutSmokeTest {

    @Test
    fun rtlAndTwoHundredPercentFontScaleKeepPrimaryControlsDiscoverable() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(1f, 2f),
            ) {
                StylishTheme(darkTheme = false, strings = com.segnities007.stylishui.theme.StylishJapaneseStrings) {
                    Column(Modifier.size(320.dp)) {
                        StylishShortNavigationBar {
                            StylishShortNavigationBarItem(
                                selected = true,
                                onClick = {},
                                icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                                label = { Text("ホーム") },
                            )
                            StylishShortNavigationBarItem(
                                selected = false,
                                onClick = {},
                                icon = { Icon(Icons.Default.Home, contentDescription = "設定") },
                                label = { Text("設定") },
                            )
                        }
                        StylishPagination(
                            page = 2,
                            onPageChange = {},
                            pageCount = 5,
                        )
                        StylishAreaChart(
                            points = listOf(
                                StylishAreaPoint("A", 10f),
                                StylishAreaPoint("B", 20f),
                            ),
                            contentDescription = "売上推移",
                        )
                    }
                }
            }
        }

        onNodeWithText("ホーム").assertIsDisplayed()
        onNodeWithContentDescription("前のページ").assertIsDisplayed()
        onNodeWithTag("stylish_area_chart").assertIsDisplayed()
        val selected = onNodeWithText("ホーム").fetchSemanticsNode().config
        assertTrue(selected[SemanticsProperties.Selected])
    }

    @Test
    fun navigationBarRemainsDiscoverableForCustomIconInRtl() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                StylishTheme(darkTheme = false) {
                    StylishShortNavigationBar {
                        StylishShortNavigationBarItem(
                            selected = true,
                            onClick = {},
                            icon = { Box(Modifier.size(24.dp).testTag("custom_nav_icon")) },
                            label = { Text("Dashboard") },
                        )
                    }
                }
            }
        }

        // The item merges descendants, so the custom icon slot is only
        // visible in the unmerged tree; the merged label stays searchable.
        onNodeWithTag("custom_nav_icon", useUnmergedTree = true).assertExists()
        onNodeWithText("Dashboard").assertIsDisplayed()
        val state = onNodeWithText("Dashboard").fetchSemanticsNode().config
        assertTrue(state[SemanticsProperties.Selected])
    }

    @Test
    fun paginationKeepsInteractionTargetAtLeast48DpWithLargeFontScale() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f, 2f)) {
                StylishTheme(darkTheme = false) {
                    StylishPagination(
                        page = 1,
                        onPageChange = {},
                        pageCount = 3,
                        modifier = Modifier.testTag("large_font_pagination"),
                    )
                }
            }
        }

        onNodeWithTag("large_font_pagination").assertIsDisplayed()
        onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun longLocalizedTransferLabelsKeepRoleStateAndFocusContract() = runComposeUiTest {
        val longLabel = "非常に長いファイル名を含む項目でも支援技術が意味を失わないことを確認するためのラベル"
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalDensity provides Density(1f, 2f),
            ) {
                StylishTheme(darkTheme = false, strings = com.segnities007.stylishui.theme.StylishJapaneseStrings) {
                    StylishTransfer(
                        available = listOf(StylishTransferItem("long", Unit, longLabel)),
                        selectedKeys = emptySet(),
                        onSelectedKeysChange = {},
                        title = "利用可能な項目の一覧（長い見出し）",
                        selectedTitle = "選択済みの項目の一覧（長い見出し）",
                    )
                }
            }
        }

        val item = onNodeWithContentDescription(longLabel)
        item.assertIsDisplayed()
        item.assertHasClickAction()
        val config = item.fetchSemanticsNode().config
        assertEquals(Role.Checkbox, config[SemanticsProperties.Role])
        assertEquals("利用可能", config[SemanticsProperties.StateDescription])
        assertTrue(config[SemanticsProperties.Selected] == false)
        assertNotNull(config[SemanticsActions.RequestFocus])
    }

    @Test
    fun qrCodePublishesDimensionsAsStateDescription() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false, strings = com.segnities007.stylishui.theme.StylishJapaneseStrings) {
                StylishQrCode(
                    matrix = listOf(
                        listOf(true, false, true),
                        listOf(false, true, false),
                    ),
                )
            }
        }

        val config = onNodeWithTag("stylish_qr_code").fetchSemanticsNode().config
        assertEquals("2 × 3", config[SemanticsProperties.StateDescription])
    }
}

package com.segnities007.stylishui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.models.StylishFloatingBottomBarItem
import com.segnities007.stylishui.components.patterns.StylishFloatingBottomBar
import com.segnities007.stylishui.components.patterns.StylishScaffold
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishLayerColor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class FloatingBottomBarTest {

    @Test
    fun itemBasedBarExposesSelectionAndRoutesClick() = runComposeUiTest {
        val clickedKeys = mutableListOf<String>()
        setContent {
            StylishTheme(darkTheme = false) {
                Box(Modifier.size(393.dp)) {
                    StylishFloatingBottomBar(
                        items = listOf(
                            StylishFloatingBottomBarItem("home", Icons.Default.Home, "ホーム"),
                            StylishFloatingBottomBarItem("secure", Icons.Default.Lock, "セキュア"),
                        ),
                        selectedKey = "home",
                        onItemClick = clickedKeys::add,
                    )
                }
            }
        }

        onNodeWithTag("stylish_floating_bottom_bar").assertIsDisplayed()
        val home = onNodeWithContentDescription("ホーム")
        assertEquals(Role.Tab, home.fetchSemanticsNode().config[SemanticsProperties.Role])
        assertEquals(true, home.fetchSemanticsNode().config[SemanticsProperties.Selected])

        home.performClick()
        assertEquals(listOf("home"), clickedKeys)
    }

    @Test
    fun defaultSurfaceRendersWithTranslucencyAndOutline() = runComposeUiTest {
        val backdrop = Color(0xFF2D4A66)
        var expectedFill = 0
        var expectedOutline = 0
        setContent {
            StylishTheme(darkTheme = false) {
                expectedFill = stylishLayerColor(level = 0.25f)
                    .copy(alpha = 0.9f)
                    .compositeOver(backdrop)
                    .toArgb()
                expectedOutline = MaterialTheme.colorScheme.outlineVariant.toArgb()
                Box(
                    modifier = Modifier
                        .size(393.dp)
                        .background(backdrop),
                ) {
                    StylishFloatingBottomBar(
                        actions = { Spacer(Modifier.size(200.dp, 48.dp)) },
                        shape = RectangleShape,
                        contentPadding = PaddingValues(0.dp),
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                    )
                }
            }
        }

        waitForIdle()
        val pixels = onNodeWithTag("stylish_floating_bottom_bar")
            .captureToImage()
            .toPixelMap()
        val interior = pixels[pixels.width / 2, pixels.height / 2].toArgb()
        val backdropArgb = backdrop.toArgb()
        val hasOutlinePixel = (0 until pixels.width).any { x ->
            val pixel = pixels[x, pixels.height / 2].toArgb()
            pixel != interior && pixel != backdropArgb
        }

        assertTrue(
            colorDistance(interior, expectedFill) <= 8,
            "Expected the default surface to composite at alpha 0.9: " +
                "actual=$interior expected=$expectedFill",
        )
        assertTrue(
            hasOutlinePixel,
            "Expected the default hairline outline ($expectedOutline) at the surface edge",
        )
    }

    @Test
    fun scaffoldPlacesFabAboveTheFloatingBottomBar() = runComposeUiTest {
        var fabClicks = 0
        var floatingBottomBarClicks = 0
        setContent {
            StylishTheme(darkTheme = false) {
                Box(Modifier.size(393.dp)) {
                    StylishScaffold(
                        modifier = Modifier.fillMaxSize(),
                        header = {},
                        floatingBottomCenter = {
                            StylishFloatingBottomBar(
                                items = listOf(
                                    StylishFloatingBottomBarItem("home", Icons.Default.Home, "ホーム"),
                                    StylishFloatingBottomBarItem("history", Icons.Default.History, "履歴"),
                                    StylishFloatingBottomBarItem(
                                        "notifications",
                                        Icons.Default.Notifications,
                                        "通知",
                                    ),
                                    StylishFloatingBottomBarItem("settings", Icons.Default.Settings, "設定"),
                                ),
                                onItemClick = { floatingBottomBarClicks++ },
                            )
                        },
                        floatingActionButton = {
                            StylishFab(
                                imageVector = Icons.Default.Add,
                                contentDescription = "追加",
                                onClick = { fabClicks++ },
                            )
                        },
                    ) { }
                }
            }
        }

        val bottomBarBounds = onNodeWithTag("stylish_floating_bottom_bar")
            .fetchSemanticsNode()
            .boundsInRoot
        val fabBounds = onNodeWithTag("stylish_fab")
            .fetchSemanticsNode()
            .boundsInRoot
        val overlapLeft = maxOf(bottomBarBounds.left, fabBounds.left)
        val overlapTop = maxOf(bottomBarBounds.top, fabBounds.top)
        val overlapRight = minOf(bottomBarBounds.right, fabBounds.right)
        val overlapBottom = minOf(bottomBarBounds.bottom, fabBounds.bottom)
        assertTrue(overlapLeft < overlapRight, "Expected horizontal overlap")
        assertTrue(overlapTop < overlapBottom, "Expected vertical overlap")

        onRoot().performTouchInput {
            click(Offset((overlapLeft + overlapRight) / 2f, (overlapTop + overlapBottom) / 2f))
        }

        assertEquals(1, fabClicks)
        assertEquals(0, floatingBottomBarClicks)
    }

    private fun colorDistance(left: Int, right: Int): Int =
        channelDistance(left shr 16, right shr 16) +
            channelDistance(left shr 8, right shr 8) +
            channelDistance(left, right)

    private fun channelDistance(left: Int, right: Int): Int =
        kotlin.math.abs((left and 0xFF) - (right and 0xFF))
}

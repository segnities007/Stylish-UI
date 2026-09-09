package com.segnities007.stylishui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishGradientFooterItem
import com.segnities007.stylishui.components.patterns.StylishGradientFooter
import com.segnities007.stylishui.foundation.LocalStylishReducedMotion
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class GradientFooterVisibilityTest {
    @Test fun lightControlsHideScrollingContent() = assertContentOccluded(false)
    @Test fun darkControlsHideScrollingContent() = assertContentOccluded(true)

    private fun assertContentOccluded(dark: Boolean) = runComposeUiTest {
        val behind = mutableStateOf(Color.Red)
        var background = Color.Unspecified
        setContent {
            CompositionLocalProvider(
                LocalDensity provides Density(1f),
                // Capture the settled visual state; motion itself is covered
                // by the shared floating-surface contract and its callers.
                LocalStylishReducedMotion provides true,
            ) {
                StylishTheme(darkTheme = dark) {
                    background = MaterialTheme.colorScheme.background
                    Box(Modifier.size(320.dp, 120.dp).background(behind.value)) {
                        StylishGradientFooter(
                            items = listOf(StylishGradientFooterItem("home", Icons.Default.Home, "Home")),
                            selectedKey = "home",
                            onItemClick = {},
                            modifier = Modifier.align(Alignment.BottomCenter),
                        )
                    }
                }
            }
        }
        val first = onRoot().captureToImage().toPixelMap()
        runOnIdle { behind.value = Color.Blue }
        val second = onRoot().captureToImage().toPixelMap()
        // Sample beside the centered destination, across icon and label heights.
        for (y in 76 until 120) {
            assertTrue(
                colorDistance(first[8, y].toArgb(), second[8, y].toArgb()) < 64,
                "Scrolling content is too visible at y=$y, dark=$dark",
            )
            assertTrue(
                colorDistance(background.toArgb(), second[8, y].toArgb()) < 64,
                "Footer is too far from the page background at y=$y, dark=$dark",
            )
        }
        // Keep a real transparent-to-opaque transition at the upper edge.
        assertNotEquals(first[8, 59], second[8, 59])
    }

    private fun colorDistance(first: Int, second: Int): Int {
        fun channel(color: Int, shift: Int): Int = (color shr shift) and 0xff
        return (0..16 step 8).sumOf { shift ->
            kotlin.math.abs(channel(first, shift) - channel(second, shift))
        }
    }
}

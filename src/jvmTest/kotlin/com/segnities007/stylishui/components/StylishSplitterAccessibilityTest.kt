package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.molecules.StylishSplitter
import com.segnities007.stylishui.components.molecules.StylishSplitterDirection
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Linux-renderable regression tests for the splitter's layout and accessibility contract. */
@OptIn(ExperimentalTestApi::class)
class StylishSplitterAccessibilityTest {

    @Test
    fun verticalDirectionDividesHeightAndExposesBoundedProgress() = runComposeUiTest {
        var changedRatio = Float.NaN
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSplitter(
                    direction = StylishSplitterDirection.Vertical,
                    ratio = 0.25f,
                    minRatio = 0.2f,
                    maxRatio = 0.8f,
                    modifier = Modifier.size(width = 200.dp, height = 400.dp),
                    onRatioChange = { changedRatio = it },
                    first = { Box(Modifier.fillMaxSize().testTag("split_top")) },
                    second = { Box(Modifier.fillMaxSize().testTag("split_bottom")) },
                )
            }
        }

        onNodeWithTag("split_top").assertIsDisplayed()
        onNodeWithTag("split_bottom").assertIsDisplayed()
        val topBounds = onNodeWithTag("split_top").fetchSemanticsNode().boundsInRoot
        assertTrue(
            topBounds.width > topBounds.height,
            "Vertical split must divide the root height, not its width",
        )

        val handle = onNodeWithContentDescription("Resize panels")
        val semantics = handle.fetchSemanticsNode().config
        assertNotNull(semantics[SemanticsActions.SetProgress])
        assertEquals(0.25f, semantics[SemanticsProperties.ProgressBarRangeInfo].current)

        handle.performSemanticsAction(SemanticsActions.SetProgress) { setProgress ->
            assertTrue(setProgress(2f))
        }
        assertEquals(0.8f, changedRatio)
    }
}

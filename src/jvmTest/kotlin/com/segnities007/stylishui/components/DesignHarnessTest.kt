package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishContentState
import com.segnities007.stylishui.components.models.StylishScreenDocument
import com.segnities007.stylishui.components.models.StylishScreenElement
import com.segnities007.stylishui.components.models.StylishScreenEvent
import com.segnities007.stylishui.components.atoms.StylishSurface
import com.segnities007.stylishui.components.atoms.StylishText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import com.segnities007.stylishui.components.patterns.StylishScreen
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class DesignHarnessTest {
    @Test fun rejectsInvalidContracts() {
        assertFailsWith<IllegalArgumentException> {
            StylishScreenDocument(listOf(StylishScreenElement.Action("save", "")))
        }
        assertFailsWith<IllegalArgumentException> {
            StylishScreenDocument(listOf(
                StylishScreenElement.Text("same", "A"),
                StylishScreenElement.Text("same", "B"),
            ))
        }
        assertFailsWith<IllegalArgumentException> {
            StylishScreenDocument(listOf(StylishScreenElement.Toggle("", "Sync", true)))
        }
    }

    @Test fun snapshotsCallerList() {
        val list = mutableListOf<StylishScreenElement>(StylishScreenElement.Text("a", "A"))
        val document = StylishScreenDocument(list)
        list.clear()
        assertEquals(1, document.elements.size)
    }

    @Test fun controlledEditingAndActivationInLightTheme() = exerciseScreen(false)
    @Test fun controlledEditingAndActivationInDarkTheme() = exerciseScreen(true)

    private fun exerciseScreen(dark: Boolean) = runComposeUiTest {
        val value = mutableStateOf("Alex")
        val events = mutableListOf<StylishScreenEvent>()
        setContent {
            StylishTheme(darkTheme = dark) {
                Box(Modifier.size(400.dp, 700.dp)) {
                    StylishScreen("Settings", StylishContentState.Content(StylishScreenDocument(listOf(
                        StylishScreenElement.Input("name", "Name", value.value),
                        StylishScreenElement.Toggle("sync", "Sync", false),
                        StylishScreenElement.Action("save", "Save"),
                        StylishScreenElement.Action("blocked", "Unavailable", enabled = false),
                    ))), onEvent = {
                        events += it
                        if (it is StylishScreenEvent.Edit) value.value = it.value
                    })
                }
            }
        }
        onNodeWithTag("screen_element:name").assertExists()
        onNodeWithText("Name").performScrollTo().performTextReplacement("Sam")
        onNodeWithText("Sam").assertIsDisplayed()
        onNodeWithText("Sync").performScrollTo().performClick()
        onNodeWithText("Save").performScrollTo().performClick()
        onNodeWithText("Unavailable").performScrollTo().assertIsNotEnabled()
        runOnIdle {
            assertEquals(listOf(
                StylishScreenEvent.Edit("name", "Sam"),
                StylishScreenEvent.Toggle("sync", true),
                StylishScreenEvent.Activate("save"),
            ), events)
        }
    }

    @Test fun errorRetryAndStateTransition() = runComposeUiTest {
        val state = mutableStateOf<StylishContentState<StylishScreenDocument>>(
            StylishContentState.Error("Offline", "Try again"),
        )
        val events = mutableListOf<StylishScreenEvent>()
        setContent {
            StylishTheme {
                Box(Modifier.size(400.dp, 700.dp)) {
                    StylishScreen("Library", state.value, onEvent = { events += it })
                }
            }
        }
        onNodeWithText("Offline").assertIsDisplayed()
        onNodeWithText("Try again").performClick()
        runOnIdle {
            assertEquals(listOf<StylishScreenEvent>(StylishScreenEvent.Retry), events)
            state.value = StylishContentState.Content(StylishScreenDocument(listOf(
                StylishScreenElement.Entry("book", "A book", actionId = "open"),
            )))
        }
        onNodeWithText("A book").performClick()
        runOnIdle { assertEquals(StylishScreenEvent.Activate("open"), events.last()) }
    }

    @Test fun textInheritsSurfaceContentColor() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSurface(color = Color.Blue, contentColor = Color.Red) {
                    StylishText("MMMM")
                }
            }
        }
        val pixels = onNodeWithText("MMMM").captureToImage().toPixelMap()
        assertTrue((0 until pixels.width).any { x ->
            (0 until pixels.height).any { y ->
                val color = pixels[x, y]
                color.red > 0.8f && color.green < 0.2f && color.blue < 0.2f
            }
        }, "Text must inherit the surface's content color")
    }

    @Test fun loadingActionBlocksActivation() = runComposeUiTest {
        val events = mutableListOf<StylishScreenEvent>()
        setContent {
            StylishTheme {
                Box(Modifier.size(400.dp, 700.dp)) {
                    StylishScreen("Save", StylishContentState.Content(StylishScreenDocument(listOf(
                        StylishScreenElement.Action("save", "Saving", loading = true),
                    ))), onEvent = { events += it })
                }
            }
        }
        onNodeWithTag("screen_element:save").performScrollTo().assertIsNotEnabled()
        runOnIdle { assertTrue(events.isEmpty()) }
    }
}

package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.molecules.StylishToastData
import com.segnities007.stylishui.components.molecules.StylishToastHost
import com.segnities007.stylishui.components.molecules.StylishToastHostState
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class StylishToastHostBehaviorTest {
    @Test
    fun newestToastIsPlacedAboveOlderToast() = runComposeUiTest {
        val hostState = StylishToastHostState()
        hostState.addToast(StylishToastData("古いToast"))
        hostState.addToast(StylishToastData("新しいToast"))

        setContent {
            StylishTheme(darkTheme = false) {
                ToastHostForTest(hostState)
            }
        }
        waitForIdle()

        val olderTop = onNodeWithText("古いToast").fetchSemanticsNode().boundsInRoot.top
        val newerTop = onNodeWithText("新しいToast").fetchSemanticsNode().boundsInRoot.top
        assertTrue(newerTop < olderTop)
    }
}

@Composable
private fun ToastHostForTest(hostState: StylishToastHostState) {
    StylishToastHost(
        hostState = hostState,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
    )
}

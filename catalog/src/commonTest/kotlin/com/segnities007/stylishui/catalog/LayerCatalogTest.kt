package com.segnities007.stylishui.catalog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class LayerCatalogTest {
    @Test
    fun playgroundRendersWithContinuousLayerSurfaces() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = true) {
                StylishPlayground(
                    darkTheme = true,
                    onToggleTheme = {},
                )
            }
        }

        onNodeWithText("Stylish UI").assertIsDisplayed()
        onNodeWithText("All").assertIsDisplayed()
        onNodeWithText("No.").assertIsDisplayed()
    }
}

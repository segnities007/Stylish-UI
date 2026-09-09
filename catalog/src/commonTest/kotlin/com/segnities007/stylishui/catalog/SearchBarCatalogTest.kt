package com.segnities007.stylishui.catalog

import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SearchBarCatalogTest {
    @Test
    fun searchBarCanExpandInsideStaggeredCatalogGrid() = runComposeUiTest {
        val searchBar = DemoRegistry.allDemos.single { it.name == "Search bar" }

        setContent {
            StylishTheme {
                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(1)) {
                    item { searchBar.preview() }
                }
            }
        }

        onNodeWithText("検索").performClick()
        waitForIdle()
        onNodeWithText("Stylish UI").assertExists()
    }
}

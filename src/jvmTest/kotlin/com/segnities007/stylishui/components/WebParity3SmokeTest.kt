package com.segnities007.stylishui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.molecules.StylishAvatarGroup
import com.segnities007.stylishui.components.molecules.StylishFormField
import com.segnities007.stylishui.components.molecules.StylishMasonry
import com.segnities007.stylishui.components.molecules.StylishSplitter
import com.segnities007.stylishui.components.organisms.StylishCommandItem
import com.segnities007.stylishui.components.organisms.StylishCommandPalette
import com.segnities007.stylishui.components.organisms.StylishHoverCard
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class WebParity3SmokeTest {

    @Test
    fun splitterRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishSplitter(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    first = { Text("左") },
                    second = { Text("右") },
                )
            }
        }
        onNodeWithText("左").assertIsDisplayed()
        onNodeWithText("右").assertIsDisplayed()
    }

    @Test
    fun masonryRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishMasonry(itemCount = 3, columns = 2) { index ->
                    Text("項目$index")
                }
            }
        }
        onNodeWithText("項目0").assertIsDisplayed()
        onNodeWithText("項目1").assertIsDisplayed()
    }

    @Test
    fun formFieldRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishFormField(
                    label = "車両名",
                    required = true,
                    supportingText = "ヘルプテキスト",
                ) {
                    Text("Stylish Car")
                }
            }
        }
        onNodeWithText("車両名").assertIsDisplayed()
        onNodeWithText("ヘルプテキスト").assertIsDisplayed()
    }

    @Test
    fun avatarGroupRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAvatarGroup(count = 3, avatar = { Text("U$it") })
            }
        }
        onNodeWithText("U0").assertIsDisplayed()
        onNodeWithText("U2").assertIsDisplayed()
    }

    @Test
    fun hoverCardRenders() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishHoverCard(trigger = { Text("トリガー") }) {
                    Text("カード内容")
                }
            }
        }
        onNodeWithText("トリガー").assertIsDisplayed()
    }

    @Test
    fun commandPaletteRendersItems() = runComposeUiTest {
        setContent {
            var query by remember { mutableStateOf("") }
            StylishTheme(darkTheme = false) {
                StylishCommandPalette(
                    expanded = true,
                    onDismiss = {},
                    query = query,
                    onQueryChange = { query = it },
                    items = listOf(
                        StylishCommandItem("ダッシュボードを開く", {}),
                        StylishCommandItem("設定を開く", {}),
                    ),
                )
            }
        }
        waitForIdle()
    }
}

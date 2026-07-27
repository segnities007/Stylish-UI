package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A Stylish UI wrapper around Material 3 [Scaffold] that provides the
 * full-screen structural skeleton for a page.
 *
 * Supplies slots for a top bar, bottom bar, and floating action button
 * around a scrollable content area. Use this as the outermost layout of
 * every screen; pass a [StylishHeader] as [topBar] and a
 * [StylishPageContent] as [content] for the standard page composition.
 *
 * @param topBar Composable rendered above the content area, typically a
 *   [StylishHeader]. Defaults to empty.
 * @param bottomBar Composable rendered below the content area, e.g. a
 *   navigation bar. Defaults to empty.
 * @param floatingActionButton Composable rendered floating above the
 *   content, typically a FAB. Defaults to empty.
 * @param containerColor Background color behind all content. Defaults to
 *   [MaterialTheme.colorScheme.background].
 * @param contentWindowInsets [WindowInsets] consumed by the scaffold.
 *   Defaults to [WindowInsets.navigationBars] so content avoids the
 *   system navigation bar.
 * @param content Main page content. Receives the [PaddingValues] that
 *   account for the top bar, bottom bar, and window insets; apply them
 *   via [Modifier.padding].
 *
 * @see StylishHeader
 * @see StylishPageContent
 */
@Composable
public fun StylishScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentWindowInsets: WindowInsets = WindowInsets.navigationBars,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = containerColor,
        contentWindowInsets = contentWindowInsets,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview(name = "StylishScaffold", showBackground = true, widthDp = 393)
@Composable
private fun StylishScaffoldPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishScaffold {
                Text("StylishScaffold with content")
            }
        }
    }
}

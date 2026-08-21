package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.molecules.StylishSnackbarHost
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A Stylish UI wrapper around Material 3 [Scaffold] that provides the
 * full-screen structural skeleton for a page.
 *
 * Supplies slots for a top bar, bottom bar, floating action button, and
 * snackbar host around a content area. Use this as the outermost layout
 * of every screen; pass a [StylishHeader] as [topBar] and a
 * [StylishPageContent] as [content] for the standard page composition.
 *
 * @param topBar Composable rendered above the content area, typically a
 *   [StylishHeader]. Defaults to empty.
 * @param bottomBar Composable rendered below the content area, e.g. a
 *   navigation bar. Defaults to empty.
 * @param floatingActionButton Composable rendered floating above the
 *   content, typically a FAB. Defaults to empty.
 * @param snackbarHost Composable rendered to host transient snackbar
 *   messages above the content, typically a [StylishSnackbarHost].
 *   Defaults to empty.
 * @param modifier Modifier applied to the scaffold root.
 * @param containerColor Background color behind all content. Defaults to
 *   [MaterialTheme.colorScheme.background].
 * @param contentColor Default content color propagated to child
 *   composables. Defaults to [contentColorFor] of [containerColor],
 *   matching the Material 3 [Scaffold] default.
 * @param floatingActionButtonPosition Position of [floatingActionButton]
 *   along the bottom edge, forwarded to the Material 3 [Scaffold].
 *   Defaults to [FabPosition.End].
 * @param contentWindowInsets [WindowInsets] consumed by the scaffold.
 *   Defaults to [WindowInsets.systemBars] (status + navigation bars) so
 *   content avoids both system bars, matching the Material 3 [Scaffold]
 *   default.
 * @param content Main page content. Receives the [PaddingValues] that
 *   account for the top bar, bottom bar, and window insets; apply them
 *   via [Modifier.padding].
 *
 * @see StylishHeader
 * @see StylishPageContent
 * @see StylishSnackbarHost
 */
@Composable
public fun StylishScaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.stylishTestTag("scaffold"),
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview(name = "StylishScaffold", showBackground = true, widthDp = 393)
@Composable
private fun StylishScaffoldPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishScaffold(
                snackbarHost = { StylishSnackbarHost(remember { SnackbarHostState() }) },
            ) {
                Text("StylishScaffold with content")
            }
        }
    }
}

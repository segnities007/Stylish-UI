package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.stylishTestTag
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Standard page scaffold: a **pinned header**, a content slot that receives
 * the measured [PaddingValues], and optional floating overlays.
 *
 * This is the structural answer to recurring "content hides under the
 * header / system bars" bugs: the header is rendered in [Scaffold]'s
 * `topBar` (so it never scrolls away), the navigation-bar inset is reserved
 * by the bottom bar, and [content] is invoked with the resulting
 * innerPadding. Screens must apply that padding to their root — never add
 * ad-hoc status/navigation bar paddings on top of it.
 *
 * The header sits on an opaque [containerColor] surface so scrolled content
 * slides cleanly beneath it.
 *
 * @param header Pinned top content (title, navigation, actions). Rendered
 *   above a [containerColor] background with the standard screen padding.
 * @param modifier Modifier applied to the underlying [Scaffold].
 * @param containerColor Background color of the whole page, including the
 *   header surface.
 * @param floatingBottomCenter Optional overlay anchored to the bottom center
 *   of the content area (e.g. a pager indicator pill). Drawn over the
 *   content; not part of the inset math.
 * @param floatingActionButton Optional FAB rendered by [Scaffold]'s own FAB
 *   slot at the bottom end, above the navigation-bar inset.
 * @param content Content laid out inside the safe area. Receives the
 *   [PaddingValues] that must be applied to the content root.
 */
@Composable
public fun StylishScreenScaffold(
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    floatingBottomCenter: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.stylishTestTag("screen_scaffold"),
        containerColor = containerColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Surface(color = containerColor) {
                Column {
                    Spacer(Modifier.statusBarsPadding())
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = StylishTheme.dimensions.screenPadding,
                                vertical = 8.dp,
                            ),
                    ) {
                        header()
                    }
                }
            }
        },
        bottomBar = { Spacer(Modifier.navigationBarsPadding()) },
        floatingActionButton = { floatingActionButton?.invoke() },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            content(innerPadding)
            floatingBottomCenter?.let { overlay ->
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp),
                ) {
                    overlay()
                }
            }
        }
    }
}


@Preview(name = "Stylish screen scaffold", showBackground = true, widthDp = 393)
@Composable
private fun StylishScreenScaffoldPreview() {
    StylishTheme(darkTheme = false) {
        StylishScreenScaffold(
            header = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
        ) { padding ->
            Box(Modifier.padding(padding)) {
                Text("コンテンツ", modifier = Modifier.padding(20.dp))
            }
        }
    }
}

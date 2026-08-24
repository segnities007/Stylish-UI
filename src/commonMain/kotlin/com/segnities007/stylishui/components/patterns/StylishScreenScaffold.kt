package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.stylishTestTag
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Overlay-style page scaffold. Content fills the entire screen and scrolls
 * **behind** the pinned header; nothing is pushed down by padding.
 *
 * The header floats above the content on a gradient scrim of
 * [containerColor]: near the status bar it is fully opaque so the title
 * stays legible, and it fades out downward so scrolled content remains
 * visible right up to the title line.
 *
 * This is the structural answer to recurring "content hides under the
 * header / system bars" bugs: there are no insets to wire through and no
 * per-screen padding math — every element simply floats over full-bleed
 * content.
 *
 * @param header Pinned floating top content (title, navigation, actions).
 * @param modifier Modifier applied to the root.
 * @param containerColor Page background, also used for the header scrim.
 * @param floatingBottomCenter Optional overlay anchored to the bottom center
 *   (e.g. a pager indicator pill), above the navigation-bar inset.
 * @param floatingActionButton Optional FAB anchored to the bottom end, above
 *   the navigation-bar inset.
 * @param content Full-bleed page content. It is NOT padded — layers float
 *   over it by design.
 */
@Composable
public fun StylishScreenScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    floatingBottomCenter: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            .stylishTestTag("screen_scaffold"),
    ) {
        content()

        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to containerColor,
                        1f to containerColor.copy(alpha = 0f),
                    ),
                ),
        ) {
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

        floatingActionButton?.let { fab ->
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp),
            ) {
                fab()
            }
        }

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

@Preview(name = "Stylish screen scaffold", showBackground = true, widthDp = 393, heightDp = 700)
@Composable
private fun StylishScreenScaffoldPreview() {
    StylishTheme(darkTheme = false) {
        StylishScreenScaffold(
            header = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
        ) {
            Surface(
                Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Text("フルブリードのコンテンツ", modifier = Modifier.padding(20.dp))
            }
        }
    }
}

package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Overlay-style page scaffold. Content fills the entire screen and scrolls
 * **behind** the pinned header; nothing is pushed down by padding.
 *
 * The header floats above the content on a gradient scrim of
 * [containerColor]: near the status bar it is fully opaque so the title
 * stays legible, fading out downward so scrolled content remains visible
 * right up to the title line.
 *
 * Unlike inset-based scaffolds, [content] receives the **measured header
 * height** ([headerHeight]) instead of padding that clips the content area.
 * Use it as the initial top spacing of your scrollable (e.g. a LazyColumn
 * contentPadding.top) so the first item clears the header at rest, while
 * scrolled items keep flowing behind it. Pass nothing if you truly want
 * full-bleed from pixel zero.
 *
 * @param header Pinned floating top content (title, navigation, actions).
 *   The scaffold owns the status-bar inset: it clears and consumes it, so
 *   header content must not add its own top status-bar padding.
 * @param modifier Modifier applied to the root.
 * @param containerColor Page background, also used for the header scrim.
 * @param floatingBottomCenter Optional overlay anchored to the bottom center
 *   (e.g. a pager indicator pill), above the navigation-bar inset.
 * @param floatingActionButton Optional FAB anchored to the bottom end, above
 *   the navigation-bar inset.
 * @param content Full-bleed page content. Receives the measured header
 *   height (including the status bar) as its initial top clearance.
 */
@Composable
public fun StylishScreenScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    floatingBottomCenter: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (headerHeight: Dp) -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            .stylishTestTag("screen_scaffold"),
    ) {
        SubcomposeLayout { constraints ->
            val loose = Constraints(maxWidth = constraints.maxWidth)
            val headerPlaceables = subcompose("header") {
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
                    // The scaffold OWNS the status-bar inset: it clears the
                    // bar here (padding) and then marks the inset consumed,
                    // so self-insetting headers (StylishHeader et al.)
                    // resolve zero remaining inset instead of double-padding.
                    Spacer(Modifier.statusBarsPadding())
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = StylishTheme.dimensions.screenPadding,
                                vertical = 8.dp,
                            )
                            .consumeWindowInsets(WindowInsets.statusBars),
                    ) {
                        header()
                    }
                }
            }.map { it.measure(loose) }

            val headerHeight = with(this) { headerPlaceables.maxOf { it.height }.toDp() }

            val contentPlaceables = subcompose("content") {
                content(headerHeight)
            }.map { it.measure(constraints.copy(minHeight = 0)) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                contentPlaceables.forEach { it.place(0, 0) }
                headerPlaceables.forEach { it.place(0, 0) }
            }
        }

        // Bottom scrim: scrolled content fades into the container color
        // across the navigation-bar zone instead of colliding with the
        // gesture pill / cutout.
        Spacer(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    Brush.verticalGradient(
                        0f to containerColor.copy(alpha = 0f),
                        1f to containerColor,
                    ),
                ),
        )

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
        ) { headerHeight ->
            Surface(
                Modifier.padding(top = headerHeight).padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Text("フルブリードのコンテンツ", modifier = Modifier.padding(20.dp))
            }
        }
    }
}

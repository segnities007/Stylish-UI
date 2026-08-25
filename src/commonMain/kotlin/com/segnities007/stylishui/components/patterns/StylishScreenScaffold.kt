package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
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
 * @param containerColor Page background.
 * @param statusBarScrimColor Optional scrim across the status-bar zone.
 *   Defaults to transparent — system bar icon contrast is expected to be
 *   handled by the host activity (e.g. enableEdgeToEdge styles synced with
 *   the app theme).
 * @param hideOnScroll When `true`, the header slides away on downward
 *   scrolls of ANY nested scrollable inside the content and returns on
 *   upward scrolls.
 * @param scrollHideState State backing [hideOnScroll]; hoist to observe or
 *   reset it.
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
    statusBarScrimColor: Color = Color.Transparent,
    hideOnScroll: Boolean = false,
    scrollHideState: StylishScrollHideState = rememberStylishScrollHideState(),
    floatingBottomCenter: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (headerHeight: Dp) -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            .nestedScroll(scrollHideState.connection)
            .stylishTestTag("screen_scaffold"),
    ) {
        // Largest header height ever measured. Kept stable while the header
        // slides away (AnimatedVisibility shrinks it to zero) so the
        // content's top clearance does not collapse mid-animation.
        var headerHeightPx by androidx.compose.runtime.mutableIntStateOf(0)

        SubcomposeLayout { constraints ->
            val loose = Constraints(maxWidth = constraints.maxWidth)
            val headerPlaceables = subcompose("header") {
                StylishScrollHideVisibility(
                    visible = !hideOnScroll || scrollHideState.visible,
                    direction = StylishSlideDirection.UP,
                ) {
                Box(
                    Modifier.onSizeChanged { size ->
                        if (size.height > headerHeightPx) headerHeightPx = size.height
                    }
                ) {
                Column {
                    // Subtle scrim: keeps the status-bar zone readable
                    // without hiding the content behind it.
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(statusBarScrimColor)
                            .statusBarsPadding(),
                    )

                    // The scaffold owns the status-bar inset: it cleared the
                    // bar above and marks the inset consumed, so self-
                    // insetting headers (StylishHeader et al.) resolve zero
                    // remaining inset instead of double-padding.
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
                }
                }
            }.map { it.measure(loose) }

            val headerHeight = with(this) { headerHeightPx.toDp() }

            val contentPlaceables = subcompose("content") {
                content(headerHeight)
            }.map { it.measure(constraints.copy(minHeight = 0)) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                contentPlaceables.forEach { it.place(0, 0) }
                headerPlaceables.forEach { it.place(0, 0) }
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

package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.foundation.layout.Arrangement
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.segnities007.stylishui.components.atoms.StylishFloatingVisibility
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Modern overlay-style page scaffold (the canonical Stylish scaffold). Content fills the entire screen and scrolls
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
 * @param hideOnScroll When `true`, the header, bottom-center overlay, and
 *   FAB slide away on downward scrolls of ANY nested scrollable inside the
 *   content and return on upward scrolls. All three use the shared Stylish
 *   floating motion; only their direction differs.
 * @param scrollHideState State backing [hideOnScroll]; hoist to observe or
 *   reset it.
 * @param floatingBottomCenter Optional overlay anchored to the bottom center
 *   (e.g. a pager indicator pill), above the navigation-bar inset.
 * @param floatingActionButton Optional FAB anchored to the bottom end, above
 *   the navigation-bar inset.
 * @param bottomContentPadding Additional bottom clearance required by custom
 *   floating content. The navigation-bar inset is included automatically.
 *   This value is delivered to scrollable descendants through
 *   [LocalStylishScaffoldContentPadding] and does not change the scaffold's
 *   measured content size.
 * @param floatingActionButtonContentPadding Extra clearance reserved for the
 *   standard [floatingActionButton] slot. Defaults to
 *   [StylishScaffoldDefaults.floatingActionButtonContentPadding]. Set it to
 *   zero when the slot is retained for an exit animation but the app-level
 *   [bottomContentPadding] already owns the reservation.
 * @param floatingContent Full-screen overlay slot for multiple or custom
 *   floating surfaces. Children may use [BoxScope.align]. When its content
 *   covers the bottom of the screen, also provide [bottomContentPadding].
 * @param content Full-bleed page content. Receives the measured header
 *   height (including the status bar) as its initial top clearance.
 */
@Composable
public fun StylishScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    statusBarScrimColor: Color = Color.Transparent,
    hideOnScroll: Boolean = false,
    scrollHideState: StylishScrollHideState = rememberStylishScrollHideState(),
    floatingBottomCenter: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    bottomContentPadding: Dp = 0.dp,
    floatingActionButtonContentPadding: Dp = StylishScaffoldDefaults.floatingActionButtonContentPadding,
    floatingContent: @Composable BoxScope.() -> Unit = {},
    content: @Composable (headerHeight: Dp) -> Unit,
) {
    val parentContentPadding = LocalStylishScaffoldContentPadding.current
    val navigationBarPadding = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val defaultFloatingActionButtonPadding = if (floatingActionButton != null) {
        floatingActionButtonContentPadding
    } else {
        0.dp
    }
    val resolvedBottomPadding = maxOf(
        parentContentPadding.calculateBottomPadding(),
        navigationBarPadding + maxOf(bottomContentPadding, defaultFloatingActionButtonPadding),
    )
    val scaffoldContentPadding = PaddingValues(bottom = resolvedBottomPadding)

    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            // A scaffold that does not hide floating content must not join
            // the nested-scroll chain. Keeping the connection attached in
            // that mode creates an unnecessary second observer (especially
            // when an app shell wraps a screen scaffold) and can start
            // scroll-hide work after the gesture has already ended.
            .then(
                if (hideOnScroll) {
                    Modifier.nestedScroll(scrollHideState.connection)
                } else {
                    Modifier
                },
            )
            .stylishTestTag("screen_scaffold"),
    ) {
        // Largest header height ever measured. Kept stable while the header
        // slides away (AnimatedVisibility shrinks it to zero) so the
        // content's top clearance does not collapse mid-animation.
        var headerHeightPx by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableIntStateOf(0) }

        SubcomposeLayout { constraints ->
            val loose = Constraints(maxWidth = constraints.maxWidth)
            val headerPlaceables = subcompose("header") {
                val headerContent: @Composable () -> Unit = {
                    Box(
                        Modifier.onSizeChanged { size ->
                            if (size.height > headerHeightPx) headerHeightPx = size.height
                        },
                    ) {
                        Column {
                            // Subtle scrim: keeps the status-bar zone readable
                            // without hiding the content behind it.
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .background(statusBarScrimColor),
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
                if (hideOnScroll) {
                    StylishScrollHideVisibility(
                        visible = scrollHideState.visible,
                        direction = StylishSlideDirection.UP,
                        content = headerContent,
                    )
                } else {
                    headerContent()
                }
            }.map { it.measure(loose) }

            // Use the current measurement on the first frame as well as the
            // remembered maximum while the exit transition is shrinking the
            // header. This prevents content from initially rendering under it.
            val measuredHeaderHeight = headerPlaceables.maxOfOrNull { it.height } ?: 0
            val headerHeight = with(this) {
                maxOf(headerHeightPx, measuredHeaderHeight).toDp()
            }

            val contentPlaceables = subcompose("content") {
                CompositionLocalProvider(
                    LocalContentColor provides contentColorFor(containerColor),
                    LocalStylishScaffoldContentPadding provides scaffoldContentPadding,
                ) {
                    content(headerHeight)
                }
            }.map { it.measure(constraints.copy(minHeight = 0)) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                contentPlaceables.forEach { it.place(0, 0) }
                headerPlaceables.forEach { it.place(0, 0) }
            }
        }

        floatingBottomCenter?.let { overlay ->
            val floatingModifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
            if (hideOnScroll) {
                StylishFloatingVisibility(
                    visible = scrollHideState.visible,
                    direction = StylishFloatingSlideDirection.Down,
                    modifier = floatingModifier,
                    content = overlay,
                )
            } else {
                Box(floatingModifier) {
                    overlay()
                }
            }
        }

        // Keep the floating bottom bar below the FAB, matching the original
        // MyVehicles overlay order when the two surfaces overlap.
        floatingActionButton?.let { fab ->
            val floatingModifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
            if (hideOnScroll) {
                StylishFloatingVisibility(
                    visible = scrollHideState.visible,
                    direction = StylishFloatingSlideDirection.Down,
                    modifier = floatingModifier,
                    content = fab,
                )
            } else {
                Box(floatingModifier) {
                    fab()
                }
            }
        }

        // A single overlay layer can host any number of app- or feature-level
        // floating surfaces. The scaffold owns the layer; callers only choose
        // each surface's arrangement with BoxScope.align.
        floatingContent()
    }
}

@Preview(name = "Stylish screen scaffold", showBackground = true, widthDp = 393, heightDp = 700)
@Composable
private fun StylishScaffoldPreview() {
    StylishTheme(darkTheme = false) {
        StylishScaffold(
            header = {
                StylishHeader(
                    title = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
                    navigation = {
                        StylishIconButton(
                            Icons.Filled.ArrowBack,
                            "戻る",
                            {},
                        )
                    },
                    actions = {
                        StylishIconButton(
                            Icons.Filled.Settings,
                            "設定",
                            {},
                        )
                    },
                )
            },
        ) { headerHeight ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = headerHeight + 8.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            ) {
                StylishConnectedCard(
                    title = "カード 1",
                    supportingText = "フルブリードのコンテンツ。スクロールするとヘッダーの後ろを流れます。",
                )
                StylishConnectedCard(title = "カード 2", supportingText = "本文テキスト")
                StylishConnectedCard(title = "カード 3", supportingText = "本文テキスト")
            }
        }
    }
}

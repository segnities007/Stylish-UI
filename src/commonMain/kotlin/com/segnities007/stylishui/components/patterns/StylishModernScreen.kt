package com.segnities007.stylishui.components.patterns

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Scroll-direction state for [StylishModernScreen]. [visible] flips as soon
 * as the content list scrolls against the current direction (1px consumed
 * is enough) and auto-shows when the list returns to the top.
 */
@Stable
public class StylishScrollHideState internal constructor() {
    public var visible: Boolean by mutableStateOf(true)
        internal set
    private var accumulated = 0f

    internal val connection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            // Count only scroll the list actually consumed: overscroll
            // attempts at the edges must not toggle visibility.
            if (consumed.y != 0f) {
                accumulated += consumed.y
                when {
                    !visible && accumulated > 1f -> {
                        visible = true
                        accumulated = 0f
                    }
                    visible && accumulated < -1f -> {
                        visible = false
                        accumulated = 0f
                    }
                }
            }
            return Offset.Zero
        }
    }

    /** Forces the floating layer back to visible. */
    public fun show(): Unit {
        visible = true
        accumulated = 0f
    }
}

/** Remembers a [StylishScrollHideState]. */
@Composable
public fun rememberStylishScrollHideState(): StylishScrollHideState =
    remember { StylishScrollHideState() }

/** The side a floating layer exits toward when hidden. */
public enum class StylishSlideDirection { DOWN, UP }

/**
 * Shows/hides floating content with the ModernScreen fade + half-slide
 * animation (200ms). [direction] is the exit side: headers exit [UP],
 * FABs and bottom indicators exit [DOWN].
 */
@Composable
public fun StylishScrollHideVisibility(
    visible: Boolean,
    direction: StylishSlideDirection,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(tween(200)) +
            slideInVertically(tween(200)) { half ->
                if (direction == StylishSlideDirection.UP) -half else half
            },
        exit = fadeOut(tween(200)) +
            slideOutVertically(tween(200)) { half ->
                if (direction == StylishSlideDirection.UP) -half else half
            },
    ) {
        content()
    }
}

/**
 * The modern full-screen page: a floating pinned header, a full-bleed lazy
 * content list, and floating overlays — with the scroll behavior built in.
 *
 * iOS-style scroll behavior:
 * - scrolling down slides the header and floating layers out 1:1 with the
 *   finger,
 * - any upward scroll brings them back,
 * - the floating layer is always visible while the list is at the top
 *   (including rubber-band bounce), and after navigation or pager page
 *   switches.
 * A hidden state can therefore never strand the user.
 *
 * The header height is measured and applied as the list's initial top
 * content padding, so content starts clear of the header and scrolls
 * behind it. The status-bar zone is tinted with [statusBarScrimColor].
 *
 * @param header Floating pinned top content. The scaffold owns the
 *   status-bar inset; header content must not add its own.
 * @param modifier Modifier applied to the root.
 * @param containerColor Page background.
 * @param statusBarScrimColor Tint of the status-bar zone. Defaults to
 *   transparent — system icon contrast is the host activity's job.
 * @param hideOnScroll When `true` (default), header and floating layers
 *   slide out on downward scrolls and return on upward scrolls.
 * @param scrollHideState Optional hoisted state; share one instance across
 *   screens to coordinate them, or read [StylishScrollHideState.visible]
 *   for extra floating elements of your own.
 * @param listState Hoisted [LazyListState] for the content list.
 * @param horizontalContentPadding Horizontal padding of the content list.
 * @param bottomContentPadding Extra bottom padding after the last item
 *   (the navigation-bar inset is reserved automatically).
 * @param floatingBottomCenter Optional overlay anchored bottom-center
 *   (e.g. a pager indicator pill).
 * @param floatingActionButton Optional FAB anchored bottom-end.
 * @param content Lazy list content.
 */
@Composable
public fun StylishModernScreen(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    statusBarScrimColor: Color = Color.Transparent,
    hideOnScroll: Boolean = true,
    scrollHideState: StylishScrollHideState = rememberStylishScrollHideState(),
    listState: LazyListState = rememberLazyListState(),
    horizontalContentPadding: Dp = StylishTheme.dimensions.screenPadding,
    bottomContentPadding: Dp = 24.dp,
    floatingBottomCenter: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    // iOS-style rule: the floating layer is ALWAYS visible while the list
    // sits at the top (contentOffset <= 0). This also restores it after
    // navigation/pager switches, where a hidden shared state could
    // otherwise never receive an upward consumed scroll again.
    var atTop by androidx.compose.runtime.mutableStateOf(true)
    LaunchedEffect(listState, scrollHideState) {
        androidx.compose.runtime.snapshotFlow {
            listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset == 0
        }.collect { top ->
            atTop = top
            if (top) scrollHideState.show()
        }
    }

    // Automatic scroll edge effect (iOS scrollEdgeEffectStyle(.automatic)):
    // the scrim over the header zone is transparent at the top edge and
    // fades in as content scrolls beneath the floating layers.
    val edgeProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (atTop) 0f else 1f,
        animationSpec = tween(200),
        label = "scrollEdge",
    )

    val headerVisible = !hideOnScroll || scrollHideState.visible
    val floatingVisible = !hideOnScroll || scrollHideState.visible
    val headerHeightPx = remember { mutableIntStateOf(0) }
    val density = androidx.compose.ui.platform.LocalDensity.current

    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            .nestedScroll(scrollHideState.connection)
            .stylishTestTag("modern_screen"),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = horizontalContentPadding,
                end = horizontalContentPadding,
                top = with(density) { headerHeightPx.intValue.toDp() } + 8.dp,
                bottom = bottomContentPadding,
            ),
            content = content,
        )

        // Status-bar tint strip (kept under the header layer).
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(
                    statusBarScrimColor.copy(
                        alpha = statusBarScrimColor.alpha * edgeProgress,
                    ),
                )
                .statusBarsPadding(),
        )

        // Floating pinned header. onSizeChanged keeps the LARGEST measured
        // height so the content's clearance never collapses while the
        // header slides away.
        StylishScrollHideVisibility(
            visible = headerVisible,
            direction = StylishSlideDirection.UP,
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size ->
                        if (size.height > headerHeightPx.intValue) {
                            headerHeightPx.intValue = size.height
                        }
                    },
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
        }

        floatingBottomCenter?.let { overlay ->
            StylishScrollHideVisibility(
                visible = floatingVisible,
                direction = StylishSlideDirection.DOWN,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
            ) {
                overlay()
            }
        }

        floatingActionButton?.let { fab ->
            StylishScrollHideVisibility(
                visible = floatingVisible,
                direction = StylishSlideDirection.DOWN,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp),
            ) {
                fab()
            }
        }
    }
}

@Composable
private fun StylishModernScreenPreview() {
    MaterialTheme {
        StylishModernScreen(
            header = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
            floatingBottomCenter = { Text("indicator") },
        ) {
            items(20) { index ->
                Text(
                    "項目 $index",
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Preview(name = "Stylish scroll hide visibility", showBackground = true, widthDp = 200)
@Composable
private fun StylishScrollHideVisibilityPreview() {
    StylishTheme(darkTheme = false) {
        StylishScrollHideVisibility(
            visible = true,
            direction = StylishSlideDirection.DOWN,
        ) {
            Text("floating content", modifier = Modifier.padding(12.dp))
        }
    }
}

@Preview(name = "Remember stylish scroll hide state", showBackground = true, widthDp = 200)
@Composable
private fun RememberStylishScrollHideStatePreview() {
    StylishTheme(darkTheme = false) {
        val state = rememberStylishScrollHideState()
        Text("visible = ${state.visible}", modifier = Modifier.padding(12.dp))
    }
}

package com.segnities007.stylishui.components.patterns

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.drop
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Scroll-direction state for [StylishModernScreen]. [progress] tracks the
 * finger 1:1 while scrolling (0 = shown, 1 = hidden) and settles to the
 * nearest edge when the gesture or fling ends.
 */
@Stable
public class StylishScrollHideState internal constructor(
    private val scope: CoroutineScope,
) {
    private val animatable = Animatable(0f)

    /** 0f = fully shown, 1f = fully hidden. */
    public val progress: Float get() = animatable.value

    public val visible: Boolean get() = progress < 0.5f

    internal val connection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            // Count only scroll the list actually consumed: overscroll
            // attempts at the edges must not move the floating layer.
            // 方向を検知した時点で完全にスライドイン/アウトする
            // (指の移動量に比例させない)。
            if (consumed.y != 0f) {
                val target = if (consumed.y < 0f) 1f else 0f
                if (animatable.targetValue != target) {
                    scope.launch { animatable.animateTo(target, tween(200)) }
                }
            }
            return Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            animatable.animateTo(
                if (animatable.value > 0.5f) 1f else 0f,
                tween(200),
            )
            return Velocity.Zero
        }
    }

    /** Forces the floating layer back to visible (e.g. on page switch). */
    public fun show() {
        scope.launch { animatable.animateTo(0f, tween(200)) }
    }
}

/** Remembers a [StylishScrollHideState]. */
@Composable
public fun rememberStylishScrollHideState(): StylishScrollHideState {
    val scope = rememberCoroutineScope()
    return remember { StylishScrollHideState(scope) }
}

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
    androidx.compose.animation.AnimatedVisibility(
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
 * The header height is measured synchronously (SubcomposeLayout) and
 * applied as the list's initial top content padding, so content starts
 * clear of the header and scrolls behind it.
 *
 * Scroll behavior: detecting a downward scroll fully slides the header and
 * floating layers out (200 ms); any upward scroll slides them fully back in.
 * The floating layer is always visible while the list is at the top
 * (including rubber-band bounce) and after pager page switches.
 *
 * The status-bar zone is tinted with [statusBarScrimColor] while content
 * scrolls beneath the floating layers (automatic scroll edge effect).
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
    // Largest header height ever measured. rememberSaveable so a pager-
    // disposed and recreated page starts with the last known clearance.
    val headerHeightPx = androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableIntStateOf(0) }
    val density = androidx.compose.ui.platform.LocalDensity.current

    // 0f = shown, 1f = hidden. Tracks the finger 1:1 via nested scroll and
    // settles to the nearest edge when the gesture/fling ends.

    // iOS-style guarantee: always visible while the list is at the top
    // (also restores visibility after pager page switches).
    var atTop by androidx.compose.runtime.mutableStateOf(true)
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset == 0
        }.collect { top ->
            atTop = top
            if (top) scrollHideState.show()
        }
    }

    // Automatic scroll edge effect: transparent at the top edge, fades in
    // as content scrolls beneath the floating layers.
    val edgeProgress by animateFloatAsState(
        targetValue = if (atTop) 0f else 1f,
        animationSpec = tween(200),
        label = "scrollEdge",
    )

    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            .nestedScroll(scrollHideState.connection)
            .stylishTestTag("modern_screen"),
    ) {
        SubcomposeLayout { constraints ->
            val loose = Constraints(maxWidth = constraints.maxWidth)

            // Header: slides UP as progress grows. Measured synchronously so
            // the content's top clearance is correct from the first frame.
            val headerPlaceables = subcompose("header") {
                // 自身の高さ分スライドして完全に画面外へ出る
                val slideOffset = -(scrollHideState.progress * headerHeightPx.intValue)
                Box(Modifier.offset(y = with(this@SubcomposeLayout) { slideOffset.toDp() })) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .onSizeChanged { size ->
                                if (size.height > headerHeightPx.intValue) {
                                    headerHeightPx.intValue = size.height
                                }
                            },
                    ) {
                        // The scaffold owns the status-bar clearance
                        // (B-style inset ownership).
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
            }.map { it.measure(loose) }

            val headerHeight = headerPlaceables.maxOf { it.height }
            val headerHeightDp = with(this@SubcomposeLayout) { headerHeight.toDp() }

            val contentPlaceables = subcompose("content") {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    // Measured header height keeps the first card clear at
                    // rest; scrolled items flow behind the floating header.
                    contentPadding = PaddingValues(
                        start = horizontalContentPadding,
                        end = horizontalContentPadding,
                        top = headerHeightDp + 8.dp,
                        bottom = bottomContentPadding,
                    ),
                    content = content,
                )
            }.map { it.measure(constraints) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                contentPlaceables.forEach { it.place(0, 0) }
                headerPlaceables.forEach { it.place(0, 0) }
            }
        }

        // Status-bar tint strip: appears only while scrolled (automatic
        // scroll edge effect).
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

        floatingBottomCenter?.let { overlay ->
            val slideOffset by animateDpAsState(
                targetValue = with(density) { 140.dp * scrollHideState.progress },
                animationSpec = tween(220),
                label = "bottomSlide",
            )
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp)
                    .offset(y = slideOffset),
            ) {
                overlay()
            }
        }

        floatingActionButton?.let { fab ->
            val fabSlide by animateDpAsState(
                targetValue = with(density) { 160.dp * scrollHideState.progress },
                animationSpec = tween(220),
                label = "fabSlide",
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .offset(y = fabSlide),
            ) {
                fab()
            }
        }
    }
}

@Preview(name = "Stylish modern screen", showBackground = true, widthDp = 393, heightDp = 700)
@Composable
private fun StylishModernScreenPreview() {
    MaterialTheme {
        StylishModernScreen(
            header = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
            floatingBottomCenter = { Text("indicator") },
        ) {
            items(20) { index ->
                Text("項目 $index", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
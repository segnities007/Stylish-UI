package com.segnities007.stylishui.components.patterns

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
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
import com.segnities007.stylishui.components.atoms.StylishFloatingVisibility
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Scroll-direction state for [StylishModernScreen]. [progress] reports the
 * current animated position between fully shown (0) and fully hidden (1).
 * The target changes as soon as consumed scroll or fling direction is
 * detected, and the position then settles to that edge.
 */
@Stable
public class StylishScrollHideState internal constructor(
    private val scope: CoroutineScope,
    private val motionSpec: AnimationSpec<Float>,
) {
    private val animatable = Animatable(0f)
    private var targetVisible by mutableStateOf(true)

    /** 0f = fully shown, 1f = fully hidden. */
    public val progress: Float get() = animatable.value

    /**
     * Whether the floating layer should be shown. This changes immediately
     * on scroll direction; [progress] is retained as the animated position
     * for callers that need continuous progress.
     */
    public val visible: Boolean get() = targetVisible

    private fun requestVisibility(visible: Boolean) {
        if (targetVisible == visible && animatable.targetValue == if (visible) 0f else 1f) {
            return
        }
        targetVisible = visible
        scope.launch {
            animatable.animateTo(if (visible) 0f else 1f, motionSpec)
        }
    }

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
                requestVisibility(visible = consumed.y >= 0f)
            } else if (available.y > 0f && animatable.targetValue != 0f) {
                // A list at its top can report the downward drag as
                // unconsumed. Use that edge gesture to reveal the layer.
                requestVisibility(visible = true)
            }
            return Offset.Zero
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            // A fling starts when the finger is released. Resolve the
            // floating layer at that point, before the child consumes the
            // fling, so the transition does not wait for the fling to finish
            // and then appear to start late at the edge of the list.
            if (available.y != 0f) {
                requestVisibility(visible = available.y >= 0f)
            }
            return Velocity.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            // The settle animation is started from onPreFling. Waiting here
            // would defer the animation until after the child scroll/fling
            // has completed, which is perceptibly late when the list reaches
            // an edge before the finger is released.
            return Velocity.Zero
        }
    }

    /** Forces the floating layer back to visible (e.g. on page switch). */
    public fun show() {
        requestVisibility(visible = true)
    }
}

/** Remembers a [StylishScrollHideState]. */
@Composable
public fun rememberStylishScrollHideState(): StylishScrollHideState {
    val scope = rememberCoroutineScope()
    val animation = StylishTheme.animation
    val reducedMotion = isStylishReducedMotionEnabled()
    return remember(animation.durationMedium, animation.defaultEasing, reducedMotion) {
        StylishScrollHideState(
            scope = scope,
            motionSpec = if (reducedMotion) {
                snap()
            } else {
                tween(
                    durationMillis = animation.durationMedium,
                    easing = animation.defaultEasing,
                )
            },
        )
    }
}

/** The side a floating layer exits toward when hidden. */
public enum class StylishSlideDirection { DOWN, UP }

/**
 * Shows/hides floating content with the standard Stylish fade + full-slide
 * animation. [direction] is the exit side: headers exit [UP],
 * FABs and bottom indicators exit [DOWN].
 */
@Composable
public fun StylishScrollHideVisibility(
    visible: Boolean,
    direction: StylishSlideDirection,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    StylishFloatingVisibility(
        visible = visible,
        modifier = modifier,
        direction = direction.toFloatingDirection(),
        content = content,
    )
}

private fun StylishSlideDirection.toFloatingDirection(): StylishFloatingSlideDirection =
    when (this) {
        StylishSlideDirection.DOWN -> StylishFloatingSlideDirection.Down
        StylishSlideDirection.UP -> StylishFloatingSlideDirection.Up
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
 * floating layers out using the shared floating motion timing; any upward
 * scroll slides them fully back in.
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
        animationSpec = if (isStylishReducedMotionEnabled()) snap() else tween(200),
        label = "scrollEdge",
    )
    val navigationBarPadding = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    Box(
        modifier
            .fillMaxSize()
            .background(containerColor)
            .then(if (hideOnScroll) Modifier.nestedScroll(scrollHideState.connection) else Modifier)
            .stylishTestTag("modern_screen"),
    ) {
        SubcomposeLayout { constraints ->
            val loose = Constraints(maxWidth = constraints.maxWidth)

            // Header: uses the same shared floating transition as every other
            // floating surface. Its measured height remains stable while the
            // visibility exit transition is running.
            val headerPlaceables = subcompose("header") {
                StylishFloatingVisibility(
                    visible = !hideOnScroll || scrollHideState.visible,
                    direction = StylishFloatingSlideDirection.Up,
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

            val headerHeight = maxOf(
                headerHeightPx.intValue,
                headerPlaceables.maxOfOrNull { it.height } ?: 0,
            )
            val headerHeightDp = with(this@SubcomposeLayout) { headerHeight.toDp() }

            val contentPlaceables = subcompose("content") {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    // Measured header height keeps the first card clear at
                    // rest; scrolled items flow behind the floating header.
                    contentPadding = stylishScaffoldContentPadding(
                        PaddingValues(
                            start = horizontalContentPadding,
                            end = horizontalContentPadding,
                            top = headerHeightDp + 8.dp,
                            bottom = bottomContentPadding + navigationBarPadding,
                        ),
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
                .statusBarsPadding()
                .background(
                    statusBarScrimColor.copy(
                        alpha = statusBarScrimColor.alpha * edgeProgress,
                    ),
                )
        )

        floatingBottomCenter?.let { overlay ->
            StylishFloatingVisibility(
                visible = !hideOnScroll || scrollHideState.visible,
                direction = StylishFloatingSlideDirection.Down,
                Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
            ) { overlay() }
        }

        floatingActionButton?.let { fab ->
            StylishFloatingVisibility(
                visible = !hideOnScroll || scrollHideState.visible,
                direction = StylishFloatingSlideDirection.Down,
                Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp),
            ) { fab() }
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

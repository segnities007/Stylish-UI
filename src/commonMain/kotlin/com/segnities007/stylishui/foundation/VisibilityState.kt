package com.segnities007.stylishui.foundation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.ScrollState
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.math.sign

/**
 * Describes the visibility behavior of a UI element that can be shown or
 * hidden based on scroll position or other conditions.
 *
 * Used by components like [StylishHeader][com.segnities007.stylishui.components.patterns.StylishHeader],
 * [StylishFab][com.segnities007.stylishui.components.atoms.StylishFab], and
 * [StylishFooter][com.segnities007.stylishui.components.patterns.StylishFooter] to control
 * when they appear or disappear in response to user interaction.
 *
 * @see AlwaysVisible
 * @see AlwaysHidden
 * @see ScrollAware
 * @see NestedScrollAware
 */
@Immutable
public sealed class VisibilityState {
    /**
     * The element is always visible, regardless of scroll position or other conditions.
     *
     * This is the default behavior for most components and maintains backward compatibility
     * with existing code that does not specify a visibility state.
     */
    public object AlwaysVisible : VisibilityState()

    /**
     * The element is always hidden, regardless of scroll position or other conditions.
     *
     * Useful for temporarily disabling a component without removing it from the composition.
     */
    public object AlwaysHidden : VisibilityState()

    /**
     * The element's visibility is determined by the scroll position of a [ScrollState].
     *
     * The element is visible when the scroll offset is less than [threshold] (i.e., near the
     * top of the scrollable content), and hidden when the scroll offset exceeds the threshold.
     * This is commonly used to hide headers or footers when the user scrolls down and show them
     * when the user scrolls back up.
     *
     * @param scrollState The [ScrollState] to observe for scroll position changes.
     * @param threshold The scroll offset (in dp) below which the element is visible. Defaults
     *   to 48.dp. When the scroll position exceeds this value, the element is hidden.
     */
    public class ScrollAware(
        public val scrollState: ScrollState,
        public val threshold: Dp = 48.dp,
    ) : VisibilityState()

    /**
     * Visibility state driven by vertical nested-scroll gestures.
     *
     * Attach [nestedScrollConnection] to the scrolling container with `Modifier.nestedScroll`.
     * Upward content scrolling hides the element after [thresholdPx] has accumulated; downward
     * content scrolling shows it again after the same distance. A direction change resets the
     * accumulated distance so small opposing movements do not unexpectedly toggle visibility.
     *
     * Create instances with [rememberNestedScrollAwareVisibilityState], which converts the public
     * dp threshold to pixels for the current density.
     *
     * @property nestedScrollConnection Connection to attach to the scrolling container.
     * @property visible Whether the controlled element is currently visible.
     * @property thresholdPx Scroll distance in pixels required before visibility changes.
     */
    @Stable
    public class NestedScrollAware internal constructor(
        public val thresholdPx: Float,
        initiallyVisible: Boolean,
    ) : VisibilityState() {
        init {
            require(thresholdPx > 0f) { "thresholdPx must be greater than zero" }
        }

        public var visible: Boolean by mutableStateOf(initiallyVisible)
            private set

        private var accumulatedScroll: Float = 0f

        public val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                updateFromScroll(available.y)
                return Offset.Zero
            }
        }

        internal fun updateFromScroll(deltaY: Float) {
            if (deltaY == 0f) return

            if (accumulatedScroll != 0f && deltaY.sign != accumulatedScroll.sign) {
                accumulatedScroll = 0f
            }
            accumulatedScroll += deltaY

            when {
                visible && accumulatedScroll <= -thresholdPx -> {
                    visible = false
                    accumulatedScroll = 0f
                }
                !visible && accumulatedScroll >= thresholdPx -> {
                    visible = true
                    accumulatedScroll = 0f
                }
            }
        }
    }
}

/**
 * Creates a [VisibilityState.NestedScrollAware] for a nested-scrolling container.
 *
 * Apply the returned state's `nestedScrollConnection` to the scrolling parent:
 *
 * ```kotlin
 * val visibility = rememberNestedScrollAwareVisibilityState()
 * LazyColumn(Modifier.nestedScroll(visibility.nestedScrollConnection)) { /* ... */ }
 * StylishFab(onClick = {}, visibilityState = visibility) { /* ... */ }
 * ```
 *
 * @param threshold Distance of continuous scrolling in one direction before visibility changes.
 * @param initiallyVisible Initial visibility before any scroll gesture is observed.
 */
@Composable
public fun rememberNestedScrollAwareVisibilityState(
    threshold: Dp = 48.dp,
    initiallyVisible: Boolean = true,
): VisibilityState.NestedScrollAware {
    require(threshold > 0.dp) { "threshold must be greater than zero" }
    val thresholdPx = with(LocalDensity.current) { threshold.toPx() }
    return remember(thresholdPx, initiallyVisible) {
        VisibilityState.NestedScrollAware(
            thresholdPx = thresholdPx,
            initiallyVisible = initiallyVisible,
        )
    }
}

/**
 * Returns the current visibility state as a boolean, observing any reactive sources
 * (such as [VisibilityState.ScrollAware.scrollState]) as needed.
 *
 * This composable function evaluates the [VisibilityState] and returns `true` if the
 * element should be visible, `false` otherwise. For [VisibilityState.ScrollAware], it
 * observes the scroll position and compares it against the threshold.
 *
 * @return `true` if the element should be visible based on the current state, `false` otherwise.
 *
 * @see VisibilityState
 */
@Composable
public fun VisibilityState.isVisible(): Boolean {
    return when (this) {
        is VisibilityState.AlwaysVisible -> true
        is VisibilityState.AlwaysHidden -> false
        is VisibilityState.ScrollAware -> {
            val thresholdPx = with(LocalDensity.current) { threshold.toPx() }
            val visible = remember(scrollState, thresholdPx) {
                derivedStateOf {
                    scrollState.value < thresholdPx
                }
            }
            visible.value
        }
        is VisibilityState.NestedScrollAware -> visible
    }
}

/**
 * Holds the animated visibility state for a component, including the current visibility
 * boolean and an animated alpha value.
 *
 * @property visible Whether the component should be visible (before animation).
 * @property alpha The animated alpha value (0f when hidden, 1f when visible), suitable
 *   for applying to the component's modifier or content.
 *
 * @see rememberVisibilityAnimation
 */
@Immutable
public data class AnimatedVisibilityState(
    public val visible: Boolean,
    public val alpha: Float,
)

/**
 * Computes the animated visibility state for a given [VisibilityState], providing both
 * the current visibility boolean and an animated alpha value.
 *
 * This composable function observes the [visibilityState] and returns an [AnimatedVisibilityState]
 * that includes the visibility boolean and an animated alpha value. The alpha animates between
 * 0f (hidden) and 1f (visible) using the animation tokens from [StylishTheme.animation].
 *
 * Use this when you need both the visibility state and a smoothly animated alpha value for
 * custom transitions or when [AnimatedVisibility] is not suitable.
 *
 * @param visibilityState The [VisibilityState] to observe and animate.
 * @return An [AnimatedVisibilityState] with the current visibility and animated alpha.
 *
 * @see VisibilityState
 * @see AnimatedVisibilityState
 */
@Composable
public fun rememberVisibilityAnimation(visibilityState: VisibilityState): AnimatedVisibilityState {
    val visible = visibilityState.isVisible()
    val reducedMotion = isStylishReducedMotionEnabled()
    val animatedAlpha = animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (reducedMotion) 0 else StylishTheme.animation.durationShort,
            easing = StylishTheme.animation.defaultEasing,
        ),
        label = "visibilityAlpha",
    )
    return AnimatedVisibilityState(visible, animatedAlpha.value)
}

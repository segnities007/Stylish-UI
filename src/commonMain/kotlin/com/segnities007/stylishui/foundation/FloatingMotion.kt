package com.segnities007.stylishui.foundation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.IntOffset
import com.segnities007.stylishui.theme.StylishTheme

/** Marks content hosted by [StylishFloatingVisibility] to prevent nested motion. */
internal val LocalStylishFloatingVisibilityHost = compositionLocalOf { false }

/**
 * The edge from which a floating surface enters and toward which it exits.
 *
 * The direction controls only the travel axis and edge. All other motion
 * properties are fixed by [stylishFloatingEnterTransition] and
 * [stylishFloatingExitTransition], so Floating components share one motion
 * language across the library.
 */
public enum class StylishFloatingSlideDirection {
    /** Enter from the top and exit toward the top. */
    Up,

    /** Enter from the bottom and exit toward the bottom. */
    Down,

    /** Enter from the logical start edge and exit toward the start edge. */
    Start,

    /** Enter from the logical end edge and exit toward the end edge. */
    End,
}

/**
 * Creates the standard Stylish floating enter transition.
 *
 * The transition combines a full-distance slide with a fade. It uses
 * [StylishTheme.animation.durationMedium] and
 * [StylishTheme.animation.defaultEasing]. Reduced-motion settings replace
 * interpolation with an immediate transition.
 *
 * @param direction Edge from which the surface enters.
 */
@Composable
public fun stylishFloatingEnterTransition(
    direction: StylishFloatingSlideDirection,
): EnterTransition {
    val reducedMotion = isStylishReducedMotionEnabled()
    val layoutDirection = LocalLayoutDirection.current
    val fadeSpec: FiniteAnimationSpec<Float> = if (reducedMotion) {
        snap()
    } else {
        tween<Float>(
            durationMillis = StylishTheme.animation.durationMedium,
            easing = StylishTheme.animation.defaultEasing,
        )
    }
    val slideSpec: FiniteAnimationSpec<IntOffset> = if (reducedMotion) {
        snap()
    } else {
        tween<IntOffset>(
            durationMillis = StylishTheme.animation.durationMedium,
            easing = StylishTheme.animation.defaultEasing,
        )
    }

    return fadeIn(animationSpec = fadeSpec) + when (direction) {
        StylishFloatingSlideDirection.Up -> slideInVertically(slideSpec) { -it }
        StylishFloatingSlideDirection.Down -> slideInVertically(slideSpec) { it }
        StylishFloatingSlideDirection.Start -> slideInHorizontally(slideSpec) {
            if (layoutDirection == LayoutDirection.Ltr) -it else it
        }
        StylishFloatingSlideDirection.End -> slideInHorizontally(slideSpec) {
            if (layoutDirection == LayoutDirection.Ltr) it else -it
        }
    }
}

/**
 * Creates the standard Stylish floating exit transition.
 *
 * The transition combines a full-distance slide with a fade. It uses the
 * the same duration, easing, and reduced-motion behavior as
 * [stylishFloatingEnterTransition].
 *
 * @param direction Edge toward which the surface exits.
 */
@Composable
public fun stylishFloatingExitTransition(
    direction: StylishFloatingSlideDirection,
): ExitTransition {
    val reducedMotion = isStylishReducedMotionEnabled()
    val layoutDirection = LocalLayoutDirection.current
    val fadeSpec: FiniteAnimationSpec<Float> = if (reducedMotion) {
        snap()
    } else {
        tween<Float>(
            durationMillis = StylishTheme.animation.durationMedium,
            easing = StylishTheme.animation.defaultEasing,
        )
    }
    val slideSpec: FiniteAnimationSpec<IntOffset> = if (reducedMotion) {
        snap()
    } else {
        tween<IntOffset>(
            durationMillis = StylishTheme.animation.durationMedium,
            easing = StylishTheme.animation.defaultEasing,
        )
    }

    return fadeOut(animationSpec = fadeSpec) + when (direction) {
        StylishFloatingSlideDirection.Up -> slideOutVertically(slideSpec) { -it }
        StylishFloatingSlideDirection.Down -> slideOutVertically(slideSpec) { it }
        StylishFloatingSlideDirection.Start -> slideOutHorizontally(slideSpec) {
            if (layoutDirection == LayoutDirection.Ltr) -it else it
        }
        StylishFloatingSlideDirection.End -> slideOutHorizontally(slideSpec) {
            if (layoutDirection == LayoutDirection.Ltr) it else -it
        }
    }
}

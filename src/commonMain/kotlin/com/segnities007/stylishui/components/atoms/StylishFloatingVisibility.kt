package com.segnities007.stylishui.components.atoms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.LocalStylishFloatingVisibilityHost
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.foundation.stylishFloatingEnterTransition
import com.segnities007.stylishui.foundation.stylishFloatingExitTransition
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled

/**
 * Shows or hides floating content with the standard Stylish motion.
 *
 * The content enters from [direction] and exits toward the same edge. The
 * fade, travel distance, duration, easing, and reduced-motion behavior are
 * shared by every Stylish floating surface; [direction] is the only motion
 * variation.
 *
 * The underlying [AnimatedVisibility] keeps content composed until its exit
 * transition finishes. Do not wrap this composable in an outer `if` that
 * removes it when [visible] becomes `false`, or the exit transition cannot
 * run.
 *
 * @param visible Whether the floating content should be shown.
 * @param direction Edge from which the content enters and toward which it
 *   exits.
 * @param modifier Modifier applied to the visibility host.
 * @param content Floating content to display.
 */
@Composable
public fun StylishFloatingVisibility(
    visible: Boolean,
    direction: StylishFloatingSlideDirection,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (LocalStylishFloatingVisibilityHost.current) {
        // Preserve the caller's layout/test modifiers even when an outer
        // floating host owns the animation. The nested component contributes
        // no second transition, but it must still contribute its layout.
        Box(modifier = modifier) {
            if (visible) content()
        }
    } else {
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = stylishFloatingEnterTransition(direction),
            exit = stylishFloatingExitTransition(direction),
        ) {
            CompositionLocalProvider(LocalStylishFloatingVisibilityHost provides true) {
                content()
            }
        }
    }
}

/**
 * Internal compatibility motion for the original StylishMyVehicles FBB.
 *
 * The app-level FBB used a 200 ms fade plus a half-height vertical slide. Keep
 * this motion local to the bottom bar so the library's other floating surfaces
 * can continue to use the standard full-distance motion.
 */
@Composable
internal fun StylishFloatingBottomBarVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (LocalStylishFloatingVisibilityHost.current) {
        Box(modifier = modifier) {
            if (visible) content()
        }
    } else {
        val reducedMotion = isStylishReducedMotionEnabled()
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = if (reducedMotion) {
                EnterTransition.None
            } else {
                fadeIn(tween(FloatingBottomBarMotionDurationMillis)) +
                    slideInVertically(tween(FloatingBottomBarMotionDurationMillis)) { it / 2 }
            },
            exit = if (reducedMotion) {
                ExitTransition.None
            } else {
                fadeOut(tween(FloatingBottomBarMotionDurationMillis)) +
                    slideOutVertically(tween(FloatingBottomBarMotionDurationMillis)) { it / 2 }
            },
        ) {
            CompositionLocalProvider(LocalStylishFloatingVisibilityHost provides true) {
                content()
            }
        }
    }
}

private const val FloatingBottomBarMotionDurationMillis = 200

@Preview(name = "Floating visibility", showBackground = true)
@Composable
private fun StylishFloatingVisibilityPreview() {
    StylishFloatingVisibility(
        visible = true,
        direction = StylishFloatingSlideDirection.Down,
    ) {
        Text("Floating content")
    }
}

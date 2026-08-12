package com.segnities007.stylishui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.MotionDurationScale
import kotlinx.coroutines.currentCoroutineContext

/**
 * Composition local that forces the reduced-motion mode for
 * [isStylishReducedMotionEnabled], regardless of the platform setting.
 *
 * Defaults to `false`, which defers to the platform's motion preference.
 * Provide a different value to override it, e.g. in UI tests or in an app
 * that exposes its own motion preference:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalStylishReducedMotion provides true) {
 *     StylishTheme(darkTheme = darkTheme) { ... }
 * }
 * ```
 *
 * @see isStylishReducedMotionEnabled
 */
public val LocalStylishReducedMotion: ProvidableCompositionLocal<Boolean> =
    staticCompositionLocalOf { false }

/**
 * Returns whether the current platform requests reduced motion.
 *
 * Mirrors the system setting on supported platforms: the helper reads the
 * [MotionDurationScale] of the composition's coroutine context, which
 * Android sets to `0f` when the user enables the "remove animations" system
 * setting (and which test harnesses can control via the `effectContext`).
 * On platforms that do not expose such a setting (or when the scale is
 * `1f`), the helper returns the value of [LocalStylishReducedMotion],
 * which defaults to `false`.
 *
 * Components should consult this helper before starting decorative
 * animations, and snap to the target state instead of tweening when it
 * returns `true` (see the Stylish chip family for the reference pattern).
 *
 * @return `true` when the platform requests reduced motion or
 *   [LocalStylishReducedMotion] is set to `true`.
 */
@Composable
public fun isStylishReducedMotionEnabled(): Boolean {
    val context = rememberCoroutineScope().coroutineContext
    val motionDurationScale = context[MotionDurationScale]?.scaleFactor ?: 1f
    return LocalStylishReducedMotion.current || motionDurationScale == 0f
}

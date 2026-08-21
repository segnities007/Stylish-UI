package com.segnities007.stylishui.tokens

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Motion tokens for the Stylish UI design system.
 *
 * The default values produce the standard Stylish motion language: short, decisive
 * state-change animations with a single [defaultEasing] used across components. Override
 * globally via the [com.segnities007.stylishui.theme.StylishTheme] composable's `animation`
 * parameter, or per-component through individual component parameters where available.
 *
 * Access the active instance inside composables with
 * `StylishTheme.animation` (see [com.segnities007.stylishui.theme.StylishTheme]).
 *
 * @property durationShort Duration (ms) for micro-interactions: color state changes, icon
 *   swaps, selection highlights. Default 180 ms.
 * @property durationMedium Duration (ms) for element-level transitions: appearance,
 *   expansion, dialog entrances. Default 300 ms.
 * @property durationLong Duration (ms) for page-level motion: sheets, large surfaces.
 *   Default 500 ms.
 * @property durationEmphasized Duration (ms) for emphasized motion: elements that need extra
 *   attention (e.g. primary action entrances, state transitions that must be noticeable).
 *   Default 350 ms.
 * @property defaultEasing The easing curve applied to all standard Stylish animations.
 *   Default [FastOutSlowInEasing] — fast start, gentle settle.
 * @property emphasizedEasing The easing curve for emphasized motion that should feel more
 *   expressive. Default [EmphasizedEasing] — a strong accelerating start that decelerates
 *   smoothly into place.
 * @property gentleEasing The easing curve for gentle, slow-feeling motion such as fade-in and
 *   decorative ambient animation. Default [LinearOutSlowInEasing] — slow start that settles
 *   into place.
 * @property pressedScale Scale applied to press feedback. Default 0.98f.
 * @property springStiffness Stiffness used by spring-based micro-interactions. Default 700f.
 * @see DefaultStylishAnimationTokens
 * @see com.segnities007.stylishui.theme.StylishTheme
 * @see com.segnities007.stylishui.theme.StylishTheme.animation
 */
@Immutable
public data class StylishAnimationTokens(
    public val durationShort: Int = 180,
    public val durationMedium: Int = 300,
    public val durationLong: Int = 500,
    public val durationEmphasized: Int = 350,
    public val defaultEasing: Easing = FastOutSlowInEasing,
    public val emphasizedEasing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f),
    public val gentleEasing: Easing = LinearOutSlowInEasing,
    public val pressedScale: Float = 0.98f,
    public val springStiffness: Float = 700f,
)

/**
 * The default [StylishAnimationTokens] instance with all tokens at their standard values.
 *
 * Used as the initial value of [LocalStylishAnimation]. Inside a
 * [com.segnities007.stylishui.theme.StylishTheme] composition, prefer accessing tokens via
 * `StylishTheme.animation`.
 *
 * @see StylishAnimationTokens
 */
public val DefaultStylishAnimationTokens: StylishAnimationTokens = StylishAnimationTokens()

internal val LocalStylishAnimation: ProvidableCompositionLocal<StylishAnimationTokens> =
    staticCompositionLocalOf { DefaultStylishAnimationTokens }

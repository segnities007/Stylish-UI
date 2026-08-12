package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.segnities007.stylishui.tokens.DefaultStylishAnimationTokens
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import com.segnities007.stylishui.tokens.LocalStylishAnimation
import com.segnities007.stylishui.tokens.LocalStylishDimensions
import com.segnities007.stylishui.tokens.StylishAnimationTokens
import com.segnities007.stylishui.tokens.StylishDimensions

/**
 * Accessor object for the current [StylishTheme] composition-local values.
 *
 * Use `StylishTheme.dimensions` inside any composable to retrieve the [StylishDimensions]
 * tokens provided by the enclosing [StylishTheme] call, and `StylishTheme.animation` for the
 * [StylishAnimationTokens]. This mirrors the role that [MaterialTheme] plays for colors and
 * typography, but for Stylish-specific spatial and motion tokens.
 *
 * @see StylishTheme
 * @see StylishDimensions
 * @see StylishAnimationTokens
 */
public object StylishTheme {
    /** The dimension tokens provided by the current [StylishTheme] composition. */
    public val dimensions: StylishDimensions
        @Composable get() = LocalStylishDimensions.current

    /** The motion tokens provided by the current [StylishTheme] composition. */
    public val animation: StylishAnimationTokens
        @Composable get() = LocalStylishAnimation.current
}

/**
 * Root theme composable that applies the Stylish UI design language to all descendant content.
 *
 * Wraps [MaterialTheme] with a [CompositionLocalProvider] for [StylishDimensions] and
 * [StylishAnimationTokens], giving every Stylish component access to consistent spacing,
 * corner-radius, elevation, outline, and motion tokens. Place this at the top of your
 * composable hierarchy (typically in your `Activity` or `App` composable) so that all child
 * components inherit the design tokens.
 *
 * @param darkTheme Whether to use the dark color scheme. Typically derived from
 *   `isSystemInDarkTheme()`.
 * @param dynamicColor When `true` and the platform supports it (Android 12+), the wallpaper
 *   derived Material You scheme overrides [colorScheme]. On other platforms this parameter
 *   has no effect.
 * @param colorScheme The Material 3 [ColorScheme] to apply. Defaults to [StylishDarkColorScheme]
 *   when [darkTheme] is `true`, otherwise [StylishLightColorScheme]. Ignored when
 *   [dynamicColor] resolves a supported dynamic scheme.
 * @param typography The Material 3 [Typography] scale. Defaults to [StylishTypography].
 * @param shapes The Material 3 [Shapes] scale applied to components that do not use an
 *   explicit shape parameter. Defaults to the Material 3 defaults.
 * @param dimensions The [StylishDimensions] spatial tokens. Defaults to [DefaultStylishDimensions].
 *   Override globally here, or per-component via individual parameters.
 * @param animation The [StylishAnimationTokens] motion tokens. Defaults to
 *   [DefaultStylishAnimationTokens]. Override globally here, or per-component via individual
 *   parameters where available.
 * @param content The composable content that will be themed.
 * @see StylishTheme
 * @see StylishLightColorScheme
 * @see StylishDarkColorScheme
 * @see StylishTypography
 * @see DefaultStylishDimensions
 * @see DefaultStylishAnimationTokens
 */
@Composable
public fun StylishTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
    colorScheme: ColorScheme = if (darkTheme) StylishDarkColorScheme else StylishLightColorScheme,
    typography: Typography = StylishTypography,
    shapes: Shapes = Shapes(),
    dimensions: StylishDimensions = DefaultStylishDimensions,
    animation: StylishAnimationTokens = DefaultStylishAnimationTokens,
    content: @Composable () -> Unit,
) {
    val resolvedColorScheme = if (dynamicColor) {
        rememberDynamicColorSchemes()?.let { if (darkTheme) it.second else it.first } ?: colorScheme
    } else {
        colorScheme
    }
    CompositionLocalProvider(
        LocalStylishDimensions provides dimensions,
        LocalStylishAnimation provides animation,
    ) {
        MaterialTheme(
            colorScheme = resolvedColorScheme,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }
}

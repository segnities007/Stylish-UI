package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.materialkolor.dynamicColorScheme
import com.segnities007.stylishui.tokens.DefaultStylishAnimationTokens
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import com.segnities007.stylishui.tokens.DefaultStylishShapes
import com.segnities007.stylishui.tokens.LocalStylishAnimation
import com.segnities007.stylishui.tokens.LocalStylishDimensions
import com.segnities007.stylishui.tokens.LocalStylishShapes
import com.segnities007.stylishui.tokens.StylishAnimationTokens
import com.segnities007.stylishui.tokens.StylishDimensions
import com.segnities007.stylishui.tokens.StylishShapes

/**
 * Accessor object for the current [StylishTheme] composition-local values.
 *
 * Use `StylishTheme.dimensions` inside any composable to retrieve the [StylishDimensions]
 * tokens provided by the enclosing [StylishTheme] call, `StylishTheme.animation` for the
 * [StylishAnimationTokens], and `StylishTheme.shapes` for the [StylishShapes] tokens.
 * This mirrors the role that [MaterialTheme] plays for colors and typography, but for
 * Stylish-specific spatial, motion, and shape tokens.
 *
 * @see StylishTheme
 * @see StylishDimensions
 * @see StylishAnimationTokens
 * @see StylishShapes
 */
public object StylishTheme {
    /** The dimension tokens provided by the current [StylishTheme] composition. */
    public val dimensions: StylishDimensions
        @Composable get() = LocalStylishDimensions.current

    /** The motion tokens provided by the current [StylishTheme] composition. */
    public val animation: StylishAnimationTokens
        @Composable get() = LocalStylishAnimation.current

    /** The shape tokens provided by the current [StylishTheme] composition. */
    public val shapes: StylishShapes
        @Composable get() = LocalStylishShapes.current
}

/**
 * Root theme composable that applies the Stylish UI design language to all descendant content.
 *
 * Wraps [MaterialTheme] with a [CompositionLocalProvider] for [StylishDimensions],
 * [StylishAnimationTokens], [StylishShapes], and [StylishComponentColors], giving every
 * Stylish component access to consistent spacing, corner-radius, elevation, outline, motion,
 * shape, and derived-color tokens. Place this at the top of your composable hierarchy
 * (typically in your `Activity` or `App` composable) so that all child components inherit
 * the design tokens.
 *
 * @param darkTheme Whether to use the dark color scheme. Typically derived from
 *   `isSystemInDarkTheme()`.
 * @param dynamicColor When `true` and the platform supports it (Android 12+), the wallpaper
 *   derived Material You scheme overrides [colorScheme]. On other platforms this parameter
 *   has no effect. Ignored while [seedColor] resolves a scheme.
 * @param seedColor When non-null, a seed color used to generate a Material You style
 *   [ColorScheme] on **every** platform via MaterialKolor, overriding both [dynamicColor]
 *   and [colorScheme]. Use this for brand-colored themes that still follow the tonal
 *   Material 3 color system.
 * @param colorScheme The Material 3 [ColorScheme] to apply. Defaults to [StylishDarkColorScheme]
 *   when [darkTheme] is `true`, otherwise [StylishLightColorScheme]. Ignored when [seedColor]
 *   or [dynamicColor] resolves a dynamic scheme.
 * @param typography The Material 3 [Typography] scale. Defaults to [StylishTypography].
 * @param materialShapes The Material 3 [Shapes] scale applied to M3 primitives that do not use
 *   an explicit shape parameter. Defaults to the Material 3 defaults. Use [shapes] to style
 *   Stylish-specific shape tokens.
 * @param dimensions The [StylishDimensions] spatial tokens. Defaults to [DefaultStylishDimensions].
 *   Override globally here, or per-component via individual parameters.
 * @param shapes The [StylishShapes] corner-radius tokens. Defaults to [DefaultStylishShapes].
 *   Override globally here, or per-component via individual parameters.
 * @param animation The [StylishAnimationTokens] motion tokens. Defaults to
 *   [DefaultStylishAnimationTokens]. Override globally here, or per-component via individual
 *   parameters where available.
 * @param componentColors The [StylishComponentColors] used by Stylish components. When `null`
 *   (the default) they are computed from the resolved [ColorScheme]; pass an instance to
 *   override the derived colors globally.
 * @param content The composable content that will be themed.
 * @see StylishTheme
 * @see StylishLightColorScheme
 * @see StylishDarkColorScheme
 * @see StylishTypography
 * @see DefaultStylishDimensions
 * @see DefaultStylishAnimationTokens
 * @see DefaultStylishShapes
 */
@Composable
public fun StylishTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = false,
    seedColor: Color? = null,
    colorScheme: ColorScheme = if (darkTheme) StylishDarkColorScheme else StylishLightColorScheme,
    typography: Typography = StylishTypography,
    materialShapes: Shapes = Shapes(),
    dimensions: StylishDimensions = DefaultStylishDimensions,
    shapes: StylishShapes = DefaultStylishShapes,
    animation: StylishAnimationTokens = DefaultStylishAnimationTokens,
    componentColors: StylishComponentColors? = null,
    content: @Composable () -> Unit,
) {
    val resolvedColorScheme = when {
        seedColor != null -> remember(seedColor, darkTheme) {
            dynamicColorScheme(seedColor = seedColor, isDark = darkTheme)
        }
        dynamicColor -> {
            rememberDynamicColorSchemes()?.let { if (darkTheme) it.second else it.first } ?: colorScheme
        }
        else -> colorScheme
    }
    val resolvedComponentColors = componentColors ?: stylishComponentColors(resolvedColorScheme)
    CompositionLocalProvider(
        LocalStylishDimensions provides dimensions,
        LocalStylishAnimation provides animation,
        LocalStylishShapes provides shapes,
        LocalStylishComponentColors provides resolvedComponentColors,
    ) {
        MaterialTheme(
            colorScheme = resolvedColorScheme,
            typography = typography,
            shapes = materialShapes,
            content = content,
        )
    }
}

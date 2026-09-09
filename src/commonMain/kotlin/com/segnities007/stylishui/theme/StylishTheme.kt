package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.segnities007.stylishui.tokens.DefaultStylishAnimationTokens
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import com.segnities007.stylishui.tokens.DefaultStylishShapes
import com.segnities007.stylishui.tokens.LocalStylishAnimation
import com.segnities007.stylishui.tokens.LocalStylishDimensions
import com.segnities007.stylishui.tokens.LocalStylishShapes
import com.segnities007.stylishui.tokens.StylishAnimationTokens
import com.segnities007.stylishui.tokens.StylishDimensions
import com.segnities007.stylishui.tokens.StylishShapes
import com.segnities007.stylishui.tokens.toMaterialShapes

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
    /** Resolved light/dark/dynamic colors, shared by all Stylish components. */
    public val colorScheme: ColorScheme
        @Composable get() = MaterialTheme.colorScheme

    /** Resolved typography, including the host's font overrides. */
    public val typography: Typography
        @Composable get() = MaterialTheme.typography

    /** The dimension tokens provided by the current [StylishTheme] composition. */
    public val dimensions: StylishDimensions
        @Composable get() = LocalStylishDimensions.current

    /** The motion tokens provided by the current [StylishTheme] composition. */
    public val animation: StylishAnimationTokens
        @Composable get() = LocalStylishAnimation.current

    /** The shape tokens provided by the current [StylishTheme] composition. */
    public val shapes: StylishShapes
        @Composable get() = LocalStylishShapes.current

    /** Localized labels provided by the current [StylishTheme] composition. */
    public val strings: StylishStrings
        @Composable get() = LocalStylishStrings.current
}

/**
 * Applies one light/dark brand directly through [StylishTheme], without a registry or injected
 * brand object.
 *
 * Material 3 remains responsible for component behavior. Its color, typography, and shape
 * systems are applied together with Stylish UI's dimensions, shapes, motion, and component
 * colors. Descendant components read these values from the normal Compose theme hierarchy.
 *
 * @param lightColorScheme Material 3 semantic colors used in light mode.
 * @param darkColorScheme Material 3 semantic colors used in dark mode.
 * @param darkTheme Whether to select [darkColorScheme].
 * @param highContrast Whether to select the matching high-contrast scheme.
 * @param highContrastLightColorScheme Optional brand-owned high-contrast light scheme.
 * @param highContrastDarkColorScheme Optional brand-owned high-contrast dark scheme.
 * @param typography Material 3 typography shared by Material and Stylish components.
 * @param materialShapes Compatibility override for internal Material 3 shapes. Normally omit it
 *   so [shapes] remains the single brand shape source.
 * @param dimensions Stylish spacing, sizing, elevation, and geometry tokens.
 * @param shapes Single shape scale for Stylish components and internal Material 3 primitives.
 * @param animation Stylish motion tokens.
 * @param lightComponentColors Optional light component colors; `null` derives them from
 *   [lightColorScheme].
 * @param darkComponentColors Optional dark component colors; `null` derives them from
 *   [darkColorScheme].
 * @param strings Localized labels supplied independently from visual branding.
 * @param content The composable content that inherits the theme.
 */
@Composable
public fun StylishTheme(
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    highContrastLightColorScheme: ColorScheme? = null,
    highContrastDarkColorScheme: ColorScheme? = null,
    typography: Typography = StylishTypography,
    dimensions: StylishDimensions = DefaultStylishDimensions,
    shapes: StylishShapes = DefaultStylishShapes,
    materialShapes: Shapes = shapes.toMaterialShapes(),
    animation: StylishAnimationTokens = DefaultStylishAnimationTokens,
    lightComponentColors: StylishComponentColors? = null,
    darkComponentColors: StylishComponentColors? = null,
    strings: StylishStrings = StylishStrings(),
    content: @Composable () -> Unit,
) {
    StylishTheme(
        darkTheme = darkTheme,
        colorScheme = if (darkTheme) darkColorScheme else lightColorScheme,
        highContrast = highContrast,
        highContrastColorScheme = when {
            !highContrast -> null
            darkTheme -> highContrastDarkColorScheme
            else -> highContrastLightColorScheme
        },
        typography = typography,
        materialShapes = materialShapes,
        dimensions = dimensions,
        shapes = shapes,
        animation = animation,
        componentColors = if (highContrast) {
            null
        } else if (darkTheme) {
            darkComponentColors
        } else {
            lightComponentColors
        },
        strings = strings,
        content = content,
    )
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
 * @param colorScheme The Material 3 [ColorScheme] to apply. Defaults to [StylishDarkColorScheme]
 *   when [darkTheme] is `true`, otherwise [StylishLightColorScheme]. Only fixed brand schemes
 *   are supported; device wallpaper colors are never applied.
 * @param highContrast When `true`, use an explicit high-contrast semantic role set. Pass
 *   [highContrastColorScheme] to preserve product branding while retaining this mode contract.
 * @param highContrastColorScheme Optional application-owned high-contrast scheme. When `null`,
 *   Stylish's deterministic light or dark high-contrast scheme is selected.
 * @param typography The Material 3 [Typography] scale. Defaults to [StylishTypography].
 * @param materialShapes Compatibility override for the internal Material 3 [Shapes] scale. Leave
 *   `null` so [shapes] remains the single brand shape source.
 * @param dimensions The [StylishDimensions] spatial tokens. Defaults to [DefaultStylishDimensions].
 *   Override globally here, or per-component via individual parameters.
 * @param shapes The single brand shape scale for both Stylish components and internal Material 3
 *   primitives. Defaults to [DefaultStylishShapes].
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (darkTheme) StylishDarkColorScheme else StylishLightColorScheme,
    highContrast: Boolean = false,
    highContrastColorScheme: ColorScheme? = null,
    typography: Typography = StylishTypography,
    materialShapes: Shapes? = null,
    dimensions: StylishDimensions = DefaultStylishDimensions,
    shapes: StylishShapes = DefaultStylishShapes,
    animation: StylishAnimationTokens = DefaultStylishAnimationTokens,
    componentColors: StylishComponentColors? = null,
    strings: StylishStrings = StylishStrings(),
    content: @Composable () -> Unit,
) {
    val resolvedColorScheme = when {
        highContrast -> highContrastColorScheme
            ?: if (darkTheme) StylishHighContrastDarkColorScheme else StylishHighContrastLightColorScheme
        else -> colorScheme
    }
    val resolvedComponentColors = componentColors ?: stylishComponentColors(resolvedColorScheme)
    CompositionLocalProvider(
        LocalStylishDimensions provides dimensions,
        LocalStylishAnimation provides animation,
        LocalStylishShapes provides shapes,
        LocalStylishComponentColors provides resolvedComponentColors,
        LocalStylishStrings provides strings,
    ) {
        StylishLayerRoot {
            MaterialTheme(
                colorScheme = resolvedColorScheme,
                typography = typography,
                shapes = materialShapes ?: shapes.toMaterialShapes(),
                content = content,
            )
        }
    }
}

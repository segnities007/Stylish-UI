package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import com.segnities007.stylishui.tokens.LocalStylishDimensions
import com.segnities007.stylishui.tokens.StylishDimensions

/**
 * Accessor object for the current [StylishTheme] composition-local values.
 *
 * Use `StylishTheme.dimensions` inside any composable to retrieve the [StylishDimensions]
 * tokens provided by the enclosing [StylishTheme] call. This mirrors the role that
 * [MaterialTheme] plays for colors and typography, but for Stylish-specific spatial tokens.
 *
 * @see StylishTheme
 * @see StylishDimensions
 */
public object StylishTheme {
    /** The dimension tokens provided by the current [StylishTheme] composition. */
    public val dimensions: StylishDimensions
        @Composable get() = LocalStylishDimensions.current
}

/**
 * Root theme composable that applies the Stylish UI design language to all descendant content.
 *
 * Wraps [MaterialTheme] with a [CompositionLocalProvider] for [StylishDimensions], giving every
 * Stylish component access to consistent spacing, corner-radius, elevation, and outline tokens.
 * Place this at the top of your composable hierarchy (typically in your `Activity` or `App`
 * composable) so that all child components inherit the design tokens.
 *
 * @param darkTheme Whether to use the dark color scheme. Typically derived from
 *   `isSystemInDarkTheme()`.
 * @param colorScheme The Material 3 [ColorScheme] to apply. Defaults to [StylishDarkColorScheme]
 *   when [darkTheme] is `true`, otherwise [StylishLightColorScheme].
 * @param typography The Material 3 [Typography] scale. Defaults to [StylishTypography].
 * @param dimensions The [StylishDimensions] spatial tokens. Defaults to [DefaultStylishDimensions].
 *   Override globally here, or per-component via individual parameters.
 * @param content The composable content that will be themed.
 * @see StylishTheme
 * @see StylishLightColorScheme
 * @see StylishDarkColorScheme
 * @see StylishTypography
 * @see DefaultStylishDimensions
 */
@Composable
public fun StylishTheme(
    darkTheme: Boolean,
    colorScheme: ColorScheme = if (darkTheme) StylishDarkColorScheme else StylishLightColorScheme,
    typography: Typography = StylishTypography,
    dimensions: StylishDimensions = DefaultStylishDimensions,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalStylishDimensions provides dimensions) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content,
        )
    }
}

package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

/**
 * Light-mode [ColorScheme] for the Stylish UI design system.
 *
 * Built on the [StylishPalette] with a paper-white background ([StylishPalette.Paper]) and
 * ink-dark foreground ([StylishPalette.Ink]), producing high-contrast surfaces with subtle
 * warm undertones. Surface-container roles use distinct tints so that nested cards, dialogs,
 * and bottom sheets are visually distinguishable without relying on elevation shadows alone.
 *
 * Pass this to the [StylishTheme] composable's `colorScheme` parameter, or use it directly
 * with [MaterialTheme] if you only need colors without the full Stylish theme wrapper.
 *
 * @see StylishDarkColorScheme
 * @see StylishTheme
 */
public val StylishLightColorScheme: ColorScheme = lightColorScheme(
    primary = StylishPalette.Ink,
    onPrimary = StylishPalette.PureSurface,
    primaryContainer = StylishPalette.LightPrimaryContainer,
    onPrimaryContainer = StylishPalette.LightOnPrimaryContainer,
    secondary = StylishPalette.LightSecondary,
    onSecondary = StylishPalette.PureSurface,
    secondaryContainer = StylishPalette.LightSecondaryContainer,
    onSecondaryContainer = StylishPalette.Ink,
    tertiary = StylishPalette.LightTertiary,
    onTertiary = StylishPalette.PureSurface,
    tertiaryContainer = StylishPalette.LightTertiaryContainer,
    onTertiaryContainer = StylishPalette.Ink,
    error = StylishPalette.LightError,
    onError = StylishPalette.PureSurface,
    errorContainer = StylishPalette.LightErrorContainer,
    onErrorContainer = StylishPalette.LightOnErrorContainer,
    background = StylishPalette.Paper,
    onBackground = StylishPalette.Ink,
    surface = StylishPalette.PureSurface,
    onSurface = StylishPalette.Ink,
    surfaceVariant = StylishPalette.LightSurfaceVariant,
    onSurfaceVariant = StylishPalette.Muted,
    outline = StylishPalette.LightOutline,
    outlineVariant = StylishPalette.SoftOutline,
    inverseSurface = StylishPalette.LightInverseSurface,
    inverseOnSurface = StylishPalette.LightInverseOnSurface,
    inversePrimary = StylishPalette.LightInversePrimary,
    surfaceContainerLowest = StylishPalette.PureSurface,
    surfaceContainerLow = StylishPalette.LightSurfaceContainerLow,
    surfaceContainer = StylishPalette.SoftSurface,
    surfaceContainerHigh = StylishPalette.LightSurfaceContainerHigh,
    surfaceContainerHighest = StylishPalette.LightSurfaceContainerHighest,
    scrim = StylishPalette.Scrim,
)

/**
 * Dark-mode [ColorScheme] for the Stylish UI design system.
 *
 * Mirrors [StylishLightColorScheme] with inverted luminance roles: a deep paper-dark background
 * ([StylishPalette.DarkPaper]) and light ink foreground ([StylishPalette.DarkInk]). Surface
 * containers step up in brightness so that layered UI (cards, sheets, menus) reads clearly
 * against the dark canvas.
 *
 * Pass this to the [StylishTheme] composable's `colorScheme` parameter, or use it directly
 * with [MaterialTheme] if you only need colors without the full Stylish theme wrapper.
 *
 * @see StylishLightColorScheme
 * @see StylishTheme
 */
public val StylishDarkColorScheme: ColorScheme = darkColorScheme(
    primary = StylishPalette.DarkInk,
    onPrimary = StylishPalette.DarkPaper,
    primaryContainer = StylishPalette.DarkPrimaryContainer,
    onPrimaryContainer = StylishPalette.DarkOnPrimaryContainer,
    secondary = StylishPalette.DarkSecondary,
    onSecondary = StylishPalette.DarkPaper,
    secondaryContainer = StylishPalette.DarkSecondaryContainer,
    onSecondaryContainer = StylishPalette.DarkInk,
    tertiary = StylishPalette.DarkTertiary,
    onTertiary = StylishPalette.DarkPaper,
    tertiaryContainer = StylishPalette.DarkTertiaryContainer,
    onTertiaryContainer = StylishPalette.DarkInk,
    error = StylishPalette.DarkError,
    onError = StylishPalette.DarkOnError,
    errorContainer = StylishPalette.DarkErrorContainer,
    onErrorContainer = StylishPalette.DarkOnErrorContainer,
    background = StylishPalette.DarkPaper,
    onBackground = StylishPalette.DarkInk,
    surface = StylishPalette.DarkSurface,
    onSurface = StylishPalette.DarkInk,
    surfaceVariant = StylishPalette.DarkSurfaceVariant,
    onSurfaceVariant = StylishPalette.DarkMuted,
    outline = StylishPalette.DarkOutlineColor,
    outlineVariant = StylishPalette.DarkOutline,
    inverseSurface = StylishPalette.DarkInverseSurface,
    inverseOnSurface = StylishPalette.DarkInverseOnSurface,
    inversePrimary = StylishPalette.DarkInversePrimary,
    surfaceContainerLowest = StylishPalette.DarkSurfaceContainerLowest,
    surfaceContainerLow = StylishPalette.DarkSurfaceContainerLow,
    surfaceContainer = StylishPalette.DarkSoftSurface,
    surfaceContainerHigh = StylishPalette.DarkSurfaceContainerHigh,
    surfaceContainerHighest = StylishPalette.DarkSurfaceContainerHighest,
    scrim = StylishPalette.Scrim,
)

package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

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
    surfaceBright = StylishPalette.LightSurfaceBright,
    surfaceDim = StylishPalette.LightSurfaceDim,
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
    surfaceBright = StylishPalette.DarkSurfaceBright,
    surfaceDim = StylishPalette.DarkSurfaceDim,
    scrim = StylishPalette.Scrim,
)

/** High-contrast light semantic roles for users who need stronger separation. */
public val StylishHighContrastLightColorScheme: ColorScheme = StylishLightColorScheme.copy(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    secondary = Color.Black,
    onSecondary = Color.White,
    secondaryContainer = Color.White,
    onSecondaryContainer = Color.Black,
    tertiary = Color.Black,
    onTertiary = Color.White,
    tertiaryContainer = Color.White,
    onTertiaryContainer = Color.Black,
    error = Color(0xFF8B0000),
    onError = Color.White,
    errorContainer = Color.White,
    onErrorContainer = Color(0xFF5C0000),
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    outline = Color.Black,
    outlineVariant = Color.Black,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
    surfaceBright = Color.White,
    surfaceDim = Color.White,
)

/** High-contrast dark semantic roles for users who need stronger separation. */
public val StylishHighContrastDarkColorScheme: ColorScheme = StylishDarkColorScheme.copy(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color.Black,
    onPrimaryContainer = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    secondaryContainer = Color.Black,
    onSecondaryContainer = Color.White,
    tertiary = Color.White,
    onTertiary = Color.Black,
    tertiaryContainer = Color.Black,
    onTertiaryContainer = Color.White,
    error = Color(0xFFFF8A80),
    onError = Color.Black,
    errorContainer = Color.Black,
    onErrorContainer = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color.Black,
    onSurfaceVariant = Color.White,
    outline = Color.White,
    outlineVariant = Color.White,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color.Black,
    surfaceContainer = Color.Black,
    surfaceContainerHigh = Color.Black,
    surfaceContainerHighest = Color.Black,
    surfaceBright = Color.Black,
    surfaceDim = Color.Black,
)

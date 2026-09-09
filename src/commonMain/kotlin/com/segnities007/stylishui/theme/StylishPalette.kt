package com.segnities007.stylishui.theme

import androidx.compose.ui.graphics.Color

/** Internal color palette used by the StylishUI light/dark themes. */
internal object StylishPalette {
    val Ink = Color(0xFF2B261F)
    // Creamy Milk Tea layer endpoints. Intermediate surfaces are derived in
    // Oklab from these endpoints instead of being maintained as independent
    // swatches, keeping every visual layer on one continuous, low-saturation
    // warm scale. Chroma is reduced after authoring so the milk tone stays
    // present without becoming yellow or red as layers become brighter.
    private const val LayerChromaScale = 0.425f
    val Paper = desaturateLayerColor(Color(0xFFEFE4D3), LayerChromaScale)
    val PureSurface = desaturateLayerColor(Color(0xFFFFFAF2), LayerChromaScale)
    val SoftSurface = interpolateLayerColor(Paper, PureSurface, 0.5f)
    val Muted = Color(0xFF6F675B)
    val SoftOutline = Color(0xFFD8CCB9)
    val LightSecondary = Color(0xFF6B6255)
    val LightSecondaryContainer = Color(0xFFE9DFCF)
    val LightTertiary = Color(0xFF735E4C)
    val LightTertiaryContainer = Color(0xFFF0DDCC)
    val LightError = Color(0xFFBA1A1A)
    val LightErrorContainer = Color(0xFFFFDAD6)
    val LightOnErrorContainer = Color(0xFF410002)
    val LightPrimaryContainer = Color(0xFFE7DCC9)
    val LightOnPrimaryContainer = Color(0xFF292116)
    val LightSurfaceVariant = interpolateLayerColor(Paper, PureSurface, 0.5f)
    val LightOutline = Color(0xFF7C7264)
    val LightInverseSurface = Color(0xFF332F29)
    val LightInverseOnSurface = Color(0xFFF8F0E4)
    val LightInversePrimary = Color(0xFFD8CBB6)
    val LightSurfaceContainerHigh = interpolateLayerColor(Paper, PureSurface, 0.75f)
    val LightSurfaceContainerHighest = interpolateLayerColor(Paper, PureSurface, 1f)
    val LightSurfaceContainerLow = interpolateLayerColor(Paper, PureSurface, 0.25f)
    val LightSurfaceBright = PureSurface
    val LightSurfaceDim = Paper
    val Scrim = Color(0xFF000000)

    val DarkInk = Color(0xFFF1E9DC)
    // The dark endpoints use a muted taupe-brown instead of a red-brown so
    // the same milk-tea identity survives in dark mode. Their chroma is
    // reduced by the same factor as the light endpoints.
    val DarkPaper = desaturateLayerColor(Color(0xFF1C1915), LayerChromaScale)
    private val DarkLayerTop = desaturateLayerColor(Color(0xFF4B4237), LayerChromaScale)
    val DarkSurface = interpolateLayerColor(DarkPaper, DarkLayerTop, 0.5f)
    val DarkSoftSurface = interpolateLayerColor(DarkPaper, DarkLayerTop, 0.5f)
    val DarkMuted = Color(0xFFC8BDAE)
    val DarkOutline = Color(0xFF51483D)
    val DarkSecondary = Color(0xFFD5C9B8)
    val DarkSecondaryContainer = Color(0xFF4C4438)
    val DarkTertiary = Color(0xFFE2C3A9)
    val DarkTertiaryContainer = Color(0xFF594536)
    val DarkError = Color(0xFFFFB4AB)
    val DarkErrorContainer = Color(0xFF93000A)
    val DarkOnError = Color(0xFF690005)
    val DarkOnErrorContainer = Color(0xFFFFDAD6)
    val DarkPrimaryContainer = Color(0xFF50483B)
    val DarkOnPrimaryContainer = Color(0xFFF3E8D6)
    val DarkSurfaceVariant = interpolateLayerColor(DarkPaper, DarkLayerTop, 0.75f)
    val DarkOutlineColor = Color(0xFF9B8F7F)
    val DarkInverseSurface = Color(0xFFF1E9DC)
    val DarkInverseOnSurface = Color(0xFF332F29)
    val DarkInversePrimary = Color(0xFF675D4D)
    // Keep the dark container ladder monotonic: Container < High < Highest.
    val DarkSurfaceContainerHigh = interpolateLayerColor(DarkPaper, DarkLayerTop, 0.75f)
    val DarkSurfaceContainerHighest = interpolateLayerColor(DarkPaper, DarkLayerTop, 1f)
    val DarkSurfaceContainerLow = interpolateLayerColor(DarkPaper, DarkLayerTop, 0.25f)
    val DarkSurfaceContainerLowest = desaturateLayerColor(Color(0xFF0C0D06), LayerChromaScale)
    val DarkSurfaceBright = DarkLayerTop
    val DarkSurfaceDim = DarkSurfaceContainerLowest
}

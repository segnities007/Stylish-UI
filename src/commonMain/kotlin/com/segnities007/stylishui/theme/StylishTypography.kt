package com.segnities007.stylishui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private fun style(
    weight: FontWeight,
    size: Int,
    lineHeight: Int,
    letterSpacing: Float = 0f,
): TextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.sp,
)

/**
 * The default [Typography] scale for the Stylish UI design system.
 *
 * Defines all fifteen Material 3 text styles using the platform default font family
 * ([FontFamily.Default]) so that the system font renders on each platform. The scale is
 * tuned for mobile-first reading comfort:
 *
 * - **Display** (Bold, 22–28 sp): hero text, onboarding headlines.
 * - **Headline** (SemiBold, 18–24 sp): section titles, screen headers.
 * - **Title** (SemiBold/Medium, 14–18 sp): card titles, list headers, dialog titles.
 * - **Body** (Normal, 12–16 sp): paragraphs, descriptions, supporting text.
 * - **Label** (Medium, 11–14 sp): button text, captions, badges, chip labels.
 *
 * Line heights follow a ~1.27–1.5× ratio for comfortable vertical rhythm, and letter spacing
 * follows the Material 3 standard per style (titles and labels slightly tracked out, body and
 * label text at 0.4–0.5 sp, display/headline styles untracked).
 *
 * Pass this to [StylishTheme]'s `typography` parameter, or use directly with [MaterialTheme].
 *
 * @see StylishTheme
 */
public val StylishTypography: Typography = Typography(
    displayLarge = style(FontWeight.Bold, 28, 36),
    displayMedium = style(FontWeight.Bold, 24, 32),
    displaySmall = style(FontWeight.Bold, 22, 28),
    headlineLarge = style(FontWeight.SemiBold, 24, 32),
    headlineMedium = style(FontWeight.SemiBold, 20, 28),
    headlineSmall = style(FontWeight.SemiBold, 18, 24),
    titleLarge = style(FontWeight.SemiBold, 18, 26),
    titleMedium = style(FontWeight.SemiBold, 16, 24, letterSpacing = 0.15f),
    titleSmall = style(FontWeight.Medium, 14, 20, letterSpacing = 0.1f),
    bodyLarge = style(FontWeight.Normal, 16, 24, letterSpacing = 0.5f),
    bodyMedium = style(FontWeight.Normal, 14, 20, letterSpacing = 0.25f),
    bodySmall = style(FontWeight.Normal, 12, 16, letterSpacing = 0.4f),
    labelLarge = style(FontWeight.Medium, 14, 20, letterSpacing = 0.1f),
    labelMedium = style(FontWeight.Medium, 12, 16, letterSpacing = 0.5f),
    labelSmall = style(FontWeight.Medium, 11, 16, letterSpacing = 0.5f),
)

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
 * Returns this typography scale with one explicit font family applied to every Material 3
 * text role. Hosts can use this for a brand font or a bundled cross-platform fallback while
 * preserving Stylish UI's size, weight, line-height, and tracking contract.
 *
 * @param fontFamily the family used by all fifteen text roles
 */
public fun Typography.withFontFamily(fontFamily: FontFamily): Typography = copy(
    displayLarge = displayLarge.copy(fontFamily = fontFamily),
    displayMedium = displayMedium.copy(fontFamily = fontFamily),
    displaySmall = displaySmall.copy(fontFamily = fontFamily),
    headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
    titleLarge = titleLarge.copy(fontFamily = fontFamily),
    titleMedium = titleMedium.copy(fontFamily = fontFamily),
    titleSmall = titleSmall.copy(fontFamily = fontFamily),
    bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = bodySmall.copy(fontFamily = fontFamily),
    labelLarge = labelLarge.copy(fontFamily = fontFamily),
    labelMedium = labelMedium.copy(fontFamily = fontFamily),
    labelSmall = labelSmall.copy(fontFamily = fontFamily),
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

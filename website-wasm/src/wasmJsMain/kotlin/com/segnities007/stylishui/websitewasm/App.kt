package com.segnities007.stylishui.websitewasm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.segnities007.stylishui.catalog.StylishPlayground
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.StylishTypography
import org.jetbrains.compose.resources.Font
import com.segnities007.stylishui.websitewasm.resources.NotoSansJP_subset
import com.segnities007.stylishui.websitewasm.resources.Res

/**
 * Skiko/Wasm cannot read system fonts, so a Japanese subset of
 * Noto Sans JP is bundled with the site. Without it, Japanese text
 * renders as tofu boxes.
 */
@Composable
private fun stylishFontFamily(): FontFamily = FontFamily(
    Font(Res.font.NotoSansJP_subset, weight = FontWeight.Normal),
    Font(Res.font.NotoSansJP_subset, weight = FontWeight.Medium),
    Font(Res.font.NotoSansJP_subset, weight = FontWeight.Bold),
)

private fun androidx.compose.material3.Typography.withFontFamily(fontFamily: FontFamily) = copy(
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

@Composable
fun App() {
    // Default to dark theme for the gallery aesthetic
    var darkTheme by remember { mutableStateOf(true) }
    StylishTheme(
        darkTheme = darkTheme,
        typography = StylishTypography.withFontFamily(stylishFontFamily()),
    ) {
        Surface(Modifier.fillMaxSize()) {
            StylishPlayground(
                darkTheme = darkTheme,
                onToggleTheme = { darkTheme = !darkTheme },
            )
        }
    }
}

package com.segnities007.stylishui.theme

import androidx.compose.ui.text.font.FontFamily
import kotlin.test.Test
import kotlin.test.assertEquals

class StylishTypographyTest {
    @Test
    fun withFontFamilyAppliesBrandFamilyToEveryRole() {
        val brand = FontFamily.SansSerif
        val typography = StylishTypography.withFontFamily(brand)

        val roles = listOf(
            typography.displayLarge,
            typography.displayMedium,
            typography.displaySmall,
            typography.headlineLarge,
            typography.headlineMedium,
            typography.headlineSmall,
            typography.titleLarge,
            typography.titleMedium,
            typography.titleSmall,
            typography.bodyLarge,
            typography.bodyMedium,
            typography.bodySmall,
            typography.labelLarge,
            typography.labelMedium,
            typography.labelSmall,
        )

        roles.forEach { assertEquals(brand, it.fontFamily) }
    }
}

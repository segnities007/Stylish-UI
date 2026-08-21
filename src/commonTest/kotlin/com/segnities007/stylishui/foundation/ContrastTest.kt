package com.segnities007.stylishui.foundation

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertTrue
import com.segnities007.stylishui.theme.StylishDarkColorScheme
import com.segnities007.stylishui.theme.StylishHighContrastDarkColorScheme
import com.segnities007.stylishui.theme.StylishHighContrastLightColorScheme
import com.segnities007.stylishui.theme.StylishLightColorScheme

class ContrastTest {
    @Test
    fun blackAndWhiteMeetWcag() {
        assertTrue(stylishMeetsWcagAa(Color.Black, Color.White))
        assertTrue(stylishContrastRatio(Color.Black, Color.White) > 20.0)
    }

    @Test
    fun defaultLightAndDarkActionPairsHaveUsableContrast() {
        listOf(StylishLightColorScheme, StylishDarkColorScheme).forEach { scheme ->
            assertTrue(stylishMeetsWcagAa(scheme.primary, scheme.onPrimary))
            assertTrue(stylishMeetsWcagAa(scheme.surface, scheme.onSurface))
            assertTrue(stylishMeetsWcagAa(scheme.error, scheme.onError))
        }
    }

    @Test
    fun highContrastRolesMeetAaAcrossPrimarySurfaceAndError() {
        listOf(StylishHighContrastLightColorScheme, StylishHighContrastDarkColorScheme).forEach { scheme ->
            listOf(
                scheme.primary to scheme.onPrimary,
                scheme.primaryContainer to scheme.onPrimaryContainer,
                scheme.secondary to scheme.onSecondary,
                scheme.tertiary to scheme.onTertiary,
                scheme.surface to scheme.onSurface,
                scheme.surfaceVariant to scheme.onSurfaceVariant,
                scheme.error to scheme.onError,
                scheme.errorContainer to scheme.onErrorContainer,
            ).forEach { (background, foreground) ->
                assertTrue(
                    stylishMeetsWcagAa(background, foreground),
                    "contrast ratio ${stylishContrastRatio(background, foreground)} is below AA",
                )
            }
        }
    }
}

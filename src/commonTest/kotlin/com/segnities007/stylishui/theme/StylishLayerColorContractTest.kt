package com.segnities007.stylishui.theme

import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Regression tests for the visual surface ladder used by layered components. */
class StylishLayerColorContractTest {
    @Test
    fun observedRangePinsShallowestAndDeepestSurfacesToEndpoints() {
        val registry = StylishLayerRegistry()
        val shallow = Any()
        val deep = Any()
        registry.register(shallow, 2f)
        registry.register(deep, 5f)

        assertEquals(StylishLayerRange(2f, 5f), registry.range)
        assertEquals(0f, registry.range.normalize(2f), absoluteTolerance = 0.0001f)
        assertEquals(1f, registry.range.normalize(5f), absoluteTolerance = 0.0001f)
        assertEquals(0.5f, registry.range.normalize(3.5f), absoluteTolerance = 0.0001f)
    }

    @Test
    fun continuousScaleClampsToItsEndpoints() {
        val bottom = Color(0xFF211D18)
        val top = Color(0xFF564D42)

        assertEquals(bottom, interpolateLayerColor(bottom, top, -1f))
        assertEquals(top, interpolateLayerColor(bottom, top, 2f))
    }

    @Test
    fun continuousScaleIncreasesAcrossArbitraryLevels() {
        val bottom = Color(0xFF211D18)
        val top = Color(0xFF564D42)
        val colors = (0..20).map { step ->
            interpolateLayerColor(bottom, top, step / 20f)
        }

        colors.zipWithNext().forEachIndexed { index, (lower, higher) ->
            assertBrighter("level $index", lower, "level ${index + 1}", higher)
        }
    }

    @Test
    fun desaturationPreservesLightnessAndReducesChroma() {
        val original = Color(0xFFEFE4D3)
        val desaturated = desaturateLayerColor(original, chromaScale = 0.5f)
        val originalOklab = original.convert(ColorSpaces.Oklab)
        val desaturatedOklab = desaturated.convert(ColorSpaces.Oklab)

        assertEquals(originalOklab.red, desaturatedOklab.red, absoluteTolerance = 0.0005f)
        assertTrue(
            oklabChroma(desaturatedOklab) < oklabChroma(originalOklab),
            "desaturation must reduce chroma without changing Oklab lightness",
        )
    }

    @Test
    fun darkContainerRolesIncreaseWithVisualLayer() {
        val scheme = StylishDarkColorScheme

        assertBrighter(
            lowerName = "dark background",
            lower = scheme.background,
            higherName = "dark surfaceContainerLow",
            higher = scheme.surfaceContainerLow,
        )
        assertBrighter(
            lowerName = "dark surfaceContainerLow",
            lower = scheme.surfaceContainerLow,
            higherName = "dark surfaceContainer",
            higher = scheme.surfaceContainer,
        )
        assertBrighter(
            lowerName = "dark surfaceContainer",
            lower = scheme.surfaceContainer,
            higherName = "dark surfaceContainerHigh",
            higher = scheme.surfaceContainerHigh,
        )
        assertBrighter(
            lowerName = "dark surfaceContainerHigh",
            lower = scheme.surfaceContainerHigh,
            higherName = "dark surfaceContainerHighest",
            higher = scheme.surfaceContainerHighest,
        )
    }

    @Test
    fun lightContainerRolesIncreaseWithVisualLayer() {
        val scheme = StylishLightColorScheme

        assertBrighter(
            lowerName = "light background",
            lower = scheme.background,
            higherName = "light surfaceContainerLow",
            higher = scheme.surfaceContainerLow,
        )
        assertBrighter(
            lowerName = "light surfaceContainerLow",
            lower = scheme.surfaceContainerLow,
            higherName = "light surfaceContainer",
            higher = scheme.surfaceContainer,
        )
        assertBrighter(
            lowerName = "light surfaceContainer",
            lower = scheme.surfaceContainer,
            higherName = "light surfaceContainerHigh",
            higher = scheme.surfaceContainerHigh,
        )
        assertBrighter(
            lowerName = "light surfaceContainerHigh",
            lower = scheme.surfaceContainerHigh,
            higherName = "light surfaceContainerHighest",
            higher = scheme.surfaceContainerHighest,
        )
    }

    private fun assertBrighter(
        lowerName: String,
        lower: androidx.compose.ui.graphics.Color,
        higherName: String,
        higher: androidx.compose.ui.graphics.Color,
    ) {
        assertTrue(
            higher.luminance() > lower.luminance(),
            "$higherName must be brighter than $lowerName " +
            "(${higher.luminance()} <= ${lower.luminance()})",
        )
    }

    private fun oklabChroma(color: Color): Float =
        sqrt((color.green * color.green) + (color.blue * color.blue))
}

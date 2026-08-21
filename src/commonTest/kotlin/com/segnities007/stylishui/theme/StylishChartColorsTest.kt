package com.segnities007.stylishui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StylishChartColorsTest {

    @Test
    fun `color blind safe palette is stable and eight colors long`() {
        val palette = lightColorScheme().toStylishColorBlindSafeChartColors().categorical

        assertEquals(8, palette.size)
        assertEquals(Color(0xFF000000), palette.first())
        assertEquals(Color(0xFFCC79A7), palette.last())
        assertEquals(palette.size, palette.toSet().size)
    }

    @Test
    fun `color blind safe palette is independent of material theme colors`() {
        val light = lightColorScheme().toStylishColorBlindSafeChartColors().categorical
        val dark = androidx.compose.material3.darkColorScheme().toStylishColorBlindSafeChartColors().categorical

        assertEquals(light, dark)
        assertTrue(light.all { it.alpha == 1f })
    }
}

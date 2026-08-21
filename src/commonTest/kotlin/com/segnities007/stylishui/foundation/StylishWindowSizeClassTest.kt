package com.segnities007.stylishui.foundation

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class StylishWindowSizeClassTest {
    @Test
    fun widthBoundariesAreStable() {
        assertEquals(StylishWindowWidthSizeClass.Compact, calculateStylishWindowSizeClass(599.dp, 700.dp).widthSizeClass)
        assertEquals(StylishWindowWidthSizeClass.Medium, calculateStylishWindowSizeClass(600.dp, 700.dp).widthSizeClass)
        assertEquals(StylishWindowWidthSizeClass.Medium, calculateStylishWindowSizeClass(839.dp, 700.dp).widthSizeClass)
        assertEquals(StylishWindowWidthSizeClass.Expanded, calculateStylishWindowSizeClass(840.dp, 700.dp).widthSizeClass)
    }

    @Test
    fun heightBoundariesAreStable() {
        assertEquals(StylishWindowHeightSizeClass.Compact, calculateStylishWindowSizeClass(700.dp, 479.dp).heightSizeClass)
        assertEquals(StylishWindowHeightSizeClass.Medium, calculateStylishWindowSizeClass(700.dp, 480.dp).heightSizeClass)
        assertEquals(StylishWindowHeightSizeClass.Medium, calculateStylishWindowSizeClass(700.dp, 899.dp).heightSizeClass)
        assertEquals(StylishWindowHeightSizeClass.Expanded, calculateStylishWindowSizeClass(700.dp, 900.dp).heightSizeClass)
    }

    @Test
    fun customBreakpointsAreRespected() {
        val size = calculateStylishWindowSizeClass(
            width = 500.dp,
            height = 700.dp,
            breakpoints = StylishWindowBreakpoints(compactWidthMax = 400.dp, mediumWidthMax = 700.dp),
        )
        assertEquals(StylishWindowWidthSizeClass.Medium, size.widthSizeClass)
    }
}

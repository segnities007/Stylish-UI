package com.segnities007.stylishui.components.charts

import kotlin.test.Test
import kotlin.test.assertEquals

class ChartFormattersTest {

    @Test
    fun `formatInteger groups by thousands`() {
        assertEquals("0", formatInteger(0))
        assertEquals("12", formatInteger(12))
        assertEquals("1,234", formatInteger(1234))
        assertEquals("1,234,567", formatInteger(1234567))
    }

    @Test
    fun `formatInteger handles negatives`() {
        assertEquals("-1,234", formatInteger(-1234))
    }

    @Test
    fun `formatCompact uses no suffix by default`() {
        assertEquals("1.3", formatCompact(12_500f))
        assertEquals("1.2", formatCompact(12_000f))
        assertEquals("4.5", formatCompact(4_500f))
        assertEquals("850", formatCompact(850f))
    }

    @Test
    fun `formatCompact appends caller-supplied suffix`() {
        assertEquals("1.3万", formatCompact(12_500f, suffix = "万"))
        assertEquals("4.5k", formatCompact(4_500f, suffix = "k"))
    }

    @Test
    fun `formatCompact handles negative values`() {
        assertEquals("-1.2", formatCompact(-12_500f))
    }

    @Test
    fun `formatCompact boundaries between tiers`() {
        assertEquals("1.0", formatCompact(10_000f))
        assertEquals("1.0", formatCompact(1_000f))
        assertEquals("999", formatCompact(999.4f))
        assertEquals("1000", formatCompact(999.9f))
        assertEquals("0", formatCompact(0f))
    }

    @Test
    fun `formatCompact rounds at the boundary with the scaled tier`() {
        assertEquals("10.0", formatCompact(9_999f))
        assertEquals("1.0", formatCompact(10_000f))
    }

    @Test
    fun `formatDecimal pads fractional digits`() {
        assertEquals("12.50", 12.5f.formatDecimal(2))
        assertEquals("1.25", 1.25f.formatDecimal(2))
        assertEquals("3", 3.2f.formatDecimal(0))
    }
}

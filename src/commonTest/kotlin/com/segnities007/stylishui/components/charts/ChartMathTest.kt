package com.segnities007.stylishui.components.charts

import kotlin.test.Test
import kotlin.test.assertEquals

class ChartMathTest {

    @Test
    fun `pieSweepAngles is empty for empty input`() {
        assertEquals(emptyList(), pieSweepAngles(emptyList()))
    }

    @Test
    fun `pieSweepAngles divides proportionally and sums to 360`() {
        val sweeps = pieSweepAngles(listOf(25f, 75f))
        assertEquals(90f, sweeps[0], absoluteTolerance = 0.001f)
        assertEquals(270f, sweeps[1], absoluteTolerance = 0.001f)
        assertEquals(360f, sweeps.sum(), absoluteTolerance = 0.001f)
    }

    @Test
    fun `pieSweepAngles treats negative values as zero`() {
        val sweeps = pieSweepAngles(listOf(-10f, 10f))
        assertEquals(0f, sweeps[0])
        assertEquals(360f, sweeps[1], absoluteTolerance = 0.001f)
    }

    @Test
    fun `pieSweepAngles treats non-finite values as zero`() {
        val sweeps = pieSweepAngles(listOf(Float.NaN, Float.POSITIVE_INFINITY, 10f, 10f))
        assertEquals(0f, sweeps[0])
        assertEquals(0f, sweeps[1])
        assertEquals(180f, sweeps[2], absoluteTolerance = 0.001f)
        assertEquals(180f, sweeps[3], absoluteTolerance = 0.001f)
    }

    @Test
    fun `pieSweepAngles returns all zeros when no valid slices`() {
        assertEquals(listOf(0f, 0f, 0f), pieSweepAngles(listOf(0f, -5f, Float.NaN)))
    }

    @Test
    fun `barHeight is proportional to value and usable height`() {
        assertEquals(50f, barHeight(5f, 10f, 100f))
        assertEquals(100f, barHeight(10f, 10f, 100f))
    }

    @Test
    fun `barHeight clamps negative and zero values to zero`() {
        assertEquals(0f, barHeight(0f, 10f, 100f))
        assertEquals(0f, barHeight(-5f, 10f, 100f))
    }

    @Test
    fun `barHeight treats non-finite values as zero`() {
        assertEquals(0f, barHeight(Float.NaN, 10f, 100f))
        assertEquals(0f, barHeight(Float.POSITIVE_INFINITY, 10f, 100f))
        assertEquals(0f, barHeight(Float.NEGATIVE_INFINITY, 10f, 100f))
    }

    @Test
    fun `barScaleMax returns maximum finite value`() {
        assertEquals(10f, barScaleMax(listOf(3f, 10f, 5f)))
    }

    @Test
    fun `barScaleMax returns one for empty input`() {
        assertEquals(1f, barScaleMax(emptyList()))
    }

    @Test
    fun `barScaleMax returns one for all-negative input`() {
        assertEquals(1f, barScaleMax(listOf(-3f, -5f)))
    }

    @Test
    fun `barScaleMax excludes non-finite values`() {
        assertEquals(5f, barScaleMax(listOf(Float.NaN, 5f)))
        assertEquals(1f, barScaleMax(listOf(Float.NaN)))
    }

    @Test
    fun `barScaleMax never drops below one`() {
        assertEquals(1f, barScaleMax(listOf(0.5f, 0.2f)))
    }

    @Test
    fun `lineScaleRange pads with ten percent and floors at zero for non-negative data`() {
        val range = lineScaleRange(listOf(10f, 20f))
        assertEquals(9f, range.start)
        assertEquals(21f, range.endInclusive)
    }

    @Test
    fun `lineScaleRange returns unit range for empty input`() {
        val range = lineScaleRange(emptyList())
        assertEquals(0f, range.start)
        assertEquals(1f, range.endInclusive)
    }

    @Test
    fun `lineScaleRange expands single-value datasets`() {
        val range = lineScaleRange(listOf(5f))
        assertEquals(4.9f, range.start, absoluteTolerance = 0.0001f)
        assertEquals(5.1f, range.endInclusive, absoluteTolerance = 0.0001f)
    }

    @Test
    fun `lineScaleRange supports negative values with padding on both ends`() {
        val range = lineScaleRange(listOf(-10f, -5f))
        assertEquals(-10.5f, range.start, absoluteTolerance = 0.0001f)
        assertEquals(-4.5f, range.endInclusive, absoluteTolerance = 0.0001f)
    }

    @Test
    fun `lineScaleRange excludes non-finite values`() {
        val range = lineScaleRange(listOf(Float.NaN, 5f, 10f))
        assertEquals(4.5f, range.start, absoluteTolerance = 0.0001f)
        assertEquals(10.5f, range.endInclusive, absoluteTolerance = 0.0001f)
    }

    @Test
    fun `lineNormalized maps values into zero-to-one`() {
        assertEquals(listOf(0f, 0.5f, 1f), lineNormalized(listOf(0f, 5f, 10f), 0f, 10f))
    }

    @Test
    fun `lineNormalized is empty for empty input`() {
        assertEquals(emptyList(), lineNormalized(emptyList(), 0f, 10f))
    }

    @Test
    fun `lineNormalized handles a single value range`() {
        assertEquals(listOf(0.5f), lineNormalized(listOf(5f), 5f, 5f))
    }

    @Test
    fun `lineNormalized handles negative values within range`() {
        assertEquals(listOf(0f, 1f), lineNormalized(listOf(-5f, 5f), -5f, 5f))
    }

    @Test
    fun `lineNormalized clamps out-of-range values`() {
        assertEquals(listOf(1f), lineNormalized(listOf(20f), 0f, 10f))
        assertEquals(listOf(0f), lineNormalized(listOf(-3f), 0f, 10f))
    }

    @Test
    fun `lineNormalized treats non-finite values as zero`() {
        assertEquals(listOf(0f, 0f), lineNormalized(listOf(Float.NaN, Float.NEGATIVE_INFINITY), 0f, 10f))
    }
}

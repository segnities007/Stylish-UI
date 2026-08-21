package com.segnities007.stylishui.components.charts

import kotlin.math.roundToInt

/**
 * Upper bound for points emitted by a Canvas renderer in one chart frame.
 *
 * This is an allocation/drawing guard, not a claim about frame time. Callers that need every
 * sample for inspection should keep the original data for semantics and interaction, while the
 * renderer uses [downsampleStylishSeries] for the visual path.
 */
internal const val StylishChartMaxRenderedPoints: Int = 500

/**
 * Selects a deterministic, endpoint-preserving visual window from an ordered series.
 *
 * The function performs no timing assumptions and always returns at most [maxPoints] values. The
 * first and last source values are retained when downsampling, so axes and trends do not lose the
 * boundaries of a dataset. Equal source indexes cannot be emitted twice.
 */
internal fun <T> downsampleStylishSeries(points: List<T>, maxPoints: Int): List<T> {
    require(maxPoints > 0) { "maxPoints must be positive" }
    if (points.size <= maxPoints) return points
    if (maxPoints == 1) return listOf(points.first())
    val lastIndex = points.lastIndex
    return buildList(maxPoints) {
        var previousIndex = -1
        for (slot in 0 until maxPoints) {
            val sourceIndex = (slot * lastIndex.toDouble() / (maxPoints - 1)).roundToInt()
                .coerceIn(0, lastIndex)
            if (sourceIndex != previousIndex) add(points[sourceIndex])
            previousIndex = sourceIndex
        }
    }
}

/**
 * Builds the compact accessibility description shared by categorical charts.
 *
 * Invalid numeric samples are deliberately omitted from the spoken output,
 * while every finite source sample is retained even when the visual renderer
 * downsamples the path. Keeping this as a pure function makes the visual
 * point budget and the accessibility data contract independently testable.
 */
internal fun buildStylishChartDescription(
    prefix: String,
    points: Iterable<Pair<String, Float>>,
): String = buildString {
    append(prefix)
    points.forEach { (label, value) ->
        if (value.isFinite()) append(". $label=$value")
    }
}

/**
 * Converts raw slice values into sweep angles normalized to a total of 360°.
 *
 * Negative and non-finite values (`NaN`, `±Infinity`) are treated as zero
 * and contribute no arc. The total is computed from the valid (positive,
 * finite) values only, so a dataset containing invalid entries still
 * normalizes correctly to a full circle. When no valid value exists the
 * result is a list of zeros of the same size as [values].
 *
 * @param values Raw slice magnitudes in the caller's units.
 * @return One sweep angle in degrees per input value, summing to 360° when
 *   at least one value is positive and finite.
 */
internal fun pieSweepAngles(values: List<Float>): List<Float> {
    if (values.isEmpty()) return emptyList()
    val total = values.filter { it.isFinite() && it > 0f }.sum()
    if (total <= 0f) return List(values.size) { 0f }
    return values.map { value ->
        if (value.isFinite() && value > 0f) (value / total) * 360f else 0f
    }
}

/**
 * Computes the rendered height of a bar (or stacked segment) in pixels.
 *
 * Negative, zero, and non-finite values ([NaN], [Float.POSITIVE_INFINITY],
 * [Float.NEGATIVE_INFINITY]) all produce zero height, and a non-positive
 * [maxValue] also yields zero — the chart floor is always at zero.
 *
 * @param value The data value to scale. Coerced to `>= 0f`.
 * @param maxValue The scale denominator (e.g. from [barScaleMax]).
 * @param usableHeight The vertical pixel space available for bars.
 * @return `value / maxValue * usableHeight`, or `0f` for invalid inputs.
 */
internal fun barHeight(value: Float, maxValue: Float, usableHeight: Float): Float {
    if (!value.isFinite() || value <= 0f || maxValue <= 0f) return 0f
    return (value / maxValue) * usableHeight
}

/**
 * Computes the scale denominator for a bar chart's Y axis.
 *
 * Only finite values participate in the maximum; `NaN`/`Infinity` entries
 * are ignored so a single corrupt value cannot poison the scale. The result
 * is never below `1f` (so empty, all-negative, or all-invalid datasets still
 * produce a sane grid).
 *
 * @param values Raw bar magnitudes.
 * @return The largest finite value, coerced to be at least `1f`.
 */
internal fun barScaleMax(values: List<Float>): Float {
    val maxValue = values.filter { it.isFinite() }.maxOrNull() ?: return 1f
    return maxValue.coerceAtLeast(1f)
}

/**
 * Normalizes values into the `0..1` range relative to a given axis range.
 *
 * Non-finite values normalize to `0f`; results are clamped into `0..1` so
 * out-of-range inputs never produce coordinates outside the chart. When the
 * range has no width ([maxValue] <= [minValue]) every finite value maps to
 * `0.5f` — the vertical center — rather than dividing by zero.
 *
 * @param values Raw values in the same units as the axis range.
 * @param minValue Lower axis bound (typically [ClosedFloatingPointRange.start]
 *   from [lineScaleRange]).
 * @param maxValue Upper axis bound (typically
 *   [ClosedFloatingPointRange.endInclusive] from [lineScaleRange]).
 * @return One normalized fraction per input value.
 */
internal fun lineNormalized(values: List<Float>, minValue: Float, maxValue: Float): List<Float> {
    val span = maxValue - minValue
    if (span <= 0f) return List(values.size) { 0.5f }
    return values.map { value ->
        if (!value.isFinite()) 0f
        else ((value - minValue) / span).coerceIn(0f, 1f)
    }
}

/**
 * Computes the Y-axis scale range for a line chart with 10% padding.
 *
 * The span between the smallest and largest finite values is padded by 10 %
 * on each end. For non-negative data the floor is clamped to zero so no
 * misleading negative ticks appear; negative data keeps its negative floor.
 * Non-finite values are excluded from the min/max computation, and empty or
 * all-invalid input yields the unit range `0f..1f`.
 *
 * @param values Raw point values.
 * @return The padded axis range, `floor .. ceiling`.
 */
internal fun lineScaleRange(values: List<Float>): ClosedFloatingPointRange<Float> {
    val finite = values.filter { it.isFinite() }
    if (finite.isEmpty()) return 0f..1f
    val minValue = finite.min()
    val maxValue = finite.max()
    val span = (maxValue - minValue).coerceAtLeast(1f)
    val padding = span * 0.1f
    val axisMin = if (minValue >= 0f) (minValue - padding).coerceAtLeast(0f) else minValue - padding
    return axisMin..(maxValue + padding)
}

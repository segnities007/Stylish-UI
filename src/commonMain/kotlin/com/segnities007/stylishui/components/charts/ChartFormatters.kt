package com.segnities007.stylishui.components.charts

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Formats an integer with comma-separated thousands grouping.
 *
 * Negative values are prefixed with a minus sign. The grouping uses
 * three-digit chunks (e.g. `1234567` → `"1,234,567"`). Used internally
 * by chart composables to build accessibility descriptions and axis labels.
 *
 * @param value The integer to format. May be negative.
 * @return A locale-independent string with comma grouping and no decimal
 *   places.
 * @see formatCompact
 */
internal fun formatInteger(value: Int): String {
    val absValue = kotlin.math.abs(value)
    val sign = if (value < 0) "-" else ""
    val digits = absValue.toString()
    val grouped = digits.reversed().chunked(3).joinToString(",").reversed()
    return "$sign$grouped"
}

/**
 * Formats a floating-point value into a compact, human-readable string
 * using magnitude-based unit suffixes.
 *
 * The output tier is selected by absolute value:
 * - `≥ 10 000` → divided by 10 000, suffixed with "万" (one decimal place).
 * - `≥ 1 000` → divided by 1 000, suffixed with "k" (one decimal place).
 * - Otherwise → rounded to a whole number with no suffix.
 *
 * This keeps axis labels short regardless of the data magnitude. Used by
 * [SimpleBarChart] grid-line labels.
 *
 * @param value The numeric value to format. May be negative.
 * @return A compact string such as `"3.5万"`, `"12.0k"`, or `"850"`.
 * @see formatInteger
 */
internal fun formatCompact(value: Float): String {
    val absValue = kotlin.math.abs(value)
    return when {
        absValue >= 10_000 -> "${(value / 10_000).formatDecimal(1)}万"
        absValue >= 1_000 -> "${(value / 1_000).formatDecimal(1)}k"
        else -> "${value.formatDecimal(0)}"
    }
}

private fun Float.formatDecimal(decimals: Int): String {
    if (decimals <= 0) return this.roundToInt().toString()
    val multiplier = 10.0.pow(decimals.toDouble()).toFloat()
    val rounded = kotlin.math.round(this * multiplier) / multiplier
    return rounded.toString().padEnd(decimals + 2, '0')
}

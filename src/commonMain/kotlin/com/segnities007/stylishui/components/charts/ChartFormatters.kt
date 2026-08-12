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
 * Formats a floating-point value into a compact, magnitude-scaled string.
 *
 * The output tier is selected by absolute value:
 * - `≥ 10 000` → divided by 10 000 (one decimal place).
 * - `≥ 1 000` → divided by 1 000 (one decimal place).
 * - Otherwise → rounded to a whole number.
 *
 * No unit suffix is hard-coded: a caller-supplied [suffix] (e.g. `"k"`,
 * `"万"`) is appended verbatim to the scaled number, and the default empty
 * suffix yields bare scaled values such as `"1.3"` for `12_500` or `"850"`
 * for `850`. This keeps axis labels short regardless of the data magnitude
 * while leaving the unit choice to the caller.
 *
 * All arithmetic and rounding are performed in invariant, locale-neutral
 * Kotlin, so the output never varies by device locale. Used by
 * [SimpleBarChart] grid-line labels.
 *
 * @param value The numeric value to format. May be negative.
 * @param suffix Optional unit string appended to the scaled number.
 *   Defaults to `""` (no suffix).
 * @return A compact string such as `"3.5"`, `"12.0"`, or `"850"` — with
 *   [suffix] appended when provided (e.g. `"3.5万"` for `suffix = "万"`).
 * @see formatInteger
 */
internal fun formatCompact(value: Float, suffix: String = ""): String {
    val absValue = kotlin.math.abs(value)
    return when {
        absValue >= 10_000 -> "${(value / 10_000).formatDecimal(1)}$suffix"
        absValue >= 1_000 -> "${(value / 1_000).formatDecimal(1)}$suffix"
        else -> "${value.formatDecimal(0)}$suffix"
    }
}

/**
 * Rounds [this] to the requested number of decimal places and formats it
 * without locale-dependent separators.
 *
 * The output always carries exactly [decimals] fractional digits and is
 * produced via `Float.toString()`, which is invariant across locales. Ties
 * are rounded half-up (e.g. `1.25` → `"1.3"`). Used by [formatCompact] and
 * by chart axis labels that need fixed-precision, locale-neutral text.
 *
 * @param decimals Number of fractional digits. When zero or negative the
 *   value is rounded to a whole number and formatted without a decimal
 *   point.
 * @return Locale-neutral decimal string such as `"12.50"` or `"3"`.
 */
internal fun Float.formatDecimal(decimals: Int): String {
    if (decimals <= 0) return this.roundToInt().toString()
    val multiplier = 10.0.pow(decimals.toDouble()).toInt()
    val scaled = kotlin.math.floor(this * multiplier + 0.5f).toInt()
    val integerPart = scaled / multiplier
    val fraction = kotlin.math.abs(scaled % multiplier)
    return "$integerPart.${fraction.toString().padStart(decimals, '0')}"
}

package com.segnities007.stylishui.components.charts

import kotlin.math.pow
import kotlin.math.roundToInt

/** 整数を 3 桁区切りのカンマ付き文字列にフォーマットする。 */
internal fun formatInteger(value: Int): String {
    val absValue = kotlin.math.abs(value)
    val sign = if (value < 0) "-" else ""
    val digits = absValue.toString()
    val grouped = digits.reversed().chunked(3).joinToString(",").reversed()
    return "$sign$grouped"
}

/** 浮動小数点数を万/k 単位のコンパクトな文字列にフォーマットする。 */
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

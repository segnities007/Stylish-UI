package com.segnities007.stylishui.foundation

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/** Returns the WCAG 2.2 contrast ratio between two colors, from 1 to 21. */
public fun stylishContrastRatio(first: Color, second: Color): Double {
    val firstLuminance = first.relativeLuminance()
    val secondLuminance = second.relativeLuminance()
    val lighter = max(firstLuminance, secondLuminance)
    val darker = min(firstLuminance, secondLuminance)
    return (lighter + 0.05) / (darker + 0.05)
}

/** Whether normal text meets WCAG AA (4.5:1). */
public fun stylishMeetsWcagAa(first: Color, second: Color): Boolean = stylishContrastRatio(first, second) >= 4.5

private fun Color.relativeLuminance(): Double {
    fun linear(channel: Float): Double {
        val value = channel.toDouble()
        return if (value <= 0.04045) value / 12.92 else ((value + 0.055) / 1.055).pow(2.4)
    }
    return 0.2126 * linear(red) + 0.7152 * linear(green) + 0.0722 * linear(blue)
}

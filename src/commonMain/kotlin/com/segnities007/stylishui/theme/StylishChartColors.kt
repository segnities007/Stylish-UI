package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Categorical color palette for chart and data-visualization components.
 *
 * Provides a sequence of visually distinct colors derived from the active [ColorScheme],
 * ensuring charts remain on-brand and adapt to light/dark themes automatically. Obtain an
 * instance via [MaterialTheme.stylishChartColors] or [ColorScheme.toStylishChartColors].
 *
 * @property categorical An ordered list of colors intended for assigning to chart series or
 *   categories. The ordering prioritizes perceptual distinctiveness: primary, tertiary,
 *   secondary, error, onSurfaceVariant, outline.
 * @see MaterialTheme.stylishChartColors
 * @see ColorScheme.toStylishChartColors
 */
@Immutable
public data class StylishChartColors(public val categorical: List<Color>)

/**
 * Derives a [StylishChartColors] palette from this [ColorScheme].
 *
 * Selects six scheme roles — `primary`, `tertiary`, `secondary`, `error`,
 * `onSurfaceVariant`, and `outline` — in an order that maximizes hue separation for
 * adjacent chart segments.
 *
 * @return A [StylishChartColors] whose [StylishChartColors.categorical] list contains the
 *   selected colors.
 * @see MaterialTheme.stylishChartColors
 */
public fun ColorScheme.toStylishChartColors(): StylishChartColors = StylishChartColors(
    categorical = listOf(primary, tertiary, secondary, error, onSurfaceVariant, outline),
)

/**
 * Returns the Okabe–Ito categorical palette for charts.
 *
 * The palette is intentionally independent from the active Material color
 * scheme: its hues were selected to remain distinguishable for the most
 * common forms of red/green colour-vision deficiency. Use this palette when
 * a chart has several adjacent series or when the chart is exported outside
 * the themed application. The returned colors are opaque and can therefore
 * be paired with a separate alpha for area fills.
 *
 * This is an opt-in alternative to [toStylishChartColors] so existing charts
 * retain their current branded colors by default.
 */
public fun ColorScheme.toStylishColorBlindSafeChartColors(): StylishChartColors = StylishChartColors(
    categorical = listOf(
        Color(0xFF000000), // black
        Color(0xFFE69F00), // orange
        Color(0xFF56B4E9), // sky blue
        Color(0xFF009E73), // bluish green
        Color(0xFFF0E442), // yellow
        Color(0xFF0072B2), // blue
        Color(0xFFD55E00), // vermillion
        Color(0xFFCC79A7), // reddish purple
    ),
)

/**
 * Convenience accessor that computes [StylishChartColors] from the current
 * [MaterialTheme.colorScheme].
 *
 * Use inside any composable to obtain chart colors that automatically reflect the active theme.
 *
 * @see StylishChartColors
 * @see ColorScheme.toStylishChartColors
 */
public val MaterialTheme.stylishChartColors: StylishChartColors
    @Composable get() = colorScheme.toStylishChartColors()

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

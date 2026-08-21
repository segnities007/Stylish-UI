package com.segnities007.stylishui.components.charts

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.stylishChartColors

/**
 * A single slice of a pie or donut chart, pairing a categorical label with
 * its numeric magnitude and display color.
 *
 * Instances are consumed by [SimplePieChart] which computes each slice's
 * angular sweep proportionally from the sum of all values.
 *
 * @property label Human-readable category name used in accessibility
 *   descriptions and legends.
 * @property value Numeric magnitude of this slice. The arc angle is derived
 *   as `value / totalSum * 360°`. Negative or non-finite values (`NaN`,
 *   `±Infinity`) are treated as zero and contribute no arc.
 * @property color Fill color of the arc segment. Typically obtained from
 *   [stylishChartColor] to stay consistent with the theme's categorical
 *   palette.
 * @see SimplePieChart
 * @see stylishChartColor
 */
@Immutable
public data class PieChartData(
    val label: String,
    val value: Float,
    val color: Color,
)

/**
 * A lightweight donut-style pie chart rendered entirely on a [Canvas].
 *
 * Each [PieChartData] entry contributes an arc whose sweep angle is
 * proportional to its value relative to the total sum. A circular hole is
 * punched in the center (controlled by [holeRatio]) to produce the donut
 * appearance. When [data] is empty or all values are zero, negative, or
 * non-finite, a neutral skeleton ring is drawn instead so the layout space
 * is preserved.
 *
 * The chart exposes a combined [contentDescription] for accessibility
 * services, built from [contentDescriptionPrefix] followed by each
 * label–value pair (always using the original values).
 *
 * On first composition the slices animate their sweep angles from zero to
 * their final proportions (see [animate]). The animation is drawn with
 * Compose's [Animatable] and reads the animated value inside the draw
 * scope, so no platform-specific rendering APIs are involved.
 *
 * This composable is available on all platforms (commonMain).
 *
 * @param data The slices to render. Colors are typically sourced via
 *   [stylishChartColor].
 * @param contentDescriptionPrefix Leading text for the accessibility
 *   description (e.g. "Expense breakdown").
 * @param modifier Modifier applied to the outer [Canvas].
 * @param chartSize Diameter of the chart. Defaults to
 *   [StylishTheme.dimensions.pieChartSize] (160 dp).
 * @param holeRatio Radius of the center hole as a fraction of [chartSize].
 *   Defaults to 0.3.
 * @param skeletonRatio Radius of the skeleton ring shown when data is empty,
 *   as a fraction of [chartSize]. Defaults to 0.4.
 * @param holeColor Fill color of the center hole. Defaults to
 *   `MaterialTheme.colorScheme.surface`.
 * @param skeletonColor Fill color of the skeleton ring displayed in the
 *   empty state. Defaults to `MaterialTheme.colorScheme.outlineVariant`.
 * @param startAngle Angle in degrees where the first slice begins, measured
 *   clockwise from 3 o'clock. Defaults to -90 (top of the chart).
 * @param animate When `true`, slices animate their sweep angles from zero
 *   on first composition using `tween(StylishTheme.animation.durationMedium)`.
 *   When `false`, the chart appears instantly. Defaults to `true`.
 * @see PieChartData
 * @see stylishChartColor
 * @see SimpleBarChart
 * @see SimpleLineChart
 */
@Composable
public fun SimplePieChart(
    data: List<PieChartData>,
    contentDescriptionPrefix: String,
    modifier: Modifier = Modifier,
    chartSize: Dp = StylishTheme.dimensions.pieChartSize,
    holeRatio: Float = 0.3f,
    skeletonRatio: Float = 0.4f,
    holeColor: Color = MaterialTheme.colorScheme.surface,
    skeletonColor: Color = MaterialTheme.colorScheme.outlineVariant,
    startAngle: Float = -90f,
    animate: Boolean = true,
) {
    val sweepAngles = pieSweepAngles(data.map { it.value })
    val strings = StylishTheme.strings
    val hasSlices = data.isNotEmpty() && sweepAngles.any { it > 0f }
    val description = data.joinToString(", ") {
        "${it.label}: ${strings.formatInteger(it.value.toLong())}"
    }
    val progress = remember { Animatable(0f) }
    val animationDuration = StylishTheme.animation.durationMedium
    val shouldAnimate = animate && !isStylishReducedMotionEnabled()
    LaunchedEffect(Unit) {
        if (shouldAnimate) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(animationDuration),
            )
        }
        else {
            progress.snapTo(1f)
        }
    }

    Canvas(
        modifier = modifier
            .size(chartSize)
            .semantics { contentDescription = "$contentDescriptionPrefix: $description" },
    ) {
        val animationProgress = progress.value
        if (!hasSlices) {
            drawCircle(
                color = skeletonColor,
                radius = chartSize.toPx() * skeletonRatio,
            )
            drawCircle(
                color = holeColor,
                radius = chartSize.toPx() * holeRatio,
            )
        }
        else {
            var currentAngle = startAngle
            sweepAngles.forEachIndexed { index, sweep ->
                val animatedSweep = sweep * animationProgress
                drawArc(
                    color = data[index].color,
                    startAngle = currentAngle,
                    sweepAngle = animatedSweep,
                    useCenter = true,
                )
                currentAngle += animatedSweep
            }
            drawCircle(
                color = holeColor,
                radius = chartSize.toPx() * holeRatio,
            )
        }
    }
}

/**
 * Returns a color from the theme's categorical chart palette by index.
 *
 * The palette wraps cyclically: if [index] exceeds the palette size the
 * lookup wraps around via modulo, so callers never need to bounds-check.
 * Negative indices are not supported and will throw IndexOutOfBoundsException.
 * Use this to assign consistent, theme-aware colors to chart series or
 * pie slices without hard-coding hex values.
 *
 * @param index Zero-based position in the categorical palette. Values
 *   beyond the palette length wrap around.
 * @return The [Color] at the resolved palette position.
 * @see PieChartData
 * @see SimplePieChart
 */
@Composable
public fun stylishChartColor(index: Int): Color {
    val colors = MaterialTheme.stylishChartColors.categorical
    return colors[index % colors.size]
}

@Preview(name = "Simple pie chart", showBackground = true, widthDp = 393)
@Composable
private fun SimplePieChartPreview() {
    MaterialTheme {
        SimplePieChart(
            contentDescriptionPrefix = "円グラフ",
            data = listOf(
                PieChartData("燃料費", 35000f, stylishChartColor(0)),
                PieChartData("保険", 15000f, stylishChartColor(1)),
                PieChartData("メンテナンス", 8000f, stylishChartColor(2)),
            ),
        )
    }
}

@Preview(name = "Simple pie chart (empty)", showBackground = true, widthDp = 393)
@Composable
private fun SimplePieChartEmptyPreview() {
    MaterialTheme {
        SimplePieChart(
            data = emptyList(),
            contentDescriptionPrefix = "円グラフ",
        )
    }
}

package com.segnities007.stylishui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled

/**
 * A single segment within a stacked bar, representing one sub-category's
 * contribution to the bar's total height.
 *
 * Segments are drawn bottom-to-top in list order. Only the topmost non-zero
 * segment receives rounded top corners; all others are drawn as plain
 * rectangles so the stack appears seamless.
 *
 * @property value Numeric magnitude of this segment. Its rendered height is
 *   proportional to `value / chartMaxValue`. Negative or non-finite values
 *   (`NaN`, `±Infinity`) are treated as zero and render no height.
 * @property color Fill color for this segment's rectangle. Typically
 *   obtained from [stylishChartColor] for palette consistency.
 * @see BarChartData
 * @see SimpleBarChart
 */
@Immutable
public data class BarChartSegment(
    public val value: Float,
    public val color: Color,
)

/**
 * Data for a single bar (category) in a [SimpleBarChart].
 *
 * When [segments] is empty the bar is drawn as a single solid rectangle
 * using the chart-level `barColor`. When segments are provided, each segment
 * is drawn in its own color; the total visual height equals the sum of
 * segment values. Callers should ensure segments sum to [value] for
 * consistent scaling with non-stacked bars.
 *
 * @property label Category name rendered below the bar on the X axis and
 *   included in the accessibility description.
 * @property value Total magnitude that determines the bar's height relative
 *   to the chart's maximum value. Negative or non-finite values render as a
 *   zero-height bar; the accessibility description always shows the original
 *   value.
 * @property segments Optional stacked sub-divisions. When non-empty, each
 *   [BarChartSegment] is drawn in order from the base upward. Defaults to
 *   an empty list (single-color bar).
 * @see BarChartSegment
 * @see SimpleBarChart
 */
@Immutable
public data class BarChartData(
    public val label: String,
    public val value: Float,
    public val segments: List<BarChartSegment> = emptyList(),
)

/**
 * A vertical bar chart with optional stacked segments, rendered on a
 * Compose [Canvas] for lightweight, dependency-free drawing.
 *
 * Each [BarChartData] entry produces one bar whose height is proportional
 * to its value relative to the dataset maximum. Horizontal grid lines with
 * magnitude-scaled axis labels (via [formatCompact], without unit suffixes)
 * are drawn on the left. When [data] is empty, [emptyLabel] is centered in
 * the chart area.
 *
 * Bars support two modes:
 * - **Single-color** — when `BarChartData.segments` is empty, the bar is
 *   filled with [barColor] and given rounded corners ([topRadius]) on all four sides.
 * - **Stacked** — when segments are present, each is drawn in its own
 *   color; only the topmost non-zero segment is rounded.
 *
 * Extreme values are handled defensively: negative, `NaN`, and infinite
 * values render as zero-height bars (the chart floor is always zero), while
 * the accessibility description always reflects the original values.
 *
 * On first composition bars animate their height from zero to the target
 * value (see [animate]). Text layouts (category labels, grid values, and the
 * empty label) are measured once per data change in composition and reused
 * inside the draw scope, so frames do not re-measure text.
 *
 * This composable is available on all platforms (commonMain). Text is drawn
 * with Compose's common [rememberTextMeasurer] + [drawText] APIs.
 *
 * @param data The categories to render as bars.
 * @param contentDescriptionPrefix Leading text for the combined accessibility
 *   description (e.g. "Monthly expenses").
 * @param emptyLabel Text displayed at the chart center when [data] is empty.
 * @param modifier Modifier applied to the outer [Canvas].
 * @param barColor Fill color for single-color (non-stacked) bars. Defaults
 *   to `MaterialTheme.colorScheme.primary`.
 * @param gridColor Color of horizontal grid lines. Defaults to
 *   `MaterialTheme.colorScheme.outlineVariant`.
 * @param chartHeight Total height of the chart area. Defaults to
 *   [StylishTheme.dimensions.barChartHeight].
 * @param topRadius Corner radius applied to the top of each bar (or the
 *   topmost segment in stacked mode). Defaults to 4 dp.
 * @param labelTextSize Text size for axis and category labels. Defaults to
 *   10 dp.
 * @param gridLineCount Number of horizontal grid lines including the
 *   baseline. Defaults to 4.
 * @param animate When `true`, bars animate their height from zero on first
 *   composition using `tween(StylishTheme.animation.durationMedium)`.
 *   When `false`, the chart appears instantly. Defaults to `true`.
 * @see BarChartData
 * @see BarChartSegment
 * @see SimplePieChart
 * @see SimpleLineChart
 */
@Composable
public fun SimpleBarChart(
    data: List<BarChartData>,
    contentDescriptionPrefix: String,
    emptyLabel: String,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    chartHeight: Dp = StylishTheme.dimensions.barChartHeight,
    topRadius: Dp = 4.dp,
    labelTextSize: Dp = 10.dp,
    gridLineCount: Int = 4,
    animate: Boolean = true,
) {
    val maxValue = barScaleMax(data.map { it.value })
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strings = StylishTheme.strings
    val scaledLabelTextSize = labelTextSize * LocalDensity.current.fontScale
    val description = "$contentDescriptionPrefix: " + data.joinToString(", ") {
        "${it.label}=${strings.formatInteger(it.value.toLong())}"
    }
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        fontSize = scaledLabelTextSize.value.sp,
        color = labelColor,
    )
    val labelLayouts = remember(data, scaledLabelTextSize, labelColor) {
        data.map { textMeasurer.measure(it.label, labelStyle) }
    }
    val gridLayouts = remember(data, gridLineCount, scaledLabelTextSize, labelColor, maxValue) {
        List(gridLineCount) { i ->
            val gridValue = maxValue * (gridLineCount - 1 - i) / (gridLineCount - 1).coerceAtLeast(1)
            textMeasurer.measure(formatCompact(gridValue), labelStyle)
        }
    }
    val emptyLayout = remember(emptyLabel, scaledLabelTextSize, labelColor) {
        textMeasurer.measure(emptyLabel, labelStyle)
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
            .fillMaxWidth()
            .height(chartHeight)
            .semantics { contentDescription = description },
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val bottomPadding = 28.dp.toPx()
        val topPadding = 8.dp.toPx()
        val leftPadding = 40.dp.toPx()
        val usableWidth = chartWidth - leftPadding
        val usableHeight = chartHeight - bottomPadding - topPadding
        val animationProgress = progress.value

        for (i in 0 until gridLineCount) {
            val y = topPadding + usableHeight * i / (gridLineCount - 1).coerceAtLeast(1)
            drawLine(
                color = gridColor,
                start = Offset(leftPadding, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f,
            )
            val layout = gridLayouts[i]
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(
                    2.dp.toPx(),
                    y - layout.size.height / 2f,
                ),
            )
        }

        if (data.isEmpty()) {
            drawText(
                textLayoutResult = emptyLayout,
                topLeft = Offset(
                    (chartWidth - emptyLayout.size.width) / 2f,
                    (chartHeight - emptyLayout.size.height) / 2f,
                ),
            )
        }
        else {
            val resolvedTopRadius = topRadius.toPx()
            data.forEachIndexed { index, d ->
                val barHeightValue = barHeight(d.value, maxValue, usableHeight) * animationProgress
                val barWidth = usableWidth / (data.size * 2f + 1)
                val spacing = barWidth
                val x = leftPadding + spacing + index * (barWidth + spacing)
                val y = chartHeight - bottomPadding - barHeightValue

                if (d.segments.isEmpty()) {
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeightValue),
                        cornerRadius = CornerRadius(resolvedTopRadius, resolvedTopRadius),
                    )
                }
                else {
                    val lastNonZero = d.segments.indexOfLast { it.value > 0f }
                    var accumulated = 0f
                    d.segments.forEachIndexed { segIdx, seg ->
                        val segHeight = barHeight(seg.value, maxValue, usableHeight) * animationProgress
                        if (segHeight > 0f) {
                            val segTop = chartHeight - bottomPadding - accumulated - segHeight
                            if (segIdx == lastNonZero) {
                                drawTopRoundedRect(
                                    color = seg.color,
                                    left = x,
                                    top = segTop,
                                    width = barWidth,
                                    height = segHeight,
                                    radius = resolvedTopRadius,
                                )
                            }
                            else {
                                drawRect(
                                    color = seg.color,
                                    topLeft = Offset(x, segTop),
                                    size = Size(barWidth, segHeight),
                                )
                            }
                        }
                        accumulated += segHeight
                    }
                }

                val layout = labelLayouts[index]
                drawText(
                    textLayoutResult = layout,
                    topLeft = Offset(
                        x + (barWidth - layout.size.width) / 2f,
                        chartHeight - 8.dp.toPx() - layout.size.height,
                    ),
                )
            }
        }
    }
}

@Preview(name = "Simple bar chart", showBackground = true, widthDp = 393)
@Composable
private fun SimpleBarChartPreview() {
    MaterialTheme {
        SimpleBarChart(
            contentDescriptionPrefix = "棒グラフ",
            emptyLabel = "データがありません",
            data = listOf(
                BarChartData("1月", 30000f),
                BarChartData("2月", 45000f),
                BarChartData("3月", 28000f),
                BarChartData("4月", 52000f),
                BarChartData("5月", 41000f),
            ),
        )
    }
}

@Preview(name = "Simple bar chart (empty)", showBackground = true, widthDp = 393)
@Composable
private fun SimpleBarChartEmptyPreview() {
    MaterialTheme {
        SimpleBarChart(
            data = emptyList(),
            contentDescriptionPrefix = "棒グラフ",
            emptyLabel = "データがありません",
        )
    }
}

@Preview(name = "Stacked bar chart", showBackground = true, widthDp = 393)
@Composable
private fun StackedBarChartPreview() {
    MaterialTheme {
        SimpleBarChart(
            contentDescriptionPrefix = "棒グラフ",
            emptyLabel = "データがありません",
            data = listOf(
                BarChartData(
                    "1月", 45000f,
                    segments = listOf(
                        BarChartSegment(25000f, stylishChartColor(0)),
                        BarChartSegment(12000f, stylishChartColor(1)),
                        BarChartSegment(8000f, stylishChartColor(2)),
                    ),
                ),
                BarChartData(
                    "2月", 30000f,
                    segments = listOf(
                        BarChartSegment(18000f, stylishChartColor(0)),
                        BarChartSegment(12000f, stylishChartColor(1)),
                    ),
                ),
                BarChartData(
                    "3月", 52000f,
                    segments = listOf(
                        BarChartSegment(28000f, stylishChartColor(0)),
                        BarChartSegment(14000f, stylishChartColor(1)),
                        BarChartSegment(10000f, stylishChartColor(2)),
                    ),
                ),
            ),
        )
    }
}

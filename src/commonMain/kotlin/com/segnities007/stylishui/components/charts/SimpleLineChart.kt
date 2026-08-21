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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled

/**
 * A single data point in a [SimpleLineChart], pairing an X-axis label
 * with a numeric Y value.
 *
 * Points are plotted in list order and connected by straight line segments.
 * The X axis is categorical (evenly spaced), not continuous.
 *
 * @property label Category or time-period name displayed below the
 *   corresponding point on the X axis. Labels are thinned automatically
 *   when the dataset exceeds `maxLabelCount`.
 * @property value Numeric magnitude plotted on the Y axis. Negative values
 *   are clamped to zero for drawing (the axis floor never drops below
 *   zero); non-finite values (`NaN`, `±Infinity`) are skipped entirely.
 * @see SimpleLineChart
 */
@Immutable
public data class LineChartData(
    public val label: String,
    public val value: Float,
)

/**
 * A line chart with area fill, grid lines, data-point markers, and
 * categorical X-axis labels, rendered on a Compose [Canvas].
 *
 * Data points from [data] are plotted left-to-right in list order and
 * connected with straight line segments. The area beneath the line is
 * filled with a semi-transparent [fillColor]. Each point is marked with a
 * two-tone circle (outer [pointColor], inner surface color) for visibility.
 *
 * The Y axis auto-scales to the data range with 10 % padding above and
 * below. Negative values are clamped to zero for drawing, so the axis floor
 * never drops below zero; non-finite values (`NaN`, `±Infinity`) are
 * excluded from the scale and skipped in drawing. The accessibility
 * description always reflects the original values. Horizontal grid lines
 * carry formatted value labels on the left margin; labels use a
 * locale-neutral one-decimal format.
 *
 * X-axis labels are automatically thinned to approximately [maxLabelCount]
 * visible entries (always including the last point) to avoid overlap on
 * dense datasets.
 *
 * When [data] contains fewer than two finite points, [emptyLabel] is drawn
 * at the chart center instead of a line.
 *
 * On first composition points animate upward from the baseline (see
 * [animate]); the area fill follows the same animated points. Text layouts
 * (category labels, grid values, and the empty label) are measured once per
 * data change in composition and reused inside the draw scope, so frames do
 * not re-measure text.
 *
 * This composable is available on all platforms (commonMain). Text is drawn
 * with Compose's common [rememberTextMeasurer] + [drawText] APIs.
 *
 * @param data Ordered data points to plot.
 * @param contentDescriptionPrefix Leading text for the combined accessibility
 *   description (e.g. "Fuel efficiency trend").
 * @param emptyLabel Text displayed at the chart center when [data] has
 *   fewer than two finite entries.
 * @param modifier Modifier applied to the outer [Canvas].
 * @param lineColor Stroke color of the connecting line. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param fillColor Semi-transparent color filling the area between the line
 *   and the X axis. Defaults to primary at 10 % alpha.
 * @param pointColor Outer ring color of each data-point marker. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param gridColor Color of horizontal grid lines. Defaults to
 *   `MaterialTheme.colorScheme.outlineVariant`.
 * @param chartHeight Total height of the chart area. Defaults to
 *   [StylishTheme.dimensions.lineChartHeight].
 * @param strokeWidth Thickness of the connecting line. Defaults to 2.5 dp.
 * @param pointRadius Outer radius of each data-point circle. Defaults to
 *   4 dp.
 * @param pointInnerRadius Inner (surface-colored) radius of each data-point
 *   circle, creating a ring effect. Defaults to 2 dp.
 * @param labelTextSize Text size for axis and category labels. Defaults to
 *   10 dp.
 * @param gridLineCount Number of horizontal grid lines including top and
 *   bottom. Defaults to 5.
 * @param maxLabelCount Maximum number of X-axis labels rendered. Labels are
 *   evenly sampled; the last data point's label is always included.
 *   Defaults to 6.
 * @param animate When `true`, points animate from the baseline to their
 *   target Y positions on first composition using
 *   `tween(StylishTheme.animation.durationMedium)`. When `false`, the chart
 *   appears instantly. Defaults to `true`.
 * @see LineChartData
 * @see SimpleBarChart
 * @see SimplePieChart
 */
@Composable
public fun SimpleLineChart(
    data: List<LineChartData>,
    contentDescriptionPrefix: String,
    emptyLabel: String,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    pointColor: Color = MaterialTheme.colorScheme.primary,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    chartHeight: Dp = StylishTheme.dimensions.lineChartHeight,
    strokeWidth: Dp = 2.5.dp,
    pointRadius: Dp = 4.dp,
    pointInnerRadius: Dp = 2.dp,
    labelTextSize: Dp = 10.dp,
    gridLineCount: Int = 5,
    maxLabelCount: Int = 6,
    animate: Boolean = true,
) {
    // Keep the source list for axis/semantics fidelity, but cap Canvas work for dense series.
    val renderedData = remember(data) {
        downsampleStylishSeries(data, StylishChartMaxRenderedPoints)
    }
    val values = data.map { it.value.coerceAtLeast(0f) }
    val axisRange = lineScaleRange(values)
    val axisMin = axisRange.start
    val axisMax = axisRange.endInclusive
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strings = StylishTheme.strings
    val scaledLabelTextSize = labelTextSize * LocalDensity.current.fontScale
    val pointInnerColor = MaterialTheme.colorScheme.surface
    val description = "$contentDescriptionPrefix: " +
        data.joinToString(", ") { "${it.label}=${strings.formatDecimal(it.value.toDouble())}" }
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        fontSize = scaledLabelTextSize.value.sp,
        color = labelColor,
    )
    val labelLayouts = remember(renderedData, scaledLabelTextSize, labelColor) {
        renderedData.map { textMeasurer.measure(it.label, labelStyle) }
    }
    val gridLayouts = remember(data, gridLineCount, scaledLabelTextSize, labelColor, axisRange) {
        List(gridLineCount) { i ->
            val gridValue = axisMax - (axisMax - axisMin) * i / (gridLineCount - 1).coerceAtLeast(1)
            textMeasurer.measure(gridValue.formatDecimal(1), labelStyle)
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
            .testTag("stylish_simple_line_chart")
            .semantics { contentDescription = description },
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val bottomPadding = 32.dp.toPx()
        val topPadding = 12.dp.toPx()
        val leftPadding = 40.dp.toPx()
        val usableWidth = chartWidth - leftPadding
        val usableHeight = chartHeight - bottomPadding - topPadding
        val animationProgress = progress.value
        val baselineY = chartHeight - bottomPadding

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

        val points: List<Offset?> = if (renderedData.size < 2) {
            emptyList()
        }
        else {
            val normalizedValues = lineNormalized(renderedData.map { it.value.coerceAtLeast(0f) }, axisMin, axisMax)
            renderedData.mapIndexed { index, d ->
                if (!d.value.isFinite()) {
                    null
                }
                else {
                    val x = leftPadding + usableWidth * index / (renderedData.size - 1)
                    val targetY = topPadding + usableHeight * (1f - normalizedValues[index])
                    Offset(x, lerp(baselineY, targetY, animationProgress))
                }
            }
        }
        val drawablePoints = points.filterNotNull()

        if (drawablePoints.size < 2) {
            drawText(
                textLayoutResult = emptyLayout,
                topLeft = Offset(
                    (chartWidth - emptyLayout.size.width) / 2f,
                    (chartHeight - emptyLayout.size.height) / 2f,
                ),
            )
        }
        else {
            val fillPath = Path().apply {
                moveTo(drawablePoints.first().x, baselineY)
                drawablePoints.forEach { lineTo(it.x, it.y) }
                lineTo(drawablePoints.last().x, baselineY)
                close()
            }
            drawPath(fillPath, fillColor)

            val linePath = Path().apply {
                moveTo(drawablePoints.first().x, drawablePoints.first().y)
                for (i in 1 until drawablePoints.size) {
                    lineTo(drawablePoints[i].x, drawablePoints[i].y)
                }
            }
            drawPath(
                linePath,
                lineColor,
                style = Stroke(width = strokeWidth.toPx()),
            )

            drawablePoints.forEach { point ->
                drawCircle(
                    color = pointColor,
                    radius = pointRadius.toPx(),
                    center = point,
                )
                drawCircle(
                    color = pointInnerColor,
                    radius = pointInnerRadius.toPx(),
                    center = point,
                )
            }

            val step = (renderedData.size + maxLabelCount - 1) / maxLabelCount
            renderedData.forEachIndexed { index, d ->
                if (index % step == 0 || index == data.size - 1) {
                    val point = points[index] ?: return@forEachIndexed
                    val layout = labelLayouts[index]
                    val x = point.x.coerceIn(leftPadding, chartWidth - 10.dp.toPx())
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(
                            x - layout.size.width / 2f,
                            chartHeight - 8.dp.toPx() - layout.size.height,
                        ),
                    )
                }
            }
        }
    }
}

@Preview(name = "Simple line chart", showBackground = true, widthDp = 393)
@Composable
private fun SimpleLineChartPreview() {
    MaterialTheme {
        SimpleLineChart(
            contentDescriptionPrefix = "折れ線グラフ",
            emptyLabel = "データがありません",
            data = listOf(
                LineChartData("1月", 12.5f),
                LineChartData("2月", 15.3f),
                LineChartData("3月", 11.8f),
                LineChartData("4月", 18.2f),
            ),
        )
    }
}

@Preview(name = "Simple line chart (empty)", showBackground = true, widthDp = 393)
@Composable
private fun SimpleLineChartEmptyPreview() {
    MaterialTheme {
        SimpleLineChart(
            data = emptyList(),
            contentDescriptionPrefix = "折れ線グラフ",
            emptyLabel = "データがありません",
        )
    }
}

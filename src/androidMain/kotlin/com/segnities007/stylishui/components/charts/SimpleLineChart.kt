package com.segnities007.stylishui.components.charts

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
 * @property value Numeric magnitude plotted on the Y axis. The vertical
 *   position is normalized between the dataset minimum and maximum with
 *   10 % padding on each end.
 * @see SimpleLineChart
 */
public data class LineChartData(
    val label: String,
    val value: Float,
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
 * below. When all values are non-negative the axis floor is clamped to
 * zero so no misleading negative ticks appear. Horizontal grid lines carry
 * formatted value labels on the left margin.
 *
 * X-axis labels are automatically thinned to approximately [maxLabelCount]
 * visible entries (always including the last point) to avoid overlap on
 * dense datasets.
 *
 * When [data] contains fewer than two points, [emptyLabel] is drawn at the
 * chart center instead of a line.
 *
 * **Platform:** Android only (androidMain). Uses `android.graphics.Paint`
 * for text rendering.
 *
 * @param data Ordered data points to plot.
 * @param contentDescriptionPrefix Leading text for the combined accessibility
 *   description (e.g. "Fuel efficiency trend").
 * @param emptyLabel Text displayed at the chart center when [data] has
 *   fewer than two entries.
 * @param modifier Modifier applied to the outer [Canvas].
 * @param lineColor Stroke color of the connecting line. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param fillColor Semi-transparent color filling the area between the line
 *   and the X axis. Defaults to primary at 10 % alpha.
 * @param pointColor Outer ring color of each data-point marker. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param gridColor Color of horizontal grid lines. Defaults to
 *   `MaterialTheme.colorScheme.outlineVariant`.
 * @param chartHeight Total height of the chart area. Defaults to 200 dp.
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
    chartHeight: Dp = 200.dp,
    strokeWidth: Dp = 2.5.dp,
    pointRadius: Dp = 4.dp,
    pointInnerRadius: Dp = 2.dp,
    labelTextSize: Dp = 10.dp,
    gridLineCount: Int = 5,
    maxLabelCount: Int = 6,
) {
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val pointInnerColor = MaterialTheme.colorScheme.surface
    val description = "$contentDescriptionPrefix: " +
        data.joinToString(", ") { "${it.label}=${it.value}" }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight)
            .semantics { contentDescription = description },
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val bottomPadding = 32.dp.toPx()
        val topPadding = 12.dp.toPx()
        val leftPadding = 40.dp.toPx()
        val usableWidth = chartWidth - leftPadding
        val usableHeight = chartHeight - bottomPadding - topPadding

        val labelPaint = Paint().apply {
            color = labelColor
            textSize = labelTextSize.toPx()
            isAntiAlias = true
            typeface = Typeface.DEFAULT
        }

        val maxValue = data.maxOfOrNull { it.value } ?: 0f
        val minValue = data.minOfOrNull { it.value } ?: 0f
        val range = (maxValue - minValue).coerceAtLeast(1f)
        val axisPadding = range * 0.1f
        val axisMax = maxValue + axisPadding
        // データが非負なら値軸の下端を 0 未満にしない（燃費・費用などでマイナス目盛りが出ないよう）
        val axisMin = if (minValue >= 0f) (minValue - axisPadding).coerceAtLeast(0f) else minValue - axisPadding

        for (i in 0 until gridLineCount) {
            val y = topPadding + usableHeight * i / (gridLineCount - 1).coerceAtLeast(1)
            drawLine(
                color = gridColor,
                start = Offset(leftPadding, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f,
            )
            val gridValue = axisMax - (axisMax - axisMin) * i / (gridLineCount - 1).coerceAtLeast(1)
            drawContext.canvas.nativeCanvas.drawText(
                "%.1f".format(gridValue),
                2.dp.toPx(),
                y + 4.dp.toPx(),
                labelPaint,
            )
        }

        if (data.size < 2) {
            labelPaint.textAlign = Paint.Align.CENTER
            drawContext.canvas.nativeCanvas.drawText(
                emptyLabel,
                chartWidth / 2f,
                chartHeight / 2f,
                labelPaint,
            )
        }
        else {
            val points = data.mapIndexed { index, d ->
                val x = if (data.size > 1) {
                    leftPadding + usableWidth * index / (data.size - 1)
                }
                else {
                    leftPadding + usableWidth / 2
                }
                val normalized = (d.value - axisMin) / (axisMax - axisMin)
                val y = topPadding + usableHeight * (1 - normalized)
                Offset(x, y)
            }

            val fillPath = Path().apply {
                moveTo(points.first().x, chartHeight - bottomPadding)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, chartHeight - bottomPadding)
                close()
            }
            drawPath(fillPath, fillColor)

            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }
            drawPath(
                linePath,
                lineColor,
                style = Stroke(width = strokeWidth.toPx()),
            )

            points.forEach { point ->
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

            val step = (data.size + maxLabelCount - 1) / maxLabelCount
            data.forEachIndexed { index, d ->
                if (index % step == 0 || index == data.size - 1) {
                    val x = points[index].x
                    labelPaint.textAlign = Paint.Align.CENTER
                    drawContext.canvas.nativeCanvas.drawText(
                        d.label,
                        x.coerceIn(leftPadding, chartWidth - 10.dp.toPx()),
                        chartHeight - 8.dp.toPx(),
                        labelPaint,
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

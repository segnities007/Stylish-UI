package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.molecules.StylishSection
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A page section that pairs a [StylishSection] heading with a
 * [SimpleLineChart].
 *
 * Use this on dashboard or summary screens to present time-series data
 * (e.g. fuel efficiency trends) with a consistent heading style. Available
 * on all platforms (commonMain).
 *
 * @param title Section heading text rendered by the [StylishSection] header.
 * @param data List of [LineChartData] points to plot. Each entry carries
 *   a label and a numeric value.
 * @param contentDescriptionPrefix Leading text for the chart's combined
 *   accessibility description.
 * @param emptyLabel Text displayed when [data] has fewer than two points,
 *   informing the user that no data is available.
 * @param modifier Modifier applied to the section container.
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
 *
 * @see BarChartSection
 * @see SimpleLineChart
 * @see StylishSection
 */
@Composable
public fun LineChartSection(
    title: String,
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
) {
    StylishSection(
        title = title,
        modifier = modifier.stylishTestTag("line_chart_section"),
    ) {
        SimpleLineChart(
            data = data,
            contentDescriptionPrefix = contentDescriptionPrefix,
            emptyLabel = emptyLabel,
            lineColor = lineColor,
            fillColor = fillColor,
            pointColor = pointColor,
            gridColor = gridColor,
            chartHeight = chartHeight,
            strokeWidth = strokeWidth,
            pointRadius = pointRadius,
            pointInnerRadius = pointInnerRadius,
            labelTextSize = labelTextSize,
            gridLineCount = gridLineCount,
            maxLabelCount = maxLabelCount,
        )
    }
}

@Preview(name = "Line chart section", showBackground = true, widthDp = 393)
@Composable
private fun LineChartSectionPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            LineChartSection(
                title = "燃費推移 (km/L)",
                contentDescriptionPrefix = "折れ線グラフ",
                emptyLabel = "データがありません",
                data = listOf(
                    LineChartData("5/2", 17.2f),
                    LineChartData("5/16", 18.1f),
                    LineChartData("5/30", 17.8f),
                ),
            )
        }
    }
}

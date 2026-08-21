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
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.components.molecules.StylishSection
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A page section that pairs a [StylishSection] heading with a
 * [SimpleBarChart].
 *
 * Use this on dashboard or summary screens to present categorical data
 * (e.g. monthly expenses) with a consistent heading style. Available on all
 * platforms (commonMain).
 *
 * @param title Section heading text rendered by the [StylishSection] header.
 * @param data List of [BarChartData] points to plot. Each entry carries
 *   a label, a numeric value, and an optional `segments` list for
 *   stacked bars.
 * @param contentDescriptionPrefix Leading text for the chart's combined
 *   accessibility description.
 * @param emptyLabel Text displayed when [data] is empty, informing the
 *   user that no data is available.
 * @param modifier Modifier applied to the section container.
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
 *
 * @see LineChartSection
 * @see SimpleBarChart
 * @see StylishSection
 */
@Composable
public fun BarChartSection(
    title: String,
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
) {
    StylishSection(
        title = title,
        modifier = modifier.stylishTestTag("bar_chart_section"),
    ) {
        SimpleBarChart(
            data = data,
            contentDescriptionPrefix = contentDescriptionPrefix,
            emptyLabel = emptyLabel,
            barColor = barColor,
            gridColor = gridColor,
            chartHeight = chartHeight,
            topRadius = topRadius,
            labelTextSize = labelTextSize,
            gridLineCount = gridLineCount,
        )
    }
}

@Preview(name = "Bar chart section", showBackground = true, widthDp = 393)
@Composable
private fun BarChartSectionPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            BarChartSection(
                title = "月次費用",
                contentDescriptionPrefix = "棒グラフ",
                emptyLabel = "データがありません",
                data = listOf(
                    BarChartData("2月", 8000f),
                    BarChartData("3月", 12000f),
                    BarChartData("4月", 45000f),
                ),
            )
        }
    }
}

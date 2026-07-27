package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A page section that pairs a [StylishSectionTitle] heading with a
 * [SimpleBarChart].
 *
 * Lays out the title and chart in a full-width [Column]. Use this on
 * dashboard or summary screens to present categorical data (e.g. monthly
 * expenses) with a consistent heading style. Android-only, because
 * [SimpleBarChart] depends on platform chart rendering.
 *
 * @param title Section heading text rendered by [StylishSectionTitle]
 *   above the chart.
 * @param data List of [BarChartData] points to plot. Each entry carries
 *   a label and a numeric value.
 * @param contentDescriptionPrefix Accessibility prefix prepended to each
 *   bar's content description, e.g. "Bar chart".
 * @param emptyLabel Text displayed when [data] is empty, informing the
 *   user that no data is available.
 *
 * @see LineChartSection
 * @see SimpleBarChart
 * @see StylishSectionTitle
 */
@Composable
public fun BarChartSection(
    title: String,
    data: List<BarChartData>,
    contentDescriptionPrefix: String,
    emptyLabel: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        StylishSectionTitle(title)
        SimpleBarChart(
            data = data,
            contentDescriptionPrefix = contentDescriptionPrefix,
            emptyLabel = emptyLabel,
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

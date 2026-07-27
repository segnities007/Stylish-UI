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
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A page section that pairs a [StylishSectionTitle] heading with a
 * [SimpleLineChart].
 *
 * Lays out the title and chart in a full-width [Column]. Use this on
 * dashboard or summary screens to present time-series data (e.g. fuel
 * efficiency trends) with a consistent heading style. Android-only,
 * because [SimpleLineChart] depends on platform chart rendering.
 *
 * @param title Section heading text rendered by [StylishSectionTitle]
 *   above the chart.
 * @param data List of [LineChartData] points to plot. Each entry carries
 *   a label and a numeric value.
 * @param contentDescriptionPrefix Accessibility prefix prepended to each
 *   point's content description, e.g. "Line chart".
 * @param emptyLabel Text displayed when [data] is empty, informing the
 *   user that no data is available.
 *
 * @see BarChartSection
 * @see SimpleLineChart
 * @see StylishSectionTitle
 */
@Composable
public fun LineChartSection(
    title: String,
    data: List<LineChartData>,
    contentDescriptionPrefix: String,
    emptyLabel: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        StylishSectionTitle(title)
        SimpleLineChart(
            data = data,
            contentDescriptionPrefix = contentDescriptionPrefix,
            emptyLabel = emptyLabel,
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

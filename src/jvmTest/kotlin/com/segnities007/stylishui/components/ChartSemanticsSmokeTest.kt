package com.segnities007.stylishui.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.StylishChartSelection
import com.segnities007.stylishui.components.charts.StylishLineSeries
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.charts.StylishMultiSeriesLineChart
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ChartSemanticsSmokeTest {

    @Test
    fun simpleLineChartPublishesDataDescription() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                SimpleLineChart(
                    data = listOf(LineChartData("Jan", 12.5f), LineChartData("Feb", 15f)),
                    contentDescriptionPrefix = "Revenue trend",
                    emptyLabel = "No data",
                    animate = false,
                )
            }
        }

        val descriptions = onNodeWithTag("stylish_simple_line_chart")
            .fetchSemanticsNode()
            .config[SemanticsProperties.ContentDescription]
        assertTrue(descriptions.any { it.startsWith("Revenue trend") })
        assertTrue(descriptions.any { it.contains("Jan") && it.contains("12.5") })
    }

    @Test
    fun multiSeriesChartPublishesPointAndSelectionSemantics() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishMultiSeriesLineChart(
                    labels = listOf("Jan", "Feb"),
                    series = listOf(
                        StylishLineSeries("Actual", listOf(10f, 20f), Color.Blue),
                    ),
                    contentDescriptionPrefix = "Performance",
                    selection = StylishChartSelection(0, 1),
                    showLegend = false,
                )
            }
        }

        onNodeWithTag("stylish_multi_series_line_chart_canvas").assertIsDisplayed()
        val descriptions = onNodeWithTag("stylish_multi_series_line_chart_canvas")
            .fetchSemanticsNode()
            .config[SemanticsProperties.ContentDescription]
        assertTrue(descriptions.any { it.startsWith("Performance") })
        assertTrue(descriptions.any { it.contains("Actual") && it.contains("Feb") })
    }
}

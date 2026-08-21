package com.segnities007.stylishui.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.components.charts.StylishAreaChart
import com.segnities007.stylishui.components.charts.StylishAreaPoint
import com.segnities007.stylishui.components.charts.StylishScatterChart
import com.segnities007.stylishui.components.charts.StylishScatterPoint
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ChartAreaScatterSemanticsSmokeTest {

    @Test
    fun areaChartRetainsFiniteSourceSamplesInSemantics() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishAreaChart(
                    points = listOf(
                        StylishAreaPoint("first", 1f),
                        StylishAreaPoint("missing", Float.NaN),
                        StylishAreaPoint("last", 4f),
                    ),
                    contentDescription = "Area trend",
                )
            }
        }

        val descriptions = onNodeWithTag("stylish_area_chart")
            .fetchSemanticsNode()
            .config[SemanticsProperties.ContentDescription]
        assertTrue(descriptions.any { it == "Area trend. first=1.0. last=4.0" })
    }

    @Test
    fun scatterChartRetainsFiniteSourceSamplesInSemantics() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                StylishScatterChart(
                    points = listOf(
                        StylishScatterPoint("first", 2f),
                        StylishScatterPoint("invalid", Float.NEGATIVE_INFINITY),
                        StylishScatterPoint("last", 8f),
                    ),
                    contentDescription = "Scatter trend",
                )
            }
        }

        val descriptions = onNodeWithTag("stylish_scatter_chart")
            .fetchSemanticsNode()
            .config[SemanticsProperties.ContentDescription]
        assertTrue(descriptions.any { it == "Scatter trend. first=2.0. last=8.0" })
    }
}

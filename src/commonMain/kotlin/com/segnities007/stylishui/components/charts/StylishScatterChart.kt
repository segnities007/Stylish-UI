package com.segnities007.stylishui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/** A single point rendered by [StylishScatterChart]. */
@Immutable
public data class StylishScatterPoint(public val label: String, public val value: Float)

/**
 * Renders independent points rather than connecting them with a line.
 * Non-finite values are skipped and the accessibility description retains all finite points.
 */
@Composable
public fun StylishScatterChart(
    points: List<StylishScatterPoint>,
    contentDescription: String,
    modifier: Modifier = Modifier,
    chartHeight: Dp = StylishTheme.dimensions.lineChartHeight,
    color: Color = MaterialTheme.colorScheme.primary,
    pointRadius: Dp = 5.dp,
) {
    val renderedPoints = downsampleStylishSeries(points, StylishChartMaxRenderedPoints)
    val values = points.map { it.value }
    val range = lineScaleRange(values)
    val renderedNormalized = lineNormalized(
        renderedPoints.map { it.value },
        range.start,
        range.endInclusive,
    )
    val description = buildStylishChartDescription(
        contentDescription,
        points.map { it.label to it.value },
    )
    Column(modifier.testTag("stylish_scatter_chart").semantics { this.contentDescription = description }) {
        Canvas(Modifier.fillMaxWidth().height(chartHeight)) {
            val radius = pointRadius.toPx()
            renderedPoints.forEachIndexed { index, point ->
                if (point.value.isFinite()) {
                    val x = if (renderedPoints.size <= 1) size.width / 2f else size.width * index / (renderedPoints.lastIndex.toFloat())
                    val y = size.height * (1f - renderedNormalized[index])
                    drawCircle(color = color, radius = radius, center = Offset(x, y))
                }
            }
        }
        Text("${range.start} – ${range.endInclusive}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(name = "Scatter chart", showBackground = true, widthDp = 393)
@Composable
private fun StylishScatterChartPreview() {
    StylishTheme(darkTheme = false) {
        StylishScatterChart(
            points = listOf(
                StylishScatterPoint("A", 10f),
                StylishScatterPoint("B", 24f),
                StylishScatterPoint("C", 16f),
            ),
            contentDescription = "Measurements",
        )
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/** A labelled value rendered by [StylishAreaChart]. */
@Immutable
public data class StylishAreaPoint(public val label: String, public val value: Float)

/**
 * Renders a filled area chart using the same scale rules as the line chart.
 * Invalid points break the path rather than connecting through missing data.
 */
@Composable
public fun StylishAreaChart(
    points: List<StylishAreaPoint>,
    contentDescription: String,
    modifier: Modifier = Modifier,
    chartHeight: Dp = StylishTheme.dimensions.lineChartHeight,
    color: Color = MaterialTheme.colorScheme.primary,
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
    Column(modifier.testTag("stylish_area_chart").semantics { this.contentDescription = description }) {
        Canvas(Modifier.fillMaxWidth().height(chartHeight)) {
            val line = Path()
            var segmentStartX = 0f
            var lastX = 0f
            var hasSegment = false
            renderedPoints.forEachIndexed { index, point ->
                if (point.value.isFinite()) {
                    val x = if (renderedPoints.size <= 1) size.width / 2f else size.width * index / renderedPoints.lastIndex.toFloat()
                    val y = size.height * (1f - renderedNormalized[index])
                    if (!hasSegment) {
                        line.moveTo(x, size.height)
                        line.lineTo(x, y)
                        segmentStartX = x
                        hasSegment = true
                    } else {
                        line.lineTo(x, y)
                    }
                    lastX = x
                } else if (hasSegment) {
                    line.lineTo(lastX, size.height)
                    line.lineTo(segmentStartX, size.height)
                    line.close()
                    drawPath(line, color.copy(alpha = 0.24f))
                    drawPath(line, color, style = Stroke(width = 2.dp.toPx()))
                    line.reset()
                    hasSegment = false
                }
            }
            if (hasSegment) {
                line.lineTo(lastX, size.height)
                line.lineTo(segmentStartX, size.height)
                line.close()
                drawPath(line, color.copy(alpha = 0.24f))
                drawPath(line, color, style = Stroke(width = 2.dp.toPx()))
            }
        }
        Text("${range.start} – ${range.endInclusive}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(name = "Area chart", showBackground = true, widthDp = 393)
@Composable
private fun StylishAreaChartPreview() {
    StylishTheme(darkTheme = false) {
        StylishAreaChart(
            points = listOf(
                StylishAreaPoint("A", 10f),
                StylishAreaPoint("B", 24f),
                StylishAreaPoint("C", 16f),
            ),
            contentDescription = "Revenue",
        )
    }
}

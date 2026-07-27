package com.segnities007.stylishui.components.charts

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.stylishChartColors

/** 円グラフの各スライス（ラベル・値・色）を表現するデータ。 */
public data class PieChartData(
    val label: String,
    val value: Float,
    val color: Color,
)

/** ドーナツ型のシンプルな円グラフを描画する Composable。 */
@Composable
public fun SimplePieChart(
    data: List<PieChartData>,
    contentDescriptionPrefix: String,
    modifier: Modifier = Modifier,
    chartSize: Dp = 160.dp,
    holeRatio: Float = 0.3f,
    skeletonRatio: Float = 0.4f,
    holeColor: Color = MaterialTheme.colorScheme.surface,
    skeletonColor: Color = MaterialTheme.colorScheme.outlineVariant,
) {
    val total = data.sumOf { it.value.toDouble() }
        .toFloat()
    val description = data.joinToString(", ") {
        "${it.label}: ${formatInteger(it.value.toInt())}"
    }

    Canvas(
        modifier = modifier
            .size(chartSize)
            .semantics { contentDescription = "$contentDescriptionPrefix: $description" },
    ) {
        if (total <= 0f || data.isEmpty()) {
            drawCircle(
                color = skeletonColor,
                radius = chartSize.toPx() * skeletonRatio,
            )
            drawCircle(
                color = holeColor,
                radius = chartSize.toPx() * holeRatio,
            )
        }
        else {
            var startAngle = -90f
            data.forEach { slice ->
                val sweep = (slice.value / total) * 360f
                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                )
                startAngle += sweep
            }
            drawCircle(
                color = holeColor,
                radius = chartSize.toPx() * holeRatio,
            )
        }
    }
}

/** テーマのカテゴリカルカラーパレットからインデックス対応の色を返す。 */
@Composable
public fun stylishChartColor(index: Int): Color {
    val colors = MaterialTheme.stylishChartColors.categorical
    return colors[index % colors.size]
}

@Preview(name = "Simple pie chart", showBackground = true, widthDp = 393)
@Composable
private fun SimplePieChartPreview() {
    MaterialTheme {
        SimplePieChart(
            contentDescriptionPrefix = "円グラフ",
            data = listOf(
                PieChartData("燃料費", 35000f, stylishChartColor(0)),
                PieChartData("保険", 15000f, stylishChartColor(1)),
                PieChartData("メンテナンス", 8000f, stylishChartColor(2)),
            ),
        )
    }
}

@Preview(name = "Simple pie chart (empty)", showBackground = true, widthDp = 393)
@Composable
private fun SimplePieChartEmptyPreview() {
    MaterialTheme {
        SimplePieChart(
            data = emptyList(),
            contentDescriptionPrefix = "円グラフ",
        )
    }
}

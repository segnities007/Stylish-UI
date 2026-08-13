package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishCode
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.PieChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.charts.SimplePieChart
import com.segnities007.stylishui.components.charts.stylishChartColor
import com.segnities007.stylishui.components.molecules.StylishDescriptions
import com.segnities007.stylishui.components.molecules.StylishDescriptionItem

@Composable
internal fun DemoCharts(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Pie chart",
        description = "円グラフ（アニメーション切替）。",
        code = """SimplePieChart(
    contentDescriptionPrefix = "支出の内訳",
    data = listOf(
        PieChartData("燃料費", 35000f, stylishChartColor(0)),
        PieChartData("保険", 15000f, stylishChartColor(1)),
        PieChartData("整備", 8000f, stylishChartColor(2)),
    ),
)""",
        modifier = modifier,
    ) {
        var animate by remember { mutableStateOf(true) }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            StylishChip(label = "アニメーション", onClick = { animate = !animate }, selected = animate)
        }
        SimplePieChart(
            contentDescriptionPrefix = "支出の内訳",
            data = listOf(
                PieChartData("燃料費", 35000f, stylishChartColor(0)),
                PieChartData("保険", 15000f, stylishChartColor(1)),
                PieChartData("整備", 8000f, stylishChartColor(2)),
            ),
            animate = animate,
        )
    }

    StylishDemoCard(
        title = "Bar chart",
        description = "棒グラフ（積み上げ対応）。",
        code = """SimpleBarChart(
    contentDescriptionPrefix = "月別売上",
    emptyLabel = "データがありません",
    data = listOf(
        BarChartData("1月", 30000f),
        BarChartData("2月", 45000f),
    ),
)""",
        modifier = modifier,
    ) {
        SimpleBarChart(
            contentDescriptionPrefix = "月別売上",
            emptyLabel = "データがありません",
            data = listOf(
                BarChartData("1月", 30000f),
                BarChartData("2月", 45000f),
                BarChartData("3月", 28000f),
                BarChartData("4月", 52000f),
            ),
        )
    }

    StylishDemoCard(
        title = "Line chart",
        description = "折れ線グラフ。",
        code = """SimpleLineChart(
    contentDescriptionPrefix = "週間推移",
    emptyLabel = "データがありません",
    data = listOf(
        LineChartData("月", 10f),
        LineChartData("火", 25f),
    ),
)""",
        modifier = modifier,
    ) {
        SimpleLineChart(
            contentDescriptionPrefix = "週間推移",
            emptyLabel = "データがありません",
            data = listOf(
                LineChartData("月", 10f),
                LineChartData("火", 25f),
                LineChartData("水", 18f),
                LineChartData("木", 32f),
                LineChartData("金", 28f),
            ),
        )
    }
}

@Composable
internal fun DemoWebParity(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Descriptions",
        description = "キー・バリューの詳細表示。",
        code = """StylishDescriptions(
    items = listOf(
        StylishDescriptionItem("車両名", "Stylish Car"),
        StylishDescriptionItem("年式", "2026"),
    ),
)""",
        modifier = modifier,
    ) {
        StylishDescriptions(
            items = listOf(
                StylishDescriptionItem("車両名", "Stylish Car"),
                StylishDescriptionItem("年式", "2026"),
                StylishDescriptionItem("色", "ホワイト"),
                StylishDescriptionItem("走行距離", "12,000 km"),
            ),
        )
    }

    StylishDemoCard(
        title = "Code",
        description = "インラインコード表示。",
        code = """StylishCode("implementation(\"io.github.segnities007:stylish-ui:0.8.0\")")""",
        modifier = modifier,
    ) {
        StylishCode("implementation(\"io.github.segnities007:stylish-ui:0.8.0\")")
    }

    StylishDemoCard(
        title = "Alert / Result / Toast",
        description = "Web 由来のフィードバック群（詳細は Feedback カテゴリ）。",
        code = """StylishAlert(message = "通知", variant = StylishAlertVariant.Success)
StylishResult(title = "完了", variant = StylishResultVariant.Success)""",
        modifier = modifier,
    ) {
        Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            Text(
                "Alert / Result / Toast / Popover / Popconfirm / Autocomplete は他カテゴリでデモしています。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

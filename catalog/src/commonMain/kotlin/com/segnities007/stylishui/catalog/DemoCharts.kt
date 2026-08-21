package com.segnities007.stylishui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishCode
import com.segnities007.stylishui.components.atoms.StylishRating
import com.segnities007.stylishui.components.atoms.StylishKbd
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.PieChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.charts.SimplePieChart
import com.segnities007.stylishui.components.charts.stylishChartColor
import com.segnities007.stylishui.components.molecules.StylishAccordion
import com.segnities007.stylishui.components.molecules.StylishAccordionItem
import com.segnities007.stylishui.components.molecules.StylishAvatarGroup
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.components.molecules.StylishBreadcrumb
import com.segnities007.stylishui.components.molecules.StylishBreadcrumbItem
import com.segnities007.stylishui.components.molecules.StylishDescriptionItem
import com.segnities007.stylishui.components.molecules.StylishDescriptions
import com.segnities007.stylishui.components.molecules.StylishPagination
import com.segnities007.stylishui.components.molecules.StylishSection
import com.segnities007.stylishui.components.molecules.StylishStatistic
import com.segnities007.stylishui.components.molecules.StylishStepper
import com.segnities007.stylishui.components.molecules.StylishTable
import com.segnities007.stylishui.components.molecules.StylishTimeline
import com.segnities007.stylishui.components.molecules.StylishTimelineItem
import androidx.compose.runtime.mutableIntStateOf

/**
 * Returns all chart-related demo components for the catalog.
 */
internal fun getChartDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Pie chart",
        category = DemoCategory.Charts,
        code = """SimplePieChart(
    contentDescriptionPrefix = "支出の内訳",
    data = listOf(
        PieChartData("燃料費", 35000f, stylishChartColor(0)),
        PieChartData("保険", 15000f, stylishChartColor(1)),
        PieChartData("整備", 8000f, stylishChartColor(2)),
    ),
)""",
        preview = {
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
        },
    ),
    DemoComponent(
        name = "Bar chart",
        category = DemoCategory.Charts,
        code = """SimpleBarChart(
    contentDescriptionPrefix = "月別売上",
    emptyLabel = "データがありません",
    data = listOf(
        BarChartData("1月", 30000f),
        BarChartData("2月", 45000f),
    ),
)""",
        preview = {
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
        },
    ),
    DemoComponent(
        name = "Line chart",
        category = DemoCategory.Charts,
        code = """SimpleLineChart(
    contentDescriptionPrefix = "週間推移",
    emptyLabel = "データがありません",
    data = listOf(
        LineChartData("月", 10f),
        LineChartData("火", 25f),
    ),
)""",
        preview = {
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
        },
    ),
)

/**
 * Returns all web-parity demo components for the catalog.
 */
internal fun getWebParityDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Descriptions",
        category = DemoCategory.WebParity,
        code = """StylishDescriptions(
    items = listOf(
        StylishDescriptionItem("車両名", "Stylish Car"),
        StylishDescriptionItem("年式", "2026"),
    ),
)""",
        preview = {
            StylishDescriptions(
                items = listOf(
                    StylishDescriptionItem("車両名", "Stylish Car"),
                    StylishDescriptionItem("年式", "2026"),
                    StylishDescriptionItem("色", "ホワイト"),
                    StylishDescriptionItem("走行距離", "12,000 km"),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Code",
        category = DemoCategory.WebParity,
        code = """StylishCode("implementation(\"io.github.segnities007:stylish-ui:0.8.0\")")""",
        preview = {
            StylishCode("implementation(\"io.github.segnities007:stylish-ui:0.8.0\")")
        },
    ),
    DemoComponent(
        name = "Accordion",
        category = DemoCategory.WebParity,
        code = """StylishAccordion(
    items = listOf(
        StylishAccordionItem("車両情報", { Text("内容") }),
        StylishAccordionItem("メンテナンス", { Text("内容") }),
    ),
)""",
        preview = {
            StylishAccordion(
                items = listOf(
                    StylishAccordionItem(
                        title = "車両情報",
                        content = { Text("ナンバー・年式・色などの基本情報を表示します。", style = MaterialTheme.typography.bodyMedium) },
                    ),
                    StylishAccordionItem(
                        title = "メンテナンス",
                        content = { Text("整備履歴の一覧です。", style = MaterialTheme.typography.bodyMedium) },
                    ),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Stepper",
        category = DemoCategory.WebParity,
        code = """StylishStepper(
    steps = listOf("情報入力", "確認", "完了"),
    currentStep = 1,
    completedSteps = setOf(0),
)""",
        preview = {
            StylishStepper(
                steps = listOf("情報入力", "確認", "完了"),
                currentStep = 1,
                completedSteps = setOf(0),
            )
        },
    ),
    DemoComponent(
        name = "Breadcrumb",
        category = DemoCategory.WebParity,
        code = """StylishBreadcrumb(
    items = listOf(
        StylishBreadcrumbItem("ホーム", onClick = {}),
        StylishBreadcrumbItem("車両管理", onClick = {}),
        StylishBreadcrumbItem("詳細"),
    ),
)""",
        preview = {
            StylishBreadcrumb(
                items = listOf(
                    StylishBreadcrumbItem("ホーム", onClick = {}),
                    StylishBreadcrumbItem("車両管理", onClick = {}),
                    StylishBreadcrumbItem("詳細"),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Pagination",
        category = DemoCategory.WebParity,
        code = """StylishPagination(
    page = page,
    onPageChange = { page = it },
    pageCount = 12,
)""",
        preview = {
            var page by remember { mutableIntStateOf(3) }
            StylishPagination(page = page, onPageChange = { page = it }, pageCount = 12)
        },
    ),
    DemoComponent(
        name = "Rating",
        category = DemoCategory.WebParity,
        code = """StylishRating(
    value = rating,
    onValueChange = { rating = it },
)""",
        preview = {
            var rating by remember { mutableIntStateOf(4) }
            StylishRating(value = rating, onValueChange = { rating = it })
        },
    ),
    DemoComponent(
        name = "Kbd",
        category = DemoCategory.WebParity,
        code = """StylishKbd("Ctrl")
StylishKbd("K")""",
        preview = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                StylishKbd("Ctrl")
                StylishKbd("+")
                StylishKbd("K")
            }
        },
    ),
    DemoComponent(
        name = "Statistic",
        category = DemoCategory.WebParity,
        code = """StylishStatistic(
    label = "燃費",
    value = "15.2 km/L",
    delta = "+1.3%",
)""",
        preview = {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                StylishStatistic(label = "燃費", value = "15.2 km/L", delta = "+1.3%")
                StylishStatistic(label = "経費", value = "¥32,000", delta = "-8%", deltaPositive = false)
            }
        },
    ),
    DemoComponent(
        name = "Table",
        category = DemoCategory.WebParity,
        code = """StylishTable(
    columns = listOf("項目", "金額", "日付"),
    rows = listOf(
        listOf("オイル交換", "¥12,000", "2026/08/10"),
        listOf("タイヤ", "¥48,000", "2026/07/22"),
    ),
)""",
        preview = {
            StylishTable(
                columns = listOf("項目", "金額", "日付"),
                rows = listOf(
                    listOf("オイル交換", "¥12,000", "2026/08/10"),
                    listOf("タイヤ交換", "¥48,000", "2026/07/22"),
                    listOf("車検", "¥85,000", "2026/06/01"),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Timeline",
        category = DemoCategory.WebParity,
        code = """StylishTimeline(
    items = listOf(
        StylishTimelineItem("オイル交換", "エンジンオイル交換", "2026/08/10"),
        StylishTimelineItem("車検", "ユーザー車検にて合格", "2026/06/01"),
    ),
)""",
        preview = {
            StylishTimeline(
                items = listOf(
                    StylishTimelineItem("オイル交換", "エンジンオイル交換", "2026/08/10"),
                    StylishTimelineItem("車検", "ユーザー車検にて合格", "2026/06/01"),
                    StylishTimelineItem("タイヤ交換", "スタッドレスに交換", "2026/05/15"),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Section",
        category = DemoCategory.WebParity,
        code = """StylishSection(
    title = "セクションタイトル",
    supportingText = "補足テキスト",
) {
    Text("コンテンツ")
}""",
        preview = {
            StylishSection(
                title = "車両情報",
                supportingText = "基本情報のセクション",
            ) {
                Text("セクションのコンテンツをここに配置します。", style = MaterialTheme.typography.bodyMedium)
            }
        },
    ),
    DemoComponent(
        name = "Avatar group",
        category = DemoCategory.WebParity,
        code = """StylishAvatarGroup(count = 4, size = 40.dp) { index ->
    StylishAvatar(initials = "U${'$'}index", size = 36.dp)
}""",
        preview = {
            StylishAvatarGroup(count = 5, size = 40.dp) { index ->
                StylishAvatar(initials = "U$index", size = 36.dp)
            }
        },
    ),
)

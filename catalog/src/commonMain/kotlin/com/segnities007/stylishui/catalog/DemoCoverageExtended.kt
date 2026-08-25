// Demo previews intentionally exercise experimental M3-backed Stylish
// components (e.g. the bottom sheet scaffold), so the opt-in is file-scoped.
@file:OptIn(ExperimentalMaterial3Api::class)

package com.segnities007.stylishui.catalog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishContentSpacer
import com.segnities007.stylishui.components.atoms.StylishHorizontalSpacer
import com.segnities007.stylishui.components.atoms.StylishInlineSpacer
import com.segnities007.stylishui.components.atoms.StylishItemSpacer
import com.segnities007.stylishui.components.atoms.StylishSectionSpacer
import com.segnities007.stylishui.components.atoms.StylishSpacer
import com.segnities007.stylishui.components.atoms.StylishVisuallyHidden
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.molecules.DefaultStylishConnectedButton
import com.segnities007.stylishui.components.molecules.DefaultStylishConnectedCardItem
import com.segnities007.stylishui.components.molecules.DefaultStylishConnectedChip
import com.segnities007.stylishui.components.molecules.StylishCarousel
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardLazyColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardLazyGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedChipGrid
import com.segnities007.stylishui.components.molecules.StylishDateRangePicker
import com.segnities007.stylishui.components.molecules.StylishSnackbarHost
import com.segnities007.stylishui.components.molecules.StylishTimePicker
import com.segnities007.stylishui.components.molecules.StylishTimePickerDialog
import com.segnities007.stylishui.components.molecules.rememberStylishDateRangePickerState
import com.segnities007.stylishui.components.organisms.StylishCommandItem
import com.segnities007.stylishui.components.organisms.StylishCommandPalette
import com.segnities007.stylishui.components.organisms.StylishDismissibleNavigationDrawer
import com.segnities007.stylishui.components.organisms.StylishMultiChoiceSegmentedButtonRow
import com.segnities007.stylishui.components.organisms.StylishPermanentNavigationDrawer
import com.segnities007.stylishui.components.organisms.StylishSegmentedButton
import com.segnities007.stylishui.components.organisms.StylishSingleChoiceSegmentedButtonRow
import com.segnities007.stylishui.components.patterns.BarChartSection
import com.segnities007.stylishui.components.patterns.StylishScreenScaffold
import com.segnities007.stylishui.components.patterns.LineChartSection
import com.segnities007.stylishui.components.patterns.StylishAdaptiveNavigation
import com.segnities007.stylishui.components.patterns.StylishBottomSheetScaffold
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.stylishComponentColors
import kotlinx.coroutines.launch

/**
 * Extended coverage demos for public visual APIs that the themed family files
 * do not exercise. Every entry really composes its public API with realistic
 * content; the copy-ready snippet mirrors what the preview renders.
 *
 * State-coverage notes per demo (states not listed are not applicable to that
 * component's contract):
 * - Command palette: default / focused (keyboard navigation) / disabled item.
 * - Navigation drawers: default / selected destination.
 * - Segmented rows: default / selected / disabled.
 * - Chart sections: default / empty (emptyLabel) / long_text labels.
 * - Carousel: default / RTL variant.
 * - Date range picker: default / selected range.
 * - Snackbar host: default / action press.
 * - Time picker dialog: default / confirm / dismiss.
 * - Connected grids & lazy layouts: default / selected / disabled / long_text.
 * - Default renderers: default / selected / disabled, standalone geometry.
 * - Spacers: default rhythm steps.
 * - Visually hidden: screen-reader-only content.
 */
internal fun getExtendedCoverageDemos(): List<DemoComponent> = listOf(
    // ------------------------------------------------------------------
    // Organisms
    // ------------------------------------------------------------------
    DemoComponent(
        name = "Command palette",
        category = DemoCategory.Advanced,
        code = """var expanded by remember { mutableStateOf(false) }
var query by remember { mutableStateOf("") }
StylishButton(onClick = { expanded = true }) { Text("コマンドパレット") }
if (expanded) {
    StylishCommandPalette(
        expanded = true,
        onDismiss = { expanded = false },
        query = query,
        onQueryChange = { query = it },
        items = listOf(
            StylishCommandItem("新規記録を作成", onSelect = { expanded = false }, keywords = listOf("new", "作成")),
            StylishCommandItem("車両一覧を開く", onSelect = { expanded = false }),
            StylishCommandItem("アカウント設定", onSelect = { expanded = false }, enabled = false),
        ),
    )
}""",
        preview = {
            var expanded by remember { mutableStateOf(false) }
            var query by remember { mutableStateOf("") }
            // Keyboard navigation: Arrow Up/Down moves the selection,
            // Enter selects, Escape dismisses.
            StylishButton(onClick = { expanded = true }, variant = StylishButtonVariant.Outlined) {
                Text("⌘K コマンドパレットを開く")
            }
            if (expanded) {
                StylishCommandPalette(
                    expanded = true,
                    onDismiss = { expanded = false },
                    query = query,
                    onQueryChange = { query = it },
                    items = listOf(
                        StylishCommandItem(
                            label = "新規記録を作成",
                            onSelect = { expanded = false },
                            keywords = listOf("new", "作成"),
                        ),
                        StylishCommandItem(label = "車両一覧を開く", onSelect = { expanded = false }),
                        StylishCommandItem(label = "給与計算を開く", onSelect = { expanded = false }),
                        StylishCommandItem(
                            label = "アカウント設定",
                            onSelect = {},
                            enabled = false,
                        ),
                    ),
                )
            }
        },
    ),
    DemoComponent(
        name = "Dismissible navigation drawer",
        category = DemoCategory.Navigation,
        code = """val drawerState = rememberDrawerState(DrawerValue.Open)
StylishDismissibleNavigationDrawer(
    drawerContent = {
        Text("メニュー")
        NavigationDrawerItem(label = { Text("ホーム") }, selected = true, onClick = {})
        NavigationDrawerItem(label = { Text("設定") }, selected = false, onClick = {})
    },
    drawerState = drawerState,
) { MainScreen() }""",
        preview = {
            val drawerState = rememberDrawerState(DrawerValue.Open)
            Box(Modifier.fillMaxWidth().height(220.dp)) {
                StylishDismissibleNavigationDrawer(
                    drawerContent = {
                        Text(
                            "メニュー",
                            Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        NavigationDrawerItem(
                            label = { Text("ホーム") },
                            selected = true,
                            onClick = {},
                        )
                        NavigationDrawerItem(
                            label = { Text("記録一覧") },
                            selected = false,
                            onClick = {},
                        )
                        NavigationDrawerItem(
                            label = { Text("設定") },
                            selected = false,
                            onClick = {},
                        )
                    },
                    drawerState = drawerState,
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("スワイプでドロワーを閉じる")
                    }
                }
            }
        },
    ),
    DemoComponent(
        name = "Permanent navigation drawer",
        category = DemoCategory.Navigation,
        code = """StylishPermanentNavigationDrawer(
    drawerContent = {
        Text("ワークスペース")
        NavigationDrawerItem(label = { Text("ダッシュボード") }, selected = true, onClick = {})
        NavigationDrawerItem(label = { Text("レポート") }, selected = false, onClick = {})
    },
) { MainScreen() }""",
        preview = {
            Box(Modifier.fillMaxWidth().height(220.dp)) {
                StylishPermanentNavigationDrawer(
                    drawerContent = {
                        Text(
                            "ワークスペース",
                            Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        NavigationDrawerItem(
                            label = { Text("ダッシュボード") },
                            selected = true,
                            onClick = {},
                        )
                        NavigationDrawerItem(
                            label = { Text("レポート") },
                            selected = false,
                            onClick = {},
                        )
                        NavigationDrawerItem(
                            label = { Text("メンバー") },
                            selected = false,
                            onClick = {},
                        )
                    },
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("常設ドロワーの横のコンテンツ")
                    }
                }
            }
        },
    ),
    DemoComponent(
        name = "Segmented button rows",
        category = DemoCategory.Selection,
        code = """var period by remember { mutableIntStateOf(0) }
val filters = remember { mutableStateListOf(true, false) }
StylishSingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
    StylishSegmentedButton(
        selected = period == 0,
        onClick = { period = 0 },
        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
    ) { Text("日") }
    StylishSegmentedButton(
        selected = period == 1,
        onClick = { period = 1 },
        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
    ) { Text("週") }
    StylishSegmentedButton(
        selected = period == 2,
        onClick = { period = 2 },
        enabled = false,
        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
    ) { Text("月") }
}
StylishMultiChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
    StylishSegmentedButton(
        checked = filters[0],
        onCheckedChange = { filters[0] = it },
        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
    ) { Text("文字") }
    StylishSegmentedButton(
        checked = filters[1],
        onCheckedChange = { filters[1] = it },
        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
    ) { Text("数値") }
}""",
        preview = {
            var period by remember { mutableIntStateOf(0) }
            val filters = remember { mutableStateListOf(true, false) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishSingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    StylishSegmentedButton(
                        selected = period == 0,
                        onClick = { period = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                    ) { Text("日") }
                    StylishSegmentedButton(
                        selected = period == 1,
                        onClick = { period = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                    ) { Text("週") }
                    StylishSegmentedButton(
                        selected = period == 2,
                        onClick = { period = 2 },
                        enabled = false,
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                    ) { Text("月") }
                }
                StylishMultiChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    StylishSegmentedButton(
                        checked = filters[0],
                        onCheckedChange = { filters[0] = it },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    ) { Text("文字") }
                    StylishSegmentedButton(
                        checked = filters[1],
                        onCheckedChange = { filters[1] = it },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    ) { Text("数値") }
                }
            }
        },
    ),
    // ------------------------------------------------------------------
    // Patterns
    // ------------------------------------------------------------------
    DemoComponent(
        name = "Bar chart section",
        category = DemoCategory.Patterns,
        code = """BarChartSection(
    title = "月次費用",
    data = listOf(BarChartData("2月", 8000f), BarChartData("3月", 12000f)),
    contentDescriptionPrefix = "棒グラフ",
    emptyLabel = "データがありません",
)""",
        preview = {
            BarChartSection(
                title = "月次費用 (直近6か月の累計)",
                data = listOf(
                    BarChartData("2月", 8000f),
                    BarChartData("3月", 12000f),
                    BarChartData("4月", 45000f),
                    BarChartData("5月", 32000f),
                ),
                contentDescriptionPrefix = "棒グラフ",
                emptyLabel = "データがありません",
            )
        },
    ),
    DemoComponent(
        name = "Line chart section",
        category = DemoCategory.Patterns,
        code = """LineChartSection(
    title = "燃費推移",
    data = listOf(LineChartData("4月", 18f), LineChartData("5月", 22f)),
    contentDescriptionPrefix = "折れ線グラフ",
    emptyLabel = "データがありません",
)""",
        preview = {
            LineChartSection(
                title = "燃費推移 (km/L)",
                data = listOf(
                    LineChartData("4月", 18f),
                    LineChartData("5月", 22f),
                    LineChartData("6月", 19f),
                    LineChartData("7月", 24f),
                ),
                contentDescriptionPrefix = "折れ線グラフ",
                emptyLabel = "データがありません",
            )
        },
    ),
    DemoComponent(
        name = "Screen scaffold pinned header",
        category = DemoCategory.Patterns,
        code = """StylishScreenScaffold(
    header = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
    floatingBottomCenter = { PagerDotPill() },
    floatingActionButton = { Fab() },
) { headerHeight ->
    LazyColumn(contentPadding = PaddingValues(top = headerHeight)) { Items() }
}""",
        preview = {
            StylishScreenScaffold(
                header = { Text("ページタイトル", style = MaterialTheme.typography.titleLarge) },
                floatingBottomCenter = {
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    ) {
                        Text(
                            "indicator",
                            modifier = androidx.compose.ui.Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                },
            ) { innerPadding ->
                Box(androidx.compose.ui.Modifier.padding(innerPadding)) {
                    Text(
                        "スクロールしてもヘッダーは固定されます",
                        modifier = androidx.compose.ui.Modifier.padding(20.dp),
                    )
                }
            }
        },
    ),
    DemoComponent(
        name = "Adaptive navigation scaffold",
        category = DemoCategory.Patterns,
        code = """StylishAdaptiveNavigation(
    items = listOf(
        StylishNavigationItem(Icons.Default.Home, "ホーム", selected = true),
        StylishNavigationItem(Icons.Default.Settings, "設定"),
    ),
) { DashboardContent() }""",
        preview = {
            var selected by remember { mutableIntStateOf(0) }
            Box(Modifier.fillMaxWidth().height(240.dp)) {
                StylishAdaptiveNavigation(
                    items = listOf(
                        StylishNavigationItem(
                            Icons.Default.Home,
                            "ホーム",
                            selected = selected == 0,
                            onClick = { selected = 0 },
                        ),
                        StylishNavigationItem(
                            Icons.Default.Notifications,
                            "通知",
                            selected = selected == 1,
                            onClick = { selected = 1 },
                        ),
                        StylishNavigationItem(
                            Icons.Default.Settings,
                            "設定",
                            selected = selected == 2,
                            onClick = { selected = 2 },
                        ),
                    ),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(if (selected == 0) "ホームのコンテンツ" else "選択: $selected")
                    }
                }
            }
        },
    ),
    DemoComponent(
        name = "Bottom sheet scaffold",
        category = DemoCategory.Patterns,
        code = """StylishBottomSheetScaffold(
    sheetContent = {
        ListItem(headlineContent = { Text("経費を追加") })
        ListItem(headlineContent = { Text("写真を撮影") })
    },
) { padding ->
    MapContent(Modifier.padding(padding))
}""",
        preview = {
            Box(Modifier.fillMaxWidth().height(260.dp)) {
                StylishBottomSheetScaffold(
                    sheetContent = {
                        Text(
                            "経費を追加",
                            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        )
                        Text(
                            "写真を撮影して記録する",
                            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        )
                    },
                ) { padding ->
                    Box(
                        Modifier.padding(padding).fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("下のシートを上へドラッグ")
                    }
                }
            }
        },
    ),
    // ------------------------------------------------------------------
    // Molecules
    // ------------------------------------------------------------------
    DemoComponent(
        name = "Carousel multi browse",
        category = DemoCategory.WebParity,
        code = """StylishCarousel(
    itemCount = { banners.size },
    preferredItemWidth = 200.dp,
    modifier = Modifier.fillMaxWidth(),
    itemSpacing = 8.dp,
) { index ->
    Box(
        Modifier
            .maskClip(RoundedCornerShape(12.dp))
            .background(banners[index % banners.size])
            .height(120.dp),
    ) { Text("バナー ${'$'}index") }
}""",
        preview = { carouselCoveragePreview() },
    ),
    DemoComponent(
        name = "Date range picker inline",
        category = DemoCategory.Inputs,
        code = """val state = rememberStylishDateRangePickerState(
    initialSelectedStartDateMillis = 1_783_123_200_000,
    initialSelectedEndDateMillis = 1_784_217_600_000,
)
StylishDateRangePicker(state = state, modifier = Modifier.padding(20.dp))""",
        preview = {
            val state = rememberStylishDateRangePickerState(
                initialSelectedStartDateMillis = 1_783_123_200_000,
                initialSelectedEndDateMillis = 1_784_217_600_000,
            )
            StylishDateRangePicker(state = state)
        },
    ),
    DemoComponent(
        name = "Snackbar host queue",
        category = DemoCategory.Feedback,
        code = """val hostState = remember { SnackbarHostState() }
val scope = rememberCoroutineScope()
StylishButton(onClick = {
    scope.launch { hostState.showSnackbar("保存しました", actionLabel = "元に戻す") }
}) { Text("スナックバーを表示") }
Box {
    StylishSnackbarHost(hostState = hostState)
}""",
        preview = {
            val hostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishButton(onClick = {
                    scope.launch {
                        hostState.showSnackbar("保存しました", actionLabel = "元に戻す")
                    }
                }) { Text("スナックバーを表示") }
                Surface(
                    Modifier.fillMaxWidth().height(72.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        StylishSnackbarHost(hostState = hostState)
                    }
                }
            }
        },
    ),
    DemoComponent(
        name = "Time picker dialog modal",
        category = DemoCategory.Inputs,
        code = """var open by remember { mutableStateOf(false) }
StylishButton(onClick = { open = true }) { Text("時刻を選択") }
if (open) {
    StylishTimePickerDialog(
        onDismissRequest = { open = false },
        title = { Text("時刻を選択") },
        confirmButton = { TextButton(onClick = { open = false }) { Text("OK") } },
        dismissButton = { TextButton(onClick = { open = false }) { Text("キャンセル") } },
    ) {
        StylishTimePicker()
    }
}""",
        preview = { timePickerDialogCoveragePreview() },
    ),
    DemoComponent(
        name = "Connected button grid layout",
        category = DemoCategory.Connected,
        code = """StylishConnectedButtonGrid(
    items = listOf(
        StylishConnectedButtonItem(onClick = {}) { Text("追加") },
        StylishConnectedButtonItem(onClick = {}) { Text("編集") },
        StylishConnectedButtonItem(onClick = {}) { Text("削除") },
        StylishConnectedButtonItem(enabled = false) { Text("共有") },
    ),
    columns = 2,
)""",
        preview = {
            StylishConnectedButtonGrid(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("追加") },
                    StylishConnectedButtonItem(onClick = {}) { Text("編集") },
                    StylishConnectedButtonItem(onClick = {}) { Text("削除") },
                    StylishConnectedButtonItem(enabled = false) { Text("共有") },
                ),
                columns = 2,
            )
        },
    ),
    DemoComponent(
        name = "Connected chip grid filter",
        category = DemoCategory.Connected,
        code = """StylishConnectedChipGrid(
    items = tags.mapIndexed { index, tag ->
        StylishConnectedChipItem(tag, onClick = {}, selected = index in selectedIndexes)
    },
    columns = 2,
)""",
        preview = {
            val tags = remember { listOf("すべて", "仕事", "個人", "アイデア", "旅行", "買い物") }
            val selected = remember { mutableStateListOf(true, false, false, false, false, false) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishConnectedChipGrid(
                    items = tags.mapIndexed { index, tag ->
                        StylishConnectedChipItem(
                            label = tag,
                            onClick = { selected[index] = !selected[index] },
                            selected = selected[index],
                        )
                    },
                    columns = 2,
                )
                // RTL: 右から左のレイアウトでも接続ジオメトリは維持される
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    StylishConnectedChipGrid(
                        items = listOf(
                            StylishConnectedChipItem("الأولى", onClick = {}, selected = true),
                            StylishConnectedChipItem("الثانية", onClick = {}),
                        ),
                        columns = 2,
                    )
                }
            }
        },
    ),
    DemoComponent(
        name = "Connected card grid contacts",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardGrid(
    items = listOf(
        StylishConnectedCardItem("田中 太郎", "設計担当", onClick = {}),
        StylishConnectedCardItem("鈴木 花子", "開発担当", onClick = {}),
        StylishConnectedCardItem("佐藤 次郎", "テスト担当"),
        StylishConnectedCardItem("高橋美咲", "休職中", enabled = false),
    ),
    columns = 2,
)""",
        preview = {
            StylishConnectedCardGrid(
                items = listOf(
                    StylishConnectedCardItem("田中 太郎", "設計担当", onClick = {}),
                    StylishConnectedCardItem("鈴木 花子", "開発担当", onClick = {}),
                    StylishConnectedCardItem("佐藤 次郎", "テスト担当"),
                    StylishConnectedCardItem("高橋 美咲", "長期休職中のため無効", enabled = false),
                ),
                columns = 2,
            )
        },
    ),
    DemoComponent(
        name = "Connected cards lazy column scroll",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardLazyColumn(
    items = vehicles.map { StylishConnectedCardItem(it.name, it.body, onClick = {}) },
)""",
        preview = {
            Box(Modifier.fillMaxWidth().height(240.dp)) {
                StylishConnectedCardLazyColumn(
                    items = listOf(
                        StylishConnectedCardItem("Civic", "コンパクトセダン・走行 12,000 km", onClick = {}),
                        StylishConnectedCardItem("Accord", "セダン・走行 30,500 km", onClick = {}),
                        StylishConnectedCardItem("Prius", "ハイブリッド・走行 8,200 km"),
                        StylishConnectedCardItem("Model 3", "EV・走行 15,700 km", onClick = {}),
                        StylishConnectedCardItem("Leaf", "EV・走行 22,400 km"),
                        StylishConnectedCardItem("Outback", "SUV・走行 41,900 km", onClick = {}),
                    ),
                )
            }
        },
    ),
    DemoComponent(
        name = "Connected cards lazy grid scroll",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardLazyGrid(
    items = vehicles.map { StylishConnectedCardItem(it.name, it.body, onClick = {}) },
    columns = 2,
)""",
        preview = {
            Box(Modifier.fillMaxWidth().height(240.dp)) {
                StylishConnectedCardLazyGrid(
                    items = listOf(
                        StylishConnectedCardItem("Civic", "コンパクト", onClick = {}),
                        StylishConnectedCardItem("Accord", "セダン", onClick = {}),
                        StylishConnectedCardItem("Prius", "ハイブリッド"),
                        StylishConnectedCardItem("Model 3", "EV", onClick = {}),
                        StylishConnectedCardItem("Leaf", "EV"),
                        StylishConnectedCardItem("Golf", "ハッチバック", onClick = {}),
                    ),
                    columns = 2,
                )
            }
        },
    ),
    DemoComponent(
        name = "Connected cards lazy column employees",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardLazyColumn(
    items = employees.map {
        StylishConnectedCardItem(it.name, it.role, onClick = {})
    },
)""",
        preview = {
            Box(Modifier.fillMaxWidth().height(240.dp)) {
                StylishConnectedCardLazyColumn(
                    items = listOf(
                        StylishConnectedCardItem("田中 太郎", "設計担当・東京オフィス", onClick = {}),
                        StylishConnectedCardItem("鈴木 花子", "開発担当・大阪オフィス", onClick = {}),
                        StylishConnectedCardItem("佐藤 次郎", "テスト担当・リモート"),
                        StylishConnectedCardItem("高橋 美咲", "プロダクトマネージャー", onClick = {}),
                        StylishConnectedCardItem("山本 一郎", "インフラ担当・休職中", enabled = false),
                        StylishConnectedCardItem("中村 桜", "データ分析担当", onClick = {}),
                    ),
                )
            }
        },
    ),
    DemoComponent(
        name = "Connected cards lazy grid employees",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardLazyGrid(
    items = employees.map {
        StylishConnectedCardItem(it.name, it.role, onClick = {})
    },
    columns = 2,
)""",
        preview = {
            Box(Modifier.fillMaxWidth().height(240.dp)) {
                StylishConnectedCardLazyGrid(
                    items = listOf(
                        StylishConnectedCardItem("田中 太郎", "設計担当", onClick = {}),
                        StylishConnectedCardItem("鈴木 花子", "開発担当", onClick = {}),
                        StylishConnectedCardItem("佐藤 次郎", "テスト担当"),
                        StylishConnectedCardItem("高橋 美咲", "プロダクトマネージャー", onClick = {}),
                        StylishConnectedCardItem("山本 一郎", "休職中", enabled = false),
                        StylishConnectedCardItem("中村 桜", "データ分析担当", onClick = {}),
                    ),
                    columns = 2,
                )
            }
        },
    ),
    DemoComponent(
        name = "Default connected button renderer",
        category = DemoCategory.Connected,
        code = """// Standalone use of the default renderer with full connection geometry.
DefaultStylishConnectedButton(
    item = StylishConnectedButtonItem(onClick = {}) { Text("スタンドアロン") },
    modifier = Modifier.fillMaxWidth(),
    shape = connectedShape(ConnectedCorners.Standalone),
    outlineEdges = ConnectedEdges.All,
    outlineCorners = ConnectedCorners.Standalone,
    cornerRadius = 12.dp,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    defaultColors = ButtonDefaults.buttonColors(),
)""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DefaultStylishConnectedButton(
                    item = StylishConnectedButtonItem(onClick = {}) { Text("スタンドアロン") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = connectedShape(ConnectedCorners.Standalone),
                    outlineEdges = ConnectedEdges.All,
                    outlineCorners = ConnectedCorners.Standalone,
                    cornerRadius = 12.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    defaultColors = ButtonDefaults.buttonColors(),
                )
                DefaultStylishConnectedButton(
                    item = StylishConnectedButtonItem(enabled = false) { Text("無効なボタン") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = connectedShape(ConnectedCorners.Standalone),
                    outlineEdges = ConnectedEdges.All,
                    outlineCorners = ConnectedCorners.Standalone,
                    cornerRadius = 12.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    defaultColors = ButtonDefaults.buttonColors(),
                )
            }
        },
    ),
    DemoComponent(
        name = "Default connected card renderer",
        category = DemoCategory.Connected,
        code = """DefaultStylishConnectedCardItem(
    item = StylishConnectedCardItem(
        "スタンドアロンカード",
        "単体でレンダラーを使う例。クリックも有効です。",
        onClick = {},
    ),
    modifier = Modifier.fillMaxWidth(),
    shape = connectedShape(ConnectedCorners.Standalone),
    outlineEdges = ConnectedEdges.All,
    outlineCorners = ConnectedCorners.Standalone,
)""",
        preview = {
            DefaultStylishConnectedCardItem(
                item = StylishConnectedCardItem(
                    "スタンドアロンカード",
                    "単体でレンダラーを使う例。クリックも有効です。",
                    onClick = {},
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = connectedShape(ConnectedCorners.Standalone),
                outlineEdges = ConnectedEdges.All,
                outlineCorners = ConnectedCorners.Standalone,
            )
        },
    ),
    DemoComponent(
        name = "Default connected chip renderer",
        category = DemoCategory.Connected,
        code = """DefaultStylishConnectedChip(
    item = StylishConnectedChipItem("選択済み", onClick = {}, selected = true),
    modifier = Modifier.fillMaxWidth(),
    shape = connectedShape(ConnectedCorners.Standalone),
    outlineEdges = ConnectedEdges.All,
    outlineCorners = ConnectedCorners.Standalone,
    labelMaxLines = 1,
    labelOverflow = TextOverflow.Ellipsis,
    labelStyle = MaterialTheme.typography.labelLarge,
    selectedContainerColor = MaterialTheme.colorScheme.primary,
    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor = MaterialTheme.stylishComponentColors.groupedContainer,
    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing = 6.dp,
)""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DefaultStylishConnectedChip(
                    item = StylishConnectedChipItem("選択済みチップ", onClick = {}, selected = true),
                    modifier = Modifier.fillMaxWidth(),
                    shape = connectedShape(ConnectedCorners.Standalone),
                    outlineEdges = ConnectedEdges.All,
                    outlineCorners = ConnectedCorners.Standalone,
                    labelMaxLines = 1,
                    labelOverflow = TextOverflow.Ellipsis,
                    labelStyle = MaterialTheme.typography.labelLarge,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedContainerColor = MaterialTheme.stylishComponentColors.groupedContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    contentSpacing = 6.dp,
                )
                DefaultStylishConnectedChip(
                    item = StylishConnectedChipItem("未選択チップ", onClick = {}),
                    modifier = Modifier.fillMaxWidth(),
                    shape = connectedShape(ConnectedCorners.Standalone),
                    outlineEdges = ConnectedEdges.All,
                    outlineCorners = ConnectedCorners.Standalone,
                    labelMaxLines = 1,
                    labelOverflow = TextOverflow.Ellipsis,
                    labelStyle = MaterialTheme.typography.labelLarge,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedContainerColor = MaterialTheme.stylishComponentColors.groupedContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    contentSpacing = 6.dp,
                )
                DefaultStylishConnectedChip(
                    item = StylishConnectedChipItem("無効なチップ", enabled = false),
                    modifier = Modifier.fillMaxWidth(),
                    shape = connectedShape(ConnectedCorners.Standalone),
                    outlineEdges = ConnectedEdges.All,
                    outlineCorners = ConnectedCorners.Standalone,
                    labelMaxLines = 1,
                    labelOverflow = TextOverflow.Ellipsis,
                    labelStyle = MaterialTheme.typography.labelLarge,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedContainerColor = MaterialTheme.stylishComponentColors.groupedContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    contentSpacing = 6.dp,
                )
            }
        },
    ),
    // ------------------------------------------------------------------
    // Atoms
    // ------------------------------------------------------------------
    DemoComponent(
        name = "Rhythm spacers",
        category = DemoCategory.Buttons,
        code = """Column {
    Block()
    StylishContentSpacer()      // 16 dp — コンテンツブロック間
    Block()
    StylishSectionSpacer()      // 32 dp — セクション間
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Settings, null)
        StylishInlineSpacer()   // 4 dp — アイコンとラベル
        Text("インライン")
        StylishHorizontalSpacer(spacing = 16.dp)
        Text("右端")
    }
    StylishItemSpacer()         // 8 dp — グループ内の項目間
    Block()
    StylishSpacer(spacing = 24.dp)
    Block()
}""",
        preview = {
            Column {
                SpacerBlock("ブロック A")
                StylishContentSpacer()
                SpacerBlock("ブロック B")
                StylishSectionSpacer()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(Icons.Default.Settings, contentDescription = null)
                    StylishInlineSpacer()
                    Text("インライン")
                    StylishHorizontalSpacer(spacing = 16.dp)
                    Text("右端")
                }
                StylishItemSpacer()
                SpacerBlock("グループ内の次の項目")
                StylishSpacer(spacing = 24.dp)
                SpacerBlock("カスタム 24 dp の間隔")
            }
        },
    ),
    DemoComponent(
        name = "Visually hidden announcement",
        category = DemoCategory.Buttons,
        code = """Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(Icons.Default.Notifications, contentDescription = null)
    StylishVisuallyHidden {
        Text("通知: 新着メッセージが3件あります")
    }
    Text("通知")
}""",
        preview = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                )
                StylishVisuallyHidden {
                    Text("通知: 新着メッセージが3件あります")
                }
                Text("通知アイコン（スクリーンリーダー用ラベルは非表示）")
            }
        },
    ),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun carouselCoveragePreview() {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val secondary = MaterialTheme.colorScheme.secondary
    val banners = remember(primary, tertiary, secondary) { listOf(primary, tertiary, secondary) }
    StylishCarousel(
        itemCount = { banners.size * 2 },
        preferredItemWidth = 180.dp,
        modifier = Modifier.fillMaxWidth(),
        itemSpacing = 8.dp,
    ) { index ->
        Box(
            Modifier
                .maskClip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                .background(banners[index % banners.size])
                .height(120.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "バナー ${index + 1}",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun timePickerDialogCoveragePreview() {
    var open by remember { mutableStateOf(false) }
    StylishButton(onClick = { open = true }, variant = StylishButtonVariant.Outlined) {
        Text("時刻を選択")
    }
    if (open) {
        StylishTimePickerDialog(
            onDismissRequest = { open = false },
            title = { Text("リマインダー時刻") },
            confirmButton = {
                TextButton(onClick = { open = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { open = false }) { Text("キャンセル") }
            },
        ) {
            StylishTimePicker()
        }
    }
}

@Composable
private fun SpacerBlock(label: String) {
    Surface(
        Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Text(label, Modifier.padding(12.dp))
    }
}

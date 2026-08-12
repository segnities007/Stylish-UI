package com.segnities007.stylishui.catalog

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.state.ToggleableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.components.atoms.StylishBadge
import com.segnities007.stylishui.components.atoms.StylishBadgedBox
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishCode
import com.segnities007.stylishui.components.atoms.StylishCard
import com.segnities007.stylishui.components.atoms.StylishCheckbox
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishChipVariant
import com.segnities007.stylishui.components.atoms.StylishCircularProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishDropdownMenu
import com.segnities007.stylishui.components.atoms.StylishDropdownMenuItem
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.atoms.StylishFilledIconButton
import com.segnities007.stylishui.components.atoms.StylishFilledTonalIconButton
import com.segnities007.stylishui.components.atoms.StylishFilledTextField
import com.segnities007.stylishui.components.atoms.StylishHorizontalDivider
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.components.atoms.StylishLinearProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishOutlinedIconButton
import com.segnities007.stylishui.components.atoms.StylishRadioButton
import com.segnities007.stylishui.components.atoms.StylishRangeSlider
import com.segnities007.stylishui.components.atoms.StylishRoundedIconButton
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.atoms.StylishSlider
import com.segnities007.stylishui.components.atoms.StylishSwitch
import com.segnities007.stylishui.components.atoms.StylishTriStateCheckbox
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.PieChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.charts.SimplePieChart
import com.segnities007.stylishui.components.charts.stylishChartColor
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishAlert
import com.segnities007.stylishui.components.molecules.StylishAutocomplete
import com.segnities007.stylishui.components.molecules.StylishDescriptions
import com.segnities007.stylishui.components.molecules.StylishDescriptionItem
import com.segnities007.stylishui.components.molecules.StylishConnectedCardColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardLazyColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.molecules.StylishConnectedChipColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedChipGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemRow
import com.segnities007.stylishui.components.molecules.StylishListItem
import com.segnities007.stylishui.components.molecules.StylishSkeletonCard
import com.segnities007.stylishui.components.molecules.StylishResult
import com.segnities007.stylishui.components.molecules.StylishResultVariant
import com.segnities007.stylishui.components.molecules.StylishToastData
import com.segnities007.stylishui.components.molecules.StylishToastHost
import com.segnities007.stylishui.components.molecules.StylishToastVariant
import com.segnities007.stylishui.components.molecules.rememberStylishToastHostState
import com.segnities007.stylishui.components.organisms.StylishAlertDialog
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.components.organisms.StylishPopconfirm
import com.segnities007.stylishui.components.organisms.StylishNavigationRail
import com.segnities007.stylishui.components.organisms.StylishNavigationRailItem
import com.segnities007.stylishui.components.organisms.StylishSearchBar
import com.segnities007.stylishui.components.organisms.StylishSegmentedButton
import com.segnities007.stylishui.components.organisms.StylishTabBar
import com.segnities007.stylishui.components.patterns.StylishBottomAppBar
import com.segnities007.stylishui.components.patterns.StylishCenterAlignedTopAppBar
import com.segnities007.stylishui.components.patterns.StylishLargeTopAppBar
import com.segnities007.stylishui.components.patterns.StylishTopAppBar
import com.segnities007.stylishui.theme.StylishTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComponentCatalog() {
    Surface {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // ── Atoms ──
            StylishSectionTitle("Atoms")

            StylishSectionTitle("StylishButton", textStyle = MaterialTheme.typography.titleSmall)
            StylishButton(onClick = {}) { Text("保存する") }
            StylishButton(onClick = {}, enabled = false) { Text("無効") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishButton(onClick = {}, variant = StylishButtonVariant.Tonal) { Text("Tonal") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Outlined) { Text("Outlined") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Text) { Text("Text") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Elevated) { Text("Elevated") }
            }
            var loading by remember { mutableStateOf(false) }
            StylishButton(onClick = { loading = !loading }, isLoading = loading) {
                Text(if (loading) "処理中…" else "非同期処理")
            }

            StylishSectionTitle("StylishChip", textStyle = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishChip(label = "通常", onClick = {})
                StylishChip(label = "選択中", onClick = {}, selected = true)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishChip(label = "Filter", onClick = {}, variant = StylishChipVariant.Filter, selected = true)
                StylishChip(label = "Input", onClick = {}, variant = StylishChipVariant.Input)
                StylishChip(label = "Suggestion", onClick = {}, variant = StylishChipVariant.Suggestion)
            }

            StylishSectionTitle("StylishSlider / StylishRangeSlider", textStyle = MaterialTheme.typography.titleSmall)
            var sliderValue by remember { mutableStateOf(0.5f) }
            StylishSlider(value = sliderValue, onValueChange = { sliderValue = it })
            var rangeValue by remember { mutableStateOf(0.2f..0.8f) }
            StylishRangeSlider(value = rangeValue, onValueChange = { rangeValue = it })

            StylishSectionTitle("StylishAvatar", textStyle = MaterialTheme.typography.titleSmall)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StylishAvatar(initials = "SM")
                StylishAvatar(initials = "SM", size = 56.dp)
            }

            StylishSectionTitle("StylishDropdownMenu", textStyle = MaterialTheme.typography.titleSmall)
            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                StylishButton(onClick = { menuExpanded = true }, variant = StylishButtonVariant.Outlined) {
                    Text("メニューを開く")
                }
                StylishDropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    StylishDropdownMenuItem(text = { Text("編集") }, onClick = { menuExpanded = false })
                    StylishDropdownMenuItem(text = { Text("削除") }, onClick = { menuExpanded = false })
                    StylishDropdownMenuItem(text = { Text("無効") }, onClick = {}, enabled = false)
                }
            }

            StylishSectionTitle("StylishCard", textStyle = MaterialTheme.typography.titleSmall)
            StylishCard(
                title = "Actionable",
                supportingText = "Click and elevation",
                onClick = {},
            )
            StylishCard(
                title = "Read only",
                supportingText = "No click or elevation",
            )
            StylishCard(
                onClick = {},
                minHeight = 96.dp,
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
                ) {
                    Text("Content mode", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "自由にレイアウトできるコンテンツモード",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            StylishSectionTitle("Selection Controls", textStyle = MaterialTheme.typography.titleSmall)
            var switchChecked by remember { mutableStateOf(true) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                StylishSwitch(checked = switchChecked, onCheckedChange = { switchChecked = it })
                StylishSwitch(checked = false, onCheckedChange = null, enabled = false)
            }
            var checkboxChecked by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                StylishCheckbox(checked = checkboxChecked, onCheckedChange = { checkboxChecked = it })
                StylishCheckbox(checked = true, onCheckedChange = null, enabled = false)
            }
            var radioSelected by remember { mutableIntStateOf(0) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                StylishRadioButton(selected = radioSelected == 0, onClick = { radioSelected = 0 })
                StylishRadioButton(selected = radioSelected == 1, onClick = { radioSelected = 1 })
            }

            StylishSectionTitle("Badge", textStyle = MaterialTheme.typography.titleSmall)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StylishBadge { Text("99+") }
                StylishBadge { Text("3") }
            }

            StylishSectionTitle("Icon Buttons", textStyle = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishIconButton(Icons.Default.Add, "追加", {})
                StylishIconButton(Icons.Default.Add, "アクティブ", {}, active = true)
                StylishRoundedIconButton(Icons.Default.Add, "追加", {})
                StylishFab(Icons.Default.Add, "追加", {})
            }

            StylishSectionTitle("Progress", textStyle = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishCircularProgressIndicator(Modifier.size(32.dp))
            }
            StylishLinearProgressIndicator(Modifier.fillMaxWidth())

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Connected Cards ──
            StylishSectionTitle("Connected Cards")
            StylishConnectedCardColumn(
                items = listOf(
                    StylishConnectedCardItem(
                        title = "Actionable",
                        supportingText = "Click and elevation",
                        onClick = {},
                    ),
                    StylishConnectedCardItem(
                        title = "Read only",
                        supportingText = "No click or elevation",
                    ),
                ),
            )
            StylishConnectedCardRow(
                items = listOf(
                    StylishConnectedCardItem(title = "Row A", onClick = {}),
                    StylishConnectedCardItem(title = "Row B"),
                ),
            )
            StylishConnectedCardGrid(
                items = listOf(
                    StylishConnectedCardItem(title = "Grid 1", onClick = {}),
                    StylishConnectedCardItem(title = "Grid 2"),
                    StylishConnectedCardItem(title = "Grid 3", onClick = {}),
                    StylishConnectedCardItem(title = "Grid 4"),
                ),
                columns = 2,
            )

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Connected Buttons ──
            StylishSectionTitle("Connected Buttons")
            StylishConnectedButtonRow(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("Enabled") },
                    StylishConnectedButtonItem { Text("No action") },
                ),
            )
            StylishConnectedButtonColumn(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("Column A") },
                    StylishConnectedButtonItem(onClick = {}) { Text("Column B") },
                ),
            )

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Connected Chips ──
            StylishSectionTitle("Connected Chips")
            StylishConnectedChipRow(
                items = listOf(
                    StylishConnectedChipItem("Selected", {}, selected = true),
                    StylishConnectedChipItem("Read only"),
                ),
            )
            StylishConnectedChipColumn(
                items = listOf(
                    StylishConnectedChipItem("Selected", {}, selected = true),
                    StylishConnectedChipItem("Read only"),
                ),
            )
            StylishConnectedChipGrid(
                items = listOf(
                    StylishConnectedChipItem("A", {}, selected = true),
                    StylishConnectedChipItem("B", {}),
                    StylishConnectedChipItem("C", {}),
                ),
                columns = 2,
            )

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Connected List Items ──
            StylishSectionTitle("Connected List Items")
            StylishConnectedListItemColumn(
                items = listOf(
                    StylishConnectedListItem("Actionable item", onClick = {}),
                    StylishConnectedListItem("Read-only item"),
                    StylishConnectedListItem("Disabled item", enabled = false),
                ),
            )
            StylishConnectedListItemRow(
                items = listOf(
                    StylishConnectedListItem("Item A", onClick = {}),
                    StylishConnectedListItem("Item B", onClick = {}),
                ),
            )
            StylishConnectedListItemGrid(
                items = listOf(
                    StylishConnectedListItem("Item A", onClick = {}),
                    StylishConnectedListItem("Item B", onClick = {}),
                    StylishConnectedListItem("Item C", onClick = {}),
                ),
                columns = 2,
            )

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Organisms ──
            StylishSectionTitle("Organisms")

            StylishSectionTitle("StylishTabBar", textStyle = MaterialTheme.typography.titleSmall)
            var selectedTab by remember { mutableIntStateOf(0) }
            StylishTabBar(
                tabs = listOf("概要", "記録", "統計"),
                selectedIndex = selectedTab,
                onSelectedChange = { selectedTab = it },
            )

            StylishSectionTitle("StylishNavigationBar", textStyle = MaterialTheme.typography.titleSmall)
            StylishNavigationBar(
                items = listOf(
                    StylishNavigationItem(Icons.Default.Home, "ホーム", selected = true),
                    StylishNavigationItem(Icons.Default.Search, "検索"),
                    StylishNavigationItem(Icons.Default.Settings, "設定"),
                ),
            )

            StylishSectionTitle("StylishListItem", textStyle = MaterialTheme.typography.titleSmall)
            StylishListItem(
                headline = "プレーンなリスト項目",
                supportingText = "Connected ではない単体の行",
                onClick = {},
            )

            StylishSectionTitle("StylishAlertDialog", textStyle = MaterialTheme.typography.titleSmall)
            var showDialog by remember { mutableStateOf(false) }
            StylishButton(onClick = { showDialog = true }, variant = StylishButtonVariant.Outlined) {
                Text("ダイアログを開く")
            }
            if (showDialog) {
                StylishAlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("車両を削除") },
                    text = { Text("この操作は取り消せません。") },
                    confirmButton = {
                        StylishButton(onClick = { showDialog = false }) { Text("削除") }
                    },
                    dismissButton = {
                        StylishButton(
                            onClick = { showDialog = false },
                            variant = StylishButtonVariant.Text,
                        ) { Text("キャンセル") }
                    },
                )
            }

            StylishSectionTitle("StylishSearchBar", textStyle = MaterialTheme.typography.titleSmall)
            var query by remember { mutableStateOf("") }
            var searchActive by remember { mutableStateOf(false) }
            StylishSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { searchActive = false },
                active = searchActive,
                onActiveChange = { searchActive = it },
                placeholder = { Text("検索") },
                leadingIcon = {
                    androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = null)
                },
            ) {
                Column {
                    Text("Stylish UI")
                    Text("Compose Multiplatform")
                }
            }

            StylishSectionTitle("StylishTopAppBar", textStyle = MaterialTheme.typography.titleSmall)
            StylishTopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = {}) {
                        androidx.compose.material3.Icon(Icons.Default.Add, contentDescription = "戻る")
                    }
                },
            )

            StylishSectionTitle("Lazy Connected List", textStyle = MaterialTheme.typography.titleSmall)
            StylishConnectedCardLazyColumn(
                items = List(20) { index ->
                    StylishConnectedCardItem(
                        title = "項目 $index",
                        supportingText = "遅延描画される Connected カード",
                        onClick = {},
                    )
                },
            )

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Charts ──
            StylishSectionTitle("Charts")
            SimplePieChart(
                contentDescriptionPrefix = "円グラフ",
                data = listOf(
                    PieChartData("燃料費", 35000f, stylishChartColor(0)),
                    PieChartData("保険", 15000f, stylishChartColor(1)),
                    PieChartData("メンテナンス", 8000f, stylishChartColor(2)),
                ),
            )
            SimpleBarChart(
                contentDescriptionPrefix = "棒グラフ",
                emptyLabel = "データがありません",
                data = listOf(
                    BarChartData("1月", 30000f),
                    BarChartData("2月", 45000f),
                    BarChartData("3月", 28000f),
                ),
            )
            SimpleLineChart(
                contentDescriptionPrefix = "折れ線グラフ",
                emptyLabel = "データがありません",
                data = listOf(
                    LineChartData("1月", 30000f),
                    LineChartData("2月", 45000f),
                    LineChartData("3月", 28000f),
                    LineChartData("4月", 52000f),
                ),
            )

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── M3 Parity (0.8) ──
            StylishSectionTitle("M3 Parity")

            StylishSectionTitle("BadgedBox / TriStateCheckbox", textStyle = MaterialTheme.typography.titleSmall)
            var triState by remember { mutableStateOf(ToggleableState.Indeterminate) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishBadgedBox(badge = { StylishBadge { Text("3") } }) {
                    androidx.compose.material3.Icon(Icons.Default.Home, contentDescription = "通知")
                }
                StylishTriStateCheckbox(state = triState, onClick = {
                    triState = when (triState) {
                        ToggleableState.On -> ToggleableState.Off
                        else -> ToggleableState.On
                    }
                })
            }

            StylishSectionTitle("Icon Button Variants", textStyle = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishFilledIconButton(onClick = {}) {
                    androidx.compose.material3.Icon(Icons.Default.Add, contentDescription = "追加")
                }
                StylishFilledTonalIconButton(onClick = {}) {
                    androidx.compose.material3.Icon(Icons.Default.Add, contentDescription = "追加")
                }
                StylishOutlinedIconButton(onClick = {}) {
                    androidx.compose.material3.Icon(Icons.Default.Add, contentDescription = "追加")
                }
            }

            StylishSectionTitle("Filled / Secure TextField", textStyle = MaterialTheme.typography.titleSmall)
            var filledValue by remember { mutableStateOf("") }
            StylishFilledTextField(value = filledValue, onValueChange = { filledValue = it }, label = "フィールド")

            StylishSectionTitle("ToggleButton", textStyle = MaterialTheme.typography.titleSmall)
            var toggleChecked by remember { mutableStateOf(false) }
            androidx.compose.material3.SingleChoiceSegmentedButtonRow {
                StylishSegmentedButton(selected = !toggleChecked, onClick = { toggleChecked = false }) { Text("オフ") }
                StylishSegmentedButton(selected = toggleChecked, onClick = { toggleChecked = true }) { Text("オン") }
            }

            StylishSectionTitle("NavigationRail", textStyle = MaterialTheme.typography.titleSmall)
            var railSelected by remember { mutableIntStateOf(0) }
            StylishNavigationRail {
                StylishNavigationRailItem(selected = railSelected == 0, onClick = { railSelected = 0 }, icon = { androidx.compose.material3.Icon(Icons.Default.Home, contentDescription = "ホーム") }, label = { Text("ホーム") })
                StylishNavigationRailItem(selected = railSelected == 1, onClick = { railSelected = 1 }, icon = { androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = "検索") }, label = { Text("検索") })
            }

            StylishSectionTitle("TopAppBar Variants", textStyle = MaterialTheme.typography.titleSmall)
            StylishCenterAlignedTopAppBar(title = { Text("中央寄せ") })
            StylishLargeTopAppBar(title = { Text("ラージ") })

            StylishSectionTitle("BottomAppBar", textStyle = MaterialTheme.typography.titleSmall)
            StylishBottomAppBar(actions = {
                androidx.compose.material3.IconButton(onClick = {}) {
                    androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = "検索")
                }
            })

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Web Parity 2 (0.9) ──
            StylishSectionTitle("Web Parity 2")

            StylishSectionTitle("Alert / Toast", textStyle = MaterialTheme.typography.titleSmall)
            StylishAlert(
                title = "お知らせ",
                message = "新しいバージョンが利用可能です。",
                onDismiss = {},
            )
            var toastVisible by remember { mutableStateOf(false) }
            StylishButton(onClick = { toastVisible = true }, variant = StylishButtonVariant.Outlined) {
                Text("トーストを表示")
            }
            val toastHostState = rememberStylishToastHostState()
            LaunchedEffect(toastVisible) {
                if (toastVisible) {
                    toastHostState.showToast(StylishToastData("保存しました", StylishToastVariant.Success))
                    toastVisible = false
                }
            }
            StylishToastHost(toastHostState)

            StylishSectionTitle("Result / Popconfirm", textStyle = MaterialTheme.typography.titleSmall)
            StylishResult(
                title = "送信が完了しました",
                description = "お問い合わせを受け付けました。",
                variant = StylishResultVariant.Success,
            )
            var confirmExpanded by remember { mutableStateOf(false) }
            StylishPopconfirm(
                expanded = confirmExpanded,
                onExpandedChange = { confirmExpanded = it },
                anchor = {
                    StylishButton(
                        onClick = { confirmExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                    ) { Text("削除") }
                },
                title = "この記録を削除しますか?",
                confirmLabel = "削除",
                onConfirm = {},
            )

            StylishSectionTitle("Descriptions / Autocomplete", textStyle = MaterialTheme.typography.titleSmall)
            StylishDescriptions(
                items = listOf(
                    StylishDescriptionItem("車両名", "Stylish Car"),
                    StylishDescriptionItem("年式", "2026"),
                    StylishDescriptionItem("色", "ホワイト"),
                    StylishDescriptionItem("走行距離", "12,000 km"),
                ),
            )
            var autocompleteValue by remember { mutableStateOf("") }
            StylishAutocomplete(
                value = autocompleteValue,
                onValueChange = { autocompleteValue = it },
                options = listOf("Stylish UI", "Compose Multiplatform", "Material 3", "Kotlin"),
                label = "ライブラリ検索",
            )

            StylishSectionTitle("Code / VisuallyHidden", textStyle = MaterialTheme.typography.titleSmall)
            StylishCode("implementation(\"io.github.segnities007:stylish-ui:0.8.0\")")

            StylishHorizontalDivider(Modifier.fillMaxWidth())

            // ── Skeleton ──
            StylishSectionTitle("Skeleton")
            StylishSkeletonCard(Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "StylishUI catalog · Light", showBackground = true, widthDp = 393)
@Composable
private fun ComponentCatalogLightPreview() {
    StylishTheme(darkTheme = false) {
        ComponentCatalog()
    }
}

@Preview(name = "StylishUI catalog · Dark", showBackground = true, widthDp = 393)
@Composable
private fun ComponentCatalogDarkPreview() {
    StylishTheme(darkTheme = true) {
        ComponentCatalog()
    }
}
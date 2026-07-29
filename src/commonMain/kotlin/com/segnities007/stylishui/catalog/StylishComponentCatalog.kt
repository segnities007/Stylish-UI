package com.segnities007.stylishui.catalog

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishCircularProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.atoms.StylishHorizontalDivider
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.components.atoms.StylishLinearProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishRoundedIconButton
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.charts.PieChartData
import com.segnities007.stylishui.components.charts.SimplePieChart
import com.segnities007.stylishui.components.charts.stylishChartColor
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishConnectedCardColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.molecules.StylishConnectedChipColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedChipGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemRow
import com.segnities007.stylishui.components.molecules.StylishSkeletonCard
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.components.organisms.StylishTabBar
import com.segnities007.stylishui.theme.StylishTheme

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

            StylishSectionTitle("StylishChip", textStyle = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishChip(label = "通常", onClick = {})
                StylishChip(label = "選択中", onClick = {}, selected = true)
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
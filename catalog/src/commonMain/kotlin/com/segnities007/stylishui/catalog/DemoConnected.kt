package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishConnectedIconButtonItem
import com.segnities007.stylishui.components.molecules.StylishAvatarItem
import com.segnities007.stylishui.components.molecules.StylishConnectedAvatarRow
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishConnectedCardColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.molecules.StylishConnectedChipColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.molecules.StylishConnectedIconButtonRow
import androidx.compose.material3.Text

/**
 * Returns all connected-component demo components for the catalog.
 */
internal fun getConnectedDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Connected cards (column)",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardColumn(
    items = listOf(
        StylishConnectedCardItem("タイトル", "説明", onClick = {}),
        StylishConnectedCardItem("タイトル", "説明"),
    ),
)""",
        preview = {
            StylishConnectedCardColumn(
                items = listOf(
                    StylishConnectedCardItem("Actionable", "クリックで選択状態に", onClick = {}),
                    StylishConnectedCardItem("Read only", "表示専用"),
                    StylishConnectedCardItem("Disabled", "無効", enabled = false),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Connected card row",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardRow(
    items = listOf(
        StylishConnectedCardItem("A", onClick = {}),
        StylishConnectedCardItem("B"),
    ),
)""",
        preview = {
            StylishConnectedCardRow(
                items = listOf(
                    StylishConnectedCardItem("Row A", "左", onClick = {}),
                    StylishConnectedCardItem("Row B", "右"),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Connected card grid",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardGrid(
    items = listOf(
        StylishConnectedCardItem("1", onClick = {}),
        StylishConnectedCardItem("2"),
        StylishConnectedCardItem("3", onClick = {}),
        StylishConnectedCardItem("4"),
    ),
    columns = 2,
)""",
        preview = {
            StylishConnectedCardGrid(
                items = listOf(
                    StylishConnectedCardItem("Grid 1", onClick = {}),
                    StylishConnectedCardItem("Grid 2"),
                    StylishConnectedCardItem("Grid 3", onClick = {}),
                    StylishConnectedCardItem("Grid 4"),
                ),
                columns = 2,
            )
        },
    ),
    DemoComponent(
        name = "Connected chips",
        category = DemoCategory.Connected,
        code = """StylishConnectedChipRow(
    items = listOf(
        StylishConnectedChipItem("選択中", onClick = {}, selected = true),
        StylishConnectedChipItem("通常", onClick = {}),
    ),
)""",
        preview = {
            var selected by remember { mutableIntStateOf(0) }
            StylishConnectedChipRow(
                items = listOf(
                    StylishConnectedChipItem("概要", onClick = { selected = 0 }, selected = selected == 0),
                    StylishConnectedChipItem("記録", onClick = { selected = 1 }, selected = selected == 1),
                    StylishConnectedChipItem("統計", onClick = { selected = 2 }, selected = selected == 2),
                ),
            )
            StylishConnectedChipColumn(
                items = listOf(
                    StylishConnectedChipItem("列チップ A", onClick = {}),
                    StylishConnectedChipItem("列チップ B", onClick = {}, selected = true),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Connected cards",
        category = DemoCategory.Connected,
        code = """StylishConnectedCardColumn(
    items = listOf(
        StylishConnectedCardItem(title = "見出し", supportingText = "説明", onClick = {}),
        StylishConnectedCardItem(title = "見出し", supportingText = "説明", enabled = false),
    ),
)""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishConnectedCardColumn(
                    items = listOf(
                        StylishConnectedCardItem(title = "Actionable", supportingText = "クリック可能な項目", onClick = {}),
                        StylishConnectedCardItem(title = "Read only", supportingText = "表示専用の項目"),
                        StylishConnectedCardItem(title = "Disabled", supportingText = "無効な項目", enabled = false),
                    ),
                )
                StylishConnectedCardRow(
                    items = listOf(
                        StylishConnectedCardItem(title = "A", onClick = {}),
                        StylishConnectedCardItem(title = "B", onClick = {}),
                    ),
                )
            }
        },
    ),
    DemoComponent(
        name = "Connected buttons",
        category = DemoCategory.Connected,
        code = """StylishConnectedButtonRow(
    items = listOf(
        StylishConnectedButtonItem(onClick = {}) { Text("OK") },
        StylishConnectedButtonItem(onClick = {}) { Text("キャンセル") },
    ),
)""",
        preview = {
            StylishConnectedButtonRow(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("保存") },
                    StylishConnectedButtonItem(onClick = {}) { Text("キャンセル") },
                ),
            )
            StylishConnectedButtonColumn(
                items = listOf(
                    StylishConnectedButtonItem(onClick = {}) { Text("全項目") },
                    StylishConnectedButtonItem(onClick = {}) { Text("選択項目") },
                ),
            )
        },
    ),
    DemoComponent(
        name = "Connected icon buttons",
        category = DemoCategory.Connected,
        code = """StylishConnectedIconButtonRow(
    items = listOf(
        StylishConnectedIconButtonItem(Icons.Default.Settings, "設定1", onClick = {}),
        StylishConnectedIconButtonItem(Icons.Default.Settings, "設定2", onClick = {}),
        StylishConnectedIconButtonItem(Icons.Default.Settings, "設定3", onClick = {}),
        StylishConnectedIconButtonItem(Icons.Default.Search, "検索", onClick = {}, active = true),
    ),
)""",
        preview = {
            StylishConnectedIconButtonRow(
                items = listOf(
                    StylishConnectedIconButtonItem(Icons.Default.Settings, "設定1", onClick = {}),
                    StylishConnectedIconButtonItem(Icons.Default.Settings, "設定2", onClick = {}),
                    StylishConnectedIconButtonItem(Icons.Default.Settings, "設定3", onClick = {}),
                    StylishConnectedIconButtonItem(Icons.Default.Search, "検索", onClick = {}, active = true),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Connected avatar row",
        category = DemoCategory.Connected,
        code = """StylishConnectedAvatarRow(
    items = listOf(
        StylishAvatarItem("AB"),
        StylishAvatarItem("CD"),
        StylishAvatarItem("EF"),
    ),
)""",
        preview = {
            StylishConnectedAvatarRow(
                items = listOf(
                    StylishAvatarItem("AB"),
                    StylishAvatarItem("CD"),
                    StylishAvatarItem("EF"),
                    StylishAvatarItem("GH"),
                    StylishAvatarItem("IJ"),
                    StylishAvatarItem("KL"),
                ),
                maxVisible = 4,
            )
        },
    ),
)

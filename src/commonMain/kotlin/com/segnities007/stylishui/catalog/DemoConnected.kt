package com.segnities007.stylishui.catalog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishConnectedCardColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.molecules.StylishConnectedChipColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemRow

@Composable
internal fun DemoConnected(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Connected cards",
        description = "連結カードの行・列・グリッド。",
        code = """StylishConnectedCardColumn(
    items = listOf(
        StylishConnectedCardItem("タイトル", "説明", onClick = {}),
        StylishConnectedCardItem("タイトル", "説明"),
    ),
)""",
        modifier = modifier,
    ) {
        var selected by remember { mutableIntStateOf(0) }
        StylishConnectedCardColumn(
            items = listOf(
                StylishConnectedCardItem("Actionable", "クリックで選択状態に", onClick = { selected = 0 }),
                StylishConnectedCardItem("Read only", "表示専用"),
                StylishConnectedCardItem("Disabled", "無効", enabled = false),
            ),
        )
    }

    StylishDemoCard(
        title = "Connected card row / grid",
        description = "横並びとグリッド。",
        code = """StylishConnectedCardRow(
    items = listOf(
        StylishConnectedCardItem("A", onClick = {}),
        StylishConnectedCardItem("B"),
    ),
)
StylishConnectedCardGrid(items = listOf(...), columns = 2)""",
        modifier = modifier,
    ) {
        StylishConnectedCardRow(
            items = listOf(
                StylishConnectedCardItem("Row A", "左", onClick = {}),
                StylishConnectedCardItem("Row B", "右"),
            ),
        )
        StylishConnectedCardGrid(
            items = listOf(
                StylishConnectedCardItem("Grid 1", onClick = {}),
                StylishConnectedCardItem("Grid 2"),
                StylishConnectedCardItem("Grid 3", onClick = {}),
                StylishConnectedCardItem("Grid 4"),
            ),
            columns = 2,
        )
    }

    StylishDemoCard(
        title = "Connected chips",
        description = "連結チップの行・列。",
        code = """StylishConnectedChipRow(
    items = listOf(
        StylishConnectedChipItem("選択中", onClick = {}, selected = true),
        StylishConnectedChipItem("通常", onClick = {}),
    ),
)""",
        modifier = modifier,
    ) {
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
    }

    StylishDemoCard(
        title = "Connected list items",
        description = "連結リストの列・行。",
        code = """StylishConnectedListItemColumn(
    items = listOf(
        StylishConnectedListItem("見出し", "説明", onClick = {}),
        StylishConnectedListItem("見出し", "説明", enabled = false),
    ),
)""",
        modifier = modifier,
    ) {
        StylishConnectedListItemColumn(
            items = listOf(
                StylishConnectedListItem("Actionable", "クリック可能な項目", onClick = {}),
                StylishConnectedListItem("Read only", "表示専用の項目"),
                StylishConnectedListItem("Disabled", "無効な項目", enabled = false),
            ),
        )
        StylishConnectedListItemRow(
            items = listOf(
                StylishConnectedListItem("A", onClick = {}),
                StylishConnectedListItem("B", onClick = {}),
            ),
        )
    }

    StylishDemoCard(
        title = "Connected buttons",
        description = "連結ボタンの行・列。",
        code = """StylishConnectedButtonRow(
    items = listOf(
        StylishConnectedButtonItem(onClick = {}) { Text("OK") },
        StylishConnectedButtonItem(onClick = {}) { Text("キャンセル") },
    ),
)""",
        modifier = modifier,
    ) {
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
    }
}

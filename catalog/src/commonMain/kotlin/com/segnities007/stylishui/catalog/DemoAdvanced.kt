package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.charts.StylishChartSelection
import com.segnities007.stylishui.components.charts.StylishLineSeries
import com.segnities007.stylishui.components.charts.StylishMultiSeriesLineChart
import com.segnities007.stylishui.components.organisms.StylishColorPicker
import com.segnities007.stylishui.components.organisms.StylishContextMenu
import com.segnities007.stylishui.components.organisms.StylishDataTable
import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.components.organisms.StylishDataTableSortState
import com.segnities007.stylishui.components.organisms.StylishMenu
import com.segnities007.stylishui.components.organisms.StylishMenuItem
import com.segnities007.stylishui.components.organisms.StylishMenubar
import com.segnities007.stylishui.components.organisms.StylishQrCode
import com.segnities007.stylishui.components.organisms.StylishScrollArea
import com.segnities007.stylishui.components.organisms.StylishTransfer
import com.segnities007.stylishui.components.organisms.StylishTransferItem
import com.segnities007.stylishui.components.organisms.StylishTree
import com.segnities007.stylishui.components.organisms.StylishTreeNode
import com.segnities007.stylishui.components.organisms.StylishUpload
import com.segnities007.stylishui.components.organisms.StylishUploadFile

/**
 * Advanced, data-rich and platform-adapter demos.
 *
 * These previews intentionally keep their state local so the catalog is useful as a
 * copy/paste playground: every item can be operated without a backend or an OS picker.
 */
internal fun getAdvancedDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Data table: sort / select / resize",
        category = DemoCategory.Advanced,
        code = """StylishDataTable(
    rows = rows,
    rowKey = { it.id },
    columns = listOf(
        StylishDataTableColumn("name", "名前", comparator = compareBy { it.name }, exportValue = { it.name }) { Text(it.name) },
        StylishDataTableColumn("status", "状態", exportValue = { it.status }) { Text(it.status) },
    ),
    sortState = sortState,
    onSortStateChange = { sortState = it },
    selectedKeys = selectedKeys,
    onSelectedKeysChange = { selectedKeys = it },
    columnWidths = widths,
    onColumnWidthsChange = { widths = it },
)""",
        preview = {
            dataTablePreview()
        },
    ),
    DemoComponent(
        name = "Tree: expansion / keyboard",
        category = DemoCategory.Advanced,
        code = """StylishTree(
    nodes = nodes,
    expandedIds = expandedIds,
    onExpandedIdsChange = { expandedIds = it },
    selectedId = selectedId,
    onSelectedIdChange = { selectedId = it },
)""",
        preview = {
            val nodes = remember {
                listOf(
                    StylishTreeNode("src", "src", "src", listOf(StylishTreeNode("main", "main", "main"))),
                    StylishTreeNode("docs", "docs", "docs"),
                )
            }
            var expandedIds by remember { mutableStateOf<Set<Any>>(setOf("src")) }
            var selectedId by remember { mutableStateOf<Any?>(null) }
            StylishTree(nodes, expandedIds = expandedIds, onExpandedIdsChange = { expandedIds = it }, selectedId = selectedId, onSelectedIdChange = { selectedId = it })
        },
    ),
    DemoComponent(
        name = "Transfer list",
        category = DemoCategory.Advanced,
        code = """StylishTransfer(
    available = items,
    selectedKeys = selectedKeys,
    onSelectedKeysChange = { selectedKeys = it },
)""",
        preview = {
            val items = remember { listOf("design", "tokens", "a11y", "charts").map { StylishTransferItem(it, it, it) } }
            var selectedKeys by remember { mutableStateOf<Set<Any>>(setOf("tokens")) }
            StylishTransfer(items, selectedKeys, onSelectedKeysChange = { selectedKeys = it }, modifier = Modifier.fillMaxWidth())
        },
    ),
    DemoComponent(
        name = "Upload adapter",
        category = DemoCategory.Advanced,
        code = """StylishUpload(
    files = files,
    onFilesChange = { files = it },
    onRequestFiles = { platformFilePicker.launch() },
)""",
        preview = {
            var files by remember { mutableStateOf(listOf(StylishUploadFile("tokens.json", 3_024, "application/json"))) }
            StylishUpload(files, onFilesChange = { files = it }, onRequestFiles = { files = files + StylishUploadFile("new-file.txt", 512, "text/plain") })
        },
    ),
    DemoComponent(
        name = "Color picker",
        category = DemoCategory.Advanced,
        code = """var color by remember { mutableStateOf(Color(0.2f, 0.5f, 0.9f)) }
StylishColorPicker(color = color, onColorChange = { color = it })""",
        preview = {
            var color by remember { mutableStateOf(Color(0.2f, 0.5f, 0.9f)) }
            StylishColorPicker(color = color, onColorChange = { color = it })
        },
    ),
    DemoComponent(
        name = "QR code matrix",
        category = DemoCategory.Advanced,
        code = """StylishQrCode(
    matrix = matrixFromEncoder,
    contentDescription = \"招待コード\",
)""",
        preview = {
            val matrix = remember {
                listOf(
                    listOf(true, true, true, false, true, true, true),
                    listOf(true, false, true, false, true, false, true),
                    listOf(true, true, true, false, true, true, true),
                    listOf(false, false, false, true, false, false, false),
                    listOf(true, true, true, false, true, false, true),
                    listOf(true, false, true, false, false, true, true),
                    listOf(true, true, true, false, true, true, true),
                )
            }
            StylishQrCode(matrix, modifier = Modifier.size(56.dp), contentDescription = "招待コード")
        },
    ),
    DemoComponent(
        name = "Context menu",
        category = DemoCategory.Advanced,
        code = """StylishContextMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false },
    items = listOf(StylishMenuItem(\"編集\") { onEdit() }),
) { Text(\"右クリック対象\") }""",
        preview = {
            var expanded by remember { mutableStateOf(false) }
            StylishContextMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                items = listOf(
                    StylishMenuItem("編集", { expanded = false }),
                    StylishMenuItem("複製", { expanded = false }),
                ),
            ) {
                Button(onClick = { expanded = true }) { Text("メニューを開く") }
            }
        },
    ),
    DemoComponent(
        name = "Menubar",
        category = DemoCategory.Advanced,
        code = """StylishMenubar(
    menus = listOf(
        StylishMenu(\"ファイル\", listOf(StylishMenuItem(\"新規\") { onNew() })),
        StylishMenu(\"表示\", listOf(StylishMenuItem(\"拡大\") { onZoom() })),
    ),
)""",
        preview = {
            StylishMenubar(
                menus = listOf(
                    StylishMenu("ファイル", listOf(StylishMenuItem("新規", {}))),
                    StylishMenu("表示", listOf(StylishMenuItem("拡大", {}))),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Multi-series chart",
        category = DemoCategory.Advanced,
        code = """StylishMultiSeriesLineChart(
    labels = labels,
    series = series,
    contentDescriptionPrefix = \"月次推移\",
    showAxisTicks = true,
    onSelectionChange = { selection = it },
)""",
        preview = {
            var selection by remember { mutableStateOf<StylishChartSelection?>(null) }
            StylishMultiSeriesLineChart(
                labels = listOf("1月", "2月", "3月", "4月"),
                series = listOf(
                    StylishLineSeries("売上", listOf(12f, 18f, 15f, 23f), Color(0xFF4F6BED)),
                    StylishLineSeries("利益", listOf(6f, 9f, 8f, 14f), Color(0xFF00A884)),
                ),
                contentDescriptionPrefix = "月次推移",
                chartHeight = 180.dp,
                showAxisTicks = true,
                onSelectionChange = { selection = it },
                selection = selection,
            )
        },
    ),
)

private data class DemoTableRow(val id: Int, val name: String, val status: String)

@Composable
private fun dataTablePreview() {
    val rows = remember {
        listOf(
            DemoTableRow(1, "Design tokens", "Ready"),
            DemoTableRow(2, "Keyboard QA", "Review"),
            DemoTableRow(3, "Wasm bundle", "Ready"),
        )
    }
    var selectedKeys by remember { mutableStateOf<Set<Any>>(emptySet()) }
    var sortState by remember { mutableStateOf<StylishDataTableSortState?>(null) }
    var widths by remember { mutableStateOf(mapOf("name" to 148.dp, "status" to 96.dp)) }
    StylishDataTable(
        rows = rows,
        columns = listOf(
            StylishDataTableColumn("name", "名前", comparator = compareBy { it.name }, exportValue = { it.name }) { Text(it.name) },
            StylishDataTableColumn("status", "状態", exportValue = { it.status }) { Text(it.status) },
        ),
        rowKey = { it.id },
        sortState = sortState,
        onSortStateChange = { sortState = it },
        selectedKeys = selectedKeys,
        onSelectedKeysChange = { selectedKeys = it },
        columnWidths = widths,
        onColumnWidthsChange = { widths = it },
        modifier = Modifier.fillMaxWidth(),
    )
}

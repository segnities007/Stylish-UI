package com.segnities007.stylishui.androidruntime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.organisms.StylishDataTableCellPosition
import com.segnities007.stylishui.components.organisms.StylishDataTable
import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.theme.StylishTheme

private data class RuntimeRow(val id: Int, val name: String)

/** Small Android consumer screen used by the runtime evidence script. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StylishTheme(darkTheme = false) {
                val rows = remember { (1..20).map { RuntimeRow(it, "Runtime row $it") } }
                // The data table publishes its full accessibility semantics only
                // when the corresponding interactions are connected: the filter
                // field, pagination, row selection, and per-cell content
                // descriptions each require their hoisted handler, and the
                // runtime evidence script asserts those semantics.
                var filterText by remember { mutableStateOf("") }
                var page by remember { mutableIntStateOf(1) }
                var selectedKeys by remember { mutableStateOf<Set<Any>>(emptySet()) }
                var focusedCell by remember { mutableStateOf<StylishDataTableCellPosition?>(null) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .testTag("android_runtime_root"),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Stylish UI Android runtime", style = MaterialTheme.typography.headlineSmall)
                    Text("state page=1 size=10", modifier = Modifier.testTag("android_runtime_state"))
                    StylishDataTable(
                        rows = rows,
                        columns = listOf(
                            StylishDataTableColumn<RuntimeRow>("id", "ID", comparator = compareBy { it.id }) { Text(it.id.toString()) },
                            StylishDataTableColumn<RuntimeRow>("name", "Name") { Text(it.name) },
                        ),
                        rowKey = { it.id },
                        pageSize = 10,
                        filterText = filterText,
                        onFilterTextChange = { filterText = it },
                        page = page,
                        onPageChange = { page = it },
                        selectedKeys = selectedKeys,
                        onSelectedKeysChange = { selectedKeys = it },
                        focusedCell = focusedCell,
                        onFocusedCellChange = { focusedCell = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("android_runtime_table"),
                    )
                }
            }
        }
    }
}

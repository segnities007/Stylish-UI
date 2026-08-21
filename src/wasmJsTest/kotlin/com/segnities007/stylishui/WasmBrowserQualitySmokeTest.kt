package com.segnities007.stylishui

import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.components.organisms.StylishDataTableSortDirection
import com.segnities007.stylishui.components.organisms.StylishDataTableSortState
import com.segnities007.stylishui.components.organisms.resolveStylishDataTableRows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A real Wasm browser test (not just a compile task) for the platform-neutral
 * data pipeline used by the UI. This keeps the browser job meaningful even on
 * hosts where Compose screenshot tooling is unavailable.
 */
class WasmBrowserQualitySmokeTest {
    @Test
    fun browserExecutesDeterministicTablePipeline() {
        val columns = listOf(
            StylishDataTableColumn<Int>("value", "Value", comparator = compareBy { it }, cell = {}),
        )
        val result = resolveStylishDataTableRows(
            rows = (1..100).toList(),
            columns = columns,
            filterText = "1",
            filterPredicate = { value, query -> value.toString().contains(query) },
            sortStates = listOf(StylishDataTableSortState("value", StylishDataTableSortDirection.Descending)),
            page = 1,
            pageSize = 10,
        )
        assertTrue(result.visibleRows.isNotEmpty())
        assertEquals(20, result.filteredRows.size)
        assertEquals(10, result.visibleRows.size)
        assertEquals(2, result.pageCount)
        assertTrue(result.visibleRows.all { it.toString().contains("1") })
    }
}

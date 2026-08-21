package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.components.organisms.StylishDataTableSortDirection
import com.segnities007.stylishui.components.organisms.StylishDataTableSortState
import com.segnities007.stylishui.components.organisms.resolveStylishDataTableRows
import kotlin.test.Test
import kotlin.test.assertEquals

class StylishDataTableEngineTest {
    private val columns = listOf(
        StylishDataTableColumn<Int>("value", "Value", comparator = compareBy { it }, cell = {}),
    )

    @Test
    fun queryPipelineIsDeterministic() {
        val result = resolveStylishDataTableRows(
            rows = (1..10).toList(),
            columns = columns,
            filterText = "1",
            filterPredicate = { row, query -> row.toString().contains(query) },
            sortStates = listOf(StylishDataTableSortState("value", StylishDataTableSortDirection.Descending)),
            page = 1,
            pageSize = 2,
        )
        assertEquals(listOf(1, 10), result.filteredRows)
        assertEquals(listOf(10, 1), result.sortedRows)
        assertEquals(listOf(10, 1), result.visibleRows)
        assertEquals(1, result.pageCount)
    }

    @Test
    fun pageIsClampedAndRowsAreVirtualizedByPage() {
        val result = resolveStylishDataTableRows(
            rows = (1..10).toList(),
            columns = columns,
            page = 99,
            pageSize = 3,
        )
        assertEquals(4, result.page)
        assertEquals(4, result.pageCount)
        assertEquals(listOf(10), result.visibleRows)
    }

    @Test
    fun largeCollectionsKeepTheRenderedPageBounded() {
        val result = resolveStylishDataTableRows(
            rows = (0 until 10_000).toList(),
            columns = columns,
            page = 20,
            pageSize = 50,
        )
        assertEquals(50, result.visibleRows.size)
        assertEquals(950, result.visibleRows.first())
        assertEquals(10_000, result.sortedRows.size)
    }

    @Test
    fun equalSortKeysRetainAStableInputOrderAcrossTargets() {
        val result = resolveStylishDataTableRows(
            rows = listOf(2, 1, 4, 3),
            columns = listOf(
                StylishDataTableColumn<Int>("parity", "Parity", comparator = compareBy { it % 2 }, cell = {}),
            ),
            sortStates = listOf(StylishDataTableSortState("parity")),
        )

        assertEquals(listOf(2, 4, 1, 3), result.sortedRows)
    }
}

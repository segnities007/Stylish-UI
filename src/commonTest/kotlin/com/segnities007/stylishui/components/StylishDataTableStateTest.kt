package com.segnities007.stylishui.components

import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.organisms.StylishDataTableAction
import com.segnities007.stylishui.components.organisms.StylishDataTableCellPosition
import com.segnities007.stylishui.components.organisms.StylishDataTableSortDirection
import com.segnities007.stylishui.components.organisms.StylishDataTableSortState
import com.segnities007.stylishui.components.organisms.StylishDataTableState
import com.segnities007.stylishui.components.organisms.reduce
import kotlin.test.Test
import kotlin.test.assertEquals

class StylishDataTableStateTest {
    @Test
    fun reducerNormalizesAndKeepsDistinctColumns() {
        val state = StylishDataTableState()
            .reduce(StylishDataTableAction.PageChanged(0))
            .reduce(StylishDataTableAction.PageSizeChanged(0))
            .reduce(
                StylishDataTableAction.SortChanged(
                    listOf(
                        StylishDataTableSortState("name"),
                        StylishDataTableSortState("name", StylishDataTableSortDirection.Descending),
                    ),
                ),
            )
            .reduce(StylishDataTableAction.ColumnOrderChanged(listOf("name", "name", "value")))

        assertEquals(1, state.page)
        assertEquals(null, state.pageSize)
        assertEquals(1, state.sortStates.size)
        assertEquals(listOf("name", "value"), state.columnOrder)
    }

    @Test
    fun reducerHoistsInteractionAndLayoutState() {
        val focused = StylishDataTableCellPosition(2, "value")
        val state = StylishDataTableState()
            .reduce(StylishDataTableAction.FilterChanged("card"))
            .reduce(StylishDataTableAction.SelectionChanged(setOf("row-1")))
            .reduce(StylishDataTableAction.ExpansionChanged(setOf("row-1")))
            .reduce(StylishDataTableAction.ColumnWidthsChanged(mapOf("value" to 160.dp)))
            .reduce(StylishDataTableAction.FocusChanged(focused))

        assertEquals("card", state.filterText)
        assertEquals(setOf("row-1"), state.selectedKeys)
        assertEquals(setOf("row-1"), state.expandedKeys)
        assertEquals(160.dp, state.columnWidths["value"])
        assertEquals(focused, state.focusedCell)
    }
}

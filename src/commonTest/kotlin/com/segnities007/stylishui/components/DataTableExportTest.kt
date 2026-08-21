package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.components.organisms.StylishDataTableExportFormat
import com.segnities007.stylishui.components.organisms.exportStylishDataTable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataTableExportTest {
    private data class Row(val id: Int, val name: String)

    @Test
    fun csvAndJsonEscapeValues() {
        val columns = listOf(
            StylishDataTableColumn<Row>("id", "ID", exportValue = { it.id }) { },
            StylishDataTableColumn<Row>("name", "Name", exportValue = { it.name }) { },
        )
        val rows = listOf(Row(1, "A, \"quoted\""))
        val csv = exportStylishDataTable(rows, columns)
        val json = exportStylishDataTable(rows, columns, StylishDataTableExportFormat.Json)
        assertTrue(csv.contains("\"A, \"\"quoted\"\"\""))
        assertTrue(json.contains("A, \\\"quoted\\\""))
    }

    @Test
    fun tsvQuotesTabsAndNewlines() {
        val columns = listOf(
            StylishDataTableColumn<Row>("id", "ID", exportValue = { it.id }) { },
            StylishDataTableColumn<Row>("name", "Name", exportValue = { it.name }) { },
        )

        val tsv = exportStylishDataTable(
            rows = listOf(Row(1, "first\tsecond\nthird")),
            columns = columns,
            format = StylishDataTableExportFormat.Tsv,
        )

        assertEquals("ID\tName\n1\t\"first\tsecond\nthird\"\n", tsv)
    }

    @Test
    fun jsonRetainsPrimitiveValuesAndEscapesControlCharacters() {
        data class TypedRow(val count: Int, val enabled: Boolean, val note: String?, val missing: String)
        val columns = listOf(
            StylishDataTableColumn<TypedRow>("count", "Count", exportValue = { it.count }) { },
            StylishDataTableColumn<TypedRow>("enabled", "Enabled", exportValue = { it.enabled }) { },
            StylishDataTableColumn<TypedRow>("note", "Note", exportValue = { it.note }) { },
            StylishDataTableColumn<TypedRow>("missing", "Missing") { },
        )

        val json = exportStylishDataTable(
            rows = listOf(TypedRow(2, true, "line\u0001\n", "ignored")),
            columns = columns,
            format = StylishDataTableExportFormat.Json,
        )

        assertEquals("[{\"count\":2,\"enabled\":true,\"note\":\"line\\u0001\\n\",\"missing\":\"\"}]", json)
    }
}

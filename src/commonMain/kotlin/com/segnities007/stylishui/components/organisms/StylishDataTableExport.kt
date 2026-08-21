package com.segnities007.stylishui.components.organisms

/** Text export formats supported by [exportStylishDataTable]. */
public enum class StylishDataTableExportFormat { Csv, Tsv, Json }

private data class ExportedDataTableValue(val value: Any?, val hasAdapter: Boolean)

/**
 * Serializes rows using the column-level [StylishDataTableColumn.exportValue] adapters.
 * Columns without an adapter are exported as an empty value rather than introspecting arbitrary
 * Kotlin objects. This keeps the common API deterministic on every target.
 */
public fun <T> exportStylishDataTable(
    rows: List<T>,
    columns: List<StylishDataTableColumn<T>>,
    format: StylishDataTableExportFormat = StylishDataTableExportFormat.Csv,
): String {
    val values = rows.map { row ->
        columns.map { column ->
            ExportedDataTableValue(column.exportValue?.invoke(row), column.exportValue != null)
        }
    }
    return when (format) {
        StylishDataTableExportFormat.Json -> buildString {
            append('[')
            values.forEachIndexed { rowIndex, row ->
                if (rowIndex > 0) append(',')
                append('{')
                row.forEachIndexed { columnIndex, value ->
                    if (columnIndex > 0) append(',')
                    append('"').append(jsonEscape(columns[columnIndex].id)).append('"').append(':')
                    append(jsonValue(value.value, value.hasAdapter))
                }
                append('}')
            }
            append(']')
        }
        StylishDataTableExportFormat.Csv, StylishDataTableExportFormat.Tsv -> {
            val separator = if (format == StylishDataTableExportFormat.Csv) ',' else '\t'
            buildString {
                append(columns.joinToString(separator.toString()) { quoteDelimited(it.title, separator) })
                append('\n')
                values.forEach { row ->
                    append(row.joinToString(separator.toString()) { quoteDelimited(it.value?.toString().orEmpty(), separator) })
                    append('\n')
                }
            }
        }
    }
}

private fun quoteDelimited(value: String, separator: Char): String {
    val escaped = value.replace("\"", "\"\"")
    return if (escaped.any { it == separator || it == '\n' || it == '\r' || it == '"' }) "\"$escaped\"" else escaped
}

/**
 * Emits JSON primitives without relying on a platform JSON implementation. Values returned by
 * [StylishDataTableColumn.exportValue] retain their primitive type in JSON; all other values are
 * represented as strings. Non-finite floating point values are emitted as `null`, as required by
 * the JSON grammar.
 */
private fun jsonValue(value: Any?, hasAdapter: Boolean): String = when {
    !hasAdapter -> "\"\""
    value == null -> "null"
    value is Boolean -> value.toString()
    value is Byte || value is Short || value is Int || value is Long -> value.toString()
    value is Float -> if (value.isFinite()) value.toString() else "null"
    value is Double -> if (value.isFinite()) value.toString() else "null"
    value is Number -> value.toString()
    else -> "\"${jsonEscape(value.toString())}\""
}

private fun jsonEscape(value: String): String = buildString {
    value.forEach { character ->
        when (character) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\b' -> append("\\b")
            '\u000C' -> append("\\f")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> if (character.code < 0x20) {
                append("\\u")
                append(character.code.toString(16).padStart(4, '0'))
            } else append(character)
        }
    }
}

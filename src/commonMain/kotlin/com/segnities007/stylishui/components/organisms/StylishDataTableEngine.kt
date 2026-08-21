package com.segnities007.stylishui.components.organisms

import androidx.compose.runtime.Immutable

/** Pure, UI-free result of filtering, sorting, and paging a data-table collection. */
@Immutable
public data class StylishDataTableEngineResult<T>(
    public val filteredRows: List<T>,
    public val sortedRows: List<T>,
    public val visibleRows: List<T>,
    public val page: Int,
    public val pageCount: Int,
)

/**
 * Applies the data-table query pipeline without Compose state or platform APIs.
 *
 * Keeping this pipeline pure makes it reusable by server-backed adapters, deterministic in tests,
 * and cheap to benchmark independently from layout and rendering.
 */
public fun <T> resolveStylishDataTableRows(
    rows: List<T>,
    columns: List<StylishDataTableColumn<T>>,
    filterText: String = "",
    filterPredicate: ((T, String) -> Boolean)? = null,
    sortStates: List<StylishDataTableSortState> = emptyList(),
    page: Int = 1,
    pageSize: Int? = null,
): StylishDataTableEngineResult<T> {
    val filteredRows = if (filterText.isBlank() || filterPredicate == null) {
        rows
    } else {
        rows.filter { filterPredicate(it, filterText) }
    }
    val sortedRows = if (sortStates.isEmpty()) {
        filteredRows
    } else {
        // Include the original index as a final key. This makes equal rows deterministic on every
        // target, instead of depending on the stability details of a platform's List.sort.
        filteredRows.withIndex().sortedWith(Comparator { first, second ->
            val valueComparison = sortStates.asSequence().mapNotNull { state ->
                columns.firstOrNull { it.id == state.columnId }?.comparator?.let { comparator ->
                    val result = comparator.compare(first.value, second.value)
                    if (state.direction == StylishDataTableSortDirection.Descending) -result else result
                }
            }.firstOrNull { it != 0 } ?: 0
            if (valueComparison != 0) valueComparison else first.index.compareTo(second.index)
        }).map { it.value }
    }
    val normalizedPageSize = pageSize?.takeIf { it > 0 }
    val pageCount = normalizedPageSize?.let { ((sortedRows.size - 1).coerceAtLeast(0) / it) + 1 } ?: 1
    val safePage = page.coerceIn(1, pageCount)
    val visibleRows = normalizedPageSize?.let { size ->
        sortedRows.drop((safePage - 1) * size).take(size)
    } ?: sortedRows
    return StylishDataTableEngineResult(
        filteredRows = filteredRows,
        sortedRows = sortedRows,
        visibleRows = visibleRows,
        page = safePage,
        pageCount = pageCount,
    )
}

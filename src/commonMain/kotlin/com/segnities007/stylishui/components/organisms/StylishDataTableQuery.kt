package com.segnities007.stylishui.components.organisms

import androidx.compose.runtime.Immutable

/**
 * Serializable-agnostic query state for server-backed tables.
 *
 * The model intentionally contains only primitives and the library's sort state, so an
 * application can map it to REST, GraphQL, SQL, or a local cache without coupling the UI to a
 * transport library.
 */
@Immutable
public data class StylishDataTableQuery(
    public val filter: String = "",
    public val page: Int = 1,
    public val pageSize: Int = 25,
    public val sort: List<StylishDataTableSortState> = emptyList(),
    public val visibleColumnIds: List<String> = emptyList(),
    public val columnOrder: List<String> = emptyList(),
) {
    public val normalizedPage: Int get() = page.coerceAtLeast(1)
    public val normalizedPageSize: Int get() = pageSize.coerceAtLeast(1)
}

/** A page returned by a server-backed [StylishDataTableQuery]. */
@Immutable
public data class StylishDataTableQueryResult<T>(
    public val rows: List<T>,
    public val totalRowCount: Int? = null,
    public val hasNextPage: Boolean? = null,
)

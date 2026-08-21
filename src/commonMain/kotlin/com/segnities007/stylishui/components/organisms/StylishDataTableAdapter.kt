package com.segnities007.stylishui.components.organisms

/**
 * Platform- and transport-neutral boundary for server-backed data tables.
 *
 * Implementations may use REST, GraphQL, SQL, a local database, or an in-memory cache. The
 * adapter owns cancellation, retries, authentication, and error mapping; Stylish UI only sends
 * the immutable query and renders the returned page. Keeping this contract in common code lets
 * Android, iOS, desktop, and Web hosts share the same table state without pulling a transport
 * dependency into the UI library.
 *
 * Adapter implementations should treat [StylishDataTableQuery.normalizedPage] and
 * [StylishDataTableQuery.normalizedPageSize] as the canonical paging values. Exceptions are
 * intentionally not swallowed so a host can map failures to [StylishContentState.Error] or its
 * own domain error model.
 */
public fun interface StylishDataTableAdapter<T> {
    /** Loads one page for the supplied immutable query. */
    public suspend fun load(query: StylishDataTableQuery): StylishDataTableQueryResult<T>
}

/**
 * Invokes a [StylishDataTableAdapter] with normalized paging values.
 *
 * This helper gives every host the same boundary behavior for invalid page numbers and page
 * sizes while preserving all other query fields verbatim.
 */
public suspend fun <T> StylishDataTableAdapter<T>.loadNormalized(
    query: StylishDataTableQuery,
): StylishDataTableQueryResult<T> = load(
    query.copy(
        page = query.normalizedPage,
        pageSize = query.normalizedPageSize,
    ),
)

package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.focusable
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalDensity
import com.segnities007.stylishui.structure.DataTableLayout
import com.segnities007.stylishui.foundation.rememberStylishFocusRequesters
import com.segnities007.stylishui.foundation.stylishRovingFocus
import com.segnities007.stylishui.foundation.headless.StylishReducer
import com.segnities007.stylishui.theme.StylishTheme

/** Sort direction for [StylishDataTable]. */
public enum class StylishDataTableSortDirection { Ascending, Descending }

/** A stable coordinate used by the optional cell-level keyboard navigation. */
@Immutable
public data class StylishDataTableCellPosition(
    public val rowIndex: Int,
    public val columnId: String,
)

private val MinimumDataTableColumnWidth = 48.dp
private val DefaultDataTableColumnWidth = 120.dp
private val DataTableSelectionColumnWidth = 48.dp
private val DataTableResizeStep = 8.dp

/** Hoistable sorting state identified by a column id. */
@Immutable
public data class StylishDataTableSortState(
    public val columnId: String,
    public val direction: StylishDataTableSortDirection = StylishDataTableSortDirection.Ascending,
)

/**
 * Headless, transport-neutral state for [StylishDataTable].
 *
 * The state model keeps query, selection, expansion, layout, and keyboard focus separate from
 * rendering. Hosts can persist it, reduce it from REST/GraphQL events, or share it with a native
 * screen without depending on Compose runtime state.
 */
@Immutable
public data class StylishDataTableState(
    public val filterText: String = "",
    public val page: Int = 1,
    public val pageSize: Int? = null,
    public val sortStates: List<StylishDataTableSortState> = emptyList(),
    public val selectedKeys: Set<Any> = emptySet(),
    public val expandedKeys: Set<Any> = emptySet(),
    public val visibleColumnIds: Set<String> = emptySet(),
    public val columnOrder: List<String> = emptyList(),
    public val columnWidths: Map<String, Dp> = emptyMap(),
    public val pinnedColumnIds: Set<String> = emptySet(),
    public val freezePinnedColumns: Boolean = false,
    public val focusedCell: StylishDataTableCellPosition? = null,
) {
    /** Returns a state with paging values normalized for an adapter or renderer. */
    public fun normalized(): StylishDataTableState = copy(
        page = page.coerceAtLeast(1),
        pageSize = pageSize?.takeIf { it > 0 },
    )
}

/** Pure event set used by [StylishDataTableState.reduce]. */
@Immutable
public sealed interface StylishDataTableAction {
    @Immutable
    public data class FilterChanged(public val value: String) : StylishDataTableAction
    @Immutable
    public data class PageChanged(public val value: Int) : StylishDataTableAction
    @Immutable
    public data class PageSizeChanged(public val value: Int?) : StylishDataTableAction
    @Immutable
    public data class SortChanged(public val value: List<StylishDataTableSortState>) : StylishDataTableAction
    @Immutable
    public data class SelectionChanged(public val value: Set<Any>) : StylishDataTableAction
    @Immutable
    public data class ExpansionChanged(public val value: Set<Any>) : StylishDataTableAction
    @Immutable
    public data class VisibleColumnsChanged(public val value: Set<String>) : StylishDataTableAction
    @Immutable
    public data class ColumnOrderChanged(public val value: List<String>) : StylishDataTableAction
    @Immutable
    public data class ColumnWidthsChanged(public val value: Map<String, Dp>) : StylishDataTableAction
    @Immutable
    public data class PinnedColumnsChanged(public val value: Set<String>) : StylishDataTableAction
    @Immutable
    public data class FreezePinnedColumnsChanged(public val value: Boolean) : StylishDataTableAction
    @Immutable
    public data class FocusChanged(public val value: StylishDataTableCellPosition?) : StylishDataTableAction
}

/** Shared pure reducer used by Compose, SwiftUI, Web, and desktop host stores. */
public object StylishDataTableStateReducer : StylishReducer<StylishDataTableState, StylishDataTableAction> {
    override fun reduce(state: StylishDataTableState, action: StylishDataTableAction): StylishDataTableState = when (action) {
        is StylishDataTableAction.FilterChanged -> state.copy(filterText = action.value)
        is StylishDataTableAction.PageChanged -> state.copy(page = action.value.coerceAtLeast(1))
        is StylishDataTableAction.PageSizeChanged -> state.copy(pageSize = action.value?.takeIf { it > 0 })
        is StylishDataTableAction.SortChanged -> state.copy(sortStates = action.value.distinctBy { it.columnId })
        is StylishDataTableAction.SelectionChanged -> state.copy(selectedKeys = action.value)
        is StylishDataTableAction.ExpansionChanged -> state.copy(expandedKeys = action.value)
        is StylishDataTableAction.VisibleColumnsChanged -> state.copy(visibleColumnIds = action.value)
        is StylishDataTableAction.ColumnOrderChanged -> state.copy(columnOrder = action.value.distinct())
        is StylishDataTableAction.ColumnWidthsChanged -> state.copy(columnWidths = action.value)
        is StylishDataTableAction.PinnedColumnsChanged -> state.copy(pinnedColumnIds = action.value)
        is StylishDataTableAction.FreezePinnedColumnsChanged -> state.copy(freezePinnedColumns = action.value)
        is StylishDataTableAction.FocusChanged -> state.copy(focusedCell = action.value)
    }
}

/** Reduces a headless table event without Compose or transport dependencies. */
public fun StylishDataTableState.reduce(action: StylishDataTableAction): StylishDataTableState =
    StylishDataTableStateReducer.reduce(this, action)

/**
 * Column definition for [StylishDataTable]. The composable [cell] slot supports arbitrary UI,
 * while [comparator] opts the column into sorting.
 */
public class StylishDataTableColumn<T>(
    public val id: String,
    public val title: String,
    public val weight: Float = 1f,
    public val horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    public val comparator: Comparator<T>? = null,
    public val header: (@Composable () -> Unit)? = null,
    public val exportValue: ((T) -> Any?)? = null,
    public val cell: @Composable (T) -> Unit,
)

/**
 * A virtualized, sortable, selectable data table with stable row identity and a sticky header.
 *
 * State is hoisted: pass [sortState] and [onSortStateChange] for controlled sorting, and
 * [selectedKeys] with [onSelectedKeysChange] for controlled selection. Omit either callback to
 * disable that interaction. Optional local filtering, pagination, row expansion, column
 * visibility, and export hooks can be connected to server-side data on Web, Android, desktop,
 * and iOS.
 */
@Composable
public fun <T> StylishDataTable(
    rows: List<T>,
    columns: List<StylishDataTableColumn<T>>,
    rowKey: (T) -> Any,
    modifier: Modifier = Modifier,
    sortState: StylishDataTableSortState? = null,
    onSortStateChange: ((StylishDataTableSortState) -> Unit)? = null,
    sortStates: List<StylishDataTableSortState> = emptyList(),
    onSortStatesChange: ((List<StylishDataTableSortState>) -> Unit)? = null,
    selectedKeys: Set<Any> = emptySet(),
    onSelectedKeysChange: ((Set<Any>) -> Unit)? = null,
    stickyHeader: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    headerContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    selectedRowColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    rowColor: Color = MaterialTheme.colorScheme.surface,
    isLoading: Boolean = false,
    error: Throwable? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    filterText: String = "",
    onFilterTextChange: ((String) -> Unit)? = null,
    filterPredicate: ((T, String) -> Boolean)? = null,
    /** Placeholder for the filter field; blank uses [StylishTheme.strings.filter]. */
    filterPlaceholder: String = "",
    page: Int = 1,
    pageSize: Int? = null,
    onPageChange: ((Int) -> Unit)? = null,
    expandedKeys: Set<Any> = emptySet(),
    onExpandedKeysChange: ((Set<Any>) -> Unit)? = null,
    expandedContent: (@Composable (T) -> Unit)? = null,
    /** Column ids to render. An empty set keeps all columns visible. */
    visibleColumnIds: Set<String> = emptySet(),
    /** Column ids in the desired order. Unknown ids are ignored; omitted columns keep source order. */
    columnOrder: List<String> = emptyList(),
    /** Called when a header move control requests a new controlled column order. */
    onColumnOrderChange: ((List<String>) -> Unit)? = null,
    /** Controlled pixel-independent widths. When present, a resize handle is rendered. */
    columnWidths: Map<String, Dp> = emptyMap(),
    onColumnWidthsChange: ((Map<String, Dp>) -> Unit)? = null,
    /** Pinned ids receive a stable elevated surface treatment. Use [freezePinnedColumns] for leading-column freeze. */
    pinnedColumnIds: Set<String> = emptySet(),
    /** When true, leading pinned columns remain stationary while the table viewport scrolls. */
    freezePinnedColumns: Boolean = false,
    /** Hoisted cell coordinate. Supplying [onFocusedCellChange] enables arrow-key cell navigation. */
    focusedCell: StylishDataTableCellPosition? = null,
    onFocusedCellChange: ((StylishDataTableCellPosition) -> Unit)? = null,
    /** Called with the current filtered/sorted rows when the optional export action is pressed. */
    onExport: ((List<T>) -> Unit)? = null,
    onExportText: ((String) -> Unit)? = null,
    exportFormat: StylishDataTableExportFormat = StylishDataTableExportFormat.Csv,
    exportLabel: String = "",
) {
    val visibleColumns = remember(columns, visibleColumnIds, columnOrder) {
        val filtered = if (visibleColumnIds.isEmpty()) columns else columns.filter { it.id in visibleColumnIds }
        if (columnOrder.isEmpty()) filtered else {
            val byId = filtered.associateBy { it.id }
            val orderedIds = columnOrder.asSequence().filter { it in byId }.distinct().toList()
            orderedIds.mapNotNull(byId::get) + filtered.filterNot { it.id in orderedIds }
        }
    }
    val effectiveSortStates = if (sortStates.isNotEmpty()) sortStates else sortState?.let { listOf(it) }.orEmpty()
    val queryResult = remember(rows, columns, filterText, filterPredicate, effectiveSortStates, page, pageSize) {
        resolveStylishDataTableRows(
            rows = rows,
            columns = columns,
            filterText = filterText,
            filterPredicate = filterPredicate,
            sortStates = effectiveSortStates,
            page = page,
            pageSize = pageSize,
        )
    }
    val sortedRows = queryResult.sortedRows
    val visibleRows = queryResult.visibleRows
    val pageCount = queryResult.pageCount
    val safePage = queryResult.page
    val normalizedPageSize = pageSize?.takeIf { it > 0 }
    val selectionEnabled = onSelectedKeysChange != null
    val cellNavigationEnabled = onFocusedCellChange != null
    // Do not allocate focus infrastructure for interactions that the caller did not enable.
    // This matters for large, read-only tables: a FocusRequester is a stateful Compose object,
    // not a cheap marker, and the old unconditional cell matrix made every table pay for
    // optional cell navigation during first composition.
    val focusRequesters = if (!cellNavigationEnabled) {
        rememberStylishFocusRequesters(visibleRows.size)
    } else {
        emptyList()
    }
    val cellFocusRequesters = if (cellNavigationEnabled) {
        remember(visibleRows.size, visibleColumns.map { it.id }) {
            List(visibleRows.size * visibleColumns.size) { FocusRequester() }
        }
    } else {
        emptyList()
    }
    val horizontalScrollState = rememberScrollState()
    val density = LocalDensity.current
    val strings = StylishTheme.strings
    val resolvedEmptyContent: (@Composable () -> Unit)? = when {
        isLoading -> loadingContent ?: { Text(strings.loading, Modifier.padding(16.dp)) }
        error != null -> errorContent ?: { Text(strings.error, Modifier.padding(16.dp)) }
        else -> emptyContent
    }

    Surface(
        modifier = modifier
            .testTag("stylish_data_table")
            .semantics {
            collectionInfo = CollectionInfo(visibleRows.size + 1, visibleColumns.size + if (selectionEnabled) 1 else 0)
        },
        color = rowColor,
    ) {
        Column(Modifier.fillMaxWidth()) {
            if (onFilterTextChange != null) {
                OutlinedTextField(
                    value = filterText,
                    onValueChange = onFilterTextChange,
                    singleLine = true,
                    label = { Text(filterPlaceholder.ifBlank { strings.filter }) },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                )
            }
            if (onExport != null || onExportText != null) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End) {
                    TextButton(onClick = {
                        onExport?.invoke(sortedRows)
                        onExportText?.invoke(exportStylishDataTable(sortedRows, visibleColumns, exportFormat))
                    }) { Text(exportLabel.ifBlank { strings.export }) }
                }
            }
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val tableWidth = maxOf(
                    maxWidth,
                    DataTableSelectionColumnWidth + visibleColumns.fold(0f) { total, column ->
                        // Keep a minimum track for weight-based columns so a wide table can
                        // actually scroll instead of collapsing its weighted children.
                        total + (columnWidths[column.id]?.normalizedDataTableWidth()?.value
                            ?: DefaultDataTableColumnWidth.value)
                    }.dp,
                )
                DataTableLayout(
                    rows = visibleRows,
                    key = rowKey,
                    stickyHeader = stickyHeader,
                    contentPadding = contentPadding,
                    listState = listState,
                    emptyContent = resolvedEmptyContent,
                    header = {
                Row(Modifier.width(tableWidth).horizontalScroll(horizontalScrollState).background(headerContainerColor)) {
                    if (selectionEnabled) {
                        val visibleKeys = visibleRows.map(rowKey).toSet()
                        Checkbox(
                            checked = visibleKeys.isNotEmpty() && visibleKeys.all(selectedKeys::contains),
                            onCheckedChange = { checked ->
                                onSelectedKeysChange(
                                    if (checked) selectedKeys + visibleKeys else selectedKeys - visibleKeys,
                                )
                            },
                            modifier = Modifier.width(DataTableSelectionColumnWidth).semantics { contentDescription = strings.selectAllRows },
                        )
                    }
                    visibleColumns.forEach { column ->
                        val width = columnWidths[column.id]?.normalizedDataTableWidth()
                        Row(
                            modifier = (if (width != null) Modifier.width(width) else Modifier.weight(column.effectiveDataTableWeight()))
                                .fillMaxHeight()
                                .padding(horizontal = 4.dp)
                                .background(if (column.id in pinnedColumnIds) headerContainerColor else Color.Transparent)
                                .then(
                                    if (freezePinnedColumns && column.id in pinnedColumnIds) {
                                        Modifier.graphicsLayer { translationX = horizontalScrollState.value.toFloat() }
                                    } else Modifier,
                                )
                                .semantics {
                                    if (column.id in pinnedColumnIds) {
                                        stateDescription = if (freezePinnedColumns) strings.frozenColumn else strings.pinnedColumn
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.weight(1f), contentAlignment = column.horizontalAlignment.withVerticalCenter()) {
                            if (column.comparator != null && (onSortStateChange != null || onSortStatesChange != null)) {
                                val currentState = effectiveSortStates.firstOrNull { it.columnId == column.id }
                                TextButton(onClick = {
                                    val next = StylishDataTableSortState(
                                        column.id,
                                        if (currentState?.direction == StylishDataTableSortDirection.Ascending) {
                                            StylishDataTableSortDirection.Descending
                                        } else StylishDataTableSortDirection.Ascending,
                                    )
                                    if (onSortStatesChange != null) {
                                        onSortStatesChange(sortStates.filterNot { it.columnId == column.id } + next)
                                    } else onSortStateChange?.invoke(next)
                                }, modifier = Modifier.semantics {
                                    val directionLabel = when (currentState?.direction) {
                                        StylishDataTableSortDirection.Ascending -> strings.sortAscending
                                        StylishDataTableSortDirection.Descending -> strings.sortDescending
                                        null -> null
                                    }
                                    contentDescription = column.title
                                    if (directionLabel != null) stateDescription = directionLabel
                                }) {
                                    Text(column.title + when (currentState?.direction) {
                                        StylishDataTableSortDirection.Ascending -> " ↑"
                                        StylishDataTableSortDirection.Descending -> " ↓"
                                        null -> ""
                                    })
                                }
                            } else {
                            if (column.header != null) column.header.invoke()
                                else Text(column.title, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(12.dp))
                            }
                            if (onColumnOrderChange != null) {
                                Row {
                                    val orderedIds = visibleColumns.map { it.id }
                                    val columnIndex = orderedIds.indexOf(column.id)
                                    TextButton(
                                        onClick = {
                                            if (columnIndex > 0) {
                                                onColumnOrderChange(orderedIds.swapDataTableColumns(columnIndex, columnIndex - 1))
                                            }
                                        },
                                        enabled = columnIndex > 0,
                                        modifier = Modifier.semantics { contentDescription = strings.moveColumnLeft(column.title) },
                                    ) { Text("←") }
                                    TextButton(
                                        onClick = {
                                            if (columnIndex in 0 until orderedIds.lastIndex) {
                                                onColumnOrderChange(orderedIds.swapDataTableColumns(columnIndex, columnIndex + 1))
                                            }
                                        },
                                        enabled = columnIndex >= 0 && columnIndex < orderedIds.lastIndex,
                                        modifier = Modifier.semantics { contentDescription = strings.moveColumnRight(column.title) },
                                    ) { Text("→") }
                                }
                            }
                            }
                            if (onColumnWidthsChange != null) {
                                Box(
                                    Modifier
                                        .width(8.dp)
                                        .fillMaxHeight()
                                        .semantics {
                                            contentDescription = strings.resizeColumn(column.title)
                                            stateDescription = width?.let { "${it.value} dp" } ?: "${DefaultDataTableColumnWidth.value} dp"
                                        }
                                        .focusable()
                                        .onKeyEvent { event ->
                                            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                                            val current = width ?: DefaultDataTableColumnWidth
                                            val next = when (event.key) {
                                                Key.DirectionLeft, Key.DirectionDown -> current - DataTableResizeStep
                                                Key.DirectionRight, Key.DirectionUp -> current + DataTableResizeStep
                                                Key.MoveHome -> MinimumDataTableColumnWidth
                                                else -> return@onKeyEvent false
                                            }.coerceAtLeast(MinimumDataTableColumnWidth)
                                            onColumnWidthsChange(columnWidths + (column.id to next))
                                            true
                                        }
                                        .pointerInput(column.id, width) {
                                            detectDragGestures { _, dragAmount ->
                                                val delta = with(density) { dragAmount.x.toDp() }
                                                val next = (width ?: DefaultDataTableColumnWidth) + delta
                                                onColumnWidthsChange(columnWidths + (column.id to next.coerceAtLeast(MinimumDataTableColumnWidth)))
                                            }
                                        },
                                )
                            }
                        }
                    }
                }
                HorizontalDivider()
                    },
                    row = { index, item ->
                val key = rowKey(item)
                val selected = key in selectedKeys
                val expandable = onExpandedKeysChange != null && expandedContent != null
                val toggleRow: () -> Unit = {
                    if (selectionEnabled) {
                        onSelectedKeysChange(
                            if (selected) selectedKeys - key else selectedKeys + key,
                        )
                    }
                    if (expandable) {
                        onExpandedKeysChange(
                            if (key in expandedKeys) expandedKeys - key else expandedKeys + key,
                        )
                    }
                }
                Row(
                    Modifier
                        .width(tableWidth)
                        .horizontalScroll(horizontalScrollState)
                        .then(
                            if (!cellNavigationEnabled) {
                                Modifier.stylishRovingFocus(
                                    requester = focusRequesters[index],
                                    index = index,
                                    itemCount = visibleRows.size,
                                    onMove = { target -> focusRequesters[target].requestFocus() },
                                    onActivate = if (selectionEnabled || expandable) toggleRow else null,
                                )
                            } else Modifier,
                        )
                        .background(if (selected) selectedRowColor else rowColor)
                        .then(
                            if (selectionEnabled || expandable) Modifier.clickable(onClick = toggleRow) else Modifier,
                        )
                        .semantics {
                            collectionItemInfo = CollectionItemInfo(index + 1, 1, 0, visibleColumns.size)
                            if (expandable) stateDescription = if (key in expandedKeys) strings.collapseRow else strings.expandRow
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (selectionEnabled) {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = { checked ->
                                onSelectedKeysChange(
                                    if (checked) selectedKeys + key else selectedKeys - key,
                                )
                            },
                            modifier = Modifier.width(DataTableSelectionColumnWidth).semantics { contentDescription = strings.selectRow(index) },
                        )
                    }
                    visibleColumns.forEachIndexed { columnIndex, column ->
                        val width = columnWidths[column.id]?.normalizedDataTableWidth()
                        val position = StylishDataTableCellPosition(index, column.id)
                        val focusIndex = index * visibleColumns.size + columnIndex
                        Box(
                            modifier = (if (width != null) Modifier.width(width) else Modifier.weight(column.effectiveDataTableWeight()))
                                .padding(12.dp)
                                .background(if (column.id in pinnedColumnIds) selectedRowColor.copy(alpha = 0.12f) else Color.Transparent)
                                .then(
                                    if (freezePinnedColumns && column.id in pinnedColumnIds) {
                                        Modifier.graphicsLayer { translationX = horizontalScrollState.value.toFloat() }
                                    } else Modifier,
                                ),
                            contentAlignment = column.horizontalAlignment.withVerticalCenter(),
                        ) {
                            Box(
                                Modifier.then(
                                    if (cellNavigationEnabled) {
                                        Modifier
                                            .focusRequester(cellFocusRequesters[focusIndex])
                                            .focusable()
                                            .onPreviewKeyEvent { event ->
                                                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                                                val target = when (event.key) {
                                                    Key.DirectionLeft -> position.copy(columnId = visibleColumns[(columnIndex - 1).coerceAtLeast(0)].id)
                                                    Key.DirectionRight -> position.copy(columnId = visibleColumns[(columnIndex + 1).coerceAtMost(visibleColumns.lastIndex)].id)
                                                    Key.DirectionUp -> position.copy(rowIndex = (index - 1).coerceAtLeast(0))
                                                    Key.DirectionDown -> position.copy(rowIndex = (index + 1).coerceAtMost(visibleRows.lastIndex))
                                                    Key.MoveHome -> position.copy(columnId = visibleColumns.firstOrNull()?.id ?: column.id)
                                                    Key.MoveEnd -> position.copy(columnId = visibleColumns.lastOrNull()?.id ?: column.id)
                                                    else -> return@onPreviewKeyEvent false
                                                }
                                                if (target != position) {
                                                    onFocusedCellChange(target)
                                                    val targetColumnIndex = visibleColumns.indexOfFirst { it.id == target.columnId }
                                                    val targetFocusIndex = target.rowIndex * visibleColumns.size + targetColumnIndex
                                                    cellFocusRequesters.getOrNull(targetFocusIndex)?.requestFocus()
                                                }
                                                true
                                            }
                                            .semantics {
                                                contentDescription = strings.columnPosition(column.title, index)
                                                stateDescription = when {
                                                    focusedCell == position -> "Focused"
                                                    column.id in pinnedColumnIds && freezePinnedColumns -> strings.frozenColumn
                                                    column.id in pinnedColumnIds -> strings.pinnedColumn
                                                    else -> ""
                                                }
                                            }
                                    } else Modifier,
                                ),
                            ) { column.cell(item) }
                        }
                    }
                }
                if (expandedContent != null && key in expandedKeys) {
                    Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                        expandedContent.invoke(item)
                    }
                }
                HorizontalDivider()
                    },
                )
            }
            if (normalizedPageSize != null && onPageChange != null && pageCount > 1) {
                Row(
                    Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = { onPageChange(safePage - 1) }, enabled = safePage > 1) {
                        Text(strings.previousPage)
                    }
                    Spacer(Modifier.weight(1f))
                    Text(strings.pageOf(safePage, pageCount), style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { onPageChange(safePage + 1) }, enabled = safePage < pageCount) {
                        Text(strings.nextPage)
                    }
                }
            }
        }
    }
}

/**
 * Renders [StylishDataTable] from a single hoisted [StylishDataTableState].
 *
 * This overload is the recommended entry point for feature teams that want a headless reducer
 * and a replaceable renderer. Presentation-only options remain available while all query and
 * interaction changes are emitted as [StylishDataTableAction]-compatible state transitions.
 */
@Composable
public fun <T> StylishDataTable(
    rows: List<T>,
    columns: List<StylishDataTableColumn<T>>,
    rowKey: (T) -> Any,
    state: StylishDataTableState,
    onStateChange: (StylishDataTableState) -> Unit,
    modifier: Modifier = Modifier,
    stickyHeader: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    headerContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    selectedRowColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    rowColor: Color = MaterialTheme.colorScheme.surface,
    isLoading: Boolean = false,
    error: Throwable? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    filterPredicate: ((T, String) -> Boolean)? = null,
    filterPlaceholder: String = "",
    expandedContent: (@Composable (T) -> Unit)? = null,
    onExport: ((List<T>) -> Unit)? = null,
    onExportText: ((String) -> Unit)? = null,
    exportFormat: StylishDataTableExportFormat = StylishDataTableExportFormat.Csv,
    exportLabel: String = "",
) {
    val normalized = state.normalized()
    StylishDataTable(
        rows = rows,
        columns = columns,
        rowKey = rowKey,
        modifier = modifier,
        sortStates = normalized.sortStates,
        onSortStatesChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.SortChanged(value))) },
        selectedKeys = normalized.selectedKeys,
        onSelectedKeysChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.SelectionChanged(value))) },
        stickyHeader = stickyHeader,
        contentPadding = contentPadding,
        listState = listState,
        headerContainerColor = headerContainerColor,
        selectedRowColor = selectedRowColor,
        rowColor = rowColor,
        isLoading = isLoading,
        error = error,
        loadingContent = loadingContent,
        emptyContent = emptyContent,
        errorContent = errorContent,
        filterText = normalized.filterText,
        onFilterTextChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.FilterChanged(value))) },
        filterPredicate = filterPredicate,
        filterPlaceholder = filterPlaceholder,
        page = normalized.page,
        pageSize = normalized.pageSize,
        onPageChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.PageChanged(value))) },
        expandedKeys = normalized.expandedKeys,
        onExpandedKeysChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.ExpansionChanged(value))) },
        expandedContent = expandedContent,
        visibleColumnIds = normalized.visibleColumnIds,
        columnOrder = normalized.columnOrder,
        onColumnOrderChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.ColumnOrderChanged(value))) },
        columnWidths = normalized.columnWidths,
        onColumnWidthsChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.ColumnWidthsChanged(value))) },
        pinnedColumnIds = normalized.pinnedColumnIds,
        freezePinnedColumns = normalized.freezePinnedColumns,
        focusedCell = normalized.focusedCell,
        onFocusedCellChange = { value -> onStateChange(normalized.reduce(StylishDataTableAction.FocusChanged(value))) },
        onExport = onExport,
        onExportText = onExportText,
        exportFormat = exportFormat,
        exportLabel = exportLabel,
    )
}

private fun Alignment.Horizontal.withVerticalCenter(): Alignment =
    when (this) {
        Alignment.End -> Alignment.CenterEnd
        Alignment.CenterHorizontally -> Alignment.Center
        else -> Alignment.CenterStart
    }

private fun Dp.normalizedDataTableWidth(): Dp? =
    takeIf { value.isFinite() }?.coerceAtLeast(MinimumDataTableColumnWidth)

private fun StylishDataTableColumn<*>.effectiveDataTableWeight(): Float =
    weight.takeIf { it.isFinite() && it > 0f } ?: 1f

private fun List<String>.swapDataTableColumns(first: Int, second: Int): List<String> =
    toMutableList().also {
        val value = it[first]
        it[first] = it[second]
        it[second] = value
    }

private data class PreviewRow(val id: Int, val name: String, val value: Int)

@Preview(name = "Stylish data table", widthDp = 600, heightDp = 360)
@Composable
private fun StylishDataTablePreview() {
    StylishTheme(darkTheme = false) {
        StylishDataTable(
            rows = listOf(PreviewRow(1, "Alpha", 30), PreviewRow(2, "Beta", 20)),
            columns = listOf(
                StylishDataTableColumn("name", "Name", comparator = compareBy { it.name }) { Text(it.name) },
                StylishDataTableColumn("value", "Value", horizontalAlignment = Alignment.End, comparator = compareBy { it.value }) { Text(it.value.toString()) },
            ),
            rowKey = { it.id },
        )
    }
}

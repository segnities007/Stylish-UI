package com.segnities007.stylishui.structure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Headless virtualized table structure with optional sticky header and stable row identity.
 * Styling, cells, selection, sorting, and interaction are entirely supplied by slots.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun <T> DataTableLayout(
    rows: List<T>,
    header: @Composable () -> Unit,
    row: @Composable (index: Int, item: T) -> Unit,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    stickyHeader: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    emptyContent: (@Composable () -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
    ) {
        if (stickyHeader) stickyHeader { header() } else item { header() }
        if (rows.isEmpty() && emptyContent != null) item { emptyContent() }
        itemsIndexed(
            items = rows,
            key = key?.let { factory -> { _: Int, item: T -> factory(item) } },
        ) { index, item -> row(index, item) }
    }
}

@Preview(name = "Headless data table", widthDp = 393, heightDp = 300)
@Composable
private fun DataTableLayoutPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            DataTableLayout(
                rows = listOf("Alpha", "Beta", "Gamma"),
                header = { Text("Name", Modifier.fillMaxWidth().padding(12.dp)) },
                row = { _, value -> Text(value, Modifier.fillMaxWidth().padding(12.dp)) },
            )
        }
    }
}

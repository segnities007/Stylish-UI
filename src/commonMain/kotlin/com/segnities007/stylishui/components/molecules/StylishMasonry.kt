package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A masonry layout — the web "Masonry" pattern from Ant Design and MUI.
 *
 * Distributes [itemCount] items across [columns] vertical columns,
 * filling each column top-to-bottom. Items may have arbitrary heights;
 * the columns share the available width equally. Note: items are
 * assigned to columns in round-robin order (index % columns), which
 * keeps the layout deterministic without measuring item heights. For
 * height-balanced columns, prefer a lazy grid or manual grouping.
 *
 * @param itemCount Number of items to lay out.
 * @param modifier Modifier applied to the root row.
 * @param columns Number of columns. Defaults to 2.
 * @param horizontalSpacing Gap between columns. Defaults to
 *   [StylishTheme.dimensions.itemSpacing].
 * @param verticalSpacing Gap between items within a column. Defaults to
 *   [StylishTheme.dimensions.itemSpacing].
 * @param content Composes the item at [ColumnScope](index). Receives
 *   [ColumnScope] so items can use `Modifier.weight`.
 */
@Composable
public fun StylishMasonry(
    itemCount: Int,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    horizontalSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    verticalSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    content: @Composable ColumnScope.(index: Int) -> Unit,
) {
    require(columns > 0) { "columns must be greater than zero" }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
    ) {
        repeat(columns) { column ->
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            ) {
                for (index in column until itemCount step columns) {
                    content(index)
                }
            }
        }
    }
}

@Preview(name = "Stylish masonry", showBackground = true, widthDp = 393)
@Composable
private fun StylishMasonryPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishMasonry(itemCount = 5, columns = 2) { index ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .height(if (index % 2 == 0) 80.dp else 120.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("項目 $index")
                }
            }
        }
    }
}

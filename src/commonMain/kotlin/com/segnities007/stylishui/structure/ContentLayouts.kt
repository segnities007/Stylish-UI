package com.segnities007.stylishui.structure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Headless vertical placement. The caller supplies spacing and all rendered content. */
@Composable
public fun ContentColumn(
    spacing: Dp,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(spacing), content = content)
}

/** Headless horizontal placement with centered cross-axis alignment. */
@Composable
public fun ContentRow(
    spacing: Dp,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** Headless overlay placement; visual decisions belong to slot renderers. */
@Composable
public fun ContentBox(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier, contentAlignment = alignment, content = content)
}

@Preview(name = "Content layout slots")
@Composable
private fun ContentLayoutsPreview() {
    ContentColumn(0.dp) { ContentRow(0.dp) { ContentBox { } } }
}

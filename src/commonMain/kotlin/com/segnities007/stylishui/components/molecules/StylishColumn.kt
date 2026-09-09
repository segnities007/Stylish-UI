package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.structure.ContentColumn
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/** Column layout with Stylish spacing defaults. Custom content belongs to the flexible API tier. */
@Composable
public fun StylishColumn(
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.itemSpacing,
    content: @Composable ColumnScope.() -> Unit,
) {
    ContentColumn(spacing = spacing, modifier = modifier.stylishTestTag("column"), content = content)
}

@Preview(name = "Column — light")
@Preview(name = "Column — dark", uiMode = 0x20)
@Composable
private fun StylishColumnPreview() {
    StylishTheme { StylishColumn { StylishText("Content") } }
}

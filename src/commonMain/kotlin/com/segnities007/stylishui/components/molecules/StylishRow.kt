package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.structure.ContentRow
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/** Row layout with Stylish spacing defaults. Custom content belongs to the flexible API tier. */
@Composable
public fun StylishRow(
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.itemSpacing,
    content: @Composable RowScope.() -> Unit,
) {
    ContentRow(spacing = spacing, modifier = modifier.stylishTestTag("row"), content = content)
}

@Preview(name = "Row — light")
@Preview(name = "Row — dark", uiMode = 0x20)
@Composable
private fun StylishRowPreview() {
    StylishTheme { StylishRow { StylishText("Content") } }
}

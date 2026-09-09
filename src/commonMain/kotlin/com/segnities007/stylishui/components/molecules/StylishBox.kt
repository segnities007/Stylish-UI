package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.structure.ContentBox
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/** Box layout with Stylish spacing defaults. Custom content belongs to the flexible API tier. */
@Composable
public fun StylishBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    ContentBox(modifier = modifier.stylishTestTag("box"), content = content)
}

@Preview(name = "Box — light")
@Preview(name = "Box — dark", uiMode = 0x20)
@Composable
private fun StylishBoxPreview() {
    StylishTheme { StylishBox { StylishText("Content") } }
}

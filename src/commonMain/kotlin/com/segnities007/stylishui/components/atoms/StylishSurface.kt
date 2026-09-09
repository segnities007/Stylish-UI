package com.segnities007.stylishui.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Non-interactive surface with paired theme colors. Custom [color] must be accompanied
 * by an appropriate [contentColor]; interactive surfaces should use StylishCard/Button.
 */
@Composable
public fun StylishSurface(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.stylishTestTag("surface"),
        color = color,
        contentColor = contentColor,
        shape = shape,
        content = content,
    )
}

@Preview(name = "Surface — light")
@Preview(name = "Surface — dark", uiMode = 0x20)
@Composable
private fun StylishSurfacePreview() {
    StylishTheme { StylishSurface { Text("Surface content") } }
}

package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.stylishui.foundation.stylishTestTag
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A monospaced inline code display — the web "Code" pattern from Chakra
 * UI and Ant Design.
 *
 * @param text The code text to display.
 * @param modifier Modifier applied to the root surface.
 * @param containerColor Background of the code block. Defaults to
 *   [MaterialTheme.colorScheme.surfaceVariant].
 * @param contentColor Foreground of the code text. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param shape Corner shape. Defaults to a small rounded rectangle.
 * @param textStyle Typography of the code. Defaults to bodySmall in a
 *   monospace family.
 */
@Composable
public fun StylishCode(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = RectangleShape,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(
        fontFamily = FontFamily.Monospace,
    ),
) {
    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.stylishTestTag("code"),
    ) {
        Text(
            text,
            style = textStyle,
            color = contentColor,
        )
    }
}

@Preview(name = "Stylish code", showBackground = true, widthDp = 393)
@Composable
private fun StylishCodePreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishCode("import com.segnities007.stylishui.theme.StylishTheme")
        }
    }
}

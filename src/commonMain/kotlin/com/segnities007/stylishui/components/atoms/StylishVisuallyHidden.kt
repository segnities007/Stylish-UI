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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A visually hidden element that remains accessible to screen readers —
 * the web "Visually Hidden" pattern from Chakra UI.
 *
 * Renders [content] in a 1 dp, fully transparent box. The content is not
 * visible on screen but keeps its semantics and remains focusable, so
 * screen-reader-only labels and skip links can be composed with it.
 *
 * @param modifier Modifier applied to the hidden box.
 * @param content The content to hide visually.
 */
@Composable
public fun StylishVisuallyHidden(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .size(1.dp)
            .clip(RectangleShape)
            .alpha(0f),
    ) {
        content()
    }
}

@Preview(name = "Stylish visually hidden", showBackground = true, widthDp = 393)
@Composable
private fun StylishVisuallyHiddenPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishVisuallyHidden {
                Text("スクリーンリーダー専用テキスト")
            }
        }
    }
}

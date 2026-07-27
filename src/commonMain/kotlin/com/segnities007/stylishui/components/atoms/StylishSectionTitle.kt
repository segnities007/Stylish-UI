package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A section heading label rendered in the primary color. Provides
 * consistent typography and vertical rhythm for group headers within
 * lists, forms, and detail screens.
 *
 * The text is padded vertically by [verticalPadding] to create
 * breathing room between the preceding section's content and the
 * next section's body. When [maxLines] is reached, [overflow]
 * controls how the excess text is handled.
 *
 * @param title Heading text to display.
 * @param maxLines Maximum number of visible lines. Defaults to
 *   [Int.MAX_VALUE] (unlimited).
 * @param overflow Strategy applied when text exceeds [maxLines].
 *   Defaults to [TextOverflow.Ellipsis].
 * @param textStyle Typography for the heading. Defaults to
 *   `MaterialTheme.typography.titleMedium`.
 * @param color Text color. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param verticalPadding Symmetric vertical padding applied above
 *   and below the text. Defaults to 20 dp.
 */
@Composable
public fun StylishSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    verticalPadding: Dp = 20.dp,
) {
    Text(
        title,
        style = textStyle,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier.padding(vertical = verticalPadding),
    )
}

@Preview(name = "Section title", showBackground = true, widthDp = 393)
@Composable
private fun StylishSectionTitlePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishSectionTitle("期限管理")
        }
    }
}

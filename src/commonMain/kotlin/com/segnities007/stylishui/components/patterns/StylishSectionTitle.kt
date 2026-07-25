package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/** セクションの見出し。プライマリカラーの中見出しテキスト。 */
@Composable
fun StylishSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    verticalPadding: Dp = 20.dp,
) {
    Text(
        title,
        style = textStyle,
        color = MaterialTheme.colorScheme.primary,
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

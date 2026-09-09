package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/** A theme-aware top header whose surface fades into the screen background. */
@Composable
public fun StylishGradientHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val background = MaterialTheme.colorScheme.background
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                Brush.verticalGradient(
                    0f to background,
                    .6f to background.copy(alpha = .8f),
                    1f to Color.Transparent,
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview(name = "Gradient header - light", showBackground = true, widthDp = 393, heightDp = 96)
@Composable
private fun StylishGradientHeaderLightPreview() {
    StylishTheme(darkTheme = false) {
        StylishGradientHeader {
            Text("メモ", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Preview(name = "Gradient header - dark", showBackground = true, widthDp = 393, heightDp = 96)
@Composable
private fun StylishGradientHeaderDarkPreview() {
    StylishTheme(darkTheme = true) {
        StylishGradientHeader {
            Text("メモ", style = MaterialTheme.typography.titleLarge)
        }
    }
}

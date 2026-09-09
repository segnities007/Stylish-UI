package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/** Resource/painter image. Loading and resource ownership remain with the host. */
@Composable
public fun StylishImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(painter, contentDescription, modifier.stylishTestTag("image"), contentScale = contentScale)
}

@Preview(name = "Image — light")
@Preview(name = "Image — dark", uiMode = 0x20)
@Composable
private fun StylishImagePreview() {
    StylishTheme { StylishImage(ColorPainter(Color.Gray), "Image placeholder") }
}

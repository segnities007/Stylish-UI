package com.segnities007.stylishui.components.atoms

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/** Vector icon. Pass null [contentDescription] only when adjacent text labels the same meaning. */
@Composable
public fun StylishIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(imageVector, contentDescription, modifier.stylishTestTag("icon"), tint)
}

@Preview(name = "Icon — light")
@Preview(name = "Icon — dark", uiMode = 0x20)
@Composable
private fun StylishIconPreview() {
    StylishTheme { StylishIcon(Icons.Default.Home, "Home") }
}

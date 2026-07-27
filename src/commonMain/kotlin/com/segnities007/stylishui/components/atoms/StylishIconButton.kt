package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/** 円形アイコンボタン。アウトライン付きのサーフェスにアイコンを配置。active 時はプライマリカラーに反転。 */
@Composable
public fun StylishIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    active: Boolean = false,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    minWidth: Dp = 48.dp,
    minHeight: Dp = 48.dp,
    iconContent: (@Composable () -> Unit)? = null,
) {
    val resolvedContainerColor = containerColor ?: if (active) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val resolvedContentColor = contentColor ?: if (active) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier.sizeIn(minWidth = minWidth, minHeight = minHeight),
        shape = shape ?: CircleShape,
        color = resolvedContainerColor,
        border = BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
        shadowElevation = if (enabled) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            iconContent?.invoke() ?: Icon(imageVector, contentDescription, tint = resolvedContentColor)
        }
    }
}

@Preview(name = "Icon button default", showBackground = true)
@Composable
private fun StylishIconButtonPreview() {
    MaterialTheme {
        StylishIconButton(Icons.Default.Search, "検索", {})
    }
}

@Preview(name = "Icon button active", showBackground = true)
@Composable
private fun StylishIconButtonActivePreview() {
    MaterialTheme {
        StylishIconButton(
            imageVector = Icons.Default.Search,
            contentDescription = "検索",
            onClick = {},
            active = true,
        )
    }
}

@Preview(name = "Icon button disabled", showBackground = true)
@Composable
private fun StylishIconButtonDisabledPreview() {
    MaterialTheme {
        StylishIconButton(
            imageVector = Icons.Default.Search,
            contentDescription = "検索",
            onClick = {},
            enabled = false,
        )
    }
}

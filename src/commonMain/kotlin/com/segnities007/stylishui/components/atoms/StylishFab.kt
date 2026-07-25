package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.segnities007.stylishui.tokens.StylishDimensions

@Composable
fun StylishFab(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    size: Dp = 56.dp,
    border: BorderStroke? = null,
) {
    Surface(
        modifier = modifier.size(size),
        shape = shape ?: CircleShape,
        color = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        border = border ?: BorderStroke(
            StylishDimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
        tonalElevation = StylishDimensions.floatingElevation,
        shadowElevation = StylishDimensions.floatingElevation,
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            Icon(imageVector, contentDescription)
        }
    }
}

@Preview(name = "FAB default", showBackground = true)
@Composable
private fun StylishFabPreview() {
    MaterialTheme {
        StylishFab(Icons.Default.Add, "追加", {})
    }
}

@Preview(name = "FAB disabled", showBackground = true)
@Composable
private fun StylishFabDisabledPreview() {
    MaterialTheme {
        StylishFab(
            imageVector = Icons.Default.Add,
            contentDescription = "追加",
            onClick = {},
            enabled = false,
        )
    }
}

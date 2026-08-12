package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * The optional visual marker placed on top of a bottom sheet to
 * indicate that it may be dragged, wrapping the Material 3
 * [BottomSheetDefaults.DragHandle] with the theme's default look.
 *
 * Place this inside the sheet's content column at the top. The handle
 * is a small rounded pill with the theme's `onSurfaceVariant` color and
 * carries a "drag handle" content description for accessibility.
 *
 * @param modifier Modifier applied to the handle.
 * @param width Width of the pill. Defaults to 32.dp (Material
 *   specification).
 * @param height Height of the pill. Defaults to 4.dp (Material
 *   specification).
 * @param shape Shape of the pill. Defaults to
 *   [MaterialTheme.shapes.extraLarge].
 * @param color Color of the pill. Defaults to
 *   `MaterialTheme.colorScheme.onSurfaceVariant`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishDragHandle(
    modifier: Modifier = Modifier,
    width: Dp = 32.dp,
    height: Dp = 4.dp,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    BottomSheetDefaults.DragHandle(
        modifier = modifier,
        width = width,
        height = height,
        shape = shape,
        color = color,
    )
}

@Preview(name = "Stylish drag handle", showBackground = true, widthDp = 393)
@Composable
private fun StylishDragHandlePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                StylishDragHandle()
            }
        }
    }
}

package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A modal bottom sheet styled with the Stylish design language —
 * rounded top corners from
 * [StylishTheme.dimensions.connectedCornerRadius], a hairline drag
 * handle, and theme-aware container colors.
 *
 * Wraps Material 3 [ModalBottomSheet] with Stylish defaults.
 *
 * @param onDismiss Called when the sheet is dismissed (swipe down,
 *   tap outside, or back gesture).
 * @param modifier Modifier applied to the [ModalBottomSheet].
 * @param shape Corner shape of the sheet. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius] on the top
 *   corners only.
 * @param containerColor Background color of the sheet.
 * @param contentColor Default content color.
 * @param tonalElevation Tonal elevation of the sheet surface.
 * @param dragHandle Optional custom drag handle composable. When
 *   `null`, the default M3 drag handle is used.
 * @param content The sheet content.
 *
 * @see com.segnities007.stylishui.components.atoms.StylishDialogSurface
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(
        topStart = StylishTheme.dimensions.connectedCornerRadius,
        topEnd = StylishTheme.dimensions.connectedCornerRadius,
    ),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = 1.dp,
    dragHandle: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        dragHandle = dragHandle,
    ) {
        content()
    }
}

@Preview(name = "Stylish bottom sheet content", showBackground = true, widthDp = 393)
@Composable
private fun StylishBottomSheetPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Column {
                Text("ボトムシートのコンテンツ", style = MaterialTheme.typography.titleMedium)
                Text(
                    "ここにアクションや情報を配置します",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
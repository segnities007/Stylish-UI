@file:Suppress("DEPRECATION")

package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
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
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A modal bottom sheet styled with the Stylish design language —
 * rounded top corners from
 * [StylishTheme.dimensions.connectedCornerRadius] and theme-aware
 * container colors.
 *
 * Wraps Material 3 [ModalBottomSheet] with Stylish defaults.
 *
 * @param onDismiss Called when the sheet is dismissed (swipe down,
 *   tap outside, or back gesture).
 * @param modifier Modifier applied to the [ModalBottomSheet].
 * @param skipPartiallyExpanded Whether the partially-expanded state
 *   should be skipped, so the sheet always opens fully expanded.
 *   Applied to the default [sheetState]; ignored when a custom
 *   [sheetState] is supplied.
 * @param sheetState The state of the sheet. Defaults to
 *   [rememberModalBottomSheetState] honoring
 *   [skipPartiallyExpanded].
 * @param shape Corner shape of the sheet. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius] on the top
 *   corners only.
 * @param containerColor Background color of the sheet. Defaults to
 *   [BottomSheetDefaults.ContainerColor] (the M3 `surfaceContainerLow`
 *   tone, so dark-theme sheets stay distinct from the surrounding chrome).
 * @param contentColor Default content color.
 * @param tonalElevation Tonal elevation of the sheet surface.
 * @param scrimColor Color of the scrim that obscures content while
 *   the sheet is open. Defaults to
 *   [BottomSheetDefaults.ScrimColor] (the M3 scrim tone with a 32%
 *   alpha baked in, so background content stays dimly visible).
 * @param dragHandle Optional drag handle composable. When `null`
 *   (the default), **no** drag handle is shown. Pass
 *   `@Composable { BottomSheetDefaults.DragHandle() }` for the M3
 *   default handle, or custom content for a bespoke one.
 * @param contentWindowInsets [WindowInsets] consumed by the sheet's
 *   content area so it clears system bars. Defaults to
 *   [BottomSheetDefaults.windowInsets].
 * @param properties Window behavior of the sheet (dismiss on back
 *   press / scrim click). Defaults to [ModalBottomSheetProperties].
 * @param content The sheet content, laid out inside a [ColumnScope].
 *
 * @see com.segnities007.stylishui.components.atoms.StylishDialogSurface
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
    ),
    shape: Shape = RoundedCornerShape(
        topStart = StylishTheme.dimensions.connectedCornerRadius,
        topEnd = StylishTheme.dimensions.connectedCornerRadius,
    ),
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = 1.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = null,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier.stylishTestTag("bottom_sheet"),
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        content = content,
    )
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

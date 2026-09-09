@file:Suppress("DEPRECATION")

package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import com.segnities007.stylishui.theme.StylishModalLayer
import com.segnities007.stylishui.theme.stylishModalContainerColor
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A modal bottom sheet styled with the Stylish design language —
 * a floating all-corner shape, restrained translucency, screen-edge
 * margins, and content padding.
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
 * @param shape Floating shape of the sheet.
 * @param containerColor Background color of the sheet.
 * @param contentColor Default content color.
 * @param tonalElevation Tonal elevation of the sheet surface.
 * @param horizontalMargin Horizontal space between the floating sheet
 *   and the window edges.
 * @param bottomMargin Space between the floating sheet and the bottom
 *   safe drawing edge.
 * @param contentPadding Padding applied inside the sheet around [content].
 * @param scrimColor Color of the scrim that obscures content while
 *   the sheet is open. Defaults to
 *   [BottomSheetDefaults.ScrimColor] (32% alpha scrim).
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
    shape: Shape = RoundedCornerShape(StylishTheme.shapes.floatingCornerRadius),
    containerColor: Color = stylishModalContainerColor().copy(alpha = 0.96f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    tonalElevation: Dp = StylishTheme.dimensions.floatingElevation,
    horizontalMargin: Dp = StylishTheme.dimensions.screenPadding,
    bottomMargin: Dp = StylishTheme.dimensions.contentSpacing,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StylishTheme.dimensions.screenPadding,
        vertical = StylishTheme.dimensions.contentSpacing,
    ),
    // M3's own default (BottomSheetDefaults.ScrimColor) carries 32% alpha;
    // an opaque scrim color would black out everything behind the sheet.
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = null,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
            .padding(horizontal = horizontalMargin)
            .padding(bottom = bottomMargin)
            .stylishTestTag("bottom_sheet"),
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
            ) {
                val contentScope = this
                StylishModalLayer { content(contentScope) }
            }
        },
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

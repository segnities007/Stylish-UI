package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A scaffold with a bottom sheet, wrapping the Material 3
 * [BottomSheetScaffold] with Stylish defaults.
 *
 * The scaffold hosts a persistent, partially expandable [sheetContent]
 * above a full-screen [content] area, with optional [topBar] and
 * [snackbarHost] slots. The sheet's top corners use
 * [StylishTheme.dimensions.connectedCornerRadius], matching
 * [com.segnities007.stylishui.components.organisms.StylishBottomSheet].
 *
 * @param sheetContent The sheet content, laid out inside a
 *   [ColumnScope].
 * @param modifier Modifier applied to the [BottomSheetScaffold] root.
 * @param scaffoldState State of the scaffold (sheet + snackbar host).
 *   Defaults to [rememberBottomSheetScaffoldState].
 * @param sheetPeekHeight Height of the sheet in its peek (collapsed)
 *   state. Defaults to [BottomSheetDefaults.SheetPeekHeight].
 * @param sheetMaxWidth Maximum width of the sheet. Defaults to
 *   [BottomSheetDefaults.SheetMaxWidth].
 * @param sheetShape Corner shape of the sheet. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius] on the top
 *   corners only.
 * @param sheetContainerColor Background color of the sheet. Defaults
 *   to [MaterialTheme.colorScheme.surface].
 * @param sheetContentColor Default content color of the sheet.
 *   Defaults to [contentColorFor] of [sheetContainerColor].
 * @param sheetTonalElevation Tonal elevation of the sheet. Defaults
 *   to 0 dp (the M3 spec value).
 * @param sheetShadowElevation Shadow elevation of the sheet. Defaults
 *   to [BottomSheetDefaults.Elevation].
 * @param sheetDragHandle Optional drag handle composable. When `null`
 *   (the default), **no** drag handle is shown. Pass
 *   `@Composable { BottomSheetDefaults.DragHandle() }` for the M3
 *   default handle.
 * @param sheetSwipeEnabled When `false`, the sheet cannot be swiped
 *   up or down. Defaults to `true`.
 * @param topBar Optional top bar rendered above the content, e.g. a
 *   [StylishTopAppBar]. When null, no top bar is shown.
 * @param snackbarHost Composable that renders transient messages using
 *   the [SnackbarHostState] of [scaffoldState]. Defaults to the M3
 *   [SnackbarHost].
 * @param containerColor Background color behind all content. Defaults
 *   to [MaterialTheme.colorScheme.surface].
 * @param contentColor Default content color propagated to [content].
 *   Defaults to [contentColorFor] of [containerColor].
 * @param content Main page content. Receives the [PaddingValues] that
 *   account for the top bar and the sheet; apply them via
 *   [Modifier.padding].
 *
 * @see com.segnities007.stylishui.components.organisms.StylishBottomSheet
 * @see com.segnities007.stylishui.components.patterns.StylishScaffold
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishBottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetPeekHeight: Dp = BottomSheetDefaults.SheetPeekHeight,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    sheetShape: Shape = RoundedCornerShape(
        topStart = StylishTheme.dimensions.connectedCornerRadius,
        topEnd = StylishTheme.dimensions.connectedCornerRadius,
    ),
    sheetContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
    sheetContentColor: Color = contentColorFor(sheetContainerColor),
    sheetTonalElevation: Dp = 0.dp,
    sheetShadowElevation: Dp = BottomSheetDefaults.Elevation,
    sheetDragHandle: @Composable (() -> Unit)? = null,
    sheetSwipeEnabled: Boolean = true,
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit,
) {
    BottomSheetScaffold(
        sheetContent = sheetContent,
        modifier = modifier.stylishTestTag("bottom_sheet_scaffold"),
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetMaxWidth = sheetMaxWidth,
        sheetShape = sheetShape,
        sheetContainerColor = sheetContainerColor,
        sheetContentColor = sheetContentColor,
        sheetTonalElevation = sheetTonalElevation,
        sheetShadowElevation = sheetShadowElevation,
        sheetDragHandle = sheetDragHandle,
        sheetSwipeEnabled = sheetSwipeEnabled,
        topBar = topBar,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish bottom sheet scaffold content", showBackground = true, widthDp = 393)
@Composable
private fun StylishBottomSheetScaffoldPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishBottomSheetScaffold(
                modifier = Modifier.fillMaxSize(),
                sheetContent = {
                    Text(
                        "ボトムシート",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(StylishTheme.dimensions.contentSpacing),
                    )
                    Text(
                        "シートのコンテンツをここに配置します",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(
                            horizontal = StylishTheme.dimensions.contentSpacing,
                            vertical = StylishTheme.dimensions.itemSpacing,
                        ),
                    )
                },
            ) { innerPadding ->
                Text(
                    "スクリーンのコンテンツ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

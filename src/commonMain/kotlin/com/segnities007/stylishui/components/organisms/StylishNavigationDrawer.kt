package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.coroutines.launch

/**
 * A modal navigation drawer with Stylish theme defaults, wrapping the
 * Material 3 [ModalNavigationDrawer].
 *
 * The drawer overlays the [content] and dims it with [scrimColor]
 * while open; tapping the scrim, swiping, or pressing back dismisses
 * it. [drawerContent] is placed inside a [ModalDrawerSheet] so it
 * inherits the Stylish container colors, shape, elevation, and window
 * insets without any extra setup.
 *
 * @param drawerContent The drawer content, typically a list of
 *   [NavigationDrawerItem]s, laid out inside the sheet's
 *   [ColumnScope].
 * @param modifier Modifier applied to the
 *   [ModalNavigationDrawer] root.
 * @param drawerState State of the drawer, controlling open/closed
 *   position. Defaults to [rememberDrawerState] starting
 *   [DrawerValue.Closed].
 * @param gesturesEnabled When `false`, swipe gestures do not open or
 *   close the drawer. Defaults to `true`.
 * @param shape Corner shape of the drawer sheet. Defaults to
 *   [DrawerDefaults.shape] (the Material 3 drawer shape).
 * @param containerColor Background color of the drawer sheet.
 *   Defaults to [DrawerDefaults.modalContainerColor]
 *   (surfaceContainerLow), the Material 3 modal drawer spec value.
 * @param contentColor Default content color of the drawer sheet.
 *   Defaults to [contentColorFor] of [containerColor].
 * @param tonalElevation Tonal elevation of the drawer sheet.
 *   Defaults to [DrawerDefaults.ModalDrawerElevation].
 * @param windowInsets [WindowInsets] consumed by the drawer sheet.
 *   Defaults to [DrawerDefaults.windowInsets].
 * @param scrimColor Color of the scrim that obscures the [content]
 *   while the drawer is open. Defaults to [DrawerDefaults.scrimColor].
 * @param content The page content behind the drawer, typically the
 *   main screen scaffold.
 *
 * @see StylishDismissibleNavigationDrawer
 * @see StylishPermanentNavigationDrawer
 */
@Composable
public fun StylishModalNavigationDrawer(
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    shape: Shape = DrawerDefaults.shape,
    containerColor: Color = DrawerDefaults.modalContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = DrawerDefaults.ModalDrawerElevation,
    windowInsets: WindowInsets = DrawerDefaults.windowInsets,
    scrimColor: Color = DrawerDefaults.scrimColor,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = shape,
                drawerContainerColor = containerColor,
                drawerContentColor = contentColor,
                drawerTonalElevation = tonalElevation,
                windowInsets = windowInsets,
            ) { drawerContent() }
        },
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        scrimColor = scrimColor,
        content = content,
    )
}

/**
 * A dismissible navigation drawer with Stylish theme defaults,
 * wrapping the Material 3 [DismissibleNavigationDrawer].
 *
 * The drawer sits at the screen edge and stays visible until the user
 * dismisses it; while open it does not dim the [content]. On wide
 * screens prefer [StylishPermanentNavigationDrawer].
 * [drawerContent] is placed inside a [DismissibleDrawerSheet] so it
 * inherits the Stylish container colors, shape, elevation, and window
 * insets without any extra setup.
 *
 * @param drawerContent The drawer content, typically a list of
 *   [NavigationDrawerItem]s, laid out inside the sheet's
 *   [ColumnScope].
 * @param modifier Modifier applied to the
 *   [DismissibleNavigationDrawer] root.
 * @param drawerState State of the drawer, controlling open/closed
 *   position. Defaults to [rememberDrawerState] starting
 *   [DrawerValue.Closed].
 * @param gesturesEnabled When `false`, swipe gestures do not open or
 *   close the drawer. Defaults to `true`.
 * @param shape Corner shape of the drawer sheet. Defaults to
 *   [DrawerDefaults.shape] (the Material 3 drawer shape).
 * @param containerColor Background color of the drawer sheet.
 *   Defaults to [DrawerDefaults.standardContainerColor] (surface),
 *   the Material 3 standard drawer spec value.
 * @param contentColor Default content color of the drawer sheet.
 *   Defaults to [contentColorFor] of [containerColor].
 * @param tonalElevation Tonal elevation of the drawer sheet.
 *   Defaults to [DrawerDefaults.DismissibleDrawerElevation].
 * @param windowInsets [WindowInsets] consumed by the drawer sheet.
 *   Defaults to [DrawerDefaults.windowInsets].
 * @param content The page content beside the drawer, typically the
 *   main screen scaffold.
 *
 * @see StylishModalNavigationDrawer
 * @see StylishPermanentNavigationDrawer
 */
@Composable
public fun StylishDismissibleNavigationDrawer(
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    shape: Shape = DrawerDefaults.shape,
    containerColor: Color = DrawerDefaults.standardContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = DrawerDefaults.DismissibleDrawerElevation,
    windowInsets: WindowInsets = DrawerDefaults.windowInsets,
    content: @Composable () -> Unit,
) {
    DismissibleNavigationDrawer(
        drawerContent = {
            DismissibleDrawerSheet(
                drawerShape = shape,
                drawerContainerColor = containerColor,
                drawerContentColor = contentColor,
                drawerTonalElevation = tonalElevation,
                windowInsets = windowInsets,
            ) { drawerContent() }
        },
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        content = content,
    )
}

/**
 * A permanent navigation drawer with Stylish theme defaults, wrapping
 * the Material 3 [PermanentNavigationDrawer].
 *
 * The drawer is always visible at the start edge beside the [content]
 * and never dismisses — use it on wide screens only.
 * [drawerContent] is placed inside a [PermanentDrawerSheet] so it
 * inherits the Stylish container colors, shape, elevation, and window
 * insets without any extra setup.
 *
 * @param drawerContent The drawer content, typically a list of
 *   [NavigationDrawerItem]s, laid out inside the sheet's
 *   [ColumnScope].
 * @param modifier Modifier applied to the
 *   [PermanentNavigationDrawer] root.
 * @param shape Corner shape of the drawer sheet. Defaults to
 *   [DrawerDefaults.shape] (the Material 3 drawer shape).
 * @param containerColor Background color of the drawer sheet.
 *   Defaults to [DrawerDefaults.standardContainerColor] (surface),
 *   the Material 3 standard drawer spec value.
 * @param contentColor Default content color of the drawer sheet.
 *   Defaults to [contentColorFor] of [containerColor].
 * @param tonalElevation Tonal elevation of the drawer sheet.
 *   Defaults to [DrawerDefaults.PermanentDrawerElevation].
 * @param windowInsets [WindowInsets] consumed by the drawer sheet.
 *   Defaults to [DrawerDefaults.windowInsets].
 * @param content The page content beside the drawer, typically the
 *   main screen scaffold.
 *
 * @see StylishModalNavigationDrawer
 * @see StylishDismissibleNavigationDrawer
 */
@Composable
public fun StylishPermanentNavigationDrawer(
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = DrawerDefaults.shape,
    containerColor: Color = DrawerDefaults.standardContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = DrawerDefaults.PermanentDrawerElevation,
    windowInsets: WindowInsets = DrawerDefaults.windowInsets,
    content: @Composable () -> Unit,
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                drawerShape = shape,
                drawerContainerColor = containerColor,
                drawerContentColor = contentColor,
                drawerTonalElevation = tonalElevation,
                windowInsets = windowInsets,
            ) { drawerContent() }
        },
        modifier = modifier,
        content = content,
    )
}

@Preview(name = "Stylish modal navigation drawer content", showBackground = true, widthDp = 393)
@Composable
private fun StylishModalNavigationDrawerPreview() {
    StylishTheme(darkTheme = false) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        StylishModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Text(
                    "メニュー",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(StylishTheme.dimensions.itemSpacing),
                )
                NavigationDrawerItem(
                    label = { Text("ホーム") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                )
                NavigationDrawerItem(
                    label = { Text("検索") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                )
                NavigationDrawerItem(
                    label = { Text("設定") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                )
            },
        ) {
            Surface(
                Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Text(
                    "モーダルドロワーのコンテンツ",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(name = "Stylish dismissible navigation drawer content", showBackground = true, widthDp = 393)
@Composable
private fun StylishDismissibleNavigationDrawerPreview() {
    StylishTheme(darkTheme = false) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        StylishDismissibleNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawerItem(
                    label = { Text("ホーム") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                )
                NavigationDrawerItem(
                    label = { Text("検索") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                )
                NavigationDrawerItem(
                    label = { Text("設定") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                )
            },
        ) {
            Surface(
                Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Text(
                    "ディスミッシブルドロワーのコンテンツ",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(name = "Stylish permanent navigation drawer content", showBackground = true, widthDp = 393)
@Composable
private fun StylishPermanentNavigationDrawerPreview() {
    StylishTheme(darkTheme = false) {
        StylishPermanentNavigationDrawer(
            drawerContent = {
                Text(
                    "メニュー",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(StylishTheme.dimensions.itemSpacing),
                )
                NavigationDrawerItem(
                    label = { Text("ホーム") },
                    selected = true,
                    onClick = {},
                )
                NavigationDrawerItem(
                    label = { Text("検索") },
                    selected = false,
                    onClick = {},
                )
                NavigationDrawerItem(
                    label = { Text("設定") },
                    selected = false,
                    onClick = {},
                )
            },
        ) {
            Surface(
                Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Text(
                    "パーマネントドロワーのコンテンツ",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

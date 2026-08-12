package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import androidx.compose.ui.unit.Dp

/**
 * A Material 3 bottom app bar with Stylish theme defaults.
 *
 * Displays [actions] (typically navigation and key actions) at the
 * bottom of the screen, with an optional [floatingActionButton]
 * embedded at the end. Wraps [BottomAppBar] with Stylish container
 * colors: [MaterialTheme.colorScheme.surfaceContainerHigh].
 * Designed to be placed inside the `bottomBar` slot of
 * [com.segnities007.stylishui.components.patterns.StylishScaffold].
 *
 * @param actions The action content, laid out horizontally in a
 *   [RowScope], typically [IconButton]s.
 * @param modifier Modifier applied to the [BottomAppBar] root.
 * @param floatingActionButton Optional floating action button rendered
 *   at the end of the bar. When null, no FAB is shown.
 * @param containerColor Background color of the bar. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh], matching the
 *   Stylish header and footer containers.
 * @param contentColor Default content color propagated to children.
 *   Defaults to [contentColorFor] of [containerColor].
 * @param tonalElevation Tonal elevation of the bar. Defaults to
 *   [BottomAppBarDefaults.ContainerElevation] (0 dp, the M3 spec
 *   value).
 * @param contentPadding Padding applied around [actions]. Defaults to
 *   [BottomAppBarDefaults.ContentPadding].
 * @param windowInsets [WindowInsets] consumed by the bar. Defaults to
 *   [BottomAppBarDefaults.windowInsets] so content stays clear of the
 *   system navigation bar.
 *
 * @see com.segnities007.stylishui.components.patterns.StylishScaffold
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishBottomAppBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
) {
    BottomAppBar(
        actions = actions,
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish bottom app bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishBottomAppBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishBottomAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                floatingActionButton = {
                    FloatingActionButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = "追加")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "メニュー")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "検索")
                    }
                },
            )
        }
    }
}

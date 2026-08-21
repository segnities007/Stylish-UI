package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A standard Material 3 top app bar with Stylish theme defaults.
 *
 * This is the Material-parity app bar for screens that follow the
 * platform convention (edge-to-edge content, scrolling behavior,
 * window insets). It wraps [TopAppBar] with Stylish container colors:
 * [MaterialTheme.colorScheme.surfaceContainerHigh] at rest and
 * [MaterialTheme.colorScheme.surfaceContainer] once content scrolls
 * underneath, driven by [scrollBehavior].
 *
 * The floating pill header remains [StylishHeader] — use that when you
 * want the Stylish floating silhouette, and this component when you
 * want standard M3 app-bar behavior (e.g. inside the `topBar` slot of
 * [com.segnities007.stylishui.components.patterns.StylishScaffold] with
 * a [TopAppBarScrollBehavior] such as
 * `TopAppBarDefaults.enterAlwaysScrollBehavior()`).
 *
 * @param title The title to be displayed in the top app bar, typically
 *   a [Text] with the screen name.
 * @param modifier Modifier applied to the [TopAppBar] root.
 * @param navigationIcon Optional composable at the start of the bar,
 *   typically an [IconButton] for back or drawer navigation. When null,
 *   no leading content is shown.
 * @param actions Optional composables at the end of the bar, laid out
 *   horizontally in a [RowScope], typically [IconButton]s.
 * @param windowInsets [WindowInsets] that the app bar respects.
 *   Defaults to [TopAppBarDefaults.windowInsets].
 * @param colors [TopAppBarColors] for the bar. Defaults to Stylish
 *   containers: [MaterialTheme.colorScheme.surfaceContainerHigh] at
 *   rest and [MaterialTheme.colorScheme.surfaceContainer] when scrolled.
 * @param scrollBehavior A [TopAppBarScrollBehavior] that changes the
 *   bar's height and colors as content scrolls. Typically created via
 *   `TopAppBarDefaults.enterAlwaysScrollBehavior()` or
 *   `TopAppBarDefaults.pinnedScrollBehavior()` and shared with the
 *   scrolling content via [Modifier.nestedScroll].
 * @param titleContentColor Override for the title content color. When
 *   `null` (default), [colors] decides the title color.
 * @param navigationIconContentColor Override for the navigation icon
 *   color. When `null` (default), [colors] decides.
 * @param actionIconContentColor Override for the action icons color.
 *   When `null` (default), [colors] decides.
 *
 * @see StylishHeader
 * @see com.segnities007.stylishui.components.patterns.StylishScaffold
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    titleContentColor: Color? = null,
    navigationIconContentColor: Color? = null,
    actionIconContentColor: Color? = null,
) {
    val resolvedColors = if (
        titleContentColor == null &&
        navigationIconContentColor == null &&
        actionIconContentColor == null
    ) {
        colors
    } else {
        TopAppBarDefaults.topAppBarColors(
            containerColor = colors.containerColor,
            scrolledContainerColor = colors.scrolledContainerColor,
            navigationIconContentColor = navigationIconContentColor ?: colors.navigationIconContentColor,
            titleContentColor = titleContentColor ?: colors.titleContentColor,
            actionIconContentColor = actionIconContentColor ?: colors.actionIconContentColor,
        )
    }
    TopAppBar(
        title = title,
        modifier = modifier.stylishTestTag("top_app_bar"),
        navigationIcon = navigationIcon ?: {},
        actions = actions,
        windowInsets = windowInsets,
        colors = resolvedColors,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish top app bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishTopAppBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishTopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "検索")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "その他")
                    }
                },
            )
        }
    }
}

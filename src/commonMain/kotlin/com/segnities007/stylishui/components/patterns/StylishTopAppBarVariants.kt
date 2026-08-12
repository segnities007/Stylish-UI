package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A center-aligned Material 3 top app bar with Stylish theme defaults.
 *
 * The title is horizontally centered between [navigationIcon] and
 * [actions]. This is the Material-parity variant of
 * [StylishTopAppBar] for screens that center their title; it wraps
 * [CenterAlignedTopAppBar] with Stylish container colors
 * (surfaceContainerHigh at rest, surfaceContainer when scrolled).
 *
 * @param title The title to be displayed in the top app bar, typically
 *   a [Text] with the screen name.
 * @param modifier Modifier applied to the [CenterAlignedTopAppBar]
 *   root.
 * @param navigationIcon Optional composable at the start of the bar,
 *   typically an [IconButton] for back or drawer navigation. When
 *   null, no leading content is shown.
 * @param actions Optional composables at the end of the bar, laid out
 *   horizontally in a [RowScope], typically [IconButton]s.
 * @param windowInsets [WindowInsets] that the app bar respects.
 *   Defaults to [TopAppBarDefaults.windowInsets].
 * @param colors [TopAppBarColors] for the bar. Defaults to Stylish
 *   containers: [MaterialTheme.colorScheme.surfaceContainerHigh] at
 *   rest and [MaterialTheme.colorScheme.surfaceContainer] when
 *   scrolled.
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
 * @see StylishTopAppBar
 * @see StylishMediumTopAppBar
 * @see StylishLargeTopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishCenterAlignedTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = stylishTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    titleContentColor: Color? = null,
    navigationIconContentColor: Color? = null,
    actionIconContentColor: Color? = null,
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon ?: {},
        actions = actions,
        windowInsets = windowInsets,
        colors = resolvedTopAppBarColors(
            colors = colors,
            titleContentColor = titleContentColor,
            navigationIconContentColor = navigationIconContentColor,
            actionIconContentColor = actionIconContentColor,
        ),
        scrollBehavior = scrollBehavior,
    )
}

/**
 * A medium Material 3 top app bar with Stylish theme defaults.
 *
 * The title starts large and collapses into the top row as content
 * scrolls. This is the Material-parity variant of [StylishTopAppBar]
 * for screens with a medium app bar; it wraps [MediumTopAppBar] with
 * Stylish container colors (surfaceContainerHigh at rest,
 * surfaceContainer when scrolled).
 *
 * @param title The title to be displayed in the top app bar, typically
 *   a [Text] with the screen name.
 * @param modifier Modifier applied to the [MediumTopAppBar] root.
 * @param navigationIcon Optional composable at the start of the bar,
 *   typically an [IconButton] for back or drawer navigation. When
 *   null, no leading content is shown.
 * @param actions Optional composables at the end of the bar, laid out
 *   horizontally in a [RowScope], typically [IconButton]s.
 * @param windowInsets [WindowInsets] that the app bar respects.
 *   Defaults to [TopAppBarDefaults.windowInsets].
 * @param colors [TopAppBarColors] for the bar. Defaults to Stylish
 *   containers: [MaterialTheme.colorScheme.surfaceContainerHigh] at
 *   rest and [MaterialTheme.colorScheme.surfaceContainer] when
 *   scrolled.
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
 * @see StylishTopAppBar
 * @see StylishCenterAlignedTopAppBar
 * @see StylishLargeTopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishMediumTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = stylishTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    titleContentColor: Color? = null,
    navigationIconContentColor: Color? = null,
    actionIconContentColor: Color? = null,
) {
    MediumTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon ?: {},
        actions = actions,
        windowInsets = windowInsets,
        colors = resolvedTopAppBarColors(
            colors = colors,
            titleContentColor = titleContentColor,
            navigationIconContentColor = navigationIconContentColor,
            actionIconContentColor = actionIconContentColor,
        ),
        scrollBehavior = scrollBehavior,
    )
}

/**
 * A large Material 3 top app bar with Stylish theme defaults.
 *
 * The title sits in a second row below the navigation and actions and
 * collapses as content scrolls. This is the Material-parity variant of
 * [StylishTopAppBar] for screens with a large app bar; it wraps
 * [LargeTopAppBar] with Stylish container colors (surfaceContainerHigh
 * at rest, surfaceContainer when scrolled).
 *
 * @param title The title to be displayed in the top app bar, typically
 *   a [Text] with the screen name.
 * @param modifier Modifier applied to the [LargeTopAppBar] root.
 * @param navigationIcon Optional composable at the start of the bar,
 *   typically an [IconButton] for back or drawer navigation. When
 *   null, no leading content is shown.
 * @param actions Optional composables at the end of the bar, laid out
 *   horizontally in a [RowScope], typically [IconButton]s.
 * @param windowInsets [WindowInsets] that the app bar respects.
 *   Defaults to [TopAppBarDefaults.windowInsets].
 * @param colors [TopAppBarColors] for the bar. Defaults to Stylish
 *   containers: [MaterialTheme.colorScheme.surfaceContainerHigh] at
 *   rest and [MaterialTheme.colorScheme.surfaceContainer] when
 *   scrolled.
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
 * @see StylishTopAppBar
 * @see StylishCenterAlignedTopAppBar
 * @see StylishMediumTopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishLargeTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = stylishTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    titleContentColor: Color? = null,
    navigationIconContentColor: Color? = null,
    actionIconContentColor: Color? = null,
) {
    LargeTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon ?: {},
        actions = actions,
        windowInsets = windowInsets,
        colors = resolvedTopAppBarColors(
            colors = colors,
            titleContentColor = titleContentColor,
            navigationIconContentColor = navigationIconContentColor,
            actionIconContentColor = actionIconContentColor,
        ),
        scrollBehavior = scrollBehavior,
    )
}

/**
 * The Stylish top app bar color defaults shared by all
 * [StylishTopAppBar] variants: surfaceContainerHigh at rest and
 * surfaceContainer once content scrolls underneath.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun stylishTopAppBarColors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
)

/**
 * Resolves [colors] with any explicit content-color overrides applied,
 * mirroring the parameter contract of
 * [com.segnities007.stylishui.components.patterns.StylishTopAppBar].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun resolvedTopAppBarColors(
    colors: TopAppBarColors,
    titleContentColor: Color?,
    navigationIconContentColor: Color?,
    actionIconContentColor: Color?,
): TopAppBarColors {
    if (
        titleContentColor == null &&
        navigationIconContentColor == null &&
        actionIconContentColor == null
    ) {
        return colors
    }
    return TopAppBarDefaults.topAppBarColors(
        containerColor = colors.containerColor,
        scrolledContainerColor = colors.scrolledContainerColor,
        navigationIconContentColor = navigationIconContentColor ?: colors.navigationIconContentColor,
        titleContentColor = titleContentColor ?: colors.titleContentColor,
        actionIconContentColor = actionIconContentColor ?: colors.actionIconContentColor,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish center-aligned top app bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishCenterAlignedTopAppBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishCenterAlignedTopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "その他")
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish medium top app bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishMediumTopAppBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishMediumTopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "検索")
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish large top app bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishLargeTopAppBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishLargeTopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "その他")
                    }
                },
            )
        }
    }
}

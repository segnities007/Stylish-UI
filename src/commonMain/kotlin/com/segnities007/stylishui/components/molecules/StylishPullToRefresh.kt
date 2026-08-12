package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A pull-to-refresh container that wraps scrollable content and shows the
 * Material 3 [PullToRefreshDefaults.Indicator] while a refresh is in
 * progress.
 *
 * This is the Finish-layer wrapper around the Material 3 [PullToRefreshBox]:
 * it forwards every parameter unchanged, so callers get the standard M3
 * gesture, indicator, and state handling under the Stylish theme without
 * importing the experimental `pulltorefresh` API. The indicator uses the
 * active theme's colors via [PullToRefreshDefaults.Indicator]; pass a custom
 * [indicator] to replace it.
 *
 * @param isRefreshing Whether a refresh is currently occurring. Drive this
 *   from the parent (e.g. a `LaunchedEffect` around a network call).
 * @param onRefresh Callback invoked when the user's pull crosses the refresh
 *   threshold.
 * @param modifier Modifier applied to the container [Box] root.
 * @param state The [PullToRefreshState] tracking the pull distance. Defaults
 *   to [rememberPullToRefreshState].
 * @param contentAlignment The default alignment inside the [Box]. Defaults to
 *   [Alignment.TopStart].
 * @param indicator The indicator drawn on top of the content while pulling or
 *   refreshing. Defaults to the Material 3 [PullToRefreshDefaults.Indicator]
 *   aligned to [Alignment.TopCenter].
 * @param content The scrollable content (e.g. a [LazyColumn] or a
 *   `Modifier.verticalScroll` layout). Receives [BoxScope].
 *
 * @see PullToRefreshBox
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        PullToRefreshDefaults.Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state,
        )
    },
    content: @Composable BoxScope.() -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        contentAlignment = contentAlignment,
        indicator = indicator,
        content = content,
    )
}

@Preview(name = "Stylish pull to refresh", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishPullToRefreshPreview() {
    StylishTheme(darkTheme = false) {
        var refreshing by remember { mutableStateOf(false) }
        val items = remember { List(20) { "項目 $it" } }
        StylishPullToRefresh(
            isRefreshing = refreshing,
            onRefresh = { refreshing = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(items) { item ->
                    Text(
                        item,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    )
                }
            }
        }
    }
}

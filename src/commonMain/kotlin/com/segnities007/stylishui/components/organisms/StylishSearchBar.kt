@file:Suppress("DEPRECATION")

package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A search bar with a collapsed input field and an expanded results
 * panel, wrapping the Material 3 [SearchBar] with Stylish defaults.
 *
 * Hoist [query] and [active] to the caller. While [active] is `true`,
 * the bar expands and shows [content] (typically a list of suggestions)
 * below the input field. [onSearch] fires when the user commits a query
 * via the IME search action; [onActiveChange] fires when the bar is
 * expanded or collapsed.
 *
 * @param query Current text of the search query.
 * @param onQueryChange Called with the new text as the user types.
 * @param onSearch Called with the committed query when the user presses
 *   the search action on the keyboard.
 * @param active Whether the search bar is expanded and showing search
 *   results.
 * @param onActiveChange Called when the expanded state should change.
 * @param modifier Modifier applied to the [SearchBar] root.
 * @param placeholder Optional hint text shown when [query] is empty,
 *   typically a [Text] in the on-surface-variant color.
 * @param leadingIcon Optional icon shown at the start of the input
 *   field, typically a search icon.
 * @param trailingIcon Optional icon shown at the end of the input
 *   field, e.g. a clear button.
 * @param shape Corner shape of the collapsed search bar. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param colors [SearchBarColors] for the bar. Defaults to
 *   [SearchBarDefaults.colors] with a
 *   [MaterialTheme.colorScheme.surfaceContainerHigh] container.
 * @param content Content shown below the input field while [active],
 *   typically a column of suggestion [Text]s or rows.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    glass: Boolean = false,
    colors: SearchBarColors = SearchBarDefaults.colors(
        containerColor = if (glass) {
            // 磨りガラス: テーマ連動の半透明+影なし
            val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
            if (isDark) {
                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.55f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.60f)
            }
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
    ),
    content: @Composable ColumnScope.() -> Unit = {},
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        modifier = modifier.stylishTestTag("search_bar"),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        colors = colors,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish search bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishSearchBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var query by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }
            StylishSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { query = it },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("検索キーワード") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "クリア")
                        }
                    }
                },
            ) {
                Column {
                    Text("おすすめの検索", style = MaterialTheme.typography.labelMedium)
                    Text("Stylish UI")
                    Text("Compose")
                }
            }
        }
    }
}

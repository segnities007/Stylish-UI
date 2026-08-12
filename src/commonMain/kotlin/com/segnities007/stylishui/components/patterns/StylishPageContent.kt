package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A page-content pattern that renders a header as the first item of a
 * scrollable lazy list.
 *
 * Wraps a [LazyColumn] whose first item is the [header] composable
 * (typically a [StylishHeader]) followed by a spacer and the caller's
 * list items. Use this inside [StylishScaffold]'s content slot as the
 * standard layout for list-driven screens.
 *
 * @param header Composable rendered as the first item of the list
 *   (scrolls with the content). Typically a [StylishHeader] with the
 *   page title and navigation actions.
 * @param listState [LazyListState] controlling scroll position. Defaults
 *   to [rememberLazyListState]. Supply a hoisted state to observe scroll
 *   offset or programmatically scroll.
 * @param modifier Modifier applied to the underlying [LazyColumn].
 * @param contentPadding [PaddingValues] applied around the entire lazy
 *   list. Defaults to [DefaultStylishDimensions.screenPadding] horizontal
 *   padding (20.dp).
 * @param headerSpacing Vertical gap between the [header] and the first
 *   list item. Defaults to [Dp.Unspecified], which resolves to the active
 *   theme's [StylishTheme.dimensions.itemSpacing] so global theme
 *   overrides apply. Pass an explicit value to override.
 * @param verticalArrangement [Arrangement.Vertical] governing spacing and
 *   alignment of list items. Defaults to [Arrangement.Top].
 * @param horizontalAlignment Horizontal alignment of the list items
 *   inside the [LazyColumn]. Defaults to [Alignment.Start].
 * @param reverseLayout When true, items are laid out bottom to top and
 *   the list scrolls toward its top edge. Defaults to false.
 * @param userScrollEnabled Whether the user can scroll the list via
 *   touch input. Defaults to true.
 * @param content [LazyListScope] receiver where the caller emits list
 *   items via `item {}` / `items {}` builders.
 *
 * @see StylishHeader
 * @see StylishScaffold
 */
@Composable
public fun StylishPageContent(
    header: @Composable () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = DefaultStylishDimensions.screenPadding),
    headerSpacing: Dp = Dp.Unspecified,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    reverseLayout: Boolean = false,
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    val resolvedHeaderSpacing = if (headerSpacing == Dp.Unspecified) {
        StylishTheme.dimensions.itemSpacing
    } else {
        headerSpacing
    }
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = contentPadding,
        state = listState,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        userScrollEnabled = userScrollEnabled,
    ) {
        item {
            header()
            Spacer(Modifier.height(resolvedHeaderSpacing))
        }
        content()
    }
}

@Preview(name = "Page content", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishPageContentPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishPageContent(
                header = {
                    StylishHeader(
                        title = { Text("Page title") },
                        navigation = {
                            StylishIconButton(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                onClick = {},
                            )
                        },
                    )
                },
            ) {
                items(20) { index ->
                    Text(
                        text = "Item $index",
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }
        }
    }
}

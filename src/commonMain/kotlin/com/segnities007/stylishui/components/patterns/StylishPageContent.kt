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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.theme.StylishTheme

/** ヘッダー固定＋スクロール可能なリストのページ構成パターン。一覧画面の基本レイアウトとして使う。 */
@Composable
public fun StylishPageContent(
    header: @Composable () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    headerSpacing: Dp = 8.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = contentPadding,
        state = listState,
        verticalArrangement = verticalArrangement,
    ) {
        item {
            header()
            Spacer(Modifier.height(headerSpacing))
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

package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.theme.StylishTheme

@Composable
fun StylishPageContent(
    header: @Composable () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        state = listState,
    ) {
        item {
            header()
            Spacer(Modifier.height(8.dp))
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

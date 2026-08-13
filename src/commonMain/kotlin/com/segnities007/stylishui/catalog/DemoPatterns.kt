package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.patterns.StylishFooter
import com.segnities007.stylishui.components.patterns.StylishHeader
import com.segnities007.stylishui.components.patterns.StylishPageContent
import com.segnities007.stylishui.components.patterns.StylishScaffold
import com.segnities007.stylishui.components.atoms.StylishFab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DemoPatterns(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Header / Footer",
        description = "浮遊型ヘッダーとフッター。",
        code = """StylishHeader(
    title = { Text("タイトル") },
    navigation = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) } },
    actions = { IconButton(onClick = {}) { Icon(Icons.Default.Add, null) } },
)
StylishFooter { Text("フッター") }""",
        modifier = modifier,
    ) {
        Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            StylishHeader(
                title = { Text("Stylish Header") },
                navigation = {
                    androidx.compose.material3.IconButton(onClick = {}) {
                        androidx.compose.material3.Icon(Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    androidx.compose.material3.IconButton(onClick = {}) {
                        androidx.compose.material3.Icon(Icons.Default.Add, contentDescription = null)
                    }
                },
            )
            StylishFooter(
                content = {
                    Text(
                        "Footer content",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
        }
    }

    StylishDemoCard(
        title = "Scaffold",
        description = "アプリの骨組み（トップバー・FAB・コンテンツ）。",
        code = """StylishScaffold(
    topBar = { StylishTopAppBar(title = { Text("タイトル") }) },
    floatingActionButton = { StylishFab(Icons.Default.Add, "追加", {}) },
) { padding -> content(padding) }""",
        modifier = modifier,
    ) {
        StylishScaffold(
            topBar = {
                com.segnities007.stylishui.components.patterns.StylishTopAppBar(
                    title = { Text("Scaffold") },
                )
            },
            floatingActionButton = {
                StylishFab(Icons.Default.Add, "追加", {})
            },
            modifier = Modifier.height(220.dp),
        ) { padding ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text(
                    "Scaffold はトップバー・FAB・コンテンツ領域をまとめるページ骨組みです。",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }

    StylishDemoCard(
        title = "Page content",
        description = "ヘッダー付きのスクロールページ。",
        code = """StylishPageContent(
    header = { StylishHeader(title = { Text("ページ") }) },
) {
    item { Text("コンテンツ") }
}""",
        modifier = modifier,
    ) {
        StylishPageContent(
            header = {
                StylishHeader(title = { Text("Page Content") })
            },
            modifier = Modifier.height(200.dp),
        ) {
            item {
                Text("スクロール可能なページコンテンツです。", style = MaterialTheme.typography.bodyMedium)
            }
            item {
                StylishButton(onClick = {}) { Text("ボタン") }
            }
        }
    }
}

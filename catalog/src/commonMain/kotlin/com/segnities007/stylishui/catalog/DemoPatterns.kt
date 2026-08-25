package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.atoms.StylishGlassSurface
import com.segnities007.stylishui.components.atoms.StylishGlassVariant
import com.segnities007.stylishui.components.atoms.StylishJapaneseGlass
import com.segnities007.stylishui.components.molecules.StylishSkeletonCard
import com.segnities007.stylishui.components.patterns.StylishFooter
import com.segnities007.stylishui.components.patterns.StylishHeader
import com.segnities007.stylishui.components.patterns.StylishPageContent
import com.segnities007.stylishui.components.patterns.StylishScaffold
import com.segnities007.stylishui.components.patterns.StylishTopAppBar

/**
 * Returns all pattern-related demo components for the catalog.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal fun getPatternDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Header",
        category = DemoCategory.Patterns,
        code = """StylishHeader(
    title = { Text("タイトル") },
    navigation = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) } },
    actions = { IconButton(onClick = {}) { Icon(Icons.Default.Add, null) } },
)""",
        preview = {
            StylishHeader(
                title = { Text("Stylish Header") },
                navigation = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                },
            )
        },
    ),
    DemoComponent(
        name = "Footer",
        category = DemoCategory.Patterns,
        code = """StylishFooter {
    Text("フッター")
}""",
        preview = {
            StylishFooter(
                content = {
                    Text(
                        "© 2026 Stylish UI. All rights reserved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
        },
    ),
    DemoComponent(
        name = "Glass surface",
        category = DemoCategory.Patterns,
        code = """StylishGlassSurface {
    Text("ガラス風コンテンツ", modifier = Modifier.padding(16.dp))
}""",
        preview = {
            Box(Modifier.padding(20.dp)) {
                StylishGlassSurface {
                    Text(
                        "ガラス風サーフェス",
                        modifier = androidx.compose.ui.Modifier.padding(16.dp),
                    )
                }
            }
        },
    ),
    DemoComponent(
        name = "Glass variants",
        category = DemoCategory.Patterns,
        code = """StylishGlassSurface(variant = StylishGlassVariant.Regular) {
    Text("Regular: 可読性優先", modifier = Modifier.padding(16.dp))
}
StylishGlassSurface(variant = StylishGlassVariant.Clear) {
    Text("Clear: 高透過", modifier = Modifier.padding(16.dp))
}""",
        preview = {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishGlassSurface(variant = StylishGlassVariant.Regular) {
                    Text(
                        "Regular: 可読性優先",
                        modifier = androidx.compose.ui.Modifier.padding(16.dp),
                    )
                }
                StylishGlassSurface(variant = StylishGlassVariant.Clear) {
                    Text(
                        "Clear: 高透過",
                        modifier = androidx.compose.ui.Modifier.padding(16.dp),
                    )
                }
            }
        },
    ),
    DemoComponent(
        name = "Japanese glass presets",
        category = DemoCategory.Patterns,
        code = """StylishGlassSurface(preset = StylishJapaneseGlass.Sumi) {
    Text("墨", modifier = Modifier.padding(16.dp))
}
StylishGlassSurface(preset = StylishJapaneseGlass.Sakura) {
    Text("桜", modifier = Modifier.padding(16.dp))
}""",
        preview = {
            Row(
                Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                listOf(
                    "墨" to StylishJapaneseGlass.Sumi,
                    "藍" to StylishJapaneseGlass.Ai,
                    "桜" to StylishJapaneseGlass.Sakura,
                    "紅" to StylishJapaneseGlass.Beni,
                ).forEach { (label, preset) ->
                    StylishGlassSurface(preset = preset) {
                        Text(
                            label,
                            modifier = androidx.compose.ui.Modifier.padding(16.dp),
                        )
                    }
                }
            }
        },
    ),
    DemoComponent(
        name = "Scaffold",
        category = DemoCategory.Patterns,
        code = """StylishScaffold(
    header = { Text("タイトル", style = MaterialTheme.typography.titleLarge) },
    floatingActionButton = { StylishFab(Icons.Default.Add, "追加", {}) },
) { headerHeight ->
    Content(Modifier.padding(top = headerHeight))
}""",
        preview = {
            StylishScaffold(
                header = {
                    Text("Scaffold", style = MaterialTheme.typography.titleLarge)
                },
                floatingActionButton = {
                    StylishFab(Icons.Default.Add, "追加", {})
                },
            ) { headerHeight ->
                Column(
                    Modifier
                        .padding(top = headerHeight)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        "Scaffold は浮遊ヘッダー・FAB・コンテンツ領域をまとめるページ骨組みです。",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
    ),
    DemoComponent(
        name = "Page content",
        category = DemoCategory.Patterns,
        code = """StylishPageContent(
    header = { StylishHeader(title = { Text("ページ") }) },
) {
    item { Text("コンテンツ") }
}""",
        preview = {
            StylishPageContent(
                header = {
                    StylishHeader(title = { Text("Page Content") })
                },
            ) {
                item {
                    Text("スクロール可能なページコンテンツです。", style = MaterialTheme.typography.bodyMedium)
                }
                item {
                    StylishButton(onClick = {}) { Text("ボタン") }
                }
            }
        },
    ),
    DemoComponent(
        name = "Skeleton card",
        category = DemoCategory.Patterns,
        code = """StylishSkeletonCard(Modifier.fillMaxWidth())""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishSkeletonCard(Modifier.fillMaxWidth())
                StylishSkeletonCard(Modifier.fillMaxWidth())
            }
        },
    ),
)

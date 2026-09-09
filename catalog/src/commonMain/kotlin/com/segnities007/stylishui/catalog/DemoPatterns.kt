package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishFloatingFab
import com.segnities007.stylishui.components.molecules.StylishSkeletonCard
import com.segnities007.stylishui.components.patterns.StylishFooter
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishLayerColor
import com.segnities007.stylishui.components.patterns.StylishFloatingTopBar
import com.segnities007.stylishui.components.patterns.StylishPageContent
import com.segnities007.stylishui.components.patterns.StylishScaffold

/**
 * Returns all pattern-related demo components for the catalog.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal fun getPatternDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Header",
        category = DemoCategory.Patterns,
        code = """StylishFloatingTopBar(
    title = { Text("タイトル") },
    navigation = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) } },
    actions = { IconButton(onClick = {}) { Icon(Icons.Default.Add, null) } },
)""",
        preview = {
            StylishFloatingTopBar(
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
        name = "Scaffold",
        category = DemoCategory.Patterns,
        code = """StylishScaffold(
    header = { Text("タイトル", style = MaterialTheme.typography.titleLarge) },
    floatingActionButton = { StylishFloatingFab(Icons.Default.Add, "追加", {}) },
) { headerHeight ->
    Content(Modifier.padding(top = headerHeight))
}""",
        preview = {
            // StylishScaffold fills its parent and lays out with infinite
            // maxHeight when unbounded: bound it so the catalog grid item
            // never hands it infinite constraints.
            Box(Modifier.fillMaxWidth().height(320.dp)) {
                StylishScaffold(
                    header = {
                        Text("Scaffold", style = MaterialTheme.typography.titleLarge)
                    },
                    floatingBottomCenter = {
                        androidx.compose.material3.Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                            color = stylishLayerColor(level = 0.62f),
                        ) {
                            Text(
                                "indicator",
                                modifier = androidx.compose.ui.Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                    },
                    floatingActionButton = {
                        StylishFloatingFab(Icons.Default.Add, "追加", {})
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
            }
        },
    ),
    DemoComponent(
        name = "Page content",
        category = DemoCategory.Patterns,
        code = """StylishPageContent(
    header = { StylishFloatingTopBar(title = { Text("ページ") }) },
) {
    item { Text("コンテンツ") }
}""",
        preview = {
            Box(Modifier.fillMaxWidth().height(320.dp)) {
                StylishPageContent(
                    header = {
                        StylishFloatingTopBar(title = { Text("Page Content") })
                    },
                ) {
                    item {
                        Text("スクロール可能なページコンテンツです。", style = MaterialTheme.typography.bodyMedium)
                    }
                    item {
                        StylishButton(onClick = {}) { Text("ボタン") }
                    }
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
    DemoComponent(
        name = "Elevation levels",
        category = DemoCategory.Patterns,
        code = """(0..8).forEach { step ->
    val level = step / 8f
    Surface(color = stylishLayerColor(level)) { Text("level = ${'$'}level") }
}""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                (0..8).forEach { step ->
                    val level = step / 8f
                    Surface(
                        color = stylishLayerColor(level),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            StylishTheme.dimensions.outlineWidth,
                            MaterialTheme.colorScheme.outlineVariant,
                        ),
                    ) {
                        Text(
                            "level = $level",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        },
    ),
)

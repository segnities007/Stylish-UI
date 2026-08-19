package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.organisms.StylishModalNavigationDrawer
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.components.organisms.StylishNavigationRail
import com.segnities007.stylishui.components.organisms.StylishNavigationRailItem
import com.segnities007.stylishui.components.organisms.StylishSearchBar
import com.segnities007.stylishui.components.organisms.StylishShortNavigationBar
import com.segnities007.stylishui.components.organisms.StylishShortNavigationBarItem
import com.segnities007.stylishui.components.patterns.StylishBottomAppBar
import com.segnities007.stylishui.components.patterns.StylishCenterAlignedTopAppBar
import com.segnities007.stylishui.components.patterns.StylishLargeTopAppBar
import com.segnities007.stylishui.components.patterns.StylishMediumTopAppBar
import com.segnities007.stylishui.components.patterns.StylishTopAppBar
import kotlinx.coroutines.launch

/**
 * Returns all navigation-related demo components for the catalog.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal fun getNavigationDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Navigation bar",
        category = DemoCategory.Navigation,
        code = """StylishNavigationBar(
    items = listOf(
        StylishNavigationItem(Icons.Default.Home, "ホーム", selected = index == 0),
        StylishNavigationItem(Icons.Default.Search, "検索", selected = index == 1),
    ),
)""",
        preview = {
            var index by remember { mutableIntStateOf(0) }
            StylishNavigationBar(
                items = listOf(
                    StylishNavigationItem(Icons.Default.Home, "ホーム", selected = index == 0),
                    StylishNavigationItem(Icons.Default.Search, "検索", selected = index == 1),
                    StylishNavigationItem(Icons.Default.Settings, "設定", selected = index == 2),
                ),
            )
        },
    ),
    DemoComponent(
        name = "Short navigation bar",
        category = DemoCategory.Navigation,
        code = """StylishShortNavigationBar {
    StylishShortNavigationBarItem(
        selected = index == 0,
        onClick = { index = 0 },
        icon = { Icon(Icons.Default.Home, null) },
        label = { Text("ホーム") },
    )
}""",
        preview = {
            var index by remember { mutableIntStateOf(0) }
            StylishShortNavigationBar {
                StylishShortNavigationBarItem(
                    selected = index == 0,
                    onClick = { index = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("ホーム") },
                )
                StylishShortNavigationBarItem(
                    selected = index == 1,
                    onClick = { index = 1 },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("検索") },
                )
                StylishShortNavigationBarItem(
                    selected = index == 2,
                    onClick = { index = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("設定") },
                )
            }
        },
    ),
    DemoComponent(
        name = "Navigation rail",
        category = DemoCategory.Navigation,
        code = """StylishNavigationRail {
    StylishNavigationRailItem(selected = index == 0, onClick = { index = 0 },
        icon = { Icon(Icons.Default.Home, null) }, label = { Text("ホーム") })
}""",
        preview = {
            var index by remember { mutableIntStateOf(0) }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                StylishNavigationRail {
                    StylishNavigationRailItem(
                        selected = index == 0,
                        onClick = { index = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("ホーム") },
                    )
                    StylishNavigationRailItem(
                        selected = index == 1,
                        onClick = { index = 1 },
                        icon = { Icon(Icons.Default.Search, contentDescription = null) },
                        label = { Text("検索") },
                    )
                    StylishNavigationRailItem(
                        selected = index == 2,
                        onClick = { index = 2 },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text("設定") },
                    )
                }
            }
        },
    ),
    DemoComponent(
        name = "Navigation drawer",
        category = DemoCategory.Navigation,
        code = """StylishModalNavigationDrawer(
    drawerState = rememberDrawerState(DrawerValue.Open),
    drawerContent = { Text("メニュー項目") },
) { content }""",
        preview = {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                StylishButton(onClick = { scope.launch { drawerState.open() } }) { Text("ドロワーを開く") }
            }
            StylishModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    Column(Modifier.padding(20.dp)) {
                        Text("ホーム", style = MaterialTheme.typography.titleMedium)
                        Text("検索")
                        Text("設定")
                    }
                },
            ) {
                Box(Modifier.fillMaxWidth().height(80.dp)) { }
            }
        },
    ),
    DemoComponent(
        name = "Search bar",
        category = DemoCategory.Navigation,
        code = """StylishSearchBar(
    query = query,
    onQueryChange = { query = it },
    active = active,
    onActiveChange = { active = it },
    placeholder = { Text("検索") },
) { Text("候補") }""",
        preview = {
            var query by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }
            StylishSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("検索") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
            ) {
                Text("Stylish UI")
                Text("Compose Multiplatform")
            }
        },
    ),
    DemoComponent(
        name = "Top app bar",
        category = DemoCategory.Navigation,
        code = """StylishTopAppBar(
    title = { Text("設定") },
)""",
        preview = {
            StylishTopAppBar(
                title = { Text("設定") },
            )
        },
    ),
    DemoComponent(
        name = "Center-aligned top app bar",
        category = DemoCategory.Navigation,
        code = """StylishCenterAlignedTopAppBar(
    title = { Text("中央寄せ") },
)""",
        preview = {
            StylishCenterAlignedTopAppBar(
                title = { Text("中央寄せタイトル") },
            )
        },
    ),
    DemoComponent(
        name = "Medium top app bar",
        category = DemoCategory.Navigation,
        code = """StylishMediumTopAppBar(
    title = { Text("ミディアム") },
)""",
        preview = {
            StylishMediumTopAppBar(
                title = { Text("ミディアムタイトル") },
            )
        },
    ),
    DemoComponent(
        name = "Large top app bar",
        category = DemoCategory.Navigation,
        code = """StylishLargeTopAppBar(
    title = { Text("ラージ") },
)""",
        preview = {
            StylishLargeTopAppBar(
                title = { Text("ラージタイトル") },
            )
        },
    ),
    DemoComponent(
        name = "Bottom app bar",
        category = DemoCategory.Navigation,
        code = """StylishBottomAppBar(actions = {
    IconButton(onClick = {}) { Icon(Icons.Default.Search, null) }
})""",
        preview = {
            StylishBottomAppBar(actions = {
                androidx.compose.material3.IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "検索")
                }
                androidx.compose.material3.IconButton(onClick = {}) {
                    Icon(Icons.Default.Settings, contentDescription = "設定")
                }
            })
        },
    ),
)

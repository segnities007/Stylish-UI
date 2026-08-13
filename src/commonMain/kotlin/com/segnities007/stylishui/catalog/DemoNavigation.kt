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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.organisms.StylishModalNavigationDrawer
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.components.organisms.StylishNavigationRail
import com.segnities007.stylishui.components.organisms.StylishNavigationRailItem
import com.segnities007.stylishui.components.organisms.StylishSearchBar
import com.segnities007.stylishui.components.atoms.StylishButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DemoNavigation(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Navigation bar",
        description = "下部ナビゲーションの項目選択。",
        code = """StylishNavigationBar(
    items = listOf(
        StylishNavigationItem(Icons.Default.Home, "ホーム", selected = index == 0),
        StylishNavigationItem(Icons.Default.Search, "検索", selected = index == 1),
    ),
)""",
        modifier = modifier,
    ) {
        var index by remember { mutableIntStateOf(0) }
        StylishNavigationBar(
            items = listOf(
                StylishNavigationItem(Icons.Default.Home, "ホーム", selected = index == 0),
                StylishNavigationItem(Icons.Default.Search, "検索", selected = index == 1),
                StylishNavigationItem(Icons.Default.Settings, "設定", selected = index == 2),
            ),
        )
    }

    StylishDemoCard(
        title = "Navigation rail",
        description = "横並びのナビゲーション（タブレット/デスクトップ向け）。",
        code = """StylishNavigationRail {
    StylishNavigationRailItem(selected = index == 0, onClick = { index = 0 },
        icon = { Icon(Icons.Default.Home, null) }, label = { Text("ホーム") })
}""",
        modifier = modifier,
    ) {
        var index by remember { mutableIntStateOf(0) }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            StylishNavigationRail {
                StylishNavigationRailItem(
                    selected = index == 0,
                    onClick = { index = 0 },
                    icon = { androidx.compose.material3.Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("ホーム") },
                )
                StylishNavigationRailItem(
                    selected = index == 1,
                    onClick = { index = 1 },
                    icon = { androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("検索") },
                )
                StylishNavigationRailItem(
                    selected = index == 2,
                    onClick = { index = 2 },
                    icon = { androidx.compose.material3.Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("設定") },
                )
            }
        }
    }

    StylishDemoCard(
        title = "Navigation drawer",
        description = "左から開くモーダルドロワー。",
        code = """StylishModalNavigationDrawer(
    drawerState = rememberDrawerState(DrawerValue.Open),
    drawerContent = { Text("メニュー項目") },
) { content }""",
        modifier = modifier,
    ) {
        val drawerState = androidx.compose.material3.rememberDrawerState(
            androidx.compose.material3.DrawerValue.Closed,
        )
        val scope = rememberCoroutineScope()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StylishButton(onClick = { scope.launch { drawerState.open() } }) { Text("ドロワーを開く") }
        }
        StylishModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column(Modifier.padding(20.dp)) {
                    Text("ホーム", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    Text("検索")
                    Text("設定")
                }
            },
        ) {
            Box(Modifier.fillMaxWidth().height(80.dp)) { }
        }
    }

    StylishDemoCard(
        title = "Search bar",
        description = "検索バー（展開して候補表示）。",
        code = """StylishSearchBar(
    query = query,
    onQueryChange = { query = it },
    active = active,
    onActiveChange = { active = it },
    placeholder = { Text("検索") },
) { Text("候補") }""",
        modifier = modifier,
    ) {
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
                androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = null)
            },
        ) {
            Text("Stylish UI")
            Text("Compose Multiplatform")
        }
    }
}

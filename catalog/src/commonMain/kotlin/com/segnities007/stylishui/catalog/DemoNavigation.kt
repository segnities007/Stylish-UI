package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFloatingPagerIndicator
import com.segnities007.stylishui.components.models.StylishFloatingBottomBarItem
import com.segnities007.stylishui.components.models.StylishGradientFooterItem
import com.segnities007.stylishui.components.organisms.StylishFloatingNavigationRail
import com.segnities007.stylishui.components.organisms.StylishGradientNavigationRail
import com.segnities007.stylishui.components.organisms.StylishSearchBar
import com.segnities007.stylishui.components.patterns.StylishBottomAppBar
import com.segnities007.stylishui.components.patterns.StylishCenterAlignedTopAppBar
import com.segnities007.stylishui.components.patterns.StylishFloatingBottomBar
import com.segnities007.stylishui.components.patterns.StylishGradientFooter
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 320.dp),
            ) {
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
            }
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
    DemoComponent(
        name = "Gradient footer",
        category = DemoCategory.Navigation,
        code = """var selected by remember { mutableStateOf("memo") }
StylishGradientFooter(
    items = listOf(
        StylishGradientFooterItem("memo", Icons.Default.Description, "メモ"),
        StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
        StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
    ),
    selectedKey = selected,
    onItemClick = { selected = it },
)""",
        preview = {
            var selected by remember { mutableStateOf("memo") }
            StylishGradientFooter(
                items = listOf(
                    StylishGradientFooterItem("memo", Icons.Default.Description, "メモ"),
                    StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
                    StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
                ),
                selectedKey = selected,
                onItemClick = { selected = it },
            )
        },
    ),
    DemoComponent(
        name = "Floating bottom bar",
        category = DemoCategory.Navigation,
        code = """var selected by remember { mutableStateOf("home") }
StylishFloatingBottomBar(
    items = listOf(
        StylishFloatingBottomBarItem("home", Icons.Default.Home, "ホーム"),
        StylishFloatingBottomBarItem("records", Icons.Default.History, "履歴"),
        StylishFloatingBottomBarItem("notifications", Icons.Default.Notifications, "通知"),
        StylishFloatingBottomBarItem("settings", Icons.Default.Settings, "設定"),
    ),
    selectedKey = selected,
    onItemClick = { selected = it },
)""",
        preview = {
            var selected by remember { mutableStateOf("home") }
            StylishFloatingBottomBar(
                items = listOf(
                    StylishFloatingBottomBarItem("home", Icons.Default.Home, "ホーム"),
                    StylishFloatingBottomBarItem("records", Icons.Default.History, "履歴"),
                    StylishFloatingBottomBarItem("notifications", Icons.Default.Notifications, "通知"),
                    StylishFloatingBottomBarItem("settings", Icons.Default.Settings, "設定"),
                ),
                selectedKey = selected,
                onItemClick = { selected = it },
            )
        },
    ),
    DemoComponent(
        name = "Floating pager indicator",
        category = DemoCategory.Navigation,
        code = """var page by remember { mutableIntStateOf(0) }
StylishFloatingPagerIndicator(
    pageCount = 5,
    currentPage = page,
    onPageSelected = { page = it },
)""",
        preview = {
            var page by remember { mutableIntStateOf(0) }
            StylishFloatingPagerIndicator(
                pageCount = 5,
                currentPage = page,
                onPageSelected = { page = it },
            )
        },
    ),
    DemoComponent(
        name = "Gradient navigation rail",
        category = DemoCategory.Navigation,
        code = """var selected by remember { mutableStateOf("memo") }
StylishGradientNavigationRail(
    items = listOf(
        StylishGradientFooterItem("memo", Icons.Default.Description, "メモ"),
        StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
        StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
    ),
    selectedKey = selected,
    onItemClick = { selected = it },
)""",
        preview = {
            var selected by remember { mutableStateOf("memo") }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                StylishGradientNavigationRail(
                    items = listOf(
                        StylishGradientFooterItem("memo", Icons.Default.Description, "メモ"),
                        StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
                        StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
                    ),
                    selectedKey = selected,
                    onItemClick = { selected = it },
                )
            }
        },
    ),
    DemoComponent(
        name = "Floating navigation rail",
        category = DemoCategory.Navigation,
        code = """var selected by remember { mutableStateOf("home") }
StylishFloatingNavigationRail(
    items = listOf(
        StylishGradientFooterItem("home", Icons.Default.Home, "ホーム"),
        StylishGradientFooterItem("search", Icons.Default.Search, "検索"),
        StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
    ),
    selectedKey = selected,
    onItemClick = { selected = it },
)""",
        preview = {
            var selected by remember { mutableStateOf("home") }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                StylishFloatingNavigationRail(
                    items = listOf(
                        StylishGradientFooterItem("home", Icons.Default.Home, "ホーム"),
                        StylishGradientFooterItem("search", Icons.Default.Search, "検索"),
                        StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
                    ),
                    selectedKey = selected,
                    onItemClick = { selected = it },
                )
            }
        },
    ),
)

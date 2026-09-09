package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.segnities007.stylishui.components.atoms.StylishBadge
import com.segnities007.stylishui.components.atoms.StylishBadgedBox
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishCard
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishChipVariant
import com.segnities007.stylishui.components.atoms.StylishDotIndicator
import com.segnities007.stylishui.components.atoms.StylishDropdownMenu
import com.segnities007.stylishui.components.atoms.StylishDropdownMenuItem
import com.segnities007.stylishui.components.atoms.StylishExtendedFab
import com.segnities007.stylishui.components.atoms.StylishFilledIconButton
import com.segnities007.stylishui.components.atoms.StylishFilledIconToggleButton
import com.segnities007.stylishui.components.atoms.StylishFilledTonalIconButton
import com.segnities007.stylishui.components.atoms.StylishFilledTonalIconToggleButton
import com.segnities007.stylishui.components.atoms.StylishHorizontalDivider
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.components.atoms.StylishIconToggleButton
import com.segnities007.stylishui.components.atoms.StylishOutlinedIconButton
import com.segnities007.stylishui.components.atoms.StylishOutlinedIconToggleButton
import com.segnities007.stylishui.components.atoms.StylishRoundedIconButton
import com.segnities007.stylishui.components.atoms.StylishTooltip
import com.segnities007.stylishui.components.atoms.StylishVerticalDivider

/**
 * Returns all button-related demo components for the catalog.
 */
internal fun getButtonDemos(): List<DemoComponent> = listOf(

    DemoComponent(
        name = "Loading button",
        category = DemoCategory.Buttons,
        code = """StylishButton(
    onClick = {},
    isLoading = loading,
) { Text("実行") }""",
        preview = {
            // The label stays put while loading: content turns invisible
            // under a centered spinner instead of being replaced.
            var loading by remember { mutableStateOf(false) }
            StylishButton(onClick = { loading = !loading }, isLoading = loading) {
                Text("実行")
            }
        },
    ),
    DemoComponent(
        name = "Icon buttons",
        category = DemoCategory.Buttons,
        code = """StylishTooltip(text = "アイテムを追加") {
    StylishIconButton(Icons.Default.Add, "追加", {})
}
StylishRoundedIconButton(Icons.Default.Add, "追加", {})
StylishFilledIconButton(onClick = onClick) { Icon(Icons.Default.Add, "Add") }
StylishFilledTonalIconButton(onClick = onClick) { Icon(Icons.Default.Settings, "Settings") }
StylishOutlinedIconButton(onClick = onClick) { Icon(Icons.Default.Delete, "Delete") }""",
        preview = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StylishTooltip(text = "アイテムを追加") {
                    StylishIconButton(Icons.Default.Add, "追加", {})
                }
                StylishRoundedIconButton(Icons.Default.Favorite, "お気に入り", {})
                StylishIconButton(Icons.Default.Share, "共有", {})
                StylishFilledIconButton(onClick = {}) { Icon(Icons.Default.Add, "Add") }
                StylishFilledTonalIconButton(onClick = {}) { Icon(Icons.Default.Settings, "Settings") }
                StylishOutlinedIconButton(onClick = {}) { Icon(Icons.Default.Delete, "Delete") }
            }
        },
    ),
    DemoComponent(
        name = "Icon toggle buttons",
        category = DemoCategory.Buttons,
        code = """StylishFilledIconToggleButton(
    checked = checked,
    onCheckedChange = { checked = it },
) { Icon(Icons.Default.Favorite, null) }""",
        preview = {
            var checked1 by remember { mutableStateOf(false) }
            var checked2 by remember { mutableStateOf(true) }
            var checked3 by remember { mutableStateOf(false) }
            var checked4 by remember { mutableStateOf(true) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StylishIconToggleButton(checked = checked1, onCheckedChange = { checked1 = it }) {
                    Icon(
                        if (checked1) Icons.Default.Favorite else Icons.Default.Add,
                        contentDescription = null,
                    )
                }
                StylishFilledIconToggleButton(checked = checked2, onCheckedChange = { checked2 = it }) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
                StylishFilledTonalIconToggleButton(checked = checked3, onCheckedChange = { checked3 = it }) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
                StylishOutlinedIconToggleButton(checked = checked4, onCheckedChange = { checked4 = it }) {
                    Icon(
                        if (checked4) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                    )
                }
            }
        },
    ),

    DemoComponent(
        name = "Extended FAB",
        category = DemoCategory.Buttons,
        code = """StylishExtendedFab(
    text = "新規作成",
    icon = Icons.Default.Add,
    onClick = {},
)""",
        preview = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishExtendedFab(
                    text = "新規作成",
                    icon = Icons.Default.Add,
                    onClick = {},
                )
                StylishExtendedFab(
                    text = "編集",
                    icon = Icons.Default.Edit,
                    onClick = {},
                )
            }
        },
    ),
    DemoComponent(
        name = "Chips",
        category = DemoCategory.Buttons,
        code = """StylishChip(label = "選択中", onClick = {}, selected = selected)
StylishChip(label = "フィルタ", onClick = {}, variant = StylishChipVariant.Filter, selected = selected)""",
        preview = {
            var selected by remember { mutableStateOf(true) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishChip(label = "通常", onClick = {}, selected = selected)
                StylishChip(
                    label = "フィルタ",
                    onClick = { selected = !selected },
                    variant = StylishChipVariant.Filter,
                    selected = selected,
                )
                StylishChip(label = "無効", onClick = {}, enabled = false)
            }
        },
    ),
    DemoComponent(
        name = "Badge / BadgedBox",
        category = DemoCategory.Buttons,
        code = """StylishBadge { Text("99+") }
StylishBadgedBox(badge = { StylishBadge { Text("3") } }) {
    Icon(Icons.Default.Home, null)
}""",
        preview = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StylishBadge { Text("3") }
                StylishBadge { Text("99+") }
                StylishBadgedBox(badge = { StylishBadge { Text("5") } }) {
                    Icon(Icons.Default.Favorite, contentDescription = "通知")
                }
                StylishBadgedBox(badge = { StylishBadge { Text("12") } }) {
                    Icon(Icons.Default.Share, contentDescription = "共有")
                }
            }
        },
    ),
    DemoComponent(
        name = "Dropdown menu",
        category = DemoCategory.Buttons,
        code = """StylishDropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false },
) {
    StylishDropdownMenuItem(text = { Text("編集") }, onClick = {})
    StylishDropdownMenuItem(text = { Text("削除") }, onClick = {})
}""",
        preview = {
            var expanded by remember { mutableStateOf(false) }
            androidx.compose.foundation.layout.Box {
                StylishButton(onClick = { expanded = true }, variant = StylishButtonVariant.Outlined) {
                    Text("メニューを開く")
                }
                StylishDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    StylishDropdownMenuItem(text = { Text("編集") }, onClick = { expanded = false })
                    StylishDropdownMenuItem(text = { Text("複製") }, onClick = { expanded = false })
                    StylishDropdownMenuItem(text = { Text("削除") }, onClick = { expanded = false })
                    StylishDropdownMenuItem(text = { Text("無効") }, onClick = {}, enabled = false)
                }
            }
        },
    ),
    DemoComponent(
        name = "Card",
        category = DemoCategory.Buttons,
        code = """StylishCard(
    title = "Actionable",
    supportingText = "Click and elevation",
    onClick = {},
)""",
        preview = {
            StylishCard(
                title = "クリック可能なカード",
                supportingText = "タップで反応するカードコンポーネント",
                onClick = {},
            )
            StylishCard(
                title = "読み取り専用",
                supportingText = "クリックや押下の反応がないカード",
            )
        },
    ),
    DemoComponent(
        name = "Divider",
        category = DemoCategory.Buttons,
        code = """StylishHorizontalDivider(Modifier.fillMaxWidth())
StylishVerticalDivider()""",
        preview = {
            StylishHorizontalDivider(Modifier.fillMaxWidth())
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("左")
                StylishVerticalDivider()
                Text("中央")
                StylishVerticalDivider()
                Text("右")
            }
            StylishHorizontalDivider(Modifier.fillMaxWidth())
        },
    ),
    DemoComponent(
        name = "Dot indicator",
        category = DemoCategory.Buttons,
        code = """StylishDotIndicator(
    pageCount = 5,
    currentPage = page,
)""",
        preview = {
            var page by remember { mutableIntStateOf(0) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) { i ->
                    StylishChip(
                        label = "${i + 1}",
                        onClick = { page = i },
                        selected = page == i,
                    )
                }
            }
            StylishDotIndicator(pageCount = 5, currentPage = page)
        },
    ),
)

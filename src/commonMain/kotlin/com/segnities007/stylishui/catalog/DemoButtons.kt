package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishChipVariant
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.atoms.StylishFabSize
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.components.atoms.StylishRoundedIconButton
import com.segnities007.stylishui.components.atoms.StylishSwitch

@Composable
internal fun DemoButtons(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Button variants",
        description = "バリアントと有効状態を切り替えられます。",
        code = """StylishButton(
    onClick = {},
    variant = StylishButtonVariant.Tonal,
    enabled = enabled,
) { Text("保存する") }""",
        modifier = modifier,
    ) {
        var variant by remember { mutableStateOf(StylishButtonVariant.Filled) }
        var enabled by remember { mutableStateOf(true) }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            StylishButtonVariant.entries.forEach { v ->
                StylishChip(
                    label = v.name,
                    onClick = { variant = v },
                    selected = variant == v,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StylishButton(onClick = {}, variant = variant, enabled = enabled) { Text("保存する") }
            StylishSwitch(checked = enabled, onCheckedChange = { enabled = it })
        }
    }

    StylishDemoCard(
        title = "Loading button",
        description = "非同期処理中のローディング表示。",
        code = """StylishButton(
    onClick = {},
    isLoading = loading,
) { Text(if (loading) "処理中…" else "実行") }""",
        modifier = modifier,
    ) {
        var loading by remember { mutableStateOf(false) }
        StylishButton(onClick = { loading = !loading }, isLoading = loading) {
            Text(if (loading) "処理中…" else "実行")
        }
    }

    StylishDemoCard(
        title = "Icon buttons",
        description = "アイコンボタンの各形状。",
        code = """StylishIconButton(Icons.Default.Add, "追加", {})
StylishRoundedIconButton(Icons.Default.Add, "追加", {})""",
        modifier = modifier,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StylishIconButton(Icons.Default.Add, "追加", {})
            StylishRoundedIconButton(Icons.Default.Favorite, "お気に入り", {})
        }
    }

    StylishDemoCard(
        title = "Floating action button",
        description = "サイズバリアントを切り替えられます。",
        code = """StylishFab(
    imageVector = Icons.Default.Add,
    contentDescription = "追加",
    sizeVariant = StylishFabSize.Large,
) {}""",
        modifier = modifier,
    ) {
        var size by remember { mutableStateOf(StylishFabSize.Regular) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            StylishFabSize.entries.forEach { s ->
                StylishChip(
                    label = s.name,
                    onClick = { size = s },
                    selected = size == s,
                )
            }
            StylishFab(
                imageVector = Icons.Default.Add,
                contentDescription = "追加",
                sizeVariant = size,
                onClick = {},
            )
        }
    }

    StylishDemoCard(
        title = "Chips",
        description = "選択・フィルタ・入力チップ。",
        code = """StylishChip(label = "選択中", onClick = {}, selected = selected)
StylishChip(label = "フィルタ", onClick = {}, variant = StylishChipVariant.Filter, selected = selected)""",
        modifier = modifier,
    ) {
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
    }
}

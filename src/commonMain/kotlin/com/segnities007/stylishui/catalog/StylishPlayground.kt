package com.segnities007.stylishui.catalog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishChipVariant

/**
 * Playground categories shown as filter chips on the website.
 */
internal enum class DemoCategory(val label: String) {
    Buttons("Buttons"),
    Selection("Selection"),
    Inputs("Inputs"),
    Navigation("Navigation"),
    Feedback("Feedback"),
    Connected("Connected"),
    Charts("Charts"),
    WebParity("Web Parity"),
    Patterns("Patterns"),
}

/**
 * The interactive component playground of the Stylish website — the
 * shadcn/Tailwind "UI blocks" style gallery.
 *
 * Renders a clean header with a dark-mode toggle, a category filter
 * chip row, and a single centered column of interactive
 * [StylishDemoCard]s. Each demo lets the visitor change component state
 * live and view/copy its code.
 *
 * @param darkTheme Current theme flag.
 * @param onToggleTheme Switches between light and dark theme.
 */
@Composable
public fun StylishPlayground(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    var category by remember { mutableStateOf(DemoCategory.Buttons) }

    Column(Modifier.fillMaxSize()) {
        // Clean app-bar style header with hairline border.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    "Stylish UI",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Compose Multiplatform design system",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (darkTheme) "ライトモードへ" else "ダークモードへ",
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        CategoryFilter(selected = category, onSelect = { category = it })
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            // Centered content column — single column layout never breaks
            // across viewport widths.
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    Modifier.width(720.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    CategoryIntro(category)
                    CategoryDemos(category, Modifier.fillMaxWidth())
                    PlaygroundFooter()
                }
            }
        }
    }
}

@Composable
private fun CategoryFilter(
    selected: DemoCategory,
    onSelect: (DemoCategory) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DemoCategory.entries.forEach { category ->
            StylishChip(
                label = category.label,
                onClick = { onSelect(category) },
                selected = category == selected,
                variant = StylishChipVariant.Filter,
            )
        }
    }
}

@Composable
private fun CategoryIntro(category: DemoCategory) {
    val description = when (category) {
        DemoCategory.Buttons -> "ボタン・アイコンボタン・FAB・チップ — バリアントと状態を切り替えて確認できます。"
        DemoCategory.Selection -> "スイッチ・チェックボックス・ラジオ・スライダー・セグメント・タブ。"
        DemoCategory.Inputs -> "テキスト入力・数値入力・PIN・オートコンプリート・日付選択。"
        DemoCategory.Navigation -> "ナビゲーションバー・レール・ドロワー・検索バー。"
        DemoCategory.Feedback -> "アラート・トースト・ダイアログ・ポップオーバー・結果ページ・ローディング。"
        DemoCategory.Connected -> "連結カード・チップ・リスト・ボタンのグループ表現。"
        DemoCategory.Charts -> "円・棒・折れ線グラフ。"
        DemoCategory.WebParity -> "Web UI ライブラリと同等のコンポーネント群。"
        DemoCategory.Patterns -> "ページレベルのレイアウト（ヘッダー・フッター・スキャフォールド）。"
    }
    Text(
        description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun CategoryDemos(category: DemoCategory, modifier: Modifier) {
    when (category) {
        DemoCategory.Buttons -> DemoButtons(modifier)
        DemoCategory.Selection -> DemoSelection(modifier)
        DemoCategory.Inputs -> DemoInputs(modifier)
        DemoCategory.Navigation -> DemoNavigation(modifier)
        DemoCategory.Feedback -> DemoFeedback(modifier)
        DemoCategory.Connected -> DemoConnected(modifier)
        DemoCategory.Charts -> DemoCharts(modifier)
        DemoCategory.WebParity -> DemoWebParity(modifier)
        DemoCategory.Patterns -> DemoPatterns(modifier)
    }
}

@Composable
private fun PlaygroundFooter() {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                "Stylish UI — Clear, Simple, Modern.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}

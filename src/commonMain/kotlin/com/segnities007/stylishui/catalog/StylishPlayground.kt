package com.segnities007.stylishui.catalog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.segnities007.stylishui.components.patterns.StylishFooter
import com.segnities007.stylishui.components.patterns.StylishHeader
import com.segnities007.stylishui.theme.StylishTheme

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
 * Renders a header with a dark-mode toggle, a category filter chip row,
 * and a responsive two-column grid of interactive [StylishDemoCard]s
 * (one column on narrow viewports). Each demo lets the visitor change
 * component state live and view/copy its code.
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

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val twoColumns = maxWidth >= 760.dp
        val cardWidth = if (twoColumns) {
            (maxWidth - 12.dp) / 2
        } else {
            maxWidth
        }

        Column(Modifier.fillMaxSize()) {
            StylishHeader(
                title = {
                    Column {
                        Text("Stylish UI", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "Compose Multiplatform design system",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (darkTheme) "ライトモードへ" else "ダークモードへ",
                        )
                    }
                },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            CategoryFilter(
                selected = category,
                onSelect = { category = it },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CategoryIntro(category)
                DemoColumn(
                    category = category,
                    cardWidth = cardWidth,
                    twoColumns = twoColumns,
                )
                Spacer(Modifier.size(8.dp))
                PlaygroundFooter()
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
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DemoCategory.entries.forEach { category ->
            StylishChip(
                label = category.label,
                onClick = { onSelect(category) },
                selected = category == selected,
                variant = com.segnities007.stylishui.components.atoms.StylishChipVariant.Filter,
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
private fun DemoColumn(
    category: DemoCategory,
    cardWidth: androidx.compose.ui.unit.Dp,
    twoColumns: Boolean,
) {
    if (twoColumns) {
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CategoryDemos(category, Modifier.width(cardWidth))
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CategoryDemos(category, Modifier.fillMaxWidth())
        }
    }
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
    StylishFooter(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "Stylish UI — Clear, Simple, Modern.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

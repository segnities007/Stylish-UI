package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * One selectable entry of a [StylishCommandPalette].
 *
 * @property label The display text of the entry.
 * @property onSelect Called when the entry is chosen (click or Enter).
 * @property icon Optional leading icon.
 * @property keywords Extra search terms matched in addition to [label].
 * @property enabled When `false`, the entry is not selectable.
 */
public data class StylishCommandItem(
    public val label: String,
    public val onSelect: () -> Unit,
    public val icon: (@Composable () -> Unit)? = null,
    public val keywords: List<String> = emptyList(),
    public val enabled: Boolean = true,
)

/**
 * A searchable command palette — the web "Command/⌘K" pattern from
 * shadcn/ui (cmdk) and Radix UI.
 *
 * Shows a centered modal with a query input and the filtered command
 * list. Items are filtered against [StylishCommandItem.label] and
 * [StylishCommandItem.keywords]. The list supports keyboard
 * navigation: Arrow Up/Down moves the selection, Enter selects, Escape
 * dismisses.
 *
 * @param expanded Whether the palette is visible.
 * @param onDismiss Called when the palette should close (Escape, click
 *   outside, or item selection).
 * @param query The current query text.
 * @param onQueryChange Called as the user types.
 * @param modifier Modifier applied to the palette surface.
 * @param items The commands to search.
 * @param placeholder Placeholder text of the query input. Defaults to
 *   "コマンドを入力…".
 * @param maxResults Maximum number of filtered results shown. Defaults
 *   to 8.
 * @param shape Corner shape of the palette. Defaults to
 *   [RoundedCornerShape] with
 *   [DefaultStylishDimensions.connectedCornerRadius].
 * @param containerColor Background of the palette. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param contentColor Foreground color of the palette.
 * @param width Width of the palette. Defaults to 480 dp.
 */
@Composable
public fun StylishCommandPalette(
    expanded: Boolean,
    onDismiss: () -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    items: List<StylishCommandItem>,
    placeholder: String = "コマンドを入力…",
    maxResults: Int = 8,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    width: Dp = 480.dp,
) {
    if (!expanded) return

    val filtered = remember(query, items) {
        if (query.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.label.contains(query, ignoreCase = true) ||
                    item.keywords.any { it.contains(query, ignoreCase = true) }
            }
        }.take(maxResults)
    }
    var selectedIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(query, filtered.size) { selectedIndex = 0 }
    val focusRequester = remember { FocusRequester() }
    val onSurface = MaterialTheme.colorScheme.onSurface

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier
                    .width(width)
                    .background(containerColor, shape)
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown) {
                            when (event.key) {
                                Key.DirectionDown -> {
                                    selectedIndex = (selectedIndex + 1).coerceAtMost(filtered.lastIndex)
                                    true
                                }

                                Key.DirectionUp -> {
                                    selectedIndex = (selectedIndex - 1).coerceAtLeast(0)
                                    true
                                }

                                Key.Enter -> {
                                    filtered.getOrNull(selectedIndex)
                                        ?.takeIf { it.enabled }
                                        ?.let { it.onSelect() }
                                    onDismiss()
                                    true
                                }

                                Key.Escape -> {
                                    onDismiss()
                                    true
                                }

                                else -> false
                            }
                        } else {
                            false
                        }
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.width(12.dp))
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurface),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { },
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                if (filtered.isEmpty()) {
                    Text(
                        "結果がありません",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                    )
                } else {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        filtered.forEachIndexed { index, item ->
                            val selected = index == selectedIndex
                            Surface(
                                onClick = {
                                    if (item.enabled) {
                                        item.onSelect()
                                        onDismiss()
                                    }
                                },
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                } else {
                                    Color.Transparent
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    item.icon?.invoke()
                                    if (item.icon != null) {
                                        Spacer(Modifier.width(12.dp))
                                    }
                                    Text(
                                        item.label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (item.enabled) onSurface else {
                                            onSurface.copy(alpha = 0.38f)
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Preview(name = "Stylish command palette", showBackground = true, widthDp = 393)
@Composable
private fun StylishCommandPalettePreview() {
    StylishTheme(darkTheme = false) {
        var query by remember { mutableStateOf("") }
        StylishCommandPalette(
            expanded = true,
            onDismiss = {},
            query = query,
            onQueryChange = { query = it },
            items = listOf(
                StylishCommandItem("ダッシュボードを開く", {}),
                StylishCommandItem("給油記録を追加", {}),
                StylishCommandItem("設定を開く", {}),
            ),
        )
    }
}

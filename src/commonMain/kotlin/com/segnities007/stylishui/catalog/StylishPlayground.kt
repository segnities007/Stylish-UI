package com.segnities007.stylishui.catalog

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled

/**
 * Sort options for the component gallery.
 */
private enum class SortOption(val label: String) {
    NameAsc("A-Z"),
    NameDesc("Z-A"),
    Category("Category"),
}

/**
 * The interactive component gallery for the Stylish UI website.
 *
 * Renders a premium component gallery inspired by uiverse.io:
 * - Dark gallery aesthetic by default
 * - Responsive grid layout (1-6 columns based on viewport)
 * - Preview-first cards with hover overlays
 * - Three-tier navigation: global header, category tabs, filter bar
 * - Search and sort functionality
 *
 * Each demo lets the visitor interact with components live and view/copy code.
 *
 * @param darkTheme Current theme flag.
 * @param onToggleTheme Switches between light and dark theme.
 */
@Composable
public fun StylishPlayground(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    var selectedCategory by remember { mutableStateOf<DemoCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(SortOption.NameAsc) }

    // Get all demos and apply filters/sorting
    val filteredDemos = remember(selectedCategory, searchQuery, sortOption) {
        val demos = DemoRegistry.getDemosByCategory(selectedCategory)
        val filtered = if (searchQuery.isBlank()) {
            demos
        } else {
            demos.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
        when (sortOption) {
            SortOption.NameAsc -> filtered.sortedBy { it.name }
            SortOption.NameDesc -> filtered.sortedByDescending { it.name }
            SortOption.Category -> filtered.sortedBy { it.category.label }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Tier 1: Global header
        GlobalHeader(darkTheme, onToggleTheme)
        
        // Tier 2: Category tabs
        CategoryTabs(
            selectedCategory = selectedCategory,
            onSelectCategory = { selectedCategory = it },
            categoryCounts = DemoRegistry.getCategoryCounts(),
        )
        
        // Tier 3: Filter bar
        FilterBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            sortOption = sortOption,
            onSortOptionChange = { sortOption = it },
            resultCount = filteredDemos.size,
        )

        // Component grid
        ComponentGrid(demos = filteredDemos)
    }
}

/**
 * Global header with logo and theme toggle.
 *
 * Sticky top bar with minimal chrome: logo on left, theme toggle on right.
 */
@Composable
private fun GlobalHeader(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    "Stylish UI",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
    }
}

/**
 * Horizontal scrollable category tabs with component counts.
 *
 * Active tab has filled background, inactive tabs are text-only.
 * Includes an "All" option to show all categories.
 */
@Composable
private fun CategoryTabs(
    selectedCategory: DemoCategory?,
    onSelectCategory: (DemoCategory?) -> Unit,
    categoryCounts: Map<DemoCategory, Int>,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // "All" tab
            item {
                CategoryTab(
                    label = "All",
                    count = DemoRegistry.allDemos.size,
                    selected = selectedCategory == null,
                    onClick = { onSelectCategory(null) },
                )
            }
            
            // Category tabs
            items(DemoCategory.entries.toList()) { category ->
                CategoryTab(
                    label = category.label,
                    count = categoryCounts[category] ?: 0,
                    selected = selectedCategory == category,
                    onClick = { onSelectCategory(category) },
                )
            }
        }
    }
}

/**
 * Individual category tab with label and count badge.
 */
@Composable
private fun CategoryTab(
    label: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val reducedMotion = isStylishReducedMotionEnabled()
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        },
        animationSpec = tween(
            durationMillis = if (reducedMotion) 0 else 200,
        ),
        label = "tabBackground",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(
            durationMillis = if (reducedMotion) 0 else 200,
        ),
        label = "tabText",
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
            )
            Text(
                count.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.7f),
            )
        }
    }
}

/**
 * Filter bar with search input, sort dropdown, and result count.
 *
 * Compact single-row layout for filtering and sorting the component grid.
 */
@Composable
private fun FilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    sortOption: SortOption,
    onSortOptionChange: (SortOption) -> Unit,
    resultCount: Int,
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Search input
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "コンポーネントを検索...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )
                }
            }

            // Sort dropdown
            Box {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showSortMenu = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            sortOption.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                ) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                onSortOptionChange(option)
                                showSortMenu = false
                            },
                        )
                    }
                }
            }

            // Result count
            Text(
                "$resultCount 個のコンポーネント",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * Responsive grid of component demo cards.
 *
 * Uses LazyVerticalGrid with adaptive cells for responsive layout:
 * - Mobile: 1 column
 * - Tablet: 2-3 columns
 * - Desktop: 4-6 columns
 */
@Composable
private fun ComponentGrid(
    demos: List<DemoComponent>,
) {
    if (demos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "コンポーネントが見つかりません",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "検索条件やフィルターを変更してください",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(demos, key = { it.name }) { demo ->
                StylishDemoCard(
                    name = demo.name,
                    code = demo.code,
                    preview = demo.preview,
                )
            }
        }
    }
}

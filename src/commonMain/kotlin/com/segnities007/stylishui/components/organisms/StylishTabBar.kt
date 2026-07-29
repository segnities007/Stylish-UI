package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontally scrollable row of tab-like chips for switching
 * between views within a page. Each tab is rendered as a
 * [StylishChip] with animated selection, making this a lightweight
 * alternative to [StylishConnectedSegmentedControl] when the number
 * of tabs is dynamic or exceeds the screen width.
 *
 * Unlike [StylishConnectedSegmentedControl], tabs are not visually
 * connected — each chip has its own rounded outline and spacing.
 *
 * @param tabs The tab labels to display.
 * @param selectedIndex The zero-based index of the currently selected
 *   tab.
 * @param onSelectedChange Called with the new index when a tab is
 *   tapped.
 * @param modifier Modifier applied to the outer [Row].
 * @param tabIcons Optional icons for each tab, indexed to match
 *   [tabs]. When `null` or shorter than [tabs], tabs without an icon
 *   show label only.
 *
 * @see StylishChip
 * @see StylishConnectedSegmentedControl
 */
@Composable
public fun StylishTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tabIcons: List<ImageVector>? = null,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEachIndexed { index, label ->
            StylishChip(
                label = label,
                onClick = { onSelectedChange(index) },
                selected = index == selectedIndex,
                leadingContent = tabIcons?.getOrNull(index)?.let { icon ->
                    {
                        androidx.compose.material3.Icon(
                            icon,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 0.dp),
                        )
                    }
                },
            )
        }
    }
}

@Preview(name = "Stylish tab bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishTabBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var selected by remember { mutableIntStateOf(0) }
            StylishTabBar(
                tabs = listOf("概要", "給油記録", "メンテナンス", "統計"),
                selectedIndex = selected,
                onSelectedChange = { selected = it },
            )
        }
    }
}
package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors
import kotlin.math.roundToInt

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
 * @param leadingContents Optional custom leading content per tab,
 *   indexed to match [tabs]. When non-null, a non-null entry at an
 *   index takes precedence over [tabIcons] and is rendered in place
 *   of the icon; a `null` entry falls back to [tabIcons], then to
 *   label only.
 * @param labelStyle [TextStyle] for each tab label, forwarded to
 *   [StylishChip]. Defaults to
 *   [MaterialTheme.typography.labelLarge].
 * @param containerColor Background color of the tab bar surface.
 *   Defaults to [MaterialTheme.colorScheme.surfaceContainer].
 * @param selectedContainerColor Container color of the selected tab.
 *   When `null`, [StylishChip]'s default is used.
 * @param selectedContentColor Content color of the selected tab.
 *   When `null`, [StylishChip]'s default is used.
 * @param unselectedContainerColor Container color of unselected tabs.
 *   When `null`, [StylishChip]'s default is used.
 * @param unselectedContentColor Content color of unselected tabs.
 *   When `null`, [StylishChip]'s default is used.
 * @param enabled When `false`, every tab is disabled: chips ignore
 *   pointer input and render in their disabled colors. Defaults to
 *   `true`.
 * @param scrollState The [ScrollState] of the tab row. When
 *   [selectedIndex] changes, the row animates to scroll the selected
 *   tab into view.
 *
 * Selection feedback relies on the [StylishChip] color animation —
 * there is deliberately no sliding indicator, since each chip already
 * animates its container/content colors (see
 * [com.segnities007.stylishui.components.atoms.StylishChip]).
 * Tab icons are decorative and carry no content description.
 *
 * ## Accessibility
 *
 * Selection, enabling, and disabled semantics are provided by the
 * underlying [StylishChip]. The auto-scroll that brings the selected
 * tab into view snaps instead of animating when the system
 * reduced-motion setting is active (see
 * [isStylishReducedMotionEnabled]).
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
    leadingContents: List<(@Composable () -> Unit)?>? = null,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    selectedContainerColor: Color? = null,
    selectedContentColor: Color? = null,
    unselectedContainerColor: Color? = null,
    unselectedContentColor: Color? = null,
    enabled: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
) {
    val tabPositions = remember { mutableStateMapOf<Int, Float>() }
    val reducedMotion = isStylishReducedMotionEnabled()
    LaunchedEffect(selectedIndex) {
        tabPositions[selectedIndex]?.let { x ->
            val target = x.roundToInt()
            if (reducedMotion) {
                scrollState.scrollTo(target)
            } else {
                scrollState.animateScrollTo(target)
            }
        }
    }
    Surface(
        modifier = modifier,
        color = containerColor,
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEachIndexed { index, label ->
                val leading: (@Composable () -> Unit)? = leadingContents?.getOrNull(index)
                    ?: tabIcons?.getOrNull(index)?.let { icon ->
                        {
                            Icon(
                                icon,
                                contentDescription = null,
                            )
                        }
                    }
                Box(
                    Modifier.onGloballyPositioned { coordinates ->
                        tabPositions[index] = coordinates.positionInParent().x
                    },
                ) {
                    StylishChip(
                        label = label,
                        onClick = { onSelectedChange(index) },
                        selected = index == selectedIndex,
                        enabled = enabled,
                        labelStyle = labelStyle,
                        selectedContainerColor = selectedContainerColor ?: MaterialTheme.colorScheme.primary,
                        selectedContentColor = selectedContentColor ?: MaterialTheme.colorScheme.onPrimary,
                        unselectedContainerColor = unselectedContainerColor
                            ?: MaterialTheme.stylishComponentColors.groupedContainer,
                        unselectedContentColor = unselectedContentColor
                            ?: MaterialTheme.colorScheme.onSurfaceVariant,
                        leadingContent = leading?.let { content -> { content() } },
                    )
                }
            }
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
                tabIcons = listOf(
                    Icons.Default.Home,
                    Icons.Default.List,
                    Icons.Default.Settings,
                    Icons.Default.Star,
                ),
            )
        }
    }
}
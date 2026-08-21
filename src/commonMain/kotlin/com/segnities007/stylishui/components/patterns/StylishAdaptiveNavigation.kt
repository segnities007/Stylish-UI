package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.components.organisms.StylishNavigationRail
import com.segnities007.stylishui.components.organisms.StylishNavigationRailItem
import com.segnities007.stylishui.foundation.StylishWindowBreakpoints
import com.segnities007.stylishui.foundation.StylishWindowWidthSizeClass
import com.segnities007.stylishui.foundation.calculateStylishWindowSizeClass
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * Responsive navigation orchestrator that chooses a bottom bar for compact
 * windows and a navigation rail for medium/expanded windows.
 *
 * This keeps the destination model identical across phone, tablet, desktop,
 * and web layouts while allowing callers to customize breakpoints and content.
 */
@Composable
public fun StylishAdaptiveNavigation(
    items: List<StylishNavigationItem>,
    modifier: Modifier = Modifier,
    breakpoints: StylishWindowBreakpoints = StylishWindowBreakpoints(),
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier.stylishTestTag("adaptive_navigation").fillMaxSize()) {
        val sizeClass = calculateStylishWindowSizeClass(maxWidth, maxHeight, breakpoints)
        when (sizeClass.widthSizeClass) {
            StylishWindowWidthSizeClass.Compact -> {
                Column {
                    androidx.compose.foundation.layout.Box(Modifier.weight(1f).fillMaxSize()) { content() }
                    StylishNavigationBar(items = items)
                }
            }
            StylishWindowWidthSizeClass.Medium,
            StylishWindowWidthSizeClass.Expanded -> {
                Row {
                    StylishNavigationRail(Modifier.widthIn(min = 80.dp)) {
                        items.forEach { item ->
                            StylishNavigationRailItem(
                                selected = item.selected,
                                onClick = item.onClick,
                                enabled = item.enabled,
                                icon = { item.iconContent?.invoke() ?: androidx.compose.material3.Icon(item.icon, item.label) },
                                label = { androidx.compose.material3.Text(item.label) },
                                alwaysShowLabel = true,
                            )
                        }
                    }
                    androidx.compose.foundation.layout.Box(Modifier.weight(1f).fillMaxSize()) { content() }
                }
            }
        }
    }
}

@Preview(name = "Stylish adaptive navigation", showBackground = true, widthDp = 393, heightDp = 640)
@Composable
private fun StylishAdaptiveNavigationPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishAdaptiveNavigation(
                items = listOf(
                    StylishNavigationItem(Icons.Default.Home, "ホーム", selected = true),
                    StylishNavigationItem(Icons.Default.Settings, "設定"),
                ),
            ) {
                Text("コンテンツ")
            }
        }
    }
}

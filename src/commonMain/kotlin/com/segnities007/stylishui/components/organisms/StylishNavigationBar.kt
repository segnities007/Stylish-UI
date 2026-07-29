package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A bottom navigation bar that displays a row of
 * [StylishNavigationItem] destinations. Each destination shows an
 * icon, a label, and an optional badge. The selected destination is
 * highlighted with the primary color.
 *
 * Designed to be placed inside the `bottomBar` slot of
 * [com.segnities007.stylishui.components.patterns.StylishScaffold] or
 * within a
 * [com.segnities007.stylishui.components.patterns.StylishFooter].
 *
 * @param items The navigation destinations to display.
 * @param modifier Modifier applied to the root [Row].
 * @param labelMaxLines Maximum lines for each destination label.
 * @param labelOverflow Overflow strategy for labels.
 *
 * @see StylishNavigationItem
 * @see com.segnities007.stylishui.components.patterns.StylishFooter
 */
@Composable
public fun StylishNavigationBar(
    items: List<StylishNavigationItem>,
    modifier: Modifier = Modifier,
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            val actionable = isActionable(
                enabled = item.enabled,
                hasClickAction = true,
            )
            val contentColor = when {
                !item.enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                item.selected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        this.selected = item.selected
                        role = Role.Tab
                    }
                    .then(
                        if (actionable) {
                            Modifier.clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                item.onClick()
                            }
                        } else {
                            Modifier
                        },
                    )
                    .padding(vertical = StylishTheme.dimensions.itemSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
            ) {
                if (item.badge != null) {
                    item.badge()
                } else {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = contentColor,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text(
                    item.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                    maxLines = labelMaxLines,
                    overflow = labelOverflow,
                )
            }
        }
    }
}

@Preview(name = "Stylish navigation bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishNavigationBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishNavigationBar(
                items = listOf(
                    StylishNavigationItem(Icons.Default.Home, "ホーム", selected = true),
                    StylishNavigationItem(Icons.Default.Search, "検索"),
                    StylishNavigationItem(Icons.Default.Settings, "設定"),
                ),
            )
        }
    }
}
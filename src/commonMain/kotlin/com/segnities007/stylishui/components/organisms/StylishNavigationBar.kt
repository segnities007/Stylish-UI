package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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
 * @param labelStyle [TextStyle] applied to each destination label.
 *   Defaults to [MaterialTheme.typography.labelSmall].
 * @param iconSize Size of each destination icon. Defaults to 24 dp.
 * @param selectedContentColor Content color for the selected
 *   destination. Defaults to [MaterialTheme.colorScheme.primary].
 * @param unselectedContentColor Content color for unselected
 *   destinations. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param disabledContentColor Content color for disabled
 *   destinations. Defaults to
 *   [MaterialTheme.colorScheme.onSurface] at 38 % alpha.
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
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall,
    iconSize: Dp = 24.dp,
    selectedContentColor: Color = MaterialTheme.colorScheme.primary,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
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
                !item.enabled -> disabledContentColor
                item.selected -> selectedContentColor
                else -> unselectedContentColor
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        this.selected = item.selected
                        role = Role.Tab
                        if (!item.enabled) disabled()
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
                Box {
                    val iconContent = item.iconContent
                    if (iconContent != null) {
                        iconContent()
                    } else {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint = contentColor,
                            modifier = Modifier.size(iconSize),
                        )
                    }
                    item.badge?.let { badge ->
                        Box(Modifier.align(Alignment.TopEnd)) { badge() }
                    }
                }
                Text(
                    item.label,
                    style = labelStyle,
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
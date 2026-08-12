package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A bottom navigation bar that displays a row of
 * [StylishNavigationItem] destinations. Each destination shows an
 * icon, a label, and an optional badge. The selected destination is
 * highlighted with an animated pill behind its icon and the primary
 * content color.
 *
 * The bar renders inside a [Surface] with [containerColor] so it gets
 * a background and a minimum height of 80.dp, and consumes
 * [windowInsets] (defaulting to the navigation-bar insets) so it
 * clears the system gesture area. When [alwaysShowLabel] is `false`,
 * the label of unselected destinations is hidden and only the
 * selected destination keeps its label visible.
 *
 * Designed to be placed inside the `bottomBar` slot of
 * [com.segnities007.stylishui.components.patterns.StylishScaffold] or
 * within a
 * [com.segnities007.stylishui.components.patterns.StylishFooter].
 *
 * @param items The navigation destinations to display.
 * @param modifier Modifier applied to the root [Surface].
 * @param labelStyle [TextStyle] applied to each destination label.
 *   Defaults to [MaterialTheme.typography.labelSmall].
 * @param iconSize Size of each destination icon. Defaults to 24 dp.
 * @param containerColor Background color of the bar. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainer].
 * @param selectedContentColor Content color for the selected
 *   destination. Defaults to [MaterialTheme.colorScheme.primary]. Also
 *   used as the base tint of the selection pill behind the icon.
 * @param unselectedContentColor Content color for unselected
 *   destinations. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param disabledContentColor Content color for disabled
 *   destinations. Defaults to
 *   [MaterialTheme.colorScheme.onSurface] at 38 % alpha.
 * @param alwaysShowLabel When `true` (the default), every destination
 *   shows its label. When `false`, only the selected destination shows
 *   its label; unselected destinations render icon-only.
 * @param labelMaxLines Maximum lines for each destination label.
 * @param labelOverflow Overflow strategy for labels.
 * @param windowInsets [WindowInsets] consumed by the bar via
 *   [Modifier.windowInsetsPadding]. Defaults to
 *   [WindowInsets.navigationBars] so content stays clear of the system
 *   navigation bar.
 *
 * ## Accessibility
 *
 * Each destination is exposed with `Role.Tab` semantics, marks
 * [StylishNavigationItem.selected] via the `selected` semantics and
 * [StylishNavigationItem.enabled] via the `disabled` semantics, so
 * screen readers announce the selection and disabled states. The
 * selection-pill cross-fade honors the system reduced-motion setting
 * (see
 * [isStylishReducedMotionEnabled]) by snapping instead of tweening.
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
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    selectedContentColor: Color = MaterialTheme.colorScheme.primary,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    alwaysShowLabel: Boolean = true,
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    windowInsets: WindowInsets = WindowInsets.navigationBars,
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets),
        color = containerColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp),
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
                val indicatorAlpha by animateFloatAsState(
                    targetValue = if (item.selected && item.enabled) 0.12f else 0f,
                    animationSpec = if (isStylishReducedMotionEnabled()) {
                        snap()
                    } else {
                        tween(durationMillis = StylishTheme.animation.durationShort)
                    },
                    label = "navIndicator",
                )
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
                    Box(Modifier.size(64.dp)) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp),
                            shape = CircleShape,
                            color = selectedContentColor.copy(alpha = indicatorAlpha),
                        ) {}
                        val iconContent = item.iconContent
                        if (iconContent != null) {
                            iconContent()
                        } else {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                tint = contentColor,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(iconSize),
                            )
                        }
                        item.badge?.let { badge ->
                            Box(Modifier.align(Alignment.TopEnd)) { badge() }
                        }
                    }
                    if (alwaysShowLabel || item.selected) {
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
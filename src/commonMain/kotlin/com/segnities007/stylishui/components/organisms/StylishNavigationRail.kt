package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A Material 3 navigation rail with Stylish theme defaults.
 *
 * A vertical navigation column for medium-to-large screens, wrapping
 * [NavigationRail] unchanged so it keeps the full M3 behavior
 * (window insets, header slot, and column [content]). Combine it with
 * [StylishNavigationRailItem] for styled destinations, and place it
 * alongside the page content (e.g. inside a
 * [com.segnities007.stylishui.components.patterns.StylishScaffold]
 * content slot) on wide layouts.
 *
 * @param modifier Modifier applied to the [NavigationRail] root.
 * @param containerColor Background color of the rail. Defaults to
 *   [NavigationRailDefaults.ContainerColor]
 *   (surfaceContainer), the Material 3 spec value.
 * @param contentColor Default content color propagated to children.
 *   Defaults to [contentColorFor] of [containerColor], matching M3.
 * @param header Optional composable above the items, e.g. a logo or a
 *   menu button. Rendered inside the rail's [ColumnScope].
 * @param windowInsets [WindowInsets] consumed by the rail. Defaults to
 *   [NavigationRailDefaults.windowInsets].
 * @param content The destinations, laid out in a [ColumnScope];
 *   typically a sequence of [StylishNavigationRailItem].
 *
 * @see StylishNavigationRailItem
 */
@Composable
public fun StylishNavigationRail(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationRailDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    header: @Composable (ColumnScope.() -> Unit)? = null,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier.stylishTestTag("navigation_rail"),
        containerColor = containerColor,
        contentColor = contentColor,
        header = header,
        windowInsets = windowInsets,
        content = content,
    )
}

/**
 * A single destination inside [StylishNavigationRail], wrapping the
 * Material 3 [NavigationRailItem] with M3 default colors.
 *
 * Handles the selected-state indicator, disabled state, and
 * semantics (`selected` / `disabled`) exactly like the M3
 * [NavigationRailItem].
 *
 * @param selected Whether this destination is currently selected.
 * @param onClick Called when the destination is tapped.
 * @param icon The icon slot, typically an [Icon].
 * @param modifier Modifier applied to the [NavigationRailItem] root.
 * @param enabled When `false`, the destination ignores pointer input
 *   and renders in its disabled colors. Defaults to `true`.
 * @param label Optional label slot rendered below [icon]. When
 *   `null`, the destination renders icon-only.
 * @param alwaysShowLabel Whether to keep the [label] visible when the
 *   destination is not [selected]. Ignored when [label] is `null`.
 *   Defaults to `true`.
 * @param colors [NavigationRailItemColors] resolving the item colors
 *   per state. Defaults to [NavigationRailItemDefaults.colors].
 * @param interactionSource Optional hoisted
 *   [MutableInteractionSource] for observing interactions. When
 *   `null`, one is remembered internally.
 *
 * @see StylishNavigationRail
 */
@Composable
public fun StylishNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    colors: NavigationRailItemColors = NavigationRailItemDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier.stylishTestTag("navigation_rail_item"),
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish navigation rail", showBackground = true, widthDp = 120)
@Composable
private fun StylishNavigationRailPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishNavigationRail(
                modifier = Modifier.padding(
                    vertical = StylishTheme.dimensions.itemSpacing,
                ),
            ) {
                StylishNavigationRailItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                    label = { Text("ホーム") },
                )
                StylishNavigationRailItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Star, contentDescription = "お気に入り") },
                    label = { Text("お気に入り") },
                )
                StylishNavigationRailItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Settings, contentDescription = "設定") },
                    label = { Text("設定") },
                )
            }
        }
    }
}

@Preview(name = "Stylish navigation rail item", showBackground = true, widthDp = 120)
@Composable
private fun StylishNavigationRailItemPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishNavigationRailItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                label = { Text("ホーム") },
            )
        }
    }
}

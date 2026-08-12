package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationItemColors
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarArrangement
import androidx.compose.material3.ShortNavigationBarDefaults
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A Material 3 short navigation bar with Stylish theme defaults.
 *
 * A compact bottom navigation bar for small screens, wrapping
 * [ShortNavigationBar] unchanged. Unlike
 * [StylishNavigationBar], short navigation bars keep only the selected
 * destination's icon visible and collapse the rest, which makes them
 * suitable for up to three destinations. Combine with
 * [StylishShortNavigationBarItem].
 *
 * @param modifier Modifier applied to the [ShortNavigationBar] root.
 * @param containerColor Background color of the bar. Defaults to
 *   [ShortNavigationBarDefaults.containerColor]
 *   (surfaceContainer), the Material 3 spec value.
 * @param contentColor Default content color propagated to children.
 *   Defaults to [contentColorFor] of [containerColor], matching M3.
 * @param windowInsets [WindowInsets] consumed by the bar. Defaults to
 *   [ShortNavigationBarDefaults.windowInsets].
 * @param arrangement How items are distributed. Defaults to
 *   [ShortNavigationBarArrangement.EqualWeight], matching M3.
 * @param content The destinations, typically a sequence of
 *   [StylishShortNavigationBarItem].
 *
 * @see StylishShortNavigationBarItem
 */
@Composable
public fun StylishShortNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = ShortNavigationBarDefaults.containerColor,
    contentColor: Color = ShortNavigationBarDefaults.contentColor,
    windowInsets: WindowInsets = ShortNavigationBarDefaults.windowInsets,
    arrangement: ShortNavigationBarArrangement = ShortNavigationBarDefaults.arrangement,
    content: @Composable () -> Unit,
) {
    ShortNavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        windowInsets = windowInsets,
        arrangement = arrangement,
        content = content,
    )
}

/**
 * A single destination inside [StylishShortNavigationBar], wrapping
 * the Material 3 [ShortNavigationBarItem] with M3 default colors.
 *
 * Handles the selected-state indicator, disabled state, and
 * semantics (`selected` / `disabled`) exactly like the M3
 * [ShortNavigationBarItem].
 *
 * @param selected Whether this destination is currently selected.
 * @param onClick Called when the destination is tapped.
 * @param icon The icon slot, typically an [Icon].
 * @param label Optional label slot. When `null`, the destination
 *   renders icon-only.
 * @param modifier Modifier applied to the
 *   [ShortNavigationBarItem] root.
 * @param enabled When `false`, the destination ignores pointer input
 *   and renders in its disabled colors. Defaults to `true`.
 * @param iconPosition Whether [icon] sits on top of or beside the
 *   [label]. Defaults to [NavigationItemIconPosition.Top].
 * @param colors [NavigationItemColors] resolving the item colors per
 *   state. Defaults to [ShortNavigationBarItemDefaults.colors].
 * @param interactionSource Optional hoisted
 *   [MutableInteractionSource] for observing interactions. When
 *   `null`, one is remembered internally.
 *
 * @see StylishShortNavigationBar
 */
@Composable
public fun StylishShortNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconPosition: NavigationItemIconPosition = NavigationItemIconPosition.Top,
    colors: NavigationItemColors = ShortNavigationBarItemDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    ShortNavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        label = label,
        modifier = modifier,
        enabled = enabled,
        iconPosition = iconPosition,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish short navigation bar", showBackground = true, widthDp = 393)
@Composable
private fun StylishShortNavigationBarPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishShortNavigationBar {
                StylishShortNavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                    label = { Text("ホーム") },
                )
                StylishShortNavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Search, contentDescription = "検索") },
                    label = { Text("検索") },
                )
                StylishShortNavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Settings, contentDescription = "設定") },
                    label = { Text("設定") },
                )
            }
        }
    }
}

@Preview(name = "Stylish short navigation bar item", showBackground = true, widthDp = 393)
@Composable
private fun StylishShortNavigationBarItemPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(StylishTheme.dimensions.contentSpacing)) {
            StylishShortNavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                label = { Text("ホーム") },
            )
        }
    }
}

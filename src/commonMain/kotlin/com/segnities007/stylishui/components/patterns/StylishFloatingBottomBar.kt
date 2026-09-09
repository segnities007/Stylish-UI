package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.components.atoms.StylishFloatingBottomBarVisibility
import com.segnities007.stylishui.components.atoms.StylishRoundedIconButtonWithSemantics
import com.segnities007.stylishui.components.models.StylishFloatingBottomBarItem
import com.segnities007.stylishui.foundation.VisibilityState
import com.segnities007.stylishui.foundation.isVisible
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.StylishElevationLayer
import com.segnities007.stylishui.theme.stylishFloatingContainerColor

/**
 * Floating bottom bar with optional embedded FAB and action slots.
 *
 * The bar uses the shared floating treatment: a translucent outlined surface,
 * rounded shape, tonal elevation, and shadow.
 * Visibility uses the original StylishMyVehicles 200 ms fade + half-distance
 * slide motion and exits toward the bottom edge.
 *
 * @param visibilityState Visibility behavior for the floating bar. Defaults
 *   to [VisibilityState.AlwaysVisible].
 * @param shape Shape of the floating surface. Defaults to the shared floating
 *   corner radius.
 * @param border Optional outline around the floating surface. Defaults to a
 *   hairline of [StylishTheme.dimensions.outlineWidth] using
 *   `MaterialTheme.colorScheme.outlineVariant`.
 * @param actionsSpacing Horizontal gap applied between action slots and an
 *   optional embedded FAB.
 * @param shadowElevation Drop-shadow elevation of the floating surface.
 * @param windowInsets Insets applied outside the floating surface. Defaults to
 *   no insets because callers normally position a floating bar above the
 *   navigation bar with their overlay layout.
 */
@Composable
public fun StylishFloatingBottomBar(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.shapes.floatingCornerRadius),
    containerColor: Color = stylishFloatingContainerColor(),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    actionsSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    windowInsets: WindowInsets = WindowInsets(0.dp),
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
) {
    StylishFloatingBottomBarVisibility(
        visible = visibilityState.isVisible(),
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .windowInsetsPadding(windowInsets)
                .stylishTestTag("floating_bottom_bar"),
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
            border = border,
        ) {
            StylishElevationLayer {
                Row(
                    modifier = Modifier
                        .padding(contentPadding)
                        // A bottom bar is a single set of mutually exclusive
                        // destinations. Keep the group relationship available to
                        // TalkBack and keyboard users while each item exposes its
                        // own Role.Tab and Selected state below.
                        .semantics { selectableGroup() },
                    horizontalArrangement = Arrangement.spacedBy(actionsSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    actions()
                    floatingActionButton?.invoke()
                }
            }
        }
    }
}

/**
 * Floating bottom bar using the standard Stylish rounded icon actions.
 *
 * This is the default item-based FBB used by Stylish applications. Each item
 * is rendered as an independent [StylishRoundedIconButton], so accessible
 * selection state, disabled state, focus treatment, and minimum touch targets
 * stay consistent across Android and Web hosts. The default item geometry and
 * pill-button appearance match the original StylishMyVehicles FBB. Use the slot-based
 * overload when a custom action layout is required.
 *
 * @param items Icon actions to render from left to right.
 * @param selectedKey Key of the currently selected item, or `null` when no
 *   item is selected.
 * @param onItemClick Called with the selected item's [StylishFloatingBottomBarItem.key].
 * @param modifier Modifier applied to the floating bar.
 * @param floatingActionButton Optional FAB rendered after the item actions.
 * @param visibilityState Visibility behavior for the floating bar.
 */
@Composable
public fun StylishFloatingBottomBar(
    items: List<StylishFloatingBottomBarItem>,
    selectedKey: String? = null,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.shapes.floatingCornerRadius),
    containerColor: Color = stylishFloatingContainerColor(),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    actionsSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    windowInsets: WindowInsets = WindowInsets(0.dp),
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
) {
    StylishFloatingBottomBar(
        actions = {
            items.forEach { item ->
                StylishRoundedIconButtonWithSemantics(
                    imageVector = item.imageVector,
                    contentDescription = item.contentDescription,
                    onClick = { onItemClick(item.key) },
                    enabled = item.enabled,
                    // Share narrow widths evenly so the last action is never
                    // the only one squeezed; minimum touch targets stay intact.
                    modifier = Modifier.weight(1f),
                    // Keep the MyVehicles pill-button appearance. Selection
                    // is exposed through semantics without changing its
                    // neutral button fill.
                    active = false,
                    role = Role.Tab,
                    selected = item.key == selectedKey,
                )
            }
        },
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        border = border,
        actionsSpacing = actionsSpacing,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
        visibilityState = visibilityState,
    )
}

@Preview(name = "Floating bottom bar", showBackground = true)
@Composable
private fun StylishFloatingBottomBarPreview() {
    StylishFloatingBottomBar(
        actions = {
            Text("Home")
            Text("History")
        },
    )
}

@Preview(name = "Floating bottom bar actions", showBackground = true, widthDp = 393)
@Composable
private fun StylishFloatingBottomBarActionsPreview() {
    StylishTheme(darkTheme = false) {
        StylishFloatingBottomBar(
            items = listOf(
                StylishFloatingBottomBarItem("home", Icons.Default.Home, "ホーム"),
                StylishFloatingBottomBarItem("history", Icons.Default.History, "履歴"),
                StylishFloatingBottomBarItem("settings", Icons.Default.Settings, "設定"),
            ),
            selectedKey = "home",
            onItemClick = {},
        )
    }
}

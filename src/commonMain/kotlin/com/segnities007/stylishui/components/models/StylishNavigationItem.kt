package com.segnities007.stylishui.components.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Immutable data describing a single destination in a
 * [com.segnities007.stylishui.components.organisms.StylishNavigationBar].
 *
 * @property icon The icon displayed for this destination.
 * @property label The text label displayed below (or beside) [icon].
 * @property selected Whether this destination is currently active.
 * @property onClick Called when the destination is tapped.
 * @property enabled When `false`, the destination ignores pointer input
 *   and renders in a dimmed state.
 * @property badge Optional badge content (e.g. a notification count)
 *   rendered near the icon. When `null`, no badge is shown.
 * @property iconContent Optional custom content rendered in place of
 *   [icon] when non-null. When `null` (the default), [icon] is
 *   rendered.
 */
@Immutable
public data class StylishNavigationItem(
    val icon: ImageVector,
    val label: String,
    val selected: Boolean = false,
    val onClick: () -> Unit = {},
    val enabled: Boolean = true,
    val badge: (@Composable () -> Unit)? = null,
    val iconContent: (@Composable () -> Unit)? = null,
)
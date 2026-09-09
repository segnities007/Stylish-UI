package com.segnities007.stylishui.components.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Immutable description of one icon action rendered in a
 * [com.segnities007.stylishui.components.patterns.StylishFloatingBottomBar].
 *
 * The bar keeps each item as an independent button so that pointer, keyboard,
 * and accessibility interactions target the action rather than the whole bar.
 *
 * @property key Stable identifier returned by the bar when the item is activated.
 * @property imageVector Icon rendered for the action.
 * @property contentDescription Accessibility label for the action.
 * @property enabled Whether the action accepts input.
 */
@Immutable
public data class StylishFloatingBottomBarItem(
    val key: String,
    val imageVector: ImageVector,
    val contentDescription: String,
    val enabled: Boolean = true,
)

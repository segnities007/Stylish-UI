package com.segnities007.stylishui.components.organisms

import androidx.compose.runtime.Immutable

/** A top-level menu entry for [StylishMenubar].
 *
 * @property label Text shown in the menubar.
 * @property items Actions shown when this menu is expanded.
 */
@Immutable
public data class StylishMenu(val label: String, val items: List<StylishMenuItem>)

/** An action item shared by context menus and menubars.
 *
 * @property label Text shown for the action.
 * @property onClick Callback invoked when the action is selected.
 * @property enabled Whether the action accepts input.
 */
@Immutable
public data class StylishMenuItem(
    public val label: String,
    public val onClick: () -> Unit,
    public val enabled: Boolean = true,
)

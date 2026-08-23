package com.segnities007.stylishui.components.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Immutable data for one icon button in a [StylishConnectedIconButtonRow].
 *
 * The row owns the connected geometry. Each item remains an independent
 * action, so accessibility services and pointer input see one button per
 * icon rather than one large click target.
 *
 * @property imageVector Icon rendered for this action.
 * @property contentDescription Accessibility label for the icon action.
 * @property onClick Action invoked when this item is tapped.
 * @property enabled Whether this item accepts input and renders as enabled.
 * @property active Whether this item uses the selected/primary color pair.
 */
@Immutable
public data class StylishConnectedIconButtonItem(
    val imageVector: ImageVector,
    val contentDescription: String? = null,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val active: Boolean = false,
)

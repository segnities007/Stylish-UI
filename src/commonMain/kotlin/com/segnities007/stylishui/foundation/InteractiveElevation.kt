package com.segnities007.stylishui.foundation

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Resolves the shared elevation ladder for an actionable surface.
 *
 * Press wins over hover/focus, followed by hover, focus, and the default interactive elevation.
 * Disabled or non-actionable surfaces are flat. Keeping this resolution in Foundation makes
 * desktop pointer feedback and keyboard focus feedback agree with touch behavior across cards,
 * list items, chips, and custom renderers.
 */
@Composable
public fun stylishInteractiveElevation(
    interactionSource: InteractionSource,
    actionable: Boolean,
): Dp {
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val focused by interactionSource.collectIsFocusedAsState()
    if (!actionable) return StylishTheme.dimensions.disabledElevation
    return when {
        pressed -> StylishTheme.dimensions.pressedElevation
        hovered -> StylishTheme.dimensions.hoveredElevation
        focused -> StylishTheme.dimensions.focusedElevation
        else -> StylishTheme.dimensions.interactiveElevation
    }
}

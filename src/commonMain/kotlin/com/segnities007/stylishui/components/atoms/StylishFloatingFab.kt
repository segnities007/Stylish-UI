package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.foundation.VisibilityState
import com.segnities007.stylishui.theme.StylishTheme

/** Floating action button using the shared floating color and opacity. */
@Composable
public fun StylishFloatingFab(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape? = null,
    sizeVariant: StylishFabSize = StylishFabSize.Regular,
    size: Dp? = null,
    border: BorderStroke? = null,
    tonalElevation: Dp = StylishTheme.dimensions.floatingElevation,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    interactionSource: MutableInteractionSource? = null,
    iconContent: (@Composable () -> Unit)? = null,
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
) {
    StylishFab(
        imageVector = imageVector,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        sizeVariant = sizeVariant,
        size = size,
        border = border,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        interactionSource = interactionSource,
        iconContent = iconContent,
        visibilityState = visibilityState,
    )
}

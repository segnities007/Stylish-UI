package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.VisibilityState
import com.segnities007.stylishui.theme.StylishTheme

/** Floating top bar with a centered title and edge action slots. */
@Composable
public fun StylishFloatingTopBar(
    title: @Composable () -> Unit,
    navigation: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    height: Dp = 56.dp,
    topPadding: Dp = StylishTheme.dimensions.itemSpacing,
    bottomPadding: Dp = StylishTheme.dimensions.contentSpacing,
    actionsSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    windowInsets: WindowInsets = WindowInsets(0.dp),
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
) {
    StylishHeader(
        title = title,
        navigation = navigation,
        actions = actions,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        border = border,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        height = height,
        topPadding = topPadding,
        bottomPadding = bottomPadding,
        actionsSpacing = actionsSpacing,
        windowInsets = windowInsets,
        visibilityState = visibilityState,
    )
}

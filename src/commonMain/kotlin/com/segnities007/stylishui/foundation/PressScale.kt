package com.segnities007.stylishui.foundation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Adds a restrained press-scale response to an interactive surface.
 *
 * The scale is driven by [InteractionSource], so it remains compatible with hoisted
 * interaction state and pointer/keyboard activation. Reduced-motion settings disable the
 * interpolation and keep the surface at its resting scale.
 *
 * @param pressedScale Optional override for the scale while pressed. When omitted, the
 *   current theme's [com.segnities007.stylishui.tokens.StylishAnimationTokens.pressedScale]
 *   is used.
 */
@Composable
public fun Modifier.stylishPressScale(
    interactionSource: InteractionSource,
    pressedScale: Float? = null,
): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val resolvedPressedScale = pressedScale ?: StylishTheme.animation.pressedScale
    val target = if (pressed) resolvedPressedScale.coerceIn(0.9f, 1f) else 1f
    val scale by animateFloatAsState(
        targetValue = target,
        animationSpec = if (isStylishReducedMotionEnabled()) snap() else spring(
            stiffness = StylishTheme.animation.springStiffness,
        ),
        label = "stylishPressScale",
    )
    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

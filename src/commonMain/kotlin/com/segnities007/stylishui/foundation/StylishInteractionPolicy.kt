package com.segnities007.stylishui.foundation

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.tokens.DefaultStylishAnimationTokens
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * The cross-component interaction contract used by Stylish UI.
 *
 * Keeping interaction behavior in one immutable value prevents each component family from
 * inventing a different minimum target, focus ring, or press response. Components may still
 * override the policy for a deliberate platform-specific treatment, but the defaults remain
 * identical across atoms, molecules, organisms, and patterns.
 */
@Immutable
public data class StylishInteractionPolicy(
    /** Minimum size of an actionable target. */
    public val minimumTarget: Dp = DefaultStylishDimensions.iconButtonMinSize,
    /** Width of the keyboard focus indicator. */
    public val focusRingWidth: Dp = DefaultStylishDimensions.focusRingWidth,
    /** Scale applied while an actionable surface is pressed. */
    public val pressedScale: Float = DefaultStylishAnimationTokens.pressedScale,
    /** Whether custom surfaces should draw a state-layer overlay. */
    public val stateLayerEnabled: Boolean = true,
    /** Whether custom surfaces should draw the shared focus ring. */
    public val focusRingEnabled: Boolean = true,
    /** Whether custom surfaces should use the shared press-scale response. */
    public val pressScaleEnabled: Boolean = true,
)

/** The default interaction contract for all Stylish components. */
public val DefaultStylishInteractionPolicy: StylishInteractionPolicy = StylishInteractionPolicy()

/**
 * Hoists an interaction source without forcing every component to repeat the nullable-source
 * boilerplate. Passing a source keeps interaction state observable by the caller and tests.
 */
@Composable
public fun rememberStylishInteractionSource(
    interactionSource: MutableInteractionSource? = null,
): MutableInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

/**
 * Applies the common interaction contract to a custom surface.
 *
 * Material 3 components already provide their own indication and focus treatment; this helper
 * is intended for custom `Surface`/layout implementations in the Structure and Finish layers.
 * The order is deliberate: target sizing first, then state layer, focus ring, and press scale.
 */
@Composable
public fun Modifier.stylishInteractiveSurface(
    interactionSource: InteractionSource,
    shape: Shape = RectangleShape,
    policy: StylishInteractionPolicy = DefaultStylishInteractionPolicy,
): Modifier {
    var result = this.stylishInteractiveTarget(minimumTarget = policy.minimumTarget)
    if (policy.stateLayerEnabled) {
        result = result.stylishStateLayer(interactionSource = interactionSource, shape = shape)
    }
    if (policy.focusRingEnabled) {
        result = result.stylishFocusRing(
            interactionSource = interactionSource,
            shape = shape,
            width = policy.focusRingWidth,
        )
    }
    if (policy.pressScaleEnabled) {
        result = result.stylishPressScale(
            interactionSource = interactionSource,
            pressedScale = policy.pressedScale,
        )
    }
    return result
}

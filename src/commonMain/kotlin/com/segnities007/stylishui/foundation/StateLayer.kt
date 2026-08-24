package com.segnities007.stylishui.foundation

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Shape

/**
 * Draws the Material 3 state-layer overlay on top of this component's content.
 *
 * Implements the M3 state-layer pattern for custom components: while the
 * [interactionSource] reports a hover, a translucent `onSurface` overlay of
 * 4 % alpha is drawn over the content; while it reports a press, the overlay
 * darkens to 8 % alpha. A pressed state always wins over a hovered state.
 * When neither state is active, nothing is drawn and the modifier has no
 * visual effect.
 *
 * The overlay is drawn with [drawWithContent], so it renders **on top of** the
 * component's content. The overlay follows [shape] (which should match the
 * container's shape), so rounded or circular containers get a matching
 * overlay without clipping the container or its shadow.
 *
 * The overlay color is derived from `MaterialTheme.colorScheme.onSurface`,
 * matching the Material 3 state-layer specification. This helper is intended
 * for custom [Surface]-based Stylish components (such as the Fab and chip
 * families); components that wrap Material 3 widgets already receive the M3
 * ripple and state layer internally and should not add this modifier again.
 *
 * @param interactionSource The source to observe for hover and press
 *   interactions. Typically the same source hoisted through the component's
 *   `interactionSource` parameter (or an internally remembered one).
 * @param shape The shape the overlay is drawn with. Defaults to
 *   [RectangleShape]; pass the container's shape (e.g. `CircleShape`) for
 *   non-rectangular surfaces.
 */
@Composable
public fun Modifier.stylishStateLayer(
    interactionSource: InteractionSource,
    shape: Shape = RectangleShape,
): Modifier {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val onSurface = MaterialTheme.colorScheme.onSurface
    return drawWithContent {
        drawContent()
        if (isPressed || isHovered) {
            val alpha = if (isPressed) PressedStateLayerAlpha else HoveredStateLayerAlpha
            val overlayColor = onSurface.copy(alpha = alpha)
            // Fill through the exact outline so per-corner radii are honored;
            // reusing one corner's radius would overflow asymmetric shapes
            // such as the end cards of a Connected group.
            when (val outline = shape.createOutline(size, layoutDirection, this)) {
                is Outline.Rectangle -> drawRect(color = overlayColor)
                else -> drawPath(Path().apply { addOutline(outline) }, overlayColor)
            }
        }
    }
}

/** State-layer alpha for the pressed state (M3 spec). */
internal const val PressedStateLayerAlpha: Float = 0.08f

/** State-layer alpha for the hovered state (M3 spec). */
internal const val HoveredStateLayerAlpha: Float = 0.04f

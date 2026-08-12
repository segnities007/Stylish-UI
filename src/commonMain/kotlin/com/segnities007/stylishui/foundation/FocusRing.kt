package com.segnities007.stylishui.foundation

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Draws a focus indicator ring around this component while the
 * [interactionSource] reports a focus interaction.
 *
 * Implements the web "focus-visible ring" pattern used by shadcn/ui and
 * Radix UI for keyboard accessibility: an interactive element gains a
 * clearly visible outline only when it is focused, making keyboard
 * navigation discoverable without adding permanent visual noise. When
 * the source does not report focus, the modifier has no visual effect.
 *
 * The ring is drawn with [drawWithContent] — the component's content is
 * drawn first, then the ring on top — so it remains visible over any
 * container color or state layer. The ring follows [shape] (which should
 * match the component's container shape): rounded shapes get a rounded
 * stroke using the shape's outline corner radius, and non-rounded shapes
 * fall back to a rectangular stroke.
 *
 * Combine this with [Modifier.stylishStateLayer] on custom
 * [androidx.compose.material3.Surface]-based components (such as the
 * Stylish chip, FAB, and icon-button families); components that wrap
 * Material 3 widgets already receive M3's internal focus indicator and
 * should not add this modifier again.
 *
 * @param interactionSource The source to observe for focus interactions.
 *   Typically the same source hoisted through the component's
 *   `interactionSource` parameter (or an internally remembered one).
 * @param shape The shape the ring is drawn with. Defaults to
 *   [RectangleShape]; pass the container's shape (e.g. `CircleShape`) for
 *   non-rectangular surfaces.
 * @param color The color of the ring. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param width The stroke width of the ring in dp. Defaults to 2.dp —
 *   subtle enough not to compete with the component, visible enough for
 *   keyboard navigation.
 *
 * @see Modifier.stylishStateLayer
 */
@Composable
public fun Modifier.stylishFocusRing(
    interactionSource: InteractionSource,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.primary,
    width: Dp = 2.dp,
): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    return drawWithContent {
        drawContent()
        if (isFocused) {
            val stroke = Stroke(width = width.toPx())
            when (val outline = shape.createOutline(size, layoutDirection, this)) {
                is Outline.Rounded -> drawRoundRect(
                    color = color,
                    style = stroke,
                    cornerRadius = outline.roundRect.topLeftCornerRadius,
                )
                is Outline.Rectangle -> drawRect(color = color, style = stroke)
                is Outline.Generic -> drawRect(color = color, style = stroke)
            }
        }
    }
}

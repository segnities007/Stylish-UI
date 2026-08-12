package com.segnities007.stylishui.components.charts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * Draws a rectangle whose only the top-left and top-right corners are
 * rounded, leaving the bottom edge square.
 *
 * This is used by [SimpleBarChart] to cap the topmost non-zero segment of
 * a stacked bar so the stack as a whole appears to have rounded top corners
 * while internal segment boundaries remain flush.
 *
 * The effective radius is clamped to half the width and to the full height,
 * preventing visual artifacts when the segment is very short or narrow.
 * If either [width] or [height] is non-positive the call is a no-op.
 *
 * Available on all platforms (commonMain); the implementation uses only
 * Compose Multiplatform drawing APIs.
 *
 * @param color Fill color of the rectangle.
 * @param left X coordinate of the rectangle's left edge in pixels.
 * @param top Y coordinate of the rectangle's top edge in pixels.
 * @param width Rectangle width in pixels.
 * @param height Rectangle height in pixels.
 * @param radius Desired corner radius in pixels; clamped internally to
 *   `min(radius, height, width / 2)`.
 * @see SimpleBarChart
 * @see BarChartSegment
 */
public fun DrawScope.drawTopRoundedRect(
    color: Color,
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    radius: Float,
) {
    if (height <= 0f || width <= 0f) return
    val r = radius.coerceAtMost(height).coerceAtMost(width / 2f)
    val path = Path().apply {
        moveTo(left, top + height)
        lineTo(left, top + r)
        quadraticTo(left, top, left + r, top)
        lineTo(left + width - r, top)
        quadraticTo(left + width, top, left + width, top + r)
        lineTo(left + width, top + height)
        close()
    }
    drawPath(path, color)
}

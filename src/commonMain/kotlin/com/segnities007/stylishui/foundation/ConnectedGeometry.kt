package com.segnities007.stylishui.foundation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * Describes which corners of a Connected UI item are *outer* corners (large radius) versus
 * *inner/joined* corners (small radius).
 *
 * In the Connected UI layout system, items are arranged edge-to-edge with a narrow gap. Corners
 * that face the exterior of the group use [StylishDimensions.connectedCornerRadius] (default
 * 12 dp), while corners that face an adjacent item use [StylishDimensions.joinedCornerRadius]
 * (default 2 dp). This creates the visual "notch" effect where items interlock.
 *
 * The coordinate system follows Compose layout conventions: `topStart` is the top-left corner
 * in LTR layouts (top-right in RTL), `topEnd` is top-right in LTR, and so on.
 *
 * Compute instances with [connectedColumnCorners], [connectedRowCorners], or
 * [connectedGridCorners], then pass to [connectedShape] and
 * [com.segnities007.stylishui.foundation.connectedOutline].
 *
 * @property topStart Whether the top-start corner is an outer (large-radius) corner.
 * @property topEnd Whether the top-end corner is an outer (large-radius) corner.
 * @property bottomStart Whether the bottom-start corner is an outer (large-radius) corner.
 * @property bottomEnd Whether the bottom-end corner is an outer (large-radius) corner.
 * @see ConnectedEdges
 * @see connectedShape
 */
@Immutable
public data class ConnectedCorners(
    public val topStart: Boolean = false,
    public val topEnd: Boolean = false,
    public val bottomStart: Boolean = false,
    public val bottomEnd: Boolean = false,
) {
    public companion object {
        /**
         * A [ConnectedCorners] with all four corners marked as outer corners.
         *
         * Use for a standalone item that has no neighbors — every corner receives the large
         * [StylishDimensions.connectedCornerRadius].
         */
        public val Standalone: ConnectedCorners = ConnectedCorners(true, true, true, true)
    }
}

/**
 * Describes which edges of a Connected UI item should have an outline border drawn.
 *
 * Each boolean corresponds to one side of the item's bounding box. When an edge is `false`,
 * [com.segnities007.stylishui.foundation.connectedOutline] skips drawing that segment, allowing
 * adjacent items to share a seamless boundary without doubled strokes.
 *
 * The naming follows Compose layout direction: `start` is left in LTR (right in RTL), `end`
 * is right in LTR.
 *
 * @property top Whether to draw the top edge.
 * @property end Whether to draw the end (trailing) edge.
 * @property bottom Whether to draw the bottom edge.
 * @property start Whether to draw the start (leading) edge.
 * @see ConnectedCorners
 * @see com.segnities007.stylishui.foundation.connectedOutline
 */
@Immutable
public data class ConnectedEdges(
    public val top: Boolean,
    public val end: Boolean,
    public val bottom: Boolean,
    public val start: Boolean,
) {
    public companion object {
        /**
         * A [ConnectedEdges] with all four edges enabled.
         *
         * Use for standalone items or when every side of the item should display a border.
         */
        public val All: ConnectedEdges = ConnectedEdges(true, true, true, true)
    }
}

/**
 * Builds a [RoundedCornerShape] for a Connected UI item based on its [corners] configuration.
 *
 * Each corner flagged as outer in [corners] receives [cornerRadius] (the large outer radius);
 * all other corners receive [joinedCornerRadius] (the small junction radius). The resulting
 * shape clips the item's background and content so that outer corners are smoothly rounded
 * while inner corners form tight notches against neighboring items.
 *
 * @param corners Which corners are outer (large-radius) versus joined (small-radius).
 * @param cornerRadius The radius for outer corners. Defaults to
 *   [DefaultStylishDimensions.connectedCornerRadius] (12 dp).
 * @param joinedCornerRadius The radius for inner/joined corners. Defaults to
 *   [DefaultStylishDimensions.joinedCornerRadius] (2 dp).
 * @return A [Shape] suitable for use with `Modifier.clip()` or `Modifier.background()`.
 * @see ConnectedCorners
 * @see connectedColumnCorners
 * @see connectedRowCorners
 * @see connectedGridCorners
 */
public fun connectedShape(
    corners: ConnectedCorners,
    cornerRadius: Dp = DefaultStylishDimensions.connectedCornerRadius,
    joinedCornerRadius: Dp = DefaultStylishDimensions.joinedCornerRadius,
): Shape = RoundedCornerShape(
    topStart = if (corners.topStart) cornerRadius else joinedCornerRadius,
    topEnd = if (corners.topEnd) cornerRadius else joinedCornerRadius,
    bottomStart = if (corners.bottomStart) cornerRadius else joinedCornerRadius,
    bottomEnd = if (corners.bottomEnd) cornerRadius else joinedCornerRadius,
)

/**
 * Computes the outer corners for the item at [index] in a vertical (column) Connected UI list.
 *
 * In a vertical arrangement, only the first item has outer top corners and only the last item
 * has outer bottom corners; all middle items have only joined corners. A single-item list
 * produces [ConnectedCorners.Standalone].
 *
 * @param index Zero-based position of the item within the list.
 * @param size Total number of items in the list. Must be greater than zero.
 * @return The [ConnectedCorners] describing which corners face the exterior.
 * @see connectedShape
 * @see connectedColumnEdges
 */
public fun connectedColumnCorners(index: Int, size: Int): ConnectedCorners {
    require(index in 0 until size) { "index must reference an existing item" }
    return ConnectedCorners(
        topStart = index == 0,
        topEnd = index == 0,
        bottomStart = index == size - 1,
        bottomEnd = index == size - 1,
    )
}

/**
 * Computes the outer corners for the item at [index] in a horizontal (row) Connected UI list.
 *
 * In a horizontal arrangement, only the first item has outer start-side corners (topStart and
 * bottomStart) and only the last item has outer end-side corners (topEnd and bottomEnd).
 *
 * @param index Zero-based position of the item within the row.
 * @param size Total number of items in the row. Must be greater than zero.
 * @return The [ConnectedCorners] describing which corners face the exterior.
 * @see connectedShape
 * @see connectedRowEdges
 */
public fun connectedRowCorners(index: Int, size: Int): ConnectedCorners {
    require(index in 0 until size) { "index must reference an existing item" }
    return ConnectedCorners(
        topStart = index == 0,
        bottomStart = index == 0,
        topEnd = index == size - 1,
        bottomEnd = index == size - 1,
    )
}

/**
 * Computes the outer corners for the item at [index] in a grid-based Connected UI layout.
 *
 * The grid is filled row-major with [columns] items per row. A corner is marked as outer only
 * when the item has no neighbor in the two directions that meet at that corner. For example,
 * `topStart` is outer when there is no item above *and* no item to the left. The last row may
 * be partially filled; the function accounts for this by checking actual item existence rather
 * than assuming a full rectangular grid.
 *
 * A partially filled final row is stretched to the full grid width by the Connected grid
 * layouts, so every column of the row above it is treated as having a neighbor below. This
 * keeps the junction between the last full row and the stretched final row a seamless
 * joined edge instead of exposing outer corners toward the stretched row.
 *
 * @param index Zero-based position of the item in row-major order.
 * @param size Total number of items in the grid. Must be greater than zero.
 * @param columns Number of columns in the grid layout.
 * @return The [ConnectedCorners] describing which corners face the exterior.
 * @throws IllegalArgumentException if [columns] is not greater than zero, or if [index] is
 *   outside the range `0 until size`.
 * @see connectedShape
 * @see connectedColumnCorners
 * @see connectedRowCorners
 */
public fun connectedGridCorners(index: Int, size: Int, columns: Int): ConnectedCorners {
    require(columns > 0) { "columns must be greater than zero" }
    require(index in 0 until size) { "index must reference an existing item" }

    val column = index % columns
    val row = index / columns
    val lastRow = (size - 1) / columns
    val finalRowStretched = size % columns != 0

    // Left/right neighbours within the same row are checked against actual item existence.
    fun hasHorizontalNeighbor(targetColumn: Int): Boolean {
        if (targetColumn !in 0 until columns) return false
        return row * columns + targetColumn in 0 until size
    }

    // Above/below neighbours count only when an item actually exists in the same column.
    fun hasVerticalNeighbor(targetRow: Int): Boolean {
        if (targetRow < 0) return false
        return targetRow * columns + column in 0 until size
    }

    val hasAbove = hasVerticalNeighbor(row - 1)
    // A partially filled final row is stretched to the full grid width by the layouts, so
    // every column of the row directly above it has a neighbor below.
    val hasBelow = if (finalRowStretched && row == lastRow - 1) {
        true
    } else {
        hasVerticalNeighbor(row + 1)
    }
    val hasLeft = hasHorizontalNeighbor(column - 1)
    val hasRight = hasHorizontalNeighbor(column + 1)

    return ConnectedCorners(
        topStart = !hasAbove && !hasLeft,
        topEnd = !hasAbove && !hasRight,
        bottomStart = !hasBelow && !hasLeft,
        bottomEnd = !hasBelow && !hasRight,
    )
}

/**
 * Returns the outline edges for the item at [index] in a horizontal (row) Connected UI list.
 *
 * Currently all items draw all four edges ([ConnectedEdges.All]); the [index] and [size]
 * parameters are validated and reserved for future edge-suppression logic (e.g. hiding shared
 * internal borders between adjacent items).
 *
 * @param index Zero-based position of the item within the row.
 * @param size Total number of items in the row.
 * @return The [ConnectedEdges] indicating which sides to outline.
 * @throws IllegalArgumentException if [index] is outside the range `0 until size`.
 * @see connectedRowCorners
 * @see com.segnities007.stylishui.foundation.connectedOutline
 */
public fun connectedRowEdges(index: Int, size: Int): ConnectedEdges {
    require(index in 0 until size) { "index must reference an existing item" }
    return ConnectedEdges.All
}

/**
 * Returns the outline edges for the item at [index] in a vertical (column) Connected UI list.
 *
 * Currently all items draw all four edges ([ConnectedEdges.All]); the [index] and [size]
 * parameters are validated and reserved for future edge-suppression logic (e.g. hiding shared
 * internal borders between adjacent items).
 *
 * @param index Zero-based position of the item within the column.
 * @param size Total number of items in the column.
 * @return The [ConnectedEdges] indicating which sides to outline.
 * @throws IllegalArgumentException if [index] is outside the range `0 until size`.
 * @see connectedColumnCorners
 * @see com.segnities007.stylishui.foundation.connectedOutline
 */
public fun connectedColumnEdges(index: Int, size: Int): ConnectedEdges {
    require(index in 0 until size) { "index must reference an existing item" }
    return ConnectedEdges.All
}

/**
 * Returns the outline edges for the item at [index] in a grid-based Connected UI layout.
 *
 * Currently all items draw all four edges ([ConnectedEdges.All]); the parameters are validated
 * and reserved for future edge-suppression logic (e.g. hiding shared internal borders between
 * adjacent items). Provided for symmetry with [connectedRowEdges] and [connectedColumnEdges]
 * so grid layouts source their edges from the Foundation layer.
 *
 * @param index Zero-based position of the item in row-major order.
 * @param size Total number of items in the grid. Must be greater than zero.
 * @param columns Number of columns in the grid layout. Must be greater than zero.
 * @return The [ConnectedEdges] indicating which sides to outline.
 * @throws IllegalArgumentException if [columns] is not greater than zero, or if [index] is
 *   outside the range `0 until size`.
 * @see connectedGridCorners
 * @see com.segnities007.stylishui.foundation.connectedOutline
 */
public fun connectedGridEdges(index: Int, size: Int, columns: Int): ConnectedEdges {
    require(columns > 0) { "columns must be greater than zero" }
    require(index in 0 until size) { "index must reference an existing item" }
    return ConnectedEdges.All
}

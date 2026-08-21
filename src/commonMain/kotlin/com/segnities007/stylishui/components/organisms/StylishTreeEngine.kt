package com.segnities007.stylishui.components.organisms

import androidx.compose.runtime.Immutable
import com.segnities007.stylishui.foundation.headless.StylishLayoutDirection
import com.segnities007.stylishui.foundation.headless.StylishLayoutEngine
import com.segnities007.stylishui.foundation.headless.StylishLayoutRect
import com.segnities007.stylishui.foundation.headless.StylishRenderNode
import com.segnities007.stylishui.foundation.headless.StylishRenderPlan
import com.segnities007.stylishui.foundation.headless.StylishSemanticRole
import com.segnities007.stylishui.foundation.headless.StylishViewport

/** A visible tree node paired with its indentation depth. */
@Immutable
public data class StylishVisibleTreeNode<T>(
    public val node: StylishTreeNode<T>,
    public val depth: Int,
)

/**
 * Flattens only expanded branches of a tree in display order.
 *
 * The result is suitable for `LazyColumn`, so collapsed or off-screen branches do not create a
 * recursive composition tree. IDs must be stable and unique within the supplied tree.
 */
public fun <T> flattenStylishTree(
    nodes: List<StylishTreeNode<T>>,
    expandedIds: Set<Any>,
): List<StylishVisibleTreeNode<T>> {
    // An explicit stack keeps deeply nested, user-supplied trees from overflowing the platform
    // call stack. Push children in reverse order so the resulting list remains display-ordered.
    val pending = nodes.asReversed().map { it to 0 }.toMutableList()
    return buildList {
        while (pending.isNotEmpty()) {
            val (node, depth) = pending.removeAt(pending.lastIndex)
            add(StylishVisibleTreeNode(node, depth))
            if (node.id in expandedIds && node.children.isNotEmpty()) {
                node.children.asReversed().forEach { child -> pending += child to (depth + 1) }
            }
        }
    }
}

/** Input to [StylishTreeLayoutEngine], independent from a Compose tree or platform view. */
public data class StylishTreeLayoutInput<T>(
    public val visibleNodes: List<StylishVisibleTreeNode<T>>,
    public val expandedIds: Set<Any> = emptySet(),
    public val selectedId: Any? = null,
    public val focusedId: Any? = null,
)

/**
 * Deterministic tree layout and semantics plan for native and Compose renderers.
 *
 * [StylishTree] remains the reference Compose renderer. A Web, SwiftUI, or desktop renderer can
 * consume this same output to preserve row geometry, identity, and accessibility semantics
 * without importing Compose. Coordinates are in viewport pixels.
 */
public class StylishTreeLayoutEngine<T>(
    public val rowHeightPx: Float = 48f,
    public val indentPx: Float = 20f,
    public val horizontalPaddingPx: Float = 0f,
) : StylishLayoutEngine<StylishTreeLayoutInput<T>> {
    override fun layout(input: StylishTreeLayoutInput<T>, viewport: StylishViewport): StylishRenderPlan {
        val safeViewport = viewport.normalized()
        val rowHeight = rowHeightPx.safeTreeDimension(48f)
        val indent = indentPx.safeTreeDimension(20f)
        val horizontalPadding = horizontalPaddingPx.safeTreeDimension()
            .coerceAtMost(safeViewport.widthPx / 2f)
        val nodes = input.visibleNodes.mapIndexed { index, visible ->
            val depthOffset = visible.depth * indent
            val left = if (safeViewport.layoutDirection == StylishLayoutDirection.Ltr) {
                horizontalPadding + depthOffset
            } else {
                horizontalPadding
            }
            val right = if (safeViewport.layoutDirection == StylishLayoutDirection.Ltr) {
                safeViewport.widthPx - horizontalPadding
            } else {
                safeViewport.widthPx - horizontalPadding - depthOffset
            }.coerceAtLeast(left).coerceAtMost(safeViewport.widthPx)
            val node = visible.node
            StylishRenderNode(
                id = treeNodeRenderId(node.id),
                bounds = StylishLayoutRect(
                    leftPx = left.coerceAtMost(safeViewport.widthPx),
                    topPx = index * rowHeight,
                    rightPx = right,
                    bottomPx = (index + 1) * rowHeight,
                ),
                role = StylishSemanticRole.TreeItem,
                label = node.label,
                stateDescription = if (node.children.isNotEmpty()) {
                    if (node.id in input.expandedIds) "expanded" else "collapsed"
                } else {
                    null
                },
                selected = node.id == input.selectedId,
                children = node.children.map { child -> treeNodeRenderId(child.id) },
            )
        }
        val treeBottom = nodes.lastOrNull()?.bounds?.bottomPx ?: 0f
        return StylishRenderPlan(
            nodes = listOf(
                StylishRenderNode(
                    id = "stylish-tree",
                    bounds = StylishLayoutRect(0f, 0f, safeViewport.widthPx, treeBottom),
                    role = StylishSemanticRole.Tree,
                    children = nodes.map { it.id },
                ),
            ) + nodes,
            focusedNodeId = input.focusedId?.let(::treeNodeRenderId),
        )
    }
}

/** Stable string identity used by every platform renderer for a tree node. */
public fun treeNodeRenderId(id: Any): String = "tree-node-${id.toString()}"

private fun Float.safeTreeDimension(fallback: Float = 0f): Float =
    if (isFinite() && this >= 0f) this else fallback

package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.organisms.StylishTreeNode
import com.segnities007.stylishui.components.organisms.flattenStylishTree
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StylishTreeEngineTest {
    @Test
    fun flattenOnlyIncludesExpandedBranches() {
        val nodes = listOf(
            StylishTreeNode("root", "Root", 0, listOf(StylishTreeNode("child", "Child", 1))),
            StylishTreeNode("second", "Second", 2),
        )
        assertEquals(listOf("root", "second"), flattenStylishTree(nodes, emptySet()).map { it.node.id })
        assertEquals(listOf("root", "child", "second"), flattenStylishTree(nodes, setOf("root")).map { it.node.id })
        assertEquals(listOf(0, 1, 0), flattenStylishTree(nodes, setOf("root")).map { it.depth })
    }

    @Test
    fun flattenHandlesDeepTreesWithoutRecursiveStackGrowth() {
        val root = (0 until 2_000).fold(null as StylishTreeNode<Unit>?) { child, index ->
            StylishTreeNode(index, "Node $index", Unit, listOfNotNull(child))
        }
        val nodes = if (root == null) emptyList() else listOf(root)
        val expanded = (0 until 2_000).toSet()

        val visible = flattenStylishTree(nodes, expanded)

        assertEquals(2_000, visible.size)
        assertEquals(1_999, visible.first().node.id)
        assertEquals(0, visible.last().node.id)
        assertEquals(1_999, visible.last().depth)
    }

    @Test
    fun flattenHandlesLargeSiblingCollectionsWithoutExpandingCollapsedBranches() {
        val nodes = (0 until 100_000).map { index ->
            StylishTreeNode(index, "Node $index", Unit)
        }

        val visible = flattenStylishTree(nodes, emptySet())

        assertEquals(100_000, visible.size)
        assertEquals(0, visible.first().node.id)
        assertEquals(99_999, visible.last().node.id)
        assertTrue(visible.all { it.depth == 0 })
    }
}

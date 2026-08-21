package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.charts.StylishChartAction
import com.segnities007.stylishui.components.charts.StylishChartState
import com.segnities007.stylishui.components.charts.StylishChartStateReducer
import com.segnities007.stylishui.components.organisms.StylishTreeAction
import com.segnities007.stylishui.components.organisms.StylishTreeLayoutEngine
import com.segnities007.stylishui.components.organisms.StylishTreeLayoutInput
import com.segnities007.stylishui.components.organisms.StylishTreeNode
import com.segnities007.stylishui.components.organisms.StylishTreeState
import com.segnities007.stylishui.components.organisms.StylishTreeStateReducer
import com.segnities007.stylishui.components.organisms.flattenStylishTree
import com.segnities007.stylishui.components.organisms.treeNodeRenderId
import com.segnities007.stylishui.foundation.headless.StylishLayoutDirection
import com.segnities007.stylishui.foundation.headless.StylishLayoutRect
import com.segnities007.stylishui.foundation.headless.StylishReducer
import com.segnities007.stylishui.foundation.headless.StylishSemanticRole
import com.segnities007.stylishui.foundation.headless.StylishViewport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StylishHeadlessArchitectureTest {
    @Test
    fun genericReducerIsFrameworkNeutralAndReplayable() {
        val reducer = StylishReducer<Int, Int> { state, action -> state + action }
        assertEquals(6, reducer.reduce(reducer.reduce(1, 2), 3))
    }

    @Test
    fun viewportAndRectNormalizeHostInput() {
        val normalized = StylishViewport(
            widthPx = Float.NaN,
            heightPx = Float.POSITIVE_INFINITY,
            density = -2f,
        ).normalized()
        assertEquals(0f, normalized.widthPx)
        assertEquals(0f, normalized.heightPx)
        assertEquals(0.01f, normalized.density)

        val rect = StylishLayoutRect(2f, 4f, 12f, 14f)
        assertEquals(10f, rect.widthPx)
        assertEquals(10f, rect.heightPx)
        assertTrue(rect.contains(2f, 4f))
        assertFalse(rect.contains(13f, 4f))
    }

    @Test
    fun treeLayoutPlanPreservesIdentitySemanticsAndFocus() {
        val root = StylishTreeNode(
            id = "root",
            label = "Root",
            value = Unit,
            children = listOf(StylishTreeNode("child", "Child", Unit)),
        )
        val visible = flattenStylishTree(listOf(root), setOf("root"))
        val plan = StylishTreeLayoutEngine<Unit>(rowHeightPx = 40f, indentPx = 16f).layout(
            StylishTreeLayoutInput(
                visibleNodes = visible,
                expandedIds = setOf("root"),
                selectedId = "child",
                focusedId = "child",
            ),
            StylishViewport(widthPx = 200f, heightPx = 100f),
        )

        assertEquals(listOf("stylish-tree", "tree-node-root", "tree-node-child"), plan.nodeIds())
        assertEquals("tree-node-child", plan.focusedNodeId)
        assertEquals(StylishSemanticRole.Tree, plan.node("stylish-tree")?.role)
        assertEquals("expanded", plan.node(treeNodeRenderId("root"))?.stateDescription)
        assertTrue(plan.node(treeNodeRenderId("child"))?.selected == true)
        assertEquals(16f, plan.node(treeNodeRenderId("child"))?.bounds?.leftPx)
        assertEquals(40f, plan.node(treeNodeRenderId("child"))?.bounds?.topPx)
    }

    @Test
    fun rtlTreePlanIndentsFromTheTrailingEdge() {
        val node = StylishTreeNode("n", "Node", Unit)
        val visible = flattenStylishTree(listOf(node), emptySet())
        val plan = StylishTreeLayoutEngine<Unit>(indentPx = 20f, horizontalPaddingPx = 4f).layout(
            StylishTreeLayoutInput(visibleNodes = visible),
            StylishViewport(200f, 50f, layoutDirection = StylishLayoutDirection.Rtl),
        )
        val bounds = plan.node(treeNodeRenderId("n"))!!.bounds
        assertEquals(4f, bounds.leftPx)
        assertEquals(196f, bounds.rightPx)
    }

    @Test
    fun componentReducersExposeOneSharedReducerContract() {
        val tree = StylishTreeStateReducer.reduce(StylishTreeState(), StylishTreeAction.Select("x"))
        assertEquals("x", tree.focusedId)
        val chart = StylishChartStateReducer.reduce(
            StylishChartState(),
            StylishChartAction.ToggleSeries("Revenue"),
        )
        assertEquals(setOf("Revenue"), chart.visibleSeriesNames)
    }
}

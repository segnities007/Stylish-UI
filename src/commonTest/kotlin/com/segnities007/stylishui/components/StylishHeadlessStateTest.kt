package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.charts.StylishChartAction
import com.segnities007.stylishui.components.charts.StylishChartSelection
import com.segnities007.stylishui.components.charts.StylishChartState
import com.segnities007.stylishui.components.charts.reduce as reduceChartState
import com.segnities007.stylishui.components.organisms.StylishTransferAction
import com.segnities007.stylishui.components.organisms.StylishTransferState
import com.segnities007.stylishui.components.organisms.reduce
import com.segnities007.stylishui.components.organisms.StylishTreeAction
import com.segnities007.stylishui.components.organisms.StylishTreeState
import kotlin.test.Test
import kotlin.test.assertEquals

class StylishHeadlessStateTest {
    @Test
    fun treeReducerIsPureAndFocusFollowsSelection() {
        val initial = StylishTreeState()
        val expanded = initial.reduce(StylishTreeAction.ToggleExpanded("root"))
        assertEquals(setOf<Any>("root"), expanded.expandedIds)
        assertEquals(initial, StylishTreeState())
        assertEquals(
            "child",
            expanded.reduce(StylishTreeAction.Select("child")).focusedId,
        )
    }

    @Test
    fun transferReducerMovesOnlyHighlightedKeys() {
        val initial = StylishTransferState(selectedKeys = setOf<Any>("b"), highlightedKeys = setOf<Any>("a"))
        val moved = initial.reduce(StylishTransferAction.MoveHighlightedToSelected)
        assertEquals(setOf<Any>("a", "b"), moved.selectedKeys)
        assertEquals(emptySet(), moved.highlightedKeys)
        assertEquals(initial, StylishTransferState(selectedKeys = setOf<Any>("b"), highlightedKeys = setOf<Any>("a")))
    }

    @Test
    fun transferSingleHighlightCanBeToggledWithoutChangingSelection() {
        val state = StylishTransferState(selectedKeys = setOf<Any>("selected"))
        val highlighted = state.reduce(StylishTransferAction.ToggleHighlighted("candidate"))
        assertEquals(setOf<Any>("selected"), highlighted.selectedKeys)
        assertEquals(setOf<Any>("candidate"), highlighted.highlightedKeys)
    }

    @Test
    fun chartReducerKeepsSelectionAndSeriesVisibilityIndependent() {
        val selected = StylishChartState().reduceChartState(
            StylishChartAction.Select(StylishChartSelection(seriesIndex = 1, pointIndex = 2)),
        )
        val visible = selected.reduceChartState(StylishChartAction.ToggleSeries("Target"))
        assertEquals(StylishChartSelection(1, 2), visible.selection)
        assertEquals(setOf("Target"), visible.visibleSeriesNames)
    }
}

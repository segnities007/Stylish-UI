package com.segnities007.stylishui.foundation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NestedScrollVisibilityStateTest {
    @Test
    fun hidesAfterUpwardScrollCrossesThreshold() {
        val state = VisibilityState.NestedScrollAware(
            thresholdPx = 48f,
            initiallyVisible = true,
        )

        state.updateFromScroll(-20f)
        assertTrue(state.visible)

        state.updateFromScroll(-28f)
        assertFalse(state.visible)
    }

    @Test
    fun showsAfterDownwardScrollCrossesThreshold() {
        val state = VisibilityState.NestedScrollAware(
            thresholdPx = 48f,
            initiallyVisible = false,
        )

        state.updateFromScroll(48f)

        assertTrue(state.visible)
    }

    @Test
    fun directionChangeResetsAccumulatedDistance() {
        val state = VisibilityState.NestedScrollAware(
            thresholdPx = 48f,
            initiallyVisible = true,
        )

        state.updateFromScroll(-40f)
        state.updateFromScroll(10f)
        state.updateFromScroll(-10f)

        assertTrue(state.visible)
    }

    @Test
    fun zeroDeltaDoesNotChangeVisibility() {
        val state = VisibilityState.NestedScrollAware(
            thresholdPx = 48f,
            initiallyVisible = false,
        )

        state.updateFromScroll(0f)

        assertFalse(state.visible)
    }
}

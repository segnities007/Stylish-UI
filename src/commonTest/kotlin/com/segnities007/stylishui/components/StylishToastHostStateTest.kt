package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.molecules.StylishToastData
import com.segnities007.stylishui.components.molecules.StylishToastHostState
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StylishToastHostStateTest {
    @Test
    fun dismissingOneDuplicateToastDoesNotHideTheOtherEntry() {
        val state = StylishToastHostState()
        val toast = StylishToastData("同じ内容")
        val first = state.addToast(toast)
        val second = state.addToast(toast.copy())

        state.dismiss(first)

        assertFalse(first.visible)
        assertTrue(second.visible)
    }

    @Test
    fun publicDismissTargetsTheNewestMatchingToast() {
        val state = StylishToastHostState()
        val toast = StylishToastData("同じ内容")
        val first = state.addToast(toast)
        val second = state.addToast(toast.copy())

        state.dismiss(toast)

        assertTrue(first.visible)
        assertFalse(second.visible)
    }
}

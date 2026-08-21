package com.segnities007.stylishui.foundation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StylishInteractionPolicyTest {
    @Test
    fun defaultPolicyIsConsistentAndAccessible() {
        assertEquals(48f, DefaultStylishInteractionPolicy.minimumTarget.value)
        assertEquals(2f, DefaultStylishInteractionPolicy.focusRingWidth.value)
        assertEquals(0.98f, DefaultStylishInteractionPolicy.pressedScale)
        assertTrue(DefaultStylishInteractionPolicy.stateLayerEnabled)
        assertTrue(DefaultStylishInteractionPolicy.focusRingEnabled)
        assertTrue(DefaultStylishInteractionPolicy.pressScaleEnabled)
    }
}

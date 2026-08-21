package com.segnities007.stylishui.tokens

import kotlin.test.Test
import kotlin.test.assertEquals

class StylishAnimationTokensTest {
    @Test
    fun pressMotionDefaultsAreSafe() {
        val tokens = StylishAnimationTokens()
        assertEquals(0.98f, tokens.pressedScale)
        assertEquals(700f, tokens.springStiffness)
    }

    @Test
    fun pressMotionCanBeBranded() {
        val tokens = StylishAnimationTokens(pressedScale = 0.95f, springStiffness = 500f)
        assertEquals(0.95f, tokens.pressedScale)
        assertEquals(500f, tokens.springStiffness)
    }
}

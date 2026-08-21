package com.segnities007.stylishui.foundationconsumer

import com.segnities007.stylishui.foundation.headless.StylishViewport
import kotlin.test.Test
import kotlin.test.assertEquals

/** Verifies the sample consumes Foundation directly, without the styled root module. */
public class FoundationConsumerTest {
    @Test
    public fun normalizesInvalidViewportBeforeRendering() {
        val plan = FoundationConsumer.plan(
            StylishViewport(widthPx = Float.NaN, heightPx = -10f, density = 0f),
        )

        assertEquals(0f, plan.nodes.single().bounds.widthPx)
        assertEquals(0f, plan.nodes.single().bounds.heightPx)
        assertEquals("consumer-root", plan.nodeIds().single())
    }
}

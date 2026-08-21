package com.segnities007.stylishui.foundationconsumer

import com.segnities007.stylishui.foundation.headless.StylishLayoutEngine
import com.segnities007.stylishui.foundation.headless.StylishLayoutRect
import com.segnities007.stylishui.foundation.headless.StylishRenderNode
import com.segnities007.stylishui.foundation.headless.StylishRenderPlan
import com.segnities007.stylishui.foundation.headless.StylishSemanticRole
import com.segnities007.stylishui.foundation.headless.StylishViewport

/** Minimal downstream contract proving Foundation can be consumed without Compose. */
public object FoundationConsumer {
    /** Builds a deterministic, accessible one-node plan from a viewport. */
    public fun plan(viewport: StylishViewport): StylishRenderPlan =
        StylishLayoutEngine<Unit> { _, normalizedViewport ->
            val viewport = normalizedViewport.normalized()
            StylishRenderPlan(
                nodes = listOf(
                    StylishRenderNode(
                        id = "consumer-root",
                        bounds = StylishLayoutRect(
                            leftPx = 0f,
                            topPx = 0f,
                            rightPx = viewport.widthPx,
                            bottomPx = viewport.heightPx,
                        ),
                        role = StylishSemanticRole.Generic,
                        label = "Foundation consumer",
                    ),
                ),
            )
        }.layout(Unit, viewport)
}

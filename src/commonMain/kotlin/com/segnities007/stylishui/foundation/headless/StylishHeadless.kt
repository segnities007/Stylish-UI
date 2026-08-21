package com.segnities007.stylishui.foundation.headless

// Binary-compatibility copy: the canonical source is :foundation, while the root artifact keeps
// these classes until the next major release can migrate existing consumers safely.

/** Framework-neutral reducer contract retained in the root artifact for source and binary compatibility. */
public fun interface StylishReducer<S, A> {
    /** Returns the next state for [action] without mutating [state]. */
    public fun reduce(state: S, action: A): S
}

/** Direction used by a shared layout plan. */
public enum class StylishLayoutDirection { Ltr, Rtl }

/** Platform-neutral viewport metrics supplied by a host before layout. */
public data class StylishViewport(
    public val widthPx: Float,
    public val heightPx: Float,
    public val density: Float = 1f,
    public val layoutDirection: StylishLayoutDirection = StylishLayoutDirection.Ltr,
) {
    /** Returns safe, non-negative, finite metrics suitable for a layout engine. */
    public fun normalized(): StylishViewport = copy(
        widthPx = widthPx.safeDimension(),
        heightPx = heightPx.safeDimension(),
        density = density.safeDimension().coerceAtLeast(0.01f),
    )
}

/** A renderer-independent rectangle in viewport pixels. */
public data class StylishLayoutRect(
    public val leftPx: Float,
    public val topPx: Float,
    public val rightPx: Float,
    public val bottomPx: Float,
) {
    /** Width of this rectangle after coordinates have been normalized. */
    public val widthPx: Float get() = (rightPx - leftPx).coerceAtLeast(0f)
    /** Height of this rectangle after coordinates have been normalized. */
    public val heightPx: Float get() = (bottomPx - topPx).coerceAtLeast(0f)
    /** Whether a point lies inside this rectangle, including its edges. */
    public fun contains(xPx: Float, yPx: Float): Boolean =
        xPx >= leftPx && xPx <= rightPx && yPx >= topPx && yPx <= bottomPx
}

/** Semantic roles understood by host renderers. */
public enum class StylishSemanticRole {
    Generic,
    Button,
    Checkbox,
    Chart,
    Grid,
    GridCell,
    List,
    ListItem,
    Tree,
    TreeItem,
}

/** A single node in a renderer-independent visual and semantic plan. */
public data class StylishRenderNode(
    public val id: String,
    public val bounds: StylishLayoutRect,
    public val role: StylishSemanticRole = StylishSemanticRole.Generic,
    public val label: String? = null,
    public val stateDescription: String? = null,
    public val enabled: Boolean = true,
    public val selected: Boolean = false,
    public val children: List<String> = emptyList(),
)

/** Immutable output of a headless layout engine. */
public data class StylishRenderPlan(
    public val nodes: List<StylishRenderNode>,
    public val focusedNodeId: String? = null,
) {
    /** Finds a node by stable identity. */
    public fun node(id: String): StylishRenderNode? = nodes.firstOrNull { it.id == id }
    /** Returns node ids in deterministic plan order. */
    public fun nodeIds(): List<String> = nodes.map { it.id }
}

/** Computes a [StylishRenderPlan] from a pure model and host viewport. */
public fun interface StylishLayoutEngine<I> {
    /** Computes deterministic geometry and semantics for [input] in [viewport]. */
    public fun layout(input: I, viewport: StylishViewport): StylishRenderPlan
}

/** Platform bridge boundary for a render plan. */
public fun interface StylishRenderer<T> {
    /** Applies [plan] to [target] without changing semantic identity. */
    public fun render(plan: StylishRenderPlan, target: T)
}

private fun Float.safeDimension(): Float = if (isFinite() && this >= 0f) this else 0f

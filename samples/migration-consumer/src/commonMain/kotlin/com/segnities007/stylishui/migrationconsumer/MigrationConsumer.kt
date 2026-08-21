package com.segnities007.stylishui.migrationconsumer

import com.segnities007.stylishui.foundation.headless.StylishLayoutDirection
import com.segnities007.stylishui.foundation.headless.StylishReducer
import com.segnities007.stylishui.foundation.headless.StylishViewport
import com.segnities007.stylishui.structure.StylishGridSlot
import com.segnities007.stylishui.structure.stylishGridRows

/**
 * Minimal downstream canary for the staged Structure/Components split.
 *
 * It imports ONLY the two extracted physical artifacts (`:foundation` and
 * `:structure`) and never the styled root publication. A host that adopts this
 * surface today keeps compiling when the root stops shipping duplicated
 * headless packages in a future major release.
 */

/** Immutable domain value a host would render inside one grid cell. */
public data class MigrationTile(
    public val id: String,
    public val label: String,
)

/** Aggregated two-artifact adoption surface used by the boundary build gate. */
public object MigrationConsumer {
    /**
     * Pure Foundation reducer shared by every host runtime. Deliberately
     * framework-neutral: no Compose state holder, no platform type.
     */
    public val selectionReducer: StylishReducer<Boolean, Unit> =
        StylishReducer { selected, _ -> !selected }

    /** Deterministic Structure plan: row-major cells with explicit padding. */
    public fun gridPlan(
        tiles: List<MigrationTile>,
        columns: Int,
    ): List<List<StylishGridSlot<MigrationTile>>> = stylishGridRows(tiles, columns)

    /** Normalizes a raw host viewport through the Foundation contract. */
    public fun normalizedViewport(widthPx: Float, heightPx: Float): StylishViewport =
        StylishViewport(
            widthPx = widthPx,
            heightPx = heightPx,
            layoutDirection = StylishLayoutDirection.Ltr,
        ).normalized()
}

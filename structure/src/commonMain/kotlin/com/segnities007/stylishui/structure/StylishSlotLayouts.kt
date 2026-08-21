package com.segnities007.stylishui.structure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key as composeKey
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A framework-level cell in a [StylishSlotGrid] plan.
 *
 * Empty cells are explicit rather than represented by a nullable item, so a
 * grid can safely render nullable domain values and still preserve stable
 * column geometry in its final row.
 */
public sealed interface StylishGridSlot<out T> {
    /** A real item and its stable source index. */
    public data class Item<T>(public val value: T, public val index: Int) : StylishGridSlot<T>

    /** A layout-only cell used to pad the final row. */
    public data object Empty : StylishGridSlot<Nothing>
}

/**
 * Computes deterministic rows for a slot grid.
 *
 * This pure function is the shared contract behind the composable renderer.
 * It validates the grid shape once, preserves source order, and pads the last
 * row with explicit [StylishGridSlot.Empty] cells. Callers on Web, Android,
 * iOS, and Desktop can replay the same plan without a Compose runtime.
 *
 * @param items Values to place in row-major order.
 * @param columns Number of columns in every row; must be greater than zero.
 */
public fun <T> stylishGridRows(items: List<T>, columns: Int): List<List<StylishGridSlot<T>>> {
    require(columns > 0) { "columns must be greater than zero, was $columns" }
    if (items.isEmpty()) return emptyList()

    return items
        .chunked(columns)
        .mapIndexed { rowIndex, row ->
            val firstIndex = rowIndex * columns
            buildList(columns) {
                row.forEachIndexed { index, item ->
                    add(StylishGridSlot.Item(item, firstIndex + index))
                }
                repeat(columns - row.size) { add(StylishGridSlot.Empty) }
            }
        }
}

/**
 * A headless horizontal slot layout.
 *
 * This API owns only ordering, spacing, and stable identity. The caller owns
 * every visual and semantic decision inside [content]. Use it as the physical
 * Structure module's lowest-level row primitive, or wrap it with a branded
 * Finish component in the published Stylish UI module.
 *
 * @param items Values rendered in source order.
 * @param modifier Modifier applied to the root row.
 * @param spacing Horizontal space between slots.
 * @param key Optional stable key used to preserve slot-local state when items move.
 * @param content Renderer for one item. The [RowScope] allows the caller to
 *   opt into a weight without the Structure layer deciding widths.
 */
@Composable
public fun <T> StylishSlotRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    key: ((T) -> Any)? = null,
    content: @Composable RowScope.(item: T, index: Int) -> Unit,
) {
    require(spacing >= 0.dp) { "spacing must be non-negative, was $spacing" }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            if (key == null) content(item, index) else composeKey(key(item)) {
                content(item, index)
            }
        }
    }
}

/**
 * A headless vertical slot layout.
 *
 * The component deliberately exposes [ColumnScope] to the slot renderer so a
 * skin can choose weight or alignment while the Structure module remains
 * free of visual policy.
 *
 * @param items Values rendered in source order.
 * @param modifier Modifier applied to the root column.
 * @param spacing Vertical space between slots.
 * @param key Optional stable key used to preserve slot-local state when items move.
 * @param content Renderer for one item.
 */
@Composable
public fun <T> StylishSlotColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    key: ((T) -> Any)? = null,
    content: @Composable ColumnScope.(item: T, index: Int) -> Unit,
) {
    require(spacing >= 0.dp) { "spacing must be non-negative, was $spacing" }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            if (key == null) content(item, index) else composeKey(key(item)) {
                content(item, index)
            }
        }
    }
}

/**
 * A headless fixed-column grid composed from the pure [stylishGridRows] plan.
 *
 * Every row receives the same number of weighted cells. The final row is
 * padded with [StylishGridSlot.Empty] so responsive skins do not need special
 * casing or accidentally stretch the final item. Empty cells occupy space but
 * emit no content, semantics, or visual styling.
 *
 * @param items Values rendered in row-major order.
 * @param columns Number of columns; must be greater than zero.
 * @param modifier Modifier applied to the root column.
 * @param horizontalSpacing Horizontal space between cells.
 * @param verticalSpacing Vertical space between rows.
 * @param key Optional stable key used to preserve slot-local state when items move.
 * @param content Renderer for one real item. The supplied [RowScope] allows
 *   the caller to further control cell width inside the slot.
 */
@Composable
public fun <T> StylishSlotGrid(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    key: ((T) -> Any)? = null,
    content: @Composable RowScope.(item: T, index: Int) -> Unit,
) {
    require(horizontalSpacing >= 0.dp) {
        "horizontalSpacing must be non-negative, was $horizontalSpacing"
    }
    require(verticalSpacing >= 0.dp) {
        "verticalSpacing must be non-negative, was $verticalSpacing"
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        stylishGridRows(items, columns).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
            ) row@{
                row.forEach { slot ->
                    when (slot) {
                        StylishGridSlot.Empty -> Spacer(Modifier.weight(1f))
                        is StylishGridSlot.Item -> {
                            Box(Modifier.weight(1f)) {
                                if (key == null) {
                                    content(this@row, slot.value, slot.index)
                                } else composeKey(key(slot.value)) {
                                    content(this@row, slot.value, slot.index)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

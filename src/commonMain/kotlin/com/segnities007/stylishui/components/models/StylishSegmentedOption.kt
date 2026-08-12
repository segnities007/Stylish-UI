package com.segnities007.stylishui.components.models

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Immutable data describing a single option within a Segmented Button control.
 *
 * A Segmented Button presents a set of mutually exclusive (or multi-select) options in a
 * horizontal strip with connected corner geometry. Each option is represented by one instance
 * of this class. The generic type [T] allows callers to associate arbitrary domain values
 * (e.g. an enum constant or a route ID) with each segment.
 *
 * @param T The type of the domain value this option represents.
 * @property value The domain value associated with this option. The consuming composable
 *   compares [value] against the current selection to determine the selected state.
 * @property label The text displayed on the segment.
 * @property enabled Whether this segment is enabled. When `false`, the segment is
 *   visually dimmed and does not respond to interaction.
 * @property leadingContent Optional composable slot rendered before [label] (e.g. an icon).
 *   Receives [RowScope] for alignment control. `null` omits the slot.
 * @property trailingContent Optional composable slot rendered after [label] (e.g. a badge).
 *   Receives [RowScope] for alignment control. `null` omits the slot.
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishSegmentedOption<T>(
    val value: T,
    val label: String,
    val enabled: Boolean = true,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
    val trailingContent: (@Composable RowScope.() -> Unit)? = null,
)

package com.segnities007.stylishui.components.models

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Immutable data describing a single chip within a Connected Chip layout.
 *
 * Connected Chip layouts arrange chips horizontally with shared corner geometry
 * (see [com.segnities007.stylishui.foundation.ConnectedCorners]). The consuming composable
 * iterates over a `List<StylishConnectedChipItem>` and applies position-aware shapes and
 * outlines. Chips support single- or multi-select patterns via the [selected] flag.
 *
 * @property label The text displayed on the chip.
 * @property onClick The action invoked when the chip is tapped. When `null`, the chip renders
 *   as display-only per the [com.segnities007.stylishui.foundation.isActionable] contract —
 *   no ripple, no selection toggle, no click handling.
 * @property selected Whether the chip is currently in the selected state. The consuming layout
 *   applies a distinct visual treatment (e.g. filled background) for selected chips.
 * @property enabled Whether the chip is enabled. When `false`, the chip is visually dimmed
 *   and non-interactive regardless of [onClick].
 * @property leadingContent Optional composable slot rendered before [label] (e.g. a check icon
 *   or avatar). Receives [RowScope]. `null` omits the slot.
 * @property trailingContent Optional composable slot rendered after [label] (e.g. a close icon
 *   for removable chips). Receives [RowScope]. `null` omits the slot.
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedChipItem(
    val label: String,
    val onClick: (() -> Unit)? = null,
    val selected: Boolean = false,
    val enabled: Boolean = true,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
    val trailingContent: (@Composable RowScope.() -> Unit)? = null,
)

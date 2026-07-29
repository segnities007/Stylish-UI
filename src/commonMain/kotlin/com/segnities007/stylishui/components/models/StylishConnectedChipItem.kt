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
 * ## Two rendering modes
 *
 * **Structured mode** (default) — when [content] is `null`, the chip renders
 * a fixed row: [leadingContent] | [Text]([label]) | [trailingContent].
 *
 * **Content mode** — when [content] is non-null, it completely replaces the
 * structured row, giving the caller full control over the chip's inner layout.
 * [label], [leadingContent], and [trailingContent] are ignored in this mode.
 *
 * @property label The text displayed on the chip in structured mode.
 *   Ignored in content mode. Defaults to `""`.
 * @property onClick The action invoked when the chip is tapped. When `null`,
 *   the chip renders as display-only per the
 *   [com.segnities007.stylishui.foundation.isActionable] contract.
 * @property selected Whether the chip is currently in the selected state.
 * @property enabled Whether the chip is enabled. When `false`, the chip is
 *   visually dimmed and non-interactive regardless of [onClick].
 * @property leadingContent Composable slot rendered before [label] in
 *   structured mode. Ignored in content mode.
 * @property trailingContent Composable slot rendered after [label] in
 *   structured mode. Ignored in content mode.
 * @property content When non-null, replaces the entire structured row with
 *   caller-supplied content. Receives [RowScope] for weight/alignment
 *   control. When `null` (default), the structured row is rendered.
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedChipItem(
    val label: String = "",
    val onClick: (() -> Unit)? = null,
    val selected: Boolean = false,
    val enabled: Boolean = true,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
    val trailingContent: (@Composable RowScope.() -> Unit)? = null,
    val content: (@Composable RowScope.() -> Unit)? = null,
)

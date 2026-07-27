package com.segnities007.stylishui.components.models

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Immutable data describing a single row within a Connected List layout.
 *
 * Connected List layouts stack rows vertically with shared corner geometry
 * (see [com.segnities007.stylishui.foundation.ConnectedCorners]) and a narrow gap
 * ([com.segnities007.stylishui.tokens.StylishDimensions.connectedSpacing]). The consuming
 * composable iterates over a `List<StylishConnectedListItem>` and applies position-aware
 * shapes, outlines, and optional elevation for interactive rows.
 *
 * @property headline The primary text of the list row, displayed in a prominent style.
 * @property supportingText Optional single-line secondary text below [headline]. When `null`,
 *   no supporting-text region is rendered. For multi-line supporting content, use
 *   [supportingLines] instead.
 * @property onClick The action invoked on tap. When `null`, the row is display-only per the
 *   [com.segnities007.stylishui.foundation.isActionable] contract — no ripple, no elevation
 *   lift, no click handling.
 * @property onLongClick The action invoked on long-press. When `null`, long-press is not
 *   handled. A row is actionable if either [onClick] or [onLongClick] is non-null.
 * @property enabled Whether the row is enabled. When `false`, the row is visually dimmed and
 *   non-interactive regardless of click handlers.
 * @property leadingContent Optional composable slot rendered at the start of the row (e.g. an
 *   icon, avatar, or checkbox). Receives [RowScope]. `null` omits the slot.
 * @property trailingContent Optional composable slot rendered at the end of the row (e.g. a
 *   chevron, switch, or metadata badge). Receives [RowScope]. `null` omits the slot.
 * @property supportingLines A list of additional text lines rendered below [headline] and
 *   [supportingText]. Defaults to an empty list (no extra lines). Use this for structured
 *   multi-line detail such as address fields or metadata rows.
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedListItem(
    val headline: String,
    val supportingText: String? = null,
    val onClick: (() -> Unit)? = null,
    val onLongClick: (() -> Unit)? = null,
    val enabled: Boolean = true,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
    val trailingContent: (@Composable RowScope.() -> Unit)? = null,
    val supportingLines: List<String> = emptyList(),
)

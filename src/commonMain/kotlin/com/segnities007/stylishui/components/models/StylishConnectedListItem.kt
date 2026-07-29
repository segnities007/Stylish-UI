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
 * ## Two rendering modes
 *
 * **Structured mode** (default) — when [content] is `null`, the row renders
 * a fixed three-slot layout: [leadingContent] | text column ([headline] +
 * [supportingText] + [supportingLines]) | [trailingContent].
 *
 * **Content mode** — when [content] is non-null, it completely replaces the
 * structured row, giving the caller full control over the row's inner layout.
 * [headline], [supportingText], [supportingLines], [leadingContent], and
 * [trailingContent] are ignored in this mode.
 *
 * @property headline The primary text of the list row in structured mode.
 *   Ignored in content mode. Defaults to `""`.
 * @property supportingText Optional single-line secondary text below
 *   [headline] in structured mode. Ignored in content mode.
 * @property onClick The action invoked on tap. When `null`, the row is
 *   display-only per the [com.segnities007.stylishui.foundation.isActionable]
 *   contract.
 * @property onLongClick The action invoked on long-press. When `null`,
 *   long-press is not handled.
 * @property enabled Whether the row is enabled. When `false`, the row is
 *   visually dimmed and non-interactive regardless of click handlers.
 * @property leadingContent Composable slot rendered at the start of the row
 *   in structured mode. Ignored in content mode.
 * @property trailingContent Composable slot rendered at the end of the row
 *   in structured mode. Ignored in content mode.
 * @property supportingLines Additional text lines below [headline] and
 *   [supportingText] in structured mode. Ignored in content mode.
 * @property content When non-null, replaces the entire structured row with
 *   caller-supplied content, enabling fully custom row layouts. When `null`
 *   (default), the structured three-slot row is rendered.
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedListItem(
    val headline: String = "",
    val supportingText: String? = null,
    val onClick: (() -> Unit)? = null,
    val onLongClick: (() -> Unit)? = null,
    val enabled: Boolean = true,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
    val trailingContent: (@Composable RowScope.() -> Unit)? = null,
    val supportingLines: List<String> = emptyList(),
    val content: (@Composable () -> Unit)? = null,
)

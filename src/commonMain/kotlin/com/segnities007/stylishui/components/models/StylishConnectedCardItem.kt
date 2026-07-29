package com.segnities007.stylishui.components.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Immutable data describing a single card within a Connected Card layout.
 *
 * Connected Card layouts stack cards vertically with shared corner geometry
 * (see [com.segnities007.stylishui.foundation.ConnectedCorners]) and a narrow gap
 * ([com.segnities007.stylishui.tokens.StylishDimensions.connectedSpacing]). The consuming
 * composable iterates over a `List<StylishConnectedCardItem>` and applies position-aware
 * shapes, outlines, and elevation.
 *
 * ## Two rendering modes
 *
 * **Structured mode** (default) — when [content] is `null`, the card renders
 * a fixed three-slot row: [leadingContent] | title column ([title] +
 * [supportingText]) | [trailingContent].
 *
 * **Content mode** — when [content] is non-null, it completely replaces the
 * structured row, giving the caller full control over the card's inner layout.
 * [title], [supportingText], [leadingContent], and [trailingContent] are
 * ignored in this mode.
 *
 * @property title The primary text displayed prominently on the card in
 *   structured mode. Ignored in content mode. Defaults to `""`.
 * @property supportingText Secondary text shown below [title] in structured
 *   mode. An empty string (the default) hides the supporting-text region
 *   entirely. Ignored in content mode.
 * @property onClick The action invoked on tap. When `null`, the card is
 *   display-only per the [com.segnities007.stylishui.foundation.isActionable]
 *   contract — no ripple, no elevation lift, no click handling.
 * @property onLongClick The action invoked on long-press. When `null`,
 *   long-press is not handled. A card is actionable if either [onClick] or
 *   [onLongClick] is non-null.
 * @property enabled Whether the card is enabled. When `false`, the card is
 *   visually dimmed and non-interactive regardless of click handlers.
 * @property leadingContent Composable slot rendered at the start of the card
 *   in structured mode. Ignored in content mode.
 * @property trailingContent Composable slot rendered at the end of the card
 *   in structured mode. Ignored in content mode.
 * @property content When non-null, replaces the entire structured row with
 *   caller-supplied content, enabling fully custom card layouts. When `null`
 *   (default), the structured three-slot row is rendered.
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedCardItem(
    val title: String = "",
    val supportingText: String = "",
    val onClick: (() -> Unit)? = null,
    val onLongClick: (() -> Unit)? = null,
    val enabled: Boolean = true,
    val leadingContent: (@Composable () -> Unit)? = null,
    val trailingContent: (@Composable () -> Unit)? = null,
    val content: (@Composable () -> Unit)? = null,
)

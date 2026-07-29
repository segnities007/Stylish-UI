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
 * @property title The primary text displayed prominently on the card.
 * @property supportingText Secondary text shown below [title]. An empty string (the default)
 *   hides the supporting-text region entirely.
 * @property onClick The action invoked on tap. When `null`, the card is display-only per the
 *   [com.segnities007.stylishui.foundation.isActionable] contract — no ripple, no elevation
 *   lift, no click handling.
 * @property onLongClick The action invoked on long-press. When `null`, long-press is not
 *   handled. A card is actionable if either [onClick] or [onLongClick] is non-null.
 * @property enabled Whether the card is enabled. When `false`, the card is visually dimmed
 *   and non-interactive regardless of click handlers.
 * @property leadingContent Composable slot rendered at the start of the card (e.g. an icon or
 *   thumbnail). When `null` (the default), no leading content is rendered and no spacing
 *   is reserved.
 * @property trailingContent Composable slot rendered at the end of the card (e.g. a chevron,
 *   switch, or overflow menu). When `null` (the default), no trailing content is rendered
 *   and no spacing is reserved.
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedCardItem(
    val title: String,
    val supportingText: String = "",
    val onClick: (() -> Unit)? = null,
    val onLongClick: (() -> Unit)? = null,
    val enabled: Boolean = true,
    val leadingContent: (@Composable () -> Unit)? = null,
    val trailingContent: (@Composable () -> Unit)? = null,
)

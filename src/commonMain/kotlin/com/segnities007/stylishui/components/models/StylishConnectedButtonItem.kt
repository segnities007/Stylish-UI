package com.segnities007.stylishui.components.models

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Immutable data describing a single button within a Connected Button layout.
 *
 * Connected Button layouts arrange multiple buttons edge-to-edge with shared corner geometry
 * (see [com.segnities007.stylishui.foundation.ConnectedCorners]). Each item in the layout is
 * represented by one instance of this class. The consuming composable iterates over a
 * `List<StylishConnectedButtonItem>` and applies the appropriate shape and outline per position.
 *
 * @property onClick The action invoked when the button is tapped. When `null`, the button
 *   renders as display-only per the [com.segnities007.stylishui.foundation.isActionable]
 *   contract — no ripple, no click handling.
 * @property enabled Whether the button is enabled. When `false`, the button is visually
 *   dimmed and non-interactive regardless of [onClick].
 * @property colors Optional [ButtonColors] override for this specific button. When `null`,
 *   the consuming layout applies its default color mapping.
 * @property leadingContent Optional composable slot rendered before the main [content]
 *   (e.g. an icon). Receives [RowScope] for weight/alignment control. `null` omits the slot.
 * @property trailingContent Optional composable slot rendered after the main [content]
 *   (e.g. a badge or chevron). Receives [RowScope]. `null` omits the slot.
 * @property content The primary label composable for the button, rendered within [RowScope].
 * @see com.segnities007.stylishui.foundation.isActionable
 * @see com.segnities007.stylishui.foundation.ConnectedCorners
 */
@Immutable
public data class StylishConnectedButtonItem(
    val onClick: (() -> Unit)? = null,
    val enabled: Boolean = true,
    val colors: ButtonColors? = null,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
    val trailingContent: (@Composable RowScope.() -> Unit)? = null,
    val content: @Composable RowScope.() -> Unit,
)

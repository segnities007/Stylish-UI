package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.foundation.rememberStylishInteractionSource
import com.segnities007.stylishui.foundation.stylishInteractiveElevation
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A standalone list item for one row of information with optional
 * interaction, tap/long-press actions, and leading/trailing slots.
 *
 * This is the plain (non-connected) sibling of the connected list-item
 * family: it renders as an independent rounded surface rather than
 * participating in connected-group geometry. Use it for single rows,
 * or place several in a [Column] when the connected look is not wanted.
 *
 * When [onClick] or [onLongClick] is provided, the row lifts with
 * [StylishTheme.dimensions.interactiveElevation] and is exposed with
 * `Role.Button` semantics; long-press fires a haptic pulse. When
 * [enabled] is `false` the row ignores pointer input, is flagged
 * `disabled()` in semantics, and its content is dimmed to 38 % alpha.
 * Rows with no action at all render as static content.
 *
 * @param headline Primary text of the row.
 * @param supportingText Optional secondary text below the headline.
 * @param modifier Modifier applied to the [Surface] root.
 * @param onClick Called when the row is tapped. `null` makes the row
 *   non-interactive.
 * @param onLongClick Called when the row is long-pressed. `null`
 *   disables long-press.
 * @param enabled When `false`, the row ignores pointer input and is
 *   visually dimmed.
 * @param containerColor Background color of the row. Defaults to
 *   [MaterialTheme.colorScheme.surface].
 * @param contentColor Default content color of the row. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 * @param shape Corner shape of the row. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param tonalElevation Tonal elevation of the row surface. Defaults
 *   to 0.dp.
 * @param headlineMaxLines Maximum lines for [headline].
 * @param headlineOverflow Overflow strategy for [headline].
 * @param headlineStyle Text style for [headline]. Defaults to
 *   [MaterialTheme.typography.titleMedium].
 * @param supportingTextMaxLines Maximum lines for [supportingText].
 * @param supportingTextOverflow Overflow strategy for [supportingText].
 * @param supportingTextStyle Text style for [supportingText]. Defaults
 *   to [MaterialTheme.typography.bodyMedium].
 * @param leadingContent Optional content before the text block (e.g. an
 *   icon or a [com.segnities007.stylishui.components.atoms.StylishAvatar]).
 * @param trailingContent Optional content after the text block (e.g. a
 *   chevron or a badge).
 * @param content When non-null, replaces the entire leading/text/
 *   trailing layout with caller-supplied content, which fills the row
 *   with [Modifier.weight] of 1f.
 *
 * @see com.segnities007.stylishui.components.molecules.StylishConnectedListItemRow
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun StylishListItem(
    headline: String,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    tonalElevation: Dp = 0.dp,
    headlineMaxLines: Int = Int.MAX_VALUE,
    headlineOverflow: TextOverflow = TextOverflow.Ellipsis,
    headlineStyle: TextStyle = MaterialTheme.typography.titleMedium,
    supportingTextMaxLines: Int = Int.MAX_VALUE,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val actionable = isActionable(
        enabled = enabled,
        hasClickAction = onClick != null,
        hasLongClickAction = onLongClick != null,
    )
    val resolvedInteractionSource = rememberStylishInteractionSource()
    Surface(
        modifier = modifier
            .stylishTestTag("list_item")
            .heightIn(min = 56.dp)
            .then(
                if (actionable) {
                    Modifier
                        .combinedClickable(
                            interactionSource = resolvedInteractionSource,
                            indication = null,
                            onClick = onClick ?: {},
                            onLongClick = onLongClick?.let {
                                {
                                    haptic.performHapticFeedback(
                                        HapticFeedbackType.LongPress,
                                    )
                                    it()
                                }
                            },
                        )
                        .semantics { role = Role.Button }
                } else {
                    Modifier
                },
            )
            .semantics {
                if (!enabled) disabled()
            },
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = stylishInteractiveElevation(resolvedInteractionSource, actionable),
    ) {
        val innerModifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = StylishTheme.dimensions.controlPadding,
                vertical = StylishTheme.dimensions.controlVerticalPadding,
            )
            .then(if (!enabled) Modifier.alpha(0.38f) else Modifier)
        if (content != null) {
            Box(innerModifier) {
                content()
            }
        } else {
            Row(
                modifier = innerModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
            ) {
                leadingContent?.invoke()
                Column(Modifier.weight(1f)) {
                    Text(
                        headline,
                        style = headlineStyle,
                        maxLines = headlineMaxLines,
                        overflow = headlineOverflow,
                    )
                    supportingText?.let {
                        Text(
                            it,
                            style = supportingTextStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = supportingTextMaxLines,
                            overflow = supportingTextOverflow,
                        )
                    }
                }
                trailingContent?.invoke()
            }
        }
    }
}

@Preview(name = "Stylish list item", showBackground = true, widthDp = 393)
@Composable
private fun StylishListItemPreview() {
    StylishTheme(darkTheme = false) {
        Column(Modifier.padding(20.dp)) {
            StylishListItem(
                headline = "テーマ",
                supportingText = "アプリ全体の外観を変更",
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                trailingContent = {
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                },
                onClick = {},
            )
        }
    }
}

@Preview(name = "Stylish list item disabled", showBackground = true, widthDp = 393)
@Composable
private fun StylishListItemDisabledPreview() {
    StylishTheme(darkTheme = false) {
        Column(Modifier.padding(20.dp)) {
            StylishListItem(
                headline = "通知",
                supportingText = "この項目は利用できません",
                leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                onClick = {},
                enabled = false,
            )
        }
    }
}

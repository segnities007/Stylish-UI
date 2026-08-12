package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A connected card that shares edges and corner radii with adjacent cards,
 * forming a visually continuous group. Supports tap and long-press
 * interactions; long-press triggers haptic feedback. When neither [onClick]
 * nor [onLongClick] is provided, the card renders flat (zero elevation)
 * and ignores pointer input.
 *
 * Use [StylishConnectedCardRow], [StylishConnectedCardColumn], or
 * [StylishConnectedCardGrid] to lay out multiple connected cards —
 * those layouts compute [outlineEdges] and [outlineCorners] automatically.
 *
 * ## Two rendering modes
 *
 * **Structured mode** (default) — when [content] is `null`, the card
 * renders a fixed three-slot row: [leadingContent] | title column
 * ([title] + [supportingText]) | [trailingContent]. The title column
 * occupies the remaining width via `weight(1f)`.
 *
 * **Content mode** — when [content] is non-null, it completely replaces
 * the structured row. The caller has full control over the card's inner
 * layout; [title], [supportingText], [leadingContent], [trailingContent],
 * and their style parameters ([titleStyle], [titleMaxLines],
 * [titleOverflow], [supportingTextStyle], [supportingTextMaxLines],
 * [supportingTextOverflow], [contentSpacing], [titleSpacing]) are
 * ignored. Container-level parameters ([minHeight], [horizontalPadding],
 * [verticalPadding], [shape], [containerColor], [contentColor],
 * [disabledContainerColor], [disabledContentColor]) still apply in
 * both modes.
 *
 * @param title Primary text displayed in the card body in structured
 *   mode. Ignored in content mode. Defaults to `""`.
 * @param supportingText Secondary text below the title in structured
 *   mode. Omitted when blank. Ignored in content mode.
 * @param modifier Modifier applied to the card root.
 * @param shape Shape of the card surface. Defaults to
 *   [connectedShape] with [ConnectedCorners.Standalone].
 * @param outlineEdges Which edges of the hairline outline to draw.
 *   Defaults to [ConnectedEdges.All].
 * @param outlineCorners Which corners use the large (outer) radius.
 *   Defaults to [ConnectedCorners.Standalone].
 * @param containerColor Background color. Defaults to
 *   [stylishComponentColors.groupedContainer]. Ignored when
 *   [enabled] is `false`.
 * @param contentColor Default content color. Defaults to
 *   `MaterialTheme.colorScheme.onSurface`. Ignored when [enabled]
 *   is `false`.
 * @param disabledContainerColor Background color used when [enabled]
 *   is `false`, visually dimming the card. Defaults to
 *   `MaterialTheme.colorScheme.surfaceVariant`.
 * @param disabledContentColor Content color used when [enabled] is
 *   `false`, visually dimming the card. Defaults to
 *   `MaterialTheme.colorScheme.onSurfaceVariant`.
 * @param titleStyle Text style for [title] in structured mode.
 * @param supportingTextStyle Text style for [supportingText] in
 *   structured mode.
 * @param supportingTextColor Color for [supportingText] in structured
 *   mode. Defaults to `MaterialTheme.colorScheme.onSurfaceVariant`.
 * @param minHeight Minimum height of the card body. Defaults to 77.dp.
 *   Applies in both modes.
 * @param horizontalPadding Horizontal padding inside the card.
 *   Defaults to 16.dp. Applies in both modes.
 * @param verticalPadding Vertical padding inside the card.
 *   Defaults to 12.dp. Applies in both modes.
 * @param contentSpacing Horizontal gap between slots in structured
 *   mode. Ignored in content mode.
 * @param titleSpacing Vertical gap between [title] and [supportingText]
 *   in structured mode. Ignored in content mode.
 * @param enabled When `false`, the card ignores pointer input, renders
 *   at zero elevation regardless of [onClick], is announced as disabled
 *   via semantics, and is visually dimmed with [disabledContainerColor]
 *   and [disabledContentColor].
 * @param onClick Called when the card is tapped. `null` (default) makes
 *   the card display-only.
 * @param onLongClick Called when the card is long-pressed. Triggers
 *   [HapticFeedbackType.LongPress] before invocation.
 * @param titleMaxLines Maximum lines for [title] in structured mode.
 * @param titleOverflow Overflow strategy for [title] in structured mode.
 * @param supportingTextMaxLines Maximum lines for [supportingText] in
 *   structured mode.
 * @param supportingTextOverflow Overflow strategy for [supportingText]
 *   in structured mode.
 * @param variant The visual style of the card (see [StylishCardVariant]).
 *   Defaults to [StylishCardVariant.Elevated], preserving the classic
 *   Stylish look.
 * @param border Border stroke drawn around the card, in addition to the
 *   connected outline. When `null` (default), resolved from [variant]:
 *   a hairline of [StylishTheme.dimensions.outlineWidth] using
 *   `MaterialTheme.colorScheme.outlineVariant` for
 *   [StylishCardVariant.Outlined], and no border otherwise. Pass an
 *   explicit [BorderStroke] to override.
 * @param interactionSource The [MutableInteractionSource] for the
 *   card, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param leadingContent Content before the title column in structured
 *   mode. Ignored in content mode.
 * @param trailingContent Content after the title column in structured
 *   mode. Ignored in content mode.
 * @param content When non-null, replaces the entire structured row with
 *   caller-supplied content, enabling fully custom card layouts. The
 *   content is placed inside a [Box] constrained by [minHeight],
 *   [horizontalPadding], and [verticalPadding]. When `null` (default),
 *   the structured three-slot row is rendered.
 *
 * @see StylishConnectedCardRow
 * @see StylishConnectedCardColumn
 * @see StylishConnectedCardGrid
 * @see connectedShape
 * @see connectedOutline
 * @see StylishCardVariant
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun StylishConnectedCard(
    title: String = "",
    supportingText: String = "",
    modifier: Modifier = Modifier,
    shape: Shape = connectedShape(ConnectedCorners.Standalone),
    outlineEdges: ConnectedEdges = ConnectedEdges.All,
    outlineCorners: ConnectedCorners = ConnectedCorners.Standalone,
    containerColor: Color? = null,
    contentColor: Color? = null,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    supportingTextColor: Color? = null,
    minHeight: Dp = 77.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 12.dp,
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    titleSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextMaxLines: Int = 1,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    variant: StylishCardVariant = StylishCardVariant.Elevated,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
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
    val resolvedBorder = border ?: if (variant == StylishCardVariant.Outlined) {
        BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        )
    } else {
        null
    }
    val resolvedElevation = if (variant == StylishCardVariant.Elevated) {
        if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp
    } else {
        0.dp
    }
    val resolvedContainerColor = if (!enabled) {
        disabledContainerColor
    } else {
        containerColor ?: MaterialTheme.stylishComponentColors.groupedContainer
    }
    val resolvedContentColor = if (!enabled) {
        disabledContentColor
    } else {
        contentColor ?: MaterialTheme.colorScheme.onSurface
    }
    Card(
        modifier = modifier
            .connectedOutline(outlineEdges, outlineCorners)
            .semantics {
                if (!enabled) disabled()
            }
            .then(
                if (actionable) {
                    Modifier
                        .semantics(mergeDescendants = true) { role = Role.Button }
                        .combinedClickable(
                            interactionSource = interactionSource,
                            indication = LocalIndication.current,
                            onClick = { onClick?.invoke() },
                            onLongClick = onLongClick?.let {
                                {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    it()
                                }
                            },
                        )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = resolvedContainerColor,
            contentColor = resolvedContentColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = resolvedElevation,
        ),
        border = resolvedBorder,
    ) {
        if (content != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = minHeight)
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            ) {
                content()
            }
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = minHeight)
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(contentSpacing),
            ) {
                leadingContent?.invoke()
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(titleSpacing, Alignment.CenterVertically),
                ) {
                    Text(
                        title,
                        style = titleStyle,
                        maxLines = titleMaxLines,
                        overflow = titleOverflow,
                    )
                    if (supportingText.isNotBlank()) {
                        Text(
                            supportingText,
                            style = supportingTextStyle,
                            color = supportingTextColor ?: MaterialTheme.colorScheme.onSurfaceVariant,
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

@Preview(name = "Stylish connected card", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCard(
                title = "項目のタイトル",
                supportingText = "補足テキスト",
                onClick = {},
                onLongClick = {},
                trailingContent = { Text("詳細", style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}

@Preview(name = "Stylish connected card content mode", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardContentModePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCard(
                onClick = {},
                minHeight = 120.dp,
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(
                        StylishTheme.dimensions.inlineSpacing,
                    ),
                ) {
                    Text("カスタムタイトル", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "自由にレイアウトできるコンテンツモード",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

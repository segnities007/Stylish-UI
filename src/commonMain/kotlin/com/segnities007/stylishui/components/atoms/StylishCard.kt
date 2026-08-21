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
import androidx.compose.ui.platform.testTag
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
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * The visual style of a [StylishCard] (and the connected card family).
 *
 * [Filled] renders flat with no elevation, [Elevated] lifts the card
 * with the interactive elevation, and [Outlined] renders flat with a
 * hairline border.
 *
 * @see StylishCard
 * @see StylishConnectedCard
 */
public enum class StylishCardVariant {
    /** Flat card with no elevation and no border. */
    Filled,

    /** Card lifted with the interactive elevation (the classic Stylish look). */
    Elevated,

    /** Flat card outlined with a hairline border. */
    Outlined,
}

/**
 * A standalone card for presenting a single piece of grouped content.
 * Supports tap and long-press interactions; long-press triggers haptic
 * feedback. When neither [onClick] nor [onLongClick] is provided, the
 * card renders flat (zero elevation) and ignores pointer input.
 *
 * This is the standalone card — it carries no connected-group geometry.
 * Use [StylishConnectedCard], [StylishConnectedCardRow],
 * [StylishConnectedCardColumn], or [StylishConnectedCardGrid] when
 * multiple cards should share edges and corner radii as a visually
 * continuous group.
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
 * [verticalPadding], [shape], [containerColor], [contentColor]) still
 * apply in both modes.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_card` for UI tests.
 * Callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
 *
 * @param modifier Modifier applied to the [Card] root.
 * @param onClick Called when the card is tapped. `null` (default) makes
 *   the card display-only.
 * @param onLongClick Called when the card is long-pressed. Triggers
 *   [HapticFeedbackType.LongPress] before invocation.
 * @param enabled When `false`, the card ignores pointer input, renders
 *   at zero elevation regardless of [onClick], and is announced as
 *   disabled via semantics.
 * @param variant The visual style of the card (see [StylishCardVariant]).
 *   Defaults to [StylishCardVariant.Elevated], preserving the classic
 *   Stylish look.
 * @param shape Shape of the card surface. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param containerColor Background color. Defaults to
 *   [stylishComponentColors.groupedContainer].
 * @param contentColor Default content color. Defaults to
 *   `MaterialTheme.colorScheme.onSurface`.
 * @param minHeight Minimum height of the card body. Defaults to the theme's card size token.
 *   Applies in both modes.
 * @param horizontalPadding Horizontal padding inside the card.
 *   Defaults to the theme's content padding. Applies in both modes.
 * @param verticalPadding Vertical padding inside the card.
 *   Defaults to the theme's control vertical padding. Applies in both modes.
 * @param title Primary text displayed in the card body in structured
 *   mode. Ignored in content mode. Defaults to `""`.
 * @param supportingText Secondary text below the title in structured
 *   mode. Omitted when blank. Ignored in content mode.
 * @param titleStyle Text style for [title] in structured mode.
 * @param titleMaxLines Maximum lines for [title] in structured mode.
 * @param titleOverflow Overflow strategy for [title] in structured mode.
 * @param supportingTextStyle Text style for [supportingText] in
 *   structured mode.
 * @param supportingTextMaxLines Maximum lines for [supportingText] in
 *   structured mode.
 * @param supportingTextOverflow Overflow strategy for [supportingText]
 *   in structured mode.
 * @param contentSpacing Horizontal gap between slots in structured
 *   mode. Ignored in content mode.
 * @param titleSpacing Vertical gap between [title] and [supportingText]
 *   in structured mode. Ignored in content mode.
 * @param border Border stroke drawn around the card. When `null`
 *   (default), resolved from [variant]: a hairline of
 *   [StylishTheme.dimensions.outlineWidth] using
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
 * @see StylishConnectedCard
 * @see StylishConnectedCardRow
 * @see StylishConnectedCardColumn
 * @see StylishConnectedCardGrid
 * @see StylishCardVariant
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun StylishCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = StylishCardDefaults.shape(),
    containerColor: Color? = null,
    contentColor: Color? = null,
    minHeight: Dp = StylishTheme.dimensions.cardMinHeight,
    horizontalPadding: Dp = StylishTheme.dimensions.contentPadding,
    verticalPadding: Dp = StylishTheme.dimensions.controlVerticalPadding,
    title: String = "",
    supportingText: String = "",
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    supportingTextMaxLines: Int = 1,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    titleSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
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
    val resolvedInteractionSource = rememberStylishInteractionSource(interactionSource)
    val resolvedBorder = border ?: StylishCardDefaults.border(variant)
    val resolvedElevation = if (variant == StylishCardVariant.Elevated) {
        stylishInteractiveElevation(resolvedInteractionSource, actionable)
    } else {
        StylishCardDefaults.elevation(variant, actionable)
    }
    Card(
        modifier = modifier
            .testTag("stylish_card")
            .semantics {
                if (!enabled) disabled()
            }
            .then(
                if (actionable) {
                    Modifier
                        .semantics(mergeDescendants = true) { role = Role.Button }
                        .combinedClickable(
                            interactionSource = resolvedInteractionSource,
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
        colors = if (containerColor == null && contentColor == null) {
            StylishCardDefaults.colors()
        } else CardDefaults.cardColors(
            containerColor = containerColor ?: MaterialTheme.stylishComponentColors.groupedContainer,
            contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
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

@Preview(name = "Stylish card", showBackground = true, widthDp = 393)
@Composable
private fun StylishCardPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishCard(
                title = "項目のタイトル",
                supportingText = "補足テキスト",
                onClick = {},
                onLongClick = {},
                trailingContent = { Text("詳細", style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}

@Preview(name = "Stylish card content mode", showBackground = true, widthDp = 393)
@Composable
private fun StylishCardContentModePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishCard(
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

@Preview(name = "Stylish card variants", showBackground = true, widthDp = 393)
@Composable
private fun StylishCardVariantsPreview() {
    StylishTheme(darkTheme = false) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StylishCard(title = "Filled", variant = StylishCardVariant.Filled)
            StylishCard(title = "Elevated", variant = StylishCardVariant.Elevated)
            StylishCard(title = "Outlined", variant = StylishCardVariant.Outlined)
        }
    }
}

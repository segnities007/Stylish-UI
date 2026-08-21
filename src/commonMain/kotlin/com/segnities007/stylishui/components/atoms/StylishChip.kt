package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.stylishFocusRing
import com.segnities007.stylishui.foundation.stylishStateLayer
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishInteractiveTarget
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * The visual role of a [StylishChip].
 *
 * Mirrors the Material 3 chip family: [Assist] for smart, contextual
 * actions, [Filter] for filtering content (renders a checkmark when
 * selected), [Input] for user-entered data, and [Suggestion] for
 * suggested content.
 *
 * @see StylishChip
 */
public enum class StylishChipVariant {
    /** Assist chip: smart or automated action (e.g. "open calendar event"). */
    Assist,

    /** Filter chip: marks selection with an animated checkmark when selected. */
    Filter,

    /** Input chip: user-entered data, typically with a close affordance. */
    Input,

    /** Suggestion chip: recommended content such as a search suggestion. */
    Suggestion,
}

/**
 * A standalone Stylish chip with the same visual language as the
 * connected-chip family — animated selection colors, `Role.Tab`
 * semantics, interactive elevation, and a haptic pulse on tap — but
 * without any connected-group geometry. Use this when a single chip
 * is needed outside a [StylishConnectedChipRow],
 * [StylishConnectedChipColumn], or [StylishConnectedChipGrid].
 *
 * For the [StylishChipVariant.Filter] variant, a checkmark is rendered
 * automatically before the label when [selected] is `true` and no
 * [leadingContent] is provided. For the [StylishChipVariant.Input]
 * variant, the container defaults to `surfaceVariant` and a trailing
 * close affordance can be supplied via [trailingContent].
 *
 * When [onClick] is `null` the chip is display-only: no `Role.Tab`
 * semantics, no ripple, and no elevation lift. When [enabled] is
 * `false`, the chip is announced as disabled via semantics. When
 * actionable, a Material-style state layer (see
 * [Modifier.stylishStateLayer]) darkens the surface on hover and press,
 * and a primary-colored focus ring (see [Modifier.stylishFocusRing]) is
 * drawn around the chip while it holds keyboard focus.
 *
 * The selected/unselected color transition animates with
 * [StylishTheme.animation.durationShort]; when the platform requests
 * reduced motion (see
 * [isStylishReducedMotionEnabled]) the color snaps to its target
 * instead of tweening.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_chip` for UI tests.
 * Callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
 *
 * @param label Text displayed on the chip in structured mode. Ignored
 *   in content mode. Defaults to `""`.
 * @param onClick Called when the chip is tapped. `null` makes the chip
 *   display-only.
 * @param modifier Modifier applied to the [Surface] root.
 * @param variant The visual role of the chip (see [StylishChipVariant]).
 * @param selected When `true`, the chip renders in the selected color
 *   scheme. For the [StylishChipVariant.Filter] variant, also renders
 *   an automatic checkmark when [leadingContent] is `null`.
 * @param enabled When `false`, the chip ignores pointer input, renders
 *   at zero elevation, is announced as disabled, and is visually
 *   dimmed with [disabledContainerColor] and [disabledContentColor]
 *   regardless of [selected].
 * @param interactionSource The [MutableInteractionSource] for the
 *   chip, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param shape Corner shape. Defaults to [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param labelMaxLines Maximum lines for [label] in structured mode.
 * @param labelOverflow Overflow strategy for [label] in structured mode.
 * @param labelStyle Text style for [label] in structured mode.
 * @param selectedContainerColor Background color when selected.
 * @param selectedContentColor Content color when selected.
 * @param unselectedContainerColor Background color when unselected.
 *   When `null` (default), resolved from [variant]:
 *   `surfaceVariant` for [StylishChipVariant.Input] and
 *   [stylishComponentColors.groupedContainer] otherwise.
 * @param unselectedContentColor Content color when unselected.
 * @param disabledContainerColor Background color used when [enabled]
 *   is `false`, visually dimming the chip. Defaults to
 *   `MaterialTheme.colorScheme.surfaceVariant`.
 * @param disabledContentColor Content color used when [enabled] is
 *   `false`, visually dimming the chip. Defaults to
 *   `MaterialTheme.colorScheme.onSurfaceVariant`.
 * @param contentPadding Inner padding of the chip.
 * @param contentSpacing Gap between leading slot, label, and trailing
 *   slot in structured mode. Defaults to
 *   [StylishTheme.dimensions.inlineSpacing].
 * @param leadingContent Optional content before the label in structured
 *   mode. Ignored in content mode. For the filter variant, a provided
 *   leading content replaces the automatic checkmark.
 * @param trailingContent Optional content after the label in structured
 *   mode. Ignored in content mode. Use this for the
 *   [StylishChipVariant.Input] close affordance.
 * @param content When non-null, replaces the entire structured row
 *   (leading + label + trailing) with caller-supplied content. Receives
 *   [RowScope] for weight/alignment control. When `null` (default), the
 *   structured row is rendered.
 *
 * @see StylishConnectedChipRow
 * @see StylishConnectedChipColumn
 * @see StylishConnectedChipGrid
 * @see StylishChipVariant
 */
@Composable
public fun StylishChip(
    label: String = "",
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    variant: StylishChipVariant = StylishChipVariant.Assist,
    selected: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color? = null,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val actionable = isActionable(enabled = enabled, hasClickAction = onClick != null)
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val resolvedUnselectedContainerColor = unselectedContainerColor ?: when (variant) {
        StylishChipVariant.Input -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.stylishComponentColors.groupedContainer
    }
    val reducedMotion = isStylishReducedMotionEnabled()
    val colorAnimationSpec: AnimationSpec<Color> = if (reducedMotion) {
        snap()
    } else {
        tween(
            durationMillis = StylishTheme.animation.durationShort,
            easing = StylishTheme.animation.defaultEasing,
        )
    }
    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> disabledContainerColor
            selected -> selectedContainerColor
            else -> resolvedUnselectedContainerColor
        },
        animationSpec = colorAnimationSpec,
        label = "chipContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> disabledContentColor
            selected -> selectedContentColor
            else -> unselectedContentColor
        },
        animationSpec = colorAnimationSpec,
        label = "chipContent",
    )
    val resolvedLeadingContent: (@Composable RowScope.() -> Unit)? = when {
        variant == StylishChipVariant.Filter && selected && leadingContent == null -> {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = contentColor,
                )
            }
        }
        else -> leadingContent
    }
    Surface(
        modifier = modifier
            .stylishInteractiveTarget()
            .testTag("stylish_chip")
            .semantics {
                this.selected = selected
                if (onClick != null) {
                    role = Role.Tab
                }
            }
            .then(
                if (!enabled) {
                    Modifier.semantics { disabled() }
                } else {
                    Modifier
                },
            )
            .then(
                if (actionable) {
                    Modifier.clickable(
                        interactionSource = resolvedInteractionSource,
                        indication = LocalIndication.current,
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick?.invoke()
                    }
                } else {
                    Modifier
                },
            )
            .then(
                if (actionable) {
                    Modifier.stylishStateLayer(
                        interactionSource = resolvedInteractionSource,
                        shape = shape,
                    )
                } else {
                    Modifier
                },
            )
            .then(
                if (actionable) {
                    Modifier.stylishFocusRing(
                        interactionSource = resolvedInteractionSource,
                        shape = shape,
                    )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        Row(
            Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(contentSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (content != null) {
                content(this)
            } else {
                resolvedLeadingContent?.invoke(this)
                Text(
                    label,
                    style = labelStyle,
                    maxLines = labelMaxLines,
                    overflow = labelOverflow,
                )
                trailingContent?.invoke(this)
            }
        }
    }
}

@Preview(name = "Stylish chip unselected", showBackground = true, widthDp = 393)
@Composable
private fun StylishChipPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishChip(label = "カテゴリ", onClick = {})
        }
    }
}

@Preview(name = "Stylish chip selected", showBackground = true, widthDp = 393)
@Composable
private fun StylishChipSelectedPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishChip(label = "選択中", onClick = {}, selected = true)
        }
    }
}

@Preview(name = "Stylish chip filter selected", showBackground = true, widthDp = 393)
@Composable
private fun StylishChipFilterSelectedPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishChip(
                label = "すべて",
                onClick = {},
                variant = StylishChipVariant.Filter,
                selected = true,
            )
        }
    }
}

@Preview(name = "Stylish chip input", showBackground = true, widthDp = 393)
@Composable
private fun StylishChipInputPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishChip(
                label = "メール",
                onClick = {},
                variant = StylishChipVariant.Input,
                trailingContent = {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                },
            )
        }
    }
}

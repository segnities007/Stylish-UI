package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A standalone Stylish chip with the same visual language as the
 * connected-chip family — animated selection colors, `Role.Tab`
 * semantics, interactive elevation, and a haptic pulse on tap — but
 * without any connected-group geometry. Use this when a single chip
 * is needed outside a [StylishConnectedChipRow],
 * [StylishConnectedChipColumn], or [StylishConnectedChipGrid].
 *
 * @param label Text displayed on the chip.
 * @param onClick Called when the chip is tapped. `null` makes the chip
 *   display-only.
 * @param modifier Modifier applied to the [Surface] root.
 * @param selected When `true`, the chip renders in the selected color
 *   scheme.
 * @param enabled When `false`, the chip ignores pointer input and
 *   renders at zero elevation.
 * @param shape Corner shape. Defaults to [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param labelMaxLines Maximum lines for [label]. Defaults to 1.
 * @param labelOverflow Overflow strategy for [label].
 * @param labelStyle Text style for [label].
 * @param selectedContainerColor Background color when selected.
 * @param selectedContentColor Content color when selected.
 * @param unselectedContainerColor Background color when unselected.
 * @param unselectedContentColor Content color when unselected.
 * @param contentPadding Inner padding of the chip.
 * @param contentSpacing Gap between leading slot, label, and trailing
 *   slot. Defaults to [StylishTheme.dimensions.inlineSpacing].
 * @param leadingContent Optional content before the label.
 * @param trailingContent Optional content after the label.
 *
 * @see StylishConnectedChipRow
 * @see StylishConnectedChipColumn
 * @see StylishConnectedChipGrid
 */
@Composable
public fun StylishChip(
    label: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color = MaterialTheme.stylishComponentColors.groupedContainer,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    contentSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val actionable = isActionable(enabled = enabled, hasClickAction = onClick != null)
    val containerColor by animateColorAsState(
        targetValue = if (selected) selectedContainerColor else unselectedContainerColor,
        animationSpec = tween(180),
        label = "chipContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) selectedContentColor else unselectedContentColor,
        animationSpec = tween(180),
        label = "chipContent",
    )
    Surface(
        modifier = modifier
            .semantics {
                this.selected = selected
                role = Role.Tab
            }
            .then(
                if (actionable) {
                    Modifier.clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick?.invoke()
                    }
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
            leadingContent?.invoke(this)
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
package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.structure.ConnectedToggleRow
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A horizontally connected row of toggle buttons for mutually exclusive
 * selection among a small set of string-labeled options.
 *
 * This is the Finish-layer component: it supplies the Stylish toggle rendering
 * to the headless Structure layout [ConnectedToggleRow], which owns arrangement
 * and connection geometry. Each item occupies an equal weight within the row
 * and stretches to the tallest sibling. Outline edges and corner radii are
 * computed automatically from each item's index. The selected item is
 * highlighted with [selectedContainerColor] and [selectedContentColor];
 * unselected items use [unselectedContainerColor] and [unselectedContentColor].
 * Selection changes trigger haptic feedback and smooth color animation.
 *
 * Use this component for day-of-week pickers, view-mode switches, or any
 * inline multi-option selection where exactly one option is active at a time.
 *
 * @param items The list of string labels for each toggle segment.
 * @param selectedIndex The index of the currently selected item, or `null` if
 *   no item is selected.
 * @param onSelectedChange Callback invoked with the index of the tapped item.
 * @param modifier The modifier applied to the root layout.
 * @param cornerRadius The radius applied to the outer corners of the first and
 *   last items. Defaults to
 *   [StylishTheme.dimensions.connectedCornerRadius] (12 dp).
 * @param spacing The horizontal gap between adjacent items. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp).
 * @param contentPadding The inner padding of each toggle button. Defaults to
 *   12 dp horizontal and 12 dp vertical.
 * @param selectedContainerColor The container color for the selected item.
 *   Defaults to [MaterialTheme.colorScheme.primaryContainer].
 * @param selectedContentColor The content color for the selected item.
 *   Defaults to [MaterialTheme.colorScheme.onPrimaryContainer].
 * @param unselectedContainerColor The container color for unselected items.
 *   Defaults to [MaterialTheme.stylishComponentColors.groupedContainer].
 * @param unselectedContentColor The content color for unselected items.
 *   Defaults to [MaterialTheme.colorScheme.onSurface].
 *
 * @see ConnectedToggleRow
 */
@Composable
public fun StylishConnectedToggleRow(
    items: List<String>,
    selectedIndex: Int?,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StylishTheme.dimensions.connectedCornerRadius,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
    selectedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    unselectedContainerColor: Color = MaterialTheme.stylishComponentColors.groupedContainer,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val haptic = LocalHapticFeedback.current
    ConnectedToggleRow(
        items = items,
        selectedIndex = selectedIndex,
        onSelectedChange = onSelectedChange,
        modifier = modifier,
        cornerRadius = cornerRadius,
        spacing = spacing,
    ) { item, index, isSelected, itemModifier, shape, _, _ ->
        val containerColor by animateColorAsState(
            targetValue = if (isSelected) selectedContainerColor else unselectedContainerColor,
            animationSpec = tween(durationMillis = StylishTheme.animation.durationShort),
            label = "toggleContainer",
        )
        val contentColor by animateColorAsState(
            targetValue = if (isSelected) selectedContentColor else unselectedContentColor,
            animationSpec = tween(durationMillis = StylishTheme.animation.durationShort),
            label = "toggleContent",
        )
        Surface(
            selected = isSelected,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onSelectedChange(index)
            },
            modifier = itemModifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = StylishTheme.dimensions.interactiveElevation,
            shadowElevation = 0.dp,
        ) {
            Box(
                modifier = Modifier.padding(contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(item)
            }
        }
    }
}

@Preview(name = "Connected toggle row", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedToggleRowPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedToggleRow(
                items = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                selectedIndex = 2,
                onSelectedChange = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Connected toggle row unselected", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedToggleRowUnselectedPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedToggleRow(
                items = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
                selectedIndex = null,
                onSelectedChange = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

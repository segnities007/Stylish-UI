package com.segnities007.stylishui.components.organisms

import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A connected segmented control for mutually exclusive selection among a
 * small set of options.
 *
 * Renders a horizontally joined row of segment buttons where exactly one
 * segment is visually highlighted at a time. Use this when the user must
 * pick one mode or view from two-to-five alternatives (e.g. list vs. grid).
 * Internally composes [StylishConnectedButtonRow] and maps each
 * [StylishSegmentedOption] to a [StylishConnectedButtonItem].
 *
 * @param options The selectable segments. Each [StylishSegmentedOption]
 *   carries a [value], a display [label], and an optional [leadingContent]
 *   icon slot rendered before the label.
 * @param selectedValue The currently selected value. The segment whose
 *   [StylishSegmentedOption.value] equals this is rendered with
 *   [selectedColors].
 * @param onSelected Callback invoked with the tapped segment's value.
 * @param labelMaxLines Maximum lines for each segment label. Defaults to 1.
 * @param labelOverflow [TextOverflow] strategy when a label exceeds
 *   [labelMaxLines]. Defaults to [TextOverflow.Ellipsis].
 * @param selectedColors [ButtonColors] applied to the active segment.
 *   Defaults to [MaterialTheme.colorScheme.primary] container with
 *   [MaterialTheme.colorScheme.onPrimary] content.
 * @param defaultColors [ButtonColors] applied to inactive segments.
 *   Defaults to [MaterialTheme.stylishComponentColors.groupedContainer]
 *   container with [MaterialTheme.colorScheme.onSurface] content.
 * @param spacing Horizontal gap between joined segments. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (typically 3.dp).
 *
 * @see StylishConnectedButtonRow
 * @see StylishSegmentedOption
 */
@Composable
public fun <T> StylishConnectedSegmentedControl(
    options: List<StylishSegmentedOption<T>>,
    selectedValue: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    selectedColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
    defaultColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
) {
    StylishConnectedButtonRow(
        items = options.map { option ->
            StylishConnectedButtonItem(
                onClick = { onSelected(option.value) },
                colors = selectedColors.takeIf { option.value == selectedValue },
                leadingContent = option.leadingContent,
            ) {
                Text(
                    option.label,
                    maxLines = labelMaxLines,
                    overflow = labelOverflow,
                )
            }
        },
        modifier = modifier,
        spacing = spacing,
        defaultColors = defaultColors,
    )
}

@Preview(name = "Connected segmented control", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedSegmentedControlPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedSegmentedControl(
                options = listOf(
                    StylishSegmentedOption("list", "リスト") {
                        Icon(Icons.AutoMirrored.Filled.ViewList, null)
                    },
                    StylishSegmentedOption("grid", "グリッド") {
                        Icon(Icons.Default.GridView, null)
                    },
                ),
                selectedValue = "list",
                onSelected = {},
            )
        }
    }
}

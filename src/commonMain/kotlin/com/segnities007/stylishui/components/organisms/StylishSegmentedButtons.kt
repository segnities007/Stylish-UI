package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.MultiChoiceSegmentedButtonRowScope
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A row of mutually exclusive segmented buttons, wrapping the Material
 * 3 [SingleChoiceSegmentedButtonRow] with Stylish defaults.
 *
 * Use this to pick exactly one option from a small set. Place
 * [StylishSegmentedButton] instances inside [content]; the row
 * overlaps their borders so the group reads as one connected control.
 *
 * @param modifier Modifier applied to the [SingleChoiceSegmentedButtonRow]
 *   root.
 * @param space The overlap between adjacent buttons. Defaults to
 *   [SegmentedButtonDefaults.BorderWidth], matching the stroke width.
 * @param content The buttons, typically a sequence of
 *   [StylishSegmentedButton] in the
 *   [SingleChoiceSegmentedButtonRowScope].
 *
 * @see StylishMultiChoiceSegmentedButtonRow
 * @see StylishSegmentedButton
 */
@Composable
public fun StylishSingleChoiceSegmentedButtonRow(
    modifier: Modifier = Modifier,
    space: Dp = SegmentedButtonDefaults.BorderWidth,
    content: @Composable SingleChoiceSegmentedButtonRowScope.() -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
        space = space,
        content = content,
    )
}

/**
 * A row of multi-select segmented buttons, wrapping the Material 3
 * [MultiChoiceSegmentedButtonRow] with Stylish defaults.
 *
 * Use this when several options can be selected at once. Place
 * [StylishSegmentedButton] instances inside [content]; the row
 * overlaps their borders so the group reads as one connected control.
 *
 * @param modifier Modifier applied to the [MultiChoiceSegmentedButtonRow]
 *   root.
 * @param space The overlap between adjacent buttons. Defaults to
 *   [SegmentedButtonDefaults.BorderWidth], matching the stroke width.
 * @param content The buttons, typically a sequence of
 *   [StylishSegmentedButton] in the
 *   [MultiChoiceSegmentedButtonRowScope].
 *
 * @see StylishSingleChoiceSegmentedButtonRow
 * @see StylishSegmentedButton
 */
@Composable
public fun StylishMultiChoiceSegmentedButtonRow(
    modifier: Modifier = Modifier,
    space: Dp = SegmentedButtonDefaults.BorderWidth,
    content: @Composable MultiChoiceSegmentedButtonRowScope.() -> Unit,
) {
    MultiChoiceSegmentedButtonRow(
        modifier = modifier,
        space = space,
        content = content,
    )
}

/**
 * A single mutually exclusive option inside
 * [StylishSingleChoiceSegmentedButtonRow], wrapping the Material 3
 * single-choice [androidx.compose.material3.SegmentedButton].
 *
 * The default [shape] renders the segment as a plain rectangle; for
 * joined rows pass
 * `SegmentedButtonDefaults.itemShape(index = i, count = n)` so the
 * outer corners round and the inner corners notch.
 *
 * @param selected Whether this button is selected.
 * @param onClick Called when the button is tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in its disabled colors. Defaults to `true`.
 * @param shape The shape of this button. Defaults to
 *   [SegmentedButtonDefaults.baseShape]; use
 *   [SegmentedButtonDefaults.itemShape] for multi-segment rows.
 * @param colors [SegmentedButtonColors] resolving the button colors
 *   per state. Defaults to [SegmentedButtonDefaults.colors].
 * @param border The [BorderStroke] around the button. Defaults to the
 *   M3 outline style: a [SegmentedButtonDefaults.BorderWidth] stroke
 *   colored by the active/inactive border color for the current
 *   [selected] and [enabled] state.
 * @param contentPadding Internal spacing between the container and
 *   the content. Defaults to [SegmentedButtonDefaults.ContentPadding].
 * @param interactionSource Optional hoisted
 *   [MutableInteractionSource] for observing interactions. When
 *   `null`, one is remembered internally.
 * @param icon The icon slot, shown before the label. Defaults to the
 *   M3 check/blank icon for the [selected] state.
 * @param label The button content, typically a [Text].
 *
 * @see StylishSingleChoiceSegmentedButtonRow
 * @see StylishSegmentedButton
 */
@Composable
public fun SingleChoiceSegmentedButtonRowScope.StylishSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SegmentedButtonDefaults.baseShape,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
    border: BorderStroke = BorderStroke(
        width = SegmentedButtonDefaults.BorderWidth,
        color = when {
            enabled && selected -> colors.activeBorderColor
            enabled -> colors.inactiveBorderColor
            selected -> colors.disabledActiveBorderColor
            else -> colors.disabledInactiveBorderColor
        },
    ),
    contentPadding: PaddingValues = SegmentedButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(selected) },
    label: @Composable () -> Unit,
) {
    SegmentedButton(
        selected = selected,
        onClick = onClick,
        shape = shape,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        icon = icon,
        label = label,
    )
}

/**
 * A single multi-select option inside
 * [StylishMultiChoiceSegmentedButtonRow], wrapping the Material 3
 * multi-choice [androidx.compose.material3.SegmentedButton].
 *
 * The default [shape] renders the segment as a plain rectangle; for
 * joined rows pass
 * `SegmentedButtonDefaults.itemShape(index = i, count = n)` so the
 * outer corners round and the inner corners notch.
 *
 * @param checked Whether this button is currently checked.
 * @param onCheckedChange Called with the new checked state when the
 *   button is tapped.
 * @param modifier Modifier applied to the button root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders in its disabled colors. Defaults to `true`.
 * @param shape The shape of this button. Defaults to
 *   [SegmentedButtonDefaults.baseShape]; use
 *   [SegmentedButtonDefaults.itemShape] for multi-segment rows.
 * @param colors [SegmentedButtonColors] resolving the button colors
 *   per state. Defaults to [SegmentedButtonDefaults.colors].
 * @param border The [BorderStroke] around the button. Defaults to the
 *   M3 outline style: a [SegmentedButtonDefaults.BorderWidth] stroke
 *   colored by the active/inactive border color for the current
 *   [checked] and [enabled] state.
 * @param contentPadding Internal spacing between the container and
 *   the content. Defaults to [SegmentedButtonDefaults.ContentPadding].
 * @param interactionSource Optional hoisted
 *   [MutableInteractionSource] for observing interactions. When
 *   `null`, one is remembered internally.
 * @param icon The icon slot, shown before the label. Defaults to the
 *   M3 check/blank icon for the [checked] state.
 * @param label The button content, typically a [Text].
 *
 * @see StylishMultiChoiceSegmentedButtonRow
 * @see StylishSegmentedButton
 */
@Composable
public fun MultiChoiceSegmentedButtonRowScope.StylishSegmentedButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SegmentedButtonDefaults.baseShape,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
    border: BorderStroke = BorderStroke(
        width = SegmentedButtonDefaults.BorderWidth,
        color = when {
            enabled && checked -> colors.activeBorderColor
            enabled -> colors.inactiveBorderColor
            checked -> colors.disabledActiveBorderColor
            else -> colors.disabledInactiveBorderColor
        },
    ),
    contentPadding: PaddingValues = SegmentedButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(checked) },
    label: @Composable () -> Unit,
) {
    SegmentedButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        shape = shape,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        icon = icon,
        label = label,
    )
}

@Preview(name = "Stylish segmented buttons", showBackground = true, widthDp = 393)
@Composable
private fun StylishSegmentedButtonsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(StylishTheme.dimensions.contentSpacing)) {
            var selected by remember { mutableIntStateOf(0) }
            StylishSingleChoiceSegmentedButtonRow {
                StylishSegmentedButton(
                    selected = selected == 0,
                    onClick = { selected = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("リスト") },
                )
                StylishSegmentedButton(
                    selected = selected == 1,
                    onClick = { selected = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    icon = { Icon(Icons.Default.GridView, contentDescription = null) },
                    label = { Text("グリッド") },
                )
            }
        }
    }
}

@Preview(name = "Stylish multi-choice segmented buttons", showBackground = true, widthDp = 393)
@Composable
private fun StylishMultiChoiceSegmentedButtonsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(StylishTheme.dimensions.contentSpacing)) {
            val checked = remember { mutableStateListOf(0) }
            StylishMultiChoiceSegmentedButtonRow {
                StylishSegmentedButton(
                    checked = 0 in checked,
                    onCheckedChange = { isChecked ->
                        if (isChecked) checked += 0 else checked -= 0
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    label = { Text("メール") },
                )
                StylishSegmentedButton(
                    checked = 1 in checked,
                    onCheckedChange = { isChecked ->
                        if (isChecked) checked += 1 else checked -= 1
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    label = { Text("SMS") },
                )
            }
        }
    }
}

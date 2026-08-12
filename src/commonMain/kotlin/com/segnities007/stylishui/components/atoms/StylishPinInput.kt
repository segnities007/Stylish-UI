package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A one-time password (OTP) / PIN entry field showing [length] boxes,
 * each holding one digit of [value].
 *
 * The value is hoisted — the caller owns the state via [value] and
 * [onValueChange]. Only digits are accepted and input is capped at
 * [length] characters. A hidden [BasicTextField] (drawn with zero
 * opacity, number-password keyboard) overlays the boxes and owns the
 * real text state, so hardware and software keyboards, backspace, IME
 * composition, and text-field semantics all work unchanged; tapping any
 * box focuses the hidden field via a [FocusRequester]. While the field
 * is focused, the box outlines highlight in the primary color; when
 * [isError] is `true` the filled boxes and outlines render in
 * [errorColor].
 *
 * The root is announced as a text field (via the underlying
 * [BasicTextField] semantics), so screen readers report the entered
 * digits. When [enabled] is `false`, the field rejects input and the
 * boxes render in the empty color.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_pininput` for UI tests.
 * Callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
 *
 * @param value The current PIN digits (controlled state). Only digits
 *   are expected; longer strings are truncated for display.
 * @param onValueChange Called with the filtered digit string on every
 *   edit.
 * @param modifier Modifier applied to the root [Box].
 * @param length The number of boxes. Defaults to 6.
 * @param enabled When `false`, the field rejects input and the boxes
 *   render in the empty color.
 * @param isError When `true`, filled boxes and outlines render in
 *   [errorColor] to signal a failed verification.
 * @param filledColor Background of boxes holding a digit. Defaults to
 *   `MaterialTheme.colorScheme.primaryContainer`.
 * @param emptyColor Background of empty boxes. Defaults to
 *   `MaterialTheme.colorScheme.surfaceVariant`.
 * @param errorColor Color used for filled boxes and outlines while
 *   [isError] is `true`. Defaults to
 *   `MaterialTheme.colorScheme.error`.
 * @param boxSize The edge length of each box. Defaults to 48.dp.
 * @param spacing The gap between boxes. Defaults to
 *   [StylishTheme.dimensions.itemSpacing].
 * @param interactionSource The [MutableInteractionSource] for the hidden
 *   text field, used to observe focus interactions (driving the box
 *   outline highlight). When `null`, an internal one is remembered.
 */
@Composable
public fun StylishPinInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
    filledColor: Color = MaterialTheme.colorScheme.primaryContainer,
    emptyColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    errorColor: Color = MaterialTheme.colorScheme.error,
    boxSize: Dp = 48.dp,
    spacing: Dp = StylishTheme.dimensions.itemSpacing,
    interactionSource: MutableInteractionSource? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by resolvedInteractionSource.collectIsFocusedAsState()
    val boxShape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius)
    Box(
        modifier = modifier
            .testTag("stylish_pininput")
            .clickable(enabled = enabled) {
                focusRequester.requestFocus()
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(length) { index ->
                val digit = value.getOrNull(index)
                val filled = digit != null
                Box(
                    modifier = Modifier
                        .size(boxSize)
                        .clip(boxShape)
                        .background(
                            when {
                                filled && isError -> errorColor
                                filled -> filledColor
                                else -> emptyColor
                            },
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when {
                                    isError -> errorColor
                                    isFocused -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                },
                            ),
                            shape = boxShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (filled) {
                        Text(
                            text = digit.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isError) {
                                MaterialTheme.colorScheme.onError
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            },
                        )
                    }
                }
            }
        }
        BasicTextField(
            value = value,
            onValueChange = { input ->
                onValueChange(input.filter { it.isDigit() }.take(length))
            },
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .focusRequester(focusRequester),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            textStyle = TextStyle(fontSize = 1.sp),
            interactionSource = resolvedInteractionSource,
        )
    }
}

@Preview(name = "Stylish pin input", showBackground = true, widthDp = 393)
@Composable
private fun StylishPinInputPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var value by remember { mutableStateOf("12") }
            StylishPinInput(
                value = value,
                onValueChange = { value = it },
            )
        }
    }
}

@Preview(name = "Stylish pin input error", showBackground = true, widthDp = 393)
@Composable
private fun StylishPinInputErrorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var value by remember { mutableStateOf("34") }
            StylishPinInput(
                value = value,
                onValueChange = { value = it },
                isError = true,
            )
        }
    }
}

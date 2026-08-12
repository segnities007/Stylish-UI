package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A numeric stepper input combining a text field with minus/plus
 * buttons, for choosing an integer within [range].
 *
 * The value is hoisted — the caller owns the state via [value] and
 * [onValueChange]. The field allows free typing: each edit is parsed as
 * an [Int] (and coerced into [range]); edits that do not parse are
 * rejected and the field snaps back to the previous value. The minus and
 * plus buttons step the value by [step] and disable automatically at the
 * bounds of [range] or when [enabled] is `false`. Since the field
 * (through [StylishFormTextField]) is a Material 3 outlined text field,
 * it carries M3's built-in focus indicator and error outline.
 *
 * The field is wrapped in [StylishFormTextField], which lays out label,
 * supporting text, and error message below the field; the steppers flank
 * it left and right. Use [leadingIcon] / [trailingIcon] for icons inside
 * the field (e.g. a currency symbol or unit), which are independent of
 * the steppers.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_numberinput` for UI
 * tests. Callers can override it by passing their own
 * `Modifier.testTag(...)` in [modifier].
 *
 * @param value The current integer value (controlled state).
 * @param onValueChange Called with the new value on every accepted edit
 *   and on every stepper tap. Values are coerced into [range] before
 *   being reported.
 * @param modifier Modifier applied to the root [Row].
 * @param enabled When `false`, the field rejects input and the steppers
 *   are disabled.
 * @param range The allowed value range. Steppers disable at the bounds;
 *   typed values are coerced into the range. Defaults to `0..Int.MAX_VALUE`.
 * @param step The amount added or removed by one stepper tap. Defaults
 *   to 1.
 * @param label Label displayed above the field. Defaults to `""`, which
 *   renders no label region.
 * @param isError When `true`, the field renders its error outline color.
 * @param errorMessage Error text displayed below the field in the error
 *   color.
 * @param leadingIcon Optional icon slot at the start of the field (the
 *   minus stepper sits outside the field).
 * @param trailingIcon Optional icon slot at the end of the field (the
 *   plus stepper sits outside the field).
 * @param textStyle Typography for the input text. Defaults to
 *   `MaterialTheme.typography.bodyLarge`.
 * @param shape Shape of the field's outlined border. Defaults to
 *   [RoundedCornerShape] with
 *   [DefaultStylishDimensions.connectedCornerRadius].
 * @param colors Color scheme for the field. Defaults to
 *   `OutlinedTextFieldDefaults.colors()`.
 * @param interactionSource The [MutableInteractionSource] for the field,
 *   used to observe focus/press/hover interactions. When `null`, an
 *   internal one is remembered.
 */
@Composable
public fun StylishNumberInput(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    range: IntRange = 0..Int.MAX_VALUE,
    step: Int = 1,
    label: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    colors: TextFieldColors? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val canDecrement = enabled && value > range.first
    val canIncrement = enabled && value < range.last
    Row(
        modifier = modifier.testTag("stylish_numberinput"),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onValueChange((value - step).coerceIn(range)) },
            enabled = canDecrement,
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "減少",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            StylishFormTextField(
                value = value.toString(),
                onValueChange = { input ->
                    input.toIntOrNull()?.let { onValueChange(it.coerceIn(range)) }
                },
                label = label,
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                errorMessage = errorMessage,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                textStyle = textStyle,
                shape = shape,
                colors = colors,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                interactionSource = interactionSource,
            )
        }
        IconButton(
            onClick = { onValueChange((value + step).coerceIn(range)) },
            enabled = canIncrement,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "増加",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(name = "Stylish number input", showBackground = true, widthDp = 393)
@Composable
private fun StylishNumberInputPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var value by remember { mutableIntStateOf(3) }
            StylishNumberInput(
                value = value,
                onValueChange = { value = it },
                label = "数量",
                range = 0..10,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Stylish number input error", showBackground = true, widthDp = 393)
@Composable
private fun StylishNumberInputErrorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishNumberInput(
                value = 0,
                onValueChange = {},
                label = "数量",
                range = 0..10,
                isError = true,
                errorMessage = "範囲内の値を入力してください",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

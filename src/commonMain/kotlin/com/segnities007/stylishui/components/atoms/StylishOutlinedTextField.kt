package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Default values and factory methods for [StylishOutlinedTextField].
 *
 * Provides a [colors] factory that returns a [TextFieldColors] instance configured for
 * outlined text fields with Stylish styling. The colors adapt to the current theme
 * (light/dark) and use Material 3's outlined text field color scheme as the base.
 *
 * @see StylishOutlinedTextField
 */
@Immutable
public object StylishOutlinedTextFieldDefaults {
    /**
     * Creates a [TextFieldColors] instance for outlined text fields with Stylish styling.
     *
     * The returned colors use Material 3's outlined text field defaults, which provide
     * a transparent container with an outlined border. The colors automatically adapt
     * to light and dark themes.
     *
     * @param focusedBorderColor Color of the border when the field is focused. Defaults
     *   to [MaterialTheme.colorScheme.primary].
     * @param unfocusedBorderColor Color of the border when the field is not focused.
     *   Defaults to [MaterialTheme.colorScheme.outline].
     * @param disabledBorderColor Color of the border when the field is disabled. Defaults
     *   to [MaterialTheme.colorScheme.outline] with reduced alpha.
     * @param errorBorderColor Color of the border when [StylishOutlinedTextField.isError]
     *   is true. Defaults to [MaterialTheme.colorScheme.error].
     * @param focusedTextColor Color of the text when the field is focused. Defaults to
     *   [MaterialTheme.colorScheme.onSurface].
     * @param unfocusedTextColor Color of the text when the field is not focused. Defaults
     *   to [MaterialTheme.colorScheme.onSurface].
     * @param disabledTextColor Color of the text when the field is disabled. Defaults to
     *   [MaterialTheme.colorScheme.onSurface] with reduced alpha.
     * @param errorTextColor Color of the text when [StylishOutlinedTextField.isError] is
     *   true. Defaults to [MaterialTheme.colorScheme.onSurface].
     * @param focusedLabelColor Color of the label when the field is focused. Defaults to
     *   [MaterialTheme.colorScheme.primary].
     * @param unfocusedLabelColor Color of the label when the field is not focused.
     *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
     * @param disabledLabelColor Color of the label when the field is disabled. Defaults
     *   to [MaterialTheme.colorScheme.onSurfaceVariant] with reduced alpha.
     * @param errorLabelColor Color of the label when [StylishOutlinedTextField.isError]
     *   is true. Defaults to [MaterialTheme.colorScheme.error].
     * @param placeholderColor Color of the placeholder text. Defaults to
     *   [MaterialTheme.colorScheme.onSurfaceVariant].
     * @param disabledPlaceholderColor Color of the placeholder text when the field is
     *   disabled. Defaults to [MaterialTheme.colorScheme.onSurfaceVariant] with reduced
     *   alpha.
     * @param cursorColor Color of the text cursor. Defaults to [MaterialTheme.colorScheme.primary].
     * @param errorCursorColor Color of the text cursor when [StylishOutlinedTextField.isError]
     *   is true. Defaults to [MaterialTheme.colorScheme.error].
     * @param focusedLeadingIconColor Color of the leading icon when the field is focused.
     *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
     * @param unfocusedLeadingIconColor Color of the leading icon when the field is not
     *   focused. Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
     * @param disabledLeadingIconColor Color of the leading icon when the field is disabled.
     *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant] with reduced alpha.
     * @param errorLeadingIconColor Color of the leading icon when [StylishOutlinedTextField.isError]
     *   is true. Defaults to [MaterialTheme.colorScheme.error].
     * @param focusedTrailingIconColor Color of the trailing icon when the field is focused.
     *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
     * @param unfocusedTrailingIconColor Color of the trailing icon when the field is not
     *   focused. Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
     * @param disabledTrailingIconColor Color of the trailing icon when the field is disabled.
     *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant] with reduced alpha.
     * @param errorTrailingIconColor Color of the trailing icon when [StylishOutlinedTextField.isError]
     *   is true. Defaults to [MaterialTheme.colorScheme.error].
     */
    @Composable
    public fun colors(
        focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor: Color = MaterialTheme.colorScheme.outline,
        disabledBorderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
        errorBorderColor: Color = MaterialTheme.colorScheme.error,
        focusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor: Color = MaterialTheme.colorScheme.onSurface,
        disabledTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor: Color = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor: Color = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLabelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        errorLabelColor: Color = MaterialTheme.colorScheme.error,
        placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        cursorColor: Color = MaterialTheme.colorScheme.primary,
        errorCursorColor: Color = MaterialTheme.colorScheme.error,
        focusedLeadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLeadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLeadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        errorLeadingIconColor: Color = MaterialTheme.colorScheme.error,
        focusedTrailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedTrailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        errorTrailingIconColor: Color = MaterialTheme.colorScheme.error,
    ): TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        disabledBorderColor = disabledBorderColor,
        errorBorderColor = errorBorderColor,
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        errorTextColor = errorTextColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        errorLeadingIconColor = errorLeadingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        errorTrailingIconColor = errorTrailingIconColor,
        focusedPlaceholderColor = placeholderColor,
        unfocusedPlaceholderColor = placeholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        errorPlaceholderColor = placeholderColor,
    )
}

/**
 * An outlined text field with Stylish styling, designed for form input with built-in
 * label, placeholder, and supporting text support. Wraps Material's [OutlinedTextField]
 * with Stylish theme integration.
 *
 * The outlined variant displays a transparent container with a border outline, making
 * it suitable for forms where you want a lighter visual weight compared to filled text
 * fields. The border animates and changes color on focus, error, and disabled states.
 *
 * When [minLines] is 1 and [maxLines] is 1 (the default), the field operates in
 * single-line mode. Setting [minLines] greater than 1 switches to a multiline field
 * whose [maxLines] defaults to [Int.MAX_VALUE]. When [singleLine] is `true`, both
 * [minLines] and [maxLines] are forced to 1 (matching Material's single-line contract).
 *
 * Error display: when [isError] is `true`, the field renders its error indicator color
 * on the border. The [supportingText] slot can be used to display error messages or
 * helper text below the field.
 *
 * @param value Current text value (controlled state).
 * @param onValueChange Called with the updated text on every edit.
 * @param modifier Modifier applied to the enclosing [Column] root.
 * @param label Optional label displayed above the field. Animates to float above the
 *   container when the field is focused or has content.
 * @param placeholder Optional hint text shown when the field is empty and not focused.
 * @param leadingIcon Optional icon slot at the start of the field.
 * @param trailingIcon Optional icon slot at the end of the field.
 * @param supportingText Optional slot rendered below the field. Use this for helper
 *   text, error messages, or character counters.
 * @param isError When `true`, the field renders its error indicator color on the border.
 *   Defaults to `false`.
 * @param enabled When `false`, the field rejects input and renders in Material's disabled
 *   color scheme. Defaults to `true`.
 * @param readOnly When `true`, the field displays its text without allowing edits.
 *   Defaults to `false`.
 * @param singleLine When `true`, forces single-line mode: [minLines] and [maxLines] are
 *   set to 1 and the field scrolls horizontally instead of wrapping. When `false`
 *   (default), single-line mode is derived from [minLines] and [maxLines].
 * @param maxLines Maximum lines before scrolling. Defaults to 1 when [singleLine] is
 *   true, otherwise [Int.MAX_VALUE]. Ignored when [singleLine] is `true`.
 * @param minLines Minimum visible lines. Defaults to 1. Ignored when [singleLine] is
 *   `true`.
 * @param keyboardOptions Software keyboard options (keyboard type, capitalization, IME
 *   action) for the field.
 * @param keyboardActions Software keyboard action handlers for the field.
 * @param visualTransformation Visual transformation applied to the input text (e.g.
 *   password masking). Defaults to [VisualTransformation.None].
 * @param shape Shape of the field container and border. Defaults to
 *   [RoundedCornerShape] with [StylishTheme.shapes.medium].
 * @param colors Color scheme for the field. Defaults to [StylishOutlinedTextFieldDefaults.colors].
 * @param interactionSource The [MutableInteractionSource] for the field, used to observe
 *   focus/press/hover interactions. When `null`, an internal one is remembered.
 *
 * @see StylishFilledTextField
 * @see StylishFormTextField
 * @see StylishOutlinedTextFieldDefaults
 */
@Composable
public fun StylishOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = StylishTheme.shapes.medium,
    colors: TextFieldColors = StylishOutlinedTextFieldDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedSingleLine = singleLine || (minLines == 1 && maxLines == 1)
    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            minLines = if (singleLine) 1 else minLines,
            maxLines = if (singleLine) 1 else maxLines,
            shape = shape,
            colors = colors,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth(),
            singleLine = resolvedSingleLine,
            isError = isError,
            interactionSource = interactionSource,
            supportingText = supportingText,
        )
    }
}

@Preview(name = "Outlined text field - default", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedTextFieldDefaultPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val value = remember { mutableStateOf("") }
            StylishOutlinedTextField(
                value = value.value,
                onValueChange = { value.value = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Outlined text field - with label and placeholder", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedTextFieldLabelPlaceholderPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val value = remember { mutableStateOf("") }
            StylishOutlinedTextField(
                value = value.value,
                onValueChange = { value.value = it },
                label = { Text("メールアドレス") },
                placeholder = { Text("example@email.com") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Outlined text field - with icons", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedTextFieldIconsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val value = remember { mutableStateOf("") }
            StylishOutlinedTextField(
                value = value.value,
                onValueChange = { value.value = it },
                label = { Text("検索") },
                placeholder = { Text("キーワードを入力") },
                leadingIcon = { Text("🔍") },
                trailingIcon = { Text("✕") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Outlined text field - error state", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedTextFieldErrorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishOutlinedTextField(
                value = "invalid-email",
                onValueChange = {},
                label = { Text("メールアドレス") },
                supportingText = {
                    Text(
                        text = "有効なメールアドレスを入力してください",
                        color = MaterialTheme.colorScheme.error,
                    )
                },
                isError = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Outlined text field - disabled state", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedTextFieldDisabledPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishOutlinedTextField(
                value = "disabled value",
                onValueChange = {},
                label = { Text("無効なフィールド") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Outlined text field - multiline", showBackground = true, widthDp = 393)
@Composable
private fun StylishOutlinedTextFieldMultilinePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val value = remember { mutableStateOf("複数行の入力例です。\nOutlinedTextFieldはminLinesを設定することで、複数行表示が可能です。") }
            StylishOutlinedTextField(
                value = value.value,
                onValueChange = { value.value = it },
                label = { Text("コメント") },
                placeholder = { Text("コメントを入力してください") },
                singleLine = false,
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

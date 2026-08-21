package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/** Default bullet character used to mask secure text input. */
private const val StylishObfuscationCharacter: Char = '\u2022'

/**
 * A secure single-line text field for password-like input, wrapping the
 * Material 3 [SecureTextField] with the theme's defaults.
 *
 * The field is designed for secret input: it masks the text with
 * [StylishObfuscationCharacter], disables auto-correct, and uses a
 * password keyboard type by default. The masking mode can be tuned via
 * [textObfuscationMode] (e.g. [TextObfuscationMode.RevealLastTyped],
 * the default, briefly reveals the most recent character).
 *
 * Unlike the string-based text fields, this component operates on a
 * hoisted [TextFieldState] (see
 * [androidx.compose.foundation.text.input.TextFieldState]) instead of
 * `value`/`onValueChange` callbacks; the state owns the text and
 * selection and survives recomposition.
 *
 * @param state The [TextFieldState] holding the field's text and
 *   selection. Callers typically create it with
 *   `remember { TextFieldState() }` and hoist it.
 * @param modifier Modifier applied to the [SecureTextField] root.
 * @param enabled When `false`, the field rejects input and renders in
 *   Material's disabled color scheme. Defaults to `true`.
 * @param textStyle Typography for the input text. Defaults to
 *   [LocalTextStyle].
 * @param labelPosition The position of [label] relative to the field.
 *   Defaults to [TextFieldLabelPosition.Attached].
 * @param label Optional label composable displayed with the field.
 * @param placeholder Optional hint composable shown when the field is
 *   empty.
 * @param leadingIcon Optional icon slot at the start of the field.
 * @param trailingIcon Optional icon slot at the end of the field (e.g.
 *   a visibility toggle).
 * @param prefix Optional slot rendered inline before the input text.
 * @param suffix Optional slot rendered inline after the input text.
 * @param supportingText Optional slot rendered below the field.
 * @param isError When `true`, the field renders its error indicator
 *   color.
 * @param inputTransformation Optional transformation applied to input
 *   as it is entered.
 * @param textObfuscationMode How the entered text is masked. Defaults
 *   to [TextObfuscationMode.RevealLastTyped].
 * @param textObfuscationCharacter The character used to mask the text.
 *   Defaults to `'\u2022'` (bullet).
 * @param keyboardOptions Software keyboard options for the field.
 *   Defaults to a password keyboard with auto-correct disabled.
 * @param onKeyboardAction Called when the user presses the IME action
 *   button or the enter key.
 * @param onTextLayout Callback executed when the text layout becomes
 *   queryable, receiving a function that returns the
 *   [TextLayoutResult] (or `null` when not yet laid out).
 * @param shape Shape of the field's container. Defaults to
 *   [TextFieldDefaults.shape].
 * @param colors Color scheme for the field. Defaults to
 *   [TextFieldDefaults.colors].
 * @param contentPadding Padding separating the input text from the
 *   field's surroundings. Defaults to Material's label-aware padding.
 * @param interactionSource The [MutableInteractionSource] for the
 *   field, used to observe focus/press/hover interactions. When
 *   `null`, an internal one is remembered.
 *
 * @see StylishFormTextField
 * @see StylishFilledTextField
 */
@Composable
public fun StylishSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = StylishObfuscationCharacter,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Password,
    ),
    onKeyboardAction: KeyboardActionHandler? = null,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    contentPadding: PaddingValues =
        if (label == null || labelPosition is TextFieldLabelPosition.Above) {
            TextFieldDefaults.contentPaddingWithoutLabel()
        } else {
            TextFieldDefaults.contentPaddingWithLabel()
        },
    interactionSource: MutableInteractionSource? = null,
) {
    SecureTextField(
        state = state,
        modifier = modifier.stylishTestTag("secure_text_field"),
        enabled = enabled,
        textStyle = textStyle,
        labelPosition = labelPosition,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        inputTransformation = inputTransformation,
        textObfuscationMode = textObfuscationMode,
        textObfuscationCharacter = textObfuscationCharacter,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        onTextLayout = onTextLayout,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish secure text field", showBackground = true, widthDp = 393)
@Composable
private fun StylishSecureTextFieldPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val state = remember { TextFieldState() }
            StylishSecureTextField(
                state = state,
                label = { Text("パスワード") },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview(name = "Stylish secure text field error", showBackground = true, widthDp = 393)
@Composable
private fun StylishSecureTextFieldErrorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val state = remember { TextFieldState() }
            StylishSecureTextField(
                state = state,
                label = { Text("パスワード") },
                isError = true,
                supportingText = {
                    Text(
                        text = "パスワードが短すぎます",
                        color = MaterialTheme.colorScheme.error,
                    )
                },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

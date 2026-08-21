package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * An outlined text field designed for form input, with built-in
 * label, placeholder, and error-message support. Wraps Material's
 * [OutlinedTextField] inside a [Column] and fills the available
 * width.
 *
 * When [minLines] is 1 and [maxLines] is 1 (the default), the field
 * operates in single-line mode. Setting [minLines] greater than 1
 * switches to a multiline field whose [maxLines] defaults to
 * [Int.MAX_VALUE]. When [singleLine] is `true`, both [minLines] and
 * [maxLines] are forced to 1 (matching Material's single-line
 * contract).
 *
 * Error display follows a priority chain: if [supportingContent] is
 * provided it is used as-is; otherwise, when [errorMessage] is
 * non-null, it is rendered as supporting text in
 * `MaterialTheme.colorScheme.error`. The [isError] flag independently
 * controls the field's error outline color.
 *
 * A character counter can be added by passing a counter composable
 * via [supportingContent] (e.g. `{ Text("${value.length} / 100") }`);
 * no counter is added automatically.
 *
 * @param value Current text value (controlled state).
 * @param onValueChange Called with the updated text on every edit.
 * @param label Label displayed above the field. Defaults to `""`,
 *   which renders no label region at all. Overridden by
 *   [labelContent] when provided.
 * @param placeholder Hint text shown when the field is empty.
 *   Defaults to `""`, which renders no placeholder region at all.
 *   Overridden by [placeholderContent] when provided.
 * @param minLines Minimum visible lines. Defaults to 1. Ignored when
 *   [singleLine] is `true`.
 * @param maxLines Maximum lines before scrolling. Defaults to 1 when
 *   [minLines] is 1, otherwise [Int.MAX_VALUE]. Ignored when
 *   [singleLine] is `true`.
 * @param isError When `true`, the field renders its error outline
 *   color. Independent of [errorMessage].
 * @param errorMessage Error text displayed below the field in the
 *   error color. Ignored when [supportingContent] is provided.
 * @param leadingIcon Optional icon slot at the start of the field.
 * @param trailingIcon Optional icon slot at the end of the field.
 * @param textStyle Typography for the input text. Defaults to
 *   `MaterialTheme.typography.bodyLarge`.
 * @param shape Shape of the outlined border. Defaults to
 *   `OutlinedTextFieldDefaults.shape`.
 * @param colors Color scheme for the field. Defaults to
 *   `OutlinedTextFieldDefaults.colors()`, with the cursor color
 *   derived from [cursorBrush] when it is a solid color.
 * @param enabled When `false`, the field rejects input and renders
 *   in Material's disabled color scheme. Defaults to `true`.
 * @param readOnly When `true`, the field displays its text without
 *   allowing edits. Defaults to `false`.
 * @param keyboardOptions Software keyboard options (keyboard type,
 *   capitalization, IME action) for the field.
 * @param keyboardActions Software keyboard action handlers for the
 *   field.
 * @param visualTransformation Visual transformation applied to the
 *   input text (e.g. password masking). Defaults to
 *   [VisualTransformation.None].
 * @param labelContent Optional slot that replaces the default [Text]
 *   label built from [label].
 * @param placeholderContent Optional slot that replaces the default
 *   [Text] placeholder built from [placeholder].
 * @param supportingContent Optional slot rendered below the field,
 *   replacing [errorMessage] when provided. Use this for counters
 *   and helper text.
 * @param fieldModifier Modifier applied to the inner
 *   [OutlinedTextField], before `fillMaxWidth` in the modifier chain
 *   (i.e. `fieldModifier.fillMaxWidth()`). Use this to add
 *   test tags or input-specific modifiers without affecting the
 *   outer [Column].
 * @param cursorBrush Brush used for the text cursor. Defaults to
 *   [SolidColor] of `MaterialTheme.colorScheme.onSurface`. Note: Material's string-based
 *   [OutlinedTextField] derives the cursor from its colors'
 *   `cursorColor`, so a [SolidColor] brush is honored through the
 *   default [colors], while non-solid brushes fall back to the
 *   theme cursor color; an explicit [colors] overrides the brush.
 * @param singleLine When `true`, forces single-line mode: [minLines]
 *   and [maxLines] are set to 1 and the field scrolls horizontally
 *   instead of wrapping. When `false` (default), single-line mode is
 *   derived from [minLines] and [maxLines] as before.
 * @param interactionSource The [MutableInteractionSource] for the
 *   field, used to observe focus/press/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param prefix Optional slot rendered inline before the input text
 *   (e.g. a currency symbol).
 * @param suffix Optional slot rendered inline after the input text
 *   (e.g. a unit).
 */
@Composable
public fun StylishFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = if (minLines == 1) 1 else Int.MAX_VALUE,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    labelContent: @Composable (() -> Unit)? = null,
    placeholderContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    fieldModifier: Modifier = Modifier,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
    singleLine: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
) {
    val resolvedLabel: @Composable (() -> Unit)? = when {
        labelContent != null -> labelContent
        label.isNotBlank() -> {
            { Text(label) }
        }
        else -> null
    }
    val resolvedPlaceholder: @Composable (() -> Unit)? = when {
        placeholderContent != null -> placeholderContent
        placeholder.isNotBlank() -> {
            { Text(placeholder) }
        }
        else -> null
    }
    val resolvedColors = colors ?: run {
        val solidCursor = (cursorBrush as? SolidColor)?.value
        if (solidCursor != null && solidCursor != MaterialTheme.colorScheme.onSurface) {
            OutlinedTextFieldDefaults.colors(cursorColor = solidCursor)
        } else {
            OutlinedTextFieldDefaults.colors()
        }
    }
    val resolvedSingleLine = singleLine || (minLines == 1 && maxLines == 1)
    Column(modifier.stylishTestTag("form_text_field")) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            label = resolvedLabel,
            placeholder = resolvedPlaceholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            minLines = if (singleLine) 1 else minLines,
            maxLines = if (singleLine) 1 else maxLines,
            textStyle = textStyle,
            shape = shape,
            colors = resolvedColors,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            modifier = fieldModifier.fillMaxWidth(),
            singleLine = resolvedSingleLine,
            isError = isError,
            interactionSource = interactionSource,
            supportingText = supportingContent ?: errorMessage?.let { message ->
                {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
        )
    }
}

@Preview(name = "Form text field", showBackground = true, widthDp = 393)
@Composable
private fun StylishFormTextFieldPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val value = remember { mutableStateOf("") }
            StylishFormTextField(
                value = value.value,
                onValueChange = { value.value = it },
                label = "ラベル",
                placeholder = "プレースホルダー",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Form text field with error", showBackground = true, widthDp = 393)
@Composable
private fun StylishFormTextFieldErrorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishFormTextField(
                value = "",
                onValueChange = {},
                label = "ラベル",
                placeholder = "プレースホルダー",
                modifier = Modifier.fillMaxWidth(),
                isError = true,
                errorMessage = "エラーメッセージ",
            )
        }
    }
}

@Preview(name = "Form text field multiline", showBackground = true, widthDp = 393)
@Composable
private fun StylishFormTextFieldMultilinePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            val value = remember { mutableStateOf("複数行の入力例です。") }
            StylishFormTextField(
                value = value.value,
                onValueChange = { value.value = it },
                label = "詳細",
                placeholder = "詳細を入力",
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
        }
    }
}

@Preview(name = "Form text field prefix suffix", showBackground = true, widthDp = 393)
@Composable
private fun StylishFormTextFieldPrefixSuffixPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishFormTextField(
                value = "100",
                onValueChange = {},
                label = "金額",
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("¥") },
                suffix = { Text("円") },
            )
        }
    }
}

package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A form-oriented outlined text field with built-in label, placeholder, and
 * inline error-message display.
 *
 * This molecules-layer variant is a streamlined version of the atoms-layer
 * [com.segnities007.stylishui.components.atoms.StylishFormTextField]. It omits
 * the `labelContent`, `placeholderContent`, `supportingContent`, and
 * `fieldModifier` parameters, always rendering the label and placeholder as
 * plain [Text] composables and applying [Modifier.fillMaxWidth] to the inner
 * field. Use this variant when the default text-based label/placeholder is
 * sufficient; use the atoms variant when custom slot content or a separate
 * field modifier is needed.
 *
 * When [minLines] is 1 and [maxLines] resolves to 1, the field operates in
 * single-line mode. Setting [minLines] greater than 1 enables multi-line
 * input with [maxLines] defaulting to [Int.MAX_VALUE]. When [isError] is
 * `true` the field draws its error outline, and a non-null [errorMessage] is
 * displayed below the field in [MaterialTheme.colorScheme.error].
 *
 * @param value The current text value of the field.
 * @param onValueChange Callback invoked with the updated text whenever the
 *   user edits the field.
 * @param label The label text displayed above the field outline.
 * @param placeholder The placeholder text shown when the field is empty.
 * @param minLines The minimum number of visible text lines. Defaults to 1.
 * @param maxLines The maximum number of visible text lines. Defaults to 1
 *   when [minLines] is 1, otherwise [Int.MAX_VALUE].
 * @param isError When `true`, the field renders its error visual state
 *   (error-colored outline). Defaults to `false`.
 * @param errorMessage An optional error message displayed as supporting text
 *   below the field in the error color. When `null`, no supporting text is
 *   shown.
 * @param leadingIcon An optional composable rendered at the start of the
 *   field, typically an [androidx.compose.material3.Icon]. When `null`, no
 *   leading icon is shown.
 * @param trailingIcon An optional composable rendered at the end of the
 *   field, typically an icon or visibility toggle. When `null`, no trailing
 *   icon is shown.
 * @param textStyle The [TextStyle] applied to the input text. Defaults to
 *   [MaterialTheme.typography.bodyLarge].
 * @param shape The [Shape] of the field's outline border. Defaults to
 *   [OutlinedTextFieldDefaults.shape].
 * @param colors The [TextFieldColors] controlling the field's color scheme.
 *   When `null`, defaults to [OutlinedTextFieldDefaults.colors].
 *
 * @see com.segnities007.stylishui.components.atoms.StylishFormTextField
 * @see StylishDatePickerField
 */
@Composable
public fun StylishFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
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
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            minLines = minLines,
            maxLines = maxLines,
            textStyle = textStyle,
            shape = shape,
            colors = colors ?: OutlinedTextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = minLines == 1 && maxLines == 1,
            isError = isError,
            supportingText = errorMessage?.let { message ->
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

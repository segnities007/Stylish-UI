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

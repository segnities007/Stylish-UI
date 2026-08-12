package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * An inline-editable text row — the web "Editable" pattern from Chakra
 * UI.
 *
 * Displays [value] as static text with a small edit affordance. Tapping
 * switches to an input field; confirm (check) commits via [onCommit] and
 * [onValueChange], cancel (close) discards the edit. The field is
 * committed when the keyboard's done action is pressed.
 *
 * @param value The current text.
 * @param onValueChange Called as the user types.
 * @param modifier Modifier applied to the root row.
 * @param enabled When `false`, editing is not possible.
 * @param onCommit Optional callback invoked with the final text when an
 *   edit is committed. When `null`, only [onValueChange] is called.
 * @param textStyle Typography of both the display text and the input.
 *   Defaults to [MaterialTheme.typography.bodyLarge].
 * @param editShape Corner shape of the input field. Defaults to
 *   [RoundedCornerShape] with
 *   [DefaultStylishDimensions.connectedCornerRadius].
 * @param singleLine Whether the value is a single line. When `true`, the
 *   IME done action commits the edit. Defaults to `true`.
 */
@Composable
public fun StylishEditable(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCommit: ((String) -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    editShape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    singleLine: Boolean = true,
) {
    var editing by remember { mutableStateOf(false) }

    fun commit() {
        onCommit?.invoke(value)
        editing = false
    }

    if (editing) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                enabled = enabled,
                shape = editShape,
                textStyle = textStyle,
                keyboardOptions = KeyboardOptions(imeAction = if (singleLine) ImeAction.Done else ImeAction.Default),
                keyboardActions = KeyboardActions(onDone = { commit() }),
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { commit() }, enabled = enabled) {
                Icon(Icons.Default.Check, contentDescription = "確定")
            }
            IconButton(onClick = { editing = false }, enabled = enabled) {
                Icon(Icons.Default.Close, contentDescription = "キャンセル")
            }
        }
    } else {
        Row(
            modifier = modifier
                .then(
                    if (enabled) {
                        Modifier.clickable { editing = true }
                    } else {
                        Modifier
                    },
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
        ) {
            Text(
                value.ifEmpty { "（未入力）" },
                style = textStyle,
                color = if (value.isEmpty()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (enabled) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "編集",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Preview(name = "Stylish editable", showBackground = true, widthDp = 393)
@Composable
private fun StylishEditablePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishEditable(value = "Stylish UI", onValueChange = {}, onCommit = {})
        }
    }
}

package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFormTextField
import com.segnities007.stylishui.components.molecules.StylishPopover
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * A text field with a filtered suggestion dropdown — the web
 * "AutoComplete" pattern from Ant Design and MUI.
 *
 * As the user types, [options] are filtered with [match] and up to
 * [maxSuggestions] matches are shown in a popover below the field.
 * Selecting a suggestion calls [onOptionSelect] (and [onValueChange] with
 * the selected text).
 *
 * @param value The current field text.
 * @param onValueChange Called as the user types.
 * @param options The full candidate list to filter against.
 * @param modifier Modifier applied to the root box.
 * @param label Optional field label.
 * @param placeholder Optional placeholder text.
 * @param onOptionSelect Optional callback invoked with the selected
 *   suggestion.
 * @param match Predicate deciding whether [option] matches [query].
 *   Defaults to a case-insensitive substring match.
 * @param maxSuggestions Maximum suggestions shown. Defaults to 6.
 * @param enabled When `false`, the field is read-only and suggestions are
 *   hidden.
 * @param isError Whether the field renders in its error state.
 * @param errorMessage Optional supporting error text.
 * @param shape Corner shape of the field and popover. Defaults to
 *   [RoundedCornerShape] with
 *   [DefaultStylishDimensions.connectedCornerRadius].
 * @param width Width of the suggestion popover. Defaults to 320 dp.
 */
@Composable
public fun StylishAutocomplete(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    onOptionSelect: ((String) -> Unit)? = null,
    match: (String, String) -> Boolean = { query, option ->
        option.contains(query, ignoreCase = true)
    },
    maxSuggestions: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    width: Dp = 320.dp,
) {
    val suggestions = remember(value, options) {
        if (value.isBlank()) emptyList() else options.filter { match(value, it) }.take(maxSuggestions)
    }
    var expanded by remember { mutableStateOf(false) }

    StylishPopover(
        expanded = expanded && enabled && suggestions.isNotEmpty(),
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth(),
        trigger = {
            StylishFormTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    expanded = true
                },
                label = label,
                placeholder = placeholder,
                enabled = enabled,
                isError = isError,
                errorMessage = errorMessage,
                shape = shape,
            )
        },
        shape = shape,
        width = width,
        offset = DpOffset(0.dp, 4.dp),
        content = {
        if (suggestions.isEmpty()) {
            Text(
                "候補がありません",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing)) {
                suggestions.forEach { suggestion ->
                    Surface(
                        onClick = {
                            onValueChange(suggestion)
                            onOptionSelect?.invoke(suggestion)
                            expanded = false
                        },
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Transparent,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            suggestion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 6.dp),
                        )
                    }
                }
            }
        }
    },
)
}

@Preview(name = "Stylish autocomplete", showBackground = true, widthDp = 393)
@Composable
private fun StylishAutocompletePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var value by remember { mutableStateOf("") }
            StylishAutocomplete(
                value = value,
                onValueChange = { value = it },
                options = listOf("Stylish UI", "Compose Multiplatform", "Material 3", "Kotlin"),
                label = "検索",
                placeholder = "ライブラリ名を入力",
            )
        }
    }
}

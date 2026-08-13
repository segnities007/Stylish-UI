package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A labeled form field wrapper — the web "Field/Form" pattern from
 * shadcn/ui and Ant Design.
 *
 * Composes a [label] (with an optional required marker), the [content]
 * slot (the input itself), and supporting/error text below it. The
 * error message takes precedence over the supporting text and is
 * rendered in the error color.
 *
 * @param modifier Modifier applied to the root column.
 * @param label Optional label text shown above the content.
 * @param required When `true` and [label] is set, an asterisk is shown
 *   next to the label. Defaults to `false`.
 * @param isError Whether the field is in its error state. When `true`,
 *   [errorMessage] is rendered in the error color. Defaults to `false`.
 * @param errorMessage Optional error text shown below the content.
 * @param supportingText Optional helper text shown below the content
 *   when there is no error message.
 * @param labelStyle Typography of the label. Defaults to
 *   [MaterialTheme.typography.labelLarge].
 * @param supportingTextStyle Typography of [supportingText]. Defaults to
 *   [MaterialTheme.typography.bodySmall].
 * @param content The input content (e.g. a text field).
 */
@Composable
public fun StylishFormField(
    modifier: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
    ) {
        if (label != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing / 2),
            ) {
                Text(
                    label,
                    style = labelStyle,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (required) {
                    Text(
                        "*",
                        style = labelStyle,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
        content()
        when {
            isError && errorMessage != null -> {
                Text(
                    errorMessage,
                    style = supportingTextStyle,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            supportingText != null -> {
                Text(
                    supportingText,
                    style = supportingTextStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(name = "Stylish form field", showBackground = true, widthDp = 393)
@Composable
private fun StylishFormFieldPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishFormField(
                label = "車両名",
                required = true,
                supportingText = "登録する車両の名前を入力してください。",
            ) {
                Text("Stylish Car", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

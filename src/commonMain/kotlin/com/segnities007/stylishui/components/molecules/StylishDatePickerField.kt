package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * ライブラリ内部で使うデフォルトの日付書式。
 * java.time への依存を避け、KMP 各ターゲットで動作させる。
 */
private val defaultDateFormatter: (LocalDate) -> String = { date ->
    val month = date.monthNumber.toString().padStart(2, '0')
    val day = date.dayOfMonth.toString().padStart(2, '0')
    "${date.year}/$month/$day"
}

private fun LocalDate.toEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    return this.atStartOfDayIn(timeZone).toEpochMilliseconds()
}

private fun Long.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
}

/**
 * A date-input field that opens a Material 3 [DatePickerDialog] when tapped,
 * displaying the selected date as formatted text in an [OutlinedTextField].
 *
 * The text field itself is read-only and disabled; all interaction goes
 * through the date-picker dialog. When [value] is `null` the field shows the
 * [placeholder] text. The dialog's confirm button commits the selected date
 * via [onValueChange]; the dismiss button closes the dialog without changing
 * the value. Date formatting avoids `java.time` so the component works across
 * all Kotlin Multiplatform targets.
 *
 * @param value The currently selected date, or `null` if no date has been
 *   chosen.
 * @param onValueChange Callback invoked with the newly selected [LocalDate]
 *   when the user confirms a date in the dialog. Receives `null`-safe: only
 *   called when a date is actually selected. If the user confirms without
 *   selecting a date, the dialog closes silently and [onValueChange] is not
 *   called.
 * @param label The label text displayed on the outlined text field. Ignored
 *   when [labelContent] is provided.
 * @param confirmLabel The text for the dialog's confirm button. Ignored when
 *   [confirmLabelContent] is provided.
 * @param dismissLabel The text for the dialog's dismiss button. Ignored when
 *   [dismissLabelContent] is provided.
 * @param placeholder The placeholder text shown when [value] is `null`.
 *   Ignored when [placeholderContent] is provided.
 * @param formatter A function that converts a [LocalDate] to its display
 *   string. Defaults to `yyyy/MM/dd` formatting.
 * @param labelContent An optional custom composable for the text field label.
 *   When `null`, a [Text] composable with [label] is used.
 * @param placeholderContent An optional custom composable for the text field
 *   placeholder. When `null`, a [Text] composable with [placeholder] is used.
 * @param confirmLabelContent An optional custom composable for the dialog's
 *   confirm button content. When `null`, a [Text] composable with
 *   [confirmLabel] is used.
 * @param dismissLabelContent An optional custom composable for the dialog's
 *   dismiss button content. When `null`, a [Text] composable with
 *   [dismissLabel] is used.
 *
 * @see StylishFormTextField
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishDatePickerField(
    value: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    label: String,
    confirmLabel: String,
    dismissLabel: String,
    modifier: Modifier = Modifier,
    placeholder: String,
    formatter: (LocalDate) -> String = defaultDateFormatter,
    labelContent: @Composable (() -> Unit)? = null,
    placeholderContent: @Composable (() -> Unit)? = null,
    confirmLabelContent: @Composable (() -> Unit)? = null,
    dismissLabelContent: @Composable (() -> Unit)? = null,
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
    ) {
        OutlinedTextField(
            value = value?.let(formatter) ?: "",
            onValueChange = {},
            label = labelContent ?: { Text(label) },
            placeholder = placeholderContent ?: { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            singleLine = true,
        )
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = value?.toEpochMillis(),
        )
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onValueChange(millis.toLocalDate())
                    }
                    showDialog = false
                }) { confirmLabelContent ?: Text(confirmLabel) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { dismissLabelContent ?: Text(dismissLabel) }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(name = "Stylish date picker field", showBackground = true, widthDp = 393)
@Composable
private fun StylishDatePickerFieldPreview() {
    MaterialTheme {
        StylishDatePickerField(
            value = LocalDate(2026, 7, 25),
            onValueChange = {},
            label = "日付",
            confirmLabel = "OK",
            dismissLabel = "キャンセル",
            placeholder = "日付を選択",
        )
    }
}

@Preview(name = "Stylish date picker field (null)", showBackground = true, widthDp = 393)
@Composable
private fun StylishDatePickerFieldNullPreview() {
    MaterialTheme {
        StylishDatePickerField(
            value = null,
            onValueChange = {},
            label = "日付",
            confirmLabel = "OK",
            dismissLabel = "キャンセル",
            placeholder = "日付を選択",
        )
    }
}

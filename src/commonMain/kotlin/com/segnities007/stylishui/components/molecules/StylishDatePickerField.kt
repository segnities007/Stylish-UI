@file:Suppress("DEPRECATION")

package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * The default date formatter used by [StylishDatePickerField], producing
 * `yyyy/MM/dd` output (e.g. `2026/07/25`).
 *
 * The format is **locale-neutral**: it always uses the zero-padded Gregorian
 * month and day numbers with ASCII digits and a forward-slash separator,
 * regardless of the device locale. This keeps the field display stable across
 * languages, at the cost of not following the user's locale conventions.
 * Pass a custom [formatter][com.segnities007.stylishui.components.molecules.StylishDatePickerField.formatter]
 * when a locale-aware format is required.
 */
public val defaultDateFormatter: (LocalDate) -> String = { date ->
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
 * The text field itself is read-only but keeps its enabled appearance; all
 * interaction goes through the date-picker dialog. Set [enabled] to `false`
 * to grey the field out and block the dialog. When [value] is `null` the
 * field shows the [placeholder] text. The dialog's confirm button commits the
 * selected date via [onValueChange]; the dismiss button closes the dialog
 * without changing the value. Date formatting avoids `java.time` so the
 * component works across all Kotlin Multiplatform targets.
 *
 * The dialog is the Material 3 [DatePickerDialog], which applies platform
 * window insets (system bars, IME) itself; this component does not add any
 * additional inset handling.
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
 *   string. When omitted, the formatter from [StylishTheme.strings] is used;
 *   supply a custom formatter when app-specific locale rules are required.
 * @param enabled Whether the field is interactive. When `false`, taps do not
 *   open the date-picker dialog and the field renders in a disabled state.
 *   Defaults to `true`.
 * @param isError When `true`, the field renders its error visual state
 *   (error-colored outline). Defaults to `false`.
 * @param supportingText An optional text displayed below the field, in the
 *   error color when [isError] is `true`. When `null`, no supporting text is
 *   shown.
 * @param yearRange The range of years selectable in the picker, forwarded to
 *   the Material 3 [rememberDatePickerState]. Defaults to
 *   [DatePickerDefaults.YearRange].
 * @param initialDisplayMode The display mode (calendar picker or text input)
 *   the dialog opens with, forwarded to the Material 3
 *   [rememberDatePickerState]. Defaults to [DisplayMode.Picker].
 * @param showModeToggle Whether the picker shows its picker/input mode toggle
 *   button, forwarded to the Material 3 [DatePicker]. Defaults to `false`.
 * @param selectableDates A policy deciding which dates may be selected,
 *   forwarded to the Material 3 [rememberDatePickerState]. Defaults to
 *   [DatePickerDefaults.AllDates].
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
 * @param leadingIcon An optional composable rendered at the start of the
 *   field, typically an [androidx.compose.material3.Icon]. When `null`, no
 *   leading icon is shown.
 * @param trailingIcon An optional composable rendered at the end of the
 *   field, typically an [androidx.compose.material3.Icon]. When `null`, no
 *   trailing icon is shown.
 *
 * @see com.segnities007.stylishui.components.atoms.StylishFormTextField
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishDatePickerField(
    value: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    label: String,
    confirmLabel: String,
    dismissLabel: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    formatter: ((LocalDate) -> String)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    showModeToggle: Boolean = false,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    labelContent: @Composable (() -> Unit)? = null,
    placeholderContent: @Composable (() -> Unit)? = null,
    confirmLabelContent: @Composable (() -> Unit)? = null,
    dismissLabelContent: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var showDialog by remember { mutableStateOf(false) }
    val strings = StylishTheme.strings
    val resolvedFormatter = formatter ?: { date: LocalDate ->
        strings.formatDate(date.year, date.monthNumber, date.dayOfMonth)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { showDialog = true },
    ) {
        OutlinedTextField(
            value = value?.let(resolvedFormatter) ?: "",
            onValueChange = {},
            label = labelContent ?: { Text(label) },
            placeholder = placeholderContent ?: { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = true,
            singleLine = true,
            isError = isError,
            supportingText = supportingText?.let { text -> { Text(text) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = value?.toEpochMillis(),
            yearRange = yearRange,
            initialDisplayMode = initialDisplayMode,
            selectableDates = selectableDates,
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
            DatePicker(
                state = datePickerState,
                showModeToggle = showModeToggle,
            )
        }
    }
}

@Preview(name = "Stylish date picker field", showBackground = true, widthDp = 393)
@Composable
private fun StylishDatePickerFieldPreview() {
    StylishTheme(darkTheme = false) {
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
    StylishTheme(darkTheme = false) {
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

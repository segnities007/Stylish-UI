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
@OptIn(kotlin.time.ExperimentalTime::class)
private val defaultDateFormatter: (LocalDate) -> String = { date ->
    val month = date.monthNumber.toString().padStart(2, '0')
    val day = date.dayOfMonth.toString().padStart(2, '0')
    "${date.year}/$month/$day"
}

@OptIn(kotlin.time.ExperimentalTime::class)
private fun LocalDate.toEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    return this.atStartOfDayIn(timeZone).toEpochMilliseconds()
}

@OptIn(kotlin.time.ExperimentalTime::class)
private fun Long.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
}

/** テキストフィールドをタップすると Material3 DatePicker ダイアログが開く日付入力フィールド。 */
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

package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A clock-and-input time picker for selecting a time of day, styled with the
 * Stylish design language.
 *
 * This is the Finish-layer wrapper around the Material 3 [TimePicker] under
 * the [ExperimentalMaterial3Api] opt-in, so callers can use the picker
 * without importing the experimental M3 API. The picker is an inline
 * component; embed it in a screen or inside
 * [StylishTimePickerDialog] for a modal flow. The default [state] follows
 * the system's 24-hour preference via [rememberTimePickerState]; supply an
 * explicit [TimePickerState] to hoist or control the time.
 *
 * @param state The [TimePickerState] driving the picker, exposing [hour] and
 *   [minute]. Defaults to [rememberTimePickerState], which follows the
 *   system 24-hour format preference.
 * @param modifier Modifier applied to the picker root.
 * @param colors The [TimePickerColors] for the picker in its different
 *   states. Defaults to [TimePickerDefaults.colors], resolved from the
 *   active theme.
 * @param layoutType The [TimePickerLayoutType] controlling the picker's
 *   arrangement. Defaults to [TimePickerDefaults.layoutType], which chooses
 *   vertical or horizontal based on available space.
 *
 * @see StylishTimePickerDialog
 * @see TimePicker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishTimePicker(
    state: TimePickerState = rememberTimePickerState(),
    modifier: Modifier = Modifier,
    colors: TimePickerColors = TimePickerDefaults.colors(),
    layoutType: TimePickerLayoutType = TimePickerDefaults.layoutType(),
) {
    TimePicker(
        state = state,
        modifier = modifier,
        colors = colors,
        layoutType = layoutType,
    )
}

/**
 * A dialog hosting a [StylishTimePicker], styled with the Stylish design
 * language.
 *
 * This is the Finish-layer wrapper around the Material 3 [TimePickerDialog]
 * under the [ExperimentalMaterial3Api] opt-in. The dialog surface uses a
 * [RoundedCornerShape] with
 * [StylishTheme.dimensions.connectedCornerRadius] and the theme's
 * `surfaceContainerHigh` color; like the M3 dialog it applies platform
 * window insets itself, so no additional inset handling is needed. The
 * [confirmButton], [dismissButton], [modeToggleButton], and [title] slots
 * are forwarded unchanged.
 *
 * @param onDismissRequest Called when the user dismisses the dialog by
 *   clicking outside or pressing back.
 * @param confirmButton The button that confirms the selection, typically a
 *   [TextButton].
 * @param title The dialog title, typically a [Text] or a slot wrapping one.
 * @param modifier Modifier applied to the dialog content.
 * @param properties Platform-specific [DialogProperties]. Defaults to the M3
 *   time-picker defaults (`usePlatformDefaultWidth = false`).
 * @param modeToggleButton An optional toggle switching between clock and text
 *   input modes. When `null`, no toggle is shown.
 * @param dismissButton The button that dismisses the dialog, typically a
 *   [TextButton]. When `null`, no dismiss button is shown.
 * @param shape The dialog surface shape. Defaults to [RoundedCornerShape]
 *   with [StylishTheme.dimensions.connectedCornerRadius].
 * @param containerColor The dialog surface color. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param content The dialog body, typically a [StylishTimePicker] (or any
 *   [TimePicker] variant). Receives [ColumnScope].
 *
 * @see StylishTimePicker
 * @see TimePickerDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishTimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    modeToggleButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    content: @Composable ColumnScope.() -> Unit,
) {
    TimePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        title = title,
        modifier = modifier,
        properties = properties,
        modeToggleButton = modeToggleButton,
        dismissButton = dismissButton,
        shape = shape,
        containerColor = containerColor,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish time picker", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishTimePickerPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishTimePicker()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Stylish time picker dialog", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishTimePickerDialogPreview() {
    StylishTheme(darkTheme = false) {
        StylishTimePickerDialog(
            onDismissRequest = {},
            title = { Text("時刻を選択") },
            confirmButton = { TextButton(onClick = {}) { Text("OK") } },
            dismissButton = { TextButton(onClick = {}) { Text("キャンセル") } },
        ) {
            StylishTimePicker()
        }
    }
}

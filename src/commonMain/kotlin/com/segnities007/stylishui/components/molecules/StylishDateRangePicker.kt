package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

private val StylishDateRangePickerTitlePadding = PaddingValues(start = 64.dp, end = 12.dp)
private val StylishDateRangePickerHeadlinePadding =
    PaddingValues(start = 64.dp, end = 12.dp, bottom = 12.dp)

/**
 * A calendar-based date range picker for selecting a start and end date,
 * rendered with the active theme's Material 3 [DatePickerColors].
 *
 * This is the Finish-layer wrapper around the Material 3 [DateRangePicker]
 * under the [ExperimentalMaterial3Api] opt-in, so callers can embed the
 * picker without importing the experimental M3 API. The picker is an inline
 * component; embed it in a screen or dialog and create its state with
 * [rememberStylishDateRangePickerState]. The default [title] and [headline]
 * mirror the M3 defaults (selection summary above the calendar, with a mode
 * toggle to switch to direct date input when [showModeToggle] is `true`).
 *
 * @param state The [DateRangePickerState] driving the picker, exposing
 *   [DateRangePickerState.selectedStartDateMillis] and
 *   [DateRangePickerState.selectedEndDateMillis].
 * @param modifier Modifier applied to the picker root.
 * @param dateFormatter The [DatePickerFormatter] used to display dates.
 *   Defaults to [DatePickerDefaults.dateFormatter].
 * @param colors The [DatePickerColors] resolving the picker's colors.
 *   Defaults to [DatePickerDefaults.colors] from the active theme.
 * @param title An optional composable displayed above the calendar as the
 *   picker title. Defaults to the M3 title showing the current display mode.
 * @param headline An optional composable showing the currently selected
 *   range. Defaults to the M3 range headline.
 * @param showModeToggle Whether to show the toggle switching between the
 *   calendar and the date range input mode. Defaults to `true`.
 * @param focusRequester A [FocusRequester] used to focus the text field when
 *   the picker is in input mode. Pass `null` to skip focusing. Defaults to a
 *   remembered [FocusRequester].
 *
 * @see DateRangePicker
 * @see StylishDatePickerField
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishDateRangePicker(
    state: DateRangePickerState,
    modifier: Modifier = Modifier,
    dateFormatter: DatePickerFormatter = remember { DatePickerDefaults.dateFormatter() },
    colors: DatePickerColors = DatePickerDefaults.colors(),
    title: (@Composable () -> Unit)? = {
        DateRangePickerDefaults.DateRangePickerTitle(
            displayMode = state.displayMode,
            modifier = Modifier.padding(StylishDateRangePickerTitlePadding),
            contentColor = colors.titleContentColor,
        )
    },
    headline: (@Composable () -> Unit)? = {
        DateRangePickerDefaults.DateRangePickerHeadline(
            selectedStartDateMillis = state.selectedStartDateMillis,
            selectedEndDateMillis = state.selectedEndDateMillis,
            displayMode = state.displayMode,
            dateFormatter = dateFormatter,
            modifier = Modifier.padding(StylishDateRangePickerHeadlinePadding),
            contentColor = colors.headlineContentColor,
        )
    },
    showModeToggle: Boolean = true,
    focusRequester: FocusRequester? = remember { FocusRequester() },
) {
    DateRangePicker(
        state = state,
        modifier = modifier,
        dateFormatter = dateFormatter,
        colors = colors,
        title = title,
        headline = headline,
        showModeToggle = showModeToggle,
        focusRequester = focusRequester,
    )
}

/**
 * Creates and remembers a [DateRangePickerState] for use with
 * [StylishDateRangePicker].
 *
 * This is the Finish-layer wrapper around the Material 3
 * [rememberDateRangePickerState], keeping the experimental M3 API behind the
 * Stylish opt-in.
 *
 * @param initialSelectedStartDateMillis The initially selected start date as
 *   epoch milliseconds (UTC midnight), or `null` for none.
 * @param initialSelectedEndDateMillis The initially selected end date as
 *   epoch milliseconds (UTC midnight), or `null` for none.
 * @param initialDisplayedMonthMillis The initially displayed month as epoch
 *   milliseconds, or `null` to default to the selected start date's month.
 * @param yearRange The range of selectable years. Defaults to
 *   [DatePickerDefaults.YearRange].
 * @param initialDisplayMode The mode the picker opens in. Defaults to the
 *   calendar picker mode.
 * @param selectableDates A policy deciding which dates may be selected.
 *   Defaults to [DatePickerDefaults.AllDates].
 *
 * @see StylishDateRangePicker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun rememberStylishDateRangePickerState(
    initialSelectedStartDateMillis: Long? = null,
    initialSelectedEndDateMillis: Long? = null,
    initialDisplayedMonthMillis: Long? = initialSelectedStartDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
): DateRangePickerState =
    rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialSelectedStartDateMillis,
        initialSelectedEndDateMillis = initialSelectedEndDateMillis,
        initialDisplayedMonthMillis = initialDisplayedMonthMillis,
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode,
        selectableDates = selectableDates,
    )

@Preview(name = "Stylish date range picker", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishDateRangePickerPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.fillMaxSize()) {
            StylishDateRangePicker(
                state = rememberStylishDateRangePickerState(
                    initialSelectedStartDateMillis = 1_783_123_200_000,
                    initialSelectedEndDateMillis = 1_784_217_600_000,
                ),
                modifier = Modifier.padding(20.dp),
            )
        }
    }
}

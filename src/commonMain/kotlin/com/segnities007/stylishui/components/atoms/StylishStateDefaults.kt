@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.segnities007.stylishui.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.segnities007.stylishui.components.models.StylishDatePickerState
import com.segnities007.stylishui.components.models.StylishDrawerState
import com.segnities007.stylishui.components.models.StylishDrawerValue
import com.segnities007.stylishui.components.models.StylishSheetState
import com.segnities007.stylishui.components.models.StylishSheetValue
import com.segnities007.stylishui.components.models.StylishSnackbarHostState
import com.segnities007.stylishui.components.models.StylishTimePickerState

/** Remember a snackbar queue without importing Material types. */
@Composable
public fun rememberStylishSnackbarHostState(): StylishSnackbarHostState =
    remember { StylishSnackbarHostState() }

/** Saveable drawer state; use the returned state's open/close suspend functions. */
@Composable
public fun rememberStylishDrawerState(
    initialValue: StylishDrawerValue = StylishDrawerValue.Closed,
    confirmStateChange: (StylishDrawerValue) -> Boolean = { true },
): StylishDrawerState = androidx.compose.material3.rememberDrawerState(initialValue, confirmStateChange)

/** Modal sheet state with host-controlled transition confirmation. */
@Composable
public fun rememberStylishModalBottomSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (StylishSheetValue) -> Boolean = { true },
): StylishSheetState = androidx.compose.material3.rememberModalBottomSheetState(
    skipPartiallyExpanded = skipPartiallyExpanded,
    confirmValueChange = confirmValueChange,
)

/** Saveable calendar state. [initialSelectedDateMillis] is a UTC date in milliseconds. */
@Composable
public fun rememberStylishDatePickerState(
    initialSelectedDateMillis: Long? = null,
): StylishDatePickerState = androidx.compose.material3.rememberDatePickerState(
    initialSelectedDateMillis = initialSelectedDateMillis,
)

/** Saveable clock state with an explicit locale/time-format decision from the host. */
@Composable
public fun rememberStylishTimePickerState(
    initialHour: Int = 0,
    initialMinute: Int = 0,
    is24Hour: Boolean = true,
): StylishTimePickerState = androidx.compose.material3.rememberTimePickerState(
    initialHour = initialHour,
    initialMinute = initialMinute,
    is24Hour = is24Hour,
)

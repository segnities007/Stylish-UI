package com.segnities007.stylishui.catalog.harness

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.segnities007.stylishui.components.models.StylishContentState
import com.segnities007.stylishui.components.models.StylishScreenDocument
import com.segnities007.stylishui.components.models.StylishScreenElement
import com.segnities007.stylishui.components.models.StylishScreenEvent
import com.segnities007.stylishui.components.patterns.StylishScreen

/** Compile-time consumer: a complete editable screen with no Material or layout imports. */
@Composable
internal fun SettingsScreen() {
    var name by rememberSaveable { mutableStateOf("Alex") }
    var sync by rememberSaveable { mutableStateOf(true) }
    var saved by rememberSaveable { mutableStateOf(false) }
    StylishScreen(
        title = "Settings",
        state = StylishContentState.Content(StylishScreenDocument(listOf(
            StylishScreenElement.Input("name", "Name", name),
            StylishScreenElement.Toggle("sync", "Sync", sync),
            StylishScreenElement.Action("save", "Save", enabled = name.isNotBlank()),
            StylishScreenElement.Text("status", if (saved) "Saved" else "Edit your preferences"),
        ))),
        onEvent = { event ->
            when (event) {
                is StylishScreenEvent.Edit -> { name = event.value; saved = false }
                is StylishScreenEvent.Toggle -> { sync = event.checked; saved = false }
                is StylishScreenEvent.Activate -> saved = true
                StylishScreenEvent.Retry -> Unit
            }
        },
    )
}

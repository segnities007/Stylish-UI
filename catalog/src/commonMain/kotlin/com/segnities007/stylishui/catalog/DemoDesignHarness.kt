package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.catalog.harness.SettingsScreen

internal fun getDesignHarnessDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "StylishScreen",
        category = DemoCategory.Patterns,
        preview = {
            Box(Modifier.fillMaxWidth().height(480.dp)) {
                SettingsScreen()
            }
        },
        code = "StylishScreen(title, state, onEvent)",
    ),
)

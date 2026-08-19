package com.segnities007.stylishui.website

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.segnities007.stylishui.catalog.StylishPlayground
import com.segnities007.stylishui.theme.StylishTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Stylish UI - Official Website",
    ) {
        var darkTheme by remember { mutableStateOf(true) }
        StylishTheme(darkTheme = darkTheme) {
            Surface(Modifier.fillMaxSize()) {
                StylishPlayground(
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlaygroundPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.fillMaxSize()) {
            StylishPlayground(
                darkTheme = false,
                onToggleTheme = {},
            )
        }
    }
}

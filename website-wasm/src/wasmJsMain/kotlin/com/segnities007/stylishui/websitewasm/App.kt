package com.segnities007.stylishui.websitewasm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.segnities007.stylishui.catalog.StylishPlayground
import com.segnities007.stylishui.theme.StylishTheme

@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }
    StylishTheme(darkTheme = darkTheme) {
        Surface(Modifier.fillMaxSize()) {
            StylishPlayground(
                darkTheme = darkTheme,
                onToggleTheme = { darkTheme = !darkTheme },
            )
        }
    }
}

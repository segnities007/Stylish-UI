package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Scrollable container with a common cross-platform API.
 *
 * @param modifier Modifier applied to the scrollable column.
 * @param content Content displayed inside the scrollable area.
 */
@Composable
public fun StylishScrollArea(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier.verticalScroll(rememberScrollState())) { content() }
}

@Preview(name = "Stylish scroll area", showBackground = true, heightDp = 120)
@Composable
private fun StylishScrollAreaPreview() {
    StylishTheme(darkTheme = false) {
        StylishScrollArea {
            repeat(8) { Text("Scrollable item ${it + 1}") }
        }
    }
}

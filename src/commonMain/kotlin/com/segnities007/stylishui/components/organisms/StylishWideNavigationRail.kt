package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/** Wide navigation rail variant that reserves space for persistent labels. */
@Composable
public fun StylishWideNavigationRail(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    StylishNavigationRail(
        modifier = modifier.stylishTestTag("wide_navigation_rail").widthIn(min = 176.dp),
        content = content,
    )
}

@Preview(name = "Stylish wide navigation rail", showBackground = true, widthDp = 220)
@Composable
private fun StylishWideNavigationRailPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishWideNavigationRail {
                StylishNavigationRailItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "ホーム") },
                    label = { Text("ホーム") },
                )
                StylishNavigationRailItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Settings, contentDescription = "設定") },
                    label = { Text("設定") },
                )
            }
        }
    }
}

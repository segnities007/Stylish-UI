package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFloatingVisibility
import com.segnities007.stylishui.components.molecules.StylishGradientFooterItemContent
import com.segnities007.stylishui.components.models.StylishGradientFooterItem
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A theme-aware footer whose upper edge fades into the scrolling content.
 * Navigation controls and the system navigation inset have an opaque page-colored
 * background so content cannot show through the icons or labels.
 *
 * The layout and color treatment are intentionally fixed so the footer keeps
 * the same visual rhythm across products; callers provide only destinations
 * and selection state.
 *
 * The footer uses the shared floating-surface motion: it fades and slides from
 * the bottom edge when [visible] changes, and honors reduced-motion settings.
 *
 * @param visible Whether the footer should be composed and shown.
 */
@Composable
public fun StylishGradientFooter(
    items: List<StylishGradientFooterItem>,
    selectedKey: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    val background = MaterialTheme.colorScheme.background
    // Keep the fade above the icon/label artwork, independent of system inset height.
    val fadeHeightPx = with(LocalDensity.current) { 20.dp.toPx() }
    StylishFloatingVisibility(
        visible = visible,
        direction = StylishFloatingSlideDirection.Down,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to background.copy(alpha = 0f),
                        0.1f to background.copy(alpha = 0.2f),
                        0.2f to background.copy(alpha = 0.4f),
                        0.3f to background.copy(alpha = 0.6f),
                        0.4f to background.copy(alpha = 0.8f),
                        1f to background.copy(alpha = .9f),
                        endY = fadeHeightPx,
                    ),
                )
                .navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                items.forEach { item ->
                    StylishGradientFooterItemContent(
                        modifier = Modifier.weight(1f),
                        item = item,
                        selected = item.key == selectedKey,
                        onClick = { onItemClick(item.key) },
                    )
                }
            }
        }
    }
}

@Preview(name = "Gradient footer - light", showBackground = true, widthDp = 393, heightDp = 160)
@Composable
private fun StylishGradientFooterLightPreview() {
    StylishGradientFooterPreview(darkTheme = false)
}

@Preview(name = "Gradient footer - dark", showBackground = true, widthDp = 393, heightDp = 160)
@Composable
private fun StylishGradientFooterDarkPreview() {
    StylishGradientFooterPreview(darkTheme = true)
}

@Composable
private fun StylishGradientFooterPreview(darkTheme: Boolean) {
    StylishTheme(darkTheme = darkTheme) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            Text(
                text = if (darkTheme) {
                    "Dark theme memo content behind the footer. ".repeat(24)
                } else {
                    "Light theme memo content behind the footer. ".repeat(24)
                },
                color = MaterialTheme.colorScheme.onBackground,
            )
            StylishGradientFooter(
                items = listOf(
                    StylishGradientFooterItem("memo", Icons.Default.Home, "メモ"),
                    StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
                    StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
                ),
                selectedKey = "memo",
                onItemClick = {},
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

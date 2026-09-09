package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFloatingVisibility
import com.segnities007.stylishui.components.models.StylishGradientFooterItem
import com.segnities007.stylishui.components.molecules.StylishGradientFooterItemContent
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A theme-aware vertical navigation rail wearing the
 * [StylishGradientFooter] treatment: each destination renders through
 * [StylishGradientFooterItemContent], and the rail's content-facing edge
 * fades into the page behind it.
 *
 * Place this on the leading edge (for example as the first child of a
 * [Row]); the fade is mirrored automatically in RTL layouts so it always
 * faces the content. Callers provide only destinations and selection state.
 *
 * The rail uses the shared floating-surface motion: it fades and slides
 * from the start edge when [visible] changes, and honors reduced-motion
 * settings.
 *
 * @param items Destinations rendered top to bottom.
 * @param selectedKey Key of the currently selected destination, or `null`
 *   when nothing is selected.
 * @param onItemClick Called with the tapped destination's
 *   [StylishGradientFooterItem.key].
 * @param modifier Modifier applied to the rail root.
 * @param visible Whether the rail should be composed and shown.
 */
@Composable
public fun StylishGradientNavigationRail(
    items: List<StylishGradientFooterItem>,
    selectedKey: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    val background = MaterialTheme.colorScheme.background
    // Keep the fade beside the icon/label artwork, independent of rail width.
    val fadeWidthPx = with(LocalDensity.current) { 20.dp.toPx() }
    val opaque = background.copy(alpha = .9f)
    val stops = listOf(
        0f to opaque,
        0.2f to background.copy(alpha = .8f),
        0.4f to background.copy(alpha = .6f),
        0.6f to background.copy(alpha = .4f),
        0.8f to background.copy(alpha = .2f),
        1f to background.copy(alpha = 0f),
    )
    // Opaque behind the artwork, fading toward the content edge, mirrored
    // in RTL so the fade always faces the page content.
    val fade = if (LocalLayoutDirection.current == LayoutDirection.Ltr) {
        Brush.horizontalGradient(*stops.toTypedArray(), endX = fadeWidthPx)
    } else {
        Brush.horizontalGradient(*stops.reversed().toTypedArray(), endX = fadeWidthPx)
    }
    StylishFloatingVisibility(
        visible = visible,
        direction = StylishFloatingSlideDirection.Start,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(fade),
            contentAlignment = Alignment.Center,
        ) {
            RailDestinations(items = items, selectedKey = selectedKey, onItemClick = onItemClick)
        }
    }
}

/**
 * Destination column shared by the gradient rail layout.
 *
 * Spaced sequentially (never evenly) so the rail also measures correctly
 * inside unbounded parents such as lazy grid items.
 */
@Composable
private fun RailDestinations(
    items: List<StylishGradientFooterItem>,
    selectedKey: String?,
    onItemClick: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
    ) {
        items.forEach { item ->
            StylishGradientFooterItemContent(
                item = item,
                selected = item.key == selectedKey,
                onClick = { onItemClick(item.key) },
            )
        }
    }
}

@Preview(name = "Gradient navigation rail - light", showBackground = true, widthDp = 200, heightDp = 320)
@Composable
private fun StylishGradientNavigationRailLightPreview() {
    StylishGradientNavigationRailPreview(darkTheme = false)
}

@Preview(name = "Gradient navigation rail - dark", showBackground = true, widthDp = 200, heightDp = 320)
@Composable
private fun StylishGradientNavigationRailDarkPreview() {
    StylishGradientNavigationRailPreview(darkTheme = true)
}

@Composable
private fun StylishGradientNavigationRailPreview(darkTheme: Boolean) {
    StylishTheme(darkTheme = darkTheme) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) {
            Text(
                text = "Content behind the rail. ".repeat(24),
                color = MaterialTheme.colorScheme.onBackground,
            )
            StylishGradientNavigationRail(
                items = listOf(
                    StylishGradientFooterItem("memo", Icons.Default.Home, "メモ"),
                    StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
                    StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
                ),
                selectedKey = "memo",
                onItemClick = {},
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }
    }
}

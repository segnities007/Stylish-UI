package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFloatingVisibility
import com.segnities007.stylishui.components.models.StylishGradientFooterItem
import com.segnities007.stylishui.components.molecules.StylishGradientFooterItemContent
import com.segnities007.stylishui.foundation.StylishFloatingSlideDirection
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.StylishElevationLayer
import com.segnities007.stylishui.theme.stylishFloatingContainerColor

/**
 * A floating vertical navigation rail wearing the
 * [StylishFloatingBottomBar] treatment: a translucent outlined pill
 * with tonal elevation and shadow. Each destination renders through
 * [StylishGradientFooterItemContent], so labels, selection colors, and
 * touch targets match the gradient footer while the container floats
 * above content like the bottom bar.
 *
 * Destinations form a single mutually exclusive group for assistive
 * technologies. The rail uses the shared floating-surface motion: it
 * fades and slides from the start edge when [visible] changes, and
 * honors reduced-motion settings.
 *
 * @param items Destinations rendered top to bottom.
 * @param selectedKey Key of the currently selected destination, or `null`
 *   when nothing is selected.
 * @param onItemClick Called with the tapped destination's
 *   [StylishGradientFooterItem.key].
 * @param modifier Modifier applied to the rail root.
 * @param visible Whether the rail should be composed and shown.
 * @param shape Shape of the floating surface. Defaults to the shared floating
 *   corner radius.
 * @param containerColor Background color of the rail. Defaults to the shared
 *   floating container color.
 * @param contentColor Default content color propagated to destinations.
 * @param border Optional outline around the floating surface. Defaults to a
 *   hairline using `MaterialTheme.colorScheme.outlineVariant`.
 * @param tonalElevation Tonal elevation of the floating surface.
 * @param shadowElevation Drop-shadow elevation of the floating surface.
 * @param contentPadding Padding applied around the destinations. Defaults to
 *   8 dp.
 */
@Composable
public fun StylishFloatingNavigationRail(
    items: List<StylishGradientFooterItem>,
    selectedKey: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(StylishTheme.shapes.floatingCornerRadius),
    containerColor: Color = stylishFloatingContainerColor(),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    contentPadding: PaddingValues = PaddingValues(8.dp),
) {
    StylishFloatingVisibility(
        visible = visible,
        direction = StylishFloatingSlideDirection.Start,
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier.stylishTestTag("floating_navigation_rail"),
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
            border = border,
        ) {
            StylishElevationLayer {
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .semantics { selectableGroup() },
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
        }
    }
}

@Preview(name = "Floating navigation rail - light", showBackground = true, widthDp = 200, heightDp = 320)
@Composable
private fun StylishFloatingNavigationRailLightPreview() {
    StylishFloatingNavigationRailPreview(darkTheme = false)
}

@Preview(name = "Floating navigation rail - dark", showBackground = true, widthDp = 200, heightDp = 320)
@Composable
private fun StylishFloatingNavigationRailDarkPreview() {
    StylishFloatingNavigationRailPreview(darkTheme = true)
}

@Composable
private fun StylishFloatingNavigationRailPreview(darkTheme: Boolean) {
    StylishTheme(darkTheme = darkTheme) {
        Surface {
            StylishFloatingNavigationRail(
                items = listOf(
                    StylishGradientFooterItem("memo", Icons.Default.Home, "メモ"),
                    StylishGradientFooterItem("secure", Icons.Default.Lock, "保護"),
                    StylishGradientFooterItem("settings", Icons.Default.Settings, "設定"),
                ),
                selectedKey = "secure",
                onItemClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

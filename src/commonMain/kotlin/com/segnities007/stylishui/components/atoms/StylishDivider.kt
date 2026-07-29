package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A horizontal rule styled with the Stylish hairline outline.
 * Wraps [HorizontalDivider] with
 * [StylishTheme.dimensions.outlineWidth] as the default thickness
 * and [MaterialTheme.colorScheme.outlineVariant] as the default
 * color, keeping dividers visually consistent with connected outlines.
 *
 * @param modifier Modifier applied to the divider.
 * @param thickness Thickness of the line. Defaults to
 *   [StylishTheme.dimensions.outlineWidth].
 * @param color Color of the line. Defaults to
 *   [MaterialTheme.colorScheme.outlineVariant].
 *
 * @see StylishVerticalDivider
 */
@Composable
public fun StylishHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = StylishTheme.dimensions.outlineWidth,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
) {
    HorizontalDivider(modifier = modifier, thickness = thickness, color = color)
}

/**
 * A vertical rule styled with the Stylish hairline outline.
 * Wraps [VerticalDivider] with
 * [StylishTheme.dimensions.outlineWidth] as the default thickness
 * and [MaterialTheme.colorScheme.outlineVariant] as the default
 * color.
 *
 * @param modifier Modifier applied to the divider.
 * @param thickness Thickness of the line. Defaults to
 *   [StylishTheme.dimensions.outlineWidth].
 * @param color Color of the line. Defaults to
 *   [MaterialTheme.colorScheme.outlineVariant].
 *
 * @see StylishHorizontalDivider
 */
@Composable
public fun StylishVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = StylishTheme.dimensions.outlineWidth,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
) {
    VerticalDivider(modifier = modifier, thickness = thickness, color = color)
}

@Preview(name = "Stylish horizontal divider", showBackground = true, widthDp = 393)
@Composable
private fun StylishHorizontalDividerPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishHorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}
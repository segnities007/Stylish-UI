package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A small notification badge that communicates a count or status at a
 * glance. Intended for the badge slot of a navigation item (see
 * [StylishNavigationBar]) and similar compact contexts such as icon
 * buttons or tab bars.
 *
 * The [content] is rendered inside a pill-shaped [Surface] using the
 * error color scheme by default, with
 * `MaterialTheme.typography.labelSmall` provided as the default
 * [LocalTextStyle] so plain [Text] children pick up the compact badge
 * typography automatically. Text color falls back to [contentColor]
 * via `LocalContentColor`.
 *
 * @param modifier Modifier applied to the [Surface] root.
 * @param containerColor Background color of the badge. Defaults to
 *   `MaterialTheme.colorScheme.error`.
 * @param contentColor Default color for the badge content. Defaults to
 *   `MaterialTheme.colorScheme.onError`.
 * @param shape Shape of the badge surface. Defaults to [CircleShape]
 *   (the classic pill).
 * @param content Content inside the badge, typically a short [Text]
 *   such as a count (`"3"`, `"99+"`) or a dot.
 *
 * @see StylishNavigationBar
 */
@Composable
public fun StylishBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
    shape: Shape = CircleShape,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp,
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelSmall) {
            Box(
                Modifier.padding(horizontal = StylishTheme.dimensions.inlineSpacing),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}

@Preview(name = "Stylish badge", showBackground = true, widthDp = 393)
@Composable
private fun StylishBadgePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishBadge { Text("99+") }
        }
    }
}

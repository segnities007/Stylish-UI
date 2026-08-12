package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A layout box that positions a [badge] relative to its [content],
 * wrapping the Material 3 [BadgedBox] so it can be themed consistently
 * with the rest of Stylish UI.
 *
 * Use this to attach a notification badge (e.g. a [Badge] with a count)
 * to an icon or other anchor, typically in navigation bars, tab bars,
 * or action rows. The badge is placed at the top-end corner of the
 * content and may overflow it.
 *
 * @param badge The badge to display, typically a [Badge] with a short
 *   count text. Receives a [BoxScope] for alignment control.
 * @param modifier Modifier applied to the [BadgedBox] root.
 * @param content The anchor content to which the badge is positioned,
 *   typically an [Icon]. Receives a [BoxScope].
 *
 * @see StylishBadge
 */
@Composable
public fun StylishBadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    BadgedBox(
        badge = badge,
        modifier = modifier,
        content = content,
    )
}

@Preview(name = "Stylish badged box", showBackground = true, widthDp = 393)
@Composable
private fun StylishBadgedBoxPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishBadgedBox(
                badge = {
                    Badge { Text("3") }
                },
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "ホーム",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A stacked avatar group — the web "Avatar.Group/AvatarGroup" pattern
 * from Ant Design and MUI.
 *
 * Renders [count] avatars overlapping by [overlap], with the surface
 * color drawn between them so each avatar keeps a visible ring. Each
 * avatar is composed by [avatar]; the default renderer shows the
 * initials `"U$index"` — pass a custom lambda (e.g. using
 * [StylishAvatar]) for real data.
 *
 * @param count Number of avatars to display.
 * @param modifier Modifier applied to the root row.
 * @param size Diameter of each avatar. Defaults to 32 dp.
 * @param overlap How much each subsequent avatar covers the previous
 *   one. Defaults to 12 dp.
 * @param ringColor Color drawn as the ring between overlapping avatars.
 *   Defaults to [MaterialTheme.colorScheme.surface].
 * @param avatar Composes the avatar content at [index]. Defaults to
 *   initials `"U$index"`.
 */
@Composable
public fun StylishAvatarGroup(
    count: Int,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    overlap: Dp = 12.dp,
    ringColor: Color = MaterialTheme.colorScheme.surface,
    avatar: @Composable (index: Int) -> Unit = { index ->
        Text(
            "U$index",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    },
) {
    Row(modifier = modifier) {
        repeat(count) { index ->
            Box(
                Modifier
                    .offset(x = if (index == 0) 0.dp else -overlap)
                    .size(size)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(2.dp, ringColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                avatar(index)
            }
        }
    }
}

@Preview(name = "Stylish avatar group", showBackground = true, widthDp = 393)
@Composable
private fun StylishAvatarGroupPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishAvatarGroup(
                count = 4,
                size = 40.dp,
                avatar = { index -> StylishAvatar(initials = "U$index", size = 36.dp) },
            )
        }
    }
}

package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A circular avatar that shows a person's initials, an arbitrary image,
 * or caller-supplied content.
 *
 * When neither [avatarImage] nor [content] is provided, [initials] are
 * rendered in [MaterialTheme.typography.titleMedium], truncated to at
 * most three characters. When [avatarImage] is provided it is clipped
 * to the circle and fills the avatar. [content] takes precedence over
 * [avatarImage].
 *
 * Accessibility: when [initials] is non-blank and no custom
 * [avatarImage] or [content] is supplied, the initials are exposed as
 * the avatar's [androidx.compose.ui.semantics.contentDescription].
 * For image avatars, set an appropriate description inside [avatarImage]
 * (e.g. via [Modifier.semantics]) since the library cannot infer it.
 *
 * The root carries the default test tag `stylish_avatar` for UI tests;
 * callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
 *
 * @param initials Short text shown in the avatar, typically a person's
 *   initials (max 3 characters are displayed). Ignored when
 *   [avatarImage] or [content] is provided.
 * @param modifier Modifier applied to the avatar surface. The size is
 *   enforced via [size], overriding any incoming size modifier.
 * @param size Diameter of the avatar. Defaults to 40.dp.
 * @param containerColor Background color of the avatar. Defaults to
 *   [MaterialTheme.colorScheme.primaryContainer].
 * @param contentColor Default content color (text/icon) inside the
 *   avatar. Defaults to
 *   [MaterialTheme.colorScheme.onPrimaryContainer].
 * @param avatarImage Optional image content, clipped to the circle and
 *   scaled to fill the avatar. Ignored when [content] is provided.
 * @param content Optional fully custom content, drawn centered inside
 *   the circle and taking precedence over [avatarImage] and [initials].
 */
@Composable
public fun StylishAvatar(
    initials: String = "",
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    avatarImage: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .testTag("stylish_avatar")
            .size(size)
            .then(
                if (initials.isNotBlank() && avatarImage == null && content == null) {
                    Modifier.semantics { contentDescription = initials }
                } else {
                    Modifier
                },
            ),
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when {
                content != null -> content()
                avatarImage != null -> Box(
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                ) {
                    avatarImage()
                }

                else -> Text(
                    text = initials.trim().take(3),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(name = "Stylish avatar initials", showBackground = true, widthDp = 393)
@Composable
private fun StylishAvatarInitialsPreview() {
    StylishTheme(darkTheme = false) {
        Box(Modifier.padding(20.dp)) {
            StylishAvatar(initials = "SM")
        }
    }
}

@Preview(name = "Stylish avatar image", showBackground = true, widthDp = 393)
@Composable
private fun StylishAvatarImagePreview() {
    StylishTheme(darkTheme = false) {
        Box(Modifier.padding(20.dp)) {
            StylishAvatar(
                size = 56.dp,
                avatarImage = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "プロフィール画像",
                        modifier = Modifier.fillMaxSize(),
                    )
                },
            )
        }
    }
}

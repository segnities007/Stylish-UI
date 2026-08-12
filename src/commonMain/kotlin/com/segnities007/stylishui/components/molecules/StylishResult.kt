package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Visual variants of a [StylishResult] status page.
 */
public enum class StylishResultVariant { Info, Success, Error, Warning }

/**
 * A full status page — the web "Result" pattern from Ant Design.
 *
 * Shows a large variant icon, a [title], an optional [description], and
 * an optional [action] slot (e.g. a button row). Use it for
 * success/error/empty outcomes of complete flows (form submission,
 * payment, sign-in, etc.).
 *
 * @param title The status heading.
 * @param modifier Modifier applied to the root column.
 * @param variant The semantic variant controlling icon and colors.
 * @param description Optional supporting text under the title.
 * @param icon Optional leading icon override. Defaults to the variant
 *   icon.
 * @param titleStyle Typography of [title]. Defaults to
 *   [MaterialTheme.typography.headlineSmall].
 * @param descriptionStyle Typography of [description]. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 * @param contentColor Foreground color of the text. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param iconSize Edge length of the leading icon. Defaults to 64 dp.
 * @param action Optional trailing slot rendered below the description
 *   (e.g. confirm/cancel buttons).
 */
@Composable
public fun StylishResult(
    title: String,
    modifier: Modifier = Modifier,
    variant: StylishResultVariant = StylishResultVariant.Info,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconSize: androidx.compose.ui.unit.Dp = 64.dp,
    action: (@Composable () -> Unit)? = null,
) {
    val iconColor = when (variant) {
        StylishResultVariant.Info -> MaterialTheme.colorScheme.primary
        StylishResultVariant.Success -> MaterialTheme.colorScheme.primary
        StylishResultVariant.Error -> MaterialTheme.colorScheme.error
        StylishResultVariant.Warning -> MaterialTheme.colorScheme.error
    }

    Column(
        modifier = modifier.padding(StylishTheme.dimensions.contentSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
    ) {
        if (icon != null) {
            icon()
        } else {
            Icon(
                imageVector = when (variant) {
                    StylishResultVariant.Info -> Icons.Default.Info
                    StylishResultVariant.Success -> Icons.Default.CheckCircle
                    StylishResultVariant.Error -> Icons.Default.Error
                    StylishResultVariant.Warning -> Icons.Default.Warning
                },
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(iconSize),
            )
        }
        Text(
            title,
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        if (description != null) {
            Text(
                description,
                style = descriptionStyle,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (action != null) {
            action()
        }
    }
}

@Preview(name = "Stylish result", showBackground = true, widthDp = 393)
@Composable
private fun StylishResultPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishResult(
                title = "送信が完了しました",
                description = "お問い合わせを受け付けました。担当者より折り返しご連絡いたします。",
                variant = StylishResultVariant.Success,
            )
        }
    }
}

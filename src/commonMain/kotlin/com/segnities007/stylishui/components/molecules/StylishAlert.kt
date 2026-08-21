package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * Visual variants of a [StylishAlert].
 *
 * @property Info Informational message (primary colors).
 * @property Success Successful operation (primary colors with a check
 *   icon).
 * @property Warning Warning message (warning container colors).
 * @property Error Error message (error container colors).
 */
public enum class StylishAlertVariant { Info, Success, Warning, Error }

/**
 * An inline alert/banner — the web "Alert" pattern from Ant Design,
 * Chakra UI, and MUI.
 *
 * Renders [message] (optionally with a [title]) in a tinted surface with
 * a variant icon. Can be dismissed via [onDismiss] and can carry an
 * [action] slot (e.g. a text button).
 *
 * @param message The alert body text.
 * @param modifier Modifier applied to the root surface.
 * @param variant The semantic variant controlling colors and icon.
 * @param title Optional bold heading rendered above [message].
 * @param onDismiss When non-null, a close button is shown and this
 *   callback is invoked on tap. The caller is responsible for hiding the
 *   alert.
 * @param action Optional trailing slot (e.g. a [TextButton]-like
 *   action). Rendered after the message.
 * @param containerColor Optional override for the alert background.
 * @param contentColor Optional override for the foreground.
 * @param shape Corner shape. Defaults to [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param icon Optional leading icon. Defaults to the variant icon.
 */
@Composable
public fun StylishAlert(
    message: String,
    modifier: Modifier = Modifier,
    variant: StylishAlertVariant = StylishAlertVariant.Info,
    title: String? = null,
    onDismiss: (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    icon: (@Composable () -> Unit)? = null,
) {
    val strings = StylishTheme.strings
    val resolvedContainerColor = containerColor ?: when (variant) {
        StylishAlertVariant.Info -> MaterialTheme.colorScheme.primaryContainer
        StylishAlertVariant.Success -> MaterialTheme.colorScheme.primaryContainer
        StylishAlertVariant.Warning -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        StylishAlertVariant.Error -> MaterialTheme.colorScheme.errorContainer
    }
    val resolvedContentColor = contentColor ?: when (variant) {
        StylishAlertVariant.Info -> MaterialTheme.colorScheme.onPrimaryContainer
        StylishAlertVariant.Success -> MaterialTheme.colorScheme.onPrimaryContainer
        StylishAlertVariant.Warning -> MaterialTheme.colorScheme.onErrorContainer
        StylishAlertVariant.Error -> MaterialTheme.colorScheme.onErrorContainer
    }

    Surface(
        shape = shape,
        color = resolvedContainerColor,
        contentColor = resolvedContentColor,
        modifier = modifier.stylishTestTag("alert"),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = StylishTheme.dimensions.controlPadding,
                vertical = StylishTheme.dimensions.controlVerticalPadding,
            ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
        ) {
            if (icon != null) {
                icon()
            } else {
                Icon(
                    imageVector = when (variant) {
                        StylishAlertVariant.Info -> Icons.Default.Info
                        StylishAlertVariant.Success -> Icons.Default.CheckCircle
                        StylishAlertVariant.Warning -> Icons.Default.Warning
                        StylishAlertVariant.Error -> Icons.Default.Error
                    },
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing / 2),
            ) {
                if (title != null) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleSmall,
                        color = resolvedContentColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = resolvedContentColor,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
                if (action != null) {
                    action()
                }
            }
            if (onDismiss != null) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = strings.close,
                        tint = resolvedContentColor,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Preview(name = "Stylish alert", showBackground = true, widthDp = 393)
@Composable
private fun StylishAlertPreview() {
    StylishTheme(darkTheme = false) {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
        ) {
            StylishAlert(title = "お知らせ", message = "新しいバージョンが利用可能です。")
            StylishAlert(
                variant = StylishAlertVariant.Success,
                title = "保存しました",
                message = "変更内容が正常に保存されました。",
            )
            StylishAlert(
                variant = StylishAlertVariant.Error,
                message = "ネットワーク接続に失敗しました。",
                onDismiss = {},
            )
        }
    }
}

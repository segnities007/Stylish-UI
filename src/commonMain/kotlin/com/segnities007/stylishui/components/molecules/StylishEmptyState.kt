package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A placeholder displayed when a list or content area has no data, presenting
 * an optional icon, title, description, and an optional call-to-action.
 *
 * The component centers its children vertically and horizontally within the
 * space granted by [modifier]; callers that want it to occupy all available
 * space add `Modifier.fillMaxSize()` themselves. When an [icon] vector is
 * supplied it is rendered with [iconTint]; provide [iconContent] to replace
 * it with arbitrary composable content, or leave both `null` to omit the icon
 * entirely. The title and description each render a [Text] from their string
 * unless a [titleContent] / [descriptionContent] slot is provided; blank
 * strings without a slot render nothing. The action area renders a
 * [TextButton] with [actionLabel] when both [actionLabel] and [onAction] are
 * non-null, or the custom [action] composable when provided. If neither
 * action parameter is set, no action area is shown.
 *
 * @param icon The optional [ImageVector] displayed above the title. Rendered
 *   only when non-null and [iconContent] is not provided.
 * @param title The headline text describing the empty state. Skipped when
 *   blank and [titleContent] is `null`.
 * @param description The explanatory text shown below the title. Skipped when
 *   blank and [descriptionContent] is `null`.
 * @param actionLabel The label for the optional call-to-action button. When
 *   `null` (or when [onAction] is `null`), no default button is rendered.
 * @param onAction The callback invoked when the default action button is
 *   clicked. When `null`, no default button is rendered even if [actionLabel]
 *   is set.
 * @param titleMaxLines Maximum number of lines for the title. Defaults to
 *   [Int.MAX_VALUE] (unlimited).
 * @param titleOverflow The [TextOverflow] strategy for the title. Defaults to
 *   [TextOverflow.Ellipsis].
 * @param titleStyle The [TextStyle] for the title. Defaults to
 *   [MaterialTheme.typography.headlineMedium].
 * @param descriptionMaxLines Maximum number of lines for the description.
 *   Defaults to [Int.MAX_VALUE] (unlimited).
 * @param descriptionOverflow The [TextOverflow] strategy for the description.
 *   Defaults to [TextOverflow.Ellipsis].
 * @param descriptionStyle The [TextStyle] for the description. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 * @param iconTint The tint color applied to the default [icon]. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param contentColor The color applied to the title and description text.
 *   Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param iconContent An optional custom composable that replaces the default
 *   icon. When `null`, the [icon] vector is rendered (if non-null). Note: the
 *   default icon includes bottom spacing from
 *   [StylishTheme.dimensions.contentSpacing]; custom slots do not inherit this
 *   spacing.
 * @param titleContent An optional custom composable that replaces the default
 *   title [Text]. When `null`, the [title] string is rendered unless blank.
 * @param descriptionContent An optional custom composable that replaces the
 *   default description [Text]. When `null`, the [description] string is
 *   rendered unless blank. Note: the default description includes top spacing
 *   from [StylishTheme.dimensions.itemSpacing]; custom slots do not inherit
 *   this spacing.
 * @param action An optional custom composable that replaces the default
 *   action button. When `null`, the default [TextButton] logic applies.
 */
@Composable
public fun StylishEmptyState(
    icon: ImageVector? = null,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    titleMaxLines: Int = Int.MAX_VALUE,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    titleStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    descriptionMaxLines: Int = Int.MAX_VALUE,
    descriptionOverflow: TextOverflow = TextOverflow.Ellipsis,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconContent: (@Composable () -> Unit)? = null,
    titleContent: (@Composable () -> Unit)? = null,
    descriptionContent: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (iconContent != null) {
            iconContent()
        } else if (icon != null) {
            Icon(
                icon, contentDescription = null, tint = iconTint,
                modifier = Modifier.padding(bottom = StylishTheme.dimensions.contentSpacing),
            )
        }
        if (titleContent != null || title.isNotBlank()) {
            (titleContent ?: {
                Text(
                    title,
                    style = titleStyle,
                    color = contentColor,
                    maxLines = titleMaxLines,
                    overflow = titleOverflow,
                )
            })()
        }
        if (descriptionContent != null || description.isNotBlank()) {
            (descriptionContent ?: {
                Text(
                    description,
                    style = descriptionStyle,
                    color = contentColor,
                    maxLines = descriptionMaxLines,
                    overflow = descriptionOverflow,
                    modifier = Modifier.padding(top = StylishTheme.dimensions.itemSpacing),
                )
            })()
        }
        if (action != null) {
            action()
        } else if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
                modifier = Modifier.padding(top = StylishTheme.dimensions.contentSpacing),
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Preview(name = "Empty state", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishEmptyStatePreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishEmptyState(
                icon = Icons.Default.DirectionsCar,
                title = "まだ車両が登録されていません",
                description = "下の＋ボタンから最初の車を登録しましょう",
                modifier = Modifier.fillMaxSize(),
                actionLabel = "車両を登録する",
                onAction = {},
            )
        }
    }
}

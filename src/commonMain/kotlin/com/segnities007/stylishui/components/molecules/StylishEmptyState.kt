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
 * A full-size placeholder displayed when a list or content area has no data,
 * presenting an icon, title, description, and an optional call-to-action.
 *
 * The component fills the available space and centers its children
 * vertically and horizontally. By default the [icon] vector is rendered with
 * [iconTint]; supply [iconContent] to replace it with arbitrary composable
 * content. Similarly, the action area renders a [TextButton] with
 * [actionLabel] when both [actionLabel] and [onAction] are non-null, or the
 * custom [action] composable when provided. If neither action parameter is
 * set, no action area is shown.
 *
 * @param icon The [ImageVector] displayed above the title. Ignored when
 *   [iconContent] is provided.
 * @param title The headline text describing the empty state.
 * @param description The explanatory text shown below the title.
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
 *   icon. When `null`, the [icon] vector is rendered. Note: the default icon
 *   includes 16 dp bottom padding; custom slots do not inherit this spacing.
 * @param action An optional custom composable that replaces the default
 *   action button. When `null`, the default [TextButton] logic applies.
 */
@Composable
public fun StylishEmptyState(
    icon: ImageVector,
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
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (iconContent != null) iconContent() else Icon(
            icon, contentDescription = null, tint = iconTint,
            modifier = Modifier.padding(bottom = StylishTheme.dimensions.contentSpacing),
        )
        Text(
            title,
            style = titleStyle,
            color = contentColor,
            maxLines = titleMaxLines,
            overflow = titleOverflow,
        )
        Text(
            description,
            style = descriptionStyle,
            color = contentColor,
            maxLines = descriptionMaxLines,
            overflow = descriptionOverflow,
            modifier = Modifier.padding(top = StylishTheme.dimensions.itemSpacing),
        )
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
                actionLabel = "車両を登録する",
                onAction = {},
            )
        }
    }
}

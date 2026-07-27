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

/** リストが空のときのプレースホルダー。アイコン・見出し・説明・任意のアクション。 */
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
            modifier = Modifier.padding(bottom = 16.dp),
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
            modifier = Modifier.padding(top = 8.dp),
        )
        if (action != null) {
            action()
        } else if (actionLabel != null && onAction != null) {
            TextButton(
                onClick = onAction,
                modifier = Modifier.padding(top = 16.dp),
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

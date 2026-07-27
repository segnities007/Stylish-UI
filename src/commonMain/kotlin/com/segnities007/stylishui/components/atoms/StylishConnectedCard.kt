package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/** 接続型カード。リスト内で隣接カードと辺・角を共有するコンテナ。タップ・長押し対応。 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun StylishConnectedCard(
    title: String,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    shape: Shape = connectedShape(ConnectedCorners.Standalone),
    outlineEdges: ConnectedEdges = ConnectedEdges.All,
    outlineCorners: ConnectedCorners = ConnectedCorners.Standalone,
    enabled: Boolean = true,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    supportingTextMaxLines: Int = 1,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    containerColor: Color? = null,
    contentColor: Color? = null,
    minHeight: Dp = 77.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 12.dp,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    val haptic = LocalHapticFeedback.current
    val actionable = isActionable(
        enabled = enabled,
        hasClickAction = onClick != null,
        hasLongClickAction = onLongClick != null,
    )
    Card(
        modifier = modifier
            .connectedOutline(outlineEdges, outlineCorners)
            .then(
                if (actionable) {
                    Modifier
                        .semantics(mergeDescendants = true) { role = Role.Button }
                        .combinedClickable(
                            onClick = { onClick?.invoke() },
                            onLongClick = onLongClick?.let {
                                {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    it()
                                }
                            },
                        )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor ?: MaterialTheme.stylishComponentColors.groupedContainer,
            contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
        ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = minHeight)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent()
            Spacer(Modifier.width(12.dp))
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    title,
                    style = titleStyle,
                    maxLines = titleMaxLines,
                    overflow = titleOverflow,
                )
                if (supportingText.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        supportingText,
                        style = supportingTextStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = supportingTextMaxLines,
                        overflow = supportingTextOverflow,
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            trailingContent()
        }
    }
}

@Preview(name = "Stylish connected card", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCard(
                title = "項目のタイトル",
                supportingText = "補足テキスト",
                onClick = {},
                onLongClick = {},
            ) { Text("詳細", style = MaterialTheme.typography.labelSmall) }
        }
    }
}

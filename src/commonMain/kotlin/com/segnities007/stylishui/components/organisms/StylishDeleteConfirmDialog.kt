package com.segnities007.stylishui.components.organisms

import com.segnities007.stylishui.components.atoms.StylishDialogSurface

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A confirmation dialog for destructive delete operations.
 *
 * Displays a title, an explanatory message, and a confirm/cancel button
 * pair inside a [StylishDialogSurface]. Always interpose this dialog
 * before performing an irreversible action so the user can back out.
 * Composes the [StylishDialogSurface] atom for the modal container and
 * the [StylishDialogActions] organism for the button row.
 *
 * @param title Heading text shown at the top of the dialog, e.g.
 *   "Delete record".
 * @param message Body text explaining the consequence of confirming,
 *   rendered in [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param confirmLabel Text for the destructive-action button, e.g.
 *   "Delete".
 * @param cancelLabel Text for the dismiss button, e.g. "Cancel".
 * @param onConfirm Callback invoked when the user taps the confirm button.
 *   The caller is responsible for performing the deletion and dismissing
 *   the dialog.
 * @param onDismiss Callback invoked when the user taps cancel or taps
 *   outside the dialog. The caller should dismiss the dialog.
 * @param titleMaxLines Maximum lines for [title]. Defaults to
 *   [Int.MAX_VALUE] (unlimited).
 * @param titleOverflow [TextOverflow] strategy for [title]. Defaults to
 *   [TextOverflow.Ellipsis].
 * @param titleStyle [TextStyle] for [title]. Defaults to
 *   [MaterialTheme.typography.titleLarge].
 * @param messageMaxLines Maximum lines for [message]. Defaults to
 *   [Int.MAX_VALUE] (unlimited).
 * @param messageOverflow [TextOverflow] strategy for [message]. Defaults
 *   to [TextOverflow.Ellipsis].
 * @param messageStyle [TextStyle] for [message]. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 *
 * @see StylishDialogActions
 * @see StylishDialogSurface
 */
@Composable
public fun StylishDeleteConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    cancelLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    titleMaxLines: Int = Int.MAX_VALUE,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    messageMaxLines: Int = Int.MAX_VALUE,
    messageOverflow: TextOverflow = TextOverflow.Ellipsis,
    messageStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    StylishDialogSurface(onDismiss = onDismiss) {
        Column(Modifier.padding(24.dp)) {
            Text(
                title,
                style = titleStyle,
                maxLines = titleMaxLines,
                overflow = titleOverflow,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                message,
                style = messageStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = messageMaxLines,
                overflow = messageOverflow,
            )
            Spacer(Modifier.height(24.dp))
            StylishDialogActions(
                confirmLabel = confirmLabel,
                cancelLabel = cancelLabel,
                onConfirm = onConfirm,
                onCancel = onDismiss,
            )
        }
    }
}

@Preview(name = "Delete confirm dialog", showBackground = true, widthDp = 393, heightDp = 400)
@Composable
private fun StylishDeleteConfirmDialogPreview() {
    StylishTheme(darkTheme = false) {
        StylishDeleteConfirmDialog(
            title = "給油記録を削除",
            message = "この給油記録を削除しますか？この操作は取り消せません。",
            confirmLabel = "削除",
            cancelLabel = "キャンセル",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

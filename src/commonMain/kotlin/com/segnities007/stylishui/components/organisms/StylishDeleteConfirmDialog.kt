package com.segnities007.stylishui.components.organisms

import com.segnities007.stylishui.components.atoms.StylishDialogSurface

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
 * @param modifier Modifier applied to the [StylishDialogSurface]'s
 *   root card.
 * @param message Body text explaining the consequence of confirming,
 *   rendered in [messageColor].
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
 * @param messageColor Color for [message]. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param cancelColors Optional [ButtonColors] for the cancel button,
 *   forwarded to [StylishDialogActions]. When null (the default), the
 *   subdued [StylishDialogActions] defaults are used.
 * @param confirmColors Optional [ButtonColors] for the confirm button,
 *   forwarded to [StylishDialogActions]. When null (the default), the
 *   prominent [StylishDialogActions] defaults are used.
 * @param confirmEnabled Whether the confirm button is interactive.
 *   Defaults to true. Set to false to block confirmation until
 *   preconditions are met.
 * @param cancelEnabled Whether the cancel button is interactive.
 *   Defaults to true.
 * @param animate When `true` (the default), the dialog plays the
 *   scale-and-fade entrance animation of [StylishDialogSurface]. When
 *   `false`, the dialog appears immediately.
 * @param properties [DialogProperties] forwarded to [StylishDialogSurface],
 *   e.g. to disable dismissal on back press or on tapping outside.
 *   Defaults to [DialogProperties] with `usePlatformDefaultWidth = false`.
 *
 * @see StylishDialogActions
 * @see StylishDialogSurface
 */
@Composable
public fun StylishDeleteConfirmDialog(
    title: String,
    modifier: Modifier = Modifier,
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
    messageColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    cancelColors: ButtonColors? = null,
    confirmColors: ButtonColors? = null,
    confirmEnabled: Boolean = true,
    cancelEnabled: Boolean = true,
    animate: Boolean = true,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
) {
    StylishDialogSurface(
        onDismiss = onDismiss,
        modifier = modifier,
        animate = animate,
        properties = properties,
    ) {
        Column(Modifier.padding(StylishTheme.dimensions.sectionSpacing - StylishTheme.dimensions.itemSpacing)) {
            Text(
                title,
                style = titleStyle,
                maxLines = titleMaxLines,
                overflow = titleOverflow,
            )
            Spacer(Modifier.height(StylishTheme.dimensions.itemSpacing + StylishTheme.dimensions.inlineSpacing))
            Text(
                message,
                style = messageStyle,
                color = messageColor,
                maxLines = messageMaxLines,
                overflow = messageOverflow,
            )
            Spacer(Modifier.height(StylishTheme.dimensions.contentSpacing + StylishTheme.dimensions.itemSpacing))
            StylishDialogActions(
                confirmLabel = confirmLabel,
                cancelLabel = cancelLabel,
                onConfirm = onConfirm,
                onCancel = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                cancelColors = cancelColors,
                confirmColors = confirmColors,
                confirmEnabled = confirmEnabled,
                cancelEnabled = cancelEnabled,
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

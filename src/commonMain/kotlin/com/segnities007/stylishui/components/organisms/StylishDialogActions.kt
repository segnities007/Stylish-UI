package com.segnities007.stylishui.components.organisms

import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A confirm/cancel button row for the bottom of a dialog.
 *
 * Renders two visually connected buttons — cancel on the left, confirm
 * on the right — using [StylishConnectedButtonRow]. The cancel button
 * uses a subdued [MaterialTheme.colorScheme.surfaceVariant] container
 * while the confirm button uses the default prominent style. Place this
 * inside a [com.segnities007.stylishui.components.atoms.StylishDialogSurface]
 * or any dialog-like container, typically with
 * [Modifier.fillMaxWidth] passed via [modifier].
 *
 * @param confirmLabel Text displayed on the confirm button when
 *   [confirmContent] is null.
 * @param modifier Modifier applied to the underlying
 *   [StylishConnectedButtonRow]. Callers control the row's width —
 *   pass [Modifier.fillMaxWidth] to stretch it to the container.
 * @param cancelLabel Text displayed on the cancel button when
 *   [cancelContent] is null.
 * @param onConfirm Callback invoked when the confirm button is tapped.
 * @param onCancel Callback invoked when the cancel button is tapped.
 * @param cancelColors Optional [ButtonColors] for the cancel button.
 *   When null (the default), the subdued
 *   [MaterialTheme.colorScheme.surfaceVariant] container with
 *   [MaterialTheme.colorScheme.onSurfaceVariant] content is used.
 * @param confirmColors Optional [ButtonColors] for the confirm button.
 *   When null (the default), the prominent [StylishConnectedButtonRow]
 *   default colors are used.
 * @param confirmEnabled Whether the confirm button is interactive.
 *   Defaults to true. Set to false to block confirmation until
 *   preconditions are met (e.g. a required field is filled).
 * @param cancelEnabled Whether the cancel button is interactive.
 *   Defaults to true.
 * @param cancelContent Optional custom composable rendered inside the
 *   cancel button, replacing the [cancelLabel] text. When null (the
 *   default), a [Text] with [cancelLabel] is rendered.
 * @param confirmContent Optional custom composable rendered inside the
 *   confirm button, replacing the [confirmLabel] text. When null (the
 *   default), a [Text] with [confirmLabel] is rendered.
 *
 * @see StylishDeleteConfirmDialog
 * @see com.segnities007.stylishui.components.atoms.StylishDialogSurface
 * @see StylishConnectedButtonRow
 */
@Composable
public fun StylishDialogActions(
    confirmLabel: String,
    modifier: Modifier = Modifier,
    cancelLabel: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    cancelColors: ButtonColors? = null,
    confirmColors: ButtonColors? = null,
    confirmEnabled: Boolean = true,
    cancelEnabled: Boolean = true,
    cancelContent: @Composable (() -> Unit)? = null,
    confirmContent: @Composable (() -> Unit)? = null,
) {
    StylishConnectedButtonRow(
        items = listOf(
            StylishConnectedButtonItem(
                onClick = onCancel,
                enabled = cancelEnabled,
                colors = cancelColors ?: ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            ) { cancelContent?.invoke() ?: Text(cancelLabel) },
            StylishConnectedButtonItem(
                onClick = onConfirm,
                enabled = confirmEnabled,
                colors = confirmColors,
            ) { confirmContent?.invoke() ?: Text(confirmLabel) },
        ),
        modifier = modifier,
        defaultColors = ButtonDefaults.buttonColors(),
    )
}

@Preview(name = "Stylish dialog actions", showBackground = true, widthDp = 393)
@Composable
private fun StylishDialogActionsPreview() {
    StylishTheme(darkTheme = false) {
        Card(Modifier.padding(20.dp)) {
            StylishDialogActions(
                confirmLabel = "削除",
                modifier = Modifier.fillMaxWidth(),
                cancelLabel = "キャンセル",
                onConfirm = {},
                onCancel = {},
            )
        }
    }
}

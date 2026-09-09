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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A confirm/cancel button row for the bottom of a dialog.
 *
 * Renders a connected button group using [StylishConnectedButtonRow]. When
 * [cancelLabel] is null or blank, [cancelContent] is null, or [onCancel] is
 * null, only the confirm button is emitted. The cancel button uses a subdued
 * [MaterialTheme.colorScheme.surfaceVariant] container while the confirm
 * button uses the default prominent style. Place this
 * inside a [com.segnities007.stylishui.components.atoms.StylishDialogSurface]
 * or any dialog-like container, typically with
 * [Modifier.fillMaxWidth] passed via [modifier].
 *
 * @param confirmLabel Text displayed on the confirm button when
 *   [confirmContent] is null.
 * @param cancelLabel Text displayed on the cancel button when
 *   [cancelContent] is null. Leave it null or blank when the dialog has no
 *   cancel action.
 * @param onConfirm Callback invoked when the confirm button is tapped.
 * @param onCancel Callback invoked when the cancel button is tapped. When
 *   null, no cancel button is emitted.
 * @param modifier Modifier applied to the underlying
 *   [StylishConnectedButtonRow]. Callers control the row's width —
 *   pass [Modifier.fillMaxWidth] to stretch it to the container.
 * @param cancelColors Optional [ButtonColors] for the cancel button.
 *   When null (the default), the subdued
 *   [MaterialTheme.colorScheme.surfaceVariant] container with
 *   [MaterialTheme.colorScheme.onSurfaceVariant] content is used.
 * @param confirmColors Optional [ButtonColors] for the confirm button.
 *   When null (the default), the prominent [StylishConnectedButtonRow]
 *   default colors are used.
 * @param spacing Horizontal gap between the two buttons. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing].
 * @param confirmEnabled Whether the confirm button is interactive.
 *   Defaults to true. Set to false to block confirmation until
 *   preconditions are met (e.g. a required field is filled).
 * @param cancelEnabled Whether the cancel button is interactive.
 *   Defaults to true.
 * @param cancelContent Optional custom composable rendered inside the
 *   cancel button, replacing the [cancelLabel] text. When non-null and
 *   [onCancel] is provided, the cancel button is emitted even if
 *   [cancelLabel] is blank.
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
    cancelLabel: String? = null,
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    cancelColors: ButtonColors? = null,
    confirmColors: ButtonColors? = null,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    confirmEnabled: Boolean = true,
    cancelEnabled: Boolean = true,
    cancelContent: @Composable (() -> Unit)? = null,
    confirmContent: @Composable (() -> Unit)? = null,
) {
    val cancelAction = onCancel
    val cancelItem = if (cancelAction != null &&
        (cancelLabel?.isNotBlank() == true || cancelContent != null)
    ) {
        StylishConnectedButtonItem(
            onClick = cancelAction,
            enabled = cancelEnabled,
            colors = cancelColors ?: ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        ) { cancelContent?.invoke() ?: Text(cancelLabel.orEmpty()) }
    } else {
        null
    }
    val confirmItem = StylishConnectedButtonItem(
        onClick = onConfirm,
        enabled = confirmEnabled,
        colors = confirmColors,
    ) { confirmContent?.invoke() ?: Text(confirmLabel) }

    StylishConnectedButtonRow(
        items = listOfNotNull(cancelItem, confirmItem),
        modifier = modifier,
        spacing = spacing,
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

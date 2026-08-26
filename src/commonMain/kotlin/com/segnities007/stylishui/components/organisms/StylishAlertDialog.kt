package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A modal alert dialog that prompts the user for a decision, wrapping
 * the Material 3 [AlertDialog] with Stylish defaults.
 *
 * Supply [title] and [text] for the message, and wire [confirmButton]
 * and [dismissButton] — typically [TextButton]s or the
 * [com.segnities007.stylishui.components.organisms.StylishDialogActions]
 * row. The dialog does not set up any events itself: callers own
 * dismissal via [onDismissRequest] and each button's callback.
 *
 * @param onDismissRequest Called when the user taps outside the dialog
 *   or presses the system back button.
 * @param modifier Modifier applied to the dialog content.
 * @param icon Optional icon shown above [title] (or above [text] when
 *   no title is provided).
 * @param title Optional heading that states the purpose of the dialog.
 * @param text Optional body text with the details of the dialog.
 * @param confirmButton Composable for the action that confirms the
 *   proposed action, e.g. a [TextButton] labelled "OK".
 * @param dismissButton Optional composable for the action that dismisses
 *   the dialog, e.g. a [TextButton] labelled "キャンセル".
 * @param containerColor Background color of the dialog. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param shape Corner shape of the dialog. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param tonalElevation Tonal elevation of the dialog surface. Defaults
 *   to 1.dp for a subtle lift.
 * @param properties Platform-specific [DialogProperties] of the dialog.
 *   Defaults to using the platform default width.
 *
 * @see StylishDeleteConfirmDialog
 * @see StylishDialogActions
 * @see com.segnities007.stylishui.components.atoms.StylishDialogSurface
 */
@Composable
public fun StylishAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    tonalElevation: Dp = 1.dp,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = true),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.stylishTestTag("alert_dialog"),
        icon = icon,
        title = title,
        text = text,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        containerColor = containerColor,
        shape = shape,
        tonalElevation = tonalElevation,
        properties = properties,
    )
}

@Preview(name = "Stylish alert dialog", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishAlertDialogPreview() {
    StylishTheme(darkTheme = false) {
        StylishAlertDialog(
            onDismissRequest = {},
            icon = null,
            title = { Text("設定をリセット") },
            text = { Text("すべての設定を初期値に戻しますか？この操作は取り消せません。") },
            confirmButton = {
                TextButton(onClick = {}) {
                    Text("リセット")
                }
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text("キャンセル")
                }
            },
        )
    }
}

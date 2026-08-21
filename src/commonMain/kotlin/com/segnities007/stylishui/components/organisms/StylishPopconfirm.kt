package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.molecules.StylishPopover
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * An inline confirmation anchored to a trigger — the web "Popconfirm"
 * pattern from Ant Design.
 *
 * Renders [title] (and optional [description]) in a popover anchored to
 * [anchor], with [confirmLabel]/[cancelLabel] buttons. The popover is
 * fully hoisted via [expanded]/[onExpandedChange].
 *
 * @param expanded Whether the confirmation popover is visible.
 * @param onExpandedChange Called when the popover should open or close
 *   (including dismissal by tapping outside).
 * @param modifier Modifier applied to the anchor wrapper.
 * @param anchor The trigger content (typically a destructive button).
 * @param title The confirmation question, e.g. "この項目を削除しますか?".
 * @param description Optional supporting explanation.
 * @param confirmLabel Text of the confirm button. Defaults to "OK".
 * @param cancelLabel Text of the cancel button. Defaults to "キャンセル".
 * @param onConfirm Called when the user confirms. The caller should
 *   dismiss the popover and perform the action.
 * @param onCancel Called when the user cancels or dismisses.
 * @param confirmColors Optional [ButtonColors] for the confirm button.
 *   Defaults to the error container look for destructive confirmations.
 * @param titleStyle Typography of [title]. Defaults to
 *   [MaterialTheme.typography.titleSmall].
 * @param descriptionStyle Typography of [description]. Defaults to
 *   [MaterialTheme.typography.bodySmall].
 * @param shape Corner shape of the popover. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param width Width of the popover. Defaults to 260 dp.
 */
@Composable
public fun StylishPopconfirm(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    anchor: @Composable () -> Unit,
    title: String,
    description: String? = null,
    confirmLabel: String = "OK",
    cancelLabel: String = "キャンセル",
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {},
    confirmColors: ButtonColors? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodySmall,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    width: Dp = 260.dp,
) {
    StylishPopover(
        trigger = anchor,
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier.stylishTestTag("popconfirm"),
        shape = shape,
        width = width,
        content = {
            Column(
            verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
        ) {
            Text(
                title,
                style = titleStyle,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (description != null) {
                Text(
                    description,
                    style = descriptionStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = {
                    onCancel()
                    onExpandedChange(false)
                }) {
                    Text(cancelLabel, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StylishButton(
                    onClick = {
                        onConfirm()
                        onExpandedChange(false)
                    },
                    colors = confirmColors ?: ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                ) {
                    Text(confirmLabel)
                }
            }
        }
    },
)
}

@Preview(name = "Stylish popconfirm", showBackground = true, widthDp = 393)
@Composable
private fun StylishPopconfirmPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishPopconfirm(
                expanded = true,
                onExpandedChange = {},
                anchor = {
                    com.segnities007.stylishui.components.atoms.StylishButton(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                    ) { Text("削除") }
                },
                title = "この記録を削除しますか?",
                description = "この操作は取り消せません。",
                confirmLabel = "削除",
                onConfirm = {},
            )
        }
    }
}

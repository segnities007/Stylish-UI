package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.theme.StylishTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishDragHandle
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishCircularProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishLinearProgressIndicator
import com.segnities007.stylishui.components.molecules.StylishAlert
import com.segnities007.stylishui.components.molecules.StylishAlertVariant
import com.segnities007.stylishui.components.molecules.StylishEmptyState
import com.segnities007.stylishui.components.molecules.StylishPopover
import com.segnities007.stylishui.components.molecules.StylishResult
import com.segnities007.stylishui.components.molecules.StylishResultVariant
import com.segnities007.stylishui.components.molecules.StylishSnackbar
import com.segnities007.stylishui.components.molecules.StylishToastData
import com.segnities007.stylishui.components.molecules.StylishToastHost
import com.segnities007.stylishui.components.molecules.StylishToastVariant
import com.segnities007.stylishui.components.molecules.rememberStylishToastHostState
import com.segnities007.stylishui.components.organisms.StylishAlertDialog
import com.segnities007.stylishui.components.organisms.StylishBottomSheet
import com.segnities007.stylishui.components.organisms.StylishDeleteConfirmDialog
import com.segnities007.stylishui.components.organisms.StylishPopconfirm
import kotlinx.coroutines.launch

/**
 * Returns all feedback-related demo components for the catalog.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal fun getFeedbackDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Alert",
        category = DemoCategory.Feedback,
        code = """StylishAlert(
    title = "お知らせ",
    message = "内容",
    variant = StylishAlertVariant.Info,
)""",
        preview = {
            var variant by remember { mutableStateOf(StylishAlertVariant.Info) }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StylishAlertVariant.entries.forEach { v ->
                    StylishChip(
                        label = v.name,
                        onClick = { variant = v },
                        selected = variant == v,
                    )
                }
            }
            StylishAlert(
                title = "お知らせ",
                message = "Stylish Alert は variant で色とアイコンが変わります。",
                variant = variant,
            )
        },
    ),
    DemoComponent(
        name = "Toast",
        category = DemoCategory.Feedback,
        code = """val host = rememberStylishToastHostState()
scope.launch { host.showToast(StylishToastData("保存しました", StylishToastVariant.Success)) }
StylishToastHost(host)""",
        preview = {
            val hostState = rememberStylishToastHostState()
            val scope = rememberCoroutineScope()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishButton(onClick = {
                    scope.launch {
                        hostState.showToast(
                            StylishToastData("保存しました", StylishToastVariant.Success),
                        )
                    }
                }) { StylishText("成功トースト") }
                StylishButton(
                    onClick = {
                        scope.launch {
                            hostState.showToast(
                                StylishToastData("エラーが発生しました", StylishToastVariant.Error, actionLabel = "再試行"),
                            )
                        }
                    },
                    variant = StylishButtonVariant.Outlined,
                ) { StylishText("エラートースト") }
            }
            StylishToastHost(hostState, Modifier.fillMaxWidth())
        },
    ),
    DemoComponent(
        name = "Snackbar",
        category = DemoCategory.Feedback,
        code = """StylishSnackbar(
    action = { StylishButton(onClick = {}, variant = StylishButtonVariant.Text) { StylishText("元に戻す") } },
) { StylishText("アイテムを削除しました") }""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StylishSnackbar {
                    StylishText("メッセージを送信しました")
                }
                StylishSnackbar(
                    action = {
                        StylishButton(onClick = {}, variant = StylishButtonVariant.Text) { StylishText("元に戻す") }
                    },
                ) {
                    StylishText("アイテムを削除しました")
                }
            }
        },
    ),
    DemoComponent(
        name = "Popover",
        category = DemoCategory.Feedback,
        code = """StylishPopover(
    trigger = { StylishButton(onClick = { expanded = true }) { StylishText("開く") } },
    expanded = expanded,
    onExpandedChange = { expanded = it },
) { StylishText("内容") }""",
        preview = {
            var expanded by remember { mutableStateOf(false) }
            StylishPopover(
                trigger = {
                    StylishButton(onClick = { expanded = true }, variant = StylishButtonVariant.Outlined) {
                        StylishText("フィルターを開く")
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                content = {
                    StylishText("絞り込み条件", style = StylishTheme.typography.titleSmall)
                    StylishText("ここにフィルター項目を配置します。", style = StylishTheme.typography.bodySmall)
                },
            )
        },
    ),
    DemoComponent(
        name = "Popconfirm",
        category = DemoCategory.Feedback,
        code = """StylishPopconfirm(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    anchor = { 削除ボタン },
    title = "削除しますか?",
    confirmLabel = "削除",
    onConfirm = {},
)""",
        preview = {
            var expanded by remember { mutableStateOf(false) }
            StylishPopconfirm(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                anchor = {
                    StylishButton(
                        onClick = { expanded = true },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = StylishTheme.colorScheme.error,
                            contentColor = StylishTheme.colorScheme.onError,
                        ),
                    ) { StylishText("記録を削除") }
                },
                title = "この記録を削除しますか?",
                description = "この操作は取り消せません。",
                confirmLabel = "削除",
                onConfirm = {},
            )
        },
    ),
    DemoComponent(
        name = "Alert dialog",
        category = DemoCategory.Feedback,
        code = """StylishAlertDialog(
    onDismissRequest = { open = false },
    title = { StylishText("削除") },
    text = { StylishText("よろしいですか?") },
    confirmButton = { StylishButton(onClick = { open = false }) { StylishText("OK") } },
)""",
        preview = {
            var open by remember { mutableStateOf(false) }
            StylishButton(onClick = { open = true }, variant = StylishButtonVariant.Outlined) {
                StylishText("ダイアログを開く")
            }
            if (open) {
                StylishAlertDialog(
                    onDismissRequest = { open = false },
                    title = { StylishText("車両を削除") },
                    text = { StylishText("この操作は取り消せません。よろしいですか?") },
                    confirmButton = {
                        StylishButton(onClick = { open = false }) { StylishText("削除") }
                    },
                    dismissButton = {
                        StylishButton(
                            onClick = { open = false },
                            variant = StylishButtonVariant.Text,
                        ) { StylishText("キャンセル") }
                    },
                )
            }
        },
    ),
    DemoComponent(
        name = "Delete confirm dialog",
        category = DemoCategory.Feedback,
        code = """StylishDeleteConfirmDialog(
    title = "記録を削除",
    message = "この操作は取り消せません。",
    confirmLabel = "削除",
    cancelLabel = "キャンセル",
    onConfirm = {},
    onDismiss = {},
)""",
        preview = {
            var open by remember { mutableStateOf(false) }
            StylishButton(
                onClick = { open = true },
                variant = StylishButtonVariant.Outlined,
            ) { StylishText("削除ダイアログを開く") }
            if (open) {
                StylishDeleteConfirmDialog(
                    title = "記録を削除",
                    message = "この操作は取り消せません。本当によろしいですか?",
                    confirmLabel = "削除",
                    cancelLabel = "キャンセル",
                    onConfirm = { open = false },
                    onDismiss = { open = false },
                )
            }
        },
    ),
    DemoComponent(
        name = "Bottom sheet",
        category = DemoCategory.Feedback,
        code = """StylishBottomSheet(
    onDismiss = { open = false },
    dragHandle = { StylishDragHandle() },
) {
    StylishText("ボトムシートの内容")
}""",
        preview = {
            var open by remember { mutableStateOf(false) }
            StylishButton(onClick = { open = true }, variant = StylishButtonVariant.Outlined) {
                StylishText("ボトムシートを開く")
            }
            if (open) {
                StylishBottomSheet(
                    onDismiss = { open = false },
                    dragHandle = { StylishDragHandle() },
                ) {
                    StylishText("ボトムシートの内容", style = StylishTheme.typography.titleMedium)
                    StylishText("ここに詳細情報を表示します。", style = StylishTheme.typography.bodyMedium)
                }
            }
        },
    ),
    DemoComponent(
        name = "Result",
        category = DemoCategory.Feedback,
        code = """StylishResult(
    title = "送信が完了しました",
    description = "担当者より折り返しご連絡いたします。",
    variant = StylishResultVariant.Success,
)""",
        preview = {
            var variant by remember { mutableStateOf(StylishResultVariant.Success) }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StylishResultVariant.entries.forEach { v ->
                    StylishChip(
                        label = v.name,
                        onClick = { variant = v },
                        selected = variant == v,
                    )
                }
            }
            StylishResult(
                title = "処理が完了しました",
                description = "処理は正常に完了しました。",
                variant = variant,
            )
        },
    ),
    DemoComponent(
        name = "Empty state",
        category = DemoCategory.Feedback,
        code = """StylishEmptyState(
    title = "データがありません",
    description = "新しい記録を追加してください。",
    actionLabel = "追加する",
    onAction = {},
)""",
        preview = {
            StylishEmptyState(
                title = "データがありません",
                description = "新しい記録を追加してください。",
                actionLabel = "追加する",
                onAction = {},
            )
        },
    ),
    DemoComponent(
        name = "Progress",
        category = DemoCategory.Feedback,
        code = """StylishCircularProgressIndicator()
StylishLinearProgressIndicator(progress = { 0.7f })""",
        preview = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                StylishCircularProgressIndicator()
                StylishCircularProgressIndicator(progress = { 0.7f })
            }
            StylishLinearProgressIndicator(Modifier.fillMaxWidth(), progress = { 0.7f })
        },
    ),
)

package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishCircularProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishLinearProgressIndicator
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.molecules.StylishAlert
import com.segnities007.stylishui.components.molecules.StylishAlertVariant
import com.segnities007.stylishui.components.molecules.StylishEmptyState
import com.segnities007.stylishui.components.molecules.StylishResult
import com.segnities007.stylishui.components.molecules.StylishResultVariant
import com.segnities007.stylishui.components.molecules.StylishToastData
import com.segnities007.stylishui.components.molecules.StylishToastHost
import com.segnities007.stylishui.components.molecules.StylishToastVariant
import com.segnities007.stylishui.components.molecules.rememberStylishToastHostState
import com.segnities007.stylishui.components.organisms.StylishAlertDialog
import com.segnities007.stylishui.components.organisms.StylishPopconfirm
import com.segnities007.stylishui.components.molecules.StylishPopover
import kotlinx.coroutines.launch

@Composable
internal fun DemoFeedback(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Alert",
        description = "バリアントを切り替えられます。",
        code = """StylishAlert(
    title = "お知らせ",
    message = "内容",
    variant = StylishAlertVariant.Info,
)""",
        modifier = modifier,
    ) {
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
    }

    StylishDemoCard(
        title = "Toast",
        description = "キュー式トースト（自動消滅）。",
        code = """val host = rememberStylishToastHostState()
scope.launch { host.showToast(StylishToastData("保存しました", StylishToastVariant.Success)) }
StylishToastHost(host)""",
        modifier = modifier,
    ) {
        val hostState = rememberStylishToastHostState()
        val scope = rememberCoroutineScope()
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StylishButton(onClick = {
                scope.launch {
                    hostState.showToast(
                        StylishToastData("保存しました", StylishToastVariant.Success),
                    )
                }
            }) { Text("成功トースト") }
            StylishButton(
                onClick = {
                    scope.launch {
                        hostState.showToast(
                            StylishToastData("エラーが発生しました", StylishToastVariant.Error, actionLabel = "再試行"),
                        )
                    }
                },
                variant = StylishButtonVariant.Outlined,
            ) { Text("エラートースト") }
        }
        StylishToastHost(hostState, Modifier.fillMaxWidth())
    }

    StylishDemoCard(
        title = "Popover",
        description = "アンカー付きのフローティングコンテンツ。",
        code = """StylishPopover(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    anchor = { StylishButton(onClick = { expanded = true }) { Text("開く") } },
) { Text("内容") }""",
        modifier = modifier,
    ) {
        var expanded by remember { mutableStateOf(false) }
        StylishPopover(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            anchor = {
                StylishButton(onClick = { expanded = true }, variant = StylishButtonVariant.Outlined) {
                    Text("フィルターを開く")
                }
            },
        ) {
            Text("絞り込み条件", style = MaterialTheme.typography.titleSmall)
            Text("ここにフィルター項目を配置します。", style = MaterialTheme.typography.bodySmall)
        }
    }

    StylishDemoCard(
        title = "Popconfirm",
        description = "破壊的操作のインライン確認。",
        code = """StylishPopconfirm(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    anchor = { 削除ボタン },
    title = "削除しますか?",
    confirmLabel = "削除",
    onConfirm = {},
)""",
        modifier = modifier,
    ) {
        var expanded by remember { mutableStateOf(false) }
        StylishPopconfirm(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            anchor = {
                StylishButton(
                    onClick = { expanded = true },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                ) { Text("記録を削除") }
            },
            title = "この記録を削除しますか?",
            description = "この操作は取り消せません。",
            confirmLabel = "削除",
            onConfirm = {},
        )
    }

    StylishDemoCard(
        title = "Alert dialog",
        description = "確認ダイアログ。",
        code = """StylishAlertDialog(
    onDismissRequest = { open = false },
    title = { Text("削除") },
    text = { Text("よろしいですか?") },
    confirmButton = { StylishButton(onClick = { open = false }) { Text("OK") } },
)""",
        modifier = modifier,
    ) {
        var open by remember { mutableStateOf(false) }
        StylishButton(onClick = { open = true }, variant = StylishButtonVariant.Outlined) {
            Text("ダイアログを開く")
        }
        if (open) {
            StylishAlertDialog(
                onDismissRequest = { open = false },
                title = { Text("車両を削除") },
                text = { Text("この操作は取り消せません。よろしいですか?") },
                confirmButton = {
                    StylishButton(onClick = { open = false }) { Text("削除") }
                },
                dismissButton = {
                    StylishButton(
                        onClick = { open = false },
                        variant = StylishButtonVariant.Text,
                    ) { Text("キャンセル") }
                },
            )
        }
    }

    StylishDemoCard(
        title = "Result",
        description = "処理完了・エラーのステータスページ。",
        code = """StylishResult(
    title = "送信が完了しました",
    description = "担当者より折り返しご連絡いたします。",
    variant = StylishResultVariant.Success,
)""",
        modifier = modifier,
    ) {
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
    }

    StylishDemoCard(
        title = "Empty state",
        description = "データが無いときの表示。",
        code = """StylishEmptyState(
    title = "データがありません",
    description = "新しい記録を追加してください。",
    actionLabel = "追加する",
    onAction = {},
)""",
        modifier = modifier,
    ) {
        StylishEmptyState(
            title = "データがありません",
            description = "新しい記録を追加してください。",
            actionLabel = "追加する",
            onAction = {},
        )
    }

    StylishDemoCard(
        title = "Progress",
        description = "円形・直線のインジケーター。",
        code = """StylishCircularProgressIndicator()
StylishLinearProgressIndicator(progress = { 0.7f })""",
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            StylishCircularProgressIndicator()
            StylishCircularProgressIndicator(progress = { 0.7f })
        }
        StylishLinearProgressIndicator(Modifier.fillMaxWidth(), progress = { 0.7f })
    }
}

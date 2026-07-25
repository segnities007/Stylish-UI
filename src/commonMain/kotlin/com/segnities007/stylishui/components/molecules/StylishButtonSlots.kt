package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Connected button 内で leading / trailing スロットを同じサイズ感に保つ内部コンポーネント。
 * Row・Column・Grid の3形態で再利用するため、幅の最小値のみ呼び元で調整する。
 */
@Composable
internal fun RowScope.StylishButtonSlot(
    content: (@Composable RowScope.() -> Unit)?,
    alignment: Alignment,
    minWidth: Dp = 24.dp,
) {
    Box(
        modifier = Modifier.widthIn(min = minWidth),
        contentAlignment = alignment,
    ) {
        if (content != null) {
            Row(content = content)
        }
    }
}

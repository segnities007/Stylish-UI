package com.segnities007.stylishui.components.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/** Connected card layout の各カードを表現する不変データ。 */
@Immutable
public data class StylishConnectedCardItem(
    val title: String,
    val supportingText: String = "",
    val onClick: (() -> Unit)? = null,
    val onLongClick: (() -> Unit)? = null,
    val enabled: Boolean = true,
    val leadingContent: @Composable () -> Unit = {},
    val trailingContent: @Composable () -> Unit = {},
)

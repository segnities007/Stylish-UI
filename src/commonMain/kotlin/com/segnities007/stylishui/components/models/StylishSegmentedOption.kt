package com.segnities007.stylishui.components.models

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/** Segmented button の各選択肢を表現する不変データ。 */
@Immutable
public data class StylishSegmentedOption<T>(
    val value: T,
    val label: String,
    val leadingContent: (@Composable RowScope.() -> Unit)? = null,
)

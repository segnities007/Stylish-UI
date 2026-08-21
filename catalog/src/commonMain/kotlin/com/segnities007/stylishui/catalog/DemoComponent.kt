package com.segnities007.stylishui.catalog

import androidx.compose.runtime.Composable

/**
 * Represents a single demo component in the Stylish UI catalog.
 *
 * @property name Display name shown on hover.
 * @property category Category for filtering and grouping.
 * @property preview Composable that renders the interactive preview.
 * @property code Kotlin source code snippet for the component.
 */
public data class DemoComponent(
    val name: String,
    val category: DemoCategory,
    val preview: @Composable () -> Unit,
    val code: String,
)

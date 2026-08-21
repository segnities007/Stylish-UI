package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import com.segnities007.stylishui.theme.StylishTheme

/** Shared theme-aware defaults for controls that are commonly skinned together. */
public object StylishChipDefaults {
    /** Returns the shared rounded shape for chip controls. */
    @Composable
    public fun shape(): Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius)

    /** Returns theme-aware colors for selectable chip controls. */
    @Composable
    public fun colors(): SelectableChipColors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )
}

/** Defaults for icon-button skins and custom icon-button slots. */
public object StylishIconButtonDefaults {
    /** Returns theme-aware colors for icon-button controls. */
    @Composable
    public fun colors(): IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    )
}

/** Defaults for outlined text-field skins. */
public object StylishTextFieldDefaults {
    /** Returns theme-aware colors for outlined text fields. */
    @Composable
    public fun colors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        errorBorderColor = MaterialTheme.colorScheme.error,
    )
}

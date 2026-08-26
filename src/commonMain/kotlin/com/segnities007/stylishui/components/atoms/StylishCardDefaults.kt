package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/** Theme-aware defaults for standalone and connected Stylish cards. */
public object StylishCardDefaults {
    /** Resolves the standard card shape from the active Stylish theme. */
    @Composable
    public fun shape(): Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius)

    /** Resolves card colors while preserving the active component color overrides. */
    @Composable
    public fun colors(): CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        contentColor = MaterialTheme.colorScheme.onSurface,
    )

    /** Resolves the optional outline for a card variant. */
    @Composable
    public fun border(variant: StylishCardVariant): BorderStroke? = when (variant) {
        StylishCardVariant.Outlined -> BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        )
        else -> null
    }

    /** Resolves the elevation ladder for the selected variant and interaction state. */
    @Composable
    public fun elevation(variant: StylishCardVariant, actionable: Boolean): Dp =
        if (variant == StylishCardVariant.Elevated && actionable) {
            StylishTheme.dimensions.interactiveElevation
        } else {
            0.dp
        }
}

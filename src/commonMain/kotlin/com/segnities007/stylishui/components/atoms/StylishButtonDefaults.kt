package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/** Theme-aware defaults that can be reused by custom Stylish button skins. */
public object StylishButtonDefaults {
    /** Resolves colors for a button variant. */
    @Composable
    public fun colors(variant: StylishButtonVariant): ButtonColors = when (variant) {
        StylishButtonVariant.Filled, StylishButtonVariant.Elevated -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
        StylishButtonVariant.Tonal -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        StylishButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
        StylishButtonVariant.Text -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    }

    /** Resolves the standard Stylish corner shape. */
    @Composable
    public fun shape(): Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius)

    /** Resolves the elevation ladder for a button variant. */
    @Composable
    public fun elevation(variant: StylishButtonVariant): ButtonElevation = when (variant) {
        StylishButtonVariant.Filled -> ButtonDefaults.buttonElevation(
            defaultElevation = StylishTheme.dimensions.interactiveElevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
        StylishButtonVariant.Elevated -> ButtonDefaults.buttonElevation(
            defaultElevation = StylishTheme.dimensions.floatingElevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
        else -> ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp)
    }

    /** Resolves the optional outline for a variant. */
    @Composable
    public fun border(variant: StylishButtonVariant): BorderStroke? = when (variant) {
        StylishButtonVariant.Outlined -> BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        )
        else -> null
    }
}

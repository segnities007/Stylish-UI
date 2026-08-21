package com.segnities007.stylishui.foundation

import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * Applies the platform minimum touch target to an interactive Stylish element.
 *
 * Use this on custom clickable slots and headless structures so custom content
 * retains the same 48dp target contract as Material controls.
 */
public fun Modifier.stylishInteractiveTarget(): Modifier =
    stylishInteractiveTarget(DefaultStylishDimensions.iconButtonMinSize)

/**
 * Applies a caller-selected minimum target while retaining Material 3's local minimum-target
 * semantics. The explicit size constraint makes custom components obey the same contract even
 * when a host application changes `LocalMinimumInteractiveComponentEnforcement`.
 */
public fun Modifier.stylishInteractiveTarget(minimumTarget: Dp): Modifier =
    minimumInteractiveComponentSize().sizeIn(
        minWidth = minimumTarget,
        minHeight = minimumTarget,
    )

package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A spacing element that inserts a fixed gap between siblings,
 * backed by the Stylish Rhythm spacing scale. Use the convenience
 * factories ([StylishInlineSpacer], [StylishItemSpacer],
 * [StylishContentSpacer], [StylishSectionSpacer]) for the standard
 * steps, or pass a custom [spacing] for one-off gaps.
 *
 * @param spacing The size of the gap. Defaults to
 *   [StylishTheme.dimensions.itemSpacing].
 * @param modifier Modifier applied to the [Spacer]. Use
 *   [Modifier.height] or [Modifier.width] to control orientation;
 *   by default the spacer has no intrinsic size and must be
 *   constrained by the caller.
 *
 * @see StylishInlineSpacer
 * @see StylishItemSpacer
 * @see StylishContentSpacer
 * @see StylishSectionSpacer
 */
@Composable
public fun StylishSpacer(
    spacing: Dp = StylishTheme.dimensions.itemSpacing,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier.height(spacing))
}

/**
 * A horizontal [StylishSpacer] using
 * [StylishTheme.dimensions.inlineSpacing] (4 dp) — the smallest
 * Rhythm step, for icon-to-label gaps.
 */
@Composable
public fun StylishInlineSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.width(StylishTheme.dimensions.inlineSpacing))
}

/**
 * A vertical [StylishSpacer] using
 * [StylishTheme.dimensions.itemSpacing] (8 dp) — the small Rhythm
 * step, for items within a group.
 */
@Composable
public fun StylishItemSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.height(StylishTheme.dimensions.itemSpacing))
}

/**
 * A vertical [StylishSpacer] using
 * [StylishTheme.dimensions.contentSpacing] (16 dp) — the medium
 * Rhythm step, for distinct content blocks within a section.
 */
@Composable
public fun StylishContentSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.height(StylishTheme.dimensions.contentSpacing))
}

/**
 * A vertical [StylishSpacer] using
 * [StylishTheme.dimensions.sectionSpacing] (32 dp) — the large
 * Rhythm step, for top-level page sections.
 */
@Composable
public fun StylishSectionSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.height(StylishTheme.dimensions.sectionSpacing))
}

@Preview(name = "Stylish spacers", showBackground = true, widthDp = 393)
@Composable
private fun StylishSpacerPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Box {
                StylishItemSpacer()
            }
        }
    }
}
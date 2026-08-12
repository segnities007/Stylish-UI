package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.stylishFocusRing
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A star rating selector for choosing a value from 1 to [max] stars.
 *
 * Renders a row of [max] star icons; the first [value] stars are tinted
 * with [filledColor] and the rest with [emptyColor]. Tapping the *n*-th
 * star calls [onValueChange] with `n`. The value is hoisted — the caller
 * owns the state — so the rating never changes itself.
 *
 * Each star is announced as a radio button via semantics with `selected`
 * set per star, and the row carries a `contentDescription` summarizing
 * the rating (e.g. `評価 3 / 5`), matching the web rating-group
 * accessibility pattern. When [enabled] is `false` the stars reject
 * pointer input and are announced as disabled.
 *
 * The star row follows the web focus-visible pattern: when a star is
 * focused (keyboard navigation), a focus ring (see
 * [Modifier.stylishFocusRing]) is drawn around that star. The rating
 * uses no animation, so reduced-motion settings need no special
 * handling.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_rating` for UI tests.
 * Callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
 *
 * @param value The current rating between 0 and [max]. Values outside
 *   the range are coerced for display.
 * @param onValueChange Called with the new rating (1-based) when a star
 *   is tapped.
 * @param modifier Modifier applied to the [Row] root.
 * @param max The number of stars displayed. Defaults to 5.
 * @param enabled When `false`, the stars reject pointer input and are
 *   announced as disabled.
 * @param size The edge length of each star's touch target. Defaults to
 *   28.dp.
 * @param filledColor Tint of stars at or below [value]. Defaults to
 *   `MaterialTheme.colorScheme.primary`.
 * @param emptyColor Tint of stars above [value]. Defaults to
 *   `MaterialTheme.colorScheme.surfaceVariant`.
 * @param interactionSource The [MutableInteractionSource] shared by all
 *   stars, used to observe focus interactions (driving the focus ring).
 *   When `null`, an internal one is remembered.
 */
@Composable
public fun StylishRating(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    max: Int = 5,
    enabled: Boolean = true,
    size: Dp = 28.dp,
    filledColor: Color = MaterialTheme.colorScheme.primary,
    emptyColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val resolvedValue = value.coerceIn(0, max)
    Row(
        modifier = modifier
            .testTag("stylish_rating")
            .semantics {
                contentDescription = "評価 $resolvedValue / $max"
            }
            .then(
                if (!enabled) {
                    Modifier.semantics { disabled() }
                } else {
                    Modifier
                },
            ),
        horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(max) { index ->
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .then(
                        if (enabled) {
                            Modifier.clickable(
                                interactionSource = resolvedInteractionSource,
                                indication = LocalIndication.current,
                            ) {
                                onValueChange(index + 1)
                            }
                        } else {
                            Modifier
                        },
                    )
                    .semantics {
                        role = Role.RadioButton
                        selected = index < resolvedValue
                    }
                    .stylishFocusRing(
                        interactionSource = resolvedInteractionSource,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = if (index < resolvedValue) filledColor else emptyColor,
                )
            }
        }
    }
}

@Preview(name = "Stylish rating", showBackground = true, widthDp = 393)
@Composable
private fun StylishRatingPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.fillMaxSize()) {
            var value by remember { mutableIntStateOf(3) }
            StylishRating(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.padding(20.dp),
            )
        }
    }
}

@Preview(name = "Stylish rating disabled", showBackground = true, widthDp = 393)
@Composable
private fun StylishRatingDisabledPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.fillMaxSize()) {
            StylishRating(
                value = 4,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.padding(20.dp),
            )
        }
    }
}

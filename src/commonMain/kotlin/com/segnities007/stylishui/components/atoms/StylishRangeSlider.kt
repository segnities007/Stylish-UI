package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A range slider for selecting both endpoints of a value range,
 * wrapping the Material 3 [RangeSlider] with Stylish defaults.
 *
 * Use this when the user must pick an interval (e.g. a price band or a
 * time window). For a single value prefer [StylishSlider].
 *
 * @param value Current start..end selection of the range slider. Both
 *   endpoints are coerced to [valueRange] if outside of it.
 * @param onValueChange Called with the new start..end range while the
 *   user drags either thumb.
 * @param modifier Modifier applied to the [RangeSlider] root. The root
 *   carries the default test tag `stylish_rangeslider` for UI tests;
 *   callers can override it by passing their own `Modifier.testTag(...)`
 *   here.
 * @param enabled When `false`, the range slider ignores pointer input
 *   and renders in the disabled color scheme.
 * @param valueRange Range of values the slider can take. Defaults to
 *   `0f..1f`.
 * @param steps If positive, the number of discrete steps between the
 *   endpoints of [valueRange]. `0` makes the slider continuous.
 * @param onValueChangeFinished Called when the user completes a drag or
 *   tap. Use this for expensive operations; update the value in
 *   [onValueChange].
 * @param colors [SliderColors] for each state. Defaults to a primary
 *   thumb and active track with a [MaterialTheme.colorScheme.surfaceVariant]
 *   inactive track.
 * @param interactionSource Optional hoisted [MutableInteractionSource]
 *   for observing interactions with the range slider. When `null`, an
 *   internal instance is remembered. Note that a single source is
 *   shared by both thumbs.
 *
 * @see StylishSlider
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StylishRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary,
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.testTag("stylish_rangeslider"),
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        startInteractionSource = resolvedInteractionSource,
        endInteractionSource = resolvedInteractionSource,
    )
}

@Preview(name = "Stylish range slider", showBackground = true, widthDp = 393)
@Composable
private fun StylishRangeSliderPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var range by remember { mutableStateOf(0.2f..0.7f) }
            StylishRangeSlider(value = range, onValueChange = { range = it })
        }
    }
}

@Preview(name = "Stylish range slider disabled", showBackground = true, widthDp = 393)
@Composable
private fun StylishRangeSliderDisabledPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishRangeSlider(value = 0.2f..0.7f, onValueChange = {}, enabled = false)
        }
    }
}

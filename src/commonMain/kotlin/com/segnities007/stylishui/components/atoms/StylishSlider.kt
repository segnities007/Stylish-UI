package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A slider for selecting a single value from a continuous or stepped
 * range, wrapping the Material 3 [Slider] with Stylish defaults.
 *
 * Use a slider when the exact value is secondary to the position along
 * a range (volume, brightness, price). For selecting both endpoints of
 * a range use [StylishRangeSlider], and for a stepped on/off choice
 * prefer [StylishSwitch] or [StylishCheckbox].
 *
 * @param value Current value of the slider. Coerced to [valueRange] if
 *   outside of it.
 * @param onValueChange Called with the new value while the user drags
 *   or taps the track.
 * @param modifier Modifier applied to the [Slider] root.
 * @param enabled When `false`, the slider ignores pointer input and
 *   renders in the disabled color scheme.
 * @param valueRange Range of values the slider can take. Defaults to
 *   `0f..1f`.
 * @param steps If positive, the number of discrete steps between the
 *   endpoints of [valueRange]. For example, `0f..10f` with 4 steps
 *   allows 2, 4, 6, and 8. `0` makes the slider continuous.
 * @param onValueChangeFinished Called when the user completes a drag or
 *   tap. Use this for expensive operations; update the value in
 *   [onValueChange].
 * @param colors [SliderColors] for each state. Defaults to a primary
 *   thumb and active track with a [MaterialTheme.colorScheme.surfaceVariant]
 *   inactive track.
 * @param interactionSource Optional hoisted [MutableInteractionSource]
 *   for observing interactions with the slider. When `null`, an
 *   internal instance is remembered.
 *
 * @see StylishRangeSlider
 * @see StylishSwitch
 * @see StylishCheckbox
 */
@Composable
public fun StylishSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
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
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
    )
}

@Preview(name = "Stylish slider", showBackground = true, widthDp = 393)
@Composable
private fun StylishSliderPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var value by remember { mutableFloatStateOf(0.4f) }
            StylishSlider(value = value, onValueChange = { value = it })
        }
    }
}

@Preview(name = "Stylish slider stepped", showBackground = true, widthDp = 393)
@Composable
private fun StylishSliderSteppedPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            var value by remember { mutableFloatStateOf(0.5f) }
            StylishSlider(
                value = value,
                onValueChange = { value = it },
                valueRange = 0f..10f,
                steps = 4,
            )
        }
    }
}

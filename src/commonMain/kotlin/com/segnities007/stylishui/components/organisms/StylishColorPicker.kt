package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A platform-neutral RGB/alpha color editor.
 *
 * @param color Current color value to edit.
 * @param onColorChange Receives a color whenever one of the channels changes.
 * @param modifier Modifier applied to the channel column.
 * @param showAlpha Whether to include the alpha channel slider.
 */
@Composable
public fun StylishColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    showAlpha: Boolean = true,
) {
    Column(modifier.stylishTestTag("color_picker")) {
        val strings = StylishTheme.strings
        ColorChannel(strings.colorRed, color.red) { onColorChange(Color(it, color.green, color.blue, color.alpha)) }
        ColorChannel(strings.colorGreen, color.green) { onColorChange(Color(color.red, it, color.blue, color.alpha)) }
        ColorChannel(strings.colorBlue, color.blue) { onColorChange(Color(color.red, color.green, it, color.alpha)) }
        if (showAlpha) ColorChannel(strings.colorAlpha, color.alpha) { onColorChange(Color(color.red, color.green, color.blue, it)) }
    }
}

@Composable
private fun ColorChannel(label: String, value: Float, onChange: (Float) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.widthIn(min = 20.dp))
        Slider(
            value = value.coerceIn(0f, 1f),
            onValueChange = onChange,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth().semantics {
                contentDescription = label
                stateDescription = "${(value.coerceIn(0f, 1f) * 100).toInt()}%"
            },
        )
    }
}

@Preview(name = "Stylish color picker", showBackground = true, widthDp = 393)
@Composable
private fun StylishColorPickerPreview() {
    StylishTheme(darkTheme = false) {
        StylishColorPicker(color = Color(0.2f, 0.5f, 0.8f), onColorChange = {})
    }
}

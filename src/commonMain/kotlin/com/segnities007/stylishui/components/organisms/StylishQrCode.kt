package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Renders a QR-like boolean matrix supplied by a platform encoder.
 *
 * @param matrix Matrix rows, where `true` represents a dark module.
 * @param modifier Modifier applied to the rendered matrix.
 * @param darkColor Color used for dark modules.
 * @param lightColor Color used for light modules.
 * @param contentDescription Accessibility description; a localized default is used when blank.
 */
@Composable
public fun StylishQrCode(
    matrix: List<List<Boolean>>,
    modifier: Modifier = Modifier,
    darkColor: Color = Color.Black,
    lightColor: Color = Color.White,
    contentDescription: String = "",
) {
    val strings = StylishTheme.strings
    val columnCount = matrix.maxOfOrNull { it.size } ?: 0
    Column(modifier.testTag("stylish_qr_code").semantics {
        this.contentDescription = contentDescription.ifBlank { strings.qrCode }
        stateDescription = if (columnCount > 0) "${matrix.size} × $columnCount" else "0 × 0"
    }) {
        matrix.forEach { row ->
            Row {
                repeat(columnCount) { index ->
                    val dark = row.getOrNull(index) ?: false
                    Box(Modifier.size(4.dp).background(if (dark) darkColor else lightColor))
                }
            }
        }
    }
}

@Preview(name = "Stylish QR code", showBackground = true)
@Composable
private fun StylishQrCodePreview() {
    StylishTheme(darkTheme = false) {
        StylishQrCode(
            matrix = listOf(
                listOf(true, true, false, true),
                listOf(true, false, false, true),
                listOf(false, true, true, false),
                listOf(true, true, false, true),
            ),
        )
    }
}

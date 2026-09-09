package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishGradientFooterItem
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Renders one selectable destination in a [StylishGradientFooter].
 *
 * The molecule owns the destination's touch target, icon, label, and selected
 * color. The surrounding footer owns arrangement and background treatment.
 *
 * @param item Destination data used for the icon and accessibility label.
 * @param selected Whether this destination is the current selection.
 * @param onClick Called when the destination is activated.
 * @param modifier Modifier applied to the destination touch target.
 */
@Composable
public fun StylishGradientFooterItemContent(
    item: StylishGradientFooterItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = .72f)
    }
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .size(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = item.imageVector,
            contentDescription = item.contentDescription,
            tint = contentColor,
        )
        Text(
            text = item.contentDescription,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

@Preview(name = "Gradient footer item - light", showBackground = true, widthDp = 120)
@Composable
private fun StylishGradientFooterItemContentLightPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishGradientFooterItemContent(
                item = StylishGradientFooterItem("memo", Icons.Default.Home, "メモ"),
                selected = true,
                onClick = {},
            )
        }
    }
}

@Preview(name = "Gradient footer item - dark", showBackground = true, widthDp = 120)
@Composable
private fun StylishGradientFooterItemContentDarkPreview() {
    StylishTheme(darkTheme = true) {
        Surface {
            StylishGradientFooterItemContent(
                item = StylishGradientFooterItem("memo", Icons.Default.Home, "メモ"),
                selected = false,
                onClick = {},
            )
        }
    }
}

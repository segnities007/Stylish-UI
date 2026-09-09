package com.segnities007.stylishui.components.atoms

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.components.models.StylishTextRole
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Text resolved from a semantic [role]. By default [color] inherits the enclosing
 * surface/control's content color, including its disabled and selected states.
 * [style] is an explicit escape hatch for rich application typography.
 */
@Composable
public fun StylishText(
    text: String,
    modifier: Modifier = Modifier,
    role: StylishTextRole = StylishTextRole.Body,
    color: Color = Color.Unspecified,
    style: TextStyle? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    val heading = role in setOf(StylishTextRole.Display, StylishTextRole.Heading, StylishTextRole.Title)
    Text(
        text = text,
        modifier = modifier.stylishTestTag("text").semantics { if (heading) heading() },
        color = if (color == Color.Unspecified) LocalContentColor.current else color,
        style = style ?: stylishRoleStyle(role),
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
private fun stylishRoleStyle(role: StylishTextRole): TextStyle = with(MaterialTheme.typography) {
    when (role) {
        StylishTextRole.Display -> displaySmall
        StylishTextRole.Heading -> headlineSmall
        StylishTextRole.Title -> titleLarge
        StylishTextRole.Body -> bodyLarge
        StylishTextRole.Supporting -> bodyMedium
        StylishTextRole.Label -> labelLarge
    }
}

@Preview(name = "Semantic text — light")
@Preview(name = "Semantic text — dark", uiMode = 0x20)
@Composable
private fun StylishTextPreview() {
    StylishTheme { StylishText("A meaningful heading", role = StylishTextRole.Heading) }
}

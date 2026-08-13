package com.segnities007.stylishui.components.organisms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.molecules.StylishPopover
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import kotlinx.coroutines.delay

/**
 * A hover-triggered info card — the web "HoverCard" pattern from Radix
 * UI and shadcn/ui.
 *
 * Hovering the [trigger] shows a floating [content] card after
 * [delayMillis]. Moving the pointer away dismisses it. This is a
 * pointer (desktop) interaction: on touch devices the card does not
 * open.
 *
 * @param modifier Modifier applied to the trigger wrapper.
 * @param trigger The content that opens the card on hover.
 * @param shape Corner shape of the card. Defaults to [RoundedCornerShape]
 *   with [DefaultStylishDimensions.connectedCornerRadius].
 * @param containerColor Background of the card. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param contentColor Foreground color of the card.
 * @param width Width of the card. Defaults to 320 dp.
 * @param delayMillis Hover duration before the card opens. Defaults to
 *   300 ms.
 * @param content The card content.
 */
@Composable
public fun StylishHoverCard(
    modifier: Modifier = Modifier,
    trigger: @Composable () -> Unit,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    width: Dp = 320.dp,
    delayMillis: Int = 300,
    content: @Composable ColumnScope.() -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(hovered) {
        if (hovered) {
            delay(delayMillis.toLong())
            if (hovered) expanded = true
        } else {
            expanded = false
        }
    }

    StylishPopover(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
        anchor = {
            Box(
                Modifier.pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Enter -> hovered = true
                                PointerEventType.Exit -> hovered = false
                                else -> Unit
                            }
                        }
                    }
                },
            ) {
                trigger()
            }
        },
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        width = width,
    ) {
        content()
    }
}

@Preview(name = "Stylish hover card", showBackground = true, widthDp = 393)
@Composable
private fun StylishHoverCardPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishHoverCard(
                trigger = {
                    Text("ホバーして詳細を表示", style = MaterialTheme.typography.bodyLarge)
                },
            ) {
                Text("詳細情報カード", style = MaterialTheme.typography.titleSmall)
                Text(
                    "ホバーで表示される情報です。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

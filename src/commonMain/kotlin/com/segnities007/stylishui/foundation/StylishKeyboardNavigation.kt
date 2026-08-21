package com.segnities007.stylishui.foundation

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

/** Remembers one focus requester per item in a keyboard-navigable collection. */
@Composable
public fun rememberStylishFocusRequesters(count: Int): List<FocusRequester> = remember(count) {
    List(count.coerceAtLeast(0)) { FocusRequester() }
}

/**
 * Adds roving focus behavior to a collection item. Arrow/Home/End keys are handled and the
 * callback receives the target index; the caller owns the requesters so focus remains hoisted.
 */
public fun Modifier.stylishRovingFocus(
    requester: FocusRequester,
    index: Int,
    itemCount: Int,
    onMove: (Int) -> Unit,
    onActivate: (() -> Unit)? = null,
    onEscape: (() -> Unit)? = null,
): Modifier = focusRequester(requester)
    .focusable()
    .onPreviewKeyEvent { event ->
        if (event.type != KeyEventType.KeyDown || itemCount <= 0) return@onPreviewKeyEvent false
        if (event.key == Key.Enter || event.key == Key.Spacebar) {
            onActivate?.invoke()
            return@onPreviewKeyEvent onActivate != null
        }
        if (event.key == Key.Escape) {
            onEscape?.invoke()
            return@onPreviewKeyEvent onEscape != null
        }
        val target = when (event.key) {
            Key.DirectionLeft, Key.DirectionUp -> (index - 1).coerceAtLeast(0)
            Key.DirectionRight, Key.DirectionDown -> (index + 1).coerceAtMost(itemCount - 1)
            Key.MoveHome -> 0
            Key.MoveEnd -> itemCount - 1
            else -> return@onPreviewKeyEvent false
        }
        if (target != index) onMove(target)
        true
    }

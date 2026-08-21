package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishContentState
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Renders the shared Loading/Empty/Error/Content state machine.
 *
 * Use this at the boundary between a screen's state holder and its visual content. The default
 * branches are deliberately modest and localization-aware; production screens can replace each
 * branch with a slot without changing their state model or introducing boolean combinations.
 */
@Composable
public fun <T> StylishContentStateHost(
    state: StylishContentState<T>,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = {
        Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    },
    emptyContent: @Composable (String?) -> Unit = { message ->
        Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
            Text(message ?: StylishTheme.strings.empty, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    },
    errorContent: @Composable (StylishContentState.Error) -> Unit = { error ->
        Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
            Text(error.message, color = MaterialTheme.colorScheme.error)
        }
    },
    content: @Composable (T) -> Unit,
) {
    Box(modifier) {
        when (state) {
            StylishContentState.Loading -> loadingContent()
            is StylishContentState.Empty -> emptyContent(state.message)
            is StylishContentState.Error -> errorContent(state)
            is StylishContentState.Content -> content(state.value)
        }
    }
}

@Preview(name = "Content state host", showBackground = true, widthDp = 393)
@Composable
private fun StylishContentStateHostPreview() {
    StylishTheme(darkTheme = false) {
        StylishContentStateHost(StylishContentState.Content("Ready")) { value -> Text(value) }
    }
}

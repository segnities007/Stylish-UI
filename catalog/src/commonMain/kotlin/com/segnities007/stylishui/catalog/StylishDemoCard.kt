@file:Suppress("DEPRECATION")

package com.segnities007.stylishui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A preview-first demo card for the Stylish UI catalog gallery.
 *
 * Displays the component preview as the primary content with minimal chrome.
 * Clicking the card expands a code panel below.
 *
 * This design matches the uiverse.io gallery aesthetic: clean, preview-focused,
 * with interactive reveals rather than persistent labels.
 *
 * @param name Display name shown above the preview.
 * @param code Kotlin source code snippet for the component.
 * @param modifier Modifier applied to the card.
 * @param preview Composable that renders the interactive preview.
 */
@Composable
public fun StylishDemoCard(
    name: String,
    code: String,
    modifier: Modifier = Modifier,
    preview: @Composable () -> Unit,
) {
    var showCode by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Card container
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 1.dp,
            shadowElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Component name
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                
                // Preview content
                preview()

                // Code toggle button
                TextButton(
                    onClick = { showCode = !showCode },
                    modifier = Modifier.semantics {
                        role = Role.Button
                        contentDescription = if (showCode) "コードを隠す" else "コードを表示"
                    },
                ) {
                    Icon(
                        Icons.Default.Code,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp),
                    )
                    Text(if (showCode) "コードを隠す" else "コードを表示")
                }
            }
        }

        // Expandable code panel
        if (showCode) {
            CodePanel(code, Modifier.padding(top = 12.dp))
        }
    }
}

/**
 * Dark-themed code panel with syntax-friendly monospace font and copy button.
 *
 * @param code The Kotlin source code to display.
 * @param modifier Modifier applied to the panel.
 */
@Composable
private fun CodePanel(
    code: String,
    modifier: Modifier = Modifier,
) {
    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    var copied by remember { mutableStateOf(false) }
    val panelShape = RoundedCornerShape(12.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = panelShape,
        color = Color(0xFF1E1F22),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                code,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFE6E6E6),
                ),
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 4.dp),
            )
            IconButton(onClick = {
                clipboard.setText(AnnotatedString(code))
                copied = true
                scope.launch {
                    delay(1500)
                    copied = false
                }
            }) {
                Icon(
                    imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = "コードをコピー",
                    tint = Color(0xFF9A9A9E),
                )
            }
        }
    }
}

/**
 * Preview of the demo card in dark theme.
 */
@androidx.compose.ui.tooling.preview.Preview(name = "Demo Card - Dark", showBackground = false, widthDp = 320)
@Composable
private fun StylishDemoCardPreview() {
    StylishTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            StylishDemoCard(
                name = "Button variants",
                code = """StylishButton(
    onClick = {},
    variant = StylishButtonVariant.Filled,
) { Text("保存する") }""",
                preview = {
                    com.segnities007.stylishui.components.atoms.StylishButton(onClick = {}) {
                        Text("保存する")
                    }
                },
            )
        }
    }
}

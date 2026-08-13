package com.segnities007.stylishui.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A playable demo card of the Stylish playground — title, description,
 * an interactive preview area, and a collapsible code snippet with a
 * copy button. Styled like the shadcn/Tailwind UI blocks gallery:
 * white surface, hairline border, tinted preview area, dark code block.
 *
 * This is part of the public catalog (like [StylishComponentCatalog]);
 * it is not a design-system component but the website gallery's building
 * block.
 *
 * @param title Name of the demo.
 * @param description One-line explanation of the demo.
 * @param code The Kotlin snippet shown when the code section is
 *   expanded.
 * @param modifier Modifier applied to the card.
 * @param content The interactive preview.
 */
@Composable
public fun StylishDemoCard(
    title: String,
    description: String,
    code: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    var showCode by remember { mutableStateOf(false) }
    val reducedMotion = isStylishReducedMotionEnabled()
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, cardShape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, cardShape),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (description.isNotEmpty()) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
        // Preview area with a subtle tinted background, separated from the
        // card body like the shadcn component previews.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    RoundedCornerShape(12.dp),
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            content()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = { showCode = !showCode }) {
                Text(if (showCode) "コードを隠す" else "コードを表示")
            }
        }
        AnimatedVisibility(
            visible = showCode,
            enter = expandVertically(
                animationSpec = tween(if (reducedMotion) 0 else StylishTheme.animation.durationShort),
            ),
            exit = shrinkVertically(
                animationSpec = tween(if (reducedMotion) 0 else StylishTheme.animation.durationShort),
            ),
        ) {
            CodeBlock(code)
        }
    }
}

@Composable
private fun CodeBlock(code: String) {
    val clipboard = androidx.compose.ui.platform.LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    var copied by remember { mutableStateOf(false) }
    val blockShape = RoundedCornerShape(12.dp)

    // Dark code block, shadcn style.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1F22), blockShape)
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

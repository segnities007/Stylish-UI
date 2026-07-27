package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.segnities007.stylishui.theme.StylishTheme

/** ダイアログサーフェス。スケール・フェードアニメーション付きのダイアログ用コンテナ。 */
@Composable
public fun StylishDialogSurface(
    onDismiss: () -> Unit,
    animate: Boolean = true,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color? = null,
    horizontalPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val dialogScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.92f,
        animationSpec = tween(durationMillis = 200, easing = { it * it }),
        label = "dialogScale",
    )
    val dialogAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 180),
        label = "dialogAlpha",
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .then(
                    if (animate) {
                        Modifier.graphicsLayer {
                            scaleX = dialogScale
                            scaleY = dialogScale
                            alpha = dialogAlpha
                        }
                    } else {
                        Modifier
                    },
                ),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            content = content,
        )
    }
}

@Preview(name = "Stylish dialog surface", showBackground = true, widthDp = 393, heightDp = 500)
@Composable
private fun StylishDialogSurfacePreview() {
    StylishTheme(darkTheme = false) {
        StylishDialogSurface({}) {
            Text("ダイアログ", Modifier.padding(24.dp), style = MaterialTheme.typography.titleLarge)
        }
    }
}

package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import kotlinx.coroutines.delay

/**
 * Visual variants of a toast shown by [StylishToastHost].
 */
public enum class StylishToastVariant { Normal, Info, Success, Warning, Error }

/**
 * A single toast entry managed by [StylishToastHostState].
 *
 * @property message The toast body text.
 * @property variant The semantic variant controlling colors and icon.
 * @property durationMillis How long the toast stays visible before it is
 *   dismissed automatically. Defaults to 3000 ms.
 * @property actionLabel Optional action button label.
 * @property onAction Optional callback invoked when the action button is
 *   tapped.
 */
public data class StylishToastData(
    public val message: String,
    public val variant: StylishToastVariant = StylishToastVariant.Normal,
    public val durationMillis: Long = 3000L,
    public val actionLabel: String? = null,
    public val onAction: (() -> Unit)? = null,
)

/**
 * State holder for a stack of toasts displayed by [StylishToastHost].
 *
 * Create with [rememberStylishToastHostState] and show toasts from any
 * coroutine scope:
 *
 * ```kotlin
 * val hostState = rememberStylishToastHostState()
 * val scope = rememberCoroutineScope()
 * scope.launch { hostState.showToast(StylishToastData("保存しました", StylishToastVariant.Success)) }
 * ```
 *
 * Toasts are displayed bottom-stacked and dismissed automatically after
 * their [StylishToastData.durationMillis].
 */
public class StylishToastHostState internal constructor() {
    internal val toasts: SnapshotStateList<StylishToastData> = mutableStateListOf()

    /** Shows [toast] and dismisses it automatically after its duration. */
    public suspend fun showToast(toast: StylishToastData) {
        toasts.add(toast)
        delay(toast.durationMillis)
        dismiss(toast)
    }

    /** Removes [toast] from the stack immediately. */
    public fun dismiss(toast: StylishToastData) {
        toasts.remove(toast)
    }
}

/**
 * Creates and remembers a [StylishToastHostState].
 *
 * @see StylishToastHostState
 */
@Composable
public fun rememberStylishToastHostState(): StylishToastHostState =
    remember { StylishToastHostState() }

/**
 * A stackable toast display — the web "Toast/Message/Notification"
 * pattern from shadcn/ui, Sonner, Ant Design, and Chakra UI.
 *
 * Composes the toasts of [hostState] at the bottom of its content box.
 * Newest toasts appear above older ones. Each toast auto-dismisses after
 * its [StylishToastData.durationMillis] and can carry an action button.
 *
 * @param hostState The toast stack state.
 * @param modifier Modifier applied to the root box (fill the parent to
 *   anchor toasts at the bottom edge).
 * @param shape Corner shape of each toast. Defaults to [RoundedCornerShape]
 *   with [DefaultStylishDimensions.connectedCornerRadius].
 * @param containerColor Background of each toast. Defaults to the inverse
 *   surface.
 * @param contentColor Foreground of each toast. Defaults to the inverse
 *   on-surface.
 */
@Composable
public fun StylishToastHost(
    hostState: StylishToastHostState,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
) {
    val reducedMotion = isStylishReducedMotionEnabled()
    val fadeSpec = if (reducedMotion) {
        tween<Float>(0)
    } else {
        tween<Float>(StylishTheme.animation.durationShort)
    }
    val sizeSpec = if (reducedMotion) {
        tween<androidx.compose.ui.unit.IntSize>(0)
    } else {
        tween<androidx.compose.ui.unit.IntSize>(StylishTheme.animation.durationShort)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        hostState.toasts.forEach { toast ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(fadeSpec) + expandVertically(sizeSpec),
                exit = fadeOut(fadeSpec) + shrinkVertically(sizeSpec),
            ) {
                StylishToast(
                    toast = toast,
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    onDismiss = { hostState.dismiss(toast) },
                )
            }
        }
    }
}

@Composable
private fun StylishToast(
    toast: StylishToastData,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    onDismiss: () -> Unit,
) {
    val accentColor = when (toast.variant) {
        StylishToastVariant.Normal -> contentColor
        StylishToastVariant.Info -> MaterialTheme.colorScheme.inversePrimary
        StylishToastVariant.Success -> MaterialTheme.colorScheme.inversePrimary
        StylishToastVariant.Warning -> MaterialTheme.colorScheme.inversePrimary
        StylishToastVariant.Error -> MaterialTheme.colorScheme.error
    }
    val icon = when (toast.variant) {
        StylishToastVariant.Normal -> null
        StylishToastVariant.Info -> Icons.Default.Info
        StylishToastVariant.Success -> Icons.Default.CheckCircle
        StylishToastVariant.Warning -> Icons.Default.Warning
        StylishToastVariant.Error -> Icons.Default.Error
    }

    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier.padding(
            horizontal = DefaultStylishDimensions.contentSpacing,
            vertical = DefaultStylishDimensions.inlineSpacing,
        ),
        shadowElevation = DefaultStylishDimensions.floatingElevation,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = DefaultStylishDimensions.controlPadding,
                vertical = 10.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                toast.message,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (toast.actionLabel != null) {
                TextButton(onClick = { toast.onAction?.invoke() }) {
                    Text(toast.actionLabel, color = accentColor)
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "閉じる",
                    tint = contentColor,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Preview(name = "Stylish toast host", showBackground = true, widthDp = 393)
@Composable
private fun StylishToastHostPreview() {
    StylishTheme(darkTheme = false) {
        val hostState = rememberStylishToastHostState()
        LaunchedEffect(Unit) {
            hostState.toasts.addAll(
                listOf(
                    StylishToastData("保存しました", StylishToastVariant.Success),
                    StylishToastData("エラーが発生しました", StylishToastVariant.Error, actionLabel = "再試行"),
                ),
            )
        }
        Surface {
            StylishToastHost(hostState)
        }
    }
}

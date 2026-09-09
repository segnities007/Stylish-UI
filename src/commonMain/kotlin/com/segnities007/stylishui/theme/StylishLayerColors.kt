package com.segnities007.stylishui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.lerp

/**
 * The observed raw-depth interval for one visual root.
 *
 * Raw depth is an accumulated structural value. It is deliberately kept
 * separate from color: the interval is normalized only after all currently
 * composed surfaces have reported their depth. This makes the shallowest
 * and deepest surface the two stable endpoints of the continuous palette.
 */
internal data class StylishLayerRange(
    val minDepth: Float,
    val maxDepth: Float,
) {
    /** Converts a raw depth to the palette's clamped 0..1 coordinate. */
    fun normalize(depth: Float): Float {
        val safeDepth = depth.takeIf { it.isFinite() } ?: minDepth
        val span = maxDepth - minDepth
        return if (span > MinimumLayerSpan) {
            ((safeDepth - minDepth) / span).coerceIn(0f, 1f)
        } else {
            // A root with one visual surface has no interval to interpolate.
            // Keep the historical four-step ladder as a deterministic
            // fallback until a second depth is observed.
            (safeDepth / CanonicalLayerSpan).coerceIn(0f, 1f)
        }
    }

    internal companion object {
        /** Default used before the first composition reports a surface. */
        val Empty = StylishLayerRange(0f, CanonicalLayerSpan)
    }
}

private const val MinimumLayerSpan = 0.0001f
private const val CanonicalLayerSpan = 4f
private const val StructuralDepthStep = 1f

// Keep role offsets smaller than one structural step. This preserves the
// invariant that a nested surface is always above its parent while giving
// sibling roles a deterministic visual order: card < control < floating.
private const val CardSurfaceDepthStep = StructuralDepthStep
private const val ControlSurfaceDepthStep = StructuralDepthStep + 0.2f
private const val FloatingSurfaceDepthStep = StructuralDepthStep + 0.4f

/**
 * Tracks visual surfaces that are currently composed below one root.
 *
 * Registration is lifecycle-aware: a surface is removed when its composition
 * slot leaves the tree, so a disappearing dialog or lazy item cannot leave a
 * stale maximum that would flatten the remaining palette.
 */
@Stable
internal class StylishLayerRegistry {
    private val contributions = mutableStateMapOf<Any, Float>()

    /** The live min/max interval; reading it observes the snapshot map. */
    val range: StylishLayerRange
        get() {
            var minDepth = Float.POSITIVE_INFINITY
            var maxDepth = Float.NEGATIVE_INFINITY
            contributions.values.forEach { depth ->
                if (depth.isFinite()) {
                    minDepth = minOf(minDepth, depth)
                    maxDepth = maxOf(maxDepth, depth)
                }
            }
            return if (minDepth.isFinite() && maxDepth.isFinite()) {
                StylishLayerRange(minDepth, maxDepth)
            } else {
                StylishLayerRange.Empty
            }
        }

    fun register(key: Any, depth: Float) {
        if (depth.isFinite()) {
            contributions[key] = depth
        }
    }

    fun unregister(key: Any) {
        contributions.remove(key)
    }
}

private val LocalStylishLayerRegistry = staticCompositionLocalOf<StylishLayerRegistry?> { null }
private val LocalStylishElevationDepth = staticCompositionLocalOf { 0f }

/**
 * Starts a page-level visual root.
 *
 * [StylishTheme] owns this root automatically. Keeping it as a separate
 * internal seam also lets window-backed surfaces establish their own range.
 */
@Composable
internal fun StylishLayerRoot(content: @Composable () -> Unit) {
    val registry = remember { StylishLayerRegistry() }
    CompositionLocalProvider(
        LocalStylishLayerRegistry provides registry,
        LocalStylishElevationDepth provides 0f,
        content = content,
    )
}

/** Marks content as being placed above a modal surface and starts a fresh range. */
@Composable
internal fun StylishModalLayer(content: @Composable () -> Unit) {
    val registry = remember { StylishLayerRegistry() }
    CompositionLocalProvider(
        LocalStylishLayerRegistry provides registry,
        LocalStylishElevationDepth provides 0f,
        content = content,
    )
}

/**
 * Resolves a floating surface above normal page cards and interactive controls.
 * Sibling role offsets are part of the raw depth, so a FAB/bar remains above
 * a card even when both are composed at the same structural depth.
 */
@Composable
internal fun stylishFloatingContainerColor(alpha: Float = 0.9f): Color {
    return stylishSurfaceColor(depthStep = FloatingSurfaceDepthStep, alpha = alpha)
}

/** Resolves an opaque modal surface above the floating layer. */
@Composable
internal fun stylishModalContainerColor(): Color {
    return stylishLayerColor(StylishElevationLevel.Modal.level)
}

/**
 * Resolves an opaque interactive surface above its containing page or card.
 * This is the default tone for controls that own a container, including
 * outlined buttons whose border should not be the only separation cue.
 */
@Composable
internal fun stylishElevatedControlContainerColor(): Color {
    return stylishSurfaceColor(depthStep = ControlSurfaceDepthStep)
}

/** Resolves an opaque card surface relative to the page or modal that contains it. */
@Composable
internal fun stylishCardContainerColor(): Color {
    return stylishSurfaceColor(depthStep = CardSurfaceDepthStep)
}

/**
 * Registers one visual surface and resolves its normalized color.
 *
 * The registration runs as a [DisposableEffect] so the range follows the
 * actual composed tree. [StylishElevationLayer] changes only the raw depth;
 * non-surface layout nodes do not change the interval.
 */
@Composable
private fun stylishSurfaceColor(depthStep: Float, alpha: Float = 1f): Color {
    val depth = LocalStylishElevationDepth.current + depthStep
    val registry = LocalStylishLayerRegistry.current
    val registrationKey = remember { Any() }
    if (registry != null) {
        DisposableEffect(registry, registrationKey, depth) {
            registry.register(registrationKey, depth)
            onDispose { registry.unregister(registrationKey) }
        }
    }
    val level = registry?.range?.normalize(depth)
        ?: StylishLayerRange.Empty.normalize(depth)
    return stylishLayerColor(level).copy(alpha = alpha)
}

/**
 * Resolves a surface color at any normalized visual elevation [level].
 *
 * `0.0f` is the page canvas and `1.0f` is the highest modal surface. Values
 * between them are interpolated in Oklab so equal level steps produce a more
 * perceptually even lightness progression than direct RGB interpolation.
 * Values outside the range are clamped.
 *
 * The active [MaterialTheme] supplies both endpoints, so custom color schemes
 * retain control over the hue and the available lightness range.
 *
 * @param level Normalized visual elevation from `0.0f` to `1.0f`.
 */
@Composable
public fun stylishLayerColor(level: Float): Color {
    val scheme = MaterialTheme.colorScheme
    return interpolateLayerColor(
        bottom = scheme.background,
        top = scheme.surfaceBright,
        level = level,
    )
}

internal fun interpolateLayerColor(bottom: Color, top: Color, level: Float): Color {
    val oklabBottom = bottom.convert(ColorSpaces.Oklab)
    val oklabTop = top.convert(ColorSpaces.Oklab)
    return lerp(oklabBottom, oklabTop, level.coerceIn(0f, 1f)).convert(ColorSpaces.Srgb)
}

/**
 * Reduces a layer color's chroma without changing its Oklab lightness.
 *
 * Layer endpoints are authored once and then reused by the continuous
 * surface scale. Applying this operation before interpolation keeps the
 * palette's perceptual lightness ladder intact while making the milk-tea tint
 * quieter.
 * The scale is clamped so callers cannot accidentally increase saturation.
 *
 * @param color Source layer color.
 * @param chromaScale Fraction of the original Oklab chroma to retain.
 */
internal fun desaturateLayerColor(color: Color, chromaScale: Float): Color {
    val oklab = color.convert(ColorSpaces.Oklab)
    val scale = chromaScale.coerceIn(0f, 1f)
    return Color(
        red = oklab.red,
        green = oklab.green * scale,
        blue = oklab.blue * scale,
        alpha = oklab.alpha,
        colorSpace = ColorSpaces.Oklab,
    ).convert(ColorSpaces.Srgb)
}

/**
 * Z-order level of a surface, bottom (dark) to top (bright).
 *
 * The level owns the container color: deeper surfaces resolve darker,
 * higher surfaces resolve brighter, in both light and dark themes.
 * Components declare only their level instead of hard-coding a container
 * role, so palette tuning propagates from this single table.
 *
 * The named levels are representative points on one continuous scale:
 *
 * | Level | Scale position |
 * |---|---|
 * | [Page] | 0.0 |
 * | [Surface] | 0.25 |
 * | [Raised] | 0.5 |
 * | [Overlay] | 0.75 |
 * | [Modal] | 1.0 |
 *
 * Use [stylishLayerColor] directly when a surface needs an intermediate
 * position instead of adding another named level.
 *
 * Exceptions (opt out, keep their own treatment): the fading
 * [com.segnities007.stylishui.components.patterns.StylishGradientFooter]
 * family, scrims, inverse surfaces such as tooltips, and spec-owned
 * containers (menus, drawers, and standard Material navigation rails).
 * Translucent floating surfaces still use the dynamic role order through
 * [stylishFloatingContainerColor].
 *
 * @property level Normalized position on the continuous layer scale.
 */
public enum class StylishElevationLevel(public val level: Float) {
    /** The page canvas itself. */
    Page(0f),

    /** Cards and list surfaces resting on the page. */
    Surface(0.25f),

    /** Popovers, palettes, and search surfaces above page content. */
    Raised(0.5f),

    /** Hover cards and transient panels above raised surfaces. */
    Overlay(0.75f),

    /** Opaque modal surfaces (dialogs, sheets) at the very top. */
    Modal(1f),
}

/**
 * Resolves the container color for this [StylishElevationLevel].
 *
 * @see StylishElevationLevel
 */
@Composable
public fun StylishElevationLevel.containerColor(): Color = stylishLayerColor(level)

/**
 * Provides one deeper raw visual-depth step to [content].
 *
 * Surface components that own a container wrap their content so nested
 * surfaces resolve a larger raw depth automatically. The active root then
 * calculates its min/max interval and maps those depths to the continuous
 * palette. This function itself does not register a surface: a [Column],
 * [Box], or other layout wrapper should not consume a color level.
 *
 * Dialogs and popups create their own windows: keep marking them with
 * [StylishModalLayer] so their interval is independent from the page.
 * Explicit container colors always win over automatic resolution.
 *
 * Surfaces read their own level with [stylishNestedLevel]:
 *
 * ```kotlin
 * @Composable
 * fun MySurface(content: @Composable () -> Unit) {
 *     Surface(color = stylishNestedLevel().containerColor()) {
 *         StylishElevationLayer(content = content)
 *     }
 * }
 * ```
 */
@Composable
public fun StylishElevationLayer(content: @Composable () -> Unit) {
    val depth = LocalStylishElevationDepth.current
    CompositionLocalProvider(
        LocalStylishElevationDepth provides (depth + StructuralDepthStep),
        content = content,
    )
}

/**
 * Resolves the [StylishElevationLevel] for a surface composing at the
 * current tree depth: one step above its parent scope, clamped at
 * [StylishElevationLevel.Modal] (a surface is never [StylishElevationLevel.Page]).
 *
 * Modal content is placed in a separate root by [StylishModalLayer], so its
 * depth interval is resolved independently from the page interval.
 *
 * @see StylishElevationLayer
 * @see StylishElevationLevel
 */
@Composable
public fun stylishNestedLevel(): StylishElevationLevel {
    val depth = LocalStylishElevationDepth.current + 1f
    return when {
        depth <= 1f -> StylishElevationLevel.Surface
        depth <= 2f -> StylishElevationLevel.Raised
        depth <= 3f -> StylishElevationLevel.Overlay
        else -> StylishElevationLevel.Modal
    }
}

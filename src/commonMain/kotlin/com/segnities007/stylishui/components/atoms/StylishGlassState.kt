package com.segnities007.stylishui.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.unit.toIntSize

/**
 * すりガラス(backdrop blur)のための共有状態。
 *
 * [Modifier.stylishGlassSource] を付けた背景コンテンツの描画を毎フレーム
 * GraphicsLayer へ録画し、録画完了後に効果側ノードへ直接 再描画要求を出す。
 * 描画フェーズ中のスナップショット書き込みは行わない(HWUI クラッシュ対策)。
 *
 * 1画面につき1インスタンスを作り、背景とガラスの両方に渡す。
 */
public class StylishGlassState internal constructor() {
    internal val areas = mutableListOf<Area>()
    internal val effects = mutableListOf<StylishGlassEffectNode>()

    /** ソースの録画完了後にガラス側へ再描画を要求する(draw フェーズ外から呼ばれる)。 */
    internal fun notifyRecorded() {
        // 逆順イテレーションで detach 中の除去にも耐える
        for (i in effects.indices.reversed()) {
            effects.getOrNull(i)?.invalidateDraw()
        }
    }

    internal class Area {
        var position: Offset = Offset.Zero
            set(value) {
                field = value
                onChanged?.invoke()
            }
        var onChanged: (() -> Unit)? = null
        var layer: GraphicsLayer? = null
    }
}

/**
 * 背景コンテンツをすりガラスのソースとして録画する。
 * 1つの [StylishGlassState] に対して複数付与できる。
 */
public fun Modifier.stylishGlassSource(state: StylishGlassState): Modifier =
    this.then(StylishGlassSourceElement(state))

internal class StylishGlassSourceElement(
    private val state: StylishGlassState,
) : ModifierNodeElement<StylishGlassSourceNode>() {
    override fun create(): StylishGlassSourceNode = StylishGlassSourceNode(state)
    override fun update(node: StylishGlassSourceNode) { node.state = state }
    override fun equals(other: Any?): Boolean = other is StylishGlassSourceElement && other.state === state
    override fun hashCode(): Int = state.hashCode()
}

internal class StylishGlassSourceNode(
    var state: StylishGlassState,
) : Modifier.Node(),
    DrawModifierNode,
    GlobalPositionAwareModifierNode,
    CompositionLocalConsumerModifierNode {

    private val area = StylishGlassState.Area()

    override val shouldAutoInvalidate: Boolean get() = true

    init {
        area.onChanged = { /* 位置変化は効果側の onGloballyPositioned 経由で反映 */ }
    }

    override fun ContentDrawScope.draw() {
        if (!size.isEmpty()) {
            val context = currentValueOf(LocalGraphicsContext)
            val l = area.layer ?: context.createGraphicsLayer().also { area.layer = it }
            l.record(size.toIntSize()) {
                this@draw.drawContent()
            }
            drawLayer(l)
            // 録画が済んだフレームの内容を効果側へ通知(draw フェーズの状態書き込みなし)
            state.notifyRecorded()
        } else {
            drawContent()
        }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        area.position = coordinates.positionInRoot()
    }

    override fun onAttach() {
        super.onAttach()
        state.areas.add(area)
    }

    override fun onDetach() {
        state.areas.remove(area)
        area.layer?.let { currentValueOf(LocalGraphicsContext).releaseGraphicsLayer(it) }
        area.layer = null
        super.onDetach()
    }
}

/** 動的すりガラスの再生・ぼかしを行う効果モディファイア。 */
public fun Modifier.stylishGlassEffect(
    state: StylishGlassState,
    blurRadiusPx: Float,
): Modifier = this.then(StylishGlassEffectElement(state, blurRadiusPx))

internal class StylishGlassEffectElement(
    private val state: StylishGlassState,
    private val blurRadiusPx: Float,
) : ModifierNodeElement<StylishGlassEffectNode>() {
    override fun create(): StylishGlassEffectNode =
        StylishGlassEffectNode(state, blurRadiusPx)
    override fun update(node: StylishGlassEffectNode) {
        node.state = state
        node.blurRadiusPx = blurRadiusPx
        node.refreshParams()
    }
    override fun equals(other: Any?): Boolean =
        other is StylishGlassEffectElement &&
            other.state === state && other.blurRadiusPx == blurRadiusPx
    override fun hashCode(): Int = state.hashCode() * 31 + blurRadiusPx.hashCode()
}

internal class StylishGlassEffectNode(
    var state: StylishGlassState,
    var blurRadiusPx: Float,
) : Modifier.Node(),
    DrawModifierNode,
    GlobalPositionAwareModifierNode {

    private var position: Offset = Offset.Zero

    // 録画側から明示的に invalidateDraw されるため自動無効化は不要
    override val shouldAutoInvalidate: Boolean get() = false

    fun refreshParams() {
        invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        val myPos = position
        for (area in state.areas) {
            val p = area.position
            translate(p.x - myPos.x, p.y - myPos.y) {
                area.layer?.let(::drawLayer)
            }
        }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        position = coordinates.positionInRoot()
    }

    override fun onAttach() {
        super.onAttach()
        state.effects.add(this)
    }

    override fun onDetach() {
        state.effects.remove(this)
        super.onDetach()
    }
}

/**
 * 実行環境が backdrop blur(RenderEffect)をサポートしているか。
 * 未サポート環境では磨りガラスはティント+白濁のみのフォールバック表示になる。
 */
internal expect fun isGlassBlurSupported(): Boolean

/** 画面ごとに 1 つ作る [StylishGlassState] を記憶する。 */
@Composable
public fun rememberStylishGlassState(): StylishGlassState =
    androidx.compose.runtime.remember { StylishGlassState() }

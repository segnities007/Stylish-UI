package com.segnities007.stylishui.components.atoms

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize

/**
 * すりガラス(backdrop blur)のための共有状態。
 *
 * [Modifier.stylishGlassSource] を付けた背景コンテンツの描画を毎フレーム
 * GraphicsLayer へ録画し、[StylishFrostedGlassSurface](glassState 指定時)が
 * 自分の位置に合わせて再生・ぼかしする。スクロールする動的コンテンツにも追従する。
 *
 * 1画面につき1インスタンスを作り、背景とガラスの両方に渡す。
 */
public class StylishGlassState {
    internal val areas = mutableListOf<Area>()
    internal val revision = mutableIntStateOf(0)

    /** ガラス側の描画を自動再描画させるための依存登録カウンタ。 */
    internal fun invalidate() {
        revision.intValue = revision.intValue + 1
    }

    internal class Area {
        val position = mutableStateOf(Offset.Zero)
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
    private var layer: GraphicsLayer? = null

    // スナップショット読み替え中も確実に録画されるよう手動管理はしない
    override val shouldAutoInvalidate: Boolean get() = true

    override fun ContentDrawScope.draw() {
        val context = currentValueOf(LocalGraphicsContext)
        val l = layer ?: context.createGraphicsLayer().also {
            layer = it
            area.layer = it
        }
        l.record(size.toIntSize()) {
            this@draw.drawContent()
        }
        drawLayer(l)
        state.invalidate()
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        area.position.value = coordinates.positionInRoot()
    }

    override fun onAttach() {
        super.onAttach()
        state.areas.add(area)
    }

    override fun onDetach() {
        state.areas.remove(area)
        layer?.let { currentValueOf(LocalGraphicsContext).releaseGraphicsLayer(it) }
        layer = null
        area.layer = null
        super.onDetach()
    }
}

/**
 * 実行環境が backdrop blur(RenderEffect)をサポートしているか。
 * 未サポート環境では磨りガラスはティント+白濁のみのフォールバック表示になる。
 */
internal expect fun isGlassBlurSupported(): Boolean

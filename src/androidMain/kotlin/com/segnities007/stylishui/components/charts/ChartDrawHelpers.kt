package com.segnities007.stylishui.components.charts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * 上部2隅のみ角丸の矩形を描画する（積み上げ棒グラフの最上段用）。
 */
fun DrawScope.drawTopRoundedRect(
    color: Color,
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    radius: Float,
) {
    if (height <= 0f || width <= 0f) return
    val r = radius.coerceAtMost(height).coerceAtMost(width / 2f)
    val path = Path().apply {
        moveTo(left, top + height)
        lineTo(left, top + r)
        quadraticTo(left, top, left + r, top)
        lineTo(left + width - r, top)
        quadraticTo(left + width, top, left + width, top + r)
        lineTo(left + width, top + height)
        close()
    }
    drawPath(path, color)
}

package com.segnities007.stylishui.foundation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/** 連結コンポーネントの各角が外側（大半径）かどうかを表すデータ。 */
@Immutable
public data class ConnectedCorners(
    public val topStart: Boolean = false,
    public val topEnd: Boolean = false,
    public val bottomStart: Boolean = false,
    public val bottomEnd: Boolean = false,
) {
    /** 単独配置時の既定角定義。 */
    public companion object {
        /** 全角が外側角のインスタンス。 */
        public val Standalone: ConnectedCorners = ConnectedCorners(true, true, true, true)
    }
}

/** 連結コンポーネントの各辺に境界線を描画するかどうかを表すデータ。 */
@Immutable
public data class ConnectedEdges(
    public val top: Boolean,
    public val end: Boolean,
    public val bottom: Boolean,
    public val start: Boolean,
) {
    /** 全辺に境界線を描画する既定定義。 */
    public companion object {
        /** 全辺が有効なインスタンス。 */
        public val All: ConnectedEdges = ConnectedEdges(true, true, true, true)
    }
}

/** [corners] に基づいて連結コンポーネント用の [Shape] を生成する。 */
public fun connectedShape(
    corners: ConnectedCorners,
    cornerRadius: Dp = DefaultStylishDimensions.connectedCornerRadius,
    joinedCornerRadius: Dp = DefaultStylishDimensions.joinedCornerRadius,
): Shape = RoundedCornerShape(
    topStart = if (corners.topStart) cornerRadius else joinedCornerRadius,
    topEnd = if (corners.topEnd) cornerRadius else joinedCornerRadius,
    bottomStart = if (corners.bottomStart) cornerRadius else joinedCornerRadius,
    bottomEnd = if (corners.bottomEnd) cornerRadius else joinedCornerRadius,
)

/** 縦方向リストの [index] 番目における外側角を計算する。 */
public fun connectedColumnCorners(index: Int, size: Int): ConnectedCorners = ConnectedCorners(
    topStart = index == 0,
    topEnd = index == 0,
    bottomStart = index == size - 1,
    bottomEnd = index == size - 1,
)

/** 横方向リストの [index] 番目における外側角を計算する。 */
public fun connectedRowCorners(index: Int, size: Int): ConnectedCorners = ConnectedCorners(
    topStart = index == 0,
    bottomStart = index == 0,
    topEnd = index == size - 1,
    bottomEnd = index == size - 1,
)

/** グリッド配置の [index] 番目における外側角を隣接関係から計算する。 */
public fun connectedGridCorners(index: Int, size: Int, columns: Int): ConnectedCorners {
    require(columns > 0) { "columns must be greater than zero" }
    require(index in 0 until size) { "index must reference an existing item" }

    val column = index % columns
    val row = index / columns
    // 同じ行内の左右は実座標のまま厳密に判定する
    fun hasHorizontalNeighbor(targetColumn: Int): Boolean {
        if (targetColumn !in 0 until columns) return false
        return row * columns + targetColumn in 0 until size
    }

    // 上下も同じ列に実際にアイテムがある場合のみ隣接とみなす
    fun hasVerticalNeighbor(targetRow: Int): Boolean {
        if (targetRow < 0) return false
        return targetRow * columns + column in 0 until size
    }

    val hasAbove = hasVerticalNeighbor(row - 1)
    val hasBelow = hasVerticalNeighbor(row + 1)
    val hasLeft = hasHorizontalNeighbor(column - 1)
    val hasRight = hasHorizontalNeighbor(column + 1)

    return ConnectedCorners(
        topStart = !hasAbove && !hasLeft,
        topEnd = !hasAbove && !hasRight,
        bottomStart = !hasBelow && !hasLeft,
        bottomEnd = !hasBelow && !hasRight,
    )
}

/** 横方向リストの [index] 番目における描画辺を返す。 */
public fun connectedRowEdges(index: Int, size: Int): ConnectedEdges {
    require(index in 0 until size)
    return ConnectedEdges.All
}

/** 縦方向リストの [index] 番目における描画辺を返す。 */
public fun connectedColumnEdges(index: Int, size: Int): ConnectedEdges {
    require(index in 0 until size)
    return ConnectedEdges.All
}

package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.theme.StylishTheme

/**
 * 磨りガラス フローティング要素の確認用プレビュー(公開 API は無し)。
 * ヘッダー(フローティング TopBar)と FAB をすりガラスで浮かせた実使用イメージ。
 */

@Composable
private fun FloatingBackdropVisual(modifier: Modifier = Modifier) {
    Box(
        modifier.background(
            Brush.linearGradient(
                listOf(Color(0xFFD7003A), Color(0xFF7A3FE0), Color(0xFF165E83)),
            ),
        ),
    ) {
        Text(
            "背景コンテンツ",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.align(Alignment.Center),
        )
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 24.dp)
                .height(40.dp)
                .background(Color(0xFFF2D9A0), RoundedCornerShape(50)),
        )
    }
}

@Preview(name = "Frosted floating bar / light", showBackground = true, widthDp = 400, heightDp = 300)
@Composable
private fun FrostedFloatingBarLightPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF12141E)),
        ) {
            FloatingBackdropVisual(Modifier.fillMaxSize())

            Column(Modifier.fillMaxSize()) {
                StylishHeader(
                    title = { Text("磨りガラス TopBar") },
                    navigation = { Icon(Icons.Default.Search, contentDescription = null) },
                    actions = { Icon(Icons.Default.Add, contentDescription = null) },
                    backdrop = { FloatingBackdropVisual(Modifier.matchParentSize()) },
                )
                Spacer(Modifier.weight(1f))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    StylishFab(
                        imageVector = Icons.Default.Add,
                        contentDescription = "作成",
                        onClick = {},
                        backdrop = { FloatingBackdropVisual(Modifier.matchParentSize()) },
                    )
                }
            }
        }
    }
}

@Preview(name = "Frosted floating bar / dark", showBackground = true, widthDp = 400, heightDp = 300)
@Composable
private fun FrostedFloatingBarDarkPreview() {
    StylishTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFF12141E)),
        ) {
            FloatingBackdropVisual(Modifier.fillMaxSize())

            Column(Modifier.fillMaxSize()) {
                StylishHeader(
                    title = { Text("磨りガラス TopBar") },
                    navigation = { Icon(Icons.Default.Search, contentDescription = null) },
                    actions = { Icon(Icons.Default.Add, contentDescription = null) },
                    backdrop = { FloatingBackdropVisual(Modifier.matchParentSize()) },
                )
                Spacer(Modifier.weight(1f))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    StylishFab(
                        imageVector = Icons.Default.Add,
                        contentDescription = "作成",
                        onClick = {},
                        backdrop = { FloatingBackdropVisual(Modifier.matchParentSize()) },
                    )
                }
            }
        }
    }
}

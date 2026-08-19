package com.segnities007.stylishui.catalog

/**
 * Categories for grouping demo components in the catalog gallery.
 *
 * Each category maps to a tab in the playground filter bar and lets
 * visitors narrow the grid to a specific component family.
 *
 * @property label Human-readable label shown on category tabs.
 */
public enum class DemoCategory(public val label: String) {
    Buttons("ボタン"),
    Selection("選択"),
    Inputs("入力"),
    Navigation("ナビゲーション"),
    Feedback("フィードバック"),
    Connected("Connected"),
    Charts("チャート"),
    WebParity("Web"),
    Patterns("パターン"),
}

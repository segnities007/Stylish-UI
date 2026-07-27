# Stylish UI

**Clear, Simple, Modern.**

Stylish UI は、この3つを体現した Compose Multiplatform デザインシステムです。
Android・JVM Desktop・Web（Wasm）をサポートしています。

## デザイン哲学

| 原則 | 意味 |
|------|------|
| **Clear** | 情報が明確であること。階層・状態・操作可否が見た目で伝わる |
| **Simple** | 要素が少なく、シンプルであること。装飾ではなく構造で魅せる |
| **Modern** | 最新のトレンドに合った、おしゃれなUIであること |

詳細は [DESIGN.md](DESIGN.md) のデザインチェックリストを参照してください。

## インストール

```kotlin
dependencies {
    implementation("io.github.segnities007:stylish-ui:<version>")
}
```

## 使い方

```kotlin
StylishTheme(darkTheme = isSystemInDarkTheme()) {
    // コンポーネントを使う
    StylishConnectedCardGrid(
        items = listOf(
            StylishConnectedCardItem("操作可能", onClick = { }),
            StylishConnectedCardItem("表示専用"),
        ),
        columns = 2,
    )
}
```

### カスタマイズ

デフォルトパラメータで Stylish UI の標準ルックが適用されます。
独自の UI を実現したい場合は、テーマまたはコンポーネントパラメータで上書きできます。

```kotlin
// グローバル上書き
StylishTheme(
    darkTheme = false,
    dimensions = StylishDimensions(
        connectedCornerRadius = 20.dp,
        connectedSpacing = 6.dp,
        outlineWidth = 1.dp,
    ),
) { ... }

// 個別上書き
StylishConnectedButtonRow(items = items, spacing = 8.dp)
```

## アーキテクチャ

コンポーネントは Atomic Design に従い、依存は一方向です。

```
patterns → organisms → molecules → atoms → foundation / theme / tokens
```

| 層 | パッケージ | 定義 |
|---|---|---|
| **atoms** | `components.atoms` | 単一のUI要素。Stylish コンポーネントを合成しない |
| **molecules** | `components.molecules` | atoms + M3 プリミティブの合成 |
| **organisms** | `components.organisms` | 複数の molecules の合成 |
| **patterns** | `components.patterns` | ページレベルのレイアウト |

### 公開コンポーネント

- **atoms** — `StylishFab`, `StylishIconButton`, `StylishRoundedIconButton`, `StylishSectionTitle`, `StylishConnectedCard`, `StylishDialogSurface`, `StylishFormTextField`
- **molecules** — Connected Button (Row/Column/Grid), Connected Card (Row/Column/Grid), Connected Chip (Row/Column/Grid), Connected ListItem (Row/Column/Grid), `StylishDatePickerField`, `StylishEmptyState`
- **organisms** — `StylishConnectedSegmentedControl`, `StylishDialogActions`, `StylishDeleteConfirmDialog`
- **patterns** — `StylishHeader`, `StylishScaffold`, `StylishPageContent`
- **charts** — `SimplePieChart` (common), `SimpleBarChart`, `SimpleLineChart` (Android)
- **theme** — `StylishTheme`, `StylishLightColorScheme`, `StylishDarkColorScheme`, `StylishTypography`
- **tokens** — `StylishDimensions`（テーマ経由でカスタマイズ可能）

## 開発

```bash
./gradlew jvmTest       # テスト
./gradlew assemble      # ビルド
./gradlew apiCheck      # ABI 互換性チェック
./gradlew apiDump       # ABI ダンプ更新（意図的な API 変更時）
```

## 公式サイト

`main` ブランチへの push で GitHub Pages に自動デプロイされます。

| URL | 内容 |
|-----|------|
| `/` | コンポーネントギャラリー（Compose Wasm） |
| `/api/` | API リファレンス（Dokka） |

ローカルでの確認:

```bash
./gradlew :website-wasm:wasmJsBrowserRun   # ギャラリー
./gradlew dokkaGeneratePublicationHtml      # API ドキュメント
```

## リリース

[Release Please](https://github.com/googleapis/release-please) で自動化されています。
詳細は [CONTRIBUTING.md](CONTRIBUTING.md) を参照してください。

## ライセンス

[Apache License 2.0](LICENSE)

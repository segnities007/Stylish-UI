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

### 3層アーキテクチャ（視覚的完成度）

Atomic Design（**合成**の複雑さ）とは独立に、すべてのコードは「見た目がどれだけ決まっているか」という第2の軸に乗ります。依存は一方向（`Finish → Structure → Foundation`）です。

```
Finish  →  Structure  →  Foundation
(仕上げ)    (骨格)        (素材)
```

| 層 | パッケージ | 定義 |
|---|---|---|
| **Foundation**（基礎） | `foundation/`, `tokens/`, `theme/` | 素材と規則。ジオメトリ計算・トークン・色/書体・判定ロジック。**UIを描画しない** |
| **Structure**（構造） | `structure/` | headlessコンポーネント。レイアウト・スロット・セマンティクスを持つが、**視覚スタイル（色/elevation/角丸/アニメーション）を持たない**。ジオメトリを計算し、描画をスロットに委譲 |
| **Finish**（仕上げ） | `components/` | Stylishの見た目をまとった完成品。Structure（またはatomsはFoundation直接）を消費 |

`Stylish` 接頭辞はスタイル済みFinish（`StylishConnectedCardRow`）、裸名はheadless Structure（`ConnectedCardRow`）を示します。Structureに自前のレンダラーを渡せば、同じ連結ジオメトリで独自スキンを組めます。

### 公開コンポーネント

- **atoms** — `StylishButton`（Filled/Tonal/Outlined/Text/Elevated の variant・ローディング対応）, `StylishIconButton`, `StylishRoundedIconButton`, `StylishFab`（Regular/Small/Large）, `StylishChip`（Assist/Filter/Input/Suggestion）, `StylishCard`（Filled/Elevated/Outlined）, `StylishConnectedCard`, `StylishSwitch`, `StylishCheckbox`, `StylishRadioButton`, `StylishSlider`, `StylishRangeSlider`, `StylishAvatar`, `StylishBadge`, `StylishSectionTitle`, `StylishHorizontalDivider`, `StylishVerticalDivider`, `StylishCircularProgressIndicator`, `StylishLinearProgressIndicator`, `StylishSpacer`, `StylishDialogSurface`, `StylishFormTextField`, `StylishDropdownMenu` / `StylishDropdownMenuItem`
- **molecules** — Connected Button (Row/Column/Grid), Connected Card (Row/Column/Grid + LazyColumn/LazyGrid), Connected Chip (Row/Column/Grid), Connected ListItem (Row/Column/Grid + LazyColumn/LazyGrid), `StylishListItem`, `StylishSection`, `StylishDatePickerField`, `StylishEmptyState`, `StylishSnackbar`, `StylishSnackbarHost`, `StylishSkeletonLine`, `StylishSkeletonAvatar`, `StylishSkeletonCard`
- **organisms** — `StylishConnectedSegmentedControl`, `StylishDialogActions`, `StylishDeleteConfirmDialog`, `StylishAlertDialog`, `StylishNavigationBar`, `StylishTabBar`, `StylishSearchBar`, `StylishBottomSheet`
- **patterns** — `StylishTopAppBar`, `StylishHeader`, `StylishScaffold`, `StylishPageContent`, `StylishFooter`
- **structure**（headless） — `ConnectedCard` / `ConnectedButton` / `ConnectedChip` / `ConnectedListItem` の Row/Column/Grid + LazyColumn/LazyGrid + 各 `Connected*ItemContent` スロット契約
- **charts** — `SimplePieChart`, `SimpleBarChart`, `SimpleLineChart`（すべて common で全プラットフォーム対応）
- **theme** — `StylishTheme`（`dynamicColor` / `shapes` 対応）, `StylishLightColorScheme`, `StylishDarkColorScheme`, `StylishTypography`
- **tokens** — `StylishDimensions`（Connectedジオメトリ + Rhythm間隔スケール + コンポーネントサイズ。テーマ経由でカスタマイズ可能）, `StylishAnimationTokens`（モーション）

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

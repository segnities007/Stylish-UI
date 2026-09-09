# Stylish UI

**Clear, Simple, Modern.**

Stylish UI は、この3つを体現した Compose Multiplatform デザインシステムです。
Android・JVM Desktop・Web（Wasm）をサポートし、iOS（`iosArm64` / `iosSimulatorArm64`）は
common compile対応です。iOS simulator testはmacOS CI jobとして構成していますが、実機A11yは未検証です。

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

Material3 をアプリから直接呼ばずに UI を構築する入口を提供します。
基本表示には `StylishText` / `StylishIcon` / `StylishImage` / `StylishSurface`、
配置には `StylishColumn` / `StylishRow` / `StylishBox`、通常の行には
`StylishListItem` を使えます。`StylishIcons` は一般的な操作アイコンを公開します。
`StylishTheme.colorScheme` と `StylishTheme.typography` から現在のテーマを参照できます。
`StylishTheme()` はシステムのライト／ダーク設定に従います。

ブランドは`StylishTheme`へ直接設定します。別のブランドオブジェクトやRegistryは不要で、
配下のコンポーネントは通常どおり使えます。

```kotlin
StylishTheme(
    lightColorScheme = ProductLightColors,
    darkColorScheme = ProductDarkColors,
    typography = ProductTypography,
    dimensions = ProductDimensions,
    shapes = ProductShapes,
) {
    StylishButton(onClick = { save() }) {
        StylishText("保存")
    }
}
```

デザインハーネスでは、任意描画を受け付けない `StylishScreen` を使います。
ID とラベルの検証、ヘッダー・本文の配置、読み込み／空／エラー表示と操作をライブラリが所有します。

```kotlin
StylishTheme {
    StylishScreen(
        title = "設定",
        state = StylishContentState.Content(
            StylishScreenDocument(listOf(
                StylishScreenElement.Input("name", "名前", name),
                StylishScreenElement.Toggle("sync", "同期", sync),
                StylishScreenElement.Action("save", "保存", enabled = name.isNotBlank()),
            )),
        ),
        onEvent = ::handleScreenEvent,
    )
}
```

状態更新・保存・画面遷移は `StylishScreenEvent` を受け取るアプリが担当します。
コンパイル対象の [設定画面サンプル](catalog/src/commonMain/kotlin/com/segnities007/stylishui/catalog/harness/SettingsScreen.kt)、
[利用規約と検証手順](docs/design-harness.md) を参照してください。

既存 API の色・状態型は `components.models.StylishSnackbarHostState` 等の
Stylish 名で import できます。これらは互換用 typealias であり、Material3 は引き続き内部依存です。
これは Material3 の全公開 API と引数をそのまま置換するバイナリ互換製品ではありません。

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

レイヤー面の色は `0.0f`（Page）から `1.0f`（Modal）までの連続値で取得できます。
補間はOklab色空間で行われるため、任意の段数でも知覚的な明るさが滑らかに変化します。

```kotlin
Surface(color = stylishLayerColor(level = 0.62f)) {
    // Raised と Overlay の中間にある Floating surface
}
```

標準の5段階には `StylishElevationLevel` を使用します。独自のレイヤー数が必要な場合だけ
`stylishLayerColor()`へ任意の値を渡してください。範囲外の値は `0.0f..1.0f` に丸められます。

### Edge-to-edge（標準）

Stylish UI は **Edge-to-edge を基本**とします。画面の背景とスクロールコンテンツは、ステータスバー・ナビゲーションバーの背後まで描画してください。Android アプリ側では Activity の起動時に `enableEdgeToEdge()`（または同等の Window 設定）を有効にします。

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StylishTheme {
                App()
            }
        }
    }
}
```

`StylishScaffold` などのコンテナが、必要な場所だけシステムバーInsetsを担当します。画面全体に `statusBarsPadding()` や `navigationBarsPadding()` を付けたり、Scaffoldの `innerPadding` を全画面コンテンツへ機械的に適用したりしないでください。Headerのタイトルや操作部品はステータスバーと重ならないようコンテナ内で保護し、Floating UIはナビゲーションバーの上に浮かせます。単独で使う `StylishHeader` など、Insetsを所有するコンテナの外側では、`windowInsets = WindowInsets.statusBars` のように必要なInsetsを明示してください。

### カスタマイズ

デフォルトパラメータで Stylish UI の標準ルックが適用されます。
独自の UI を実現したい場合は、テーマまたはコンポーネントパラメータで上書きできます。

```kotlin
// グローバル上書き
StylishTheme(
    darkTheme = false,
    shapes = StylishShapes(
        connectedCornerRadius = 20.dp,
    ),
    dimensions = StylishDimensions(
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

- **atoms** — `StylishButton`（Filled/Tonal/Outlined/Text/Elevated の variant・ローディング対応）, `StylishIconButton`, `StylishRoundedIconButton`, `StylishFloatingFab`（Regular/Small/Large）, `StylishFloatingVisibility`（Floating surface の fade + slide 共通モーション）, `StylishFloatingPagerIndicator`（Floating pill）, `StylishDotIndicator`（インライン）, `StylishChip`（Assist/Filter/Input/Suggestion）, `StylishCard`（Filled/Elevated/Outlined）, `StylishConnectedCard`, `StylishSwitch`, `StylishCheckbox`, `StylishTriStateCheckbox`, `StylishRadioButton`, `StylishSlider`, `StylishRangeSlider`, `StylishAvatar`, `StylishBadge`, `StylishBadgedBox`, `StylishRating`, `StylishNumberInput`, `StylishPinInput`, `StylishKbd`, `StylishSpeedDial`, `StylishSectionTitle`, `StylishHorizontalDivider`, `StylishVerticalDivider`, `StylishCircularProgressIndicator`, `StylishLinearProgressIndicator`, `StylishSpacer`, `StylishDialogSurface`, `StylishFormTextField`, `StylishFilledTextField`, `StylishSecureTextField`, `StylishDropdownMenu` / `StylishDropdownMenuItem`, `StylishExposedDropdownMenu`, `StylishDragHandle`, IconButton variants（Filled/FilledTonal/Outlined + Toggle）

Pager indicator は用途で使い分けます。`StylishDotIndicator` は通常レイアウトに参加する
背景なしのコンパクトなインライン表示、`StylishFloatingPagerIndicator` はコンテンツ上に重ねる
枠線付きFloating pill表示です。アプリごとにPagerIndicatorを再実装せず、表示位置だけを
各画面のコンテナで決めてください。
- **molecules** — Connected Button (Row/Column/Grid), Connected Card (Row/Column/Grid + LazyColumn/LazyGrid), Connected Chip (Row/Column/Grid), `StylishListItem`, `StylishSection`, `StylishToolbar`, `StylishButtonGroup`（Horizontal/Vertical・任意slot）, `StylishAccordion`, `StylishStepper`, `StylishBreadcrumb`, `StylishPagination`, `StylishEditable`, `StylishStatistic`, `StylishTimeline`, `StylishTable`, `StylishDatePickerField`, `StylishTimePicker`, `StylishDateRangePicker`, `StylishEmptyState`, `StylishSnackbar`, `StylishSnackbarHost`, `StylishSwipeToDismissBox`, `StylishPullToRefresh`, `StylishCarousel`, `StylishSkeletonLine`, `StylishSkeletonAvatar`, `StylishSkeletonCard`
- **organisms** — `StylishConnectedSegmentedControl`, `StylishSegmentedButton` 系列, `StylishDialogActions`, `StylishDeleteConfirmDialog`, `StylishAlertDialog`, `StylishPopover`, `StylishNavigationRail`, `StylishShortNavigationBar`, `StylishWideNavigationRail`, `StylishNavigationDrawer`（Modal/Dismissible/Permanent）, `StylishTabBar`, `StylishSearchBar`, `StylishBottomSheet`, `StylishDataTable`（列リサイズ、CSV/TSV/JSON exporter）, `StylishTree`, `StylishTransfer`, `StylishUpload`, `StylishColorPicker`, `StylishQrCode`, `StylishScrollArea`, `StylishContextMenu`, `StylishMenubar`
- **patterns** — `StylishTopAppBar`（標準M3）, `StylishFloatingTopBar`, `StylishFloatingBottomBar`, `StylishBottomSheetScaffold`, `StylishHeader`, `StylishScaffold`, `StylishPageContent`, `StylishFooter`
- **structure**（headless） — `ConnectedCard` / `ConnectedButton` / `ConnectedChip` の Row/Column/Grid + LazyColumn/LazyGrid + `DataTableLayout` + 各 slot 契約
- **charts** — `SimplePieChart`, `SimpleBarChart`, `SimpleLineChart`, `StylishMultiSeriesLineChart`（すべて common で全プラットフォーム対応）
- **adaptive** — `calculateStylishWindowSizeClass`, `StylishAdaptiveLayout`, `StylishAdaptiveNavigation`
- **localization** — `StylishStrings`, `StylishJapaneseStrings`、テーマ経由の文言差し替え
- **theme** — `StylishTheme`（固定ブランド配色 / `highContrast` / `shapes` 対応。端末Dynamic Color・シードカラー生成は使用しない）, `StylishLightColorScheme`, `StylishDarkColorScheme`, `StylishTypography`, `Typography.withFontFamily`, `StylishColorUtils`
- **tokens** — `StylishDimensions`（Connectedジオメトリ + Rhythm間隔スケール + コンポーネントサイズ + elevation ラダー。テーマ経由でカスタマイズ可能）, `StylishAnimationTokens`（モーション）, `StylishShapes`

アプリ共通の下部ナビゲーションには、`StylishFloatingBottomBar(items = ...)` を使います。
`StylishFloatingBottomBarItem` の `key`、`selectedKey`、`onItemClick` で状態と遷移を
接続すると、RoundedIconButton、80.dpの項目幅、`Role.Tab` semantics、フォーカス表示、
無効状態、Floating surface の余白とモーションが一箇所に揃います。特殊なアクション配置が
必要な場合だけ、既存の `actions` slot overload を使ってください。

## 開発

Floating UI の表示・非表示を自作する場合も、個別の `AnimatedVisibility` を
実装せず、方向だけを指定して共通 API を使います。

```kotlin
StylishFloatingVisibility(
    visible = isVisible,
    direction = StylishFloatingSlideDirection.Down,
) {
    StylishFab(
        imageVector = Icons.Default.Add,
        contentDescription = "追加",
        onClick = onAdd,
    )
}
```

この API は全高（または全幅）の slide と fade、共通 duration/easing、
Reduced Motion、Exit 完了までの Composition 保持を一括で提供します。

```bash
./gradlew jvmTest       # テスト
./gradlew wasmJsBrowserTest  # Wasm/Chrome shared-logic smoke (not the packaged UI workflow)
node scripts/wasm-ui-e2e.mjs  # packaged-site accessibility workflow (Chrome CDP)
./gradlew assemble      # ビルド
./gradlew apiCheck      # ABI 互換性チェック
./gradlew apiDump       # ABI ダンプ更新（意図的な API 変更時）

# Linuxで実行可能な全受入ゲート（JVM/Wasmブラウザ/API/構造）
./scripts/verify-linux-quality.sh
```

## 品質

Stylish UI の品質管理に関するドキュメントです。

| ドキュメント | 内容 |
|---|---|
| [DESIGN.md](DESIGN.md) | デザインチェックリスト（Clear / Simple / Modern） |
| [docs/catalog.md](docs/catalog.md) | 92 interactive demos、追加規約、Linux受入チェック |
| [ROADMAP.md](ROADMAP.md) | 公開ロードマップ（0.8.0 / 0.9.0 / 1.0.0） |

ゴールデンテスト（`src/jvmTest/.../visual/`）は、明暗テーマの描画を
ピクセル単位で検証します。初回実行でベースライン画像を記録し、以降は
比較して描画の回帰を検知します。ベースラインを更新するには対象の PNG を
削除して再実行してください。

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

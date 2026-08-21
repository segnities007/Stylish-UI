# Stylish UI 品質監査（Quality Audit）

本ドキュメントは Stylish UI の UI ライブラリとしての欠点を漏れなく列挙し、
修正の進捗を管理する作業ファイルです。各項目は ID・軸・重大度・現状の根拠・
対応方針・担当ワークストリーム・進捗で構成されます。

- 更新ルール: 実装完了時にステータスを `done` に更新する。実装中は `in-progress`。
- 重大度: **Critical**（品質として致命的）/ High / Medium / Low
- 監査日: 2026-08-20 / 対象バージョン: 0.10.0 / 監査方法: コード実測 + M3 jar 解析 + 競合調査 + Luna xhigh再監査

### 証跡の扱い

この表の `done` は、コードまたはCI設定が存在することを示す状態であり、今回の作業環境で実行成功を再確認したことを意味しません。特に、CIの実行ログ・スクリーンショットartifact・性能履歴がリポジトリにない項目は、実機／CIでの受入証跡としては `partial` または `pending` とします。`scripts/verify-quality-evidence.sh` はGradleを実行せず、これらの証跡ファイルと過大な合格宣言がないことだけをLinux上で機械検査します。

2026-08-21に、Android API 35 の新規 `StylishRuntime_API35` エミュレータ上で
`scripts/verify-android-runtime.sh` を実行し、`build/reports/android-runtime/` に
UIAutomator XML、スクリーンショット、端末fingerprint、manifestを生成した。これは
Compose consumerのインストール・起動・アクセシビリティツリー露出の実行証跡であるが、
TalkBack/Dynamic Type/OEM差分/SLOの証明ではない。

2026-08-20に、`docs/verification-log.md` に記録した単一ワーカーの統合実行で `check`
、`apiCheck`、`wasmJsBrowserTest`、JVM/Android host test が成功した。iOS Arm64 compile /
simulator testは別のmacOS CI jobとして構成されているが、今回のLinux実行には含まれない。
これはLinuxで再現できる内部ゲートの実行証跡であり、今回追加した
`scripts/wasm-ui-e2e.mjs` のローカル accessibility-tree workflowとは分離される。Hosted CIの
UI artifact、実機A11y、UI/DOM業務フローの全範囲、visual baseline、frame/memory SLO、Figma同期、
Native ABI、Hosted CI immutable SBOM、license承認の証明ではない。SBOMのローカル構造生成と
Android R8 consumer sampleは別途実行済み証跡として記録する。

今回追加した `src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt`
は、light/dark × high-contrast on/off × LTR/RTL × 100/200% font scale の32ケースを320×1200dpの固定面で描画し、
通常・loading・disabled・error・empty・長文状態の表示存在、画像サイズ、非空ピクセル量、
色バケット数を決定論的に検査する。`WRITE_VISUAL_MATRIX=1` を付けた実行では
`build/reports/visual-matrix/*.png` に比較用artifactを書き出せる。このターンではGradleを実行して
いないため、テストコードとCI artifact upload設定の存在は記録するが、実行成功やPNG artifactの実在・内容を主張しない。

## ワークストリーム割り当て

| WS | 名称 | 担当項目 |
|---|---|---|
| WS1 | 検証基盤 | QA-01〜QA-12 |
| WS2 | アクセシビリティ | A11Y-01〜A11Y-12 |
| WS3 | インタラクション/モーション | INT-01〜INT-11 |
| WS4 | 堅牢性/ロケール | ROB-01〜ROB-08 |
| WS5 | API 設計 | API-01〜API-10 |
| WS6 | テーマ/トークン | THE-01〜THE-10 |
| WS7 | 在庫（M3 パリティ） | INV-01〜INV-30 |
| WS8 | チャート | CHT-01〜CHT-07 |
| WS9 | パフォーマンス | PERF-01〜PERF-07 |
| WS10 | ガバナンス | GOV-01〜GOV-06 |
| WS11 | ドキュメント/DX | DOC-01〜DOC-07 |
| WS12 | プラットフォーム | PLT-01〜PLT-04 |
| — | 採用・統合 | ADO-01〜ADO-08 |
| — | エコシステム | ECO-01〜ECO-07 |
| — | 内部アーキテクチャ | ARC-01〜ARC-03 |

---

## 軸1: 在庫・コンポーネント（INV）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| INV-01 | `CenterAlignedTopAppBar` / `LargeTopAppBar` / `MediumTopAppBar` / `TwoRowsTopAppBar` / Flexible 系 | High | M3 1.9 jar 実測（AppBarKt） | WS7: M3 ラッパー追加 | done |
| INV-02 | `BottomAppBar` | Medium | 同上 | WS7 | done |
| INV-03 | `NavigationRail` + `ShortNavigationBar` + `WideNavigationRail` | High | jar 実測 + Wide wrapper実装 | WS7 | done |
| INV-04 | `NavigationDrawer`（Modal / Dismissible / Permanent） | High | jar 実測 | WS7 | done |
| INV-05 | M3 公式 `SegmentedButton` 系列 | Medium | jar 実測 | WS7 | done |
| INV-06 | `TimePicker` + `TimePickerDialog` | High | jar 実測 | WS7 | done |
| INV-07 | `DateRangePicker` / `DateInput` / `DateRangeInput` | Medium | jar 実測 | WS7 | partial |
| INV-29 | Tree / Transfer / Upload / ColorPicker / QRCode / ContextMenu / Menubar / ScrollArea | High | 共通primitive、keyboard操作、テーマlocale、`StylishTreeState`/`StylishTreeAction`、`StylishTransferState`/`StylishTransferAction`を追加。Treeはcontrolled `focusedId`を`FocusRequester`へ復元し、OS file pickerとQR encoderはadapter委譲。Tree/Transfer/Upload/menus/color/QR/scroll を責務別ファイルへ分割 | WS7 | partial（Transfer/ContextMenuの操作網羅、実機IME/スクリーンリーダーは継続） |
| INV-30 | DataTable exporter / 高度操作 | Medium | CSV/TSV/JSON exporter、公開headless `StylishDataTableState`/純粋reducer、列幅controlled state、freeze/列順変更/セル移動/query modelを実装 | WS7 | done |
| INV-08 | `ExposedDropdownMenu` | Medium | jar 実測 | WS7 | done |
| INV-09 | `SwipeToDismissBox` | Medium | jar 実測 | WS7 | done |
| INV-10 | `PullToRefresh` | High | jar 実測（pulltorefresh） | WS7 | done |
| INV-11 | `Carousel` | Medium | jar 実測（carousel） | WS7 | done |
| INV-12 | `TriStateCheckbox` | Medium | jar 実測 | WS7 | done |
| INV-13 | `BadgedBox`（バッジのアンカー配置） | Medium | jar 実測 | WS7 | done |
| INV-14 | `DragHandle`（単体公開） | Low | jar 実測 | WS7 | done |
| INV-15 | `FilledIconButton` / `FilledTonalIconButton` / `OutlinedIconButton` | Medium | jar 実測 | WS7 | done |
| INV-16 | `IconToggleButton` 4種 | Low | jar 実測 | WS7 | done |
| INV-17 | Filled `TextField` | High | jar 実測（FilledTextField） | WS7 | done |
| INV-18 | `SecureTextField`（パスワード欄） | Medium | jar 実測 | WS7 | done |
| INV-19 | `ToggleButton` 4種 | Medium | jar 実測 | WS7 | 対象外 (解決版M3 common に非存在) |
| INV-20 | `SplitButton` | Low | jar 実測 | WS7 | 対象外 (解決版M3 common に非存在) |
| INV-21 | `ButtonGroup` | Medium | Web/iOS共通のaction grouping | WS7 | done (`StylishButtonGroup`: Horizontal/Vertical + slot + border/shape/background) |
| INV-22 | `FloatingActionButtonMenu` | Low | jar 実測 | WS7 | 対象外 (解決版M3 common に非存在) |
| INV-23 | `FloatingToolbar` | Low | jar 実測 | WS7 | done (`StylishToolbar`: title/subtitle/navigation/actions + custom title slot) |
| INV-24 | `Menu`（M3 デスクトップメニュー） | Low | jar 実測 | WS7 | 対象外 (解決版M3 common に非存在) |
| INV-25 | `LoadingIndicator`（M3 1.9） | Low | jar 実測 | WS7 | 対象外 (解決版M3 common に非存在) |
| INV-26 | `WavyProgressIndicator`（M3 1.9） | Low | jar 実測 | WS7 | 対象外 (解決版M3 common に非存在) |
| INV-27 | `BottomSheetScaffold` | Medium | jar 実測 | WS7 | done |
| INV-28 | `MotionScheme` / `TonalPalette` / `DynamicTonalPalette` | Low | jar 実測 | WS6 で motion トークン拡張のみ採用 | partial |

## 軸2: アクセシビリティ（A11Y）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| A11Y-01 | アニメーションが reduced-motion 設定を無視 | **Critical** | 22 shared animation source filesを`verify-motion-contract.sh`で監査し、共通設定とvisibility/interactive animationに適用。FAB/Header/Footerはsnap遷移を明示 | WS2: 全アニメをシステム設定で無効化できる仕組み | partial（OS実機設定・TalkBack/VoiceOverの実証は継続） |
| A11Y-02 | `stateDescription` 未使用 | High | Pagination の選択ページに適用 | WS2: 選択・状態を持つコンポーネントに適用 | partial |
| A11Y-03 | `progressBarRangeInfo` なし（チャート） | Medium | Chartは進捗ではないため、構造化description/selection semanticsを採用 | WS8: チャートに進捗セマンティクス | 対象外（Chart semantics） |
| A11Y-04 | `liveRegion` なし（Snackbar 等） | Medium | Snackbar / Toast に polite live region を適用 | WS2 | done |
| A11Y-05 | フォーカス管理ゼロ（connected ファミリー） | **Critical** | ConnectedCardにfocusable/focus ring、他familyは要拡張 | WS2: フォーカスリング + focusable | partial |
| A11Y-06 | キーボードナビゲーションなし | High | 共通roving focus utility、DataTable row/resize、chart point、Tree、Menubar navigationを実装 | WS2: connected row/column に矢印キー | partial（connected全系統・Transfer/ContextMenuの専用テストは継続） |
| A11Y-07 | RTL 未検証・未対応 | High | PaginationのRTLテスト、Compose Row/Alignmentの自動ミラーを確認。Canvas軸・カスタム描画は追加監査が必要 | WS2: RTL テスト + 必要箇所 mirror | partial |
| A11Y-08 | フォントスケール 200% 破綻リスク | High | チャートCanvas文字をfontScale追従、ComposeのDynamic Type/FontScaleで主要UIを検証。固定高さの実機QAは継続 | WS2/WS4: 高さ上限付与・ラベル検証 | partial |
| A11Y-09 | WCAG コントラストの全role網羅 | Medium | WCAG ratio utility と `ContrastTest` でhigh-contrast light/darkのprimary/container/secondary/tertiary/surface/error AAペアを検証。全ブランドseed・実機表示は未検証 | WS2: seed/theme別パレットと実機コントラスト | partial |
| A11Y-10 | contentDescription ポリシー不統一 | Medium | StylishStringsによる主要文言のテーマ伝播、DataTable/Chartの状態説明を追加。Preview固定文言は残存 | WS2: KDoc でポリシー明文化 | partial |
| A11Y-11 | `minimumInteractiveComponentSize` 未活用 | Medium | `stylishInteractiveTarget`をButton/Chip/IconButton/ConnectedCard/ListItem/Treeへ適用 | WS3 | done |
| A11Y-12 | `testTag` ゼロ | Medium | commonMain 23ファイルで主要部品に付与（DataTable / multi-series chartを追加） | WS1: 主要コンポーネントに識別子 | partial |

## 軸3: インタラクション・モーション（INT）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| INT-01 | State layer（hover/press オーバーレイ）なし | **Critical** | M3 は全操作可能要素にあり | WS3: `stateLayerColor` ベースのオーバーレイ | done |
| INT-02 | 状態別 elevation ラダーなし | High | `stylishInteractiveElevation`がpress/hover/focus/default/disabledを共通tokenから解決し、Card/ListItem/ConnectedCard/ConnectedListItem/ConnectedChipへ適用 | WS3: トークン + 適用 | partial（全public surfaceと実機visual証跡は継続） |
| INT-03 | デスクトップ ホバー反応なし | High | `InteractionSource.collectIsHoveredAsState`を共通resolverで読み、hoveredElevation/state layerをcustom surfaceへ適用 | WS3 | partial（全component・Desktop実行artifactは継続） |
| INT-04 | ハプティクス不統一 | Medium | 7/16 コンポーネントのみ | WS3: 統一ヘルパー | pending (次期) |
| INT-05 | spring モーションの適用範囲が限定的 | Medium | `stylishPressScale` と Dot indicator は `spring` を使用し、`StylishAnimationTokens.springStiffness` で調整可能。展開・ページ遷移への統一適用は未検証 | WS3: 出現/拡張にも reduced-motion対応のspring契約を適用 | partial |
| INT-06 | `AnimatedContent` ゼロ | Low | 使用ゼロ | WS6: テーマ切替 | pending (次期) |
| INT-07 | モーション仕様が DESIGN.md に未定義 | High | 仕様書不在 | WS3: emphasized/standard/gentle 定義 | done |
| INT-08 | 選択アニメーション不揃い | Medium | NavBar は alpha のみ | WS3: 統一 | partial |
| INT-09 | FAB hide-on-scroll | Low | `StylishFab(visibilityState = ...)` と `VisibilityState.ScrollAware` / `NestedScrollAware` を実装し、reduced-motion対応のAnimatedVisibilityで表示を制御 | WS7: `hideOnScroll` 付きラッパー | done（実機スクロール回帰は未検証） |
| INT-10 | 押下スケール効果なし | Low | `Modifier.stylishPressScale` とテーマの `pressedScale` / `springStiffness` を追加 | WS3 | done |
| INT-11 | ジェスチャ駆動アニメが未設計 | Low | M3 任せ | WS3: 仕様化のみ | partial |

## 軸4: 堅牢性・ロケール（ROB）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ROB-01 | Dialog がウィンドウインセット未処理 | High | 実測 | WS4: `safeDrawing` padding | done |
| ROB-02 | insets 対応がコンポーネント毎に不統一 | Medium | Header/Footer のみ対応 | WS4: 監査表 + 統一 | partial |
| ROB-03 | チャート極値（負値・0・NaN）未検証 | Medium | テストなし | WS8: 数理テスト | done |
| ROB-04 | 超長文・多言語対応が場当たり | Medium | 場当たり | WS4: 共通 ellipsis 方針 | pending (次期) |
| ROB-05 | 空状態の標準化なし | Medium | 場当たり | WS4: EmptyState 自動適用ガイド | pending (次期) |
| ROB-06 | マルチウィンドウ/リサイズ未検証 | Low | customizable window breakpointsを追加、実機試験は継続 | WS4: テスト | partial |
| ROB-07 | 固定 dp 高さがスケールで破綻リスク | Medium | 52/77dp | WS4: `heightIn` 上限化 | pending (次期) |
| ROB-08 | 日付フォーマット `yyyy/MM/dd` ハードコード | High | 実測 | WS4: ロケール対応フォーマッタ | partial |

## 軸5: API 設計（API）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| API-01 | Fab 二重 size API | Medium | `sizeVariant` + `size: Dp?` | WS5: `size: Dp?` 廃止 | done |
| API-02 | `onSelected` / `onSelectedChange` 不統一 | Medium | 実測 | WS5: `onSelectedChange` に統一 | done |
| API-03 | Snackbar 同名2シグネチャ | Low | 実測 | WS5: ドキュメント整備（削除は破壊的） | pending (次期) |
| API-04 | deprecated molecules `StylishFormTextField` | Medium | 実測 | WS5: 削除 | done |
| API-05 | @OptIn 漏れ（SearchBar/TopAppBar/RangeSlider） | High | 実測 | WS5: KDoc 明記 + 利用ガイド | pending (次期) |
| API-06 | ConnectedCard Row/Column/Grid スタイル非転送 | High | 実測 | WS5: パラメータ転送 | done |
| API-07 | connected ファミリーに interactionSource なし | High | 実測 0 ファイル | WS5: 追加 | done |
| API-08 | 破壊的変更ポリシー未明文化 | Medium | 不在 | WS10: CONTRIBUTING に明記 | done |
| API-09 | パラメータ順序規約の再監査 | Low | 未監査 | WS5: スイープ | partial |
| API-10 | Defaults オブジェクト（`ButtonDefaults` 相当）なし | Medium | Button/Card/Chip/IconButton/TextFieldのDefaultsを追加 | WS5: 主要コンポーネントに `StylishXDefaults` | done |

## 軸6: テーマ・トークン・拡張性（THE）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| THE-01 | シードカラー Dynamic Color が Android 限定 | **Critical** | expect/actual | WS6: MaterialKolor で全プラットフォーム | done |
| THE-02 | カラー効用関数なし | Medium | 不在 | WS6: lighten/darken/harmonize | done |
| THE-03 | テーマ切替アニメーションなし | Low | `AnimatedContent` ゼロ | WS6 | pending |
| THE-04 | `letterSpacing` 未定義 | Medium | 実測 0 | WS6: タイポグラフィ補完 | done |
| THE-05 | コンポーネントカラー上書き不可 | High | `groupedContainer` のみ | WS6: `StylishComponentColors` 拡張 + CompositionLocal | done |
| THE-06 | elevation トークン2段のみ | Medium | 実測 | WS3: ラダー拡張 | done |
| THE-07 | ハイコントラストモード非対応 | Low | 不在 | ドキュメント化のみ（要判断） | done |
| THE-08 | M3 Expressive 非対応 | Low | 不在 | motion トークン拡張のみ（要判断） | done |
| THE-09 | ダークスキーム primary 反転 | Low | 設計判断 | 意図的として明記 | done |
| THE-10 | Stylish 独自 shapes トークンなし | Low | M3 既定 | WS6: `StylishShapes` 導入 | done |

## 軸7: パフォーマンス（PERF）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| PERF-01 | チャートが描画ループ内でテキスト再計測 | **Critical** | 3箇所実測 | WS8: measure キャッシュ | done |
| PERF-02 | `rememberTextMeasurer` キャッシュ戦略なし | Medium | 実測 | WS8 | done |
| PERF-03 | シャドウ多用の描画コスト未検証 | Low | 未検証 | WS9: 計測 | pending (次期) |
| PERF-04 | 再コンポジション検証なし | Medium | Compose compiler metricsを`verify-compose-metrics.py`でJSON化し、Strong Skipping・skippable比率・安定性閾値をCIへ接続。ただし実行時recomposition回数は未計測 | WS9: compiler metrics + platform benchmark | partial |
| PERF-05 | プラットフォーム性能回帰テストなし | Low | Linuxの決定論的algorithmic smokeとJSON artifactを追加。frame/memory/recompositionのSLOは未計測 | WS9: platform performance benchmark | partial |
| PERF-06 | Compose compiler metrics 未導入 | Medium | JVM-main metricsを81.5% skippable / 95.2% stable classes / 106 unknown argumentsとしてartifact化し、headless generic overloadsの110以内回帰予算とともにCI/releaseへ接続 | WS9 | done |
| PERF-07 | バイナリサイズのbaseline/diff履歴なし | Low | CIでJS 700KiB / Kotlin Wasm 10MiBを判定し、`wasm-bundle-evidence` のper-run artifactをアップロードする。長期baselineとの差分・全target履歴は未導入 | WS9: baseline/diffレポート | partial |

## 軸8: チャート（CHT）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| CHT-01 | 単一系列モデルのみ | Medium | `StylishMultiSeriesLineChart` の系列・legend・表示切替を実装 | WS8 | done |
| CHT-02 | 非有限値・欠損点の描画破綻 | Medium | 非有限値を線分から除外する共通処理 + 数理テスト | WS8 | done |
| CHT-03 | 軸目盛り（ticks）なし | Medium | y軸グリッドと `showAxisTicks` を実装 | WS8 | done |
| CHT-04 | 軸目盛りのlocale/業務単位変換なし | Medium | `xAxisTickFormatter` / `yAxisTickFormatter` を追加 | WS8 | done |
| CHT-05 | チャートのキーボード点移動なし | High | focusable Canvas + 矢印/Home/Endによる controlled selection | WS8 | done |
| CHT-06 | 選択点の詳細セマンティクスなし | High | 選択点を `stateDescription` と tooltip slotへ伝播し、`StylishChartState`/`StylishChartAction`のcontrolled rendererとJVM semantics smokeを追加 | WS8 | partial（実機スクリーンリーダー検証は継続） |
| CHT-07 | 高度な mark/area/scatter・色覚多様性パレットなし | Low | Okabe–Ito系パレットと色覚テスト、Area/Scatter chart を実装。mark/series switchingは継続 | WS8 | partial |

## 軸9: 検証・QA（QA）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| QA-01 | ゴールデンテスト/スクリーンショットの網羅的証跡なし | **Critical** | `GoldenTest` に96シーンのpixel matrix宣言を追加し、`VisualRegressionMatrixTest` に32ケース（high contrastを含む）の決定論的画像構造検査を追加。CI Build jobはPNGを `visual-matrix-evidence` artifactへ保存するが、ホストフォント依存のためpixel比較と実機artifactは未達 | WS1: pinned rendering imageでmatrix artifactをCI保存し、pixel baseline比較をrelease gate化 | partial |
| QA-02 | カタログが静的プレビューのみ | High | `:catalog` に 92 件の状態付き interactive preview、検索・カテゴリ・コードコピーを実装 | WS1: 描画スモークテスト化 | done（OS依存adapterはモック） |
| QA-03 | ダークテーマのCI視覚証跡なし | High | dark goldenはローカル比較用に存在するが、`GoldenTest` はCIでskip。CI artifact／実行ログなし | WS1: 安定した描画環境で明暗goldenをCI実行しartifactを保存 | partial |
| QA-04 | RTL/フォントスケール/密度テストなし | High | `AccessibilityLayoutSmokeTest` に加え、`VisualRegressionMatrixTest` が320dp・high contrast・RTL・100/200% font scaleと長文/状態行列を固定描画する。ただし130%、density行列、実機Dynamic Typeは未検証 | WS1: 130%/density matrixと実機Dynamic Type | partial |
| QA-05 | セマンティクス検証がごく一部 | High | JVMのAccessibility/Chart smokeはあるが、Wasm DOM、Webスクリーンリーダー、TalkBack/VoiceOver、focus順の実機証跡はない | WS2/WS1: a11y テスト | partial |
| QA-06 | チャート数理の単体テストなし | Medium | 実測 | WS8 | done |
| QA-07 | ~20 コンポーネント未テスト | High | 実測 | WS1: スモーク追加 | partial |
| QA-08 | パフォーマンスSLO・回帰計測なし | Low | DataTableの10,000行bounded-page/安定sort、Treeの2,000深度・100,000 sibling非再帰flatten、Area/Scatterの500点/frame・100,000点決定論的downsampleを実装し、Linux JVM algorithmic p95、JVM Compose recomposition harness、API 35 emulator startup/gfxinfo frame proxy、Wasm byte baseline/budgetをCI/report verifierへ接続。ただしiOS/Web/Desktop実機、memory、OEM、Macrobenchmark、Hosted長期履歴は未達 | WS9: platform performance benchmarkとHosted artifact retention | partial |
| QA-09 | ABI 検証が jvm のみ | Medium | iOS Arm64/Simulator compile + JVM API check、`klib dump-abi`によるarm64/simulator 1,769 declaration snapshot、Native ABI artifact uploadを追加。Hosted CI成功artifact、framework binary diff、全target baselineは未取得 | WS12: iOS 追加でカバー | partial |
| QA-10 | Android 実機テスト基盤なし | Medium | `androidHostTest`/compileに加え、API 35 emulator smokeがUIAutomator XML・PNG・fingerprint・manifestを生成し、CI jobにも接続した。ただしTalkBack・Dynamic Type・OEM・startup/frame/memory SLOのartifactはない | WS1: device A11y/SLO gate | partial |
| QA-11 | クロスプラットフォーム描画差未検証 | Medium | macOS CIにiOS simulator共通テストのjobを構成。現リポジトリには実行artifact・実機差分証跡なし | WS12: CI実行artifact + iOS描画スモーク | partial |
| QA-12 | Wasm browser UI accessibility workflowのHosted CI artifactが未取得 | Medium | `scripts/wasm-ui-e2e.mjs` がChrome accessibility treeでカタログ92件、カテゴリ14件、Card検索、テーマラベル、Tab操作を検証し、JSON/PNGを出力する。Hosted CI jobとartifact uploadは構成済みだが、成功artifactはまだ取得していない | WS12: Hosted CI成功artifactを取得し、viewport/fontを固定してUI_E2E_VERIFIEDへ昇格 | partial |

## 軸10: ドキュメント・DX・ツーリング（DOC）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| DOC-01 | ドキュメントサイトが簡素 | Medium | `docs/catalog.md` にカバレッジ表・追加規約・Linux受入チェックを追加。サイトの検索/API landing は継続 | WS11: カタログ強化 | partial |
| DOC-02 | DESIGN.md にモーション/インタラクション仕様なし | High | 実測 | WS3: 仕様追記 | done |
| DOC-03 | サンプルアプリ/チュートリアルなし | Medium | `:catalog` に DataTable / Tree / Transfer / Upload / QR / Menubar / multi-series の実行例と copy-ready code を追加 | WS11: 参照スクリーン例 | done（外部adapterはモック） |
| DOC-04 | Figma キットなし | Low | 実測 | 対象外（明記） | 対象外 (Figma キット) |
| DOC-05 | マイグレーションガイドなし | High | 実測 | WS11: M3→Stylish ガイド | done |
| DOC-06 | コンポーネント毎ドキュメントなし | Low | catalog の全デモに名前・カテゴリ・preview・code を付与。公開 API の詳細は KDoc/Dokka を継続 | KDoc 中心で対応 | partial |
| DOC-07 | CONTRIBUTING に仕様テンプレートなし | Medium | 実測 | WS11: テンプレート追加 | done |

## 軸11: 運用・ガバナンス（GOV）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| GOV-01 | SECURITY.md なし | High | 実測 | WS10 | done |
| GOV-02 | CODE_OF_CONDUCT なし | Medium | 実測 | WS10 | done |
| GOV-03 | ISSUE_TEMPLATE なし | Medium | .github 実測 | WS10 | done |
| GOV-04 | 破壊的変更ポリシー未明文化 | Medium | 実測 | WS10 | done |
| GOV-05 | ロードマップ非公開 | Low | 実測 | WS10: ROADMAP.md | done |
| GOV-06 | デザインレビュー手順未運用化 | Medium | 実測 | WS10: チェックリスト運用 | partial |

## 軸12: 採用・統合（ADO）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ADO-01 | M3→Stylish マイグレーションガイドなし | High | 不在 | WS11 | done |
| ADO-02 | M3 混在利用の公式スタンスなし | Medium | 不在 | WS11 | done |
| ADO-03 | サポート CMP バージョン範囲未明示 | Medium | README 記載なし | WS11 | done |
| ADO-04 | BOM / version catalog 提供なし | Low | 単一 artifact | 対象外（明記） | 対象外 |
| ADO-05 | `kotlinx-datetime` 必須依存 | Medium | build 実測 | 残す+ドキュメント明記（要判断） | done |
| ADO-06 | `compose.material`（M2）コア依存 | Medium | build 実測 | 使用箇所確認後、不要なら削除（要判断） | done |
| ADO-07 | Android ProGuard ルール未整備 | Low | consumer rulesと縮小/ABI方針を追加。`samples/android-r8` のminified/resource-shrink/mapping証跡と、API 35 runtime smokeのUI/accessibility artifactをLinuxで確認した。ただしpublished ABI・実機TalkBack・SLOは未確認 | WS10: R8/ProGuard + device gate | partial |
| ADO-08 | ABI 検証が jvm のみ | Medium | iOS compile gate、arm64/simulator KLib build、`klib dump-abi` snapshot生成を追加したが、Hosted artifact、framework diff、全target ABI baselineは未取得 | WS12 | partial |

## 軸13: エコシステム相互運用（ECO）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ECO-01 | 画像読み込み統合なし（Avatar） | Medium | initials のみ | WS7: `avatarImage` スロットは既存、AsyncImage 連携ガイド | partial |
| ECO-02 | ナビゲーション連携パターンなし | Low | 不在 | WS11: 利用ガイド | pending (次期) |
| ECO-03 | 状態管理（Flow）統合パターンなし | Low | 不在 | WS11 | pending (次期) |
| ECO-04 | アイコン戦略未定義 | Medium | 全量依存 | WS11: ポリシー明記 | done |
| ECO-05 | ロケール対応不足（複数形・通貨） | Medium | StylishStringsで主要A11y/Advanced文言をテーマ伝播、数値/通貨Providerは残存 | WS4: フォーマッタのみ対応 | partial |
| ECO-06 | アダプティブ/レスポンシブ非対応 | Medium | 純粋 size-class + custom breakpoints + adaptive slot API | WS12: window size 基盤 | done |
| ECO-07 | チャートが単一系列のみ | Medium | legend / selection / tooltip 付き複数系列モデル | WS8: 複数系列モデル | done |

## 軸14: 内部アーキテクチャ（ARC）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ARC-01 | 3層規約の逸脱（charts/DatePicker/patterns） | Medium | 実測 | 新規分のみ規約適用（要判断） | partial |
| ARC-02 | Structure 層カバレッジが connected のみ | Medium | 実測 | 新規コンポーネントは構造分離 | partial |
| ARC-03 | デッドコード検出プロセスなし | Low | 実測 | WS10: 運用ルール | pending (次期) |

## 軸15: プラットフォーム（PLT）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| PLT-01 | iOS ターゲットなし | **Critical** | build.gradle.kts 実測 | WS12: iosArm64/iosSimulatorArm64 + CI | done |
| PLT-02 | Dynamic Color が Android 12+ 限定 | High | 実測 | WS6: MaterialKolor | done |
| PLT-03 | デスクトップ固有機能なし | Low | 不在 | 対象外（明記） | 対象外 |
| PLT-04 | 対応プラットフォーム範囲の明示なし | Low | 不在 | WS11: README 明記 | done |

---

## 実装前に判断が必要な6点

1. `compose.material`(M2): 使用箇所確認の上、不要ならコア依存から削除
2. `kotlinx-datetime`: DatePickerField のため残す + ドキュメント明記
3. `materialIconsExtended`: 依存維持 + アイコンポリシー文書化
4. アダプティブ対応: window-size-class ユーティリティ + ガイドまで（フル adaptive 化は次期）
5. ハイコントラスト: トークン導入ではなくドキュメント化のみ
6. M3 Expressive: motion トークン拡張のみ採用

## 判断結果（実装時に記録）

| # | 判断 | 決定 |
|---|---|---|
| 1 | M2 依存 | **維持**: compose.material は materialIconsExtended の依存元のため必要（実測確認） |
| 2 | kotlinx-datetime | **維持** + ドキュメント明記（DatePickerField のため） |
| 3 | アイコン | **維持** + INTEROP.md にポリシー明記 |
| 4 | アダプティブ | 0.10.0で window-size-class + custom breakpoints + adaptive slot API を実装（複雑なナビゲーション統合は次期） |
| 5 | ハイコントラスト | **ドキュメント化のみ**（design-spec.md に方針記載） |
| 6 | Expressive | **motion トークンのみ採用**（emphasizedEasing / gentleEasing / durationEmphasized） |

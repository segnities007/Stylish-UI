# Stylish UI 品質監査（Quality Audit）

本ドキュメントは Stylish UI の UI ライブラリとしての欠点を漏れなく列挙し、
修正の進捗を管理する作業ファイルです。各項目は ID・軸・重大度・現状の根拠・
対応方針・担当ワークストリーム・進捗で構成されます。

- 更新ルール: 実装完了時にステータスを `done` に更新する。実装中は `in-progress`。
- 重大度: **Critical**（品質として致命的）/ High / Medium / Low
- 監査日: 2026-08-12 / 対象バージョン: 0.7.0 / 監査方法: コード実測 + M3 1.9 jar 解析 + 競合調査

## ワークストリーム割り当て

| WS | 名称 | 担当項目 |
|---|---|---|
| WS1 | 検証基盤 | QA-01〜QA-09 |
| WS2 | アクセシビリティ | A11Y-01〜A11Y-12 |
| WS3 | インタラクション/モーション | INT-01〜INT-11 |
| WS4 | 堅牢性/ロケール | ROB-01〜ROB-08 |
| WS5 | API 設計 | API-01〜API-10 |
| WS6 | テーマ/トークン | THE-01〜THE-10 |
| WS7 | 在庫（M3 パリティ） | INV-01〜INV-28 |
| WS8 | チャート | CHT-01〜CHT-07 |
| WS9 | パフォーマンス | PERF-01〜PERF-05 |
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
| INV-01 | `CenterAlignedTopAppBar` / `LargeTopAppBar` / `MediumTopAppBar` / `TwoRowsTopAppBar` / Flexible 系 | High | M3 1.9 jar 実測（AppBarKt） | WS7: M3 ラッパー追加 | pending |
| INV-02 | `BottomAppBar` | Medium | 同上 | WS7 | pending |
| INV-03 | `NavigationRail` + `ShortNavigationBar` + `WideNavigationRail` | High | jar 実測 | WS7 | pending |
| INV-04 | `NavigationDrawer`（Modal / Dismissible / Permanent） | High | jar 実測 | WS7 | pending |
| INV-05 | M3 公式 `SegmentedButton` 系列 | Medium | jar 実測 | WS7 | pending |
| INV-06 | `TimePicker` + `TimePickerDialog` | High | jar 実測 | WS7 | pending |
| INV-07 | `DateRangePicker` / `DateInput` / `DateRangeInput` | Medium | jar 実測 | WS7 | pending |
| INV-08 | `ExposedDropdownMenu` | Medium | jar 実測 | WS7 | pending |
| INV-09 | `SwipeToDismissBox` | Medium | jar 実測 | WS7 | pending |
| INV-10 | `PullToRefresh` | High | jar 実測（pulltorefresh） | WS7 | pending |
| INV-11 | `Carousel` | Medium | jar 実測（carousel） | WS7 | pending |
| INV-12 | `TriStateCheckbox` | Medium | jar 実測 | WS7 | pending |
| INV-13 | `BadgedBox`（バッジのアンカー配置） | Medium | jar 実測 | WS7 | pending |
| INV-14 | `DragHandle`（単体公開） | Low | jar 実測 | WS7 | pending |
| INV-15 | `FilledIconButton` / `FilledTonalIconButton` / `OutlinedIconButton` | Medium | jar 実測 | WS7 | pending |
| INV-16 | `IconToggleButton` 4種 | Low | jar 実測 | WS7 | pending |
| INV-17 | Filled `TextField` | High | jar 実測（FilledTextField） | WS7 | pending |
| INV-18 | `SecureTextField`（パスワード欄） | Medium | jar 実測 | WS7 | pending |
| INV-19 | `ToggleButton` 4種 | Medium | jar 実測 | WS7 | pending |
| INV-20 | `SplitButton` | Low | jar 実測 | WS7 | pending |
| INV-21 | `ButtonGroup` | Medium | jar 実測 | WS7 | pending |
| INV-22 | `FloatingActionButtonMenu` | Low | jar 実測 | WS7 | pending |
| INV-23 | `FloatingToolbar` | Low | jar 実測 | WS7 | pending |
| INV-24 | `Menu`（M3 デスクトップメニュー） | Low | jar 実測 | WS7 | pending |
| INV-25 | `LoadingIndicator`（M3 1.9） | Low | jar 実測 | WS7 | pending |
| INV-26 | `WavyProgressIndicator`（M3 1.9） | Low | jar 実測 | WS7 | pending |
| INV-27 | `BottomSheetScaffold` | Medium | jar 実測 | WS7 | pending |
| INV-28 | `MotionScheme` / `TonalPalette` / `DynamicTonalPalette` | Low | jar 実測 | WS6 で motion トークン拡張のみ採用 | pending |

## 軸2: アクセシビリティ（A11Y）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| A11Y-01 | アニメーションが reduced-motion 設定を無視 | **Critical** | `isSystemInReducedMotion` 使用ゼロ | WS2: 全アニメをシステム設定で無効化できる仕組み | pending |
| A11Y-02 | `stateDescription` 未使用 | High | 使用ゼロ | WS2: 選択・状態を持つコンポーネントに適用 | pending |
| A11Y-03 | `progressBarRangeInfo` なし（チャート） | Medium | 使用ゼロ | WS8: チャートに進捗セマンティクス | pending |
| A11Y-04 | `liveRegion` なし（Snackbar 等） | Medium | 使用ゼロ | WS2 | pending |
| A11Y-05 | フォーカス管理ゼロ（connected ファミリー） | **Critical** | focusable は DropdownMenu のみ | WS2: フォーカスリング + focusable | pending |
| A11Y-06 | キーボードナビゲーションなし | High | M3 は対応 | WS2: connected row/column に矢印キー | pending |
| A11Y-07 | RTL 未検証・未対応 | High | mirror 4ファイルのみ | WS2: RTL テスト + 必要箇所 mirror | pending |
| A11Y-08 | フォントスケール 200% 破綻リスク | High | 固定高さ・チャート sp 変換 | WS2/WS4: 高さ上限付与・ラベル検証 | pending |
| A11Y-09 | WCAG コントラスト未検証 | Medium | テストなし | WS2: パレットのコントラストテスト | pending |
| A11Y-10 | contentDescription ポリシー不統一 | Medium | null 既定と必須の混在 | WS2: KDoc でポリシー明文化 | pending |
| A11Y-11 | `minimumInteractiveComponentSize` 未活用 | Medium | 手動 min size のみ | WS3 | pending |
| A11Y-12 | `testTag` ゼロ | Medium | 使用ゼロ | WS1: 主要コンポーネントに識別子 | pending |

## 軸3: インタラクション・モーション（INT）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| INT-01 | State layer（hover/press オーバーレイ）なし | **Critical** | M3 は全操作可能要素にあり | WS3: `stateLayerColor` ベースのオーバーレイ | pending |
| INT-02 | 状態別 elevation ラダーなし | High | 平坦な 1dp のみ | WS3: トークン + 適用 | pending |
| INT-03 | デスクトップ ホバー反応なし | High | 同上 | WS3 | pending |
| INT-04 | ハプティクス不統一 | Medium | 7/16 コンポーネントのみ | WS3: 統一ヘルパー | pending |
| INT-05 | spring モーションゼロ | Medium | 使用ゼロ | WS3: 出現/拡張に spring | pending |
| INT-06 | `AnimatedContent` ゼロ | Low | 使用ゼロ | WS6: テーマ切替 | pending |
| INT-07 | モーション仕様が DESIGN.md に未定義 | High | 仕様書不在 | WS3: emphasized/standard/gentle 定義 | pending |
| INT-08 | 選択アニメーション不揃い | Medium | NavBar は alpha のみ | WS3: 統一 | pending |
| INT-09 | FAB hide-on-scroll なし | Low | 不在 | WS7: `hideOnScroll` 付きラッパー | pending |
| INT-10 | 押下スケール効果なし | Low | 不在 | WS3: オプション導入 | pending |
| INT-11 | ジェスチャ駆動アニメが未設計 | Low | M3 任せ | WS3: 仕様化のみ | pending |

## 軸4: 堅牢性・ロケール（ROB）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ROB-01 | Dialog がウィンドウインセット未処理 | High | 実測 | WS4: `safeDrawing` padding | pending |
| ROB-02 | insets 対応がコンポーネント毎に不統一 | Medium | Header/Footer のみ対応 | WS4: 監査表 + 統一 | pending |
| ROB-03 | チャート極値（負値・0・NaN）未検証 | Medium | テストなし | WS8: 数理テスト | pending |
| ROB-04 | 超長文・多言語対応が場当たり | Medium | 場当たり | WS4: 共通 ellipsis 方針 | pending |
| ROB-05 | 空状態の標準化なし | Medium | 場当たり | WS4: EmptyState 自動適用ガイド | pending |
| ROB-06 | マルチウィンドウ/リサイズ未検証 | Low | 未検証 | WS4: テスト | pending |
| ROB-07 | 固定 dp 高さがスケールで破綻リスク | Medium | 52/77dp | WS4: `heightIn` 上限化 | pending |
| ROB-08 | 日付フォーマット `yyyy/MM/dd` ハードコード | High | 実測 | WS4: ロケール対応フォーマッタ | pending |

## 軸5: API 設計（API）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| API-01 | Fab 二重 size API | Medium | `sizeVariant` + `size: Dp?` | WS5: `size: Dp?` 廃止 | pending |
| API-02 | `onSelected` / `onSelectedChange` 不統一 | Medium | 実測 | WS5: `onSelectedChange` に統一 | pending |
| API-03 | Snackbar 同名2シグネチャ | Low | 実測 | WS5: ドキュメント整備（削除は破壊的） | pending |
| API-04 | deprecated molecules `StylishFormTextField` | Medium | 実測 | WS5: 削除 | pending |
| API-05 | @OptIn 漏れ（SearchBar/TopAppBar/RangeSlider） | High | 実測 | WS5: KDoc 明記 + 利用ガイド | pending |
| API-06 | ConnectedCard Row/Column/Grid スタイル非転送 | High | 実測 | WS5: パラメータ転送 | pending |
| API-07 | connected ファミリーに interactionSource なし | High | 実測 0 ファイル | WS5: 追加 | pending |
| API-08 | 破壊的変更ポリシー未明文化 | Medium | 不在 | WS10: CONTRIBUTING に明記 | pending |
| API-09 | パラメータ順序規約の再監査 | Low | 未監査 | WS5: スイープ | pending |
| API-10 | Defaults オブジェクト（`ButtonDefaults` 相当）なし | Medium | 構造的差分 | WS5: 主要コンポーネントに `StylishXDefaults` | pending |

## 軸6: テーマ・トークン・拡張性（THE）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| THE-01 | シードカラー Dynamic Color が Android 限定 | **Critical** | expect/actual | WS6: MaterialKolor で全プラットフォーム | pending |
| THE-02 | カラー効用関数なし | Medium | 不在 | WS6: lighten/darken/harmonize | pending |
| THE-03 | テーマ切替アニメーションなし | Low | `AnimatedContent` ゼロ | WS6 | pending |
| THE-04 | `letterSpacing` 未定義 | Medium | 実測 0 | WS6: タイポグラフィ補完 | pending |
| THE-05 | コンポーネントカラー上書き不可 | High | `groupedContainer` のみ | WS6: `StylishComponentColors` 拡張 + CompositionLocal | pending |
| THE-06 | elevation トークン2段のみ | Medium | 実測 | WS3: ラダー拡張 | pending |
| THE-07 | ハイコントラストモード非対応 | Low | 不在 | ドキュメント化のみ（要判断） | pending |
| THE-08 | M3 Expressive 非対応 | Low | 不在 | motion トークン拡張のみ（要判断） | pending |
| THE-09 | ダークスキーム primary 反転 | Low | 設計判断 | 意図的として明記 | pending |
| THE-10 | Stylish 独自 shapes トークンなし | Low | M3 既定 | WS6: `StylishShapes` 導入 | pending |

## 軸7: パフォーマンス（PERF）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| PERF-01 | チャートが描画ループ内でテキスト再計測 | **Critical** | 3箇所実測 | WS8: measure キャッシュ | pending |
| PERF-02 | `rememberTextMeasurer` キャッシュ戦略なし | Medium | 実測 | WS8 | pending |
| PERF-03 | シャドウ多用の描画コスト未検証 | Low | 未検証 | WS9: 計測 | pending |
| PERF-04 | 再コンポジション検証なし | Medium | 不在 | WS9: compiler metrics 導入 | pending |
| PERF-05 | パフォーマンス回帰テストなし | Low | 不在 | WS9 | pending |
| PERF-06 | Compose compiler metrics 未導入 | Medium | 実測 | WS9 | pending |
| PERF-07 | バイナリサイズ追跡なし | Low | 実測 | WS9: レポート導入 | pending |

## 軸8: 検証・QA（QA）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| QA-01 | ゴールデンテスト/スクリーンショットテストゼロ | **Critical** | 実測 | WS1: `captureToImage` 基盤 | pending |
| QA-02 | カタログが静的プレビューのみ | High | 実測 | WS1: 描画スモークテスト化 | pending |
| QA-03 | ダークテーマの視覚検証なし | High | レンダーのみ | WS1: 明暗ゴールデン | pending |
| QA-04 | RTL/フォントスケール/密度テストなし | High | 実測 | WS1 | pending |
| QA-05 | セマンティクス検証がごく一部 | High | 実測 | WS2/WS1: a11y テスト | pending |
| QA-06 | チャート数理の単体テストなし | Medium | 実測 | WS8 | pending |
| QA-07 | ~20 コンポーネント未テスト | High | 実測 | WS1: スモーク追加 | pending |
| QA-08 | パフォーマンステストなし | Low | 実測 | WS9 | pending |
| QA-09 | ABI 検証が jvm のみ | Medium | api/ 実測 | WS12: iOS 追加でカバー | pending |
| QA-10 | Android 側テスト基盤なし | Medium | androidHostTest なし | WS1: 設定追加 | pending |
| QA-11 | クロスプラットフォーム描画差未検証 | Medium | 未検証 | WS12: CI で iOS 描画スモーク | pending |

## 軸9: ドキュメント・DX・ツーリング（DOC）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| DOC-01 | ドキュメントサイトが簡素 | Medium | 実測 | WS11: カタログ強化 | pending |
| DOC-02 | DESIGN.md にモーション/インタラクション仕様なし | High | 実測 | WS3: 仕様追記 | pending |
| DOC-03 | サンプルアプリ/チュートリアルなし | Medium | 実測 | WS11: 参照スクリーン例 | pending |
| DOC-04 | Figma キットなし | Low | 実測 | 対象外（明記） | pending |
| DOC-05 | マイグレーションガイドなし | High | 実測 | WS11: M3→Stylish ガイド | pending |
| DOC-06 | コンポーネント毎ドキュメントなし | Low | 実測 | KDoc 中心で対応 | pending |
| DOC-07 | CONTRIBUTING に仕様テンプレートなし | Medium | 実測 | WS11: テンプレート追加 | pending |

## 軸10: 運用・ガバナンス（GOV）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| GOV-01 | SECURITY.md なし | High | 実測 | WS10 | pending |
| GOV-02 | CODE_OF_CONDUCT なし | Medium | 実測 | WS10 | pending |
| GOV-03 | ISSUE_TEMPLATE なし | Medium | .github 実測 | WS10 | pending |
| GOV-04 | 破壊的変更ポリシー未明文化 | Medium | 実測 | WS10 | pending |
| GOV-05 | ロードマップ非公開 | Low | 実測 | WS10: ROADMAP.md | pending |
| GOV-06 | デザインレビュー手順未運用化 | Medium | 実測 | WS10: チェックリスト運用 | pending |

## 軸11: 採用・統合（ADO）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ADO-01 | M3→Stylish マイグレーションガイドなし | High | 不在 | WS11 | pending |
| ADO-02 | M3 混在利用の公式スタンスなし | Medium | 不在 | WS11 | pending |
| ADO-03 | サポート CMP バージョン範囲未明示 | Medium | README 記載なし | WS11 | pending |
| ADO-04 | BOM / version catalog 提供なし | Low | 単一 artifact | 対象外（明記） | pending |
| ADO-05 | `kotlinx-datetime` 必須依存 | Medium | build 実測 | 残す+ドキュメント明記（要判断） | pending |
| ADO-06 | `compose.material`（M2）コア依存 | Medium | build 実測 | 使用箇所確認後、不要なら削除（要判断） | pending |
| ADO-07 | Android ProGuard ルール未整備 | Low | 未確認 | WS10: 確認・明記 | pending |
| ADO-08 | ABI 検証が jvm のみ | Medium | api/ 実測 | WS12 | pending |

## 軸12: エコシステム相互運用（ECO）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ECO-01 | 画像読み込み統合なし（Avatar） | Medium | initials のみ | WS7: `avatarImage` スロットは既存、AsyncImage 連携ガイド | pending |
| ECO-02 | ナビゲーション連携パターンなし | Low | 不在 | WS11: 利用ガイド | pending |
| ECO-03 | 状態管理（Flow）統合パターンなし | Low | 不在 | WS11 | pending |
| ECO-04 | アイコン戦略未定義 | Medium | 全量依存 | WS11: ポリシー明記 | pending |
| ECO-05 | ロケール対応不足（複数形・通貨） | Medium | 実測 | WS4: フォーマッタのみ対応 | pending |
| ECO-06 | アダプティブ/レスポンシブ非対応 | Medium | 不在 | WS12: window size 基盤 | pending |
| ECO-07 | チャートが単一系列のみ | Medium | 実測 | WS8: 複数系列モデル | pending |

## 軸13: 内部アーキテクチャ（ARC）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| ARC-01 | 3層規約の逸脱（charts/DatePicker/patterns） | Medium | 実測 | 新規分のみ規約適用（要判断） | pending |
| ARC-02 | Structure 層カバレッジが connected のみ | Medium | 実測 | 新規コンポーネントは構造分離 | pending |
| ARC-03 | デッドコード検出プロセスなし | Low | 実測 | WS10: 運用ルール | pending |

## 軸14: プラットフォーム（PLT）

| ID | 項目 | 重大度 | 根拠 | 対応方針 | 進捗 |
|---|---|---|---|---|---|
| PLT-01 | iOS ターゲットなし | **Critical** | build.gradle.kts 実測 | WS12: iosArm64/iosSimulatorArm64 + CI | pending |
| PLT-02 | Dynamic Color が Android 12+ 限定 | High | 実測 | WS6: MaterialKolor | pending |
| PLT-03 | デスクトップ固有機能なし | Low | 不在 | 対象外（明記） | pending |
| PLT-04 | 対応プラットフォーム範囲の明示なし | Low | 不在 | WS11: README 明記 | pending |

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
| 1 | M2 依存 | 実装時に決定 |
| 2 | kotlinx-datetime | 実装時に決定 |
| 3 | アイコン | 実装時に決定 |
| 4 | アダプティブ | 実装時に決定 |
| 5 | ハイコントラスト | 実装時に決定 |
| 6 | Expressive | 実装時に決定 |

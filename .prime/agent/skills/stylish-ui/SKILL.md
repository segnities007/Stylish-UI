---
name: stylish-ui
description: Stylish-UI (KMP Compose design system, /home/segnities007/Projects/Stylish-UI) の開発・拡張・ブラッシュアップ手順。コンポーネント新規作成、カタログ デモ追加、ガラス(すりガラス)システム、検証コマンド(apiCheck/checkArchitecture/checkComposableSize)、リリース手順に対応。Stylish-UI や StylishUI という言葉が出たらこのスキルを使う。
---

# Stylish-UI 開発ガイド

Kotlin Multiplatform Compose デザインシステム (`io.github.segnities007:stylish-ui`)。
リポジトリ: `/home/segnities007/Projects/Stylish-UI`

## 検証コマンド(変更後は必ず実行)

```bash
cd /home/segnities007/Projects/Stylish-UI
./gradlew :compileKotlinJvm apiCheck :catalog:jvmApiCheck :checkArchitecture :checkComposableSize --console=plain 2>&1 | grep -E "^e:|BUILD|FAILED"
```

- public API を変更したら `./gradlew apiDump` → 再検証（`jvmApiCheck` 失敗の大半はこれ）
- コンパイラ生成 lambda 名の変化でも apiCheck は落ちる → apiDump で解消
- `checkArchitecture`: 層の依存方向 (patterns→organisms→molecules→atoms→foundation) 違反を検出
- `checkComposableSize`: 80行超の新規関数で失敗(ベースラチェット)。**意味単位で分割すること**（80行未満でも意味単位なら切る。詳細はリポジトリ AGENTS.md）
- 作業後: `./gradlew --stop` と `pkill -f KotlinCompileDaemon` でデーモン停止(合計約5.7GB食う)

## アーキテクチャ

- Atomic Design: atoms → molecules → organisms → patterns（依存は一方向のみ）
- `explicitApi()` 有効: 全 public 宣言に明示的修飾子が必要
- テーマ色は必ず `MaterialTheme.colorScheme` から。`Color.White/Black` 固定禁止（ダイナミック カラー破壊）
- 同一パッケージの別ファイルに private 同名 top-level 関数を作らない（解決エラーになる。ヘルパーは接頭辞等で一意化）

## コンポーネント新規作成

1. 配置レイヤを決定（atoms/molecules/organisms/patterns）
2. `Xxx.kt` を作成: KDoc（全 public パラメータ）+ `stylishTestTag("xxx_xxx")` + Preview（ライト/ダーク両方）
3. `checkComponentContracts` が KDoc/preview 契約を検査する
4. カタログ デモ追加: `catalog/.../DemoXxx.kt` に `DemoComponent(name=..., category=..., code="""...""", preview={...})`
   - `code` 文字列内の `$` は `${'$'}` にエスケープ（文字列テンプレート解釈される）
   - `DemoRegistry` に自動登録される。`apiDump` で lambda エントリ更新
5. Preview は AS layoutlib で見る。blur は layoutlib で再現されない場合あり → 検証は `:website:run` デスクトップ アプリ（`Window` に `rememberWindowState` 必須、無いと LazyList がクラッシュ）

## ガラス(すりガラス)システム

設計原則は liquid-glass-design スキル参照。実装構成:

- **`StylishFrostedGlassSurface(backdrop= | glassState=, tint, haze, blurRadius, borderColor)`** (atoms)
  - `backdrop` モード(静的): 背景を全面に描き、その複製を blur。背景が既知の場合
  - `glassState` モード(動的): `Modifier.stylishGlassSource(state)` で録画した画面を自分の位置で再生+ぼかす。スクロール追従。backdrop より優先
  - 内部レイヤーは全て `matchParentSize` → **サイズは modifier で確定必須**（高さ0バグの元）
- **`StylishGlassState` + `Modifier.stylishGlassSource`**: DrawModifierNode で毎フレーム GraphicsLayer に録画。録画後に効果ノードへ `invalidateDraw()`（描画フェーズ中のスナップショット書き込みは HWUI クラッシュの元、絶対しない）
- **致命的制約**: ガラス(効果側)をソースの録画範囲内に置かない。RenderNode 自己参照で SIGSEGV スタックオーバーフロー。ModernScreen は `glassState` を内部の LazyColumn(コンテンツのみ)に付ける設計
- **ブラー対応**: Android 12+ / desktop / iOS。未対応環境はティントのみへフォールバック（`isGlassBlurSupported()`）
- **ヘッダー等への適用パターン**: `StylishHeader(backdrop= | glassState=)`、`StylishFab`、`StylishBottomAppBar`、`StylishNavigationBar`、`StylishShortNavigationBar`、`StylishSearchBar(glass=)`、`StylishModernScreen(glassState=)`
- **質感レシピ**: ティントは colorScheme 由来、白濁(haze)+粒子600+単色ヘアライン。Brush グラデーション シーンはユーザーが削除済み(戻さない)
- 調整パラメータ: `haze`(乳白) / `blurRadius`(透過) / `tint`(着色)

## リリース

1. `version.properties` を更新（x-release-please 形式）
2. 検証 → コミット → push → `./gradlew publishToMavenCentral`
   - 失敗 "Invalid token" = `~/.gradle/gradle.properties` の Central Portal トークン期限切れ。ユーザーに再発行を依頼
3. StylishMemo は composite build (`includeBuild`) なのでローカル即反映。Maven 版に追従する場合は toml の `stylishUi` バージョンを更新

## 関連

- `liquid-glass-design` スキル: Apple Liquid Glass の設計原則
- リポジトリ `AGENTS.md`: 意味単位分解ルール(80行ラチェット含む)
- StylishMemo (`/home/segnities007/Projects/StylishMemo`): 主要消費者。`modify` ブランチで作業

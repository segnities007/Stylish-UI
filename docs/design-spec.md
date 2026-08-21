# Stylish UI モーション & インタラクション仕様書

本仕様書は Stylish UI の **モーション言語** と **インタラクションフィードバック規則** を定義する。
[DESIGN.md](DESIGN.md) の Modern（M2: 状態変化のアニメーション / M3: 触覚フィードバック）と
Simple（S2/S3: 最小の輪郭・標高）を、**トークンと機械的な規則** として具体化したものである
（監査項目 INT-07 の参照仕様）。

すべての継続時間・イージングは `tokens/StylishAnimationTokens.kt`、標高は
`tokens/StylishDimensions.kt`、角丸は `tokens/StylishShapes.kt` のトークンに対応する。
コンポーネントはハードコードせず、必ず `StylishTheme.animation` / `StylishTheme.dimensions` /
`StylishTheme.shapes` 経由でトークンを参照すること。

テーマ依存色の漏れは `scripts/verify-token-literals.sh` が監査する。Preview専用の例とQRの
黒白符号化だけを明示的に許可し、実コンポーネントは`MaterialTheme.colorScheme`または
Stylishのsemantic tokenを使う。allowlistの追加は、色がテーマ非依存である理由を同じPRの
設計文書へ記録する。

押下時の縮小表現は `Modifier.stylishPressScale` に統一し、`StylishAnimationTokens.pressedScale`
と `springStiffness` でアプリ全体から調整できます。減少モーション時は即時切替になります。

---

## 1. モーション言語

Stylish UI のモーションは **「短く、決定的で、穏やかに着地する」** ことを原則とする。
動きの種類は用途に応じて4段階の継続時間と3種のイージングに分類される。

### 1.1 継続時間（Duration）

| 名前 | 値 | 用途 |
|------|-----|------|
| `durationShort` | **180 ms** | 状態色の変化（hover / pressed / focus の色・ステートレイヤー）、アイコン切替、選択ハイライトなどのマイクロインタラクション |
| `durationMedium` | **300 ms** | 要素レベルの遷移（出現・消失・展開・収縮、ダイアログ・メニューの入場） |
| `durationLong` | **500 ms** | ページレベルの動き（ボトムシート、大きなサーフェスの入退場、画面遷移） |
| `durationEmphasized` | **350 ms** | 強調モーション — 特に注目させたい遷移（プライマリ動作の入場、目立たせたい状態変化） |

継続時間は **状態の切替が速く、サーフェスが大きいほど長く** なる。状態色の変化に
`durationMedium` 以上を使ってはならない（モタついて見える）。

### 1.2 イージング（Easing）

| 名前 | 値 | 用途 |
|------|-----|------|
| `emphasizedEasing` | **Emphasized** | 強調モーション。強い加速から滑らかに減速して着地し、勢いと注目を演出する |
| `defaultEasing` | **FastOutSlowIn** | 標準。すべてのアニメーションの既定。速く始まり穏やかに着地する |
| `gentleEasing` | **LinearOutSlowIn** | 穏やか。フェードイン/フェードアウト、装飾的なアンビエント動きなど、ゆっくり始めて静かに着地させる |

### 1.3 適用ガイドライン

| 動きの種類 | 継続時間 | イージング |
|-----------|---------|-----------|
| ステートレイヤー / 状態色の変化 | `durationShort` | `defaultEasing` |
| 要素の出現・消失・展開 | `durationMedium` | `defaultEasing` |
| ページレベルのサーフェス | `durationLong` | `defaultEasing` |
| 強調したい遷移 | `durationEmphasized` | `emphasizedEasing` |
| フェードイン / 装飾アニメーション | `durationMedium`（目立たせないときは `durationShort`） | `gentleEasing` |

## 2. インタラクションフィードバック規則

操作可能な（actionable な）コンポーネントは、**全状態で次の3点を満たす** こと：

1. **標高の変化** — 状態に応じた `StylishDimensions` の標高トークンを適用する
2. **ステートレイヤーオーバーレイ** — 表面に状態色のオーバーレイを重ねる（色トークンは
   スキーム由来の `onSurface` / `primary` 系を使い、アルファで強度を表す）
3. **触覚フィードバック**（モバイル）— タップ・長押し時にハプティクスを発火する

### 2.1 状態別の標高

| 状態 | 標高トークン | 既定値 | 備考 |
|------|-------------|--------|------|
| 静止（rest） | `interactiveElevation` | 1 dp | 操作可能であることを示す最小限の浮き |
| ホバー（hover） | `hoveredElevation` | 2 dp | ポインタデバイスのみ |
| フォーカス（focused） | `focusedElevation` | 1 dp | 標高は静止と同じ。**フォーカスリング（outline）が主な手掛かり** |
| 押下（pressed） | `pressedElevation` | 0 dp | 平らに沈めて押下を伝える |
| 無効（disabled） | `disabledElevation` | 0 dp | 浮きをなくし非操作を伝える |

### 2.2 状態別の表現

| 状態 | 標高 | ステートレイヤー | その他 |
|------|------|------------------|--------|
| rest | `interactiveElevation` | なし | — |
| hover | `hoveredElevation` | 弱いオーバーレイ（例: `onSurface` 8% 相当） | — |
| focused | `focusedElevation` | 弱いオーバーレイ | **outline リング**（輪郭）を追加。標高ではフォーカスを表さない |
| pressed | `pressedElevation` | 強いオーバーレイ（例: `onSurface` 12% 相当） | モバイルでハプティクス発火 |
| disabled | `disabledElevation` | なし | **標高なし + `surfaceVariant` 系の色**（容器・内容・輪郭すべてをトーンで沈める）。操作可能な状態と明確に区別する（C5） |

### 2.3 遷移規則

- 状態間の変化は `durationShort` + `defaultEasing` でアニメーションする（M2）。
- ホバー/フォーカスの変化は標高とステートレイヤーの両方を同時に動かす。
- 無効⇔有効の切替は状態色と標高を同時に遷移させ、唐突に切り替えない。
- 触覚は **press の開始時に** 発火する（release ではない）。

## 3. 減少モーション（Reduced Motion）ポリシー

**装飾的なアニメーションはすべて `isStylishReducedMotionEnabled` を尊重すること。**

- ユーザーが OS の減少モーション設定を有効にしている場合、**装飾的**（ambient）な
  アニメーションは無効化し、状態変化などの**機能的**（functional）なアニメーションは
  最低限（フェード or 即時切替）に縮退させる。
- 「装飾的」とは情報伝達に不要な動き（繰り返しのアニメーション、スケール演出、
  ページレベルの装飾的なサーフェスの移動）を指す。
- 「機能的」とは状態の変化・選択・エラー表示など、動きそのものが意味を持つものを指す。
- 減少モーション下でも、**状態の切り替えは非表示にならない**（フェード or 即時）こと。

## 4. トークン対応表

| 仕様上の名前 | トークン |
|-------------|---------|
| 短い継続時間 | `StylishAnimationTokens.durationShort` (180) |
| 中程度の継続時間 | `StylishAnimationTokens.durationMedium` (300) |
| 長い継続時間 | `StylishAnimationTokens.durationLong` (500) |
| 強調継続時間 | `StylishAnimationTokens.durationEmphasized` (350) |
| 標準イージング | `StylishAnimationTokens.defaultEasing` (FastOutSlowIn) |
| 強調イージング | `StylishAnimationTokens.emphasizedEasing` (Emphasized) |
| 穏やかイージング | `StylishAnimationTokens.gentleEasing` (LinearOutSlowIn) |
| 静止/操作可能標高 | `StylishDimensions.interactiveElevation` (1 dp) |
| ホバー標高 | `StylishDimensions.hoveredElevation` (2 dp) |
| フォーカス標高 | `StylishDimensions.focusedElevation` (1 dp) |
| 押下標高 | `StylishDimensions.pressedElevation` (0 dp) |
| 無効標高 | `StylishDimensions.disabledElevation` (0 dp) |
| フォーカスリング | `ColorScheme.outline` |
| 無効色 | `ColorScheme.surfaceVariant` / `onSurfaceVariant` |

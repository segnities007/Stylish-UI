# Stylish UI デザインチェックリスト

StylishUI のデザイン哲学は **Clear, Simple, Modern** です。
この3つを意識的に追求したUIを「Stylish」と定義します。

新しいコンポーネントの作成・レビュー時に、このチェックリストで判定してください。
全項目が Yes である必要はありませんが、**No の項目には意図的な理由が必要です。**

---

## 3層アーキテクチャと原則の対応

StylishUI は **Foundation（基礎）→ Structure（構造）→ Finish（仕上げ）** の3層で構成されます（詳細は [AGENTS.md](AGENTS.md)）。この分割は、デザインの原則を「気合」ではなく**構造によって強制する**ためのものです。各原則の責任は、特定の層に固定されます。

| 層 | 担う原則 | 仕組み |
|----|---------|--------|
| **Foundation**（素材） | Simple（S2/S3/S6/S7） | ヘアライン幅・最小elevation・角丸・間隔を**トークン**として保持。装飾ではなく値でSimpleを定義する |
| **Structure**（骨格） | Clear（C2/C5/C6/C7）+ Simple（S4） | セマンティクス（Role, disabled, selected）と連結ジオメトリ（S4）を、**見た目に関係なく**保証する。スタイルを剥いでもClearは壊れない |
| **Finish**（仕上げ） | Clear（C1/C3）+ Modern（M1/M2/M3） | 視覚的階層・エラー色・ダークテーマ・アニメーション・触覚で、原則を見た目として実現する |

この対応の帰結として：

- **Clear は Structure に宿る** — セマンティクスはスタイルの有無に依存しないため、Finishがどう塗っても C2/C5/C6/C7 は維持される。
- **Simple は Foundation に宿る** — 装飾の余地をトークンが狭めるため、S2/S3/S7 の逸脱はトークン上書きという明示的行為を要する。
- **Modern は Finish に宿る** — M1/M2/M3 の磨き込みはFinish層の責任であり、Structure/Foundationを汚さない。

新しいコンポーネントを作るときは、まず「この原則の責任はどの層か」を意識し、**層をまたいで責任を混ぜない**こと（例: Structureに色を書かない、Finishでジオメトリを再計算しない）。

---

## Clear — 情報が明確か

| # | 判定項目 | Yes/No |
|---|---------|--------|
| C1 | 見出し・本文・補足の階層がタイポグラフィのウェイト/サイズで区別できるか | |
| C2 | 操作可能な要素と読み取り専用の要素が、見た目だけで区別できるか（標高・輪郭・色） | |
| C3 | エラー状態が色（error）とテキストの両方で伝わるか | |
| C4 | ラベルまたはプレースホルダーが、入力すべき内容を指示しているか | |
| C5 | 無効状態（disabled）が有効状態と明確に区別できるか | |
| C6 | 選択状態・非選択状態が一目で分かるか | |
| C7 | スクリーンリーダーで意味が通るセマンティックロールが設定されているか | |

## Simple — 要素が少なく、シンプルか

| # | 判定項目 | Yes/No |
|---|---------|--------|
| S1 | 有彩色は意味を持つ箇所（エラー、チャート等）に限定され、装飾目的の色がないか | |
| S2 | 輪郭線はヘアライン（0.4dp 程度）で、主張しすぎていないか | |
| S3 | 標高は最小限か（操作可能: 1dp、浮遊: 2dp を目安） | |
| S4 | 関連する要素は連結・グループ化され、バラバラに浮遊していないか | |
| S5 | 装飾的なアイコン・イラスト・グラデーションがなくても成立するか | |
| S6 | 余白・間隔が StylishDimensions のトークンで統一されているか | |
| S7 | 角丸がトークンに沿っているか（連結外角: 12dp、接合部: 2dp、浮遊: 28dp） | |

## Modern — 今どきのおしゃれなUIか

| # | 判定項目 | Yes/No |
|---|---------|--------|
| M1 | ダークテーマで破綻なく表示されるか | |
| M2 | 状態変化にアニメーションがあるか（出現・消失・選択切替等） | |
| M3 | タップ・長押しに触覚フィードバックがあるか | |
| M4 | 現在のプラットフォームのUIトレンド（カード、チップ、セグメント等）に沿った形態か | |
| M5 | 角丸・余白・タイポグラフィのバランスが、現代的な印象を与えるか | |
| M6 | 古いUIパターン（ベベル、ドロップシャドウの多用、原色の多用）に陥っていないか | |

---

## 使い方

### 新しいコンポーネントを作るとき

1. 実装後にこのチェックリストを一通り確認する
2. No の項目について、意図的な理由があるか検討する
3. 理由なく No なら修正する

### レビューするとき

1. 変更されたコンポーネントに対してチェックする
2. 既存の Yes が No に変わっていないか（リグレッション）を確認する
3. 判定に迷ったら、同じ層の既存コンポーネントと比較する

### AI エージェントが判定するとき

AGENTS.md の規約とこのチェックリストの両方を参照すること。
フォルダ構造（Atomic Design）が「どこに置くか」を決め、
このチェックリストが「どう作るか」を決める。

---

## カスタマイズ

デフォルトパラメータは StylishUI の標準ルック（Clear, Simple, Modern）を実現する。
独自の UI を実現したい場合は、以下の2つの方法で上書きできる。

### グローバル上書き（テーマ経由）

```kotlin
StylishTheme(
    darkTheme = false,
    dimensions = StylishDimensions(
        connectedCornerRadius = 20.dp,  // 角丸を大きく
        connectedSpacing = 6.dp,        // 間隔を広く
        outlineWidth = 1.dp,            // 輪郭を太く
    ),
) { ... }
```

Android 12+ では `dynamicColor = true` で Material You（壁紙由来）の色を適用できます（他のプラットフォームでは無視され、静的スキームにフォールバック）。`seedColor` を指定すると **全プラットフォーム** で MaterialKolor によるシードカラー由来のダイナミックスキーム（トーナル M3 配色）が適用されます。`animation` パラメータでモーショントークン（`StylishAnimationTokens`）、`shapes` パラメータで角丸トークン（`StylishShapes`）、`componentColors` パラメータで派生コンポーネントカラー（`StylishComponentColors`）も上書き可能です。

### 個別上書き（コンポーネントパラメータ経由）

```kotlin
StylishConnectedButtonRow(
    items = items,
    spacing = 8.dp,  // このコンポーネントだけ間隔を変更
)
```

### 上書き可能なトークン一覧

#### `StylishDimensions`（空間・サイズ）

| トークン | デフォルト | 意味 |
|---------|-----------|------|
| `connectedSpacing` | 3.dp | 連結アイテムの間隔 |
| `outlineWidth` | 0.4.dp | 輪郭線の太さ |
| `interactiveElevation` | 1.dp | 操作可能要素の標高 |
| `focusedElevation` | 1.dp | フォーカス時の標高（フォーカスリングが主な手掛かり） |
| `hoveredElevation` | 2.dp | ホバー時の標高（ポインタデバイス） |
| `pressedElevation` | 0.dp | 押下時の標高（平らに沈める） |
| `disabledElevation` | 0.dp | 無効時の標高（フラット） |
| `floatingElevation` | 2.dp | 浮遊要素（FAB/ヘッダー）の標高 |
| `connectedCornerRadius` | 12.dp | 連結外角の丸み |
| `joinedCornerRadius` | 2.dp | 連結接合部の丸み |
| `floatingCornerRadius` | 28.dp | FAB/ヘッダーの丸み |
| `inlineSpacing` | 4.dp | インライン要素間の最小間隔 |
| `itemSpacing` | 8.dp | グループ内アイテム間の間隔 |
| `contentSpacing` | 16.dp | コンテンツブロック間の間隔 |
| `sectionSpacing` | 32.dp | セクション間の間隔 |
| `buttonMinHeight` | 52.dp | ボタンの最小高さ |
| `cardMinHeight` | 77.dp | カードの最小高さ |
| `iconButtonMinSize` | 48.dp | アイコンボタンのタッチターゲット |
| `roundedIconButtonMinWidth` | 80.dp | ラベル付きアイコンボタンの最小幅 |
| `fabSize` | 56.dp | 通常 FAB のサイズ |
| `fabSmallSize` | 40.dp | スモール FAB のサイズ |
| `fabLargeSize` | 96.dp | ラージ FAB のサイズ |
| `screenPadding` | 20.dp | 画面端の標準パディング |
| `controlPadding` | 16.dp | コントロール内の水平パディング |
| `controlVerticalPadding` | 12.dp | コントロール内の垂直パディング |
| `pieChartSize` | 160.dp | 円グラフの一辺 |
| `barChartHeight` | 180.dp | 棒グラフの高さ |
| `lineChartHeight` | 200.dp | 折れ線グラフの高さ |

#### `StylishAnimationTokens`（モーション）

| トークン | デフォルト | 意味 |
|---------|-----------|------|
| `durationShort` | 180 ms | マイクロインタラクション（状態色変化など） |
| `durationMedium` | 300 ms | 要素の出現・展開 |
| `durationLong` | 500 ms | ページレベルの動き |
| `durationEmphasized` | 350 ms | 強調モーション（注目させたい遷移） |
| `defaultEasing` | FastOutSlowIn | 全アニメーションの標準イージング |
| `emphasizedEasing` | Emphasized | 強調モーションのイージング（減速して着地） |
| `gentleEasing` | LinearOutSlowIn | フェードイン等の穏やかなイージング |

#### `StylishShapes`（角丸）

| トークン | デフォルト | 意味 |
|---------|-----------|------|
| `small` | 6.dp | チップ・バッジ等の小さい角丸 |
| `medium` | 12.dp | カード・連結アイテム等の標準角丸 |
| `large` | 20.dp | ダイアログ・シート等の大きい角丸 |
| `extraLarge` | 28.dp | カプセル・拡張FAB等の全丸角丸 |

モーションの使い分けとインタラクションフィードバックの詳細は [docs/design-spec.md](docs/design-spec.md)（モーション & インタラクション仕様書）を参照。

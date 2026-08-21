# 100点到達の実装計画

この計画の100点は「コンポーネント数」ではなく、デザイナーから利用チーム、CI、実機、リリース、サポートまで同じ契約で再現できる状態を指す。各フェーズは、実装・テスト・文書・受入条件を同時に完了させる。

## 0. 完了条件と採点

| 軸 | 重み | 100点の受入条件 |
|---|---:|---|
| Visual system | 15 | 単一token source、フォント/密度/shape/motion、全状態のstate matrix、Figmaとコードの一致 |
| Component/API design | 20 | Atomic DesignとFinish/Structure/Foundationの境界、Defaults/slots/stateの一貫性、破壊的変更ゼロの互換ポリシー |
| A11y/inclusive UX | 20 | WCAG 2.2 AA、RTL、200%文字、high contrast、TalkBack/VoiceOver/keyboardの実機シナリオ |
| Platform parity | 15 | Android/JVM/Wasm/iOSで同じ状態・意味・レイアウト契約、safe-area/Dynamic Type/adapterを実機検証 |
| Data/performance | 10 | 10k行、長大Tree、複数系列ChartのSLO・benchmark・virtualization・memory回帰 |
| QA/release | 10 | visual/semantic/API/ABI/E2E、coverage閾値、SBOM/license/R8、再現可能artifact |
| Docs/ecosystem | 10 | versioned API docs、migration、Navigation/Flow/image/file adapters、Figma handoff、SLA/採用事例 |

## 1. 直ちに固定する開発規約（実装済みの土台を含む）

- Atomic Design: `atoms → molecules → organisms → patterns`。上位層から下位層への一方向依存のみ。
- Visual completeness: `Finish → Structure → Foundation`。Structureはheadless、Foundationは描画なし、Finishが見た目を決める。
- 1ファイル1公開Composable、公開APIはKDoc・Preview・testTag・Defaultsを持つ。
- 全actionable componentは `MutableInteractionSource` をhoist可能にし、48dp target、focus ring、state layer、press motionを共通Policyから適用する。
- データ状態は `StylishContentState`（Loading/Empty/Error/Content）に統一し、nullable + 複数Booleanの組み合わせを新規APIで禁止する。
- 状態・レイアウト・描画を分離する。特にDataTable/Tree/Transfer/Chartは `State`、`Layout`、`Renderer`、`Adapter` を別APIにする。
- 固定文言をComposable内に書かず、`StylishStrings`またはslot/formatterで注入する。
- 直書きdp/色/shape/elevationは禁止し、tokenまたは公開Defaultsを使う。
- `scripts/verify-architecture.sh` を `check` に接続し、依存方向の違反をCIで阻止する。

## 2. 実装フェーズ

### Phase A — 契約とモジュール境界

1. Foundationのinteraction/semantics/motion/geometryを共通化する。
2. `StylishInteractionPolicy`、`StylishContentState` を全新規コンポーネントの必須契約にする。
3. 依存方向、公開API KDoc、Preview、testTag、直書きtokenの静的チェックを追加する。
4. 現在の単一ライブラリを段階的に `foundation`、`structure`、`components`、`adapters`、`catalog` のGradleモジュールへ抽出する。最初はsource setを移し、API dumpを各モジュールで固定する。

### Phase B — APIとデザインパターン

1. DataTableを `StylishDataTableState` / `StylishDataTableLayout` / renderer / query-export adapterへ分割する。
2. Treeを平坦化・virtualized stateとrendererに分割し、矢印キー、Home/End、collection semanticsを提供する。
3. Transfer/Menubar/ContextMenuをroving-focusとmulti-selectの共通primitive上に移す。
4. Chartをscale/ticks/series/interaction/rendererに分割し、line/bar/area/scatterを同じモデルで描画する。
5. すべての公開Composableでparameter order、Defaults、slot、enabled/selected、onChange命名を統一する。

### Phase C — デザイン品質

1. コード・Figma・JSONを同一token sourceから生成する。
2. フォント、fallback、display/headline/body/label、line-height、letter-spacing、densityをプラットフォーム間で固定する。
3. component anatomy/state matrix（default/hover/focus/pressed/selected/disabled/loading/error/empty）を全公開部品で作る。
4. 320/600/840幅、RTL、長文、日本語/英語、font scale 100/130/200%、high contrastをカタログで可視化する。

### Phase D — A11yと実機

1. JVM semantics smokeに加えてAndroid Compose UI test、TalkBack、iOS VoiceOver/Dynamic Type、Wasm keyboard/screen-readerをシナリオ化する。
2. 最小ターゲット、focus order、focus restoration、stateDescription、liveRegion、Escape、reduced motionを全主要部品で検証する。
3. WCAGコントラストを通常、disabled、alpha合成、chart隣接色、dark/high-contrastで測定する。

### Phase E — 性能と信頼性

1. 10k行DataTable、100kノードTree、複数系列ChartをJVM/Web/iOSでbenchmarkする。
2. 行/列virtualization、sticky columnのz-order/clip/RTL、recomposition、memory、startupを測る。
3. Wasm/JS/Native/Android artifact size、R8/ProGuard、dependency lock、SBOM、licenseをCI gateにする。

### Phase F — リリースとエコシステム

1. clean checkoutから署名artifact、API/ABI dump、versioned Dokka、CHANGELOG、migrationを再生成する。
2. Navigation/Flow/image/file-picker/QRのadapter例と、M3混在・SwiftUI host・Web hostのサンプルを公開する。
3. issue triage、security response、deprecation、support window、SLA、採用事例を文書化する。

## 3. 実行順序

`architecture guard → shared policy → state/layout/renderer分離 → token/Figma → visual/a11y matrix → performance benchmark → native/browser E2E → release/support` の順に進める。後段の数値を前倒しで「完了」と判定しない。

## 4. 現在の実行結果

- `StylishInteractionPolicy` と `StylishContentState` をFoundation/Modelsに追加。
- `StylishIconButton`、`StylishFab`、`DefaultStylishConnectedChip` を共通interaction policyへ移行。
- `scripts/verify-architecture.sh` を追加し、Gradle `checkArchitecture` と `check`へ接続。
- 残りは、実機・Figma・性能計測・モジュール抽出など、Linux単独では完了を証明できない受入ゲートである。

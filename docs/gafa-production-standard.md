# Stylish UI Production Standard

これは「CIを通すためのチェックリスト」ではなく、Stylish UIを大規模プロダクトの標準UI基盤として採用できる状態にするための最上位仕様である。CI、ゴールデン、ブラウザテストは、この仕様を証明する手段として扱う。

## 最上位の成功条件

Stylish UIは、Web・Android・iOS・Desktopで同じ設計原則を保ち、デザイナーが意図した視覚・操作・アクセシビリティを、開発者が予測可能なAPIで再現できなければならない。利用チームは、コンポーネントの見た目だけでなく、状態・エラー・データ量・入力方式・画面サイズ・言語を含む実運用を安全に構築できる必要がある。

## 評価モデル（100点）

| 大分類 | 配点 | 100点の定義 |
|---|---:|---|
| 製品・デザイン原則 | 15 | anatomy、状態、密度、レスポンシブ、ブランド、Do/Don'tが全コンポーネントで一貫 |
| トークン・テーマ | 10 | colors/type/shape/space/elevation/motion/densityが単一源で、light/dark/high-contrast/RTLに対応 |
| API・アーキテクチャ | 20 | headless state・layout・renderer・featureが分離され、Defaults/slot/controlled state/ABI/semverが安定 |
| コンポーネント実運用 | 15 | loading/empty/error/disabled/long text/large data/keyboardを含む状態が揃う |
| A11y・Inclusive design | 15 | WCAG AA、最小ターゲット、focus、keyboard、screen reader semantics、RTL、200% scaleを実証 |
| 性能・信頼性 | 10 | 10k rows、長大Tree、複数系列ChartのSLO、再コンポジション、メモリ、bundle budgetを管理 |
| DX・エコシステム | 10 | カタログ、KDoc/API docs、サンプル、移行、adapter、Figma/JSON token連携、issue運用 |
| リリース・ガバナンス | 5 | clean checkout、ABI、SBOM/license、R8/ProGuard、変更管理、サポート方針を再現可能 |

## マクロからミクロへの分解

### 1. 製品・デザイン原則

- 主要ペルソナ（プロダクトデザイナー、アプリ開発者、業務UI開発者）ごとの成功指標を定義する。
- 全コンポーネントに anatomy、variant、state、content rules、responsive rules、Do/Don'tを定義する。
- ブランドフォント、フォールバック、字幅、数値・日付・通貨表記を単一仕様にする。
- 320/600/840dp、RTL、100/130/200% font scale、long textを設計ケースに含める。

### 2. トークン・テーマ

- JSON/コード/Figmaへ同期できるtoken schemaを定義する。Stylish UIの交換形式は
  [`docs/tokens/stylish-ui.tokens.json`](tokens/stylish-ui.tokens.json)、ランタイム実装は
  `src/commonMain/.../tokens` とする。
- color、typography、shape、spacing、size、elevation、motion、densityをsemantic tokenとして公開する。
- component tokenはsemantic tokenからのみ参照し、直書きdp・色・文字列を禁止する。
- reduced motion、high contrast、dark/light、localeをCompositionLocal/adapterで統一する。

### 3. API・アーキテクチャ

- `model → state → layout → renderer → component` の依存方向を固定する。
- DataTable、Tree、Chart、Navigationをheadless engineとUI rendererに分離する。
- public APIはDefaults、slot、controlled/uncontrolled、stable key、event semanticsを持つ。
- Atomic Design依存を機械検査し、public composableの責務・KDoc・Preview・testTagを監査する。
- 破壊的変更、deprecation、migration、ABIをversioned policyで管理する。

### 4. コンポーネント実運用

- 各コンポーネントを default/loading/empty/error/disabled/selected/focused/pressed/hovered/long text/RTLで確認する。
- DataTableはsorting/filtering/paging/selection/resize/pin/freeze/export/keyboard/cell navigationを分離して検証する。
- Tree/Transfer/Upload/Menuは大規模データ、複数選択、focus復帰、Escape、IMEを検証する。
- Chartはfinite/NaN/negative/zero/missing/large series/ticks/selection/legend/色覚多様性を扱う。

### 5. A11y・Inclusive design

- semantics treeをrole、stateDescription、collection info、live regionまで設計する。
- 48dp以上の操作領域、visible focus、logical keyboard order、roving focus、Escapeを統一する。
- WCAG AAコントラストをnormal/large text別に測定する。
- LinuxではDesktop/Wasmのキーボード・スクリーンリーダー契約を自動化し、Android/iOSでは実機ゲートへ接続する。

### 6. 性能・信頼性

- 10k rows、1k Tree nodes、複数系列Chartで初回表示、更新、scroll、memory、recomposition SLOを定義する。
- virtualization、stable key、derived state、text measurement cache、Canvas path cacheを検証する。
- JVM/Wasm/Android/Native artifactのサイズ予算と回帰を記録する。

### 7. DX・エコシステム

- カタログは全public APIを検索、カテゴリ、状態、コピー可能コード、Previewで示す。
- KDoc、API docs、M3 migration、adapter（Navigation/Flow/file/image）、FAQを揃える。
- Designer handoff用にtoken schema、Figma mapping、component anatomy、state matrixを公開する。
  参照テンプレートは [`docs/component-state-matrix.md`](component-state-matrix.md) とする。

### 8. リリース・ガバナンス

- clean checkoutから同じartifactを再生成できる。
- API dump/check、Native compile、browser test、visual/a11y/performance budget、SBOM/licenseをrelease gateにする。
- issue、security、breaking change、support SLA、deprecation policyを公開する。

## 完了判定

各項目は「実装した」ではなく、(1)仕様、(2)コード、(3)自動テストまたは実機証跡、(4)ドキュメント、(5)回帰防止ゲートの5点が揃ったときだけ完了とする。Linuxで実行できない項目は未検証として明示し、100点に含めてはいけない。

## 採用判定の運用

採点は「機能の存在」ではなく、利用チームがリスクを判断できる証跡の有無で行う。各項目の証跡は、実装ファイル、テストまたは実機シナリオ、カタログ例、API/KDoc、リリース時の回帰ゲートを相互リンクする。ブラウザのビルド成功、コンポーネント件数、CIジョブ定義だけでは、視覚・操作・性能・アクセシビリティの合格証跡にならない。未検証のプラットフォームは点数を与えず、採用報告書に残存リスクとして表示する。

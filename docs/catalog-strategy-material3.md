# Stylish UI カタログ戦略（Material 3 比較）

調査日: 2026-09-10
対象: `catalog` モジュールと `src/commonMain` の公開 UI
目的: Material 3 の 1:1 コピーを増やさず、Stylish UI を選ぶ理由がカタログだけで分かる構成にする。

## 結論

Stylish UI のカタログは、Atomic Design の層（atom / molecule / organism）をそのままタブにするのではなく、次の二つのレーンを主軸にする。

1. **M3 parity（互換性）** — Material 3 と同じ利用目的の UI を、意味的なファミリー単位で一つにまとめる。Filled / Tonal / Outlined のような見た目のバリエーションは同じカード内の variant 切り替えにする。
2. **Stylish value（独自価値）** — M3 の単体部品にはない、レイヤー連動、接続形状、浮遊・モーダルの組み合わせ、適応レイアウト、データ表示などを、実際の画面シナリオとして見せる。

「同じ目的・同じ相互作用・同じコード契約」のエントリは一つにする。単に実装クラスが違う、色が違う、または子部品が一つ増えただけのものは、新しいカタログカードにしない。Drag handle のように単体で利用目的を持たない部品は、BottomSheet などの親シナリオ内でだけ表示する。

## 一次資料から確認できる Material 3 の前提

### カタログの分類は「実装層」ではなく「ユーザーの仕事」

公式の [Material components in Compose](https://developer.android.com/develop/ui/compose/components) は、UI を Actions、Communication、Containment、Navigation、Selection、Text inputs に分類している。例えば Buttons / FAB は Actions、Cards / Bottom sheets / Dialogs / Lists / Scaffold は Containment、Navigation bar / drawer / rail / Tabs は Navigation として掲載される。同じ画面で一緒に使うものを同じ目的群で探せる分類であり、Compose のパッケージ構造を反映した分類ではない。

したがって Stylish UI も、最初の入口は次の目的分類に合わせる。`atoms` や `molecules` は二次フィルターまたは詳細情報として保持する。

### M3 は一つの部品に複数の意味的バリエーションを持たせる

公式の [Button](https://developer.android.com/develop/ui/compose/components/button) は Filled、Filled tonal、Elevated、Outlined、Text の五種類を、一つの Button ファミリーの用途・強調度として説明している。M3 API も各 variant を個別の composable として提供するが、ユーザーが選ぶのは「どの強調度のアクションか」である。

このため、カタログでは `Button` を五枚のカードに分けず、次のような一枚の比較面にする。

```text
Button family
  [Filled] [Tonal] [Elevated] [Outlined] [Text]
  用途 / 強調度 / disabled / loading / code
```

同じ扱いを Cards、Icon buttons、FAB、Chips、Text fields、Top app bars、Navigation に適用する。

### M3 の価値はテーマ・役割・適応性にもある

M3 の [Compose API reference](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) は、`ColorScheme`、`Typography`、`Shapes` と多数のコンポーネント variant を同じテーマ体系の下で提供する。[Material 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3) は、コンポーネントが色・elevation をカスタマイズでき、tonal elevation でコンテナを区別すること、役割に合った `on-*` 色でコントラストを確保することを説明している。

従って「色を変えただけ」の Stylish カードは独自価値とは見なさない。Stylish の色レイヤー、役割解決、深さの正規化など、M3 の既存 API にはない契約を見せる必要がある。

### 合成・Insets・状態を一緒に見せる

Compose 公式の [slot-based layouts](https://developer.android.com/develop/ui/compose/layouts/basics) は、`Scaffold` や app bar のような部品を slot で組み合わせる設計を推奨している。[Scaffold](https://developer.android.com/develop/ui/compose/components/scaffold) は topBar、bottomBar、FAB を一つの画面構造として受け取り、content に `PaddingValues` を渡す。

また [Material 3 insets](https://developer.android.com/develop/ui/compose/system/material-insets) では、Top/Bottom app bar、NavigationBar、ModalBottomSheet などが配置に応じて inset を扱う一方、`Scaffold` 自体は content に inset を適用せず、呼び出し側が `PaddingValues` を消費する。従って BottomSheet や floating bar を単体の静止画で出すだけでは不十分で、親 Scaffold、スクロール、Insets、開閉状態を同じ preview で検証できるようにする。

### 適応レイアウトは独立した価値軸

公式の [adaptive apps](https://developer.android.com/develop/ui/compose/layouts/adaptive/get-started-with-adaptive-apps) は、compact / medium / expanded の window size class に応じて、NavigationBar と NavigationRail を切り替えたり、list-detail の二ペインを表示したりする。サイズを単に伸縮するのではなく、レイアウト部品や表示内容を置き換えるのが適応 UI である。

Stylish の AdaptiveNavigation や Screen/Scaffold 系は、M3 の単体 NavigationBar と同じカードに押し込めず、**compact → medium → expanded の遷移シナリオ**として Stylish value に置く。M3 Adaptive に相当する場合は比較対象として明記する。

### アクセシビリティとテストはカードの証拠にする

Compose の [semantics](https://developer.android.com/develop/ui/compose/accessibility/semantics) は、描画ツリーとは別に UI の意味を表すツリーを作り、アクセシビリティと UI テストの両方が利用する。[Compose testing](https://developer.android.com/develop/ui/compose/testing) は semantics で要素を検索し、属性確認と操作を行うことを基本としている。

したがってカタログの各 canonical entry は、見た目だけでなく次を確認できる必要がある。

- role、content description、state description、focus 順序
- enabled / disabled、pressed / focused、loading / error / empty
- light / dark、RTL、font scale、compact / expanded
- キーボード・マウス・タッチ、Reduced Motion（該当する場合）

## 現行カタログの監査結果

### 現在の構造

- `DemoRegistry` は 13 個の provider を連結し、最後に名前の除外集合を適用している（[`DemoRegistry.kt`](../catalog/src/commonMain/kotlin/com/segnities007/stylishui/catalog/DemoRegistry.kt#L9-L49)）。
- 除外集合は 17 個の表示名を手書きしている。これは canonical な意味ではなく、登録順と文字列に依存する。
- `DemoComponent` のメタデータは `name`、`category`、`preview`、`code` の四つだけで、M3 対応先、variant 群、独自価値、状態対応、安定 ID を持たない（[`DemoComponent.kt`](../catalog/src/commonMain/kotlin/com/segnities007/stylishui/catalog/DemoComponent.kt#L5-L18)）。
- カテゴリは Buttons、Selection、Inputs、Navigation、Feedback、Connected、Charts、Advanced、Web、Patterns の 10 種類で、M3 の目的分類と独自価値分類が混在している（[`DemoCategory.kt`](../catalog/src/commonMain/kotlin/com/segnities007/stylishui/catalog/DemoCategory.kt#L11-L22)）。
- 現在のテストも、重複名の集合と特定コード文字列を直接検査している（[`DemoRegistryTest.kt`](../catalog/src/commonTest/kotlin/com/segnities007/stylishui/catalog/DemoRegistryTest.kt#L8-L49)）。これは当面の回帰止めにはなるが、意味的な重複を発見する仕組みではない。

### 問題の本質

今の `filterNot { it.name in ... }` は「13 と 14 が同じ UI に見える」問題を解決していない。片方の名前を隠しているだけで、次の問題が残る。

1. 親子関係（BottomSheet と DragHandle、Dialog と DialogActions）がメタデータにない。
2. 形状違い・variant 違い・用途違い・構成違いが同じ `DemoComponent` 型に平坦化される。
3. `DemoCoverage` / `DemoCoverageExtended` / `DemoPatterns` のような「網羅性のための登録」と、利用者が選ぶ「代表例」が同じ一覧に入る。
4. 名前変更で除外漏れや番号の移動が起きる。

## 提案する情報設計

### 1. 入口（Featured / Choose by task）

最初に全件グリッドを出さず、代表的な 8〜12 シナリオを出す。

| 入口カード | 見せるもの | M3 比較 |
|---|---|---|
| 画面の骨格 | `StylishScreen`、Scaffold、Header、FloatingTop/Bottom | Scaffold / App bars |
| 主要アクション | Button、IconButton、FAB | Button family / FAB |
| 入力と選択 | TextField、Search、Autocomplete、Switch/Checkbox/Radio、Slider | Text inputs / Selection |
| 一時 UI | BottomSheet + DragHandle、AlertDialog、Popover、CommandPalette | Bottom sheets / Dialogs / Menus |
| 階層のある画面 | Layer-aware Surface、Card、Connected Card | Surface / Card / elevation |
| 適応ナビゲーション | NavigationBar / Rail / Drawer の切り替え | M3 Adaptive |
| 状態とフィードバック | Loading、Empty、Error、Toast/Snackbar、Progress | Communication |
| データ表示 | Table、Tree、Charts、Masonry | M3 にない、または限定的 |

Featured は「何を作れるか」を伝える面であり、全 API の索引ではない。

### 2. M3 parity レーン

M3 の公式分類に沿って、意味的ファミリーを一つの canonical entry にする。

| ファミリー | Stylish の代表 entry | 同じ entry 内の variant / 子要素 | 比較対象 |
|---|---|---|---|
| Button | `Button family` | Filled / Tonal / Outlined / Text / loading | M3 Button 5 種 |
| Icon action | `Icon button family` | standard / filled / tonal / outlined / toggle | M3 IconButton |
| FAB | `FAB family` | small / standard / large / extended / floating | M3 FAB |
| Card | `Card family` | filled / elevated / outlined / actionable / disabled | M3 Card |
| Input | `Text input family` | filled / outlined / secure / number / pin / autocomplete | M3 TextField / Search |
| Selection | `Selection controls` | switch / checkbox / tri-state / radio / slider | M3 Selection |
| Chips | `Chip family` | assist / filter / input / suggestion / elevated | M3 Chips |
| Navigation | `Navigation family` | top / bottom / tabs / drawer / rail | M3 Navigation |
| Feedback | `Feedback family` | alert / snackbar / toast / progress / tooltip | M3 Communication |
| Containment | `Modal & containment` | dialog / bottom sheet / popover / scaffold | M3 Containment |

このレーンで M3 と見た目や API がほぼ同じものは、Stylish のテーマ適用例として扱う。独自差分がないものを別カードに増やさない。

### 3. Stylish value レーン

ここは M3 の代替一覧ではなく、Stylish の判断・組み合わせ・契約を見せる。

| 価値グループ | 代表シナリオ | 必ず見せる証拠 |
|---|---|---|
| Layer-aware surfaces | Page → Card → Control → Floating → Modal のネスト | 深さ、min/max 正規化、light/dark の順序、役割別コントラスト |
| Connected composition | Connected Card/Button/Chip/Toggle の row/column/grid | 接続形状、端部、actionable/readonly、キーボード移動 |
| Floating + modal | Floating bars + FAB + BottomSheet/Dialog/Popover | padding、scrim 透過、開閉、focus、スクロール、Insets |
| Adaptive patterns | Screen、AdaptiveNavigation、list-detail | compact/medium/expanded、window resize、pane 切り替え |
| Stateful data UI | DataTable、Tree、Charts、Masonry、PullToRefresh | loading/empty/error、selection、resize、RTL、font scale |
| Cross-platform / Web parity | Web 用の同等 interaction | platform 差を明記し、M3 parity と混ぜない |

`StylishLayerColors` のような基盤実装や spacer、divider、drag handle 単体は、通常このレーンのカードにしない。「その部品を単独で選ぶ理由」がある場合だけ、Foundation/Contract の開発者向け索引に置く。

## canonical metadata の提案

`DemoComponent` に表示名だけを持たせず、カタログ内部型を次のようにする。公開ライブラリ API を増やす必要はない。

```kotlin
internal enum class CatalogLane { Featured, M3Parity, StylishValue, Contract }

internal data class CatalogEntry(
    val id: String,                 // 変更しない安定 ID。番号の代わりに検索・URLで使う
    val familyId: String,           // 1枚にまとめる意味的ファミリー
    val title: String,
    val lane: CatalogLane,
    val m3Equivalent: M3Equivalent?,
    val variantIds: List<String>,
    val composedOf: List<String>,   // DragHandle は BottomSheet の子として記録
    val whyStylish: String?,        // M3 parity 以外では必須
    val supportedStates: Set<CatalogState>,
    val preview: @Composable () -> Unit,
    val code: String,
)
```

登録時に次を検証する。

- `id` と `familyId` は一意。表示名を一意性の根拠にしない。
- `M3Parity` は `m3Equivalent` 必須。`StylishValue` は `whyStylish` 必須。
- `familyId` ごとに表示カードは一枚。variant はカード内状態として扱う。
- `composedOf` に載った子は独立カードに登録しない。ただし子 API のコードは親カードから参照できる。
- `supportedStates` にない状態を、別カードを増やして表現しない。

既存の `demosRepresentedByAnotherCatalogEntry` は削除し、コンパイル時またはテスト時の metadata 検証に置き換える。

## Preview の標準契約

各 canonical entry は同じ順序の詳細面を持つ。

1. **Preview** — 操作可能な最小シナリオ。単体部品だけでなく必要な親（例: BottomSheet + DragHandle）を含む。
2. **Variants** — 同じ意味ファミリーの切り替え。別カードを作らない。
3. **States** — default / pressed / focused / disabled / loading / error / empty のうち対応するもの。
4. **M3 comparison** — M3 equivalent、差分、選定理由。該当なしは「M3 core に相当なし」と明示。
5. **Code** — コピー可能な最小コード。preview の内部実装をそのまま貼らず、利用者が呼ぶ API を示す。
6. **Contract** — semantics、Insets、theme、adaptive、Reduced Motion の確認結果。

## 重複を判定するルール

登録前に、次の順で判定する。

1. **同じ目的か** — ユーザーが同じ仕事をするなら同じ family。
2. **同じ相互作用か** — 開閉、選択、入力、スクロールなどが同じなら variant 候補。
3. **同じ親が必要か** — 親なしで意味を持たない部品は親シナリオへ統合。
4. **差分は契約か** — レイアウト、状態、アクセシビリティ、適応性、プラットフォーム契約の差がなければ統合。
5. **差分が見た目だけか** — 色・角丸・elevation だけならテーマ/variant として統合。
6. 上記を通過して初めて独立 entry とする。

このルールなら、ユーザーが指摘した「カードとボタン」「13/14」「16〜18」「BottomSheet と DragHandle」のような問題を、名前の除外ではなく意味の重複として扱える。

## 実装ロードマップ

### Phase 0: inventory（先に実施）

- 現行 provider の全 entry に安定 `id`、`familyId`、lane、M3 対応先を付ける。
- 既存の 17 個の除外名を、canonical entry の `composedOf` または variant に移す。
- `DemoCoverage` と `DemoCoverageExtended` は、代表 preview と内部検証用ケースを分離する。

### Phase 1: registry と表示を変更

- `CatalogEntry` ベースの registry に移行し、family 一意性をテストする。
- タブを `Featured / M3 parity / Stylish value / Contract` にする。M3 の目的カテゴリは parity 内のサブフィルターにする。
- カードに「M3 equivalent」「Stylish value」「variants」のバッジを表示する。
- 番号は表示上の通し番号に限定し、検索・コピー・URL は `id` を使う。

### Phase 2: 差分を操作可能にする

- Button、Card、Input、Selection、Navigation、Modal の 6 ファミリーを parity の基準にする。
- Layer、Connected、Floating/Modal、Adaptive、Data の 5 グループを Stylish value の基準にする。
- light/dark、RTL、font scale、compact/expanded の matrix preview を共通 harness にする。

### Phase 3: 品質ゲート

- metadata の family/id 重複を `jvmTest` で検証する。
- 各 entry の semantics 操作テストを追加する。文字列の存在だけをテストしない。
- golden は canonical entry × theme × state × size の最小 matrix に限定する。variant ごとに無制限に画像を増やさない。
- `./gradlew jvmTest assemble apiCheck :catalog:jvmApiCheck :checkArchitecture :checkComposableSize --no-daemon --max-workers=1 --console=plain` を完了条件にする。

## 採用判断

この戦略を採用する。理由は以下の通り。

- M3 公式分類と同じ目的語で比較できるため、M3 利用者が迷わない。
- variant と親子部品を一枚にまとめられ、表示カード数と重複を抑えられる。
- Stylish の独自価値を「色違い」ではなく、レイヤー・構成・適応・状態・契約として説明できる。
- Catalog metadata が API 実装名から独立するため、実装ファイルの分割・名前変更で重複除外が壊れない。
- semantics、Insets、adaptive、golden を同じ entry の証拠として扱える。

なお、この文書は戦略と監査結果であり、カタログの Phase 1 移行自体はまだ実施していない。次の変更ではまず Phase 0 の metadata と canonical map を実装し、ユーザー確認後に不要なカードを削除する。

## 参照した公式資料

- [Material components in Compose](https://developer.android.com/develop/ui/compose/components)
- [Button](https://developer.android.com/develop/ui/compose/components/button)
- [Compose Material 3 API reference](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary)
- [Material 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Custom design systems in Compose](https://developer.android.com/develop/ui/compose/designsystems/custom)
- [Compose layout basics（slot-based layouts）](https://developer.android.com/develop/ui/compose/layouts/basics)
- [Scaffold](https://developer.android.com/develop/ui/compose/components/scaffold)
- [Use Material 3 insets](https://developer.android.com/develop/ui/compose/system/material-insets)
- [Get started with adaptive apps](https://developer.android.com/develop/ui/compose/layouts/adaptive/get-started-with-adaptive-apps)
- [Semantics in Compose](https://developer.android.com/develop/ui/compose/accessibility/semantics)
- [Test your Compose layout](https://developer.android.com/develop/ui/compose/testing)

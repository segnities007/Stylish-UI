# Architectural Constraint 基盤監査

調査日: 2026-09-08

対象: `/home/segnities007/Projects/Stylish-UI`

ブランチ: `fix/wasmjs-cmp-compat`

## 結論

判定は **PARTIAL / Architectural Constraint の信頼できる基盤は未成立** です。

このリポジトリには、依存方向・Gradle の project edge・package/path・API/KDoc/Preview
を検査する本物の静的ゲートがあります。したがって「何もない」という状態ではありません。
しかし中心となる実装所有権と実装経路が固定されていません。特に、

- 物理的に抽出した `:foundation` / `:structure` が root の Finish 実装経路に入っていない
- root の `foundation` と `structure` に、ドキュメント上の層定義と衝突する実装が残っている
- 現行の検査が主に「禁止する」負の制約であり、「必ずこの seam を通る」という肯定制約を検査していない
- 関数サイズ検査が有効な Kotlin の大半を認識できず、PASS が実効的な分解保証になっていない
- Linux の統合ゲートが存在しないスクリプトを参照し、CI もその統合ゲートを実行していない

という状態です。

従って、現状の PASS は「既存 checker が認識する範囲での静的衛生」を示すだけであり、
`Foundation → Structure → Finish` の責任分離や、Finish が Structure を実際に消費している
ことの証明にはなりません。

## 期待される契約

リポジトリの規約自体は明確です。

- `AGENTS.md:58-75` は `Finish → Structure → Foundation` の一方向依存を定義し、Foundation
  は UI を描画せず、Structure は色・elevation・角丸・アニメーションを決めず、Finish が
  Structure を消費するとしています。
- `AGENTS.md:90-114` は各層の判定テストと、Connected family を Structure/Finish 分割の
  exemplar として定義し、「新しい Finish は Structure を消費し、レイアウトを再実装しない」
  としています。
- `README.md:97-112` も同じ層定義を公開 API の説明として掲げています。
- `DESIGN.md:11-27` は、この分割を設計原則を構造で強制する仕組みだと説明しています。

この契約に対して、現在の基盤が検査できるのは一部の import とモジュール graph までです。

## 主要な所見

### 1. seam は作られたが、実装所有権が移っていない — FAIL

物理モジュールは存在しますが、実装の深さが不足しています。

- `foundation/src/commonMain/.../StylishHeadless.kt` は reducer、viewport、render plan、
  renderer の framework-neutral 契約だけです。
- `structure/src/commonMain/.../StylishSlotLayouts.kt` は `StylishSlotRow`、
  `StylishSlotColumn`、`StylishSlotGrid`、`stylishGridRows` という低レベルの slot/layout
  primitive だけです。
- 一方、root の `src/commonMain/.../foundation` は 17 ファイル、root の
  `src/commonMain/.../structure` は 19 ファイルあります。
- `src/commonMain/.../components` の 16 ファイルは root の
  `com.segnities007.stylishui.structure.*` を import しています。例えば
  `StylishConnectedCardRow.kt:15-17,79-121` は root の `ConnectedCardRow` を消費します。
- root `build.gradle.kts:353-355` は、物理 module を依存させるのではなく、root artifact に
  binary-compatibility copy を保持する方針を明記しています。root の
  `build.gradle.kts` には `project(":foundation")` / `project(":structure")` の実装依存が
  ありません。

`verify-module-boundaries.py:248-260,388-430` はこの状態を意図的な compatibility copy と
  して扱い、同じ package を同一 classpath に載せないことだけを保証します。これは D8 の
  duplicate-class 防止としては有効ですが、root の実装が物理 module と同じ契約を通ること、
  または copy が実装として drift していないことを保証しません。

つまり、現在の `:foundation` / `:structure` は「移行 canary と公開契約の一部」であり、
root Finish の深い実装を所有する module ではありません。肯定制約の中心である
「Finish の実装はこの Structure seam を必ず通る」が成立していません。

### 2. root Structure は headless 契約から逸脱し、checker の対象外 — FAIL

root の `structure` package は、公開関数だけでなくデフォルト値・Preview 内でも visual
system に依存しています。

- `src/commonMain/.../structure/ConnectedButtonRow.kt:11-27,59-81` は
  `MaterialTheme`、`Surface`、`Color`、`StylishTheme` を import し、公開 API の
  `cornerRadius` と `spacing` の default を `StylishTheme` から取得します。
- `ConnectedCardRow.kt:12-28,61-84` も同じ形です。
- `DataTableLayout.kt:11-18,24-48` も `MaterialTheme`、`Surface`、`Text`、
  `StylishTheme` を import します。

これは少なくとも「Structure は視覚スタイルを持たない」「角丸を決めない」という
`AGENTS.md:71-75,95-105` の契約と整合しません。Preview だけの styling を許すかどうかを
別途決める余地はありますが、公開 API の default と root implementation 本体にも
theme/visual dependency があるため、Preview 例外だけでは説明できません。

それにもかかわらず `scripts/verify-architecture.sh:37-45` が root `structure` に対して
禁止するのは `components.*` import だけで、`theme`、`tokens`、Material3、色、elevation、
角丸値、animation は禁止していません。`verify-module-boundaries.py:302-317` の厳格な
headless rule は物理 `:structure` の source にだけ適用され、root の compatibility copy
には適用されません。実際に architecture checker は PASS します。

### 3. root Foundation が混在し、純度を機械的に確認できない — FAIL

`AGENTS.md:71-75,90-94` は Foundation を「UI を描画しない」と定義しています。
物理 `:foundation` の `StylishHeadless.kt` は import なしでこの契約に近い一方、root の
Foundation は Compose UI/state/rendering helper と混在しています。

明確な反例は `src/commonMain/.../foundation/StylishWindowSizeClass.kt:68-87` の
`StylishAdaptiveLayout` です。`BoxWithConstraints` を配置し、選択した slot content を
呼び出す公開 `@Composable` なので、単なる値計算ではなく UI layout をレンダリングします。
ほかにも `FocusRing.kt:55-65`、`StateLayer.kt:45-55`、`FloatingMotion.kt:56-116`、
`StylishInteractionPolicy.kt:46-74` など、Modifier、transition、interaction state に
結びついた Compose API があります。`ConnectedOutline.kt:40-...` には rendering exception
の説明がありますが、例外を明示的に allowlist 化するゲートはありません。

現状の architecture checker は Foundation から `components` / `structure` への import
だけを禁止します（`verify-architecture.sh:40-41`）。`@Composable` が UI を描画するか、
Modifier が visual decision を行うか、例外が文書化されているかは判定しません。したがって
Foundation の package 名だけで純度を主張できません。

### 4. 関数サイズ gate は実効性がない — FAIL

`build.gradle.kts:87-100` は 80 行超の関数を検査すると宣言し、
`verify-composable-size.py:19` は次の形式だけを認識します。

```text
fun <name><
```

しかし Kotlin の通常の generic 宣言は `fun <T> name(...)` であり、通常の non-generic
宣言は `fun name(...)` です。checker の regex は後者を認識せず、前者も認識しません。
現行 source scan で認識されたのは次の 2 件だけで、いずれも private helper でした。

- `StylishDataTableColumn<*>.effectiveDataTableWeight`
- `List<String>.swapDataTableColumns`

例えば `StylishDataTable.kt:188-599` の公開 `@Composable fun <T> StylishDataTable` は
約 411 行ありますが、`fun <T> StylishDataTable` 形式のため認識されません。現在の
`verify-composable-size.py src catalog/src --max-lines 80 --baseline ...` は
`0 baseline` で PASS します。これは baseline が空で違反がないのではなく、検査対象として
抽出されていないという false negative です。

### 5. component contract は品質契約であって層の firewall ではない — PARTIAL

`verify-component-contracts.sh:11,21-45` は root の `src/commonMain/kotlin` だけを走査し、
physical `:foundation` / `:structure` の source set を走査しません。KDoc と Preview は
strict failure 対象ですが、`testTag` と multi-public-file は `:68-87,93-97` の通り
advisory です。

今回の実行でも 226 declarations、KDoc 226/226、Preview 198/198 は PASS しましたが、
testTag は 106/226、multi-public file は 33 件で、これらは failure になりません。
これは API documentation の検査としては有用ですが、層の所有権、seam の利用、semantic
contract の強制にはなりません。

### 6. 統合ゲートが壊れており、CI の実行経路も一致していない — FAIL

root `check` には `checkComposableSize`、`checkArchitecture`、`checkModuleBoundaries`、
`checkComponentContracts`、`checkCatalogStateMatrix` と複数の canary test が
`build.gradle.kts:300-311` で登録されています。この構成自体は土台として有用です。

しかし `scripts/verify-linux-quality.sh:9-20` は次の 3 ファイルを呼びますが、現在の
`scripts/` に存在するのは `verify-linux-quality.sh` と別の検査群だけです。

- `scripts/verify-token-contract.sh`
- `scripts/verify-quality-evidence.sh`
- `scripts/verify-release-contract.sh`

従って `bash scripts/verify-linux-quality.sh` は architecture PASS の直後に
`verify-token-contract.sh: No such file or directory` で exit 127 になります。さらに
`.github/workflows/ci.yml:68-79` は `./gradlew check` と個別 report checker を実行しますが、
この Linux wrapper 自体は実行していません。ローカルの「標準ゲート」と CI の「標準ゲート」
が別物です。

## 現在の検証結果

| 検査 | 結果 | 何を意味するか |
|---|---|---|
| `git diff --check` | PASS | whitespace error なし |
| `bash scripts/verify-architecture.sh` | PASS | checker が定義した import 禁止規則には違反なし |
| `python3 scripts/verify-module-boundaries.py` | PASS | 11 Gradle modules、291 Kotlin files、package error 0。duplicate package は compatibility copy として許可 |
| `bash scripts/verify-component-contracts.sh --strict` | PASS | KDoc/Preview の必須検査を通過。advisory 項目は failure ではない |
| `python3 scripts/verify-composable-size.py ...` | PASS | 認識できた関数が 2 件だけ、baseline 0。実効的なサイズ保証ではない |
| `python3 scripts/verify-catalog-state-matrix.py` | PASS | public `@Composable` 226、demo 143、visual API 174/185。source-level inventory であり runtime proof ではない |
| `:jvmTest` | PASS | 60 actionable tasks。root/physical module/canary の JVM compile/test を確認 |
| `apiCheck` | PASS | 98 actionable tasks。root/catalog/physical module/sample の ABI/API 検査を確認 |
| `assemble` | PASS | 365 actionable tasks。Android/iOS/Wasm を含む assemble を確認 |
| `bash scripts/verify-linux-quality.sh` | FAIL | 欠落した `verify-token-contract.sh` で exit 127 |
| `./gradlew check` | FAIL（環境/依存解決） | `:kotlinWasmNodeJsSetup` の `org.nodejs:node:25.0.0` を Maven repositories から解決できず構成段階で停止 |

Build/assemble の PASS は、層の設計が正しいことや Android/iOS/Wasm の実機・ブラウザ挙動を
証明しません。また今回の調査では実機 UI、スクリーンリーダー、RTL、font scale、browser
DOM/keyboard、performance SLO は測定していません。

## 「肯定制約」を成立させるための必要条件

実装を直す前に、次の意思決定を ADR などで固定する必要があります。

1. **所有権を一つにする。** root compatibility copy を移行期間だけ許すなら、copy と
   physical module の同値性/同期を機械的に検査する。最終形では実装を物理 module に移し、
   root Finish がその artifact/API を消費できる graph と package 名にする。duplicate-class
   回避のために root dependency を禁止したままでは、実装経路の肯定制約は成立しません。
2. **許可された seam を positive assertion にする。** Connected family などについて、
   Finish の公開関数が Structure の API/slot を実際に呼ぶこと、layout/geometry を Finish
   側で再計算していないことを compiler/AST または限定された生成契約で検査する。
3. **層の純度を source-set 横断で検査する。** root と physical module の両方を対象に、
   Foundation の rendering/Compose exception、Structure の theme/color/elevation/shape/
   animation 依存を明示的な allowlist 付きで検査する。Preview を production source に置く
   かどうかも同時に決める。
4. **checker を Kotlin の構文に合わせる。** regex だけでなく少なくとも annotation、
   `fun <T> name`、modifier/extension、式本体、nested lambda を含めたテスト fixture を作り、
   long function の positive/negative fixture を CI で実行する。可能なら Kotlin PSI/compiler
   parser を使う。
5. **一つの統合ゲートを CI と local で共有する。** 欠落スクリプトを復元するか wrapper
   の責務を現存 checker に合わせ、CI が同じ入口を実行する。Wasm Node version と依存取得も
   lock/CI cache/公式配布のいずれかに固定する。
6. **architecture の PASS/FAIL と runtime adoption を分ける。** 構造検査に加えて、各
   platform の rendered visual、semantics、keyboard/touch、RTL、reduced motion、performance
   の証拠を別 gate として保持する。

## 変更範囲

この監査で source implementation、Gradle 設定、checker は変更していません。この Markdown
レポートだけを新規追加しました。調査開始時から存在した次の dirty/untracked 差分は保持し、
内容を変更していません。

- `api/jvm/Stylish-UI.api`
- `src/commonMain/.../StylishGradientFooterItem.kt`
- `src/commonMain/.../StylishGradientFooterItemContent.kt`
- `src/commonMain/.../StylishGradientFooter.kt`
- `src/commonMain/.../StylishGradientHeader.kt`
- `src/jvmTest/.../GradientFooterVisibilityTest.kt`

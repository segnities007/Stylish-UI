# Stylish UI 競合比較・GAFA採用準備度報告

調査日: 2026-08-21。これは「Linuxでコンパイルできるか」ではなく、デザイナー、利用チーム、品質保証、リリース運用、サポート組織が存在する大規模企業へ外販し、標準UI基盤として採用される準備度を評価する再監査です。前回の92点は、実装量・ビルド成功を製品成熟度として扱っていたため撤回します。

## 結論

**現時点でGAFAへ売り込み、標準UI基盤として採用してもらえるレベルではありません。** 現状は「機能豊富なCMPコンポーネントの社内α〜β」または「限定された社内プロダクトの共通UI基盤」です。Linux統合ゲートは再実行して成功し、A11y semantics、決定論的性能上限、release policy入力、accessibility-tree UI workflowを追加しましたが、実機アクセシビリティ、視覚回帰、frame/memory SLO、Native ABI、Hosted CI E2E artifact、Figma/デザイナー運用、サポート実運用は未完です。

厳格なGAFA外販・採用準備度は **68/100**（デザイン責任者視点62、技術責任者視点74の中央値）。high-contrast semantic roles、32ケースのLinux visual smoke、transport-neutral DataTable adapter、API/CI同期、実解決グラフSBOM生成を反映した再採点です。80点未満は条件付き社内採用、90点以上を外販Production Readyの目安とします。SBOMは構造検証済みですが15件のlicense reviewが残るため、リリースを通せる状態ではありません。

なお、`scripts/verify-linux-quality.sh` はJVMテスト、Android host test、Wasmブラウザタスク、API互換性、依存方向、token/evidence静的監査を一括実行します。加えてAPI 35 emulatorでは `scripts/verify-android-runtime.sh` がCompose consumerのUIAutomator XML/PNG/fingerprint/manifestを生成します。iOS Arm64 compile / simulator testは別のmacOS CI jobで構成され、Linuxスクリプトには含まれません。2026-08-21の統合実行では `check apiCheck wasmJsBrowserTest` が成功し、Android UI semantics smoke（タイトル/state/ID・Name列/Tab後focus）も通過しました。ただし同じemulatorで測定した性能proxyはstartup p95 2,449ms、frame proxy p95 300msとなり、設定budgetを超過してFAILです。**この成功やローカルUI workflowをLinux受入ゲートやGAFA採用度を100/100とは判定しません。** Hosted CI artifact、実機のVoiceOver/TalkBack/Dynamic Type、Figma運用、frame/memory SLO、published ABIは別の未達項目です。

## 競合との位置づけ

| ライブラリ | 主対象 | 相対的な成熟度 | Stylish UIとの主な差 |
|---|---|---:|---|
| SwiftUI/UIKit + HIG | iOS | 非常に高い | OS統合、Dynamic Type、VoiceOver、実機検証、長期互換性 |
| MUI | Web | 高い | API/テーマ成熟度、エコシステム、実運用の回帰資産 |
| Ant Design | Web | 高い | 業務コンポーネントの深度、DataTable/フォームの運用実績 |
| Radix + shadcn/ui | Web | 高い | headless、ARIA、キーボード、ソース所有モデル |
| Chakra UI | Web | 高い | composable style props、semantic tokens、ドキュメント運用 |
| **Stylish UI** | Android/JVM/Web/iOS | **社内α〜β** | common APIと在庫は強いが、実機・性能・運用・エコシステムが未証明 |

## 再採点（100点）

| GAFA採用軸（重み） | 点 | 監査所見 |
|---|---:|---|
| コンポーネント網羅性・実運用の深度 (20) | 13 | 数は多いが、Tree/Transfer/Upload/ContextMenu等の大規模・複雑状態の保証は未成熟 |
| A11y / Focus / Keyboard / Inclusive design (20) | 13 | RTL/200%/Checkbox semantics/focus契約、high-contrast role/WCAG testを追加したが、VoiceOver/TalkBack、実機フォーカス復帰、全体ゲートは未達 |
| カスタマイズ・API柔軟性 (20) | 15 | tokens/defaults/slots、transport-neutral adapter、DataTable/Tree/Transfer/Chartのheadless state/action/reducerとcontrolled rendererを追加。ただし既存の巨大Composable、Java facade互換、固定dp・slot不足、全public APIの一貫性監査が残る |
| Responsive / platform parity (15) | 9 | Android/JVM/Wasm/iOSのcompileは強いが、実行時差分とinsetsは未証明 |
| Data-rich / charts / scale (10) | 7 | 安定sort、深いTreeの2,000深度/100,000 sibling stack-safe flatten、Chart 500点上限と100,000点境界回帰、Linux JVMの10k/100k algorithmic smoke、Wasm 528 KiB JS/6,276 KiB Kotlin Wasm budget実測を追加。列virtualization、frame/memory実測は未証明 |
| Theme / tokens / motion (10) | 6 | semantic JSON↔Kotlin契約、high-contrast modes、22 shared animation sourceのreduced-motion静的契約を追加したが、Figma同期、Typography/密度の二重定義、OS実機でのmotion検証が未達 |
| QA / docs / DX / operations (5) | 5 | API/evidence/release gate、A11y/performance/support/SBOM/R8文書、adapter契約と実行済みLinux統合証跡を追加。実機artifactと実運用は不足 |
| **合計** | **68** | **社内β。外販Production Ready未達** |

## 実測で確認できた強み

- カタログのソースには状態付きデモ、API dump/check、JVM golden、共通テスト、iOS/WasmのCI設定がある。ただし、これらの件数や設定の存在は、全デモをブラウザで操作した実行証跡を意味しない。
- `StylishDataTable` のソート、選択、ページング、列操作、export、Chartのticks/selection semanticsなど、基礎的な業務UI機能は揃っている。
- `StylishTheme`、Dimensions/Shapes/Animation tokens、slot API、M3混在ガイドがあり、個人開発用の基盤としては有望。
- press/hover/focus/default/disabledのElevationを`stylishInteractiveElevation`で共通解決し、カード・リスト・チップ系のデスクトップpointer/keyboard視覚契約を揃えた。ただし全public surfaceと実Desktop artifactは未検証。
- `StylishSplitter`のVertical分割を`Column`へ修正し、ratioの有限値clamp、最新ratioを参照するドラッグ、focusable resize handle、bounded `SetProgress`、矢印/Home/End操作を追加した。これは既存のResizable APIより堅牢になったが、実ブラウザ支援技術とmacOS/iOS実機の読み上げ証跡は未取得である。
- 公開surfaceの安定root tagを共有`StylishAccessibilityTags`へ統一し、advisory coverageを101/220へ改善した。headless契約は物理`:foundation`へ抽出し、既存root ABIは互換コピーで維持したが、全public semanticsの宣言-level 100%、Structure/Componentsの物理分割、実機支援技術の証跡は未完了である。

## GAFA基準で落とす根拠

- デモ在庫は状態網羅の代替ではなく、全状態（loading/error/empty/disabled/long text/RTL/high contrast）を証明するものではない。
- light/dark各1枚の既存goldenに加えて、`GoldenTest` にRTL・320/393dp・100/200% font scale・6状態の96ケース宣言を追加し、`VisualRegressionMatrixTest` にLinux常時検査可能な32ケース（high contrast含む）の画像構造ゲートを追加した。ただしホストフォント差を理由にpixel比較はCIでskipされ、実機artifactはない。
- [docs/quality-audit.md](quality-audit.md) 自身が、A11Y、ロケール、性能回帰、Native ABI、ProGuard、ナビゲーション/Flow連携などを `partial` または `pending` と記録している。
- `StylishTypography` は既定では `FontFamily.Default` だが、共通API `Typography.withFontFamily` と全15 roleの回帰テストを追加し、Wasmサイトも同じAPIでNoto Sans JPを適用するようにした。なお、各ターゲットで同一フォントを配布・計測した証跡まではないため、字幅・改行・視覚階層の完全一致は未達。[StylishTypography.kt](/home/segnities007/Projects/Stylish-UI/src/commonMain/kotlin/com/segnities007/stylishui/theme/StylishTypography.kt:25)
- Dimensions/Shapes/Material Shapesと実装内の直書きdpが併存し、トークンを一箇所変更して全体が変わる保証がない。[StylishDimensions.kt](/home/segnities007/Projects/Stylish-UI/src/commonMain/kotlin/com/segnities007/stylishui/tokens/StylishDimensions.kt:99)
- DataTableに加え、Tree/Transferへheadless state/reducerとcontrolled rendererを追加し、状態をUI外から再生・永続化できる範囲を広げた。一方、既存の多数機能を一つのComposable/APIへ集約した経路も残り、headless state、layout、featureを完全分離したMUI/Radix級の拡張性には未達。Upload/Menu/Color/QR/Scrollは責務別ファイルへ分割したが、Preview/状態網羅はまだ不足している。
- CIにはChrome/Xvfb上の共有ロジック smoke、パッケージ済みサイト、`scripts/wasm-ui-e2e.mjs` によるaccessibility-tree UI workflow、bundle budgetのジョブを構成した。Linuxローカルではカタログ92件、Buttons=14件、Card検索=1件、テーマラベル変更、Tab操作をJSON/PNG付きで通過した。ただしHosted CIの成功artifact、全デモ操作、業務フローE2E、サイズ履歴baselineはまだ未取得であり、実機スクリーンリーダーの代替でもない。
- Wasmのraw webpack出力ではCompose resources（フォント等）が別配布になるため、`assembleWasmProductionSite` を追加してindex・JS/Wasm・processed resourcesを一つの配布ディレクトリへ同期した。Linuxのローカルブラウザでは92件表示、Buttons=14件、Card検索=1件、テーマ切替、Tab操作、コンソールエラー0件を確認し、JSON/PNG artifactを生成した。CIには同じUI workflowと `wasm-ui-e2e-evidence` uploadを構成したが、Hosted CI成功artifactと長期baselineは未取得のため、完全な受入点には加算していない。
- 実行時のiOS VoiceOver/TalkBack、Webスクリーンリーダー、Dynamic Type、ハプティクス、safe-areaをLinuxだけでは証明できない。AppleのAccessibilityガイダンスやMaterialのアクセシビリティ原則に照らしても、compile成功だけでは採用条件を満たさない。[Apple Accessibility](https://developer.apple.com/design/human-interface-guidelines/accessibility), [Material accessible design](https://m3.material.io/foundations/accessible-design/overview)

## デザイナー視点の判定（62/100）

- Visual hierarchy 7/10、Typography 6/10、Spacing/layout 6/10、Color/contrast 7/10、Component consistency 6/10、Motion/interaction 5/10、Responsive 5/10、A11y design 8/15、Catalog UX 5/10、Design operations/Figma 3/10。
- `FontFamily.Default` とサイト固有フォントの分離、トークンと直書きdpの混在、カタログの固定一行レイアウト、light/dark各1枚中心のgolden、Figma/Token Studio/Code Connect不在が、見た目の「それらしさ」とデザインシステム運用の差になる。
- コンポーネント数を増やすより、全状態の anatomy/state matrix、Do/Don't、長文/RTL/200%の実例、デザイナーが編集できる単一トークン源を先に整備すべきである。

## GAFAで売り込める状態にする必須ゲート

1. **デザイン運用**: 単一のtokens source（コード/Figma/JSON）、全コンポーネントの anatomy・variant・state・Do/Don't、ブランドフォント/フォールバック、密度/RTL/長文の規約。
2. **視覚QA**: light/dark、RTL、320/600/840幅、font scale 100/130/200%、high contrast、disabled/loading/error/emptyを主要部品でスクリーンショット回帰し、CI閾値を設定。
3. **実機A11y**: Android TalkBack、iOS VoiceOver/Dynamic Type、Web keyboard/screen readerをシナリオ化し、フォーカス順、状態説明、live region、Escape、最小ターゲットをゲート化。
4. **API/アーキテクチャ**: DataTableのheadless state/reducer・controlled rendererを足場に、DataTableやAdvanced UIを state / layout / feature modulesへ完全分割し、Defaults・slot・state model、Native ABI、semver・deprecation・migrationを固定。Android側はminified consumer sampleとmapping証跡まで実装する。
5. **性能**: 10k行DataTable、長いTree、複数系列Chartを対象に、初回表示、フレーム時間、再コンポジション、メモリ、Wasm/JS/NativeサイズのSLOと回帰テストを公開。
6. **リリース/エコシステム**: clean checkoutから再現可能な署名artifact、SBOM/license/R8、versioned API docs、Navigation/Flow/image/file-picker adapter、問い合わせ/サポートSLA、採用事例。

これらを満たすまでは、UI数をさらに増やすより、未検証の状態を減らす方が点数と採用可能性を大きく改善します。

## 競合比較の参照

- [MUI theming](https://mui.com/material-ui/customization/theming/) / [component customization](https://mui.com/material-ui/customization/theme-components/)
- [Ant Design overview](https://5x.ant.design/components/overview/)
- [Chakra UI components](https://chakra-ui.com/docs/components/concepts/overview) / [theming](https://chakra-ui.com/docs/theming/overview)
- [Radix accessibility](https://www.radix-ui.com/primitives/docs/overview/accessibility)
- [shadcn/ui components](https://ui.shadcn.com/docs/components) / [source ownership](https://ui.shadcn.com/docs/new)
- [Apple SwiftUI accessibility fundamentals](https://developer.apple.com/documentation/swiftui/accessibility-fundamentals)

## 2026-08-21 継続作業の再監査追記

前回評価後、アクセシビリティ契約、モジュール境界、release evidence checksum突合を
機械検査へ接続した。Linuxの最新実行結果は次のとおりである。

- Gradle `check --no-daemon`: **BUILD SUCCESSFUL**（220 actionable tasks、48 executed）
- JVM test / API dump・check / Native ABI: **PASS**
- accessibility contract / module boundaries / release evidence / token literals / Compose metrics:
  **PASS**
- Wasm packaged UI workflow: **PASS**（console errors 0、catalog/category/search/theme/keyboard証跡）
- Android API 35 consumer smoke: **PASS**（クリーン再生成後のDataTable型、UIAutomator semantics、
  `ui-after-tab.xml` のfocusable nodeを確認）

この追加証跡は技術的な再現性を上げるが、採用準備度の厳格スコアは **68/100のまま据え置く**。
スコアを上げるには、Hosted CIの成功artifactを実際に保存したうえで、macOS iOS framework/simulator、
VoiceOver/TalkBack/Dynamic Type、Webスクリーンリーダー、OEM差分、frame/memory/recomposition SLO、
Figma export diffとdesign-owner承認、15件のlicense review、physical Structure/Components Gradle分割を
完了しなければならない。FoundationはCompose-free物理moduleへ抽出済みだが、LinuxローカルのPASSを
残りの外部ゲートの代替にはしない。

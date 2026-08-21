# Verification log

このログは、採用評価の根拠となる実行結果を「設定の存在」と分離して記録する。実行環境や
依存キャッシュが変わるため、採用リリースでは同じコマンドのCI artifactも保存する。

## 2026-08-21 — Deterministic designer/Figma handoff contract

デザイナーと開発者が同じ semantic token source をレビューできるよう、
`scripts/export-design-tokens.py` を拡張し、25 token / 4 mode の typed Figma-variable
interchange (`stylish-ui.figma-variable-handoff.v1`) を生成するようにした。各変数には安定した
`stylish.<path>` ID、slash-delimited name、resolved type、全mode値、`{material.*}` の外部
alias宣言、`dp`/`ms` unitを含め、CSS handoffとsource SHA-256も同一manifestで追跡する。
これはnative Figma exportではなく、Figma/Token Studio adapterへ渡すためのreviewableな
interchangeである。

```text
python3 scripts/verify-design-handoff.py
Design handoff: PASS (10 top-level groups; deterministic Figma interop)
GRADLE_USER_HOME=$PWD/.gradle-local ./gradlew checkDesignHandoff checkReleaseContract checkQualityEvidence --no-daemon
BUILD SUCCESSFUL (3 actionable tasks)
```

検証はsource hash、token path/name重複、mode coverage、Figma variable type/alias inventory、
CSS parity、artifact set、2回のbyte-level deterministic exportを確認する。CI/releaseの静的
契約にも接続した。実Figmaファイルへのimport、export diff、Code Connect、design-owner承認は
Linuxから証明できないため、GAFA採用ゲートとしては未達のまま残す。

## 2026-08-20 — Linux integration

実行コマンド（単一ワーカー、非インクリメンタル）:

```text
GRADLE_USER_HOME=.gradle-ci ./gradlew check apiCheck wasmJsBrowserTest \
  --no-daemon --max-workers=1 \
  -Djava.net.preferIPv4Stack=true \
  -Dkotlin.incremental=false \
  -Dkotlin.compiler.execution.strategy=in-process
```

結果: `BUILD SUCCESSFUL`（141 actionable tasks）。含まれるものは JVM test、Android host
test、Wasm browser test、API check、Atomic architecture、component inventory、token
contract、quality evidence static auditである。iOS Arm64 compile / simulator testは
Linuxコマンドには含まれず、別のmacOS CI jobで構成されている。

このログはLinuxで再現可能な内部ゲートの証跡であり、実機VoiceOver/TalkBack、Figma同期、
UI/DOM業務フローE2E、frame time/memory/recomposition SLO、Native ABI/SBOMを証明しない。
それらを満たすまで、総合GAFA採用スコアは100点にしない。

## 2026-08-20 — Integrated adoption hardening rerun

A11y semantics、deterministic performance bounds、release/support/SBOM/R8 policy inputsを
追加した後、同じ単一ワーカー設定で `check apiCheck wasmJsBrowserTest` を再実行した。
結果は `BUILD SUCCESSFUL`（142 actionable tasks）。`checkReleaseContract` も通過し、
consumer rules、support policy、adapter contract、SBOM policyの入力が揃っていることを確認した。

この成功は、実機A11y、実フレーム/メモリ計測、SBOM生成物、R8 minified sample、Native ABI
差分、Figma同期、外部サポート運用が完了したことを意味しない。

## 2026-08-21 — High-contrast and adapter hardening

追加した契約:

- `StylishHighContrastLightColorScheme` / `StylishHighContrastDarkColorScheme` と
  `StylishTheme(highContrast = true)` を共通APIへ追加。
- `ContrastTest` でprimary/container/secondary/tertiary/surface/errorのAAペアを検証。
- `VisualRegressionMatrixTest` を light/dark × high-contrast on/off × RTL/LTR × 100/200%
  の32ケースへ拡張し、画像構造・状態存在を検査。
- `StylishDataTableAdapter` の transport-neutral suspend境界、ページ正規化、契約テストを追加。
- Popoverのreduced-motionでfade/scale双方をゼロ時間化。

実行コマンド:

```text
GRADLE_USER_HOME=.gradle-ci ./gradlew apiDump jvmTest \
  --no-daemon --max-workers=1 \
  -Djava.net.preferIPv4Stack=true \
  -Dkotlin.incremental=false \
  -Dkotlin.compiler.execution.strategy=in-process
```

結果: `BUILD SUCCESSFUL`（JVMテスト、32ケースvisual matrix、WCAG high-contrast testを含む）。
その後、`check apiCheck wasmJsBrowserTest` の全体ゲートも `BUILD SUCCESSFUL`（143 actionable tasks）
を再確認した。iOS Simulator実行、実機A11y、UI/DOM E2E、frame/memory SLO、Figma同期、
Native ABI/SBOMは引き続き未検証である。

100,000-node sibling Tree と 100,000-point chart downsample の決定論的境界テストを追加後も、
同じ `check apiCheck wasmJsBrowserTest` は `BUILD SUCCESSFUL`（143 actionable tasks）だった。
これはデータ変換・描画上限の証跡であり、frame time、heap、recompositionのSLO計測ではない。

Transferをcontrolled multi-select（`StylishTransferSelectionMode.Multiple`）へ拡張し、
JVM smokeで2項目を選択して一括移動できることも確認した。API dumpと同じ全体ゲートは
`BUILD SUCCESSFUL`（143 actionable tasks）を維持している。

Wasm production webpackもLinuxで完了し、JS 528 KiB / Kotlin Wasm 6,276 KiBを計測した。
設定済みの700 KiB / 10,000 KiB budget内だが、CI履歴・UI/DOM E2E・フレーム性能を証明する
artifactではない。

Wasm配布の実欠陥（raw webpack出力にCompose resourcesが含まれず、フォント取得が失敗する）を
`website-wasm:assembleWasmProductionSite` で修正した。このタスクは`index.html`、webpack JS/Wasm、
processed Compose resourcesを `website-wasm/build/wasmSite` に同期し、`BUILD SUCCESSFUL`
（54 actionable tasks）となった。完全配布ディレクトリをLinuxのローカルHTTPサーバーで実行し、
92件カタログ表示、Buttonsカテゴリの14件絞り込み、検索によるCard 1件表示、Cardデモの操作、
ライト/ダーク切替のアクセシブルラベル変更をブラウザで確認した。コンソールエラーは0件だった。
favicon.svgも配布ディレクトリに含め、404ノイズを除去した。完全配布アセットの存在チェックもPASSした。
favicon付き最終配布物を再度ローカルHTTP配信し、92件表示とコンソール errors/warnings 0件を確認した。
ただしこれは手動ローカル受入であり、CIに保存された
スクリーンショット/DOMログを持つUI/DOM E2Eではないため、E2Eゲートは未達のまま維持する。

Wasm配布タスク、契約スクリプト、ドキュメント更新後に `GRADLE_USER_HOME=.gradle-ci
bash scripts/verify-linux-quality.sh` を単一ワーカーで再実行した。Architecture、token、quality
evidence、release contract、JVM/Android host、Wasm browser task、API check、component
inventoryを含む143 actionable tasksが `BUILD SUCCESSFUL` となり、最終出力は
`Linux quality gate: PASS` だった。iOS Arm64 compile / simulator testは別のmacOS CI jobで
構成され、Linuxでは実行していない。iOS Simulator testはこのLinux実行ではSKIPPEDであり、実機・外部運用の
未検証項目をこのPASSへ含めていない。

共通 `Typography.withFontFamily` と全15 roleの回帰テストを追加し、Web専用の重複実装を除去した。
`apiDump jvmTest` は `BUILD SUCCESSFUL`（77 actionable tasks）、続くLinux品質ゲートは
`BUILD SUCCESSFUL`（143 actionable tasks）で `Linux quality gate: PASS` を維持した。
このAPI変更を含む `:website-wasm:assembleWasmProductionSite` も再実行し、`BUILD SUCCESSFUL`
（54 actionable tasks）で配布ディレクトリを更新した。
CIのWasm budgetジョブはraw webpackではなく `assembleWasmProductionSite` を実行し、index・favicon・
bundled fontの存在も検査する。commit・JS/Wasmサイズを記録する `wasm-bundle-evidence` per-run
artifactのアップロードも追加した。長期baseline/diffは未導入のため、性能SLOの証明とは分離している。
CI Build jobは `WRITE_VISUAL_MATRIX=1` で32ケースのPNG構造artifactを `visual-matrix-evidence`
として保存する設定にした。ホストフォント差を理由にpixel baseline比較はまだ行わない。

`PerformanceBudgetTest` を追加し、Linux JVMで10k DataTable sort=9.768ms、100k Tree flatten=8.708ms、
100k chart downsample=0.089ms（小数3桁、設定予算5,000/5,000/2,000ms）を計測した。`jvmTest` は
`BUILD SUCCESSFUL`（20 actionable tasks）。これはアルゴリズムスモークの測定値であり、frame time、
heap、recomposition、実機SLOではない。CIでは `algorithmic-performance-evidence` として保存する。

この性能証跡の整合性を `scripts/verify-performance-contract.sh` で静的監査し、root Gradleの
`checkPerformanceContract` と `check` に接続した。スクリプトは測定対象・JSON出力先・CI artifact・
非フレーム/非ヒープという限定を同時に検査するため、報告だけが先行してSLOを過大主張することを防ぐ。

共有インタラクション既定値（最小ターゲット、フォーカスリング幅、押下スケール）を semantic
token 参照へ統一し、`checkSemanticTokens` を `check` に追加した。`focusRingWidth` の公開API追加後は
`apiDump` を更新し、続く `jvmApiCheck` を含む root `check` が `BUILD SUCCESSFUL`（145 actionable
tasks）となった。これは公開ABIの同期を確認するもので、実機のフォーカス表示を証明するものではない。

最新の `WRITE_PERFORMANCE_REPORT=1 GRADLE_USER_HOME=.gradle-ci bash scripts/verify-linux-quality.sh`
も `BUILD SUCCESSFUL`（145 actionable tasks）で `Linux quality gate: PASS`。このゲートにはiOS
Simulator実行は含まれず、Linux上で不可能な実機・スクリーンリーダー・Figma・UI/DOM E2Eの証跡は
引き続き未達として扱う。

その後、`website-wasm/build/wasmSite` をローカルHTTP配信し、Chrome DevTools accessibility treeを
使う `node scripts/wasm-ui-e2e.mjs` を実行した。カタログ92件、Buttons=14件、Card検索=1件、テーマ
アクセシブルラベル変更、Tab操作を検証し、console error=0で完了した。CDPでviewport 1440×1000、
deviceScaleFactor=1、locale override=ja-JPを設定し、実際のnavigator localeもartifactへ記録した。
`wasm-ui-e2e.json` と
`wasm-ui-e2e.png` を `website-wasm/build/ci-evidence/` に生成し、CIにも同じ実行とartifact uploadを
追加した。Hosted CI成功artifactは未取得のため、ブラウザ受入ステータスは
`UI_E2E_WORKFLOW_IMPLEMENTED_CI_PENDING` のまま維持する。

UI workflow artifact validatorとbrowser contract更新後も root `./gradlew check` は
`BUILD SUCCESSFUL`（145 actionable tasks）。root checkはUI workflow自体を起動せず、CIの
Wasm jobが実行・保存するartifactの契約と、ローカルに存在する場合のschema整合性だけを検査する。

再利用可能な `scripts/run-wasm-ui-e2e.sh` を隔離ポート（HTTP 8767 / CDP 9224）で実行し、
HTTP server readiness、Chrome lifecycle、CDP accessibility workflow、JSON/PNG artifact validator、
cleanupを終了コード0で確認した。`node --check`、browser contract、quality evidence、release
contract、artifact schema、shell/Python syntax、`git diff --check` もすべてPASS。これはLinuxローカル
の再現性を確認する証跡であり、Hosted CI成功artifact、実機VoiceOver/TalkBack、Figma同期、
frame/memory/recomposition SLOの未達を解消するものではない。

その後 `WRITE_PERFORMANCE_REPORT=1 ./scripts/verify-linux-quality.sh` を再実行し、
`Linux quality gate: PASS`（145 actionable tasks、31 executed / 114 up-to-date）を確認した。
この結果にもiOS Simulatorの実行、実機スクリーンリーダー、Hosted CI artifact、Figma、
Native ABI、SBOM、frame/memory/recomposition SLOは含まれない。

`./gradlew generateSbom --no-daemon` を実行し、Gradle実解決グラフからCycloneDX 1.5の607
componentを生成した。全binary artifactのSHA-256、POMの親license継承、third-party notices、
checksums、license-checkを出力し、`python3 scripts/verify-sbom.py` はPASS（592 allowlist相当、
15 review、missing 0）となった。CI通常ビルドのartifact uploadへ接続し、release workflowでは
`--require-clean`により未承認licenseをfail-closedにした。現時点では15件の法務レビューとHosted
CI immutable artifactが未完了であり、SBOM verifiedやGAFA採用完了とは判定していない。

Androidの最小consumer sample（`samples/android-r8`）を追加し、`./gradlew
:samples:android-r8:assembleRelease --no-daemon` をLinuxで実行した。R8 minify、resource shrinking、
consumer rules適用、mapping/configuration/seeds/usage生成を通過し、`verify-android-r8.py` は
294,816 bytes・1 dex・SHA-256付きのPASS artifactを生成した。CI/release workflowへ接続済みだが、
実機起動、TalkBack、startup SLO、Android published ABIは未検証である。

sampleをroot settingsへ組み込んだ後も `./gradlew check --no-daemon` は `BUILD SUCCESSFUL`
（186 actionable tasks、61 executed / 125 up-to-date）で、sampleのlint/checkも含めて通過した。

`python3 scripts/export-design-tokens.py` を実行し、Kotlin↔JSON token contractの入力から25
semantic tokenを4 modeで正規化したJSON、Web CSS custom-property handoff、source SHA-256付き
manifestを生成した。CI artifact uploadへ接続済みだが、実Figma export diff、Code Connect、
design-owner承認、全component anatomy/state handoffは未完了である。

Compose compiler metricsのJVM-main JSONを `python3 scripts/verify-compose-metrics.py` で検査し、
Strong Skipping、skippable composable 81.8%、effectively stable class 97.4%をPASSとして
`build/reports/performance/compose-metrics.json`へ保存した。CI/release artifactへ接続したが、
これは実行時recomposition回数、frame time、heap、startup、device SLOの証明ではない。

`python3 scripts/verify-native-abi.py --build` をLinuxで実行し、iOS arm64とsimulatorのKLibを
生成して `klib dump-abi` snapshotを取得した。両targetは各1,769 declaration linesで、snapshot
SHA-256とKLib SHA-256を `build/reports/native-abi/manifest.json` に保存し、CI iOS jobへ接続した。
これはKLib公開ABIの証跡であり、macOS Hosted artifact、framework binary diff、iOS runtime、
VoiceOverを証明しない。

`StylishDataTableState`、`StylishDataTableAction`、純粋な`reduce`、controlled renderer overloadを
公開し、filter/page/sort/selection/expansion/column visibility/order/width/pin/focusをUI外から
再生・永続化できるheadless状態契約を追加した。`StylishDataTableStateTest`で正規化・重複列除去・
レイアウト状態のhoistを検証し、`apiDump`後の`jvmTest apiCheck`はBUILD SUCCESSFULとなった。
これはDataTableのAPI柔軟性を改善するが、既存の巨大Composable経路、列virtualization、実UIの
frame/memory/recomposition SLO、RTL golden、全操作シナリオは未完了として扱う。

DataTable action型を`@Immutable`として安定性契約へ組み込み、Compose compiler proxyを再生成した。
`verify-compose-metrics.py` はskippable 81.8%、effectively stable classes 97.4%でPASSした。
その後 `GRADLE_USER_HOME=.gradle-local ./gradlew check --no-daemon` を実行し、Wasm browser test、
Android host test、iOS arm64/simulator compile（simulator testはLinuxではSKIPPED）、JVM API/全静的
quality gateを含む186 actionable tasksがBUILD SUCCESSFUL（79 executed / 107 up-to-date）となった。
これはLinux上の統合成功であり、Hosted CI artifact、macOS framework、実機A11y、Figma承認、
runtime frame/memory/recomposition SLO、15件のlicense reviewを完了扱いにはしない。

共有アニメーションを再監査し、22 source filesが`isStylishReducedMotionEnabled()`または
`reducedMotion`を参照することを`verify-motion-contract.sh`で検査した。FAB、Extended FAB、Header、
Footerのvisibility遷移はreduced motion時にsnapへ切り替え、`checkMotionContract`をGradle `check`
へ接続した。これはLinux上のソース契約であり、OS設定を使ったTalkBack/VoiceOverや実機frameの
検証ではない。

その後、`stylishInteractiveElevation`の導入後に `GRADLE_USER_HOME=.gradle-local ./gradlew
check --no-daemon` を再実行し、`checkMotionContract`、JVM/Android host、Wasm browser test、API
互換性、iOS compileを含む187 actionable tasksがBUILD SUCCESSFUL（59 executed / 128 up-to-date）
となった。LinuxではiOS simulator testはSKIPPEDのため、macOS実機・Hosted artifactの未達は維持する。

`stylishInteractiveElevation`をFoundationへ追加し、press/hover/focus/default/disabledのElevation
ladderを`StylishDimensions`から共通解決するようにした。Standalone/Connected Card、ListItem、
ConnectedListItem、ConnectedChipへ接続し、`InteractionSource`のhover/focus/press状態を同じ
視覚契約で扱う。`jvmTest`、`apiDump`、`apiCheck`は成功したが、全public surfaceと実Desktop
pointer artifactは未検証である。
## 2026-08-21 Android API 35 runtime smoke

`StylishRuntime_API35`（`google_apis_playstore;x86_64`）を新規作成し、
`ANDROID_SERIAL=emulator-5556 bash scripts/verify-android-runtime.sh` を実行した。
結果は `Android runtime: PASS`。`build/reports/android-runtime/` に
`ui.xml`（タイトル、hoisted state、ID/Name row semantics）、`screenshot.png`、
`build-fingerprint.txt`、`manifest.json` を保存した。起動直後のCompose accessibility
bridgeが空になるケースには20秒の再試行を実装した。

この証跡はAPI 35 emulator上のインストール・起動・Compose測定・UIAutomator露出を
示す。TalkBack音声、Dynamic Type、OEM描画、startup/frame/memory SLO、published ABI、
Hosted CI実行成功は未検証であり、GAFA採用判定や100/100判定には使用しない。

## 2026-08-21 Headless state contract expansion

Tree/Transfer/Chartにも`State`・`Action`・純粋`reduce`・controlled renderer overloadを追加し、
`StylishHeadlessStateTest`で選択・展開・系列表示・移送の遷移を検証した。公開Composable監査は
220/220 KDoc、194/194 Previewへ更新され、`apiDump`→`apiCheck`とJVM testは成功した。
Compose compiler proxyはunknown argumentが103へ増えたため、generic collectionを受けるheadless
overloadのbounded regression budgetを110に設定し、skippable 81.5%/stable classes 95.2%とともに
検査する。これは実行時frame/memory/recomposition SLOではない。

同日、`StylishFormTextField`のcursor既定色を`MaterialTheme.colorScheme.onSurface`へ移し、
`verify-token-literals.sh`を`check`へ接続した。テーマ依存色の直書き監査、220/220 KDoc、
194/194 Preview、SBOM、R8、Native ABI、release contract、quality evidenceを再実行してPASS。
`./gradlew check --no-daemon`も218 actionable tasksでBUILD SUCCESSFULとなった。

Wasmは`bash scripts/run-wasm-ui-e2e.sh`をChrome headlessで実行し、catalog/category/search/theme/
keyboard、consoleErrors=0のJSON/PNG証跡を生成した。AndroidはAPI 35 `emulator-5556`で再実行し、
UIAutomator XML、screenshot、fingerprint、manifestを更新してPASS。Androidスクリプトは既定端末が
オフラインの場合にオンラインemulatorを選択し、テスト対象パッケージだけを事前削除して容量不足を
避けるよう強化した。これらはLinux/Android実行証跡であり、Hosted CI、VoiceOver/TalkBack実機、
Dynamic Type、Figma承認、frame/memory/recomposition SLO、SBOM 15件の法務レビューは未完了である。

Tree controlled rendererは`focusedId`を行ごとの`FocusRequester`へ接続し、再表示時のフォーカス復元と
フォーカス状態のhoistを実装した。JVM compile/testは再度PASS。これはCompose側の復元契約であり、
TalkBack/VoiceOverの読み上げ順やIMEを含む実機受入を代替しない。

## 2026-08-21 Release evidence checksum audit

`generateSbom --rerun-tasks`を実行し、現行のGradle解決グラフから613 component（allowlist相当598、
review 15、missing 0）を再生成した。従来の`checksums.txt`生成処理が存在しない`hash`キーを参照して
`null`を出力していたため、CycloneDXの`hashes[SHA-256]`をMaven座標/ファイル名付きで出力するよう修正した。
`scripts/verify-release-evidence.py`を追加し、SBOMのハッシュ、third-party noticesの全座標、license件数、
および保持済みmanifestのサイズ・SHA-256を独立突合する。Linuxで `Release evidence: PASS` と
`verify-sbom.py: PASS`、意図的に`null`へ壊したchecksumのmutation testは期待どおりFAILとなった。
`build/reports/release/evidence-manifest.json`は`REVIEW_REQUIRED`を明示して生成されるため、法務レビュー
15件が残る状態を`VERIFIED`とは扱わない。release workflowでは既存の`verify-sbom.py --require-clean`と
この証跡indexの両方を実行する。Hosted CI immutable保存、法務承認、実機A11y、iOS runtime、性能SLOは未達のまま。

## 2026-08-21 Final Linux acceptance pass (continued)

Accessibility contract、module boundary、release evidenceの各独立検査を追加後のGradle
`check --no-daemon`で再実行し、220 actionable tasksがBUILD SUCCESSFULとなった。`jvmTest`、
`apiDump`/`apiCheck`、Native ABI、token literal、Compose metrics、quality evidence、release
contract、visual matrix、Wasm browser contract/artifactもPASSした。

Android API 35 consumer smokeでは、増分ビルドの古いdex transformがDataTableの新しい公開型を
APKへ取り込まない事象を検出した。生成物の`build`と`build/.transforms`だけを削除してクリーン再生成し、
`StylishDataTableKt`/`StylishDataTableState`の存在を確認したうえで、`verify-android-runtime.sh`を
再実行した。`manifest.json`には`ui-after-tab.xml`が記録され、Tab操作後のfocusable node、タイトル、
hoisted state、table header/row semanticsを再確認できる。

この修正でLinux上の再現可能なAndroid受入証跡は最新化されたが、古いキャッシュを隠すための
証跡ではない。TalkBack/VoiceOver/Dynamic Type、OEM端末、macOS iOS framework/simulator、
Hosted CI immutable artifact、runtime frame/memory/recomposition SLO、Figma/design-owner承認、
15件のlicense review、物理Foundation/Structure/Components Gradle分割は引き続き未完了であり、
GAFA採用判定や100/100判定には使用しない。

## 2026-08-21 Splitter layout and keyboard accessibility regression

`StylishSplitter`を再監査し、Verticalモードが`Row`で幅をweight分割していたため上下パネルに
ならない欠陥を修正した。`Column`で高さを分割し、有限な`minRatio`/`maxRatio`/`ratio`を共通clamp
して不正な永続化値でも負weightを生成しないようにした。ドラッグ処理は`rememberUpdatedState`経由で
最新ratioを参照し、初期ratioを捕捉して戻る問題を防止した。リサイズhandleにはpercentageの
`stateDescription`、bounded `ProgressBarRangeInfo`/`SetProgress`、矢印/Home/Endキー操作、カスタム
content description、`keyboardStep`を追加した。

`StylishSplitterAccessibilityTest`でVerticalレイアウト、SetProgress semantics、ratio clampを検証し、
対象JVMテストはBUILD SUCCESSFULとなった。これはLinux Compose semantics/レイアウト証跡であり、実機
TalkBack/VoiceOverやブラウザ支援技術の読み上げを代替しない。

Splitterの公開引数を`apiDump`/`apiCheck`へ反映後、root `check --no-daemon`を直列で再実行し、
220 actionable tasks（70 executed / 150 up-to-date）がBUILD SUCCESSFULとなった。Native ABI、release
evidence、`git diff --check`も再確認し、Android API 35 consumer smokeを再実行してPASSした。今回の
再実行でもiOS simulatorはLinux上でSKIPPEDであり、外部実機・Hosted CI証跡の未達は変わらない。

## 2026-08-21 Headless model/layout/renderer boundary

物理Foundation/Structure/Components Gradle分割は、現行の公開artifactとconsumer migrationを壊す
リスクが高く、今回のLinux実装単位には選ばなかった。その代わり、Compose/platform importを持たない
`foundation.headless.StylishReducer`、`StylishViewport`、`StylishLayoutEngine`、`StylishRenderPlan`、
`StylishRenderer`を追加した。Tree/Transfer/DataTable/Chartの各state reducerを共通`StylishReducer`
実装へ統一し、既存の`state.reduce(action)` APIはsource-compatibleなdelegateとして維持した。

`StylishTreeLayoutEngine`はvisible rowsをstable id、Tree/TreeItem semantics、expanded/collapsed、selected、
focused、LTR/RTL geometryを含むrender planへ変換する。Web/SwiftUI/Desktop hostはComposeを依存せず同じ
planをnative rendererへ渡せる。`StylishHeadlessArchitectureTest`でreducer replay、viewport normalization、
hit-test、Tree semantics/focus、RTL計算を検証した。

`checkHeadlessArchitecture`、`python3 scripts/verify-headless-architecture.py`、module/architecture guardは
PASS。`jvmTest --tests ...StylishHeadlessArchitectureTest`はBUILD SUCCESSFUL、`apiDump`後に単独の
`apiCheck`もBUILD SUCCESSFUL（Gradle 9.7ではdumpとcheckを同じ呼び出しにするとimplicit dependency
validationで失敗するため、release手順でも分離する）。これはCompose-free common contractの証明であり、
VoiceOver/TalkBack、DOM/SwiftUI/native frame、実機性能SLOの証明ではない。物理module分割、macOS/iOS
runtime、Hosted CI、実機A11yは引き続き外部ゲートとして未達である。

上記変更を含むroot `./gradlew check --no-daemon --max-workers=1`も直列実行し、222 actionable tasks
（67 executed / 155 up-to-date）がBUILD SUCCESSFULとなった。JVM、Android host、Wasm browser、API
互換性、iOS arm64/simulator compile、lint、全静的quality gate、headless architectureを含む。ただし
LinuxではiOS simulator test自体はSKIPPEDであり、これはmacOS runtime/VoiceOver受入の代替ではない。

## 2026-08-21 Algorithmic performance report protocol

`PerformanceBudgetTest`を単一のwall-clock測定から、warm-up 2回＋7サンプルのp95プロトコルへ更新した。
`WRITE_PERFORMANCE_REPORT=1`で生成される`build/reports/performance/algorithmic-budgets.json`は
schemaVersion、protocol、Java/OS/arch/revision、raw samples、min/median/p95、workload budget、statusを
含む。今回のLinux JVM artifactはDataTable 10k sort p95=9.248ms、Tree 100k flatten p95=14.743ms、Chart
100k downsample p95=0.234msで、各budget（5,000/5,000/2,000ms）以内だった。

`scripts/verify-performance-report.py`で必須3 workload、サンプル数、統計整合性、p95 budget ruleを独立検証し、
`performance report: PASS (3 workloads, p95 protocol, budget-only; not a device/frame/heap SLO)`を得た。
CIにも同じvalidatorを接続した。これはLinux/JVMのアルゴリズム回帰証跡を強化するもので、frame/startup/
memory/recomposition、Android/iOS/Web実機やHosted CIのSLO証跡ではないため、採用判定の未達は維持する。

## 2026-08-21 Integrated gate and cross-platform evidence refresh

3系統の並列変更（headless共通契約、Figma token handoff、p95性能証跡）を統合後、root
`GRADLE_USER_HOME=$PWD/.gradle-local ./gradlew check --no-daemon`を同時実行なしで直列実行し、
222 actionable tasks（39 executed / 183 up-to-date）がBUILD SUCCESSFULとなった。iOS simulator testは
Linux上の仕様どおりSKIPPEDであり、成功として数えていない。

統合後のAndroid API 35 runtime smokeは`Android runtime: PASS`となり、UI XML、Tab後のfocus、table
semantics、screenshot、manifestを`build/reports/android-runtime/`へ更新した。Wasm UI E2EもChrome
headlessでcatalog 92件、Buttons 14件、Card検索1件、theme toggle、keyboard focus、consoleErrors=0を
確認し、`website-wasm/build/ci-evidence/wasm-ui-e2e.json`を更新した。

`generateSbom`で現行Gradle解決グラフ613 component（allowlist相当598、review 15、missing 0）を生成し、
`verify-sbom.py`、`verify-release-evidence.py`、`verify-native-abi.py`、`verify-android-r8.py`、
`verify-compose-metrics.py`を再実行してPASSした。Compose stability proxyは現行値81.5% skippable、
95.2% effectively stable classes、106 unknown stable argumentsであり、文書の旧値を更新した。SBOMの
license statusはREVIEW_REQUIREDのままで、法務レビュー完了とは扱っていない。

この統合ゲート成功はLinux上のコード・証跡整合性を示すが、実Figma import/Code Connect/design-owner承認、
Hosted CI immutable artifacts、macOS iOS runtime、TalkBack/VoiceOver/Dynamic Type/OEM差、frame/startup/
heap/recomposition SLO、物理Foundation/Structure/Components Gradle分割、15件のlicense reviewは未達である。
したがってGAFA採用基準100/100や本番採用完了とは判定しない。

## 2026-08-21 Accessibility root tags and physical Foundation boundary

公開Compose surface 220宣言を再監査し、共有`StylishAccessibilityTags`/`Modifier.stylishTestTag`
名前空間（`stylish_`接頭辞、英小文字・数字・`_`/`-`制約）を追加した。主要interactive/visual
componentへ適用し、安定root tagのファイルcoverageは34/220から101/220へ増加した。非描画のslot・
state factoryは誤ったUI nodeを捏造しないためadvisoryのままにし、`verify-component-contracts.sh`
は直接`testTag`と共通helperの両方を監査する。

headless契約はCompose-freeの物理`:foundation` KMP moduleへ抽出し、`:samples:foundation-consumer`
で直接利用するcanaryを追加した。既存root artifactから公開型を消さないため、root側にはmajor release
までのbinary-compatibility copyを残し、`verify-headless-architecture.py`でcanonical sourceと移行境界を
明示する。移行後の`apiDump`→`apiCheck`、`:foundation:apiCheck`、consumer checkはPASSした。

compatibility copyとroot tagsを含む最終`./gradlew check --no-daemon --max-workers=1`は244 actionable
tasks（86 executed / 158 up-to-date）がBUILD SUCCESSFULとなった。`checkModuleBoundaries`は8 Gradle
modules/5 allowed edges、source boundaries 273 Kotlin files/0 errors、headless/accessibility/quality
evidence各gateもPASS。これは物理Foundation抽出とroot ABI維持を同時に確認するLinux証跡であり、
Structure/Componentsの完全物理分割、Hosted CI、実機スクリーンリーダー、Figma承認、frame/memory SLOは
引き続き未達である。

同一FQCNをroot互換コピーと`:foundation` runtime依存の双方へ埋め込むとAndroid D8がduplicate classを
検出するため、rootのruntime `api(project(":foundation"))`は除去した。rootは既存ABIを内包し、移行用
`:foundation`は直接consumerが依存する構成に固定した。`samples:android-runtime:assembleDebug`はD8
duplicateなしでBUILD SUCCESSFUL、最終`verify-android-runtime.sh`とWasm UI E2E（consoleErrors=0）もPASS。

## 2026-08-21 Platform performance evidence contracts (Linux implementation)

R-0/R-6/QA-08の性能証跡を拡張した。Wasm production bundleはJS/Wasmのbyte数、SHA-256、
コミット済みbaselineとの差分を`verify-wasm-bundle-evidence.py`で生成・検証し、CIの
`wasm-bundle-size.json`/`wasm-bundle-history.json` artifactへ接続した。clean checkoutでは
baseline+currentの比較に留まり、`trendClaimAllowed=false`を必須にしている。Hosted前run artifactを
復元しない限り、長期トレンドやregression historyとは主張しない。

JVM Composeでは`ComposeRecompositionBudgetTest`が実際のCompose UI testで5回のstate updateを行い、
successful `SideEffect` composition countとupdate wall-time p95をJSON化する。これはLinux JVMの
recomposition harnessであり、`not_a_frame_or_device_slo=true`を付与する。CIの`check`で生成し、
`verify-compose-recomposition.py --require`で不在・形式不整合・budget超過をfailする。

Android API 35 emulatorでは`verify-android-performance.py`をruntime smokeから呼び、5回の
`adb shell am start -W` startup samplesと`dumpsys gfxinfo`のframe percentileを収集する。必要な
platform fieldが返らない場合は`UNMEASURED`で停止し、`--require`時はjobをfailする。startup p95
2,000ms、frame-proxy p95 32msはemulator proxy budgetであり、production device SLO、Macrobenchmark、
OEM、TalkBack、memory/scroll traceを示さない。したがってこれらの証跡追加後もiOS/Web/Desktop実測、
Hosted長期履歴、実機・OEM性能の未達は採用判定上残る。

## 2026-08-21 Platform adapter and support-policy contract

`samples/adapters`を追加し、Compose-freeのKMP共通契約としてNavigation、kotlinx.coroutines
Flow、image、file-picker/upload、QR encoderを実装した。各境界は成功・分類済み失敗・明示的キャンセルを
`StylishAdapterResult`で表し、file pickerは`Granted/Denied/Restricted/Unknown`を入力として stable
identity、重複、サイズ、単一/複数選択を検証する。Flow adapterはloading/content/empty/errorを発行し、
`CancellationException`をエラーへ変換しない。

Linux JVMの`:samples:adapters:jvmTest`は5 tests PASS（Navigationのnavigate/back/deep-link/restore/invalid、
fileのsuccess/cancel/permission/duplicate、imageのsuccess/decode failure、QRのsuccess/invalid/cancel、
Flowのloading/content/error）となった。`compileKotlinWasmJs`、`compileKotlinIosArm64`、
`compileKotlinIosSimulatorArm64`もPASSしたが、これはWeb/iOS runtimeや実機権限UIの証跡ではない。

`docs/support-policy.json`と`verify-support-policy.py`を追加し、latest/previous-minor support window、
S0〜S3初回人手応答目標、deprecation 2 minor、incident必須項目、秘密情報方針、release evidenceを
機械検証する`checkSupportPolicy`をroot checkおよびCI/releaseへ接続した。`verify-release-contract.sh`、
`verify-module-boundaries.py`、`git diff --check`はPASS。policyStatusは
`defined-not-operationally-proven`であり、実on-call、SLA実績、issue記録、rollback drill、実OS
picker/decoder/SwiftUI/Web host runtimeは未取得のため、採用100/100とは判定しない。

## 2026-08-21 Integrated gate refresh after adapter/performance work

並列作業（catalog state matrix、platform adapter/support DX、性能SLO証跡）を統合し、
`GRADLE_USER_HOME=.gradle-local ./gradlew check --no-daemon --max-workers=1` を直列再実行した。
adapter ABI snapshotを初回生成した後、**263 actionable tasks / 121 executed / 142 up-to-dateで
BUILD SUCCESSFUL**。JVM、Android host、Wasm browser task、iOS Arm64/Simulator Arm64 compile、
API/ABI、9 module境界、catalog 220宣言/92 demo、support/release/token/accessibility gateを含む。

Wasm UI E2Eも再実行し、catalog 92、Buttons 14、Card検索1、theme/keyboard、consoleErrors 0で
artifactを更新した。Android API 35 emulatorのUIAutomator smokeはタイトル、hoisted state、
ID/Name semantics、Tab後focusable nodeまで通過した。一方、同時収集したAndroid性能proxyは
startup p95 **2,449 ms**（budget 2,000 ms）、frame proxy p95 **300 ms**（budget 32 ms）で
**FAIL**となった。これはemulator proxyの実測失敗をそのまま記録したものであり、実機SLOへ
置き換えたり、PASSへ緩和したりしていない。Macrobenchmark/OEM複数端末・実UI traceが必要である。

## 2026-08-21: Web(Wasm)対応の完全削除とゲートのスリム化

オーナー判断により以下を決定・実施した。

1. **Web(Wasm)対応を完全削除。** 理由は現時点でWeb需要がなく、wasmビルド/E2Eが繰り返し
   エラー源になっていたため。KMP UIライブラリ10種の調査ではWeb維持が主流だったが、
   実需を優先する。削除内容: 全モジュールの`wasmJs`ターゲット、`:website-wasm`モジュール、
   `src/wasmJsTest/`、Wasm E2E/bundle/browser-contractスクリプト5本、関連CI job
   （wasm-browser）とartifact upload、Binaryen/Node/Yarnリポジトリ定義。
2. **プラットフォーム方針を確定。** Android/Desktopはフル対応（CI/CDあり）、iOSは
   compileターゲットのみ（検証手段がないためCI/CDゲートなし）、Webは削除。
   macOS CIのiOS jobも削除済み。
3. **検証スクリプトをスリム化。** 構造ガード（module-boundaries/catalog-matrix/
   component-contracts/architecture）と実測系（android-runtime/performance/
   recomposition/metrics/sbom/release-evidence/r8/tokens-export）のみ残し、
   ドキュメント整合監査・token/motion細目の14スクリプトを削除した。
   `checkQualityEvidence`等のExecタスクとCI stepも削除。
4. **GitHub PagesはDokka APIドキュメント専用に変更**（deploy-website.yml）。
   対話型カタログはDesktopアプリ（`:website:run`）と`docs/catalog.md`、JVMゴールデン
   テストで代替する。Storytale等の生成ツールは調査したが、いずれもalpha/Android限定/
   移行コスト大のため不採用。

検証: `./gradlew check --no-daemon --max-workers=1` が **BUILD SUCCESSFUL
（219 actionable tasks）**。旧構成の340タスクから大幅削減。catalog matrix
（220宣言/demo 140/visual API 175/175/missing 0）、module boundaries
（11モジュール/8エッジ/0エラー）、component contracts（KDoc 220/220、Preview 194/194）
はすべてPASS。Android性能proxyのbudget超過（FAIL）は従来どおり正直に記録されており、
次回emulator実行時に新計測契約で再測定する。

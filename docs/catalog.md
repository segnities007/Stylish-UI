# Catalog / Playground ガイド

Stylish UI の `:catalog` は、API の在庫表ではなく「触ってからコピーできる」実行可能な
サンプル集です。Wasm の公式サイトと `:catalog` の JVM/Wasm 成果物は同じ
`DemoRegistry` を参照するため、サンプルの追加・削除とカタログの件数が乖離しません。

## 現在のカバレッジ

2026-08-21 時点で、カタログには **140 件**のインタラクティブデモがあります。
（各 `DemoComponent` の preview を合計した値。実装コンポーネントの API 数とは別の指標です。）

| カテゴリ | デモ数 | 代表的な確認項目 |
|---|---:|---|
| ボタン | 22 | variant、loading、icon、menu、badge、card、Rhythm spacer、visually hidden |
| 選択 | 11 | checked / indeterminate、slider、segmented rows、tab |
| 入力 | 13 | validation、autocomplete、date / date range / time dialog、number、OTP |
| ナビゲーション | 14 | bar / rail / drawer（modal・dismissible・permanent）、search、adaptive |
| フィードバック | 14 | alert、toast、snackbar host、popover、dialog、empty |
| Connected | 18 | card / chip / list / button の row・column・grid・lazy 系と default renderer |
| チャート | 5 | pie、bar、line、area、scatter、empty / animation |
| 高度な UI | 13 | DataTable、Tree、Transfer、Upload、QR、Menubar、command palette、multi-series |
| Web | 21 | descriptions、accordion、stepper、table、timeline、rating、carousel |
| パターン | 9 | header、footer、scaffold、page content、chart sections、bottom sheet scaffold |
| **合計** | **140** | `DemoRegistry.allDemos.size` を正とする |

> 件数はソース変更時に更新されます。手計算の表より、アプリ内の「全て」タブに表示される
> 件数と `DemoRegistry.allDemos.size` を優先してください。

## Public API 対応表と状態マトリクス

`checkCatalogStateMatrix` は、`:catalog` の `DemoComponent` 140件と common の public
`@Composable` 220宣言を同じソースから毎回収集し、双方向の対応表を生成します。
ビジュアルAPI 175件のうち **175件（100%）** がデモと対応し、`missing` は 0 です。
UI を描画しない composable — foundation/theme/tokens ヘルパー、Defaults 系の
パラメータ既定値プロバイダ（`colors` / `shape` / `elevation` / `border`）、headless の
Structure レイアウトホスト（`Connected*` / `DataTableLayout`）、`remember*` ステート
ファクトリ — は、重複したビジュアルカードを作らず `api-doc` 対象として分類します
（根拠は [catalog-state-policy.json](catalog-state-policy.json) の `apiDocOnlyRules`）。
デモ側で実際に呼び出しているファクトリは mapped を優先し、分類の降格は行いません。
シンボル参照の判定は名前一致に加えて引数ラベル（`shape = ...` など）を除外するため、
ラベルだけのエイリアス対応を数えません。ビジュアルAPIに対応するデモが存在しない場合は
`missing`、状態の根拠文字列（loading/empty/error/disabled/selected/focused/pressed/
hovered/long text/RTL）がデモソースにない場合は状態マトリクスの不足として出力します。

```bash
python3 scripts/verify-catalog-state-matrix.py
./gradlew checkCatalogStateMatrix
```

生成物は `build/reports/catalog-state-matrix/` の
`catalog-component-state-matrix.json`（機械利用）と
`catalog-component-state-matrix.md`（レビュー用）です。状態の不足は意図的にレポート
されますが、Linuxの静的根拠をAndroid TalkBack/iOS VoiceOver/Webスクリーンリーダーや
実機visual acceptanceと取り違えないため、通常の構造ゲートは対応表の破損・件数減少のみ
fail-closedにします。採用判定で全行を要求する場合は `--strict` を明示的に使います。

状態の必要条件と根拠パターンは [catalog-state-policy.json](catalog-state-policy.json)
に固定し、レビュー可能な監査テンプレートは
[component-state-matrix.md](component-state-matrix.md) に置きます。

## 触り方

1. Desktopアプリを起動する: `./gradlew :website:run`
   （対話型のWasmカタログは2026-08-21に削除しました。ソース閲覧はこのドキュメント、
   視覚確認はJVMゴールデンテスト画像が代替です。）
2. カテゴリタブで対象領域を絞り込む。
3. 検索欄でコンポーネント名を検索し、A-Z / Z-A / Category で並べ替える。
4. プレビューを操作する。選択・展開・入力・ドラッグ・キーボード操作は、状態が
   カード内に閉じるため安全に試せる。
5. 「コードを表示」→コピーで最小スニペットを取得する。

カタログ内検索を使うと、表示中のデモだけを対象にできます。暗色/明色の切替は
ヘッダーのテーマボタンで行います。Reduced motion が有効な環境では、カテゴリ切替
などの遷移を即時化します。

## サンプル追加の規約

```kotlin
DemoComponent(
    name = "わかりやすい短い名前",
    category = DemoCategory.Advanced,
    code = """StylishTree(nodes = nodes, ...)""",
    preview = {
        // 状態は remember で preview 内に閉じる
        // 外部サービス・OS API は adapter の代替を用意する
    },
)
```

- preview はネットワーク、ファイルシステム、Clipboard などの外部状態に依存させない。
- `code` はそのまま貼り付けられる最小 API 例にし、内部実装や長いデータ生成を含めない。
- ボタンやアイコンには可視ラベルまたは `contentDescription` を付ける。
- 選択・展開・入力などの状態がある場合は、初期状態と操作後の状態を同じ画面で確認できる
  ようにする。
- 既存カテゴリに当てはまらないデータリッチ/OS adapter系は `Advanced` を使う。
- `DemoRegistry` への登録を忘れない。カテゴリ件数は registry から自動算出される。

## Linux での受入チェック

Linux で再現できる品質を、サンプルごとに次の順で確認します。

```bash
GRADLE_USER_HOME=$PWD/.gradle-ci ./gradlew :catalog:compileKotlinJvm --no-daemon
GRADLE_USER_HOME=$PWD/.gradle-ci ./gradlew :catalog:compileKotlinWasmJs --no-daemon
GRADLE_USER_HOME=$PWD/.gradle-ci ./gradlew wasmJsBrowserTest --no-daemon
```

確認項目は、(a)明暗テーマ、(b) 320dp程度の狭い幅、(c)キーボード Tab/Enter/Escape/矢印、
(d)空・エラー・ローディング、(e)長い日本語/英語ラベル、(f)検索とコードコピーです。
`StylishDataTable` は列リサイズ、並べ替え、選択、export の各 controlled callback を
接続したデモにし、Chart は選択点・ticks・legend の表示切替を確認します。

## 受入できない項目を明示する

以下は Linux catalog の品質ゲートではなく、プラットフォーム側で追加確認します。

- iOS VoiceOver の実機フォーカス順・Dynamic Type。
- OS の file picker と QR encoder の実装そのもの（catalog は adapter をモックする）。
- Android TalkBack と端末固有の haptic / insets。

これらを「未検証なのに合格」と扱わず、`docs/quality-audit.md` の platform gate に記録します。

## API を探す順番

画面を作る場合は、まず `patterns` → `organisms` → `molecules` → `atoms` の順に探し、
見た目を独自化したい場合は `structure` の headless slot に降ります。データの表示では
`StylishTable`（軽量）→ `StylishDataTable`（選択・ソート・ページング・export）→
`DataTableLayout`（完全 headless）の順に選択すると、最初から過剰な API を抱えません。

関連資料:

- [adaptive-and-data.md](adaptive-and-data.md): Adaptive / DataTable / Chart の API レシピ
- [MIGRATION.md](MIGRATION.md): M3 からの移行と混在ルール
- [INTEROP.md](INTEROP.md): M3 / Stylish の相互運用
- [quality-audit.md](quality-audit.md): a11y / QA / platform gate

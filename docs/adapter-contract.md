# Stylish UI Platform Adapter Contract

Stylish UI の共通 component は、OS の権限、ファイル、画像、ナビゲーション、ストリームを
直接所有しません。UI は「表示されている状態」と「ユーザーが選択した意図」を公開し、ホスト
アプリが platform adapter を注入します。これにより Android/iOS/Desktop/Web で同じ UI 契約を
保ちながら、権限・lifecycle・非同期処理を各ホストの標準 API に委譲できます。

## 共通規則

| 規則 | 契約 |
|---|---|
| state ownership | 画面/feature が single source of truth を持ち、component は controlled state を描画 |
| event | UI event は意図（request/cancel/retry/remove/select）だけを返し、IO を開始しない |
| lifecycle | adapter は host lifecycle に束縛し、composition の再生成で二重購読しない |
| failure | 失敗は `message` だけでなく分類可能な error code と retryable を持つ |
| cancellation | cancel は idempotent。完了後の cancel と重複 callback は無視できる |
| identity | file/upload/list item には stable key を渡し、表示名を key にしない |
| privacy | path、token、credential、個人情報は component のログに出さない |
| semantics | permission denied、loading、error、success を状態説明として公開する |

## File / image / upload adapter

`StylishUpload` はファイル picker や upload transport を実装しません。最小 adapter は次の
ライフサイクルを満たします。

```kotlin
// UI layer: only intent and immutable state.
StylishUpload(
    files = uploadState.files,
    onFilesChange = viewModel::replaceFiles,
    onRequestFiles = { filePicker.launch(mimeTypes) },
)
```

1. `onRequestFiles` は権限/システム picker を開始する intent であり、UI 再描画から自動実行しない。
2. picker の結果は host が `StylishUploadFile` に変換し、サイズ・MIME・表示名を検証してから state に反映する。
3. upload transport の進行状況、失敗、再試行、cancel は view-model/store が所有し、UI の loading/error/empty slot に渡す。
4. ファイル名は表示用文字列であり stable identity ではない。同名ファイルや順番変更を許容する。
5. Web の drag-and-drop、Android Storage Access Framework、iOS document picker はそれぞれ別 adapter
   とし、共通 component に platform 型を漏らさない。

## Navigation adapter

Navigation は `StylishNavigationItem` の `selected` と `onClick` を host navigation state に接続します。
component は back stack、deep link、saved state を管理しません。

- `selected` は現在の route から導出し、クリック時に先に手動反映しない。
- 同一 item の再選択、back、deep link、復元は host policy に従う。
- route 名や deep link の secret/query token を semantics label に含めない。
- responsive な NavigationBar/Rail の切替は window class に従い、route state は共有する。

## Flow / observable state adapter

Compose 側では `collectAsState` 等の lifecycle-aware adapter を利用し、component の中で Flow を
collect しません。SwiftUI/React/Web store でも同じ one-way data flow を守ります。

```text
source → host adapter → immutable UI state → Stylish component → intent callback → source
```

adapter は initial state、loading/empty/error、stale data、retry、cancellation、exception を
明示的に変換します。callback を複数回発火しないこと、購読を dispose することを adapter test
で検証します。

## DataTable query adapter

Server-backed tables use the common `StylishDataTableAdapter<T>` contract. The adapter is a
`suspend` boundary and therefore remains independent of Retrofit, Ktor, Apollo, SQLDelight, or a
platform lifecycle library:

```kotlin
val adapter = StylishDataTableAdapter<Row> { query ->
    val response = repository.search(
        filter = query.filter,
        page = query.normalizedPage,
        pageSize = query.normalizedPageSize,
        sort = query.sort,
    )
    StylishDataTableQueryResult(
        rows = response.rows,
        totalRowCount = response.totalCount,
        hasNextPage = response.hasNextPage,
    )
}

// Call from the host state holder, not from the composable body.
val result = adapter.loadNormalized(query)
```

`loadNormalized` clamps invalid paging values consistently across hosts. The host owns
cancellation, retries, stale-data policy, authentication, and mapping exceptions into its state
model; the adapter must not log credentials, URLs containing secrets, or row contents.

## Adapter acceptance matrix

The runnable, Compose-free examples live in [`samples/adapters`](../samples/adapters) and the
copy-ready host mapping is in [`docs/adapter-examples.md`](adapter-examples.md). The JVM contract
test is wired into the root `check` lifecycle as `:samples:adapters:jvmTest`; Wasm, iOS Arm64,
and iOS simulator common-source compilation is also a targeted sample gate. These artifacts
validate the transport-neutral state/failure contract, not an OS picker, native decoder, or
screen-reader run.

モジュール境界の観点では、adapter は依存方向テーブル（[`docs/module-boundaries.md`](module-boundaries.md)）
で最上位 consumer 層に置かれる。`:samples:adapters` が採用できる下位モジュールは Compose-free の
`:foundation` 契約のみで、`:structure`・styled root・host を直接依存に追加することは
`scripts/verify-module-boundaries.py` が禁止する。

| adapter | 必須シナリオ | Linuxで確認可能 | 未検証 |
|---|---|---|---|
| file/upload | success、cancel、permission denied、duplicate、large file、retry | state/contract test | Android/iOS実 picker と権限UI |
| image | loading、decode failure、placeholder、content description | pure state test | platform decoder、memory pressure |
| navigation | selected、back、deep link、restore、reselect | model test | host router と実機 back gesture |
| Flow/store | initial、stale、error、retry、dispose、duplicate emission | pure state test | 各ホスト lifecycle |
| DataTable query | normalized page、sort/filter mapping、empty page、retry/cancel | pure adapter contract test | server transport、host lifecycle、offline cache |
| QR/export | invalid input、large input、clipboard/share failure | encoder test | native share/pasteboard permission |

この表の「Linuxで確認可能」は API 契約の証明であって、OS adapter の実装受入を意味しません。
各採用プロジェクトは host adapter を別モジュールで持ち、実機シナリオを release evidence に
追加してください。

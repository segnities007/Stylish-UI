# Adaptive layout / data-rich UI

## Window size class

`calculateStylishWindowSizeClass(width, height)` はプラットフォーム API に依存せず、
親から渡された利用可能領域を Compact / Medium / Expanded に分類します。幅の境界は
600dp・840dp、高さの境界は 480dp・900dp です。

```kotlin
StylishAdaptiveLayout(
    compact = { PhoneScreen() },
    medium = { TabletScreen() },
    expanded = { DesktopScreen() },
)
```

`medium` と `expanded` は省略すると小さい側の slot にフォールバックします。OS の
画面サイズではなく、コンポーネントが実際に使える制約を基準にするため、split view、
デスクトップのリサイズ、Web のレスポンシブ配置でも同じ判定になります。
`StylishWindowBreakpoints` を渡せば、アプリ固有の境界へ変更できます。

`StylishAdaptiveNavigation` は同じ `StylishNavigationItem` を使って、Compactでは
NavigationBar、Medium/ExpandedではNavigationRailへ自動切り替えします。

## DataTable

`StylishDataTable` は既存の読み取り専用 `StylishTable` と併用できます。大量データには
Lazy 仮想化、sticky header、stable row key、列ごとの幅・整列・cell slot、controlled
sorting / selection を提供します。`filterText` / `filterPredicate` による組み込みフィルタ、
`page` / `pageSize` によるページング、`expandedKeys` / `expandedContent` による行展開も
利用できます。サーバー側検索・ソートを使う場合は各変更コールバックを接続します。

`isLoading`、`error`、`loadingContent`、`errorContent`、`emptyContent` により、
非同期データの状態も同じテーブル構造で表現できます。

`visibleColumnIds` と `columnOrder` で表示列と順序を制御できます。`onColumnOrderChange` を
接続するとヘッダーの左右キーで列を並べ替えられます。`columnWidths` と
`onColumnWidthsChange` を渡すとヘッダーにドラッグ/キーボードリサイズハンドルが現れ、
`pinnedColumnIds` と `freezePinnedColumns = true` で先頭の固定列を水平スクロールから分離できます。
`onExport` は現在のフィルタ・ソート後の行を渡し、`onExportText` は列の`exportValue`を使って
CSV/TSV/JSONを生成します。サーバー側のページングやエクスポートは、
`onPageChange` / `onExport` をネットワーク層へ接続してください。

```kotlin
StylishDataTable(
    rows = visibleRows,
    rowKey = { it.id },
    columns = listOf(
        StylishDataTableColumn(
            id = "name",
            title = "Name",
            comparator = compareBy { it.name },
            exportValue = { it.name },
        ) { Text(it.name) },
    ),
    sortState = sortState,
    onSortStateChange = { sortState = it },
    selectedKeys = selectedKeys,
    onSelectedKeysChange = { selectedKeys = it },
    filterText = query,
    onFilterTextChange = { query = it },
    filterPredicate = { row, text -> row.name.contains(text, ignoreCase = true) },
    page = page,
    pageSize = 25,
    onPageChange = { page = it },
    columnWidths = widths,
    onColumnWidthsChange = { widths = it },
    exportFormat = StylishDataTableExportFormat.Csv,
    onExportText = { csv -> saveCsv(csv) },
)
```

列幅の変更はドラッグとキーボード（矢印キー）で行えます。exporterは列の
`exportValue`だけを使い、CSV/TSV/JSONを同じcommon APIから生成します。列固定は
`pinnedColumnIds`で固定状態を示します。`focusedCell` と `onFocusedCellChange` を接続すると、
表のセル間を矢印/Home/End/Tabキーで移動でき、選択セルの位置をアプリ側で復元できます。

サーバー検索の状態は `StylishDataTableQuery`、結果は `StylishDataTableQueryResult<T>` に
保持できます。これらは transport 非依存の primitive モデルなので、REST/GraphQL/SQL の
いずれにも変換できます。

独自の見た目が必要な場合は headless な `DataTableLayout` を直接使います。構造層は
header / row slot と LazyListState だけを扱い、色やタイポグラフィを決めません。

## Multi-series chart

`StylishMultiSeriesLineChart` は複数系列、横スクロール可能な legend、controlled point
selection、tooltip slot、系列表示切替、軸ラベル、value formatterを提供します。単一の
`StylishChartState`/`StylishChartAction`をhoistすれば、チャートと詳細パネルを同じstoreで
同期・永続化できます。非有限値と系列ごとの欠損点は描画から
安全に除外されます。Canvasはフォーカス可能で、矢印/Home/Endキーから点を選択できます。
`showAxisTicks`、`yAxisTickFormatter`、`xAxisTickFormatter`で軸値をlocale/業務単位へ
差し替えられ、選択点はstateDescriptionへ出力されます。
`ColorScheme.toStylishColorBlindSafeChartColors()` は Okabe–Ito 系のカテゴリ色を提供し、
色だけに依存しない系列名・選択点 semantics と併用できます。

## Advanced primitives

`StylishTree`、`StylishTransfer`、`StylishUpload`、`StylishColorPicker`、
`StylishScrollArea`、`StylishContextMenu`、`StylishMenubar`を共通APIとして提供します。
Tree/MenubarはEnter/Space/Escape/矢印キー、Transfer項目はfocus/selected semanticsを備え、
Tree/Transferはそれぞれ`StylishTreeState`/`StylishTreeAction`、
`StylishTransferState`/`StylishTransferAction`としてUI外へ状態をhoistでき、reducerを
ストア・永続化・リプレイへ接続できます。従来の個別callback APIも互換維持しています。
Advanced primitiveの操作ラベルは`StylishStrings`から注入されます。
ファイル選択などOS固有処理は`StylishUpload.onRequestFiles`からプラットフォーム側へ委譲します。

## Catalog packaging

公開 Maven artifact には実行用デモ catalog を含めません。catalog はリポジトリ内の
`:catalog` モジュールに分離され、`:website` と `:website-wasm` だけが依存します。

## Localization hooks

`StylishTheme(strings = ...)` で主要な操作ラベルと整数・小数・通貨のformatterを
アプリのlocaleへ差し替えられます。日付は既存の`StylishDatePickerField.formatter`へ
locale-aware formatterを渡します。

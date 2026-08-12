# M3 → Stylish UI マイグレーションガイド

このガイドは、Material 3（M3）で書かれた Compose アプリを Stylish UI に
移行するための手引きです。Stylish UI は **M3 を土台**にしたデザインシステム
（MaterialTheme ベース）のため、移行は段階的に進められます。

## 移行の基本方針

1. **`StylishTheme` で包む**: ルートの `MaterialTheme` を `StylishTheme` に
   置き換えます。`StylishTheme` は内部で `MaterialTheme` を提供するため、
   カスタム `colorScheme` / `typography` / `shapes` はそのまま渡せます。
2. **コンポーネントを置き換える**: 下表を参考に、M3 コンポーネントを Stylish
   版に置き換えます。置き換えは画面単位・コンポーネント単位で段階的に可能です。
3. **M3 との混在は許可**: 移行途中は Stylish と M3 が混在しても問題ありません。
   混在時のルールは [docs/INTEROP.md](INTEROP.md) を参照してください。

```kotlin
// 移行前
MaterialTheme(colorScheme = MyColorScheme) {
    Button(onClick = { }) { Text("保存") }
}

// 移行後
StylishTheme(colorScheme = MyColorScheme) {
    StylishButton(onClick = { }) { Text("保存") }
}
```

## コンポーネント対応表

一般的な M3 コンポーネントと Stylish UI の対応です。

| M3 | Stylish UI | 備考 |
|---|---|---|
| `Button` | `StylishButton` | `variant` で Filled / Tonal / Outlined / Text / Elevated を切替え。`isLoading` 対応 |
| `IconButton` | `StylishIconButton` / `StylishRoundedIconButton` | Rounded はラベル付き |
| `FloatingActionButton` | `StylishFab` | `sizeVariant` で Regular / Small / Large |
| `AssistChip` / `FilterChip` / `InputChip` / `SuggestionChip` | `StylishChip` | `variant` + `selected` で統一。Filter は選択でチェックマーク |
| `Card` / `ElevatedCard` / `OutlinedCard` | `StylishCard` | `variant` で Filled / Elevated / Outlined |
| `Switch` / `Checkbox` / `RadioButton` | `StylishSwitch` / `StylishCheckbox` / `StylishRadioButton` | 同名のスタイル統一版 |
| `Slider` / `RangeSlider` | `StylishSlider` / `StylishRangeSlider` | RangeSlider は M3 experimental のラッパー |
| `Badge` / `BadgedBox` | `StylishBadge` | |
| `Divider` / `HorizontalDivider` | `StylishHorizontalDivider` / `StylishVerticalDivider` | |
| `CircularProgressIndicator` / `LinearProgressIndicator` | `StylishCircularProgressIndicator` / `StylishLinearProgressIndicator` | |
| `Snackbar` / `SnackbarHost` | `StylishSnackbar` / `StylishSnackbarHost` | |
| `ListItem` | `StylishListItem` | 連結グループは `StylishConnectedListItemColumn` など |
| `NavigationBar` / `NavigationBarItem` | `StylishNavigationBar` + `StylishNavigationItem` | モデルデータ方式 |
| `TabRow` / `Tab` | `StylishTabBar` | |
| `TopAppBar` 系 | `StylishTopAppBar` | M3 experimental のラッパー |
| `SearchBar` / `DockedSearchBar` | `StylishSearchBar` | M3 experimental のラッパー |
| `DatePicker` / `DatePickerDialog` | `StylishDatePickerField` | |
| `AlertDialog` / `Dialog` | `StylishAlertDialog` / `StylishDeleteConfirmDialog` / `StylishDialogSurface` / `StylishDialogActions` | |
| `Scaffold` | `StylishScaffold` | |
| `Text` / `TextField` | `Text`（M3 のまま） / `StylishFormTextField` | Text は M3 のものをそのまま使用 |
| — | `StylishConnectedCardRow` / `StylishConnectedCardColumn` / `StylishConnectedCardGrid` | M3 にない連結カード |
| — | `StylishConnectedButtonRow` / Column / Grid | M3 にない連結ボタン |
| — | `StylishConnectedChipRow` / Column / Grid | M3 にない連結チップ |
| — | `StylishConnectedListItemRow` / Column / Grid / LazyColumn / LazyGrid | M3 にない連結リスト |
| — | `SimplePieChart` / `SimpleBarChart` / `SimpleLineChart` | チャート（common で全プラットフォーム） |

## テーマの移行

### `StylishTheme` ラッパー

`StylishTheme` は `MaterialTheme` の上位互換ラッパーです。

```kotlin
StylishTheme(
    darkTheme = isSystemInDarkTheme(),
    dynamicColor = true,             // Android 12+ では Material You
    colorScheme = MyColorScheme,     // M3 の ColorScheme をそのまま渡せる
    typography = MyTypography,       // M3 の Typography
    shapes = MyShapes,               // M3 の Shapes
) { ... }
```

M3 の `MaterialTheme.colorScheme` / `typography` / `shapes` への参照は、
`StylishTheme` 内でもそのまま動きます。

### トークンの上書き

Stylish 固有のトークンは `dimensions` / `animation` パラメータで上書きします。

```kotlin
StylishTheme(
    darkTheme = false,
    dimensions = StylishDimensions(
        connectedCornerRadius = 20.dp,  // 連結グループの外側の角丸
        connectedSpacing = 6.dp,        // 連結アイテム間の隙間
        outlineWidth = 1.dp,            // ヘアライン枠の太さ
        interactiveElevation = 2.dp,    // 操作可能アイテムの浮き
    ),
    animation = StylishAnimationTokens(durationMedium = 400),
) { ... }
```

トークンの一覧は `com.segnities007.stylishui.tokens.StylishDimensions` /
`StylishAnimationTokens` の KDoc を参照してください。

### 色の上書き

M3 の `colorScheme` に加え、Stylish 固有色
（`com.segnities007.stylishui.theme.StylishComponentColors`）を
`MaterialTheme.stylishComponentColors` から参照・上書きできます。

## Connected ファミリー（新しいプリミティブ）

Stylish UI の大きな特徴は **Connected（連結）ファミリー**です。複数の
ボタン / カード / チップ / リストアイテムを、境界線と角丸を共有しながら
連結したグループとして描画します。M3 にはない新しいプリミティブとして
扱ってください。カードの連結はレイアウト（`Structure`）と見た目（`Finish`）が
分離されており、`StylishConnectedCardItemContent` スロットに独自の
レンダラーを渡すことで、同じ連結ジオメトリで独自スキンを組めます。

```kotlin
StylishConnectedCardRow(
    items = listOf(
        StylishConnectedCardItem("売上", "¥1,200,000"),
        StylishConnectedCardItem("経費", "¥320,000"),
    ),
)
```

各アイテムはインデックスに応じて自動的に角丸・境界線が計算されます
（先頭は左のみ丸、末尾は右のみ丸、中央は角なし + 縦線共有）。

## interactionSource の利用

連結ファミリーは現在 `interactionSource` を公開していません（0.8.0 で対応
予定、[docs/quality-audit.md](quality-audit.md) API-07）。単体コンポーネント
（`StylishButton` / `StylishChip` / `StylishCard` / `StylishSlider` /
`StylishRangeSlider` など）は `interactionSource` パラメータで外部から
`MutableInteractionSource` を渡せます。ホバー / フォーカス / プレス状態の
監視（例: デスクトップでのホバーエフェクト）はこれを使用してください。

```kotlin
val interactionSource = remember { MutableInteractionSource() }
val pressed by interactionSource.collectIsPressedAsState()

StylishButton(onClick = { }, interactionSource = interactionSource) {
    Text("保存")
}
```

## Experimental API のラッパーについて

`StylishSearchBar` / `StylishTopAppBar` / `StylishRangeSlider` は、M3 の
experimental API（`@ExperimentalMaterial3Api`）を内部でラップしています。
ラッパー側で `@OptIn` を済ませているため、**通常の呼び出しに `@OptIn` は
不要**です。ただし、以下の場合は呼び出し側で
`@OptIn(ExperimentalMaterial3Api::class)` が必要です。

- `SearchBarColors` / `TopAppBarScrollBehavior` など、experimental な型を
  パラメータで直接参照する場合
- ラッパーの実装が依存する M3 experimental API の挙動が M3 のバージョンで
  変わる可能性があることへの理解（破壊的変更の可能性）

## M3 と Stylish の混在

M3 コンポーネントと Stylish コンポーネントは同じ画面内で混在できます。
詳しいルールは [docs/INTEROP.md](INTEROP.md) の「混在利用ガイド」を
参照してください。

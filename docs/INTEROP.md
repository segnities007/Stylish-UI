# Stylish UI 相互運用ガイド（Interop）

このドキュメントは、Stylish UI を利用・統合する際の公式スタンスをまとめた
ものです。プラットフォーム対応、依存関係、アイコン方針、M3 との混在、
および品質監査で判断が必要だった 6 項目の決定ログを含みます。

## プラットフォーム対応マトリクス

| プラットフォーム | ステータス | 備考 |
|---|---|---|
| Android | ✅ 対応（CI/CDあり） | Dynamic Color（Material You）は Android 12+ で有効 |
| JVM Desktop | ✅ 対応（CI/CDあり） | Windows / macOS / Linux。カタログ閲覧は `:website:run` |
| Web（Wasm） | ❌ 削除（2026-08-21） | 需要がないため削除。カタログはDesktopアプリと`docs/catalog.md`で代替 |
| iOS | ⚠️ compile対応のみ | `iosArm64` / `iosSimulatorArm64`。検証手段がないためCI/CDゲートはなし |

## サポート環境

| 依存 | バージョン | 備考 |
|---|---|---|
| Compose Multiplatform | 1.11.1 | `compose` plugin と一致 |
| Kotlin | 2.4.10 | `kotlin` plugin と一致 |
| `kotlinx-datetime` | 0.8.0 | 必須依存（後述） |
| `material-kolor`（MaterialKolor） | 5.0.0 | Dynamic Color のシードカラー用（後述） |

Stylish UI は `io.github.segnities007:stylish-ui` の単一 artifact です。
BOM や version catalog の提供予定はありません（複数 artifact に分かれていないため）。

## 依存関係ポリシー

### `kotlinx-datetime`（必須）

`kotlinx-datetime` は `StylishDatePickerField` の日付計算に使用しており、
**必須の推移的依存**です。利用側で明示的に追加する必要はありませんが、
バージョンを管理したい場合は以下のように上書きできます。

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0")
}
```

### `material-kolor`（MaterialKolor）

Dynamic Color のシードカラー変換を全プラットフォームで行うために追加されました。
Android ではシステムの Material You パレットが優先されますが、それ以外の
プラットフォームでも同じシードカラーから一貫したトーンを生成できます。

```kotlin
StylishTheme(
    darkTheme = false,
    dynamicColor = true,   // シードカラー（MaterialTheme.colorScheme の primary）を元に生成
)
```

## アイコンポリシー

Stylish UI は内部で **`materialIconsExtended`** に依存しています（components
が `Icons.Default.*` を使用するため）。この依存は維持し、利用ガイドとして
以下を公式方針とします。

- ライブラリ内部では `materialIconsExtended` のアイコンを使用してよい。
- 利用側アプリのアイコン選定は自由。アイコンはデータクラスの
  `icon: ImageVector` パラメータとして渡す設計のため、`material-icons-core`、
  `materialIconsExtended`、独自アイコンライブラリのいずれも利用できます。
- `Icon` の描画に M3 の `Icon` / `IconButton` を利用しており、アイコン自体の
  サイズ・色は `MaterialTheme` のトークンに従います。

## 混在利用ガイド（M3 と Stylish）

Stylish UI は M3 の上に構築されています（`MaterialTheme` ベース）。したがって
**M3 コンポーネントと Stylish コンポーネントの混在は正式にサポート**します。

1. **`StylishTheme` で包む**: M3 と Stylish を混在させる画面は、ルートを
   `StylishTheme` にします。`StylishTheme` は `MaterialTheme` を内部で提供する
   ため、`MaterialTheme.colorScheme` 等の参照はそのまま動作します。
2. **M3 コンポーネントは StylishTheme 内でそのまま使える**: `Text`、`Icon`、
   `TextField`、`Scaffold` など、Stylish 版のない M3 コンポーネントは
   `StylishTheme { ... }` の中でそのまま使ってください。トークン（色・
   タイポグラフィ）は統一されます。
3. **Stylish が提供するコンポーネントは Stylish 版を使う**: 対応する Stylish
   コンポーネントがあるもの（Button / Card / Chip / NavigationBar など）は
   スタイルが統一されるよう Stylish 版を使ってください。対応表は
   [docs/MIGRATION.md](MIGRATION.md) を参照。
4. **`dynamicColor` の注意**: `StylishTheme(dynamicColor = true)` と M3 の
   `MaterialTheme` を直接ネストすると色が2重適用されます。ルートは必ず
   `StylishTheme` を1つにしてください。

```kotlin
StylishTheme(darkTheme = isSystemInDarkTheme()) {
    // M3 と Stylish の混在が可能
    Scaffold(topBar = { TopAppBar(title = { Text("設定") }) }) { padding ->
        StylishConnectedListItemColumn(
            items = listOf(
                StylishConnectedListItem(
                    headline = "テーマ",
                    trailingContent = { Switch(checked = true, onCheckedChange = {}) },
                ),
            ),
            modifier = Modifier.padding(padding),
        )
    }
}
```

移行の詳細は [docs/MIGRATION.md](MIGRATION.md) を参照してください。

## 判断ログ（設計上の 6 判断）

主要な設計判断とその決定です。未確定の項目は 0.8.0 の実装中に確定します。

| # | 判断 | 決定 | ステータス |
|---|---|---|---|
| 1 | `compose.material`（M2）コア依存 | commonMain の使用箇所を grep で確認した結果、`androidx.compose.material.*` の利用は **`material.icons` のみ**で、M2 の UI コンポーネント（`androidx.compose.material.Button` 等）は未使用。M2 コアは不要と判断し、削除を検討 | 0.8.0 実装中に確定 |
| 2 | `kotlinx-datetime` | **残す**。`StylishDatePickerField` が使用。必須依存として本ドキュメントに明記 | 確定 |
| 3 | `materialIconsExtended`（アイコン） | **残す**。依存を維持し、上記「アイコンポリシー」として文書化 | 確定 |
| 4 | アダプティブ（adaptive）対応 | window-size-class、custom breakpoints、adaptive slotを提供。OS固有のNavigation自動切替は利用側で選択 | 実装済み（0.10.0） |
| 5 | ハイコントラストモード | **ドキュメント化のみ**。トークン導入は行わず、利用ガイドで推奨設定を案内 | 確定 |
| 6 | M3 Expressive | **モーション（motion）トークンの拡張のみ**採用。MotionScheme / TonalPalette 等のフル対応はしない | 確定 |

## 関連ドキュメント

- [M3 → Stylish マイグレーションガイド](MIGRATION.md)
- [デザインガイドライン](../DESIGN.md)

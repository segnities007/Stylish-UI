# Web UI ライブラリ比較と実装判断（Web Parity Audit）

Web の著名 UI ライブラリ（shadcn/ui, Radix UI, Material UI, Ant Design,
Chakra UI）と Stylish UI を比較し、欠落している要素と実装判断を記録する。

調査日: 2026-08-13 / 対象: v0.8.0 時点の Stylish UI

## 比較方法

- shadcn/ui コンポーネント一覧（ui.shadcn.com/docs/components）
- Material UI コンポーネント一覧（mui.com/material-ui/all-components）
- Radix UI / Ant Design / Chakra UI は知識ベースで補完

## 第1ラウンド: shadcn/MUI 比較（本ブランチで実装）

| # | コンポーネント | Web 出典 | Stylish 判断 | 状態 |
|---|---|---|---|---|
| 1 | Accordion / Collapsible | shadcn/MUI/Chakra | **実装**: 折りたたみセクションの標準パターン | ✅ |
| 2 | Stepper / Steps | MUI/Ant/Chakra | **実装**: ウィザード・段階表示 | ✅ |
| 3 | Breadcrumb | shadcn/MUI/Ant | **実装**: 階層ナビゲーション | ✅ |
| 4 | Popover | shadcn/Radix/MUI | **実装**: 汎用アンカー付きフローティング（メニュー以外） | ✅ |
| 5 | Pagination | shadcn/MUI/Ant | **実装**: ページング | ✅ |
| 6 | Rating / Rate | MUI/Ant/Chakra | **実装**: スター評価 | ✅ |
| 7 | NumberInput / NumberField | MUI/Ant/Chakra | **実装**: 数値スピナー入力 | ✅ |
| 8 | InputOTP / PinInput | shadcn/Chakra | **実装**: ワンタイムコード入力 | ✅ |
| 9 | Editable | Chakra | **実装**: インライン編集 | ✅ |
| 10 | Table | shadcn(MUI/Ant) | **実装**: シンプルなデータテーブル | ✅ |
| 11 | Statistic / Stat | Ant/Chakra | **実装**: ラベル+値+変化量の表示 | ✅ |
| 12 | Timeline | Ant/MUI(lab) | **実装**: 時系列の項目表示 | ✅ |
| 13 | Kbd | shadcn/Chakra | **実装**: キーボードキー表示（軽量） | ✅ |
| 14 | SpeedDial | MUI | **実装**: FAB を中心に展開するアクション群 | ✅ |
| 15 | Drawer / Sheet | shadcn/MUI/Ant | 対応済み: StylishModalNavigationDrawer 等 | — |
| 16 | Toggle Group | shadcn/MUI | 対応済み: StylishToggleButton / SegmentedButton | — |
| 17 | Select / Combobox | shadcn/MUI/Ant | 対応済み: StylishExposedDropdownMenu / SearchBar | — |
| 18 | Toast / Snackbar | shadcn/MUI | 対応済み: StylishSnackbar | — |
| 19 | ContextMenu | Radix/shadcn | 見送り: 右クリック検出が CMP のプラットフォーム差あり | 次期 |
| 20 | Command Palette | shadcn/cmdk | 見送り: SearchBar + List で構成可能 | 次期 |
| 21 | TransferList | MUI | 見送り: 複雑・利用頻度低 | 次期 |
| 22 | ScrollArea / Resizable / Masonry | Radix/MUI | 見送り: プラットフォーム固有のスクロールに依存 | 次期 |
| 23 | Autocomplete | MUI/Ant | 見送り: ExposedDropdownMenu + フィルタで構成可能 | 次期 |
| 24 | Alert / Result | Ant/MUI | 対応済み: StylishAlertDialog / StylishEmptyState | — |
| 25 | Label | shadcn/Radix | 見送り: Text で十分（KDoc で案内） | — |

## 第2ラウンド: Ant Design / Chakra UI 固有（#32 で実装済み）

| # | コンポーネント | Web 出典 | Stylish 判断 | 状態 |
|---|---|---|---|---|
| 1 | Alert（インライン通知） | Ant/Chakra/MUI | **実装**: StylishAlert（4 variant・閉じる・アクション） | ✅ |
| 2 | Message / Notification（トースト） | Ant/shadcn(Sonner)/Chakra | **実装**: StylishToast + StylishToastHost（キュー・自動消滅） | ✅ |
| 3 | Result（ステータスページ） | Ant | **実装**: StylishResult | ✅ |
| 4 | Popconfirm（インライン確認） | Ant | **実装**: StylishPopconfirm（StylishPopover ベース） | ✅ |
| 5 | Descriptions（キー・バリュー表示） | Ant | **実装**: StylishDescriptions | ✅ |
| 6 | AutoComplete | Ant/MUI | **実装**: StylishAutocomplete（フィールド+候補絞り込み） | ✅ |
| 7 | Code（インラインコード） | Chakra | **実装**: StylishCode | ✅ |
| 8 | VisuallyHidden | Chakra | **実装**: StylishVisuallyHidden（a11y） | ✅ |
| 9 | ContextMenu（右クリック） | Radix/shadcn | 見送り: 解決版 CMP に secondary ボタン検出 API なし（jar 実測） | 次期 |
| 10 | Tour / Tree / Transfer / Watermark | Ant | 見送り: 高複雑度・利用頻度低 | 次期 |
| 11 | Anchor / Splitter / Masonry / Calendar | Ant | 見送り: プラットフォーム固有・複雑 | 次期 |
| 12 | ColorPicker / QRCode / Upload | Ant | 見送り: 外部依存が必要 | 次期 |
| 13 | Tag | Ant | 対応済み: StylishChip / StylishBadge で代替 | — |
| 14 | FloatButton | Ant | 対応済み: StylishFab / StylishSpeedDial | — |
| 15 | Steps / Collapse / Stat / PinInput / Editable | Ant/Chakra | 対応済み: 第1ラウンドで実装 | — |

## 品質面の比較と対応

- **フォーカス可視化**（focus-visible リング）: 前回ラウンドで実装済み（stylishFocusRing）
- **VisuallyHidden**: スクリーンリーダー専用コンテンツの標準パターン（Chakra）を今回実装

## 第3ラウンド: Radix/shadcn(Ant) の追加パターン

| # | コンポーネント | Web 出典 | Stylish 判断 | 状態 |
|---|---|---|---|---|
| 1 | Command Palette（⌘K） | shadcn(cmdk)/Radix | **実装**: StylishCommandPalette（キーボードナビ付き） | ✅ |
| 2 | Splitter / Resizable | Ant/Radix | **実装**: StylishSplitter（ドラッグ分割パネル） | ✅ |
| 3 | Masonry | Ant/MUI | **実装**: StylishMasonry（カラム分配レイアウト） | ✅ |
| 4 | Field（フォームラッパー） | shadcn/Ant | **実装**: StylishFormField（ラベル+必須+エラー+ヘルプ） | ✅ |
| 5 | Avatar.Group | Ant/MUI | **実装**: StylishAvatarGroup（重ね表示） | ✅ |
| 6 | HoverCard | Radix/shadcn | **実装**: StylishHoverCard（ホバー情報カード） | ✅ |
| 7 | Menubar / NavigationMenu | Radix/MUI | 見送り: デスクトップ特化・複雑 | 次期 |
| 8 | TransferList / Tree / Tour | Ant | 見送り: 高複雑度 | 次期 |

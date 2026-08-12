# Web UI ライブラリ比較と実装判断（Web Parity Audit）

Web の著名 UI ライブラリ（shadcn/ui, Radix UI, Material UI, Ant Design,
Chakra UI）と Stylish UI を比較し、欠落している要素と実装判断を記録する。

調査日: 2026-08-13 / 対象: v0.8.0 時点の Stylish UI

## 前回分（ブランチ feat/web-parity にて実装済み・マージ待ち）

Accordion / Stepper / Breadcrumb / Popover / Pagination / Rating /
NumberInput / PinInput / Editable / Table / Statistic / Timeline / Kbd /
SpeedDial / focus-visible リング（stylishFocusRing）は前回ラウンドで実装し、
`feat/web-parity` ブランチとして PR 提出済み。

## 第2ラウンド: Ant Design / Chakra UI 固有の比較

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
| 15 | Steps / Collapse / Stat / PinInput / Editable | Ant/Chakra | 対応済み: 前回ラウンドで実装 | — |

## 品質面

- **フォーカス可視化**（focus-visible リング）: 前回ラウンドで実装済み（stylishFocusRing）
- **VisuallyHidden**: スクリーンリーダー専用コンテンツの標準パターン（Chakra）を今回実装

# Contributing to Stylish UI

Stylish UI への貢献ありがとうございます。

## 開発フロー

1. `main` ブランチから feature ブランチを作成
   ```bash
   git checkout -b feature/your-change
   ```

2. 変更を加える

3. Conventional Commits に従ってコミット

4. Pull Request を作成

5. CI が通過し、PR タイトルチェックを満たしたらマージ

## Conventional Commits

コミットメッセージ（Squash merge 時の PR タイトル）は以下の形式にしてください。

```
<type>(<scope>): <subject>
```

### type

| 値 | 用途 |
|---|---|
| `feat` | 新機能 |
| `fix` | バグ修正 |
| `docs` | ドキュメントのみの変更 |
| `style` | 空白、フォーマット、セミコロンなど |
| `refactor` | リファクタリング |
| `perf` | パフォーマンス改善 |
| `test` | テストの追加・修正 |
| `build` | ビルドシステムや依存関係 |
| `ci` | CI 設定 |
| `chore` | その他 |
| `revert` | 変更取り消し |

### 例

```
feat(components): add StylishConnectedButtonGrid
fix(foundation): correct corner calculation for incomplete rows
docs: update README usage example
```

## Pull Request

- `main` ブランチへの直接 push はできません
- Squash merge を使用してください
- PR タイトルは Conventional Commits 形式にしてください
- CI が全て通過する必要があります

## リリース

リリースは [Release Please](https://github.com/googleapis/release-please) によって自動化されています。

- `feat:` のコミット → minor バージョンアップ
- `fix:` のコミット → patch バージョンアップ
- 破壊的変更 → major バージョンアップ

Release Please が作成するリリース PR をマージすると、自動的に Git タグが作成され、Maven Central へ公開されます。

## バージョン関連ファイルの取り扱い

`version.properties` やリリース関連ファイルは、Release Please 以外が変更しないでください。

## 破壊的変更ポリシー

ユーザーのコードを壊す変更は、以下のルールに従って導入してください。

- **deprecate-first**: 破壊的変更はまず旧 API を非推奨（`@Deprecated`）にし、
  置き換え先を提供してから導入します。非推奨の通知には必ず移行先を明記してください。
- **削除タイミング**: 非推奨 API の削除は **2 回の minor リリース後、または次の
  major リリース** で行います。
- **1.0.0 より前**: 破壊的変更は minor リリースでも許可されますが、リリースノートに
  必ず明記し、可能な限り deprecate-first に従ってください。
- **1.0.0 以降**: [Semantic Versioning](https://semver.org/) を厳密に適用します。
  破壊的変更は major リリースでのみ許可されます。
- **例外**: バグ修正や内部 API の変更で、公開 API のシグネチャを変えないものは
  対象外です。

## コンポーネント仕様テンプレート

新規コンポーネント、または既存コンポーネントへの大きな変更を提案する場合は、
PR の説明に以下のチェックリストを記載してください。

### 基本

- [ ] 設計ガイドライン [DESIGN.md](DESIGN.md)（Clear / Simple / Modern）を満たしている
- [ ] Atomic Design のレイヤー判定（atoms / molecules / organisms / patterns）が正しい
- [ ] 3 層アーキテクチャ（Foundation / Structure / Finish）の配置が正しい。
      headless のレイアウトが必要なら Structure 層として分離し、Finish がそれを消費する

### 仕様（コンポーネントごとに記載）

- [ ] **役割**: このコンポーネントが解決する問題と、M3 のどのコンポーネントに対応するか
- [ ] **状態表**: 以下の状態ごとに見た目・挙動・セマンティクスを定義している
      - [ ] enabled / disabled
      - [ ] selected / unselected（選択状態を持つ場合）
      - [ ] focused / pressed / hovered
      - [ ] loading（該当する場合）
- [ ] **reduced-motion**: アニメーションはシステムの reduced-motion 設定を尊重する
- [ ] **RTL**: ミラーリングが必要な要素（アイコン、矢印、進捗など）を検討している
- [ ] **フォントスケール**: 200% スケールでレイアウトが破綻しない（固定高さの上限化など）
- [ ] **セマンティクス**: 操作可能要素の role、選択状態、無効状態、状態説明が適切
- [ ] **インタラクション**: interactionSource を外部に公開している（該当する場合）

### コード

- [ ] 公開 API に KDoc がある（パラメータの説明を含む）
- [ ] 同じファイルに `@Preview` がある
- [ ] パラメータ順序規約（content → modifier → style → behavior → slot）に従っている
- [ ] 既存テストにスモークテストを追加する（またはゴールデンテストのシーンに含める）
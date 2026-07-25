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

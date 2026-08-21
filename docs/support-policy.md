# Stylish UI サポート・ライフサイクル方針

この文書は「ビルドが通る」ことではなく、採用チームが導入後のリスク、問い合わせ先、
アップグレード経路を判断できるようにするための運用契約です。実際の応答時間を測定した
運用記録がまだない項目は、対応済みとは扱いません。

機械可読な正本は [`support-policy.json`](support-policy.json) です。`scripts/verify-support-policy.py`
は対象platform、latest/previous-minorのsupport window、S0〜S3の初回人手応答目標、
deprecation、incident必須項目、release evidenceをfail-closedで検査します。この検査は
「方針が明文化されている」ことだけを証明し、実際のon-call応答、SLA実績、rollback drillの
完了を証明しません。

## 対応範囲

| 項目 | 方針 |
|---|---|
| 対象 artifact | `io.github.segnities007:stylish-ui` |
| 対象プラットフォーム | Android、JVM Desktop、iOS（compileのみ・検証gateなし）。Web(Wasm)は2026-08-21に削除 |
| 対応バージョン | 最新の安定版を第一優先。pre-1.0 の旧 minor は原則サポート対象外 |
| API 契約 | 公開 Kotlin API は `apiCheck` と deprecate-first 方針で管理 |
| セキュリティ窓口 | GitHub Private Vulnerability Reporting（詳細は `SECURITY.md`） |
| 通常窓口 | GitHub Issues。再現可能な最小サンプルと platform/version を必須とする |

## 重大度と目標応答時間

目標時間は「最初の人間による受領確認」であり、修正リリースの保証ではありません。
実績は issue のラベル、担当者、受領時刻、解決時刻で記録します。

| 重大度 | 例 | 受領確認目標 | 対応方針 |
|---|---|---:|---|
| S0 | セキュリティ脆弱性、公開 artifact の破損、データ損失を誘発 | 1 business day | 公開を避けて security channel で triage。必要なら release を停止 |
| S1 | crash、入力不能、主要 A11y 回帰、互換性を壊す release | 2 business days | hotfix または rollback を優先。回避策を issue に記録 |
| S2 | 主要機能の不具合、顕著な性能/視覚回帰 | 5 business days | 次の patch/minor 候補として優先度と期限を合意 |
| S3 | 改善提案、軽微な表示差、ドキュメント不足 | 10 business days | roadmap と重複を確認し、採用可否を triage |

`business day` の実運用カレンダー、担当者、S0/S1の実績記録はリポジトリだけでは検証
できません。採用時は組織の on-call とこの表を接続してください。

## 互換性と deprecation

1. 破壊的変更には、旧 API の deprecation、移行先、release note、最低2回の minor 期間を
   設けます（pre-1.0 は例外を明記）。
2. 既存の `StylishTheme` と component の標準動作を変える場合、migration guide に before/after
   とロールバック方法を追加します。
3. API dump、target 別 compile、adapter contract、sample app の受入結果が揃わない変更は、
   release candidate に昇格させません。

## インシデントとロールバック

- 発見者は影響範囲、最初に壊れた version、platform、再現手順、回避策を incident issue に記録する。
- release owner は publish を止め、直前の既知の正常版、影響する API、利用者向け告知を紐付ける。
- 修正後は root cause、検知できなかった理由、回帰テスト、再発防止ゲートを postmortem に残す。
- 署名鍵、Maven Central 認証情報、利用者データを issue やログへ書き込まない。

現リポジトリには release automation と security reporting はありますが、実インシデント、
on-call、rollback drill の証跡は未取得です。したがって、この文書の存在だけでは運用成熟度を
100点とは判定しません。

## 採用チーム向けチェック

- [ ] 利用する version と platform matrix を固定した
- [ ] S0/S1 の連絡先と on-call owner を決めた
- [ ] deprecation とアップグレードの担当者を決めた
- [ ] release artifact の checksum、SBOM、license report を保存する場所を決めた
- [ ] minified Android sample とアプリ側の R8 検証を完了した
- [ ] Android TalkBack / iOS VoiceOver / Web screen reader の受入結果を保存した
- [ ] 直前 version への rollback を一度演習した

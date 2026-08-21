# SBOM・ライセンス・再現可能性ポリシー

GAFA級の採用判断では、テストが通るだけでなく、配布した artifact に何が含まれるかを release
ごとに追跡できなければなりません。本ポリシーは生成物の形式、許可ライセンス、例外、保存物を
固定します。生成・保存が未実行の release は「SBOM verified」と表示してはいけません。

## 必須 release evidence

各 version/tag について、同じ source revision と lockfile から次を保存します。

1. CycloneDX 1.5 または SPDX 2.3 の SBOM（runtime、compile、test を scope 付きで記録）。
2. 依存名、version、component type、package URL、license expression、SHA-256。
3. `LICENSE`、third-party notices、生成ツール version、JDK/Gradle/Kotlin version。
4. Maven POM、module metadata、各公開 target の checksum と provenance。
5. allowlist 判定結果、手動例外、reviewer、期限。期限なしの例外は許可しない。

推奨保存先は GitHub Actions の immutable artifact と公開 release asset です。ローカルの
`build/`だけに置かれたレポートは採用証跡とはみなしません。

## ライセンス判定

| 判定 | 既定ポリシー |
|---|---|
| 許可 | Apache-2.0、MIT、BSD-2-Clause、BSD-3-Clause、ISC、CC0（コード依存に限る） |
| 要レビュー | MPL-2.0、LGPL、CDDL、複数ライセンス expression、NOTICE義務がある依存 |
| 原則不許可 | GPL/AGPL、未知/欠落、利用条件が特定できない依存 |

これは法的助言ではありません。許可表に合致していても、商用配布の notice、特許、商標、
フォント・画像・サンプルデータの条件を別途確認します。

## 変更時の gate

- 依存追加・更新は SBOM diff と license diff を PR に添付する。
- 不許可または欠落 license は release を fail closed にする。
- 要レビュー依存は maintainer と法務/ライセンス責任者の記録が揃うまで publish しない。
- source tarball と binary artifact が同じ commit から生成されたことを provenance で確認する。
- checksum と署名検証を、取得側の clean checkout 手順でも再現する。

`./gradlew generateSbom` はGradleが実際に解決した全configurationからCycloneDX 1.5の
`sbom.json`、`third-party-notices.txt`、`checksums.txt`、`license-check.txt`を生成する。
`checksums.txt` は各バイナリの実 SHA-256 と Maven 座標/ファイル名を記録し、
`scripts/verify-release-evidence.py` が SBOM の `hashes` と一対一で照合する。
2026-08-21のLinux実行では613 component、SHA-256付きartifact、POMの親license継承を確認し、
600件がallowlist相当、15件がレビュー対象だった（内訳は次節の表）。従ってローカル生成物の
構造は検証済みだが、法務レビューとHosted CI immutable artifact保存が完了するまで
「SBOM verified」とは表示しない。

## レビュー対象 15 コンポーネントの判定記録（2026-08-21）

`build/reports/release/sbom.json`（components=613, allowed=598, review=15, missing=0,
`scripts/verify-sbom.py` PASS）に対する release-evidence 監査の結果。license 表記は SBOM 内の
POM 由来 metadata（`third-party-notices.txt` と一致）。「実質ライセンス」欄は公開情報に基づく
監査者の判定であり、maintainer / 法務の承認記録が揃うまで判定の確定とはしない。
重要な緩和事実: **15件すべてが `test` または `development` scope であり、runtime/compile
クラスパスに載らない**ため、公開ライブラリ artifact には配布されない。

| # | Component | POM license 表記 | scope | 実質ライセンス（監査判定） | 処分 |
|---|---|---|---|---|---|
| 1 | com.google.testing.platform:android-device-provider-local:0.0.9-alpha04 | Android Software Development Kit License Agreement | test (metadata-only) | Google SDK 専用ライセンス（独自） | 要レビュー維持（法務判断必要） |
| 2 | com.google.testing.platform:android-driver-instrumentation:0.0.9-alpha04 | 同上 | test (metadata-only) | 同上 | 要レビュー維持 |
| 3 | com.google.testing.platform:android-test-plugin:0.0.9-alpha04 | 同上 | test (metadata-only) | 同上 | 要レビュー維持 |
| 4 | com.google.testing.platform:core:0.0.9-alpha04 | 同上 | test (metadata-only) | 同上 | 要レビュー維持 |
| 5 | com.google.testing.platform:launcher:0.0.9-alpha04 | 同上 | test (metadata-only) | 同上 | 要レビュー維持 |
| 6 | com.sun.istack:istack-commons-runtime:3.0.8 | Eclipse Distribution License - v 1.0 | development | EDL 1.0 ＝ BSD-3-Clause 相当 | allowlist 適合候補・承認待ち |
| 7 | jakarta.activation:jakarta.activation-api:1.2.1 | EDL 1.0 | development | 同上 | allowlist 適合候補・承認待ち |
| 8 | jakarta.xml.bind:jakarta.xml.bind-api:2.3.2 | Eclipse Distribution License - v 1.0 | development | 同上 | allowlist 適合候補・承認待ち |
| 9 | org.glassfish.jaxb:jaxb-runtime:2.3.2 | Eclipse Distribution License - v 1.0 | development | 同上 | allowlist 適合候補・承認待ち |
| 10 | org.glassfish.jaxb:txw2:2.3.2 | Eclipse Distribution License - v 1.0 | development | 同上 | allowlist 適合候補・承認待ち |
| 11 | org.jvnet.staxex:stax-ex:1.8.1 | Eclipse Distribution License - v 1.0 | development | 同上 | allowlist 適合候補・承認待ち |
| 12 | javax.annotation:javax.annotation-api:1.3.2 | CDDL + GPLv2 with classpath exception | test (metadata-only) | CDDL 1.1 + GPL-2.0 w/ classpath exception のデュアル | 複数ライセンス式につき要レビュー維持 |
| 13 | junit:junit:4.13.2 | Eclipse Public License 1.0 | test | EPL-1.0（weak copyleft、テスト専用） | 要レビュー区分のまま（テスト依存として容認可否を記録） |
| 14 | net.java.dev.jna:jna:5.6.0 | LGPL, version 2.1; Apache License v2.0 | development | LGPL-2.1-or-later OR Apache-2.0 のデュアル | Apache-2.0 選択で許可可能・選択決定の記録待ち |
| 15 | net.java.dev.jna:jna-platform:5.6.0 | LGPL, version 2.1; Apache License v2.0 | development | 同上 | 同上 |

補足:

- EDL 1.0（Eclipse Distribution License）は Eclipse Foundation が公表する BSD-3-Clause 相当の
  ライセンスであり、本ポリシーの許可表（BSD-3-Clause）に適合すると判定できる。ただし判定者・
  日付・根拠URLを添えた maintainer 承認記録が存在しないため、本監査では status を反転しない。
- JNA 2件は Apache-2.0 オプションを選択すれば許可表に適合する。依存追加時に
  `Licensee`/manifest での選択明示など「Apache-2.0 を選択した」記録を残すことが条件。
- com.google.testing.platform 5件は alpha 版の Android instrumented-test 基盤で、
  Gradle 解決上 metadata-only（binary hash なし）。published artifact への混入経路はないが、
  独自ライセンスのためポリシー上は法務レビュー必須。
- 上記の承認が完了しても、全体 `status=VERIFIED` への反転は
  `python3 scripts/verify-sbom.py --require-clean` が review/missing 0 を確認して初めて行う。

## 生成手順（採用環境で固定する）

組織の標準 CycloneDX/SPDX Gradle plugin を選定し、次の入力を immutable な CI artifact として
保存してください。ここでは特定 plugin の導入を未検証のまま強制しません。

```text
source revision = exact release tag
dependency resolution = lock/verification metadata enabled
report format = SPDX 2.3 or CycloneDX 1.5 JSON
license policy = docs/sbom-license-policy.md
outputs = sbom.json, third-party-notices.txt, license-check.txt, checksums.txt
```

CIの通常ビルドは `python3 scripts/verify-sbom.py` で構造・重複・SHA-256・license欠落を検査し、
続けて `python3 scripts/verify-release-evidence.py --write-manifest` で通知・件数・checksumを
突合した `evidence-manifest.json` を保存する。release workflowは `--require-clean` を指定して
レビュー対象が残る限り公開を停止する。

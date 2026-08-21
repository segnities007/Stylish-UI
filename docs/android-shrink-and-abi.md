# Android shrink・consumer rules・ABI policy

Stylish UI は library として利用されるため、利用側アプリの R8 設定を壊さず、不要な keep
で縮小率を落とさないことが重要です。公開 API を理由なく全 keep する設計は採用しません。

## ルール

1. library は `proguard/stylish-ui-consumer-rules.pro` を consumer rules として公開する。
2. consumer rules は annotation/metadata など、反射・コード生成に必要な最小集合に限定する。
3. 利用側 sample は release/minified、resource shrinking、baseline profile の有無を分けて検証する。
4. `Missing class`、Compose compiler metadata、serialization/reflective adapter の warning は、
   ignore ではなく依存の ownership と rule の根拠を記録する。
5. ルール変更時は before/after の APK/AAB size、startup、mapping、runtime smoke を保存する。

## ABI

JVM の `apiCheck` だけでは Android metadata、iOS framework、Wasm export の互換性を証明しません。
release candidate では少なくとも次を記録します。

| target | 必須検証 | 現 repository の状態 |
|---|---|---|
| JVM/common | API dump/check、deprecation、migration | JVM API gateあり |
| Android | minified host app、consumer rules、runtime smoke | `samples/android-r8` のLinux release/minify/resource-shrink、mapping/seeds/usage証跡と、API 35 emulatorの `build/reports/android-runtime/` UIAutomator/PNG/fingerprint/manifestを確認。TalkBack/Dynamic Type/OEM/SLOは未確認 |
| iOS | arm64/simulator compile、公開 framework/KLib diff | arm64/simulator KLibを`klib dump-abi`し、1,769 declaration linesのtarget snapshotとSHA-256をLinuxで生成。macOS CIで同じartifactを保存するが、framework binary diffとruntimeは未確認 |
| Wasm | browser test、public export/bundle budget | browser/budget gateあり |

破壊的変更は `CONTRIBUTING.md` の deprecate-first、migration guide、API diff、release noteを
同じ PR で更新します。target別 ABI の証跡が欠けた場合、その target を「supported」ではなく
「compile checked」へ降格表示します。

## Consumer rules file

根拠のない広域 keep を避けるため、現時点のルールは metadata 保持に限定しています。Compose
や Kotlin の runtime rules はアプリ側/依存側の責務であり、この library から再定義しません。
変更する場合は issue に reflective entry point と再現テストを記録してください。

## Linux R8 acceptance sample

`./gradlew :samples:android-r8:assembleRelease --no-daemon` は、Javaの最小consumer appに
Stylish UIを依存させ、`isMinifyEnabled=true` と `isShrinkResources=true`、公開consumer rulesを
適用する。`python3 scripts/verify-android-r8.py` はAPKのZIP/DEX/Manifest/resources、SHA-256、
`mapping.txt`、`configuration.txt`、`seeds.txt`、`usage.txt`を
`build/reports/release/android-r8.json`へ記録する。2026-08-21のLinux実行では294,816 bytes、
1 dex、R8 PASSだった。これはリンク・縮小の証跡であり、Android実機の起動、TalkBack、startup
SLOを証明しない。API 35 emulatorのUI smoke計測は2026-08-21に削除した（履歴はgit参照）。

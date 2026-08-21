# Wasm browser acceptance contract

Status: UI_E2E_WORKFLOW_IMPLEMENTED_CI_PENDING

This contract separates three kinds of evidence:

1. **Browser pipeline** — `wasmJsBrowserTest` is scheduled with Chrome/Xvfb,
   `assembleWasmProductionSite` verifies the deployable resource set, and the
   production bundle has a size budget.
2. **Browser-executed shared logic** —
   `src/wasmJsTest/kotlin/com/segnities007/stylishui/WasmBrowserQualitySmokeTest.kt`
   executes the deterministic DataTable query pipeline on the Wasm test target.
3. **UI accessibility workflow** — a rendered Compose/Wasm screen is opened and
   a user flow is driven through Chrome's accessibility tree and DevTools input
   domain, with a retained JSON log and PNG artifact.

The repository has evidence for (1) and (2), plus the executable
`scripts/wasm-ui-e2e.mjs` workflow. It locates Compose controls by accessibility
role/name, verifies catalog load (92), category filtering (14), Card search (1),
theme-label changes, and a keyboard Tab path, then writes
`wasm-ui-e2e.json` and `wasm-ui-e2e.png`. The same workflow passed locally on
2026-08-21 against the packaged site with zero console errors. The CI job fixes
a 1440×1000 viewport, device scale 1, and a `ja-JP` locale override request;
the artifact records the actual browser locale as well. It uploads the artifacts.
A successful hosted CI run is still required
before changing the status to fully verified.

On 2026-08-21, `:website-wasm:wasmJsBrowserProductionWebpack` completed on Linux.
The generated production assets measured 528 KiB JavaScript and 6,276 KiB Kotlin Wasm
(`stylishUiWebsite.wasm`), within the configured 700 KiB / 10,000 KiB budgets. These are
local bundle measurements, not a retained CI history or a UI/DOM workflow artifact.

The deployable handoff is produced by `:website-wasm:assembleWasmProductionSite`.
It packages `index.html`, the webpack JS/Wasm assets, and processed Compose resources
(including the bundled font) under `website-wasm/build/wasmSite`. Serving only the
raw JS directory is unsupported because it omits runtime resources.

The CI budget step writes `website-wasm/build/ci-evidence/wasm-bundle-size.txt` and
uploads it as `wasm-bundle-evidence` per run. The UI workflow uploads
`wasm-ui-e2e.json` and `wasm-ui-e2e.png` as `wasm-ui-e2e-evidence`. These provide
reviewable per-run evidence; they are not yet a baseline/diff history and do not
replace native screen-reader testing.

Run the Linux-friendly static contract without Gradle:

```bash
python3 scripts/verify-wasm-browser-contract.py
```

The check verifies that the CI recipe, browser smoke source, UI workflow source,
bundle budgets, artifact paths, and public evidence wording agree.

When a per-run artifact exists, validate its schema and required steps with:

```bash
python3 scripts/verify-wasm-ui-e2e-artifact.py --require
```

## To complete the hosted browser evidence gate

The following acceptance conditions must all be met before changing the
status to `UI_E2E_VERIFIED`:

- Run `scripts/wasm-ui-e2e.mjs` successfully in hosted CI against the packaged
  `StylishPlayground` site.
- Assert visible state changes and at least one accessibility-facing label or
  state, not only a Kotlin transformation result.
- Retain a CI artifact containing the test log and a screenshot or DOM
  snapshot, reviewable from the pull request.
- Keep deterministic viewport/locale configuration and document the host-locale/font
  limitation. Do not use the presence of `wasmJsBrowserTest` alone as proof.

Until then, the honest acceptance result is **workflow implemented, hosted CI
verification pending**. This is an explicit release gate, not a substitute for
TalkBack/VoiceOver, Dynamic Type, or native device acceptance.

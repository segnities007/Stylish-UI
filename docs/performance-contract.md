# Performance and reliability contract

This contract separates deterministic work bounds from platform measurements. It prevents a green
unit test from being misreported as a frame-time or memory guarantee. The Linux algorithmic smoke is
not a frame-time or heap SLO.

## Deterministic contracts implemented in common code

| Workload | Contract | Evidence |
|---|---|---|
| DataTable | `pageSize` bounds the number of rows handed to the table layout; `rowKey` is required by the renderer; equal sort keys retain input order. | `StylishDataTableEngineTest` |
| Tree | The flattened result contains only expanded branches and is consumed by `LazyColumn`. Flattening uses an explicit stack, so deep user data does not consume the call stack; a 100,000-sibling fixture verifies bounded flattening for large collapsed collections. | `StylishTreeEngineTest` |
| Canvas charts | Line, area, and scatter render at most `StylishChartMaxRenderedPoints` (500) points per frame. Downsampling is deterministic and preserves the first/last source points, including a 100,000-point fixture. Accessibility text retains the original finite samples through the shared pure `buildStylishChartDescription` contract. | `ChartMathTest`, `ChartAreaScatterSemanticsSmokeTest`, chart implementations |
| Invalid numeric input | NaN, infinities, negative bar values, and degenerate ranges have deterministic finite rendering rules. | `ChartMathTest` |
| Linux algorithmic smoke | 10k DataTable sort, 100k Tree flatten, and 100k chart downsample run under broad deterministic smoke budgets (5,000/5,000/2,000 ms). With `WRITE_PERFORMANCE_REPORT=1`, measured values are emitted as JSON. | `src/jvmTest/kotlin/com/segnities007/stylishui/performance/PerformanceBudgetTest.kt`, `build/reports/performance/algorithmic-budgets.json`, CI `algorithmic-performance-evidence` artifact |
| Android runtime performance proxy | API 35 emulator proxy with two explicitly separated measurement windows. (1) Cold-process startup: one unrecorded warmup launch after fresh install absorbs install-time dexopt/verification, then 5 `am force-stop` + `am start -W` `TotalTime` samples; with 5 samples the nearest-rank p95 equals the worst start, and the warmup value is retained in the report. (2) Post-warmup frame window: `dumpsys gfxinfo` counters are reset twice around a settle interval so startup frames are excluded, then one `KEYCODE_TAB` interaction window is sampled through machine-readable `FrameCompleted − IntendedVsync` durations (human-readable percentile is a compatibility fallback). Raw per-start and per-frame samples stay in `performance.json` next to derived min/median/max, deadline-miss counts against the budget, and a small-sample caveat: an idle window can hold fewer than 30 frames, where nearest-rank p95 approximates the worst single frame instead of a stable distribution estimate. Budgets are unchanged (startup p95 ≤ 2,000 ms, frame-proxy p95 ≤ 32 ms); the report records `environment.buildType=debug` and `scopeGuard.emulatorOnly=true`, so an emulator FAIL is a proxy verdict, never a production-device SLO claim — Macrobenchmark/OEM runs remain required before any device claim. | `scripts/verify-android-performance.py`, `scripts/verify-android-runtime.sh`, `build/reports/android-runtime/performance.json` |

The 500-point chart limit is an allocation/drawing bound, not a frame-time SLO. Consumers that need
every sample for inspection must retain the source data and provide a separate inspection/selection
path; the visual renderer must not be used as the data store.

## Reproducible report protocol

`PerformanceBudgetTest` emits `build/reports/performance/algorithmic-budgets.json` when
`WRITE_PERFORMANCE_REPORT=1`. The report is a versioned, machine-checkable algorithmic baseline
artifact, not a hand-copied timing note:

- `schemaVersion=1` identifies the report shape; `protocol.warmupIterations=2` and
  `protocol.measurementIterations=7` make the sampling procedure explicit.
- Each workload contains all raw `samplesMillis`, `minMillis`, `medianMillis`, `p95Millis`, its
  budget, and a `PASS` status. The gate is explicitly `p95Millis <= budgetMillis`.
- `environment` records Java version, operating system, architecture, and source revision so a
  review can distinguish a local artifact from a Hosted CI artifact.
- `scripts/verify-performance-report.py` validates the schema, required workload set, summary
  statistics, and budget rule. Missing or extra workloads fail validation rather than becoming an
  incomplete green report.

This is a reproducible Linux/JVM protocol and a budget baseline for algorithmic work. It is not a
cross-machine trend database: timing comparisons are meaningful only when runner/toolchain and
fixture are held constant. No field in this report certifies frame time, startup, heap, or
recomposition. Those platform measurements remain required below.

## Measurements still required before adoption claims

The repository does not claim a frame-time, startup, memory, or recomposition budget from these
algorithmic smoke tests. Those require platform instrumentation with a fixed device/browser matrix and a repeatable
fixture. The required next evidence is:

- 10,000-row DataTable: first composition, update, scroll p95 frame time, heap, and recomposition count.
- 100,000-node Tree: expansion and scroll p95 frame time, heap, and focus restoration.
- Multi-series charts: render/update p95 frame time, path allocation, heap, and bundle size.
- Android, iOS, and Desktop runs with recorded toolchain/device versions.

The Android runtime job now records a real API 35 emulator startup/frame proxy when the emulator
is online. `status=PASS` is reserved for a measured `am start -W` p95 and a measured `gfxinfo`
frame percentile inside the explicit budgets; missing fields are `UNMEASURED`, never a green
fallback. `frameTimeProxy` is intentionally not a production-device SLO: Android Macrobenchmark,
OEM matrix, and long-running scroll/interaction traces remain required for adoption.

The 2026-08-21 local API 35 emulator run measured startup p95 ≈ 2,449 ms (budget 2,000 ms) and
frame-proxy p95 ≈ 300 ms (budget 32 ms): FAIL, retained verbatim. Root-cause reading of the frame
number: the post-settle window on an idle paginated table screen contains very few frames, so the
nearest-rank p95 approximates the worst single frame of a debug/JIT emulator build (no R8, no
baseline profile), where one focus-traversal or idle-wake frame — including goldfish vsync
scheduling latency already documented in the collector's parser — can land near 300 ms. The
budgets are deliberately NOT relaxed to absorb emulator overhead: keeping 32 ms preserves
comparability across runs and leaves production frame SLOs owned by Macrobenchmark. The value
must be re-measured under the separated protocol above, not averaged away or reclassified.

Until those measurements are stored and gated, the performance dimension remains partial in the
GAFA adoption score even though the common deterministic contracts are complete.

## Compose compiler stability proxy

`python3 scripts/verify-compose-metrics.py` converts the JVM-main Compose compiler metrics into a
reviewable JSON artifact. It gates Strong Skipping, at least 80% skippable composables, at most 110
unknown and 100 known-unstable arguments, and at least 90% effectively stable classes. The 2026-08-21
Linux run measured 81.5% skippable composables, 95.2% effectively stable classes, and 106 unknown
arguments after adding the generic headless Tree/Transfer/Chart overloads. The 110 threshold is a
bounded regression budget, not an assertion that generic collection arguments are device-stable. This is a
source/compiler stability signal only; it is explicitly not a frame-time, heap, recomposition-count,
startup, or device SLO.

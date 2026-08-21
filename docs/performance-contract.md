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

Android emulator runtime/performance measurement tooling was removed on 2026-08-21 together with
the hosted emulator job (no current demand). The last recorded local run measured startup p95 ≈
2,449 ms and frame-proxy p95 ≈ 300 ms against budgets of 2,000 ms / 32 ms: FAIL. That history
stays in git if device-level measurement is ever revisited; until then no startup or frame SLO is
claimed.

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

# Module and source boundaries

Stylish-UI keeps the stable styled publication in one multiplatform library
module (`:`), but the first visual-completeness layers are now physically
extracted: a Compose-free `:foundation` contract module and a headless
`:structure` arrangement module. The catalog, desktop host, Wasm host, and
Android consumer samples remain separate Gradle modules. This is an intentional
packaging boundary: the published artifact must never acquire a dependency on a
demo or host application, while headless contracts can be adopted without
pulling Compose.

The permitted physical module graph is:

```text
library (:) ───► (compatibility copies; no runtime sibling edges)
catalog ───────► library (:)
website ───────► catalog ─────► library (:)
website-wasm ──► catalog ─────► library (:)
android-r8 ────► library (:)
android-runtime ► library (:)
foundation-consumer ───────────► foundation
structure-consumer ────────────► structure
migration-consumer ─► foundation + structure   (two-artifact adoption canary)
adapters ───────► (may adopt :foundation; currently zero project edges)
```

There are no reverse edges and no host-to-host edges. The catalog is allowed to
expose the library API because its public demo contracts contain Stylish
component types; the website hosts consume the catalog as an application
surface. Consumer samples depend directly on the published-library boundary so
R8 and runtime acceptance cannot accidentally pass through the catalog.

The first Compose headless Structure slice is now a physical module. Its
consumer can be adopted without the styled root:

```text
structure-consumer ───► structure ───► Compose layout primitives
```

`samples:migration-consumer` compiles against BOTH extracted artifacts at once
(`:foundation` reducer/viewport contracts + `:structure` slot layouts) without
touching the styled root. It is the compile-time proof that stage 3 below will
not strand downstream adopters.

The existing connected-card/list implementations remain in the root artifact
as a source-compatible migration lane. They are not duplicated into the new
module beyond the documented compatibility copies, which avoids unexpected
duplicate classes and ABI drift. The next migration can move one connected
family at a time behind the same contracts.

## Dependency-direction policy

Direction flows one way, from consumers down to Foundation. Every declared
Gradle project edge must point strictly downward in this table;
`scripts/verify-module-boundaries.py` enforces both the allowlist and the
ranking, plus required canary edges so a consumer cannot silently drop its
dependency on an extracted module.

| Rank | Module | May depend on | Enforced canary |
|---|---|---|---|
| 5 | `samples:*` (adapters, android-r8, android-runtime, foundation-consumer, migration-consumer, structure-consumer) | any lower physical module, per allowlist | each sample keeps its required edge(s); nothing may depend on a sample |
| 4 | `website`, `website-wasm` | `:catalog` only | required `:catalog` edge |
| 3 | `catalog` | `:` (components/root publication) | required `:` edge |
| 2 | root library `:` | **no sibling edges** while compatibility copies exist | zero-edge assertion |
| 1 | `:structure` | `:foundation` (currently unused), Compose layout primitives | import guard stays headless/style-free |
| 0 | `:foundation` | nothing (framework-neutral) | framework-neutrality import guard |

Additional executable rules:

- **Duplicate-package guard (D8 hazard):** if two modules own the same Kotlin
  package, no module may declare project edges to both, and the pair must not
  be directly connected. Today the only shared packages are the intentional
  root↔`:foundation` and root↔`:structure` binary-compatibility copies, which
  are safe precisely because the root has zero sibling edges.
- **Required edges:** catalog→`:`, website/website-wasm→`:catalog`,
  android-r8/android-runtime→`:`, foundation-consumer→`:foundation`,
  structure-consumer→`:structure`, migration-consumer→`:foundation`+`:structure`.
- **Adapter direction:** `samples/adapters` may adopt `:foundation`
  (adapter→foundation is a legal downward edge); it must never gain an edge to
  `:structure`, `:`, or any host.

## Physical split decision: Structure/Components out of the root

**Decision: do NOT physically extract the styled `components/` tree now.
Option (a) — the root remains the sole host of `components/*` packages — is
kept for this release cycle.** The headless leaves (`:foundation`,
first `:structure` slice) are the only code that moves ahead of the major
version bump.

Options evaluated:

1. **(a) Root remains sole host; `structure/` stays source-set organization.**
   ✅ Zero D8 duplicate-class risk, zero published-coordinate churn, no Apple
   host validation needed. ❌ The physical boundary for styled components stays
   virtual (guarded by `verify-architecture.sh` instead of Gradle).
2. **(b) Move packages to new coordinates + deprecated typealiases in root.**
   Rejected for now: `@Composable` functions with default arguments compile to
   synthetic `DefaultArgs` masks and overload groups that typealiases cannot
   fully re-expose; expect/actual and internal contracts do not alias at all.
   Moving ~200 component files would rewrite source-set publication, target
   variants, and every consumer import in one change, with no macOS/iOS
   validation environment available in this lane to prove the KLib ABI story.
3. **(c) Consumer substitution** (Gradle dependency substitution /
   relocation POMs). Rejected: hides the real migration from adopters, does not
   fix published coordinates, and breaks reproducible builds for anyone not
   using the substitution.

The blocking constraint for any same-package split is Android D8: if the root
keeps publishing `com.segnities007.stylishui.components.*` while a new module
hosts the same packages, every consumer that puts both artifacts on one
classpath fails the build with duplicate classes. The current design avoids
this by pairing every extraction with (i) compatibility copies left in the
root, (ii) **zero runtime sibling edges** out of the root, and (iii) the
duplicate-package guard above, which fails the moment anyone wires both
same-package artifacts together.

### Staged migration plan

- **Stage 1 — done (this cycle).** Extract framework-neutral leaves:
  `:foundation` (headless reducer/layout/renderer contracts) and the first
  `:structure` slice (`StylishSlotRow/Column/Grid`). Root keeps
  binary-compatibility copies and no sibling edges. Consumers prove adoption:
  `samples:foundation-consumer`, `samples:structure-consumer`,
  `samples:migration-consumer`. Per-module ABI snapshots wired (see below).
- **Stage 2 — next minor cycles.** Move remaining Structure families one at a
  time (connected card/list layout engines first) behind the same contracts,
  updating this document, the allowlisted graph, and per-module API dumps in
  the same change. Grow consumer samples before each move; keep the
  duplicate-package guard green by never adding root sibling edges.
- **Stage 3 — next major release.** Root stops shipping the duplicated
  headless packages, gains `api(project(":foundation"))` /
  `api(project(":structure"))` edges, ships deprecated forwarding shims only
  where a typealias is lossless, and bumps the major version. Only after this
  can a physical `components` module be considered, following the same
  copy → canary → cut-over sequence.

Inside the published root `:`, the remaining Structure and Finish layers are
still virtual:

```text
Finish (components) → Structure → Foundation (:foundation contract + : foundation internals)
                                  Models are data contracts
```

The cross-platform portion of that boundary is executable as a Compose-free
headless contract. `foundation.headless.StylishReducer` owns pure state
transitions, `StylishLayoutEngine` produces deterministic geometry and
semantics, and `StylishRenderPlan` is consumed by a host renderer. The canonical
contract is owned by `:foundation`; the root library keeps a binary-compatible
copy without a runtime sibling edge, avoiding duplicate classes while existing
imports remain source-compatible. `:samples:foundation-consumer` compiles and
tests a direct, Compose-free consumer. See `docs/headless-architecture.md` and
the sample; the two guards prevent the contract from importing a platform or
rendered component layer.

## Executable guards

`scripts/verify-architecture.sh` enforces Atomic Design and Finish/Structure/
Foundation import direction. `scripts/verify-module-boundaries.py` complements
it by checking all of the following on Linux without a network or Gradle:

- the settings project set and the allowlisted Gradle project dependency graph;
- required canary edges per consumer module (a dropped extraction dependency
  fails the gate);
- the layered direction matrix: every declared edge must point strictly
  downward (consumers → hosts → catalog → root → structure → foundation);
- the duplicate-package guard: same-package modules must never be co-consumed
  or directly connected (Android D8 duplicate-class hazard);
- the published root module has no runtime sibling dependency while its
  binary-compatibility copies remain. This avoids duplicate Android/Wasm/JVM
  classes; the direct `:foundation` artifact is consumed by the migration
  samples and can replace the copy in a future major release;
- `:structure` contains only headless Compose arrangement and slot contracts;
  it cannot import the root's components, theme, tokens, catalog, or host
  packages. `:samples:structure-consumer` verifies direct adoption;
- `:foundation` remains framework-neutral and lower than the root styled module;
- `:samples:migration-consumer` imports only the two extracted artifacts and
  never the rendered component, theme, token, or catalog packages;
- every Kotlin file has a package matching its source path;
- the library cannot reference catalog, website, or sample packages;
- catalog and host modules cannot leak host packages into one another. The
  catalog package intentionally uses the `com.segnities007.stylishui.catalog`
  namespace for ergonomic imports, but it remains outside the root module's
  source set and is never part of the published library artifact.

Both checks run from the root `check` task. A future extraction into physical
`components` and additional Structure families must first update this
document, the allowlisted graph, and per-module API dumps in the same change.
The new Structure slice is intentionally small and mechanically reviewable:
moving every existing Compose-heavy source in one change would alter
source-set publication, target variants, and ABI without an Apple/host
validation environment. The contract/sample/edge guards make the next
extraction incremental rather than leaving the boundary as documentation only.

## Per-module ABI snapshots

The root artifact is guarded by `api/jvm/Stylish-UI.api` via
binary-compatibility-validator. The extracted modules now expose the same
tasks (`:foundation:apiCheck` / `:foundation:apiDump`,
`:structure:apiCheck` / `:structure:apiDump`; applied centrally from the root
build file so plugin policy lives in one place).

Status: **wired, snapshots pending.** Until the first dumps are generated and
committed, both modules are listed in the root `apiValidation.ignoredProjects`
to keep aggregate gates green. Orchestrator follow-up, in order:

```bash
GRADLE_USER_HOME=$PWD/.gradle-local ./gradlew :foundation:apiDump --no-daemon --max-workers=1
GRADLE_USER_HOME=$PWD/.gradle-local ./gradlew :structure:apiDump --no-daemon --max-workers=1
```

This creates `foundation/api/jvm/foundation.api` and
`structure/api/jvm/structure.api`. Commit those snapshots, then remove the two
entries from `ignoredProjects` in the root `build.gradle.kts` so the aggregated
root `apiCheck` enforces both modules from then on.

# Visual regression matrix

Stylish-UI's visual contract is larger than the two theme screenshots. The
JVM golden harness declares a deterministic matrix of 96 scenes:

`2 themes × 2 layout directions × 2 widths × 2 font scales × 6 content states`

| Axis | Values | Why it is in the release matrix |
|---|---|---|
| Theme | light, dark | Surface/content contrast and token derivation |
| Direction | LTR, RTL | Logical padding, icon order, navigation and focus geometry |
| Width | 393dp, 320dp | Normal mobile width and narrow/compact wrapping |
| Font scale | 100%, 200% | Large text reflow and interaction-target preservation |
| Content state | default, disabled, loading, error, empty, long text | The states users actually encounter, not only the happy path |

`src/jvmTest/kotlin/com/segnities007/stylishui/visual/GoldenTest.kt` renders
each scene with fixed density, fixed dimensions, no animations and stable
strings. A first local run records the missing files below
`src/jvmTest/resources/golden/matrix/`; later runs compare pixels with the
same antialiasing tolerance as the existing light/dark baselines.

`src/jvmTest/kotlin/com/segnities007/stylishui/visual/VisualRegressionMatrixTest.kt`
adds a smaller 32-case Linux smoke matrix (light/dark × high-contrast on/off × LTR/RTL × 100/200%
font scale at 320dp) that always checks rendered dimensions, non-empty pixel
coverage, and color diversity while exercising long text plus
default/loading/disabled/error/empty states. It can emit non-baseline PNGs to
`build/reports/visual-matrix/` with `WRITE_VISUAL_MATRIX=1`. The test is part of
the JVM gate; the CI Build job enables the flag and uploads `visual-matrix-evidence`.
PNG output remains separate from a pinned cross-machine baseline.

The current repository intentionally does not claim that these PNGs are a
cross-machine pixel baseline. Host font availability affects Japanese glyph
rasterization, so CI retains the structure PNGs but skips pixel comparison until
a pinned rendering image is provided. The matrix contract itself is still
checked by `visualRegressionMatrixContractIsComplete`, and
`scripts/verify-visual-matrix.sh` is a Linux-friendly static guard that can
run without Gradle.

This distinction is important: a complete scenario list is evidence of test
coverage, not evidence of VoiceOver/TalkBack, browser DOM, or native-device
rendering. Those platform runs remain separate adoption gates.

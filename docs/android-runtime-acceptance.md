# Android runtime acceptance

`scripts/verify-android-runtime.sh` is the Linux/CI smoke gate for a real Android
Compose host. It builds the `samples/android-runtime` consumer, installs it on an
API 35 emulator, launches it, and records the Android accessibility tree,
post-Tab accessibility tree, screenshot, device fingerprint, and a JSON manifest under
`build/reports/android-runtime/`.

The same online-emulator run invokes `scripts/verify-android-performance.py`.
It captures five `am start -W` startup samples and a post-focus
`dumpsys gfxinfo <package> framestats` frame-statistics snapshot in
`build/reports/android-runtime/performance.json`. A measured `PASS` requires
all startup samples, a returned frame sample/percentile, startup p95 <= 2,000 ms,
and frame-proxy p95 <= 32 ms. The collector waits for the initial composition
to settle, resets the counters again, and measures the explicit post-Tab window;
it prefers the machine-readable `FrameCompleted - IntendedVsync` nearest-rank
p95 and falls back to the human-readable percentile only when `PROFILEDATA` is
not available. If adb or a required platform field is missing, the report is
`UNMEASURED`; it is never converted to a successful SLO.

Run locally with an already booted emulator:

```bash
ANDROID_SDK_ROOT=/home/segnities007/Android/Sdk \
ANDROID_SERIAL=emulator-5556 \
bash scripts/verify-android-runtime.sh
```

When `ANDROID_SERIAL` is omitted, the script selects the conventional
`emulator-5554` when online and otherwise the first online emulator. It removes
only the smoke-test package before installation so stale debug data cannot
exhaust the emulator's internal app volume.

The gate asserts the user-visible title/state text and the `ID`/`Name` row
semantics. Compose `testTag` values are not required because they are not
guaranteed to be exported to UIAutomator. The script waits for the first Compose
measure/layout pass so a cold emulator does not produce a false negative. It
then dispatches one platform `KEYCODE_TAB` event and requires at least one
focusable node in `ui-after-tab.xml`; this proves an input/focusability path is
present without claiming a particular TalkBack announcement or OEM traversal
order.

This is evidence of Android installation, launch, Compose measurement, and
accessibility-tree exposure. The performance report is an emulator frame proxy,
not a production-device SLO. It does not certify TalkBack speech, Dynamic
Type/font-scale parity, OEM rendering, memory/scroll traces, release signing, or
published Android ABI compatibility. Android Macrobenchmark, an OEM/device
matrix, and Hosted CI retention remain explicit adoption gates.

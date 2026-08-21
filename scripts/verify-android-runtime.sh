#!/usr/bin/env bash
set -euo pipefail

# Optional Android emulator smoke. It proves that a consumer APK launches and
# exposes its primary UI nodes through Android's accessibility tree. It does not
# certify TalkBack speech, Dynamic Type, OEM rendering, or production startup SLO.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

ADB="${ADB:-${ANDROID_SDK_ROOT:-/home/segnities007/Android/Sdk}/platform-tools/adb}"
EMULATOR_SERIAL="${ANDROID_SERIAL:-emulator-5554}"
APK="samples/android-runtime/build/outputs/apk/debug/android-runtime-debug.apk"
REPORT_DIR="${ANDROID_RUNTIME_REPORT_DIR:-build/reports/android-runtime}"
PACKAGE="com.segnities007.stylishui.androidruntime"
UI_XML_AFTER_TAB="$REPORT_DIR/ui-after-tab.xml"
PERFORMANCE_REPORT="$REPORT_DIR/performance.json"

[[ -x "$ADB" ]] || { echo "Android runtime: SKIP (adb not found: $ADB)"; exit 0; }
"$ADB" -s "$EMULATOR_SERIAL" get-state >/dev/null 2>&1 || {
  if [[ -n "${ANDROID_SERIAL:-}" ]]; then
    echo "Android runtime: SKIP (device $EMULATOR_SERIAL is not online)"
    exit 0
  fi
  # CI and local hosts can expose a different emulator index. Prefer the
  # conventional 5554 device, then select the first online emulator instead
  # of silently skipping a valid runtime gate.
  fallback_serial="$($ADB devices | awk '$1 ~ /^emulator-/ && $2 == "device" { print $1; exit }')"
  [[ -n "$fallback_serial" ]] || {
    echo "Android runtime: SKIP (no online emulator device)"
    exit 0
  }
  EMULATOR_SERIAL="$fallback_serial"
}

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$ROOT/.gradle-local}" ./gradlew :samples:android-runtime:assembleDebug --no-daemon >/dev/null
mkdir -p "$REPORT_DIR"
# This is an isolated smoke-test package. Removing only this package avoids a
# stale debug install exhausting the emulator's internal app volume while
# preserving all other emulator state.
"$ADB" -s "$EMULATOR_SERIAL" shell pm uninstall "$PACKAGE" >/dev/null 2>&1 || true
"$ADB" -s "$EMULATOR_SERIAL" install -r "$APK" >/dev/null
"$ADB" -s "$EMULATOR_SERIAL" shell am force-stop "$PACKAGE"
"$ADB" -s "$EMULATOR_SERIAL" shell monkey -p "$PACKAGE" 1 >/dev/null
ui_ready=false
for _ in $(seq 1 20); do
  # Compose may keep the accessibility bridge empty for a few frames while
  # the first measure/layout pass is running on a cold emulator, and a failed
  # dump leaves the PREVIOUS XML on the device, so each iteration validates
  # the freshly dumped file before it replaces the recorded one.
  if "$ADB" -s "$EMULATOR_SERIAL" shell uiautomator dump /sdcard/stylish-runtime.xml >/dev/null 2>&1; then
    "$ADB" -s "$EMULATOR_SERIAL" shell cat /sdcard/stylish-runtime.xml > "$REPORT_DIR/ui.xml.next" 2>/dev/null || true
    if grep -F -q '<hierarchy' "$REPORT_DIR/ui.xml.next"; then
      mv "$REPORT_DIR/ui.xml.next" "$REPORT_DIR/ui.xml"
    fi
    rm -f "$REPORT_DIR/ui.xml.next"
  fi
  if grep -F -q 'Stylish UI Android runtime' "$REPORT_DIR/ui.xml" \
    && grep -F -q 'state page=1 size=10' "$REPORT_DIR/ui.xml" \
    && grep -F -q 'Runtime row 1' "$REPORT_DIR/ui.xml"; then
    ui_ready=true
    break
  fi
  sleep 1
done
"$ADB" -s "$EMULATOR_SERIAL" exec-out screencap -p > "$REPORT_DIR/screenshot.png"
"$ADB" -s "$EMULATOR_SERIAL" shell getprop ro.build.fingerprint > "$REPORT_DIR/build-fingerprint.txt"

[[ "$ui_ready" == true ]] || {
  echo 'Android runtime: FAIL (required UI not published within timeout)' >&2
  exit 1
}
# Compose testTag is intentionally not assumed to be exported to UIAutomator;
# assert user-visible state and table headers instead. Cell-level row
# semantics are asserted against the post-Tab tree below because Compose
# publishes cell content-desc to the bridge upon the first focus traversal.
grep -F -q 'state page=1 size=10' "$REPORT_DIR/ui.xml" || { echo 'Android runtime: FAIL (state text missing)' >&2; exit 1; }
grep -F -q 'text="ID"' "$REPORT_DIR/ui.xml" || { echo 'Android runtime: FAIL (ID column header missing)' >&2; exit 1; }
grep -F -q 'text="Name"' "$REPORT_DIR/ui.xml" || { echo 'Android runtime: FAIL (Name column header missing)' >&2; exit 1; }
# Exercise the platform keyboard path once and retain the post-Tab tree. This
# is deliberately an input/focusability smoke, not a claim about TalkBack
# speech or OEM focus order; those require device-specific certification.
"$ADB" -s "$EMULATOR_SERIAL" shell input keyevent KEYCODE_TAB >/dev/null 2>&1 || true
sleep 1
"$ADB" -s "$EMULATOR_SERIAL" shell uiautomator dump /sdcard/stylish-runtime-after-tab.xml >/dev/null 2>&1 || true
"$ADB" -s "$EMULATOR_SERIAL" shell cat /sdcard/stylish-runtime-after-tab.xml > "$UI_XML_AFTER_TAB" 2>/dev/null || true
grep -F -q 'focusable="true"' "$UI_XML_AFTER_TAB" || {
  echo 'Android runtime: FAIL (post-Tab accessibility tree has no focusable node)' >&2
  exit 1
}
# Row/cell content-desc semantics are published to the accessibility bridge on
# the first focus traversal, so they are asserted against the post-Tab tree.
grep -F -q 'ID, row 1' "$UI_XML_AFTER_TAB" || { echo 'Android runtime: FAIL (table row semantics missing)' >&2; exit 1; }
grep -F -q 'Name, row 1' "$UI_XML_AFTER_TAB" || { echo 'Android runtime: FAIL (second column semantics missing)' >&2; exit 1; }
# Record platform-reported startup samples and gfxinfo frame statistics while
# the emulator is genuinely online. The collector refuses to call missing
# fields a PASS and marks the result as an emulator proxy rather than a
# production-device Macrobenchmark SLO.
python3 scripts/verify-android-performance.py \
  --collect \
  --adb "$ADB" \
  --serial "$EMULATOR_SERIAL" \
  --package "$PACKAGE" \
  --activity ".MainActivity" \
  --report "$PERFORMANCE_REPORT"
printf '{"schema":"stylish-ui.android-runtime.v1","device":"%s","apk":"%s","uiXml":"%s","uiXmlAfterTab":"%s","screenshot":"%s","performance":"%s"}\n' \
  "$EMULATOR_SERIAL" "$APK" "$REPORT_DIR/ui.xml" "$UI_XML_AFTER_TAB" "$REPORT_DIR/screenshot.png" "$PERFORMANCE_REPORT" > "$REPORT_DIR/manifest.json"
performance_status="$(jq -r '.status // "UNMEASURED"' "$PERFORMANCE_REPORT" 2>/dev/null || echo UNMEASURED)"
if [[ "$performance_status" != "PASS" ]]; then
  echo "Android runtime: UI PASS; performance proxy $performance_status ($REPORT_DIR)" >&2
  exit 1
fi
echo "Android runtime: PASS ($REPORT_DIR)"

#!/usr/bin/env bash
set -euo pipefail

# Start the packaged site and a clean headless Chrome endpoint, then execute the
# dependency-free accessibility-tree workflow. The caller receives the JSON/PNG
# artifacts produced by scripts/wasm-ui-e2e.mjs.
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

site_dir="${WASM_UI_E2E_SITE_DIR:-$ROOT/website-wasm/build/wasmSite}"
port="${WASM_UI_E2E_PORT:-8765}"
cdp_port="${WASM_UI_E2E_CDP_PORT:-9222}"
base_url="http://127.0.0.1:${port}/"
cdp_url="http://127.0.0.1:${cdp_port}"

[[ -s "$site_dir/index.html" ]] || { echo "missing packaged site: $site_dir/index.html" >&2; exit 1; }
command -v python3 >/dev/null || { echo "python3 is required" >&2; exit 1; }
command -v node >/dev/null || { echo "node is required" >&2; exit 1; }
command -v curl >/dev/null || { echo "curl is required" >&2; exit 1; }
chrome_bin="$(command -v google-chrome-stable || command -v google-chrome || true)"
[[ -n "$chrome_bin" ]] || { echo "Chrome is required" >&2; exit 1; }

tmp_root="${RUNNER_TEMP:-/tmp}"
http_log="$tmp_root/stylish-ui-http-${port}.log"
chrome_log="$tmp_root/stylish-ui-chrome-${cdp_port}.log"
chrome_profile="$tmp_root/stylish-ui-chrome-${cdp_port}-$$"

python3 -m http.server "$port" --directory "$site_dir" >"$http_log" 2>&1 &
http_pid=$!
cleanup() {
  [[ -n "${chrome_pid:-}" ]] && kill "$chrome_pid" 2>/dev/null || true
  [[ -n "${http_pid:-}" ]] && kill "$http_pid" 2>/dev/null || true
}
trap cleanup EXIT

for attempt in $(seq 1 30); do
  if curl --fail --silent "$base_url" >/dev/null; then break; fi
  if [[ "$attempt" == 30 ]]; then
    cat "$http_log" >&2 || true
    exit 1
  fi
  sleep 1
done
"$chrome_bin" --headless=new --no-sandbox --disable-gpu \
  --window-size=1440,1000 --force-device-scale-factor=1 --lang=ja-JP \
  --remote-debugging-port="$cdp_port" --user-data-dir="$chrome_profile" \
  "$base_url" >"$chrome_log" 2>&1 &
chrome_pid=$!

for attempt in $(seq 1 30); do
  if curl --fail --silent "$cdp_url/json/version" >/dev/null; then
    WASM_BASE_URL="$base_url" CDP_HTTP_URL="$cdp_url" \
      WASM_E2E_EVIDENCE_DIR="${WASM_E2E_EVIDENCE_DIR:-$ROOT/website-wasm/build/ci-evidence}" \
      node scripts/wasm-ui-e2e.mjs
    python3 scripts/verify-wasm-ui-e2e-artifact.py --require
    exit 0
  fi
  sleep 1
done

cat "$http_log" "$chrome_log" >&2 || true
exit 1

#!/usr/bin/env node

/**
 * Deterministic, dependency-free UI smoke for the packaged Compose/Wasm site.
 *
 * The app renders through Compose's accessibility bridge rather than ordinary
 * HTML controls. Chrome's Accessibility tree is therefore the stable surface:
 * actions are located by role/name, resolved to their backend bounds, and
 * driven through the DevTools input domain. The output is a JSON log and a
 * screenshot suitable for CI review. This is a workflow smoke, not a claim
 * that it replaces TalkBack/VoiceOver or a full browser test matrix.
 */

import { mkdir, writeFile } from "node:fs/promises";

const baseUrl = process.env.WASM_BASE_URL ?? "http://127.0.0.1:8765/";
const cdpHttp = process.env.CDP_HTTP_URL ?? "http://127.0.0.1:9222";
const evidenceDir = process.env.WASM_E2E_EVIDENCE_DIR ?? "website-wasm/build/ci-evidence";
const requestedTimeoutMs = Number(process.env.WASM_E2E_TIMEOUT_MS ?? 30_000);
const timeoutMs = Number.isFinite(requestedTimeoutMs) && requestedTimeoutMs > 0
  ? requestedTimeoutMs
  : 30_000;

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

async function json(url) {
  const response = await fetch(url, { signal: AbortSignal.timeout(timeoutMs) });
  if (!response.ok) throw new Error(`${url} returned ${response.status}`);
  return response.json();
}

class CdpConnection {
  constructor(url) {
    this.url = url;
    this.nextId = 0;
    this.pending = new Map();
    this.events = [];
  }

  async connect() {
    this.socket = new WebSocket(this.url);
    this.socket.addEventListener("message", (event) => {
      const message = JSON.parse(event.data);
      if (message.id && this.pending.has(message.id)) {
        const pending = this.pending.get(message.id);
        this.pending.delete(message.id);
        clearTimeout(pending.timer);
        pending.resolve(message);
      } else if (message.method) {
        this.events.push(message);
      }
    });
    await new Promise((resolve, reject) => {
      this.socket.addEventListener("open", resolve, { once: true });
      this.socket.addEventListener("error", reject, { once: true });
    });
  }

  send(method, params = {}) {
    const id = ++this.nextId;
    return new Promise((resolve, reject) => {
      const timer = setTimeout(() => {
        if (this.pending.delete(id)) reject(new Error(`CDP timeout: ${method}`));
      }, timeoutMs);
      this.pending.set(id, { resolve, reject, timer });
      this.socket.send(JSON.stringify({ id, method, params }));
    });
  }

  close() {
    this.socket?.close();
  }
}

function axValue(node, key) {
  return node?.[key]?.value;
}

async function accessibilityTree(cdp) {
  const result = await cdp.send("Accessibility.getFullAXTree");
  return result.result?.nodes ?? [];
}

async function evaluate(cdp, expression) {
  const result = await cdp.send("Runtime.evaluate", { expression, returnByValue: true });
  return result.result?.result?.value;
}

function findAx(nodes, role, name) {
  return nodes.find((node) =>
    !node.ignored && axValue(node, "role") === role && axValue(node, "name") === name,
  );
}

async function waitForAx(cdp, role, name) {
  const deadline = Date.now() + timeoutMs;
  let nodes;
  while (Date.now() < deadline) {
    nodes = await accessibilityTree(cdp);
    const match = findAx(nodes, role, name);
    if (match) return { node: match, nodes };
    await sleep(250);
  }
  const visible = (nodes ?? [])
    .filter((node) => !node.ignored && axValue(node, "name"))
    .slice(0, 30)
    .map((node) => `${axValue(node, "role")}:${axValue(node, "name")}`)
    .join(" | ");
  throw new Error(`Accessibility node not found: ${role} ${name}; visible=${visible}`);
}

async function boxCenter(cdp, node) {
  const model = await cdp.send("DOM.getBoxModel", { backendNodeId: node.backendDOMNodeId });
  const content = model.result?.model?.content;
  if (!content || content.length < 8) throw new Error(`No box for ${axValue(node, "name")}`);
  return {
    x: (content[0] + content[2] + content[4] + content[6]) / 4,
    y: (content[1] + content[3] + content[5] + content[7]) / 4,
  };
}

async function clickAx(cdp, role, name) {
  const { node } = await waitForAx(cdp, role, name);
  const point = await boxCenter(cdp, node);
  for (const type of ["mousePressed", "mouseReleased"]) {
    await cdp.send("Input.dispatchMouseEvent", {
      type,
      x: point.x,
      y: point.y,
      button: "left",
      clickCount: 1,
    });
  }
  await sleep(900);
}

async function typeAx(cdp, role, name, text) {
  const { node } = await waitForAx(cdp, role, name);
  const point = await boxCenter(cdp, node);
  await cdp.send("Input.dispatchMouseEvent", {
    type: "mousePressed",
    x: point.x,
    y: point.y,
    button: "left",
    clickCount: 1,
  });
  await cdp.send("Input.dispatchMouseEvent", {
    type: "mouseReleased",
    x: point.x,
    y: point.y,
    button: "left",
    clickCount: 1,
  });
  await cdp.send("Input.insertText", { text });
  await sleep(500);
}

function names(nodes) {
  return nodes.filter((node) => !node.ignored).map((node) => axValue(node, "name")).filter(Boolean);
}

function requireName(nodes, expected) {
  if (!names(nodes).includes(expected)) throw new Error(`Expected AX name ${expected}`);
}

let activeConnection;

async function main() {
  const targets = await json(`${cdpHttp}/json/list`);
  const target = targets.find((entry) => entry.type === "page" && entry.url.startsWith(baseUrl));
  if (!target) throw new Error(`No Chrome page target for ${baseUrl}`);

  const cdp = new CdpConnection(target.webSocketDebuggerUrl);
  activeConnection = cdp;
  await cdp.connect();
  await cdp.send("Runtime.enable");
  await cdp.send("Page.enable");
  await cdp.send("DOM.enable");
  await cdp.send("Accessibility.enable");
  await cdp.send("Emulation.setDeviceMetricsOverride", {
    width: 1440,
    height: 1000,
    deviceScaleFactor: 1,
    mobile: false,
  });
  await cdp.send("Emulation.setLocaleOverride", { locale: "ja-JP" });
  await cdp.send("Page.navigate", { url: baseUrl });
  await waitForAx(cdp, "button", "All, 92");

  const evidence = {
    schema: "stylish-ui.wasm-ui-e2e.v1",
    baseUrl,
    browser: targets.find((entry) => entry.id === target.id)?.title ?? "unknown",
    steps: [],
    consoleErrors: [],
    viewport: await evaluate(
      cdp,
      "({width: window.innerWidth, height: window.innerHeight, deviceScaleFactor: window.devicePixelRatio, locale: navigator.language, localeOverride: 'ja-JP'})",
    ),
  };

  let nodes = await accessibilityTree(cdp);
  requireName(nodes, "92 個のコンポーネント");
  evidence.steps.push({ name: "catalog-loaded", assertion: "All, 92 and 92 個のコンポーネント visible" });

  await clickAx(cdp, "button", "ボタン, 14");
  nodes = await accessibilityTree(cdp);
  await waitForAx(cdp, "StaticText", "14 個のコンポーネント");
  nodes = await accessibilityTree(cdp);
  evidence.steps.push({ name: "category-filter", assertion: "ボタン category shows 14 components" });

  await typeAx(cdp, "textbox", "コンポーネントを検索", "Card");
  await waitForAx(cdp, "StaticText", "1 個のコンポーネント");
  await waitForAx(cdp, "StaticText", "Card");
  nodes = await accessibilityTree(cdp);
  evidence.steps.push({ name: "search", assertion: "Card search narrows catalog to one component" });

  await clickAx(cdp, "button", "ライトモードへ");
  await waitForAx(cdp, "button", "ダークモードへ");
  nodes = await accessibilityTree(cdp);
  evidence.steps.push({ name: "theme-toggle", assertion: "theme button accessible label changes" });

  // A keyboard path: move focus from the theme control into the search field.
  const search = findAx(nodes, "textbox", "コンポーネントを検索");
  if (!search) throw new Error("Search textbox disappeared after theme toggle");
  const searchPoint = await boxCenter(cdp, search);
  await cdp.send("Input.dispatchMouseEvent", {
    type: "mousePressed", x: searchPoint.x, y: searchPoint.y, button: "left", clickCount: 1,
  });
  await cdp.send("Input.dispatchMouseEvent", {
    type: "mouseReleased", x: searchPoint.x, y: searchPoint.y, button: "left", clickCount: 1,
  });
  await cdp.send("Input.dispatchKeyEvent", { type: "keyDown", key: "Tab", code: "Tab", windowsVirtualKeyCode: 9 });
  await cdp.send("Input.dispatchKeyEvent", { type: "keyUp", key: "Tab", code: "Tab", windowsVirtualKeyCode: 9 });
  evidence.steps.push({ name: "keyboard-focus", assertion: "Tab key dispatched from search control" });

  const screenshot = await cdp.send("Page.captureScreenshot", { format: "png" });
  await mkdir(evidenceDir, { recursive: true });
  await writeFile(`${evidenceDir}/wasm-ui-e2e.png`, Buffer.from(screenshot.result.data, "base64"));
  evidence.consoleErrors = cdp.events
    .filter((event) => event.method === "Runtime.consoleAPICalled" && event.params.type === "error")
    .map((event) => event.params.args?.map((arg) => arg.value ?? arg.description).join(" "));
  if (evidence.consoleErrors.length > 0) throw new Error(`Console errors: ${evidence.consoleErrors.join(" | ")}`);
  evidence.completedAt = new Date().toISOString();
  await writeFile(`${evidenceDir}/wasm-ui-e2e.json`, `${JSON.stringify(evidence, null, 2)}\n`);
  console.log(JSON.stringify(evidence, null, 2));
  cdp.close();
}

main().catch((error) => {
  activeConnection?.close();
  console.error(`Wasm UI E2E failed: ${error.stack ?? error}`);
  process.exitCode = 1;
});

// Navigation helpers: drive the app to each screen via D-pad (+ minimal taps for content setup).
import { execSync } from "node:child_process";
import {
  adb, key, signature, foreground, PKG,
  DOWN, UP, LEFT, RIGHT, CENTER, BACK,
} from "./adb.mjs";

export const sleep = (s) => execSync(`sleep ${s}`);

export function clearData() {
  adb(["shell", "pm", "clear", PKG]);
  sleep(1);
}
export function start() {
  adb(["shell", "am", "start", "-n", `${PKG}/.MainActivity`]);
  sleep(2.5);
}
export function startSingleTop() {
  adb(["shell", "am", "start", "-n", `${PKG}/.MainActivity`, "--activity-single-top"]);
  sleep(1.5);
}

/** Grant the runtime + special permissions the onboarding permission gate asks for. */
export function grantAllPermissions() {
  for (const p of [
    "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES",
    "android.permission.READ_MEDIA_VISUAL_USER_SELECTED", "android.permission.POST_NOTIFICATIONS",
  ]) try { adb(["shell", "pm", "grant", PKG, p]); } catch {}
  try { adb(["shell", "dumpsys", "deviceidle", "whitelist", `+${PKG}`]); } catch {}
}

/** Tap a node whose text matches `re` (content setup only; the tests themselves are pure D-pad). */
export function tapText(re) {
  adb(["shell", "uiautomator", "dump", "/sdcard/ui.xml"]);
  const xml = adb(["shell", "cat", "/sdcard/ui.xml"]);
  const nodes = [...xml.matchAll(/text="([^"]*)"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"/g)];
  for (const m of nodes) {
    if (re.test(m[1])) {
      adb(["shell", "input", "tap", String((+m[2] + +m[4]) / 2), String((+m[3] + +m[5]) / 2)]);
      sleep(1.5);
      return true;
    }
  }
  return false;
}

/** Tap the full-width mini-player bar to open the now-playing screen. */
export function openPlayer() {
  adb(["shell", "uiautomator", "dump", "/sdcard/ui.xml"]);
  const xml = adb(["shell", "cat", "/sdcard/ui.xml"]);
  const bar = [...xml.matchAll(/clickable="true"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"/g)]
    .map((x) => x.slice(1).map(Number))
    .find(([x1, y1, , x2]) => y1 > 2100 && x1 === 0 && x2 >= 1000);
  if (!bar) return false;
  adb(["shell", "input", "tap", String((bar[0] + bar[2]) / 2), "2235"]);
  sleep(2);
  return true;
}

/** Open the navigation drawer (top-left hamburger). */
export function openDrawer() {
  adb(["shell", "input", "tap", "45", "73"]);
  sleep(1);
}

/** From an open drawer, move to the row with `label` and activate it. */
export function drawerGoto(label) {
  openDrawer();
  // drawer order: Account, Home, Artists, Kid Zone, Search, Library, Radio mode, Settings
  const order = ["Account", "Home", "Artists", "Kid Zone", "Search", "Library", "Radio mode", "Settings"];
  const idx = order.indexOf(label);
  if (idx < 0) return false;
  for (let i = 0; i < 12; i++) key(UP);
  for (let i = 0; i < idx; i++) key(DOWN);
  key(CENTER);
  sleep(1.5);
  return true;
}

/** Walk the onboarding flow with D-pad, invoking audit(name) on each screen. Returns the audits. */
export function walkOnboarding(audit) {
  const out = [];
  clearData();
  start();

  out.push(audit("onboarding/welcome"));
  key(CENTER);                      // agree checkbox (initial focus)
  key(DOWN); key(CENTER);           // Continue
  sleep(1.5);

  out.push(audit("onboarding/density"));
  // radios -> Skip for now: go to bottom then up one
  for (let i = 0; i < 8; i++) key(DOWN);
  key(UP); key(CENTER);
  sleep(1.5);

  out.push(audit("onboarding/content-filters"));
  // navigate to Continue (down past toggles + create-sync, then right) and activate
  key(DOWN); key(DOWN); key(DOWN); key(RIGHT); key(CENTER);
  sleep(1.5);

  // permission gate: grant via adb, resume to re-check, Continue
  out.push(audit("onboarding/permissions"));
  grantAllPermissions();
  adb(["shell", "input", "keyevent", "KEYCODE_HOME"]); sleep(1);
  startSingleTop();
  key(DOWN); key(CENTER);           // Continue (now enabled)
  sleep(1.5);

  out.push(audit("onboarding/nav-setup"));
  key(DOWN); key(DOWN); key(DOWN); key(CENTER); // No thanks -> Continue
  sleep(2.5);

  out.push(audit("onboarding/login-gate"));
  // anonymous login (second button)
  key(DOWN); key(CENTER);
  sleep(5);
  return out;
}

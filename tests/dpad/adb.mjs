// adb + oracle helpers for the D-pad test harness. Terminal, hard data.
// Focus state is identified by a screenshot "signature" (a downscaled grayscale hash): the visible
// focus ring/highlight changes pixels when focus moves, so two screenshots with the same focused
// element collapse to the same signature and a moved focus produces a different one.
import { execFileSync, execSync } from "node:child_process";
import { mkdtempSync, readFileSync, rmSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";

export const SERIAL = process.env.DPAD_SERIAL || "emulator-5554";
export const PKG = "com.jtech.zemer";

const TMP = mkdtempSync(join(tmpdir(), "dpad-"));
process.on("exit", () => { try { rmSync(TMP, { recursive: true, force: true }); } catch {} });

export function adb(args, { binary = false } = {}) {
  return execFileSync("adb", ["-s", SERIAL, ...args], {
    maxBuffer: 64 * 1024 * 1024,
    encoding: binary ? "buffer" : "utf8",
  });
}

export const sleep = (ms) => execSync(`sleep ${ms / 1000}`);

export function key(code) {
  adb(["shell", "input", "keyevent", code]);
  execSync("sleep 0.32");
}
export const DOWN = "KEYCODE_DPAD_DOWN", UP = "KEYCODE_DPAD_UP";
export const LEFT = "KEYCODE_DPAD_LEFT", RIGHT = "KEYCODE_DPAD_RIGHT";
export const CENTER = "KEYCODE_DPAD_CENTER", BACK = "KEYCODE_BACK";
export const DIRECTIONS = [DOWN, UP, LEFT, RIGHT];

let shotN = 0;
/** Capture the screen and return a stable signature string (same focus -> same signature). */
export function signature() {
  const png = join(TMP, `s${shotN++}.png`);
  execSync(`adb -s ${SERIAL} exec-out screencap -p > ${png}`);
  // 24x24 grayscale, 4-bit depth, hex of the raw bytes = a perceptual signature robust to noise.
  const sig = execSync(
    `convert ${png} -resize 24x24! -colorspace Gray -depth 4 gray:- | xxd -p | tr -d '\\n'`,
  ).toString().trim();
  return sig;
}

/** Current foreground component (package/activity), from the window manager. */
export function foreground() {
  const out = adb(["shell", "dumpsys", "window"]);
  const m = out.match(/mCurrentFocus=Window\{[^ ]+ [^ ]+ ([^}]+)\}/);
  return m ? m[1] : "(unknown)";
}

/** Number of clickable nodes the accessibility tree exposes on the current screen. */
export function clickableCount() {
  adb(["shell", "uiautomator", "dump", "/sdcard/ui.xml"]);
  const xml = adb(["shell", "cat", "/sdcard/ui.xml"]);
  return (xml.match(/clickable="true"/g) || []).length;
}

/** media_session playback state + position — the oracle for play/pause and seek. */
export function media() {
  const out = adb(["shell", "dumpsys", "media_session"]);
  const m = out.match(/state=(PLAYING|PAUSED|STOPPED|BUFFERING)\(\d\), position=(-?\d+)/);
  return m ? { state: m[1], position: Number(m[2]) } : { state: "NONE", position: -1 };
}

/** Ordered visible texts matching a regex — the oracle for reorder. */
export function visibleOrder(re) {
  adb(["shell", "uiautomator", "dump", "/sdcard/ui.xml"]);
  const xml = adb(["shell", "cat", "/sdcard/ui.xml"]);
  const out = [];
  for (const m of xml.matchAll(/text="([^"]+)"/g)) if (re.test(m[1])) out.push(m[1]);
  return out;
}

export function launch() {
  adb(["shell", "am", "start", "-n", `${PKG}/.MainActivity`]);
  execSync("sleep 2.5");
}

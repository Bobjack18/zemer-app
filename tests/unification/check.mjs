// UI unification + reusability checks. Pure static analysis over the tracked source — deterministic,
// no device, no network. Terminal truth: every assertion is a grep/count over the real files.
//
//   node tests/unification/check.mjs
//
// Exit 0 = all checks pass. Exit 1 = at least one regression (a bespoke duplicate crept back in, the
// audit script found a token/dialog/material-2 violation, or an interactive icon lost its label).
import { execSync } from "node:child_process";
import { readFileSync, readdirSync, statSync } from "node:fs";
import { join } from "node:path";
import { fileURLToPath } from "node:url";

const ROOT = join(fileURLToPath(new URL(".", import.meta.url)), "..", "..");
const UI = join(ROOT, "app/src/main/kotlin/com/jtech/zemer/ui");
const sh = (cmd) => execSync(cmd, { cwd: ROOT, encoding: "utf8", maxBuffer: 64 << 20 });

function walk(dir) {
  const out = [];
  for (const e of readdirSync(dir)) {
    const p = join(dir, e);
    if (statSync(p).isDirectory()) out.push(...walk(p));
    else if (p.endsWith(".kt")) out.push(p);
  }
  return out;
}
const FILES = walk(UI);
const read = (p) => readFileSync(p, "utf8");

// --- paren-matched call extraction (so `Icon(...)` spanning lines is one unit) ---
function calls(src, name) {
  const out = [];
  const re = new RegExp(`\\b${name}\\s*\\(`, "g");
  let m;
  while ((m = re.exec(src))) {
    let i = src.indexOf("(", m.index), depth = 0, j = i;
    for (; j < src.length; j++) {
      if (src[j] === "(") depth++;
      else if (src[j] === ")" && --depth === 0) break;
    }
    out.push(src.slice(m.index, j + 1));
    re.lastIndex = j + 1;
  }
  return out;
}

const results = [];
const check = (name, pass, detail) => {
  results.push({ name, pass, detail });
  console.log(`  [${pass ? "PASS" : "FAIL"}] ${name}${detail ? " — " + detail : ""}`);
};

console.log("\n=== UI unification / reusability ===\n");

// 1) The committed mechanical standard (R0–R9): Material-3-only, one dialog system, tokens, etc.
let auditOut = "";
try { auditOut = sh("bash scripts/ui-audit.sh 2>&1"); } catch (e) { auditOut = e.stdout || ""; }
const total = (auditOut.match(/TOTAL violations\s+(\d+)/) || [])[1];
check("ui-audit.sh (R0–R9) clean", total === "0", `violations=${total ?? "?"}`);

// 2) The in-app-bar search field is the single shared AppBarSearchField — no copy-pasted variant.
let straySearch = 0;
for (const p of FILES) {
  if (p.endsWith("AppBarSearchField.kt") || p.endsWith("SearchBar.kt")) continue;
  for (const c of calls(read(p), "TextField"))
    if (/imeAction\s*=\s*ImeAction\.Search/.test(c) && /Color\.Transparent/.test(c)) straySearch++;
}
check("no duplicated app-bar search TextField (use AppBarSearchField)", straySearch === 0, `stray=${straySearch}`);

// 3) Every interactive icon-only IconButton is labelled (only touch-only drag_handle may be null).
let unlabeled = [];
for (const p of FILES)
  for (const c of calls(read(p), "IconButton"))
    for (const ic of [...calls(c, "Icon"), ...calls(c, "Image")]) {
      const nullCd = /contentDescription\s*=\s*null/.test(ic) || /\b(?:Icon|Image)\s*\(\s*[^,]+,\s*null\b/.test(ic);
      if (nullCd && !/drawable\.drag_handle/.test(ic)) unlabeled.push(p.slice(UI.length + 1));
    }
check("every interactive IconButton is labelled (R12)", unlabeled.length === 0, unlabeled.length ? unlabeled.slice(0, 5).join(", ") : "0 unlabeled");

// 4) No hand-rolled dialogs — all go through Dialog.kt (R7 also enforces this; double-checked here).
let rawDialogs = 0;
for (const p of FILES) {
  if (p.includes(`${UI}/component/`)) continue;
  rawDialogs += (read(p).match(/\b(AlertDialog|BasicAlertDialog)\s*\(/g) || []).length;
}
check("no raw AlertDialog/BasicAlertDialog outside component/", rawDialogs === 0, `raw=${rawDialogs}`);

// 5) The shared reusable components that replace the old bespoke patterns all exist.
const required = [
  "component/AppBarSearchField.kt", "component/InfoCard.kt", "component/SelectPreference.kt",
  "component/Preference.kt", "component/Dialog.kt", "component/Items.kt", "component/ChipsRow.kt",
  "component/SortHeader.kt",
];
const missing = required.filter((r) => !FILES.some((f) => f.endsWith(r)));
check("shared reusable components present", missing.length === 0, missing.length ? "missing: " + missing.join(", ") : required.length + " present");

// 6) The deleted bespoke settings group stays deleted (replaced by Preference.kt rows).
const hasOldGroup = FILES.some((f) => f.endsWith("Material3SettingsGroup.kt"));
check("Material3SettingsGroup.kt stays deleted", !hasOldGroup);

// 7) No hand-rolled D-pad focus border outside the shared layer — custom focusables use
//    Modifier.dpadFocusBorder (utils/FocusBorder.kt). Shared components own their internal focus
//    visuals (border + background variants). Documented exceptions:
//    - AlbumScreen track row (1): border driven by the *inner item's* focus — dpadFocusBorder
//      would add a second focus target.
//    - Player title + artist (2): their exact modifier order (border -> padding -> focusable ->
//      onFocusChanged around clickable children) is load-bearing; bundling into dpadFocusBorder
//      broke focus initialization for the whole player surface (bisect-verified on-device).
const FOCUS_BORDER_IDIOM = /animateColorAsState\s*\(\s*targetValue\s*=\s*if\s*\([^)]*[fF]ocused[^)]*\)[^\n]*\n?[^\n]*else\s+Color\.Transparent/g;
const FOCUS_BORDER_ALLOWLIST = { "screens/AlbumScreen.kt": 1, "player/Player.kt": 2 };
let strayFocusBorders = [];
for (const p of FILES) {
  if (p.includes(`${UI}/component/`) || p.endsWith("utils/FocusBorder.kt")) continue;
  const rel = p.slice(UI.length + 1);
  const hits = (read(p).match(FOCUS_BORDER_IDIOM) || []).length;
  if (hits > (FOCUS_BORDER_ALLOWLIST[rel] ?? 0)) strayFocusBorders.push(`${rel}:${hits}`);
}
check("no hand-rolled focus border outside shared layer (use dpadFocusBorder)", strayFocusBorders.length === 0,
  strayFocusBorders.length ? strayFocusBorders.join(", ") : "0 stray");

// 8) The "which artist?" picker is the single shared SelectArtistDialog — every menu that opens
//    one calls the component instead of hand-rolling a ListDialog of artist rows.
const hasSelectArtist = FILES.some((f) => f.endsWith("component/SelectArtistDialog.kt"));
let unsharedArtistDialogs = [];
for (const p of FILES) {
  if (p.includes(`${UI}/component/`)) continue;
  const src = read(p);
  let i = src.indexOf("if (showSelectArtistDialog)");
  while (i !== -1) {
    if (!/^\s*if \(showSelectArtistDialog\) \{\s*\n\s*SelectArtistDialog\(/.test(src.slice(i, i + 200)))
      unsharedArtistDialogs.push(p.slice(UI.length + 1));
    i = src.indexOf("if (showSelectArtistDialog)", i + 1);
  }
}
check("artist picker goes through shared SelectArtistDialog", hasSelectArtist && unsharedArtistDialogs.length === 0,
  !hasSelectArtist ? "component missing" : unsharedArtistDialogs.length ? "hand-rolled: " + unsharedArtistDialogs.join(", ") : "all call sites shared");

const failed = results.filter((r) => !r.pass).length;
console.log(`\n=== ${results.length - failed} passed, ${failed} failed ===\n`);
process.exit(failed ? 1 : 0);

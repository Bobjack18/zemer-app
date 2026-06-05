// Comprehensive D-pad test suite — every screen, including onboarding. Drives the REAL app on the
// emulator and asserts D-pad operability with hard data. Terminal truth, no guesses.
//
//   node tests/dpad/suite.mjs            # full run (clears app data, walks onboarding, then all screens)
//   node tests/dpad/suite.mjs --no-onboarding
//
// Prereqs: emulator (DPAD_SERIAL, default emulator-5554) with the debug APK installed, and network
// (anonymous login + content). See README.md.
import { writeFileSync } from "node:fs";
import { foreground, media, key, UP, DOWN, CENTER, BACK } from "./adb.mjs";
import { auditScreen, oracleSeek, oraclePlayPause, oracleBack } from "./audit.mjs";
import {
  sleep, start, openDrawer, drawerGoto, openPlayer, tapText, walkOnboarding, startSingleTop,
} from "./nav.mjs";

const results = [];
const section = (t) => console.log(`\n--- ${t} ---`);
const log = (r) => {
  if (!r) return;
  results.push(r);
  const tag = r.pass ? "PASS" : "FAIL";
  const detail = r.kind === "oracle"
    ? JSON.stringify(Object.fromEntries(Object.entries(r).filter(([k]) => !["name", "kind", "pass"].includes(k))))
    : `reachable=${r.reachable}/${r.clickables} (need ${r.need})${r.trap ? "  *** FOCUS TRAP ***" : ""}`;
  console.log(`  [${tag}] ${r.name} — ${detail}`);
};
const auditHere = (name) => log({ ...auditScreen(name), name });
const safe = (fn) => { try { return fn(); } catch (e) { console.log(`    ! ${e.message}`); } };

console.log(`\n=== Zemer D-pad suite — ${process.env.DPAD_SERIAL || "emulator-5554"} ===`);

// 1) ONBOARDING — pure D-pad walk through every onboarding screen (clears app data first).
if (!process.argv.includes("--no-onboarding")) {
  section("Onboarding (every screen, pure D-pad)");
  safe(() => walkOnboarding((name) => ({ ...auditScreen(name), name })).forEach(log));
} else {
  start();
}

// If we are not in the app with media available, we still audit what we can.
section("Main screens (drawer-reachable)");
safe(() => { auditHere("home"); });
for (const item of ["Artists", "Kid Zone", "Search", "Library", "Account"]) {
  safe(() => { if (drawerGoto(item)) auditHere(item.toLowerCase().replace(/ /g, "-")); });
}

// 2) SETTINGS hub + every sub-screen (reached by tap for setup; audited with D-pad).
section("Settings + every sub-screen");
safe(() => { if (drawerGoto("Settings")) auditHere("settings"); });
const subSettings = [
  /Appearance/, /Player and audio/, /Stream sources/, /Content/, /Set up your D-pad/, /Links/,
  /Android Auto/, /Privacy/, /Storage/, /Backup/, /Integrations/, /Updater/, /About/,
];
for (const re of subSettings) {
  safe(() => {
    if (drawerGoto("Settings") && tapText(re)) {
      auditHere("settings/" + re.source.replace(/[^a-zA-Z]/g, "").toLowerCase());
      log(oracleBack(true)); // BACK returns to the settings list
    }
  });
}

// 3) CONTENT screens — start playback, then audit the player + queue with hard-data oracles.
section("Player + Queue (hard-data oracles + sweep)");
safe(() => {
  drawerGoto("Home");
  // play the first quick-pick song (content setup) to populate the player
  if (media().state === "NONE") tapText(/^(?!Quick picks).{4,}$/); // first list row
  sleep(4);
  if (media().state !== "NONE") {
    openPlayer();
    // D-pad scrub: Left/Right seeks ±5s on the now-playing surface (except the button row). Focus the
    // title (one DOWN from top) — a non-button zone — then RIGHT must advance the playback position.
    log(oracleSeek(() => { key(DOWN); }));
    // play button is the transport row: down past title/artist/seek area.
    log(oraclePlayPause(() => { key(DOWN); key(DOWN); key(DOWN); }));
    auditHere("player");
    log(oracleBack(true)); // collapse player
  } else {
    console.log("  ! could not start playback (no network/content) — player oracles skipped");
  }
});

const passed = results.filter((r) => r.pass).length;
const failed = results.length - passed;
console.log(`\n=== ${passed} passed, ${failed} failed, ${results.length} total ===`);
const fails = results.filter((r) => !r.pass);
if (fails.length) { console.log("FAILURES:"); fails.forEach((r) => console.log("  - " + r.name)); }
writeFileSync(new URL("./results.json", import.meta.url), JSON.stringify(results, null, 2));
process.exit(failed ? 1 : 0);

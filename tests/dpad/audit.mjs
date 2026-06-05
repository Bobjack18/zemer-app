// Per-screen D-pad coverage sweep + hard-data interaction oracles.
import {
  key, signature, clickableCount, foreground, media, visibleOrder,
  DOWN, UP, LEFT, RIGHT, CENTER, BACK, DIRECTIONS,
} from "./adb.mjs";

/**
 * Systematically sweep focus across the current screen and report the distinct focus states
 * reached. Algorithm: reset to the top, then for each row reached by DOWN, sweep RIGHT across the
 * row (and back), collecting screenshot signatures. Distinct signatures = reachable focus positions.
 * A "trap" is a state from which no direction changes the signature while other elements exist.
 */
export function sweep() {
  for (let i = 0; i < 14; i++) key(UP);          // reset to top
  const seen = new Set();
  let trap = null;

  const record = () => { seen.add(signature()); };
  const exploreRow = () => {
    record();
    let prev = signature(), moved = false;
    for (let r = 0; r < 12; r++) {                // walk right across the row
      key(RIGHT);
      const s = signature();
      record();
      if (s !== prev) moved = true; else break;
      prev = s;
    }
    for (let r = 0; r < 12; r++) key(LEFT);       // return to row start
    return moved;
  };

  let prevTop = signature();
  exploreRow();
  for (let row = 0; row < 40; row++) {
    key(DOWN);
    const s = signature();
    if (s === prevTop && seen.has(s)) {
      // DOWN produced nothing new — confirm it is the bottom, not a trap, by trying every direction
      const before = signature();
      let escaped = false;
      for (const d of DIRECTIONS) { key(d); if (signature() !== before) { escaped = true; break; } }
      if (!escaped && seen.size === 1 && clickableCount() > 1) trap = before;
      break;
    }
    prevTop = s;
    if (!seen.has(s)) exploreRow();
    if (seen.size > 80) break;
  }
  return { reachable: seen.size, trap };
}

/** A screen passes coverage if it reached >= ceil(clickables * 0.6) distinct focus states and has
 *  no hard trap. The 0.6 factor absorbs a11y nodes that are not focus targets or look identical. */
export function auditScreen(name) {
  const clickables = clickableCount();
  const { reachable, trap } = sweep();
  const need = Math.max(1, Math.ceil(clickables * 0.6));
  const pass = !trap && reachable >= need;
  return { name, kind: "coverage", clickables, reachable, need, trap: !!trap, pass };
}

/** INFALLIBLE oracle: press CENTER while focused on `findFocus()`; assert media state toggles. */
export function oraclePlayPause(focusSteps) {
  for (let i = 0; i < 14; i++) key(UP);
  focusSteps();                                   // navigate to the play control
  const before = media().state;
  key(CENTER);
  const after = media().state;
  return {
    name: "play/pause toggles via D-pad CENTER", kind: "oracle",
    before, after, pass: before !== after && after !== "NONE",
  };
}

/** INFALLIBLE oracle: focus the seek bar, press RIGHT, assert media position advances. */
export function oracleSeek(focusSteps) {
  for (let i = 0; i < 14; i++) key(UP);
  focusSteps();
  const before = media().position;
  for (let i = 0; i < 6; i++) key(RIGHT);
  const after = media().position;
  return {
    name: "seek advances via D-pad RIGHT", kind: "oracle",
    before, after, pass: after > before,
  };
}

/** INFALLIBLE oracle: run `doReorder()`, assert the visible order of `re` changed. */
export function oracleReorder(re, doReorder) {
  const before = visibleOrder(re).slice(0, 6).join(" | ");
  doReorder();
  const after = visibleOrder(re).slice(0, 6).join(" | ");
  return { name: "reorder changes list order via D-pad", kind: "oracle", before, after, pass: before !== after && before !== "" };
}

/** INFALLIBLE oracle: BACK changes the foreground/screen (no dead-end). */
export function oracleBack(expectChange = true) {
  const before = foreground() + "|" + signature();
  key(BACK);
  const after = foreground() + "|" + signature();
  return { name: "BACK navigates (no dead-end)", kind: "oracle", changed: before !== after, pass: expectChange ? before !== after : true };
}

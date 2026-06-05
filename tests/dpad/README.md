# tests/dpad — D-pad / no-touchscreen test harness

Drives the **real app on an emulator** via `adb` and asserts D-pad operability with hard data. Same
philosophy as the streaming harness: terminal output is the source of truth, not guesses.

The standard is: **every function and screen must be operable without a touchscreen.** This harness
checks that two ways.

## What it asserts

**Infallible state oracles** (deterministic hard data — a real state change is read back):

| Oracle | Hard-data source | Passes when |
| --- | --- | --- |
| play/pause via CENTER | `dumpsys media_session` state | state toggles PLAYING↔PAUSED |
| **seek via D-pad RIGHT** | `media_session` `position` | position advances |
| reorder via D-pad | `uiautomator` text order | the visible order changes |
| BACK navigates | foreground window + screen signature | the screen changes (no dead-end) |

**Focus-coverage sweep** (per screen): resets to the top, walks DOWN row-by-row and RIGHT across each
row, and counts the distinct *focus signatures* reached (a screenshot down-sampled to a grayscale
hash — the visible focus ring/highlight makes focus movement detectable). It flags:
- a **focus trap** (a state from which no direction changes anything while other elements exist), and
- **low coverage** (fewer distinct focus states than ~0.6 × the screen's clickable count).

## Run

```bash
# emulator-5554 (default) with the debug APK installed; network for anonymous login + content.
node tests/dpad/suite.mjs                 # clears app data, walks ONBOARDING, then every screen
node tests/dpad/suite.mjs --no-onboarding # skip the data-clear + onboarding walk
DPAD_SERIAL=emulator-5556 node tests/dpad/suite.mjs
```

Covers: onboarding (welcome, density, content-filters, permissions, nav-setup, login-gate — pure
D-pad), the drawer-reachable screens, every Settings sub-screen, and the player + queue oracles.
Results are written to `results.json`.

## Files

- `adb.mjs` — adb wrappers + oracles (`media`, `visibleOrder`, `clickableCount`, `signature`).
- `audit.mjs` — the coverage sweep + the four state oracles.
- `nav.mjs` — navigation (drawer, settings taps, the onboarding walker, permission granting).
- `suite.mjs` — the runner.

## Player seek (how the harness drove the fix)

The Material3 seek `Slider` cannot acquire D-pad focus in the player layout (proven here: a
forced-focus + preview-key probe never received a key, and navigation always skipped it — a Compose
focus-graph limitation, no extra buttons were added). The seek is instead handled at the now-playing
**surface** level: an `onPreviewKeyEvent` on the controls column scrubs ±5s on Left/Right, gated off
while the transport button row is focused (so prev/play/next navigation is preserved) — the standard
Android-TV media pattern. `oracleSeek` verifies it: focus the title (a non-button zone) and assert
`media_session` `position` advances on RIGHT.

# UI standards (design guidelines and requirements)

The single source of truth for building UI in this app. These are requirements, not suggestions: new
code must comply and existing code is being migrated to comply. All UI is Jetpack Compose on
**Material 3** (standard `MaterialTheme`). Enforced by review, the checklists at the end, and
`scripts/ui-audit.sh`.

## Principles

- **Material 3 only.** Standard `MaterialTheme` (the app's `ZemerTheme`). Never Material 2
  (`androidx.compose.material.*` components/theme). The Material Icons library is allowed.
- **D-pad first.** Every screen, dialog, sheet, and menu must be 100% navigable AND operable with a
  directional pad (D-pad + center + back) - TV, Android Auto, switch/keyboard. No action is
  touch-only. Permanent and non-negotiable.
- **Beautiful and cohesive.** One visual language. Consistent shape, spacing, motion, and color across
  every surface. Polished, not loud.
- **Tokens over magic numbers.** Every size, gap, radius, duration, and color resolves to a token.
- **One canonical component.** Reuse `ui/component/` before building; never a parallel widget set.
- **Theme-driven.** Color from `colorScheme`, type from `typography`, shape from `shapes`, motion from
  shared specs.

## Foundations (tokens)

- **Theme:** the app root is `ZemerTheme` -> `MaterialTheme(colorScheme, typography = AppTypography, shapes)`. Dynamic color + `ColorScheme.pureBlack()` (the only AMOLED path) stay.
- **Color:** `MaterialTheme.colorScheme` roles only. Media/video/art overlays use `AppColors`
  (`scrim`/`onMedia`/`mediaOverlay`). No `Color(0x...)` or named `Color.*` outside `ui/theme/`.
- **Typography:** `MaterialTheme.typography.*` roles (`.copy()` for weight/color only). No literal
  `fontSize`. Exempt: `LyricsImageCard` (fixed-size share bitmap).
- **Shape:** `MaterialTheme.shapes` (a defined scale: small 12 / medium 16 / large 20 / extraLarge 28)
  + a `pill` token. No `RoundedCornerShape(N.dp)` literals in screens.
- **Spacing/size:** `Dimens` tokens (`space1..space8` = 4/8/12/16/24/32; `ScreenPaddingH=16`,
  `IconSize=24`, `MinTouchTarget=48`, `DialogPadding=24`). No bare `.dp` outside the token defs.
- **Motion:** shared animation specs in `Motion` (a standard spring + `short=150`/`medium=250`/
  `long=400` ms tweens). Transitions, expand/collapse, and selection use these - no ad-hoc durations.

## Components

Use these; do not hand-roll equivalents.

- **Settings rows (`Preference.kt`):** `PreferenceGroupTitle`, `PreferenceEntry`, `SwitchPreference`,
  `EditTextPreference`, `SliderPreference`, `SelectPreference` (row that opens a `ListDialog`).
  `InfoCard`/`StatusRow` for rich status. Never `Material3SettingsGroup` (removed) or a hand-rolled
  Switch+Row.
- **Dialogs (`Dialog.kt`):** `DefaultDialog`, `ListDialog`, `TextFieldDialog`, `ActionPromptDialog`,
  `InfoLabel`. Never a raw `AlertDialog`/`BasicAlertDialog`/Card-as-dialog. See "Dialogs" below.
  The "which artist?" picker is the shared **`SelectArtistDialog`** (`component/SelectArtistDialog.kt`)
  — menus map their artist models to `SelectableArtist(id, name, thumbnailUrl?)` and handle navigation
  in `onSelect`; never re-roll the artist `ListDialog`.
- **Custom focusables:** a bespoke focusable control (not built on the shared components) gets its
  focus ring via **`Modifier.dpadFocusBorder(color, shape, width)`** (`ui/utils/FocusBorder.kt`) —
  never a hand-rolled `remember`+`animateColorAsState`+`border`+`focusable`+`onFocusChanged` chain.
  Two verified exceptions live with explanatory comments (see "D-pad and focus").
- **App bar:** Material3 `TopAppBar` + the app's `ui/component/IconButton` (which carries a visible
  D-pad focus ring). In-app-bar search uses the shared **`AppBarSearchField`** — never a re-rolled
  transparent `TextField`.
- **Lists/items:** `Items.kt`, `ChipsRow`, `SortHeader`. **States:** `EmptyPlaceholder`,
  `AppStateViews`, `shimmer/ShimmerHost`.

## Screen skeleton

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleSettings(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {
    val (enabled, onEnabledChange) = rememberPreference(ExampleKey, defaultValue = true)

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState()),
    ) {
        PreferenceGroupTitle(title = stringResource(R.string.example_group))
        SwitchPreference(
            title = { Text(stringResource(R.string.example_toggle)) },
            description = stringResource(R.string.example_toggle_desc),
            icon = { Icon(painterResource(R.drawable.example), null) },
            checked = enabled,
            onCheckedChange = onEnabledChange,
        )
    }

    TopAppBar(
        title = { Text(stringResource(R.string.example_title)) },
        navigationIcon = {
            IconButton(onClick = navController::navigateUp, onLongClick = navController::backToMain) {
                Icon(painterResource(R.drawable.arrow_back), contentDescription = null)
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
```

- Signature is `(navController: NavController, scrollBehavior: TopAppBarScrollBehavior)`; the file name
  matches the function name (`XxxSettings`/`XxxScreen`).
- Body is the scroll `Column` above, or a single `LazyColumn` when a dynamic/reorderable list is
  present. No nested scrollables, no `Scaffold`, no extra padding wrappers.
- The `TopAppBar` MUST get `scrollBehavior`. Back button MUST be the app `IconButton` with both
  `onClick = navController::navigateUp` and `onLongClick = navController::backToMain`.
- Group spacing comes only from `PreferenceGroupTitle`. No magic-number `Spacer`s.

## D-pad and focus (first-class)

- Every interactive element is `focusable()` (or focusable via `clickable`) with a clear, consistent
  focus state (the `PreferenceEntry` focus background + border is the baseline).
- Focus order follows visual order (`focusGroup()`/`focusProperties` where needed). No focus traps.
- Each screen requests a sensible initial focus (the `firstFocus`/`backFocus` `FocusRequester` pattern).
- Focusing an off-screen item scrolls it into view.
- Every touch-only gesture has a D-pad equivalent (reorder via the item menu's Move up/down, swipe
  actions via a focusable control / menu, etc.). When a control genuinely cannot hold D-pad focus
  (e.g. a Material `Slider` in a complex layout), handle the key at the surface level: the **player
  scrubs ±5s on Left/Right** via an `onPreviewKeyEvent` on the controls column, gated off while the
  transport button row is focused so prev/play/next navigation is preserved.
- Interactive elements are `>= 48dp`; icon-only controls carry a localized `contentDescription`
  (decorative icons pass `null`). The shared `IconButton` shows a primary focus ring.
- Custom focusables use `Modifier.dpadFocusBorder` (see "Components"). Two verified exceptions keep
  the raw idiom, with comments at the site and an allowlist in `tests/unification/check.mjs`:
  - **Player title + artist rows:** their exact modifier order (`border -> padding -> focusable ->
    onFocusChanged`, wrapping clickable children) is load-bearing — bundling it into the shared
    modifier broke Compose focus *initialization* for the whole player surface (bisect-verified
    on-device: no element could take focus until reverted).
  - **AlbumScreen track rows:** the border reacts to the *inner item's* focus via a shared map;
    `dpadFocusBorder` owns its own `focusable()` and would add a second focus target per row.
- Verified by the harness in `tests/dpad/` (adb-driven, hard-data oracles: play/pause, seek, reorder,
  BACK + a per-screen focus-coverage sweep). Every new screen must pass a D-pad-only walkthrough.

## Dialogs (beautiful + unified)

Always via the `Dialog.kt` helpers. One look, one structure, one motion.

- Structure (top to bottom): optional centered hero icon -> title (`headlineSmall`, `onSurface`) ->
  supporting text (`bodyMedium`, `onSurfaceVariant`) -> content -> actions row.
- Surface: `shapes.extraLarge`, `surfaceContainerHigh` tonal background, `Dimens.DialogPadding` (24dp),
  constrained max width, respects insets.
- Actions: text buttons, end-aligned, max 2-3. Affirmative in `confirmButton`, cancel/negative in
  `dismissButton`. Destructive affirmative uses `colorScheme.error`.
- Long or "pick one of N" content scrolls; selection uses `ListDialog` via `SelectPreference` - never a
  hand-rolled radio column.
- Motion: enter/exit via the shared `Motion` spec (scale + fade).
- D-pad: takes focus when shown, fully traversable, center activates, back dismisses; the *safe* action
  is default-focused (cancel for destructive dialogs).
- All text localized; no duplicated dialog content across screens (one component).

## Strings

- All user-facing text via `stringResource(R.string.x)`. No hardcoded literals.
- New strings go in `app/src/main/res/values/metrolist_strings.xml`. Never `strings.xml` (upstream;
  headed "do not add new features here").

## Lists, cards, and surfaces

- Song / album / artist / playlist rows use the shared items in `Items.kt` - never a bespoke
  `Row` of thumbnail + text + menu. Context menus open via `GridMenu` / `NewMenuComponents`.
- Cards and containers use a `MaterialTheme.shapes` token and a `surfaceContainer*` tonal color; rich
  status blocks use `InfoCard` / `StatusRow`, not a one-off `Card`.
- Chips use `ChipsRow` (filter/sort). A standalone chip uses `MaterialTheme.shapes` / `PillShape`.
- Bottom sheets use `BottomSheet` / `BottomSheetMenu` / `BottomSheetPage`; never build a sheet from
  scratch. Sheets are D-pad operable and dismissable (R11).

## States

Every list or content surface handles three states with the shared components:
- Loading: `shimmer/ShimmerHost` placeholders for content that will fill in (not a bare spinner).
- Empty: `EmptyPlaceholder` (icon + message + optional action).
- Error: `AppStateViews` (message + retry). Always offer a retry path.

Transient feedback is a snackbar/Toast with a localized string - never a hardcoded literal.

## Navigation

- Screens are registered as routes in `NavigationBuilder.kt`; settings screens are reached from
  `SettingsScreen` via a `PreferenceEntry` row.
- Back: the app-bar `IconButton` does `navigateUp`, long-press does `backToMain` (R3). Hardware/gesture
  back maps to `navigateUp`.
- Bottom-navigation items are user-configurable; respect the saved set and order.
- Screen transitions use the shared `Motion` specs (R9).

## Accessibility

- D-pad and focus per the section above (R10/R11) - this also covers keyboard and switch access.
- Touch targets `>= 48dp` (R12); icon-only controls carry a localized `contentDescription`; decorative
  icons pass `null`.
- Rely on `colorScheme` for contrast; never hardcode a color that can fail in light/dark/pureBlack.
- Text scales with the user's font-size setting (typography roles, no fixed `fontSize`).

## The rules (quick reference)

- R0 Material 3 only (standard `MaterialTheme`); no Material 2.
- R1 Screen `(navController, scrollBehavior)`, `@OptIn(ExperimentalMaterial3Api)`, file name = function.
- R2 `TopAppBar` receives `scrollBehavior`.
- R3 Back button = app `IconButton` + `onLongClick = backToMain`.
- R4 Body = scroll `Column` or single `LazyColumn`; no nested scrollables/`Scaffold`/extra padding.
- R5 Group spacing only from `PreferenceGroupTitle`; no magic `Spacer`s.
- R6 Settings rows = `Preference.kt` widgets only.
- R7 Dialogs via `Dialog.kt` + the Dialogs structure above.
- R8 Text via `stringResource`; new strings in `metrolist_strings.xml`.
- R9 Color/type/shape/motion/size from theme tokens; no raw literals outside `ui/theme/`.
- R10 D-pad complete: reachable + operable, visible focus, logical order, scroll-into-view, no traps,
  initial focus.
- R11 No touch-only actions; dialogs/sheets/menus fully D-pad operable.
- R12 Interactive `>= 48dp`; icon-only controls carry a `contentDescription`.

## Checklists

New screen: R1 signature + file name; R2 scrollBehavior; R3 back button; R4 body; R5 spacing;
`Preference.kt` rows; all strings localized in `metrolist_strings.xml`; D-pad-only walkthrough passes;
build green + `ui-audit.sh` clean.

New component: lives in `ui/component/`; tokens only (R9); focusable + visible focus + `>= 48dp` (R10,
R12); has a `contentDescription` path for icon-only use; no parallel duplicate.

New dialog: built from a `Dialog.kt` helper (R7); follows the Dialogs structure; shared `Motion`;
D-pad-operable with the safe default focus; localized.

## Exemptions

R9 flags magic-number / hardcoded literals only. These documented cases are exempt (and encoded in
`scripts/ui-audit.sh`); everything else must use tokens:
- `ui/theme/*` (the token definitions) and `LyricsImageCard` (a fixed-size share bitmap).
- Media / video / art overlays, which use `AppColors` (`scrim`/`onMedia`/`mediaOverlay`).
- The AMOLED pure-black convention (`if (pureBlack) <color> else <role>`) - `ColorScheme.pureBlack()`
  cannot express the foreground overrides.
- Alpha masks (`BlendMode.DstIn`, fading edges) where the color value is irrelevant.
- User-selectable color data (e.g. the lyrics share-image palette).
- Named shape constants (`ThumbnailCornerRadius`), asymmetric (per-corner) shapes, and
  computed/animated shapes - none are magic-number literals.
- `AutoResizeText` (computed size) and typography-derived sizes (`MaterialTheme.typography.x.fontSize`).

## Tests

- `scripts/ui-audit.sh` - the mechanical R0-R9 enforcement (must read 0).
- `tests/unification/check.mjs` - static reuse checks (audit clean, no duplicated app-bar search field,
  every interactive `IconButton` labelled, no raw dialogs, shared components present).
- `tests/dpad/` - adb-driven D-pad harness on the running app (hard-data oracles + per-screen focus
  coverage), including the onboarding flow.

## Documentation

No emojis or decorative symbols anywhere under `docs/` - ASCII only (use `->` over a glyph). Keep this
file in sync when a shared convention changes. The code-derived docs (`repository-map.md`,
`reference/*.md`, `ui/README.md` inventory tables) are regenerated by `python3 docs/generate.py`.

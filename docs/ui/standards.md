# UI standards (hard rules)

The single source of truth for building UI in this app. These are rules, not suggestions; new code
must comply and existing code is being migrated to comply. All UI is Jetpack Compose on
**Material 3 Expressive** (`material3 1.4.0`). Enforced by review, the checklists at the end, and
`scripts/ui-audit.sh`.

## Principles

- **Material 3 Expressive, exclusively.** The app root is `MaterialExpressiveTheme`. Never Material 2
  (`androidx.compose.material.*` components/theme). The Material Icons library is allowed.
- **D-pad first.** Every screen, dialog, sheet, and menu must be 100% navigable AND operable with a
  directional pad (D-pad + center + back) - TV, Android Auto, switch/keyboard. No action is
  touch-only. Permanent and non-negotiable.
- **Expressive but tasteful.** Spring motion, shape morph, and `MaterialShapes` for hero/selection
  moments; settings, lists, and dialogs stay calm and legible. Beautiful = cohesive, not loud.
- **Tokens over magic numbers.** Every size, gap, radius, motion spec, and color resolves to a token.
- **One canonical component.** Reuse `ui/component/` before building; never a parallel widget set.
- **Theme-driven.** Color from `colorScheme`, type from `typography`, shape from `shapes`/`MaterialShapes`,
  motion from `motionScheme`.

## Foundations (tokens)

- **Theme:** app root is `MaterialExpressiveTheme(colorScheme, motionScheme = MotionScheme.expressive(), shapes, typography)`. Keep dynamic color + `ColorScheme.pureBlack()` (the only AMOLED path).
- **Color:** `MaterialTheme.colorScheme` roles only. Media/video/art overlays use `AppColors`
  (`scrim`/`onMedia`/`mediaOverlay`). No `Color(0x...)` or named `Color.*` outside `ui/theme/`.
- **Typography:** `MaterialTheme.typography.*` roles (`.copy()` for weight/color only). No literal
  `fontSize`. Exempt: `LyricsImageCard` (fixed-size share bitmap).
- **Shape:** `MaterialTheme.shapes` (`extraSmall..extraLarge`) + a `pill`; `MaterialShapes` for accents.
  No `RoundedCornerShape(N.dp)` literals.
- **Spacing/size:** `Dimens` tokens (`space1..space8` = 4/8/12/16/24/32; `ScreenPaddingH=16`,
  `IconSize=24`, `MinTouchTarget=48`). No bare `.dp` outside the token defs.
- **Motion:** `MaterialTheme.motionScheme` spring specs (`spatialSpec`/`effectsSpec`) for transitions.
  No millisecond literals.

## Components

Use these; do not hand-roll equivalents.

- **Settings rows (`Preference.kt`):** `PreferenceGroupTitle`, `PreferenceEntry`, `SwitchPreference`,
  `EditTextPreference`, `SliderPreference`, `SelectPreference` (row that opens a `ListDialog`).
  `InfoCard`/`StatusRow` for rich status. Never `Material3SettingsGroup` (removed) or a hand-rolled
  Switch+Row.
- **Dialogs (`Dialog.kt`):** `DefaultDialog`, `ListDialog`, `TextFieldDialog`, `ActionPromptDialog`,
  `InfoLabel`. Never a raw `AlertDialog`/`BasicAlertDialog`/Card-as-dialog. See "Dialogs" below.
- **Expressive variants:** `LoadingIndicator`/wavy progress over plain spinners; the expressive +
  wavy `Slider`; `ButtonGroup`/`SplitButton`; `FloatingActionButtonMenu`.
- **App bar:** Material3 `TopAppBar` + the app's `ui/component/IconButton`.
- **Lists/items:** `Items.kt`, `ChipsRow`. **States:** `EmptyPlaceholder`, `AppStateViews`,
  `shimmer/ShimmerHost`.

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
- Every touch-only gesture has a D-pad equivalent (reorder via focus + center/long-press, drag-seek via
  D-pad left/right, swipe actions via a focusable control).
- Interactive elements are `>= 48dp`; icon-only controls carry a localized `contentDescription`
  (decorative icons pass `null`).

## Dialogs (beautiful + unified)

Always via the `Dialog.kt` helpers. One look, one structure, one motion.

- Structure (top to bottom): optional centered hero icon -> title (`headlineSmall`, `onSurface`) ->
  supporting text (`bodyMedium`, `onSurfaceVariant`) -> content -> actions row.
- Surface: `shapes.extraLarge`, `surfaceContainerHigh` tonal background, 24dp content padding,
  constrained max width, respects insets.
- Actions: text buttons, end-aligned, max 2-3. Affirmative in `confirmButton`, cancel/negative in
  `dismissButton`. Destructive affirmative uses `colorScheme.error`.
- Long or "pick one of N" content scrolls; selection uses `ListDialog` via `SelectPreference` - never a
  hand-rolled radio column.
- Motion: expressive spring (scale + fade) from `motionScheme`.
- D-pad: takes focus when shown, fully traversable, center activates, back dismisses; the *safe* action
  is default-focused (cancel for destructive dialogs).
- All text localized; no duplicated dialog content across screens (one component).

## Strings

- All user-facing text via `stringResource(R.string.x)`. No hardcoded literals.
- New strings go in `app/src/main/res/values/metrolist_strings.xml`. Never `strings.xml` (upstream;
  headed "do not add new features here").

## The rules (quick reference)

- R0 Material 3 Expressive only; `MaterialExpressiveTheme`; no Material 2.
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

New dialog: built from a `Dialog.kt` helper (R7); follows the Dialogs structure; spring motion;
D-pad-operable with the safe default focus; localized.

## Documentation

No emojis or decorative symbols anywhere under `docs/` - ASCII only (use `->` over a glyph). Keep this
file in sync when a shared convention changes.

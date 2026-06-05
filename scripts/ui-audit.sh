#!/usr/bin/env bash
# UI standards audit (docs/ui/standards.md). Greps the Compose UI for rule violations, prints
# file:line, and exits non-zero if any are found. Intended for local use and a CI "UI lint" job.
#
#   scripts/ui-audit.sh            # report + non-zero exit on violations
#   scripts/ui-audit.sh --quiet    # counts only
set -uo pipefail
cd "$(git rev-parse --show-toplevel)"

UI="app/src/main/kotlin/com/jtech/zemer/ui"
SETTINGS="$UI/screens/settings"
THEME="$UI/theme"
QUIET="${1:-}"
total=0

# check <rule> <description> <grep-extended-pattern> <path> [extra egrep -v exclude]
check() {
  local rule="$1" desc="$2" pat="$3" path="$4" excl="${5:-}"
  local hits
  # A line may opt out explicitly with a trailing  // ui-audit: ignore  comment (documented exemption).
  if [ -n "$excl" ]; then
    hits=$(grep -rnE "$pat" "$path" 2>/dev/null | grep -vE "$excl" | grep -vF 'ui-audit: ignore')
  else
    hits=$(grep -rnE "$pat" "$path" 2>/dev/null | grep -vF 'ui-audit: ignore')
  fi
  local n
  n=$(printf '%s' "$hits" | grep -c . )
  total=$((total + n))
  printf '%-6s %-44s %3d\n' "$rule" "$desc" "$n"
  if [ "$QUIET" != "--quiet" ] && [ "$n" -gt 0 ]; then
    printf '%s\n' "$hits" | sed 's/^/    /' | head -12
    [ "$n" -gt 12 ] && echo "    ... +$((n - 12)) more"
  fi
}

echo "== UI standards audit =="
echo "rule   check                                        hits"
check "R0"  "Material 2 imports (excl. icons)"            "import androidx\.compose\.material\." "$UI" "material\.icons|material\.ripple|material3"
check "R3"  "plain material3 IconButton in settings/"     "import androidx\.compose\.material3\.IconButton" "$SETTINGS"
check "R6"  "Material3SettingsGroup/Item usage"           "Material3Settings(Group|Item)" "$UI" "Material3SettingsGroup\.kt"
check "R7"  "raw AlertDialog/BasicAlertDialog"            "(^|[^a-zA-Z])(Basic)?AlertDialog\(" "$UI" "/component/Dialog\.kt"
# R9 flags magic-number/hardcoded literals only. Documented exemptions (docs/ui/standards.md):
# theme files, the fixed-size LyricsImageCard bitmap, AutoResizeText (computed size) and
# typography-derived sizes, named shape constants / asymmetric / computed shapes (not [0-9]-leading),
# media overlays (AppColors), the pureBlack AMOLED convention, and alpha masks (BlendMode).
check "R9"  "literal fontSize"                            "fontSize\s*=" "$UI" "theme/Type\.kt|LyricsImageCard\.kt|AutoResizeText\.kt|MaterialTheme\.typography"
check "R9"  "magic RoundedCornerShape"                    "RoundedCornerShape\([0-9]" "$UI" "/theme/|RoundedCornerShape\(0\.dp|LyricsImageCard\.kt"
check "R9"  "hardcoded Color"                             "Color\(0x|Color\.(Black|White|Red|Gray|LightGray|Green|Blue)" "$UI" "/theme/|AppColors|pureBlack|useBlackBackground|BlendMode|LyricsImageCard\.kt|component/Lyrics\.kt|FadingEdge\.kt|ShimmerHost\.kt"

echo "-----------------------------------------------------------"
printf '%-51s %3d\n' "TOTAL violations" "$total"

# Informational (not counted): gesture-only modifiers to verify are D-pad operable (R10/R11).
# Every one of these must have a focus + key path or a focusable control alternative; confirm
# with a D-pad-only walkthrough of the affected screen.
if [ "$QUIET" != "--quiet" ]; then
  echo
  echo "== D-pad review (informational - verify each has a focus/D-pad path) =="
  grep -rlE "pointerInput|detectTapGestures|\.draggable\(|anchoredDraggable|\.swipeable\(" "$UI" 2>/dev/null \
    | sed "s#$UI/##" | sort | sed 's/^/  gesture: /'
fi

[ "$total" -eq 0 ] && { echo "clean"; exit 0; } || exit 1

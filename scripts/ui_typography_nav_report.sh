#!/usr/bin/env bash
set -uo pipefail

SCREEN_DIR="app/src/main/java/com/astro/vajra/ui/screen"

echo "Running UI typography/nav consistency report..."

if command -v rg >/dev/null 2>&1; then
  FONT_SIZE_COUNT=$(rg -n --glob "*.kt" "fontSize\\s*=\\s*[0-9]+\\.?[0-9]*\\.sp" "$SCREEN_DIR" | wc -l | tr -d ' ')
  BYPASS_COUNT=$(rg -n --glob "*.kt" "(FilterChip\\(|ScrollableTabRow\\(|TabRow\\(|Tab\\()" "$SCREEN_DIR" | wc -l | tr -d ' ')
  SHARED_NAV_COUNT=$(rg -n --glob "*.kt" "ModernPillTabRow\\(" "$SCREEN_DIR" | wc -l | tr -d ' ')
else
  FONT_SIZE_COUNT=$(grep -RInE --include="*.kt" "fontSize[[:space:]]*=[[:space:]]*[0-9]+(\.[0-9]+)?\.sp" "$SCREEN_DIR" | wc -l | tr -d ' ')
  BYPASS_COUNT=$(grep -RInE --include="*.kt" "FilterChip\(|ScrollableTabRow\(|TabRow\(|Tab\(" "$SCREEN_DIR" | wc -l | tr -d ' ')
  SHARED_NAV_COUNT=$(grep -RIn --include="*.kt" "ModernPillTabRow(" "$SCREEN_DIR" | wc -l | tr -d ' ')
fi

echo "UI report:"
echo "  hardcoded_font_size_usages=$FONT_SIZE_COUNT"
echo "  nav_patterns_bypassing_shared_row=$BYPASS_COUNT"
echo "  modern_pill_tab_row_usages=$SHARED_NAV_COUNT"
echo "Report completed (non-blocking)."

exit 0

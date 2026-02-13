#!/usr/bin/env bash
set -euo pipefail

UI_DIR="app/src/main/java/com/astro/storm/ui"
SCREEN_DIR="$UI_DIR/screen"

echo "UI Style Report"
echo "==============="

HARDCODED_FONT_SIZES=$( (grep -RInE --include="*.kt" "fontSize\\s*=\\s*[0-9]+\\.sp" "$UI_DIR" || true) | wc -l | tr -d ' ')
MODERN_PILL_USAGES=$( (grep -RIn --include="*.kt" "ModernPillTabRow\\(" "$SCREEN_DIR" || true) | wc -l | tr -d ' ')
DIRECT_FILTER_CHIP_USAGES=$( (grep -RIn --include="*.kt" "FilterChip\\(" "$SCREEN_DIR" || true) | wc -l | tr -d ' ')
DIRECT_TABROW_USAGES=$( (grep -RInE --include="*.kt" "\\b(TabRow|ScrollableTabRow)\\(" "$SCREEN_DIR" || true) | wc -l | tr -d ' ')

echo "hardcoded_font_sizes=$HARDCODED_FONT_SIZES"
echo "modern_pill_tab_row_usages=$MODERN_PILL_USAGES"
echo "direct_filter_chip_usages=$DIRECT_FILTER_CHIP_USAGES"
echo "direct_tabrow_usages=$DIRECT_TABROW_USAGES"
echo "nav_patterns_bypassing_shared=$((DIRECT_FILTER_CHIP_USAGES + DIRECT_TABROW_USAGES))"

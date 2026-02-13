#!/usr/bin/env bash
set -uo pipefail

SCREEN_DIR="app/src/main/java/com/astro/storm/ui/screen"

echo "Running UI consistency checks..."

# 1) Hardcoded hex colors are forbidden in screen files.
HEX_MATCHES=$(grep -RIn --include="*.kt" "Color(0x" "$SCREEN_DIR" || true)
if [[ -n "$HEX_MATCHES" ]]; then
  echo "WARN: Found hardcoded hex colors in UI screen files."
  echo "$HEX_MATCHES"
fi

# 2) Runtime dark palette references are forbidden in screen files.
DARK_MATCHES=$(grep -RIn --include="*.kt" "DarkAppThemeColors" "$SCREEN_DIR" || true)
if [[ -n "$DARK_MATCHES" ]]; then
  echo "WARN: Found DarkAppThemeColors references in UI screen files."
  echo "$DARK_MATCHES"
fi

# 3) Direct RoundedCornerShape literals are forbidden in screen files.
SHAPE_MATCHES=$(grep -RInE --include="*.kt" "RoundedCornerShape\([0-9]+\.dp\)" "$SCREEN_DIR" || true)
if [[ -n "$SHAPE_MATCHES" ]]; then
  echo "WARN: Found non-token shape literals in UI screen files."
  echo "$SHAPE_MATCHES"
fi

TOTAL_SCREENS=$(find "$SCREEN_DIR" -type f -name "*.kt" | wc -l | tr -d ' ')
TOKEN_SCREENS=$( (grep -RIl --include="*.kt" "NeoVedicTokens" "$SCREEN_DIR" || true) | wc -l | tr -d ' ')
SHARED_PRIMITIVE_SCREENS=$( (grep -RIlE --include="*.kt" "ModernCard|ExpandableCard|NeoVedicCard|NeoVedicSectionHeader|NeoVedicListItem|NeoVedicStatusChip|NeoVedicMetricRow|NeoVedicExpandableCard|com\.astro\.storm\.ui\.components\.SectionHeader" "$SCREEN_DIR" || true) | wc -l | tr -d ' ')

echo "UI consistency report:"
echo "  total_screens=$TOTAL_SCREENS"
echo "  screens_using_neovedic_tokens=$TOKEN_SCREENS"
echo "  screens_using_shared_primitives=$SHARED_PRIMITIVE_SCREENS"
echo "  screens_with_hardcoded_hex_colors=$(if [[ -n \"$HEX_MATCHES\" ]]; then echo 1; else echo 0; fi)"
echo "  runtime_dark_palette_usages=$(if [[ -n \"$DARK_MATCHES\" ]]; then echo 1; else echo 0; fi)"
echo "  non-token-shape-warnings=$(if [[ -n \"$SHAPE_MATCHES\" ]]; then echo 1; else echo 0; fi)"
echo "UI consistency checks completed in report-only mode."

exit 0

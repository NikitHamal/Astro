$ErrorActionPreference = "Continue"

$screenDir = "app/src/main/java/com/astro/vajra/ui/screen"

Write-Output "Running UI consistency checks..."

function Find-Matches {
    param(
        [string]$Pattern
    )

    if (Get-Command rg -ErrorAction SilentlyContinue) {
        return rg -n --glob "*.kt" $Pattern $screenDir
    }

    return (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt |
        Select-String -Pattern $Pattern |
        ForEach-Object { "{0}:{1}:{2}" -f $_.Path, $_.LineNumber, $_.Line.Trim() })
}

$hexMatches = Find-Matches "Color\(0x"
if ($hexMatches) {
    Write-Output "WARN: Found hardcoded hex colors in UI screen files."
    $hexMatches
}

$darkMatches = Find-Matches "DarkAppThemeColors"
if ($darkMatches) {
    Write-Output "WARN: Found DarkAppThemeColors references in UI screen files."
    $darkMatches
}

$shapeMatches = Find-Matches "RoundedCornerShape\([0-9]+\.dp\)"
if ($shapeMatches) {
    Write-Output "WARN: Found non-token shape literals in UI screen files."
    $shapeMatches
}

$totalScreens = (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt | Measure-Object).Count

if (Get-Command rg -ErrorAction SilentlyContinue) {
    $tokenScreens = (rg -l --glob "*.kt" "NeoVedicTokens" $screenDir | Measure-Object).Count
    $sharedPrimitiveScreens = (rg -l --glob "*.kt" "ModernCard|ExpandableCard|NeoVedicPageHeader|NeoVedicStatusPill|NeoVedicEmptyState|ModernPillTabRow|NeoVedicFeatureTabs" $screenDir | Measure-Object).Count
} else {
    $tokenScreens = (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt | Select-String -Pattern "NeoVedicTokens" | Select-Object -ExpandProperty Path -Unique | Measure-Object).Count
    $sharedPrimitiveScreens = (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt | Select-String -Pattern "ModernCard|ExpandableCard|NeoVedicPageHeader|NeoVedicStatusPill|NeoVedicEmptyState|ModernPillTabRow|NeoVedicFeatureTabs" | Select-Object -ExpandProperty Path -Unique | Measure-Object).Count
}

Write-Output "UI consistency report:"
Write-Output "  total_screens=$totalScreens"
Write-Output "  screens_using_neovedic_tokens=$tokenScreens"
Write-Output "  screens_using_shared_primitives=$sharedPrimitiveScreens"
Write-Output ("  screens_with_hardcoded_hex_colors={0}" -f $(if ($hexMatches) { 1 } else { 0 }))
Write-Output ("  runtime_dark_palette_usages={0}" -f $(if ($darkMatches) { 1 } else { 0 }))
Write-Output ("  non-token-shape-warnings={0}" -f $(if ($shapeMatches) { 1 } else { 0 }))
Write-Output "UI consistency checks completed in report-only mode."

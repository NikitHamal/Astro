$ErrorActionPreference = "Stop"

$uiDir = "app/src/main/java/com/astro/storm/ui"
$screenDir = "$uiDir/screen"

Write-Output "UI Style Report"
Write-Output "==============="

function Count-Matches {
    param(
        [string]$Pattern,
        [string]$Path
    )

    if (Get-Command rg -ErrorAction SilentlyContinue) {
        return (rg -n --glob "*.kt" $Pattern $Path | Measure-Object).Count
    }

    return (Get-ChildItem -Path $Path -Recurse -Filter *.kt |
        Select-String -Pattern $Pattern | Measure-Object).Count
}

$hardcodedFontSizes = Count-Matches "fontSize\s*=\s*[0-9]+\.?([0-9]+)?\.sp" $uiDir
$modernPillUsages = Count-Matches "ModernPillTabRow\(" $screenDir
$directFilterChipUsages = Count-Matches "FilterChip\(" $screenDir
$directTabRowUsages = Count-Matches "\b(TabRow|ScrollableTabRow)\(" $screenDir

Write-Output "hardcoded_font_sizes=$hardcodedFontSizes"
Write-Output "modern_pill_tab_row_usages=$modernPillUsages"
Write-Output "direct_filter_chip_usages=$directFilterChipUsages"
Write-Output "direct_tabrow_usages=$directTabRowUsages"
Write-Output ("nav_patterns_bypassing_shared={0}" -f ($directFilterChipUsages + $directTabRowUsages))

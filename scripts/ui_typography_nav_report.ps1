$ErrorActionPreference = "Stop"

$screenDir = "app/src/main/java/com/astro/vajra/ui/screen"

Write-Output "Running UI typography/nav consistency report..."

if (Get-Command rg -ErrorAction SilentlyContinue) {
    $fontSizeCount = (rg -n --glob "*.kt" "fontSize\s*=\s*[0-9]+\.?([0-9]+)?\.sp" $screenDir | Measure-Object).Count
    $bypassCount = (rg -n --glob "*.kt" "FilterChip\(|ScrollableTabRow\(|\bTabRow\(|\bTab\(" $screenDir | Measure-Object).Count
    $sharedNavCount = (rg -n --glob "*.kt" "ModernPillTabRow\(|NeoVedicFeatureTabs\(" $screenDir | Measure-Object).Count
} else {
    $fontSizeCount = (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt | Select-String -Pattern "fontSize\s*=\s*[0-9]+\.?([0-9]+)?\.sp" | Measure-Object).Count
    $bypassCount = (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt | Select-String -Pattern "FilterChip\(|ScrollableTabRow\(|\bTabRow\(|\bTab\(" | Measure-Object).Count
    $sharedNavCount = (Get-ChildItem -Path $screenDir -Recurse -Filter *.kt | Select-String -Pattern "ModernPillTabRow\(|NeoVedicFeatureTabs\(" | Measure-Object).Count
}

Write-Output "UI report:"
Write-Output "  hardcoded_font_size_usages=$fontSizeCount"
Write-Output "  nav_patterns_bypassing_shared_row=$bypassCount"
Write-Output "  modern_pill_or_feature_tabs_usages=$sharedNavCount"
Write-Output "Report completed (non-blocking)."

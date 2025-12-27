# AstroStorm UI/UX & Performance Overhaul - Continuity Ledger

## Goal (incl. success criteria)
Comprehensive UI/UX redesign and performance optimization of the AstroStorm Vedic astrology Android app:
1. **Light Mode Optimization**: Fix all screens not optimized for light mode (birth chart, full screen chart, dialogs, planets, dashas)
2. **Remove Shadbala from Planets Screen**: Remove shadbala summary card and dialog (separate screen exists)
3. **Home Tab Revamp**: Complete redesign - modern, minimal, clean, uncluttered, responsive
4. **Tab Navigation Consistency**: Standardize all screens to use modern tab navigation (like nakshatras, shadbala, shodasvarga screens)
5. **AppBar Title Fixes**: Single line titles with ellipsis for all long titles
6. **Responsive Layouts**: Fix text justification, vertical crowding, overflow issues throughout
7. **Matchmaking Screen Fixes**: Show numeric values beside progress bars, fix AI insights lag/freeze
8. **Performance Optimization**: Address app-wide lag, freezing, poor user experience
9. **Separate Dasha Screens**: Create individual screens for each dasha type
10. **Birth Chart Accuracy**: Fix house number inconsistencies between chart image and planet details
11. **Codebase Cleanup**: Remove redundancies, duplications, dead code; modularize; localize hardcoded strings
12. **Vedic Accuracy**: Verify all calculators are precise and based on authentic Vedic astrology

## Constraints/Assumptions
- Kotlin + Jetpack Compose codebase (182 files, 164K+ LOC)
- Must maintain all existing functionality
- No git commits - only file changes
- Production-grade implementations only
- Localization for English and Nepali
- MVVM architecture with Room database
- Must not break existing navigation or features

## Key decisions
- Use `ModernTabRow` component pattern (from newer screens) for tab navigation consistency
- Follow `AppThemeColors` system for all light/dark mode colors
- StringResources.kt is the source for localization
- Focus on performance through: LazyColumn optimization, coroutine scoping, remember/derivedStateOf usage

## State
### Done:
- Initial codebase exploration and analysis completed
- Identified 36+ screens, 46 calculators, 7 ViewModels
- Mapped navigation structure (50+ routes)
- Identified key issues: redesigned screen duplication, color spread, large dialogs

### Now:
- Starting implementation - Light mode fixes and Planets screen cleanup

### Next (Priority Order):
1. Light mode fixes for birth chart, planets, dashas screens
2. Remove Shadbala card/dialog from PlanetsScreen
3. Standardize tab navigation across all screens
4. Home tab complete revamp
5. AppBar title truncation fixes
6. Responsive layout fixes (overflow, crowding)
7. Matchmaking screen fixes
8. Performance optimizations
9. Dasha screen separation
10. Birth chart house number accuracy
11. Codebase cleanup and dead code removal

## Open questions (UNCONFIRMED if needed)
- UNCONFIRMED: Exact visual design preferences for home tab (will create modern minimal design)
- UNCONFIRMED: Specific screens with most critical overflow issues beyond screenshots

## Working set (files/ids/commands)
### Key Files:
- `app/src/main/java/com/astro/storm/ui/screen/home/HomeTab.kt`
- `app/src/main/java/com/astro/storm/ui/screen/planets/PlanetsScreen.kt`
- `app/src/main/java/com/astro/storm/ui/screen/birthchart/BirthChartScreen.kt`
- `app/src/main/java/com/astro/storm/ui/screen/dashas/DashasScreen.kt`
- `app/src/main/java/com/astro/storm/ui/theme/AppTheme.kt`
- `app/src/main/java/com/astro/storm/ui/theme/Color.kt`
- `app/src/main/java/com/astro/storm/ui/components/ModernTabRow.kt`
- `app/src/main/java/com/astro/storm/ui/navigation/Navigation.kt`
- `app/src/main/java/com/astro/storm/data/localization/StringResources.kt`

### Commands:
- Build: `./gradlew assembleDebug`
- Test: `./gradlew test`

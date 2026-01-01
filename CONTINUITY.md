# AstroStorm Overhaul - Continuity Ledger
> Last updated: 2026-01-01T12:45:00+05:45

## Goal
Complete production-grade UI/UX and performance overhaul including:
1. Light mode optimization for all screens (birth chart, dialogs, planets, dashas)
2. Remove Shadbala summary card and dialog from Planets screen
3. Complete Home tab revamp (modern, minimal, responsive)
4. Fix tab navigation inconsistencies across all screens
5. Single-line app bar titles with ellipsis
6. Responsive/compact layouts throughout
7. Fix text overflow, crowding, and layout issues
8. Performance optimization (AI insights lag, general responsiveness)
9. Full codebase refactoring, cleanup, dead code removal
10. Localization of hardcoded strings (EN/NE)

**Success Criteria**:
- All screens render correctly in light and dark mode
- Consistent modern tab navigation across all screens
- No overflow/crowding issues on any screen size
- Smooth performance with no lag on AI features
- Clean, modular codebase with no dead code

## Constraints/Assumptions
- Kotlin + Jetpack Compose codebase
- Vedic astrology calculations must remain accurate
- Offline-first architecture must be preserved
- English and Nepali localization required
- Must not break existing functionality

## Key Decisions
- Use modern ScrollableTabRow pattern from newer screens (Shadbala, Nakshatras, etc.) for all tab navigation
- Remove Shadbala dialog/card from PlanetsScreen (separate ShadbalaScreen exists)
- Home tab will use card-based grid layout with quick actions
- All app bar titles will use `maxLines = 1, overflow = TextOverflow.Ellipsis`

## State

### Done (Previous Session):
- Enum refactoring for `Planet`, `ZodiacSign`, `Nakshatra`, `StrengthRating` with `stringKey`
- `LocalizationProvider.kt` enhanced
- `PlanetsTabContent.kt`, `KalachakraDashaScreen.kt`, `PrashnaScreen.kt` localized

### Now:
- Phase 1: Light mode fixes for screens/components
- Phase 2: Remove Shadbala from Planets screen
- Phase 3: Home tab revamp

### Next:
- Phase 4: Tab navigation standardization
- Phase 5: App bar title fixes
- Phase 6: Layout/overflow fixes throughout
- Phase 7: Performance optimization
- Phase 8: Codebase cleanup and refactoring

## Open Questions
- UNCONFIRMED: Specific design preferences for Home tab (will implement modern minimal approach)

## Working Set
**Active Files**:
- `ui/screen/PlanetsScreen.kt` - Remove Shadbala
- `ui/components/ChartDialogs.kt` - Clean up Shadbala dialog
- `ui/screen/main/HomeTab.kt` - Complete revamp
- `ui/screen/BirthChartScreen.kt` - Light mode fixes
- `ui/screen/DashasScreen.kt` - Tab navigation fix
- `ui/theme/Color.kt`, `ui/theme/AppTheme.kt` - Light mode colors

**Key Patterns**:
- Modern tab navigation: `ScrollableTabRow` with `Tab` composables
- Light mode colors: `LightAppThemeColors` from `AppTheme.kt`
- Localization: `stringResource(StringKey.*)` pattern

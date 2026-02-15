# AstroStorm - Advanced Vedic Astrology App Instructions.

## Overview
AstroStorm is a high-precision Vedic Astrology application designed for Android using Jetpack Compose. It features advanced calculation engines (Swiss Ephemeris), comprehensive divisional chart analysis, and specialized timing systems like Shoola Dasha and Nadi Amsha.

## Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Calculation Engine**: Swiss Ephemeris (JNI/Wrapper)
- **Navigation**: Jetpack Compose Navigation

## Build Instructions
- Never Build, just verify everythig mentally by double checking or so and push changes.

## Code Instructions
- **Always deliver high quality, fully functional, vedic astrologically accurate and precise production grade implementations only.**
- **Never add todos, basic, or simpified implementations/changes in the codebase. Everything shall be production ready.**

## Learnings
- PWA updates: `sw.js` stale-while-revalidate for local assets reduces hard-refresh issues.
- Dasha precision: Chara/Narayana benefit from exaltation/debilitation year adjustments.
- Localization: `_getDisplayName(key)` style indirection prevents wrong translation key namespaces.
- Edit flow reliability: in chart edit mode, unchanged input should short-circuit update to avoid unnecessary recalculation/error surfaces.
- Legacy timezone strings can break runtime chart/transit paths; central sanitization with safe fallback prevents parser edge-case crashes.

## Actions Taken
- Implemented Kalachakra and Narayana dasha systems.
- Added standalone Avasthas screen with 3-system toggle.
- Updated service worker strategy and versioned cache rollout.
- Fixed raw translation key leaks in Analysis/Yogas areas.
- Expanded classical yoga coverage substantially.
- Added edit-form no-change short-circuit and timezone normalization in `ChartInputScreen`.
- Added centralized `TimezoneSanitizer` and integrated it into repository mapping, transit analysis, and Swiss engine timezone resolution.
- Added transit error-state handling with retry to avoid indefinite loading on analysis exceptions.
- Added UI-state reset hooks to prevent stale `ChartUiState.Error` toast repetition after back navigation.

## Architecture Decisions
- Keep calculation engines modular by domain (dasha, varga, transit, prashna, varshaphala).
- Keep chart input/update UX resilient: normalize persisted timezone IDs and avoid no-op recalculation updates.
- Normalize/sanitize timezone values at data boundaries (`ChartRepository`) so runtime screens consume canonical timezone IDs.

## App Structure
- Core calculation engines: `app/src/main/java/com/astro/storm/ephemeris/**`
- Primary chart input screen: `app/src/main/java/com/astro/storm/ui/screen/ChartInputScreen.kt`

## Known Issues
- Runtime stack source for the reported `length=13; index=13` error is UNCONFIRMED without crash logs; defensive timezone sanitization + fallback and transit error handling are applied.


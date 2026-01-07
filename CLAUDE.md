# AstroStorm - Advanced Vedic Astrology Component

## Overview
AstroStorm is a high-precision Vedic Astrology application designed for Android using Jetpack Compose. It features advanced calculation engines (Swiss Ephemeris), comprehensive divisional chart analysis, and specialized timing systems like Shoola Dasha and Nadi Amsha.

## Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Calculation Engine**: Swiss Ephemeris (JNI/Wrapper)
- **Navigation**: Jetpack Compose Navigation

## Key Features
- **Vedic Chart Calculation**: High-precision sidereal chart (Lahiri/Chitra Paksha).
- **Divisional Charts**: Full support for Shodashvarga (D-1 to D-60) + D-150 (Nadi Amsha).
- **Dasha Systems**: Vimshottari, Yogini, and **Shoola Dasha (Jaimini)**.
- **Transits**: Real-time transits, **Ashtavarga Transit**, and **Kakshya Transit**.
- **Prashna (Horary)**: Tajika system with Itthasala, Saham, and Arudha Lagna.
- **Matchmaking**: Ashta Kuta and Dasha Kuta systems.
- **Localization**: Full English (EN) and Nepali (NE) support.

## Feature Implementation Details

### Shoola Dasha
- **File**: `ShoolaDashaCalculator.kt`
- **Logic**: Jaimini sign-based dasha for health and longevity.
- **Components**: Brahma/Rudra/Maheshwara calculation, Health Severity Index.

### Kakshya Transit
- **File**: `KakshaTransitCalculator.kt`
- **Logic**: 8-fold division of signs for micro-transit timing (3°45').
- **Optimization**: Uses `AtomicReference` caching in ViewModel for performance.

### Ashtavarga Transit
- **File**: `AshtavargaTransitCalculator.kt`
- **Logic**: Transit intensity prediction based on SAV/BAV scores.

### Nadi Amsha (D-150)
- **File**: `NadiAmshaCalculator.kt`
- **Logic**: Finest divisional chart (12' arc) for birth time rectification.
- **UI**: Interactive rectification slider to visualize Nadi shifts.

### Enhanced Prashna
- **File**: `PrashnaCalculator.kt`
- **Logic**: Includes Tajika aspects (Itthasala, Isarafa, Nakta, Yamaya) and Arudha Lagna calculation.
- **Note**: This calculator is currently a large file (~2500 lines) and is a candidate for future modularization.

## Build Instructions
1.  **Prerequisites**: Android Studio Koala+, JDK 17+.
2.  **Build**: `./gradlew assembleDebug`
3.  **Test**: `./gradlew test` (Unit tests currently limited).

## Code Style
- **Kotlin Style Guide**: Standard Android Kotlin conventions.
- **Composable Formatting**: Modifier as first optional argument.
- **State Management**: `StateFlow` in ViewModels, `collectAsState` in UI.

## Future Roadmap
- [ ] Refactor `PrashnaCalculator.kt` into sub-modules (Planetary, Aspects, Tajika).
- [ ] Add PDF report generation.
- [ ] Cloud backup for user charts.

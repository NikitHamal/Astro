# AstroStorm: Deep-Dive Technical & Astrological Analysis

This document provides a comprehensive analysis of the AstroStorm codebase, identifying critical issues, architectural flaws, and opportunities for reaching a production-grade, high-precision Vedic astrology implementation.

**Last Analysis Date:** 2026-01-14

---

## 1. Astrological Precision & Scriptural Adherence

### 1.1 Shadbala Sunrise/Sunset Precision (RESOLVED ✅)
*   **Previous Issue:** `ShadbalaCalculator.kt` was reported to use hardcoded 6 AM/6 PM sunrise and sunset approximations.
*   **Current Status:** **FIXED** - The calculator now properly uses `PanchangaCalculator` to get real astronomical sunrise/sunset Julian Day values.
*   **Code Evidence:**
    - `ShadbalaCalculator.kt:372` - Uses `PanchangaCalculator(context).use { ... }` to fetch real data
    - `ShadbalaCalculator.kt:382-386` - Lazy properties for `sunriseJD`, `sunsetJD`, and `isDay` calculation
    - `ShadbalaCalculator.kt:679-713` - Full implementation using real sunrise/sunset for Tribhaga Bala
*   **Note:** Lines 649-664 contain documentation comments explaining the concept, but the implementation uses actual ephemeris data.

### 1.2 Shadbala Varga Variants (MEDIUM)
*   **Current Issue:** Uses D7 (Saptamsa) instead of D60 (Shashtiamsa) for `Saptavargaja Bala` in some instances, or lacks the full Shashtiamsa sensitivity.
*   **Deep Dive:** Brihat Parashara Hora Shastra (BPHS) explicitly specifies D60 for the 60-virupa Saptavargaja calculation. D60 is the most sensitive and important varga in Shadbala.
*   **Recommendation:** Ensure `ShadbalaCalculator` fully integrates the D60 calculations already available in `DivisionalChartCalculator`.

### 1.3 Lunar Node Calculation (MEDIUM)
*   **Current Issue:** Rahu/Ketu are often defaults to Mean Node.
*   **Deep Dive:** Modern practitioners expect a choice between Mean and True nodes.
*   **Recommendation:** Add a global `CalculationSettings` object to support Node Type (Mean/True) and Ayanamsa selection (Lahiri, Raman, KP, etc.).

### 1.4 Panchanga Data Exposure (MEDIUM)
*   **Current Issue:** `PanchangaCalculator` returns formatted strings (e.g., "6:42:15 AM") instead of raw `LocalTime` or Julian Day values.
*   **Impact:** Other calculators (like `MuhurtaCalculator` or `ShadbalaCalculator`) cannot easily reuse these results without fragile string parsing.
*   **Recommendation:** Refactor `PanchangaData` to hold raw astronomical values. Move formatting to a dedicated `PanchangaFormatter`.

---

## 2. Architectural & Technical Quality

### 2.1 Stalled Modularization (MAJOR)
*   **Current Issue:** The project contains `:core:common` and `:core:model` directories, but they are empty and not included in `settings.gradle.kts`. The entire application is a monolith in `:app`.
*   **Impact:** 
    *   **Build Times:** Full rebuilds are required for every change.
    *   **Tightly Coupled Logic:** Astrological logic is mixed with Android `Context` and UI dependencies.
*   **Recommendation:** 
    *   Activate `:core:model` and move all data classes (`Planet`, `ZodiacSign`, `VedicChart`) there.
    *   Create `:core:ephemeris` for pure Kotlin calculation logic.
    *   Create `:core:ui` for reusable Compose components.

### 2.2 Large File Refactoring (PARTIALLY RESOLVED ✅)
*   **Guidelines:** Files should be 500-1000 lines maximum per CONTINUITY.md constraints.
*   **Files Status:**

| File | Lines | Status | Priority |
|------|-------|--------|----------|
| `YogaCalculator.kt` | 225 | **REFACTORED ✅** | DONE |
| `PrashnaCalculator.kt` | 2,613 | Needs modularization | CRITICAL |
| `RemediesCalculator.kt` | 2,176 | Needs modularization | HIGH |
| `VarshaphalaCalculator.kt` | 2,171 | Needs modularization | HIGH |
| `MuhurtaCalculator.kt` | 1,928 | Needs modularization | HIGH |
| `DivisionalChartAnalyzer.kt` | 1,826 | Needs modularization | MEDIUM |
| `NativeAnalysisCalculator.kt` | 1,572 | Needs modularization | MEDIUM |
| `ShoolaDashaCalculator.kt` | 1,490 | Borderline | LOW |
| `MarakaCalculator.kt` | 1,452 | Borderline | LOW |
| `DashaCalculator.kt` | 1,438 | Borderline | LOW |
| `KalachakraDashaCalculator.kt` | 1,435 | Borderline | LOW |
| `ArudhaPadaCalculator.kt` | 1,425 | Borderline | LOW |
| `HoroscopeCalculator.kt` | 1,337 | Borderline | LOW |
| `BadhakaCalculator.kt` | 1,246 | Borderline | LOW |
| `ChartRenderer.kt` | 1,227 | Borderline | LOW |

*   **YogaCalculator Refactoring COMPLETE (2026-01-14):**
    *   Implemented **Strategy Pattern** with `YogaEvaluator` interface
    *   Split 3,002 lines into 12 focused files:
        - `YogaCalculator.kt` (225 lines) - Orchestrator
        - `YogaModels.kt` (293 lines) - Data classes
        - `YogaLocalization.kt` (241 lines) - Localization utilities
        - `YogaHelpers.kt` (543 lines) - Shared utilities
        - `YogaEvaluator.kt` (42 lines) - Interface
        - `RajaYogaEvaluator.kt` (349 lines)
        - `DhanaYogaEvaluator.kt` (212 lines)
        - `MahapurushaYogaEvaluator.kt` (174 lines)
        - `NabhasaYogaEvaluator.kt` (417 lines)
        - `ChandraYogaEvaluator.kt` (290 lines)
        - `SolarYogaEvaluator.kt` (215 lines)
        - `NegativeYogaEvaluator.kt` (469 lines)
        - `SpecialYogaEvaluator.kt` (415 lines)
    *   All files now under 550 lines, meeting guidelines

*   **Recommendation for PrashnaCalculator (2,613 lines):**
    *   Split into: `PrashnaCore`, `PrashnaYogaEvaluator`, `PrashnaTimingCalculator`, `PrashnaCategoryHandler`.
    *   Extract Tajika aspect calculations to dedicated module.

### 2.3 Hardcoded Interpretations (MAJOR)
*   **Current Issue:** Thousands of lines of text (descriptions, effects, remedies) are hardcoded in Kotlin files (e.g., `YogaCalculator.kt:501`).
*   **Impact:** Localization into Hindi, Sanskrit, or Nepali is practically impossible without a full code rewrite.
*   **Recommendation:** 
    *   Move all strings to `StringKey` constants.
    *   Use external JSON/XML assets for long-form interpretations.
    *   Implement a template engine for dynamic interpretations (e.g., "Planet {planet} in house {house}").

### 2.4 Lack of Dependency Injection (MEDIUM)
*   **Current Issue:** Many classes manually instantiate `SwissEph` or other calculators.
*   **Recommendation:** Introduce Hilt or Koin for better testability and lifecycle management of heavy objects like the Swiss Ephemeris engine.

---

## 3. UI/UX & Rendering

### 3.1 South Indian Chart Rendering (RESOLVED ✅)
*   **Previous Issue:** `ChartRenderer.drawSouthIndianChart` was an empty stub that redirected to the North Indian renderer.
*   **Current Status:** **FIXED** - Full South Indian chart implementation completed.
*   **Implementation Details:**
    - 4x4 grid with 12 outer cells for the 12 zodiac signs
    - Fixed sign positions (Pisces at top-left, clockwise: Ar, Ta, Ge, Cn, Le, Vi, Li, Sc, Sg, Cp, Aq)
    - Ascendant marked with diagonal line in the ascendant sign cell
    - Sign abbreviations in top-left corner of each cell
    - Full planet display with degrees, retrograde/combust/vargottama indicators
    - Dignity indicators (exalted/debilitated/own sign/moolatrikona)
    - Center area displays chart title
*   **New Methods Added:**
    - `drawSouthIndianChart()` - Main rendering function
    - `drawSouthIndianCellContents()` - Cell content layout
    - `createSouthIndianChartBitmap()` - Bitmap generation
    - `drawSouthIndianChartWithLegend()` - Chart with legend

### 3.2 PDF Export & Print Quality (MEDIUM)
*   **Current Issue:** `ChartRenderer` uses `android.graphics.Paint` which is tied to the Android platform.
*   **Recommendation:** Ensure the renderer can target `Canvas` objects from PDF documents to provide high-resolution, vector-based exports.

---

## 4. Testing & Reliability

### 4.1 Missing Automated Validation (CRITICAL)
*   **Current Issue:** There are no unit tests for the complex ephemeris logic.
*   **Impact:** A small change in a utility function could silently break Shadbala or Dasha calculations across the entire app.
*   **Recommendation:** 
    *   Establish a "Golden Data" test suite.
    *   Compare AstroStorm's output for 10+ diverse charts against industry standards like **Jagannatha Hora** and **Swiss Ephemeris** CLI.
    *   Implement property-based testing for angular calculations (ensure longitudes are always 0-360, etc.).

---

## 5. Summary of High-Priority Fixes

### Completed ✅
1.  **Sunrise/Sunset**: ✅ Now using real ephemeris data from PanchangaCalculator for Shadbala.
2.  **South Indian Chart**: ✅ Full 4x4 grid implementation with proper sign placement completed.
3.  **Localization**: ✅ KalachakraDashaCalculator favorable areas now fully localized (16 new StringKeyDosha entries).
4.  **YogaCalculator Refactoring**: ✅ Split 3,002-line monolith into 12 focused files using Strategy Pattern (2026-01-14).

### Pending
1.  **Modularize**: Move model and ephemeris logic out of `:app`.
2.  **Refactor PrashnaCalculator**: Split 2613-line calculator into sub-modules.
3.  **Refactor RemediesCalculator**: Split 2176-line calculator into sub-modules.
4.  **Testing**: Implement automated validation with golden data test suite.

---
*Analysis updated on 2026-01-14 by AstroStorm Senior Engineering Agent.*
# AstroStorm: Deep-Dive Technical & Astrological Analysis

This document provides a comprehensive analysis of the AstroStorm codebase, identifying critical issues, architectural flaws, and opportunities for reaching a production-grade, high-precision Vedic astrology implementation.

---

## 1. Astrological Precision & Scriptural Adherence

### 1.1 Shadbala Accuracy (CRITICAL)
*   **Current Issue:** `ShadbalaCalculator.kt` and `KalaBalaCalculator.kt` use hardcoded 6 AM/6 PM sunrise and sunset approximations for `Tribhaga Bala` and `Nathonnatha Bala`.
*   **Deep Dive:** In Vedic astrology, the day (Dinaman) and night (Ratriman) durations vary significantly based on latitude and season. Using a 6/6 split leads to massive errors (up to 100% in these specific balas) for births in high latitudes or near the equinoxes.
*   **Code Evidence:** `ShadbalaCalculator.kt:664` contains a note explicitly acknowledging this approximation.
*   **Recommendation:** Implement a centralized `SolarEventProvider` or `AstroContext` that provides precise sunrise/sunset JD values using `swissEph.swe_rise_trans`.

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

### 2.2 YogaCalculator: The "God Object" (CRITICAL)
*   **Current Issue:** `YogaCalculator.kt` is a 3000+ line monolith with hundreds of hardcoded logic blocks.
*   **Deep Dive:** Adding or modifying a single Yoga requires navigating a massive file. The logic is highly repetitive.
*   **Recommendation:** 
    *   Implement a **Strategy Pattern**. Create a `YogaEvaluator` interface.
    *   Each Yoga (or category) becomes its own class (e.g., `RajaYogaEvaluator`, `PanchaMahapurushaEvaluator`).
    *   Use a `YogaRegistry` to manage and execute these evaluators.

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

### 3.1 South Indian Chart Placeholder (MAJOR BUG)
*   **Current Issue:** `ChartRenderer.drawSouthIndianChart` is an empty stub that redirects to the North Indian renderer.
*   **Impact:** The app is unusable for users in South India, Sri Lanka, and parts of Southeast Asia who rely on the square-grid format.
*   **Recommendation:** Implement the standard 4x4 grid rendering logic with proper sign placement (Aries at top-left-inner, etc.).

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

1.  **Modularize**: Move model and ephemeris logic out of `:app`.
2.  **Refactor Yogas**: Break down the 3000-line `YogaCalculator`.
3.  **Localization**: Extract all hardcoded strings to the localization system.
4.  **South Indian Chart**: Implement the missing rendering logic.
5.  **Sunrise/Sunset**: Use real ephemeris data for Shadbala and Panchanga.

---
*Analysis updated on 2026-01-14 by AstroStorm Senior Engineering Agent.*
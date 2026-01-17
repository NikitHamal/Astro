# Astro Storm - Codebase Audit & Findings

## Overview
A comprehensive audit of the "Astro Storm" Vedic astrology application has been conducted. The application features a robust Vedic calculation engine based on Swiss Ephemeris, a custom canvas-based chart renderer, and a sophisticated AI agent ("Stormy") with tool-calling capabilities.

## 1. Architecture & Best Practices

### Critical Issues
- **[RESOLVED] Threading in ViewModel:** `ChartViewModel` previously used `Executors.newSingleThreadExecutor()`.
  - **Action Taken:** Refactored all ViewModels to use Hilt-injected `IoDispatcher` (Kotlin Coroutines).
- **[RESOLVED] Dependency Injection:** The project lacked a DI framework.
  - **Action Taken:** Implemented **Hilt**. Added `AppModule` for singletons (`SwissEphemerisEngine`, `ChartDatabase`, `PromptManager`) and annotated all ViewModels, Repositories, and Managers with `@Inject` and `@HiltViewModel`.

### Database Design
- **[RESOLVED] JSON Storage:** `ChartEntity` stored `planetPositions` and `houseCusps` as raw JSON strings.
  - **Action Taken:** Refactored `ChartEntity` to use `List<PlanetPosition>` and `List<Double>` with Room `@TypeConverter`. Mapped new typed fields to existing JSON columns using `@ColumnInfo` to maintain schema compatibility. Simplified `ChartRepository` by removing manual JSON parsing.

## 2. Vedic Astrology Logic & Calculations

### Strengths
- **Swiss Ephemeris Integration:** Correctly uses `SwissEph` for high-precision calculations.
- **Parashari Principles:** `VedicAstrologyUtils` correctly implements core concepts like Dignity and Yogas.

### Areas for Improvement
- **[RESOLVED] House System Handling:** The engine defaulted to settings but didn't expose configuration for all calculators.
  - **Action Taken:** Updated `PrashnaCalculator` to accept a `HouseSystem` parameter.
- **[RESOLVED] Prashna Logic:** `PrashnaCalculatorWrapper` relied on profile defaults for context.
  - **Action Taken:** Refactored `PrashnaCalculatorWrapper` to use the injected `AstrologySettingsManager` for preferences and verified `PrashnaCalculator` uses `LocalDateTime.now()` for accurate timing. Updated `AstrologyToolRegistry` to provide settings access.

## 3. UI/UX & Rendering

### Strengths
- **Canvas Rendering:** Efficient custom views for North/South Indian charts.

### Areas for Improvement
- **[RESOLVED] Hardcoded Sizes:** `ChartRenderer` relied on magic numbers (e.g., `BASE_TEXT_SIZE_RATIO`).
  - **Action Taken:** Created `dimens.xml` with resource-backed dimensions. Refactored `ChartRenderer` to accept `Context` and load density-aware dimensions and ratios from resources.
- **State Management:** Manual hash-based deduplication in `ChartViewModel` is brittle.

## 4. AI & Agent System

### Strengths
- **Robust Tool Parsing:** Resilient parser for various LLM output formats.

### Improvements
- **[RESOLVED] Prompt Engineering:** System prompt generation was hardcoded in the Agent.
  - **Action Taken:** Extracted logic into a dedicated `@Singleton` `PromptManager`.
- **[RESOLVED] Context Limit Management:** No explicit handling of token limits for long chats.
  - **Action Taken:** Implemented sliding window logic in `ChatRepository.getConversationMessagesForApi` to keep input tokens within limits (default 4000) by trimming older messages while preserving the system prompt.

## 5. Security & Stability

- **[RESOLVED] Geocoding:** `GeocodingService` used a hardcoded User-Agent.
  - **Action Taken:** Refactored `GeocodingService` from `object` to `@Singleton` class. Injected `Context` to generate dynamic User-Agent (`AppName/Version (PackageName; Contact)`). Updated `ChartViewModel` and `LocationSearchField` to use the injected service.
- **Error Handling:** Generic exceptions in `ChartRepository`.
  - **Recommendation:** Granular error handling.

## 6. Testing

- **Missing Unit Tests:** (Deferred per instruction).

---

## Conclusion

The core application architecture has been significantly modernized. 
- **Hilt** handles dependency injection globally.
- **Coroutines** manage threading efficiently.
- **Database** interactions are type-safe.
- **AI** is more resilient with context management and modular prompts.
- **UI** rendering is density-aware and configurable.
- **Security** compliance for OSM/Nominatim is established.

The codebase is now in a much stronger state for future feature development and maintenance.
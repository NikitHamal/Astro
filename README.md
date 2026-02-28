# AstroVajra Onboarding Guide


## Overview


**AstroVajra** is a comprehensive Android application for Vedic astrology analysis, built with Kotlin and Jetpack Compose. The application serves astrology enthusiasts, practitioners, and students by providing professional-grade astrological calculations, interpretations, and insights.


### Purpose and User Value


AstroVajra enables users to:


- **Generate and analyze Vedic birth charts** with detailed planetary positions, house cusps, and Nakshatra placements
- **Calculate complex astrological metrics** including Shadbala (planetary strength), Ashtakavarga (point-based strength), and Divisional Charts (D1-D60)
- **Access predictive systems** such as Vimshottari Dasha (planetary periods), Transits, Varshaphala (annual predictions), and Prashna (horary astrology)
- **Perform compatibility analysis** through Guna Milan (Kundli matching) with detailed Dosha analysis
- **Receive personalized remedies** based on planetary afflictions and chart weaknesses
- **Interact with an AI assistant** (Stormy) that can answer astrological questions and perform calculations conversationally
- **Export professional reports** as PDFs, images, JSON, or CSV files


### Architecture at a Glance


The application follows a clean, layered architecture:


1. **UI Layer** (Jetpack Compose) - Navigation, screens, and visual components
2. **AI Layer** - Conversational interface with multi-model support and 30+ specialized tools
3. **Calculation Engine** (Ephemeris) - Core Vedic astrology computation logic
4. **Data Layer** - Room database for chart persistence, SharedPreferences for settings
5. **Visualization Layer** - Chart rendering and export capabilities
6. **Localization** - Multi-language support (English, Nepali, Hindi)


The codebase leverages modern Android development practices including:
- Kotlin coroutines for asynchronous operations
- Hilt for dependency injection
- StateFlow for reactive state management
- Room for database persistence
- Jetpack Compose for declarative UI
- Swiss Ephemeris for astronomical calculations


---


## Project Organization


### Repository Structure


```
astro/
├── .github/
│   └── workflows/
│       └── android.yml              # CI/CD pipeline for automated builds
├── app/
│   ├── build.gradle.kts             # App-level build configuration
│   └── src/main/java/com/astro/vajra/
│       ├── data/
│       │   ├── ai/                  # AI agent and provider abstractions
│       │   │   ├── agent/           # Stormy AI agent implementation
│       │   │   │   ├── StormyAgent.kt            # Main AI orchestrator
│       │   │   │   ├── PromptManager.kt          # System prompt generation
│       │   │   │   └── tools/                    # 30+ astrology tools
│       │   │   │       ├── AstrologyTools.kt     # Tool implementations
│       │   │   │       ├── AstrologyToolRegistry.kt
│       │   │   │       └── CalculationWrappers.kt
│       │   │   └── provider/        # AI model providers
│       │   │       └── AiProviderRegistry.kt
│       │   ├── database/            # Room database entities and DAOs
│       │   ├── localization/        # String resource management
│       │   │   ├── StringResources.kt
│       │   │   ├── StringKeyMatch.kt
│       │   │   ├── StringKeyAnalysis.kt
│       │   │   └── StringKeyDosha.kt
│       │   ├── model/               # Core data models
│       │   └── preferences/         # Settings managers
│       │       ├── OnboardingManager.kt
│       │       ├── ThemeManager.kt
│       │       ├── AstrologySettingsManager.kt
│       │       └── LocalizationManager.kt
│       ├── ephemeris/               # Calculation engine (Core importance: 30.54)
│       │   ├── ShadbalaCalculator.kt           # Six-fold planetary strength
│       │   ├── DashaCalculator.kt              # Vimshottari dasha periods
│       │   ├── DivisionalChartCalculator.kt    # D1-D60 varga charts
│       │   ├── AshtakavargaCalculator.kt       # Bindu point system
│       │   ├── VedicAstrologyUtils.kt          # Common utility functions
│       │   ├── YoginiDashaCalculator.kt
│       │   ├── KalachakraDashaCalculator.kt
│       │   ├── CharaDashaCalculator.kt
│       │   ├── VarshaphalaCalculator.kt
│       │   ├── PrashnaCalculator.kt
│       │   ├── TransitAnalyzer.kt
│       │   └── muhurta/
│       │       ├── MuhurtaCalculator.kt
│       │       └── MuhurtaAstronomicalCalculator.kt
│       ├── ui/
│       │   ├── navigation/
│       │   │   └── Navigation.kt               # App-wide navigation graph
│       │   ├── screen/
│       │   │   ├── main/
│       │   │   │   ├── MainScreen.kt          # Bottom navigation hub
│       │   │   │   ├── HomeTab.kt             # Feature discovery
│       │   │   │   └── SettingsTab.kt
│       │   │   ├── chartdetail/
│       │   │   │   └── tabs/                   # Chart analysis tabs
│       │   │   │       ├── ChartTabContent.kt
│       │   │   │       ├── PlanetsTabContent.kt
│       │   │   │       ├── YogasTabContent.kt
│       │   │   │       ├── DashasTabContent.kt
│       │   │   │       └── AshtakavargaTabContent.kt
│       │   │   ├── ChartAnalysisScreen.kt
│       │   │   ├── MuhurtaScreen.kt
│       │   │   ├── RemediesScreen.kt
│       │   │   ├── VarshaphalaScreen.kt
│       │   │   ├── PrashnaScreen.kt
│       │   │   ├── matchmaking/
│       │   │   │   ├── MatchmakingScreen.kt
│       │   │   │   └── MatchmakingComponents.kt
│       │   │   ├── YoginiDashaScreen.kt
│       │   │   ├── KalachakraDashaScreen.kt
│       │   │   └── CharaDashaScreen.kt
│       │   ├── chart/
│       │   │   └── ChartRenderer.kt           # Visual chart generation
│       │   └── components/
│       │       ├── ChartDialogs.kt
│       │       └── dialogs/
│       └── util/
│           └── ChartExporter.kt               # PDF/JSON/CSV/Image export
├── core/
│   ├── model/                       # Shared data models (VedicChart, Planet, etc.)
│   └── common/                      # Common utilities
├── build.gradle.kts                 # Root build configuration
├── settings.gradle.kts              # Module declarations
└── gradle.properties                # Gradle configuration
```


### Core Systems


#### 1. Navigation System (Importance: 81.60)


**Entry Point:** `app/src/main/java/com/astro/vajra/ui/navigation/Navigation.kt`


The `AstroVajraNavigation` composable defines the complete navigation graph with 40+ screens. The `Screen` sealed class provides type-safe route definitions with helper functions for parameter passing.


**Main Screen:** `app/src/main/java/com/astro/vajra/ui/screen/main/MainScreen.kt`


The bottom navigation hub with four tabs:
- **HomeTab** - Feature discovery grid showing available analyses
- **InsightsTab** - Aggregated chart insights
- **ChatTab** - AI assistant (Stormy) interface
- **SettingsTab** - User preferences and profile management


#### 2. Ephemeris Calculation Engine (Importance: 30.54)


**Location:** `app/src/main/java/com/astro/vajra/ephemeris/`


The calculation engine contains 20+ specialized calculators organized by function:


**Foundation:**
- `VedicAstrologyUtils.kt` - Common functions (dignities, relationships, aspects, combustion)
- `DivisionalChartCalculator.kt` - Generates D1-D60 varga charts


**Strength Analysis:**
- `ShadbalaCalculator.kt` - Six-fold planetary strength (Sthana, Dig, Kala, Chesta, Naisargika, Drik)
- `AshtakavargaCalculator.kt` - Bindu point system with Shodhana reduction


**Time Period Systems:**
- `DashaCalculator.kt` - Vimshottari dasha (6-level hierarchical periods)
- `YoginiDashaCalculator.kt` - Yogini dasha system
- `KalachakraDashaCalculator.kt` - Nakshatra-based Kalachakra system
- `CharaDashaCalculator.kt` - Jaimini Chara dasha


**Predictive Tools:**
- `TransitAnalyzer.kt` - Gochara analysis
- `VarshaphalaCalculator.kt` - Annual solar return predictions
- `PrashnaCalculator.kt` - Horary question analysis
- `MuhurtaCalculator.kt` - Electional astrology


**Compatibility:**
- `MatchmakingCalculator.kt` - Guna Milan with Dosha analysis


**Remedies:**
- `RemediesCalculator.kt` - Personalized corrective measures


All calculators accept `VedicChart` as input and return structured result objects.


#### 3. AI Assistant System (Importance: 27.72 + 28.29)


**Core Components:**


- `StormyAgent.kt` - Main AI orchestrator with iterative tool-calling loop
- `AstrologyToolRegistry.kt` - Manages 30+ specialized tools
- `AiProviderRegistry.kt` - Multi-provider abstraction (DeepInfra, Yqcloud, WeWordle)
- `PromptManager.kt` - Context-aware system prompt generation


**Tool Categories:**


1. **Profile Management** - get_profile_info, get_all_profiles, create_profile
2. **Chart Data Access** - get_planet_positions, get_house_positions, get_nakshatra_info
3. **Dasha & Timing** - get_dasha_info, get_current_dasha, calculate_muhurta
4. **Yoga & Strength** - get_yogas, get_ashtakavarga, get_strength_analysis
5. **Compatibility** - get_compatibility, get_deep_compatibility
6. **Advanced Analysis** - get_divisional_chart, get_bhrigu_bindu, get_argala


The agent supports autonomous multi-step reasoning with up to 15 tool iterations.


#### 4. Chart Visualization (Importance: 40.26 + 24.09)


**Chart Rendering:** `app/src/main/java/com/astro/vajra/ui/chart/ChartRenderer.kt`


Draws North Indian and South Indian style charts with:
- Planetary positions with degree precision
- Dignity indicators (exaltation arrows, debilitation arrows)
- Retrograde/Combust/Vargottama symbols
- House numbers and chart legends
- Three color schemes (Light, Dark, Print)


**Chart Export:** `app/src/main/java/com/astro/vajra/util/ChartExporter.kt`


Generates professional reports in multiple formats:
- **PDF** - Multi-page reports with charts, positions, analysis, predictions
- **JSON** - Structured data export
- **CSV** - Tabular data export
- **Image** - High-resolution chart bitmaps


#### 5. Data Persistence


**Room Database:** Chart and profile entities with DAOs
**SharedPreferences:** Lightweight config via specialized managers:
- `OnboardingManager` - First-time setup status
- `ThemeManager` - UI theme (Light/Dark/System)
- `AstrologySettingsManager` - Ayanamsa, house system, node mode
- `LocalizationManager` - Language and date system


All managers expose reactive `StateFlow` objects for UI observation.


#### 6. Localization System (Importance: 81.60 + 28.13)


**Location:** `app/src/main/java/com/astro/vajra/data/localization/`


String resources are organized by domain:
- `StringKeyMatch.kt` - UI navigation and matching
- `StringKeyAnalysis.kt` - Analysis and formatting strings
- `StringKeyDosha.kt` - Dosha/transit analysis
- `StringKeyRemedy.kt` - Remedy descriptions


`StringResources.kt` provides the lookup mechanism with multi-language support (English, Nepali, Hindi).


### Key Screens and Their Functions


| Screen | Purpose | Importance |
|--------|---------|-----------|
| `MainScreen.kt` | Bottom navigation hub | 81.60 |
| `ChartAnalysisScreen.kt` | Comprehensive chart view with 7 tabs | 24.75 |
| `MuhurtaScreen.kt` | Auspicious timing selection | 24.75 |
| `MatchmakingScreen.kt` | Compatibility analysis | 32.62 |
| `VarshaphalaScreen.kt` | Annual horoscope predictions | 31.31 |
| `PrashnaScreen.kt` | Horary question analysis | 32.62 |
| `RemediesScreen.kt` | Personalized corrective measures | 24.75 |
| `YoginiDashaScreen.kt` | Yogini dasha periods | 35.89 |
| `KalachakraDashaScreen.kt` | Kalachakra dasha analysis | 35.89 |
| `CharaDashaScreen.kt` | Chara dasha (Jaimini) | 35.89 |


### Build System (Importance: 38.99)


**Gradle Configuration:**
- `build.gradle.kts` (root) - Plugin versions
- `app/build.gradle.kts` - App dependencies and build types
- `settings.gradle.kts` - Module structure
- `gradle.properties` - Build optimizations


**Key Dependencies:**
- Jetpack Compose BOM 2024.02.00
- Hilt 2.51 (Dependency Injection)
- Room 2.6.1 (Database)
- Navigation Compose 2.7.7
- Coroutines 1.7.3
- Markwon 4.6.2 (Markdown rendering)


**CI/CD:** `.github/workflows/android.yml`
- Triggers on push to main paths (excludes `wip/**` branches)
- Builds release APK with ProGuard
- Renames with commit hash
- Uploads artifact (90-day retention)


### Development Practices


**Code Style:**
- Kotlin with JVM target 17
- Annotation processing via KSP (not KAPT)
- Compose-first UI (no XML layouts)
- Singleton pattern for managers and calculators


**State Management:**
- `StateFlow` for reactive state
- `rememberSaveable` for UI state persistence
- ViewModels with Hilt injection


**Threading:**
- `Dispatchers.IO` for calculations and database
- `Dispatchers.Default` for CPU-intensive work
- Structured concurrency with coroutine scopes


**Error Handling:**
- Sealed classes for state (Loading/Success/Error)
- Try-catch blocks with user-friendly messages
- Timeout protection for long calculations (30s)


---


## Glossary of Codebase-Specific Terms


### Core Data Models


**VedicChart**
- `data class` representing a complete Vedic birth chart
- Fields: `birthData`, `planetPositions`, `houseCusps`, `ayanamsa`, `julianDay`
- Used as input to all calculation functions
- Location: `core/model/`


**PlanetPosition**
- `data class` with planet's zodiacal position
- Fields: `planet`, `sign`, `longitude`, `nakshatra`, `pada`, `house`, `isRetrograde`, `isCombust`
- Location: `core/model/`


**SavedChart** / **ChartEntity**
- Room database entity for persisting charts
- Convertible to/from `VedicChart` via extension functions
- Location: `app/src/main/java/com/astro/vajra/data/database/`


**InsightFeature**
- `enum class` defining available analysis types
- Values: `FULL_CHART`, `PLANETS`, `YOGAS`, `DASHAS`, `TRANSITS`, `ASHTAKAVARGA`, `PANCHANGA`
- Used for navigation and feature flags
- Location: `HomeTab.kt:454-586`


### Navigation & UI


**AstroVajraNavigation**
- Main `@Composable` navigation graph setup
- Defines 40+ screen routes with parameters
- Location: `ui/navigation/Navigation.kt:284-1087`


**Screen** (sealed class)
- Type-safe route definitions
- Each route has a `createRoute()` helper for parameter passing
- Examples: `Screen.Main`, `Screen.ChartAnalysis`, `Screen.Matchmaking`
- Location: `ui/navigation/Navigation.kt:76-272`


**MainScreen**
- Bottom navigation hub with 4 tabs
- Entry point after onboarding
- Uses `rememberSaveable` for tab persistence
- Location: `ui/screen/main/MainScreen.kt:70-333`


**HomeTab**
- Feature discovery interface with grid layout
- Shows implemented vs. coming-soon features
- Location: `ui/screen/main/HomeTab.kt:48-663`


**ProfileSwitcher**
- UI component for switching between saved charts
- Displayed as bottom sheet from MainTopBar
- Location: `ui/screen/main/MainScreen.kt`


### Calculation Engine Terms


**Shadbala**
- Six-fold planetary strength system from BPHS
- Components: Sthana, Dig, Kala, Chesta, Naisargika, Drik
- Output: `PlanetaryShadbala` with total Rupas and strength rating
- Location: `ephemeris/ShadbalaCalculator.kt:249-264`


**Ashtakavarga**
- Point-based (bindu) strength system
- Three types: Bhinnashtakavarga (per planet), Sarvashtakavarga (total), Prastarashtakavarga (detailed)
- Includes Shodhana (reduction) process
- Location: `ephemeris/AshtakavargaCalculator.kt:237-264`


**Vimshottari Dasha**
- 120-year planetary period system
- Six levels: Mahadasha → Antardasha → Pratyantardasha → Sookshmadasha → Pranadasha → Dehadasha
- Starting point based on Moon's Nakshatra
- Location: `ephemeris/DashaCalculator.kt:309-370`


**DashaTimeline**
- `data class` containing complete dasha sequence
- Fields: list of Mahadashas, current periods at all 6 levels, upcoming Sandhis
- Location: `ephemeris/DashaCalculator.kt:199-299`


**Divisional Chart** (Varga)
- Micro-charts calculated by dividing zodiac signs
- Types: D1 (Rashi), D2 (Hora), D9 (Navamsa), D12, D30, D60, etc.
- Used for specialized analysis (marriage, career, etc.)
- Location: `ephemeris/DivisionalChartCalculator.kt`


**Vargottama**
- Planet occupying same sign in D1 and D9 charts
- Indicates strength and consistency
- Symbol: ¤ in chart rendering
- Calculated in: `DivisionalChartCalculator`


**Nakshatra**
- 27 lunar mansions (13°20' each)
- Determines dasha starting point and compatibility
- Each divided into 4 padas (quarters)
- Used throughout calculation engine


**Ayanamsa**
- Precession correction for zodiac alignment
- Options: Lahiri, Raman, KP, etc.
- User-configurable via `AstrologySettingsManager`
- Affects all planetary calculations


**Deha-Jeeva**
- Body (Deha) and Soul (Jeeva) rashis in Kalachakra Dasha
- Deha = Navamsa sign, Jeeva = 5th from Deha
- Relationship determines health/spiritual predictions
- Location: `ephemeris/KalachakraDashaCalculator.kt:427-444`


**Guna Milan**
- Compatibility scoring for matchmaking
- Eight Kutas: Varna, Vashya, Tara, Yoni, Graha, Gana, Bhakoot, Nadi
- Maximum: 36 points
- Location: `matchmaking/MatchmakingScreen.kt`


**Dosha**
- Astrological affliction (e.g., Manglik, Nadi, Bhakoot)
- Analyzed in matchmaking and remedies
- Requires specific remedial measures
- String keys: `StringKeyDosha.kt`


**Yoga**
- Planetary combination with specific effects
- Categories: Raj Yoga (auspicious), Dosha Yoga (affliction)
- Calculated from planetary positions and relationships
- Location: `ephemeris/YogaCalculator.kt`


**Varshaphala**
- Annual solar return chart and predictions
- Includes Muntha, Tajika aspects, Mudda Dasha
- Calculated for specific age/year
- Location: `ephemeris/VarshaphalaCalculator.kt`


**Prashna**
- Horary astrology for specific questions
- Chart generated for question time, not birth
- Analyzes Moon and Lagna for answers
- Location: `ephemeris/PrashnaCalculator.kt`


**Muhurta**
- Electional astrology for auspicious timing
- Considers Panchanga, Choghadiya, Rahu Kala
- Activity-specific recommendations
- Location: `ephemeris/muhurta/MuhurtaCalculator.kt`


**Panchanga**
- Five limbs: Vara (weekday), Tithi (lunar day), Nakshatra, Yoga, Karana
- Daily Vedic calendar elements
- Used in Muhurta and general analysis
- Calculated via Swiss Ephemeris


**Choghadiya**
- Eight time periods in day/night
- Types: Amrit (excellent), Shubh (good), Labh (gain), Chal (moving), Udveg (anxiety), Roga (illness), Kaal (death), Rog (affliction)
- Location: `MuhurtaScreen.kt`


### AI System Terms


**Stormy / StormyAgent**
- AI assistant name and main orchestrator class
- Processes messages with iterative tool-calling loop
- Supports up to 15 tool iterations per query
- Location: `data/ai/agent/StormyAgent.kt:20-285`


**AstrologyToolRegistry**
- Manages 30+ specialized astrology tools
- Provides `executeTool()` with fuzzy matching
- Generates tool descriptions for AI system prompt
- Location: `data/ai/agent/tools/AstrologyToolRegistry.kt:22-298`


**AstrologyTool**
- Interface for AI-callable functions
- Methods: `execute(arguments, context)`, parameter definitions
- Examples: GetProfileInfoTool, GetDashaInfoTool, GetCompatibilityTool
- Location: `data/ai/agent/tools/AstrologyTools.kt`


**ToolContext**
- `data class` passed to tool execution
- Fields: `currentProfile`, `currentChart`, `allProfiles`, `database`, `context`
- Provides all dependencies tools need
- Location: `AstrologyToolRegistry.kt:269-282`


**AiProviderRegistry**
- Multi-provider abstraction for AI models
- Manages model configuration (enabled, aliases, custom models)
- Providers: DeepInfra, Yqcloud, WeWordle
- Location: `data/ai/provider/AiProviderRegistry.kt:20-296`


**AgentResponse** (sealed class)
- Streaming response types from Stormy
- Variants: `ContentChunk`, `ReasoningChunk`, `ToolExecuting`, `ToolResult`, `Complete`, `Error`
- Enables real-time UI updates
- Location: `StormyAgent.kt:448-515`


**CalculationWrappers**
- Adapter layer between AI tools and ephemeris calculators
- Formats calculation results as JSON-serializable objects
- Examples: `VimshottariDashaCalculator`, `YogaCalculatorWrapper`
- Location: `data/ai/agent/tools/CalculationWrappers.kt`


### Rendering & Export


**ChartRenderer**
- `class` for drawing Vedic charts on Canvas
- Supports North Indian and South Indian styles
- Methods: `drawNorthIndianChart()`, `drawSouthIndianChart()`, `createChartBitmap()`
- Location: `ui/chart/ChartRenderer.kt:81-850`


**ChartColorConfig**
- `data class` defining chart color scheme
- Predefined: `Light`, `Dark`, `Print`
- Fields: `frameColor`, `planetColor`, `exaltedColor`, etc.
- Location: `ui/chart/ChartRenderer.kt:19-79`


**ChartExporter**
- Generates export artifacts (PDF, JSON, CSV, images)
- PDF: Multi-page reports with charts and analysis
- Methods: `exportToPdf()`, `exportToJson()`, `exportToCsv()`, `exportToImage()`
- Location: `util/ChartExporter.kt:67-398`


**PlanetaryDignity** (enum)
- Visual dignity indicators in charts
- Values: `EXALTED` (↑), `DEBILITATED` (↓), `MOOL_TRIKONA` (△), `OWN_SIGN` (⬠), `NEUTRAL`
- Calculated via `VedicAstrologyUtils`
- Location: `ui/chart/ChartRenderer.kt:114-120`


### Settings & Preferences


**ThemeManager**
- Singleton managing app theme
- Modes: `LIGHT`, `DARK`, `SYSTEM`
- Exposes `StateFlow<ThemeMode>`
- Location: `data/preferences/ThemeManager.kt:19-85`


**AstrologySettingsManager**
- Singleton managing calculation parameters
- Settings: `nodeMode` (MEAN/TRUE), `ayanamsa`, `houseSystem`
- Influences all ephemeris calculations
- Location: `data/preferences/AstrologySettingsManager.kt:20-105`


**LocalizationManager**
- Singleton managing language and date system
- Languages: English, Nepali, Hindi
- Date systems: AD (Gregorian), BS (Bikram Sambat)
- Location: `data/preferences/LocalizationManager.kt`


**StringResources**
- Central localization lookup
- Method: `get(key: StringKey, language: Language, ...args)`
- Domain-specific key enums: StringKeyMatch, StringKeyAnalysis, StringKeyDosha, StringKeyRemedy
- Location: `data/localization/StringResources.kt`


### Database Terms


**ChartViewModel**
- ViewModel managing chart state with Hilt injection
- Methods: `saveChart()`, `deleteChart()`, `getChartById()`
- Exposes: `savedCharts`, `uiState`, `selectedChartId` StateFlows
- Location: Inferred from usage


**ChartDao**
- Room DAO for chart database operations
- CRUD methods for `ChartEntity`
- Location: `data/database/`


**OnboardingManager**
- Singleton tracking first-time setup status
- Exposes `StateFlow<Boolean>` for completion status
- Methods: `completeOnboarding()`, `resetOnboarding()`
- Location: `data/preferences/OnboardingManager.kt:16-66`


### Utility Terms


**VedicAstrologyUtils**
- Static utility object with common astrology functions
- Functions: `isExalted()`, `getDignity()`, `areNaturalFriends()`, `aspectsHouse()`
- Delegates to `AstrologicalConstants` for data
- Location: `ephemeris/VedicAstrologyUtils.kt`


**SwissEphemeris** / **SwissEph**
- Third-party astronomical calculation library
- Used for precise planetary positions
- Wrapped in `MuhurtaAstronomicalCalculator`
- Not directly exposed in main codebase


This glossary covers the essential domain-specific terminology needed to navigate the AstroVajra codebase effectively. Engineers should familiarize themselves with these terms when starting work on the project, as they form the common vocabulary used throughout discussions, code reviews, and documentation.

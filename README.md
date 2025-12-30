# AstroStorm Codebase Onboarding Guide


## Overview


**AstroStorm** is a comprehensive Android application for Vedic astrology, combining traditional astrological calculations with modern AI-powered insights. The application serves users interested in birth chart analysis, astrological predictions, compatibility matching, and personalized consultations.


### Purpose and Target Users


The application enables users to:
- **Generate and manage birth charts** with precise astronomical calculations using Swiss Ephemeris
- **Access 30+ specialized astrological analyses** including planetary periods (Dashas), yogas (planetary combinations), strength assessments (Ashtakavarga, Shadbala), and predictive tools (Varshaphala, Prashna)
- **Receive AI-powered astrological consultations** through an intelligent agent that can execute specialized calculation tools and provide contextual interpretations
- **Perform compatibility analysis** for relationships using traditional Vedic matchmaking techniques
- **Export detailed reports** in multiple formats (PDF, JSON, CSV, images) for personal records or sharing


### Key Capabilities


1. **Multi-language Support**: Full internationalization with English and Nepali, including support for both Anno Domini (AD) and Bikram Sambat (BS) calendar systems
2. **AI Integration**: Conversational AI assistant with access to 20+ astrological calculation tools, enabling dynamic analysis and personalized guidance
3. **Professional Visualization**: High-quality chart rendering in North and South Indian styles with multiple color themes
4. **Comprehensive Calculations**: 15+ specialized calculators covering basic (Dashas, Yogas), advanced (Ashtakavarga, Divisional Charts), predictive (Varshaphala, Muhurta), and specialized (Jaimini techniques, Graha Yuddha) astrological methods
5. **Persistent Storage**: Room database for chart management and chat history with efficient caching strategies


## Project Organization


### Directory Structure


```
app/src/main/java/com/astro/storm/
├── ui/                          # User Interface Layer
│   ├── screen/                  # Feature screens (30+ specialized analysis screens)
│   │   ├── main/               # Main screens (HomeTab, InsightsTab, ChatTab, SettingsTab)
│   │   ├── chartdetail/        # Chart detail and tabbed analysis views
│   │   ├── matchmaking/        # Compatibility analysis
│   │   ├── ashtakavarga/       # Ashtakavarga bindu system
│   │   ├── panchanga/          # Vedic calendar calculations
│   │   └── transits/           # Current planetary movements
│   ├── viewmodel/              # ViewModels for state management
│   ├── components/             # Reusable UI components
│   │   ├── agentic/           # AI message display components
│   │   └── dialogs/           # Dialog components
│   ├── navigation/             # Navigation graph and routing
│   └── chart/                  # Chart rendering components
│
├── data/                        # Data Layer
│   ├── model/                  # Core data models (VedicChart, BirthData, etc.)
│   ├── repository/             # Data repositories
│   ├── local/                  # Local persistence (Room database)
│   │   ├── chart/             # Chart database entities and DAOs
│   │   └── chat/              # Chat database entities and DAOs
│   ├── api/                    # External API services (Geocoding)
│   ├── ai/                     # AI integration
│   │   ├── agent/             # StormyAgent orchestration
│   │   │   └── tools/         # Astrology tools for AI (20+ tools)
│   │   └── provider/          # AI provider implementations
│   ├── localization/           # Internationalization resources
│   └── preferences/            # App preferences (theme, language)
│
├── ephemeris/                   # Astrological Calculation Layer
│   ├── DashaCalculator.kt      # Vimshottari dasha system
│   ├── YogaCalculator.kt       # Planetary combinations
│   ├── AshtakavargaCalculator.kt  # Bindu strength system
│   ├── VarshaphalaCalculator.kt   # Annual horoscope (Tajika)
│   ├── MatchmakingCalculator.kt   # Compatibility analysis
│   ├── PanchangaCalculator.kt     # Vedic calendar elements
│   ├── TransitAnalyzer.kt         # Current planetary effects
│   ├── VedicAstrologyUtils.kt     # Common astrological functions
│   └── [15+ more calculators]     # Specialized calculations
│
└── util/                        # Utility Layer
    ├── ChartUtils.kt           # Chart-related utilities
    ├── ChartExporter.kt        # Export functionality (PDF, JSON, CSV)
    └── ContentCleaner.kt       # Content formatting utilities
```


### Core Systems


#### 1. Navigation System (`ui/navigation/`)
- **`AstroStormNavigation`**: Defines the navigation graph with 30+ routes
- **`Screen` sealed class**: Type-safe route definitions with parameter handling
- **`MainScreen`**: Primary container with bottom navigation (Home, Insights, Chat, Settings)


#### 2. UI State Management (`ui/viewmodel/`)
- **`ChartViewModel`**: Manages chart calculation, persistence, and export
- **`ChatViewModel`**: Orchestrates AI conversations with tool execution and streaming
- **`InsightsViewModel`**: Concurrent calculation of Dashas, horoscopes, and planetary influences
- **`DashaViewModel`**: Specialized Dasha timeline management with caching


#### 3. AI Agent System (`data/ai/`)
- **`StormyAgent`**: Core AI orchestration with streaming support
- **`AstrologyTool` interface**: Contract for AI-invokable tools (20+ implementations)
- **`CalculationWrappers`**: Standardizes calculator outputs for AI consumption
- **`AiProviderRegistry`**: Abstraction over multiple LLM backends (OpenAI, Anthropic, custom)


#### 4. Calculation Engine (`ephemeris/`)
- **Swiss Ephemeris Integration**: Astronomical calculations via SwissEph library
- **Specialized Calculators**: 15+ modules for different astrological techniques
- **`VedicAstrologyUtils`**: Centralized utility functions (dignity checks, house classifications, nakshatra attributes)
- **`AstrologicalConstants`**: Single source of truth for astrological parameters


#### 5. Data Persistence (`data/local/`)
- **Room Database**: Stores charts, chat conversations, and messages
- **`ChartRepository`**: Chart CRUD operations with caching
- **`ChatDao`**: Comprehensive chat persistence with Flow-based reactivity


#### 6. Localization System (`data/localization/`)
- **`StringResources`**: Central translation hub supporting English and Nepali
- **`StringKey` hierarchy**: Type-safe string keys organized by feature domain
- **Date system support**: Both Anno Domini (AD) and Bikram Sambat (BS) calendars


### Main Entry Points


1. **`MainActivity.kt`**: Application entry point, sets up theme and navigation
2. **`AstroStormNavigation`** (`ui/navigation/Navigation.kt`): Routing configuration
3. **`MainScreen`** (`ui/screen/main/MainScreen.kt`): Primary UI container with tab navigation
4. **`HomeTab`** (`ui/screen/main/HomeTab.kt`): Feature discovery interface displaying 30+ analyses
5. **`ChatTab`** (`ui/screen/main/ChatTab.kt`): AI assistant interface
6. **`ChartInputScreen`** (`ui/screen/ChartInputScreen.kt`): Birth data entry with location search


### Key Classes and Functions


#### UI Layer
- **`InsightFeature` enum** (`HomeTab.kt`): Defines all 30+ features with metadata (title, description, icon, color, implementation status)
- **`AgenticMessageCard`** (`components/agentic/`): Modern IDE-style message display for AI responses
- **`ChartRenderer`** (`ui/chart/ChartRenderer.kt`): Draws Vedic charts in North/South Indian styles with theme support
- **`ProfileSwitcherBottomSheet`** (`components/ProfileSwitcherBottomSheet.kt`): Chart selection and management UI


#### ViewModel Layer
- **`ChatViewModel.sendMessage()`**: Handles message streaming with section management and tool execution
- **`ChartViewModel.calculateChart()`**: Orchestrates chart calculation with validation and persistence
- **`InsightsViewModel.loadInsights()`**: Concurrent calculation of multiple insight types with graceful error handling


#### Calculation Layer
- **`DashaCalculator.calculateDashaTimeline()`**: Generates complete Vimshottari dasha periods (6 nested levels)
- **`VarshaphalaCalculator.calculateVarshaphala()`**: Comprehensive annual horoscope using Tajika system
- **`MatchmakingCalculator.calculateMatchmaking()`**: Ashtakoota Guna compatibility with dosha analysis
- **`AshtakavargaCalculator.calculateAshtakavarga()`**: Bindu strength calculation for all planets and houses


#### AI Integration
- **`StormyAgent.processMessage()`**: Main agent loop with reasoning, tool calls, and content generation
- **`AstrologyTool.execute()`**: Interface method for tool execution with `JSONObject` arguments and `ToolContext`
- **`CalculationWrappers`**: Static wrapper methods providing standardized data formats for 15+ calculators


#### Utilities
- **`ChartUtils.generateChartKey()`**: Deterministic cache key generation from birth data
- **`ChartExporter.exportToPdf()`**: Multi-page PDF report generation with chart images and analyses
- **`ContentCleaner.cleanForDisplay()`**: Removes tool call artifacts from AI responses


## Glossary of Codebase-Specific Terms


### Core Data Models


**VedicChart**
- Complete birth chart data model containing `BirthData`, planet positions, house cusps, ayanamsa
- Primary input for all astrological calculations
- Location: `data/model/VedicChart.kt`
- Used by: All calculators, ChartViewModel, AI tools


**BirthData**
- User input data: name, date/time, location coordinates, timezone, gender
- Converted to `VedicChart` via SwissEph calculations
- Location: `data/model/BirthData.kt`
- Created by: ChartInputScreen, validated before calculation


**PlanetPosition**
- Detailed planetary data: longitude, sign, nakshatra, house, retrograde status, degree
- Contained within `VedicChart`
- Location: `data/model/PlanetPosition.kt`
- Used by: All strength and dignity calculations


**SavedChart**
- Persistent chart entity with ID for database storage and profile management
- Maps to `ChartEntity` for Room database
- Location: `data/model/SavedChart.kt`
- Used by: ChartRepository, ProfileSwitcherBottomSheet


### UI Architecture


**InsightFeature**
- Enum defining all 30+ astrological analyses with UI metadata (title, description, icon, color)
- Method: `getLocalizedTitle(language)`, `getLocalizedDescription(language)`
- Location: `ui/screen/main/HomeTab.kt`
- Powers: Feature grid display, navigation routing


**MainTab**
- Enum for bottom navigation: HOME, INSIGHTS, CHAT, SETTINGS
- Location: `ui/screen/main/MainScreen.kt`
- Determines: Tab content rendering in MainScreen


**AgenticMessageCard**
- Modern IDE-inspired UI component for displaying AI messages with reasoning, tools, and content sections
- Location: `ui/components/agentic/AgenticMessageComponents.kt`
- Used by: ChatScreen for real-time streaming messages


**SectionedMessageCard**
- Advanced message UI using dynamic `AgentSection` types for structured AI responses
- Supports: Reasoning, ToolGroups, Content, AskUser interactions, TodoLists, TaskBoundaries
- Location: `ui/components/agentic/SectionedMessageCard.kt`
- Used by: ChatScreen for both streaming and completed messages


**ChartRenderer**
- Draws Vedic charts in North/South Indian diamond/square formats with theme-aware colors
- Method: `drawNorthIndianChart(drawScope, chart, size)`
- Location: `ui/chart/ChartRenderer.kt`
- Used by: Chart screens, ChartExporter for PDF generation


### State Management


**ChartUiState**
- Sealed class: Initial, Loading, Calculating, Success(VedicChart), Error(String), Saved, Exporting, Exported
- Location: `ui/viewmodel/ChartViewModel.kt`
- Drives: UI rendering in ChartInputScreen and other chart-related screens


**StreamingMessageState**
- Real-time AI message state with `content`, `reasoning`, `toolSteps` for live updates
- Location: `ui/viewmodel/ChatViewModel.kt`
- Used by: AgenticMessageCard during message streaming


**SectionedMessageState**
- Structured message state with chronologically-ordered `AgentSection` list
- Enables: IDE-style message display with collapsible sections
- Location: `ui/viewmodel/ChatViewModel.kt`
- Used by: SectionedMessageCard for advanced message rendering


**InsightsUiState**
- Sealed class: Idle, Loading, Success(InsightsData), Error(String)
- Contains: DashaTimeline, horoscopes (today/tomorrow/weekly), planetary influences, error list
- Location: `ui/viewmodel/InsightsViewModel.kt`
- Drives: InsightsTab display with graceful degradation on partial failures


### AI System


**StormyAgent**
- Core AI orchestration class handling conversations, tool calls, and streaming responses
- Method: `processMessage(messages, model): Flow<AgentResponse>`
- Location: `data/ai/agent/StormyAgent.kt`
- Integrates: AiProviderRegistry, AstrologyTools, ToolContext


**AstrologyTool**
- Interface for AI-invokable functions with `name`, `description`, `parameters`, `execute()` method
- 20+ implementations: get_profile_info, get_planet_positions, get_dashas, get_yogas, etc.
- Location: `data/ai/agent/tools/AstrologyTools.kt`
- Pattern: Each tool receives `JSONObject` arguments and `ToolContext` with chart access


**ToolContext**
- Dependency injection container providing AI tools with: currentChart, allProfiles, database access
- Location: `data/ai/agent/tools/AstrologyTools.kt`
- Purpose: Enables tools to access user data and perform contextual analyses


**CalculationWrappers**
- Static wrapper functions standardizing calculator outputs for AI consumption
- Wraps: 15+ calculators into consistent data class outputs
- Location: `data/ai/agent/tools/CalculationWrappers.kt`
- Purpose: Bridge between raw calculators and AI tool system


**AgentResponse**
- Sealed class for streaming AI output: ReasoningChunk, ContentChunk, ToolCallsStarted, ToolResult, Complete, Error
- Location: `data/ai/agent/StormyAgent.kt`
- Used by: ChatViewModel to build StreamingMessageState and SectionedMessageState


**AgentSection**
- Sealed interface for message sections: Reasoning, ToolGroup, Content, AskUser, TodoList, TaskBoundary, ProfileOperation
- Location: `ui/viewmodel/ChatViewModel.kt`
- Enables: Chronological message construction with heterogeneous content types


### Astrological Calculations


**DashaCalculator**
- Calculates Vimshottari planetary period system (120-year cycle, 6 nested levels)
- Method: `calculateDashaTimeline(chart): DashaTimeline`
- Location: `ephemeris/DashaCalculator.kt`
- Output: Complete timeline from Mahadasha through Dehadasha with dates and durations


**YogaCalculator**
- Identifies auspicious and inauspicious planetary combinations (Raja Yoga, Dhana Yoga, Parivartana, etc.)
- Method: `calculateYogas(chart): YogaAnalysis`
- Location: `ephemeris/YogaCalculator.kt`
- Categories: Wealth, power, spirituality, health, obstacles


**AshtakavargaCalculator**
- Calculates bindu (strength points) system for planets and signs
- Method: `calculateAshtakavarga(chart): AshtakavargaAnalysis`
- Location: `ephemeris/AshtakavargaCalculator.kt`
- Output: Sarvashtakavarga (total), Bhinnashtakavarga (per planet)


**VarshaphalaCalculator**
- Annual horoscope calculation using Tajika system with solar return chart
- Method: `calculateVarshaphala(natalChart, year): VarshaphalaResult`
- Location: `ephemeris/VarshaphalaCalculator.kt`
- Includes: Solar return, Muntha, Sahams, Tajika aspects, Mudda Dasha, house predictions


**MatchmakingCalculator**
- Traditional Vedic compatibility analysis using Ashtakoota Guna system
- Method: `calculateMatchmaking(chart1, chart2): MatchmakingResult`
- Location: `ephemeris/MatchmakingCalculator.kt`
- Analyzes: 8 Gunas, Manglik dosha, Nadi dosha, nakshatra compatibility


**VedicAstrologyUtils**
- Centralized utility object for common astrological calculations
- Functions: `isExalted()`, `isDebilitated()`, `isInOwnSign()`, `isKendra()`, `getGana()`, `aspectsHouse()`
- Location: `ephemeris/VedicAstrologyUtils.kt`
- Used by: All calculators for atomic operations and dignity checks


**AstrologicalConstants**
- Single source of truth for astrological parameters: exaltation/debilitation signs, house categories
- Location: Referenced by VedicAstrologyUtils
- Purpose: Ensures consistency across all calculations


### Specialized Terms


**Dasha**
- Planetary period system in Vedic astrology; Vimshottari uses 9 planets in 120-year cycle
- Levels: Mahadasha (years), Antardasha (months), Pratyantardasha (days), Sookshmadasha (hours), Pranadasha (minutes), Dehadasha
- Location: `ephemeris/DashaCalculator.kt`, `ui/screen/chartdetail/tabs/DashasTabContent.kt`


**DashaSandhi**
- Transition period between two Dasha periods, considered sensitive time
- Method: `getUpcomingSandhisWithin(days)` returns upcoming transitions
- Location: `ephemeris/DashaCalculator.kt`
- UI: Displayed in SandhiAlertsCard with color-coded urgency


**Nakshatra**
- One of 27 lunar mansions dividing the zodiac; each planet's nakshatra influences characteristics
- Attributes: Gana (temperament), Yoni (animal symbol), Nadi (pulse), Varna (caste), Rajju (rope type)
- Location: `data/model/Nakshatra.kt`, `ephemeris/VedicAstrologyUtils.kt`


**Ashtakavarga**
- Point-based strength system using "bindus" (dots) for each planet in each sign
- Sarvashtakavarga: Combined strength (total bindus); Bhinnashtakavarga: Individual planet strength
- Location: `ephemeris/AshtakavargaCalculator.kt`, `ui/screen/ashtakavarga/`
- Range: 0-56 bindus per sign for Sarvashtakavarga


**Yoga**
- Planetary combination producing specific effects (e.g., Raja Yoga for power, Dhana Yoga for wealth)
- Special yogas: Pancha Mahapurusha, Viparita Raja, Neecha Bhanga
- Location: `ephemeris/YogaCalculator.kt`, `ui/screen/chartdetail/tabs/YogasTabContent.kt`


**Ayanamsa**
- Precession correction applied to convert tropical to sidereal zodiac
- Systems: Lahiri (most common), Raman, Krishnamurti, etc.
- Location: Chart calculation parameters, affects all planetary positions


**Panchanga**
- Five limbs of Vedic calendar: Tithi (lunar day), Vara (weekday), Nakshatra, Yoga, Karana
- Method: `calculatePanchanga(dateTime, lat, lon, timezone): PanchangaData`
- Location: `ephemeris/PanchangaCalculator.kt`, `ui/screen/panchanga/`


**Muhurta**
- Electional astrology for finding auspicious timing for activities
- Method: `calculateMuhurta(activity, location, daysAhead): List<MuhurtaTime>`
- Location: `ephemeris/MuhurtaCalculator.kt`, `ui/screen/MuhurtaScreen.kt`


**Maraka**
- Death-inflicting planets (typically 2nd and 7th house lords); used in longevity analysis
- Severity: VERY_HIGH, HIGH, MODERATE, LOW, MINIMAL, NONE
- Location: `ephemeris/MarakaCalculator.kt`


**Badhaka**
- Obstruction-causing planets based on ascendant type (movable/fixed/dual)
- Analyzes: Career, health, relationship, financial obstacles
- Location: `ephemeris/BadhakaCalculator.kt`


### Localization


**StringResources**
- Central localization hub with `get(key: StringKeyInterface, language: Language): String`
- Supports: English, Nepali with parameter interpolation
- Location: `data/localization/StringResources.kt`
- Used by: All UI components via `stringResource()` Composable


**StringKey**
- Enum implementing `StringKeyInterface` with `en` and `ne` properties for each string
- Split into: StringKey (core UI), StringKeyMatch (matchmaking), StringKeyAnalysis (calculations)
- Location: `data/localization/StringResources.kt`
- Pattern: Type-safe string access prevents missing translations


**DateSystem**
- Enum: AD (Anno Domini), BS (Bikram Sambat - Nepali calendar)
- Conversion: `BikramSambatConverter.toBS()` and `toAD()`
- Location: Date input screens, preference settings
- Note: Internal storage always uses AD; BS is display/input convenience


### Performance and Caching


**ChartUtils.generateChartKey()**
- Deterministic cache key from birth timestamp, coordinates (6 decimals), ayanamsa, timezone
- Pattern: `epochSeconds|latInt|lonInt|ayanamsa|timezone`
- Location: `util/ChartUtils.kt`
- Used by: ChartViewModel, DashaViewModel, InsightsViewModel for cache management


**Throttling**
- Performance optimization: 50ms for UI updates (~20 FPS), 500ms for DB writes
- Implementation: Timestamp checking in streaming message updates
- Location: `ui/viewmodel/ChatViewModel.kt`
- Purpose: Balance smooth UX with resource efficiency

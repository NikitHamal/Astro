# AstroStorm Continuity Ledger

## Goal (incl. success criteria)
Complete refactoring and bug fixes for AstroStorm Vedic astrology app:
1. Fix critical chat UI bugs (message duplication, raw tool JSON display, ask_user flow, section expansion)
2. Implement 3+ new features from IDEAS.md
3. Add AI integration to Prashna and Matchmaking screens
4. Comprehensive codebase cleanup and refactoring
5. Performance optimization
6. Localization of hardcoded strings

## Constraints/Assumptions
- Must maintain all existing functionality
- All Vedic astrology calculations must remain accurate per classical texts
- Production-grade, fully functional implementations only
- Use modularization and best practices
- No hardcoded strings - use localization
- Kotlin/Jetpack Compose codebase

## Key Decisions
1. Fix tool expansion bug by giving each tool its own independent expanded state
2. Fix ask_user tool to properly interrupt agent flow and wait for user response
3. Improve deduplication in StormyAgent to prevent duplicate content emission
4. Clean tool call JSON from appearing in content
5. Implement features from IDEAS.md: Transit Alert System, Context-Aware Dasha, Educational Mode

## State

### Done
- Analyzed codebase structure and identified all relevant files
- Reviewed all screenshots showing issues
- Identified root causes of bugs:
  - Tool expansion: all tools share same isExpanded state from ToolGroup
  - Raw JSON: tool call blocks not being cleaned from content properly
  - Duplicates: content being emitted multiple times during streaming
  - ask_user: not interrupting flow, showing as raw JSON

### Now
- Fixing critical chat UI bugs

### Next
- Implement AI integration in Prashna screen
- Implement AI integration in Matchmaking screen
- Implement 3+ new features from IDEAS.md
- Codebase cleanup and dead code removal
- Performance optimization
- Localization improvements

## Open Questions
- None currently

## Working Set
- `ChatViewModel.kt` - Main chat logic with duplication issues
- `StormyAgent.kt` - Agent processing with content cleaning issues
- `SectionedComponents.kt` - Tool expansion bug (isExpanded shared)
- `SectionedMessageCard.kt` - Message rendering
- `AgentSectionModels.kt` - Section data models
- `PrashnaScreen.kt` - Needs AI integration
- `MatchmakingScreen.kt` - Needs AI integration
- `IDEAS.md` - Feature ideas to implement
- `AI.md` - AI enhancement roadmap

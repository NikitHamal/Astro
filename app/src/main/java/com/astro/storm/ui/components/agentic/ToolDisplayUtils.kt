package com.astro.storm.ui.components.agentic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyUICommon
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.StringResources

/**
 * Centralized Tool Display Utilities
 *
 * This module provides a single source of truth for tool name formatting
 * and icon mapping throughout the agentic UI components.
 *
 * Key Features:
 * - Consistent tool name formatting across all UI components
 * - Semantic icon mapping for Vedic astrology tools
 * - Duration formatting utilities
 * - Eliminates code duplication across ChatTab, ChatViewModel,
 *   AgenticMessageComponents, SectionedMessageCard, and SectionedComponents
 */
object ToolDisplayUtils {

    /**
     * Format tool name for user-friendly display
     *
     * Converts snake_case tool names to localized display names using StringKeyAgent.
     * Fallback to Title Case if key not found.
     *
     * @param toolName The raw tool name from the agent
     * @param language The current language
     * @return Formatted display name for the UI
     */
    fun formatToolName(toolName: String, language: Language = Language.ENGLISH): String {
        val key = when (toolName) {
            "get_planet_positions" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_PLANET_POSITIONS
            "get_house_positions" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_HOUSE_POSITIONS
            "get_nakshatra_info" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_NAKSHATRA_INFO
            "get_dasha_info" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_DASHA_INFO
            "get_current_dasha" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_CURRENT_DASHA
            "get_yogas" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_YOGAS
            "get_ashtakavarga" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_ASHTAKAVARGA
            "get_transits" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_TRANSITS
            "get_remedies" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_REMEDIES
            "get_strength_analysis" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_STRENGTH_ANALYSIS
            "get_divisional_chart" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_DIVISIONAL_CHART
            "calculate_muhurta" -> com.astro.storm.core.common.StringKeyAgent.TOOL_CALCULATE_MUHURTA
            "get_bhrigu_bindu" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_BHRIGU_BINDU
            "get_argala" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_ARGALA
            "get_prashna_analysis" -> com.astro.storm.core.common.StringKeyAgent.TOOL_GET_PRASHNA_ANALYSIS
            else -> null
        }

        if (key != null) {
            return StringResources.get(key, language)
        }

        return toolName
            .removePrefix("get_")
            .removePrefix("calculate_")
            .removePrefix("search_")
            .removePrefix("analyze_")
            .removePrefix("fetch_")
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }

    /**
     * Get the appropriate icon for a tool based on its name
     *
     * Maps Vedic astrology tools to semantic Material icons
     * for better visual identification in the UI.
     *
     * @param toolName The raw tool name
     * @return ImageVector icon appropriate for the tool
     */
    fun getToolIcon(toolName: String): ImageVector {
        return when {
            // Planet and celestial body tools
            toolName.contains("planet") -> Icons.Outlined.Public
            toolName.contains("sun") -> Icons.Outlined.WbSunny
            toolName.contains("moon") -> Icons.Outlined.NightsStay

            // House and chart tools
            toolName.contains("house") -> Icons.Outlined.Home
            toolName.contains("chart") -> Icons.Outlined.Analytics
            toolName.contains("ascendant") -> Icons.Outlined.Navigation

            // Time-based tools
            toolName.contains("dasha") -> Icons.Outlined.Timeline
            toolName.contains("transit") -> Icons.Outlined.TrendingUp
            toolName.contains("muhurta") -> Icons.Outlined.Schedule
            toolName.contains("time") -> Icons.Outlined.AccessTime

            // Yoga and combination tools
            toolName.contains("yoga") -> Icons.Outlined.SelfImprovement
            toolName.contains("combination") -> Icons.Outlined.Merge

            // Compatibility tools
            toolName.contains("compatibility") -> Icons.Outlined.Favorite
            toolName.contains("match") -> Icons.Outlined.CompareArrows

            // Remedy and recommendation tools
            toolName.contains("remedy") -> Icons.Outlined.Healing
            toolName.contains("gemstone") -> Icons.Outlined.Diamond
            toolName.contains("mantra") -> Icons.Outlined.MusicNote

            // Profile tools
            toolName.contains("profile") -> Icons.Outlined.Person
            toolName.contains("birth") -> Icons.Outlined.Cake

            // Prediction tools
            toolName.contains("predict") -> Icons.Outlined.Visibility
            toolName.contains("forecast") -> Icons.Outlined.CloudQueue

            // Generic calculation tools
            toolName.contains("calculate") -> Icons.Outlined.Calculate
            toolName.contains("analyze") -> Icons.Outlined.Insights

            // Default tool icon
            else -> Icons.Outlined.Build
        }
    }

    /**
     * Format duration for display
     *
     * Converts milliseconds to a human-readable duration string.
     * Uses appropriate units based on the duration magnitude.
     *
     * @param durationMs Duration in milliseconds
     * @return Formatted duration string (e.g., "250ms", "2.3s", "1m 15s")
     */
    fun formatDuration(durationMs: Long, language: Language = Language.ENGLISH): String {
        val msKey = StringResources.get(StringKeyUICommon.DURATION_MS, language)
        val sKey = StringResources.get(StringKeyUICommon.DURATION_S, language)
        val mKey = StringResources.get(StringKeyUICommon.DURATION_M, language)

        return when {
            durationMs < 1000 -> "${durationMs}$msKey"
            durationMs < 60000 -> {
                val sec = durationMs / 1000
                val dec = (durationMs % 1000) / 100
                "${sec}.${dec}${sKey}"
            }
            else -> "${durationMs / 60000}$mKey " + "${(durationMs % 60000) / 1000}$sKey"
        }
    }

    /**
     * Format reasoning duration for display in section headers
     *
     * Provides a more verbose format for reasoning duration display.
     *
     * @param durationMs Duration in milliseconds
     * @return Formatted duration string (e.g., "for 250ms", "for 2s", "for 1m 15s")
     */
    fun formatReasoningDuration(durationMs: Long, language: Language = Language.ENGLISH): String {
        val forTemplate = StringResources.get(StringKeyUICommon.FOR, language)
        val duration = formatDuration(durationMs, language)
        return if (forTemplate.contains("%s")) {
            forTemplate.replace("%s", duration)
        } else {
            "$forTemplate $duration"
        }
    }

    /**
     * Build status summary text for tool execution groups
     *
     * @param completed Number of completed tools
     * @param executing Number of currently executing tools
     * @param failed Number of failed tools
     * @param total Total number of tools
     * @return Status summary string for display
     */
    fun buildToolStatusSummary(
        completed: Int,
        executing: Int,
        failed: Int,
        total: Int,
        language: Language = Language.ENGLISH
    ): String {
        val completedKey = StringResources.get(StringKeyUICommon.COMPLETED, language)
        val runningKey = StringResources.get(StringKeyUICommon.RUNNING, language)
        val failedKey = StringResources.get(StringKeyUICommon.FAILED, language)
        val toolsKey = StringResources.get(if (executing > 1 || completed > 1 || failed > 1) StringKeyUICommon.TOOL_PLURAL else StringKeyUICommon.TOOL_SINGULAR, language)

        val slash = StringResources.get(StringKeyUIExtra.SLASH, language)
        val comma = StringResources.get(StringKeyUIExtra.COMMA_SPACE, language)
        val ellipsis = StringResources.get(StringKeyUIExtra.ELLIPSIS, language)

        return when {
            executing > 0 && completed > 0 -> "$completed$slash$total $completedKey$comma$executing $runningKey"
            executing > 0 -> "$executing $toolsKey $runningKey$ellipsis"
            failed > 0 && completed > 0 -> "$completed $completedKey$comma$failed $failedKey"
            failed > 0 -> "$failed $toolsKey $failedKey"
            completed == total && total > 0 -> "$completed $toolsKey $completedKey"
            else -> "$completed$slash$total $completedKey"
        }
    }

    /**
     * Check if a tool name represents an internal/agentic tool
     * that should be filtered from user-facing displays
     *
     * @param toolName The raw tool name
     * @return True if this is an internal tool that should be hidden
     */
    fun isInternalTool(toolName: String): Boolean {
        return toolName in listOf(
            "start_task",
            "finish_task",
            "update_todo",
            "set_status"
        )
    }

    /**
     * Check if a tool name represents a profile management tool
     *
     * @param toolName The raw tool name
     * @return True if this is a profile-related tool
     */
    fun isProfileTool(toolName: String): Boolean {
        return toolName in listOf(
            "create_profile",
            "update_profile",
            "delete_profile",
            "set_active_profile",
            "get_profile",
            "list_profiles"
        )
    }

    /**
     * Check if a tool name represents an interactive tool
     * that requires user input
     *
     * @param toolName The raw tool name
     * @return True if this is an interactive tool
     */
    fun isInteractiveTool(toolName: String): Boolean {
        return toolName in listOf(
            "ask_user",
            "confirm_action",
            "select_option"
        )
    }
}

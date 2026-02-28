package com.astro.vajra.ui.components

import android.content.Context
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.astro.vajra.R
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.linkify.LinkifyPlugin

/**
 * Custom TextView that handles drag operations safely.
 *
 * The crash occurs when Android attempts to create a drag shadow with
 * zero or negative dimensions (IllegalStateException: Drag shadow dimensions must be positive).
 * This can happen with empty text, invisible text, or edge cases in text selection.
 *
 * This custom view disables drag-and-drop functionality to prevent crashes while
 * still allowing text selection and copy operations.
 */
private class SafeSelectableTextView(context: Context) : TextView(context) {

    init {
        // Disable drag-and-drop to prevent crashes with invalid shadow dimensions
        // Text selection and copy still work via the context menu
        setOnLongClickListener { false }
    }
}

/**
 * Content cleaning utilities for generated markdown responses.
 *
 * This utility handles the critical task of cleaning generated content
 * before display. It addresses several common issues:
 *
 * 1. Tool Call Artifacts: Some generators may include tool call JSON in their
 *    responses that should not be displayed to users.
 *
 * 2. Streaming Artifacts: During streaming, various artifacts like "null",
 *    "undefined", "[DONE]" markers, and incomplete code blocks may appear.
 *
 * 3. Content Duplication: Generators (especially after tool calls) may
 *    repeat content. This cleaner detects and removes duplicated sections.
 *
 * 4. Thinking/Reasoning Tags: Content wrapped in thinking tags should be
 *    displayed separately in the reasoning panel.
 *
 * 5. Whitespace Normalization: Excessive whitespace is normalized while
 *    preserving intentional markdown formatting.
 */
object ContentCleaner {

    // Pattern to match tool_call code blocks (various formats models might use)
    private val toolCallBlockPatterns = listOf(
        // Standard tool_call code block (greedy for nested braces)
        Regex("""```tool_call\s*\n?\s*\{[\s\S]*?\}\s*\n?```""", RegexOption.MULTILINE),
        // JSON code block with tool call inside
        Regex("""```json\s*\n?\s*\{"tool"\s*:[\s\S]*?\}\s*\n?```""", RegexOption.MULTILINE),
        // Plain code block with tool call
        Regex("""```\s*\n?\s*\{"tool"\s*:[\s\S]*?\}\s*\n?```""", RegexOption.MULTILINE),
        // Code block with function name
        Regex("""```\s*\n?\s*\{"name"\s*:[\s\S]*?\}\s*\n?```""", RegexOption.MULTILINE),
        // Code block with nested arguments (multi-line) - more aggressive
        Regex("""```(?:json|tool_call)?\s*\n\s*\{[^`]*"tool"[^`]*\}\s*\n?```""", RegexOption.MULTILINE),
        // Catch any code block that contains tool JSON with nested objects
        Regex("""```[^`]*\n\s*\{\s*\n?\s*"tool"\s*:[^`]*\}\s*\n?```""", RegexOption.MULTILINE),
        // Protocol-style leaked tool section
        Regex("""<\|tool_calls_section_begin\|>[\s\S]*?<\|tool_calls_section_end\|>""", RegexOption.IGNORE_CASE)
    )

    // Pattern to match inline tool call JSON (various formats including multi-line)
    private val inlineToolCallPatterns = listOf(
        // Multi-line tool call with nested arguments (most common issue from screenshots)
        Regex("""\{\s*\n?\s*"tool"\s*:\s*"[^"]+"\s*,\s*\n?\s*"arguments"\s*:\s*\{[\s\S]*?\}\s*\n?\s*\}""", RegexOption.MULTILINE),
        // Standard format (single line)
        Regex("""\{"tool"\s*:\s*"[^"]+"\s*,\s*"arguments"\s*:\s*\{[^}]*\}\s*\}"""),
        // Alternate order
        Regex("""\{"arguments"\s*:\s*\{[^}]*\}\s*,\s*"tool"\s*:\s*"[^"]+"\s*\}"""),
        // With extra whitespace (single line)
        Regex("""\{\s*"tool"\s*:\s*"[^"]+"\s*,\s*"arguments"\s*:\s*\{[^}]*\}\s*\}"""),
        // Multi-line with nested arrays (for ask_user options)
        Regex("""\{\s*\n?\s*"tool"\s*:\s*"[^"]+"\s*,\s*\n?\s*"arguments"\s*:\s*\{[^}]*\[[^\]]*\][^}]*\}\s*\n?\s*\}""", RegexOption.MULTILINE),
        // Name-first format
        Regex("""\{\s*"name"\s*:\s*"[^"]+"\s*,\s*"parameters"\s*:\s*\{[^}]*\}\s*\}"""),
        // Function call format
        Regex("""\{\s*"function"\s*:\s*"[^"]+"\s*,\s*"args"\s*:\s*\{[^}]*\}\s*\}"""),
        // Deeply nested JSON tool call (handles ask_user with options array containing objects)
        Regex("""\{\s*"tool"\s*:\s*"[^"]+"\s*,\s*"arguments"\s*:\s*\{[^{}]*(?:\{[^{}]*\}[^{}]*)*(?:\[[^\[\]]*(?:\{[^\{\}]*\}[^\[\]]*)*\][^{}]*)*\}\s*\}""", RegexOption.MULTILINE)
    )

    // Pattern to match thinking/reasoning blocks that shouldn't be in main content
    private val thinkingBlockPatterns = listOf(
        Regex("""<think>[\s\S]*?</think>""", RegexOption.MULTILINE),
        Regex("""<thinking>[\s\S]*?</thinking>""", RegexOption.MULTILINE),
        Regex("""<reasoning>[\s\S]*?</reasoning>""", RegexOption.MULTILINE),
        Regex("""<reflection>[\s\S]*?</reflection>""", RegexOption.MULTILINE)
    )

    // Pattern to match common response artifacts from tool-enabled generators
    private val artifactPatterns = listOf(
        // Remove standalone "null" text artifacts (common in streaming)
        Regex("""(?<![a-zA-Z0-9_])null(?![a-zA-Z0-9_])"""),
        // Remove standalone "undefined" text artifacts
        Regex("""(?<![a-zA-Z0-9_])undefined(?![a-zA-Z0-9_])"""),
        // Remove empty code blocks
        Regex("""```\s*```"""),
        // Remove lone backticks at start/end of line
        Regex("""^```\s*$""", RegexOption.MULTILINE),
        // Remove trailing backticks without opening
        Regex("""(?<![`])```$"""),
        // Remove [DONE] markers that might leak through
        Regex("""\[DONE\]"""),
        // Remove data: prefixes that might leak through
        Regex("""^data:\s*""", RegexOption.MULTILINE),
        // Remove SSE event markers
        Regex("""^event:\s*\w+\s*$""", RegexOption.MULTILINE),
        // Remove id: markers from SSE
        Regex("""^id:\s*\S+\s*$""", RegexOption.MULTILINE)
    )

    // Strip leaked prompt/meta-instruction fragments if a model exposes internal text.
    private val leakedInstructionPatterns = listOf(
        Regex("""(?i)\bi\s+(?:should\s+)?not use italics in (?:my\s+)?responses?(?:\s+as per the guidelines)?\.?"""),
        Regex("""(?i)\bnever use italics in your responses\.?""")
    )

    private val leakedToolFragmentPatterns = listOf(
        Regex("""(?m)^\s*tool_call\s*$"""),
        Regex("""(?m)^\s*`{3}_?[a-zA-Z0-9_]*\s*$"""),
        Regex("""(?m)^\s*(profile_id|partner_profile_id|dasha_type|years_ahead)\b.*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*<\|tool_[^|]+?\|>\s*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*(?:functions\.)?[a-zA-Z0-9_]+\s*:\s*\d+\s*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*"?\s*(tool|arguments|parameters|function)\s*"?\s*:\s*.*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*arguments\s*\{[\s\S]*?\}\s*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*ask_user.*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*[\{\}]\s*$""")
    )

    // Lightweight patterns for streaming updates (hot path).
    // Keep this cheap and avoid expensive deduplication while text is still arriving.
    private val streamingArtifactPatterns = listOf(
        Regex("""\[DONE\]"""),
        Regex("""^data:\s*""", RegexOption.MULTILINE),
        Regex("""^event:\s*\w+\s*$""", RegexOption.MULTILINE),
        Regex("""^id:\s*\S+\s*$""", RegexOption.MULTILINE)
    )

    // Fast-path removal of tool call scaffolding during live streaming.
    private val streamingToolCallPatterns = listOf(
        Regex("""```tool_call[\s\S]*?```""", RegexOption.MULTILINE),
        Regex("""```json[\s\S]*?"tool"[\s\S]*?```""", RegexOption.MULTILINE),
        Regex("""<\|tool_calls_section_begin\|>[\s\S]*?<\|tool_calls_section_end\|>""", RegexOption.IGNORE_CASE),
        Regex("""<\|tool_calls_section_begin\|>[\s\S]*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*tool_call\s*$"""),
        Regex("""(?m)^\s*<\|tool_[^|]+?\|>\s*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*(?:functions\.)?[a-zA-Z0-9_]+\s*:\s*\d+\s*$""", RegexOption.IGNORE_CASE),
        Regex("""\{\s*"tool"\s*:\s*"[^"]+"\s*,\s*"arguments"\s*:\s*\{[\s\S]*?\}\s*\}"""),
        Regex("""(?m)^\s*"?\s*(tool|arguments|parameters|function)\s*"?\s*:\s*.*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*(profile_id|partner_profile_id|dasha_type|years_ahead|for_now|activity)\b.*$""", RegexOption.IGNORE_CASE),
        Regex("""(?is)\barguments\s*\{[\s\S]*?\}"""),
        Regex("""(?m)^\s*ask_user.*$""", RegexOption.IGNORE_CASE),
        Regex("""(?m)^\s*`{3}\s*$""")
    )

    private val toolProtocolStartMarkers = listOf(
        "<|tool_calls_section_begin|>",
        "<|tool_call_begin|>",
        "<|tool_call_argument_begin|>"
    )

    /**
     * Clean content for display by removing tool calls, artifacts, and formatting issues.
     * This should be called before displaying generated response content.
     */
    fun cleanForDisplay(content: String): String {
        if (content.isBlank()) return ""

        var cleaned = truncateAtToolProtocol(content)

        // Remove tool call blocks (all patterns)
        for (pattern in toolCallBlockPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Remove inline tool call JSON (all patterns)
        for (pattern in inlineToolCallPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Additional pass: Remove any remaining JSON objects that look like tool calls
        // This catches edge cases where regex patterns miss complex nested JSON
        cleaned = removeToolCallJsonObjects(cleaned)

        // Remove thinking blocks (these should be shown separately)
        for (pattern in thinkingBlockPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Remove common artifacts
        for (pattern in artifactPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Remove leaked instruction fragments from user-facing text
        for (pattern in leakedInstructionPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        for (pattern in leakedToolFragmentPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Detect and remove content duplication (common after tool calls)
        cleaned = removeDuplicatedContent(cleaned)

        // Final guard against late protocol marker leakage
        cleaned = truncateAtToolProtocol(cleaned)

        // Clean up excessive whitespace while preserving markdown formatting
        cleaned = cleaned
            .replace(Regex("\n{4,}"), "\n\n\n") // Max 3 newlines
            .replace(Regex("[ \t]+\n"), "\n") // Remove trailing spaces
            .replace(Regex("\n[ \t]+\n"), "\n\n") // Remove lines with only whitespace
            .trim()

        return cleaned
    }

    /**
     * Fast cleaner for live streaming updates.
     * Avoids heavy regex and duplication detection that can stall token rendering.
     */
    fun cleanForStreaming(content: String): String {
        if (content.isBlank()) return ""

        var cleaned = truncateAtToolProtocol(content)
        for (pattern in streamingArtifactPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        for (pattern in streamingToolCallPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Only run structural removal when tool-call markers are present.
        if (cleaned.contains("tool_call", ignoreCase = true) ||
            cleaned.contains("<|tool_", ignoreCase = true) ||
            cleaned.contains("functions.", ignoreCase = true) ||
            (cleaned.contains("\"tool\"") && cleaned.contains("\"arguments\""))
        ) {
            cleaned = removeToolCallJsonObjects(cleaned)
        }

        for (pattern in leakedInstructionPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        for (pattern in leakedToolFragmentPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        return cleaned
            .replace(Regex("\n{4,}"), "\n\n\n")
            .replace(Regex("[ \t]+\n"), "\n")
            .replace(Regex("\n[ \t]+\n"), "\n\n")
            .trim()
    }

    /**
     * Truncate from the first protocol-style tool marker onward.
     * This prevents model-internal tool protocol from leaking to user-visible text.
     */
    private fun truncateAtToolProtocol(content: String): String {
        val lowered = content.lowercase()
        var cutIndex = -1
        toolProtocolStartMarkers.forEach { marker ->
            val idx = lowered.indexOf(marker.lowercase())
            if (idx >= 0 && (cutIndex == -1 || idx < cutIndex)) {
                cutIndex = idx
            }
        }
        return if (cutIndex >= 0) content.substring(0, cutIndex) else content
    }

    /**
     * Remove JSON objects that look like tool calls using bracket matching.
     * This handles deeply nested JSON that regex patterns may miss.
     */
    private fun removeToolCallJsonObjects(content: String): String {
        var result = content
        var searchStart = 0

        while (true) {
            // Find potential tool call JSON start
            val toolIndicators = listOf(
                """"tool"""",
                """"tool" :""",
                """"name"""",
                """"function""""
            )

            var foundIndex = -1
            var foundIndicator = ""
            for (indicator in toolIndicators) {
                val idx = result.indexOf(indicator, searchStart)
                if (idx != -1 && (foundIndex == -1 || idx < foundIndex)) {
                    foundIndex = idx
                    foundIndicator = indicator
                }
            }

            if (foundIndex == -1) break

            // Find the opening brace before this indicator
            var braceStart = foundIndex - 1
            while (braceStart >= 0 && result[braceStart] != '{') {
                if (!result[braceStart].isWhitespace() && result[braceStart] != '"') {
                    // Found non-whitespace/quote char before finding brace - not a standalone JSON
                    braceStart = -1
                    break
                }
                braceStart--
            }

            if (braceStart == -1) {
                searchStart = foundIndex + foundIndicator.length
                continue
            }

            // Use bracket matching to find the complete JSON object
            val jsonEnd = findMatchingBrace(result, braceStart)
            if (jsonEnd != -1) {
                val potentialJson = result.substring(braceStart, jsonEnd + 1)

                // Verify it's actually a tool call JSON (contains "tool" or "arguments" keys)
                if (potentialJson.contains(""""tool"""") &&
                    (potentialJson.contains(""""arguments"""") || potentialJson.contains(""""parameters""""))) {
                    // Remove this JSON block
                    result = result.substring(0, braceStart) + result.substring(jsonEnd + 1)
                    // Don't advance searchStart since we removed content
                } else {
                    searchStart = jsonEnd + 1
                }
            } else {
                searchStart = foundIndex + foundIndicator.length
            }
        }

        return result
    }

    /**
     * Find the matching closing brace for an opening brace.
     * Returns -1 if no match is found.
     */
    private fun findMatchingBrace(text: String, openIndex: Int): Int {
        if (openIndex < 0 || openIndex >= text.length || text[openIndex] != '{') return -1

        var depth = 0
        var inString = false
        var escapeNext = false

        for (i in openIndex until text.length) {
            val c = text[i]

            if (escapeNext) {
                escapeNext = false
                continue
            }

            when {
                c == '\\' -> escapeNext = true
                c == '"' && !escapeNext -> inString = !inString
                !inString && c == '{' -> depth++
                !inString && c == '}' -> {
                    depth--
                    if (depth == 0) return i
                }
            }
        }

        return -1
    }

    /**
     * Detect and remove duplicated content sections.
     *
     * Generators sometimes repeat content after tool calls. This function
     * detects when a significant portion of the content is duplicated
     * and removes the repetition.
     *
     * Detection algorithm:
     * 1. Split content into paragraphs/sections
     * 2. Look for repeating sequences of paragraphs
     * 3. If found, keep only the first occurrence
     * 4. Also detect mid-content duplication using sliding window
     */
    private fun removeDuplicatedContent(content: String): String {
        if (content.length < 200) return content // Too short to have meaningful duplication

        // First check for mid-content duplication (content repeated mid-stream)
        val midDuplicated = detectAndRemoveMidDuplication(content)
        if (midDuplicated != content) {
            return removeDuplicatedContent(midDuplicated) // Recursive check for remaining duplicates
        }

        // Split into paragraphs (using double newlines as delimiter)
        val paragraphs = content.split(Regex("\n\n+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (paragraphs.size < 4) {
            return removeLineDuplications(content)
        }

        // Look for repeating sequences
        for (windowSize in (paragraphs.size / 2) downTo 2) {
            // Check if the first windowSize paragraphs repeat
            val firstHalf = paragraphs.take(windowSize)
            val secondHalf = paragraphs.drop(windowSize).take(windowSize)

            // Calculate similarity between the two halves
            if (firstHalf.size == secondHalf.size) {
                val matchCount = firstHalf.zip(secondHalf).count { (a, b) ->
                    a == b || calculateSimilarity(a, b) > 0.9
                }

                // If more than 80% of paragraphs match, we have duplication
                if (matchCount.toFloat() / firstHalf.size > 0.8) {
                    // Return only the first occurrence plus any remaining unique content
                    val uniqueContent = paragraphs.take(windowSize) +
                        paragraphs.drop(windowSize * 2)
                    return uniqueContent.joinToString("\n\n")
                }
            }
        }

        // Also check for exact substring duplication (simpler case)
        val halfLength = content.length / 2
        if (halfLength > 100) {
            val firstHalf = content.substring(0, halfLength).trim()
            val secondHalf = content.substring(halfLength).trim()

            // Check if second half starts with the beginning of first half
            if (secondHalf.startsWith(firstHalf.take(100)) ||
                calculateSimilarity(firstHalf, secondHalf) > 0.85) {
                return firstHalf
            }
        }

        return content
    }

    /**
     * Line-based duplication detection for content without paragraph breaks.
     */
    private fun removeLineDuplications(content: String): String {
        val lines = content.split(Regex("\n+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (lines.size < 6) return content

        for (windowSize in (lines.size / 2) downTo 2) {
            val firstHalf = lines.take(windowSize)
            val secondHalf = lines.drop(windowSize).take(windowSize)

            if (firstHalf.size == secondHalf.size) {
                val matchCount = firstHalf.zip(secondHalf).count { (a, b) ->
                    a == b || calculateSimilarity(a, b) > 0.9
                }
                if (matchCount.toFloat() / firstHalf.size > 0.8) {
                    val uniqueContent = lines.take(windowSize) + lines.drop(windowSize * 2)
                    return uniqueContent.joinToString("\n")
                }
            }
        }

        return content
    }

    /**
     * Detect and remove mid-content duplication.
     * This handles cases where content repeats starting mid-way through the text.
     * Example: "Hello world. How are you? Hello world. How are you?"
     */
    private fun detectAndRemoveMidDuplication(content: String): String {
        val minChunkSize = 100 // Minimum chunk to consider as a duplicate

        // Look for repeating substring patterns
        for (start in 0 until minOf(content.length / 2, 500)) {
            for (chunkSize in minChunkSize until (content.length - start) / 2) {
                val chunk = content.substring(start, start + chunkSize)
                val remainingContent = content.substring(start + chunkSize)

                // Check if this chunk appears again in the remaining content
                if (remainingContent.contains(chunk)) {
                    val repeatIndex = remainingContent.indexOf(chunk)
                    // Verify it's a substantial repeat (not just a common phrase)
                    if (repeatIndex < chunkSize * 2) { // Close enough to be a duplicate
                        val beforeRepeat = content.substring(0, start + chunkSize + repeatIndex)
                        val afterRepeat = content.substring(start + chunkSize + repeatIndex)

                        // Check if afterRepeat continues the pattern
                        if (calculateSimilarity(chunk, afterRepeat.take(chunk.length)) > 0.8) {
                            // Found duplication - return content up to the repeat
                            return beforeRepeat.trim()
                        }
                    }
                }
            }
        }

        return content
    }

    /**
     * Calculate similarity ratio between two strings (0.0 to 1.0)
     */
    private fun calculateSimilarity(a: String, b: String): Float {
        if (a == b) return 1.0f
        if (a.isEmpty() || b.isEmpty()) return 0.0f

        val maxLen = maxOf(a.length, b.length)
        val minLen = minOf(a.length, b.length)

        // Quick length check - if very different lengths, probably not similar
        if (minLen.toFloat() / maxLen < 0.5f) return 0.0f

        // Compare word sets for similarity
        val wordsA = a.lowercase().split(Regex("\\s+")).toSet()
        val wordsB = b.lowercase().split(Regex("\\s+")).toSet()

        val intersection = wordsA.intersect(wordsB).size
        val union = wordsA.union(wordsB).size

        return if (union == 0) 0.0f else intersection.toFloat() / union
    }

    /**
     * Remove duplicated content in reasoning/thinking blocks.
     * Reasoning often gets accumulated and may have repeated sections.
     * Uses sentence-based detection for better accuracy with reasoning text.
     */
    private fun removeReasoningDuplication(content: String): String {
        if (content.length < 150) return content // Too short for meaningful duplication

        // Split into sentences/lines for reasoning content
        val lines = content.split(Regex("(?<=[.!?\\n])\\s*"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (lines.size < 3) return content

        // Look for repeating sequences of lines
        val uniqueLines = mutableListOf<String>()
        val seenNormalized = mutableSetOf<String>()

        for (line in lines) {
            // Normalize line for comparison (lowercase, remove extra spaces)
            val normalized = line.lowercase().replace(Regex("\\s+"), " ").trim()

            // Skip if we've seen this exact or very similar line
            if (normalized.isNotEmpty() && normalized.length > 10) {
                val isDuplicate = seenNormalized.any { seen ->
                    seen == normalized ||
                    (normalized.length > 30 && seen.contains(normalized.take(30))) ||
                    calculateSimilarity(seen, normalized) > 0.9f
                }

                if (!isDuplicate) {
                    uniqueLines.add(line)
                    seenNormalized.add(normalized)
                }
            } else if (normalized.isNotEmpty()) {
                // Short lines - just check exact match
                if (normalized !in seenNormalized) {
                    uniqueLines.add(line)
                    seenNormalized.add(normalized)
                }
            }
        }

        return uniqueLines.joinToString(" ")
    }

    /**
     * Clean reasoning/thinking content.
     * Similar to cleanForDisplay but preserves internal thinking format.
     * Also removes duplicated reasoning content.
     */
    fun cleanReasoning(content: String): String {
        if (content.isBlank()) return ""

        var cleaned = truncateAtToolProtocol(content)

        // Remove null/undefined artifacts
        cleaned = cleaned
            .replace(Regex("""(?<![a-zA-Z])null(?![a-zA-Z])"""), "")
            .replace(Regex("""(?<![a-zA-Z])undefined(?![a-zA-Z])"""), "")

        // Remove SSE artifacts
        cleaned = cleaned
            .replace(Regex("""^data:\s*""", RegexOption.MULTILINE), "")
            .replace(Regex("""^event:\s*\w+\s*$""", RegexOption.MULTILINE), "")

        // Remove thinking tags if present (keep content)
        cleaned = cleaned
            .replace(Regex("""</?think(?:ing)?>"""), "")
            .replace(Regex("""</?reasoning>"""), "")

        cleaned = cleaned
            .replace(Regex("""</?reflection>"""), "")
            .replace(Regex("""(?m)^\s*<\|tool_[^|]+?\|>\s*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?m)^\s*(?:functions\.)?[a-zA-Z0-9_]+\s*:\s*\d+\s*$""", RegexOption.IGNORE_CASE), "")

        // Apply deduplication to reasoning as well
        cleaned = removeReasoningDuplication(cleaned)

        for (pattern in leakedInstructionPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        for (pattern in leakedToolFragmentPatterns) {
            cleaned = pattern.replace(cleaned, "")
        }

        // Clean up whitespace
        cleaned = cleaned
            .replace(Regex("\n{4,}"), "\n\n\n")
            .trim()

        return cleaned
    }

    /**
     * Fast cleaner for streaming reasoning text.
     */
    fun cleanReasoningForStreaming(content: String): String {
        if (content.isBlank()) return ""

        return truncateAtToolProtocol(content)
            .replace(Regex("""^data:\s*""", RegexOption.MULTILINE), "")
            .replace(Regex("""^event:\s*\w+\s*$""", RegexOption.MULTILINE), "")
            .replace(Regex("""</?think(?:ing)?>"""), "")
            .replace(Regex("""</?reasoning>"""), "")
            .replace(Regex("""</?reflection>"""), "")
            .replace(Regex("""(?<![a-zA-Z])null(?![a-zA-Z])"""), "")
            .replace(Regex("""(?<![a-zA-Z])undefined(?![a-zA-Z])"""), "")
            .replace(Regex("""(?i)\bi\s+(?:should\s+)?not use italics in (?:my\s+)?responses?(?:\s+as per the guidelines)?\.?"""), "")
            .replace(Regex("""(?i)\bnever use italics in your responses\.?"""), "")
            .replace(Regex("""(?m)^\s*tool_call\s*$"""), "")
            .replace(Regex("""(?m)^\s*<\|tool_[^|]+?\|>\s*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?m)^\s*(?:functions\.)?[a-zA-Z0-9_]+\s*:\s*\d+\s*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?m)^\s*(profile_id|partner_profile_id|dasha_type|years_ahead)\b.*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("[ \t]+\n"), "\n")
            .trim()
    }

    /**
     * Check if content appears to still be streaming (incomplete).
     * Useful for determining if we should show a typing indicator.
     */
    fun isIncompleteContent(content: String): Boolean {
        if (content.isBlank()) return true

        val trimmed = content.trim()

        // Check for incomplete code blocks
        val codeBlockCount = trimmed.split("```").size - 1
        if (codeBlockCount % 2 != 0) return true

        // Check for incomplete JSON
        val openBraces = trimmed.count { it == '{' }
        val closeBraces = trimmed.count { it == '}' }
        if (openBraces > closeBraces) return true

        // Check for incomplete thinking tags
        val hasOpenThink = trimmed.contains("<think") && !trimmed.contains("</think>")
        if (hasOpenThink) return true

        return false
    }

    /**
     * Extract thinking/reasoning content from a response
     */
    fun extractThinkingContent(content: String): String? {
        for (pattern in thinkingBlockPatterns) {
            val match = pattern.find(content)
            if (match != null) {
                // Extract content between tags
                val inner = match.value
                    .replace(Regex("""<[^>]+>"""), "")
                    .trim()
                if (inner.isNotEmpty()) return inner
            }
        }
        return null
    }
}

/**
 * Singleton for creating and caching Markwon instances.
 */
object MarkwonProvider {

    @Volatile
    private var instance: Markwon? = null

    fun getInstance(context: Context): Markwon {
        return instance ?: synchronized(this) {
            instance ?: createMarkwon(context).also { instance = it }
        }
    }

    private fun createMarkwon(context: Context): Markwon {
        return Markwon.builder(context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(LinkifyPlugin.create())
            .build()
    }

    /**
     * Clear the cached instance (useful for theme changes)
     */
    fun clearCache() {
        instance = null
    }
}

/**
 * Composable for rendering Markdown text using Markwon.
 *
 * @param markdown The markdown text to render
 * @param modifier Modifier for the text view
 * @param textColor Color for the text
 * @param linkColor Color for links
 * @param textSize Text size in sp
 * @param cleanContent Whether to clean the content before rendering (recommended)
 */
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    linkColor: Color = Color.Unspecified,
    textSize: Float = 14f,
    cleanContent: Boolean = true
) {
    val context = LocalContext.current
    val markwon = remember(context) { MarkwonProvider.getInstance(context) }

    // Clean content if requested
    val displayContent = remember(markdown, cleanContent) {
        if (cleanContent) ContentCleaner.cleanForDisplay(markdown) else markdown
    }

    // Pre-parse the markdown for better performance
    val spanned = remember(displayContent) {
        markwon.toMarkdown(displayContent)
    }

    // Load Poppins font for consistent typography
    val poppinsTypeface = remember(context) {
        try {
            ResourcesCompat.getFont(context, R.font.poppins_regular)
        } catch (e: Exception) {
            null
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            SafeSelectableTextView(ctx).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextIsSelectable(true)
                // Apply Poppins font
                poppinsTypeface?.let { typeface = it }
                // Set line spacing for better readability
                setLineSpacing(4f, 1.1f)
            }
        },
        update = { textView ->
            // Apply text color if specified
            if (textColor != Color.Unspecified) {
                textView.setTextColor(textColor.toArgb())
            }

            // Apply link color if specified
            if (linkColor != Color.Unspecified) {
                textView.setLinkTextColor(linkColor.toArgb())
            }

            // Apply text size
            textView.textSize = textSize

            // Ensure font is applied on update too
            poppinsTypeface?.let { textView.typeface = it }

            // Set the markdown content
            markwon.setParsedMarkdown(textView, spanned)
        }
    )
}

/**
 * Simplified MarkdownText that uses theme colors automatically.
 */
@Composable
fun ThemedMarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    textColor: Color,
    linkColor: Color,
    textSize: Float = 14f
) {
    MarkdownText(
        markdown = markdown,
        modifier = modifier,
        textColor = textColor,
        linkColor = linkColor,
        textSize = textSize,
        cleanContent = true
    )
}

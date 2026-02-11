package com.astro.storm.data.ai.agent

import android.content.Context
import com.astro.storm.data.ai.provider.AiModel
import com.astro.storm.data.ai.provider.AiProviderRegistry
import com.astro.storm.data.ai.provider.ChatMessage
import com.astro.storm.data.ai.provider.ChatResponse
import com.astro.storm.data.ai.provider.FunctionCall
import com.astro.storm.data.ai.provider.MessageRole
import com.astro.storm.data.ai.provider.ToolCall
import com.astro.storm.data.ai.agent.tools.AstrologyToolRegistry
import com.astro.storm.data.ai.agent.tools.ToolExecutionResult
import com.astro.storm.data.local.ChartDatabase
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyAgent
import com.astro.storm.core.common.StringResources
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stormy - The Vedic Astrology AI Assistant
 *
 * Stormy is an intelligent agent capable of:
 * - Providing Vedic astrology insights and guidance
 * - Executing tools to fetch chart data and perform calculations
 * - Supporting multiple AI models (model-agnostic)
 * - Handling tool calls through JSON parsing (works even with models without native tool support)
 */
@Singleton
class StormyAgent @Inject constructor(
    @ApplicationContext private val context: Context,
    private val providerRegistry: AiProviderRegistry,
    private val toolRegistry: AstrologyToolRegistry,
    private val promptManager: PromptManager
) {

    companion object {
        const val AGENT_NAME = "Stormy"
        const val AGENT_DESCRIPTION = "Your Vedic Astrology AI Assistant"

        /**
         * Maximum number of autonomous tool execution iterations.
         * The agent can call tools up to this many times in a single request
         * to accomplish the user's task autonomously.
         */
        private const val MAX_TOOL_ITERATIONS = 15

        /**
         * Maximum total iterations (including non-tool responses)
         * to prevent infinite loops in edge cases
         */
        private const val MAX_TOTAL_ITERATIONS = 20

        @Volatile
        private var instance: StormyAgent? = null

        fun getInstance(context: Context): StormyAgent =
            instance ?: synchronized(this) {
                instance ?: StormyAgent(
                    context.applicationContext,
                    AiProviderRegistry.getInstance(context),
                    AstrologyToolRegistry.getInstance(context),
                    PromptManager.getInstance(context)
                ).also {
                    instance = it
                }
            }
    }

    /**
     * Generate the system prompt for Stormy
     */
    fun generateSystemPrompt(
        currentProfile: SavedChart?,
        allProfiles: List<SavedChart>,
        currentChart: VedicChart?,
        language: Language = Language.ENGLISH
    ): String {
        return promptManager.generateSystemPrompt(currentProfile, allProfiles, currentChart, language)
    }

    /**
     * Thread-safe state holder for the agent processing
     */
    private data class AgentState(
        val totalEmittedContent: String = "",
        val totalEmittedReasoning: String = "",
        val contentByIteration: List<String> = emptyList(),
        val allReasoning: String = "",
        val executedToolCalls: Set<String> = emptySet()
    ) {
        fun withAppendedContent(content: String): AgentState =
            copy(totalEmittedContent = totalEmittedContent + content)

        fun withAppendedReasoning(reasoning: String): AgentState =
            copy(totalEmittedReasoning = totalEmittedReasoning + reasoning)

        fun withIterationContent(content: String): AgentState =
            copy(contentByIteration = contentByIteration + content)

        fun withAppendedAllReasoning(reasoning: String): AgentState =
            copy(allReasoning = if (allReasoning.isEmpty()) reasoning else "$allReasoning\n\n$reasoning")

        fun withExecutedToolCall(toolKey: String): AgentState =
            copy(executedToolCalls = executedToolCalls + toolKey)
    }

    /**
     * Creates a canonical JSON string for deduplication purposes.
     * This ensures that semantically identical JSON objects produce the same string
     * regardless of key ordering or whitespace.
     */
    private fun canonicalizeJson(jsonStr: String): String {
        return try {
            val json = JSONObject(jsonStr)
            canonicalizeJsonObject(json)
        } catch (e: Exception) {
            // If parsing fails, normalize whitespace as fallback
            jsonStr.replace(Regex("\\s+"), "").trim()
        }
    }

    private fun canonicalizeJsonObject(json: JSONObject): String {
        val sortedKeys = json.keys().asSequence().sorted()
        val entries = sortedKeys.map { key ->
            val value = json.get(key)
            val canonicalValue = when (value) {
                is JSONObject -> canonicalizeJsonObject(value)
                is JSONArray -> canonicalizeJsonArray(value)
                else -> value.toString()
            }
            "\"$key\":$canonicalValue"
        }
        return "{${entries.joinToString(",")}}"
    }

    private fun canonicalizeJsonArray(array: JSONArray): String {
        val elements = (0 until array.length()).map { i ->
            val value = array.get(i)
            when (value) {
                is JSONObject -> canonicalizeJsonObject(value)
                is JSONArray -> canonicalizeJsonArray(value)
                else -> value.toString()
            }
        }
        return "[${elements.joinToString(",")}]"
    }

    /**
     * Thread-safe content deduplication tracker
     */
    private class ContentDeduplicator {
        private val emittedChunks = ConcurrentHashMap<String, Boolean>()

        fun isDuplicate(content: String): Boolean {
            val normalized = content.trim()
            if (normalized.isEmpty()) return false
            return emittedChunks.putIfAbsent(normalized, true) != null
        }

        fun clear() {
            emittedChunks.clear()
        }
    }

    /**
     * Process a user message and generate a response with tool calling support
     */
    fun processMessage(
        messages: List<ChatMessage>,
        model: AiModel,
        currentProfile: SavedChart?,
        allProfiles: List<SavedChart>,
        currentChart: VedicChart?,
        temperature: Float? = null,
        maxTokens: Int? = null,
        language: Language = Language.ENGLISH
    ): Flow<AgentResponse> = channelFlow {
        val provider = providerRegistry.getProvider(model.providerId)
            ?: throw IllegalStateException("Provider not found: ${model.providerId}")

        val systemPrompt = generateSystemPrompt(currentProfile, allProfiles, currentChart, language)

        // Build messages with system prompt
        val fullMessages = mutableListOf<ChatMessage>().apply {
            add(ChatMessage(role = MessageRole.SYSTEM, content = systemPrompt))
            addAll(messages)
        }

        // Thread-safe state using AtomicReference
        val stateRef = AtomicReference(AgentState())

        // Thread-safe content deduplication tracker
        val contentDeduplicator = ContentDeduplicator()
        val reasoningDeduplicator = ContentDeduplicator()

        var iteration = 0
        var toolIterations = 0
        var continueProcessing = true
        val toolsUsed = mutableListOf<String>()

        while (continueProcessing && iteration < MAX_TOTAL_ITERATIONS && toolIterations < MAX_TOOL_ITERATIONS) {
            iteration++

            // Per-iteration state
            val iterationContentBuilder = StringBuilder()
            val iterationReasoningBuilder = StringBuilder()
            val pendingToolCalls = mutableListOf<ToolCallRequest>()
            val processedToolIdsInIteration = mutableSetOf<String>()
            var hasError = false
            var lastContentSnapshot = ""
            var lastReasoningSnapshot = ""

            // Channel for collecting responses from the flow
            val responseChannel = Channel<ChatResponse>(Channel.UNLIMITED)

            // Launch collector in a separate coroutine for thread-safe processing
            val collectorJob = launch {
                for (response in responseChannel) {
                    when (response) {
                        is ChatResponse.Content -> {
                            val rawChunk = response.text
                            val currentState = stateRef.get()
                            val newContent = normalizeStreamingDelta(
                                rawChunk = rawChunk,
                                alreadyEmitted = currentState.totalEmittedContent,
                                previousSnapshot = lastContentSnapshot
                            )
                            if (newContent.isEmpty()) continue
                            iterationContentBuilder.append(newContent)
                            lastContentSnapshot = rawChunk

                            if (newContent.isNotEmpty()) {
                                // Thread-safe deduplication check
                                if (!contentDeduplicator.isDuplicate(newContent)) {
                                    // Update atomic state
                                    stateRef.set(currentState.withAppendedContent(newContent))
                                    send(AgentResponse.ContentChunk(newContent))
                                }
                            }
                        }

                        is ChatResponse.Reasoning -> {
                            if (response.text.isNotEmpty()) {
                                val rawChunk = response.text
                                val currentState = stateRef.get()
                                val newReasoning = normalizeStreamingDelta(
                                    rawChunk = rawChunk,
                                    alreadyEmitted = currentState.totalEmittedReasoning,
                                    previousSnapshot = lastReasoningSnapshot
                                )
                                if (newReasoning.isEmpty()) continue
                                iterationReasoningBuilder.append(newReasoning)
                                lastReasoningSnapshot = rawChunk

                                // Thread-safe deduplication check
                                if (!reasoningDeduplicator.isDuplicate(newReasoning)) {
                                    stateRef.set(currentState.withAppendedReasoning(newReasoning))
                                    send(AgentResponse.ReasoningChunk(newReasoning))
                                }
                            }
                        }

                        is ChatResponse.ToolCallRequest -> {
                            response.toolCalls.forEach { call ->
                                val canonicalArgs = canonicalizeJson(call.function.arguments)
                                val toolKey = "${call.function.name}:$canonicalArgs"
                                val currentState = stateRef.get()

                                if (toolKey !in currentState.executedToolCalls && toolKey !in processedToolIdsInIteration) {
                                    processedToolIdsInIteration.add(toolKey)
                                    pendingToolCalls.add(
                                        ToolCallRequest(
                                            id = call.id.ifEmpty { "tool_${UUID.randomUUID().toString().take(8)}" },
                                            name = call.function.name,
                                            arguments = call.function.arguments
                                        )
                                    )
                                }
                            }
                        }

                        is ChatResponse.Error -> {
                            hasError = true
                            send(AgentResponse.Error(response.message, response.isRetryable))
                        }

                        is ChatResponse.Usage -> {
                            send(AgentResponse.TokenUsage(
                                response.promptTokens,
                                response.completionTokens,
                                response.totalTokens
                            ))
                        }

                        is ChatResponse.Done -> {
                            // Check for embedded tool calls in content if native ones weren't found
                            if (pendingToolCalls.isEmpty()) {
                                val embeddedCalls = parseEmbeddedToolCalls(iterationContentBuilder.toString())
                                embeddedCalls.forEach { call ->
                                    val canonicalArgs = canonicalizeJson(call.arguments)
                                    val toolKey = "${call.name}:$canonicalArgs"
                                    val currentState = stateRef.get()

                                    if (toolKey !in currentState.executedToolCalls && toolKey !in processedToolIdsInIteration) {
                                        processedToolIdsInIteration.add(toolKey)
                                        pendingToolCalls.add(call)
                                    }
                                }
                            }
                        }

                        is ChatResponse.ProviderInfo -> {
                            send(AgentResponse.ModelInfo(response.providerId, response.model))
                        }

                        is ChatResponse.RetryNotification -> {
                            send(AgentResponse.RetryInfo(
                                attempt = response.attempt,
                                maxAttempts = response.maxAttempts,
                                delayMs = response.delayMs,
                                reason = response.reason
                            ))
                        }
                    }
                }
            }

            // Call the AI model and send responses to channel
            provider.chat(
                messages = fullMessages,
                model = model.id,
                temperature = temperature,
                maxTokens = maxTokens,
                stream = true
            ).collect { response ->
                responseChannel.send(response)
            }

            // Close channel and wait for collector to finish
            responseChannel.close()
            collectorJob.join()

            // Clean current content of tool call blocks
            val cleanedCurrentContent = iterationContentBuilder.toString().cleanToolCallBlocks().trim()

            // Only add this iteration's content if it's not a duplicate of previous content
            if (cleanedCurrentContent.isNotEmpty()) {
                val currentState = stateRef.get()
                val isDuplicate = currentState.contentByIteration.any { previousContent ->
                    previousContent.trim() == cleanedCurrentContent ||
                    previousContent.contains(cleanedCurrentContent) ||
                    cleanedCurrentContent.contains(previousContent.trim())
                }

                if (!isDuplicate) {
                    stateRef.set(currentState.withIterationContent(cleanedCurrentContent))
                }
            }

            // Accumulate reasoning (reasoning can be additive across iterations)
            if (iterationReasoningBuilder.isNotEmpty()) {
                val currentState = stateRef.get()
                stateRef.set(currentState.withAppendedAllReasoning(iterationReasoningBuilder.toString()))
            }

            if (hasError) {
                continueProcessing = false
                continue
            }

            // Process tool calls if any
            if (pendingToolCalls.isNotEmpty()) {
                toolIterations++
                send(AgentResponse.ToolCallsStarted(pendingToolCalls.map { it.name }))

                // Add assistant message with tool calls
                val assistantContent = iterationContentBuilder.toString().cleanToolCallBlocks()
                fullMessages.add(ChatMessage(
                    role = MessageRole.ASSISTANT,
                    content = assistantContent.ifEmpty { StringResources.get(StringKeyAgent.STATUS_THINKING, language) },
                    toolCalls = pendingToolCalls.map { call ->
                        ToolCall(
                            id = call.id,
                            function = FunctionCall(call.name, call.arguments)
                        )
                    }
                ))

                // Execute each tool and add results
                var askUserInterrupt: AgentResponse.AskUserInterrupt? = null

                for (toolCall in pendingToolCalls) {
                    send(AgentResponse.ToolExecuting(toolCall.name))
                    if (!toolsUsed.contains(toolCall.name)) {
                        toolsUsed.add(toolCall.name)
                    }

                    // Mark as executed to prevent re-execution in future iterations
                    val canonicalArgs = canonicalizeJson(toolCall.arguments)
                    val toolKey = "${toolCall.name}:$canonicalArgs"
                    val currentState = stateRef.get()
                    stateRef.set(currentState.withExecutedToolCall(toolKey))

                    val result = executeToolCall(toolCall, currentProfile, allProfiles, currentChart)

                    send(AgentResponse.ToolResult(toolCall.name, result.success, result.summary))

                    // Add tool result message
                    fullMessages.add(ChatMessage(
                        role = MessageRole.TOOL,
                        content = result.toJson(),
                        toolCallId = toolCall.id,
                        name = toolCall.name
                    ))

                    // Check if this is an ask_user tool - if so, we need to interrupt
                    if (toolCall.name == "ask_user" && result.success) {
                        try {
                            val resultData = result.data
                            if (resultData != null) {
                                val question = resultData.optString("question", "")
                                val optionsArray = resultData.optJSONArray("options")
                                 val allowCustomInput = resultData.optBoolean("allow_custom_input", true)
                                 val context = if (resultData.has("context") && !resultData.isNull("context")) resultData.getString("context") else null

                                val options = mutableListOf<AskUserOptionData>()
                                optionsArray?.let { array ->
                                    for (i in 0 until array.length()) {
                                        val opt = array.optJSONObject(i)
                                        if (opt != null) {
                                             options.add(AskUserOptionData(
                                                 label = opt.optString("label", "Option ${i + 1}"),
                                                 description = if (opt.has("description") && !opt.isNull("description")) opt.getString("description") else null,
                                                 value = opt.optString("value", opt.optString("label", ""))
                                             ))
                                        }
                                    }
                                }

                                if (question.isNotEmpty()) {
                                    askUserInterrupt = AgentResponse.AskUserInterrupt(
                                        question = question,
                                        options = options,
                                        allowCustomInput = allowCustomInput,
                                        context = context,
                                        conversationHistory = fullMessages.toList(),
                                        toolsUsed = toolsUsed.distinct().toList()
                                    )
                                    // Stop executing other tools in this batch since we need user input
                                    break
                                }
                            }
                        } catch (e: Exception) {
                            // Failed to parse ask_user result, continue normally
                        }
                    }
                }

                // If ask_user was called, interrupt and wait for user response
                if (askUserInterrupt != null) {
                    send(askUserInterrupt)
                    continueProcessing = false
                    return@channelFlow // Exit the flow - UI will handle resuming with user response
                }

                // Continue processing to let AI respond to tool results
            } else {
                // No tool calls - combine unique content from all iterations
                val currentState = stateRef.get()
                val finalContent = if (currentState.contentByIteration.isNotEmpty()) {
                    currentState.contentByIteration.last()
                } else {
                    ""
                }
                val finalReasoning = currentState.allReasoning.trim()

                // Check if we only have reasoning but no content
                if (finalContent.isEmpty() && finalReasoning.isNotEmpty()) {
                    // For reasoning-only responses, check if we used tools and might need continuation
                    if (toolsUsed.isNotEmpty() && iteration < MAX_TOTAL_ITERATIONS - 1) {
                        // We used tools but got no final answer - prompt for continuation
                        fullMessages.add(ChatMessage(
                            role = MessageRole.ASSISTANT,
                            content = finalReasoning
                        ))

                        fullMessages.add(ChatMessage(
                            role = MessageRole.USER,
                            content = "Please provide your analysis and answer based on the tool results above."
                        ))

                        // Clear content for the continuation
                        contentDeduplicator.clear()
                        reasoningDeduplicator.clear()
                        stateRef.set(AgentState())

                        // Continue for one more iteration
                        continue
                    }
                }

                val contentToEmit = finalContent.ifEmpty {
                    if (toolsUsed.isNotEmpty()) {
                        "I could not complete a final user-facing response from the tool outputs. Please retry once."
                    } else {
                        "I could not generate a complete response. Please retry your request."
                    }
                }

                val reasoningToEmit = if (finalContent.isNotEmpty() && finalReasoning.isNotEmpty()) {
                    finalReasoning
                } else {
                    null
                }

                continueProcessing = false
                send(AgentResponse.Complete(
                    content = contentToEmit,
                    reasoning = reasoningToEmit,
                    toolsUsed = toolsUsed.distinct().toList()
                ))
            }
        }

        if (iteration >= MAX_TOTAL_ITERATIONS || toolIterations >= MAX_TOOL_ITERATIONS) {
            // Still emit what we have - use the last content iteration
            val currentState = stateRef.get()
            val finalContent = if (currentState.contentByIteration.isNotEmpty()) {
                currentState.contentByIteration.last()
            } else {
                ""
            }
            val finalReasoning = currentState.allReasoning.trim()

            if (finalContent.isNotEmpty() || finalReasoning.isNotEmpty()) {
                val contentToEmit = finalContent.ifEmpty {
                    "I could not complete the analysis within the allowed iterations. Please retry."
                }

                val reasoningToEmit = if (finalContent.isNotEmpty() && finalReasoning.isNotEmpty()) {
                    finalReasoning
                } else {
                    null
                }

                send(AgentResponse.Complete(
                    content = contentToEmit,
                    reasoning = reasoningToEmit,
                    toolsUsed = toolsUsed.distinct().toList()
                ))
            }

            send(AgentResponse.Error(
                "Maximum iterations reached (${iteration} total, ${toolIterations} tool calls). The analysis may be incomplete.",
                isRetryable = false
            ))
        }
    }

    /**
     * Some providers emit cumulative streaming chunks (full text-so-far) while others emit deltas.
     * Normalize both into a delta to prevent duplicated message content.
     */
    private fun normalizeStreamingDelta(
        rawChunk: String,
        alreadyEmitted: String,
        previousSnapshot: String
    ): String {
        if (rawChunk.isEmpty()) return ""

        // Cross-iteration safety: some models repeat full prior content/reasoning.
        if (alreadyEmitted.isNotEmpty()) {
            if (rawChunk.startsWith(alreadyEmitted)) {
                return rawChunk.substring(alreadyEmitted.length)
            }
            if (alreadyEmitted.startsWith(rawChunk) || alreadyEmitted.contains(rawChunk)) {
                return ""
            }
            val embeddedIndex = rawChunk.lastIndexOf(alreadyEmitted)
            if (embeddedIndex >= 0) {
                return rawChunk.substring(embeddedIndex + alreadyEmitted.length)
            }
        }

        if (previousSnapshot.isEmpty()) return rawChunk

        return when {
            rawChunk.startsWith(previousSnapshot) -> rawChunk.substring(previousSnapshot.length)
            previousSnapshot.startsWith(rawChunk) || previousSnapshot.contains(rawChunk) -> ""
            else -> {
                val overlap = findOverlapSuffixPrefix(previousSnapshot, rawChunk)
                if (overlap > 0) return rawChunk.substring(overlap)

                val emittedOverlap = findOverlapSuffixPrefix(alreadyEmitted, rawChunk)
                if (emittedOverlap > 0) return rawChunk.substring(emittedOverlap)

                rawChunk
            }
        }
    }

    /**
     * Find the longest overlap where the suffix of `existing` matches the prefix of `incoming`.
     * This helps turn cumulative or partially-overlapping streams into clean deltas.
     */
    private fun findOverlapSuffixPrefix(existing: String, incoming: String): Int {
        if (existing.isEmpty() || incoming.isEmpty()) return 0
        val maxOverlap = minOf(existing.length, incoming.length, 2000)
        for (len in maxOverlap downTo 1) {
            if (existing.regionMatches(existing.length - len, incoming, 0, len, ignoreCase = false)) {
                return len
            }
        }
        return 0
    }

    /**
     * Parse embedded tool calls from AI response content
     */
    private fun parseEmbeddedToolCalls(content: String): List<ToolCallRequest> {
        val toolCalls = mutableListOf<ToolCallRequest>()
        val processedToolIds = mutableSetOf<String>()

        // Pattern 0: Protocol-style tool tags from some models
        // Example:
        // <|tool_call_begin|>
        // functions.get_dasha_info:0
        // <|tool_call_argument_begin|>{"profile_id":"current"}
        // <|tool_call_end|>
        val protocolPattern = Regex(
            """<\|tool_call_begin\|>[\s\S]*?(?:functions\.)?([a-zA-Z0-9_]+)(?::\d+)?[\s\S]*?<\|tool_call_argument_begin\|>\s*([\s\S]*?)(?:<\|tool_call_end\|>|<\|tool_calls_section_end\|>|$)""",
            RegexOption.IGNORE_CASE
        )
        protocolPattern.findAll(content).forEach { match ->
            val toolName = match.groupValues[1]
            val rawArgs = match.groupValues[2].trim()
            val arguments = extractFirstJsonObject(rawArgs) ?: rawArgs
            addToolCallIfNotExists(
                toolName = toolName,
                arguments = if (arguments.isBlank()) "{}" else arguments,
                toolCalls = toolCalls,
                processedIds = processedToolIds
            )
        }

        // Pattern 1: Standard tool_call code block
        val toolCallBlockPattern = Regex(
            """```tool_call\s*\n?\s*(\{[\s\S]*?\})\s*\n?```""",
            RegexOption.MULTILINE
        )
        toolCallBlockPattern.findAll(content).forEach { match ->
            parseToolCallJson(match.groupValues[1], toolCalls, processedToolIds)
        }

        // Pattern 2: JSON code block with tool call
        val jsonBlockPattern = Regex(
            """```json\s*\n?\s*(\{[\s\S]*?"tool"[\s\S]*?\})\s*\n?```""",
            RegexOption.MULTILINE
        )
        jsonBlockPattern.findAll(content).forEach { match ->
            parseToolCallJson(match.groupValues[1], toolCalls, processedToolIds)
        }

        // Pattern 3: Plain code block with JSON
        val plainBlockPattern = Regex(
            """```\s*\n?\s*(\{[\s\S]*?"tool"[\s\S]*?\})\s*\n?```""",
            RegexOption.MULTILINE
        )
        plainBlockPattern.findAll(content).forEach { match ->
            parseToolCallJson(match.groupValues[1], toolCalls, processedToolIds)
        }

        // Pattern 4: Inline JSON with tool key (handles nested arguments)
        val inlinePattern = Regex(
            """\{\s*"tool"\s*:\s*"([^"]+)"\s*,\s*"arguments"\s*:\s*(\{[^}]*(?:\{[^}]*\}[^}]*)*\})\s*\}"""
        )
        inlinePattern.findAll(content).forEach { match ->
            val toolName = match.groupValues[1]
            val arguments = match.groupValues[2]
            addToolCallIfNotExists(toolName, arguments, toolCalls, processedToolIds)
        }

        // Pattern 5: Name-first format (some models use this)
        val nameFirstPattern = Regex(
            """\{\s*"name"\s*:\s*"([^"]+)"\s*,\s*"parameters"\s*:\s*(\{[^}]*(?:\{[^}]*\}[^}]*)*\})\s*\}"""
        )
        nameFirstPattern.findAll(content).forEach { match ->
            val toolName = match.groupValues[1]
            val arguments = match.groupValues[2]
            addToolCallIfNotExists(toolName, arguments, toolCalls, processedToolIds)
        }

        // Pattern 6: Function call format (tool_name(args))
        val functionPattern = Regex(
            """(get_\w+|calculate_\w+)\s*\(\s*([^)]*)\s*\)"""
        )
        functionPattern.findAll(content).forEach { match ->
            val toolName = match.groupValues[1]
            val argsStr = match.groupValues[2]
            val arguments = parseFunctionArguments(argsStr)
            addToolCallIfNotExists(toolName, arguments, toolCalls, processedToolIds)
        }

        return toolCalls
    }

    /**
     * Parse JSON string into tool call and add to list
     */
    private fun parseToolCallJson(
        jsonStr: String,
        toolCalls: MutableList<ToolCallRequest>,
        processedIds: MutableSet<String>
    ) {
        try {
            val cleanedJson = jsonStr.trim()
            val json = JSONObject(cleanedJson)

            // Try different field names for tool name
            val toolName = json.optString("tool")
                .ifEmpty { json.optString("name") }
                .ifEmpty { json.optString("function") }

            if (toolName.isEmpty()) return

            // Try different field names for arguments
            val arguments = json.optJSONObject("arguments")
                ?: json.optJSONObject("parameters")
                ?: json.optJSONObject("args")
                ?: JSONObject()

            addToolCallIfNotExists(toolName, arguments.toString(), toolCalls, processedIds)
        } catch (e: Exception) {
            // JSON parsing failed - try to extract partial information
            tryPartialParse(jsonStr, toolCalls, processedIds)
        }
    }

    /**
     * Attempt to parse partial or malformed JSON
     */
    private fun tryPartialParse(
        jsonStr: String,
        toolCalls: MutableList<ToolCallRequest>,
        processedIds: MutableSet<String>
    ) {
        // Try to extract tool name even from malformed JSON
        val toolNameMatch = Regex(""""tool"\s*:\s*"([^"]+)"""").find(jsonStr)
            ?: Regex(""""name"\s*:\s*"([^"]+)"""").find(jsonStr)
            ?: Regex(""""function"\s*:\s*"([^"]+)"""").find(jsonStr)

        if (toolNameMatch != null) {
            val toolName = toolNameMatch.groupValues[1]

            // Try to extract arguments as JSON object first
            val argsMatch = Regex(""""arguments"\s*:\s*(\{[\s\S]*?\})""").find(jsonStr)
                ?: Regex(""""parameters"\s*:\s*(\{[\s\S]*?\})""").find(jsonStr)

            if (argsMatch != null) {
                addToolCallIfNotExists(toolName, argsMatch.groupValues[1], toolCalls, processedIds)
                return
            }

            // Fallback: reconstruct likely arguments from malformed payload
            val reconstructedArgs = JSONObject()
            val scalarPairs = Regex(
                """"([a-zA-Z_][a-zA-Z0-9_]*)"\s*:\s*("([^"]*)"|[-]?\d+(?:\.\d+)?|true|false|null)"""
            )
            val excludedKeys = setOf("tool", "name", "function", "arguments", "parameters", "args")

            scalarPairs.findAll(jsonStr).forEach { match ->
                val key = match.groupValues[1]
                if (key in excludedKeys) return@forEach
                val rawValue = match.groupValues[2]
                when {
                    rawValue.equals("true", ignoreCase = true) -> reconstructedArgs.put(key, true)
                    rawValue.equals("false", ignoreCase = true) -> reconstructedArgs.put(key, false)
                    rawValue.equals("null", ignoreCase = true) -> reconstructedArgs.put(key, JSONObject.NULL)
                    rawValue.startsWith("\"") && rawValue.endsWith("\"") -> reconstructedArgs.put(
                        key,
                        rawValue.substring(1, rawValue.length - 1)
                    )
                    rawValue.contains(".") -> rawValue.toDoubleOrNull()?.let { reconstructedArgs.put(key, it) }
                        ?: reconstructedArgs.put(key, rawValue)
                    else -> rawValue.toLongOrNull()?.let { reconstructedArgs.put(key, it) }
                        ?: reconstructedArgs.put(key, rawValue)
                }
            }

            // Handle highly malformed variants like profile_id12 (missing colon)
            if (!reconstructedArgs.has("profile_id")) {
                Regex("""profile_id\s*[:=]?\s*"?(\d+)""", RegexOption.IGNORE_CASE)
                    .find(jsonStr)
                    ?.groupValues
                    ?.getOrNull(1)
                    ?.let { reconstructedArgs.put("profile_id", it) }
            }

            if (!reconstructedArgs.has("years_ahead")) {
                Regex("""years_ahead\s*[:=]?\s*"?(\d+)""", RegexOption.IGNORE_CASE)
                    .find(jsonStr)
                    ?.groupValues
                    ?.getOrNull(1)
                    ?.toIntOrNull()
                    ?.let { reconstructedArgs.put("years_ahead", it) }
            }

            if (!reconstructedArgs.has("dasha_type")) {
                Regex("""dasha_type\s*[:=]?\s*"?(vimshottari|yogini|chara)""", RegexOption.IGNORE_CASE)
                    .find(jsonStr)
                    ?.groupValues
                    ?.getOrNull(1)
                    ?.let { reconstructedArgs.put("dasha_type", it.lowercase()) }
            }

            addToolCallIfNotExists(toolName, reconstructedArgs.toString(), toolCalls, processedIds)
        }
    }

    /**
     * Extract the first balanced JSON object from text.
     */
    private fun extractFirstJsonObject(text: String): String? {
        val start = text.indexOf('{')
        if (start == -1) return null

        var depth = 0
        var inString = false
        var escaped = false

        for (i in start until text.length) {
            val c = text[i]
            if (escaped) {
                escaped = false
                continue
            }
            when {
                c == '\\' -> escaped = true
                c == '"' -> inString = !inString
                !inString && c == '{' -> depth++
                !inString && c == '}' -> {
                    depth--
                    if (depth == 0) {
                        return text.substring(start, i + 1)
                    }
                }
            }
        }

        return null
    }

    /**
     * Add tool call if not already processed (deduplication)
     */
    private fun addToolCallIfNotExists(
        toolName: String,
        arguments: String,
        toolCalls: MutableList<ToolCallRequest>,
        processedIds: MutableSet<String>
    ) {
        // Create a unique key for deduplication based on tool name and canonicalized arguments
        val canonicalArgs = canonicalizeJson(arguments)
        val toolKey = "$toolName:$canonicalArgs"
        if (toolKey in processedIds) return

        processedIds.add(toolKey)
        toolCalls.add(ToolCallRequest(
            id = "tool_${UUID.randomUUID().toString().take(8)}",
            name = toolName,
            arguments = arguments
        ))
    }

    /**
     * Parse function-style arguments into JSON
     */
    private fun parseFunctionArguments(argsStr: String): String {
        if (argsStr.isBlank()) return "{}"

        val argsJson = JSONObject()

        // Split by comma, handling quoted strings
        val argPairs = argsStr.split(Regex(""",(?=(?:[^"'*`*]*"[^"'*`*]*")*[^"'*`*]*$)"""))

        for (pair in argPairs) {
            val parts = pair.split("=", limit = 2)
            if (parts.size == 2) {
                val key = parts[0].trim()
                var value = parts[1].trim()

                // Remove surrounding quotes if present
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length - 1)
                }

                argsJson.put(key, value)
            }
        }

        return argsJson.toString()
    }

    /**
     * Execute a tool call
     */
    private suspend fun executeToolCall(
        toolCall: ToolCallRequest,
        currentProfile: SavedChart?,
        allProfiles: List<SavedChart>,
        currentChart: VedicChart?
    ): ToolExecutionResult {
        return try {
            val arguments = try {
                JSONObject(toolCall.arguments)
            } catch (e: Exception) {
                val extracted = extractFirstJsonObject(toolCall.arguments)
                if (extracted != null) {
                    try {
                        JSONObject(extracted)
                    } catch (_: Exception) {
                        JSONObject()
                    }
                } else {
                    val wrapped = toolCall.arguments.trim().trim(',').trim()
                    if (wrapped.contains(":")) {
                        try {
                            JSONObject("{${wrapped.removePrefix("{").removeSuffix("}")}}")
                        } catch (_: Exception) {
                            JSONObject()
                        }
                    } else {
                        JSONObject()
                    }
                }
            }

            toolRegistry.executeTool(
                toolName = toolCall.name,
                arguments = arguments,
                currentProfile = currentProfile,
                allProfiles = allProfiles,
                currentChart = currentChart
            )
        } catch (e: Exception) {
            ToolExecutionResult(
                success = false,
                data = null,
                error = e.message ?: "Unknown error executing tool",
                summary = "Failed to execute ${toolCall.name}"
            )
        }
    }

    /**
     * Clean tool call blocks from content for display
     */
    private fun String.cleanToolCallBlocks(): String {
        return this
            .replace(Regex("""```tool_call\s*\n?\s*\{[\s\S]*?\}\s*\n?```"""), "")
            .replace(Regex("""```json\s*\n?\s*\{[\s\S]*?"tool"[\s\S]*?\}\s*\n?```""", RegexOption.MULTILINE), "")
            .replace(Regex("""\{\"tool\"\s*:\s*\"[^\"]+\"\s*,\s*\"arguments\"\s*:\s*\{[\s\S]*?\}\s*\}"""), "")
            .replace(Regex("""<\|tool_calls_section_begin\|>[\s\S]*?<\|tool_calls_section_end\|>""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""<\|tool_calls_section_begin\|>[\s\S]*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?m)^\s*<\|tool_[^|]+?\|>\s*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?m)^\s*(?:functions\.)?[a-zA-Z0-9_]+\s*:\s*\d+\s*$""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""(?m)^\s*tool_call\s*$"""), "")
            .replace(Regex("""(?m)^\s*`{3}\s*$"""), "")
            .replace(Regex("""\{\s*"tool"\s*:\s*"[^"]+"\s*,[\s\S]*$"""), "")
            .trim()
    }
}

/**
 * Tool call request data
 */
data class ToolCallRequest(
    val id: String,
    val name: String,
    val arguments: String
)

/**
 * Sealed class for agent responses
 */
sealed class AgentResponse {
    /**
     * Content chunk during streaming
     */
    data class ContentChunk(val text: String) : AgentResponse()

    /**
     * Reasoning/thinking chunk
     */
    data class ReasoningChunk(val text: String) : AgentResponse()

    /**
     * Model information
     */
    data class ModelInfo(val providerId: String, val model: String) : AgentResponse()

    /**
     * Tool calls are starting
     */
    data class ToolCallsStarted(val toolNames: List<String>) : AgentResponse()

    /**
     * A tool is being executed
     */
    data class ToolExecuting(val toolName: String) : AgentResponse()

    /**
     * Tool execution result
     */
    data class ToolResult(
        val toolName: String,
        val success: Boolean,
        val summary: String
    ) : AgentResponse()

    /**
     * Token usage information
     */
    data class TokenUsage(
        val promptTokens: Int,
        val completionTokens: Int,
        val totalTokens: Int
    ) : AgentResponse()

    /**
     * Error occurred
     */
    data class Error(
        val message: String,
        val isRetryable: Boolean = false
    ) : AgentResponse()

    /**
     * Retry notification (rate limit or transient error)
     */
    data class RetryInfo(
        val attempt: Int,
        val maxAttempts: Int,
        val delayMs: Long,
        val reason: String
    ) : AgentResponse()

    /**
     * Agent is asking the user a question and waiting for response.
     * This pauses the agent execution until the user responds.
     */
    data class AskUserInterrupt(
        val question: String,
        val options: List<AskUserOptionData>,
        val allowCustomInput: Boolean,
        val context: String?,
        val conversationHistory: List<ChatMessage>,
        val toolsUsed: List<String>
    ) : AgentResponse()

    /**
     * Response complete
     */
    data class Complete(
        val content: String,
        val reasoning: String?,
        val toolsUsed: List<String>
    ) : AgentResponse()
}

/**
 * Data class for ask_user options
 */
data class AskUserOptionData(
    val label: String,
    val description: String?,
    val value: String
)

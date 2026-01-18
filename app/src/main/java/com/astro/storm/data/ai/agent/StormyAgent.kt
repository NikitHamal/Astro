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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
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
    ): Flow<AgentResponse> = flow {
        val provider = providerRegistry.getProvider(model.providerId)
            ?: throw IllegalStateException("Provider not found: ${model.providerId}")

        val systemPrompt = generateSystemPrompt(currentProfile, allProfiles, currentChart, language)

        // Build messages with system prompt
        val fullMessages = mutableListOf<ChatMessage>()
        fullMessages.add(ChatMessage(role = MessageRole.SYSTEM, content = systemPrompt))
        fullMessages.addAll(messages)

        var iteration = 0
        var toolIterations = 0
        var continueProcessing = true
        val toolsUsed = mutableListOf<String>()

        // Track content from each iteration separately to detect duplicates
        val contentByIteration = mutableListOf<String>()
        val allReasoning = StringBuilder()

        // Keep track of what we've already emitted to avoid duplicates
        var totalEmittedContent = StringBuilder()
        var totalEmittedReasoning = StringBuilder()

        while (continueProcessing && iteration < MAX_TOTAL_ITERATIONS && toolIterations < MAX_TOOL_ITERATIONS) {
            iteration++
            var currentContent = StringBuilder()
            var currentReasoning = StringBuilder()
            var pendingToolCalls = mutableListOf<ToolCallRequest>()
            var hasError = false

            // Call the AI model
            provider.chat(
                messages = fullMessages,
                model = model.id,
                temperature = temperature,
                maxTokens = maxTokens,
                stream = true
            ).collect { response ->
                when (response) {
                    is ChatResponse.Content -> {
                        currentContent.append(response.text)
                        // Only emit if this content hasn't already been emitted
                        val newContent = response.text
                        if (newContent.isNotEmpty()) {
                            // Check if this chunk is already in our total emitted content
                            val currentTotal = totalEmittedContent.toString()
                            if (!currentTotal.endsWith(newContent) &&
                                !currentTotal.contains(newContent.take(50))) {
                                totalEmittedContent.append(newContent)
                                emit(AgentResponse.ContentChunk(newContent))
                            }
                        }
                    }
                    is ChatResponse.Reasoning -> {
                        // Skip empty reasoning markers
                        if (response.text.isNotEmpty()) {
                            currentReasoning.append(response.text)
                            // Check for duplicate reasoning chunks
                            val newReasoning = response.text
                            val currentTotalReasoning = totalEmittedReasoning.toString()
                            if (!currentTotalReasoning.endsWith(newReasoning) &&
                                !currentTotalReasoning.contains(newReasoning.take(50))) {
                                totalEmittedReasoning.append(newReasoning)
                                emit(AgentResponse.ReasoningChunk(newReasoning))
                            }
                        }
                    }
                    is ChatResponse.ToolCallRequest -> {
                        response.toolCalls.forEach { call ->
                            pendingToolCalls.add(
                                ToolCallRequest(
                                    id = call.id.ifEmpty { "tool_${UUID.randomUUID().toString().take(8)}" },
                                    name = call.function.name,
                                    arguments = call.function.arguments
                                )
                            )
                        }
                    }
                    is ChatResponse.Error -> {
                        hasError = true
                        emit(AgentResponse.Error(response.message, response.isRetryable))
                    }
                    is ChatResponse.Usage -> {
                        emit(AgentResponse.TokenUsage(
                            response.promptTokens,
                            response.completionTokens,
                            response.totalTokens
                        ))
                    }
                    is ChatResponse.Done -> {
                        // Check for embedded tool calls in content
                        if (pendingToolCalls.isEmpty()) {
                            pendingToolCalls.addAll(parseEmbeddedToolCalls(currentContent.toString()))
                        }
                    }
                    is ChatResponse.ProviderInfo -> {
                        emit(AgentResponse.ModelInfo(response.providerId, response.model))
                    }
                    is ChatResponse.RetryNotification -> {
                        // Emit retry notification so UI can show it
                        emit(AgentResponse.RetryInfo(
                            attempt = response.attempt,
                            maxAttempts = response.maxAttempts,
                            delayMs = response.delayMs,
                            reason = response.reason
                        ))
                    }
                }
            }

            // Clean current content of tool call blocks
            val cleanedCurrentContent = currentContent.toString().cleanToolCallBlocks().trim()

            // Only add this iteration's content if it's not a duplicate of previous content
            if (cleanedCurrentContent.isNotEmpty()) {
                val isDuplicate = contentByIteration.any { previousContent ->
                    previousContent.trim() == cleanedCurrentContent ||
                    previousContent.contains(cleanedCurrentContent) ||
                    cleanedCurrentContent.contains(previousContent.trim())
                }

                if (!isDuplicate) {
                    contentByIteration.add(cleanedCurrentContent)
                }
            }

            // Accumulate reasoning (reasoning can be additive across iterations)
            if (currentReasoning.isNotEmpty()) {
                if (allReasoning.isNotEmpty()) {
                    allReasoning.append("\n\n")
                }
                allReasoning.append(currentReasoning)
            }

            if (hasError) {
                continueProcessing = false
                continue
            }

            // Process tool calls if any
            if (pendingToolCalls.isNotEmpty()) {
                toolIterations++
                emit(AgentResponse.ToolCallsStarted(pendingToolCalls.map { it.name }))

                // Add assistant message with tool calls
                val assistantContent = currentContent.toString().cleanToolCallBlocks()
                fullMessages.add(ChatMessage(
                    role = MessageRole.ASSISTANT,
                    content = assistantContent.ifEmpty { "I'll use some tools to help answer your question." },
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
                    emit(AgentResponse.ToolExecuting(toolCall.name))
                    if (!toolsUsed.contains(toolCall.name)) {
                        toolsUsed.add(toolCall.name)
                    }

                    val result = executeToolCall(toolCall, currentProfile, allProfiles, currentChart)

                    emit(AgentResponse.ToolResult(toolCall.name, result.success, result.summary))

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
                                }
                            }
                        } catch (e: Exception) {
                            // Failed to parse ask_user result, continue normally
                        }
                    }
                }

                // If ask_user was called, interrupt and wait for user response
                if (askUserInterrupt != null) {
                    emit(askUserInterrupt)
                    continueProcessing = false
                    return@flow // Exit the flow - UI will handle resuming with user response
                }

                // Continue processing to let AI respond to tool results
            } else {
                // No tool calls - combine unique content from all iterations
                val finalContent = if (contentByIteration.isNotEmpty()) {
                    contentByIteration.last()
                } else {
                    ""
                }
                val finalReasoning = allReasoning.toString().trim()

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
                        contentByIteration.clear()
                        allReasoning.clear()

                        // Continue for one more iteration
                        continue
                    }
                }

                val contentToEmit = if (finalContent.isEmpty() && finalReasoning.isNotEmpty()) {
                    finalReasoning
                } else {
                    finalContent
                }

                val reasoningToEmit = if (finalContent.isNotEmpty() && finalReasoning.isNotEmpty()) {
                    finalReasoning
                } else {
                    null
                }

                continueProcessing = false
                emit(AgentResponse.Complete(
                    content = contentToEmit,
                    reasoning = reasoningToEmit,
                    toolsUsed = toolsUsed.distinct().toList()
                ))
            }
        }

        if (iteration >= MAX_TOTAL_ITERATIONS || toolIterations >= MAX_TOOL_ITERATIONS) {
            // Still emit what we have - use the last content iteration
            val finalContent = if (contentByIteration.isNotEmpty()) {
                contentByIteration.last()
            } else {
                ""
            }
            val finalReasoning = allReasoning.toString().trim()

            if (finalContent.isNotEmpty() || finalReasoning.isNotEmpty()) {
                val contentToEmit = if (finalContent.isEmpty() && finalReasoning.isNotEmpty()) {
                    finalReasoning
                } else {
                    finalContent.ifEmpty { "I apologize, but I wasn't able to complete my analysis within the allowed iterations. Here's what I was able to determine..." }
                }

                val reasoningToEmit = if (finalContent.isNotEmpty() && finalReasoning.isNotEmpty()) {
                    finalReasoning
                } else {
                    null
                }

                emit(AgentResponse.Complete(
                    content = contentToEmit,
                    reasoning = reasoningToEmit,
                    toolsUsed = toolsUsed.distinct().toList()
                ))
            }

            emit(AgentResponse.Error(
                "Maximum iterations reached (${iteration} total, ${toolIterations} tool calls). The analysis may be incomplete.",
                isRetryable = false
            ))
        }
    }

    /**
     * Parse embedded tool calls from AI response content
     */
    private fun parseEmbeddedToolCalls(content: String): List<ToolCallRequest> {
        val toolCalls = mutableListOf<ToolCallRequest>()
        val processedToolIds = mutableSetOf<String>()

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

        if (toolNameMatch != null) {
            val toolName = toolNameMatch.groupValues[1]

            // Try to extract arguments
            val argsMatch = Regex(""""arguments"\s*:\s*(\{[^}]*\})"""").find(jsonStr)
                ?: Regex(""""parameters"\s*:\s*(\{[^}]*\})"""").find(jsonStr)

            val arguments = argsMatch?.groupValues?.get(1) ?: "{}"
            addToolCallIfNotExists(toolName, arguments, toolCalls, processedIds)
        }
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
        // Create a unique key for deduplication based on tool name and arguments
        val toolKey = "$toolName:${arguments.hashCode()}"
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
        val argPairs = argsStr.split(Regex(""",(?=(?:[^"'*`]*"[^"'*`]*")*[^"'*`]*$)"""))

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
                JSONObject()
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
            .replace(Regex("""\{\"tool\"\s*:\s*\"[^\"]+\"\s*,\s*\"arguments\"\s*:\s*\{[^}]*\}\s*\}"""), "")
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

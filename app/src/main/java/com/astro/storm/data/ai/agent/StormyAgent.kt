package com.astro.storm.data.ai.agent

import android.content.Context
import com.astro.storm.core.common.Language
import com.astro.storm.core.model.AgentStreamState
import com.astro.storm.core.model.FlashyPart
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ToolState
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.ai.agent.tools.AstrologyToolRegistry
import com.astro.storm.data.ai.agent.tools.ToolExecutionResult
import com.astro.storm.data.ai.provider.AiModel
import com.astro.storm.data.ai.provider.AiProviderRegistry
import com.astro.storm.data.ai.provider.ChatMessage
import com.astro.storm.data.ai.provider.ChatResponse
import com.astro.storm.data.ai.provider.FunctionCall
import com.astro.storm.data.ai.provider.MessageRole
import com.astro.storm.data.ai.provider.ToolCall
import com.astro.storm.data.repository.SavedChart
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
 * "Flashy" Architecture:
 * - Uses a linear list of `FlashyPart`s (Text, Thought, ToolCall, ToolResult).
 * - Streams updates by mutating the last part or appending new parts.
 * - Supports multi-turn tool execution loop.
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
        private const val MAX_TOOL_ITERATIONS = 15
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

    fun generateSystemPrompt(
        currentProfile: SavedChart?,
        allProfiles: List<SavedChart>,
        currentChart: VedicChart?,
        language: Language = Language.ENGLISH
    ): String {
        return promptManager.generateSystemPrompt(currentProfile, allProfiles, currentChart, language)
    }

    fun processMessage(
        messages: List<ChatMessage>,
        model: AiModel,
        currentProfile: SavedChart?,
        allProfiles: List<SavedChart>,
        currentChart: VedicChart?,
        temperature: Float? = null,
        maxTokens: Int? = null,
        language: Language = Language.ENGLISH
    ): Flow<AgentStreamState> = flow {
        val provider = providerRegistry.getProvider(model.providerId)
            ?: throw IllegalStateException("Provider not found: ${model.providerId}")

        val systemPrompt = generateSystemPrompt(currentProfile, allProfiles, currentChart, language)
        val fullMessages = mutableListOf<ChatMessage>()
        fullMessages.add(ChatMessage(role = MessageRole.SYSTEM, content = systemPrompt))
        fullMessages.addAll(messages)

        var iteration = 0
        var toolIterations = 0
        var continueProcessing = true
        
        val parts = mutableListOf<FlashyPart>()
        val toolsUsed = mutableListOf<String>()

        while (continueProcessing && iteration < MAX_TOTAL_ITERATIONS && toolIterations < MAX_TOOL_ITERATIONS) {
            iteration++
            var pendingToolCalls = mutableListOf<ToolCallRequest>()
            var currentIterationText = StringBuilder()

            provider.chat(
                messages = fullMessages,
                model = model.id,
                temperature = temperature,
                maxTokens = maxTokens,
                stream = true
            ).collect {
                when (it) {
                    is ChatResponse.Content -> {
                        if (it.text.isNotEmpty()) {
                            currentIterationText.append(it.text)
                            val lastIdx = parts.lastIndex
                            if (lastIdx >= 0 && parts[lastIdx] is FlashyPart.Text) {
                                val last = parts[lastIdx] as FlashyPart.Text
                                parts[lastIdx] = last.copy(content = last.content + it.text, isStreaming = true)
                            } else {
                                parts.add(FlashyPart.Text(content = it.text, isStreaming = true))
                            }
                            emit(AgentStreamState.Update(parts.toList()))
                        }
                    }
                    is ChatResponse.Reasoning -> {
                        if (it.text.isNotEmpty()) {
                            val lastIdx = parts.lastIndex
                            if (lastIdx >= 0 && parts[lastIdx] is FlashyPart.Thought) {
                                val last = parts[lastIdx] as FlashyPart.Thought
                                parts[lastIdx] = last.copy(content = last.content + it.text, isStreaming = true)
                            } else {
                                parts.add(FlashyPart.Thought(content = it.text, isStreaming = true, isCollapsed = false))
                            }
                            emit(AgentStreamState.Update(parts.toList()))
                        }
                    }
                    is ChatResponse.ToolCallRequest -> {
                        it.toolCalls.forEach { call ->
                            val id = call.id.ifEmpty { "tool_${UUID.randomUUID().toString().take(8)}" }
                            pendingToolCalls.add(ToolCallRequest(id, call.function.name, call.function.arguments))
                            
                            parts.add(FlashyPart.ToolCall(
                                id = id,
                                name = call.function.name,
                                args = call.function.arguments,
                                state = ToolState.Pending
                            ))
                            emit(AgentStreamState.Update(parts.toList()))
                        }
                    }
                    is ChatResponse.Error -> {
                        emit(AgentStreamState.Error(it.message))
                        continueProcessing = false
                    }
                    is ChatResponse.Usage -> {
                        emit(AgentStreamState.TokenUsage(
                            it.promptTokens, it.completionTokens, it.totalTokens
                        ))
                    }
                    is ChatResponse.Done -> {
                        val lastIdx = parts.lastIndex
                        if (lastIdx >= 0) {
                            when (val last = parts[lastIdx]) {
                                is FlashyPart.Text -> parts[lastIdx] = last.copy(isStreaming = false)
                                is FlashyPart.Thought -> parts[lastIdx] = last.copy(isStreaming = false)
                                else -> {}
                            }
                        }
                        
                        if (pendingToolCalls.isEmpty()) {
                            val embeddedCalls = parseEmbeddedToolCalls(currentIterationText.toString())
                            if (embeddedCalls.isNotEmpty()) {
                                pendingToolCalls.addAll(embeddedCalls)
                                embeddedCalls.forEach { call ->
                                    parts.add(FlashyPart.ToolCall(
                                        id = call.id,
                                        name = call.name,
                                        args = call.arguments,
                                        state = ToolState.Pending
                                    ))
                                }
                                emit(AgentStreamState.Update(parts.toList()))
                            }
                        }
                    }
                    else -> {}
                }
            }

            if (!continueProcessing) break

            if (pendingToolCalls.isNotEmpty()) {
                toolIterations++
                
                fullMessages.add(ChatMessage(
                    role = MessageRole.ASSISTANT,
                    content = currentIterationText.toString().cleanToolCallBlocks().ifEmpty { "Using tools..." },
                    toolCalls = pendingToolCalls.map { call ->
                        ToolCall(id = call.id, function = FunctionCall(call.name, call.arguments))
                    }
                ))

                for (toolCall in pendingToolCalls) {
                    val partIdx = parts.indexOfFirst { it is FlashyPart.ToolCall && it.id == toolCall.id }
                    if (partIdx >= 0) {
                        parts[partIdx] = (parts[partIdx] as FlashyPart.ToolCall).copy(state = ToolState.Executing)
                        emit(AgentStreamState.Update(parts.toList()))
                    }

                    if (!toolsUsed.contains(toolCall.name)) toolsUsed.add(toolCall.name)

                    val result = executeToolCall(toolCall, currentProfile, allProfiles, currentChart)

                    if (partIdx >= 0) {
                        parts[partIdx] = (parts[partIdx] as FlashyPart.ToolCall).copy(
                            state = if (result.success) ToolState.Completed else ToolState.Failed
                        )
                    }
                    
                    parts.add(FlashyPart.ToolResult(
                        id = toolCall.id,
                        result = result.summary,
                        isError = !result.success
                    ))
                    emit(AgentStreamState.Update(parts.toList()))

                    fullMessages.add(ChatMessage(
                        role = MessageRole.TOOL,
                        content = result.toJson(),
                        toolCallId = toolCall.id,
                        name = toolCall.name
                    ))

                    if (toolCall.name == "ask_user" && result.success) {
                        result.data?.let { data ->
                            val question = data.optString("question", "")
                            if (question.isNotEmpty()) {
                                val optionsList = mutableListOf<Map<String, String>>()
                                data.optJSONArray("options")?.let { arr ->
                                    for (i in 0 until arr.length()) {
                                        val opt = arr.optJSONObject(i)
                                        optionsList.add(mapOf(
                                            "label" to opt.optString("label"),
                                            "value" to opt.optString("value")
                                        ))
                                    }
                                }
                                emit(AgentStreamState.Interrupt(
                                    question = question,
                                    options = optionsList,
                                    allowCustomInput = data.optBoolean("allow_custom_input", true),
                                    context = data.optString("context")
                                ))
                                return@flow
                            }
                        }
                    }
                }
            } else {
                continueProcessing = false 
            }
        }
        
        emit(AgentStreamState.Finished(parts.toList()))
    }

    private fun parseEmbeddedToolCalls(content: String): List<ToolCallRequest> {
        val toolCalls = mutableListOf<ToolCallRequest>()
        val processedIds = mutableSetOf<String>()
        
        val blockPattern = Regex("""```tool_call\s*\n?\s*(\{[\s\S]*?\})\s*\n?```""")
        blockPattern.findAll(content).forEach { 
            parseToolCallJson(it.groupValues[1], toolCalls, processedIds) 
        }
        
        return toolCalls
    }

    private fun parseToolCallJson(jsonStr: String, list: MutableList<ToolCallRequest>, ids: MutableSet<String>) {
        try {
            val json = JSONObject(jsonStr)
            val name = json.optString("tool").ifEmpty { json.optString("name") }
            val args = json.optJSONObject("arguments") ?: json.optJSONObject("parameters") ?: JSONObject()
            
            if (name.isNotEmpty() && name !in ids) {
                ids.add(name)
                list.add(ToolCallRequest(
                    id = "tool_${UUID.randomUUID().toString().take(8)}",
                    name = name,
                    arguments = args.toString()
                ))
            }
        } catch (e: Exception) {}
    }

    private suspend fun executeToolCall(
        toolCall: ToolCallRequest,
        currentProfile: SavedChart?,
        allProfiles: List<SavedChart>,
        currentChart: VedicChart?
    ): ToolExecutionResult {
        return try {
            val args = try { JSONObject(toolCall.arguments) } catch(e: Exception) { JSONObject() }
            toolRegistry.executeTool(
                toolName = toolCall.name,
                arguments = args,
                currentProfile = currentProfile,
                allProfiles = allProfiles,
                currentChart = currentChart
            )
        } catch (e: Exception) {
            ToolExecutionResult(false, null, e.message ?: "Error", "Failed: ${e.message}")
        }
    }

    private fun String.cleanToolCallBlocks(): String {
        return this.replace(Regex("""```tool_call\s*\n?\s*\{[\s\S]*?\}\s*\n?```"""), "").trim()
    }
}

data class ToolCallRequest(val id: String, val name: String, val arguments: String)

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
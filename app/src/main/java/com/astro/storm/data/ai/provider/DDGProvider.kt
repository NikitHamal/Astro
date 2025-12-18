package com.astro.storm.data.ai.provider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * DuckDuckGo AI Chat Provider
 *
 * DuckDuckGo provides free access to various AI models through their duck.ai service.
 * This implementation mirrors gpt4free's DDG provider.
 *
 * Features:
 * - Multiple free AI models (Claude, GPT, Llama, Mixtral)
 * - Privacy-focused (no tracking)
 * - No API key required
 * - Streaming support
 *
 * Models available:
 * - claude-3-haiku: Anthropic Claude 3 Haiku
 * - gpt-4o-mini: OpenAI GPT-4o Mini
 * - llama-3.3-70b: Meta Llama 3.3 70B
 * - mixtral-small-24b: Mistral Mixtral Small 24B
 * - o3-mini: OpenAI o3-mini (reasoning model)
 */
class DDGProvider : AiProvider {

    override val providerId: String = "ddg"
    override val displayName: String = "DuckDuckGo AI"
    override val baseUrl: String = "https://duckduckgo.com"
    override val isWorking: Boolean = true
    override val supportsStreaming: Boolean = true
    override val supportsSystemMessage: Boolean = true
    override val supportsMessageHistory: Boolean = true

    override val defaultModel: String = "gpt-4o-mini"

    private val statusEndpoint = "https://duckduckgo.com/duckchat/v1/status"
    private val chatEndpoint = "https://duckduckgo.com/duckchat/v1/chat"

    // VQD token - required for DuckDuckGo API calls
    @Volatile
    private var vqdToken: String? = null

    // Cached models
    private var cachedModels: List<AiModel>? = null

    /**
     * Model mapping - DDG uses specific internal model names
     */
    private val modelMapping = mapOf(
        "gpt-4o-mini" to "gpt-4o-mini",
        "claude-3-haiku" to "claude-3-haiku-20240307",
        "llama-3.3-70b" to "meta-llama/Llama-3.3-70B-Instruct-Turbo",
        "mixtral-small-24b" to "mistralai/Mistral-Small-24B-Instruct-2501",
        "o3-mini" to "o3-mini"
    )

    /**
     * Available models configuration
     */
    private val modelConfigs = mapOf(
        "gpt-4o-mini" to ModelConfig(
            id = "gpt-4o-mini",
            name = "GPT-4o Mini",
            description = "OpenAI's efficient GPT-4o Mini model",
            maxTokens = 8192
        ),
        "claude-3-haiku" to ModelConfig(
            id = "claude-3-haiku",
            name = "Claude 3 Haiku",
            description = "Anthropic's fast and efficient Claude 3 Haiku",
            maxTokens = 8192
        ),
        "llama-3.3-70b" to ModelConfig(
            id = "llama-3.3-70b",
            name = "Llama 3.3 70B",
            description = "Meta's powerful Llama 3.3 70B model",
            maxTokens = 8192
        ),
        "mixtral-small-24b" to ModelConfig(
            id = "mixtral-small-24b",
            name = "Mixtral Small 24B",
            description = "Mistral's efficient Mixtral Small model",
            maxTokens = 8192
        ),
        "o3-mini" to ModelConfig(
            id = "o3-mini",
            name = "OpenAI o3-mini",
            description = "OpenAI's o3-mini reasoning model",
            maxTokens = 8192,
            supportsReasoning = true
        )
    )

    override suspend fun getModels(): List<AiModel> {
        return cachedModels ?: getDefaultModels().also { cachedModels = it }
    }

    override suspend fun refreshModels(): Boolean {
        cachedModels = getDefaultModels()
        return true
    }

    private fun getDefaultModels(): List<AiModel> = modelConfigs.values.map { config ->
        AiModel(
            id = config.id,
            name = config.name,
            providerId = providerId,
            displayName = config.name,
            description = config.description,
            maxTokens = config.maxTokens,
            supportsVision = config.supportsVision,
            supportsReasoning = config.supportsReasoning,
            supportsTools = true
        )
    }

    override fun chat(
        messages: List<ChatMessage>,
        model: String?,
        temperature: Float?,
        maxTokens: Int?,
        stream: Boolean
    ): Flow<ChatResponse> = flow {
        val modelId = model ?: defaultModel
        val internalModelId = modelMapping[modelId] ?: modelMapping[defaultModel]!!

        emit(ChatResponse.ProviderInfo(providerId, modelId))

        var lastException: Exception? = null
        val maxRetries = 3

        for (attempt in 0 until maxRetries) {
            try {
                // Fetch VQD token if needed
                if (vqdToken == null) {
                    vqdToken = fetchVqdToken()
                }

                val currentVqd = vqdToken
                    ?: throw AiProviderException("Failed to obtain VQD token", "auth_error", true)

                val url = URL(chatEndpoint)
                val connection = url.openConnection() as HttpsURLConnection

                try {
                    connection.requestMethod = "POST"
                    connection.doOutput = true
                    connection.connectTimeout = 30000
                    connection.readTimeout = 120000

                    applyHeaders(connection, currentVqd)

                    val payload = buildRequestPayload(messages, internalModelId)

                    connection.outputStream.use { os ->
                        os.write(payload.toString().toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    // Update VQD token from response headers
                    connection.getHeaderField("x-vqd-4")?.let { newVqd ->
                        vqdToken = newVqd
                    }

                    if (responseCode == 429) {
                        vqdToken = null // Force refresh
                        throw RateLimitException("Rate limit exceeded")
                    }

                    if (responseCode == 401 || responseCode == 403) {
                        vqdToken = null // Force refresh
                        throw AiProviderException("Authentication error", "auth_error", isRetryable = true)
                    }

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        val errorBody = try {
                            connection.errorStream?.bufferedReader()?.readText() ?: "Unknown error"
                        } catch (e: Exception) {
                            "Unknown error"
                        }

                        // Check if token expired
                        if (errorBody.contains("vqd", ignoreCase = true) ||
                            errorBody.contains("token", ignoreCase = true)) {
                            vqdToken = null
                            throw AiProviderException("Token expired", "auth_error", isRetryable = true)
                        }

                        throw AiProviderException("HTTP $responseCode: $errorBody", "http_error",
                            isRetryable = responseCode >= 500)
                    }

                    val reader = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8))
                    val contentBuilder = StringBuilder()
                    val reasoningBuilder = StringBuilder()
                    var isInReasoning = false

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val text = line?.trim() ?: continue
                        if (text.isEmpty()) continue

                        // DDG uses SSE format
                        if (!text.startsWith("data:")) continue

                        val jsonStr = text.removePrefix("data:").trim()
                        if (jsonStr.isEmpty() || jsonStr == "[DONE]") {
                            if (jsonStr == "[DONE]") {
                                // Emit final content if accumulated
                                if (contentBuilder.isNotEmpty()) {
                                    emit(ChatResponse.Content("", isComplete = true))
                                }
                                if (reasoningBuilder.isNotEmpty()) {
                                    emit(ChatResponse.Reasoning("", isComplete = true))
                                }
                                emit(ChatResponse.Done())
                            }
                            continue
                        }

                        try {
                            val json = JSONObject(jsonStr)

                            // Check for errors
                            json.optString("action", null)?.let { action ->
                                if (action == "error") {
                                    val errorStatus = json.optInt("status", 0)
                                    val errorType = json.optString("type", "unknown")
                                    vqdToken = null
                                    throw AiProviderException(
                                        "DDG error: $errorType (status: $errorStatus)",
                                        errorType,
                                        isRetryable = errorStatus >= 500 || errorType.contains("rate", ignoreCase = true)
                                    )
                                }
                            }

                            // Parse message content
                            val message = json.optString("message", null)
                            if (!message.isNullOrEmpty()) {
                                // Check if this is reasoning content (o3-mini model)
                                val role = json.optString("role", "")

                                if (role == "reasoning" || isInReasoning) {
                                    if (!isInReasoning) {
                                        isInReasoning = true
                                    }
                                    reasoningBuilder.append(message)
                                    emit(ChatResponse.Reasoning(message))
                                } else {
                                    if (isInReasoning) {
                                        isInReasoning = false
                                    }
                                    contentBuilder.append(message)
                                    emit(ChatResponse.Content(message))
                                }
                            }

                            // Check for finish reason
                            val created = json.optLong("created", 0)
                            if (created > 0 && json.has("model")) {
                                // This is the final summary message
                                // Already emitted content above
                            }

                        } catch (e: org.json.JSONException) {
                            // Skip malformed JSON
                        }
                    }

                    reader.close()

                    // Success - return
                    return@flow

                } finally {
                    connection.disconnect()
                }

            } catch (e: RateLimitException) {
                lastException = e
                if (attempt < maxRetries - 1) {
                    val delayMs = (3000L * (1 shl attempt)).coerceAtMost(15000L)
                    emit(ChatResponse.RetryNotification(
                        attempt = attempt + 1,
                        maxAttempts = maxRetries,
                        delayMs = delayMs,
                        reason = "Rate limit exceeded"
                    ))
                    delay(delayMs)
                }
            } catch (e: AiProviderException) {
                lastException = e
                if (e.isRetryable && attempt < maxRetries - 1) {
                    val delayMs = (2000L * (1 shl attempt)).coerceAtMost(10000L)
                    emit(ChatResponse.RetryNotification(
                        attempt = attempt + 1,
                        maxAttempts = maxRetries,
                        delayMs = delayMs,
                        reason = e.message ?: "Provider error"
                    ))
                    delay(delayMs)
                } else if (!e.isRetryable) {
                    break
                }
            } catch (e: Exception) {
                lastException = e
                if (attempt < maxRetries - 1 && isRetryableError(e)) {
                    val delayMs = (2000L * (1 shl attempt)).coerceAtMost(10000L)
                    emit(ChatResponse.RetryNotification(
                        attempt = attempt + 1,
                        maxAttempts = maxRetries,
                        delayMs = delayMs,
                        reason = e.message ?: "Connection error"
                    ))
                    delay(delayMs)
                } else {
                    break
                }
            }
        }

        // All retries failed
        emit(ChatResponse.Error(
            lastException?.message ?: "Request failed after $maxRetries attempts",
            "max_retries_exceeded",
            isRetryable = false
        ))
    }.flowOn(Dispatchers.IO)

    private fun isRetryableError(e: Exception): Boolean {
        return e is java.net.SocketTimeoutException ||
                e is java.net.ConnectException ||
                e is java.net.UnknownHostException ||
                e.message?.contains("Connection reset", ignoreCase = true) == true ||
                e.message?.contains("timeout", ignoreCase = true) == true
    }

    /**
     * Fetch VQD token required for DuckDuckGo API
     */
    private suspend fun fetchVqdToken(): String? = withContext(Dispatchers.IO) {
        try {
            val url = URL(statusEndpoint)
            val connection = url.openConnection() as HttpsURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                // Apply standard headers
                connection.setRequestProperty("Accept", "*/*")
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9")
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36")
                connection.setRequestProperty("Referer", "https://duckduckgo.com/")
                connection.setRequestProperty("x-vqd-accept", "1")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Token is in the response header
                    connection.getHeaderField("x-vqd-4")
                } else {
                    null
                }
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun buildRequestPayload(messages: List<ChatMessage>, modelId: String): JSONObject {
        // Convert messages to DDG format
        val formattedMessages = JSONArray()

        // DDG expects specific format - combine system + user messages
        var systemPrompt = ""
        messages.forEach { message ->
            when (message.role) {
                MessageRole.SYSTEM -> {
                    systemPrompt = message.content
                }
                MessageRole.USER -> {
                    val content = if (systemPrompt.isNotEmpty() && formattedMessages.length() == 0) {
                        // Prepend system prompt to first user message
                        "[System Instructions: $systemPrompt]\n\n${message.content}"
                    } else {
                        message.content
                    }
                    formattedMessages.put(JSONObject().apply {
                        put("role", "user")
                        put("content", content)
                    })
                }
                MessageRole.ASSISTANT -> {
                    formattedMessages.put(JSONObject().apply {
                        put("role", "assistant")
                        put("content", message.content)
                    })
                }
                MessageRole.TOOL -> {
                    // Map tool responses as user messages
                    formattedMessages.put(JSONObject().apply {
                        put("role", "user")
                        put("content", "[Tool Result]\n${message.content}")
                    })
                }
            }
        }

        return JSONObject().apply {
            put("model", modelId)
            put("messages", formattedMessages)
        }
    }

    private fun applyHeaders(connection: HttpURLConnection, vqd: String) {
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "text/event-stream")
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9")
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36")
        connection.setRequestProperty("Origin", baseUrl)
        connection.setRequestProperty("Referer", "$baseUrl/")
        connection.setRequestProperty("x-vqd-4", vqd)
        connection.setRequestProperty("Sec-Fetch-Dest", "empty")
        connection.setRequestProperty("Sec-Fetch-Mode", "cors")
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin")
    }

    /**
     * Model configuration data class
     */
    private data class ModelConfig(
        val id: String,
        val name: String,
        val description: String,
        val maxTokens: Int = 8192,
        val supportsVision: Boolean = false,
        val supportsReasoning: Boolean = false
    )
}

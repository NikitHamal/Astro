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
import java.util.UUID
import javax.net.ssl.HttpsURLConnection

/**
 * Blackbox AI Provider
 *
 * Blackbox.ai provides free access to various AI models including GPT-4o, Claude,
 * and their own Blackbox model. This implementation mirrors gpt4free's Blackbox provider.
 *
 * Features:
 * - Multiple free AI models
 * - Streaming support
 * - No API key required (uses session-based auth)
 * - Image input support on some models
 *
 * Note: This provider uses Blackbox's web interface API.
 */
class BlackboxProvider : AiProvider {

    override val providerId: String = "blackbox"
    override val displayName: String = "Blackbox AI"
    override val baseUrl: String = "https://www.blackbox.ai"
    override val isWorking: Boolean = true
    override val supportsStreaming: Boolean = true
    override val supportsSystemMessage: Boolean = true
    override val supportsMessageHistory: Boolean = true

    override val defaultModel: String = "blackboxai"

    private val apiEndpoint = "https://www.blackbox.ai/api/chat"

    // Session token - refreshed periodically
    @Volatile
    private var validatedToken: String? = null

    // Cached models
    private var cachedModels: List<AiModel>? = null

    /**
     * Available models on Blackbox
     */
    private val modelConfigs = mapOf(
        "blackboxai" to ModelConfig(
            id = "blackboxai",
            name = "Blackbox AI",
            description = "Blackbox's default AI model - fast and capable",
            maxTokens = 16384
        ),
        "blackboxai-pro" to ModelConfig(
            id = "blackboxai-pro",
            name = "Blackbox AI Pro",
            description = "Enhanced Blackbox model with improved reasoning",
            maxTokens = 32768
        ),
        "gpt-4o" to ModelConfig(
            id = "gpt-4o",
            name = "GPT-4o",
            description = "OpenAI's GPT-4o via Blackbox",
            supportsVision = true,
            maxTokens = 128000
        ),
        "claude-sonnet-3.5" to ModelConfig(
            id = "claude-sonnet-3.5",
            name = "Claude 3.5 Sonnet",
            description = "Anthropic's Claude 3.5 Sonnet via Blackbox",
            maxTokens = 200000
        ),
        "gemini-pro" to ModelConfig(
            id = "gemini-pro",
            name = "Gemini Pro",
            description = "Google's Gemini Pro via Blackbox",
            supportsVision = true,
            maxTokens = 32000
        ),
        "llama-3.1-405b" to ModelConfig(
            id = "llama-3.1-405b",
            name = "Llama 3.1 405B",
            description = "Meta's largest Llama model via Blackbox",
            maxTokens = 128000
        ),
        "llama-3.1-70b" to ModelConfig(
            id = "llama-3.1-70b",
            name = "Llama 3.1 70B",
            description = "Meta's Llama 3.1 70B via Blackbox",
            maxTokens = 128000
        )
    )

    /**
     * Agent modes that can be activated
     */
    private val agentModes = mapOf(
        "coder" to "Code generation specialist",
        "analyst" to "Data analysis specialist",
        "designer" to "Design specialist"
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

        emit(ChatResponse.ProviderInfo(providerId, modelId))

        var lastException: Exception? = null
        val maxRetries = 3

        for (attempt in 0 until maxRetries) {
            try {
                // Ensure we have a valid token
                if (validatedToken == null) {
                    validatedToken = generateToken()
                }

                val url = URL(apiEndpoint)
                val connection = url.openConnection() as HttpsURLConnection

                try {
                    connection.requestMethod = "POST"
                    connection.doOutput = true
                    connection.connectTimeout = 30000
                    connection.readTimeout = 120000

                    applyHeaders(connection)

                    val payload = buildRequestPayload(messages, modelId, maxTokens)

                    connection.outputStream.use { os ->
                        os.write(payload.toString().toByteArray(Charsets.UTF_8))
                    }

                    val responseCode = connection.responseCode

                    if (responseCode == 429) {
                        throw RateLimitException("Rate limit exceeded")
                    }

                    if (responseCode == 401 || responseCode == 403) {
                        // Token expired, refresh and retry
                        validatedToken = generateToken()
                        throw AiProviderException("Session expired", "auth_error", isRetryable = true)
                    }

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        val errorBody = try {
                            connection.errorStream?.bufferedReader()?.readText() ?: "Unknown error"
                        } catch (e: Exception) {
                            "Unknown error"
                        }
                        throw AiProviderException("HTTP $responseCode: $errorBody", "http_error",
                            isRetryable = responseCode >= 500)
                    }

                    val reader = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8))
                    val contentBuilder = StringBuilder()

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val text = line ?: continue

                        // Blackbox sends plain text chunks, not SSE format
                        if (text.isNotEmpty() && !text.startsWith("$")) {
                            // Filter out internal markers
                            val cleanedText = cleanResponse(text)
                            if (cleanedText.isNotEmpty()) {
                                contentBuilder.append(cleanedText)
                                emit(ChatResponse.Content(cleanedText))
                            }
                        }
                    }

                    reader.close()

                    // Emit final completion
                    if (contentBuilder.isNotEmpty()) {
                        emit(ChatResponse.Content(contentBuilder.toString(), isComplete = true))
                    }
                    emit(ChatResponse.Done())
                    return@flow // Success

                } finally {
                    connection.disconnect()
                }

            } catch (e: RateLimitException) {
                lastException = e
                validatedToken = null // Force token refresh
                if (attempt < maxRetries - 1) {
                    val delayMs = (2000L * (1 shl attempt)).coerceAtMost(10000L)
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

    private fun generateToken(): String {
        // Generate a random validated token for Blackbox
        return UUID.randomUUID().toString()
    }

    private fun buildRequestPayload(
        messages: List<ChatMessage>,
        modelId: String,
        maxTokens: Int?
    ): JSONObject {
        // Convert messages to Blackbox format
        val formattedMessages = JSONArray()

        // Build conversation history
        messages.forEach { message ->
            formattedMessages.put(JSONObject().apply {
                put("role", when (message.role) {
                    MessageRole.SYSTEM -> "system"
                    MessageRole.USER -> "user"
                    MessageRole.ASSISTANT -> "assistant"
                    MessageRole.TOOL -> "user" // Map tool responses as user messages
                })
                put("content", message.content)
                put("id", UUID.randomUUID().toString())
            })
        }

        return JSONObject().apply {
            put("messages", formattedMessages)
            put("id", UUID.randomUUID().toString())
            put("previewToken", JSONObject.NULL)
            put("userId", JSONObject.NULL)
            put("codeModelMode", true)
            put("agentMode", JSONObject())
            put("trendingAgentMode", JSONObject())
            put("isMicMode", false)
            put("userSystemPrompt", JSONObject.NULL)
            put("maxTokens", maxTokens ?: 16384)
            put("playgroundTopP", JSONObject.NULL)
            put("playgroundTemperature", JSONObject.NULL)
            put("isChromeExt", false)
            put("githubToken", JSONObject.NULL)
            put("clickedAnswer2", false)
            put("clickedAnswer3", false)
            put("clickedForceWebSearch", false)
            put("visitFromDelta", false)
            put("mobileClient", false)
            put("userSelectedModel", modelId)
            validatedToken?.let { put("validated", it) }
        }
    }

    private fun applyHeaders(connection: HttpURLConnection) {
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "*/*")
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9")
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36")
        connection.setRequestProperty("Origin", baseUrl)
        connection.setRequestProperty("Referer", "$baseUrl/")
        connection.setRequestProperty("Sec-Fetch-Dest", "empty")
        connection.setRequestProperty("Sec-Fetch-Mode", "cors")
        connection.setRequestProperty("Sec-Fetch-Site", "same-origin")
    }

    /**
     * Clean response text by removing internal markers and artifacts
     */
    private fun cleanResponse(text: String): String {
        // Remove internal markers that Blackbox may include
        return text
            .replace(Regex("""\$@\$.*?\$@\$"""), "")
            .replace(Regex("""\[.*?\]\(.*?\)"""), "") // Remove markdown links if malformed
            .trim()
    }

    /**
     * Model configuration data class
     */
    private data class ModelConfig(
        val id: String,
        val name: String,
        val description: String,
        val maxTokens: Int = 16384,
        val supportsVision: Boolean = false,
        val supportsReasoning: Boolean = false
    )
}

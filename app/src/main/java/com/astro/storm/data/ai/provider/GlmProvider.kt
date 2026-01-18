package com.astro.storm.data.ai.provider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

/**
 * GLM (Zhipu AI / ChatZ) AI Provider
 * 
 * Features a complex HMAC-SHA256 signature mechanism for no-auth access.
 */
class GlmProvider : BaseOpenAiCompatibleProvider() {

    override val providerId: String = "glm"
    override val displayName: String = "GLM (ChatZ)"
    override val apiBase: String = "https://chat.z.ai/api"
    override val isWorking: Boolean = true
    
    override val defaultModel: String = "glm-4.7"

    private var authToken: String? = null
    private val salt = "junjie"

    override suspend fun fetchModels(): List<AiModel> = withContext(Dispatchers.IO) {
        try {
            // First, get the dynamic auth token
            val authUrl = URL("https://chat.z.ai/api/v1/auths/")
            val authConn = authUrl.openConnection() as HttpURLConnection
            authConn.requestMethod = "GET"
            authConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            authConn.setRequestProperty("Origin", "https://chat.z.ai")
            authConn.setRequestProperty("Referer", "https://chat.z.ai/")
            
            if (authConn.responseCode == HttpURLConnection.HTTP_OK) {
                val authResponse = authConn.inputStream.bufferedReader().readText()
                authToken = JSONObject(authResponse).optString("token")
            }

            if (authToken == null) return@withContext getDefaultModels()

            // Fetch actual models using the token
            val modelsUrl = URL("$apiBase/models")
            val conn = modelsUrl.openConnection() as HttpURLConnection
            conn.setRequestProperty("Authorization", "Bearer $authToken")
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().readText()
                val data = JSONObject(response).optJSONArray("data")
                val models = mutableListOf<AiModel>()
                if (data != null) {
                    for (i in 0 until data.length()) {
                        val m = data.getJSONObject(i)
                        val id = m.getString("id")
                        models.add(AiModel(
                            id = id,
                            name = id,
                            providerId = providerId,
                            displayName = id.uppercase(),
                            supportsReasoning = id.contains("thinking", true) || id.contains("research", true),
                            supportsVision = id.contains("v", true)
                        ))
                    }
                    return@withContext models
                }
            }
            getDefaultModels()
        } catch (e: Exception) {
            getDefaultModels()
        }
    }

    override fun getDefaultModels(): List<AiModel> = listOf(
        AiModel(id = "glm-4.7", name = "GLM 4.7", providerId = providerId, displayName = "GLM 4.7"),
        AiModel(id = "glm-4-flash", name = "GLM 4 Flash", providerId = providerId, displayName = "GLM 4 Flash"),
        AiModel(id = "GLM-4.1V-Thinking-FlashX", name = "GLM 4.1V Thinking", providerId = providerId, displayName = "GLM 4.1V Thinking", supportsReasoning = true, supportsVision = true),
        AiModel(id = "deep-research", name = "GLM Deep Research", providerId = providerId, displayName = "GLM Deep Research", supportsReasoning = true)
    )

    override fun chat(
        messages: List<ChatMessage>,
        model: String?,
        temperature: Float?,
        maxTokens: Int?,
        stream: Boolean
    ): Flow<ChatResponse> = flow {
        try {
            if (authToken == null) {
                fetchModels() // Refresh token
            }

            val resolvedModel = resolveModel(model)
            emit(ChatResponse.ProviderInfo(providerId, resolvedModel))

            val userPrompt = messages.findLast { it.role == MessageRole.USER }?.content ?: ""
            val timestamp = System.currentTimeMillis()
            
            // Generate signature
            val signature = generateSignature(authToken ?: "", userPrompt, timestamp)

            val payload = JSONObject().apply {
                put("chat_id", "local")
                put("id", UUID.randomUUID().toString())
                put("stream", stream)
                put("model", resolvedModel)
                put("messages", buildMessagesArray(messages))
                put("features", JSONObject().apply { put("enable_thinking", true) })
            }

            val url = URL("$apiBase/chat/completions?signature_timestamp=$timestamp")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $authToken")
            conn.setRequestProperty("x-signature", signature)
            conn.setRequestProperty("x-fe-version", "prod-fe-1.0.95")
            
            conn.outputStream.use { it.write(payload.toString().toByteArray()) }

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = conn.inputStream.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    if (line?.startsWith("data: ") == true) {
                        val data = line!!.substring(6)
                        if (data == "[DONE]") break
                        try {
                            val json = JSONObject(data)
                            val type = json.optString("type")
                            if (type == "chat:completion") {
                                val completionData = json.optJSONObject("data")
                                val phase = completionData?.optString("phase")
                                val content = completionData?.optString("delta_content") ?: ""
                                
                                if (phase == "thinking") {
                                    emit(ChatResponse.Reasoning(content))
                                } else {
                                    emit(ChatResponse.Content(content))
                                }
                            }
                        } catch (e: Exception) {}
                    }
                }
                emit(ChatResponse.Done())
            } else {
                emit(ChatResponse.Error("HTTP ${conn.responseCode}: ${conn.errorStream?.readBytes()?.decodeToString()}", "http_error"))
            }
        } catch (e: Exception) {
            emit(ChatResponse.Error(e.message ?: "Unknown error", "error"))
        }
    }.flowOn(Dispatchers.IO)

    private fun generateSignature(token: String, prompt: String, timestamp: Long): String {
        val timeWindow = timestamp / (5 * 60 * 1000)
        
        // The python logic uses requestId, user_id and timestamp in prepare_auth_params
        // and then signs them. We'll use a simplified but functional version.
        val dataString = "requestId,${UUID.randomUUID()},timestamp,$timestamp,user_id,null|$prompt|$timestamp"
        
        val baseMac = Mac.getInstance("HmacSHA256")
        baseMac.init(SecretKeySpec(salt.toByteArray(), "HmacSHA256"))
        val baseSignature = bytesToHex(baseMac.doFinal(timeWindow.toString().toByteArray()))
        
        val finalMac = Mac.getInstance("HmacSHA256")
        finalMac.init(SecretKeySpec(baseSignature.toByteArray(), "HmacSHA256"))
        return bytesToHex(finalMac.doFinal(dataString.toByteArray()))
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

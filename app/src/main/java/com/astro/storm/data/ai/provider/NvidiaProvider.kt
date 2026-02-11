package com.astro.storm.data.ai.provider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * NVIDIA NIM/OpenAI-compatible provider.
 *
 * API base:
 * - https://integrate.api.nvidia.com/v1
 *
 * Auth:
 * - Authorization: Bearer <NVIDIA_API_KEY>
 */
class NvidiaProvider(
    private val apiKeyStore: ProviderApiKeyStore
) : BaseOpenAiCompatibleProvider() {

    override val providerId: String = "nvidia"
    override val displayName: String = "NVIDIA"
    override val apiBase: String = "https://integrate.api.nvidia.com/v1"
    override val isWorking: Boolean = true
    override val requiresAuth: Boolean = true
    override val supportsStreaming: Boolean = true
    override val supportsSystemMessage: Boolean = true
    override val supportsMessageHistory: Boolean = true
    override val defaultModel: String = "meta/llama-3.1-70b-instruct"

    override fun getAuthHeaders(): Map<String, String> {
        val key = apiKeyStore.getApiKey(providerId)?.trim().orEmpty()
        return if (key.isNotEmpty()) {
            mapOf("Authorization" to "Bearer $key")
        } else {
            emptyMap()
        }
    }

    override suspend fun fetchModels(): List<AiModel> = withContext(Dispatchers.IO) {
        if (apiKeyStore.getApiKey(providerId).isNullOrBlank()) {
            return@withContext getDefaultModels()
        }

        // Prefer live model discovery from /v1/models when key is configured.
        return@withContext try {
            super.fetchModels()
        } catch (e: Exception) {
            getDefaultModels()
        }
    }

    override fun parseModelsResponse(response: String): List<AiModel> {
        val models = mutableListOf<AiModel>()
        try {
            val json = JSONObject(response)
            val data = json.optJSONArray("data") ?: json.optJSONArray("models") ?: JSONArray()
            for (i in 0 until data.length()) {
                val modelJson = data.optJSONObject(i) ?: continue
                val id = modelJson.optString("id", modelJson.optString("name", "")).trim()
                if (id.isEmpty()) continue

                val normalized = id.lowercase()
                val supportsReasoning =
                    normalized.contains("reason") ||
                    normalized.contains("r1") ||
                    normalized.contains("thinking") ||
                    normalized.contains("glm4.7")

                models.add(
                    AiModel(
                        id = id,
                        name = id,
                        providerId = providerId,
                        displayName = formatModelDisplayName(id),
                        description = "NVIDIA hosted model",
                        supportsVision = normalized.contains("vision"),
                        supportsReasoning = supportsReasoning,
                        supportsTools = true
                    )
                )
            }
        } catch (e: Exception) {
            // fall back
        }
        return models.ifEmpty { getDefaultModels() }
    }

    override fun getDefaultModels(): List<AiModel> = listOf(
        AiModel(
            id = "meta/llama-3.1-70b-instruct",
            name = "Llama 3.1 70B Instruct",
            providerId = providerId,
            displayName = "Llama 3.1 70B",
            description = "Strong general model on NVIDIA",
            supportsTools = true
        ),
        AiModel(
            id = "meta/llama-3.1-8b-instruct",
            name = "Llama 3.1 8B Instruct",
            providerId = providerId,
            displayName = "Llama 3.1 8B",
            description = "Faster lightweight model on NVIDIA",
            supportsTools = true
        ),
        AiModel(
            id = "openai/gpt-oss-20b",
            name = "GPT-OSS 20B",
            providerId = providerId,
            displayName = "GPT-OSS 20B",
            description = "Open model served by NVIDIA",
            supportsTools = true
        ),
        AiModel(
            id = "mistralai/mistral-small-24b-instruct",
            name = "Mistral Small 24B Instruct",
            providerId = providerId,
            displayName = "Mistral Small 24B",
            description = "Balanced quality-speed model",
            supportsTools = true
        ),
        AiModel(
            id = "qwen/qwen2-7b-instruct",
            name = "Qwen2 7B Instruct",
            providerId = providerId,
            displayName = "Qwen2 7B",
            description = "Efficient instruction model",
            supportsTools = true
        ),
        AiModel(
            id = "z-ai/glm-4.7",
            name = "GLM 4.7",
            providerId = providerId,
            displayName = "GLM 4.7",
            description = "Reasoning-capable model",
            supportsReasoning = true,
            supportsTools = true
        )
    )

    override fun formatModelDisplayName(modelId: String): String {
        return modelId
            .substringAfterLast("/")
            .replace("-instruct", "", ignoreCase = true)
            .replace("-", " ")
            .replace("_", " ")
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { token -> token.replaceFirstChar { it.uppercase() } }
    }
}


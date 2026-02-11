package com.astro.storm.data.ai.provider

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists user-supplied API keys per provider.
 *
 * Note: This currently uses SharedPreferences to stay consistent with the existing
 * settings infrastructure in this module.
 */
@Singleton
class ProviderApiKeyStore @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val PREFS_NAME = "astrostorm_ai_provider_auth"
        private const val KEY_PREFIX = "provider_api_key_"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun getApiKey(providerId: String): String? {
        return prefs.getString("$KEY_PREFIX$providerId", null)?.takeIf { it.isNotBlank() }
    }

    fun setApiKey(providerId: String, apiKey: String) {
        prefs.edit().putString("$KEY_PREFIX$providerId", apiKey).apply()
    }

    fun clearApiKey(providerId: String) {
        prefs.edit().remove("$KEY_PREFIX$providerId").apply()
    }
}


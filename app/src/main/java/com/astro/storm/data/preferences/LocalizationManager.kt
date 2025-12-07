package com.astro.storm.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Localization Manager for persisting and managing language preferences
 *
 * Supports:
 * - English (en)
 * - Nepali (ne)
 *
 * Language preference is persisted using SharedPreferences and applied
 * via Locale configuration for app-wide language switching.
 * Works with Android's resource localization system (values/strings.xml, values-ne/strings.xml).
 *
 * Production-grade implementation with proper Locale management and resource configuration.
 */
class LocalizationManager private constructor(context: Context) {

    private val appContext: Context = context.applicationContext
    private val prefs: SharedPreferences = appContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _currentLanguage = MutableStateFlow(getPersistedLanguage())
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    init {
        // Apply persisted language on initialization
        applyLanguage(getPersistedLanguage())
    }

    /**
     * Set the language and persist it
     * Also applies the language immediately to the app configuration
     */
    fun setLanguage(language: Language) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
        _currentLanguage.value = language
        applyLanguage(language)
    }

    /**
     * Get the current language preference
     */
    fun getLanguage(): Language = _currentLanguage.value

    /**
     * Get localized string resource
     * Supports parameterized strings with replacements (e.g., {dashaLord})
     */
    fun getString(resId: Int, vararg formatArgs: Any?): String {
        val baseString = appContext.resources.getString(resId)
        return if (formatArgs.isNotEmpty()) {
            baseString.format(*formatArgs)
        } else {
            baseString
        }
    }

    /**
     * Get localized string resource by name
     * This allows dynamic string retrieval by resource name
     */
    fun getStringByName(name: String, vararg args: Any?): String? {
        return try {
            val resId = appContext.resources.getIdentifier(
                name,
                "string",
                appContext.packageName
            )
            if (resId != 0) {
                getString(resId, *args)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get the persisted language from SharedPreferences
     */
    private fun getPersistedLanguage(): Language {
        val savedLanguageCode = prefs.getString(KEY_LANGUAGE, Language.ENGLISH.code)
        return Language.fromCode(savedLanguageCode ?: Language.ENGLISH.code)
    }

    /**
     * Apply the language configuration to the app
     * This sets the Locale and updates the app's resource configuration
     */
    private fun applyLanguage(language: Language) {
        val locale = language.getLocale()
        Locale.setDefault(locale)

        val config = Configuration(appContext.resources.configuration)
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        appContext.resources.updateConfiguration(config, appContext.resources.displayMetrics)
    }

    /**
     * Get the language display name for UI purposes
     */
    fun getLanguageDisplayName(language: Language): String {
        return when (language) {
            Language.ENGLISH -> appContext.getString(getStringResId("setting_language_english"))
            Language.NEPALI -> appContext.getString(getStringResId("setting_language_nepali"))
        }
    }

    /**
     * Helper to get string resource ID by name
     */
    private fun getStringResId(name: String): Int {
        return appContext.resources.getIdentifier(
            name,
            "string",
            appContext.packageName
        )
    }

    companion object {
        private const val PREFS_NAME = "astro_storm_localization_prefs"
        private const val KEY_LANGUAGE = "language_code"

        @Volatile
        private var INSTANCE: LocalizationManager? = null

        fun getInstance(context: Context): LocalizationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocalizationManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}

/**
 * Supported languages in the app
 *
 * Each language maps to a Locale and language code.
 * Nepali language uses ISO 639-1 code "ne" which corresponds to values-ne/ string resources.
 */
enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    NEPALI("ne", "नेपाली");

    /**
     * Get the Locale for this language
     */
    fun getLocale(): Locale {
        return when (this) {
            ENGLISH -> Locale.ENGLISH
            NEPALI -> Locale("ne")  // Nepali language code
        }
    }

    /**
     * Get the configuration locale for this language
     */
    fun getLocaleConfig(): Locale {
        return getLocale()
    }

    companion object {
        /**
         * Get Language enum from language code
         */
        fun fromCode(code: String): Language {
            return try {
                values().first { it.code == code }
            } catch (e: NoSuchElementException) {
                ENGLISH // Default to English
            }
        }

        /**
         * Get Language enum from Locale
         */
        fun fromLocale(locale: Locale?): Language {
            return if (locale == null) {
                ENGLISH
            } else {
                val code = locale.language
                fromCode(code)
            }
        }
    }
}

package com.astro.storm.util

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

object LocaleManager {

    private const val PREFS_NAME = "AstroStormPrefs"
    private const val LANGUAGE_KEY = "language_key"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setLocale(context: Context, language: Language) {
        persistLanguage(context, language)
        updateResources(context, language)
    }

    fun persistLanguage(context: Context, language: Language) {
        val editor = getPreferences(context).edit()
        editor.putString(LANGUAGE_KEY, language.code)
        editor.apply()
    }

    fun getLanguage(context: Context): Language {
        val code = getPreferences(context).getString(LANGUAGE_KEY, Language.ENGLISH.code) ?: Language.ENGLISH.code
        return Language.fromCode(code)
    }

    fun updateResources(context: Context, language: Language): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }
}
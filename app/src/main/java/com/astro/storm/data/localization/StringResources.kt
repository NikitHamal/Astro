package com.astro.storm.data.localization

/**
 * Centralized String Resources for AstroStorm
 *
 * This object contains all translatable strings organized by category.
 * Each string has an English and Nepali translation.
 *
 * Usage:
 * ```kotlin
 * val text = StringResources.get(StringKey.HOME_TAB, language)
 * ```
 *
 * For Compose:
 * ```kotlin
 * Text(text = stringResource(StringKey.HOME_TAB))
 * ```
 */
object StringResources {

    /**
     * Get localized string for a given key
     */
    fun get(key: StringKeyInterface, language: Language): String {
        return when (language) {
            Language.ENGLISH -> key.en
            Language.NEPALI -> key.ne
        }
    }

    /**
     * Get localized string with format arguments
     */
    fun get(key: StringKeyInterface, language: Language, vararg args: Any): String {
        val template = get(key, language)
        return try {
            String.format(template, *args)
        } catch (e: Exception) {
            template
        }
    }
}

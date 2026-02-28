package com.astro.vajra.core.common

/**
 * Centralized String Resources for AstroVajra
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
        var template = get(key, language)
        
        // If template contains {0}, {1}, etc, use direct replacement
        // This avoids String.format messing up literal % characters in args
        if (template.contains(Regex("\\{\\d+\\}"))) {
            args.forEachIndexed { index, arg ->
                template = template.replace("{$index}", arg.toString())
            }
            return template
        }
        
        return try {
            String.format(template, *args)
        } catch (e: Exception) {
            template
        }
    }
}

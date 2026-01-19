package com.astro.storm.core.common

/**
 * Represents a localized text template with optional formatting arguments.
 */
data class LocalizedText(
    val key: StringKeyInterface,
    val args: List<Any> = emptyList()
) {
    fun resolve(language: Language): String {
        if (args.isEmpty()) {
            return StringResources.get(key, language)
        }
        val resolvedArgs = args.map { arg ->
            when (arg) {
                is StringKeyInterface -> StringResources.get(arg, language)
                else -> arg
            }
        }
        return StringResources.get(key, language, *resolvedArgs.toTypedArray())
    }
}

/**
 * Represents a titled list of localized text insights.
 */
data class InsightSection(
    val titleKey: StringKeyInterface,
    val items: List<LocalizedText>
)

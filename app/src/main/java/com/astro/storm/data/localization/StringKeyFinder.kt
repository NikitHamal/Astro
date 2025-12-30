package com.astro.storm.data.localization


/**
 * Helper object to find string keys by English value across all enums
 */
object StringKeyFinder {
    /**
     * Find key by English value across all string key enums
     */
    fun findByEnglish(value: String): StringKeyInterface? {
        return StringKey.entries.find { it.en.equals(value, ignoreCase = true) }
            ?: StringKeyMatch.entries.find { it.en.equals(value, ignoreCase = true) }
            ?: StringKeyAnalysis.entries.find { it.en.equals(value, ignoreCase = true) }
            ?: StringKeyDosha.entries.find { it.en.equals(value, ignoreCase = true) }
    }
}

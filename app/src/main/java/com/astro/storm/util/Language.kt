package com.astro.storm.util

enum class Language(val code: String) {
    ENGLISH("en"),
    NEPALI("ne");

    companion object {
        fun fromCode(code: String): Language {
            return entries.firstOrNull { it.code == code } ?: ENGLISH
        }
    }
}
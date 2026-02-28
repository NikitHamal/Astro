package com.astro.vajra.data.templates

import android.content.Context
import com.astro.vajra.core.model.PredictionTemplate
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateManager @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val templateCache = mutableMapOf<String, List<PredictionTemplate>>()

    fun getTemplatesByCategory(category: String): List<PredictionTemplate> {
        return templateCache.getOrPut(category) {
            loadTemplatesFromAssets(category)
        }
    }

    private fun loadTemplatesFromAssets(category: String): List<PredictionTemplate> {
        return try {
            val fileName = "templates/${category}_templates.json"
            val inputStream = context.assets.open(fileName)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()
            json.decodeFromString<List<PredictionTemplate>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getAllTemplates(): List<PredictionTemplate> {
        val categories = listOf("nadi", "house_lord", "dasha", "transit", "yoga", "varga", "life_area")
        return categories.flatMap { getTemplatesByCategory(it) }
    }
}

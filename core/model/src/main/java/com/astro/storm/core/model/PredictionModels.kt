package com.astro.storm.core.model

import kotlinx.serialization.Serializable

/**
 * Prediction Template Model
 */
@Serializable
data class PredictionTemplate(
    val id: String,
    val category: String, // 'dasha', 'transit', 'yoga', 'nadi', 'house_lord', 'varga', 'life_area'
    val templateKey: String,
    val en: String,
    val ne: String,
    val conditions: TemplateCondition,
    val priority: Int = 0,
    val confidence: String = "medium" // 'high', 'medium', 'low'
)

/**
 * Conditions for a template to be selected
 */
@Serializable
data class TemplateCondition(
    val planet: String? = null,
    val sign: String? = null,
    val house: Int? = null,
    val degreeMin: Double? = null,
    val degreeMax: Double? = null,
    val dignity: String? = null, // 'exalted', 'debilitated', 'own_sign', etc.
    val minShadbala: Double? = null,
    val ascendant: String? = null, // Used for Nadi or general ascendant-based effects
    val varga: String? = null, // 'D1', 'D9', 'D10', etc.
    val dashaLevel: Int? = null, // 1 for Mahadasha, 2 for Antardasha, etc.
    val yogaName: String? = null,
    val lifeArea: String? = null,
    val transitPlanet: String? = null,
    val natalPlanet: String? = null,
    val aspectType: String? = null
)

/**
 * Localized text wrapper
 */
@Serializable
data class LocalizedText(
    val en: String,
    val ne: String
)

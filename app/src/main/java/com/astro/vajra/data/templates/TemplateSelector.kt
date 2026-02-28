package com.astro.vajra.data.templates

import com.astro.vajra.core.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateSelector @Inject constructor(
    private val templateManager: TemplateManager
) {
    fun selectTemplates(
        category: String,
        planet: Planet? = null,
        sign: ZodiacSign? = null,
        house: Int? = null,
        degree: Double? = null,
        dignity: PlanetaryDignityLevel? = null,
        minShadbala: Double? = null,
        ascendant: ZodiacSign? = null,
        varga: String? = null,
        dashaLevel: Int? = null,
        yogaName: String? = null,
        lifeArea: LifeArea? = null,
        transitPlanet: Planet? = null,
        natalPlanet: Planet? = null,
        aspectType: String? = null
    ): List<PredictionTemplate> {
        val allTemplates = templateManager.getTemplatesByCategory(category)

        return allTemplates.filter { template ->
            val cond = template.conditions

            // Match planet
            if (cond.planet != null && cond.planet != planet?.name) return@filter false

            // Match sign
            if (cond.sign != null && cond.sign != sign?.name) return@filter false

            // Match house
            if (cond.house != null && cond.house != house) return@filter false

            // Match degree range
            if (degree != null) {
                val dMin = cond.degreeMin
                val dMax = cond.degreeMax
                if (dMin != null && degree < dMin) return@filter false
                if (dMax != null && degree > dMax) return@filter false
            }

            // Match dignity
            if (cond.dignity != null && cond.dignity != dignity?.name) return@filter false

            // Match Shadbala
            val mShadbala = cond.minShadbala
            if (mShadbala != null && minShadbala != null && minShadbala < mShadbala) return@filter false

            // Match Ascendant
            if (cond.ascendant != null && cond.ascendant != ascendant?.name) return@filter false

            // Match Varga
            if (cond.varga != null && cond.varga != varga) return@filter false

            // Match Dasha Level
            if (cond.dashaLevel != null && cond.dashaLevel != dashaLevel) return@filter false

            // Match Yoga
            if (cond.yogaName != null && cond.yogaName != yogaName) return@filter false

            // Match Life Area
            if (cond.lifeArea != null && cond.lifeArea != lifeArea?.name) return@filter false

            // Match Transit
            if (cond.transitPlanet != null && cond.transitPlanet != transitPlanet?.name) return@filter false
            if (cond.natalPlanet != null && cond.natalPlanet != natalPlanet?.name) return@filter false
            if (cond.aspectType != null && cond.aspectType != aspectType) return@filter false

            true
        }.sortedByDescending { it.priority }
    }

    fun findBestTemplate(
        category: String,
        planet: Planet? = null,
        sign: ZodiacSign? = null,
        house: Int? = null,
        degree: Double? = null,
        dignity: PlanetaryDignityLevel? = null,
        minShadbala: Double? = null,
        ascendant: ZodiacSign? = null,
        varga: String? = null,
        dashaLevel: Int? = null,
        yogaName: String? = null,
        lifeArea: LifeArea? = null,
        transitPlanet: Planet? = null,
        natalPlanet: Planet? = null,
        aspectType: String? = null
    ): PredictionTemplate? {
        return selectTemplates(
            category, planet, sign, house, degree, dignity, minShadbala,
            ascendant, varga, dashaLevel, yogaName, lifeArea,
            transitPlanet, natalPlanet, aspectType
        ).firstOrNull()
    }
}

package com.astro.vajra.data.templates

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.model.LifeArea
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.ephemeris.yoga.Yoga
import com.astro.vajra.ephemeris.yoga.YogaCategory

/**
 * Shared helpers for resolving localized text from asset-backed prediction templates.
 */
object TemplateTextResolver {

    fun resolveTransitText(
        templateSelector: TemplateSelector,
        transitPlanet: Planet,
        sign: ZodiacSign,
        house: Int,
        language: Language
    ): String? {
        return localized(
            templateSelector.findBestTemplate(
                category = "transit",
                transitPlanet = transitPlanet,
                sign = sign,
                house = house
            ),
            language
        )
    }

    fun resolveDashaText(
        templateSelector: TemplateSelector,
        chart: VedicChart,
        planet: Planet,
        dashaLevel: Int,
        language: Language
    ): String? {
        val position = chart.planetPositions.find { it.planet == planet } ?: return null
        return localized(
            templateSelector.findBestTemplate(
                category = "dasha",
                planet = planet,
                sign = position.sign,
                house = position.house,
                dashaLevel = dashaLevel
            ),
            language
        )
    }

    fun resolveNadiText(
        templateSelector: TemplateSelector,
        chart: VedicChart,
        language: Language
    ): String? {
        return localized(
            templateSelector.findBestTemplate(
                category = "nadi",
                ascendant = ZodiacSign.fromLongitude(chart.ascendant),
                degree = chart.ascendant % 30.0
            ),
            language
        )
    }

    fun resolveLifeAreaText(
        templateSelector: TemplateSelector,
        lifeArea: LifeArea,
        planet: Planet,
        sign: ZodiacSign,
        house: Int,
        language: Language
    ): String? {
        return localized(
            templateSelector.findBestTemplate(
                category = "life_area",
                lifeArea = lifeArea,
                planet = planet,
                sign = sign,
                house = house
            ),
            language
        )
    }

    fun resolveYogaOrHouseLordText(
        templateSelector: TemplateSelector,
        chart: VedicChart,
        yoga: Yoga,
        language: Language
    ): String? {
        val house = yoga.houses.firstOrNull()
        val yogaTemplate = resolveYogaText(templateSelector, yoga, house, language)
        if (!yogaTemplate.isNullOrBlank()) return yogaTemplate

        if (yoga.category == YogaCategory.BHAVA_YOGA && house != null) {
            val ascendant = ZodiacSign.fromLongitude(chart.ascendant)
            return localized(
                templateSelector.findBestTemplate(
                    category = "house_lord",
                    ascendant = ascendant,
                    house = house
                ),
                language
            )
        }

        return null
    }

    fun resolveVargaText(
        templateSelector: TemplateSelector,
        varga: String,
        planet: Planet,
        sign: ZodiacSign,
        language: Language
    ): String? {
        return localized(
            templateSelector.findBestTemplate(
                category = "varga",
                varga = varga,
                planet = planet,
                sign = sign
            ),
            language
        )
    }

    private fun resolveYogaText(
        templateSelector: TemplateSelector,
        yoga: Yoga,
        house: Int?,
        language: Language
    ): String? {
        val candidates = yogaNameCandidates(yoga.name)
        for (candidate in candidates) {
            val match = templateSelector.findBestTemplate(
                category = "yoga",
                yogaName = candidate,
                house = house
            )
            val text = localized(match, language)
            if (!text.isNullOrBlank()) return text
        }
        return null
    }

    private fun yogaNameCandidates(rawName: String): List<String> {
        val source = rawName.trim()
        if (source.isEmpty()) return emptyList()

        val stripped = source
            .replace("Yoga", "", ignoreCase = true)
            .replace("Yog", "", ignoreCase = true)
            .replace("Raja", "", ignoreCase = true)
            .trim()

        val normalized = normalizeToken(stripped)

        // These names match the known keys in assets/templates/yoga_templates.json
        val canonical = listOf(
            "Adhi",
            "Amala",
            "Anapha",
            "Bhadra",
            "Budha-Aditya",
            "Dhurudhara",
            "Gajakesari",
            "Hamsa",
            "Kemadruma",
            "Lakshmi",
            "Mahabhagya",
            "Malavya",
            "Neecha-Bhanga",
            "Parivartana",
            "Pushkala",
            "Ruchaka",
            "Sasha",
            "Sunapha",
            "Vasumathi",
            "Vipareeta-Raja"
        )

        val exact = canonical.filter { normalizeToken(it) == normalized }
        if (exact.isNotEmpty()) return exact

        val contains = canonical.filter {
            val key = normalizeToken(it)
            normalized.contains(key) || key.contains(normalized)
        }

        return if (contains.isNotEmpty()) {
            contains
        } else {
            listOf(stripped)
        }
    }

    private fun normalizeToken(value: String): String {
        return value.lowercase().replace(Regex("[^a-z0-9]"), "")
    }

    private fun localized(template: com.astro.vajra.core.model.PredictionTemplate?, language: Language): String? {
        template ?: return null
        return when (language) {
            Language.ENGLISH -> template.en
            Language.NEPALI -> template.ne
        }
    }
}


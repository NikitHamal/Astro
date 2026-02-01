package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.remedy.RemedyConstants.nakshatraDeities
import com.astro.storm.ephemeris.remedy.RemedyConstants.planetaryCharity
import com.astro.storm.ephemeris.remedy.RemedyConstants.planetaryGemstones
import com.astro.storm.ephemeris.remedy.RemedyConstants.planetaryMantras

object RemedyGenerator {

    fun getGemstoneRemedy(analysis: PlanetaryAnalysis, language: Language): Remedy? {
        val planet = analysis.planet
        val gemInfo = planetaryGemstones[planet] ?: return null
        val pName = planet.name
        val nameKey = StringKeyRemedy.valueOf("GEM_${pName}_NAME")
        val localizedGemName = StringResources.get(nameKey, language)
        val shouldRecommend = analysis.isFunctionalBenefic || analysis.isYogakaraka

        if (!shouldRecommend) {
            val titleSuffix = StringResources.get(StringKeyRemedy.GEM_CAUTION_TITLE_SUFFIX, language)
            val desc = StringResources.get(StringKeyRemedy.GEM_CAUTION_DESC, language, planet.getLocalizedName(language), localizedGemName)
            val method = StringResources.get(StringKeyRemedy.GEM_CAUTION_METHOD, language, gemInfo.minCarat, gemInfo.maxCarat, localizedGemName, gemInfo.metal, gemInfo.fingerName)
            return Remedy(
                category = RemedyCategory.GEMSTONE, title = "$localizedGemName$titleSuffix", description = desc, method = method,
                timing = StringResources.get(StringKeyRemedy.valueOf("GEM_${pName}_TIMING"), language),
                duration = StringResources.get(StringKeyRemedy.GEM_CAUTION_DURATION, language),
                planet = planet, priority = RemedyPriority.OPTIONAL,
                benefits = listOf(StringResources.get(StringKeyRemedy.GEM_BENEFIT_STRENGTHEN, language, planet.getLocalizedName(language)), StringResources.get(StringKeyRemedy.GEM_CAUTION_TRIAL, language)),
                cautions = listOf(StringResources.get(StringKeyRemedy.GEM_CAUTION_NATURAL, language), StringResources.get(StringKeyRemedy.GEM_CAUTION_CERT, language), StringResources.get(StringKeyRemedy.GEM_CAUTION_REMOVE, language)),
                alternativeGemstone = gemInfo.alternativeName
            )
        }

        val descKey = StringKeyRemedy.valueOf("GEM_${pName}_DESC")
        val methodKey = StringKeyRemedy.valueOf("GEM_${pName}_METHOD")
        val timingKey = StringKeyRemedy.valueOf("GEM_${pName}_TIMING")
        val altKey = try { StringKeyRemedy.valueOf("GEM_${pName}_ALT") } catch (e: Exception) { null }
        val priority = when {
            analysis.isYogakaraka -> RemedyPriority.ESSENTIAL
            analysis.strengthScore < 30 -> RemedyPriority.HIGHLY_RECOMMENDED
            else -> RemedyPriority.RECOMMENDED
        }
        return Remedy(
            category = RemedyCategory.GEMSTONE, title = localizedGemName, description = StringResources.get(descKey, language),
            method = StringResources.get(methodKey, language), timing = StringResources.get(timingKey, language),
            duration = StringResources.get(StringKeyRemedy.GEM_DURATION_CONTINUOUS, language), planet = planet, priority = priority,
            benefits = listOf(StringResources.get(StringKeyRemedy.GEM_BENEFIT_STRENGTHEN, language, planet.getLocalizedName(language)), StringResources.get(StringKeyRemedy.GEM_BENEFIT_BALANCE, language)),
            cautions = listOf(StringResources.get(StringKeyRemedy.GEM_CAUTION_NATURAL, language), StringResources.get(StringKeyRemedy.GEM_CAUTION_CERT, language)),
            alternativeGemstone = altKey?.let { StringResources.get(it, language) } ?: gemInfo.alternativeName
        )
    }

    fun getMantraRemedy(planet: Planet, language: Language): Remedy? {
        val mantraInfo = planetaryMantras[planet] ?: return null
        val dirKeyName = "DIR_" + mantraInfo.direction.uppercase().replace("-", "_")
        val dirKey = try { StringKeyRemedy.valueOf(dirKeyName) } catch(e: Exception) { StringKeyGeneralPart3.DIR_EAST }
        val localizedDirection = StringResources.get(dirKey, language)
        val method = StringResources.get(StringKeyRemedy.MANTRA_METHOD, language, localizedDirection, mantraInfo.minimumCount, mantraInfo.beejMantra, planet.getLocalizedName(language))
        return Remedy(
            category = RemedyCategory.MANTRA, title = "${planet.getLocalizedName(language)}${StringResources.get(StringKeyRemedy.MANTRA_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyRemedy.MANTRA_DESC, language, planet.getLocalizedName(language)),
            method = method, timing = getLocalizedMantraTiming(planet, language),
            duration = StringResources.get(StringKeyRemedy.MANTRA_DURATION, language, mantraInfo.minimumCount),
            planet = planet, priority = RemedyPriority.ESSENTIAL,
            benefits = listOf(StringResources.get(StringKeyRemedy.MANTRA_BENEFIT_SAFE, language), StringResources.get(StringKeyRemedy.MANTRA_BENEFIT_INVOKE, language, planet.getLocalizedName(language)), StringResources.get(StringKeyRemedy.MANTRA_BENEFIT_OBSTACLES, language), StringResources.get(StringKeyRemedy.MANTRA_BENEFIT_VIBES, language)),
            cautions = listOf(StringResources.get(StringKeyRemedy.MANTRA_CAUTION_PURITY, language), StringResources.get(StringKeyRemedy.MANTRA_CAUTION_MALA, language), StringResources.get(StringKeyRemedy.MANTRA_CAUTION_DIET, language), StringResources.get(StringKeyRemedy.MANTRA_CAUTION_VOW, language)),
            mantraText = mantraInfo.beejMantra, mantraSanskrit = mantraInfo.beejMantraSanskrit, mantraCount = mantraInfo.minimumCount
        )
    }

    fun getCharityRemedy(planet: Planet, language: Language): Remedy? {
        val charityInfo = planetaryCharity[planet] ?: return null
        val pName = planet.name
        val items = StringResources.get(StringKeyRemedy.valueOf("CHARITY_${pName}_ITEMS"), language)
        val recipients = StringResources.get(StringKeyRemedy.valueOf("CHARITY_${pName}_RECIPIENTS"), language)
        val special = StringResources.get(StringKeyRemedy.valueOf("CHARITY_${pName}_SPECIAL"), language)
        val dayString = charityInfo.day.uppercase().replace(" ", "_")
        val weekdayKey = try {
            StringKeyPanchanga.valueOf("WEEKDAY_$dayString")
        } catch (e: Exception) {
            try {
                // Handle cases like "TUESDAY_OR_SATURDAY" by taking the first day
                val firstDay = dayString.split("_OR_").firstOrNull() ?: "SUNDAY"
                StringKeyPanchanga.valueOf("WEEKDAY_$firstDay")
            } catch (e2: Exception) {
                StringKeyPanchanga.WEEKDAY_SUNDAY
            }
        }
        val timing = StringResources.get(StringKeyRemedy.CHARITY_TIMING, language, StringResources.get(weekdayKey, language), getLocalizedCharityTiming(charityInfo.timing, language))
        return Remedy(
            category = RemedyCategory.CHARITY, title = "${planet.getLocalizedName(language)}${StringResources.get(StringKeyRemedy.CHARITY_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyRemedy.CHARITY_DESC, language, planet.getLocalizedName(language)),
            method = StringResources.get(StringKeyRemedy.CHARITY_METHOD, language, items, recipients, special),
            timing = timing, duration = StringResources.get(StringKeyRemedy.CHARITY_DURATION, language, planet.getLocalizedName(language)),
            planet = planet, priority = RemedyPriority.HIGHLY_RECOMMENDED,
            benefits = listOf(StringResources.get(StringKeyRemedy.CHARITY_BENEFIT_KARMA, language, planet.getLocalizedName(language)), StringResources.get(StringKeyRemedy.CHARITY_BENEFIT_MERIT, language), StringResources.get(StringKeyRemedy.CHARITY_BENEFIT_UNIVERSAL, language), StringResources.get(StringKeyRemedy.CHARITY_BENEFIT_BOTH, language)),
            cautions = listOf(StringResources.get(StringKeyRemedy.CHARITY_CAUTION_INTENTION, language), StringResources.get(StringKeyRemedy.CHARITY_CAUTION_EXPECTATION, language), StringResources.get(StringKeyRemedy.CHARITY_CAUTION_RECIPIENT, language), StringResources.get(StringKeyRemedy.CHARITY_CAUTION_QUALITY, language))
        )
    }

    fun getFastingRemedy(planet: Planet, language: Language): Remedy? {
        val weekdayKey = StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}")
        val localizedDay = StringResources.get(weekdayKey, language)
        val foodRec = StringResources.get(StringKeyRemedy.valueOf("FASTING_${planet.name}_FOOD"), language)
        return Remedy(
            category = RemedyCategory.FASTING, title = "$localizedDay${StringResources.get(StringKeyRemedy.FASTING_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyRemedy.FASTING_DESC, language, planet.getLocalizedName(language)),
            method = StringResources.get(StringKeyRemedy.FASTING_METHOD, language, localizedDay, foodRec),
            timing = StringResources.get(StringKeyRemedy.FASTING_TIMING, language, localizedDay, localizedDay),
            duration = StringResources.get(StringKeyRemedy.FASTING_DURATION, language), planet = planet, priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(StringResources.get(StringKeyRemedy.FASTING_BENEFIT_PURIFY, language), StringResources.get(StringKeyRemedy.FASTING_BENEFIT_WILL, language), StringResources.get(StringKeyRemedy.FASTING_BENEFIT_PLEASE, language, planet.getLocalizedName(language))),
            cautions = listOf(StringResources.get(StringKeyRemedy.FASTING_CAUTION_HEALTH, language), StringResources.get(StringKeyRemedy.FASTING_CAUTION_PREG, language), StringResources.get(StringKeyRemedy.FASTING_CAUTION_BREAK, language), StringResources.get(StringKeyRemedy.FASTING_CAUTION_HYDRATE, language))
        )
    }

    fun getColorRemedy(planet: Planet, language: Language): Remedy? {
        val colors = StringResources.get(StringKeyRemedy.valueOf("COLOR_${planet.name}_USE"), language)
        val avoid = StringResources.get(StringKeyRemedy.valueOf("COLOR_${planet.name}_AVOID"), language)
        val localizedDay = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}"), language)
        return Remedy(
            category = RemedyCategory.COLOR, title = "${planet.getLocalizedName(language)}${StringResources.get(StringKeyUIPart1.COLOR_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyUIPart1.COLOR_DESC, language, planet.getLocalizedName(language)),
            method = StringResources.get(StringKeyUIPart1.COLOR_METHOD, language, colors, avoid, localizedDay),
            timing = StringResources.get(StringKeyUIPart1.COLOR_TIMING, language, localizedDay),
            duration = StringResources.get(StringKeyUIPart1.COLOR_DURATION, language, planet.getLocalizedName(language)),
            planet = planet, priority = RemedyPriority.OPTIONAL,
            benefits = listOf(StringResources.get(StringKeyUIPart1.COLOR_BENEFIT_SUBTLE, language), StringResources.get(StringKeyUIPart1.COLOR_BENEFIT_EASY, language), StringResources.get(StringKeyUIPart1.COLOR_BENEFIT_COST, language)),
            cautions = listOf(StringResources.get(StringKeyUIPart1.COLOR_CAUTION_BALANCE, language))
        )
    }

    fun getLifestyleRemedy(planet: Planet, language: Language): Remedy? {
        val pName = planet.name
        val practicesKey = try { StringKeyRemedy.valueOf("LIFESTYLE_${pName}_PRACTICES") } catch (e: Exception) { return null }
        val avoidKey = try { StringKeyRemedy.valueOf("LIFESTYLE_${pName}_AVOID") } catch (e: Exception) { return null }
        val practices = StringResources.get(practicesKey, language).split("|")
        val avoid = StringResources.get(avoidKey, language).split("|")
        val localizedDay = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}"), language)
        return Remedy(
            category = RemedyCategory.LIFESTYLE, title = "${planet.getLocalizedName(language)}${StringResources.get(StringKeyGeneralPart6.LIFESTYLE_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyGeneralPart6.LIFESTYLE_DESC, language, planet.getLocalizedName(language)),
            method = buildString {
                appendLine(StringResources.get(StringKeyGeneralPart6.LIFESTYLE_REC_PRACTICES, language))
                practices.forEachIndexed { index, practice -> appendLine("${index + 1}. $practice") }
                appendLine()
                appendLine(StringResources.get(StringKeyGeneralPart6.LIFESTYLE_THINGS_AVOID, language))
                avoid.forEach { item -> appendLine("• $item") }
            },
            timing = StringResources.get(StringKeyGeneralPart6.LIFESTYLE_TIMING, language, localizedDay),
            duration = StringResources.get(StringKeyGeneralPart6.LIFESTYLE_DURATION, language), planet = planet, priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(StringResources.get(StringKeyGeneralPart6.LIFESTYLE_BENEFIT_HOLISTIC, language), StringResources.get(StringKeyGeneralPart6.LIFESTYLE_BENEFIT_COST, language), StringResources.get(StringKeyGeneralPart6.LIFESTYLE_BENEFIT_KARMA, language), StringResources.get(StringKeyGeneralPart6.LIFESTYLE_BENEFIT_ALIGN, language)),
            cautions = emptyList()
        )
    }

    fun getRudrakshaRemedy(planet: Planet, language: Language): Remedy? {
        val pName = planet.name
        val (mukhi, deityKey) = when (planet) {
            Planet.SUN -> 12 to StringKeyRemedy.DEITY_VISHNU
            Planet.MOON -> 2 to StringKeyRemedy.DEITY_SOMA
            Planet.MARS -> 3 to StringKeyRemedy.DEITY_AGNI
            Planet.MERCURY -> 4 to StringKeyRemedy.DEITY_BRAHMA
            Planet.JUPITER -> 5 to StringKeyRemedy.DEITY_RUDRA
            Planet.VENUS -> 6 to StringKeyRemedy.DEITY_ARYAMAN
            Planet.SATURN -> 7 to StringKeyRemedy.DEITY_SAVITAR
            Planet.RAHU -> 8 to StringKeyRemedy.DEITY_VASUS
            Planet.KETU -> 9 to StringKeyRemedy.DEITY_VARUNA
            else -> return null
        }
        val mukhiString = "$mukhi Mukhi"
        val deity = StringResources.get(deityKey, language)
        val benefitsKey = try { StringKeyRemedy.valueOf("RUDRA_${pName}_BENEFITS") } catch (e: Exception) { return null }
        val specificBenefits = StringResources.get(benefitsKey, language).split("|")
        val localizedDay = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}"), language)
        return Remedy(
            category = RemedyCategory.RUDRAKSHA, title = "$mukhiString ${StringResources.get(StringKeyGeneralPart9.RUDRA_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyGeneralPart9.RUDRA_DESC, language, mukhi, planet.getLocalizedName(language), deity),
            method = buildString {
                appendLine(StringResources.get(StringKeyGeneralPart9.RUDRA_METHOD_1, language, mukhiString))
                appendLine(StringResources.get(StringKeyGeneralPart9.RUDRA_METHOD_2, language))
                appendLine(StringResources.get(StringKeyGeneralPart9.RUDRA_METHOD_3, language))
                appendLine(StringResources.get(StringKeyGeneralPart9.RUDRA_METHOD_4, language, planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart9.RUDRA_METHOD_5, language))
                appendLine(StringResources.get(StringKeyGeneralPart9.RUDRA_METHOD_6, language))
            },
            timing = StringResources.get(StringKeyGeneralPart9.RUDRA_TIMING, language, localizedDay, planet.getLocalizedName(language)),
            duration = StringResources.get(StringKeyGeneralPart9.RUDRA_DURATION, language), planet = planet, priority = RemedyPriority.RECOMMENDED,
            benefits = specificBenefits + listOf(StringResources.get(StringKeyGeneralPart9.RUDRA_BENEFIT_NATURAL, language), StringResources.get(StringKeyGeneralPart9.RUDRA_BENEFIT_BALANCE, language, planet.getLocalizedName(language)), StringResources.get(StringKeyGeneralPart9.RUDRA_BENEFIT_PROTECT, language), StringResources.get(StringKeyGeneralPart9.RUDRA_BENEFIT_ANYONE, language)),
            cautions = listOf(StringResources.get(StringKeyGeneralPart9.RUDRA_CAUTION_AUTH, language), StringResources.get(StringKeyGeneralPart9.RUDRA_CAUTION_SLEEP, language), StringResources.get(StringKeyGeneralPart9.RUDRA_CAUTION_CLEAN, language), StringResources.get(StringKeyGeneralPart9.RUDRA_CAUTION_EVENTS, language))
        )
    }

    fun getYantraRemedy(planet: Planet, language: Language): Remedy? {
        val pName = planet.name
        val descKey = try { StringKeyRemedy.valueOf("YANTRA_${pName}_DESC") } catch (e: Exception) { return null }
        val (yantraName, material) = when (planet) {
            Planet.SUN -> "Surya Yantra" to "Copper or Gold"
            Planet.MOON -> "Chandra Yantra" to "Silver"
            Planet.MARS -> "Mangal Yantra" to "Copper"
            Planet.MERCURY -> "Budh Yantra" to "Bronze or Copper"
            Planet.JUPITER -> "Brihaspati Yantra / Guru Yantra" to "Gold or Brass"
            Planet.VENUS -> "Shukra Yantra" to "Silver or Copper"
            Planet.SATURN -> "Shani Yantra" to "Iron or Steel (Panch Dhatu)"
            Planet.RAHU -> "Rahu Yantra" to "Lead or Ashtadhatu"
            Planet.KETU -> "Ketu Yantra" to "Ashtadhatu or Copper"
            else -> return null
        }
        val localizedDay = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}"), language)
        return Remedy(
            category = RemedyCategory.YANTRA, title = yantraName, description = StringResources.get(descKey, language),
            method = buildString {
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_INSTALL_PROC, language))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_1, language, material, yantraName))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_2, language, localizedDay, planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_3, language))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_4, language, planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_5, language))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_6, language, planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_7, language))
                appendLine(StringResources.get(StringKeyGeneralPart12.YANTRA_METHOD_8, language))
            },
            timing = StringResources.get(StringKeyGeneralPart12.YANTRA_TIMING, language, localizedDay, planet.getLocalizedName(language)),
            duration = StringResources.get(StringKeyGeneralPart12.YANTRA_DURATION, language), planet = planet, priority = RemedyPriority.OPTIONAL,
            benefits = listOf(StringResources.get(StringKeyGeneralPart12.YANTRA_BENEFIT_FIELD, language), StringResources.get(StringKeyGeneralPart12.YANTRA_BENEFIT_247, language), StringResources.get(StringKeyGeneralPart12.YANTRA_BENEFIT_MEDITATION, language), StringResources.get(StringKeyGeneralPart12.YANTRA_BENEFIT_PROTECT, language)),
            cautions = listOf(StringResources.get(StringKeyGeneralPart12.YANTRA_CAUTION_WORSHIP, language), StringResources.get(StringKeyGeneralPart12.YANTRA_CAUTION_RITUALS, language), StringResources.get(StringKeyGeneralPart12.YANTRA_CAUTION_CLEAN, language), StringResources.get(StringKeyGeneralPart12.YANTRA_CAUTION_ALT, language))
        )
    }

    fun getDeityRemedy(planet: Planet, language: Language): Remedy? {
        val pName = planet.name
        val primaryDeity = StringResources.get(StringKeyRemedy.valueOf("DEITY_${pName}_PRIM"), language)
        val secondaryDeities = StringResources.get(StringKeyRemedy.valueOf("DEITY_${pName}_SEC"), language)
        val temple = StringResources.get(StringKeyRemedy.valueOf("DEITY_${pName}_TEMPLES"), language)
        val offerings = StringResources.get(StringKeyRemedy.valueOf("DEITY_${pName}_OFFERINGS"), language)
        val localizedDay = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}"), language)
        return Remedy(
            category = RemedyCategory.DEITY, title = "${planet.getLocalizedName(language)}${StringResources.get(StringKeyRemedy.DEITY_TITLE_SUFFIX, language)}",
            description = StringResources.get(StringKeyRemedy.DEITY_DESC, language, planet.getLocalizedName(language)),
            method = buildString {
                appendLine(StringResources.get(StringKeyRemedy.DEITY_PRIMARY, language, primaryDeity))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.DEITY_SECONDARY, language, secondaryDeities))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.DEITY_TEMPLES, language, temple))
                appendLine()
                appendLine(StringResources.get(StringKeyRemedy.DEITY_PROCEDURE, language))
                appendLine(StringResources.get(StringKeyRemedy.DEITY_METHOD_1, language, localizedDay))
                appendLine(StringResources.get(StringKeyRemedy.DEITY_METHOD_2, language, offerings))
                appendLine(StringResources.get(StringKeyRemedy.DEITY_METHOD_3, language, planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyRemedy.DEITY_METHOD_4, language))
                appendLine(StringResources.get(StringKeyRemedy.DEITY_METHOD_5, language))
                appendLine(StringResources.get(StringKeyRemedy.DEITY_METHOD_6, language))
            },
            timing = StringResources.get(StringKeyRemedy.DEITY_TIMING, language, localizedDay, planet.getLocalizedName(language)),
            duration = StringResources.get(StringKeyRemedy.DEITY_DURATION, language, planet.getLocalizedName(language)), planet = planet, priority = RemedyPriority.HIGHLY_RECOMMENDED,
            benefits = listOf(StringResources.get(StringKeyRemedy.DEITY_BENEFIT_GRACE, language), StringResources.get(StringKeyRemedy.DEITY_BENEFIT_RELIEF, language), StringResources.get(StringKeyRemedy.DEITY_BENEFIT_GROWTH, language), StringResources.get(StringKeyRemedy.DEITY_BENEFIT_CONNECT, language)),
            cautions = listOf(StringResources.get(StringKeyRemedy.DEITY_CAUTION_DEVOTION, language), StringResources.get(StringKeyRemedy.DEITY_CAUTION_ETIQUETTE, language), StringResources.get(StringKeyRemedy.DEITY_CAUTION_HOME, language))
        )
    }

    fun getNakshatraRemedy(analysis: PlanetaryAnalysis, language: Language): Remedy? {
        if (!analysis.needsRemedy) return null
        val nakshatra = analysis.nakshatra
        val deityKey = nakshatraDeities[nakshatra] ?: return null
        val deity = StringResources.get(deityKey, language)
        val remedyKey = try { StringKeyRemedy.valueOf("NAK_REMEDY_METHOD_${nakshatra.name}") } catch (e: Exception) { return null }
        return Remedy(
            category = RemedyCategory.DEITY, title = StringResources.get(StringKeyRemedy.NAK_REMEDY_TITLE, language, nakshatra.displayName),
            description = StringResources.get(StringKeyRemedy.NAK_REMEDY_DESC, language, analysis.planet.getLocalizedName(language), nakshatra.displayName, deity),
            method = StringResources.get(remedyKey, language), timing = StringResources.get(StringKeyRemedy.NAK_REMEDY_TIMING, language, nakshatra.displayName),
            duration = StringResources.get(StringKeyRemedy.NAK_REMEDY_DURATION, language), planet = analysis.planet, priority = RemedyPriority.RECOMMENDED,
            benefits = listOf(StringResources.get(StringKeyGeneralPart7.NAK_BENEFIT_SPECIFIC, language), StringResources.get(StringKeyGeneralPart7.NAK_BENEFIT_BLESSING, language), StringResources.get(StringKeyGeneralPart7.NAK_BENEFIT_COMPLEMENT, language), StringResources.get(StringKeyGeneralPart7.NAK_BENEFIT_FINE_TUNE, language)),
            cautions = listOf(StringResources.get(StringKeyGeneralPart7.NAK_CAUTION_COMBINE, language), StringResources.get(StringKeyGeneralPart7.NAK_CAUTION_CHECK, language)),
            nakshatraSpecific = true
        )
    }

    fun getGandantaRemedy(analysis: PlanetaryAnalysis, language: Language): Remedy? {
        if (!analysis.isInGandanta) return null
        val gandantaType = when (analysis.sign) {
            ZodiacSign.CANCER, ZodiacSign.LEO -> "Cancer-Leo"
            ZodiacSign.SCORPIO, ZodiacSign.SAGITTARIUS -> "Scorpio-Sagittarius"
            ZodiacSign.PISCES, ZodiacSign.ARIES -> "Pisces-Aries"
            else -> return null
        }
        val localizedDay = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(analysis.planet).uppercase()}"), language)
        return Remedy(
            category = RemedyCategory.DEITY, title = StringResources.get(StringKeyGeneralPart4.GANDANTA_TITLE, language),
            description = StringResources.get(StringKeyGeneralPart4.GANDANTA_DESC, language, analysis.planet.getLocalizedName(language), gandantaType),
            method = buildString {
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_TITLE, language))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_1, language, analysis.planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_2, language, analysis.planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_3, language, analysis.planet.getLocalizedName(language)))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_4, language))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_5, language))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_6, language, localizedDay))
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_METHOD_7, language))
                appendLine()
                appendLine(StringResources.get(StringKeyGeneralPart4.GANDANTA_SPECIAL, language))
            },
            timing = StringResources.get(StringKeyGeneralPart4.GANDANTA_TIMING, language, localizedDay),
            duration = StringResources.get(StringKeyGeneralPart4.GANDANTA_DURATION, language, analysis.planet.getLocalizedName(language)), planet = analysis.planet, priority = RemedyPriority.ESSENTIAL,
            benefits = listOf(StringResources.get(StringKeyGeneralPart4.GANDANTA_BENEFIT_BLOCKAGE, language), StringResources.get(StringKeyGeneralPart4.GANDANTA_BENEFIT_REDUCE, language, analysis.planet.getLocalizedName(language)), StringResources.get(StringKeyGeneralPart4.GANDANTA_BENEFIT_TRANSFORM, language), StringResources.get(StringKeyGeneralPart4.GANDANTA_BENEFIT_PROTECT, language)),
            cautions = listOf(StringResources.get(StringKeyGeneralPart4.GANDANTA_CAUTION_CONSISTENT, language), StringResources.get(StringKeyGeneralPart4.GANDANTA_CAUTION_CONSULT, language), StringResources.get(StringKeyGeneralPart4.GANDANTA_CAUTION_SKIP, language))
        )
    }

    private fun getLocalizedMantraTiming(planet: Planet, language: Language): String {
        val day = StringResources.get(StringKeyPanchanga.valueOf("WEEKDAY_${getPlanetaryWeekday(planet).uppercase()}"), language)
        val part = when (planet) {
            Planet.SUN, Planet.MARS, Planet.MERCURY, Planet.JUPITER, Planet.VENUS -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_MORNING, language)
            Planet.MOON, Planet.SATURN -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_EVENING, language)
            Planet.RAHU -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_NIGHT, language)
            else -> ""
        }
        val waxing = if (planet == Planet.MOON) ", " + StringResources.get(StringKeyRemedy.MANTRA_TIMING_WAXING, language) else ""
        return "$day $part$waxing"
    }

    private fun getLocalizedCharityTiming(timing: String, language: Language): String {
        return when (timing.lowercase()) {
            "morning" -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_MORNING, language)
            "evening" -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_EVENING, language)
            "night" -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_NIGHT, language)
            "before sunset" -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_BEFORE_SUNSET, language)
            "before sunrise or after sunset" -> StringResources.get(StringKeyRemedy.CHARITY_TIMING_BEFORE_SUNRISE_AFTER_SUNSET, language)
            else -> timing
        }
    }

    fun getPlanetaryWeekday(planet: Planet): String {
        return when (planet) {
            Planet.SUN -> "Sunday"
            Planet.MOON -> "Monday"
            Planet.MARS -> "Tuesday"
            Planet.MERCURY -> "Wednesday"
            Planet.JUPITER -> "Thursday"
            Planet.VENUS -> "Friday"
            Planet.SATURN, Planet.RAHU -> "Saturday"
            Planet.KETU -> "Tuesday"
            else -> "Sunday"
        }
    }
}



package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringKeyYogaExpanded
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.model.Planet

/**
 * Yoga Localization - Localization utilities for Yoga names and effects
 *
 * Centralizes all localization logic for yoga-related text including:
 * - Yoga names (English and Sanskrit)
 * - Yoga effects descriptions
 * - House significations
 *
 * @author AstroStorm
 */
object YogaLocalization {

    /**
     * Get localized house significations
     */
    fun getLocalizedHouseSignifications(house: Int, language: Language): String {
        val key = when (house) {
            1 -> StringKeyMatchPart1.HOUSE_1_SIGNIFICATION
            2 -> StringKeyMatchPart1.HOUSE_2_SIGNIFICATION
            3 -> StringKeyMatchPart1.HOUSE_3_SIGNIFICATION
            4 -> StringKeyMatchPart1.HOUSE_4_SIGNIFICATION
            5 -> StringKeyMatchPart1.HOUSE_5_SIGNIFICATION
            6 -> StringKeyMatchPart1.HOUSE_6_SIGNIFICATION
            7 -> StringKeyMatchPart1.HOUSE_7_SIGNIFICATION
            8 -> StringKeyMatchPart1.HOUSE_8_SIGNIFICATION
            9 -> StringKeyMatchPart1.HOUSE_9_SIGNIFICATION
            10 -> StringKeyMatchPart1.HOUSE_10_SIGNIFICATION
            11 -> StringKeyMatchPart1.HOUSE_11_SIGNIFICATION
            12 -> StringKeyMatchPart1.HOUSE_12_SIGNIFICATION
            else -> return StringResources.get(StringKeyMatchPart1.VARIOUS_ACTIVITIES, language)
        }
        return StringResources.get(key, language)
    }

    /**
     * Get localized yoga name from a Yoga object
     */
    fun getLocalizedYogaName(yoga: Yoga, language: Language): String {
        yoga.nameKey?.let { key ->
            if (key == StringKeyYogaExpanded.YOGA_VARGOTTAMA_SPEC && yoga.planets.isNotEmpty()) {
                return StringResources.get(key, language, yoga.planets.first().getLocalizedName(language))
            }
            if (yoga.category == YogaCategory.BHAVA_YOGA && yoga.houses.isNotEmpty()) {
                val lordName = StringResources.get(getLordKey(yoga.planets.firstOrNull()), language)
                val houseName = StringResources.get(com.astro.storm.core.common.StringKeyMatchPart1.HOUSE_LABEL, language, yoga.houses.first())
                return StringResources.get(key, language, lordName, houseName)
            }
            if (yoga.category == YogaCategory.CONJUNCTION_YOGA && yoga.planets.size >= 2) {
                val planetNames = yoga.planets.map { it.getLocalizedName(language) }
                val keyToUse = when (yoga.planets.size) {
                    2 -> StringKeyYogaExpanded.YOGA_CONJUNCTION_2_PLANETS
                    3 -> StringKeyYogaExpanded.YOGA_CONJUNCTION_3_PLANETS
                    else -> StringKeyYogaExpanded.YOGA_CONJUNCTION_4_PLANETS
                }
                return when (yoga.planets.size) {
                    2 -> StringResources.get(keyToUse, language, planetNames[0], planetNames[1])
                    3 -> StringResources.get(keyToUse, language, planetNames[0], planetNames[1], planetNames[2])
                    4 -> StringResources.get(keyToUse, language, planetNames[0], planetNames[1], planetNames[2], planetNames[3])
                    else -> yoga.name
                }
            }
            return StringResources.get(key, language)
        }
        return getLocalizedYogaName(yoga.name, language)
    }

    /**
     * Get localized yoga description
     */
    fun getLocalizedYogaDescription(yoga: Yoga, language: Language): String {
        yoga.descriptionKey?.let { return StringResources.get(it, language) }
        return yoga.description // Fallback to original English
    }

    /**
     * Get localized yoga effects
     */
    fun getLocalizedYogaEffects(yoga: Yoga, language: Language): String {
        yoga.effectsKey?.let { key ->
            if (yoga.category == YogaCategory.BHAVA_YOGA && yoga.houses.isNotEmpty()) {
                val lordHouse = "House ${getHouseFromLord(yoga.planets.firstOrNull(), yoga.name)}"
                val houseName = "House ${yoga.houses.first()}"
                return StringResources.get(key, language, lordHouse, houseName)
            }
            return StringResources.get(key, language)
        }
        return getLocalizedYogaEffects(yoga.name, language).ifEmpty { yoga.effects }
    }

    private fun getLordKey(planet: Planet?): com.astro.storm.core.common.StringKeyInterface {
        return when (planet) {
            Planet.SUN -> StringKeyYogaExpanded.LORD_1 // Simplified mapping, ideally should be house-based
            Planet.MOON -> StringKeyYogaExpanded.LORD_4
            Planet.MARS -> StringKeyYogaExpanded.LORD_1
            Planet.MERCURY -> StringKeyYogaExpanded.LORD_3
            Planet.JUPITER -> StringKeyYogaExpanded.LORD_9
            Planet.VENUS -> StringKeyYogaExpanded.LORD_2
            Planet.SATURN -> StringKeyYogaExpanded.LORD_10
            else -> com.astro.storm.core.common.StringKeyReport.REPORT_PLANET
        }
    }
    
    /**
     * Get the house number ruled by a planet for a specific chart.
     * Since we don't have the chart here, we attempt to extract it from the yoga name
     * which was originally generated as "Lord of X in House Y"
     */
    private fun getHouseFromLord(planet: Planet?, yogaName: String): Int {
        // Extract from "Lord of X in House Y"
        return yogaName.substringAfter("Lord of ").substringBefore(" in").toIntOrNull() ?: 1
    }

    /**
     * Get localized yoga name from the yoga's English name
     */
    fun getLocalizedYogaName(englishName: String, language: Language): String {
        val key = when {
            englishName.contains("Kendra-Trikona Raja") -> StringKeyYogaExpanded.YOGA_KENDRA_TRIKONA
            englishName.contains("Parivartana Raja") -> StringKeyYogaExpanded.YOGA_PARIVARTANA
            englishName.contains("Viparita Raja") -> StringKeyYogaExpanded.YOGA_VIPARITA
            englishName.contains("Neecha Bhanga Raja") -> StringKeyYogaExpanded.YOGA_NEECHA_BHANGA
            englishName.contains("Maha Raja") -> StringKeyYogaExpanded.YOGA_MAHA_RAJA
            englishName.contains("Lakshmi") -> StringKeyYogaExpanded.YOGA_LAKSHMI
            englishName.contains("Kubera") -> StringKeyYogaExpanded.YOGA_KUBERA
            englishName.contains("Chandra-Mangala") -> StringKeyYogaExpanded.YOGA_CHANDRA_MANGALA
            englishName.contains("Labha") -> StringKeyYogaExpanded.YOGA_LABHA
            englishName.contains("Ruchaka") -> StringKeyYogaExpanded.YOGA_RUCHAKA
            englishName.contains("Bhadra") -> StringKeyYogaExpanded.YOGA_BHADRA
            englishName.contains("Hamsa") -> StringKeyYogaExpanded.YOGA_HAMSA
            englishName.contains("Malavya") -> StringKeyYogaExpanded.YOGA_MALAVYA
            englishName.contains("Sasa") -> StringKeyYogaExpanded.YOGA_SASA
            englishName.contains("Yava") -> StringKeyYogaExpanded.YOGA_YAVA
            englishName.contains("Shringataka") -> StringKeyYogaExpanded.YOGA_SHRINGATAKA
            englishName.contains("Gada") -> StringKeyYogaExpanded.YOGA_GADA
            englishName.contains("Shakata") -> StringKeyYogaExpanded.YOGA_SHAKATA
            englishName.contains("Rajju") -> StringKeyYogaExpanded.YOGA_RAJJU
            englishName.contains("Musala") -> StringKeyYogaExpanded.YOGA_MUSALA
            englishName.contains("Nala") -> StringKeyYogaExpanded.YOGA_NALA
            englishName.contains("Kedara") -> StringKeyYogaExpanded.YOGA_KEDARA
            englishName.contains("Shoola") -> StringKeyYogaExpanded.YOGA_SHOOLA
            englishName.contains("Yuga") -> StringKeyYogaExpanded.YOGA_YUGA
            englishName.contains("Gola") -> StringKeyYogaExpanded.YOGA_GOLA
            englishName.contains("Veena") -> StringKeyYogaExpanded.YOGA_VEENA
            englishName.contains("Sunafa") -> StringKeyYogaExpanded.YOGA_SUNAFA
            englishName.contains("Anafa") -> StringKeyYogaExpanded.YOGA_ANAFA
            englishName.contains("Durudhara") -> StringKeyYogaExpanded.YOGA_DURUDHARA
            englishName.contains("Gaja-Kesari") -> StringKeyYogaExpanded.YOGA_GAJA_KESARI
            englishName.contains("Adhi") -> StringKeyYogaExpanded.YOGA_ADHI
            englishName.contains("Vesi") -> StringKeyYogaExpanded.YOGA_VESI
            englishName.contains("Vosi") -> StringKeyYogaExpanded.YOGA_VOSI
            englishName.contains("Ubhayachari") -> StringKeyYogaExpanded.YOGA_UBHAYACHARI
            englishName.contains("Kemadruma") -> StringKeyYogaExpanded.YOGA_KEMADRUMA
            englishName.contains("Daridra") -> StringKeyYogaExpanded.YOGA_DARIDRA
            englishName.contains("Guru-Chandal") -> StringKeyYogaExpanded.YOGA_GURU_CHANDAL
            englishName.contains("Dasa-Mula") -> StringKeyYogaExpanded.YOGA_DASA_MULA
            englishName.contains("Vargottama") -> StringKeyYogaExpanded.YOGA_VARGOTTAMA_STRENGTH
            englishName.contains("Budha-Aditya") -> StringKeyYogaExpanded.YOGA_BUDHA_ADITYA
            englishName.contains("Amala") -> StringKeyYogaExpanded.YOGA_AMALA
            englishName.contains("Saraswati") -> StringKeyYogaExpanded.YOGA_SARASWATI
            englishName.contains("Parvata") -> StringKeyYogaExpanded.YOGA_PARVATA
            englishName.contains("Kahala") -> StringKeyYogaExpanded.YOGA_KAHALA
            englishName.contains("Dhana") -> StringKeyYogaExpanded.YOGA_CAT_DHANA
            // Grahan and Nodal yogas
            englishName.contains("Surya Grahan") && !englishName.contains("Ketu") -> StringKeyYogaExpanded.YOGA_SURYA_GRAHAN
            englishName.contains("Surya-Ketu Grahan") -> StringKeyYogaExpanded.YOGA_SURYA_KETU_GRAHAN
            englishName.contains("Chandra Grahan") -> StringKeyYogaExpanded.YOGA_CHANDRA_GRAHAN
            englishName.contains("Chandra-Ketu") -> StringKeyYogaExpanded.YOGA_CHANDRA_KETU
            englishName.contains("Angarak") -> StringKeyYogaExpanded.YOGA_ANGARAK
            englishName.contains("Shrapit") -> StringKeyYogaExpanded.YOGA_SHRAPIT
            englishName.contains("Kala Sarpa") -> StringKeyYogaExpanded.YOGA_KALA_SARPA
            englishName.contains("Papakartari") -> StringKeyYogaExpanded.YOGA_PAPAKARTARI
            englishName.contains("Shubhakartari") -> StringKeyYogaExpanded.YOGA_SHUBHAKARTARI
            englishName.contains("Sanyasa") -> StringKeyYogaExpanded.YOGA_SANYASA
            englishName.contains("Chamara") -> StringKeyYogaExpanded.YOGA_CHAMARA
            englishName.contains("Dharma-Karmadhipati") -> StringKeyYogaExpanded.YOGA_DHARMA_KARMADHIPATI
            englishName.contains("Maha Bhagya") -> StringKeyYogaExpanded.YOGA_MAHA_BHAGYA
            englishName.contains("Pushkala") -> StringKeyYogaExpanded.YOGA_PUSHAKALA
            englishName.contains("Akhanda Samrajya") -> StringKeyYogaExpanded.YOGA_AKHANDA_SAMRAJYA
            englishName.contains("Vasumathi") -> StringKeyYogaExpanded.YOGA_VASUMATHI
            englishName.contains("Mahalaxmi") -> StringKeyYogaExpanded.YOGA_MAHALAXMI
            englishName.contains("Gouri") -> StringKeyYogaExpanded.YOGA_GOURI
            englishName.contains("Bharathi") -> StringKeyYogaExpanded.YOGA_BHARATHI
            englishName.contains("Parijata") -> StringKeyYogaExpanded.YOGA_PARIJATA
            englishName.contains("Kusuma") -> StringKeyYogaExpanded.YOGA_KUSUMA
            englishName.contains("Indu Lagna") -> StringKeyYogaExpanded.YOGA_INDU_LAGNA
            englishName.contains("Sakata") -> StringKeyYogaExpanded.YOGA_SAKATA
            englishName.contains("Kemadruma Bhanga") -> StringKeyYogaExpanded.YOGA_KEMADRUMA_BHANGA
            englishName.contains("Vallaki") -> StringKeyYogaExpanded.YOGA_SANKHYA_VALLAKI
            englishName.contains("Damini") -> StringKeyYogaExpanded.YOGA_SANKHYA_DAMINI
            englishName.contains("Pasa") -> StringKeyYogaExpanded.YOGA_SANKHYA_PASA
            englishName.contains("Kedara") -> StringKeyYogaExpanded.YOGA_SANKHYA_KEDARA
            englishName.contains("Shoola") && englishName.contains("Sankhya") -> StringKeyYogaExpanded.YOGA_SANKHYA_SHOOLA
            englishName.contains("Yuga") && englishName.contains("Sankhya") -> StringKeyYogaExpanded.YOGA_SANKHYA_YUGA
            englishName.contains("Gola") && englishName.contains("Sankhya") -> StringKeyYogaExpanded.YOGA_SANKHYA_GOLA
            // Expanded Yogas
            englishName.contains("Bheri") -> StringKeyYogaExpanded.YOGA_BHERI
            englishName.contains("Sreenatha") -> StringKeyYogaExpanded.YOGA_SREENATHA
            englishName.contains("Khadga") -> StringKeyYogaExpanded.YOGA_KHADGA
            englishName.contains("Kalanidhi") -> StringKeyYogaExpanded.YOGA_KALANIDHI

            // Extended Raja Yogas
            englishName.contains("Simhasana") -> StringKeyYogaExpanded.YOGA_SIMHASANA
            englishName.contains("Chatussagara") -> StringKeyYogaExpanded.YOGA_CHATUSSAGARA
            englishName.contains("Digbala Raja") -> StringKeyYogaExpanded.YOGA_DIGBALA_RAJA

            // Extended Dhana Yogas
            englishName.contains("Mahalakshmi") -> StringKeyYogaExpanded.YOGA_MAHALAKSHMI
            englishName.contains("Dhana Karaka") -> StringKeyYogaExpanded.YOGA_DHANA_KARAKA
            englishName.contains("Business") -> StringKeyYogaExpanded.YOGA_BUSINESS
            englishName.contains("Property") -> StringKeyYogaExpanded.YOGA_PROPERTY
            englishName.contains("Inheritance") -> StringKeyYogaExpanded.YOGA_INHERITANCE

            // Arishta Yogas
            englishName.contains("Balarishta") -> StringKeyYogaExpanded.YOGA_BALARISHTA
            englishName.contains("Rogaishta") -> StringKeyYogaExpanded.YOGA_ROGAISHTA
            englishName.contains("Bandhana") -> StringKeyYogaExpanded.YOGA_BANDHANA
            englishName.contains("Duryoga") -> StringKeyYogaExpanded.YOGA_DURYOGA
            englishName.contains("Combustion") -> StringKeyYogaExpanded.YOGA_COMBUSTION

            // Sannyasa & Moksha Yogas
            englishName.contains("Sannyasa") -> StringKeyYogaExpanded.YOGA_SANNYASA
            englishName.contains("Moksha Trikona") -> StringKeyYogaExpanded.YOGA_MOKSHA_TRIKONA
            englishName.contains("Moksha") -> StringKeyYogaExpanded.YOGA_MOKSHA
            englishName.contains("Pravrajya") -> StringKeyYogaExpanded.YOGA_PRAVRAJYA
            englishName.contains("Ketu Moksha") -> StringKeyYogaExpanded.YOGA_KETU_MOKSHA

            // Lagna Yogas
            englishName.contains("Lagnesh Strength") -> StringKeyYogaExpanded.YOGA_LAGNESH_STRENGTH
            englishName.contains("Lagna Adhi") -> StringKeyYogaExpanded.YOGA_LAGNA_ADHI
            englishName.contains("Subhakartari Lagna") -> StringKeyYogaExpanded.YOGA_SUBHAKARTARI_LAGNA
            englishName.contains("Papakartari Lagna") -> StringKeyYogaExpanded.YOGA_PAPAKARTARI_LAGNA
            englishName.contains("Vargottama Lagna") -> StringKeyYogaExpanded.YOGA_VARGOTTAMA_LAGNA

            // Parivarttana Yogas
            englishName.contains("Maha Parivarttana") -> StringKeyYogaExpanded.YOGA_MAHA_PARIVARTTANA
            englishName.contains("Khala Parivarttana") -> StringKeyYogaExpanded.YOGA_KHALA_PARIVARTTANA
            englishName.contains("Dainya Parivarttana") -> StringKeyYogaExpanded.YOGA_DAINYA_PARIVARTTANA

            // Nakshatra Yogas
            englishName.contains("Pushya Nakshatra") -> StringKeyYogaExpanded.YOGA_PUSHYA_NAKSHATRA
            englishName.contains("Gandanta") -> StringKeyYogaExpanded.YOGA_GANDANTA
            englishName.contains("Deva Nakshatra") -> StringKeyYogaExpanded.YOGA_NAKSHATRA_DEVA
            englishName.contains("Manushya Nakshatra") -> StringKeyYogaExpanded.YOGA_NAKSHATRA_MANUSHYA
            englishName.contains("Rakshasa Nakshatra") -> StringKeyYogaExpanded.YOGA_NAKSHATRA_RAKSHASA

            // Classical Nabhasa Yogas
            englishName.contains("Kamala") -> StringKeyYogaExpanded.YOGA_KAMALA
            englishName.contains("Vapi") -> StringKeyYogaExpanded.YOGA_VAPI
            englishName.contains("Yupa") -> StringKeyYogaExpanded.YOGA_YUPA
            englishName.contains("Shara") -> StringKeyYogaExpanded.YOGA_SHARA
            englishName.contains("Shakti") -> StringKeyYogaExpanded.YOGA_SHAKTI
            englishName.contains("Danda") -> StringKeyYogaExpanded.YOGA_DANDA
            englishName.contains("Nauka") -> StringKeyYogaExpanded.YOGA_NAUKA
            englishName.contains("Kuta") -> StringKeyYogaExpanded.YOGA_KUTA
            englishName.contains("Vajra") -> StringKeyYogaExpanded.YOGA_VAJRA

            // Generic handling for Bhava and Conjunction
            englishName.contains("Lord of") -> return englishName
            englishName.contains("Conjunction of") -> return englishName
            else -> return englishName // Fallback to English name
        }
        return StringResources.get(key, language)
    }

    /**
     * Get localized yoga effects from the yoga's English name
     */
    fun getLocalizedYogaEffects(yogaName: String, language: Language): String {
        val key = when {
            yogaName.contains("Ruchaka") -> StringKeyYogaExpanded.YOGA_EFFECT_RUCHAKA
            yogaName.contains("Bhadra") -> StringKeyYogaExpanded.YOGA_EFFECT_BHADRA
            yogaName.contains("Hamsa") -> StringKeyYogaExpanded.YOGA_EFFECT_HAMSA
            yogaName.contains("Malavya") -> StringKeyYogaExpanded.YOGA_EFFECT_MALAVYA
            yogaName.contains("Sasa") -> StringKeyYogaExpanded.YOGA_EFFECT_SASA
            yogaName.contains("Gaja-Kesari") -> StringKeyYogaExpanded.YOGA_EFFECT_GAJA_KESARI
            yogaName.contains("Sunafa") -> StringKeyYogaExpanded.YOGA_EFFECT_SUNAFA
            yogaName.contains("Anafa") -> StringKeyYogaExpanded.YOGA_EFFECT_ANAFA
            yogaName.contains("Durudhara") -> StringKeyYogaExpanded.YOGA_EFFECT_DURUDHARA
            yogaName.contains("Adhi") -> StringKeyYogaExpanded.YOGA_EFFECT_ADHI
            yogaName.contains("Budha-Aditya") -> StringKeyYogaExpanded.YOGA_EFFECT_BUDHA_ADITYA
            yogaName.contains("Saraswati") -> StringKeyYogaExpanded.YOGA_EFFECT_SARASWATI
            yogaName.contains("Parvata") -> StringKeyYogaExpanded.YOGA_EFFECT_PARVATA
            yogaName.contains("Lakshmi") -> StringKeyYogaExpanded.YOGA_EFFECT_LAKSHMI
            yogaName.contains("Maha Raja") -> StringKeyYogaExpanded.YOGA_EFFECT_MAHA_RAJA
            yogaName.contains("Kendra-Trikona") -> StringKeyYogaExpanded.YOGA_EFFECT_KENDRA_TRIKONA
            yogaName.contains("Parivartana") -> StringKeyYogaExpanded.YOGA_EFFECT_PARIVARTANA
            yogaName.contains("Viparita") -> StringKeyYogaExpanded.YOGA_EFFECT_VIPARITA
            yogaName.contains("Neecha Bhanga") -> StringKeyYogaExpanded.YOGA_EFFECT_NEECHA_BHANGA
            yogaName.contains("Kemadruma") -> StringKeyYogaExpanded.YOGA_EFFECT_KEMADRUMA
            yogaName.contains("Daridra") -> StringKeyYogaExpanded.YOGA_EFFECT_DARIDRA
            yogaName.contains("Shakata") -> StringKeyYogaExpanded.YOGA_EFFECT_SHAKATA
            yogaName.contains("Guru-Chandal") -> StringKeyYogaExpanded.YOGA_EFFECT_GURU_CHANDAL
            yogaName.contains("Vesi") -> StringKeyYogaExpanded.YOGA_EFFECT_VESI
            yogaName.contains("Vosi") -> StringKeyYogaExpanded.YOGA_EFFECT_VOSI
            yogaName.contains("Ubhayachari") -> StringKeyYogaExpanded.YOGA_EFFECT_UBHAYACHARI
            yogaName.contains("Labha") -> StringKeyYogaExpanded.YOGA_EFFECT_LABHA
            yogaName.contains("Kubera") -> StringKeyYogaExpanded.YOGA_EFFECT_KUBERA
            yogaName.contains("Chandra-Mangala") -> StringKeyYogaExpanded.YOGA_EFFECT_CHANDRA_MANGALA
            yogaName.contains("Dasa-Mula") -> StringKeyYogaExpanded.YOGA_EFFECT_DASA_MULA
            yogaName.contains("Kahala") -> StringKeyYogaExpanded.YOGA_EFFECT_KAHALA
            yogaName.contains("Yava") -> StringKeyYogaExpanded.YOGA_EFFECT_YAVA
            yogaName.contains("Shringataka") -> StringKeyYogaExpanded.YOGA_EFFECT_SHRINGATAKA
            yogaName.contains("Gada") -> StringKeyYogaExpanded.YOGA_EFFECT_GADA
            yogaName.contains("Rajju") -> StringKeyYogaExpanded.YOGA_EFFECT_RAJJU
            yogaName.contains("Musala") -> StringKeyYogaExpanded.YOGA_EFFECT_MUSALA
            yogaName.contains("Nala") -> StringKeyYogaExpanded.YOGA_EFFECT_NALA
            yogaName.contains("Kedara") -> StringKeyYogaExpanded.YOGA_EFFECT_KEDARA
            yogaName.contains("Shoola") -> StringKeyYogaExpanded.YOGA_EFFECT_SHOOLA
            yogaName.contains("Yuga") -> StringKeyYogaExpanded.YOGA_EFFECT_YUGA
            yogaName.contains("Gola") -> StringKeyYogaExpanded.YOGA_EFFECT_GOLA
            yogaName.contains("Veena") -> StringKeyYogaExpanded.YOGA_EFFECT_VEENA
            // Grahan and Nodal yogas
            yogaName.contains("Surya Grahan") && !yogaName.contains("Ketu") -> StringKeyYogaExpanded.YOGA_EFFECT_SURYA_GRAHAN
            yogaName.contains("Surya-Ketu Grahan") -> StringKeyYogaExpanded.YOGA_EFFECT_SURYA_KETU_GRAHAN
            yogaName.contains("Chandra Grahan") -> StringKeyYogaExpanded.YOGA_EFFECT_CHANDRA_GRAHAN
            yogaName.contains("Chandra-Ketu") -> StringKeyYogaExpanded.YOGA_EFFECT_CHANDRA_KETU
            yogaName.contains("Angarak") -> StringKeyYogaExpanded.YOGA_EFFECT_ANGARAK
            yogaName.contains("Shrapit") -> StringKeyYogaExpanded.YOGA_EFFECT_SHRAPIT
            yogaName.contains("Kala Sarpa") -> StringKeyYogaExpanded.YOGA_EFFECT_KALA_SARPA
            yogaName.contains("Papakartari") -> StringKeyYogaExpanded.YOGA_EFFECT_PAPAKARTARI
            yogaName.contains("Shubhakartari") -> StringKeyYogaExpanded.YOGA_EFFECT_SHUBHAKARTARI
            yogaName.contains("Sanyasa") -> StringKeyYogaExpanded.YOGA_EFFECT_SANYASA
            yogaName.contains("Chamara") -> StringKeyYogaExpanded.YOGA_EFFECT_CHAMARA
            yogaName.contains("Dharma-Karmadhipati") -> StringKeyYogaExpanded.YOGA_EFFECT_DHARMA_KARMADHIPATI
            yogaName.contains("Maha Bhagya") -> StringKeyYogaExpanded.YOGA_EFFECT_MAHA_BHAGYA
            yogaName.contains("Pushkala") -> StringKeyYogaExpanded.YOGA_EFFECT_PUSHAKALA
            yogaName.contains("Akhanda Samrajya") -> StringKeyYogaExpanded.YOGA_EFFECT_AKHANDA_SAMRAJYA
            yogaName.contains("Vasumathi") -> StringKeyYogaExpanded.YOGA_EFFECT_VASUMATHI
            yogaName.contains("Mahalaxmi") -> StringKeyYogaExpanded.YOGA_EFFECT_MAHALAXMI
            yogaName.contains("Parijata") -> StringKeyYogaExpanded.YOGA_EFFECT_PARIJATA
            yogaName.contains("Kusuma") -> StringKeyYogaExpanded.YOGA_EFFECT_KUSUMA
            yogaName.contains("Indu Lagna") -> StringKeyYogaExpanded.YOGA_EFFECT_INDU_LAGNA
            yogaName.contains("Sakata") -> StringKeyYogaExpanded.YOGA_EFFECT_SAKATA
            yogaName.contains("Kemadruma Bhanga") -> StringKeyYogaExpanded.YOGA_EFFECT_KEMADRUMA_BHANGA
            yogaName.contains("Vallaki") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_VALLAKI
            yogaName.contains("Damini") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_DAMINI
            yogaName.contains("Pasa") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_PASA
            yogaName.contains("Kedara") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_KEDARA
            yogaName.contains("Shoola") && yogaName.contains("Sankhya") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_SHOOLA
            yogaName.contains("Yuga") && yogaName.contains("Sankhya") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_YUGA
            yogaName.contains("Gola") && yogaName.contains("Sankhya") -> StringKeyYogaExpanded.YOGA_EFFECT_SANKHYA_GOLA
            // Expanded Effects
            yogaName.contains("Bheri") -> StringKeyYogaExpanded.EFFECT_BHERI
            yogaName.contains("Sreenatha") -> StringKeyYogaExpanded.EFFECT_SREENATHA
            yogaName.contains("Khadga") -> StringKeyYogaExpanded.EFFECT_KHADGA
            yogaName.contains("Kalanidhi") -> StringKeyYogaExpanded.EFFECT_KALANIDHI

            // Extended Raja Yoga Effects
            yogaName.contains("Simhasana") -> StringKeyYogaExpanded.EFFECT_SIMHASANA
            yogaName.contains("Chatussagara") -> StringKeyYogaExpanded.EFFECT_CHATUSSAGARA
            yogaName.contains("Digbala Raja") -> StringKeyYogaExpanded.EFFECT_DIGBALA_RAJA

            // Extended Dhana Yoga Effects
            yogaName.contains("Mahalakshmi") -> StringKeyYogaExpanded.EFFECT_MAHALAKSHMI
            yogaName.contains("Dhana Karaka") -> StringKeyYogaExpanded.EFFECT_DHANA_KARAKA
            yogaName.contains("Business") -> StringKeyYogaExpanded.EFFECT_BUSINESS
            yogaName.contains("Property") -> StringKeyYogaExpanded.EFFECT_PROPERTY
            yogaName.contains("Inheritance") -> StringKeyYogaExpanded.EFFECT_INHERITANCE

            // Arishta Yoga Effects
            yogaName.contains("Balarishta") -> StringKeyYogaExpanded.EFFECT_BALARISHTA
            yogaName.contains("Rogaishta") -> StringKeyYogaExpanded.EFFECT_ROGAISHTA
            yogaName.contains("Bandhana") -> StringKeyYogaExpanded.EFFECT_BANDHANA
            yogaName.contains("Duryoga") -> StringKeyYogaExpanded.EFFECT_DURYOGA
            yogaName.contains("Combustion") -> StringKeyYogaExpanded.EFFECT_COMBUSTION

            // Sannyasa & Moksha Yoga Effects
            yogaName.contains("Sannyasa") -> StringKeyYogaExpanded.EFFECT_SANNYASA
            yogaName.contains("Moksha Trikona") -> StringKeyYogaExpanded.EFFECT_MOKSHA_TRIKONA
            yogaName.contains("Moksha") -> StringKeyYogaExpanded.EFFECT_MOKSHA
            yogaName.contains("Pravrajya") -> StringKeyYogaExpanded.EFFECT_PRAVRAJYA
            yogaName.contains("Ketu Moksha") -> StringKeyYogaExpanded.EFFECT_KETU_MOKSHA

            // Lagna Yoga Effects
            yogaName.contains("Lagnesh Strength") -> StringKeyYogaExpanded.EFFECT_LAGNESH_STRENGTH
            yogaName.contains("Lagna Adhi") -> StringKeyYogaExpanded.EFFECT_LAGNA_ADHI
            yogaName.contains("Subhakartari Lagna") -> StringKeyYogaExpanded.EFFECT_SUBHAKARTARI_LAGNA
            yogaName.contains("Papakartari Lagna") -> StringKeyYogaExpanded.EFFECT_PAPAKARTARI_LAGNA
            yogaName.contains("Vargottama Lagna") -> StringKeyYogaExpanded.EFFECT_VARGOTTAMA_LAGNA

            // Parivarttana Yoga Effects
            yogaName.contains("Maha Parivarttana") -> StringKeyYogaExpanded.EFFECT_MAHA_PARIVARTTANA
            yogaName.contains("Khala Parivarttana") -> StringKeyYogaExpanded.EFFECT_KHALA_PARIVARTTANA
            yogaName.contains("Dainya Parivarttana") -> StringKeyYogaExpanded.EFFECT_DAINYA_PARIVARTTANA

            // Nakshatra Yoga Effects
            yogaName.contains("Pushya Nakshatra") -> StringKeyYogaExpanded.EFFECT_PUSHYA_NAKSHATRA
            yogaName.contains("Gandanta") -> StringKeyYogaExpanded.EFFECT_GANDANTA
            yogaName.contains("Deva Nakshatra") -> StringKeyYogaExpanded.EFFECT_NAKSHATRA_DEVA
            yogaName.contains("Manushya Nakshatra") -> StringKeyYogaExpanded.EFFECT_NAKSHATRA_MANUSHYA
            yogaName.contains("Rakshasa Nakshatra") -> StringKeyYogaExpanded.EFFECT_NAKSHATRA_RAKSHASA

            // Classical Nabhasa Yoga Effects
            yogaName.contains("Kamala") -> StringKeyYogaExpanded.EFFECT_KAMALA
            yogaName.contains("Vapi") -> StringKeyYogaExpanded.EFFECT_VAPI
            yogaName.contains("Yupa") -> StringKeyYogaExpanded.EFFECT_YUPA
            yogaName.contains("Shara") -> StringKeyYogaExpanded.EFFECT_SHARA
            yogaName.contains("Shakti") -> StringKeyYogaExpanded.EFFECT_SHAKTI
            yogaName.contains("Danda") -> StringKeyYogaExpanded.EFFECT_DANDA
            yogaName.contains("Nauka") -> StringKeyYogaExpanded.EFFECT_NAUKA
            yogaName.contains("Kuta") -> StringKeyYogaExpanded.EFFECT_KUTA
            yogaName.contains("Vajra") -> StringKeyYogaExpanded.EFFECT_VAJRA

            else -> return "" // Return empty for unknown yogas, caller should use original
        }
        return StringResources.get(key, language)
    }

    /**
     * Get localized yoga Sanskrit name
     */
    fun getLocalizedYogaSanskritName(englishName: String, language: Language): String {
        // Sanskrit names remain the same in both languages, but we provide
        // the Devanagari script version for Nepali
        if (language == Language.ENGLISH) return englishName

        return when {
            englishName.contains("Kendra-Trikona") -> "केन्द्र-त्रिकोण राज योग"
            englishName.contains("Parivartana") -> "परिवर्तन राज योग"
            englishName.contains("Viparita") -> "विपरीत राज योग"
            englishName.contains("Neecha Bhanga") -> "नीच भंग राज योग"
            englishName.contains("Maha Raja") -> "महा राज योग"
            englishName.contains("Lakshmi") -> "लक्ष्मी योग"
            englishName.contains("Kubera") -> "कुबेर योग"
            englishName.contains("Chandra-Mangala") -> "चन्द्र-मंगल योग"
            englishName.contains("Labha") -> "लाभ योग"
            englishName.contains("Ruchaka") -> "रुचक महापुरुष योग"
            englishName.contains("Bhadra") -> "भद्र महापुरुष योग"
            englishName.contains("Hamsa") -> "हंस महापुरुष योग"
            englishName.contains("Malavya") -> "मालव्य महापुरुष योग"
            englishName.contains("Sasa") -> "शश महापुरुष योग"
            englishName.contains("Yava") -> "यव योग"
            englishName.contains("Shringataka") -> "शृंगाटक योग"
            englishName.contains("Gada") -> "गदा योग"
            englishName.contains("Shakata") -> "शकट योग"
            englishName.contains("Rajju") -> "रज्जु योग"
            englishName.contains("Musala") -> "मुसल योग"
            englishName.contains("Nala") -> "नल योग"
            englishName.contains("Kedara") -> "केदार योग"
            englishName.contains("Shoola") -> "शूल योग"
            englishName.contains("Yuga") -> "युग योग"
            englishName.contains("Gola") -> "गोल योग"
            englishName.contains("Veena") -> "वीणा योग"
            englishName.contains("Sunafa") -> "सुनफा योग"
            englishName.contains("Anafa") -> "अनफा योग"
            englishName.contains("Durudhara") -> "दुरुधरा योग"
            englishName.contains("Gaja-Kesari") -> "गज-केसरी योग"
            englishName.contains("Adhi") -> "अधि योग"
            englishName.contains("Vesi") -> "वेशी योग"
            englishName.contains("Vosi") -> "वोशी योग"
            englishName.contains("Ubhayachari") -> "उभयचारी योग"
            englishName.contains("Kemadruma") -> "केमद्रुम योग"
            englishName.contains("Daridra") -> "दरिद्र योग"
            englishName.contains("Guru-Chandal") -> "गुरु-चांडाल योग"
            englishName.contains("Dasa-Mula") -> "दश-मूल योग"
            englishName.contains("Vargottama") -> "वर्गोत्तम बल"
            englishName.contains("Budha-Aditya") -> "बुध-आदित्य योग"
            englishName.contains("Amala") -> "अमला योग"
            englishName.contains("Saraswati") -> "सरस्वती योग"
            englishName.contains("Parvata") -> "पर्वत योग"
            englishName.contains("Kahala") -> "कहल योग"
            englishName.contains("Dhana") -> "धन योग"
            englishName.contains("Surya Grahan") -> "सूर्य ग्रहण योग"
            englishName.contains("Chandra Grahan") -> "चन्द्र ग्रहण योग"
            englishName.contains("Angarak") -> "अंगारक योग"
            englishName.contains("Shrapit") -> "श्रापित योग"
            englishName.contains("Kala Sarpa") -> "कालसर्प योग"
            englishName.contains("Papakartari") -> "पापकर्तरी योग"
            englishName.contains("Shubhakartari") -> "शुभकर्तरी योग"
            englishName.contains("Sanyasa") -> "सन्यास योग"
            englishName.contains("Chamara") -> "चामर योग"
            englishName.contains("Dharma-Karmadhipati") -> "धर्म-कर्माधिपति योग"
            englishName.contains("Maha Bhagya") -> "महा भाग्य योग"
            englishName.contains("Pushkala") -> "पुष्कल योग"
            englishName.contains("Akhanda Samrajya") -> "अखण्ड साम्राज्य योग"
            englishName.contains("Vasumathi") -> "वसुमथि योग"
            englishName.contains("Mahalaxmi") -> "महालक्ष्मी योग"
            englishName.contains("Parijata") -> "पारिजात योग"
            englishName.contains("Kusuma") -> "कुसुम योग"
            englishName.contains("Indu Lagna") -> "इन्दु लग्न धन योग"
            englishName.contains("Sakata") -> "शकट योग"
            englishName.contains("Kemadruma Bhanga") -> "केमद्रुम भंग"
            englishName.contains("Vallaki") -> "वल्लकी साङ्ख्य योग"
            englishName.contains("Damini") -> "दामिनी साङ्ख्य योग"
            englishName.contains("Pasa") -> "पास साङ्ख्य योग"
            englishName.contains("Kedara") -> "केदार साङ्ख्य योग"
            englishName.contains("Shoola") && englishName.contains("Sankhya") -> "शूल साङ्ख्य योग"
            englishName.contains("Yuga") && englishName.contains("Sankhya") -> "युग साङ्ख्य योग"
            englishName.contains("Gola") && englishName.contains("Sankhya") -> "गोल साङ्ख्य योग"
            // Expanded Sanskrit
            englishName.contains("Bheri") -> "भेरी योग"
            englishName.contains("Sreenatha") -> "श्रीनाथ योग"
            englishName.contains("Khadga") -> "खड्ग योग"
            englishName.contains("Kalanidhi") -> "कलानिधि योग"

            // Extended Raja Yogas
            englishName.contains("Simhasana") -> "सिंहासन योग"
            englishName.contains("Chatussagara") -> "चतुःसागर योग"
            englishName.contains("Digbala Raja") -> "दिक्बल राज योग"

            // Extended Dhana Yogas
            englishName.contains("Mahalakshmi") -> "महालक्ष्मी योग"
            englishName.contains("Kubera") -> "कुबेर योग"
            englishName.contains("Dhana Karaka") -> "धन कारक योग"
            englishName.contains("Business") -> "व्यापार योग"
            englishName.contains("Property") -> "सम्पत्ति योग"
            englishName.contains("Inheritance") -> "उत्तराधिकार योग"

            // Arishta Yogas
            englishName.contains("Balarishta") -> "बालारिष्ट योग"
            englishName.contains("Rogaishta") -> "रोगेष्ट योग"
            englishName.contains("Bandhana") -> "बन्धन योग"
            englishName.contains("Duryoga") -> "दुर्योग"
            englishName.contains("Combustion") -> "अस्त योग"

            // Sannyasa & Moksha Yogas
            englishName.contains("Sannyasa") -> "सन्यास योग"
            englishName.contains("Moksha Trikona") -> "मोक्ष त्रिकोण योग"
            englishName.contains("Moksha") -> "मोक्ष योग"
            englishName.contains("Pravrajya") -> "प्रव्रज्या योग"
            englishName.contains("Ketu Moksha") -> "केतु मोक्ष योग"

            // Lagna Yogas
            englishName.contains("Lagnesh Strength") -> "लग्नेश बल योग"
            englishName.contains("Lagna Adhi") -> "लग्न अधि योग"
            englishName.contains("Subhakartari Lagna") -> "शुभकर्तरी लग्न योग"
            englishName.contains("Papakartari Lagna") -> "पापकर्तरी लग्न योग"
            englishName.contains("Vargottama Lagna") -> "वर्गोत्तम लग्न योग"

            // Parivarttana Yogas
            englishName.contains("Maha Parivarttana") -> "महा परिवर्तन योग"
            englishName.contains("Khala Parivarttana") -> "खल परिवर्तन योग"
            englishName.contains("Dainya Parivarttana") -> "दैन्य परिवर्तन योग"

            // Nakshatra Yogas
            englishName.contains("Pushya Nakshatra") -> "पुष्य नक्षत्र योग"
            englishName.contains("Gandanta") -> "गण्डान्त योग"
            englishName.contains("Deva Nakshatra") -> "देव नक्षत्र योग"
            englishName.contains("Manushya Nakshatra") -> "मनुष्य नक्षत्र योग"
            englishName.contains("Rakshasa Nakshatra") -> "राक्षस नक्षत्र योग"

            // Classical Nabhasa Yogas
            englishName.contains("Kamala") -> "कमल योग"
            englishName.contains("Vapi") -> "वापी योग"
            englishName.contains("Yupa") -> "यूप योग"
            englishName.contains("Shara") -> "शर योग"
            englishName.contains("Shakti") -> "शक्ति योग"
            englishName.contains("Danda") -> "दण्ड योग"
            englishName.contains("Nauka") -> "नौका योग"
            englishName.contains("Kuta") -> "कूट योग"
            englishName.contains("Vajra") -> "वज्र योग"

            else -> englishName
        }
    }
}



package com.astro.storm.ephemeris.yoga

import com.astro.storm.core.common.Language
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources

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
            1 -> StringKeyMatch.HOUSE_1_SIGNIFICATION
            2 -> StringKeyMatch.HOUSE_2_SIGNIFICATION
            3 -> StringKeyMatch.HOUSE_3_SIGNIFICATION
            4 -> StringKeyMatch.HOUSE_4_SIGNIFICATION
            5 -> StringKeyMatch.HOUSE_5_SIGNIFICATION
            6 -> StringKeyMatch.HOUSE_6_SIGNIFICATION
            7 -> StringKeyMatch.HOUSE_7_SIGNIFICATION
            8 -> StringKeyMatch.HOUSE_8_SIGNIFICATION
            9 -> StringKeyMatch.HOUSE_9_SIGNIFICATION
            10 -> StringKeyMatch.HOUSE_10_SIGNIFICATION
            11 -> StringKeyMatch.HOUSE_11_SIGNIFICATION
            12 -> StringKeyMatch.HOUSE_12_SIGNIFICATION
            else -> return StringResources.get(StringKeyMatch.VARIOUS_ACTIVITIES, language)
        }
        return StringResources.get(key, language)
    }

    /**
     * Get localized yoga name from the yoga's English name
     */
    fun getLocalizedYogaName(englishName: String, language: Language): String {
        val key = when {
            englishName.contains("Kendra-Trikona Raja") -> StringKeyMatch.YOGA_KENDRA_TRIKONA
            englishName.contains("Parivartana Raja") -> StringKeyMatch.YOGA_PARIVARTANA
            englishName.contains("Viparita Raja") -> StringKeyMatch.YOGA_VIPARITA
            englishName.contains("Neecha Bhanga Raja") -> StringKeyMatch.YOGA_NEECHA_BHANGA
            englishName.contains("Maha Raja") -> StringKeyMatch.YOGA_MAHA_RAJA
            englishName.contains("Lakshmi") -> StringKeyMatch.YOGA_LAKSHMI
            englishName.contains("Kubera") -> StringKeyMatch.YOGA_KUBERA
            englishName.contains("Chandra-Mangala") -> StringKeyMatch.YOGA_CHANDRA_MANGALA
            englishName.contains("Labha") -> StringKeyMatch.YOGA_LABHA
            englishName.contains("Ruchaka") -> StringKeyMatch.YOGA_RUCHAKA
            englishName.contains("Bhadra") -> StringKeyMatch.YOGA_BHADRA
            englishName.contains("Hamsa") -> StringKeyMatch.YOGA_HAMSA
            englishName.contains("Malavya") -> StringKeyMatch.YOGA_MALAVYA
            englishName.contains("Sasa") -> StringKeyMatch.YOGA_SASA
            englishName.contains("Yava") -> StringKeyMatch.YOGA_YAVA
            englishName.contains("Shringataka") -> StringKeyMatch.YOGA_SHRINGATAKA
            englishName.contains("Gada") -> StringKeyMatch.YOGA_GADA
            englishName.contains("Shakata") -> StringKeyMatch.YOGA_SHAKATA
            englishName.contains("Rajju") -> StringKeyMatch.YOGA_RAJJU
            englishName.contains("Musala") -> StringKeyMatch.YOGA_MUSALA
            englishName.contains("Nala") -> StringKeyMatch.YOGA_NALA
            englishName.contains("Kedara") -> StringKeyMatch.YOGA_KEDARA
            englishName.contains("Shoola") -> StringKeyMatch.YOGA_SHOOLA
            englishName.contains("Yuga") -> StringKeyMatch.YOGA_YUGA
            englishName.contains("Gola") -> StringKeyMatch.YOGA_GOLA
            englishName.contains("Veena") -> StringKeyMatch.YOGA_VEENA
            englishName.contains("Sunafa") -> StringKeyMatch.YOGA_SUNAFA
            englishName.contains("Anafa") -> StringKeyMatch.YOGA_ANAFA
            englishName.contains("Durudhara") -> StringKeyMatch.YOGA_DURUDHARA
            englishName.contains("Gaja-Kesari") -> StringKeyMatch.YOGA_GAJA_KESARI
            englishName.contains("Adhi") -> StringKeyMatch.YOGA_ADHI
            englishName.contains("Vesi") -> StringKeyMatch.YOGA_VESI
            englishName.contains("Vosi") -> StringKeyMatch.YOGA_VOSI
            englishName.contains("Ubhayachari") -> StringKeyMatch.YOGA_UBHAYACHARI
            englishName.contains("Kemadruma") -> StringKeyMatch.YOGA_KEMADRUMA
            englishName.contains("Daridra") -> StringKeyMatch.YOGA_DARIDRA
            englishName.contains("Guru-Chandal") -> StringKeyMatch.YOGA_GURU_CHANDAL
            englishName.contains("Dasa-Mula") -> StringKeyMatch.YOGA_DASA_MULA
            englishName.contains("Vargottama") -> StringKeyMatch.YOGA_VARGOTTAMA_STRENGTH
            englishName.contains("Budha-Aditya") -> StringKeyMatch.YOGA_BUDHA_ADITYA
            englishName.contains("Amala") -> StringKeyMatch.YOGA_AMALA
            englishName.contains("Saraswati") -> StringKeyMatch.YOGA_SARASWATI
            englishName.contains("Parvata") -> StringKeyMatch.YOGA_PARVATA
            englishName.contains("Kahala") -> StringKeyMatch.YOGA_KAHALA
            englishName.contains("Dhana") -> StringKeyMatch.YOGA_CAT_DHANA
            // Grahan and Nodal yogas
            englishName.contains("Surya Grahan") && !englishName.contains("Ketu") -> StringKeyMatch.YOGA_SURYA_GRAHAN
            englishName.contains("Surya-Ketu Grahan") -> StringKeyMatch.YOGA_SURYA_KETU_GRAHAN
            englishName.contains("Chandra Grahan") -> StringKeyMatch.YOGA_CHANDRA_GRAHAN
            englishName.contains("Chandra-Ketu") -> StringKeyMatch.YOGA_CHANDRA_KETU
            englishName.contains("Angarak") -> StringKeyMatch.YOGA_ANGARAK
            englishName.contains("Shrapit") -> StringKeyMatch.YOGA_SHRAPIT
            englishName.contains("Kala Sarpa") -> StringKeyMatch.YOGA_KALA_SARPA
            englishName.contains("Papakartari") -> StringKeyMatch.YOGA_PAPAKARTARI
            englishName.contains("Shubhakartari") -> StringKeyMatch.YOGA_SHUBHAKARTARI
            englishName.contains("Sanyasa") -> StringKeyMatch.YOGA_SANYASA
            englishName.contains("Chamara") -> StringKeyMatch.YOGA_CHAMARA
            englishName.contains("Dharma-Karmadhipati") -> StringKeyMatch.YOGA_DHARMA_KARMADHIPATI
            englishName.contains("Maha Bhagya") -> StringKeyMatch.YOGA_MAHA_BHAGYA
            englishName.contains("Pushkala") -> StringKeyMatch.YOGA_PUSHAKALA
            englishName.contains("Akhanda Samrajya") -> StringKeyMatch.YOGA_AKHANDA_SAMRAJYA
            englishName.contains("Vasumathi") -> StringKeyMatch.YOGA_VASUMATHI
            englishName.contains("Mahalaxmi") -> StringKeyMatch.YOGA_MAHALAXMI
            englishName.contains("Parijata") -> StringKeyMatch.YOGA_PARIJATA
            englishName.contains("Kusuma") -> StringKeyMatch.YOGA_KUSUMA
            englishName.contains("Indu Lagna") -> StringKeyMatch.YOGA_INDU_LAGNA
            englishName.contains("Sakata") -> StringKeyMatch.YOGA_SAKATA
            englishName.contains("Kemadruma Bhanga") -> StringKeyMatch.YOGA_KEMADRUMA_BHANGA
            englishName.contains("Vallaki") -> StringKeyMatch.YOGA_SANKHYA_VALLAKI
            englishName.contains("Damini") -> StringKeyMatch.YOGA_SANKHYA_DAMINI
            englishName.contains("Pasa") -> StringKeyMatch.YOGA_SANKHYA_PASA
            englishName.contains("Kedara") -> StringKeyMatch.YOGA_SANKHYA_KEDARA
            englishName.contains("Shoola") && englishName.contains("Sankhya") -> StringKeyMatch.YOGA_SANKHYA_SHOOLA
            englishName.contains("Yuga") && englishName.contains("Sankhya") -> StringKeyMatch.YOGA_SANKHYA_YUGA
            englishName.contains("Gola") && englishName.contains("Sankhya") -> StringKeyMatch.YOGA_SANKHYA_GOLA
            else -> return englishName // Fallback to English name
        }
        return StringResources.get(key, language)
    }

    /**
     * Get localized yoga effects from the yoga's English name
     */
    fun getLocalizedYogaEffects(yogaName: String, language: Language): String {
        val key = when {
            yogaName.contains("Ruchaka") -> StringKeyMatch.YOGA_EFFECT_RUCHAKA
            yogaName.contains("Bhadra") -> StringKeyMatch.YOGA_EFFECT_BHADRA
            yogaName.contains("Hamsa") -> StringKeyMatch.YOGA_EFFECT_HAMSA
            yogaName.contains("Malavya") -> StringKeyMatch.YOGA_EFFECT_MALAVYA
            yogaName.contains("Sasa") -> StringKeyMatch.YOGA_EFFECT_SASA
            yogaName.contains("Gaja-Kesari") -> StringKeyMatch.YOGA_EFFECT_GAJA_KESARI
            yogaName.contains("Sunafa") -> StringKeyMatch.YOGA_EFFECT_SUNAFA
            yogaName.contains("Anafa") -> StringKeyMatch.YOGA_EFFECT_ANAFA
            yogaName.contains("Durudhara") -> StringKeyMatch.YOGA_EFFECT_DURUDHARA
            yogaName.contains("Adhi") -> StringKeyMatch.YOGA_EFFECT_ADHI
            yogaName.contains("Budha-Aditya") -> StringKeyMatch.YOGA_EFFECT_BUDHA_ADITYA
            yogaName.contains("Saraswati") -> StringKeyMatch.YOGA_EFFECT_SARASWATI
            yogaName.contains("Parvata") -> StringKeyMatch.YOGA_EFFECT_PARVATA
            yogaName.contains("Lakshmi") -> StringKeyMatch.YOGA_EFFECT_LAKSHMI
            yogaName.contains("Maha Raja") -> StringKeyMatch.YOGA_EFFECT_MAHA_RAJA
            yogaName.contains("Kendra-Trikona") -> StringKeyMatch.YOGA_EFFECT_KENDRA_TRIKONA
            yogaName.contains("Parivartana") -> StringKeyMatch.YOGA_EFFECT_PARIVARTANA
            yogaName.contains("Viparita") -> StringKeyMatch.YOGA_EFFECT_VIPARITA
            yogaName.contains("Neecha Bhanga") -> StringKeyMatch.YOGA_EFFECT_NEECHA_BHANGA
            yogaName.contains("Kemadruma") -> StringKeyMatch.YOGA_EFFECT_KEMADRUMA
            yogaName.contains("Daridra") -> StringKeyMatch.YOGA_EFFECT_DARIDRA
            yogaName.contains("Shakata") -> StringKeyMatch.YOGA_EFFECT_SHAKATA
            yogaName.contains("Guru-Chandal") -> StringKeyMatch.YOGA_EFFECT_GURU_CHANDAL
            yogaName.contains("Vesi") -> StringKeyMatch.YOGA_EFFECT_VESI
            yogaName.contains("Vosi") -> StringKeyMatch.YOGA_EFFECT_VOSI
            yogaName.contains("Ubhayachari") -> StringKeyMatch.YOGA_EFFECT_UBHAYACHARI
            yogaName.contains("Labha") -> StringKeyMatch.YOGA_EFFECT_LABHA
            yogaName.contains("Kubera") -> StringKeyMatch.YOGA_EFFECT_KUBERA
            yogaName.contains("Chandra-Mangala") -> StringKeyMatch.YOGA_EFFECT_CHANDRA_MANGALA
            yogaName.contains("Dasa-Mula") -> StringKeyMatch.YOGA_EFFECT_DASA_MULA
            yogaName.contains("Kahala") -> StringKeyMatch.YOGA_EFFECT_KAHALA
            yogaName.contains("Yava") -> StringKeyMatch.YOGA_EFFECT_YAVA
            yogaName.contains("Shringataka") -> StringKeyMatch.YOGA_EFFECT_SHRINGATAKA
            yogaName.contains("Gada") -> StringKeyMatch.YOGA_EFFECT_GADA
            yogaName.contains("Rajju") -> StringKeyMatch.YOGA_EFFECT_RAJJU
            yogaName.contains("Musala") -> StringKeyMatch.YOGA_EFFECT_MUSALA
            yogaName.contains("Nala") -> StringKeyMatch.YOGA_EFFECT_NALA
            yogaName.contains("Kedara") -> StringKeyMatch.YOGA_EFFECT_KEDARA
            yogaName.contains("Shoola") -> StringKeyMatch.YOGA_EFFECT_SHOOLA
            yogaName.contains("Yuga") -> StringKeyMatch.YOGA_EFFECT_YUGA
            yogaName.contains("Gola") -> StringKeyMatch.YOGA_EFFECT_GOLA
            yogaName.contains("Veena") -> StringKeyMatch.YOGA_EFFECT_VEENA
            // Grahan and Nodal yogas
            yogaName.contains("Surya Grahan") && !yogaName.contains("Ketu") -> StringKeyMatch.YOGA_EFFECT_SURYA_GRAHAN
            yogaName.contains("Surya-Ketu Grahan") -> StringKeyMatch.YOGA_EFFECT_SURYA_KETU_GRAHAN
            yogaName.contains("Chandra Grahan") -> StringKeyMatch.YOGA_EFFECT_CHANDRA_GRAHAN
            yogaName.contains("Chandra-Ketu") -> StringKeyMatch.YOGA_EFFECT_CHANDRA_KETU
            yogaName.contains("Angarak") -> StringKeyMatch.YOGA_EFFECT_ANGARAK
            yogaName.contains("Shrapit") -> StringKeyMatch.YOGA_EFFECT_SHRAPIT
            yogaName.contains("Kala Sarpa") -> StringKeyMatch.YOGA_EFFECT_KALA_SARPA
            yogaName.contains("Papakartari") -> StringKeyMatch.YOGA_EFFECT_PAPAKARTARI
            yogaName.contains("Shubhakartari") -> StringKeyMatch.YOGA_EFFECT_SHUBHAKARTARI
            yogaName.contains("Sanyasa") -> StringKeyMatch.YOGA_EFFECT_SANYASA
            yogaName.contains("Chamara") -> StringKeyMatch.YOGA_EFFECT_CHAMARA
            yogaName.contains("Dharma-Karmadhipati") -> StringKeyMatch.YOGA_EFFECT_DHARMA_KARMADHIPATI
            yogaName.contains("Maha Bhagya") -> StringKeyMatch.YOGA_EFFECT_MAHA_BHAGYA
            yogaName.contains("Pushkala") -> StringKeyMatch.YOGA_EFFECT_PUSHAKALA
            yogaName.contains("Akhanda Samrajya") -> StringKeyMatch.YOGA_EFFECT_AKHANDA_SAMRAJYA
            yogaName.contains("Vasumathi") -> StringKeyMatch.YOGA_EFFECT_VASUMATHI
            yogaName.contains("Mahalaxmi") -> StringKeyMatch.YOGA_EFFECT_MAHALAXMI
            yogaName.contains("Parijata") -> StringKeyMatch.YOGA_EFFECT_PARIJATA
            yogaName.contains("Kusuma") -> StringKeyMatch.YOGA_EFFECT_KUSUMA
            yogaName.contains("Indu Lagna") -> StringKeyMatch.YOGA_EFFECT_INDU_LAGNA
            yogaName.contains("Sakata") -> StringKeyMatch.YOGA_EFFECT_SAKATA
            yogaName.contains("Kemadruma Bhanga") -> StringKeyMatch.YOGA_EFFECT_KEMADRUMA_BHANGA
            yogaName.contains("Vallaki") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_VALLAKI
            yogaName.contains("Damini") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_DAMINI
            yogaName.contains("Pasa") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_PASA
            yogaName.contains("Kedara") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_KEDARA
            yogaName.contains("Shoola") && yogaName.contains("Sankhya") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_SHOOLA
            yogaName.contains("Yuga") && yogaName.contains("Sankhya") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_YUGA
            yogaName.contains("Gola") && yogaName.contains("Sankhya") -> StringKeyMatch.YOGA_EFFECT_SANKHYA_GOLA
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
            else -> englishName
        }
    }
}

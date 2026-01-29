package com.astro.storm.ephemeris.spiritual

import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

/**
 * Ishta Devata and Beeja Mantra Models
 *
 * According to Jaimini Sutras and BPHS, the Ishta Devata (Personal Deity)
 * is determined by the position of Atmakaraka in Navamsa.
 *
 * Method:
 * 1. Find Atmakaraka (planet with highest degree, excluding Rahu)
 * 2. Note its Navamsa sign
 * 3. The 12th house from Navamsa AK position indicates the Ishta Devata
 * 4. The lord and occupants of this sign determine the deity
 *
 * Beeja Mantra:
 * - Based on the Nakshatra of Moon and key planets
 * - Each Nakshatra has associated seed syllables
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */

/**
 * Deity associated with planets and signs
 */
enum class Deity(
    val displayName: String,
    val sanskritName: String,
    val description: String,
    val mantra: String,
    val associatedPlanets: List<Planet>,
    val associatedSigns: List<ZodiacSign>
) {
    VISHNU(
        "Lord Vishnu",
        "विष्णु",
        "The Preserver, embodiment of Sattva guna, protector of the universe",
        "Om Namo Narayanaya",
        listOf(Planet.MERCURY, Planet.VENUS),
        listOf(ZodiacSign.TAURUS, ZodiacSign.GEMINI, ZodiacSign.VIRGO, ZodiacSign.LIBRA)
    ),
    SHIVA(
        "Lord Shiva",
        "शिव",
        "The Transformer, embodiment of Tamas transcended, lord of destruction and regeneration",
        "Om Namah Shivaya",
        listOf(Planet.SATURN, Planet.KETU),
        listOf(ZodiacSign.CAPRICORN, ZodiacSign.AQUARIUS, ZodiacSign.SCORPIO)
    ),
    DEVI_LAKSHMI(
        "Goddess Lakshmi",
        "लक्ष्मी",
        "Goddess of wealth, fortune, and prosperity, consort of Vishnu",
        "Om Shreem Mahalakshmiyei Namaha",
        listOf(Planet.VENUS, Planet.MOON),
        listOf(ZodiacSign.TAURUS, ZodiacSign.LIBRA, ZodiacSign.PISCES)
    ),
    DEVI_DURGA(
        "Goddess Durga",
        "दुर्गा",
        "The Invincible, protector against evil, embodiment of Shakti",
        "Om Dum Durgayei Namaha",
        listOf(Planet.MARS, Planet.RAHU),
        listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO)
    ),
    DEVI_SARASWATI(
        "Goddess Saraswati",
        "सरस्वती",
        "Goddess of knowledge, music, arts, and learning",
        "Om Aim Saraswatyai Namaha",
        listOf(Planet.MERCURY, Planet.JUPITER),
        listOf(ZodiacSign.GEMINI, ZodiacSign.VIRGO)
    ),
    SURYA(
        "Lord Surya",
        "सूर्य",
        "The Sun God, source of light, vitality, and soul essence",
        "Om Hraam Hreem Hraum Sah Suryaya Namaha",
        listOf(Planet.SUN),
        listOf(ZodiacSign.LEO)
    ),
    GANESHA(
        "Lord Ganesha",
        "गणेश",
        "Remover of obstacles, lord of beginnings and wisdom",
        "Om Gam Ganapataye Namaha",
        listOf(Planet.KETU, Planet.MERCURY),
        listOf(ZodiacSign.VIRGO, ZodiacSign.AQUARIUS)
    ),
    HANUMAN(
        "Lord Hanuman",
        "हनुमान",
        "Embodiment of devotion, strength, and service",
        "Om Ham Hanumate Namaha",
        listOf(Planet.MARS, Planet.SUN),
        listOf(ZodiacSign.ARIES, ZodiacSign.LEO)
    ),
    KRISHNA(
        "Lord Krishna",
        "कृष्ण",
        "The All-Attractive, avatar of Vishnu, speaker of Bhagavad Gita",
        "Hare Krishna Hare Krishna Krishna Krishna Hare Hare",
        listOf(Planet.MOON, Planet.MERCURY),
        listOf(ZodiacSign.CANCER, ZodiacSign.GEMINI)
    ),
    RAMA(
        "Lord Rama",
        "राम",
        "The Ideal Man, avatar of Vishnu, embodiment of dharma",
        "Om Sri Rama Jaya Rama Jaya Jaya Rama",
        listOf(Planet.SUN, Planet.JUPITER),
        listOf(ZodiacSign.LEO, ZodiacSign.SAGITTARIUS)
    ),
    DEVI_PARVATI(
        "Goddess Parvati",
        "पार्वती",
        "Divine Mother, consort of Shiva, goddess of fertility and devotion",
        "Om Hreem Parvati Pataye Namaha",
        listOf(Planet.MOON, Planet.VENUS),
        listOf(ZodiacSign.CANCER, ZodiacSign.TAURUS)
    ),
    KARTIKEYA(
        "Lord Kartikeya",
        "कार्तिकेय",
        "God of war, son of Shiva, commander of celestial armies",
        "Om Saravanabhavaya Namaha",
        listOf(Planet.MARS),
        listOf(ZodiacSign.ARIES, ZodiacSign.SCORPIO)
    ),
    NARASIMHA(
        "Lord Narasimha",
        "नृसिंह",
        "The Man-Lion avatar of Vishnu, protector from evil",
        "Om Namo Bhagavate Narasimhaya",
        listOf(Planet.SUN, Planet.MARS),
        listOf(ZodiacSign.LEO, ZodiacSign.ARIES)
    ),
    DATTATREYA(
        "Lord Dattatreya",
        "दत्तात्रेय",
        "Combined form of Brahma, Vishnu, and Shiva, supreme Guru",
        "Om Draam Dattatreyaya Namaha",
        listOf(Planet.JUPITER),
        listOf(ZodiacSign.SAGITTARIUS, ZodiacSign.PISCES)
    ),
    KALI(
        "Goddess Kali",
        "काली",
        "The Dark Mother, destroyer of ego and ignorance",
        "Om Kreem Kalikayei Namaha",
        listOf(Planet.SATURN, Planet.RAHU),
        listOf(ZodiacSign.SCORPIO, ZodiacSign.CAPRICORN)
    ),
    SKANDA(
        "Lord Skanda",
        "स्कन्द",
        "Another name for Kartikeya, divine warrior",
        "Om Skandaya Namaha",
        listOf(Planet.MARS),
        listOf(ZodiacSign.ARIES)
    )
}

/**
 * Beeja (Seed) Mantra for each Nakshatra
 */
enum class NakshatraBeejaMantra(
    val nakshatra: Nakshatra,
    val syllable1: String,
    val syllable2: String,
    val syllable3: String,
    val syllable4: String,
    val primaryBeeja: String,
    val deity: Deity
) {
    ASHWINI(Nakshatra.ASHWINI, "Chu", "Che", "Cho", "La", "Om Hraam", Deity.GANESHA),
    BHARANI(Nakshatra.BHARANI, "Li", "Lu", "Le", "Lo", "Om Hreem", Deity.DEVI_DURGA),
    KRITTIKA(Nakshatra.KRITTIKA, "A", "I", "U", "E", "Om Aim", Deity.SURYA),
    ROHINI(Nakshatra.ROHINI, "O", "Va", "Vi", "Vu", "Om Shreem", Deity.KRISHNA),
    MRIGASHIRA(Nakshatra.MRIGASHIRA, "Ve", "Vo", "Ka", "Ki", "Om Kleem", Deity.SHIVA),
    ARDRA(Nakshatra.ARDRA, "Ku", "Gha", "Ng", "Chha", "Om Hreem", Deity.SHIVA),
    PUNARVASU(Nakshatra.PUNARVASU, "Ke", "Ko", "Ha", "Hi", "Om Hraam", Deity.DEVI_LAKSHMI),
    PUSHYA(Nakshatra.PUSHYA, "Hu", "He", "Ho", "Da", "Om Gam", Deity.JUPITER_BRIHASPATI),
    ASHLESHA(Nakshatra.ASHLESHA, "Di", "Du", "De", "Do", "Om Hleem", Deity.VISHNU),
    MAGHA(Nakshatra.MAGHA, "Ma", "Mi", "Mu", "Me", "Om Hraam", Deity.SURYA),
    PURVA_PHALGUNI(Nakshatra.PURVA_PHALGUNI, "Mo", "Ta", "Ti", "Tu", "Om Shreem", Deity.DEVI_LAKSHMI),
    UTTARA_PHALGUNI(Nakshatra.UTTARA_PHALGUNI, "Te", "To", "Pa", "Pi", "Om Hreem", Deity.SURYA),
    HASTA(Nakshatra.HASTA, "Pu", "Sha", "Na", "Tha", "Om Gam", Deity.VISHNU),
    CHITRA(Nakshatra.CHITRA, "Pe", "Po", "Ra", "Ri", "Om Kreem", Deity.VISHWAKARMA),
    SWATI(Nakshatra.SWATI, "Ru", "Re", "Ro", "Ta", "Om Shreem", Deity.DEVI_SARASWATI),
    VISHAKHA(Nakshatra.VISHAKHA, "Ti", "Tu", "Te", "To", "Om Aim", Deity.KARTIKEYA),
    ANURADHA(Nakshatra.ANURADHA, "Na", "Ni", "Nu", "Ne", "Om Hreem", Deity.VISHNU),
    JYESHTHA(Nakshatra.JYESHTHA, "No", "Ya", "Yi", "Yu", "Om Kleem", Deity.VISHNU),
    MULA(Nakshatra.MULA, "Ye", "Yo", "Bha", "Bhi", "Om Hreem", Deity.KALI),
    PURVA_ASHADHA(Nakshatra.PURVA_ASHADHA, "Bhu", "Dha", "Pha", "Da", "Om Shreem", Deity.DEVI_LAKSHMI),
    UTTARA_ASHADHA(Nakshatra.UTTARA_ASHADHA, "Be", "Bo", "Ja", "Ji", "Om Hraam", Deity.VISHNU),
    SHRAVANA(Nakshatra.SHRAVANA, "Ju", "Je", "Jo", "Gha", "Om Shreem", Deity.VISHNU),
    DHANISHTHA(Nakshatra.DHANISHTHA, "Ga", "Gi", "Gu", "Ge", "Om Kleem", Deity.HANUMAN),
    SHATABHISHA(Nakshatra.SHATABHISHA, "Go", "Sa", "Si", "Su", "Om Hreem", Deity.SHIVA),
    PURVA_BHADRAPADA(Nakshatra.PURVA_BHADRAPADA, "Se", "So", "Da", "Di", "Om Hraam", Deity.SHIVA),
    UTTARA_BHADRAPADA(Nakshatra.UTTARA_BHADRAPADA, "Du", "Tha", "Jha", "Da", "Om Shreem", Deity.SHIVA),
    REVATI(Nakshatra.REVATI, "De", "Do", "Cha", "Chi", "Om Hleem", Deity.VISHNU);

    companion object {
        fun fromNakshatra(nakshatra: Nakshatra): NakshatraBeejaMantra? {
            return entries.find { it.nakshatra == nakshatra }
        }
    }
}

/**
 * Special deity for Jupiter's nakshatra
 */
private val JUPITER_BRIHASPATI = Deity.DATTATREYA // Jupiter represents Brihaspati/Guru

/**
 * Special deity for Vishwakarma
 */
private val VISHWAKARMA = Deity.VISHNU // Chitra's deity

/**
 * Karakamsha analysis result
 */
data class KarakamshaResult(
    val atmakaraka: Planet,
    val atmakarakaDegree: Double,
    val atmakarakaSign: ZodiacSign,
    val navamsaSign: ZodiacSign,
    val karakamshaSign: ZodiacSign,
    val ishtaDevataSign: ZodiacSign, // 12th from Karakamsha
    val ishtaDevataLord: Planet,
    val occupantsInIshtaDevataSign: List<Planet>,
    val determinedDeity: Deity,
    val alternativeDeities: List<Deity>,
    val explanation: String
)

/**
 * Beeja Mantra result
 */
data class BeejaMantraResult(
    val moonNakshatra: Nakshatra,
    val moonPada: Int,
    val nameSyllable: String,
    val primaryBeejaMantra: String,
    val relatedDeity: Deity,
    val additionalMantras: List<String>,
    val explanation: String
)

/**
 * Complete spiritual analysis result
 */
data class SpiritualAnalysisResult(
    val karakamshaAnalysis: KarakamshaResult,
    val beejaMantraAnalysis: BeejaMantraResult,
    val recommendedPractices: List<SpiritualPractice>,
    val auspiciousDays: List<AuspiciousDay>,
    val gemstoneRecommendation: GemstoneRecommendation?,
    val fullReport: String
)

/**
 * Spiritual practice recommendation
 */
data class SpiritualPractice(
    val practiceName: String,
    val description: String,
    val frequency: String,
    val bestTime: String,
    val mantra: String
)

/**
 * Auspicious day for worship
 */
data class AuspiciousDay(
    val dayName: String,
    val reason: String,
    val suggestedPractice: String
)

/**
 * Gemstone recommendation based on deity
 */
data class GemstoneRecommendation(
    val primaryGemstone: String,
    val alternativeGemstones: List<String>,
    val metal: String,
    val finger: String,
    val weight: String,
    val energizationMantra: String
)

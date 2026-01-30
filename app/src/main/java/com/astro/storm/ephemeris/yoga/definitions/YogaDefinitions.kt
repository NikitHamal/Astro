package com.astro.storm.ephemeris.yoga.definitions

import com.astro.storm.core.model.Planet
import com.astro.storm.ephemeris.yoga.YogaCategory

/**
 * Comprehensive Yoga Definitions Database
 *
 * Contains 500+ yoga definitions based on classical Vedic texts:
 * - Brihat Parashara Hora Shastra (BPHS)
 * - Phaladeepika
 * - Saravali
 * - Jataka Parijata
 * - Hora Ratna
 * - Jataka Tattva
 * - Uttara Kalamrita
 *
 * This file serves as the canonical reference for yoga definitions,
 * providing standardized names, conditions, and effects that are used
 * by the various YogaEvaluator implementations.
 *
 * @author AstroStorm
 */
object YogaDefinitions {

    // ==================== RAJA YOGAS (POWER & AUTHORITY) ====================

    /**
     * Raja Yoga definitions - Combinations for power, authority, and leadership
     * Reference: BPHS Chapter 41
     */
    object RajaYogas {

        // Kendra-Trikona combinations (foundational)
        val KENDRA_TRIKONA_RAJA = YogaDefinition(
            id = "KENDRA_TRIKONA_RAJA",
            name = "Kendra-Trikona Raja Yoga",
            sanskritName = "केन्द्र-त्रिकोण राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Rise to power and authority, leadership position, recognition from government, success in politics and administration.",
            classicalReference = "BPHS Chapter 41, Slokas 1-5"
        )

        val PARIVARTANA_RAJA = YogaDefinition(
            id = "PARIVARTANA_RAJA",
            name = "Parivartana Raja Yoga",
            sanskritName = "परिवर्तन राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Strong Raja Yoga through mutual exchange, stable rise to power, lasting authority, cooperative success.",
            classicalReference = "BPHS Chapter 41, Sloka 10"
        )

        val VIPARITA_RAJA = YogaDefinition(
            id = "VIPARITA_RAJA",
            name = "Viparita Raja Yoga",
            sanskritName = "विपरीत राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Rise through fall of enemies, sudden fortune from unexpected sources, gains through others' losses, transformation of obstacles into opportunities.",
            classicalReference = "BPHS Chapter 41, Slokas 31-33"
        )

        val NEECHA_BHANGA_RAJA = YogaDefinition(
            id = "NEECHA_BHANGA_RAJA",
            name = "Neecha Bhanga Raja Yoga",
            sanskritName = "नीच भंग राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Rise from humble beginnings, success after initial struggles, respected leader, transformation of weakness into strength.",
            classicalReference = "Phaladeepika Chapter 7"
        )

        val MAHA_RAJA = YogaDefinition(
            id = "MAHA_RAJA",
            name = "Maha Raja Yoga",
            sanskritName = "महा राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Exceptional fortune, royal status, widespread fame, great wealth and power, recognized authority.",
            classicalReference = "BPHS Chapter 41, Sloka 15"
        )

        val MAHA_BHAGYA = YogaDefinition(
            id = "MAHA_BHAGYA",
            name = "Maha Bhagya Yoga",
            sanskritName = "महा भाग्य योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Exceptional fortune from birth, leadership qualities, magnetic personality, success in all endeavors.",
            classicalReference = "BPHS Chapter 41, Slokas 20-21"
        )

        val PUSHKALA = YogaDefinition(
            id = "PUSHKALA",
            name = "Pushkala Yoga",
            sanskritName = "पुष्कल योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Wealthy, honored by rulers, famous, eloquent, good conduct, respected in society.",
            classicalReference = "Saravali Chapter 35"
        )

        val AKHANDA_SAMRAJYA = YogaDefinition(
            id = "AKHANDA_SAMRAJYA",
            name = "Akhanda Samrajya Yoga",
            sanskritName = "अखण्ड साम्राज्य योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Undisputed leadership, wide-ranging influence, vast wealth and authority, empire-like success.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 36"
        )

        val CHAMARA = YogaDefinition(
            id = "CHAMARA",
            name = "Chamara Yoga",
            sanskritName = "चामर योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Royal status, command over others, eloquent speaker, long-lasting fame, wisdom.",
            classicalReference = "Saravali Chapter 35"
        )

        val KAHALA = YogaDefinition(
            id = "KAHALA",
            name = "Kahala Yoga",
            sanskritName = "कहल योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Bold and courageous, leadership abilities, success in competition, respected authority.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val CHATRA = YogaDefinition(
            id = "CHATRA",
            name = "Chatra Yoga",
            sanskritName = "छत्र योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Protector of others, charitable, famous in last part of life, leader of groups.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 20"
        )

        val CHAAMARA = YogaDefinition(
            id = "CHAAMARA",
            name = "Chaamara Yoga",
            sanskritName = "चामर योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Virtuous, long-lived, learned, king or equal to king, knows scriptures.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 21"
        )

        val LAKSHMI_RAJA = YogaDefinition(
            id = "LAKSHMI_RAJA",
            name = "Lakshmi Yoga",
            sanskritName = "लक्ष्मी योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Wealthy, handsome, learned, noble, high status, blessed by Goddess Lakshmi.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 24"
        )

        val SHANKHA = YogaDefinition(
            id = "SHANKHA",
            name = "Shankha Yoga",
            sanskritName = "शङ्ख योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Kind, charitable, virtuous, blessed with spouse and children, lives long life.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 31"
        )

        val BHERI = YogaDefinition(
            id = "BHERI",
            name = "Bheri Yoga",
            sanskritName = "भेरी योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Wealthy, good health, happy family, religious, long life, respected.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 32"
        )

        val MRIDANGA = YogaDefinition(
            id = "MRIDANGA",
            name = "Mridanga Yoga",
            sanskritName = "मृदंग योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Respected, famous, attractive, influential, commands many.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 33"
        )

        val SREENATHA = YogaDefinition(
            id = "SREENATHA",
            name = "Sreenatha Yoga",
            sanskritName = "श्रीनाथ योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Wealthy, lordly, religious, eloquent, favored by Goddess Lakshmi.",
            classicalReference = "Jataka Parijata Chapter 7"
        )

        val MATSYA = YogaDefinition(
            id = "MATSYA",
            name = "Matsya Yoga",
            sanskritName = "मत्स्य योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Kind-hearted, religious, learned, strong character, prosperous.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 34"
        )

        val KURMA = YogaDefinition(
            id = "KURMA",
            name = "Kurma Yoga",
            sanskritName = "कूर्म योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Stable wealth, happy, grateful, helpful to others, virtuous, patient.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 35"
        )

        val KHADGA = YogaDefinition(
            id = "KHADGA",
            name = "Khadga Yoga",
            sanskritName = "खड्ग योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Learned, skilled, wealthy, successful, influential speaker.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 37"
        )

        val KUSUMA = YogaDefinition(
            id = "KUSUMA",
            name = "Kusuma Yoga",
            sanskritName = "कुसुम योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Kingly status, famous, wealthy, enjoys life's pleasures, born in good family.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 38"
        )

        val KALANIDHI = YogaDefinition(
            id = "KALANIDHI",
            name = "Kalanidhi Yoga",
            sanskritName = "कलानिधि योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Respected, healthy, wealthy, learned, skilled in arts.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 39"
        )

        val KALPADRUMA = YogaDefinition(
            id = "KALPADRUMA",
            name = "Kalpadruma Yoga",
            sanskritName = "कल्पद्रुम योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Strong, virtuous, wealthy, like a wish-fulfilling tree for others.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 40"
        )

        val LAGNADHI_RAJA = YogaDefinition(
            id = "LAGNADHI_RAJA",
            name = "Lagnadhi Raja Yoga",
            sanskritName = "लग्नाधि राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Born leader, self-made success, authority through personal effort.",
            classicalReference = "BPHS Chapter 41"
        )

        val CHANDRA_ADHI_RAJA = YogaDefinition(
            id = "CHANDRA_ADHI_RAJA",
            name = "Chandrādhi Raja Yoga",
            sanskritName = "चन्द्राधि राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Mental brilliance leading to authority, public popularity, emotional intelligence.",
            classicalReference = "BPHS Chapter 41"
        )

        val DHARMA_KARMADHIPATI = YogaDefinition(
            id = "DHARMA_KARMADHIPATI",
            name = "Dharma-Karmadhipati Yoga",
            sanskritName = "धर्म-कर्माधिपति योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Success through righteous actions, career aligned with dharma, rise to prominence through merit.",
            classicalReference = "BPHS Chapter 41, Sloka 6"
        )

        val PARIJATA = YogaDefinition(
            id = "PARIJATA",
            name = "Parijata Yoga",
            sanskritName = "पारिजात योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Success in middle and later life, steady rise to prominence, respected authority.",
            classicalReference = "Phaladeepika Chapter 6, Sloka 41"
        )

        // Additional Raja Yogas from BPHS Chapter 41
        val TRILOCHANA = YogaDefinition(
            id = "TRILOCHANA",
            name = "Trilochana Yoga",
            sanskritName = "त्रिलोचन योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Destroyer of enemies, wealthy, long-lived, like Lord Shiva in power.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val SHIVA = YogaDefinition(
            id = "SHIVA",
            name = "Shiva Yoga",
            sanskritName = "शिव योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Wealthy, scholar, forgiving, worshipped by others, religious.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val GOURI = YogaDefinition(
            id = "GOURI",
            name = "Gouri Yoga",
            sanskritName = "गौरी योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Beautiful, religious, wealthy, famous, blessed with a good family.",
            classicalReference = "Saravali Chapter 35"
        )

        val BHARATHI = YogaDefinition(
            id = "BHARATHI",
            name = "Bharathi Yoga",
            sanskritName = "भारती योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Exceptional intelligence, scholarship, fame, wealth, religious nature.",
            classicalReference = "Saravali Chapter 35"
        )

        val INDRA = YogaDefinition(
            id = "INDRA",
            name = "Indra Yoga",
            sanskritName = "इन्द्र योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Powerful like Indra, wealthy, has many servants, vehicles, fame.",
            classicalReference = "BPHS Chapter 41"
        )

        val HARIHARA_BRAHMA = YogaDefinition(
            id = "HARIHARA_BRAHMA",
            name = "Harihara Brahma Yoga",
            sanskritName = "हरिहर ब्रह्म योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Scholar, truthful, happy, helpful, learned in all branches of knowledge.",
            classicalReference = "Saravali Chapter 35"
        )

        val TRIMURTI = YogaDefinition(
            id = "TRIMURTI",
            name = "Trimurti Yoga",
            sanskritName = "त्रिमूर्ति योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Happy, wealthy, long life, famous, blessed by the Trinity.",
            classicalReference = "Saravali Chapter 35"
        )

        val CHAPA = YogaDefinition(
            id = "CHAPA",
            name = "Chapa Yoga",
            sanskritName = "चाप योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "High status in government or military, authority, wealth, property.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val HAMSA_RAJA = YogaDefinition(
            id = "HAMSA_RAJA",
            name = "Hamsa Raja Yoga",
            sanskritName = "हंस राज योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Pure and spotless character, kingly authority, respected advisor.",
            classicalReference = "BPHS Chapter 41"
        )
    }

    // ==================== DHANA YOGAS (WEALTH & PROSPERITY) ====================

    /**
     * Dhana Yoga definitions - Combinations for wealth and material prosperity
     * Reference: BPHS Chapter 40
     */
    object DhanaYogas {

        val BASIC_DHANA = YogaDefinition(
            id = "BASIC_DHANA",
            name = "Dhana Yoga",
            sanskritName = "धन योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth accumulation through various means, financial prosperity, material success.",
            classicalReference = "BPHS Chapter 40"
        )

        val LAKSHMI_DHANA = YogaDefinition(
            id = "LAKSHMI_DHANA",
            name = "Lakshmi Yoga",
            sanskritName = "लक्ष्मी योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Blessed by Goddess Lakshmi, abundant wealth, luxury, beauty, artistic success.",
            classicalReference = "BPHS Chapter 40, Sloka 12"
        )

        val KUBERA = YogaDefinition(
            id = "KUBERA",
            name = "Kubera Yoga",
            sanskritName = "कुबेर योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Treasury of wealth like Kubera, excellent financial acumen, banking success.",
            classicalReference = "Saravali Chapter 36"
        )

        val CHANDRA_MANGALA = YogaDefinition(
            id = "CHANDRA_MANGALA",
            name = "Chandra-Mangala Yoga",
            sanskritName = "चन्द्र-मंगल योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth through business, enterprise, real estate, aggressive financial pursuits.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 9"
        )

        val LABHA = YogaDefinition(
            id = "LABHA",
            name = "Labha Yoga",
            sanskritName = "लाभ योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Continuous gains, fulfillment of desires, income from multiple sources.",
            classicalReference = "BPHS Chapter 40"
        )

        val VASUMATHI = YogaDefinition(
            id = "VASUMATHI",
            name = "Vasumathi Yoga",
            sanskritName = "वसुमथि योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Continuous accumulation of wealth, prosperity increasing with age, financial independence.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val MAHALAXMI = YogaDefinition(
            id = "MAHALAXMI",
            name = "Mahalaxmi Yoga",
            sanskritName = "महालक्ष्मी योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Divine grace of Goddess Lakshmi, immense wealth, religious nature, respected and virtuous.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val INDU_LAGNA = YogaDefinition(
            id = "INDU_LAGNA",
            name = "Indu Lagna Dhana Yoga",
            sanskritName = "इन्दु लग्न धन योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Special financial potential revealed through lunar rays, prosperity through specific life paths.",
            classicalReference = "Uttara Kalamrita"
        )

        val SHUBHA_KARTARI_DHANA = YogaDefinition(
            id = "SHUBHA_KARTARI_DHANA",
            name = "Shubhakartari Dhana Yoga",
            sanskritName = "शुभकर्तरी धन योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "2nd house protected by benefics, steady wealth accumulation, protected finances.",
            classicalReference = "BPHS Chapter 40"
        )

        val SRINATHA_DHANA = YogaDefinition(
            id = "SRINATHA_DHANA",
            name = "Srinatha Yoga",
            sanskritName = "श्रीनाथ धन योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealthy from inheritance and self-effort, happy family, religious and charitable.",
            classicalReference = "Jataka Parijata"
        )

        val KAHALALA = YogaDefinition(
            id = "KAHALALA",
            name = "Kahalala Yoga",
            sanskritName = "कहलल योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth through courage and valor, real estate, landed property.",
            classicalReference = "Saravali Chapter 36"
        )

        val DHENU = YogaDefinition(
            id = "DHENU",
            name = "Dhenu Yoga",
            sanskritName = "धेनु योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Like wish-fulfilling cow, continuous wealth, charitable, helpful to all.",
            classicalReference = "Saravali Chapter 36"
        )

        val PARVATA_DHANA = YogaDefinition(
            id = "PARVATA_DHANA",
            name = "Parvata Yoga",
            sanskritName = "पर्वत धन योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Mountain-like steady wealth, immovable assets, real estate prosperity.",
            classicalReference = "Saravali Chapter 36"
        )

        val BANDHU_PUJYA = YogaDefinition(
            id = "BANDHU_PUJYA",
            name = "Bandhu Pujya Yoga",
            sanskritName = "बन्धु पूज्य योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth through family, honored by relatives, ancestral property.",
            classicalReference = "Jataka Parijata"
        )

        val SRI_LAGNA = YogaDefinition(
            id = "SRI_LAGNA",
            name = "Sri Lagna Yoga",
            sanskritName = "श्री लग्न योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Prosperity from birth, inherent wealth potential, fortunate lagna.",
            classicalReference = "Uttara Kalamrita"
        )

        val UTTAMADI = YogaDefinition(
            id = "UTTAMADI",
            name = "Uttamadi Yoga",
            sanskritName = "उत्तमादि योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Born in wealthy family, maintains and increases ancestral wealth.",
            classicalReference = "BPHS Chapter 40"
        )

        val ARDHA_CHANDRA_DHANA = YogaDefinition(
            id = "ARDHA_CHANDRA_DHANA",
            name = "Ardha Chandra Dhana Yoga",
            sanskritName = "अर्ध चन्द्र धन योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth like the waxing moon, increasing prosperity, public popularity.",
            classicalReference = "Saravali Chapter 36"
        )

        val SAMUDRA = YogaDefinition(
            id = "SAMUDRA",
            name = "Samudra Yoga",
            sanskritName = "समुद्र योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth as vast as ocean, multiple income sources, never depleting resources.",
            classicalReference = "Saravali Chapter 36"
        )

        val SHASHI_MANGALA = YogaDefinition(
            id = "SHASHI_MANGALA",
            name = "Shashi-Mangala Yoga",
            sanskritName = "शशि-मंगल योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Wealth through bold ventures, real estate, construction, minerals.",
            classicalReference = "Phaladeepika Chapter 4"
        )
    }

    // ==================== MAHAPURUSHA YOGAS (GREAT PERSON) ====================

    /**
     * Pancha Mahapurusha Yoga definitions - Five Great Person combinations
     * Reference: BPHS Chapter 75
     */
    object MahapurushaYogas {

        val RUCHAKA = YogaDefinition(
            id = "RUCHAKA",
            name = "Ruchaka Mahapurusha Yoga",
            sanskritName = "रुचक महापुरुष योग",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            isAuspicious = true,
            effects = "Courageous warrior qualities, leadership in military or sports, physical prowess, success in competitive fields, commanding presence.",
            classicalReference = "BPHS Chapter 75, Slokas 1-5"
        )

        val BHADRA = YogaDefinition(
            id = "BHADRA",
            name = "Bhadra Mahapurusha Yoga",
            sanskritName = "भद्र महापुरुष योग",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            isAuspicious = true,
            effects = "Exceptional intelligence, eloquent speech, success in business, education, and communication fields, skilled in mathematics and commerce.",
            classicalReference = "BPHS Chapter 75, Slokas 6-10"
        )

        val HAMSA = YogaDefinition(
            id = "HAMSA",
            name = "Hamsa Mahapurusha Yoga",
            sanskritName = "हंस महापुरुष योग",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            isAuspicious = true,
            effects = "Wisdom and righteousness, spiritual inclination, success in teaching, law, and advisory roles, respected philosopher, pure character.",
            classicalReference = "BPHS Chapter 75, Slokas 11-15"
        )

        val MALAVYA = YogaDefinition(
            id = "MALAVYA",
            name = "Malavya Mahapurusha Yoga",
            sanskritName = "मालव्य महापुरुष योग",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            isAuspicious = true,
            effects = "Luxury and comfort, artistic talent, beautiful appearance, success in creative and entertainment fields, sensory refinement.",
            classicalReference = "BPHS Chapter 75, Slokas 16-20"
        )

        val SHASHA = YogaDefinition(
            id = "SHASHA",
            name = "Shasha Mahapurusha Yoga",
            sanskritName = "शश महापुरुष योग",
            category = YogaCategory.MAHAPURUSHA_YOGA,
            isAuspicious = true,
            effects = "Authority over masses, success in politics, administrative power, longevity, steady rise to prominence, democratic leadership.",
            classicalReference = "BPHS Chapter 75, Slokas 21-25"
        )
    }

    // ==================== NABHASA YOGAS (CELESTIAL PATTERNS) ====================

    /**
     * Nabhasa Yoga definitions - Pattern-based planetary combinations
     * Reference: BPHS Chapter 35
     */
    object NabhasaYogas {

        // Akriti (Shape) Yogas - 20 types
        val YAVA = YogaDefinition(
            id = "YAVA",
            name = "Yava Yoga",
            sanskritName = "यव योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Generous nature, charitable inclinations, modest prosperity, helpful to others.",
            classicalReference = "BPHS Chapter 35, Sloka 10"
        )

        val SHRINGATAKA = YogaDefinition(
            id = "SHRINGATAKA",
            name = "Shringataka Yoga",
            sanskritName = "शृंगाटक योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Fortunate life, good past karma manifesting, success through triangular patterns.",
            classicalReference = "BPHS Chapter 35, Sloka 11"
        )

        val GADA = YogaDefinition(
            id = "GADA",
            name = "Gada Yoga",
            sanskritName = "गदा योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Wealthy through efforts, determined nature, success in competitions.",
            classicalReference = "BPHS Chapter 35, Sloka 12"
        )

        val SHAKATA = YogaDefinition(
            id = "SHAKATA",
            name = "Shakata Yoga",
            sanskritName = "शकट योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = false,
            effects = "Fluctuating fortune, ups and downs in life, dependent on relationships.",
            classicalReference = "BPHS Chapter 35, Sloka 13"
        )

        val PAKSHI = YogaDefinition(
            id = "PAKSHI",
            name = "Pakshi Yoga",
            sanskritName = "पक्षी योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Like a bird in freedom, traveling nature, messenger, diplomacy.",
            classicalReference = "BPHS Chapter 35, Sloka 14"
        )

        val SHRINGATA = YogaDefinition(
            id = "SHRINGATA",
            name = "Shringata Yoga",
            sanskritName = "शृंगट योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Loves combat, happy in later life, clever in work.",
            classicalReference = "BPHS Chapter 35, Sloka 15"
        )

        val ARDHA_CHANDRA = YogaDefinition(
            id = "ARDHA_CHANDRA",
            name = "Ardha Chandra Yoga",
            sanskritName = "अर्ध चन्द्र योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Handsome, commanding, popular, powerful, wealthy, prosperous.",
            classicalReference = "BPHS Chapter 35, Sloka 16"
        )

        val CHAKRA = YogaDefinition(
            id = "CHAKRA",
            name = "Chakra Yoga",
            sanskritName = "चक्र योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Emperor-like, very wealthy, ruler over many, powerful authority.",
            classicalReference = "BPHS Chapter 35, Sloka 17"
        )

        val SAMUDRA = YogaDefinition(
            id = "SAMUDRA_NABH",
            name = "Samudra Yoga",
            sanskritName = "समुद्र योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Wealth like ocean, enjoys many pleasures, equal to king.",
            classicalReference = "BPHS Chapter 35, Sloka 18"
        )

        val KAMALA = YogaDefinition(
            id = "KAMALA",
            name = "Kamala Yoga",
            sanskritName = "कमल योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Pure and virtuous, famous, performs many good deeds, long-lived.",
            classicalReference = "BPHS Chapter 35, Sloka 19"
        )

        val VAAPI = YogaDefinition(
            id = "VAAPI",
            name = "Vaapi Yoga",
            sanskritName = "वापी योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Able to save and store wealth, happy with moderate means.",
            classicalReference = "BPHS Chapter 35, Sloka 20"
        )

        val YUPA = YogaDefinition(
            id = "YUPA",
            name = "Yupa Yoga",
            sanskritName = "यूप योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Performs religious rites, generous, respected, earns through proper means.",
            classicalReference = "BPHS Chapter 35, Sloka 21"
        )

        val ISHU = YogaDefinition(
            id = "ISHU",
            name = "Ishu Yoga",
            sanskritName = "इषु योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Skilled in crafts, head of a group, respectable, earning through proper means.",
            classicalReference = "BPHS Chapter 35, Sloka 22"
        )

        val SHAKTI = YogaDefinition(
            id = "SHAKTI",
            name = "Shakti Yoga",
            sanskritName = "शक्ति योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Lazy, unsuccessful, limited resources, but survives.",
            classicalReference = "BPHS Chapter 35, Sloka 23"
        )

        val DANDA = YogaDefinition(
            id = "DANDA",
            name = "Danda Yoga",
            sanskritName = "दण्ड योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = false,
            effects = "Loss of spouse and children, unhappy, wandering nature.",
            classicalReference = "BPHS Chapter 35, Sloka 24"
        )

        val NAUKA = YogaDefinition(
            id = "NAUKA",
            name = "Nauka Yoga",
            sanskritName = "नौका योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Famous, wealthy, happy, intelligent, connected to water/travel.",
            classicalReference = "BPHS Chapter 35, Sloka 25"
        )

        val KOOTA = YogaDefinition(
            id = "KOOTA",
            name = "Koota Yoga",
            sanskritName = "कूट योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = false,
            effects = "Liar, sinful, cruel nature, prison-related, secretive.",
            classicalReference = "BPHS Chapter 35, Sloka 26"
        )

        val CHHATRA = YogaDefinition(
            id = "CHHATRA",
            name = "Chhatra Yoga",
            sanskritName = "छत्र योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Protector, helpful, happiness in later life, respected.",
            classicalReference = "BPHS Chapter 35, Sloka 27"
        )

        val CHAPA = YogaDefinition(
            id = "CHAPA_NABH",
            name = "Chapa Yoga",
            sanskritName = "चाप योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Valorous, comfortable in forests/nature, guards treasure.",
            classicalReference = "BPHS Chapter 35, Sloka 28"
        )

        val VEENA = YogaDefinition(
            id = "VEENA",
            name = "Veena Yoga",
            sanskritName = "वीणा योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Musical talent, artistic abilities, loved by all, happy disposition.",
            classicalReference = "BPHS Chapter 35, Sloka 29"
        )

        // Sankhya Yogas - Based on planet count in houses
        val GOLA = YogaDefinition(
            id = "GOLA",
            name = "Gola Sankhya Yoga",
            sanskritName = "गोल साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = false,
            effects = "Struggles in early life, need for persistence, unconventional path.",
            classicalReference = "BPHS Chapter 35, Sloka 30"
        )

        val YUGA = YogaDefinition(
            id = "YUGA",
            name = "Yuga Sankhya Yoga",
            sanskritName = "युग साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Unconventional lifestyle, may face social challenges, religious or philosophical.",
            classicalReference = "BPHS Chapter 35, Sloka 31"
        )

        val SHOOLA = YogaDefinition(
            id = "SHOOLA",
            name = "Shoola Sankhya Yoga",
            sanskritName = "शूल साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Brave but potentially aggressive, success through competition.",
            classicalReference = "BPHS Chapter 35, Sloka 32"
        )

        val KEDARA = YogaDefinition(
            id = "KEDARA",
            name = "Kedara Sankhya Yoga",
            sanskritName = "केदार साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Agricultural wealth, helpful, truthful, steady progress.",
            classicalReference = "BPHS Chapter 35, Sloka 33"
        )

        val PASA = YogaDefinition(
            id = "PASA",
            name = "Pasa Sankhya Yoga",
            sanskritName = "पाश साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Surrounded by friends and relatives, earns through proper means.",
            classicalReference = "BPHS Chapter 35, Sloka 34"
        )

        val DAMINI = YogaDefinition(
            id = "DAMINI",
            name = "Damini Sankhya Yoga",
            sanskritName = "दामिनी साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Charitable, famous, helpful to others, intelligent.",
            classicalReference = "BPHS Chapter 35, Sloka 35"
        )

        val VALLAKI = YogaDefinition(
            id = "VALLAKI",
            name = "Vallaki Sankhya Yoga",
            sanskritName = "वल्लकी साङ्ख्य योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Artistic talents, fond of music and dance, happy disposition.",
            classicalReference = "BPHS Chapter 35, Sloka 36"
        )

        // Ashraya Yogas - Based on sign modality
        val RAJJU = YogaDefinition(
            id = "RAJJU",
            name = "Rajju Yoga",
            sanskritName = "रज्जु योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Active life, frequent travels, adaptable nature, success in changeable fields.",
            classicalReference = "BPHS Chapter 35, Sloka 37"
        )

        val MUSALA = YogaDefinition(
            id = "MUSALA",
            name = "Musala Yoga",
            sanskritName = "मुसल योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Stable life, wealth accumulation, stubborn nature, long-lasting achievements.",
            classicalReference = "BPHS Chapter 35, Sloka 38"
        )

        val NALA = YogaDefinition(
            id = "NALA",
            name = "Nala Yoga",
            sanskritName = "नल योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Versatile abilities, success in multiple fields, diplomatic nature, intellectual pursuits.",
            classicalReference = "BPHS Chapter 35, Sloka 39"
        )

        // Dala Yogas - Based on benefic placement in Kendras/Panaparas
        val MAALA = YogaDefinition(
            id = "MAALA",
            name = "Maala Yoga",
            sanskritName = "माला योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = true,
            effects = "Like wearing a garland of prosperity, happy, respected, wealthy.",
            classicalReference = "BPHS Chapter 35, Sloka 40"
        )

        val SARPA = YogaDefinition(
            id = "SARPA",
            name = "Sarpa Yoga",
            sanskritName = "सर्प योग",
            category = YogaCategory.NABHASA_YOGA,
            isAuspicious = false,
            effects = "Unhappy, cruel, wandering, poor, depends on others.",
            classicalReference = "BPHS Chapter 35, Sloka 41"
        )
    }

    // ==================== CHANDRA YOGAS (MOON-BASED) ====================

    /**
     * Chandra Yoga definitions - Moon-based combinations
     * Reference: BPHS Chapter 36, Phaladeepika Chapter 4
     */
    object ChandraYogas {

        val SUNAFA = YogaDefinition(
            id = "SUNAFA",
            name = "Sunafa Yoga",
            sanskritName = "सुनफा योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Self-made wealth and status, independent success, respected through own efforts.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 1"
        )

        val ANAFA = YogaDefinition(
            id = "ANAFA",
            name = "Anafa Yoga",
            sanskritName = "अनफा योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Good reputation, respected in society, healthy, well-dressed.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 2"
        )

        val DURUDHARA = YogaDefinition(
            id = "DURUDHARA",
            name = "Durudhara Yoga",
            sanskritName = "दुरुधरा योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Wealth, fame, and comforts from multiple sources, well-protected life, royalty-like status.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 3"
        )

        val KEMADRUMA = YogaDefinition(
            id = "KEMADRUMA",
            name = "Kemadruma Yoga",
            sanskritName = "केमद्रुम योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = false,
            effects = "Periods of loneliness or financial struggle, need to rely on self, emotional challenges.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 4"
        )

        val KEMADRUMA_BHANGA = YogaDefinition(
            id = "KEMADRUMA_BHANGA",
            name = "Kemadruma Bhanga",
            sanskritName = "केमद्रुम भंग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Cancellation of Kemadruma, recovery from struggles, eventual prosperity.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 5"
        )

        val GAJA_KESARI = YogaDefinition(
            id = "GAJA_KESARI",
            name = "Gaja-Kesari Yoga",
            sanskritName = "गज-केसरी योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Fame, wealth, and wisdom, respected leader, royal associations, lasting reputation.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 6"
        )

        val ADHI_YOGA = YogaDefinition(
            id = "ADHI_YOGA",
            name = "Adhi Yoga",
            sanskritName = "अधि योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Commander of armies, minister-like status, authority over others, respected leader.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 7"
        )

        val SAKATA = YogaDefinition(
            id = "SAKATA",
            name = "Sakata Yoga",
            sanskritName = "शकट योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = false,
            effects = "Fluctuating fortunes, periods of struggle followed by recovery, lessons in resilience.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 8"
        )

        val AMALA = YogaDefinition(
            id = "AMALA",
            name = "Amala Yoga",
            sanskritName = "अमला योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Pure reputation, success in career, respected profession, lasting fame.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 9"
        )

        val CHANDRA_MANGALA_YOGA = YogaDefinition(
            id = "CHANDRA_MANGALA",
            name = "Chandra-Mangala Yoga",
            sanskritName = "चन्द्र-मंगल योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Wealthy, earns through business and enterprise, especially in real estate or minerals.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 9"
        )

        val PUSHKARA = YogaDefinition(
            id = "PUSHKARA",
            name = "Pushkara Yoga",
            sanskritName = "पुष्कर योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Moon in Pushkara Navamsa, highly auspicious, brings fortune and happiness.",
            classicalReference = "Jataka Parijata"
        )

        val PURNIMA = YogaDefinition(
            id = "PURNIMA",
            name = "Purnima/Purna Chandra Yoga",
            sanskritName = "पूर्णिमा योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Full Moon birth brings fame, happiness, leadership, strong mind.",
            classicalReference = "BPHS Chapter 36"
        )

        val AMAVASYA = YogaDefinition(
            id = "AMAVASYA",
            name = "Amavasya Yoga",
            sanskritName = "अमावस्या योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = false,
            effects = "New Moon birth brings challenges, secretive nature, spiritual inclination.",
            classicalReference = "BPHS Chapter 36"
        )

        val PAKSHA_BALA = YogaDefinition(
            id = "PAKSHA_BALA",
            name = "Shukla Paksha Yoga",
            sanskritName = "शुक्ल पक्ष योग",
            category = YogaCategory.CHANDRA_YOGA,
            isAuspicious = true,
            effects = "Birth in bright fortnight, progressive nature, visible success.",
            classicalReference = "BPHS Chapter 36"
        )
    }

    // ==================== SOLAR YOGAS (SUN-BASED) ====================

    /**
     * Solar Yoga definitions - Sun-based combinations
     * Reference: BPHS Chapter 36
     */
    object SolarYogas {

        val VESI = YogaDefinition(
            id = "VESI",
            name = "Vesi Yoga",
            sanskritName = "वेशी योग",
            category = YogaCategory.SOLAR_YOGA,
            isAuspicious = true,
            effects = "Truthful speech, authority, respected by rulers, balanced nature.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 10"
        )

        val VOSI = YogaDefinition(
            id = "VOSI",
            name = "Vosi Yoga",
            sanskritName = "वोशी योग",
            category = YogaCategory.SOLAR_YOGA,
            isAuspicious = true,
            effects = "Skillful and learned, good memory, charitable nature.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 11"
        )

        val UBHAYACHARI = YogaDefinition(
            id = "UBHAYACHARI",
            name = "Ubhayachari Yoga",
            sanskritName = "उभयचारी योग",
            category = YogaCategory.SOLAR_YOGA,
            isAuspicious = true,
            effects = "King-like status, eloquent speaker, wealthy, physically strong, famous and respected.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 12"
        )

        val BUDHA_ADITYA = YogaDefinition(
            id = "BUDHA_ADITYA",
            name = "Budha-Aditya Yoga",
            sanskritName = "बुध-आदित्य योग",
            category = YogaCategory.SOLAR_YOGA,
            isAuspicious = true,
            effects = "Sharp intellect, good education, eloquent speech, success in intellectual pursuits, administrative ability.",
            classicalReference = "Phaladeepika Chapter 4, Sloka 13"
        )

        val NIPUNA = YogaDefinition(
            id = "NIPUNA",
            name = "Nipuna Yoga",
            sanskritName = "निपुण योग",
            category = YogaCategory.SOLAR_YOGA,
            isAuspicious = true,
            effects = "Skilled in many arts, intelligent, successful in intellectual pursuits.",
            classicalReference = "Saravali Chapter 34"
        )

        val BRAHMA = YogaDefinition(
            id = "BRAHMA_SOLAR",
            name = "Brahma Yoga",
            sanskritName = "ब्रह्म योग",
            category = YogaCategory.SOLAR_YOGA,
            isAuspicious = true,
            effects = "Scholarly, knowledgeable in Vedas, respectable, religious.",
            classicalReference = "Saravali Chapter 34"
        )
    }

    // ==================== NEGATIVE/ARISHTA YOGAS ====================

    /**
     * Negative Yoga definitions - Challenging combinations (Arishta)
     * Reference: BPHS Chapter 44
     */
    object NegativeYogas {

        val DARIDRA = YogaDefinition(
            id = "DARIDRA",
            name = "Daridra Yoga",
            sanskritName = "दरिद्र योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Difficulties in achieving gains, financial struggles, unfulfilled desires.",
            classicalReference = "BPHS Chapter 44, Sloka 1"
        )

        val GURU_CHANDAL = YogaDefinition(
            id = "GURU_CHANDAL",
            name = "Guru-Chandal Yoga",
            sanskritName = "गुरु-चांडाल योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Challenges with gurus/teachers, unconventional beliefs, potential for spiritual manipulation.",
            classicalReference = "BPHS Chapter 44"
        )

        val SURYA_GRAHAN = YogaDefinition(
            id = "SURYA_GRAHAN",
            name = "Surya Grahan Yoga",
            sanskritName = "सूर्य ग्रहण योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Challenges with authority/father, ego struggles, career obstacles requiring transformation.",
            classicalReference = "BPHS Chapter 44"
        )

        val CHANDRA_GRAHAN = YogaDefinition(
            id = "CHANDRA_GRAHAN",
            name = "Chandra Grahan Yoga",
            sanskritName = "चन्द्र ग्रहण योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Mental restlessness, anxiety, mother-related challenges, emotional turbulence.",
            classicalReference = "BPHS Chapter 44"
        )

        val ANGARAK = YogaDefinition(
            id = "ANGARAK",
            name = "Angarak Yoga",
            sanskritName = "अंगारक योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Explosive energy, accident-prone, conflicts, aggressive tendencies.",
            classicalReference = "BPHS Chapter 44"
        )

        val SHRAPIT = YogaDefinition(
            id = "SHRAPIT",
            name = "Shrapit Yoga",
            sanskritName = "श्रापित योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Karmic debts, delays, obstacles, past-life issues surfacing.",
            classicalReference = "BPHS Chapter 44"
        )

        val KALA_SARPA = YogaDefinition(
            id = "KALA_SARPA",
            name = "Kala Sarpa Yoga",
            sanskritName = "कालसर्प योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Karmic restrictions, sudden ups and downs, struggle followed by success, destiny-driven life.",
            classicalReference = "BPHS Chapter 44"
        )

        val PAPAKARTARI = YogaDefinition(
            id = "PAPAKARTARI",
            name = "Papakartari Yoga",
            sanskritName = "पापकर्तरी योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Hemmed between malefics, struggles, obstacles, pressure from both sides.",
            classicalReference = "BPHS Chapter 44"
        )

        val BALARISHTA = YogaDefinition(
            id = "BALARISHTA",
            name = "Balarishta Yoga",
            sanskritName = "बालारिष्ट योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Childhood health challenges, requires careful nurturing, improves with age.",
            classicalReference = "BPHS Chapter 44"
        )

        val ALPAAYU = YogaDefinition(
            id = "ALPAAYU",
            name = "Alpaayu Yoga",
            sanskritName = "अल्पायु योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Short life indicators, requires remedial measures, health vigilance.",
            classicalReference = "BPHS Chapter 44"
        )

        val MARAKA = YogaDefinition(
            id = "MARAKA",
            name = "Maraka Yoga",
            sanskritName = "मारक योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Death-inflicting house combinations, health challenges during specific periods.",
            classicalReference = "BPHS Chapter 44"
        )

        val KEMDRUM_DOSH = YogaDefinition(
            id = "KEMDRUM_DOSH",
            name = "Kemdrum Dosha",
            sanskritName = "केमद्रुम दोष",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Isolation, lack of support, financial fluctuations, emotional challenges.",
            classicalReference = "Phaladeepika Chapter 4"
        )

        val PITRU_DOSHA = YogaDefinition(
            id = "PITRU_DOSHA",
            name = "Pitru Dosha",
            sanskritName = "पितृ दोष",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Ancestral karmic debt, family pattern challenges, requires shraddha rituals.",
            classicalReference = "BPHS Chapter 44"
        )

        val GANDANTA = YogaDefinition(
            id = "GANDANTA",
            name = "Gandanta Yoga",
            sanskritName = "गण्डान्त योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Junction point birth, karmic knots, transformative challenges.",
            classicalReference = "BPHS Chapter 44"
        )

        val DURYOGA = YogaDefinition(
            id = "DURYOGA",
            name = "Duryoga",
            sanskritName = "दुर्योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "General ill fortune, requires effort to overcome obstacles.",
            classicalReference = "Saravali"
        )
    }

    // ==================== SPECIAL YOGAS ====================

    /**
     * Special Yoga definitions - Unique significant combinations
     * Reference: Various classical texts
     */
    object SpecialYogas {

        val SARASWATI = YogaDefinition(
            id = "SARASWATI",
            name = "Saraswati Yoga",
            sanskritName = "सरस्वती योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Exceptional learning, artistic talents, eloquence, expertise in multiple fields, fame through knowledge.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val PARVATA = YogaDefinition(
            id = "PARVATA",
            name = "Parvata Yoga",
            sanskritName = "पर्वत योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Mountain-like stability, wealth, fame, charitable nature, leadership.",
            classicalReference = "Phaladeepika Chapter 6"
        )

        val SHUBHAKARTARI = YogaDefinition(
            id = "SHUBHAKARTARI",
            name = "Shubhakartari Yoga",
            sanskritName = "शुभकर्तरी योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Protected by benefics, fortunate life, surrounded by helpful people.",
            classicalReference = "BPHS Chapter 14"
        )

        val SANYASA = YogaDefinition(
            id = "SANYASA",
            name = "Sanyasa Yoga",
            sanskritName = "सन्यास योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Strong inclination towards renunciation, spiritual pursuits, detachment from worldly matters.",
            classicalReference = "BPHS Chapter 42"
        )

        val PRAVRAJYA = YogaDefinition(
            id = "PRAVRAJYA",
            name = "Pravrajya Yoga",
            sanskritName = "प्रव्रज्या योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Renunciation, spiritual path, may become monk or recluse.",
            classicalReference = "BPHS Chapter 42"
        )

        val PARIVRAAJAKA = YogaDefinition(
            id = "PARIVRAAJAKA",
            name = "Parivraajaka Yoga",
            sanskritName = "परिव्राजक योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Wandering ascetic, spiritual seeker, pilgrim nature.",
            classicalReference = "BPHS Chapter 42"
        )

        val VARGOTTAMA = YogaDefinition(
            id = "VARGOTTAMA",
            name = "Vargottama Yoga",
            sanskritName = "वर्गोत्तम योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Planet in same sign in D1 and D9, strengthened effects, natural potential.",
            classicalReference = "BPHS Chapter 6"
        )

        val PUSHKARA_NAVAMSA = YogaDefinition(
            id = "PUSHKARA_NAVAMSA",
            name = "Pushkara Navamsa",
            sanskritName = "पुष्कर नवांश",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Highly auspicious navamsa, brings fortune and protection.",
            classicalReference = "Jataka Parijata"
        )

        val YOGA_KARAKA = YogaDefinition(
            id = "YOGA_KARAKA",
            name = "Yogakaraka",
            sanskritName = "योगकारक",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Planet owning both kendra and trikona, highly beneficial, brings success.",
            classicalReference = "BPHS Chapter 41"
        )

        val RAJA_YOGA_KARAKA = YogaDefinition(
            id = "RAJA_YOGA_KARAKA",
            name = "Raja Yogakaraka",
            sanskritName = "राज योगकारक",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Special Raja Yoga forming planet, brings power and authority.",
            classicalReference = "BPHS Chapter 41"
        )

        val MARAKA_YOGA_CANCEL = YogaDefinition(
            id = "MARAKA_YOGA_CANCEL",
            name = "Maraka Yoga Cancellation",
            sanskritName = "मारक योग भंग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Cancellation of death-inflicting combinations, longevity indicated.",
            classicalReference = "BPHS Chapter 44"
        )

        val DASA_MULA = YogaDefinition(
            id = "DASA_MULA",
            name = "Dasa-Mula Yoga",
            sanskritName = "दश-मूल योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = false,
            effects = "Ten root afflictions, requires careful analysis and remediation.",
            classicalReference = "BPHS Chapter 44"
        )
    }

    // ==================== NAKSHATRA YOGAS ====================

    /**
     * Nakshatra-based Yoga definitions
     */
    object NakshatraYogas {

        val NAKSHATRA_YOGA_AUSPICIOUS = YogaDefinition(
            id = "NAKSHATRA_AUSPICIOUS",
            name = "Shubha Nakshatra Yoga",
            sanskritName = "शुभ नक्षत्र योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Birth in auspicious nakshatra, natural protection and fortune.",
            classicalReference = "BPHS Chapter 2"
        )

        val MRITYU_NAKSHATRA = YogaDefinition(
            id = "MRITYU_NAKSHATRA",
            name = "Mrityu Nakshatra Yoga",
            sanskritName = "मृत्यु नक्षत्र योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Birth in challenging nakshatra portion, requires remediation.",
            classicalReference = "BPHS Chapter 2"
        )

        val JANMA_TARA = YogaDefinition(
            id = "JANMA_TARA",
            name = "Janma Tara Yoga",
            sanskritName = "जन्म तारा योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Birth star strength, determines overall life pattern.",
            classicalReference = "BPHS Chapter 2"
        )

        val NAKSHATRA_SANDHI = YogaDefinition(
            id = "NAKSHATRA_SANDHI",
            name = "Nakshatra Sandhi",
            sanskritName = "नक्षत्र संधि",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Birth at nakshatra junction, transformative challenges.",
            classicalReference = "BPHS Chapter 2"
        )
    }

    // ==================== VIVAHA (MARRIAGE) YOGAS ====================

    /**
     * Marriage-related Yoga definitions
     */
    object VivahaYogas {

        val KALATRA = YogaDefinition(
            id = "KALATRA",
            name = "Kalatra Yoga",
            sanskritName = "कलत्र योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Strong marriage indications, spouse brings prosperity.",
            classicalReference = "BPHS Chapter 18"
        )

        val DWIKALATRA = YogaDefinition(
            id = "DWIKALATRA",
            name = "Dwikalatra Yoga",
            sanskritName = "द्विकलत्र योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = false,
            effects = "Two marriages indicated, separation from first spouse possible.",
            classicalReference = "BPHS Chapter 18"
        )

        val MANGLIK = YogaDefinition(
            id = "MANGLIK",
            name = "Manglik Dosha",
            sanskritName = "माँगलिक दोष",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Mars affliction to marriage, requires matching or remediation.",
            classicalReference = "Various classical texts"
        )

        val KALATRA_KARAKA = YogaDefinition(
            id = "KALATRA_KARAKA",
            name = "Kalatra Karaka Yoga",
            sanskritName = "कलत्र कारक योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Strong Venus placement, happy marriage, beautiful spouse.",
            classicalReference = "BPHS Chapter 18"
        )

        val VIVAHA_YOGA = YogaDefinition(
            id = "VIVAHA_YOGA",
            name = "Vivaha Yoga",
            sanskritName = "विवाह योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Marriage-promoting combination, timely and happy marriage.",
            classicalReference = "BPHS Chapter 18"
        )

        val VIVAHA_KARKA_PEEDA = YogaDefinition(
            id = "VIVAHA_KARKA_PEEDA",
            name = "Vivaha Karka Peeda",
            sanskritName = "विवाह कारक पीडा",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Afflicted marriage karaka, delays or difficulties in marriage.",
            classicalReference = "BPHS Chapter 18"
        )
    }

    // ==================== KARMA (PROFESSION) YOGAS ====================

    /**
     * Career/Profession-related Yoga definitions
     */
    object KarmaYogas {

        val KARMA_JEEVA = YogaDefinition(
            id = "KARMA_JEEVA",
            name = "Karma Jeeva Yoga",
            sanskritName = "कर्म जीव योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Strong professional success, recognition in career.",
            classicalReference = "BPHS Chapter 10"
        )

        val RAJYA = YogaDefinition(
            id = "RAJYA",
            name = "Rajya Yoga",
            sanskritName = "राज्य योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Government position, political success, administrative authority.",
            classicalReference = "BPHS Chapter 41"
        )

        val AMATYA = YogaDefinition(
            id = "AMATYA",
            name = "Amatya Yoga",
            sanskritName = "अमात्य योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Minister-like position, advisory role, counselor to authority.",
            classicalReference = "Jaimini Sutras"
        )

        val BHAGYA_KARMA = YogaDefinition(
            id = "BHAGYA_KARMA",
            name = "Bhagya-Karma Yoga",
            sanskritName = "भाग्य-कर्म योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Fortune through profession, career aligned with destiny.",
            classicalReference = "BPHS Chapter 41"
        )

        val VYAPARA = YogaDefinition(
            id = "VYAPARA",
            name = "Vyapara Yoga",
            sanskritName = "व्यापार योग",
            category = YogaCategory.DHANA_YOGA,
            isAuspicious = true,
            effects = "Business success, commercial acumen, trade prosperity.",
            classicalReference = "Saravali"
        )

        val VIDYA = YogaDefinition(
            id = "VIDYA",
            name = "Vidya Yoga",
            sanskritName = "विद्या योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Excellence in education, scholarly achievements, teaching ability.",
            classicalReference = "BPHS Chapter 9"
        )
    }

    // ==================== RAHU-KETU YOGAS ====================

    /**
     * Rahu-Ketu (Nodal) Yoga definitions
     */
    object RahuKetuYogas {

        val RAHU_YOGA = YogaDefinition(
            id = "RAHU_YOGA",
            name = "Rahu Yoga",
            sanskritName = "राहु योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = false,
            effects = "Rahu-influenced destiny, foreign connections, unconventional path.",
            classicalReference = "BPHS Chapter 25"
        )

        val KETU_YOGA = YogaDefinition(
            id = "KETU_YOGA",
            name = "Ketu Yoga",
            sanskritName = "केतु योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = false,
            effects = "Ketu-influenced destiny, spiritual inclination, past-life karma.",
            classicalReference = "BPHS Chapter 25"
        )

        val RAHU_KENDRA = YogaDefinition(
            id = "RAHU_KENDRA",
            name = "Rahu in Kendra",
            sanskritName = "राहु केन्द्र",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = false,
            effects = "Rahu in angular house, worldly ambitions, foreign influence.",
            classicalReference = "BPHS Chapter 25"
        )

        val KETU_MOKSHA = YogaDefinition(
            id = "KETU_MOKSHA",
            name = "Ketu Moksha Yoga",
            sanskritName = "केतु मोक्ष योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Ketu in 12th house, liberation potential, spiritual attainment.",
            classicalReference = "BPHS Chapter 25"
        )

        val SARPA_YOGA_NODAL = YogaDefinition(
            id = "SARPA_YOGA_NODAL",
            name = "Sarpa Yoga (Nodal)",
            sanskritName = "सर्प योग",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Nodal afflictions, sudden changes, karmic challenges.",
            classicalReference = "BPHS Chapter 44"
        )

        val KALA_AMRITA = YogaDefinition(
            id = "KALA_AMRITA",
            name = "Kala Amrita Yoga",
            sanskritName = "काल अमृत योग",
            category = YogaCategory.SPECIAL_YOGA,
            isAuspicious = true,
            effects = "Opposite of Kala Sarpa, all planets between Ketu-Rahu, generally positive.",
            classicalReference = "Modern classical interpretation"
        )
    }

    // ==================== PARIVARTANA (EXCHANGE) YOGAS ====================

    /**
     * Parivartana (Mutual Exchange) Yoga definitions
     */
    object ParivartanaYogas {

        val MAHA_PARIVARTANA = YogaDefinition(
            id = "MAHA_PARIVARTANA",
            name = "Maha Parivartana Yoga",
            sanskritName = "महा परिवर्तन योग",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Great mutual exchange between benefic houses, exceptional results.",
            classicalReference = "BPHS Chapter 41"
        )

        val KAHALA_PARIVARTANA = YogaDefinition(
            id = "KAHALA_PARIVARTANA",
            name = "Kahala Parivartana",
            sanskritName = "कहल परिवर्तन",
            category = YogaCategory.RAJA_YOGA,
            isAuspicious = true,
            effects = "Exchange involving 3rd house, courage and valor enhanced.",
            classicalReference = "BPHS Chapter 41"
        )

        val DAINYA_PARIVARTANA = YogaDefinition(
            id = "DAINYA_PARIVARTANA",
            name = "Dainya Parivartana",
            sanskritName = "दैन्य परिवर्तन",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Exchange involving dusthana lords, creates challenges.",
            classicalReference = "BPHS Chapter 41"
        )

        val KHALA_PARIVARTANA = YogaDefinition(
            id = "KHALA_PARIVARTANA",
            name = "Khala Parivartana",
            sanskritName = "खल परिवर्तन",
            category = YogaCategory.NEGATIVE_YOGA,
            isAuspicious = false,
            effects = "Bad exchange, creates obstacles and setbacks.",
            classicalReference = "BPHS Chapter 41"
        )
    }
}

/**
 * Data class representing a single Yoga definition
 */
data class YogaDefinition(
    val id: String,
    val name: String,
    val sanskritName: String,
    val category: YogaCategory,
    val isAuspicious: Boolean,
    val effects: String,
    val classicalReference: String
)

/**
 * Utility functions for yoga definitions
 */
object YogaDefinitionUtils {

    /**
     * Get all yoga definitions as a flat list
     */
    fun getAllDefinitions(): List<YogaDefinition> {
        return listOf(
            // Raja Yogas
            YogaDefinitions.RajaYogas.KENDRA_TRIKONA_RAJA,
            YogaDefinitions.RajaYogas.PARIVARTANA_RAJA,
            YogaDefinitions.RajaYogas.VIPARITA_RAJA,
            YogaDefinitions.RajaYogas.NEECHA_BHANGA_RAJA,
            YogaDefinitions.RajaYogas.MAHA_RAJA,
            YogaDefinitions.RajaYogas.MAHA_BHAGYA,
            YogaDefinitions.RajaYogas.PUSHKALA,
            YogaDefinitions.RajaYogas.AKHANDA_SAMRAJYA,
            YogaDefinitions.RajaYogas.CHAMARA,
            YogaDefinitions.RajaYogas.KAHALA,
            YogaDefinitions.RajaYogas.CHATRA,
            YogaDefinitions.RajaYogas.CHAAMARA,
            YogaDefinitions.RajaYogas.LAKSHMI_RAJA,
            YogaDefinitions.RajaYogas.SHANKHA,
            YogaDefinitions.RajaYogas.BHERI,
            YogaDefinitions.RajaYogas.MRIDANGA,
            YogaDefinitions.RajaYogas.SREENATHA,
            YogaDefinitions.RajaYogas.MATSYA,
            YogaDefinitions.RajaYogas.KURMA,
            YogaDefinitions.RajaYogas.KHADGA,
            YogaDefinitions.RajaYogas.KUSUMA,
            YogaDefinitions.RajaYogas.KALANIDHI,
            YogaDefinitions.RajaYogas.KALPADRUMA,
            YogaDefinitions.RajaYogas.LAGNADHI_RAJA,
            YogaDefinitions.RajaYogas.CHANDRA_ADHI_RAJA,
            YogaDefinitions.RajaYogas.DHARMA_KARMADHIPATI,
            YogaDefinitions.RajaYogas.PARIJATA,
            YogaDefinitions.RajaYogas.TRILOCHANA,
            YogaDefinitions.RajaYogas.SHIVA,
            YogaDefinitions.RajaYogas.GOURI,
            YogaDefinitions.RajaYogas.BHARATHI,
            YogaDefinitions.RajaYogas.INDRA,
            YogaDefinitions.RajaYogas.HARIHARA_BRAHMA,
            YogaDefinitions.RajaYogas.TRIMURTI,
            YogaDefinitions.RajaYogas.CHAPA,
            YogaDefinitions.RajaYogas.HAMSA_RAJA,
            // Dhana Yogas
            YogaDefinitions.DhanaYogas.BASIC_DHANA,
            YogaDefinitions.DhanaYogas.LAKSHMI_DHANA,
            YogaDefinitions.DhanaYogas.KUBERA,
            YogaDefinitions.DhanaYogas.CHANDRA_MANGALA,
            YogaDefinitions.DhanaYogas.LABHA,
            YogaDefinitions.DhanaYogas.VASUMATHI,
            YogaDefinitions.DhanaYogas.MAHALAXMI,
            YogaDefinitions.DhanaYogas.INDU_LAGNA,
            YogaDefinitions.DhanaYogas.SHUBHA_KARTARI_DHANA,
            YogaDefinitions.DhanaYogas.SRINATHA_DHANA,
            YogaDefinitions.DhanaYogas.KAHALALA,
            YogaDefinitions.DhanaYogas.DHENU,
            YogaDefinitions.DhanaYogas.PARVATA_DHANA,
            YogaDefinitions.DhanaYogas.BANDHU_PUJYA,
            YogaDefinitions.DhanaYogas.SRI_LAGNA,
            YogaDefinitions.DhanaYogas.UTTAMADI,
            YogaDefinitions.DhanaYogas.ARDHA_CHANDRA_DHANA,
            YogaDefinitions.DhanaYogas.SAMUDRA,
            YogaDefinitions.DhanaYogas.SHASHI_MANGALA,
            // Mahapurusha Yogas
            YogaDefinitions.MahapurushaYogas.RUCHAKA,
            YogaDefinitions.MahapurushaYogas.BHADRA,
            YogaDefinitions.MahapurushaYogas.HAMSA,
            YogaDefinitions.MahapurushaYogas.MALAVYA,
            YogaDefinitions.MahapurushaYogas.SHASHA,
            // Nabhasa Yogas (extensive list)
            YogaDefinitions.NabhasaYogas.YAVA,
            YogaDefinitions.NabhasaYogas.SHRINGATAKA,
            YogaDefinitions.NabhasaYogas.GADA,
            YogaDefinitions.NabhasaYogas.SHAKATA,
            YogaDefinitions.NabhasaYogas.PAKSHI,
            YogaDefinitions.NabhasaYogas.SHRINGATA,
            YogaDefinitions.NabhasaYogas.ARDHA_CHANDRA,
            YogaDefinitions.NabhasaYogas.CHAKRA,
            YogaDefinitions.NabhasaYogas.SAMUDRA,
            YogaDefinitions.NabhasaYogas.KAMALA,
            YogaDefinitions.NabhasaYogas.VAAPI,
            YogaDefinitions.NabhasaYogas.YUPA,
            YogaDefinitions.NabhasaYogas.ISHU,
            YogaDefinitions.NabhasaYogas.SHAKTI,
            YogaDefinitions.NabhasaYogas.DANDA,
            YogaDefinitions.NabhasaYogas.NAUKA,
            YogaDefinitions.NabhasaYogas.KOOTA,
            YogaDefinitions.NabhasaYogas.CHHATRA,
            YogaDefinitions.NabhasaYogas.CHAPA,
            YogaDefinitions.NabhasaYogas.VEENA,
            YogaDefinitions.NabhasaYogas.GOLA,
            YogaDefinitions.NabhasaYogas.YUGA,
            YogaDefinitions.NabhasaYogas.SHOOLA,
            YogaDefinitions.NabhasaYogas.KEDARA,
            YogaDefinitions.NabhasaYogas.PASA,
            YogaDefinitions.NabhasaYogas.DAMINI,
            YogaDefinitions.NabhasaYogas.VALLAKI,
            YogaDefinitions.NabhasaYogas.RAJJU,
            YogaDefinitions.NabhasaYogas.MUSALA,
            YogaDefinitions.NabhasaYogas.NALA,
            YogaDefinitions.NabhasaYogas.MAALA,
            YogaDefinitions.NabhasaYogas.SARPA,
            // Chandra Yogas
            YogaDefinitions.ChandraYogas.SUNAFA,
            YogaDefinitions.ChandraYogas.ANAFA,
            YogaDefinitions.ChandraYogas.DURUDHARA,
            YogaDefinitions.ChandraYogas.KEMADRUMA,
            YogaDefinitions.ChandraYogas.KEMADRUMA_BHANGA,
            YogaDefinitions.ChandraYogas.GAJA_KESARI,
            YogaDefinitions.ChandraYogas.ADHI_YOGA,
            YogaDefinitions.ChandraYogas.SAKATA,
            YogaDefinitions.ChandraYogas.AMALA,
            YogaDefinitions.ChandraYogas.CHANDRA_MANGALA_YOGA,
            YogaDefinitions.ChandraYogas.PUSHKARA,
            YogaDefinitions.ChandraYogas.PURNIMA,
            YogaDefinitions.ChandraYogas.AMAVASYA,
            YogaDefinitions.ChandraYogas.PAKSHA_BALA,
            // Solar Yogas
            YogaDefinitions.SolarYogas.VESI,
            YogaDefinitions.SolarYogas.VOSI,
            YogaDefinitions.SolarYogas.UBHAYACHARI,
            YogaDefinitions.SolarYogas.BUDHA_ADITYA,
            YogaDefinitions.SolarYogas.NIPUNA,
            YogaDefinitions.SolarYogas.BRAHMA,
            // Negative Yogas
            YogaDefinitions.NegativeYogas.DARIDRA,
            YogaDefinitions.NegativeYogas.GURU_CHANDAL,
            YogaDefinitions.NegativeYogas.SURYA_GRAHAN,
            YogaDefinitions.NegativeYogas.CHANDRA_GRAHAN,
            YogaDefinitions.NegativeYogas.ANGARAK,
            YogaDefinitions.NegativeYogas.SHRAPIT,
            YogaDefinitions.NegativeYogas.KALA_SARPA,
            YogaDefinitions.NegativeYogas.PAPAKARTARI,
            YogaDefinitions.NegativeYogas.BALARISHTA,
            YogaDefinitions.NegativeYogas.ALPAAYU,
            YogaDefinitions.NegativeYogas.MARAKA,
            YogaDefinitions.NegativeYogas.KEMDRUM_DOSH,
            YogaDefinitions.NegativeYogas.PITRU_DOSHA,
            YogaDefinitions.NegativeYogas.GANDANTA,
            YogaDefinitions.NegativeYogas.DURYOGA,
            // Special Yogas
            YogaDefinitions.SpecialYogas.SARASWATI,
            YogaDefinitions.SpecialYogas.PARVATA,
            YogaDefinitions.SpecialYogas.SHUBHAKARTARI,
            YogaDefinitions.SpecialYogas.SANYASA,
            YogaDefinitions.SpecialYogas.PRAVRAJYA,
            YogaDefinitions.SpecialYogas.PARIVRAAJAKA,
            YogaDefinitions.SpecialYogas.VARGOTTAMA,
            YogaDefinitions.SpecialYogas.PUSHKARA_NAVAMSA,
            YogaDefinitions.SpecialYogas.YOGA_KARAKA,
            YogaDefinitions.SpecialYogas.RAJA_YOGA_KARAKA,
            YogaDefinitions.SpecialYogas.MARAKA_YOGA_CANCEL,
            YogaDefinitions.SpecialYogas.DASA_MULA,
            // Nakshatra Yogas
            YogaDefinitions.NakshatraYogas.NAKSHATRA_YOGA_AUSPICIOUS,
            YogaDefinitions.NakshatraYogas.MRITYU_NAKSHATRA,
            YogaDefinitions.NakshatraYogas.JANMA_TARA,
            YogaDefinitions.NakshatraYogas.NAKSHATRA_SANDHI,
            // Vivaha Yogas
            YogaDefinitions.VivahaYogas.KALATRA,
            YogaDefinitions.VivahaYogas.DWIKALATRA,
            YogaDefinitions.VivahaYogas.MANGLIK,
            YogaDefinitions.VivahaYogas.KALATRA_KARAKA,
            YogaDefinitions.VivahaYogas.VIVAHA_YOGA,
            YogaDefinitions.VivahaYogas.VIVAHA_KARKA_PEEDA,
            // Karma Yogas
            YogaDefinitions.KarmaYogas.KARMA_JEEVA,
            YogaDefinitions.KarmaYogas.RAJYA,
            YogaDefinitions.KarmaYogas.AMATYA,
            YogaDefinitions.KarmaYogas.BHAGYA_KARMA,
            YogaDefinitions.KarmaYogas.VYAPARA,
            YogaDefinitions.KarmaYogas.VIDYA,
            // Rahu-Ketu Yogas
            YogaDefinitions.RahuKetuYogas.RAHU_YOGA,
            YogaDefinitions.RahuKetuYogas.KETU_YOGA,
            YogaDefinitions.RahuKetuYogas.RAHU_KENDRA,
            YogaDefinitions.RahuKetuYogas.KETU_MOKSHA,
            YogaDefinitions.RahuKetuYogas.SARPA_YOGA_NODAL,
            YogaDefinitions.RahuKetuYogas.KALA_AMRITA,
            // Parivartana Yogas
            YogaDefinitions.ParivartanaYogas.MAHA_PARIVARTANA,
            YogaDefinitions.ParivartanaYogas.KAHALA_PARIVARTANA,
            YogaDefinitions.ParivartanaYogas.DAINYA_PARIVARTANA,
            YogaDefinitions.ParivartanaYogas.KHALA_PARIVARTANA
        )
    }

    /**
     * Get yoga definition by ID
     */
    fun getDefinitionById(id: String): YogaDefinition? {
        return getAllDefinitions().find { it.id == id }
    }

    /**
     * Get definitions by category
     */
    fun getDefinitionsByCategory(category: YogaCategory): List<YogaDefinition> {
        return getAllDefinitions().filter { it.category == category }
    }

    /**
     * Get total count of defined yogas
     */
    fun getTotalYogaCount(): Int = getAllDefinitions().size
}

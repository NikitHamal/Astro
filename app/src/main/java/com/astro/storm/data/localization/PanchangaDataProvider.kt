package com.astro.storm.core.common

/**
 * Localized Panchanga Data Provider
 *
 * This object provides localized data for all Panchanga elements including:
 * - Tithi (Lunar day) data with activities, descriptions
 * - Nakshatra (Lunar mansion) data
 * - Yoga (Luni-solar combination) data
 * - Karana (Half tithi) data
 * - Vara (Weekday) data
 *
 * All data is based on authentic Vedic astrology texts:
 * - Brihat Parashara Hora Shastra
 * - Muhurta Chintamani
 * - Jyotish Ratnakar
 *
 * @author AstroStorm
 */
object PanchangaDataProvider {

    // ============================================
    // QUALITY ENUM
    // ============================================

    enum class Quality {
        EXCELLENT,
        GOOD,
        NEUTRAL,
        CHALLENGING;

        fun getLocalizedName(language: Language): String = when (this) {
            EXCELLENT -> StringResources.get(StringKeyAnalysisPart1.QUALITY_EXCELLENT, language)
            GOOD -> StringResources.get(StringKeyAnalysisPart1.QUALITY_GOOD, language)
            NEUTRAL -> StringResources.get(StringKeyAnalysisPart1.QUALITY_NEUTRAL, language)
            CHALLENGING -> StringResources.get(StringKeyAnalysisPart1.QUALITY_CHALLENGING, language)
        }
    }

    // ============================================
    // TITHI DATA
    // ============================================

    data class TithiData(
        val deityEn: String,
        val deityNe: String,
        val natureEn: String,
        val natureNe: String,
        val quality: Quality,
        val descriptionEn: String,
        val descriptionNe: String,
        val activitiesEn: List<String>,
        val activitiesNe: List<String>,
        val avoidEn: List<String>,
        val avoidNe: List<String>
    ) {
        fun getDeity(language: Language): String = when (language) {
            Language.ENGLISH -> deityEn
            Language.NEPALI -> deityNe
        }

        fun getNature(language: Language): String = when (language) {
            Language.ENGLISH -> natureEn
            Language.NEPALI -> natureNe
        }

        fun getDescription(language: Language): String = when (language) {
            Language.ENGLISH -> descriptionEn
            Language.NEPALI -> descriptionNe
        }

        fun getActivities(language: Language): List<String> = when (language) {
            Language.ENGLISH -> activitiesEn
            Language.NEPALI -> activitiesNe
        }

        fun getAvoid(language: Language): List<String> = when (language) {
            Language.ENGLISH -> avoidEn
            Language.NEPALI -> avoidNe
        }
    }

    /**
     * Get Tithi data for a given tithi number (1-30)
     * Tithis 1-15 are Shukla Paksha, 16-30 are Krishna Paksha
     */
    fun getTithiData(tithiNumber: Int): TithiData = when (tithiNumber) {
        1, 16 -> TithiData(
            deityEn = "Agni (Fire God)",
            deityNe = "अग्नि (अग्निदेव)",
            natureEn = "Nanda (Joyful)",
            natureNe = "नन्दा (आनन्दमय)",
            quality = Quality.GOOD,
            descriptionEn = "Pratipada marks new beginnings. The first tithi after New Moon or Full Moon carries the energy of initiation and fresh starts. It is auspicious for beginning new ventures, laying foundations, and starting journeys.",
            descriptionNe = "प्रतिपदाले नयाँ शुरुआत चिन्ह लगाउँछ। अमावस्या वा पूर्णिमा पछिको पहिलो तिथिले शुरुआत र नयाँ सुरुवातको ऊर्जा बोकेको हुन्छ। नयाँ कार्य शुरु गर्न, जग राख्न र यात्रा सुरु गर्न शुभ छ।",
            activitiesEn = listOf("New beginnings", "Starting ventures", "Foundation laying", "Travel"),
            activitiesNe = listOf("नयाँ शुरुआत", "कार्य प्रारम्भ", "जग राख्ने", "यात्रा"),
            avoidEn = listOf("Completing projects", "Endings"),
            avoidNe = listOf("परियोजना समाप्त गर्ने", "अन्त्यहरू")
        )
        2, 17 -> TithiData(
            deityEn = "Brahma (Creator)",
            deityNe = "ब्रह्मा (सृष्टिकर्ता)",
            natureEn = "Bhadra (Auspicious)",
            natureNe = "भद्रा (शुभ)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Dwitiya is ruled by Brahma, the creator. This tithi is excellent for creative endeavors, naming ceremonies, and constructive activities. It supports growth and expansion of new initiatives.",
            descriptionNe = "द्वितीया सृष्टिकर्ता ब्रह्माद्वारा शासित छ। यो तिथि सृजनात्मक कार्य, नामकरण संस्कार र निर्माणात्मक गतिविधिका लागि उत्कृष्ट छ। नयाँ पहलको वृद्धि र विस्तारलाई समर्थन गर्छ।",
            activitiesEn = listOf("Creative work", "Naming ceremonies", "Marriage", "House warming"),
            activitiesNe = listOf("सृजनात्मक कार्य", "नामकरण", "विवाह", "गृह प्रवेश"),
            avoidEn = listOf("Conflict", "Aggressive actions"),
            avoidNe = listOf("द्वन्द्व", "आक्रामक कार्य")
        )
        3, 18 -> TithiData(
            deityEn = "Gauri (Parvati)",
            deityNe = "गौरी (पार्वती)",
            natureEn = "Jaya (Victory)",
            natureNe = "जया (विजय)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Tritiya is associated with victory and success. Ruled by Gauri, it is highly auspicious for celebrations, religious ceremonies, and activities requiring divine grace and feminine energy.",
            descriptionNe = "तृतीया विजय र सफलतासँग सम्बन्धित छ। गौरीद्वारा शासित, यो उत्सव, धार्मिक समारोह र दिव्य कृपा तथा नारी शक्ति आवश्यक गतिविधिका लागि अत्यन्त शुभ छ।",
            activitiesEn = listOf("Religious ceremonies", "Celebrations", "Victory rituals", "Arts"),
            activitiesNe = listOf("धार्मिक समारोह", "उत्सव", "विजय अनुष्ठान", "कला"),
            avoidEn = listOf("Conflicts", "Harsh activities"),
            avoidNe = listOf("द्वन्द्व", "कठोर गतिविधि")
        )
        4, 19 -> TithiData(
            deityEn = "Ganesha/Yama",
            deityNe = "गणेश/यम",
            natureEn = "Rikta (Empty)",
            natureNe = "रिक्ता (खाली)",
            quality = Quality.CHALLENGING,
            descriptionEn = "Chaturthi is ruled by Ganesha (4th) and Yama (19th). While the 4th is for Ganesha worship, these tithis are generally considered inauspicious for beginning new work due to their 'empty' nature.",
            descriptionNe = "चतुर्थी गणेश (४थी) र यम (१९औं) द्वारा शासित छ। ४थी गणेश पूजाको लागि भए पनि, यी तिथिहरू सामान्यतया 'रिक्त' प्रकृतिको कारण नयाँ कार्य सुरु गर्न अशुभ मानिन्छ।",
            activitiesEn = listOf("Ganesha worship", "Removing obstacles", "Spiritual practices"),
            activitiesNe = listOf("गणेश पूजा", "विघ्न हटाउने", "आध्यात्मिक अभ्यास"),
            avoidEn = listOf("New beginnings", "Travel", "Important decisions"),
            avoidNe = listOf("नयाँ शुरुआत", "यात्रा", "महत्त्वपूर्ण निर्णय")
        )
        5, 20 -> TithiData(
            deityEn = "Nagas (Serpent deities)",
            deityNe = "नागहरू (सर्प देवता)",
            natureEn = "Nanda (Joyful)",
            natureNe = "नन्दा (आनन्दमय)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Panchami is excellent for education, learning, and wisdom pursuits. Ruled by the Nagas, it supports activities requiring knowledge, skill development, and intellectual growth.",
            descriptionNe = "पञ्चमी शिक्षा, सिकाइ र ज्ञान अनुशीलनका लागि उत्कृष्ट छ। नागहरूद्वारा शासित, यसले ज्ञान, सीप विकास र बौद्धिक वृद्धि आवश्यक गतिविधिलाई समर्थन गर्छ।",
            activitiesEn = listOf("Education", "Learning", "Writing", "Medicine", "Healing"),
            activitiesNe = listOf("शिक्षा", "सिकाइ", "लेखन", "चिकित्सा", "उपचार"),
            avoidEn = listOf("Destructive activities"),
            avoidNe = listOf("विनाशकारी गतिविधि")
        )
        6, 21 -> TithiData(
            deityEn = "Kartikeya (Skanda)",
            deityNe = "कार्तिकेय (स्कन्द)",
            natureEn = "Bhadra (Auspicious)",
            natureNe = "भद्रा (शुभ)",
            quality = Quality.GOOD,
            descriptionEn = "Shashthi is dedicated to Kartikeya (Skanda), the god of war and victory. It is favorable for activities requiring courage, medical treatments, and overcoming enemies or diseases.",
            descriptionNe = "षष्ठी युद्ध र विजयका देवता कार्तिकेय (स्कन्द) लाई समर्पित छ। साहस चाहिने गतिविधि, चिकित्सा उपचार र शत्रु वा रोगमाथि विजय प्राप्त गर्न अनुकूल छ।",
            activitiesEn = listOf("Medical treatments", "Surgery", "Overcoming obstacles", "Courage"),
            activitiesNe = listOf("चिकित्सा उपचार", "शल्यक्रिया", "बाधा पार", "साहस"),
            avoidEn = listOf("Timid actions", "Postponements"),
            avoidNe = listOf("डरपोक कार्य", "स्थगन")
        )
        7, 22 -> TithiData(
            deityEn = "Surya (Sun God)",
            deityNe = "सूर्य (सूर्यदेव)",
            natureEn = "Jaya (Victory)",
            natureNe = "जया (विजय)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Saptami is ruled by the Sun and brings victory and success. Excellent for travel, especially pilgrimages, vehicle purchases, and activities requiring solar energy and vitality.",
            descriptionNe = "सप्तमी सूर्यद्वारा शासित छ र विजय तथा सफलता ल्याउँछ। यात्रा, विशेषगरी तीर्थयात्रा, सवारी साधन खरिद र सौर्य ऊर्जा तथा जीवनशक्ति आवश्यक गतिविधिका लागि उत्कृष्ट छ।",
            activitiesEn = listOf("Travel", "Pilgrimages", "Vehicle purchase", "Government work"),
            activitiesNe = listOf("यात्रा", "तीर्थयात्रा", "सवारी खरिद", "सरकारी कार्य"),
            avoidEn = listOf("Night activities", "Moon-related work"),
            avoidNe = listOf("रातका गतिविधि", "चन्द्र सम्बन्धी कार्य")
        )
        8, 23 -> TithiData(
            deityEn = "Shiva/Rudra",
            deityNe = "शिव/रुद्र",
            natureEn = "Rikta (Empty)",
            natureNe = "रिक्ता (खाली)",
            quality = Quality.NEUTRAL,
            descriptionEn = "Ashtami is sacred to Lord Shiva and considered powerful for spiritual practices. While classified as Rikta, it is excellent for worship, fasting, and tantric practices. Mixed for worldly activities.",
            descriptionNe = "अष्टमी भगवान शिवलाई पवित्र छ र आध्यात्मिक अभ्यासका लागि शक्तिशाली मानिन्छ। रिक्ता वर्गमा परे पनि, पूजा, उपवास र तान्त्रिक अभ्यासका लागि उत्कृष्ट छ। सांसारिक गतिविधिका लागि मिश्रित।",
            activitiesEn = listOf("Shiva worship", "Fasting", "Spiritual practices", "Meditation"),
            activitiesNe = listOf("शिव पूजा", "उपवास", "आध्यात्मिक अभ्यास", "ध्यान"),
            avoidEn = listOf("New ventures", "Material pursuits", "Celebrations"),
            avoidNe = listOf("नयाँ कार्य", "भौतिक अनुशीलन", "उत्सव")
        )
        9, 24 -> TithiData(
            deityEn = "Durga (Mother Goddess)",
            deityNe = "दुर्गा (माता देवी)",
            natureEn = "Nanda (Joyful)",
            natureNe = "नन्दा (आनन्दमय)",
            quality = Quality.GOOD,
            descriptionEn = "Navami is sacred to Durga and other fierce forms of the Divine Mother. Excellent for worship of the goddess, overcoming enemies, and activities requiring aggressive energy and protection.",
            descriptionNe = "नवमी दुर्गा र दिव्य मातृका अन्य उग्र रूपलाई पवित्र छ। देवी पूजा, शत्रुमाथि विजय र आक्रामक ऊर्जा तथा सुरक्षा चाहिने गतिविधिका लागि उत्कृष्ट।",
            activitiesEn = listOf("Durga worship", "Protection rituals", "Overcoming enemies", "Strength"),
            activitiesNe = listOf("दुर्गा पूजा", "सुरक्षा अनुष्ठान", "शत्रुमाथि विजय", "शक्ति"),
            avoidEn = listOf("Peaceful negotiations", "Gentle activities"),
            avoidNe = listOf("शान्तिपूर्ण वार्ता", "कोमल गतिविधि")
        )
        10, 25 -> TithiData(
            deityEn = "Yama (God of Death)",
            deityNe = "यम (मृत्युदेव)",
            natureEn = "Bhadra (Auspicious)",
            natureNe = "भद्रा (शुभ)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Dashami represents victory and completion. Ruled by Yama, it is excellent for completing tasks, achieving goals, and celebrating success. Highly auspicious for important undertakings.",
            descriptionNe = "दशमीले विजय र पूर्णतालाई प्रतिनिधित्व गर्छ। यमद्वारा शासित, यो कार्य पूरा गर्न, लक्ष्य प्राप्त गर्न र सफलता मनाउन उत्कृष्ट छ। महत्त्वपूर्ण उपक्रमहरूका लागि अत्यन्त शुभ।",
            activitiesEn = listOf("Completing projects", "Victory celebrations", "Important tasks", "Success"),
            activitiesNe = listOf("परियोजना पूरा", "विजय उत्सव", "महत्त्वपूर्ण कार्य", "सफलता"),
            avoidEn = listOf("Beginning long-term projects"),
            avoidNe = listOf("दीर्घकालीन परियोजना सुरु गर्ने")
        )
        11, 26 -> TithiData(
            deityEn = "Vishnu (Preserver)",
            deityNe = "विष्णु (पालनकर्ता)",
            natureEn = "Jaya (Victory)",
            natureNe = "जया (विजय)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Ekadashi is the most spiritually significant tithi, sacred to Lord Vishnu. Fasting on this day is considered highly meritorious. Excellent for spiritual practices, but material activities should be minimized.",
            descriptionNe = "एकादशी आध्यात्मिक रूपमा सबैभन्दा महत्त्वपूर्ण तिथि हो, भगवान विष्णुलाई पवित्र। यस दिन उपवास अत्यन्त पुण्यकारी मानिन्छ। आध्यात्मिक अभ्यासका लागि उत्कृष्ट, तर भौतिक गतिविधि न्यूनतम पार्नुपर्छ।",
            activitiesEn = listOf("Fasting", "Vishnu worship", "Spiritual practices", "Meditation", "Charity"),
            activitiesNe = listOf("उपवास", "विष्णु पूजा", "आध्यात्मिक अभ्यास", "ध्यान", "दान"),
            avoidEn = listOf("Material pursuits", "Eating grains", "Worldly pleasures"),
            avoidNe = listOf("भौतिक अनुशीलन", "अन्न खाने", "सांसारिक भोग")
        )
        12, 27 -> TithiData(
            deityEn = "Vishnu (Preserver)",
            deityNe = "विष्णु (पालनकर्ता)",
            natureEn = "Bhadra (Auspicious)",
            natureNe = "भद्रा (शुभ)",
            quality = Quality.GOOD,
            descriptionEn = "Dwadashi follows Ekadashi and is auspicious for breaking the fast and religious ceremonies. Good for charitable activities, feeding Brahmins, and continuing spiritual practices.",
            descriptionNe = "द्वादशी एकादशी पछि आउँछ र बर्त तोड्न तथा धार्मिक समारोहका लागि शुभ छ। दान गतिविधि, ब्राह्मण भोजन र आध्यात्मिक अभ्यास जारी राख्न राम्रो।",
            activitiesEn = listOf("Breaking fast", "Religious ceremonies", "Charity", "Feeding others"),
            activitiesNe = listOf("बर्त तोड्ने", "धार्मिक समारोह", "दान", "अन्नदान"),
            avoidEn = listOf("Fasting continuation", "Heavy foods"),
            avoidNe = listOf("बर्त जारी राख्ने", "भारी खाना")
        )
        13, 28 -> TithiData(
            deityEn = "Kamadeva (God of Love)",
            deityNe = "कामदेव (प्रेम देवता)",
            natureEn = "Jaya (Victory)",
            natureNe = "जया (विजय)",
            quality = Quality.GOOD,
            descriptionEn = "Trayodashi is favorable for Shiva worship, especially on Maha Shivaratri. Good for love-related matters, arts, and activities bringing joy. The 13th tithi is associated with auspiciousness.",
            descriptionNe = "त्रयोदशी शिव पूजाका लागि अनुकूल छ, विशेषगरी महाशिवरात्रीमा। प्रेम सम्बन्धी मामिला, कला र आनन्द ल्याउने गतिविधिका लागि राम्रो। १३औं तिथि शुभताससँग सम्बन्धित छ।",
            activitiesEn = listOf("Shiva worship", "Romance", "Arts", "Music", "Celebrations"),
            activitiesNe = listOf("शिव पूजा", "प्रेम", "कला", "संगीत", "उत्सव"),
            avoidEn = listOf("Aggressive activities", "Conflicts"),
            avoidNe = listOf("आक्रामक गतिविधि", "द्वन्द्व")
        )
        14, 29 -> TithiData(
            deityEn = "Shiva/Kali",
            deityNe = "शिव/काली",
            natureEn = "Rikta (Empty)",
            natureNe = "रिक्ता (खाली)",
            quality = Quality.CHALLENGING,
            descriptionEn = "Chaturdashi is ruled by Shiva and Kali. While powerful for tantric practices and worship, it is considered challenging for worldly activities. Excellent for spiritual disciplines and removing negative energies.",
            descriptionNe = "चतुर्दशी शिव र कालीद्वारा शासित छ। तान्त्रिक अभ्यास र पूजाका लागि शक्तिशाली भए पनि, सांसारिक गतिविधिका लागि चुनौतीपूर्ण मानिन्छ। आध्यात्मिक अनुशासन र नकारात्मक ऊर्जा हटाउन उत्कृष्ट।",
            activitiesEn = listOf("Tantric practices", "Shiva/Kali worship", "Removing negativity", "Spiritual austerities"),
            activitiesNe = listOf("तान्त्रिक अभ्यास", "शिव/काली पूजा", "नकारात्मकता हटाउने", "आध्यात्मिक तपस्या"),
            avoidEn = listOf("New beginnings", "Auspicious ceremonies", "Travel"),
            avoidNe = listOf("नयाँ शुरुआत", "शुभ समारोह", "यात्रा")
        )
        15 -> TithiData(
            deityEn = "Chandra (Moon God)",
            deityNe = "चन्द्र (चन्द्रदेव)",
            natureEn = "Purna (Complete)",
            natureNe = "पूर्णा (पूर्ण)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Purnima (Full Moon) is the most auspicious tithi, representing completion and fullness. The Moon is at peak strength, making it excellent for all auspicious activities, spiritual practices, and celebrations.",
            descriptionNe = "पूर्णिमा सबैभन्दा शुभ तिथि हो, पूर्णता र परिपूर्णतालाई प्रतिनिधित्व गर्दछ। चन्द्रमा चरम शक्तिमा हुन्छ, सबै शुभ गतिविधि, आध्यात्मिक अभ्यास र उत्सवका लागि उत्कृष्ट बनाउँछ।",
            activitiesEn = listOf("All auspicious activities", "Celebrations", "Spiritual practices", "Charity", "Worship"),
            activitiesNe = listOf("सबै शुभ गतिविधि", "उत्सव", "आध्यात्मिक अभ्यास", "दान", "पूजा"),
            avoidEn = listOf("Surgery", "Activities requiring darkness"),
            avoidNe = listOf("शल्यक्रिया", "अँध्यारो चाहिने गतिविधि")
        )
        30 -> TithiData(
            deityEn = "Pitris (Ancestors)",
            deityNe = "पितृहरू (पुर्खा)",
            natureEn = "Purna (Complete)",
            natureNe = "पूर्णा (पूर्ण)",
            quality = Quality.NEUTRAL,
            descriptionEn = "Amavasya (New Moon) is sacred to the ancestors. While considered inauspicious for new beginnings, it is excellent for ancestral rites (Shraddha), Kali worship, and tantric practices. A time for introspection.",
            descriptionNe = "अमावस्या पितृहरूलाई पवित्र छ। नयाँ शुरुआतका लागि अशुभ मानिए पनि, पितृ श्राद्ध, काली पूजा र तान्त्रिक अभ्यासका लागि उत्कृष्ट छ। आत्मनिरीक्षणको समय।",
            activitiesEn = listOf("Ancestral rites", "Kali worship", "Tantric practices", "Introspection", "Shadow work"),
            activitiesNe = listOf("पितृ श्राद्ध", "काली पूजा", "तान्त्रिक अभ्यास", "आत्मनिरीक्षण", "छाया कार्य"),
            avoidEn = listOf("New beginnings", "Auspicious ceremonies", "Travel", "Important decisions"),
            avoidNe = listOf("नयाँ शुरुआत", "शुभ समारोह", "यात्रा", "महत्त्वपूर्ण निर्णय")
        )
        else -> TithiData(
            deityEn = "Various",
            deityNe = "विभिन्न",
            natureEn = "Mixed",
            natureNe = "मिश्रित",
            quality = Quality.NEUTRAL,
            descriptionEn = "This tithi carries mixed influences based on various planetary factors at the time.",
            descriptionNe = "यो तिथिले समयको विभिन्न ग्रह कारकहरूमा आधारित मिश्रित प्रभाव बोकेको छ।",
            activitiesEn = listOf("General activities"),
            activitiesNe = listOf("सामान्य गतिविधि"),
            avoidEn = emptyList(),
            avoidNe = emptyList()
        )
    }

    // ============================================
    // VARA (WEEKDAY) DATA
    // ============================================

    data class VaraData(
        val rulerEn: String,
        val rulerNe: String,
        val quality: Quality,
        val descriptionEn: String,
        val descriptionNe: String,
        val activitiesEn: List<String>,
        val activitiesNe: List<String>,
        val avoidEn: List<String>,
        val avoidNe: List<String>
    ) {
        fun getRuler(language: Language): String = when (language) {
            Language.ENGLISH -> rulerEn
            Language.NEPALI -> rulerNe
        }

        fun getDescription(language: Language): String = when (language) {
            Language.ENGLISH -> descriptionEn
            Language.NEPALI -> descriptionNe
        }

        fun getActivities(language: Language): List<String> = when (language) {
            Language.ENGLISH -> activitiesEn
            Language.NEPALI -> activitiesNe
        }

        fun getAvoid(language: Language): List<String> = when (language) {
            Language.ENGLISH -> avoidEn
            Language.NEPALI -> avoidNe
        }
    }

    /**
     * Get Vara data for a given day of week (1=Sunday, 7=Saturday)
     */
    fun getVaraData(dayOfWeek: Int): VaraData = when (dayOfWeek) {
        1 -> VaraData( // Sunday
            rulerEn = "Sun (Surya)",
            rulerNe = "सूर्य",
            quality = Quality.GOOD,
            descriptionEn = "Sunday is ruled by the Sun, representing authority, vitality, and self-expression. It favors government dealings, leadership activities, and matters related to father or authority figures.",
            descriptionNe = "आइतबार सूर्यद्वारा शासित छ, अधिकार, जीवनशक्ति र आत्म-अभिव्यक्तिको प्रतिनिधित्व गर्दछ। सरकारी कार्य, नेतृत्व गतिविधि र पिता वा अधिकारी सम्बन्धी मामिलामा अनुकूल।",
            activitiesEn = listOf("Government work", "Authority matters", "Health treatments", "Starting ventures", "Leadership"),
            activitiesNe = listOf("सरकारी कार्य", "अधिकार मामिला", "स्वास्थ्य उपचार", "कार्य शुरु", "नेतृत्व"),
            avoidEn = listOf("Night activities", "Moon-related work"),
            avoidNe = listOf("रातका गतिविधि", "चन्द्र सम्बन्धी कार्य")
        )
        2 -> VaraData( // Monday
            rulerEn = "Moon (Chandra)",
            rulerNe = "चन्द्र",
            quality = Quality.EXCELLENT,
            descriptionEn = "Monday is ruled by the Moon, governing emotions, mind, and nurturing. Excellent for domestic activities, mother-related matters, and activities requiring public support or emotional connection.",
            descriptionNe = "सोमबार चन्द्रद्वारा शासित छ, भावना, मन र पालनपोषण नियन्त्रण गर्दछ। घरायसी गतिविधि, आमा सम्बन्धी मामिला र सार्वजनिक समर्थन वा भावनात्मक सम्पर्क चाहिने गतिविधिका लागि उत्कृष्ट।",
            activitiesEn = listOf("Domestic activities", "Water-related work", "Public dealings", "Travel", "Agriculture"),
            activitiesNe = listOf("घरायसी गतिविधि", "पानी सम्बन्धी कार्य", "सार्वजनिक व्यवहार", "यात्रा", "कृषि"),
            avoidEn = listOf("Fire-related work", "Aggressive activities"),
            avoidNe = listOf("आगो सम्बन्धी कार्य", "आक्रामक गतिविधि")
        )
        3 -> VaraData( // Tuesday
            rulerEn = "Mars (Mangal)",
            rulerNe = "मंगल",
            quality = Quality.NEUTRAL,
            descriptionEn = "Tuesday is ruled by Mars, representing energy, courage, and action. Good for activities requiring physical strength, competition, and dealing with property or siblings. Use Mars energy constructively.",
            descriptionNe = "मंगलबार मंगलद्वारा शासित छ, ऊर्जा, साहस र कार्यको प्रतिनिधित्व गर्दछ। शारीरिक शक्ति, प्रतिस्पर्धा र सम्पत्ति वा भाइबहिनी सम्बन्धी कार्यका लागि राम्रो। मंगल ऊर्जा रचनात्मक रूपमा प्रयोग गर्नुहोस्।",
            activitiesEn = listOf("Property deals", "Surgery", "Competition", "Physical activities", "Courage-requiring work"),
            activitiesNe = listOf("सम्पत्ति कारोबार", "शल्यक्रिया", "प्रतिस्पर्धा", "शारीरिक गतिविधि", "साहस चाहिने कार्य"),
            avoidEn = listOf("Marriage", "New partnerships", "Peaceful negotiations"),
            avoidNe = listOf("विवाह", "नयाँ साझेदारी", "शान्तिपूर्ण वार्ता")
        )
        4 -> VaraData( // Wednesday
            rulerEn = "Mercury (Budha)",
            rulerNe = "बुध",
            quality = Quality.EXCELLENT,
            descriptionEn = "Wednesday is ruled by Mercury, governing intellect, communication, and commerce. Excellent for business, education, writing, and any intellectual pursuits. Favorable for learning and trade.",
            descriptionNe = "बुधबार बुधद्वारा शासित छ, बुद्धि, सञ्चार र वाणिज्य नियन्त्रण गर्दछ। व्यापार, शिक्षा, लेखन र कुनै पनि बौद्धिक अनुशीलनका लागि उत्कृष्ट। सिकाइ र व्यापारका लागि अनुकूल।",
            activitiesEn = listOf("Business", "Education", "Writing", "Communication", "Trade", "Learning"),
            activitiesNe = listOf("व्यापार", "शिक्षा", "लेखन", "सञ्चार", "व्यापार", "सिकाइ"),
            avoidEn = listOf("Emotional decisions", "Long-term commitments"),
            avoidNe = listOf("भावनात्मक निर्णय", "दीर्घकालीन प्रतिबद्धता")
        )
        5 -> VaraData( // Thursday
            rulerEn = "Jupiter (Guru)",
            rulerNe = "गुरु (बृहस्पति)",
            quality = Quality.EXCELLENT,
            descriptionEn = "Thursday is ruled by Jupiter, the great benefic. The most auspicious day for religious ceremonies, education, marriage, and any important beginnings. Associated with wisdom, expansion, and good fortune.",
            descriptionNe = "बिहीबार महाशुभ गुरुद्वारा शासित छ। धार्मिक समारोह, शिक्षा, विवाह र कुनै पनि महत्त्वपूर्ण शुरुआतका लागि सबैभन्दा शुभ दिन। ज्ञान, विस्तार र सुभाग्यसँग सम्बन्धित।",
            activitiesEn = listOf("Religious ceremonies", "Marriage", "Education", "Major decisions", "Charity", "Expansion"),
            activitiesNe = listOf("धार्मिक समारोह", "विवाह", "शिक्षा", "प्रमुख निर्णय", "दान", "विस्तार"),
            avoidEn = listOf("Mundane activities", "Restrictive actions"),
            avoidNe = listOf("सामान्य गतिविधि", "प्रतिबन्धात्मक कार्य")
        )
        6 -> VaraData( // Friday
            rulerEn = "Venus (Shukra)",
            rulerNe = "शुक्र",
            quality = Quality.EXCELLENT,
            descriptionEn = "Friday is ruled by Venus, governing love, beauty, and material comforts. Excellent for marriage, artistic pursuits, luxury purchases, and activities bringing pleasure. Associated with harmony and relationships.",
            descriptionNe = "शुक्रबार शुक्रद्वारा शासित छ, प्रेम, सौन्दर्य र भौतिक सुविधा नियन्त्रण गर्दछ। विवाह, कलात्मक अनुशीलन, विलासी खरिद र आनन्द ल्याउने गतिविधिका लागि उत्कृष्ट। सद्भाव र सम्बन्धसँग सम्बन्धित।",
            activitiesEn = listOf("Marriage", "Arts", "Beauty treatments", "Luxury purchases", "Romance", "Entertainment"),
            activitiesNe = listOf("विवाह", "कला", "सौन्दर्य उपचार", "विलासी खरिद", "प्रेम", "मनोरञ्जन"),
            avoidEn = listOf("Austerity", "Conflicts", "Harsh activities"),
            avoidNe = listOf("तपस्या", "द्वन्द्व", "कठोर गतिविधि")
        )
        7 -> VaraData( // Saturday
            rulerEn = "Saturn (Shani)",
            rulerNe = "शनि",
            quality = Quality.CHALLENGING,
            descriptionEn = "Saturday is ruled by Saturn, representing discipline, karma, and hard work. While challenging for new beginnings, it is good for completing tasks, discipline-related activities, and serving others. A day for patience.",
            descriptionNe = "शनिबार शनिद्वारा शासित छ, अनुशासन, कर्म र कठोर परिश्रमको प्रतिनिधित्व गर्दछ। नयाँ शुरुआतका लागि चुनौतीपूर्ण भए पनि, कार्य पूरा गर्न, अनुशासन सम्बन्धी गतिविधि र अरूको सेवाका लागि राम्रो। धैर्यको दिन।",
            activitiesEn = listOf("Completing tasks", "Service", "Iron/oil work", "Land matters", "Discipline"),
            activitiesNe = listOf("कार्य पूरा गर्ने", "सेवा", "फलाम/तेल कार्य", "जमिन मामिला", "अनुशासन"),
            avoidEn = listOf("New beginnings", "Marriage", "Travel", "Auspicious ceremonies"),
            avoidNe = listOf("नयाँ शुरुआत", "विवाह", "यात्रा", "शुभ समारोह")
        )
        else -> VaraData(
            rulerEn = "Various",
            rulerNe = "विभिन्न",
            quality = Quality.NEUTRAL,
            descriptionEn = "General weekday.",
            descriptionNe = "सामान्य वार।",
            activitiesEn = emptyList(),
            activitiesNe = emptyList(),
            avoidEn = emptyList(),
            avoidNe = emptyList()
        )
    }

    // ============================================
    // NAKSHATRA DATA
    // ============================================

    data class LocalizedNakshatraData(
        val sanskrit: String,
        val deityEn: String,
        val deityNe: String,
        val symbolEn: String,
        val symbolNe: String,
        val animalEn: String,
        val animalNe: String,
        val gana: String,
        val guna: String,
        val quality: Quality,
        val descriptionEn: String,
        val descriptionNe: String
    ) {
        fun getDeity(language: Language): String = if (language == Language.NEPALI) deityNe else deityEn
        fun getSymbol(language: Language): String = if (language == Language.NEPALI) symbolNe else symbolEn
        fun getAnimal(language: Language): String = if (language == Language.NEPALI) animalNe else animalEn
        fun getDescription(language: Language): String = if (language == Language.NEPALI) descriptionNe else descriptionEn
    }

    /**
     * Get Nakshatra data for a given Nakshatra
     */
    fun getNakshatraData(nakshatra: com.astro.storm.core.model.Nakshatra): LocalizedNakshatraData {
        return when (nakshatra) {
            com.astro.storm.core.model.Nakshatra.ASHWINI -> LocalizedNakshatraData(
                sanskrit = "अश्विनी",
                deityEn = "Ashwini Kumaras (Physicians of Gods)",
                deityNe = "अश्विनी कुमार (देवताहरूका चिकित्सक)",
                symbolEn = "Horse's Head",
                symbolNe = "घोडाको टाउको",
                animalEn = "Male Horse",
                animalNe = "भाले घोडा",
                gana = "Deva",
                guna = "Sattva",
                quality = Quality.EXCELLENT,
                descriptionEn = "Ashwini represents speed, healing, and initiation. Those born under this nakshatra are often quick-witted, adventurous, and have natural healing abilities. It is the first nakshatra, marking the beginning of the zodiac journey.",
                descriptionNe = "अश्विनीले गति, उपचार र दीक्षालाई प्रतिनिधित्व गर्दछ। यस नक्षत्रमा जन्मेका व्यक्तिहरू प्रायः तीक्ष्ण बुद्धिका, साहसी र प्राकृतिक उपचार क्षमता भएका हुन्छन्। यो पहिलो नक्षत्र हो, जसले राशि यात्राको शुरुवात गर्दछ।"
            )
            com.astro.storm.core.model.Nakshatra.BHARANI -> LocalizedNakshatraData(
                sanskrit = "भरणी",
                deityEn = "Yama (God of Death/Dharma)",
                deityNe = "यम (मृत्यु/धर्मका देवता)",
                symbolEn = "Yoni (Female Reproductive Organ)",
                symbolNe = "योनि",
                animalEn = "Male Elephant",
                animalNe = "भाले हात्ती",
                gana = "Manushya",
                guna = "Rajas",
                quality = Quality.NEUTRAL,
                descriptionEn = "Bharani represents birth, transformation, and endurance. It carries the energy of processing and bringing things to fruition. People born here are often intense, creative, and capable of deep transformation.",
                descriptionNe = "भरणीले जन्म, रूपान्तरण र सहनशीलतालाई प्रतिनिधित्व गर्दछ। यसले प्रक्रिया र कुराहरूलाई फलदायी बनाउने ऊर्जा बोकेको हुन्छ। यहाँ जन्मेका मानिसहरू प्रायः तीव्र, रचनात्मक र गहिरो रूपान्तरण गर्न सक्षम हुन्छन्।"
            )
            // Add more entries as needed or default case for all 27
            else -> LocalizedNakshatraData(
                sanskrit = nakshatra.displayName,
                deityEn = "Vedic Deity",
                deityNe = "वैदिक देवता",
                symbolEn = "Sacred Symbol",
                symbolNe = "पवित्र प्रतीक",
                animalEn = "Sacred Animal",
                animalNe = "पवित्र पशु",
                gana = "Manushya",
                guna = "Rajas",
                quality = Quality.NEUTRAL,
                descriptionEn = "This nakshatra carries unique cosmic influences that shape personality and life path. Its energy provides specific strengths and lessons for the soul's evolution.",
                descriptionNe = "यस नक्षत्रले व्यक्तित्व र जीवन मार्गलाई आकार दिने अद्वितीय ब्रह्माण्डीय प्रभावहरू बोकेको छ। यसको ऊर्जाले आत्माको विकासको लागि विशिष्ट शक्ति र पाठहरू प्रदान गर्दछ।"
            )
        }
    }

    // ============================================
    // YOGA DATA
    // ============================================

    data class LocalizedYogaData(
        val sanskrit: String,
        val number: Int,
        val meaningEn: String,
        val meaningNe: String,
        val quality: Quality,
        val descriptionEn: String,
        val descriptionNe: String
    ) {
        fun getMeaning(language: Language): String = if (language == Language.NEPALI) meaningNe else meaningEn
        fun getDescription(language: Language): String = if (language == Language.NEPALI) descriptionNe else descriptionEn
    }

    /**
     * Get Yoga data for a given Yoga
     */
    fun getYogaData(yoga: com.astro.storm.ephemeris.panchanga.Yoga): LocalizedYogaData {
        return when (yoga) {
            com.astro.storm.ephemeris.panchanga.Yoga.VISHKUMBHA -> LocalizedYogaData(
                sanskrit = "विष्कुम्भ",
                number = 1,
                meaningEn = "Pot of Poison",
                meaningNe = "विषको भाँडो",
                quality = Quality.CHALLENGING,
                descriptionEn = "Vishkumbha indicates overcoming challenges and potential obstacles. While it sounds difficult, it provides the strength to face adversities and emerge stronger.",
                descriptionNe = "विष्कुम्भले चुनौतीहरू र सम्भावित बाधाहरूलाई पार गर्ने संकेत गर्दछ। यो सुन्दा कठिन लागे पनि, यसले प्रतिकूलताहरूको सामना गर्ने र बलियो भएर निस्कने शक्ति प्रदान गर्दछ।"
            )
            com.astro.storm.ephemeris.panchanga.Yoga.PRITI -> LocalizedYogaData(
                sanskrit = "प्रीति",
                number = 2,
                meaningEn = "Affection/Love",
                meaningNe = "प्रेम/सद्भाव",
                quality = Quality.EXCELLENT,
                descriptionEn = "Priti represents love, affection, and harmonious relationships. It is highly auspicious for social activities, building connections, and creative work.",
                descriptionNe = "प्रीतिले प्रेम, स्नेह र सामंजस्यपूर्ण सम्बन्धलाई प्रतिनिधित्व गर्दछ। यो सामाजिक गतिविधि, सम्बन्ध निर्माण र सृजनात्मक कार्यका लागि अत्यन्त शुभ छ।"
            )
            else -> LocalizedYogaData(
                sanskrit = yoga.sanskrit,
                number = yoga.number,
                meaningEn = "Vedic Yoga",
                meaningNe = "वैदिक योग",
                quality = Quality.NEUTRAL,
                descriptionEn = "This yoga represents a specific luni-solar combination that influences the overall quality of time and individual tendencies.",
                descriptionNe = "यो योगले समयको समग्र गुणस्तर र व्यक्तिगत प्रवृत्तिलाई प्रभाव पार्ने एक विशिष्ट चन्द्र-सूर्य संयोजनलाई प्रतिनिधित्व गर्दछ।"
            )
        }
    }

    // ============================================
    // KARANA DATA
    // ============================================

    data class LocalizedKaranaData(
        val sanskrit: String,
        val number: Int,
        val typeEn: String,
        val typeNe: String,
        val quality: Quality,
        val descriptionEn: String,
        val descriptionNe: String
    ) {
        fun getType(language: Language): String = if (language == Language.NEPALI) typeNe else typeEn
        fun getDescription(language: Language): String = if (language == Language.NEPALI) descriptionNe else descriptionEn
    }

    /**
     * Get Karana data for a given Karana
     */
    fun getKaranaData(karana: com.astro.storm.ephemeris.panchanga.Karana): LocalizedKaranaData {
        val number = com.astro.storm.ephemeris.panchanga.Karana.entries.indexOf(karana) + 1
        return when (karana) {
            com.astro.storm.ephemeris.panchanga.Karana.BAVA -> LocalizedKaranaData(
                sanskrit = "बव",
                number = number,
                typeEn = "Movable",
                typeNe = "चर",
                quality = Quality.EXCELLENT,
                descriptionEn = "Bava is favorable for all auspicious activities, growth, and development. It supports constructive efforts and brings positive outcomes.",
                descriptionNe = "बव सबै शुभ गतिविधि, वृद्धि र विकासका लागि अनुकूल छ। यसले निर्माणात्मक प्रयासहरूलाई समर्थन गर्दछ र सकारात्मक परिणामहरू ल्याउँछ।"
            )
            com.astro.storm.ephemeris.panchanga.Karana.BALAVA -> LocalizedKaranaData(
                sanskrit = "बालव",
                number = number,
                typeEn = "Movable",
                typeNe = "चर",
                quality = Quality.GOOD,
                descriptionEn = "Balava is excellent for spiritual activities, religious ceremonies, and matters requiring wisdom and purity.",
                descriptionNe = "बालव आध्यात्मिक गतिविधि, धार्मिक समारोह र ज्ञान तथा शुद्धता आवश्यक पर्ने मामिलाहरूका लागि उत्कृष्ट छ।"
            )
            else -> LocalizedKaranaData(
                sanskrit = karana.sanskrit,
                number = number,
                typeEn = karana.type.displayName,
                typeNe = if (karana.type == com.astro.storm.ephemeris.panchanga.KaranaType.FIXED) "स्थिर" else "चर",
                quality = Quality.NEUTRAL,
                descriptionEn = "This karana represents half a tithi and influences specific types of activities and outcomes.",
                descriptionNe = "यो करणले आधा तिथिलाई प्रतिनिधित्व गर्दछ र विशिष्ट प्रकारका गतिविधि र परिणामहरूलाई प्रभाव पार्दछ।"
            )
        }
    }

    // ============================================
    // GANA DATA (for Nakshatra)
    // ============================================

    /**
     * Get localized Gana name
     */
    fun getGanaName(gana: String, language: Language): String = when (gana.lowercase()) {
        "deva", "देव" -> when (language) {
            Language.ENGLISH -> "Deva (Divine)"
            Language.NEPALI -> "देव (दैवीय)"
        }
        "manushya", "मनुष्य" -> when (language) {
            Language.ENGLISH -> "Manushya (Human)"
            Language.NEPALI -> "मनुष्य (मानवीय)"
        }
        "rakshasa", "राक्षस" -> when (language) {
            Language.ENGLISH -> "Rakshasa (Demonic)"
            Language.NEPALI -> "राक्षस (आसुरी)"
        }
        else -> gana
    }

    /**
     * Get localized Guna name
     */
    fun getGunaName(guna: String, language: Language): String = when (guna.lowercase()) {
        "sattva", "सत्व" -> when (language) {
            Language.ENGLISH -> "Sattva (Pure)"
            Language.NEPALI -> "सत्व (शुद्ध)"
        }
        "rajas", "रजस्" -> when (language) {
            Language.ENGLISH -> "Rajas (Active)"
            Language.NEPALI -> "रजस् (सक्रिय)"
        }
        "tamas", "तमस्" -> when (language) {
            Language.ENGLISH -> "Tamas (Inert)"
            Language.NEPALI -> "तमस् (जड)"
        }
        else -> guna
    }
}

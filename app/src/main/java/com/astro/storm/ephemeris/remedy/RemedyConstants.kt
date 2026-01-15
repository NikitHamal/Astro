package com.astro.storm.ephemeris.remedy

import com.astro.storm.core.common.StringKeyRemedy
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign

object RemedyConstants {

    data class GemstoneInfo(
        val primaryName: String,
        val hindiName: String,
        val colors: String,
        val metal: String,
        val minCarat: Double,
        val maxCarat: Double,
        val alternativeName: String,
        val alternativeHindiName: String,
        val fingerName: String,
        val dayToWear: String,
        val muhurtaTiming: String
    )

    data class MantraInfo(
        val beejMantra: String,
        val beejMantraSanskrit: String,
        val gayatriMantra: String,
        val gayatriMantraSanskrit: String,
        val minimumCount: Int,
        val timing: String,
        val direction: String
    )

    data class CharityInfo(
        val items: List<String>,
        val day: String,
        val recipients: String,
        val timing: String,
        val specialInstructions: String
    )

    data class ExaltationDebilitationInfo(
        val exaltationSign: ZodiacSign,
        val exaltationDegree: Double,
        val debilitationSign: ZodiacSign,
        val debilitationDegree: Double,
        val mooltrikonaSign: ZodiacSign,
        val mooltrikonaStartDegree: Double,
        val mooltrikonaEndDegree: Double
    )

    val exaltationDebilitationData = mapOf(
        Planet.SUN to ExaltationDebilitationInfo(
            ZodiacSign.ARIES, 10.0,
            ZodiacSign.LIBRA, 10.0,
            ZodiacSign.LEO, 0.0, 20.0
        ),
        Planet.MOON to ExaltationDebilitationInfo(
            ZodiacSign.TAURUS, 3.0,
            ZodiacSign.SCORPIO, 3.0,
            ZodiacSign.TAURUS, 3.0, 27.0
        ),
        Planet.MARS to ExaltationDebilitationInfo(
            ZodiacSign.CAPRICORN, 28.0,
            ZodiacSign.CANCER, 28.0,
            ZodiacSign.ARIES, 0.0, 12.0
        ),
        Planet.MERCURY to ExaltationDebilitationInfo(
            ZodiacSign.VIRGO, 15.0,
            ZodiacSign.PISCES, 15.0,
            ZodiacSign.VIRGO, 15.0, 20.0
        ),
        Planet.JUPITER to ExaltationDebilitationInfo(
            ZodiacSign.CANCER, 5.0,
            ZodiacSign.CAPRICORN, 5.0,
            ZodiacSign.SAGITTARIUS, 0.0, 10.0
        ),
        Planet.VENUS to ExaltationDebilitationInfo(
            ZodiacSign.PISCES, 27.0,
            ZodiacSign.VIRGO, 27.0,
            ZodiacSign.LIBRA, 0.0, 15.0
        ),
        Planet.SATURN to ExaltationDebilitationInfo(
            ZodiacSign.LIBRA, 20.0,
            ZodiacSign.ARIES, 20.0,
            ZodiacSign.AQUARIUS, 0.0, 20.0
        ),
        Planet.RAHU to ExaltationDebilitationInfo(
            ZodiacSign.TAURUS, 20.0,
            ZodiacSign.SCORPIO, 20.0,
            ZodiacSign.VIRGO, 0.0, 30.0
        ),
        Planet.KETU to ExaltationDebilitationInfo(
            ZodiacSign.SCORPIO, 20.0,
            ZodiacSign.TAURUS, 20.0,
            ZodiacSign.PISCES, 0.0, 30.0
        )
    )

    val mrityuBhagaDegrees = mapOf(
        ZodiacSign.ARIES to mapOf(Planet.SUN to 20.0, Planet.MOON to 26.0, Planet.MARS to 19.0, Planet.MERCURY to 15.0, Planet.JUPITER to 18.0, Planet.VENUS to 28.0, Planet.SATURN to 10.0),
        ZodiacSign.TAURUS to mapOf(Planet.SUN to 9.0, Planet.MOON to 12.0, Planet.MARS to 28.0, Planet.MERCURY to 14.0, Planet.JUPITER to 20.0, Planet.VENUS to 15.0, Planet.SATURN to 23.0),
        ZodiacSign.GEMINI to mapOf(Planet.SUN to 12.0, Planet.MOON to 13.0, Planet.MARS to 25.0, Planet.MERCURY to 13.0, Planet.JUPITER to 19.0, Planet.VENUS to 13.0, Planet.SATURN to 22.0),
        ZodiacSign.CANCER to mapOf(Planet.SUN to 6.0, Planet.MOON to 25.0, Planet.MARS to 23.0, Planet.MERCURY to 12.0, Planet.JUPITER to 10.0, Planet.VENUS to 6.0, Planet.SATURN to 21.0),
        ZodiacSign.LEO to mapOf(Planet.SUN to 8.0, Planet.MOON to 24.0, Planet.MARS to 23.0, Planet.MERCURY to 11.0, Planet.JUPITER to 9.0, Planet.VENUS to 4.0, Planet.SATURN to 20.0),
        ZodiacSign.VIRGO to mapOf(Planet.SUN to 24.0, Planet.MOON to 11.0, Planet.MARS to 22.0, Planet.MERCURY to 10.0, Planet.JUPITER to 8.0, Planet.VENUS to 1.0, Planet.SATURN to 19.0),
        ZodiacSign.LIBRA to mapOf(Planet.SUN to 17.0, Planet.MOON to 26.0, Planet.MARS to 21.0, Planet.MERCURY to 9.0, Planet.JUPITER to 11.0, Planet.VENUS to 29.0, Planet.SATURN to 18.0),
        ZodiacSign.SCORPIO to mapOf(Planet.SUN to 22.0, Planet.MOON to 27.0, Planet.MARS to 20.0, Planet.MERCURY to 8.0, Planet.JUPITER to 12.0, Planet.VENUS to 5.0, Planet.SATURN to 17.0),
        ZodiacSign.SAGITTARIUS to mapOf(Planet.SUN to 21.0, Planet.MOON to 6.0, Planet.MARS to 10.0, Planet.MERCURY to 7.0, Planet.JUPITER to 20.0, Planet.VENUS to 8.0, Planet.SATURN to 16.0),
        ZodiacSign.CAPRICORN to mapOf(Planet.SUN to 16.0, Planet.MOON to 25.0, Planet.MARS to 11.0, Planet.MERCURY to 6.0, Planet.JUPITER to 22.0, Planet.VENUS to 14.0, Planet.SATURN to 15.0),
        ZodiacSign.AQUARIUS to mapOf(Planet.SUN to 15.0, Planet.MOON to 5.0, Planet.MARS to 12.0, Planet.MERCURY to 5.0, Planet.JUPITER to 2.0, Planet.VENUS to 20.0, Planet.SATURN to 14.0),
        ZodiacSign.PISCES to mapOf(Planet.SUN to 10.0, Planet.MOON to 12.0, Planet.MARS to 13.0, Planet.MERCURY to 4.0, Planet.JUPITER to 1.0, Planet.VENUS to 26.0, Planet.SATURN to 13.0)
    )

    val combustionDegrees = mapOf(
        Planet.MOON to 12.0,
        Planet.MARS to 17.0,
        Planet.MERCURY to Pair(14.0, 12.0),
        Planet.JUPITER to 11.0,
        Planet.VENUS to Pair(10.0, 8.0),
        Planet.SATURN to 15.0
    )

    val planetaryGemstones = mapOf(
        Planet.SUN to GemstoneInfo(
            "Ruby", "Manikya", "Pigeon blood red, Pink-red", "Gold (22K)", 3.0, 5.0,
            "Red Garnet/Red Spinel", "Lal", "Ring finger", "Sunday",
            "Sunrise, during Sun Hora"
        ),
        Planet.MOON to GemstoneInfo(
            "Natural Pearl", "Moti", "White, Cream with orient", "Silver", 4.0, 7.0,
            "Moonstone", "Chandrakant Mani", "Little finger", "Monday",
            "Evening, during Moon Hora, Shukla Paksha"
        ),
        Planet.MARS to GemstoneInfo(
            "Red Coral", "Moonga", "Ox-blood red, Orange-red", "Gold/Copper", 5.0, 9.0,
            "Carnelian/Red Jasper", "Lal Hakik", "Ring finger", "Tuesday",
            "Morning, during Mars Hora"
        ),
        Planet.MERCURY to GemstoneInfo(
            "Emerald", "Panna", "Deep green with jardine", "Gold", 3.0, 6.0,
            "Peridot/Green Tourmaline", "Zabarjad", "Little finger", "Wednesday",
            "Morning, during Mercury Hora"
        ),
        Planet.JUPITER to GemstoneInfo(
            "Yellow Sapphire", "Pukhraj", "Golden yellow, Canary", "Gold (22K)", 3.0, 5.0,
            "Yellow Topaz/Citrine", "Sunehla", "Index finger", "Thursday",
            "Morning, during Jupiter Hora"
        ),
        Planet.VENUS to GemstoneInfo(
            "Diamond", "Heera", "Colorless, D-F color", "Platinum/White Gold", 0.5, 1.5,
            "White Sapphire/White Zircon", "Safed Pukhraj", "Middle/Little finger", "Friday",
            "Morning, during Venus Hora"
        ),
        Planet.SATURN to GemstoneInfo(
            "Blue Sapphire", "Neelam", "Cornflower blue, Royal blue", "Gold/Panch Dhatu", 3.0, 5.0,
            "Amethyst/Lapis Lazuli", "Jamunia", "Middle finger", "Saturday",
            "Evening, during Saturn Hora"
        ),
        Planet.RAHU to GemstoneInfo(
            "Hessonite Garnet", "Gomed", "Honey-colored, Cinnamon", "Silver/Ashtadhatu", 5.0, 8.0,
            "Orange Zircon", "Zarkon", "Middle finger", "Saturday",
            "Night, during Rahu Kaal (for propitiation)"
        ),
        Planet.KETU to GemstoneInfo(
            "Cat's Eye Chrysoberyl", "Lahsuniya", "Greenish-yellow with chatoyancy", "Silver/Gold", 3.0, 5.0,
            "Tiger's Eye", "Billori", "Middle finger", "Tuesday",
            "During Ketu's nakshatra days"
        )
    )

    val planetaryMantras = mapOf(
        Planet.SUN to MantraInfo(
            "Om Hraam Hreem Hraum Sah Suryaya Namaha",
            "ॐ ह्रां ह्रीं ह्रौं सः सूर्याय नमः",
            "Om Bhaskaraya Vidmahe Divyakaraya Dhimahi Tanno Surya Prachodayat",
            "ॐ भास्कराय विद्महे दिव्यकाराय धीमहि तन्नो सूर्यः प्रचोदयात्",
            7000,
            "Sunday at sunrise, facing East",
            "East"
        ),
        Planet.MOON to MantraInfo(
            "Om Shraam Shreem Shraum Sah Chandraya Namaha",
            "ॐ श्रां श्रीं श्रौं सः चन्द्राय नमः",
            "Om Kshirputraya Vidmahe Amrittatvaya Dhimahi Tanno Chandra Prachodayat",
            "ॐ क्षीरपुत्राय विद्महे अमृतत्त्वाय धीमहि तन्नो चन्द्रः प्रचोदयात्",
            11000,
            "Monday evening, during Shukla Paksha",
            "North-West"
        ),
        Planet.MARS to MantraInfo(
            "Om Kraam Kreem Kraum Sah Bhaumaya Namaha",
            "ॐ क्रां क्रीं क्रौं सः भौमाय नमः",
            "Om Angarakaya Vidmahe Shakti Hastaya Dhimahi Tanno Bhauma Prachodayat",
            "ॐ अंगारकाय विद्महे शक्तिहस्ताय धीमहि तन्नो भौमः प्रचोदयात्",
            10000,
            "Tuesday morning, facing South",
            "South"
        ),
        Planet.MERCURY to MantraInfo(
            "Om Braam Breem Braum Sah Budhaya Namaha",
            "ॐ ब्रां ब्रीं ब्रौं सः बुधाय नमः",
            "Om Gajadhvajaya Vidmahe Graha Rajaya Dhimahi Tanno Budha Prachodayat",
            "ॐ गजध्वजाय विद्महे ग्रहराजाय धीमहि तन्नो बुधः प्रचोदयात्",
            9000,
            "Wednesday morning, facing North",
            "North"
        ),
        Planet.JUPITER to MantraInfo(
            "Om Graam Greem Graum Sah Gurave Namaha",
            "ॐ ग्रां ग्रीं ग्रौं सः गुरवे नमः",
            "Om Vrishabadhvajaya Vidmahe Kruni Hastaya Dhimahi Tanno Guru Prachodayat",
            "ॐ वृषभध्वजाय विद्महे क्रुणिहस्ताय धीमहि तन्नो गुरुः प्रचोदयात्",
            19000,
            "Thursday morning, facing North-East",
            "North-East"
        ),
        Planet.VENUS to MantraInfo(
            "Om Draam Dreem Draum Sah Shukraya Namaha",
            "ॐ द्रां द्रीं द्रौं सः शुक्राय नमः",
            "Om Rajadabaaya Vidmahe Brigusuthaya Dhimahi Tanno Shukra Prachodayat",
            "ॐ राजदाबाय विद्महे भृगुसुताय धीमहि तन्नो शुक्रः प्रचोदयात्",
            16000,
            "Friday morning, facing East",
            "South-East"
        ),
        Planet.SATURN to MantraInfo(
            "Om Praam Preem Praum Sah Shanaischaraya Namaha",
            "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः",
            "Om Kakadvajaya Vidmahe Khadga Hastaya Dhimahi Tanno Manda Prachodayat",
            "ॐ काकध्वजाय विद्महे खड्गहस्ताय धीमहि तन्नो मन्दः प्रचोदयात्",
            23000,
            "Saturday evening, facing West",
            "West"
        ),
        Planet.RAHU to MantraInfo(
            "Om Bhraam Bhreem Bhraum Sah Rahave Namaha",
            "ॐ भ्रां भ्रीं भ्रौं सः राहवे नमः",
            "Om Naakadhvajaya Vidmahe Padma Hastaya Dhimahi Tanno Rahu Prachodayat",
            "ॐ नाकध्वजाय विद्महे पद्महस्ताय धीमहि तन्नो राहुः प्रचोदयात्",
            18000,
            "Saturday night or during Rahu Kaal",
            "South-West"
        ),
        Planet.KETU to MantraInfo(
            "Om Sraam Sreem Sraum Sah Ketave Namaha",
            "ॐ स्रां स्रीं स्रौं सः केतवे नमः",
            "Om Chitravarnaya Vidmahe Sarpa Roopaya Dhimahi Tanno Ketu Prachodayat",
            "ॐ चित्रवर्णाय विद्महे सर्परूपाय धीमहि तन्नो केतुः प्रचोदयात्",
            17000,
            "Tuesday or during Ketu's nakshatra",
            "South-West"
        )
    )

    val planetaryCharity = mapOf(
        Planet.SUN to CharityInfo(
            listOf("Wheat", "Jaggery (Gur)", "Copper vessel", "Red/Orange cloth", "Gold"),
            "Sunday",
            "Father figures, government servants, temples",
            "Before sunset",
            "Offer water to Sun at sunrise with copper vessel"
        ),
        Planet.MOON to CharityInfo(
            listOf("Rice", "White cloth", "Silver", "Milk", "Curd", "White flowers"),
            "Monday",
            "Mother figures, elderly women, pilgrims",
            "Evening",
            "Donate near water bodies; offer milk to Shivling"
        ),
        Planet.MARS to CharityInfo(
            listOf("Red lentils (Masoor dal)", "Red cloth", "Copper", "Jaggery", "Wheat bread"),
            "Tuesday",
            "Young men, soldiers, brothers, Hanuman temples",
            "Morning",
            "Donate at Hanuman temple; feed monkeys"
        ),
        Planet.MERCURY to CharityInfo(
            listOf("Green gram (Moong dal)", "Green cloth", "Emerald green items", "Books", "Writing materials"),
            "Wednesday",
            "Students, scholars, young children, educational institutions",
            "Morning",
            "Donate to schools; feed birds with green gram"
        ),
        Planet.JUPITER to CharityInfo(
            listOf("Chana dal", "Yellow cloth", "Turmeric", "Gold", "Yellow flowers", "Books"),
            "Thursday",
            "Teachers, priests (Brahmins), temples, religious institutions",
            "Morning",
            "Donate to Vishnu/Jupiter temples; feed cows with chana dal"
        ),
        Planet.VENUS to CharityInfo(
            listOf("Rice", "White cloth", "Silk", "Perfumes", "Sweets", "Ghee"),
            "Friday",
            "Young women, artists, Lakshmi temples, cow shelters",
            "Morning",
            "Donate to women's welfare; offer white flowers to Lakshmi"
        ),
        Planet.SATURN to CharityInfo(
            listOf("Black gram (Urad dal)", "Iron", "Sesame oil", "Blue/Black cloth", "Mustard oil"),
            "Saturday",
            "Poor and needy, servants, elderly, disabled, Shani temples",
            "Evening",
            "Feed crows; offer mustard oil at Shani temple; serve the disabled"
        ),
        Planet.RAHU to CharityInfo(
            listOf("Coconut", "Blue cloth", "Sesame seeds", "Lead", "Blanket"),
            "Saturday",
            "Outcasts, sweepers, Durga temples",
            "Night",
            "Donate at crossroads; offer to Durga temple"
        ),
        Planet.KETU to CharityInfo(
            listOf("Mixed seven grains", "Gray/Brown blanket", "Sesame seeds", "Dog food"),
            "Tuesday or Saturday",
            "Spiritual seekers, sadhus, dog shelters, Ganesha temples",
            "Before sunrise or after sunset",
            "Feed dogs; donate blankets to homeless; offer at Ganesha temple"
        )
    )

    val nakshatraDeities = mapOf(
        Nakshatra.ASHWINI to StringKeyRemedy.DEITY_ASHWINI_KUMARAS,
        Nakshatra.BHARANI to StringKeyRemedy.DEITY_YAMA,
        Nakshatra.KRITTIKA to StringKeyRemedy.DEITY_AGNI,
        Nakshatra.ROHINI to StringKeyRemedy.DEITY_BRAHMA,
        Nakshatra.MRIGASHIRA to StringKeyRemedy.DEITY_SOMA,
        Nakshatra.ARDRA to StringKeyRemedy.DEITY_RUDRA,
        Nakshatra.PUNARVASU to StringKeyRemedy.DEITY_ADITI,
        Nakshatra.PUSHYA to StringKeyRemedy.DEITY_BRIHASPATI,
        Nakshatra.ASHLESHA to StringKeyRemedy.DEITY_NAGAS,
        Nakshatra.MAGHA to StringKeyRemedy.DEITY_PITRIS,
        Nakshatra.PURVA_PHALGUNI to StringKeyRemedy.DEITY_BHAGA,
        Nakshatra.UTTARA_PHALGUNI to StringKeyRemedy.DEITY_ARYAMAN,
        Nakshatra.HASTA to StringKeyRemedy.DEITY_SAVITAR,
        Nakshatra.CHITRA to StringKeyRemedy.DEITY_VISHWAKARMA,
        Nakshatra.SWATI to StringKeyRemedy.DEITY_VAYU,
        Nakshatra.VISHAKHA to StringKeyRemedy.DEITY_INDRA_AGNI,
        Nakshatra.ANURADHA to StringKeyRemedy.DEITY_MITRA,
        Nakshatra.JYESHTHA to StringKeyRemedy.DEITY_INDRA,
        Nakshatra.MULA to StringKeyRemedy.DEITY_KALI,
        Nakshatra.PURVA_ASHADHA to StringKeyRemedy.DEITY_APAS,
        Nakshatra.UTTARA_ASHADHA to StringKeyRemedy.DEITY_VISHVADEVAS,
        Nakshatra.SHRAVANA to StringKeyRemedy.DEITY_VISHNU,
        Nakshatra.DHANISHTHA to StringKeyRemedy.DEITY_VASUS,
        Nakshatra.SHATABHISHA to StringKeyRemedy.DEITY_VARUNA,
        Nakshatra.PURVA_BHADRAPADA to StringKeyRemedy.DEITY_AJA_EKAPAD,
        Nakshatra.UTTARA_BHADRAPADA to StringKeyRemedy.DEITY_AHIR_BUDHNYA,
        Nakshatra.REVATI to StringKeyRemedy.DEITY_PUSHAN
    )
}

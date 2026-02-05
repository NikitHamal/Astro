package com.astro.storm.data.templates

import com.astro.storm.core.common.Language
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.DivisionalChartType
import com.astro.storm.ephemeris.deepanalysis.Element
import com.astro.storm.ephemeris.deepanalysis.LifeArea
import com.astro.storm.ephemeris.deepanalysis.LocalizedParagraph
import com.astro.storm.ephemeris.deepanalysis.StrengthLevel
import java.util.Locale

object TemplateDatabase {

    private data class PlanetProfile(
        val themeEn: String,
        val themeNe: String,
        val blessingEn: String,
        val blessingNe: String,
        val challengeEn: String,
        val challengeNe: String
    )

    private data class SignProfile(
        val focusEn: String,
        val focusNe: String,
        val element: Element,
        val modalityEn: String,
        val modalityNe: String
    )

    private data class HouseProfile(
        val areaEn: String,
        val areaNe: String
    )

    val degreeRanges: List<DegreeRange> = listOf(
        DegreeRange(0.0, 10.0, "early degrees", "प्रारम्भिक अंश"),
        DegreeRange(10.0, 20.0, "middle degrees", "मध्यम अंश"),
        DegreeRange(20.0, 30.0, "late degrees", "अन्तिम अंश")
    )

    val templates: List<PredictionTemplate> by lazy {
        buildTemplates()
    }

    val templatesByCategory: Map<TemplateCategory, List<PredictionTemplate>> by lazy {
        templates.groupBy { it.category }
    }

    val templateCount: Int get() = templates.size

    private fun buildTemplates(): List<PredictionTemplate> = buildList {
        addAll(buildDashaTemplates())
        addAll(buildTransitTemplates())
        addAll(buildHouseLordTemplates())
        addAll(buildDivisionalTemplates())
        addAll(buildNadiTemplates())
        addAll(buildLifeAreaTemplates())
        addAll(buildYogaTemplates())
    }

    private fun buildDashaTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()
        val planetProfiles = planetProfiles()
        val signProfiles = signProfiles()

        for (planet in Planet.MAIN_PLANETS) {
            val planetNameEn = planet.getLocalizedName(Language.ENGLISH)
            val planetNameNe = planet.getLocalizedName(Language.NEPALI)
            val profile = planetProfiles.getValue(planet)

            for (sign in ZodiacSign.entries) {
                val signNameEn = sign.getLocalizedName(Language.ENGLISH)
                val signNameNe = sign.getLocalizedName(Language.NEPALI)
                val signProfile = signProfiles.getValue(sign)

                for (range in degreeRanges) {
                    for (strengthBand in StrengthBand.entries) {
                        val en = "$planetNameEn dasha period in $signNameEn (${range.labelEn}) highlights ${signProfile.focusEn} through ${profile.themeEn}. ${profile.blessingEn} ${strengthBand.labelEn} support makes results ${strengthBandToneEn(strengthBand)}."
                        val ne = "$planetNameNe दशा अवधि $signNameNe (${range.labelNe}) मा ${signProfile.focusNe} लाई ${profile.themeNe} मार्फत उजागर गर्छ। ${profile.blessingNe} ${strengthBand.labelNe} समर्थनले परिणाम ${strengthBandToneNe(strengthBand)} बनाउँछ।"

                        templates.add(
                            PredictionTemplate(
                                id = "DASHA_${planet.name}_${sign.name}_${range.start.toInt()}_${strengthBand.name}",
                                category = TemplateCategory.DASHA,
                                conditions = TemplateConditions(
                                    planet = planet,
                                    sign = sign,
                                    degreeRange = range,
                                    strengthBand = strengthBand
                                ),
                                text = LocalizedParagraph(en, ne),
                                priority = 80 + strengthBand.ordinal
                            )
                        )
                    }
                }
            }
        }

        return templates
    }

    private fun buildTransitTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()
        val planetProfiles = planetProfiles()

        for (transitPlanet in Planet.MAIN_PLANETS) {
            val transitNameEn = transitPlanet.getLocalizedName(Language.ENGLISH)
            val transitNameNe = transitPlanet.getLocalizedName(Language.NEPALI)
            val transitProfile = planetProfiles.getValue(transitPlanet)

            for (natalPlanet in Planet.MAIN_PLANETS) {
                val natalNameEn = natalPlanet.getLocalizedName(Language.ENGLISH)
                val natalNameNe = natalPlanet.getLocalizedName(Language.NEPALI)

                for (aspect in TransitAspectType.entries) {
                    for (phase in TransitPhase.entries) {
                        val aspectTone = aspectTone(aspect)
                        val phaseTone = if (phase == TransitPhase.APPLYING) "building" else "releasing"
                        val phaseToneNe = if (phase == TransitPhase.APPLYING) "निर्माण हुँदै" else "छुट्दै"

                        val en = "$transitNameEn $aspectTone $natalNameEn while $phaseTone brings ${transitProfile.themeEn} to $natalNameEn matters. ${transitProfile.blessingEn}"
                        val ne = "$transitNameNe $natalNameNe सँग ${aspectToneNe(aspect)} ($phaseToneNe) हुँदा ${transitProfile.themeNe} को प्रभाव बढ्छ। ${transitProfile.blessingNe}"

                        templates.add(
                            PredictionTemplate(
                                id = "TRANSIT_${transitPlanet.name}_${natalPlanet.name}_${aspect.name}_${phase.name}",
                                category = TemplateCategory.TRANSIT,
                                conditions = TemplateConditions(
                                    transitingPlanet = transitPlanet,
                                    natalPlanet = natalPlanet,
                                    transitAspect = aspect,
                                    transitPhase = phase
                                ),
                                text = LocalizedParagraph(en, ne),
                                priority = 70
                            )
                        )
                    }
                }
            }
        }

        return templates
    }

    private fun buildHouseLordTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()
        val houseProfiles = houseProfiles()
        val planetProfiles = planetProfiles()

        for (house in 1..12) {
            val houseProfile = houseProfiles.getValue(house)

            for (planet in Planet.MAIN_PLANETS) {
                val planetNameEn = planet.getLocalizedName(Language.ENGLISH)
                val planetNameNe = planet.getLocalizedName(Language.NEPALI)
                val profile = planetProfiles.getValue(planet)

                for (dignityBand in DignityBand.entries) {
                    for (strengthBand in StrengthBand.entries) {
                        val en = "$planetNameEn as lord of house $house emphasizes ${houseProfile.areaEn}. ${profile.themeEn} becomes ${dignityBand.labelEn} with ${strengthBand.labelEn} support, shaping ${houseProfile.areaEn} outcomes."
                        val ne = "$planetNameNe house $house को स्वामी हुँदा ${houseProfile.areaNe} मा जोड दिन्छ। ${profile.themeNe} ${dignityBand.labelNe} हुन्छ र ${strengthBand.labelNe} समर्थनले ${houseProfile.areaNe} परिणामहरू निर्धारण गर्छ।"

                        templates.add(
                            PredictionTemplate(
                                id = "HOUSE_LORD_${house}_${planet.name}_${dignityBand.name}_${strengthBand.name}",
                                category = TemplateCategory.HOUSE_LORD,
                                conditions = TemplateConditions(
                                    planet = planet,
                                    house = house,
                                    dignityBand = dignityBand,
                                    strengthBand = strengthBand
                                ),
                                text = LocalizedParagraph(en, ne),
                                priority = 60
                            )
                        )
                    }
                }
            }
        }

        return templates
    }

    private fun buildDivisionalTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()
        val planetProfiles = planetProfiles()

        for (varga in DivisionalChartType.entries) {
            for (planet in Planet.MAIN_PLANETS) {
                val planetNameEn = planet.getLocalizedName(Language.ENGLISH)
                val planetNameNe = planet.getLocalizedName(Language.NEPALI)
                val profile = planetProfiles.getValue(planet)

                for (strengthBand in listOf(StrengthBand.STRONG, StrengthBand.WEAK)) {
                    val en = "In ${varga.shortName} (${varga.displayName}) chart, $planetNameEn shows ${strengthBandToneEn(strengthBand)} results for ${varga.description.lowercase(Locale.US)}. ${profile.themeEn} shapes this varga strongly."
                    val ne = "${varga.shortName} (${varga.displayName}) चार्टमा $planetNameNe ले ${strengthBandToneNe(strengthBand)} परिणाम दिन्छ। ${profile.themeNe} ले ${varga.description} क्षेत्रमा प्रभाव पार्छ।"

                    templates.add(
                        PredictionTemplate(
                            id = "DIV_${varga.shortName}_${planet.name}_${strengthBand.name}",
                            category = TemplateCategory.DIVISIONAL,
                            conditions = TemplateConditions(
                                varga = varga,
                                planet = planet,
                                strengthBand = strengthBand
                            ),
                            text = LocalizedParagraph(en, ne),
                            priority = 65
                        )
                    )
                }
            }
        }

        return templates
    }

    private fun buildNadiTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()

        for (startSign in ZodiacSign.entries) {
            val startSignNameEn = startSign.getLocalizedName(Language.ENGLISH)
            val startSignNameNe = startSign.getLocalizedName(Language.NEPALI)
            for (nadiNumber in 1..150) {
                val nadiSign = calculateNadiSign(startSign, nadiNumber)
                val nadiLord = nadiSign.ruler
                val energyType = if (nadiNumber % 2 == 0) "female" else "male"
                val energyTypeNe = if (nadiNumber % 2 == 0) "स्त्री" else "पुरुष"
                val nadiSignNameEn = nadiSign.getLocalizedName(Language.ENGLISH)
                val nadiSignNameNe = nadiSign.getLocalizedName(Language.NEPALI)
                val lordNameEn = nadiLord.getLocalizedName(Language.ENGLISH)
                val lordNameNe = nadiLord.getLocalizedName(Language.NEPALI)

                val en = "Nadi $nadiNumber starting in $startSignNameEn aligns with $nadiSignNameEn and $lordNameEn, indicating $energyType energy with precision in timing. Focus on the core themes of $nadiSignNameEn guided by $lordNameEn."
                val ne = "नाडी $nadiNumber ($startSignNameNe बाट) $nadiSignNameNe र $lordNameNe सँग मेल खान्छ, $energyTypeNe ऊर्जा र सूक्ष्म समय संकेत गर्छ। $nadiSignNameNe का मुख्य विषयहरूमा $lordNameNe को मार्गदर्शन हुन्छ।"

                templates.add(
                    PredictionTemplate(
                        id = "NADI_${startSign.name}_${nadiNumber}",
                        category = TemplateCategory.NADI,
                        conditions = TemplateConditions(
                            nadiNumber = nadiNumber,
                            startSign = startSign
                        ),
                        text = LocalizedParagraph(en, ne),
                        priority = 90
                    )
                )
            }
        }

        return templates
    }

    private fun buildLifeAreaTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()
        val planetProfiles = planetProfiles()

        for (area in LifeArea.entries) {
            val areaNameEn = areaName(area, Language.ENGLISH)
            val areaNameNe = areaName(area, Language.NEPALI)

            for (planet in Planet.MAIN_PLANETS) {
                val planetNameEn = planet.getLocalizedName(Language.ENGLISH)
                val planetNameNe = planet.getLocalizedName(Language.NEPALI)
                val profile = planetProfiles.getValue(planet)

                for (element in Element.entries) {
                    for (range in degreeRanges) {
                        for (strengthBand in StrengthBand.entries) {
                            for (horizon in TimeHorizon.entries) {
                                val horizonEn = timeHorizonLabel(horizon, Language.ENGLISH)
                                val horizonNe = timeHorizonLabel(horizon, Language.NEPALI)
                                val en = "$horizonEn $areaNameEn matters are guided by $planetNameEn in ${element.displayName.lowercase(Locale.US)} emphasis (${range.labelEn}). ${profile.themeEn} brings ${strengthBandToneEn(strengthBand)} progress."
                                val ne = "$horizonNe $areaNameNe मामिलाहरू $planetNameNe को ${element.displayNameNe} प्रभाव (${range.labelNe}) बाट निर्देशित हुन्छन्। ${profile.themeNe} ले ${strengthBandToneNe(strengthBand)} प्रगति ल्याउँछ।"

                                templates.add(
                                    PredictionTemplate(
                                        id = "LIFE_${area.name}_${planet.name}_${element.name}_${range.start.toInt()}_${strengthBand.name}_${horizon.name}",
                                        category = TemplateCategory.LIFE_AREA,
                                        conditions = TemplateConditions(
                                            lifeArea = area,
                                            planet = planet,
                                            element = element,
                                            degreeRange = range,
                                            strengthBand = strengthBand,
                                            timeHorizon = horizon
                                        ),
                                        text = LocalizedParagraph(en, ne),
                                        priority = 75
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        return templates
    }

    private fun buildYogaTemplates(): List<PredictionTemplate> {
        val templates = mutableListOf<PredictionTemplate>()
        val categories = YogaCategory.entries
        val signProfiles = signProfiles()

        for (category in categories) {
            val categoryEn = yogaCategoryLabel(category, Language.ENGLISH)
            val categoryNe = yogaCategoryLabel(category, Language.NEPALI)
            for (sign in ZodiacSign.entries) {
                val signNameEn = sign.getLocalizedName(Language.ENGLISH)
                val signNameNe = sign.getLocalizedName(Language.NEPALI)
                val signProfile = signProfiles.getValue(sign)

                for (strengthBand in StrengthBand.entries) {
                    val en = "$categoryEn yoga in $signNameEn amplifies ${signProfile.focusEn} with ${strengthBandToneEn(strengthBand)} potency."
                    val ne = "$signNameNe मा $categoryNe योगले ${signProfile.focusNe} लाई ${strengthBandToneNe(strengthBand)} तीव्रताका साथ बढाउँछ।"

                    templates.add(
                        PredictionTemplate(
                            id = "YOGA_${category.name}_${sign.name}_${strengthBand.name}",
                            category = TemplateCategory.YOGA,
                            conditions = TemplateConditions(
                                yogaCategory = category,
                                sign = sign,
                                strengthBand = strengthBand
                            ),
                            text = LocalizedParagraph(en, ne),
                            priority = 55
                        )
                    )
                }
            }
        }

        return templates
    }

    fun elementForSign(sign: ZodiacSign): Element = when (sign.element) {
        "Fire" -> Element.FIRE
        "Earth" -> Element.EARTH
        "Air" -> Element.AIR
        "Water" -> Element.WATER
        else -> Element.FIRE
    }

    fun calculateNadiSign(startSign: ZodiacSign, nadiNumber: Int): ZodiacSign {
        val signIndex = startSign.ordinal
        val nadiSignIndex = (signIndex + nadiNumber - 1) % 12
        return ZodiacSign.entries[nadiSignIndex]
    }

    fun nadiDegreeRange(startSign: ZodiacSign, nadiNumber: Int): Pair<Double, Double> {
        val isOdd = startSign.number % 2 != 0
        val part = 30.0 / 150.0
        val indexFromStart = if (isOdd) nadiNumber - 1 else 150 - nadiNumber
        val start = indexFromStart * part
        return Pair(start, start + part)
    }

    fun formatDegree(value: Double): String = String.format(Locale.US, "%.2f", value)

    private fun strengthBandToneEn(band: StrengthBand): String = when (band) {
        StrengthBand.STRONG -> "accelerated"
        StrengthBand.MODERATE -> "steady"
        StrengthBand.WEAK -> "measured"
    }

    private fun strengthBandToneNe(band: StrengthBand): String = when (band) {
        StrengthBand.STRONG -> "तीव्र"
        StrengthBand.MODERATE -> "स्थिर"
        StrengthBand.WEAK -> "संयमित"
    }

    private fun aspectTone(aspect: TransitAspectType): String = when (aspect) {
        TransitAspectType.CONJUNCTION -> "conjuncts"
        TransitAspectType.OPPOSITION -> "opposes"
        TransitAspectType.TRINE -> "trines"
        TransitAspectType.SQUARE -> "squares"
        TransitAspectType.SEXTILE -> "sextiles"
    }

    private fun aspectToneNe(aspect: TransitAspectType): String = when (aspect) {
        TransitAspectType.CONJUNCTION -> "संयोग"
        TransitAspectType.OPPOSITION -> "विपरीत"
        TransitAspectType.TRINE -> "त्रिकोण"
        TransitAspectType.SQUARE -> "चतुर्थ"
        TransitAspectType.SEXTILE -> "षष्ठ"
    }

    private fun timeHorizonLabel(horizon: TimeHorizon, language: Language): String = when (language) {
        Language.ENGLISH -> when (horizon) {
            TimeHorizon.SHORT -> "Short-term"
            TimeHorizon.MEDIUM -> "Mid-term"
            TimeHorizon.LONG -> "Long-term"
        }
        Language.NEPALI -> when (horizon) {
            TimeHorizon.SHORT -> "छोटो अवधिको"
            TimeHorizon.MEDIUM -> "मध्यम अवधिको"
            TimeHorizon.LONG -> "दीर्घ अवधिको"
        }
    }

    private fun areaName(area: LifeArea, language: Language): String = when (area) {
        LifeArea.GENERAL -> if (language == Language.NEPALI) "सामान्य जीवन" else "general life"
        LifeArea.CAREER -> if (language == Language.NEPALI) "क्यारियर" else "career"
        LifeArea.RELATIONSHIP -> if (language == Language.NEPALI) "सम्बन्ध" else "relationships"
        LifeArea.HEALTH -> if (language == Language.NEPALI) "स्वास्थ्य" else "health"
        LifeArea.WEALTH -> if (language == Language.NEPALI) "धन" else "wealth"
        LifeArea.EDUCATION -> if (language == Language.NEPALI) "शिक्षा" else "education"
        LifeArea.SPIRITUAL -> if (language == Language.NEPALI) "आध्यात्मिकता" else "spirituality"
    }

    private fun yogaCategoryLabel(category: YogaCategory, language: Language): String = when (language) {
        Language.ENGLISH -> when (category) {
            YogaCategory.RAJA -> "Raja"
            YogaCategory.DHANA -> "Dhana"
            YogaCategory.MAHAPURUSHA -> "Mahapurusha"
            YogaCategory.NABHASA -> "Nabhasa"
            YogaCategory.ARISHTA -> "Arishta"
            YogaCategory.VIPARITA -> "Viparita"
            YogaCategory.PARIVARTANA -> "Parivartana"
            YogaCategory.BHAVA -> "Bhava"
            YogaCategory.GENERAL -> "General"
        }
        Language.NEPALI -> when (category) {
            YogaCategory.RAJA -> "राज"
            YogaCategory.DHANA -> "धन"
            YogaCategory.MAHAPURUSHA -> "महापुरुष"
            YogaCategory.NABHASA -> "नभस"
            YogaCategory.ARISHTA -> "अरिष्ट"
            YogaCategory.VIPARITA -> "विपरीत"
            YogaCategory.PARIVARTANA -> "परिवर्तन"
            YogaCategory.BHAVA -> "भाव"
            YogaCategory.GENERAL -> "सामान्य"
        }
    }

    private fun planetProfiles(): Map<Planet, PlanetProfile> = mapOf(
        Planet.SUN to PlanetProfile(
            themeEn = "leadership and vitality",
            themeNe = "नेतृत्व र ऊर्जा",
            blessingEn = "Recognition and confidence rise.",
            blessingNe = "मान्यता र आत्मविश्वास बढ्छ।",
            challengeEn = "Avoid ego clashes.",
            challengeNe = "अहङ्कारजन्य द्वन्द्वबाट बच्नुहोस्।"
        ),
        Planet.MOON to PlanetProfile(
            themeEn = "mind and emotional balance",
            themeNe = "मन र भावनात्मक सन्तुलन",
            blessingEn = "Nurturing support increases.",
            blessingNe = "सहयोग र पोषण बढ्छ।",
            challengeEn = "Guard mood swings.",
            challengeNe = "भावनात्मक उतार-चढावमा सतर्क रहनुहोस्।"
        ),
        Planet.MARS to PlanetProfile(
            themeEn = "courage and decisive action",
            themeNe = "साहस र निर्णायक कार्य",
            blessingEn = "Drive and stamina improve.",
            blessingNe = "उत्साह र सहनशीलता बढ्छ।",
            challengeEn = "Channel anger constructively.",
            challengeNe = "आक्रोशलाई सकारात्मक रूपमा प्रयोग गर्नुहोस्।"
        ),
        Planet.MERCURY to PlanetProfile(
            themeEn = "intellect and communication",
            themeNe = "बुद्धि र सञ्चार",
            blessingEn = "Learning and trade expand.",
            blessingNe = "सिकाइ र व्यापार बढ्छ।",
            challengeEn = "Avoid scattered focus.",
            challengeNe = "ध्यान बिखरिन नदिनुहोस्।"
        ),
        Planet.JUPITER to PlanetProfile(
            themeEn = "wisdom and expansion",
            themeNe = "ज्ञान र विस्तार",
            blessingEn = "Opportunities and guidance grow.",
            blessingNe = "अवसर र मार्गदर्शन बढ्छ।",
            challengeEn = "Avoid complacency.",
            challengeNe = "आलस्यबाट टाढा रहनुहोस्।"
        ),
        Planet.VENUS to PlanetProfile(
            themeEn = "harmony and relationships",
            themeNe = "सद्भाव र सम्बन्ध",
            blessingEn = "Comforts and love deepen.",
            blessingNe = "आराम र प्रेम गहिरो हुन्छ।",
            challengeEn = "Balance indulgence.",
            challengeNe = "अतिशय भोगबाट सन्तुलन राख्नुहोस्।"
        ),
        Planet.SATURN to PlanetProfile(
            themeEn = "discipline and responsibility",
            themeNe = "अनुशासन र जिम्मेवारी",
            blessingEn = "Endurance and structure strengthen.",
            blessingNe = "धैर्य र संरचना बलियो हुन्छ।",
            challengeEn = "Accept delays gracefully.",
            challengeNe = "ढिलाइलाई धैर्यपूर्वक स्वीकार गर्नुहोस्।"
        ),
        Planet.RAHU to PlanetProfile(
            themeEn = "ambition and unconventional growth",
            themeNe = "महत्त्वाकांक्षा र अपरम्परागत वृद्धि",
            blessingEn = "Breakthroughs and innovation appear.",
            blessingNe = "नयाँ उपलब्धि र नवप्रवर्तन देखिन्छ।",
            challengeEn = "Avoid confusion and impulsive choices.",
            challengeNe = "भ्रम र आवेगपूर्ण निर्णयबाट बच्नुहोस्।"
        ),
        Planet.KETU to PlanetProfile(
            themeEn = "detachment and spiritual insight",
            themeNe = "विरक्ति र आध्यात्मिक अन्तर्दृष्टि",
            blessingEn = "Inner clarity increases.",
            blessingNe = "आन्तरिक स्पष्टता बढ्छ।",
            challengeEn = "Avoid unnecessary withdrawal.",
            challengeNe = "अनावश्यक एकान्तबाट बच्नुहोस्।"
        )
    )

    private fun signProfiles(): Map<ZodiacSign, SignProfile> = mapOf(
        ZodiacSign.ARIES to SignProfile("initiative and courage", "सुरुआत र साहस", Element.FIRE, "cardinal", "चर"),
        ZodiacSign.TAURUS to SignProfile("stability and resources", "स्थिरता र संसाधन", Element.EARTH, "fixed", "स्थिर"),
        ZodiacSign.GEMINI to SignProfile("learning and communication", "सिकाइ र सञ्चार", Element.AIR, "mutable", "द्विस्वभाव"),
        ZodiacSign.CANCER to SignProfile("nurturing and security", "पालन र सुरक्षा", Element.WATER, "cardinal", "चर"),
        ZodiacSign.LEO to SignProfile("leadership and creativity", "नेतृत्व र रचनात्मकता", Element.FIRE, "fixed", "स्थिर"),
        ZodiacSign.VIRGO to SignProfile("analysis and service", "विश्लेषण र सेवा", Element.EARTH, "mutable", "द्विस्वभाव"),
        ZodiacSign.LIBRA to SignProfile("balance and partnership", "सन्तुलन र साझेदारी", Element.AIR, "cardinal", "चर"),
        ZodiacSign.SCORPIO to SignProfile("transformation and depth", "रूपान्तरण र गहिराइ", Element.WATER, "fixed", "स्थिर"),
        ZodiacSign.SAGITTARIUS to SignProfile("exploration and wisdom", "अन्वेषण र ज्ञान", Element.FIRE, "mutable", "द्विस्वभाव"),
        ZodiacSign.CAPRICORN to SignProfile("discipline and achievement", "अनुशासन र उपलब्धि", Element.EARTH, "cardinal", "चर"),
        ZodiacSign.AQUARIUS to SignProfile("innovation and community", "नवप्रवर्तन र समुदाय", Element.AIR, "fixed", "स्थिर"),
        ZodiacSign.PISCES to SignProfile("compassion and spirituality", "करुणा र आध्यात्मिकता", Element.WATER, "mutable", "द्विस्वभाव")
    )

    private fun houseProfiles(): Map<Int, HouseProfile> = mapOf(
        1 to HouseProfile("self and vitality", "स्वरूप र ऊर्जा"),
        2 to HouseProfile("wealth and family", "धन र परिवार"),
        3 to HouseProfile("courage and siblings", "साहस र दाजुभाइ"),
        4 to HouseProfile("home and comfort", "घर र आराम"),
        5 to HouseProfile("creativity and children", "रचनात्मकता र सन्तान"),
        6 to HouseProfile("health and service", "स्वास्थ्य र सेवा"),
        7 to HouseProfile("partnerships and marriage", "साझेदारी र विवाह"),
        8 to HouseProfile("transformation and longevity", "रूपान्तरण र आयु"),
        9 to HouseProfile("fortune and dharma", "भाग्य र धर्म"),
        10 to HouseProfile("career and status", "क्यारियर र प्रतिष्ठा"),
        11 to HouseProfile("gains and networks", "लाभ र नेटवर्क"),
        12 to HouseProfile("liberation and retreat", "मोक्ष र निवृत्ति")
    )
}

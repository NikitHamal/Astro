package com.astro.vajra.ephemeris.yoga

import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.PlanetPosition
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign

/**
 * Parivarttana (Exchange) Yoga Evaluator - House Lord Mutual Exchanges
 *
 * This evaluator handles all possible Parivarttana (mutual exchange) yogas.
 * When two planets exchange signs (each occupies the other's sign), they create
 * a powerful connection between those houses, as if both are in both houses.
 *
 * Categories of Parivarttana:
 * 1. Maha Parivarttana Yoga - Exchange between lords of 1, 2, 4, 5, 7, 9, 10, 11 (auspicious)
 * 2. Khala Parivarttana Yoga - Exchange involving 3rd house (mixed)
 * 3. Dainya Parivarttana Yoga - Exchange involving 6, 8, or 12 (challenging but can be redirected)
 *
 * There are 66 possible house combinations (12 choose 2), each with unique effects
 * based on the houses involved.
 *
 * Based on classical texts:
 * - Brihat Parasara Hora Shastra (BPHS), Chapter 28
 * - Phaladeepika, Chapter 6
 * - Uttara Kalamrita, Section 4
 * - Jataka Parijata, Chapter 7
 *
 * @author AstroVajra
 */
class ParivarttanaYogaEvaluator : YogaEvaluator {

    override val category: YogaCategory = YogaCategory.SPECIAL_YOGA

    override fun evaluate(chart: VedicChart): List<Yoga> {
        val yogas = mutableListOf<Yoga>()
        val ascendantSign = ZodiacSign.fromLongitude(chart.ascendant)
        val houseLords = YogaHelpers.getHouseLords(ascendantSign)

        // Find all exchanges in the chart
        val exchanges = findAllExchanges(chart, houseLords)

        for (exchange in exchanges) {
            val yoga = createExchangeYoga(exchange, chart, houseLords)
            if (yoga != null) {
                yogas.add(yoga)
            }
        }

        // Check for multiple exchanges (rare and powerful)
        if (exchanges.size >= 2) {
            yogas.add(createMultipleExchangeYoga(exchanges, chart, houseLords))
        }

        return yogas
    }

    /**
     * Data class to hold exchange information
     */
    private data class Exchange(
        val house1: Int,
        val house2: Int,
        val lord1: Planet,
        val lord2: Planet,
        val type: ExchangeType
    )

    private enum class ExchangeType {
        MAHA,   // Auspicious - involves only 1, 2, 4, 5, 7, 9, 10, 11
        KHALA,  // Mixed - involves 3rd house
        DAINYA  // Challenging - involves 6, 8, or 12
    }

    private fun findAllExchanges(
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): List<Exchange> {
        val exchanges = mutableListOf<Exchange>()
        val mahaHouses = setOf(1, 2, 4, 5, 7, 9, 10, 11)
        val dainyaHouses = setOf(6, 8, 12)

        for (house1 in 1..11) {
            for (house2 in (house1 + 1)..12) {
                val lord1 = houseLords[house1] ?: continue
                val lord2 = houseLords[house2] ?: continue

                // Skip if same planet rules both houses
                if (lord1 == lord2) continue

                val lord1Pos = chart.planetPositions.find { it.planet == lord1 } ?: continue
                val lord2Pos = chart.planetPositions.find { it.planet == lord2 } ?: continue

                // Check for exchange: lord1 in house2's sign, lord2 in house1's sign
                if (lord1Pos.house == house2 && lord2Pos.house == house1) {
                    val type = when {
                        house1 in dainyaHouses || house2 in dainyaHouses -> ExchangeType.DAINYA
                        house1 == 3 || house2 == 3 -> ExchangeType.KHALA
                        house1 in mahaHouses && house2 in mahaHouses -> ExchangeType.MAHA
                        else -> ExchangeType.KHALA
                    }

                    exchanges.add(Exchange(house1, house2, lord1, lord2, type))
                }
            }
        }

        return exchanges
    }

    private fun createExchangeYoga(
        exchange: Exchange,
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga? {
        val (house1, house2, lord1, lord2, type) = exchange

        val yogaInfo = getExchangeDetails(house1, house2, type)

        val category = when (type) {
            ExchangeType.MAHA -> if (isRajaYogaExchange(house1, house2)) YogaCategory.RAJA_YOGA else YogaCategory.SPECIAL_YOGA
            ExchangeType.KHALA -> YogaCategory.SPECIAL_YOGA
            ExchangeType.DAINYA -> YogaCategory.NEGATIVE_YOGA
        }

        val isAuspicious = type == ExchangeType.MAHA || (type == ExchangeType.KHALA && house1 != 3 && house2 != 3)

        val strength = when (type) {
            ExchangeType.MAHA -> {
                when {
                    isRajaYogaExchange(house1, house2) -> 90.0
                    isDhanaYogaExchange(house1, house2) -> 85.0
                    else -> 80.0
                }
            }
            ExchangeType.KHALA -> 65.0
            ExchangeType.DAINYA -> 40.0
        }

        return Yoga(
            name = yogaInfo.name,
            sanskritName = yogaInfo.sanskritName,
            category = category,
            planets = listOf(lord1, lord2),
            houses = listOf(house1, house2),
            description = "${house1}th lord ${lord1.displayName} exchanges with ${house2}th lord ${lord2.displayName}",
            effects = yogaInfo.effects,
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = isAuspicious,
            activationPeriod = "${lord1.displayName} and ${lord2.displayName} dasha/antardasha periods",
            cancellationFactors = yogaInfo.cancellationFactors
        )
    }

    private fun isRajaYogaExchange(house1: Int, house2: Int): Boolean {
        val kendras = setOf(1, 4, 7, 10)
        val trikonas = setOf(1, 5, 9)
        return (house1 in kendras && house2 in trikonas) ||
                (house2 in kendras && house1 in trikonas) ||
                (house1 == 9 && house2 == 10) ||
                (house1 == 10 && house2 == 9)
    }

    private fun isDhanaYogaExchange(house1: Int, house2: Int): Boolean {
        val dhanaHouses = setOf(1, 2, 5, 9, 11)
        return house1 in dhanaHouses && house2 in dhanaHouses
    }

    private data class YogaInfo(
        val name: String,
        val sanskritName: String,
        val effects: String,
        val cancellationFactors: List<String>
    )

    private fun getExchangeDetails(house1: Int, house2: Int, type: ExchangeType): YogaInfo {
        // For readability, using smaller house first
        val (h1, h2) = if (house1 < house2) house1 to house2 else house2 to house1

        return when {
            // 1st house exchanges (Self)
            h1 == 1 && h2 == 2 -> YogaInfo(
                "Lagna-Dhana Parivarttana",
                "लग्न-धन परिवर्तन",
                "Self and wealth deeply connected, personal efforts bring wealth, identity tied to finances, good speech, family supports self-development",
                listOf("Very favorable for self-earned wealth", "Speech and family matters intertwined with self")
            )
            h1 == 1 && h2 == 3 -> YogaInfo(
                "Lagna-Sahaja Parivarttana",
                "लग्न-सहज परिवर्तन",
                "Self-expression through courage and communication, siblings influence personality, entrepreneurial nature, writing/media talents",
                listOf("Khala type but can be positive", "Courage defines personality")
            )
            h1 == 1 && h2 == 4 -> YogaInfo(
                "Lagna-Sukha Parivarttana",
                "लग्न-सुख परिवर्तन",
                "Personality shaped by home and mother, emotional security tied to self-image, property through personal efforts, peaceful nature",
                listOf("Maha Parivarttana - very auspicious", "Home and self deeply connected")
            )
            h1 == 1 && h2 == 5 -> YogaInfo(
                "Lagna-Putra Parivarttana",
                "लग्न-पुत्र परिवर्तन",
                "Creative and intelligent personality, children reflect native's character, speculative abilities, romantic nature, purva punya activated",
                listOf("Maha Parivarttana forming Raja Yoga", "Creativity and self-expression united")
            )
            h1 == 1 && h2 == 6 -> YogaInfo(
                "Lagna-Ripu Parivarttana",
                "लग्न-रिपु परिवर्तन",
                "Identity shaped by service and overcoming obstacles, health consciousness strong, may work in healing/service, competitive nature",
                listOf("Dainya Parivarttana but gives fighting spirit", "Can excel in service or healing")
            )
            h1 == 1 && h2 == 7 -> YogaInfo(
                "Lagna-Kalatra Parivarttana",
                "लग्न-कलत्र परिवर्तन",
                "Self defined through partnerships, spouse has major influence, business minded, public dealings, diplomatic nature",
                listOf("Maha Parivarttana", "Partnership central to identity")
            )
            h1 == 1 && h2 == 8 -> YogaInfo(
                "Lagna-Randhra Parivarttana",
                "लग्न-रन्ध्र परिवर्तन",
                "Transformative personality, interest in occult/research, inheritance affects self, health transformations, deep psychological insight",
                listOf("Dainya Parivarttana requiring spiritual orientation", "Can give research abilities or occult knowledge")
            )
            h1 == 1 && h2 == 9 -> YogaInfo(
                "Lagna-Bhagya Parivarttana",
                "लग्न-भाग्य परिवर्तन",
                "Fortunate personality, dharmic nature, father's influence strong, guru's blessings, higher education success, righteous character",
                listOf("Maha Parivarttana forming Raja Yoga", "Self and fortune deeply connected")
            )
            h1 == 1 && h2 == 10 -> YogaInfo(
                "Lagna-Karma Parivarttana",
                "लग्न-कर्म परिवर्तन",
                "Career defines personality, recognized in profession, self-made success, authority position, leadership abilities strong",
                listOf("Maha Parivarttana - one of the best", "Career and self perfectly aligned")
            )
            h1 == 1 && h2 == 11 -> YogaInfo(
                "Lagna-Labha Parivarttana",
                "लग्न-लाभ परिवर्तन",
                "Personal efforts bring gains, desires fulfilled through self-initiative, network supports self, elder siblings influential",
                listOf("Maha Parivarttana", "Self-effort brings material success")
            )
            h1 == 1 && h2 == 12 -> YogaInfo(
                "Lagna-Vyaya Parivarttana",
                "लग्न-व्यय परिवर्तन",
                "Spiritual personality, foreign residence possible, expenses on self-development, need for solitude, charitable nature",
                listOf("Dainya Parivarttana with spiritual potential", "May live abroad or in ashram")
            )

            // 2nd house exchanges (Wealth/Speech)
            h1 == 2 && h2 == 3 -> YogaInfo(
                "Dhana-Sahaja Parivarttana",
                "धन-सहज परिवर्तन",
                "Wealth through communication, siblings involved in finances, writing/speaking income, courage in financial matters",
                listOf("Khala type with financial implications", "Communication skills bring money")
            )
            h1 == 2 && h2 == 4 -> YogaInfo(
                "Dhana-Sukha Parivarttana",
                "धन-सुख परिवर्तन",
                "Wealth from property, family wealth in real estate, mother's resources help, comfortable finances, vehicles through savings",
                listOf("Maha Parivarttana", "Property and savings connected")
            )
            h1 == 2 && h2 == 5 -> YogaInfo(
                "Dhana-Putra Parivarttana",
                "धन-पुत्र परिवर्तन",
                "Wealth through intelligence/creativity, children inherit fortune, speculative gains, creative income, family supports education",
                listOf("Maha Parivarttana", "Intelligence brings wealth")
            )
            h1 == 2 && h2 == 6 -> YogaInfo(
                "Dhana-Ripu Parivarttana",
                "धन-रिपु परिवर्तन",
                "Money through service, debts affect family, legal matters over finances, health expenses, loans and lending",
                listOf("Dainya Parivarttana", "Service brings income but expenses too")
            )
            h1 == 2 && h2 == 7 -> YogaInfo(
                "Dhana-Kalatra Parivarttana",
                "धन-कलत्र परिवर्तन",
                "Spouse brings wealth, business income, partnership finances, family involved in trade, foreign trade possible",
                listOf("Maha Parivarttana", "Partnership wealth")
            )
            h1 == 2 && h2 == 8 -> YogaInfo(
                "Dhana-Randhra Parivarttana",
                "धन-रन्ध्र परिवर्तन",
                "Inheritance affecting family, sudden gains/losses, insurance money, research income, occult knowledge brings money",
                listOf("Dainya Parivarttana but can give inheritance", "Transformation of family wealth")
            )
            h1 == 2 && h2 == 9 -> YogaInfo(
                "Dhana-Bhagya Parivarttana",
                "धन-भाग्य परिवर्तन",
                "Fortune brings wealth, father's inheritance, dharmic income, teaching brings money, foreign wealth, lucky in finances",
                listOf("Maha Parivarttana - very favorable", "Wealth through righteousness")
            )
            h1 == 2 && h2 == 10 -> YogaInfo(
                "Dhana-Karma Parivarttana",
                "धन-कर्म परिवर्तन",
                "Career brings wealth, professional income strong, family business, speech used in profession, authoritative earnings",
                listOf("Maha Parivarttana", "Professional wealth")
            )
            h1 == 2 && h2 == 11 -> YogaInfo(
                "Dhana-Labha Parivarttana",
                "धन-लाभ परिवर्तन",
                "Savings grow into gains, accumulated wealth increases, network helps finances, elder siblings share resources, desires fulfilled through wealth",
                listOf("Maha Parivarttana - excellent for finances", "Wealth multiplies naturally")
            )
            h1 == 2 && h2 == 12 -> YogaInfo(
                "Dhana-Vyaya Parivarttana",
                "धन-व्यय परिवर्तन",
                "Expenses on family, foreign wealth, losses from family, charitable spending, spiritual investments, bed pleasures cost money",
                listOf("Dainya Parivarttana", "Wealth and expenses interlinked")
            )

            // 3rd house exchanges (Courage/Siblings)
            h1 == 3 && h2 == 4 -> YogaInfo(
                "Sahaja-Sukha Parivarttana",
                "सहज-सुख परिवर्तन",
                "Siblings affect home happiness, courage from mother, communication about property, short travels bring peace",
                listOf("Khala type", "Mixed results for home and siblings")
            )
            h1 == 3 && h2 == 5 -> YogaInfo(
                "Sahaja-Putra Parivarttana",
                "सहज-पुत्र परिवर्तन",
                "Creative courage, siblings influence children, communication talents, artistic expression, speculative courage",
                listOf("Khala type with creative potential", "Courage in creative pursuits")
            )
            h1 == 3 && h2 == 6 -> YogaInfo(
                "Sahaja-Ripu Parivarttana",
                "सहज-रिपु परिवर्तन",
                "Courage against enemies, siblings in service, competitive communication, health affects siblings, legal matters with relatives",
                listOf("Dainya-Khala combined", "Fighting spirit but complications")
            )
            h1 == 3 && h2 == 7 -> YogaInfo(
                "Sahaja-Kalatra Parivarttana",
                "सहज-कलत्र परिवर्तन",
                "Spouse influences courage, siblings affect partnerships, communication in business, short travels for partnerships",
                listOf("Khala type", "Siblings and spouse interlinked")
            )
            h1 == 3 && h2 == 8 -> YogaInfo(
                "Sahaja-Randhra Parivarttana",
                "सहज-रन्ध्र परिवर्तन",
                "Transformation through courage, research writing, occult communication, siblings face changes, mysterious travels",
                listOf("Dainya-Khala", "Deep changes through communication")
            )
            h1 == 3 && h2 == 9 -> YogaInfo(
                "Sahaja-Bhagya Parivarttana",
                "सहज-भाग्य परिवर्तन",
                "Fortune through courage, siblings affect luck, religious writing, short pilgrimages, lucky communications",
                listOf("Khala with fortune element", "Courage brings luck")
            )
            h1 == 3 && h2 == 10 -> YogaInfo(
                "Sahaja-Karma Parivarttana",
                "सहज-कर्म परिवर्तन",
                "Career through communication, siblings in profession, writing career, courage in work, media profession",
                listOf("Khala type but career-positive", "Communication-based career")
            )
            h1 == 3 && h2 == 11 -> YogaInfo(
                "Sahaja-Labha Parivarttana",
                "सहज-लाभ परिवर्तन",
                "Gains through courage, siblings bring gains, communication profits, network from siblings, desires through effort",
                listOf("Khala type with gain element", "Courageous gains")
            )
            h1 == 3 && h2 == 12 -> YogaInfo(
                "Sahaja-Vyaya Parivarttana",
                "सहज-व्यय परिवर्तन",
                "Expenses on siblings, foreign communication, losses through courage, spiritual writing, isolated siblings",
                listOf("Dainya-Khala", "Siblings and expenses linked")
            )

            // 4th house exchanges (Home/Mother)
            h1 == 4 && h2 == 5 -> YogaInfo(
                "Sukha-Putra Parivarttana",
                "सुख-पुत्र परिवर्तन",
                "Children bring happiness, creative home, mother supports education, property through intelligence, home-based creativity",
                listOf("Maha Parivarttana", "Home and children blessed")
            )
            h1 == 4 && h2 == 6 -> YogaInfo(
                "Sukha-Ripu Parivarttana",
                "सुख-रिपु परिवर्तन",
                "Home troubles with enemies, mother's health concerns, property disputes, service from home, healing home environment",
                listOf("Dainya Parivarttana", "Home and obstacles connected")
            )
            h1 == 4 && h2 == 7 -> YogaInfo(
                "Sukha-Kalatra Parivarttana",
                "सुख-कलत्र परिवर्तन",
                "Spouse brings home happiness, property through partnerships, mother approves spouse, business from home, marital bliss",
                listOf("Maha Parivarttana", "Marriage brings domestic peace")
            )
            h1 == 4 && h2 == 8 -> YogaInfo(
                "Sukha-Randhra Parivarttana",
                "सुख-रन्ध्र परिवर्तन",
                "Transformation of home, inheritance of property, mother faces changes, research at home, hidden property",
                listOf("Dainya Parivarttana", "Home undergoes transformations")
            )
            h1 == 4 && h2 == 9 -> YogaInfo(
                "Sukha-Bhagya Parivarttana",
                "सुख-भाग्य परिवर्तन",
                "Fortune in property, mother is fortunate, home for religious activities, lucky in real estate, ancestral property blessed",
                listOf("Maha Parivarttana", "Blessed home and fortune")
            )
            h1 == 4 && h2 == 10 -> YogaInfo(
                "Sukha-Karma Parivarttana",
                "सुख-कर्म परिवर्तन",
                "Career from home, property through profession, mother supports career, work-life balance, real estate profession",
                listOf("Maha Parivarttana forming Raja Yoga", "Home and career aligned")
            )
            h1 == 4 && h2 == 11 -> YogaInfo(
                "Sukha-Labha Parivarttana",
                "सुख-लाभ परिवर्तन",
                "Gains through property, home brings fulfillment, mother's gains, elder siblings help with home, desired home achieved",
                listOf("Maha Parivarttana", "Property brings gains")
            )
            h1 == 4 && h2 == 12 -> YogaInfo(
                "Sukha-Vyaya Parivarttana",
                "सुख-व्यय परिवर्तन",
                "Expenses on home, foreign property, losses of comfort, spiritual home, ashram-like residence, mother abroad",
                listOf("Dainya Parivarttana", "Home and spirituality linked")
            )

            // 5th house exchanges (Children/Intelligence)
            h1 == 5 && h2 == 6 -> YogaInfo(
                "Putra-Ripu Parivarttana",
                "पुत्र-रिपु परिवर्तन",
                "Children face obstacles, intelligence used for service, creative healing, speculative losses possible, legal matters affect children",
                listOf("Dainya Parivarttana", "Children and challenges linked")
            )
            h1 == 5 && h2 == 7 -> YogaInfo(
                "Putra-Kalatra Parivarttana",
                "पुत्र-कलत्र परिवर्तन",
                "Children through marriage blessed, spouse is creative, romantic partnerships, intelligent spouse, children support marriage",
                listOf("Maha Parivarttana", "Marriage and children interlinked positively")
            )
            h1 == 5 && h2 == 8 -> YogaInfo(
                "Putra-Randhra Parivarttana",
                "पुत्र-रन्ध्र परिवर्तन",
                "Transformation through creativity, research intelligence, children face changes, occult knowledge, inheritance from children",
                listOf("Dainya Parivarttana", "Creative transformation possible")
            )
            h1 == 5 && h2 == 9 -> YogaInfo(
                "Putra-Bhagya Parivarttana",
                "पुत्र-भाग्य परिवर्तन",
                "Children bring fortune, father supports education, creative dharma, lucky speculation, intelligent fortune",
                listOf("Maha Parivarttana forming strong Raja Yoga", "Trikona lords exchange - very powerful")
            )
            h1 == 5 && h2 == 10 -> YogaInfo(
                "Putra-Karma Parivarttana",
                "पुत्र-कर्म परिवर्तन",
                "Career through creativity, children support profession, intelligent leadership, speculative career success, authority through wisdom",
                listOf("Maha Parivarttana forming Raja Yoga", "Creativity brings authority")
            )
            h1 == 5 && h2 == 11 -> YogaInfo(
                "Putra-Labha Parivarttana",
                "पुत्र-लाभ परिवर्तन",
                "Gains through children, speculative profits, creative income, elder siblings support children, desires through intelligence",
                listOf("Maha Parivarttana", "Intelligence brings gains")
            )
            h1 == 5 && h2 == 12 -> YogaInfo(
                "Putra-Vyaya Parivarttana",
                "पुत्र-व्यय परिवर्तन",
                "Expenses on children, children abroad, creative losses, spiritual creativity, children's spiritual nature",
                listOf("Dainya Parivarttana", "Children and expenses linked")
            )

            // 6th house exchanges (Enemies/Service)
            h1 == 6 && h2 == 7 -> YogaInfo(
                "Ripu-Kalatra Parivarttana",
                "रिपु-कलत्र परिवर्तन",
                "Spouse faces obstacles, partnership complications, service through marriage, legal partnership issues, health affects spouse",
                listOf("Dainya Parivarttana", "Marriage and obstacles linked")
            )
            h1 == 6 && h2 == 8 -> YogaInfo(
                "Ripu-Randhra Parivarttana",
                "रिपु-रन्ध्र परिवर्तन",
                "Viparita Raja Yoga potential - enemies destroyed, transformation of obstacles, chronic conditions, deep healing possible, research into diseases",
                listOf("Double Dainya but can become Viparita Raja", "Obstacles destroy each other")
            )
            h1 == 6 && h2 == 9 -> YogaInfo(
                "Ripu-Bhagya Parivarttana",
                "रिपु-भाग्य परिवर्तन",
                "Fortune through service, obstacles to luck, father's enemies, dharma in healing, legal luck mixed",
                listOf("Dainya Parivarttana", "Service and fortune interlinked")
            )
            h1 == 6 && h2 == 10 -> YogaInfo(
                "Ripu-Karma Parivarttana",
                "रिपु-कर्म परिवर्तन",
                "Career in service/healing, professional obstacles, authority through overcoming, competitive career, health profession",
                listOf("Dainya Parivarttana but career-oriented", "Service profession indicated")
            )
            h1 == 6 && h2 == 11 -> YogaInfo(
                "Ripu-Labha Parivarttana",
                "रिपु-लाभ परिवर्तन",
                "Gains through service, obstacles to gains, network includes enemies, competitive gains, desires through struggle",
                listOf("Dainya Parivarttana", "Gains require struggle")
            )
            h1 == 6 && h2 == 12 -> YogaInfo(
                "Ripu-Vyaya Parivarttana",
                "रिपु-व्यय परिवर्तन",
                "Viparita Raja Yoga potential - enemies and losses neutralize, expenses on health, foreign enemies, hidden obstacles, hospital connections",
                listOf("Double Dainya forming Viparita Raja", "Negativities cancel each other")
            )

            // 7th house exchanges (Marriage/Partnerships)
            h1 == 7 && h2 == 8 -> YogaInfo(
                "Kalatra-Randhra Parivarttana",
                "कलत्र-रन्ध्र परिवर्तन",
                "Spouse brings transformation, inheritance through marriage, partner's family wealth, deep partnership bonds, occult connections through spouse",
                listOf("Dainya Parivarttana", "Marriage and transformation linked")
            )
            h1 == 7 && h2 == 9 -> YogaInfo(
                "Kalatra-Bhagya Parivarttana",
                "कलत्र-भाग्य परिवर्तन",
                "Fortunate marriage, spouse brings luck, dharmic partnership, father approves marriage, spiritual spouse",
                listOf("Maha Parivarttana", "Marriage brings fortune")
            )
            h1 == 7 && h2 == 10 -> YogaInfo(
                "Kalatra-Karma Parivarttana",
                "कलत्र-कर्म परिवर्तन",
                "Career through partnership, spouse supports profession, business success, authoritative spouse, public partnerships",
                listOf("Maha Parivarttana forming Raja Yoga", "Partnership and career aligned")
            )
            h1 == 7 && h2 == 11 -> YogaInfo(
                "Kalatra-Labha Parivarttana",
                "कलत्र-लाभ परिवर्तन",
                "Gains through spouse, partnership profits, network includes partners, spouse brings gains, desires through relationships",
                listOf("Maha Parivarttana", "Partnership brings gains")
            )
            h1 == 7 && h2 == 12 -> YogaInfo(
                "Kalatra-Vyaya Parivarttana",
                "कलत्र-व्यय परिवर्तन",
                "Expenses on spouse, foreign spouse, losses through partnership, spiritual partnership, bed pleasures emphasized",
                listOf("Dainya Parivarttana", "Marriage and expenses linked")
            )

            // 8th house exchanges (Transformation/Occult)
            h1 == 8 && h2 == 9 -> YogaInfo(
                "Randhra-Bhagya Parivarttana",
                "रन्ध्र-भाग्य परिवर्तन",
                "Fortune through transformation, inheritance from father, dharmic transformation, spiritual changes, father's longevity issues",
                listOf("Dainya Parivarttana", "Fortune and transformation linked")
            )
            h1 == 8 && h2 == 10 -> YogaInfo(
                "Randhra-Karma Parivarttana",
                "रन्ध्र-कर्म परिवर्तन",
                "Career transformation, research profession, occult career, authority through transformation, sudden career changes",
                listOf("Dainya Parivarttana with career implications", "Transformative career")
            )
            h1 == 8 && h2 == 11 -> YogaInfo(
                "Randhra-Labha Parivarttana",
                "रन्ध्र-लाभ परिवर्तन",
                "Gains through transformation, inheritance brings gains, sudden profits, research income, occult gains",
                listOf("Dainya Parivarttana", "Transformation brings gains")
            )
            h1 == 8 && h2 == 12 -> YogaInfo(
                "Randhra-Vyaya Parivarttana",
                "रन्ध्र-व्यय परिवर्तन",
                "Viparita Raja Yoga - transformation and liberation linked, deep spiritual changes, foreign occult, moksha potential, expenses on research",
                listOf("Double Dainya forming Viparita Raja", "Spiritual transformation")
            )

            // 9th house exchanges (Fortune/Dharma)
            h1 == 9 && h2 == 10 -> YogaInfo(
                "Bhagya-Karma Parivarttana",
                "भाग्य-कर्म परिवर्तन",
                "Most powerful Dharma-Karmadhipati Raja Yoga, fortune supports career, righteous profession, father in authority, lasting fame and fortune",
                listOf("Maha Parivarttana - supreme Raja Yoga", "Fortune and karma perfectly aligned")
            )
            h1 == 9 && h2 == 11 -> YogaInfo(
                "Bhagya-Labha Parivarttana",
                "भाग्य-लाभ परिवर्तन",
                "Fortune brings gains, luck multiplies, father supports gains, dharmic income, religious gains, desired fortune",
                listOf("Maha Parivarttana", "Fortune and gains multiply")
            )
            h1 == 9 && h2 == 12 -> YogaInfo(
                "Bhagya-Vyaya Parivarttana",
                "भाग्य-व्यय परिवर्तन",
                "Fortune through spirituality, father abroad, dharmic expenses, pilgrimage, foreign fortune, liberation through dharma",
                listOf("Dainya Parivarttana with spiritual benefits", "Dharma and moksha linked")
            )

            // 10th house exchanges (Career/Authority)
            h1 == 10 && h2 == 11 -> YogaInfo(
                "Karma-Labha Parivarttana",
                "कर्म-लाभ परिवर्तन",
                "Career brings gains, professional income strong, network supports career, authority brings gains, fulfilled professional desires",
                listOf("Maha Parivarttana", "Career and gains aligned")
            )
            h1 == 10 && h2 == 12 -> YogaInfo(
                "Karma-Vyaya Parivarttana",
                "कर्म-व्यय परिवर्तन",
                "Career in foreign lands, professional expenses, authority in hospitals/ashrams, work in isolation, spiritual career",
                listOf("Dainya Parivarttana", "Career and spirituality linked")
            )

            // 11th house exchanges (Gains)
            h1 == 11 && h2 == 12 -> YogaInfo(
                "Labha-Vyaya Parivarttana",
                "लाभ-व्यय परिवर्तन",
                "Gains balanced by expenses, foreign income, charitable nature, network abroad, desires for liberation",
                listOf("Dainya Parivarttana", "Gains and expenses interlinked")
            )

            else -> YogaInfo(
                "Parivarttana Yoga ($h1-$h2)",
                "परिवर्तन योग",
                "Exchange between ${h1}th and ${h2}th houses creates mutual support between these life areas",
                listOf("General exchange yoga", "Results depend on involved houses")
            )
        }
    }

    private fun createMultipleExchangeYoga(
        exchanges: List<Exchange>,
        chart: VedicChart,
        houseLords: Map<Int, Planet>
    ): Yoga {
        val mahaCount = exchanges.count { it.type == ExchangeType.MAHA }
        val allPlanets = exchanges.flatMap { listOf(it.lord1, it.lord2) }.distinct()
        val allHouses = exchanges.flatMap { listOf(it.house1, it.house2) }.distinct()

        val strength = when {
            mahaCount >= 2 -> 95.0
            mahaCount == 1 && exchanges.size == 2 -> 80.0
            else -> 70.0
        }

        return Yoga(
            name = "Bahudha Parivarttana Yoga",
            sanskritName = "बहुधा परिवर्तन योग",
            category = if (mahaCount >= 2) YogaCategory.RAJA_YOGA else YogaCategory.SPECIAL_YOGA,
            planets = allPlanets,
            houses = allHouses,
            description = "${exchanges.size} house lord exchanges in chart: ${exchanges.joinToString { "${it.house1}-${it.house2}" }}",
            effects = "Multiple exchanges create extraordinary interconnections, life areas support each other powerfully, complex but fortunate karma, multiple significations interlinked",
            strength = YogaHelpers.strengthFromPercentage(strength),
            strengthPercentage = strength,
            isAuspicious = mahaCount >= exchanges.size / 2,
            activationPeriod = "Periods of any exchanging planet activate multiple areas",
            cancellationFactors = listOf("Multiple exchanges are very rare", "Each exchange empowers the others")
        )
    }
}

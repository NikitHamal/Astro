package com.astro.storm.ephemeris.deepanalysis.character

import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.PlanetPosition
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.ephemeris.deepanalysis.*

/**
 * Sun Text Generator - Core identity and ego expression
 */
object SunTextGenerator {
    
    fun getCoreIdentity(sign: ZodiacSign, strength: StrengthLevel): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "Your core identity is that of a pioneer and warrior. With ${strength.displayName.lowercase()} Sun, " +
                "you define yourself through action, courage, and the ability to initiate new ventures.",
            ne = "तपाईंको मूल पहिचान अग्रगामी र योद्धाको हो। ${strength.displayNameNe} सूर्यको साथ।"
        )
        ZodiacSign.LEO -> LocalizedParagraph(
            en = "Your core identity is that of a king/queen and creator. With ${strength.displayName.lowercase()} Sun in its own sign, " +
                "you naturally radiate confidence, creativity, and leadership presence.",
            ne = "तपाईंको मूल पहिचान राजा/रानी र सिर्जनाकर्ताको हो।"
        )
        else -> LocalizedParagraph(
            en = "Your core identity is shaped by ${sign.displayName}'s influence. With ${strength.displayName.lowercase()} Sun, " +
                "you express your ego and vitality through this sign's characteristics.",
            ne = "तपाईंको मूल पहिचान ${sign.displayName}को प्रभावले आकार दिएको छ।"
        )
    }
    
    fun getEgoExpression(sign: ZodiacSign, house: Int): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your ego expresses itself through ${getHouseArea(house)} activities. " +
                "${sign.displayName}'s influence colors how you seek recognition and express your will.",
            ne = "तपाईंको अहंकार ${getHouseAreaNe(house)} गतिविधिहरू मार्फत आफूलाई व्यक्त गर्छ।"
        )
    }
    
    private fun getHouseArea(house: Int): String = when (house) {
        1 -> "self-presentation and personal initiative"
        2 -> "wealth accumulation and value expression"
        3 -> "communication and intellectual"
        4 -> "home-building and emotional"
        5 -> "creative and romantic"
        6 -> "service and health-conscious"
        7 -> "partnership and diplomatic"
        8 -> "transformative and investigative"
        9 -> "philosophical and expansive"
        10 -> "career and public-facing"
        11 -> "social and humanitarian"
        12 -> "spiritual and behind-the-scenes"
        else -> "varied"
    }
    
    private fun getHouseAreaNe(house: Int): String = when (house) {
        1 -> "आत्म-प्रस्तुति र व्यक्तिगत पहल"
        2 -> "धन संचय र मूल्य अभिव्यक्ति"
        3 -> "सञ्चार र बौद्धिक"
        4 -> "घर-निर्माण र भावनात्मक"
        5 -> "रचनात्मक र रोमान्टिक"
        6 -> "सेवा र स्वास्थ्य-सचेत"
        7 -> "साझेदारी र कूटनीतिक"
        8 -> "परिवर्तनकारी र अनुसन्धानात्मक"
        9 -> "दार्शनिक र विस्तारित"
        10 -> "क्यारियर र सार्वजनिक-मुखी"
        11 -> "सामाजिक र मानवतावादी"
        12 -> "आध्यात्मिक र पर्दा पछाडि"
        else -> "विविध"
    }
    
    fun getAuthorityRelationship(house: Int, strength: StrengthLevel): LocalizedParagraph {
        val quality = if (strength >= StrengthLevel.STRONG) "positive and empowering" else "growth-oriented"
        return LocalizedParagraph(
            en = "Your relationship with authority figures is generally $quality. " +
                "You ${if (strength >= StrengthLevel.STRONG) "naturally command respect" else "learn to develop authority"}.",
            ne = "अधिकार व्यक्तिहरूसँगको तपाईंको सम्बन्ध सामान्यतया ${if (strength >= StrengthLevel.STRONG) "सकारात्मक" else "विकास-उन्मुख"} छ।"
        )
    }
    
    fun getFatherRelationship(house: Int, strength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your relationship with father is ${if (strength >= StrengthLevel.STRONG) "supportive and influential" else "complex but educational"}. " +
                "Father's role in your life connects to ${getHouseArea(house)} matters.",
            ne = "बुबासँगको तपाईंको सम्बन्ध ${if (strength >= StrengthLevel.STRONG) "सहयोगी र प्रभावशाली" else "जटिल तर शैक्षिक"} छ।"
        )
    }
    
    fun getLeadershipStyle(sign: ZodiacSign): LocalizedParagraph = when (sign) {
        ZodiacSign.ARIES -> LocalizedParagraph(
            en = "Your leadership style is bold, direct, and action-oriented. You lead by example through courage.",
            ne = "तपाईंको नेतृत्व शैली साहसी, प्रत्यक्ष र कार्य-उन्मुख छ।"
        )
        ZodiacSign.LEO -> LocalizedParagraph(
            en = "Your leadership style is charismatic, generous, and creative. You inspire others through warmth.",
            ne = "तपाईंको नेतृत्व शैली करिश्माई, उदार र रचनात्मक छ।"
        )
        ZodiacSign.CAPRICORN -> LocalizedParagraph(
            en = "Your leadership style is disciplined, strategic, and achievement-focused. You lead through competence.",
            ne = "तपाईंको नेतृत्व शैली अनुशासित, रणनीतिक र उपलब्धि-केन्द्रित छ।"
        )
        else -> LocalizedParagraph(
            en = "Your leadership style reflects ${sign.displayName}'s qualities, influencing how you guide and inspire others.",
            ne = "तपाईंको नेतृत्व शैलीले ${sign.displayName}का गुणहरू प्रतिबिम्बित गर्छ।"
        )
    }
    
    fun getVitalityLevel(strength: StrengthLevel): LocalizedParagraph = when (strength) {
        StrengthLevel.EXCELLENT -> LocalizedParagraph(
            en = "Your vitality is exceptionally strong, giving you abundant energy and robust constitution.",
            ne = "तपाईंको जीवनी शक्ति असाधारण रूपमा बलियो छ।"
        )
        StrengthLevel.STRONG -> LocalizedParagraph(
            en = "Your vitality is strong, supporting good health and sustained energy throughout life.",
            ne = "तपाईंको जीवनी शक्ति बलियो छ।"
        )
        StrengthLevel.MODERATE -> LocalizedParagraph(
            en = "Your vitality is moderate, benefiting from regular attention to health and energy management.",
            ne = "तपाईंको जीवनी शक्ति मध्यम छ।"
        )
        else -> LocalizedParagraph(
            en = "Your vitality requires conscious nurturing through healthy lifestyle and positive practices.",
            ne = "तपाईंको जीवनी शक्तिलाई सचेत पालनपोषण चाहिन्छ।"
        )
    }
    
    fun getOverallInterpretation(sun: PlanetPosition, strength: StrengthLevel): LocalizedParagraph {
        return LocalizedParagraph(
            en = "Your Sun in ${sun.sign.displayName} in the ${sun.house}th house represents your core self, " +
                "ego, and life force. With ${strength.displayName.lowercase()} placement, your solar energy " +
                "expresses through ${getHouseArea(sun.house)} activities, shaping how you seek recognition.",
            ne = "तपाईंको सूर्य ${sun.sign.displayName}मा ${sun.house}औं भावमा तपाईंको मूल आत्म, " +
                "अहंकार र जीवन शक्तिलाई प्रतिनिधित्व गर्छ।"
        )
    }
}

/**
 * Atmakaraka Text Generator - Soul significator interpretations
 */
object AtmakarakaTextGenerator {
    
    fun getSoulDesire(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(
            en = "Your soul seeks recognition, authority, and the expression of your unique creative power.",
            ne = "तपाईंको आत्माले मान्यता, अधिकार र तपाईंको अद्वितीय रचनात्मक शक्तिको अभिव्यक्ति खोज्छ।"
        )
        Planet.MOON -> LocalizedParagraph(
            en = "Your soul seeks emotional fulfillment, nurturing connections, and inner peace.",
            ne = "तपाईंको आत्माले भावनात्मक पूर्ति, पालनपोषण सम्बन्धहरू र आन्तरिक शान्ति खोज्छ।"
        )
        Planet.MARS -> LocalizedParagraph(
            en = "Your soul seeks conquest, courage, and the overcoming of challenges through action.",
            ne = "तपाईंको आत्माले विजय, साहस र कार्य मार्फत चुनौतीहरू पार गर्ने खोज्छ।"
        )
        Planet.MERCURY -> LocalizedParagraph(
            en = "Your soul seeks knowledge, communication mastery, and intellectual understanding.",
            ne = "तपाईंको आत्माले ज्ञान, सञ्चार निपुणता र बौद्धिक समझ खोज्छ।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "Your soul seeks wisdom, spiritual growth, and the expansion of consciousness.",
            ne = "तपाईंको आत्माले ज्ञान, आध्यात्मिक विकास र चेतनाको विस्तार खोज्छ।"
        )
        Planet.VENUS -> LocalizedParagraph(
            en = "Your soul seeks love, beauty, harmony, and the experience of sensual pleasures.",
            ne = "तपाईंको आत्माले प्रेम, सौन्दर्य, सामंजस्य र संवेदी आनन्दहरूको अनुभव खोज्छ।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "Your soul seeks mastery through discipline, lasting achievement, and karmic completion.",
            ne = "तपाईंको आत्माले अनुशासन, स्थायी उपलब्धि र कार्मिक पूर्णता मार्फत निपुणता खोज्छ।"
        )
        else -> LocalizedParagraph(
            en = "Your soul seeks the unique lessons that ${planet.displayName} represents in your journey.",
            ne = "तपाईंको आत्माले ${planet.displayName}ले तपाईंको यात्रामा प्रतिनिधित्व गर्ने अद्वितीय पाठहरू खोज्छ।"
        )
    }
    
    fun getKarmicLesson(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(
            en = "Your karmic lesson involves learning humility while expressing authentic power.",
            ne = "तपाईंको कार्मिक पाठमा प्रामाणिक शक्ति व्यक्त गर्दा विनम्रता सिक्नु समावेश छ।"
        )
        Planet.MOON -> LocalizedParagraph(
            en = "Your karmic lesson involves emotional balance and learning healthy attachment.",
            ne = "तपाईंको कार्मिक पाठमा भावनात्मक सन्तुलन र स्वस्थ आसक्ति सिक्नु समावेश छ।"
        )
        Planet.SATURN -> LocalizedParagraph(
            en = "Your karmic lesson involves patience, responsibility, and accepting life's limitations.",
            ne = "तपाईंको कार्मिक पाठमा धैर्य, जिम्मेवारी र जीवनका सीमितताहरू स्वीकार गर्नु समावेश छ।"
        )
        else -> LocalizedParagraph(
            en = "Your karmic lesson connects to ${planet.displayName}'s themes in this lifetime.",
            ne = "तपाईंको कार्मिक पाठ यस जीवनकालमा ${planet.displayName}का विषयहरूसँग जोडिएको छ।"
        )
    }
    
    fun getSpiritualPath(planet: Planet): LocalizedParagraph = when (planet) {
        Planet.SUN -> LocalizedParagraph(
            en = "Your spiritual path unfolds through self-realization and expressing your divine nature.",
            ne = "तपाईंको आध्यात्मिक मार्ग आत्म-साक्षात्कार र तपाईंको दिव्य स्वभाव व्यक्त गर्ने मार्फत खुल्छ।"
        )
        Planet.JUPITER -> LocalizedParagraph(
            en = "Your spiritual path unfolds through wisdom-seeking, teaching, and dharmic living.",
            ne = "तपाईंको आध्यात्मिक मार्ग ज्ञान-खोज, शिक्षण र धार्मिक जीवन मार्फत खुल्छ।"
        )
        else -> LocalizedParagraph(
            en = "Your spiritual path is uniquely colored by ${planet.displayName}'s influence.",
            ne = "तपाईंको आध्यात्मिक मार्ग ${planet.displayName}को प्रभावले अद्वितीय रूपमा रंगिएको छ।"
        )
    }
    
    fun getOverall(planet: Planet, position: PlanetPosition?): LocalizedParagraph {
        val sign = position?.sign?.displayName ?: "its sign"
        return LocalizedParagraph(
            en = "${planet.displayName} as your Atmakaraka in $sign reveals your soul's deepest purpose. " +
                "This planet represents the core lessons your soul chose to learn in this incarnation.",
            ne = "${planet.displayName} तपाईंको आत्मकारकको रूपमा $sign मा तपाईंको आत्माको गहिरो उद्देश्य प्रकट गर्छ।"
        )
    }
}

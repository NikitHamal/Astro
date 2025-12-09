package com.astro.storm.ui.util

import androidx.compose.runtime.Composable
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.ZodiacSign
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.ephemeris.YogaCalculator
import com.astro.storm.ephemeris.RemediesCalculator

@Composable
fun Planet.localizedName(): String {
    val key = when (this) {
        Planet.SUN -> StringKey.PLANET_SUN
        Planet.MOON -> StringKey.PLANET_MOON
        Planet.MARS -> StringKey.PLANET_MARS
        Planet.MERCURY -> StringKey.PLANET_MERCURY
        Planet.JUPITER -> StringKey.PLANET_JUPITER
        Planet.VENUS -> StringKey.PLANET_VENUS
        Planet.SATURN -> StringKey.PLANET_SATURN
        Planet.RAHU -> StringKey.PLANET_RAHU
        Planet.KETU -> StringKey.PLANET_KETU
        Planet.ASCENDANT -> StringKey.PLANET_ASCENDANT
    }
    return stringResource(key)
}

@Composable
fun YogaCalculator.YogaCategory.localizedName(): String {
    val key = when (this) {
        YogaCalculator.YogaCategory.RAJA_YOGA -> StringKey.YOGA_CAT_RAJA
        YogaCalculator.YogaCategory.DHANA_YOGA -> StringKey.YOGA_CAT_DHANA
        YogaCalculator.YogaCategory.MAHAPURUSHA_YOGA -> StringKey.YOGA_CAT_PANCHA_MAHAPURUSHA
        YogaCalculator.YogaCategory.NABHASA_YOGA -> StringKey.YOGA_CAT_NABHASA
        YogaCalculator.YogaCategory.CHANDRA_YOGA -> StringKey.YOGA_CAT_CHANDRA
        YogaCalculator.YogaCategory.SOLAR_YOGA -> StringKey.YOGA_CAT_SOLAR
        YogaCalculator.YogaCategory.NEGATIVE_YOGA -> StringKey.YOGA_CAT_NEGATIVE
        YogaCalculator.YogaCategory.SPECIAL_YOGA -> StringKey.YOGA_CAT_SPECIAL
    }
    return stringResource(key)
}

@Composable
fun YogaCalculator.YogaStrength.localizedName(): String {
    val key = when (this) {
        YogaCalculator.YogaStrength.EXTREMELY_STRONG -> StringKey.YOGA_STRENGTH_EXTREMELY_STRONG
        YogaCalculator.YogaStrength.STRONG -> StringKey.YOGA_STRENGTH_STRONG
        YogaCalculator.YogaStrength.MODERATE -> StringKey.YOGA_STRENGTH_MODERATE
        YogaCalculator.YogaStrength.WEAK -> StringKey.YOGA_STRENGTH_WEAK
        YogaCalculator.YogaStrength.VERY_WEAK -> StringKey.YOGA_STRENGTH_VERY_WEAK
    }
    return stringResource(key)
}

@Composable
fun ZodiacSign.localizedName(): String {
    val key = when (this) {
        ZodiacSign.Aries -> StringKey.SIGN_ARIES
        ZodiacSign.Taurus -> StringKey.SIGN_TAURUS
        ZodiacSign.Gemini -> StringKey.SIGN_GEMINI
        ZodiacSign.Cancer -> StringKey.SIGN_CANCER
        ZodiacSign.Leo -> StringKey.SIGN_LEO
        ZodiacSign.Virgo -> StringKey.SIGN_VIRGO
        ZodiacSign.Libra -> StringKey.SIGN_LIBRA
        ZodiacSign.Scorpio -> StringKey.SIGN_SCORPIO
        ZodiacSign.Sagittarius -> StringKey.SIGN_SAGITTARIUS
        ZodiacSign.Capricorn -> StringKey.SIGN_CAPRICORN
        ZodiacSign.Aquarius -> StringKey.SIGN_AQUARIUS
        ZodiacSign.Pisces -> StringKey.SIGN_PISCES
    }
    return stringResource(key)
}

@Composable
fun Nakshatra.localizedName(): String {
    val key = when (this.number) {
        1 -> StringKey.NAKSHATRA_ASHWINI
        2 -> StringKey.NAKSHATRA_BHARANI
        3 -> StringKey.NAKSHATRA_KRITTIKA
        4 -> StringKey.NAKSHATRA_ROHINI
        5 -> StringKey.NAKSHATRA_MRIGASHIRSHA
        6 -> StringKey.NAKSHATRA_ARDRA
        7 -> StringKey.NAKSHATRA_PUNARVASU
        8 -> StringKey.NAKSHATRA_PUSHYA
        9 -> StringKey.NAKSHATRA_ASHLESHA
        10 -> StringKey.NAKSHATRA_MAGHA
        11 -> StringKey.NAKSHATRA_PURVA_PHALGUNI
        12 -> StringKey.NAKSHATRA_UTTARA_PHALGUNI
        13 -> StringKey.NAKSHATRA_HASTA
        14 -> StringKey.NAKSHATRA_CHITRA
        15 -> StringKey.NAKSHATRA_SWATI
        16 -> StringKey.NAKSHATRA_VISHAKHA
        17 -> StringKey.NAKSHATRA_ANURADHA
        18 -> StringKey.NAKSHATRA_JYESHTHA
        19 -> StringKey.NAKSHATRA_MULA
        20 -> StringKey.NAKSHATRA_PURVA_ASHADHA
        21 -> StringKey.NAKSHATRA_UTTARA_ASHADHA
        22 -> StringKey.NAKSHATRA_SHRAVANA
        23 -> StringKey.NAKSHATRA_DHANISHTA
        24 -> StringKey.NAKSHATRA_SHATABHISHA
        25 -> StringKey.NAKSHATRA_PURVA_BHADRAPADA
        26 -> StringKey.NAKSHATRA_UTTARA_BHADRAPADA
        27 -> StringKey.NAKSHATRA_REVATI
        else -> StringKey.UNKNOWN // Should not happen
    }
    return stringResource(key)
}

fun Planet.getLocalizedName(language: Language): String {
    val key = when (this) {
        Planet.SUN -> StringKey.PLANET_SUN
        Planet.MOON -> StringKey.PLANET_MOON
        Planet.MARS -> StringKey.PLANET_MARS
        Planet.MERCURY -> StringKey.PLANET_MERCURY
        Planet.JUPITER -> StringKey.PLANET_JUPITER
        Planet.VENUS -> StringKey.PLANET_VENUS
        Planet.SATURN -> StringKey.PLANET_SATURN
        Planet.RAHU -> StringKey.PLANET_RAHU
        Planet.KETU -> StringKey.PLANET_KETU
        Planet.ASCENDANT -> StringKey.PLANET_ASCENDANT
    }
    return StringResources.get(key, language)
}

@Composable
fun RemediesCalculator.RemedyCategory.localizedName(): String {
    val key = when (this) {
        RemediesCalculator.RemedyCategory.GEMSTONE -> StringKey.REMEDY_CAT_GEMSTONE
        RemediesCalculator.RemedyCategory.MANTRA -> StringKey.REMEDY_CAT_MANTRA
        RemediesCalculator.RemedyCategory.YANTRA -> StringKey.REMEDY_CAT_YANTRA
        RemediesCalculator.RemedyCategory.CHARITY -> StringKey.REMEDY_CAT_CHARITY
        RemediesCalculator.RemedyCategory.FASTING -> StringKey.REMEDY_CAT_FASTING
        RemediesCalculator.RemedyCategory.COLOR -> StringKey.REMEDY_CAT_COLOR
        RemediesCalculator.RemedyCategory.METAL -> StringKey.REMEDY_CAT_METAL
        RemediesCalculator.RemedyCategory.RUDRAKSHA -> StringKey.REMEDY_CAT_RUDRAKSHA
        RemediesCalculator.RemedyCategory.DEITY -> StringKey.REMEDY_CAT_DEITY
        RemediesCalculator.RemedyCategory.LIFESTYLE -> StringKey.REMEDY_CAT_LIFESTYLE
    }
    return stringResource(key)
}

@Composable
fun RemediesCalculator.RemedyPriority.localizedName(): String {
    val key = when (this) {
        RemediesCalculator.RemedyPriority.ESSENTIAL -> StringKey.REMEDY_PRIORITY_ESSENTIAL
        RemediesCalculator.RemedyPriority.HIGHLY_RECOMMENDED -> StringKey.REMEDY_PRIORITY_HIGHLY_RECOMMENDED
        RemediesCalculator.RemedyPriority.RECOMMENDED -> StringKey.REMEDY_PRIORITY_RECOMMENDED
        RemediesCalculator.RemedyPriority.OPTIONAL -> StringKey.REMEDY_PRIORITY_OPTIONAL
    }
    return stringResource(key)
}

@Composable
fun RemediesCalculator.PlanetaryStrength.localizedName(): String {
    val key = when (this) {
        RemediesCalculator.PlanetaryStrength.VERY_STRONG -> StringKey.PLANETARY_STRENGTH_VERY_STRONG
        RemediesCalculator.PlanetaryStrength.STRONG -> StringKey.PLANETARY_STRENGTH_STRONG
        RemediesCalculator.PlanetaryStrength.MODERATE -> StringKey.PLANETARY_STRENGTH_MODERATE
        RemediesCalculator.PlanetaryStrength.WEAK -> StringKey.PLANETARY_STRENGTH_WEAK
        RemediesCalculator.PlanetaryStrength.VERY_WEAK -> StringKey.PLANETARY_STRENGTH_VERY_WEAK
        RemediesCalculator.PlanetaryStrength.AFFLICTED -> StringKey.PLANETARY_STRENGTH_AFFLICTED
    }
    return stringResource(key)
}

fun ZodiacSign.getLocalizedName(language: Language): String {
    val key = when (this) {
        ZodiacSign.Aries -> StringKey.SIGN_ARIES
        ZodiacSign.Taurus -> StringKey.SIGN_TAURUS
        ZodiacSign.Gemini -> StringKey.SIGN_GEMINI
        ZodiacSign.Cancer -> StringKey.SIGN_CANCER
        ZodiacSign.Leo -> StringKey.SIGN_LEO
        ZodiacSign.Virgo -> StringKey.SIGN_VIRGO
        ZodiacSign.Libra -> StringKey.SIGN_LIBRA
        ZodiacSign.Scorpio -> StringKey.SIGN_SCORPIO
        ZodiacSign.Sagittarius -> StringKey.SIGN_SAGITTARIUS
        ZodiacSign.Capricorn -> StringKey.SIGN_CAPRICORN
        ZodiacSign.Aquarius -> StringKey.SIGN_AQUARIUS
        ZodiacSign.Pisces -> StringKey.SIGN_PISCES
    }
    return StringResources.get(key, language)
}

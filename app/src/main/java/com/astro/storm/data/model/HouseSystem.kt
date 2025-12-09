package com.astro.storm.data.model

/**
 * House systems used in Vedic astrology
 */
import com.astro.storm.data.localization.StringKey

enum class HouseSystem(val code: Char, val displayNameKey: StringKey) {
    PLACIDUS('P', StringKey.HOUSE_PLACIDUS),
    KOCH('K', StringKey.HOUSE_KOCH),
    PORPHYRIUS('O', StringKey.HOUSE_PORPHYRIUS),
    REGIOMONTANUS('R', StringKey.HOUSE_REGIOMONTANUS),
    CAMPANUS('C', StringKey.HOUSE_CAMPANUS),
    EQUAL('E', StringKey.HOUSE_EQUAL),
    WHOLE_SIGN('W', StringKey.HOUSE_WHOLE_SIGN),
    VEHLOW('V', StringKey.HOUSE_VEHLOW),
    MERIDIAN('X', StringKey.HOUSE_MERIDIAN),
    MORINUS('M', StringKey.HOUSE_MORINUS),
    ALCABITUS('B', StringKey.HOUSE_ALCABITUS);

    companion object {
        val DEFAULT = PLACIDUS
    }
}

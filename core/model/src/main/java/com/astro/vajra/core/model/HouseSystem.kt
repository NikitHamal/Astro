package com.astro.vajra.core.model

import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyInterface
import com.astro.vajra.core.common.StringResources

/**
 * House systems used in Vedic astrology
 */
enum class HouseSystem(val code: Char, val stringKey: StringKeyInterface) {
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

    val displayName: String get() = stringKey.en

    fun getLocalizedName(language: Language): String {
        return StringResources.get(stringKey, language)
    }

    companion object {
        val DEFAULT = WHOLE_SIGN
    }
}

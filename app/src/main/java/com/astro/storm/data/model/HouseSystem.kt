package com.astro.storm.data.model

import androidx.annotation.StringRes
import com.astro.storm.R

/**
 * House systems used in Vedic astrology
 */
enum class HouseSystem(val code: Char, @StringRes val stringRes: Int) {
    PLACIDUS('P', R.string.house_system_placidus),
    KOCH('K', R.string.house_system_koch),
    PORPHYRIUS('O', R.string.house_system_porphyrius),
    REGIOMONTANUS('R', R.string.house_system_regiomontanus),
    CAMPANUS('C', R.string.house_system_campanus),
    EQUAL('E', R.string.house_system_equal),
    WHOLE_SIGN('W', R.string.house_system_whole_sign),
    VEHLOW('V', R.string.house_system_vehlow),
    MERIDIAN('X', R.string.house_system_meridian),
    MORINUS('M', R.string.house_system_morinus),
    ALCABITUS('B', R.string.house_system_alcabitus);

    companion object {
        val DEFAULT = PLACIDUS
    }
}

package com.astro.storm.ephemeris

import android.content.Context
import androidx.annotation.StringRes
import com.astro.storm.R
import com.astro.storm.data.model.Nakshatra
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.model.ZodiacSign
import kotlin.math.abs

object MatchmakingCalculator {

    private const val MAX_VARNA = 1.0
    private const val MAX_VASHYA = 2.0
    private const val MAX_TARA = 3.0
    private const val MAX_YONI = 4.0
    private const val MAX_GRAHA_MAITRI = 5.0
    private const val MAX_GANA = 6.0
    private const val MAX_BHAKOOT = 7.0
    private const val MAX_NADI = 8.0
    private const val MAX_TOTAL = 36.0

    private const val EXCELLENT_THRESHOLD = 28.0
    private const val GOOD_THRESHOLD = 21.0
    private const val AVERAGE_THRESHOLD = 18.0
    private const val POOR_THRESHOLD = 14.0

    enum class Varna(val value: Int, @StringRes val displayName: Int) {
        BRAHMIN(4, R.string.varna_brahmin),
        KSHATRIYA(3, R.string.varna_kshatriya),
        VAISHYA(2, R.string.varna_vaishya),
        SHUDRA(1, R.string.varna_shudra)
    }

    enum class Vashya(@StringRes val displayName: Int) {
        CHATUSHPADA(R.string.vashya_chatushpada),
        MANAVA(R.string.vashya_manava),
        JALACHARA(R.string.vashya_jalachara),
        VANACHARA(R.string.vashya_vanachara),
        KEETA(R.string.vashya_keeta)
    }

    enum class Gana(@StringRes val displayName: Int, @StringRes val description: Int) {
        DEVA(R.string.gana_deva, R.string.gana_deva_desc),
        MANUSHYA(R.string.gana_manushya, R.string.gana_manushya_desc),
        RAKSHASA(R.string.gana_rakshasa, R.string.gana_rakshasa_desc)
    }

    enum class Yoni(@StringRes val animal: Int, val gender: YoniGender, val groupId: Int) {
        ASHWA_MALE(R.string.yoni_animal_horse, YoniGender.MALE, 1),
        ASHWA_FEMALE(R.string.yoni_animal_horse, YoniGender.FEMALE, 1),
        GAJA_MALE(R.string.yoni_animal_elephant, YoniGender.MALE, 2),
        GAJA_FEMALE(R.string.yoni_animal_elephant, YoniGender.FEMALE, 2),
        MESHA_MALE(R.string.yoni_animal_sheep, YoniGender.MALE, 3),
        MESHA_FEMALE(R.string.yoni_animal_sheep, YoniGender.FEMALE, 3),
        SARPA_MALE(R.string.yoni_animal_serpent, YoniGender.MALE, 4),
        SARPA_FEMALE(R.string.yoni_animal_serpent, YoniGender.FEMALE, 4),
        SHWAN_MALE(R.string.yoni_animal_dog, YoniGender.MALE, 5),
        SHWAN_FEMALE(R.string.yoni_animal_dog, YoniGender.FEMALE, 5),
        MARJAR_MALE(R.string.yoni_animal_cat, YoniGender.MALE, 6),
        MARJAR_FEMALE(R.string.yoni_animal_cat, YoniGender.FEMALE, 6),
        MUSHAK_MALE(R.string.yoni_animal_rat, YoniGender.MALE, 7),
        MUSHAK_FEMALE(R.string.yoni_animal_rat, YoniGender.FEMALE, 7),
        GAU_MALE(R.string.yoni_animal_cow, YoniGender.MALE, 8),
        GAU_FEMALE(R.string.yoni_animal_cow, YoniGender.FEMALE, 8),
        MAHISH_MALE(R.string.yoni_animal_buffalo, YoniGender.MALE, 9),
        MAHISH_FEMALE(R.string.yoni_animal_buffalo, YoniGender.FEMALE, 9),
        VYAGHRA_MALE(R.string.yoni_animal_tiger, YoniGender.MALE, 10),
        VYAGHRA_FEMALE(R.string.yoni_animal_tiger, YoniGender.FEMALE, 10),
        MRIGA_MALE(R.string.yoni_animal_deer, YoniGender.MALE, 11),
        MRIGA_FEMALE(R.string.yoni_animal_deer, YoniGender.FEMALE, 11),
        VANAR_MALE(R.string.yoni_animal_monkey, YoniGender.MALE, 12),
        VANAR_FEMALE(R.string.yoni_animal_monkey, YoniGender.FEMALE, 12),
        NAKUL_MALE(R.string.yoni_animal_mongoose, YoniGender.MALE, 13),
        NAKUL_FEMALE(R.string.yoni_animal_mongoose, YoniGender.FEMALE, 13),
        SIMHA_MALE(R.string.yoni_animal_lion, YoniGender.MALE, 14),
        SIMHA_FEMALE(R.string.yoni_animal_lion, YoniGender.FEMALE, 14)
    }

    enum class YoniGender(@StringRes val displayName: Int) {
        MALE(R.string.yoni_gender_male),
        FEMALE(R.string.yoni_gender_female)
    }

    enum class Nadi(@StringRes val displayName: Int, @StringRes val description: Int) {
        ADI(R.string.nadi_adi, R.string.nadi_adi_desc),
        MADHYA(R.string.nadi_madhya, R.string.nadi_madhya_desc),
        ANTYA(R.string.nadi_antya, R.string.nadi_antya_desc)
    }

    enum class Rajju(@StringRes val displayName: Int, @StringRes val bodyPart: Int) {
        PADA(R.string.rajju_pada, R.string.rajju_body_feet),
        KATI(R.string.rajju_kati, R.string.rajju_body_waist),
        NABHI(R.string.rajju_nabhi, R.string.rajju_body_navel),
        KANTHA(R.string.rajju_kantha, R.string.rajju_body_neck),
        SIRO(R.string.rajju_siro, R.string.rajju_body_head)
    }

    enum class ManglikDosha(@StringRes val displayName: Int, val severity: Int) {
        NONE(R.string.manglik_dosha_none, 0),
        PARTIAL(R.string.manglik_dosha_partial, 1),
        FULL(R.string.manglik_dosha_full, 2),
        DOUBLE(R.string.manglik_dosha_double, 3)
    }

    enum class CompatibilityRating(@StringRes val displayName: Int, @StringRes val description: Int) {
        EXCELLENT(R.string.compatibility_rating_excellent, R.string.compatibility_rating_excellent_desc),
        GOOD(R.string.compatibility_rating_good, R.string.compatibility_rating_good_desc),
        AVERAGE(R.string.compatibility_rating_average, R.string.compatibility_rating_average_desc),
        BELOW_AVERAGE(R.string.compatibility_rating_below_average, R.string.compatibility_rating_below_average_desc),
        POOR(R.string.compatibility_rating_poor, R.string.compatibility_rating_poor_desc)
    }

    enum class RajjuArudha(@StringRes val displayName: Int) {
        ASCENDING(R.string.rajju_arudha_ascending),
        DESCENDING(R.string.rajju_arudha_descending)
    }
    private fun getTaraName(taraNumber: Int): Int {
        return when (taraNumber) {
            1 -> R.string.tara_janma
            2 -> R.string.tara_sampat
            3 -> R.string.tara_vipat
            4 -> R.string.tara_kshema
            5 -> R.string.tara_pratyari
            6 -> R.string.tara_sadhana
            7 -> R.string.tara_vadha
            8 -> R.string.tara_mitra
            9 -> R.string.tara_parama_mitra
            else -> R.string.tara_unknown
        }
    }
    private fun getRajjuWarning(rajju: Rajju): Int {
        return when (rajju) {
            Rajju.SIRO -> R.string.rajju_warning_siro
            Rajju.KANTHA -> R.string.rajju_warning_kantha
            Rajju.NABHI -> R.string.rajju_warning_nabhi
            Rajju.KATI -> R.string.rajju_warning_kati
            Rajju.PADA -> R.string.rajju_warning_pada
        }
    }

    fun calculateMatchmaking(context: Context, brideChart: VedicChart, groomChart: VedicChart): MatchmakingResult {
        val brideMoon = brideChart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalArgumentException("Bride chart missing Moon position")
        val groomMoon = groomChart.planetPositions.find { it.planet == Planet.MOON }
            ?: throw IllegalArgumentException("Groom chart missing Moon position")

        val brideMoonSign = brideMoon.sign
        val groomMoonSign = groomMoon.sign
        val (brideNakshatra, bridePada) = Nakshatra.fromLongitude(brideMoon.longitude)
        val (groomNakshatra, groomPada) = Nakshatra.fromLongitude(groomMoon.longitude)

        val gunaAnalyses = listOf(
            calculateVarna(context, brideMoonSign, groomMoonSign),
            calculateVashya(context, brideMoonSign, groomMoonSign),
            calculateTara(context, brideNakshatra, groomNakshatra),
            calculateYoni(context, brideNakshatra, groomNakshatra),
            calculateGrahaMaitri(context, brideMoonSign, groomMoonSign),
            calculateGana(context, brideNakshatra, groomNakshatra),
            calculateBhakoot(context, brideMoonSign, groomMoonSign),
            calculateNadi(context, brideNakshatra, groomNakshatra, brideMoonSign, groomMoonSign, bridePada, groomPada)
        )

        val totalPoints = gunaAnalyses.sumOf { it.obtainedPoints }
        val percentage = (totalPoints / MAX_TOTAL) * 100.0

        val rating = determineRating(totalPoints, gunaAnalyses)

        val brideManglik = calculateManglikDosha(context, brideChart, "Bride")
        val groomManglik = calculateManglikDosha(context, groomChart, "Groom")
        val manglikCompatibility = assessManglikCompatibility(context, brideManglik, groomManglik)

        val additionalFactors = calculateAdditionalFactors(context, brideNakshatra, groomNakshatra)

        val specialConsiderations = calculateSpecialConsiderations(
            context, brideChart, groomChart, gunaAnalyses, brideManglik, groomManglik, additionalFactors
        )

        val remedies = calculateRemedies(
            gunaAnalyses, brideManglik, groomManglik, totalPoints, additionalFactors
        )

        val summary = generateSummary(
            context, totalPoints, rating, gunaAnalyses, brideManglik, groomManglik, additionalFactors
        )

        val detailedAnalysis = generateDetailedAnalysis(context, brideChart, groomChart, gunaAnalyses, additionalFactors)

        return MatchmakingResult(
            brideChart = brideChart,
            groomChart = groomChart,
            gunaAnalyses = gunaAnalyses,
            totalPoints = totalPoints,
            maxPoints = MAX_TOTAL,
            percentage = percentage,
            rating = rating,
            brideManglik = brideManglik,
            groomManglik = groomManglik,
            manglikCompatibility = manglikCompatibility,
            additionalFactors = additionalFactors,
            specialConsiderations = specialConsiderations,
            remedies = remedies,
            summary = summary,
            detailedAnalysis = detailedAnalysis
        )
    }
}

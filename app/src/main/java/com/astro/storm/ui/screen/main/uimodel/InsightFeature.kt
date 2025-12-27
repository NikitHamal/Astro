package com.astro.storm.ui.screen.main.uimodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources
import com.astro.storm.ui.theme.DarkAppThemeColors
import kotlin.LazyThreadSafetyMode

@Stable
enum class InsightFeature(
    val titleKey: StringKey,
    val descriptionKey: StringKey,
    val icon: ImageVector,
    val color: Color,
    val category: FeatureCategory,
    val isPinned: Boolean = false,
    val isImplemented: Boolean = true
) {
    FULL_CHART(
        titleKey = StringKey.FEATURE_BIRTH_CHART,
        descriptionKey = StringKey.FEATURE_BIRTH_CHART_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentPrimary,
        category = FeatureCategory.CHARTS,
        isPinned = true
    ),
    PLANETS(
        titleKey = StringKey.FEATURE_PLANETS,
        descriptionKey = StringKey.FEATURE_PLANETS_DESC,
        icon = Icons.Outlined.Public,
        color = DarkAppThemeColors.LifeAreaCareer,
        category = FeatureCategory.CHARTS,
        isPinned = true
    ),
    YOGAS(
        titleKey = StringKey.FEATURE_YOGAS,
        descriptionKey = StringKey.FEATURE_YOGAS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.YOGAS,
        isPinned = true
    ),
    DASHAS(
        titleKey = StringKey.FEATURE_DASHAS,
        descriptionKey = StringKey.FEATURE_DASHAS_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS,
        isPinned = true
    ),
    TRANSITS(
        titleKey = StringKey.FEATURE_TRANSITS,
        descriptionKey = StringKey.FEATURE_TRANSITS_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.TRANSITS,
        isPinned = true
    ),
    PREDICTIONS(
        titleKey = StringKey.FEATURE_PREDICTIONS,
        descriptionKey = StringKey.FEATURE_PREDICTIONS_DESC,
        icon = Icons.Outlined.AutoAwesome,
        color = DarkAppThemeColors.AccentPrimary,
        category = FeatureCategory.PREDICTIONS,
        isPinned = true
    ),
    ASHTAKAVARGA(
        titleKey = StringKey.FEATURE_ASHTAKAVARGA,
        descriptionKey = StringKey.FEATURE_ASHTAKAVARGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.SuccessColor,
        category = FeatureCategory.CHARTS
    ),
    PANCHANGA(
        titleKey = StringKey.FEATURE_PANCHANGA,
        descriptionKey = StringKey.FEATURE_PANCHANGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = DarkAppThemeColors.LifeAreaFinance,
        category = FeatureCategory.TRANSITS
    ),
    MATCHMAKING(
        titleKey = StringKey.FEATURE_MATCHMAKING,
        descriptionKey = StringKey.FEATURE_MATCHMAKING_DESC,
        icon = Icons.Outlined.Favorite,
        color = DarkAppThemeColors.LifeAreaLove,
        category = FeatureCategory.COMPATIBILITY
    ),
    MUHURTA(
        titleKey = StringKey.FEATURE_MUHURTA,
        descriptionKey = StringKey.FEATURE_MUHURTA_DESC,
        icon = Icons.Outlined.AccessTime,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.TRANSITS
    ),
    REMEDIES(
        titleKey = StringKey.FEATURE_REMEDIES,
        descriptionKey = StringKey.FEATURE_REMEDIES_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaHealth,
        category = FeatureCategory.PREDICTIONS
    ),
    VARSHAPHALA(
        titleKey = StringKey.FEATURE_VARSHAPHALA,
        descriptionKey = StringKey.FEATURE_VARSHAPHALA_DESC,
        icon = Icons.Outlined.Cake,
        color = DarkAppThemeColors.LifeAreaCareer,
        category = FeatureCategory.PREDICTIONS
    ),
    PRASHNA(
        titleKey = StringKey.FEATURE_PRASHNA,
        descriptionKey = StringKey.FEATURE_PRASHNA_DESC,
        icon = Icons.Outlined.HelpOutline,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.PREDICTIONS
    ),
    CHART_COMPARISON(
        titleKey = StringKey.FEATURE_SYNASTRY,
        descriptionKey = StringKey.FEATURE_SYNASTRY_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.LifeAreaFinance,
        category = FeatureCategory.COMPATIBILITY
    ),
    NAKSHATRA_ANALYSIS(
        titleKey = StringKey.FEATURE_NAKSHATRAS,
        descriptionKey = StringKey.FEATURE_NAKSHATRAS_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.CHARTS
    ),
    SHADBALA(
        titleKey = StringKey.FEATURE_SHADBALA,
        descriptionKey = StringKey.FEATURE_SHADBALA_DESC,
        icon = Icons.Outlined.Speed,
        color = DarkAppThemeColors.SuccessColor,
        category = FeatureCategory.CHARTS
    ),
    SHODASHVARGA(
        titleKey = StringKey.FEATURE_SHODASHVARGA,
        descriptionKey = StringKey.FEATURE_SHODASHVARGA_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.CHARTS
    ),
    YOGINI_DASHA(
        titleKey = StringKey.FEATURE_YOGINI_DASHA,
        descriptionKey = StringKey.FEATURE_YOGINI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaLove,
        category = FeatureCategory.DASHAS
    ),
    ARGALA(
        titleKey = StringKey.FEATURE_ARGALA,
        descriptionKey = StringKey.FEATURE_ARGALA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.CHARTS
    ),
    CHARA_DASHA(
        titleKey = StringKey.FEATURE_CHARA_DASHA,
        descriptionKey = StringKey.FEATURE_CHARA_DASHA_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS
    ),
    BHRIGU_BINDU(
        titleKey = StringKey.FEATURE_BHRIGU_BINDU,
        descriptionKey = StringKey.FEATURE_BHRIGU_BINDU_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.CHARTS
    ),
    ASHTOTTARI_DASHA(
        titleKey = StringKey.FEATURE_ASHTOTTARI_DASHA,
        descriptionKey = StringKey.FEATURE_ASHTOTTARI_DASHA_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.DASHAS
    ),
    SUDARSHANA_CHAKRA(
        titleKey = StringKey.FEATURE_SUDARSHANA_CHAKRA,
        descriptionKey = StringKey.FEATURE_SUDARSHANA_CHAKRA_DESC,
        icon = Icons.Outlined.Sync,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.CHARTS
    ),
    MRITYU_BHAGA(
        titleKey = StringKey.FEATURE_MRITYU_BHAGA,
        descriptionKey = StringKey.FEATURE_MRITYU_BHAGA_DESC,
        icon = Icons.Outlined.BarChart,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.CHARTS
    ),
    LAL_KITAB(
        titleKey = StringKey.FEATURE_LAL_KITAB,
        descriptionKey = StringKey.FEATURE_LAL_KITAB_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaHealth,
        category = FeatureCategory.PREDICTIONS
    ),
    DIVISIONAL_CHARTS(
        titleKey = StringKey.FEATURE_DIVISIONAL_CHARTS,
        descriptionKey = StringKey.FEATURE_DIVISIONAL_CHARTS_DESC,
        icon = Icons.Outlined.GridView,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.CHARTS
    ),
    UPACHAYA_TRANSIT(
        titleKey = StringKey.FEATURE_UPACHAYA_TRANSIT,
        descriptionKey = StringKey.FEATURE_UPACHAYA_TRANSIT_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.SuccessColor,
        category = FeatureCategory.TRANSITS
    ),
    KALACHAKRA_DASHA(
        titleKey = StringKey.FEATURE_KALACHAKRA_DASHA,
        descriptionKey = StringKey.FEATURE_KALACHAKRA_DASHA_DESC,
        icon = Icons.Outlined.HealthAndSafety,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS
    ),
    TARABALA(
        titleKey = StringKey.FEATURE_TARABALA,
        descriptionKey = StringKey.FEATURE_TARABALA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.PlanetMoon,
        category = FeatureCategory.TRANSITS
    ),
    ARUDHA_PADA(
        titleKey = StringKey.FEATURE_ARUDHA_PADA,
        descriptionKey = StringKey.FEATURE_ARUDHA_PADA_DESC,
        icon = Icons.Outlined.Spa,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.CHARTS
    ),
    GRAHA_YUDDHA(
        titleKey = StringKey.FEATURE_GRAHA_YUDDHA,
        descriptionKey = StringKey.FEATURE_GRAHA_YUDDHA_DESC,
        icon = Icons.Outlined.CompareArrows,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.YOGAS
    ),
    DASHA_SANDHI(
        titleKey = StringKey.FEATURE_DASHA_SANDHI,
        descriptionKey = StringKey.FEATURE_DASHA_SANDHI_DESC,
        icon = Icons.Outlined.Timeline,
        color = DarkAppThemeColors.LifeAreaSpiritual,
        category = FeatureCategory.DASHAS
    ),
    GOCHARA_VEDHA(
        titleKey = StringKey.FEATURE_GOCHARA_VEDHA,
        descriptionKey = StringKey.FEATURE_GOCHARA_VEDHA_DESC,
        icon = Icons.Outlined.Block,
        color = DarkAppThemeColors.WarningColor,
        category = FeatureCategory.TRANSITS
    ),
    KEMADRUMA_YOGA(
        titleKey = StringKey.FEATURE_KEMADRUMA_YOGA,
        descriptionKey = StringKey.FEATURE_KEMADRUMA_YOGA_DESC,
        icon = Icons.Outlined.Brightness2,
        color = DarkAppThemeColors.PlanetMoon,
        category = FeatureCategory.YOGAS
    ),
    PANCH_MAHAPURUSHA(
        titleKey = StringKey.FEATURE_PANCH_MAHAPURUSHA,
        descriptionKey = StringKey.FEATURE_PANCH_MAHAPURUSHA_DESC,
        icon = Icons.Outlined.Stars,
        color = DarkAppThemeColors.AccentGold,
        category = FeatureCategory.YOGAS
    ),
    NITYA_YOGA(
        titleKey = StringKey.FEATURE_NITYA_YOGA,
        descriptionKey = StringKey.FEATURE_NITYA_YOGA_DESC,
        icon = Icons.Outlined.CalendarMonth,
        color = DarkAppThemeColors.AccentTeal,
        category = FeatureCategory.YOGAS
    ),
    AVASTHA(
        titleKey = StringKey.FEATURE_AVASTHA,
        descriptionKey = StringKey.FEATURE_AVASTHA_DESC,
        icon = Icons.Outlined.Psychology,
        color = DarkAppThemeColors.AccentPrimary,
        category = FeatureCategory.CHARTS
    );

    fun getLocalizedTitle(language: Language): String = StringResources.get(titleKey, language)
    fun getLocalizedDescription(language: Language): String = StringResources.get(descriptionKey, language)

    companion object {
        val pinnedFeatures: List<InsightFeature> by lazy(LazyThreadSafetyMode.PUBLICATION) {
            entries.filter { it.isPinned }
        }

        fun getByCategory(category: FeatureCategory): List<InsightFeature> =
            if (category == FeatureCategory.ALL) entries.toList()
            else entries.filter { it.category == category }
    }
}
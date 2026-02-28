package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.SaptamsaAnalysis
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.ChildCountFactors
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.ChildIndication
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.FertilityAnalysis
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.SanthanaYoga
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.D7LagnaAnalysis
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.FifthHouseAnalysis
import com.astro.vajra.ephemeris.varga.SaptamsaAnalyzer.JupiterAnalysis
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.TabItem
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyInterface
import com.astro.vajra.core.common.StringKeySaptamsa
import com.astro.vajra.core.common.StringResources
import androidx.compose.ui.text.style.TextOverflow
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import com.astro.vajra.ui.components.common.vedicCornerMarkers

/**
 * SaptamsaScreen - D7 (Saptamsa) Children/Progeny Analysis Screen
 *
 * Displays comprehensive analysis of the D7 divisional chart for
 * children indications, fertility analysis, and Santhana Yogas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaptamsaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var analysis by remember { mutableStateOf<SaptamsaAnalysis?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Calculate Saptamsa Analysis
    LaunchedEffect(chart, language) {
        if (chart == null) {
            isLoading = false
            errorMessage = StringResources.get(StringKeySaptamsa.NO_CHART_AVAILABLE, language)
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        withContext(Dispatchers.Default) {
            try {
                analysis = SaptamsaAnalyzer.analyzeSaptamsa(chart, language)
            } catch (e: Exception) {
                errorMessage = e.message ?: StringResources.get(StringKeySaptamsa.ERROR_ANALYZING, language)
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = StringResources.get(StringKeySaptamsa.TITLE, language),
                subtitle = StringResources.get(StringKeySaptamsa.SUBTITLE, language),
                onBack = onBack
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        when {
            isLoading -> LoadingStateSS(modifier = Modifier.padding(padding))
            errorMessage != null -> ErrorStateSS(errorMessage!!, modifier = Modifier.padding(padding))
            analysis != null -> SaptamsaContent(
                analysis = analysis!!,
                language = language,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun SaptamsaContent(
    analysis: SaptamsaAnalysis,
    language: Language,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        StringResources.get(StringKeySaptamsa.TAB_OVERVIEW, language),
        StringResources.get(StringKeySaptamsa.TAB_CHILDREN, language),
        StringResources.get(StringKeySaptamsa.TAB_FERTILITY, language),
        StringResources.get(StringKeySaptamsa.TAB_YOGAS, language)
    )
    val tabItems = tabs.mapIndexed { index, title ->
        TabItem(
            title = title,
            accentColor = when (index) {
                0 -> AppTheme.AccentGold
                1 -> AppTheme.AccentTeal
                else -> AppTheme.AccentPrimary
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        ModernPillTabRow(
            tabs = tabItems,
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.padding(
                horizontal = NeoVedicTokens.ScreenPadding,
                vertical = NeoVedicTokens.SpaceXS
            )
        )

        // Tab Content
        when (selectedTab) {
            0 -> OverviewTabSS(analysis, language)
            1 -> ChildrenTab(analysis, language)
            2 -> FertilityTab(analysis, language)
            3 -> YogasTabSS(analysis, language)
        }
    }
}

@Composable
private fun OverviewTabSS(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Child Count Summary Card
        item {
            ChildCountSummaryCard(analysis.childCountEstimate, language)
        }

        // D7 Lagna Card
        item {
            D7LagnaCard(analysis, language)
        }

        // Fifth House Summary
        item {
            FifthHouseCard(analysis, language)
        }

        // Overall Interpretation
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
                color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = StringResources.get(StringKeySaptamsa.INTERPRETATION, language),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = StringResources.get(StringKeySaptamsa.RANGE, language, analysis.childCountEstimate.estimatedRange.first, analysis.childCountEstimate.estimatedRange.last) + ". " +
                                StringResources.get(StringKeySaptamsa.SUMMARY_FERTILITY_STATUS, language, StringResources.get(analysis.fertilityAnalysis.fertilityStatus.key, language)),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ChildCountSummaryCard(
    estimate: ChildCountFactors,
    language: Language
) {
    val displayCount = estimate.estimatedRange.last
    val countColor = when {
        displayCount >= 3 -> AppTheme.SuccessColor
        displayCount >= 1 -> AppTheme.AccentGold
        else -> AppTheme.WarningColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = countColor, 12.dp),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = countColor.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChildCare,
                    contentDescription = null,
                    tint = countColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.ESTIMATED_CHILDREN, language),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Estimated Count Display
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                countColor.copy(alpha = 0.3f),
                                countColor.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(3.dp, countColor.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$displayCount",
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        fontWeight = FontWeight.Bold,
                        color = countColor
                    )
                    Text(
                        text = if (displayCount == 1) StringResources.get(StringKeySaptamsa.CHILD, language) else StringResources.get(StringKeySaptamsa.CHILDREN, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = countColor.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Range indicator
            Text(
                text = StringResources.get(StringKeySaptamsa.RANGE, language, estimate.estimatedRange.first, estimate.estimatedRange.last),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Strength Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StrengthIndicator(
                    label = StringResources.get(StringKeySaptamsa.FIFTH_HOUSE, language),
                    strength = estimate.fifthLordStrength,
                    color = AppTheme.AccentGold
                )
                StrengthIndicator(
                    label = StringResources.get(StringKeySaptamsa.JUPITER, language),
                    strength = estimate.jupiterStrength,
                    color = AppTheme.PlanetJupiter
                )
            }
        }
    }
}

@Composable
private fun StrengthIndicator(
    label: String,
    strength: Double,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular progress
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { strength.toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxSize(),
                color = color,
                trackColor = color.copy(alpha = 0.1f),
                strokeWidth = 4.dp
            )
            Text(
                text = "${(strength * 100).toInt()}%",
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun D7LagnaCard(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaSign.getLocalizedName(language).take(2),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.D7_LAGNA_TITLE, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaSign.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            // D7 Lagna Lord
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.LAGNA_LORD, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaLord.getLocalizedName(language),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = getPlanetColorSS(analysis.d7LagnaAnalysis.lagnaLord)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.LORD_POSITION, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = (analysis.d7LagnaAnalysis.lagnaLordPosition?.sign ?: ZodiacSign.ARIES).getLocalizedName(language),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun FifthHouseCard(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.FIFTH_HOUSE_ANALYSIS, language),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fifth House Sign
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = StringResources.get(StringKeySaptamsa.SIGN, language),
                    value = analysis.fifthHouseAnalysis.fifthSign.getLocalizedName(language),
                    color = AppTheme.AccentGold
                )
                InfoItem(
                    label = StringResources.get(StringKeySaptamsa.LORD, language),
                    value = analysis.fifthHouseAnalysis.fifthLord.getLocalizedName(language),
                    color = getPlanetColorSS(analysis.fifthHouseAnalysis.fifthLord)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Planets in Fifth House
            if (analysis.fifthHouseAnalysis.planetsInFifth.isNotEmpty()) {
                Text(
                    text = StringResources.get(StringKeySaptamsa.PLANETS_IN_FIFTH, language),
                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(analysis.fifthHouseAnalysis.planetsInFifth) { position ->
                        PlanetChip(position.planet, language)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    color: Color
) {
    Column {
        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted
        )
        Text(
            text = value,
            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun PlanetChip(
    planet: Planet,
    language: Language
) {
    val planetColor = getPlanetColorSS(planet)

    Surface(
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
        color = planetColor.copy(alpha = 0.15f)
    ) {
        Text(
            text = planet.getLocalizedName(language),
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
            fontWeight = FontWeight.SemiBold,
            color = planetColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ChildrenTab(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeaderSS(
                title = StringResources.get(StringKeySaptamsa.INDIVIDUAL_CHILD_INDICATIONS, language),
                subtitle = StringResources.get(StringKeySaptamsa.CHILD_CHARACTERISTICS_DESC, language),
                icon = Icons.Outlined.ChildCare
            )
        }

        if (analysis.childIndications.isEmpty()) {
            item {
                EmptyStateSS(StringResources.get(StringKeySaptamsa.EMPTY_CHILD_INDICATIONS, language))
            }
        } else {
            itemsIndexed(analysis.childIndications) { index, indication ->
                ChildIndicationCard(
                    childNumber = index + 1,
                    indication = indication,
                    language = language
                )
            }
        }
    }
}

@Composable
private fun ChildIndicationCard(
    childNumber: Int,
    indication: ChildIndication,
    language: Language
) {
    val genderColor = when (indication.gender) {
        SaptamsaAnalyzer.ChildGender.MALE -> AppTheme.AccentPrimary
        SaptamsaAnalyzer.ChildGender.FEMALE -> AppTheme.LifeAreaLove
        else -> AppTheme.AccentTeal
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(genderColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$childNumber",
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                            fontWeight = FontWeight.Bold,
                            color = genderColor
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = StringResources.get(StringKeySaptamsa.CHILD_NUMBER, language, childNumber),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = StringResources.get(indication.gender.key, language),
                            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = genderColor
                        )
                    }
                }

                // Strength Badge
                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = getStrengthColor(indication.genderConfidence).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${(indication.genderConfidence * 100).toInt()}%",
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        fontWeight = FontWeight.Bold,
                        color = getStrengthColor(indication.genderConfidence),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            // Significator Planet
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.SIGNIFICATOR, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = indication.indicatingPlanet.getLocalizedName(language),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = getPlanetColorSS(indication.indicatingPlanet)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.RELATIONSHIP, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = StringResources.get(indication.relationshipQuality.key, language),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = indication.characteristics.firstOrNull()?.let { StringResources.get(it, language) } ?: StringResources.get(StringKeySaptamsa.ERROR_NO_SPECIFIC_DESC, language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            // Characteristics
            if (indication.characteristics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.CHARACTERISTICS_LABEL, language),
                    fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(indication.characteristics) { trait ->
                        Surface(
                            shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ChipBackground
                        ) {
                            Text(
                                text = StringResources.get(trait, language),
                                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                                color = AppTheme.TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            if (indication.timingIndicators.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeaderSS(
                    title = StringResources.get(StringKeySaptamsa.TIMING_INDICATORS, language),
                    subtitle = StringResources.get(StringKeySaptamsa.TIMING_SUBTITLE, language),
                    icon = Icons.Outlined.CalendarMonth
                )
                Spacer(modifier = Modifier.height(8.dp))
                indication.timingIndicators.forEach { timing ->
                    Text(
                        text = "\u2022 $timing",
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }

            if (indication.careerIndications.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeaderSS(
                    title = StringResources.get(StringKeySaptamsa.CAREER_INDICATIONS, language),
                    subtitle = StringResources.get(StringKeySaptamsa.CAREER_SUBTITLE, language),
                    icon = Icons.Outlined.Work
                )
                Spacer(modifier = Modifier.height(8.dp))
                indication.careerIndications.forEach { career ->
                    Text(
                        text = "\u2022 ${StringResources.get(career, language)}",
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }

            if (indication.healthIndications.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeaderSS(
                    title = StringResources.get(StringKeySaptamsa.HEALTH_INDICATIONS, language),
                    subtitle = StringResources.get(StringKeySaptamsa.HEALTH_SUBTITLE, language),
                    icon = Icons.Outlined.FavoriteBorder
                )
                Spacer(modifier = Modifier.height(8.dp))
                indication.healthIndications.forEach { health ->
                    Text(
                        text = "\u2022 ${StringResources.get(health, language)}",
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FertilityTab(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeaderSS(
                title = StringResources.get(StringKeySaptamsa.FERTILITY_ANALYSIS, language),
                subtitle = StringResources.get(StringKeySaptamsa.FERTILITY_SUBTITLE, language),
                icon = Icons.Outlined.FavoriteBorder
            )
        }

        // Fertility Overview Card
        item {
            FertilityOverviewCard(analysis.fertilityAnalysis, language)
        }

        // Favorable Periods
        if (analysis.fertilityAnalysis.timingForConception.isNotEmpty()) {
            item {
                Text(
                    text = StringResources.get(StringKeySaptamsa.FAVORABLE_PERIODS, language),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            items(analysis.fertilityAnalysis.timingForConception) { period ->
                FavorablePeriodCard(period.toString())
            }
        }

        // Recommendations
        if (analysis.fertilityAnalysis.remedies.isNotEmpty()) {
            item {
                RecommendationsCard(analysis.fertilityAnalysis.remedies.map { StringResources.get(it, language) }, language)
            }
        }
    }
}

@Composable
private fun FertilityOverviewCard(
    fertility: FertilityAnalysis,
    language: Language
) {
    val overallColor = when {
        fertility.overallScore >= 0.7 -> AppTheme.SuccessColor
        fertility.overallScore >= 0.4 -> AppTheme.AccentGold
        else -> AppTheme.WarningColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth().vedicCornerMarkers(color = overallColor, 12.dp),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = overallColor.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.OVERALL_FERTILITY_SCORE, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = StringResources.get(fertility.fertilityStatus.key, language),
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S19,
                        fontWeight = FontWeight.Bold,
                        color = overallColor
                    )
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(overallColor.copy(alpha = 0.15f))
                        .border(2.dp, overallColor.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${(fertility.overallScore * 100).toInt()}%",
                        fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S21,
                        fontWeight = FontWeight.Bold,
                        color = overallColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = overallColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(20.dp))

            // Factor Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FertilityFactorItem(
                    label = StringResources.get(StringKeySaptamsa.FIFTH_HOUSE, language),
                    score = fertility.fifthHouseScore,
                    icon = Icons.Outlined.Home
                )
                FertilityFactorItem(
                    label = StringResources.get(StringKeySaptamsa.JUPITER, language),
                    score = fertility.jupiterScore,
                    icon = Icons.Outlined.Stars
                )
                FertilityFactorItem(
                    label = StringResources.get(StringKey.PLANET_MOON, language),
                    score = fertility.moonScore,
                    icon = Icons.Outlined.NightsStay
                )
            }
        }
    }
}

@Composable
private fun FertilityFactorItem(
    label: String,
    score: Double,
    icon: ImageVector
) {
    val scoreColor = when {
        score >= 0.7 -> AppTheme.SuccessColor
        score >= 0.4 -> AppTheme.AccentGold
        else -> AppTheme.WarningColor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = scoreColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
            color = AppTheme.TextMuted
        )
        Text(
            text = "${(score * 100).toInt()}%",
            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
            fontWeight = FontWeight.Bold,
            color = scoreColor
        )
    }
}

@Composable
private fun FavorablePeriodCard(
    period: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = AppTheme.SuccessColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = period,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextPrimary
            )
        }
    }
}

@Composable
private fun RecommendationsCard(
    recommendations: List<String>,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.RECOMMENDATIONS, language),
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            recommendations.forEach { rec ->
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AppTheme.AccentGold)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = rec,
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun YogasTabSS(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Santhana Yogas (Positive)
        item {
            SectionHeaderSS(
                title = StringResources.get(StringKeySaptamsa.SANTHANA_YOGAS, language),
                subtitle = StringResources.get(StringKeySaptamsa.SANTHANA_YOGAS_SUBTITLE, language),
                icon = Icons.Outlined.Stars
            )
        }

        if (analysis.santhanaYogas.isEmpty()) {
            item {
                EmptyStateSS(StringResources.get(StringKeySaptamsa.EMPTY_SANTHANA_YOGAS, language))
            }
        } else {
            items(analysis.santhanaYogas) { yoga ->
                SanthanaYogaCard(yoga, isPositive = true, language = language)
            }
        }

        // Challenging Yogas
        if (analysis.challengingYogas.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeaderSS(
                    title = StringResources.get(StringKeySaptamsa.CHALLENGING_YOGAS, language),
                    subtitle = StringResources.get(StringKeySaptamsa.CHALLENGING_YOGAS_SUBTITLE, language),
                    icon = Icons.Outlined.Warning
                )
            }

            items(analysis.challengingYogas) { challenge ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
                    Text(
                        text = StringResources.get(challenge, language),
                        modifier = Modifier.padding(16.dp),
                        fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun SanthanaYogaCard(
    yoga: SanthanaYoga,
    isPositive: Boolean,
    language: Language
) {
    val yogaColor = if (isPositive)
        AppTheme.SuccessColor
    else
        AppTheme.WarningColor

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius))
                            .background(yogaColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPositive)
                                Icons.Outlined.Stars
                            else
                                Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = yogaColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = StringResources.get(yoga.nameKey, language),
                            fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = StringResources.get(StringKeySaptamsa.YOGA_STRENGTH, language, (yoga.strength * 100).toInt()),
                            fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.ElementCornerRadius),
                    color = yogaColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isPositive) StringResources.get(StringKeySaptamsa.YOGA_POSITIVE, language) else StringResources.get(StringKeySaptamsa.YOGA_CAUTION, language),
                        fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                        fontWeight = FontWeight.SemiBold,
                        color = yogaColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = StringResources.get(yoga.effectKey, language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SectionHeaderSS(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = CinzelDecorativeFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S17,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = subtitle,
                    fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun EmptyStateSS(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(com.astro.vajra.ui.theme.NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = androidx.compose.foundation.BorderStroke(com.astro.vajra.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoadingStateSS(modifier: Modifier = Modifier) {
    val language = LocalLanguage.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = StringResources.get(StringKeySaptamsa.ANALYZING_SAPTAMSA, language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorStateSS(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Error,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun getPlanetColorSS(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> AppTheme.PlanetSun
        Planet.MOON -> AppTheme.PlanetMoon
        Planet.MARS -> AppTheme.PlanetMars
        Planet.MERCURY -> AppTheme.PlanetMercury
        Planet.JUPITER -> AppTheme.PlanetJupiter
        Planet.VENUS -> AppTheme.PlanetVenus
        Planet.SATURN -> AppTheme.PlanetSaturn
        Planet.RAHU -> AppTheme.PlanetRahu
        Planet.KETU -> AppTheme.PlanetKetu
        else -> AppTheme.AccentPrimary
    }
}

@Composable
private fun getStrengthColor(strength: Double): Color {
    return when {
        strength >= 0.7 -> AppTheme.SuccessColor
        strength >= 0.4 -> AppTheme.AccentGold
        else -> AppTheme.WarningColor
    }
}






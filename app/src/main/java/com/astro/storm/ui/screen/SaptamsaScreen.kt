package com.astro.storm.ui.screen

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
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.SaptamsaAnalysis
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.ChildCountFactors
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.ChildIndication
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.FertilityAnalysis
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.SanthanaYoga
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.D7LagnaAnalysis
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.FifthHouseAnalysis
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.JupiterAnalysis
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.DarkAppThemeColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.astro.storm.core.common.StringKeySaptamsa
import com.astro.storm.core.common.StringResources

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
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = StringResources.get(StringKeySaptamsa.TITLE, language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = StringResources.get(StringKeySaptamsa.SUBTITLE, language),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = StringResources.get(StringKeySaptamsa.BTN_BACK, language)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground,
                    titleContentColor = AppTheme.TextPrimary
                )
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

    Column(modifier = modifier.fillMaxSize()) {
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = AppTheme.CardBackground,
            contentColor = AppTheme.AccentPrimary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

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
            ChildCountSummaryCard(analysis.childCountEstimate)
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = StringResources.get(StringKeySaptamsa.RANGE, language, analysis.childCountEstimate.estimatedRange.first, analysis.childCountEstimate.estimatedRange.last) + ". " +
                                StringResources.get(StringKeySaptamsa.SUMMARY_FERTILITY_STATUS, language, StringResources.get(analysis.fertilityAnalysis.fertilityStatus.key, language)),
                        style = MaterialTheme.typography.bodyMedium,
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
        displayCount >= 3 -> DarkAppThemeColors.SuccessColor
        displayCount >= 1 -> DarkAppThemeColors.AccentGold
        else -> DarkAppThemeColors.WarningColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = countColor.copy(alpha = 0.1f)
        )
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
                    style = MaterialTheme.typography.titleMedium,
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
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = countColor
                    )
                    Text(
                        text = if (displayCount == 1) StringResources.get(StringKeySaptamsa.CHILD, language) else StringResources.get(StringKeySaptamsa.CHILDREN, language),
                        style = MaterialTheme.typography.labelSmall,
                        color = countColor.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Range indicator
            Text(
                text = StringResources.get(StringKeySaptamsa.RANGE, language, estimate.estimatedRange.first, estimate.estimatedRange.last),
                style = MaterialTheme.typography.labelMedium,
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
                    color = DarkAppThemeColors.AccentGold
                )
                StrengthIndicator(
                    label = StringResources.get(StringKeySaptamsa.JUPITER, language),
                    strength = estimate.jupiterStrength,
                    color = DarkAppThemeColors.PlanetJupiter
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
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun D7LagnaCard(
    analysis: SaptamsaAnalysis,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaSign.getLocalizedName(language).take(2),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.D7_LAGNA_TITLE, language),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaSign.getLocalizedName(language),
                        style = MaterialTheme.typography.titleLarge,
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
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaLord.getLocalizedName(language),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = getPlanetColorSS(analysis.d7LagnaAnalysis.lagnaLord)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.LORD_POSITION, language),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = (analysis.d7LagnaAnalysis.lagnaLordPosition?.sign ?: ZodiacSign.ARIES).getLocalizedName(language),
                        style = MaterialTheme.typography.bodyMedium,
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                    tint = DarkAppThemeColors.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.FIFTH_HOUSE_ANALYSIS, language),
                    style = MaterialTheme.typography.titleMedium,
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
                    color = DarkAppThemeColors.AccentGold
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
                    style = MaterialTheme.typography.labelMedium,
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
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
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
        shape = RoundedCornerShape(8.dp),
        color = planetColor.copy(alpha = 0.15f)
    ) {
        Text(
            text = planet.getLocalizedName(language),
            style = MaterialTheme.typography.labelMedium,
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
        SaptamsaAnalyzer.ChildGender.MALE -> DarkAppThemeColors.AccentPrimary
        SaptamsaAnalyzer.ChildGender.FEMALE -> DarkAppThemeColors.LifeAreaLove
        else -> DarkAppThemeColors.AccentTeal
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = genderColor
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = StringResources.get(StringKeySaptamsa.CHILD_NUMBER, language, childNumber),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = StringResources.get(indication.gender.key, language),
                            style = MaterialTheme.typography.bodySmall,
                            color = genderColor
                        )
                    }
                }

                // Strength Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getStrengthColor(indication.genderConfidence).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${(indication.genderConfidence * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
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
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = indication.indicatingPlanet.getLocalizedName(language),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = getPlanetColorSS(indication.indicatingPlanet)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = StringResources.get(StringKeySaptamsa.RELATIONSHIP, language),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = StringResources.get(indication.relationshipQuality.key, language),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = indication.characteristics.firstOrNull()?.let { StringResources.get(it, language) } ?: StringResources.get(StringKeySaptamsa.ERROR_NO_SPECIFIC_DESC, language),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            // Characteristics
            if (indication.characteristics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.CHARACTERISTICS_LABEL, language),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(indication.characteristics) { trait ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AppTheme.ChipBackground
                        ) {
                            Text(
                                text = StringResources.get(trait, language),
                                style = MaterialTheme.typography.labelSmall,
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
                        text = "• $timing",
                        style = MaterialTheme.typography.bodySmall,
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
                        text = "• ${StringResources.get(career, language)}",
                        style = MaterialTheme.typography.bodySmall,
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
                        text = "• ${StringResources.get(health, language)}",
                        style = MaterialTheme.typography.bodySmall,
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
                    style = MaterialTheme.typography.titleSmall,
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
        fertility.overallScore >= 0.7 -> DarkAppThemeColors.SuccessColor
        fertility.overallScore >= 0.4 -> DarkAppThemeColors.AccentGold
        else -> DarkAppThemeColors.WarningColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = overallColor.copy(alpha = 0.1f)
        )
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
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = StringResources.get(fertility.fertilityStatus.key, language),
                        style = MaterialTheme.typography.headlineSmall,
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
                        style = MaterialTheme.typography.titleLarge,
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
        score >= 0.7 -> DarkAppThemeColors.SuccessColor
        score >= 0.4 -> DarkAppThemeColors.AccentGold
        else -> DarkAppThemeColors.WarningColor
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
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
        Text(
            text = "${(score * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = scoreColor
        )
    }
}

@Composable
private fun FavorablePeriodCard(
    period: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                tint = DarkAppThemeColors.SuccessColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = period,
                style = MaterialTheme.typography.bodyMedium,
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                    tint = DarkAppThemeColors.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = StringResources.get(StringKeySaptamsa.RECOMMENDATIONS, language),
                    style = MaterialTheme.typography.titleMedium,
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
                            .background(DarkAppThemeColors.AccentGold)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = rec,
                        style = MaterialTheme.typography.bodyMedium,
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
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
                ) {
                    Text(
                        text = StringResources.get(challenge, language),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
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
        DarkAppThemeColors.SuccessColor
    else
        DarkAppThemeColors.WarningColor

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                            .clip(RoundedCornerShape(10.dp))
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
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = StringResources.get(StringKeySaptamsa.YOGA_STRENGTH, language, (yoga.strength * 100).toInt()),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = yogaColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isPositive) StringResources.get(StringKeySaptamsa.YOGA_POSITIVE, language) else StringResources.get(StringKeySaptamsa.YOGA_CAUTION, language),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = yogaColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = StringResources.get(yoga.effectKey, language),
                style = MaterialTheme.typography.bodySmall,
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.AccentPrimary.copy(alpha = 0.1f)
        )
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun EmptyStateSS(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
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
                style = MaterialTheme.typography.bodyMedium,
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
                style = MaterialTheme.typography.bodyMedium,
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
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getPlanetColorSS(planet: Planet): Color {
    return when (planet) {
        Planet.SUN -> DarkAppThemeColors.PlanetSun
        Planet.MOON -> DarkAppThemeColors.PlanetMoon
        Planet.MARS -> DarkAppThemeColors.PlanetMars
        Planet.MERCURY -> DarkAppThemeColors.PlanetMercury
        Planet.JUPITER -> DarkAppThemeColors.PlanetJupiter
        Planet.VENUS -> DarkAppThemeColors.PlanetVenus
        Planet.SATURN -> DarkAppThemeColors.PlanetSaturn
        Planet.RAHU -> DarkAppThemeColors.PlanetRahu
        Planet.KETU -> DarkAppThemeColors.PlanetKetu
        else -> DarkAppThemeColors.AccentPrimary
    }
}

private fun getStrengthColor(strength: Double): Color {
    return when {
        strength >= 0.7 -> DarkAppThemeColors.SuccessColor
        strength >= 0.4 -> DarkAppThemeColors.AccentGold
        else -> DarkAppThemeColors.WarningColor
    }
}

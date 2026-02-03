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
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer
import com.astro.storm.ephemeris.varga.SaptamsaAnalyzer.*
import com.astro.storm.ephemeris.varga.SaptamsaAnalysis
import com.astro.storm.ephemeris.varga.ChildCountFactors
import com.astro.storm.ephemeris.varga.ChildIndication
import com.astro.storm.ephemeris.varga.FertilityAnalysis
import com.astro.storm.ephemeris.varga.SanthanaYoga
import com.astro.storm.ephemeris.varga.D7LagnaAnalysis
import com.astro.storm.ephemeris.varga.FifthHouseAnalysis
import com.astro.storm.ephemeris.varga.JupiterAnalysis
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.DarkAppThemeColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    LaunchedEffect(chart) {
        if (chart == null) {
            isLoading = false
            errorMessage = "No chart available"
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        withContext(Dispatchers.Default) {
            try {
                analysis = SaptamsaAnalyzer.analyzeSaptamsa(chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error analyzing Saptamsa"
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
                            text = "Saptamsa (D7)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Children & Progeny Analysis",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
    val tabs = listOf("Overview", "Children", "Fertility", "Yogas")

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
                            text = "Interpretation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = analysis.interpretation,
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
    estimate: ChildCountFactors
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
                    text = "Estimated Children",
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
                        text = if (displayCount == 1) "child" else "children",
                        style = MaterialTheme.typography.labelSmall,
                        color = countColor.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Range indicator
            Text(
                text = "Range: ${estimate.minimumCount} - ${estimate.maximumCount}",
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
                    label = "Fifth House",
                    strength = estimate.fifthHouseStrength,
                    color = DarkAppThemeColors.AccentGold
                )
                StrengthIndicator(
                    label = "Jupiter",
                    strength = estimate.jupiterStrength,
                    color = DarkAppThemeColors.PlanetJupiter
                )
                StrengthIndicator(
                    label = "D7 Lagna",
                    strength = estimate.d7LagnaStrength,
                    color = AppTheme.AccentPrimary
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
                        text = "D7 Lagna (Saptamsa Ascendant)",
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
                        text = "Lagna Lord",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = analysis.d7LagnaAnalysis.lagnaLord.getLocalizedName(language),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Lord Position",
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
                    text = "Fifth House Analysis",
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
                    label = "Sign",
                    value = analysis.fifthHouseAnalysis.fifthSign.getLocalizedName(language),
                    color = DarkAppThemeColors.AccentGold
                )
                InfoItem(
                    label = "Lord",
                    value = analysis.fifthHouseAnalysis.fifthLord.getLocalizedName(language),
                    color = getPlanetColorSS(analysis.fifthHouseAnalysis.fifthLord)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Planets in Fifth House
            if (analysis.fifthHouseAnalysis.planetsInFifth.isNotEmpty()) {
                Text(
                    text = "Planets in Fifth House",
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
                title = "Individual Child Indications",
                subtitle = "Detailed analysis of each child's potential characteristics",
                icon = Icons.Outlined.ChildCare
            )
        }

        if (analysis.childIndications.isEmpty()) {
            item {
                EmptyStateSS("No specific child indications available")
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
    val genderColor = when (indication.gender.displayName) {
        "Male" -> DarkAppThemeColors.AccentPrimary
        "Female" -> DarkAppThemeColors.LifeAreaLove
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
                            text = "Child #$childNumber",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = indication.gender.displayName,
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
                        text = "Significator",
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
                        text = "Relationship",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = indication.relationshipQuality.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = indication.characteristics.firstOrNull() ?: "No specific description",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            // Characteristics
            if (indication.characteristics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Characteristics:",
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
                                text = trait,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
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
                title = "Fertility Analysis",
                subtitle = "Assessment of conception and childbirth prospects",
                icon = Icons.Outlined.FavoriteBorder
            )
        }

        // Fertility Overview Card
        item {
            FertilityOverviewCard(analysis.fertilityAnalysis)
        }

        // Favorable Periods
        if (analysis.fertilityAnalysis.favorablePeriods.isNotEmpty()) {
            item {
                Text(
                    text = "Favorable Periods",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            items(analysis.fertilityAnalysis.favorablePeriods) { period ->
                FavorablePeriodCard(period)
            }
        }

        // Recommendations
        if (analysis.fertilityAnalysis.recommendations.isNotEmpty()) {
            item {
                RecommendationsCard(analysis.fertilityAnalysis.recommendations)
            }
        }
    }
}

@Composable
private fun FertilityOverviewCard(
    fertility: FertilityAnalysis
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
                        text = "Overall Fertility Score",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = fertility.fertilityStatus.displayName,
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
                    label = "5th House",
                    score = fertility.fifthHouseScore,
                    icon = Icons.Outlined.Home
                )
                FertilityFactorItem(
                    label = "Jupiter",
                    score = fertility.jupiterScore,
                    icon = Icons.Outlined.Stars
                )
                FertilityFactorItem(
                    label = "Moon",
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
    recommendations: List<String>
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
                    text = "Recommendations",
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
                title = "Santhana Yogas",
                subtitle = "Positive combinations for children",
                icon = Icons.Outlined.Stars
            )
        }

        if (analysis.santhanaYogas.isEmpty()) {
            item {
                EmptyStateSS("No Santhana Yogas detected")
            }
        } else {
            items(analysis.santhanaYogas) { yoga ->
                SanthanaYogaCard(yoga, isPositive = true)
            }
        }

        // Challenging Yogas
        if (analysis.challengingYogas.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeaderSS(
                    title = "Challenging Yogas",
                    subtitle = "Factors requiring attention",
                    icon = Icons.Outlined.Warning
                )
            }

            items(analysis.challengingYogas) { yoga ->
                SanthanaYogaCard(yoga, isPositive = false)
            }
        }
    }
}

@Composable
private fun SanthanaYogaCard(
    yoga: SanthanaYoga,
    isPositive: Boolean
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
                            text = yoga.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "Strength: ${(yoga.strength * 100).toInt()}%",
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
                        text = if (isPositive) "Positive" else "Caution",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = yogaColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = yoga.description,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )

            if (yoga.effects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Effects:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextMuted
                )
                yoga.effects.forEach { effect ->
                    Text(
                        text = "• $effect",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }

            if (yoga.remedies.isNotEmpty() && !isPositive) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Remedies:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkAppThemeColors.AccentGold
                )
                yoga.remedies.forEach { remedy ->
                    Text(
                        text = "• $remedy",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkAppThemeColors.AccentGold.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
            }
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
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Analyzing Saptamsa (D7)...",
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

package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.core.model.ZodiacSign
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator.JaiminiKarakaAnalysis
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator.KarakaType
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator.KarakaAssignment
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator.KarakamshaAnalysis
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator.SwamshaAnalysis
import com.astro.storm.ephemeris.jaimini.JaiminiKarakaCalculator.KarakenshiYoga
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.DarkAppThemeColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * JaiminiKarakaScreen - Comprehensive Chara Karaka Analysis Screen
 *
 * Displays 7 Chara Karakas, Karakamsha, Swamsha, and Karakenshi Yogas
 * based on Jaimini Astrology principles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JaiminiKarakaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var analysis by remember { mutableStateOf<JaiminiKarakaAnalysis?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Calculate Jaimini Karaka Analysis
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
                analysis = JaiminiKarakaCalculator.calculateKarakas(chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error calculating Jaimini Karakas"
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
                            text = "Jaimini Karakas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Chara Karaka Analysis",
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
            isLoading -> LoadingState(modifier = Modifier.padding(padding))
            errorMessage != null -> ErrorState(errorMessage!!, modifier = Modifier.padding(padding))
            analysis != null -> JaiminiKarakaContent(
                analysis = analysis!!,
                language = language,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun JaiminiKarakaContent(
    analysis: JaiminiKarakaAnalysis,
    language: Language,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Karakas", "Karakamsha", "Yogas", "Interpretation")

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
            0 -> KarakasTab(analysis, language)
            1 -> KarakamshaTab(analysis, language)
            2 -> YogasTab(analysis, language)
            3 -> InterpretationTab(analysis, language)
        }
    }
}

@Composable
private fun KarakasTab(
    analysis: JaiminiKarakaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header Card
        item {
            HeaderCard(
                title = "7 Chara Karakas",
                subtitle = "Variable significators based on planetary degrees",
                icon = Icons.Outlined.Stars
            )
        }

        // Karaka Cards
        items(analysis.karakas.entries.toList()) { (type, assignment) ->
            KarakaCard(
                karakaType = type,
                assignment = assignment,
                language = language
            )
        }

        // System Info
        item {
            InfoCard(
                title = "Karaka System",
                content = "Using ${analysis.karakaSystem.name.replace("_", " ")} system",
                icon = Icons.Outlined.Info
            )
        }
    }
}

@Composable
private fun KarakaCard(
    karakaType: KarakaType,
    assignment: KarakaAssignment,
    language: Language
) {
    val planetColor = getPlanetColor(assignment.planet)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Planet Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                planetColor.copy(alpha = 0.3f),
                                planetColor.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = assignment.planet.getLocalizedName(language).take(2),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = planetColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Karaka Type Name
                Text(
                    text = karakaType.name.replace("_", " "),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )

                // Planet & Sign
                Text(
                    text = "${assignment.planet.getLocalizedName(language)} in ${assignment.sign.getLocalizedName(language)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )

                // Degree
                Text(
                    text = "Degree: ${String.format("%.2f", assignment.degreeInSign)}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )

                // Signification
                Text(
                    text = karakaType.primarySignification,
                    style = MaterialTheme.typography.bodySmall,
                    color = planetColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Rank Badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(planetColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${assignment.karakaType.rank}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = planetColor
                )
            }
        }
    }
}

@Composable
private fun KarakamshaTab(
    analysis: JaiminiKarakaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Karakamsha Card
        analysis.karakamsha?.let { karakamsha ->
            item {
                HeaderCard(
                    title = "Karakamsha",
                    subtitle = "Atmakaraka's position in Navamsa determines soul's direction",
                    icon = Icons.Outlined.Explore
                )
            }

            item {
                KarakamshaDetailCard(karakamsha, language)
            }
        }

        // Swamsha Card
        analysis.swamsha?.let { swamsha ->
            item {
                HeaderCard(
                    title = "Swamsha",
                    subtitle = "Navamsa Lagna - the spiritual manifestation point",
                    icon = Icons.Outlined.SelfImprovement
                )
            }

            item {
                SwamshaDetailCard(swamsha, language)
            }
        }
    }
}

@Composable
private fun KarakamshaDetailCard(
    karakamsha: KarakamshaAnalysis,
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
            // Sign Info
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
                        text = karakamsha.karakamshaSign.getLocalizedName(language).take(2),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Karakamsha Sign",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = karakamsha.karakamshaSign.getLocalizedName(language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            // Indicators
            Text(
                text = "Life Path Indicators",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Career Indicators
            if (karakamsha.careerIndicators.isNotEmpty()) {
                IndicatorSection(
                    title = "Career",
                    indicators = karakamsha.careerIndicators,
                    icon = Icons.Outlined.Work,
                    color = DarkAppThemeColors.LifeAreaCareer
                )
            }

            // Spiritual Indicators
            if (karakamsha.spiritualIndicators.isNotEmpty()) {
                IndicatorSection(
                    title = "Spiritual",
                    indicators = karakamsha.spiritualIndicators,
                    icon = Icons.Outlined.SelfImprovement,
                    color = DarkAppThemeColors.LifeAreaSpiritual
                )
            }

            // Relationship Indicators
            if (karakamsha.relationshipIndicators.isNotEmpty()) {
                IndicatorSection(
                    title = "Relationships",
                    indicators = karakamsha.relationshipIndicators,
                    icon = Icons.Outlined.Favorite,
                    color = DarkAppThemeColors.LifeAreaLove
                )
            }
        }
    }
}

@Composable
private fun SwamshaDetailCard(
    swamsha: SwamshaAnalysis,
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
                        .background(DarkAppThemeColors.LifeAreaSpiritual.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = swamsha.swamshaSign.getLocalizedName(language).take(2),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkAppThemeColors.LifeAreaSpiritual
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Swamsha (Navamsa Lagna)",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = swamsha.swamshaSign.getLocalizedName(language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interpretation
            Text(
                text = swamsha.interpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun IndicatorSection(
    title: String,
    indicators: List<String>,
    icon: ImageVector,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        indicators.forEach { indicator ->
            Row(
                modifier = Modifier.padding(start = 26.dp, bottom = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = indicator,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun YogasTab(
    analysis: JaiminiKarakaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeaderCard(
                title = "Karakenshi Yogas",
                subtitle = "Special combinations formed by Chara Karakas",
                icon = Icons.Outlined.AutoAwesome
            )
        }

        if (analysis.karakenshiYogas.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
                            text = "No special Karakenshi Yogas detected",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(analysis.karakenshiYogas) { yoga ->
                KarakenshiYogaCard(yoga, language)
            }
        }
    }
}

@Composable
private fun KarakenshiYogaCard(
    yoga: KarakenshiYoga,
    language: Language
) {
    val yogaColor = when {
        yoga.isAuspicious -> DarkAppThemeColors.SuccessColor
        else -> DarkAppThemeColors.WarningColor
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(yogaColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Stars,
                            contentDescription = null,
                            tint = yogaColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = yoga.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = "Strength: ${String.format("%.0f", yoga.strength * 100)}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                // Benefic/Malefic Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = yogaColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (yoga.isAuspicious) "Benefic" else "Challenging",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = yogaColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = yoga.effects,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Involved Planets
            Row {
                Text(
                    text = "Planets: ",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = yoga.involvedPlanets.joinToString(", ") { it.getLocalizedName(language) },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextSecondary
                )
            }

            // Results
            if (yoga.effects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Results:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = "• ${yoga.effects}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun InterpretationTab(
    analysis: JaiminiKarakaAnalysis,
    language: Language
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeaderCard(
                title = "Complete Interpretation",
                subtitle = "Comprehensive Jaimini analysis summary",
                icon = Icons.Outlined.Description
            )
        }

        // Full Interpretation
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
                    Text(
                        text = analysis.keyInsights.joinToString("\n\n"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        // Atmakaraka Analysis (Soul's Direction)
        analysis.getAtmakaraka()?.let { ak ->
            item {
                AtmakarakaAnalysisCard(ak, language)
            }
        }

        // Gemstone Recommendations
        if (analysis.recommendations.isNotEmpty()) {
            item {
                GemstoneRecommendationsCard(analysis.recommendations)
            }
        }
    }
}

@Composable
private fun AtmakarakaAnalysisCard(
    ak: KarakaAssignment,
    language: Language
) {
    val planetColor = getPlanetColor(ak.planet)

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
            Text(
                text = "🌟 Atmakaraka Analysis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(planetColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ak.planet.getLocalizedName(language).take(2),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = planetColor
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Your Soul Planet (Atmakaraka)",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = ak.planet.getLocalizedName(language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = planetColor
                    )
                    Text(
                        text = "in ${ak.sign.getLocalizedName(language)} at ${String.format("%.2f", ak.degreeInSign)}°",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "The Atmakaraka is the planet with the highest degree in your chart. It represents your soul's deepest desires and the lessons you need to learn in this lifetime.",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun GemstoneRecommendationsCard(
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
                    imageVector = Icons.Outlined.Diamond,
                    contentDescription = null,
                    tint = DarkAppThemeColors.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Gemstone Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            recommendations.forEach { recommendation ->
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
                        text = recommendation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
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
private fun InfoCard(
    title: String,
    content: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.ChipBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Calculating Jaimini Karakas...",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun ErrorState(message: String, modifier: Modifier = Modifier) {
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

private fun getPlanetColor(planet: Planet): Color {
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

package com.astro.vajra.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.vajra.ui.components.common.ModernPillTabRow
import com.astro.vajra.ui.components.common.NeoVedicPageHeader
import com.astro.vajra.ui.components.common.TabItem
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
import com.astro.vajra.core.common.Language
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyJaimini
import com.astro.vajra.core.common.StringResources
import com.astro.vajra.core.common.getLocalizedName
import com.astro.vajra.core.model.Planet
import com.astro.vajra.core.model.VedicChart
import com.astro.vajra.core.model.ZodiacSign
import com.astro.vajra.data.localization.LocalLanguage
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.JaiminiKarakaAnalysis
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.KarakaType
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.KarakaAssignment
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.KarakamshaAnalysis
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.SwamshaAnalysis
import com.astro.vajra.ephemeris.jaimini.JaiminiKarakaCalculator.KarakenshiYoga
import com.astro.vajra.ui.theme.AppTheme
import com.astro.vajra.ui.theme.NeoVedicTokens
import com.astro.vajra.ui.theme.CinzelDecorativeFamily
import com.astro.vajra.ui.theme.SpaceGroteskFamily
import com.astro.vajra.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.BorderStroke

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
    LaunchedEffect(chart, language) {
        if (chart == null) {
            isLoading = false
            errorMessage = StringResources.get(StringKeyJaimini.NO_CHART_AVAILABLE, language)
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = null

        withContext(Dispatchers.Default) {
            try {
                analysis = JaiminiKarakaCalculator.calculateKarakas(chart)
            } catch (e: Exception) {
                errorMessage = e.message ?: StringResources.get(StringKeyJaimini.ERROR_JAIMINI, language)
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            NeoVedicPageHeader(
                title = StringResources.get(StringKeyJaimini.TITLE, language),
                subtitle = StringResources.get(StringKeyJaimini.SUBTITLE, language),
                onBack = onBack
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        when {
            isLoading -> LoadingStateSS(modifier = Modifier.padding(padding))
            errorMessage != null -> ErrorStateSS(errorMessage!!, modifier = Modifier.padding(padding))
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
    val tabs = listOf(
        StringResources.get(StringKeyJaimini.TAB_KARAKAS, language),
        StringResources.get(StringKeyJaimini.TAB_KARAKAMSHA, language),
        StringResources.get(StringKeyJaimini.TAB_YOGAS, language),
        StringResources.get(StringKeyJaimini.TAB_INTERPRETATION, language)
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
            HeaderCardSS(
                title = StringResources.get(StringKeyJaimini.HEADER_7_KARAKAS, language),
                subtitle = StringResources.get(StringKeyJaimini.SUBTITLE_7_KARAKAS, language),
                icon = Icons.Outlined.Stars
            )
        }

        // Karaka Cards
        items(analysis.karakas.entries.toList()) { (type, assignment) ->
            KarakaCardSS(
                karakaType = type,
                assignment = assignment,
                language = language
            )
        }

        // System Info
        item {
            InfoCardSS(
                title = StringResources.get(StringKeyJaimini.SYSTEM_INFO_TITLE, language),
                content = StringResources.get(StringKeyJaimini.SYSTEM_INFO_CONTENT, language, analysis.karakaSystem.name.replace("_", " ")),
                icon = Icons.Outlined.Info
            )
        }
    }
}

@Composable
private fun KarakaCardSS(
    karakaType: KarakaType,
    assignment: KarakaAssignment,
    language: Language
) {
    val planetColor = getPlanetColorSS(assignment.planet)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                                planetColor.copy(alpha = 0.25f),
                                planetColor.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .border(1.dp, planetColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = assignment.planet.symbol,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                    fontWeight = FontWeight.Bold,
                    color = planetColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Karaka Type Name
                Text(
                    text = karakaType.name.replace("_", " "),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.TextPrimary
                )

                // Planet & Sign
                Text(
                    text = "${assignment.planet.getLocalizedName(language)} in ${assignment.sign.getLocalizedName(language)}",
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                    color = AppTheme.TextSecondary
                )

                // Degree
                Text(
                    text = StringResources.get(StringKeyJaimini.DEGREE_LABEL, language, String.format("%.2f", assignment.degreeInSign)),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                    color = AppTheme.TextMuted
                )

                // Signification
                Text(
                    text = karakaType.primarySignification,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                    color = planetColor,
                    modifier = Modifier.padding(top = 4.dp),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            // Rank Badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(planetColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${assignment.karakaType.rank}",
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
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
                HeaderCardSS(
                    title = StringResources.get(StringKeyJaimini.KARAKAMSHA_TITLE, language),
                    subtitle = StringResources.get(StringKeyJaimini.KARAKAMSHA_SUBTITLE, language),
                    icon = Icons.Outlined.Explore
                )
            }

            item {
                KarakamshaDetailCardSS(karakamsha, language)
            }
        }

        // Swamsha Card
        analysis.swamsha?.let { swamsha ->
            item {
                HeaderCardSS(
                    title = StringResources.get(StringKeyJaimini.SWAMSHA_TITLE, language),
                    subtitle = StringResources.get(StringKeyJaimini.SWAMSHA_SUBTITLE, language),
                    icon = Icons.Outlined.SelfImprovement
                )
            }

            item {
                SwamshaDetailCardSS(swamsha, language)
            }
        }
    }
}

@Composable
private fun KarakamshaDetailCardSS(
    karakamsha: KarakamshaAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                        .size(56.dp)
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = karakamsha.karakamshaSign.getLocalizedName(language).take(2),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = StringResources.get(StringKeyJaimini.SIGN_LABEL, language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = karakamsha.karakamshaSign.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
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
                text = StringResources.get(StringKeyJaimini.LIFE_PATH_INDICATORS, language).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Career Indicators
            if (karakamsha.careerIndicators.isNotEmpty()) {
                IndicatorSectionSS(
                    title = StringResources.get(StringKeyJaimini.INDICATOR_CAREER, language),
                    indicators = karakamsha.careerIndicators,
                    icon = Icons.Outlined.Work,
                    color = AppTheme.LifeAreaCareer
                )
            }

            // Spiritual Indicators
            if (karakamsha.spiritualIndicators.isNotEmpty()) {
                IndicatorSectionSS(
                    title = StringResources.get(StringKeyJaimini.INDICATOR_SPIRITUAL, language),
                    indicators = karakamsha.spiritualIndicators,
                    icon = Icons.Outlined.SelfImprovement,
                    color = AppTheme.LifeAreaSpiritual
                )
            }

            // Relationship Indicators
            if (karakamsha.relationshipIndicators.isNotEmpty()) {
                IndicatorSectionSS(
                    title = StringResources.get(StringKeyJaimini.INDICATOR_RELATIONSHIPS, language),
                    indicators = karakamsha.relationshipIndicators,
                    icon = Icons.Outlined.Favorite,
                    color = AppTheme.LifeAreaLove
                )
            }
        }
    }
}

@Composable
private fun SwamshaDetailCardSS(
    swamsha: SwamshaAnalysis,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                        .size(56.dp)
                        .clip(RoundedCornerShape(NeoVedicTokens.ElementCornerRadius))
                        .background(AppTheme.LifeAreaSpiritual.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = swamsha.swamshaSign.getLocalizedName(language).take(2),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.LifeAreaSpiritual
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = StringResources.get(StringKeyJaimini.SWAMSHA_LAGNA_LABEL, language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = swamsha.swamshaSign.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S18,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interpretation
            Text(
                text = swamsha.interpretation,
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun IndicatorSectionSS(
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
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
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
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary,
                    lineHeight = 18.sp
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
            HeaderCardSS(
                title = StringResources.get(StringKeyJaimini.YOGAS_TITLE, language),
                subtitle = StringResources.get(StringKeyJaimini.YOGAS_SUBTITLE, language),
                icon = Icons.Outlined.AutoAwesome
            )
        }

        if (analysis.karakenshiYogas.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                    color = AppTheme.CardBackground,
                    border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = AppTheme.TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = StringResources.get(StringKeyJaimini.NO_YOGAS_FOUND, language),
                            fontFamily = PoppinsFontFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                            color = AppTheme.TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(analysis.karakenshiYogas) { yoga ->
                KarakenshiYogaCardSS(yoga, language)
            }
        }
    }
}

@Composable
private fun KarakenshiYogaCardSS(
    yoga: KarakenshiYoga,
    language: Language
) {
    val yogaColor = when {
        yoga.isAuspicious -> AppTheme.SuccessColor
        else -> AppTheme.WarningColor
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                            .size(44.dp)
                            .clip(CircleShape)
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
                            fontFamily = CinzelDecorativeFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = StringResources.get(StringKeyJaimini.STRENGTH_LABEL, language, String.format("%.0f", yoga.strength * 100)),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                // Benefic/Malefic Badge
                Surface(
                    shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
                    color = yogaColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (yoga.isAuspicious) StringResources.get(StringKeyJaimini.YOGA_BENEFIC, language) else StringResources.get(StringKeyJaimini.YOGA_CHALLENGING, language),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
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
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Involved Planets
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = StringResources.get(StringKeyJaimini.PLANETS_LABEL, language).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S10,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = yoga.involvedPlanets.joinToString(", ") { it.getLocalizedName(language) },
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCardSS(
                title = StringResources.get(StringKeyJaimini.INTERP_TITLE, language),
                subtitle = StringResources.get(StringKeyJaimini.INTERP_SUBTITLE, language),
                icon = Icons.Outlined.Description
            )
        }

        // Full Interpretation
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
                color = AppTheme.CardBackground,
                border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    analysis.keyInsights.forEach { insight ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                            Text("\u2022", color = AppTheme.AccentGold, modifier = Modifier.padding(end = 12.dp))
                            Text(
                                text = insight,
                                fontFamily = PoppinsFontFamily,
                                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S14,
                                color = AppTheme.TextSecondary,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }
        }

        // Atmakaraka Analysis (Soul's Direction)
        analysis.getAtmakaraka()?.let { ak ->
            item {
                AtmakarakaAnalysisCardSS(ak, language)
            }
        }

        // Gemstone Recommendations
        if (analysis.recommendations.isNotEmpty()) {
            item {
                GemstoneRecommendationsCardSS(analysis.recommendations, language)
            }
        }
    }
}

@Composable
private fun AtmakarakaAnalysisCardSS(
    ak: KarakaAssignment,
    language: Language
) {
    val planetColor = getPlanetColorSS(ak.planet)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = StringResources.get(StringKeyJaimini.ATMAKARAKA_ANALYSIS_TITLE, language).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(planetColor.copy(alpha = 0.2f), planetColor.copy(alpha = 0.05f))
                            )
                        )
                        .border(1.dp, planetColor.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ak.planet.symbol,
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S24,
                        fontWeight = FontWeight.Bold,
                        color = planetColor
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = StringResources.get(StringKeyJaimini.AK_SOUL_PLANET_LABEL, language),
                        fontFamily = PoppinsFontFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = ak.planet.getLocalizedName(language),
                        fontFamily = CinzelDecorativeFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S20,
                        fontWeight = FontWeight.Bold,
                        color = planetColor
                    )
                    Text(
                        text = "in ${ak.sign.getLocalizedName(language)} at ${String.format("%.2f", ak.degreeInSign)}\u00B0",
                        fontFamily = SpaceGroteskFamily,
                        fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = StringResources.get(StringKeyJaimini.AK_DESC, language),
                fontFamily = PoppinsFontFamily,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun GemstoneRecommendationsCardSS(
    recommendations: List<String>,
    language: Language
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.CardBackground,
        border = BorderStroke(NeoVedicTokens.BorderWidth, AppTheme.BorderColor)
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
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = StringResources.get(StringKeyJaimini.GEMSTONE_REC_TITLE, language),
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S16,
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
                    Icon(Icons.Filled.Diamond, null, tint = AppTheme.AccentGold, modifier = Modifier.size(14.dp).padding(top = 4.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = recommendation,
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
private fun HeaderCardSS(
    title: String,
    subtitle: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.CardCornerRadius),
        color = AppTheme.AccentPrimary.copy(alpha = 0.08f),
        border = BorderStroke(NeoVedicTokens.ThinBorderWidth, AppTheme.AccentPrimary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
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
                    color = AppTheme.TextMuted
                )
            }
        }
    }
}

@Composable
private fun InfoCardSS(
    title: String,
    content: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NeoVedicTokens.ElementCornerRadius),
        color = AppTheme.ChipBackground
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
                    fontFamily = SpaceGroteskFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = content,
                    fontFamily = PoppinsFontFamily,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                    color = AppTheme.TextSecondary
                )
            }
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
                text = StringResources.get(StringKeyJaimini.LOADING_JAIMINI, language),
                fontFamily = PoppinsFontFamily,
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

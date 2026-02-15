package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.astro.storm.ui.components.common.NeoVedicPageHeader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.core.common.StringKeyDeepHealth
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.nativeanalysis.*
import com.astro.storm.ephemeris.nativeanalysis.NativeAnalysisCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.NativeAnalysisUiState
import com.astro.storm.ui.viewmodel.NativeAnalysisViewModel
import com.astro.storm.ui.viewmodel.NativeSection
import androidx.compose.ui.text.style.TextOverflow

/**
 * Native Analysis Screen - Comprehensive personality and life profile
 *
 * Displays detailed analysis including:
 * - Character & Personality based on Ascendant, Moon, Sun
 * - Career prospects based on 10th house analysis
 * - Marriage & Relationships from 7th house
 * - Health & Longevity indicators
 * - Wealth & Finance potential
 * - Education & Knowledge
 * - Spiritual Path
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativeAnalysisScreen(
    chart: VedicChart?,
    onBack: () -> Unit,
    viewModel: NativeAnalysisViewModel = hiltViewModel()
) {
    val language = LocalLanguage.current
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        NativeSection.OVERVIEW,
        NativeSection.CHARACTER,
        NativeSection.CAREER,
        NativeSection.MARRIAGE,
        NativeSection.HEALTH,
        NativeSection.WEALTH,
        NativeSection.EDUCATION,
        NativeSection.SPIRITUAL
    )

    LaunchedEffect(chart) {
        chart?.let { viewModel.calculateNativeAnalysis(it) }
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            NeoVedicPageHeader(
                title = stringResource(StringKeyNative.NATIVE_ANALYSIS_TITLE),
                subtitle = stringResource(StringKeyNative.NATIVE_ANALYSIS_SUBTITLE),
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(StringKey.A11Y_SHOW_INFO),
                            tint = AppTheme.TextSecondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is NativeAnalysisUiState.Loading -> NativeLoadingContent(
                modifier = Modifier.padding(paddingValues)
            )
            is NativeAnalysisUiState.Error -> NativeErrorContent(
                message = state.message,
                modifier = Modifier.padding(paddingValues)
            )
            is NativeAnalysisUiState.Idle -> NativeEmptyContent(
                modifier = Modifier.padding(paddingValues)
            )
            is NativeAnalysisUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(AppTheme.ScreenBackground),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        NativeTabSelector(
                            tabs = tabs,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            language = language
                        )
                    }
                    when (tabs[selectedTab]) {
                        NativeSection.OVERVIEW -> item { NativeOverviewSection(state.result, language) }
                        NativeSection.CHARACTER -> item { NativeCharacterSection(state.result.characterAnalysis, language) }
                        NativeSection.CAREER -> item { NativeCareerSection(state.result.careerAnalysis, language) }
                        NativeSection.MARRIAGE -> item { NativeMarriageSection(state.result.marriageAnalysis, language) }
                        NativeSection.HEALTH -> item { NativeHealthSection(state.result.healthAnalysis, language) }
                        NativeSection.WEALTH -> item { NativeWealthSection(state.result.wealthAnalysis, language) }
                        NativeSection.EDUCATION -> item { NativeEducationSection(state.result.educationAnalysis, language) }
                        NativeSection.SPIRITUAL -> item { NativeSpiritualSection(state.result.spiritualAnalysis, language) }
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        NativeInfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun NativeTabSelector(
    tabs: List<NativeSection>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    language: Language
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs.size) { index ->
            val section = tabs[index]
            val displayName = if (language == Language.NEPALI) section.displayNameNe else section.displayName
            com.astro.storm.ui.components.common.NeoVedicChoicePill(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = displayName,
                        fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S13,
                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.CardBackground,
                    labelColor = AppTheme.TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = AppTheme.BorderColor,
                    selectedBorderColor = AppTheme.AccentPrimary.copy(alpha = 0.3f),
                    enabled = true,
                    selected = selectedTab == index
                )
            )
        }
    }
}

// -----------------------------------------------------------------------------
// OVERVIEW SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeOverviewSection(result: NativeAnalysisResult, language: Language) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OverallScoreCard(result.overallScore)

        KeySignsCard(result.characterAnalysis, language)

        KeyStrengthsCard(result.keyStrengths, language)

        KeyChallengesCard(result.keyChallenges, language)

        QuickInsightsRow(result, language)
    }
}

@Composable
private fun OverallScoreCard(score: Double) {
    val scoreColor = when {
        score >= 80 -> AppTheme.SuccessColor
        score >= 60 -> AppTheme.AccentGold
        score >= 40 -> AppTheme.WarningColor
        else -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = scoreColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(scoreColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${score.toInt()}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(StringKeyNative.LABEL_OVERVIEW),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Text(
                text = getScoreDescription(score),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun getScoreDescription(score: Double): String {
    return when {
        score >= 80 -> stringResource(StringKeyNative.STRENGTH_EXCELLENT)
        score >= 60 -> stringResource(StringKeyNative.STRENGTH_STRONG)
        score >= 40 -> stringResource(StringKeyNative.STRENGTH_MODERATE)
        else -> stringResource(StringKeyNative.STRENGTH_WEAK)
    }
}

@Composable
private fun KeySignsCard(character: CharacterAnalysis, language: Language) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyNative.LABEL_PLANETARY_INFLUENCE),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SignBadge(
                    label = stringResource(StringKeyAnalysis.CHART_LAGNA),
                    sign = character.ascendantSign.displayName,
                    color = AppTheme.AccentPrimary
                )
                SignBadge(
                    label = Planet.MOON.getLocalizedName(language),
                    sign = character.moonSign.displayName,
                    color = AppTheme.AccentTeal
                )
                SignBadge(
                    label = Planet.SUN.getLocalizedName(language),
                    sign = character.sunSign.displayName,
                    color = AppTheme.AccentGold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.PANCHANGA_ELEMENT),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = if (language == Language.NEPALI)
                            character.dominantElement.displayNameNe
                        else character.dominantElement.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.DIALOG_MOTION),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = if (language == Language.NEPALI)
                            character.dominantModality.displayNameNe
                        else character.dominantModality.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun SignBadge(label: String, sign: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = sign.take(3),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
        Text(
            text = sign,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextSecondary
        )
    }
}

@Composable
private fun KeyStrengthsCard(
    strengths: List<TraitInfo>,
    language: Language
) {
    if (strengths.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = AppTheme.SuccessColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyNative.LABEL_STRENGTHS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            strengths.forEach { trait ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("\u2713", color = AppTheme.SuccessColor)
                        Text(
                            text = if (language == Language.NEPALI) trait.trait.ne else trait.trait.en,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextPrimary
                        )
                    }
                    trait.planet?.let { planet ->
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ChipBackground
                        ) {
                            Text(
                                text = planet.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyChallengesCard(
    challenges: List<TraitInfo>,
    language: Language
) {
    if (challenges.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.WarningColor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.WarningColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyNative.LABEL_CHALLENGES),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            challenges.forEach { trait ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("\u26A0", color = AppTheme.WarningColor)
                        Text(
                            text = if (language == Language.NEPALI) trait.trait.ne else trait.trait.en,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextPrimary
                        )
                    }
                    trait.planet?.let { planet ->
                        Surface(
                            shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                            color = AppTheme.ChipBackground
                        ) {
                            Text(
                                text = planet.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickInsightsRow(result: NativeAnalysisResult, language: Language) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickInsightCard(
            title = stringResource(StringKeyNative.SECTION_CAREER),
            value = getStrengthLabel(result.careerAnalysis.careerStrength, language),
            color = getStrengthColor(result.careerAnalysis.careerStrength),
            modifier = Modifier.weight(1f)
        )
        QuickInsightCard(
            title = stringResource(StringKeyNative.SECTION_MARRIAGE),
            value = getStrengthLabel(result.marriageAnalysis.relationshipStrength, language),
            color = getStrengthColor(result.marriageAnalysis.relationshipStrength),
            modifier = Modifier.weight(1f)
        )
        QuickInsightCard(
            title = stringResource(StringKeyNative.SECTION_WEALTH),
            value = getStrengthLabel(result.wealthAnalysis.wealthPotential, language),
            color = getStrengthColor(result.wealthAnalysis.wealthPotential),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickInsightCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.TextMuted,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = color,
                maxLines = 1
            )
        }
    }
}

// -----------------------------------------------------------------------------
// CHARACTER SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeCharacterSection(
    character: CharacterAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_CHARACTER),
            icon = Icons.Outlined.Person,
            color = AppTheme.AccentPrimary
        )

        PersonalityStrengthCard(character.personalityStrength, language)

        TraitCard(
            title = stringResource(StringKeyNative.TITLE_ASCENDANT_TRAITS),
            content = if (language == Language.NEPALI) character.ascendantTrait.ne else character.ascendantTrait.en,
            color = AppTheme.AccentPrimary
        )

        TraitCard(
            title = stringResource(StringKeyNative.TITLE_MOON_SIGN_INFLUENCE),
            content = if (language == Language.NEPALI) character.moonTrait.ne else character.moonTrait.en,
            color = AppTheme.AccentTeal
        )

        TraitCard(
            title = stringResource(StringKeyNative.TITLE_NAKSHATRA_INFLUENCE),
            content = if (language == Language.NEPALI) character.nakshatraInfluenceNe else character.nakshatraInfluence,
            color = AppTheme.AccentGold
        )

        SummaryCard(
            summary = if (language == Language.NEPALI) character.summaryNe else character.summaryEn,
            color = AppTheme.AccentPrimary
        )
    }
}

@Composable
private fun PersonalityStrengthCard(strength: StrengthLevel, language: Language) {
    val color = getStrengthColor(strength)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_PERSONALITY_FOUNDATION),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = getStrengthLabel(strength, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            LinearProgressIndicator(
                progress = { strength.value / 5f },
                modifier = Modifier
                    .width(80.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

// -----------------------------------------------------------------------------
// CAREER SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeCareerSection(
    career: CareerAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_CAREER),
            icon = Icons.Outlined.Work,
            color = AppTheme.LifeAreaCareer
        )

        StrengthIndicatorCard(
            title = stringResource(StringKeyNative.TITLE_CAREER_STRENGTH),
            strength = career.careerStrength,
            language = language
        )

        HouseLordCard(
            houseNumber = 10,
            lord = career.tenthLord.displayName,
            house = career.tenthLordHouse,
            dignity = career.tenthLordDignity,
            language = language
        )

        if (career.tenthHousePlanets.isNotEmpty()) {
            PlanetsInHouseCard(
                houseNumber = 10,
                planets = career.tenthHousePlanets.map { it.displayName },
                color = AppTheme.LifeAreaCareer
            )
        }

        FavorableFieldsCard(
            fields = if (language == Language.NEPALI) career.favorableFieldsNe else career.favorableFields,
            color = AppTheme.LifeAreaCareer
        )

        if (career.careerIndicators.isNotEmpty()) {
            CareerIndicatorsCard(career.careerIndicators, language)
        }

        SummaryCard(
            summary = if (language == Language.NEPALI) career.summaryNe else career.summaryEn,
            color = AppTheme.LifeAreaCareer
        )
    }
}

@Composable
private fun FavorableFieldsCard(fields: List<String>, color: Color) {
    if (fields.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_FAVORABLE_CAREER_FIELDS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            fields.forEach { field ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\u2022", color = color)
                    Text(
                        text = field,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun CareerIndicatorsCard(
    indicators: List<StringKeyNative>,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_CAREER_INDICATORS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            indicators.forEach { indicator ->
                Text(
                    text = if (language == Language.NEPALI) indicator.ne else indicator.en,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// MARRIAGE SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeMarriageSection(
    marriage: MarriageAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_MARRIAGE),
            icon = Icons.Outlined.Favorite,
            color = AppTheme.LifeAreaLove
        )

        StrengthIndicatorCard(
            title = stringResource(StringKeyNative.TITLE_RELATIONSHIP_STRENGTH),
            strength = marriage.relationshipStrength,
            language = language
        )

        HouseLordCard(
            houseNumber = 7,
            lord = marriage.seventhLord.displayName,
            house = marriage.seventhLordHouse,
            dignity = marriage.seventhLordDignity,
            language = language
        )

        MarriageTimingCard(marriage.marriageTiming, language)

        VenusStrengthCard(marriage.venusStrength, language)

        SpouseNatureCard(
            nature = if (language == Language.NEPALI) marriage.spouseNatureNe else marriage.spouseNature
        )

        SummaryCard(
            summary = if (language == Language.NEPALI) marriage.summaryNe else marriage.summaryEn,
            color = AppTheme.LifeAreaLove
        )
    }
}

@Composable
private fun MarriageTimingCard(
    timing: MarriageTiming,
    language: Language
) {
    val color = when (timing) {
        MarriageTiming.EARLY -> AppTheme.SuccessColor
        MarriageTiming.NORMAL -> AppTheme.AccentGold
        MarriageTiming.DELAYED -> AppTheme.WarningColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_MARRIAGE_TIMING),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = if (language == Language.NEPALI) timing.displayNameNe else timing.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            }
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun VenusStrengthCard(strength: StrengthLevel, language: Language) {
    val color = getStrengthColor(strength)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_VENUS_STRENGTH),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = getStrengthLabel(strength, language),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
            LinearProgressIndicator(
                progress = { strength.value / 5f },
                modifier = Modifier
                    .width(80.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun SpouseNatureCard(nature: String) {
    if (nature.isBlank()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_SPOUSE_NATURE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nature,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )
        }
    }
}

// -----------------------------------------------------------------------------
// HEALTH SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeHealthSection(
    health: HealthAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_HEALTH),
            icon = Icons.Outlined.HealthAndSafety,
            color = AppTheme.LifeAreaHealth
        )

        ConstitutionCard(health.constitution, language)

        LongevityCard(health.longevityIndicator, language)

        VulnerableAreasCard(health.vulnerableAreas, language)

        if (health.healthConcerns.isNotEmpty()) {
            HealthConcernsCard(
                concerns = if (language == Language.NEPALI) health.healthConcernsNe else health.healthConcerns
            )
        }

        SummaryCard(
            summary = if (language == Language.NEPALI) health.summaryNe else health.summaryEn,
            color = AppTheme.LifeAreaHealth
        )
    }
}

@Composable
private fun ConstitutionCard(
    constitution: ConstitutionType,
    language: Language
) {
    val color = when (constitution) {
        ConstitutionType.STRONG -> AppTheme.SuccessColor
        ConstitutionType.MODERATE -> AppTheme.AccentGold
        ConstitutionType.SENSITIVE -> AppTheme.WarningColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(StringKeyDeepHealth.CONSTITUTION_TITLE),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = if (language == Language.NEPALI) constitution.displayNameNe else constitution.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            }
            Icon(
                imageVector = Icons.Outlined.HealthAndSafety,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun LongevityCard(
    longevity: LongevityIndicator,
    language: Language
) {
    val color = when (longevity) {
        LongevityIndicator.LONG -> AppTheme.SuccessColor
        LongevityIndicator.MEDIUM -> AppTheme.AccentGold
        LongevityIndicator.REQUIRES_CARE -> AppTheme.WarningColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_LONGEVITY_INDICATOR),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = if (language == Language.NEPALI) longevity.displayNameNe else longevity.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun VulnerableAreasCard(areas: StringKeyNative, language: Language) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_VULNERABLE_AREAS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (language == Language.NEPALI) areas.ne else areas.en,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun HealthConcernsCard(concerns: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.WarningColor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = AppTheme.WarningColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(StringKeyNative.LABEL_HEALTH_CONCERNS),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            concerns.forEach { concern ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\u2022", color = AppTheme.WarningColor)
                    Text(
                        text = concern,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// WEALTH SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeWealthSection(
    wealth: WealthAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_WEALTH),
            icon = Icons.Outlined.AutoAwesome,
            color = AppTheme.LifeAreaFinance
        )

        StrengthIndicatorCard(
            title = stringResource(StringKeyNative.TITLE_WEALTH_POTENTIAL),
            strength = wealth.wealthPotential,
            language = language
        )

        if (wealth.dhanaYogaPresent) {
            DhanaYogaCard()
        }

        WealthLordsCard(wealth, language)

        if (wealth.primarySources.isNotEmpty()) {
            WealthSourcesCard(
                sources = if (language == Language.NEPALI) wealth.primarySourcesNe else wealth.primarySources
            )
        }

        SummaryCard(
            summary = if (language == Language.NEPALI) wealth.summaryNe else wealth.summaryEn,
            color = AppTheme.LifeAreaFinance
        )
    }
}

@Composable
private fun DhanaYogaCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = AppTheme.SuccessColor,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_DHANA_YOGA_PRESENT),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )
                Text(
                    text = stringResource(StringKeyNative.WEALTH_DHANA_YOGA),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun WealthLordsCard(wealth: WealthAnalysis, language: Language) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_WEALTH_LORDS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.HOUSE_LORD_2),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = wealth.secondLord.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = getStrengthLabel(wealth.secondLordStrength, language),
                        style = MaterialTheme.typography.labelSmall,
                        color = getStrengthColor(wealth.secondLordStrength)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.HOUSE_LORD_11),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = wealth.eleventhLord.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = getStrengthLabel(wealth.eleventhLordStrength, language),
                        style = MaterialTheme.typography.labelSmall,
                        color = getStrengthColor(wealth.eleventhLordStrength)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = Planet.JUPITER.getLocalizedName(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "\u2643",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = getStrengthLabel(wealth.jupiterStrength, language),
                        style = MaterialTheme.typography.labelSmall,
                        color = getStrengthColor(wealth.jupiterStrength)
                    )
                }
            }
        }
    }
}

@Composable
private fun WealthSourcesCard(sources: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_PRIMARY_WEALTH_SOURCES),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            sources.forEach { source ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\u2022", color = AppTheme.LifeAreaFinance)
                    Text(
                        text = source,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// EDUCATION SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeEducationSection(
    education: EducationAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_EDUCATION),
            icon = Icons.Outlined.School,
            color = AppTheme.AccentTeal
        )

        StrengthIndicatorCard(
            title = stringResource(StringKeyNative.TITLE_ACADEMIC_POTENTIAL),
            strength = education.academicPotential,
            language = language
        )

        MercuryStrengthCard(education.mercuryStrength, language)

        if (education.jupiterAspectOnEducation) {
            JupiterBlessingCard()
        }

        if (education.favorableSubjects.isNotEmpty()) {
            FavorableSubjectsCard(
                subjects = if (language == Language.NEPALI) education.favorableSubjectsNe else education.favorableSubjects
            )
        }

        SummaryCard(
            summary = if (language == Language.NEPALI) education.summaryNe else education.summaryEn,
            color = AppTheme.AccentTeal
        )
    }
}

@Composable
private fun MercuryStrengthCard(strength: StrengthLevel, language: Language) {
    val color = getStrengthColor(strength)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_MERCURY_STRENGTH),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = getStrengthLabel(strength, language),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
            LinearProgressIndicator(
                progress = { strength.value / 5f },
                modifier = Modifier
                    .width(80.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun JupiterBlessingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.SuccessColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = AppTheme.SuccessColor,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_JUPITER_BLESSING),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor
                )
                Text(
                    text = stringResource(StringKeyNative.EDUCATION_JUPITER_ASPECT),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun FavorableSubjectsCard(subjects: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_FAVORABLE_SUBJECTS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            subjects.forEach { subject ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\u2022", color = AppTheme.AccentTeal)
                    Text(
                        text = subject,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SPIRITUAL SECTION
// -----------------------------------------------------------------------------

@Composable
private fun NativeSpiritualSection(
    spiritual: SpiritualAnalysis,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeaderCard(
            title = stringResource(StringKeyNative.SECTION_SPIRITUAL),
            icon = Icons.Outlined.Spa,
            color = AppTheme.LifeAreaSpiritual
        )

        StrengthIndicatorCard(
            title = stringResource(StringKeyNative.TITLE_SPIRITUAL_INCLINATION),
            strength = spiritual.spiritualInclination,
            language = language
        )

        SpiritualLordsCard(spiritual, language)

        if (spiritual.recommendedPractices.isNotEmpty()) {
            RecommendedPracticesCard(
                practices = if (language == Language.NEPALI) spiritual.recommendedPracticesNe else spiritual.recommendedPractices
            )
        }

        SummaryCard(
            summary = if (language == Language.NEPALI) spiritual.summaryNe else spiritual.summaryEn,
            color = AppTheme.LifeAreaSpiritual
        )
    }
}

@Composable
private fun SpiritualLordsCard(
    spiritual: SpiritualAnalysis,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_SPIRITUAL_INDICATORS),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.HOUSE_LORD_9),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = spiritual.ninthLord.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.HOUSE_LORD_12),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = spiritual.twelfthLord.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = Planet.JUPITER.getLocalizedName(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = getStrengthLabel(spiritual.jupiterStrength, language),
                        style = MaterialTheme.typography.bodySmall,
                        color = getStrengthColor(spiritual.jupiterStrength)
                    )
                }
            }

            spiritual.ketuPosition?.let { ketu ->
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = AppTheme.DividerColor)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = String.format(stringResource(StringKeyNative.LABEL_PLANETS_IN_HOUSE), ketu.house) + " (${ketu.sign.displayName})",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.LifeAreaSpiritual
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendedPracticesCard(practices: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.LifeAreaSpiritual.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(StringKeyNative.LABEL_RECOMMENDED_PRACTICES),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.LifeAreaSpiritual
            )
            Spacer(modifier = Modifier.height(8.dp))
            practices.forEach { practice ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\uD83D\uDE4F", fontSize = com.astro.storm.ui.theme.NeoVedicFontSizes.S12)
                    Text(
                        text = practice,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SHARED COMPONENTS
// -----------------------------------------------------------------------------

@Composable
private fun SectionHeaderCard(title: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun StrengthIndicatorCard(title: String, strength: StrengthLevel, language: Language) {
    val color = getStrengthColor(strength)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.TextMuted
                )
                Text(
                    text = getStrengthLabel(strength, language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            LinearProgressIndicator(
                progress = { strength.value / 5f },
                modifier = Modifier
                    .width(100.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun HouseLordCard(
    houseNumber: Int,
    lord: String,
    house: Int,
    dignity: PlanetaryDignity,
    language: Language
) {
    val dignityColor = when (dignity) {
        PlanetaryDignity.EXALTED -> AppTheme.SuccessColor
        PlanetaryDignity.MOOLATRIKONA,
        PlanetaryDignity.OWN_SIGN -> AppTheme.AccentTeal
        PlanetaryDignity.FRIEND_SIGN -> AppTheme.AccentGold
        PlanetaryDignity.NEUTRAL_SIGN -> AppTheme.TextSecondary
        PlanetaryDignity.ENEMY_SIGN -> AppTheme.WarningColor
        PlanetaryDignity.DEBILITATED -> AppTheme.ErrorColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = String.format(stringResource(StringKeyAnalysis.DIALOG_HOUSE_PLACEMENT), houseNumber),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.PANCHANGA_LORD),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = lord,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyNative.LABEL_IN_HOUSE),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = String.format(stringResource(StringKeyAnalysis.UI_IN_HOUSE_FMT), house),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(StringKeyAnalysis.DIALOG_DIGNITY),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = if (language == Language.NEPALI) dignity.displayNameNe else dignity.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = dignityColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanetsInHouseCard(houseNumber: Int, planets: List<String>, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = String.format(stringResource(StringKeyNative.LABEL_PLANETS_IN_HOUSE), houseNumber),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(planets) { planet ->
                    Surface(
                        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius),
                        color = color.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = planet,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = color,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TraitCard(title: String, content: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun SummaryCard(summary: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Psychology,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(StringKeyNative.LABEL_OVERVIEW), // Summary -> Overview label
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

// -----------------------------------------------------------------------------
// UTILITY FUNCTIONS
// -----------------------------------------------------------------------------

@Composable
private fun getStrengthColor(strength: StrengthLevel): Color {
    return when (strength) {
        StrengthLevel.EXCELLENT -> AppTheme.SuccessColor
        StrengthLevel.STRONG -> AppTheme.AccentTeal
        StrengthLevel.MODERATE -> AppTheme.AccentGold
        StrengthLevel.WEAK -> AppTheme.WarningColor
        StrengthLevel.AFFLICTED -> AppTheme.ErrorColor
    }
}

@Composable
private fun getStrengthLabel(strength: StrengthLevel, language: Language): String {
    return if (language == Language.NEPALI) strength.displayNameNe else strength.displayName
}

// -----------------------------------------------------------------------------
// LOADING / ERROR / EMPTY STATES
// -----------------------------------------------------------------------------

@Composable
private fun NativeLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AppTheme.AccentPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyNative.LABEL_ANALYZING_CHART),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun NativeErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = AppTheme.ErrorColor,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyNative.LABEL_ANALYSIS_ERROR),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
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
private fun NativeEmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeyNative.LABEL_NO_CHART_SELECTED),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyNative.LABEL_SELECT_CHART_DESC),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun NativeInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyNative.NATIVE_ANALYSIS_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeyNative.LABEL_COMPREHENSIVE_DESC),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeyNative.REPORT_DISCLAIMER),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_OK), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}





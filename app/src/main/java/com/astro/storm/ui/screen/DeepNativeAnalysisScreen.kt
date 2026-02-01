package com.astro.storm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyNative
import com.astro.storm.core.common.StringKeyDeepCharacter
import com.astro.storm.core.common.StringKeyDeepCareer
import com.astro.storm.core.common.StringKeyDeepRelationship
import com.astro.storm.core.common.StringKeyDeepHealth
import com.astro.storm.core.common.StringKeyDeepWealth
import com.astro.storm.core.common.StringKeyDeepSpiritual
import com.astro.storm.core.common.StringKeyDeepEducation
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.localization.localizedName
import com.astro.storm.data.localization.localized
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ui.components.deepanalysis.*
import com.astro.storm.ui.viewmodel.DeepAnalysisSection
import com.astro.storm.ui.viewmodel.DeepAnalysisUiState
import com.astro.storm.ui.viewmodel.DeepAnalysisViewModel
import com.astro.storm.ui.theme.AppTheme

import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem

/**
 * Deep Native Analysis Screen
 * 
 * Comprehensive analysis display with section navigation
 * and expandable content cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepNativeAnalysisScreen(
    chart: VedicChart,
    onBack: () -> Unit,
    viewModel: DeepAnalysisViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(StringKeyNative.NATIVE_ANALYSIS_TITLE),
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(StringKeyUIPart1.BTN_BACK),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
            )
        },
        containerColor = AppTheme.ScreenBackground
    ) { padding ->
        DeepNativeAnalysisBody(
            chart = chart,
            viewModel = viewModel,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeepNativeAnalysisBody(
    chart: VedicChart,
    viewModel: DeepAnalysisViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedSection by viewModel.selectedSection.collectAsState()
    val expandedCards by viewModel.expandedCards.collectAsState()
    
    // Trigger analysis on first composition
    LaunchedEffect(chart) {
        viewModel.calculateDeepAnalysis(chart)
    }
    
    val sections = listOf(
        DeepAnalysisSection.OVERVIEW to stringResource(StringKeyNative.LABEL_OVERVIEW),
        DeepAnalysisSection.CHARACTER to stringResource(StringKeyNative.SECTION_CHARACTER),
        DeepAnalysisSection.CAREER to stringResource(StringKeyNative.SECTION_CAREER),
        DeepAnalysisSection.RELATIONSHIP to stringResource(StringKeyNative.SECTION_MARRIAGE),
        DeepAnalysisSection.HEALTH to stringResource(StringKeyNative.SECTION_HEALTH),
        DeepAnalysisSection.WEALTH to stringResource(StringKeyNative.SECTION_WEALTH),
        DeepAnalysisSection.EDUCATION to stringResource(StringKeyNative.SECTION_EDUCATION),
        DeepAnalysisSection.SPIRITUAL to stringResource(StringKeyNative.SECTION_SPIRITUAL)
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Section tabs
        val tabItems = sections.map { TabItem(title = it.second, accentColor = AppTheme.AccentPrimary) }
        val selectedIndex = sections.indexOfFirst { it.first == selectedSection }
        
        ModernPillTabRow(
            tabs = tabItems,
            selectedIndex = if (selectedIndex >= 0) selectedIndex else 0,
            onTabSelected = { index ->
                viewModel.selectSection(sections[index].first)
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.3f))
        
        // Content based on state
        when (val state = uiState) {
            is DeepAnalysisUiState.Initial,
            is DeepAnalysisUiState.Loading -> {
                DeepAnalysisLoading()
            }
            is DeepAnalysisUiState.Error -> {
                DeepAnalysisError(
                    message = state.message,
                    onRetry = { viewModel.calculateDeepAnalysis(chart) }
                )
            }
            is DeepAnalysisUiState.Success -> {
                DeepAnalysisContent(
                    analysis = state.analysis,
                    section = selectedSection,
                    expandedCards = expandedCards,
                    onToggleCard = viewModel::toggleCardExpansion
                )
            }
        }
    }
}

@Composable
fun DeepAnalysisContent(
    analysis: DeepNativeAnalysis,
    section: DeepAnalysisSection,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (section) {
            DeepAnalysisSection.OVERVIEW -> {
                item { OverviewSection(analysis, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.CHARACTER -> {
                item { CharacterSection(analysis.character, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.CAREER -> {
                item { CareerSection(analysis.career, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.RELATIONSHIP -> {
                item { RelationshipSection(analysis.relationship, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.HEALTH -> {
                item { HealthSection(analysis.health, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.WEALTH -> {
                item { WealthSection(analysis.wealth, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.EDUCATION -> {
                item { EducationSection(analysis.education, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.SPIRITUAL -> {
                item { SpiritualSection(analysis.spiritual, expandedCards, onToggleCard) }
            }
            DeepAnalysisSection.PREDICTIONS -> {
                // Handled by separate predictions screen
            }
        }
    }
}

@Composable
private fun OverviewSection(
    analysis: DeepNativeAnalysis,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Overview summary card with all scores
    AnalysisOverviewCard(
        title = stringResource(StringKeyNative.LABEL_OVERVIEW),
        scores = listOf(
            stringResource(StringKeyNative.SECTION_CHARACTER) to analysis.character.personalityStrengthScore,
            stringResource(StringKeyNative.SECTION_CAREER) to analysis.career.careerStrengthScore,
            stringResource(StringKeyNative.SECTION_MARRIAGE) to analysis.relationship.relationshipStrengthScore,
            stringResource(StringKeyNative.SECTION_HEALTH) to analysis.health.healthStrengthScore,
            stringResource(StringKeyNative.SECTION_WEALTH) to analysis.wealth.wealthStrengthScore,
            stringResource(StringKeyNative.SECTION_EDUCATION) to analysis.education.educationStrengthScore,
            stringResource(StringKeyNative.SECTION_SPIRITUAL) to analysis.spiritual.spiritualStrengthScore
        ),
        summary = analysis.character.personalitySummary
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Quick highlights
    DeepSectionHeader(title = stringResource(StringKeyNative.LABEL_STRENGTHS), icon = Icons.Default.Star)
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepCharacter.INNER_NEEDS_TITLE),
        subtitle = stringResource(StringKeyNative.LABEL_COMPREHENSIVE_DESC),
        isExpanded = "overview_themes" in expandedCards,
        onToggle = { onToggleCard("overview_themes") }
    ) {
        Column {
            Text(
                text = stringResource(StringKeyNative.LABEL_COMPREHENSIVE_DESC),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CharacterSection(
    character: CharacterDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = character.personalityStrengthScore,
            label = stringResource(StringKeyDeepCharacter.PERSONALITY_SCORE)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyDeepCharacter.SECTION_ASCENDANT_ANALYSIS), icon = Icons.Default.Person)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyGeneralPart1.ASCENDANT)}: ${character.ascendantAnalysis.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepCharacter.FIRST_IMPRESSION_TITLE),
        strength = character.ascendantAnalysis.overallAscendantStrength,
        isExpanded = "char_ascendant" in expandedCards,
        onToggle = { onToggleCard("char_ascendant") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = character.ascendantAnalysis.physicalAppearance)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = character.ascendantAnalysis.firstImpression)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = character.ascendantAnalysis.lifeApproach)
        }
    }
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyReport.EXPORT_MOON_SIGN)}: ${character.moonAnalysis.moonSign.localizedName()}",
        subtitle = stringResource(StringKeyDeepCharacter.SECTION_EMOTIONAL_PROFILE),
        strength = character.moonAnalysis.overallEmotionalStrength,
        isExpanded = "char_moon" in expandedCards,
        onToggle = { onToggleCard("char_moon") }
    ) {
        Column {
            Text(
                text = "${stringResource(StringKeyUIPart1.DIALOG_NAKSHATRA)}: ${character.moonAnalysis.nakshatra.localizedName()} (${stringResource(StringKeyPanchanga.PANCHANGA_PADA)} ${character.moonAnalysis.nakshatraPada.localized()})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = character.moonAnalysis.nakshatraCharacteristics)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = character.moonAnalysis.emotionalNature)
        }
    }
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyReport.EXPORT_SUN_SIGN)}: ${character.sunAnalysis.sunSign.localizedName()}",
        subtitle = stringResource(StringKeyDeepCharacter.SUN_CORE_IDENTITY),
        strength = character.sunAnalysis.sunStrength,
        isExpanded = "char_sun" in expandedCards,
        onToggle = { onToggleCard("char_sun") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = character.sunAnalysis.coreIdentity)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = character.sunAnalysis.egoExpression)
        }
    }
    
    if (character.keyTraits.isNotEmpty()) {
        DeepSectionHeader(title = stringResource(StringKeyDeepCharacter.KEY_TRAITS), icon = Icons.Default.Psychology)
        TraitsList(traits = character.keyTraits)
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = character.personalitySummary)
}

@Composable
private fun CareerSection(
    career: CareerDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = career.careerStrengthScore,
            label = stringResource(StringKeyDeepCareer.CAREER_SCORE)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyDeepCareer.SECTION_10TH_HOUSE), icon = Icons.Default.Work)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepCareer.TENTH_HOUSE_SIGN)}: ${career.tenthHouseAnalysis.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepCareer.PUBLIC_IMAGE_TITLE),
        strength = career.tenthHouseAnalysis.houseStrength,
        isExpanded = "career_10th" in expandedCards,
        onToggle = { onToggleCard("career_10th") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = career.tenthHouseAnalysis.publicImage)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = career.tenthHouseAnalysis.careerEnvironment)
        }
    }
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepCareer.TENTH_HOUSE_LORD)}: ${career.tenthLordAnalysis.lord.localizedName()}",
        subtitle = "${stringResource(StringKeyGeneralPart5.HOUSE)} ${career.tenthLordAnalysis.housePosition.localized()} ${stringResource(StringKeyUIPart1.DIALOG_HOUSE_PLACEMENT)}",
        strength = career.tenthLordAnalysis.strengthLevel,
        isExpanded = "career_lord" in expandedCards,
        onToggle = { onToggleCard("career_lord") }
    ) {
        LocalizedParagraphText(paragraph = career.tenthLordAnalysis.interpretation)
    }
    
    if (career.suitableProfessions.isNotEmpty()) {
        DeepSectionHeader(title = stringResource(StringKeyDeepCareer.SUITABLE_PROFESSIONS), icon = Icons.Default.BusinessCenter)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (profession in career.suitableProfessions) {
                val strengthColor = DeepAnalysisColors.forStrength(profession.suitability)
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = strengthColor.copy(alpha = 0.08f),
                            spotColor = strengthColor.copy(alpha = 0.08f)
                        ),
                    color = AppTheme.CardBackground,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left colored indicator
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(32.dp)
                                .background(
                                    strengthColor,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        
                        Spacer(modifier = Modifier.width(14.dp))
                        
                        val language = com.astro.storm.data.localization.LocalLanguage.current
                        val localizedProfession = if (language == com.astro.storm.core.common.Language.NEPALI) {
                            profession.professionName
                        } else {
                            profession.professionName
                        }
                        Text(
                            text = localizedProfession,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        StrengthBadge(strength = profession.suitability)
                    }
                }
            }
        }
    }
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepCareer.SECTION_WORK_STYLE),
        subtitle = stringResource(StringKeyDeepCareer.WORK_STYLE_TITLE),
        isExpanded = "career_style" in expandedCards,
        onToggle = { onToggleCard("career_style") }
    ) {
        Column {
            RecommendationItem(
                label = stringResource(StringKeyDeepCareer.CAREER_ENVIRONMENT),
                text = career.workStyle.preferredEnvironment,
                icon = Icons.Default.BusinessCenter
            )
            Spacer(modifier = Modifier.height(8.dp))
            RecommendationItem(
                label = stringResource(StringKeyDeepCareer.LEADERSHIP_STYLE),
                text = career.workStyle.leadershipStyle,
                icon = Icons.Default.Engineering
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = career.careerSummary)
}

@Composable
private fun RelationshipSection(
    relationship: RelationshipDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = relationship.relationshipStrengthScore,
            label = stringResource(StringKeyDeepRelationship.REL_SCORE)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyDeepRelationship.SECTION_7TH_HOUSE), icon = Icons.Default.Favorite)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepRelationship.SEVENTH_HOUSE_SIGN)}: ${relationship.seventhHouseAnalysis.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepRelationship.RELATIONSHIP_STYLE),
        strength = relationship.seventhHouseAnalysis.houseStrength,
        isExpanded = "rel_7th" in expandedCards,
        onToggle = { onToggleCard("rel_7th") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = relationship.seventhHouseAnalysis.partnershipEnvironment)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = relationship.seventhHouseAnalysis.publicDealings)
        }
    }
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepRelationship.SECTION_VENUS),
        subtitle = "${stringResource(StringKeyDeepRelationship.SECTION_VENUS)} ${stringResource(StringKeyNative.LABEL_IN_HOUSE)} ${relationship.venusAnalysis.sign.localizedName()}",
        strength = relationship.venusAnalysis.strengthLevel,
        isExpanded = "rel_venus" in expandedCards,
        onToggle = { onToggleCard("rel_venus") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = relationship.venusAnalysis.romanticNature)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = relationship.venusAnalysis.attractionStyle)
        }
    }
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepRelationship.SECTION_PARTNER_PROFILE),
        subtitle = stringResource(StringKeyDeepRelationship.PARTNER_PERSONALITY),
        isExpanded = "rel_partner" in expandedCards,
        onToggle = { onToggleCard("rel_partner") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = relationship.partnerProfile.physicalDescription)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = relationship.partnerProfile.professionIndicators)
        }
    }
    
    DeepSectionHeader(title = stringResource(StringKeyDeepRelationship.SECTION_MARRIAGE_TIMING), icon = Icons.Default.CalendarMonth)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepRelationship.TIMING_CATEGORY)}: ${
            when(relationship.marriageTiming.timingCategory) {
                MarriageTimingCategory.EARLY -> stringResource(StringKeyNative.MARRIAGE_TIMING_EARLY)
                MarriageTimingCategory.NORMAL -> stringResource(StringKeyNative.MARRIAGE_TIMING_NORMAL)
                MarriageTimingCategory.DELAYED -> stringResource(StringKeyNative.MARRIAGE_TIMING_DELAYED)
                else -> relationship.marriageTiming.timingCategory.name
            }
        }",
        subtitle = "${stringResource(StringKeyDeepRelationship.ESTIMATED_AGE)}: ${relationship.marriageTiming.estimatedAgeRange}",
        isExpanded = "rel_timing" in expandedCards,
        onToggle = { onToggleCard("rel_timing") }
    ) {
        LocalizedParagraphText(paragraph = relationship.marriageTiming.timingAdvice)
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = relationship.relationshipSummary)
}

@Composable
private fun HealthSection(
    health: HealthDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = health.healthStrengthScore,
            label = stringResource(StringKeyDeepHealth.HEALTH_SCORE)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyDeepHealth.CONSTITUTION_TITLE), icon = Icons.Default.HealthAndSafety)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepHealth.PRIMARY_DOSHA)}: ${
            when(health.constitutionAnalysis.primaryDosha) {
                AyurvedicDosha.VATA -> "Vata (वात)"
                AyurvedicDosha.PITTA -> "Pitta (पित्त)"
                AyurvedicDosha.KAPHA -> "Kapha (कफ)"
            }
        }",
        subtitle = stringResource(StringKeyDeepHealth.SECTION_CONSTITUTION),
        isExpanded = "health_dosha" in expandedCards,
        onToggle = { onToggleCard("health_dosha") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = health.constitutionAnalysis.constitutionDescription)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(StringKeyDeepHealth.DIETARY_RECS), fontWeight = FontWeight.Medium)
            for (item in health.dietaryRecommendations) {
                 Row(
                     modifier = Modifier.padding(vertical = 4.dp),
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Icon(
                         Icons.Default.Restaurant,
                         contentDescription = null,
                         modifier = Modifier.size(16.dp),
                         tint = AppTheme.AccentPrimary
                     )
                     Spacer(modifier = Modifier.width(8.dp))
                     LocalizedTraitText(trait = item)
                 }
            }
        }
    }
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepHealth.ASCENDANT_HEALTH),
        subtitle = stringResource(StringKeyDeepHealth.GENERAL_VITALITY),
        strength = health.ascendantHealthProfile.generalVitality,
        isExpanded = "health_vitality" in expandedCards,
        onToggle = { onToggleCard("health_vitality") }
    ) {
        LocalizedParagraphText(paragraph = health.ascendantHealthProfile.physicalConstitutionType)
    }
    
    if (health.healthStrengths.isNotEmpty()) {
        DeepSectionHeader(title = stringResource(StringKeyDeepHealth.HEALTH_STRENGTHS), icon = Icons.Default.Favorite)
        TraitsList(traits = health.healthStrengths)
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = health.healthSummary)
}

@Composable
private fun WealthSection(
    wealth: WealthDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = wealth.wealthStrengthScore,
            label = stringResource(StringKeyDeepWealth.WEALTH_SCORE)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyDeepWealth.SECTION_WEALTH), icon = Icons.Default.AttachMoney)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepWealth.SECOND_HOUSE_SIGN)}: ${wealth.secondHouseAnalysis.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepWealth.ACCUMULATION_PATTERN),
        strength = wealth.secondHouseAnalysis.houseStrength,
        isExpanded = "wealth_2nd" in expandedCards,
        onToggle = { onToggleCard("wealth_2nd") }
    ) {
        LocalizedParagraphText(paragraph = wealth.secondHouseAnalysis.accumulationPattern)
    }
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepWealth.ELEVENTH_HOUSE_SIGN)}: ${wealth.eleventhHouseAnalysis.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepWealth.GAINS_PATTERN),
        strength = wealth.eleventhHouseAnalysis.houseStrength,
        isExpanded = "wealth_11th" in expandedCards,
        onToggle = { onToggleCard("wealth_11th") }
    ) {
        LocalizedParagraphText(paragraph = wealth.eleventhHouseAnalysis.gainsPattern)
    }
    
    if (wealth.dhanaYogaAnalysis.presentYogas.isNotEmpty()) {
        DeepSectionHeader(title = stringResource(StringKeyDeepWealth.DHANA_YOGA_TITLE), icon = Icons.Default.Star)
        Column {
            for (yoga in wealth.dhanaYogaAnalysis.presentYogas) {
                TimelinePeriodCard(
                    title = yoga.name,
                    dateRange = stringResource(StringKeyDeepWealth.YOGA_ACTIVATION),
                    description = yoga.wealthEffect,
                    strength = yoga.strength
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = wealth.wealthSummary)
}

@Composable
private fun EducationSection(
    education: EducationDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = education.educationStrengthScore,
            label = stringResource(StringKeyNative.TITLE_ACADEMIC_POTENTIAL)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyNative.SECTION_EDUCATION), icon = Icons.Default.School)
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepEducation.INTELLECTUAL_ABILITY),
        subtitle = stringResource(StringKeyDeepEducation.FIFTH_HOUSE_SIGN),
        strength = education.fifthHouseAnalysis.intellectualAbility,
        isExpanded = "edu_5th" in expandedCards,
        onToggle = { onToggleCard("edu_5th") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = education.fifthHouseAnalysis.creativeIntelligence)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = education.fifthHouseAnalysis.memoryAndGrasp)
        }
    }
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyGeneralPart8.PLANET_MERCURY)}: ${education.mercuryAnalysis.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepEducation.ANALYTICAL_ABILITY),
        strength = education.mercuryAnalysis.strengthLevel,
        isExpanded = "edu_mercury" in expandedCards,
        onToggle = { onToggleCard("edu_mercury") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = education.mercuryAnalysis.analyticalAbility)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = education.mercuryAnalysis.languageAbility)
        }
    }
    
    if (education.suitableSubjects.isNotEmpty()) {
        DeepSectionHeader(title = stringResource(StringKeyDeepEducation.SUBJECT_AFFINITY_TITLE), icon = Icons.Default.MenuBook)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (subject in education.suitableSubjects) {
                val strengthColor = DeepAnalysisColors.forStrength(subject.affinity)
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = strengthColor.copy(alpha = 0.08f),
                            spotColor = strengthColor.copy(alpha = 0.08f)
                        ),
                    color = AppTheme.CardBackground,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left colored indicator
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(32.dp)
                                .background(
                                    strengthColor,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        
                        Spacer(modifier = Modifier.width(14.dp))
                        
                        Text(
                            text = subject.subjectName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        StrengthBadge(strength = subject.affinity)
                    }
                }
            }
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = education.educationSummary)
}

@Composable
private fun SpiritualSection(
    spiritual: SpiritualDeepResult,
    expandedCards: Set<String>,
    onToggleCard: (String) -> Unit
) {
    // Centered score indicator
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ScoreIndicator(
            score = spiritual.spiritualStrengthScore,
            label = stringResource(StringKeyNative.TITLE_SPIRITUAL_INCLINATION)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = stringResource(StringKeyNative.SECTION_SPIRITUAL), icon = Icons.Default.SelfImprovement)
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepSpiritual.NINTH_DHARMA_SIGN)}: ${spiritual.ninthHouseDharma.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepSpiritual.DHARMA_PATH),
        strength = spiritual.ninthHouseDharma.houseStrength,
        isExpanded = "spirit_9th" in expandedCards,
        onToggle = { onToggleCard("spirit_9th") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = spiritual.ninthHouseDharma.dharmaPath)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = spiritual.ninthHouseDharma.guruConnection)
        }
    }
    
    ExpandableAnalysisCard(
        title = "${stringResource(StringKeyDeepSpiritual.TWELFTH_SIGN)}: ${spiritual.twelfthHouseMoksha.sign.localizedName()}",
        subtitle = stringResource(StringKeyDeepSpiritual.LIBERATION_PATH),
        strength = spiritual.twelfthHouseMoksha.houseStrength,
        isExpanded = "spirit_12th" in expandedCards,
        onToggle = { onToggleCard("spirit_12th") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = spiritual.twelfthHouseMoksha.liberationPath)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = spiritual.twelfthHouseMoksha.meditationInclination)
        }
    }
    
    ExpandableAnalysisCard(
        title = stringResource(StringKeyDeepSpiritual.SECTION_KETU),
        subtitle = stringResource(StringKeyDeepSpiritual.PAST_LIFE_KARMA),
        isExpanded = "spirit_ketu" in expandedCards,
        onToggle = { onToggleCard("spirit_ketu") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = spiritual.ketuAnalysis.pastLifeKarma)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = spiritual.ketuAnalysis.spiritualTalents)
        }
    }
    
    if (spiritual.karmicPatterns.isNotEmpty()) {
        DeepSectionHeader(title = stringResource(StringKeyDeepSpiritual.KARMIC_TITLE), icon = Icons.Default.Loop)
        Column {
            for (pattern in spiritual.karmicPatterns) {
                val language = com.astro.storm.data.localization.LocalLanguage.current
                val patternName = if (language == com.astro.storm.core.common.Language.NEPALI) {
                    // This pattern name should ideally be localized in the model
                    pattern.patternName
                } else {
                    pattern.patternName
                }
                
                ExpandableAnalysisCard(
                    title = patternName,
                    isExpanded = "spirit_karma_${pattern.patternName}" in expandedCards,
                    onToggle = { onToggleCard("spirit_karma_${pattern.patternName}") }
                ) {
                    Column {
                        LocalizedParagraphText(paragraph = pattern.karmicLesson)
                        Spacer(modifier = Modifier.height(8.dp))
                        LocalizedParagraphText(paragraph = pattern.lessonToLearn)
                    }
                }
            }
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    LocalizedParagraphText(paragraph = spiritual.spiritualSummary)
}

@Composable
private fun RecommendationItem(
    label: String,
    text: LocalizedParagraph,
    icon: ImageVector
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppTheme.AccentPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LocalizedParagraphText(
            paragraph = text,
            modifier = Modifier.padding(start = 28.dp)
        )
    }
}

@Composable
private fun LocalizedTraitText(
    trait: LocalizedTrait,
    useNepali: Boolean? = null,
    modifier: Modifier = Modifier
) {
    val language = com.astro.storm.data.localization.LocalLanguage.current
    val shouldShowNepali = useNepali ?: (language == com.astro.storm.core.common.Language.NEPALI)
    
    Text(
        text = if (shouldShowNepali) trait.nameNe else trait.name,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

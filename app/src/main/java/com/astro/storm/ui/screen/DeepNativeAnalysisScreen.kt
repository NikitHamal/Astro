package com.astro.storm.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.deepanalysis.*
import com.astro.storm.ui.components.deepanalysis.*
import com.astro.storm.ui.viewmodel.DeepAnalysisSection
import com.astro.storm.ui.viewmodel.DeepAnalysisUiState
import com.astro.storm.ui.viewmodel.DeepAnalysisViewModel

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
    val uiState by viewModel.uiState.collectAsState()
    val selectedSection by viewModel.selectedSection.collectAsState()
    val expandedCards by viewModel.expandedCards.collectAsState()
    
    // Trigger analysis on first composition
    LaunchedEffect(chart) {
        viewModel.calculateDeepAnalysis(chart)
    }
    
    val sections = listOf(
        DeepAnalysisSection.OVERVIEW to ("Overview" to Icons.Default.Dashboard),
        DeepAnalysisSection.CHARACTER to ("Character" to Icons.Default.Person),
        DeepAnalysisSection.CAREER to ("Career" to Icons.Default.Work),
        DeepAnalysisSection.RELATIONSHIP to ("Relationship" to Icons.Default.Favorite),
        DeepAnalysisSection.HEALTH to ("Health" to Icons.Default.HealthAndSafety),
        DeepAnalysisSection.WEALTH to ("Wealth" to Icons.Default.AttachMoney),
        DeepAnalysisSection.EDUCATION to ("Education" to Icons.Default.School),
        DeepAnalysisSection.SPIRITUAL to ("Spiritual" to Icons.Default.SelfImprovement)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Deep Analysis",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Section tabs
            SectionTabRow(
                sections = sections.map { it.second },
                selectedIndex = sections.indexOfFirst { it.first == selectedSection },
                onSelect = { index ->
                    viewModel.selectSection(sections[index].first)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
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
}

@Composable
private fun DeepAnalysisContent(
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
        title = "Life Analysis Overview",
        scores = listOf(
            "Character" to analysis.character.personalityStrengthScore,
            "Career" to analysis.career.careerStrengthScore,
            "Relationship" to analysis.relationship.relationshipStrengthScore,
            "Health" to analysis.health.healthStrengthScore,
            "Wealth" to analysis.wealth.wealthStrengthScore,
            "Education" to analysis.education.educationStrengthScore,
            "Spiritual" to analysis.spiritual.spiritualStrengthScore
        ),
        summary = analysis.character.personalitySummary
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Quick highlights
    DeepSectionHeader(title = "Key Strengths", icon = Icons.Default.Star)
    
    ExpandableAnalysisCard(
        title = "Primary Life Themes",
        subtitle = "Based on your complete chart analysis",
        isExpanded = "overview_themes" in expandedCards,
        onToggle = { onToggleCard("overview_themes") }
    ) {
        Column {
            Text(
                text = "Your chart reveals unique patterns across all life areas. " +
                    "Explore each section for detailed insights.",
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
    ScoreIndicator(
        score = character.personalityStrengthScore,
        label = "Personality Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "Ascendant Analysis", icon = Icons.Default.Person)
    
    ExpandableAnalysisCard(
        title = "Rising Sign: ${character.ascendantAnalysis.sign.displayName}",
        subtitle = "Your outer personality and first impression",
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
        title = "Moon Sign: ${character.moonAnalysis.moonSign.displayName}",
        subtitle = "Your emotional nature and inner self",
        strength = character.moonAnalysis.overallEmotionalStrength,
        isExpanded = "char_moon" in expandedCards,
        onToggle = { onToggleCard("char_moon") }
    ) {
        Column {
            Text(
                text = "Nakshatra: ${character.moonAnalysis.nakshatra.name} (Pada ${character.moonAnalysis.nakshatraPada})",
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
        title = "Sun Sign: ${character.sunAnalysis.sunSign.displayName}",
        subtitle = "Your core identity and ego expression",
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
        DeepSectionHeader(title = "Key Personality Traits", icon = Icons.Default.Psychology)
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
    ScoreIndicator(
        score = career.careerStrengthScore,
        label = "Career Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "10th House Analysis", icon = Icons.Default.Work)
    
    ExpandableAnalysisCard(
        title = "10th House: ${career.tenthHouseAnalysis.sign.displayName}",
        subtitle = "Your professional environment and public image",
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
        title = "10th Lord: ${career.tenthLordAnalysis.lord.displayName}",
        subtitle = "House ${career.tenthLordAnalysis.housePosition} placement",
        strength = career.tenthLordAnalysis.strengthLevel,
        isExpanded = "career_lord" in expandedCards,
        onToggle = { onToggleCard("career_lord") }
    ) {
        LocalizedParagraphText(paragraph = career.tenthLordAnalysis.interpretation)
    }
    
    if (career.suitableProfessions.isNotEmpty()) {
        DeepSectionHeader(title = "Suitable Professions", icon = Icons.Default.BusinessCenter)
        Column {
            career.suitableProfessions.forEach { profession ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = profession.professionName)
                    StrengthBadge(strength = profession.suitability)
                }
            }
        }
    }
    
    ExpandableAnalysisCard(
        title = "Work Style Profile",
        subtitle = "Your preferred working patterns",
        isExpanded = "career_style" in expandedCards,
        onToggle = { onToggleCard("career_style") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = career.workStyle.preferredEnvironment)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = career.workStyle.leadershipStyle)
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
    ScoreIndicator(
        score = relationship.relationshipStrengthScore,
        label = "Relationship Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "7th House Analysis", icon = Icons.Default.Favorite)
    
    ExpandableAnalysisCard(
        title = "7th House: ${relationship.seventhHouseAnalysis.sign.displayName}",
        subtitle = "Your partnership dynamics",
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
        title = "Venus Analysis",
        subtitle = "Venus in ${relationship.venusAnalysis.sign.displayName}",
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
        title = "Partner Profile",
        subtitle = "Indicated partner characteristics",
        isExpanded = "rel_partner" in expandedCards,
        onToggle = { onToggleCard("rel_partner") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = relationship.partnerProfile.physicalDescription)
            Spacer(modifier = Modifier.height(8.dp))
            LocalizedParagraphText(paragraph = relationship.partnerProfile.professionIndicators)
        }
    }
    
    DeepSectionHeader(title = "Marriage Timing", icon = Icons.Default.CalendarMonth)
    
    ExpandableAnalysisCard(
        title = "Timing: ${relationship.marriageTiming.timingCategory.name}",
        subtitle = "Estimated age: ${relationship.marriageTiming.estimatedAgeRange}",
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
    ScoreIndicator(
        score = health.healthStrengthScore,
        label = "Health Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "Constitution", icon = Icons.Default.HealthAndSafety)
    
    ExpandableAnalysisCard(
        title = "Dosha: ${health.constitutionAnalysis.primaryDosha.name}",
        subtitle = "Your Ayurvedic constitution",
        isExpanded = "health_dosha" in expandedCards,
        onToggle = { onToggleCard("health_dosha") }
    ) {
        Column {
            LocalizedParagraphText(paragraph = health.constitutionAnalysis.constitutionDescription)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Dietary Recommendations", fontWeight = FontWeight.Medium)
            LocalizedParagraphText(paragraph = health.constitutionAnalysis.dietaryRecommendations)
        }
    }
    
    ExpandableAnalysisCard(
        title = "Vitality Profile",
        subtitle = "Your innate health strength",
        strength = health.ascendantHealthProfile.generalVitality,
        isExpanded = "health_vitality" in expandedCards,
        onToggle = { onToggleCard("health_vitality") }
    ) {
        LocalizedParagraphText(paragraph = health.ascendantHealthProfile.physicalConstitutionType)
    }
    
    if (health.healthStrengths.isNotEmpty()) {
        DeepSectionHeader(title = "Health Strengths", icon = Icons.Default.Favorite)
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
    ScoreIndicator(
        score = wealth.wealthStrengthScore,
        label = "Wealth Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "Wealth Houses", icon = Icons.Default.AttachMoney)
    
    ExpandableAnalysisCard(
        title = "2nd House: ${wealth.secondHouseAnalysis.sign.displayName}",
        subtitle = "Wealth accumulation and savings",
        strength = wealth.secondHouseAnalysis.houseStrength,
        isExpanded = "wealth_2nd" in expandedCards,
        onToggle = { onToggleCard("wealth_2nd") }
    ) {
        LocalizedParagraphText(paragraph = wealth.secondHouseAnalysis.accumulationPattern)
    }
    
    ExpandableAnalysisCard(
        title = "11th House: ${wealth.eleventhHouseAnalysis.sign.displayName}",
        subtitle = "Income and gains",
        strength = wealth.eleventhHouseAnalysis.houseStrength,
        isExpanded = "wealth_11th" in expandedCards,
        onToggle = { onToggleCard("wealth_11th") }
    ) {
        LocalizedParagraphText(paragraph = wealth.eleventhHouseAnalysis.gainsPattern)
    }
    
    if (wealth.dhanaYogaAnalysis.presentYogas.isNotEmpty()) {
        DeepSectionHeader(title = "Dhana Yogas", icon = Icons.Default.Star)
        Column {
            wealth.dhanaYogaAnalysis.presentYogas.forEach { yoga ->
                TimelinePeriodCard(
                    title = yoga.name,
                    dateRange = "Active during related dasha",
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
    ScoreIndicator(
        score = education.educationStrengthScore,
        label = "Education Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "Learning Profile", icon = Icons.Default.School)
    
    ExpandableAnalysisCard(
        title = "5th House Intelligence",
        subtitle = "Your intellectual capacity",
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
        title = "Mercury: ${education.mercuryAnalysis.sign.displayName}",
        subtitle = "Communication and analytical ability",
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
        DeepSectionHeader(title = "Suitable Subjects", icon = Icons.Default.MenuBook)
        Column {
            education.suitableSubjects.forEach { subject ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = subject.subjectName)
                    StrengthBadge(strength = subject.affinity)
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
    ScoreIndicator(
        score = spiritual.spiritualStrengthScore,
        label = "Spiritual Score"
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    DeepSectionHeader(title = "Dharma Path", icon = Icons.Default.SelfImprovement)
    
    ExpandableAnalysisCard(
        title = "9th House: ${spiritual.ninthHouseDharma.sign.displayName}",
        subtitle = "Your dharmic path",
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
        title = "12th House: ${spiritual.twelfthHouseMoksha.sign.displayName}",
        subtitle = "Liberation and transcendence",
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
        title = "Ketu Analysis",
        subtitle = "Past life karma and spiritual talents",
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
        DeepSectionHeader(title = "Karmic Patterns", icon = Icons.Default.Loop)
        Column {
            spiritual.karmicPatterns.forEach { pattern ->
                ExpandableAnalysisCard(
                    title = pattern.patternName,
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

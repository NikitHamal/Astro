package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.model.Planet
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.RemediesCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Production-Grade Remedies Screen for Vedic Astrology
 *
 * Features:
 * - Planetary strength analysis
 * - Categorized remedies (Gemstones, Mantras, Charity, etc.)
 * - Priority-based recommendations
 * - Detailed remedy instructions
 * - Life area focus
 *
 * @author AstroStorm - Ultra-Precision Vedic Astrology
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemediesScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // State
    var remediesResult by remember { mutableStateOf<RemediesCalculator.RemediesResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf<RemediesCalculator.RemedyCategory?>(null) }
    var expandedRemedyIndex by remember { mutableStateOf<Int?>(null) }

    val tabs = listOf("Overview", "Remedies", "Planets")

    // Calculate remedies
    LaunchedEffect(chart) {
        if (chart != null) {
            isLoading = true
            withContext(Dispatchers.Default) {
                remediesResult = RemediesCalculator.calculateRemedies(chart)
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Remedies",
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
    ) { paddingValues ->
        if (chart == null) {
            EmptyState()
            return@Scaffold
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = AppTheme.AccentPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Analyzing your chart...",
                        color = AppTheme.TextMuted
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = AppTheme.AccentPrimary,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) AppTheme.AccentPrimary else AppTheme.TextMuted
                            )
                        }
                    )
                }
            }

            remediesResult?.let { result ->
                when (selectedTab) {
                    0 -> OverviewTab(result)
                    1 -> RemediesTab(
                        result = result,
                        selectedCategory = selectedCategory,
                        onCategoryChange = { selectedCategory = it },
                        expandedIndex = expandedRemedyIndex,
                        onExpandChange = { expandedRemedyIndex = if (expandedRemedyIndex == it) null else it }
                    )
                    2 -> PlanetsTab(result)
                }
            }
        }
    }
}

@Composable
private fun OverviewTab(result: RemediesCalculator.RemediesResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Summary Card
        item {
            SummaryCard(result)
        }

        // Weak Planets
        if (result.weakestPlanets.isNotEmpty()) {
            item {
                WeakPlanetsCard(result.weakestPlanets)
            }
        }

        // Essential Remedies Preview
        item {
            EssentialRemediesPreview(result)
        }

        // Life Area Focus
        if (result.lifeAreaFocus.isNotEmpty()) {
            item {
                LifeAreaFocusCard(result.lifeAreaFocus)
            }
        }

        // General Recommendations
        if (result.generalRecommendations.isNotEmpty()) {
            item {
                GeneralRecommendationsCard(result.generalRecommendations)
            }
        }
    }
}

@Composable
private fun SummaryCard(result: RemediesCalculator.RemediesResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Remedies Analysis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        result.chart.birthData.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }

                // Stats
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatItem(
                        value = "${result.totalRemediesCount}",
                        label = "Total"
                    )
                    StatItem(
                        value = "${result.essentialRemediesCount}",
                        label = "Essential",
                        color = AppTheme.WarningColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AppTheme.DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                result.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color = AppTheme.AccentPrimary
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun WeakPlanetsCard(weakPlanets: List<Planet>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = AppTheme.WarningColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Planets Requiring Attention",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(weakPlanets) { planet ->
                    PlanetChip(planet)
                }
            }
        }
    }
}

@Composable
private fun PlanetChip(planet: Planet) {
    Surface(
        color = getPlanetColor(planet).copy(alpha = 0.15f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(getPlanetColor(planet).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    planet.symbol,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = getPlanetColor(planet)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                planet.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = getPlanetColor(planet)
            )
        }
    }
}

@Composable
private fun EssentialRemediesPreview(result: RemediesCalculator.RemediesResult) {
    val essentialRemedies = result.prioritizedRemedies
        .filter { it.priority == RemediesCalculator.RemedyPriority.ESSENTIAL }
        .take(3)

    if (essentialRemedies.isEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Essential Remedies",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            essentialRemedies.forEach { remedy ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        getCategoryIcon(remedy.category),
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            remedy.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            remedy.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.TextMuted
                        )
                    }
                    remedy.planet?.let { planet ->
                        Text(
                            planet.symbol,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = getPlanetColor(planet)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LifeAreaFocusCard(lifeAreaFocus: Map<String, List<RemediesCalculator.Remedy>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Life Area Focus",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            lifeAreaFocus.forEach { (area, remedies) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            getLifeAreaIcon(area),
                            contentDescription = null,
                            tint = getLifeAreaColor(area),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            area,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Surface(
                        color = getLifeAreaColor(area).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "${remedies.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = getLifeAreaColor(area),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneralRecommendationsCard(recommendations: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.InfoColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = AppTheme.InfoColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "General Recommendations",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            recommendations.forEach { rec ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("•", color = AppTheme.InfoColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        rec,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun RemediesTab(
    result: RemediesCalculator.RemediesResult,
    selectedCategory: RemediesCalculator.RemedyCategory?,
    onCategoryChange: (RemediesCalculator.RemedyCategory?) -> Unit,
    expandedIndex: Int?,
    onExpandChange: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Category filter
        item {
            CategoryFilter(
                selectedCategory = selectedCategory,
                onCategoryChange = onCategoryChange
            )
        }

        // Filtered remedies
        val filteredRemedies = if (selectedCategory == null) {
            result.prioritizedRemedies
        } else {
            result.remedies.filter { it.category == selectedCategory }
        }

        items(filteredRemedies.size) { index ->
            val remedy = filteredRemedies[index]
            RemedyCard(
                remedy = remedy,
                index = index,
                isExpanded = expandedIndex == index,
                onExpand = { onExpandChange(index) }
            )
        }
    }
}

@Composable
private fun CategoryFilter(
    selectedCategory: RemediesCalculator.RemedyCategory?,
    onCategoryChange: (RemediesCalculator.RemedyCategory?) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategoryChange(null) },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.ChipBackground,
                    labelColor = AppTheme.TextSecondary
                )
            )
        }

        items(RemediesCalculator.RemedyCategory.entries.toList()) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategoryChange(category) },
                label = { Text(category.displayName) },
                leadingIcon = {
                    Icon(
                        getCategoryIcon(category),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppTheme.AccentPrimary.copy(alpha = 0.2f),
                    selectedLabelColor = AppTheme.AccentPrimary,
                    selectedLeadingIconColor = AppTheme.AccentPrimary,
                    containerColor = AppTheme.ChipBackground,
                    labelColor = AppTheme.TextSecondary
                )
            )
        }
    }
}

@Composable
private fun RemedyCard(
    remedy: RemediesCalculator.Remedy,
    index: Int,
    isExpanded: Boolean,
    onExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onExpand),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(getCategoryColor(remedy.category).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            getCategoryIcon(remedy.category),
                            contentDescription = null,
                            tint = getCategoryColor(remedy.category),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            remedy.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                remedy.category.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                            remedy.planet?.let { planet ->
                                Text(" • ", color = AppTheme.TextMuted)
                                Text(
                                    planet.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = getPlanetColor(planet)
                                )
                            }
                        }
                    }
                }

                // Priority badge
                Surface(
                    color = getPriorityColor(remedy.priority).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        remedy.priority.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = getPriorityColor(remedy.priority),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = AppTheme.DividerColor)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        remedy.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Method
                    DetailSection(
                        icon = Icons.Outlined.MenuBook,
                        title = "Method",
                        content = remedy.method
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Timing
                    DetailSection(
                        icon = Icons.Outlined.Schedule,
                        title = "Timing",
                        content = remedy.timing
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Duration
                    DetailSection(
                        icon = Icons.Outlined.DateRange,
                        title = "Duration",
                        content = remedy.duration
                    )

                    // Benefits
                    if (remedy.benefits.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Benefits",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        remedy.benefits.forEach { benefit ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = AppTheme.SuccessColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    benefit,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }

                    // Cautions
                    if (remedy.cautions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Cautions",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.WarningColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        remedy.cautions.forEach { caution ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Icon(
                                    Icons.Filled.Warning,
                                    contentDescription = null,
                                    tint = AppTheme.WarningColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    caution,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Expand indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = AppTheme.TextSubtle,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun DetailSection(
    icon: ImageVector,
    title: String,
    content: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            icon,
            contentDescription = null,
            tint = AppTheme.AccentPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Text(
                content,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun PlanetsTab(result: RemediesCalculator.RemediesResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(result.planetaryAnalyses) { analysis ->
            PlanetAnalysisCard(analysis)
        }
    }
}

@Composable
private fun PlanetAnalysisCard(analysis: RemediesCalculator.PlanetaryAnalysis) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                            .background(getPlanetColor(analysis.planet).copy(alpha = 0.15f))
                            .border(
                                width = 2.dp,
                                color = getPlanetColor(analysis.planet),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            analysis.planet.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = getPlanetColor(analysis.planet)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            analysis.planet.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            "${analysis.sign.displayName} • House ${analysis.housePosition}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                // Strength badge
                Surface(
                    color = getStrengthColor(analysis.strength).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "${analysis.strengthScore}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = getStrengthColor(analysis.strength)
                        )
                        Text(
                            analysis.strength.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = getStrengthColor(analysis.strength)
                        )
                    }
                }
            }

            // Status indicators
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (analysis.isExalted) {
                    StatusChip("Exalted", AppTheme.SuccessColor)
                }
                if (analysis.isDebilitated) {
                    StatusChip("Debilitated", AppTheme.ErrorColor)
                }
                if (analysis.isRetrograde) {
                    StatusChip("Retrograde", AppTheme.WarningColor)
                }
                if (analysis.isCombust) {
                    StatusChip("Combust", AppTheme.ErrorColor)
                }
            }

            // Issues
            if (analysis.issues.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                analysis.issues.forEach { issue ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = AppTheme.WarningColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            issue,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.WarningColor
                        )
                    }
                }
            }

            // Positives
            if (analysis.positives.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                analysis.positives.forEach { positive ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = AppTheme.SuccessColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            positive,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.SuccessColor
                        )
                    }
                }
            }

            // Needs remedy indicator
            if (analysis.needsRemedy) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = AppTheme.AccentPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Spa,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Remedies recommended for this planet",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.Spa,
                contentDescription = null,
                tint = AppTheme.TextSubtle,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No chart selected",
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextMuted
            )
            Text(
                "Select a chart to view remedies",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSubtle
            )
        }
    }
}

// Helper functions
private fun getPlanetColor(planet: Planet): Color {
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

private fun getCategoryIcon(category: RemediesCalculator.RemedyCategory): ImageVector {
    return when (category) {
        RemediesCalculator.RemedyCategory.GEMSTONE -> Icons.Outlined.Diamond
        RemediesCalculator.RemedyCategory.MANTRA -> Icons.Outlined.Mic
        RemediesCalculator.RemedyCategory.YANTRA -> Icons.Outlined.GridView
        RemediesCalculator.RemedyCategory.CHARITY -> Icons.Outlined.VolunteerActivism
        RemediesCalculator.RemedyCategory.FASTING -> Icons.Outlined.Restaurant
        RemediesCalculator.RemedyCategory.COLOR -> Icons.Outlined.Palette
        RemediesCalculator.RemedyCategory.METAL -> Icons.Outlined.Toll
        RemediesCalculator.RemedyCategory.RUDRAKSHA -> Icons.Outlined.Spa
        RemediesCalculator.RemedyCategory.DEITY -> Icons.Outlined.TempleHindu
        RemediesCalculator.RemedyCategory.LIFESTYLE -> Icons.Outlined.FavoriteBorder
    }
}

private fun getCategoryColor(category: RemediesCalculator.RemedyCategory): Color {
    return when (category) {
        RemediesCalculator.RemedyCategory.GEMSTONE -> Color(0xFF9C27B0)
        RemediesCalculator.RemedyCategory.MANTRA -> Color(0xFFFF9800)
        RemediesCalculator.RemedyCategory.YANTRA -> Color(0xFF2196F3)
        RemediesCalculator.RemedyCategory.CHARITY -> Color(0xFF4CAF50)
        RemediesCalculator.RemedyCategory.FASTING -> Color(0xFFE91E63)
        RemediesCalculator.RemedyCategory.COLOR -> Color(0xFF00BCD4)
        RemediesCalculator.RemedyCategory.METAL -> Color(0xFF607D8B)
        RemediesCalculator.RemedyCategory.RUDRAKSHA -> Color(0xFF795548)
        RemediesCalculator.RemedyCategory.DEITY -> Color(0xFFFF5722)
        RemediesCalculator.RemedyCategory.LIFESTYLE -> Color(0xFF8BC34A)
    }
}

private fun getPriorityColor(priority: RemediesCalculator.RemedyPriority): Color {
    return when (priority) {
        RemediesCalculator.RemedyPriority.ESSENTIAL -> Color(0xFFF44336)
        RemediesCalculator.RemedyPriority.HIGHLY_RECOMMENDED -> Color(0xFFFF9800)
        RemediesCalculator.RemedyPriority.RECOMMENDED -> Color(0xFF4CAF50)
        RemediesCalculator.RemedyPriority.OPTIONAL -> AppTheme.TextMuted
    }
}

private fun getStrengthColor(strength: RemediesCalculator.PlanetaryStrength): Color {
    return when (strength) {
        RemediesCalculator.PlanetaryStrength.VERY_STRONG -> Color(0xFF4CAF50)
        RemediesCalculator.PlanetaryStrength.STRONG -> Color(0xFF8BC34A)
        RemediesCalculator.PlanetaryStrength.MODERATE -> Color(0xFFFFC107)
        RemediesCalculator.PlanetaryStrength.WEAK -> Color(0xFFFF9800)
        RemediesCalculator.PlanetaryStrength.VERY_WEAK -> Color(0xFFF44336)
        RemediesCalculator.PlanetaryStrength.AFFLICTED -> Color(0xFF9C27B0)
    }
}

private fun getLifeAreaIcon(area: String): ImageVector {
    return when (area) {
        "Career" -> Icons.Outlined.Work
        "Relationships" -> Icons.Outlined.Favorite
        "Health" -> Icons.Outlined.Healing
        "Wealth" -> Icons.Outlined.AttachMoney
        "Spiritual" -> Icons.Outlined.SelfImprovement
        else -> Icons.Outlined.Star
    }
}

private fun getLifeAreaColor(area: String): Color {
    return when (area) {
        "Career" -> AppTheme.LifeAreaCareer
        "Relationships" -> AppTheme.LifeAreaLove
        "Health" -> AppTheme.LifeAreaHealth
        "Wealth" -> AppTheme.LifeAreaFinance
        "Spiritual" -> AppTheme.LifeAreaSpiritual
        else -> AppTheme.AccentPrimary
    }
}

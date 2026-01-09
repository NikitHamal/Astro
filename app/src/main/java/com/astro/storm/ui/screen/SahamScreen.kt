package com.astro.storm.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKeySaham
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.SahamCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Saham Screen (Arabic Parts) - Production Grade with Full Bilingual Support
 *
 * Advanced analysis of sensitive points with:
 * - Full EN/NE localization
 * - Search and filtering capabilities
 * - Detailed visualization
 * - Info dialog with educational content
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SahamScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current
    var isCalculating by remember { mutableStateOf(true) }
    var analysis by remember { mutableStateOf<SahamCalculator.SahamAnalysis?>(null) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var activeFilter by rememberSaveable { mutableStateOf(SahamFilter.ALL) }
    var showInfoDialog by remember { mutableStateOf(false) }

    val filteredSahams by remember(analysis, searchQuery, activeFilter) {
        derivedStateOf {
            if (analysis == null) emptyList()
            else {
                var list = analysis!!.sahams

                list = when (activeFilter) {
                    SahamFilter.ALL -> list
                    SahamFilter.MAJOR -> list.filter { it.type.category == SahamCalculator.SahamCategory.MAJOR }
                    SahamFilter.MINOR -> list.filter { it.type.category == SahamCalculator.SahamCategory.MINOR }
                    SahamFilter.STRONG -> list.filter {
                        it.strength == SahamCalculator.SahamStrength.EXCELLENT ||
                        it.strength == SahamCalculator.SahamStrength.STRONG
                    }
                    SahamFilter.WEAK -> list.filter {
                        it.strength == SahamCalculator.SahamStrength.WEAK ||
                        it.strength == SahamCalculator.SahamStrength.AFFLICTED
                    }
                    SahamFilter.ACTIVATED -> list.filter { it.isActivated }
                }

                if (searchQuery.isNotEmpty()) {
                    list = list.filter {
                        it.type.displayName.contains(searchQuery, ignoreCase = true) ||
                        it.type.name.contains(searchQuery, ignoreCase = true)
                    }
                }

                list.sortedWith(
                    compareByDescending<SahamCalculator.SahamResult> {
                        it.type.category == SahamCalculator.SahamCategory.MAJOR
                    }.thenByDescending { strengthToValue(it.strength) }
                )
            }
        }
    }

    LaunchedEffect(chart) {
        if (chart == null) return@LaunchedEffect
        isCalculating = true
        try {
            analysis = withContext(Dispatchers.Default) {
                SahamCalculator.analyzeSahams(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    if (showInfoDialog) {
        SahamInfoDialog(
            onDismiss = { showInfoDialog = false }
        )
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeySaham.TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        if (analysis != null) {
                            Text(
                                text = if (analysis!!.isDayBirth) {
                                    stringResource(StringKeySaham.DAY_BIRTH_CHART)
                                } else {
                                    stringResource(StringKeySaham.NIGHT_BIRTH_CHART)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKeySaham.SHOW_INFO),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeySaham.SHOW_INFO),
                            tint = AppTheme.TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.ScreenBackground)
            )
        }
    ) { paddingValues ->
        when {
            chart == null -> {
                SahamEmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            isCalculating -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = AppTheme.AccentGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(StringKeySaham.CALCULATING),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppTheme.TextMuted
                        )
                    }
                }
            }
            analysis != null -> {
                SahamContent(
                    analysis = analysis!!,
                    filteredSahams = filteredSahams,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    activeFilter = activeFilter,
                    onFilterChange = { activeFilter = it },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SahamContent(
    analysis: SahamCalculator.SahamAnalysis,
    filteredSahams: List<SahamCalculator.SahamResult>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    activeFilter: SahamFilter,
    onFilterChange: (SahamFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item(key = "dashboard") {
            SahamDashboard(analysis = analysis)
        }

        stickyHeader(key = "filter_header") {
            SahamFilterBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                activeFilter = activeFilter,
                onFilterChange = onFilterChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.ScreenBackground.copy(alpha = 0.95f))
                    .padding(vertical = 8.dp)
            )
        }

        if (filteredSahams.isEmpty()) {
            item(key = "empty_results") {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(StringKeySaham.NO_SAHAMS_FOUND),
                        color = AppTheme.TextMuted
                    )
                }
            }
        } else {
            items(filteredSahams, key = { it.type.name }) { saham ->
                SahamDetailCard(
                    saham = saham,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun SahamEmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(StringKeySaham.NO_CHART_SELECTED),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeySaham.SELECT_CHART_MESSAGE),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SahamDashboard(analysis: SahamCalculator.SahamAnalysis) {
    val language = LocalLanguage.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.6f),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { (analysis.overallScore / 100).toFloat() },
                            modifier = Modifier.size(60.dp),
                            color = getStrengthColor(analysis.overallScore),
                            strokeWidth = 6.dp,
                            trackColor = AppTheme.DividerColor.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "${analysis.overallScore.toInt()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(StringKeySaham.CHART_STRENGTH),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.6f),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.AccentGold.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (analysis.isDayBirth) {
                            Icons.Default.LightMode
                        } else {
                            Icons.Default.NightlightRound
                        },
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (analysis.isDayBirth) {
                            stringResource(StringKeySaham.DAY_BIRTH)
                        } else {
                            stringResource(StringKeySaham.NIGHT_BIRTH)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.AccentGold
                    )
                    Text(
                        text = stringResource(StringKeySaham.FORMULA_ADJUSTED),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentGold.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(StringKeySaham.LIFE_AREA_BALANCE),
            style = MaterialTheme.typography.titleSmall,
            color = AppTheme.TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                analysis.categorySummary.take(2).forEach { cat ->
                    SahamCategoryBar(summary = cat)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Calculate,
                        contentDescription = null,
                        tint = AppTheme.SuccessColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${analysis.sahams.count { it.isActivated }} ${stringResource(StringKeySaham.ACTIVE_SAHAMS_TODAY)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun SahamCategoryBar(summary: SahamCalculator.CategorySummary) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = summary.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(summary.avgStrength * 20).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = getStrengthColor(summary.avgStrength * 20),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { (summary.avgStrength / 5.0).toFloat().coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = getStrengthColor(summary.avgStrength * 20),
            trackColor = AppTheme.DividerColor.copy(alpha = 0.3f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SahamFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    activeFilter: SahamFilter,
    onFilterChange: (SahamFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current

    Column(modifier = modifier) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            placeholder = {
                Text(
                    text = stringResource(StringKeySaham.SEARCH_SAHAMS),
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = AppTheme.TextMuted)
            },
            trailingIcon = if (query.isNotEmpty()) {
                {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            } else null,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AppTheme.CardBackground,
                unfocusedContainerColor = AppTheme.CardBackground,
                focusedBorderColor = AppTheme.AccentGold,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AppTheme.AccentGold
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SahamFilter.entries) { filter ->
                FilterChip(
                    selected = activeFilter == filter,
                    onClick = { onFilterChange(filter) },
                    label = { Text(filter.getLocalizedLabel(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.AccentGold,
                        selectedLabelColor = AppTheme.ChipBackgroundSelected,
                        containerColor = AppTheme.CardBackground,
                        labelColor = AppTheme.TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = activeFilter == filter,
                        borderColor = if (activeFilter == filter) {
                            Color.Transparent
                        } else {
                            AppTheme.DividerColor
                        }
                    )
                )
            }
        }
    }
}

enum class SahamFilter {
    ALL, MAJOR, STRONG, ACTIVATED, WEAK, MINOR;

    fun getLocalizedLabel(language: Language): String {
        return when (this) {
            ALL -> if (language == Language.NEPALI) "सबै" else "All"
            MAJOR -> if (language == Language.NEPALI) "मुख्य" else "Major"
            STRONG -> if (language == Language.NEPALI) "बलियो" else "Strong"
            ACTIVATED -> if (language == Language.NEPALI) "सक्रिय" else "Active"
            WEAK -> if (language == Language.NEPALI) "कमजोर" else "Weak"
            MINOR -> if (language == Language.NEPALI) "सानो" else "Minor"
        }
    }
}

@Composable
private fun SahamDetailCard(
    saham: SahamCalculator.SahamResult,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val strengthColor = getStrengthColor(saham.strength)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 4.dp else 1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(strengthColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = saham.type.displayName.take(1),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = strengthColor
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = saham.type.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        if (saham.isActivated) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AppTheme.SuccessColor.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = stringResource(StringKeySaham.ACTIVE_BADGE),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.SuccessColor
                                )
                            }
                        }
                    }
                    Text(
                        text = saham.type.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }

            if (expanded) {
                SahamExpandedContent(saham = saham, strengthColor = strengthColor)
            }
        }
    }
}

@Composable
private fun SahamExpandedContent(
    saham: SahamCalculator.SahamResult,
    strengthColor: Color
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatItem(
                label = stringResource(StringKeySaham.SIGN_LABEL),
                value = saham.sign.displayName
            )
            StatItem(
                label = stringResource(StringKeySaham.HOUSE_LABEL),
                value = "${saham.house} (${getHouseType(saham.house)})"
            )
            StatItem(
                label = stringResource(StringKeySaham.LORD_LABEL),
                value = saham.lord.displayName
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatItem(
                label = stringResource(StringKeySaham.DEGREE_LABEL),
                value = "${String.format("%.2f", saham.degreeInSign)}°"
            )
            StatItem(
                label = stringResource(StringKeySaham.NAKSHATRA_LABEL),
                value = "${saham.nakshatra.displayName} (${saham.nakshatraPada})"
            )
            StatItem(
                label = stringResource(StringKeySaham.STRENGTH_LABEL),
                value = saham.strength.displayName,
                color = strengthColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(AppTheme.ScreenBackground)
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = stringResource(StringKeySaham.FORMULA_LABEL),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = saham.formula,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    color = AppTheme.TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(StringKeySaham.ANALYSIS_LABEL),
            style = MaterialTheme.typography.labelMedium,
            color = AppTheme.AccentGold,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = saham.interpretation,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.TextSecondary,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color = AppTheme.TextPrimary
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted,
            fontSize = 10.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SahamInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeySaham.INFO_TITLE),
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(StringKeySaham.INFO_DESCRIPTION),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(StringKeySaham.INFO_DAY_NIGHT),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeySaham.INFO_ACTIVATION),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(StringKeySaham.INFO_FORMULA),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(StringKeySaham.GOT_IT),
                    color = AppTheme.AccentGold
                )
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

private fun getStrengthColor(score: Double): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> Color(0xFF8BC34A)
        score >= 40 -> Color(0xFFFFC107)
        score >= 20 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}

private fun getStrengthColor(strength: SahamCalculator.SahamStrength): Color {
    return when (strength) {
        SahamCalculator.SahamStrength.EXCELLENT -> Color(0xFF4CAF50)
        SahamCalculator.SahamStrength.STRONG -> Color(0xFF8BC34A)
        SahamCalculator.SahamStrength.MODERATE -> Color(0xFFFFC107)
        SahamCalculator.SahamStrength.WEAK -> Color(0xFFFF9800)
        SahamCalculator.SahamStrength.AFFLICTED -> Color(0xFFF44336)
    }
}

private fun getHouseType(house: Int): String {
    return when (house) {
        1, 4, 7, 10 -> "Kendra"
        5, 9 -> "Trikona"
        3, 6, 11 -> "Upachaya"
        2, 8, 12 -> "Dusthana"
        else -> ""
    }
}

private fun strengthToValue(strength: SahamCalculator.SahamStrength): Int {
    return when (strength) {
        SahamCalculator.SahamStrength.EXCELLENT -> 5
        SahamCalculator.SahamStrength.STRONG -> 4
        SahamCalculator.SahamStrength.MODERATE -> 3
        SahamCalculator.SahamStrength.WEAK -> 2
        SahamCalculator.SahamStrength.AFFLICTED -> 1
    }
}

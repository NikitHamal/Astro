package com.astro.storm.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKeyShadbala
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ephemeris.SahamCalculator
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Saham Screen (Arabic Parts) - Production Grade
 * Advanced analysis of sensitive points with filtering, search, and detailed visualization.
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
    
    // Derived state for filtered list
    val filteredSahams by remember(analysis, searchQuery, activeFilter) {
        derivedStateOf {
            if (analysis == null) emptyList()
            else {
                var list = analysis!!.sahams
                
                // Apply Category Filter
                list = when (activeFilter) {
                    SahamFilter.ALL -> list
                    SahamFilter.MAJOR -> list.filter { it.type.category == SahamCalculator.SahamCategory.MAJOR }
                    SahamFilter.MINOR -> list.filter { it.type.category == SahamCalculator.SahamCategory.MINOR }
                    SahamFilter.STRONG -> list.filter { it.strength == SahamCalculator.SahamStrength.EXCELLENT || it.strength == SahamCalculator.SahamStrength.STRONG }
                    SahamFilter.WEAK -> list.filter { it.strength == SahamCalculator.SahamStrength.WEAK || it.strength == SahamCalculator.SahamStrength.AFFLICTED }
                    SahamFilter.ACTIVATED -> list.filter { it.isActivated }
                }

                // Apply Search
                if (searchQuery.isNotEmpty()) {
                    list = list.filter { 
                        it.type.displayName.contains(searchQuery, ignoreCase = true) || 
                        it.type.name.contains(searchQuery, ignoreCase = true) 
                    }
                }
                
                // Default sort: Major first, then by strength desc
                list.sortedWith(
                    compareByDescending<SahamCalculator.SahamResult> { it.type.category == SahamCalculator.SahamCategory.MAJOR }
                        .thenByDescending { strengthToValue(it.strength) }
                )
            }
        }
    }

    LaunchedEffect(chart) {
        if (chart == null) return@LaunchedEffect
        isCalculating = true
        // Simulate slight computation time for smooth transition or actually run heavy calc
        try {
            analysis = withContext(Dispatchers.Default) {
                SahamCalculator.analyzeSahams(chart)
            }
        } catch (_: Exception) { }
        isCalculating = false
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(StringKeyShadbala.SAHAM_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.TextPrimary
                        )
                        if (analysis != null) {
                            Text(
                                text = if (analysis!!.isDayBirth) "Day Birth Chart" else "Night Birth Chart",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = AppTheme.TextPrimary)
                    }
                },
                actions = {
                    // Info Button
                    IconButton(onClick = { /* TODO: Show Info Dialog */ }) {
                        Icon(Icons.Outlined.Info, "Info", tint = AppTheme.TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.ScreenBackground)
            )
        }
    ) { paddingValues ->
        if (isCalculating) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppTheme.AccentGold)
            }
        } else if (analysis != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // 1. Dashboard Section
                item {
                    SahamDashboard(analysis!!)
                }

                // 2. Search & Filter Sticky Header
                stickyHeader {
                    SahamFilterBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        activeFilter = activeFilter,
                        onFilterChange = { activeFilter = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.ScreenBackground.copy(alpha = 0.95f))
                            .padding(vertical = 8.dp)
                    )
                }

                // 3. Results List
                if (filteredSahams.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No Sahams found matching criteria", color = AppTheme.TextMuted)
                        }
                    }
                } else {
                    items(filteredSahams, key = { it.type.name }) { saham ->
                        SahamDetailCard(saham = saham, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// DASHBOARD COMPONENTS
// ---------------------------------------------------------------------------

@Composable
private fun SahamDashboard(analysis: SahamCalculator.SahamAnalysis) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Top Row: Overall Score & Birth Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Score Card
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
                        text = "Chart Strength",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextSecondary
                    )
                }
            }

            // Birth Type & Summary
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1.6f),
                colors = CardDefaults.cardColors(containerColor = AppTheme.AccentGold.copy(alpha = 0.1f)),
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
                        imageVector = if (analysis.isDayBirth) Icons.Default.LightMode else Icons.Default.NightlightRound,
                        contentDescription = null,
                        tint = AppTheme.AccentGold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (analysis.isDayBirth) "Day Birth" else "Night Birth",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.AccentGold
                    )
                    Text(
                        text = "Formula Adjusted",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentGold.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Category Breakdown
        Text(
            text = "Life Area Balance",
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
                    SahamCategoryBar(cat)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                // Optional: Show activated count
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Calculate, null, tint = AppTheme.SuccessColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${analysis.sahams.count { it.isActivated }} Active Sahams today",
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


// ---------------------------------------------------------------------------
// FILTER & SEARCH
// ---------------------------------------------------------------------------

@Composable
private fun SahamFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    activeFilter: SahamFilter,
    onFilterChange: (SahamFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Search
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            placeholder = { Text("Search Sahams...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = AppTheme.TextMuted) },
            trailingIcon = if (query.isNotEmpty()) {
                { IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Default.Close, null) } }
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
        
        // Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SahamFilter.entries) { filter ->
                FilterChip(
                    selected = activeFilter == filter,
                    onClick = { onFilterChange(filter) },
                    label = { Text(filter.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppTheme.AccentGold,
                        selectedLabelColor = AppTheme.BackgroundOverDark,
                        containerColor = AppTheme.CardBackground,
                        labelColor = AppTheme.TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = activeFilter == filter,
                        borderColor = if (activeFilter == filter) Color.Transparent else AppTheme.DividerColor
                    )
                )
            }
        }
    }
}

enum class SahamFilter(val label: String) {
    ALL("All"),
    MAJOR("Major"),
    STRONG("Strong"),
    ACTIVATED("Active"),
    WEAK("Weak"),
    MINOR("Minor")
}


// ---------------------------------------------------------------------------
// DETAIL CARD
// ---------------------------------------------------------------------------

@Composable
fun SahamDetailCard(
    saham: SahamCalculator.SahamResult,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val strengthColor = getStrengthColor(saham.strength)
    val cardBg = AppTheme.CardBackground
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 4.dp else 1.dp)
    ) {
        Column {
            // Header Row (Always Visible)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Strength Badge / Icon
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
                
                // Title and Subtitle
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
                                Text("ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AppTheme.SuccessColor)
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
                
                // Expand Icon
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = AppTheme.TextMuted
                    )
                }
            }
            
            // Expanded Content
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    HorizontalDivider(color = AppTheme.DividerColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 1. Vital Stats Grid
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatItem("Sign", saham.sign.displayName)
                        StatItem("House", "${saham.house} (${getHouseType(saham.house)})")
                        StatItem("Lord", saham.lord.displayName)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                         StatItem("Degree", "${String.format("%.2f", saham.degreeInSign)}°")
                         StatItem("Nakshatra", "${saham.nakshatra.displayName} (${saham.nakshatraPada})")
                         StatItem("Strength", saham.strength.displayName, color = strengthColor)
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // 2. Formula Visualization
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppTheme.ScreenBackground)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("Formula", style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = saham.formula,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 3. Interpretation
                    Text(
                        text = "Analysis",
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
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color = AppTheme.TextPrimary
) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = AppTheme.TextMuted, fontSize = 10.sp)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = color, fontWeight = FontWeight.Medium)
    }
}

// ---------------------------------------------------------------------------
// HELPERS
// ---------------------------------------------------------------------------

private fun getStrengthColor(score: Double): Color {
    return when {
        score >= 80 -> Color(0xFF4CAF50) // Green
        score >= 60 -> Color(0xFF8BC34A) // Light Green
        score >= 40 -> Color(0xFFFFC107) // Amber
        score >= 20 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336)        // Red
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
        2, 8, 12 -> "Dusthana" // 2 is neutral/maraka but often grouped roughly
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

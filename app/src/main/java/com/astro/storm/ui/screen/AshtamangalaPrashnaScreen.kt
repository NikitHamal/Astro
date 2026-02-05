package com.astro.storm.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAshtamangala
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.data.localization.currentLanguage
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.AshtamangalaReading
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.QueryCategory
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.CowrieThrowResult
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.PositionResult
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.AshtamangalaPosition
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.IndicationStrength
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.YesNoProbability
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.TimingPrediction
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.ChartValidation
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.Remedy
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.RemedyType
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.SpecialReadings
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.ConfidenceLevel
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.SafetyLevel
import com.astro.storm.ephemeris.AshtamangalaPrashnaCalculator.ReadingInterpretation
import com.astro.storm.ui.components.common.ModernPillTabRow
import com.astro.storm.ui.components.common.TabItem
import com.astro.storm.ui.theme.AppTheme
import kotlinx.coroutines.delay

/**
 * Ashtamangala Prashna Screen
 *
 * Kerala tradition 8-fold horary divination with:
 * - Interactive cowrie shell simulation
 * - Eight directional deity positions
 * - Question category selection
 * - Comprehensive reading display
 * - Prashna chart integration
 * - Timing and remedy suggestions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshtamangalaPrashnaScreen(
    chart: VedicChart?,
    onBack: () -> Unit
) {
    val language = currentLanguage()
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf<QueryCategory?>(null) }
    var currentReading by remember { mutableStateOf<AshtamangalaReading?>(null) }
    var isThrowingShells by remember { mutableStateOf(false) }
    var showCategorySelector by remember { mutableStateOf(true) }

    val tabs = listOf(
        stringResource(StringKeyAshtamangala.TAB_QUERY),
        stringResource(StringKeyAshtamangala.TAB_ANALYSIS),
        stringResource(StringKeyAshtamangala.TAB_POSITIONS),
        stringResource(StringKeyAshtamangala.TAB_TIMING)
    )

    if (showInfoDialog) {
        AshtamangalaInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Scaffold(
        containerColor = AppTheme.ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            stringResource(StringKeyAshtamangala.SCREEN_TITLE),
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary,
                            fontSize = 18.sp
                        )
                        Text(
                            stringResource(StringKeyAshtamangala.SCREEN_SUBTITLE),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(StringKey.BTN_BACK),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                actions = {
                    if (currentReading != null) {
                        IconButton(onClick = {
                            currentReading = null
                            selectedCategory = null
                            showCategorySelector = true
                            selectedTab = 0
                        }) {
                            Icon(
                                Icons.Outlined.Refresh,
                                contentDescription = stringResource(StringKeyAshtamangala.BTN_NEW_QUERY),
                                tint = AppTheme.TextPrimary
                            )
                        }
                    }
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(StringKeyAshtamangala.INFO_TITLE),
                            tint = AppTheme.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.ScreenBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show tabs only after reading is generated
            if (currentReading != null) {
                val tabItems = tabs.map { TabItem(title = it, accentColor = AppTheme.AccentGold) }
                ModernPillTabRow(
                    tabs = tabItems,
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            when {
                showCategorySelector && currentReading == null -> {
                    CategorySelectionContent(
                        onCategorySelected = { category ->
                            selectedCategory = category
                            showCategorySelector = false
                        }
                    )
                }
                selectedCategory != null && currentReading == null -> {
                    CowrieThrowContent(
                        category = selectedCategory!!,
                        isThrowingShells = isThrowingShells,
                        onThrowShells = {
                            isThrowingShells = true
                        },
                        onReadingGenerated = { reading ->
                            currentReading = reading
                            isThrowingShells = false
                        },
                        chart = chart
                    )
                }
                currentReading != null -> {
                    when (selectedTab) {
                        0 -> QueryResultTab(currentReading!!)
                        1 -> AnalysisTab(currentReading!!)
                        2 -> PositionsTab(currentReading!!)
                        3 -> TimingTab(currentReading!!)
                    }
                }
            }
        }
    }
}

// ============================================
// CATEGORY SELECTION
// ============================================

@Composable
private fun CategorySelectionContent(
    onCategorySelected: (QueryCategory) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(StringKeyAshtamangala.CAT_TITLE),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(StringKeyAshtamangala.CAT_SELECT),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(QueryCategory.entries) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryCard(
    category: QueryCategory,
    onClick: () -> Unit
) {
    val icon = when (category) {
        QueryCategory.HEALTH -> Icons.Outlined.HealthAndSafety
        QueryCategory.WEALTH -> Icons.Outlined.AttachMoney
        QueryCategory.RELATIONSHIP -> Icons.Outlined.Favorite
        QueryCategory.CAREER -> Icons.Outlined.Work
        QueryCategory.TRAVEL -> Icons.Outlined.FlightTakeoff
        QueryCategory.LOST_OBJECT -> Icons.Outlined.Search
        QueryCategory.LITIGATION -> Icons.Outlined.Gavel
        QueryCategory.SPIRITUAL -> Icons.Outlined.SelfImprovement
    }

    val color = when (category) {
        QueryCategory.HEALTH -> AppTheme.SuccessColor
        QueryCategory.WEALTH -> AppTheme.AccentGold
        QueryCategory.RELATIONSHIP -> Color(0xFFE91E63)
        QueryCategory.CAREER -> AppTheme.AccentPrimary
        QueryCategory.TRAVEL -> AppTheme.AccentTeal
        QueryCategory.LOST_OBJECT -> AppTheme.WarningColor
        QueryCategory.LITIGATION -> AppTheme.ErrorColor
        QueryCategory.SPIRITUAL -> Color(0xFF9C27B0)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = getCategoryDescription(category),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun getCategoryDescription(category: QueryCategory): String = when (category) {
    QueryCategory.HEALTH -> stringResource(StringKeyAshtamangala.CAT_HEALTH_DESC)
    QueryCategory.WEALTH -> stringResource(StringKeyAshtamangala.CAT_WEALTH_DESC)
    QueryCategory.RELATIONSHIP -> stringResource(StringKeyAshtamangala.CAT_RELATIONSHIP_DESC)
    QueryCategory.CAREER -> stringResource(StringKeyAshtamangala.CAT_CAREER_DESC)
    QueryCategory.TRAVEL -> stringResource(StringKeyAshtamangala.CAT_TRAVEL_DESC)
    QueryCategory.LOST_OBJECT -> stringResource(StringKeyAshtamangala.CAT_LOST_DESC)
    QueryCategory.LITIGATION -> stringResource(StringKeyAshtamangala.CAT_LITIGATION_DESC)
    QueryCategory.SPIRITUAL -> stringResource(StringKeyAshtamangala.CAT_SPIRITUAL_DESC)
}

// ============================================
// COWRIE THROW
// ============================================

@Composable
private fun CowrieThrowContent(
    category: QueryCategory,
    isThrowingShells: Boolean,
    onThrowShells: () -> Unit,
    onReadingGenerated: (AshtamangalaReading) -> Unit,
    chart: VedicChart?
) {
    var animationProgress by remember { mutableFloatStateOf(0f) }
    var shellStates by remember { mutableStateOf(List(8) { false }) }

    // Animation effect when throwing
    LaunchedEffect(isThrowingShells) {
        if (isThrowingShells) {
            // Animate shell states rapidly
            repeat(10) {
                shellStates = List(8) { kotlin.random.Random.nextBoolean() }
                delay(100)
            }
            // Generate final reading
            delay(200)
            val reading = AshtamangalaPrashnaCalculator.generateReading(
                category = category,
                chart = chart
            )
            onReadingGenerated(reading)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Category header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.AccentGold
                )
                Text(
                    text = getCategoryDescription(category),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cowrie shell visualization
        Text(
            text = stringResource(StringKeyAshtamangala.COWRIE_TITLE),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary
        )

        // 8 shells in circular arrangement
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background circle
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(AppTheme.CardBackground)
                    .border(2.dp, AppTheme.BorderColor, CircleShape)
            )

            // Shell positions (circular arrangement)
            val positions = listOf(
                Pair(0f, -80f),   // North - Kubera
                Pair(57f, -57f), // NE - Ishana
                Pair(80f, 0f),   // East - Indra
                Pair(57f, 57f),  // SE - Agni
                Pair(0f, 80f),   // South - Yama
                Pair(-57f, 57f), // SW - Nirriti
                Pair(-80f, 0f),  // West - Varuna
                Pair(-57f, -57f) // NW - Vayu
            )

            positions.forEachIndexed { index, (offsetX, offsetY) ->
                val isOpen = shellStates[index]
                val rotation = if (isThrowingShells) {
                    animateFloatAsState(
                        targetValue = if (kotlin.random.Random.nextBoolean()) 360f else -360f,
                        animationSpec = tween(100),
                        label = "shell_rotation"
                    ).value
                } else 0f

                CowrieShell(
                    isOpen = isOpen,
                    positionLabel = getPositionLabel(index),
                    modifier = Modifier.offset(x = offsetX.dp, y = offsetY.dp),
                    rotation = rotation
                )
            }

            // Center text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isThrowingShells) "..." else "8",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.AccentGold
                )
                Text(
                    text = stringResource(StringKeyAshtamangala.SHELLS),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
            }
        }

        Text(
            text = stringResource(StringKeyAshtamangala.COWRIE_INSTRUCTION),
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Cast button
        Button(
            onClick = onThrowShells,
            enabled = !isThrowingShells,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.AccentGold,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isThrowingShells) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(stringResource(StringKeyAshtamangala.LOADING))
            } else {
                Icon(
                    imageVector = Icons.Outlined.Casino,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(StringKeyAshtamangala.COWRIE_CAST),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CowrieShell(
    isOpen: Boolean,
    positionLabel: String,
    modifier: Modifier = Modifier,
    rotation: Float = 0f
) {
    val color = if (isOpen) AppTheme.AccentGold else AppTheme.TextMuted

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .rotate(rotation)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isOpen) stringResource(StringKeyUIExtra.OPEN_CIRCLE) else stringResource(StringKeyUIExtra.CLOSED_CIRCLE),
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Text(
            text = positionLabel,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextSubtle,
            fontSize = 8.sp
        )
    }
}

@Composable
private fun getPositionLabel(index: Int): String = when (index) {
    0 -> stringResource(StringKeyUIExtra.DIR_N)
    1 -> stringResource(StringKeyUIExtra.DIR_NE)
    2 -> stringResource(StringKeyUIExtra.DIR_E)
    3 -> stringResource(StringKeyUIExtra.DIR_SE)
    4 -> stringResource(StringKeyUIExtra.DIR_S)
    5 -> stringResource(StringKeyUIExtra.DIR_SW)
    6 -> stringResource(StringKeyUIExtra.DIR_W)
    7 -> stringResource(StringKeyUIExtra.DIR_NW)
    else -> ""
}

// ============================================
// QUERY RESULT TAB
// ============================================

@Composable
private fun QueryResultTab(reading: AshtamangalaReading) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary card
        item {
            SummaryCard(reading)
        }

        // Shell result
        item {
            ShellResultCard(reading.cowrieThrow)
        }

        // Yes/No Probability
        item {
            YesNoProbabilityCard(reading.yesNoProbability)
        }

        // Primary indication
        item {
            PrimaryIndicationCard(reading)
        }

        // Special readings if available
        reading.specialReadings?.let { special ->
            item {
                SpecialReadingsCard(reading.category, special)
            }
        }
    }
}

@Composable
private fun SummaryCard(reading: AshtamangalaReading) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(StringKeyAshtamangala.RESULT_TITLE),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Text(
                text = reading.interpretation.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            Divider(color = AppTheme.BorderColor)

            // Dominant position
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(StringKeyAshtamangala.POS_DOMINANT),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = "${reading.dominantPosition.deity} (${reading.dominantPosition.direction})",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                }
                IndicationBadge(reading.primaryIndication)
            }
        }
    }
}

@Composable
private fun ShellResultCard(cowrieThrow: CowrieThrowResult) {
    val openColor = AppTheme.AccentGold
    val closedColor = AppTheme.TextMuted

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(StringKeyAshtamangala.COWRIE_RESULT),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            // Shell visualization
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                cowrieThrow.shellPattern.forEachIndexed { index, isOpen ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isOpen) openColor.copy(alpha = 0.2f) else closedColor.copy(alpha = 0.1f))
                            .border(2.dp, if (isOpen) openColor else closedColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isOpen) stringResource(StringKeyUIExtra.OPEN_CIRCLE) else stringResource(StringKeyUIExtra.CLOSED_CIRCLE),
                            color = if (isOpen) openColor else closedColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Open/Closed counts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${cowrieThrow.openShells}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = openColor
                    )
                    Text(
                        text = stringResource(StringKeyAshtamangala.COWRIE_OPEN),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${cowrieThrow.closedShells}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = closedColor
                    )
                    Text(
                        text = stringResource(StringKeyAshtamangala.COWRIE_CLOSED),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            // Interpretation
            Surface(
                color = AppTheme.ChipBackground,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = cowrieThrow.interpretation,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun YesNoProbabilityCard(probability: YesNoProbability) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(StringKeyAshtamangala.YESNO_TITLE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            // Probability bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .weight(probability.yesPercentage.toFloat())
                        .fillMaxHeight()
                        .background(AppTheme.SuccessColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (probability.yesPercentage >= 20) {
                        val yesLabel = stringResource(StringKeyAshtamangala.YESNO_YES)
                        Text(
                            text = "${probability.yesPercentage}% $yesLabel",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(probability.noPercentage.toFloat())
                        .fillMaxHeight()
                        .background(AppTheme.ErrorColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (probability.noPercentage >= 20) {
                        val noLabel = stringResource(StringKeyAshtamangala.YESNO_NO)
                        Text(
                            text = "${probability.noPercentage}% $noLabel",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Confidence
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKeyAshtamangala.YESNO_CONFIDENCE),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                val confidenceColor = when (probability.confidence) {
                    ConfidenceLevel.HIGH -> AppTheme.SuccessColor
                    ConfidenceLevel.MEDIUM -> AppTheme.AccentGold
                    ConfidenceLevel.LOW -> AppTheme.WarningColor
                }
                Surface(
                    color = confidenceColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = probability.confidence.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = confidenceColor
                    )
                }
            }

            Text(
                text = probability.reasoning,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun PrimaryIndicationCard(reading: AshtamangalaReading) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(StringKeyAshtamangala.RESULT_PRIMARY),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Text(
                text = reading.interpretation.categorySpecific,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary,
                lineHeight = 22.sp
            )

            // Positive factors
            if (reading.interpretation.positiveFactors.isNotEmpty()) {
                Text(
                    text = stringResource(StringKeyAshtamangala.POSITIVE_FACTORS),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.SuccessColor
                )
                reading.interpretation.positiveFactors.forEach { factor ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("✓", color = AppTheme.SuccessColor, fontSize = 12.sp)
                        Text(
                            text = factor,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }

            // Negative factors
            if (reading.interpretation.negativeFactors.isNotEmpty()) {
                Text(
                    text = stringResource(StringKeyAshtamangala.CHALLENGES),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.WarningColor
                )
                reading.interpretation.negativeFactors.forEach { factor ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("⚠", color = AppTheme.WarningColor, fontSize = 12.sp)
                        Text(
                            text = factor,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecialReadingsCard(category: QueryCategory, special: SpecialReadings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val title = when (category) {
                QueryCategory.LOST_OBJECT -> stringResource(StringKeyAshtamangala.SPECIAL_LOST_DIRECTION)
                QueryCategory.TRAVEL -> stringResource(StringKeyAshtamangala.SPECIAL_TRAVEL_SAFE)
                QueryCategory.LITIGATION -> stringResource(StringKeyAshtamangala.SPECIAL_LITIGATION_WIN)
                QueryCategory.RELATIONSHIP -> stringResource(StringKeyAshtamangala.SPECIAL_MARRIAGE_TIMING)
                QueryCategory.HEALTH -> stringResource(StringKeyAshtamangala.SPECIAL_HEALTH_RECOVERY)
                else -> stringResource(StringKeyAshtamangala.SPECIAL_INSIGHTS)
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.AccentPrimary
            )

            when (category) {
                QueryCategory.LOST_OBJECT -> {
                    special.lostObjectDirection?.let {
                        InfoRow(stringResource(StringKeyAshtamangala.DIRECTION_TO_SEARCH), it)
                    }
                    special.recoveryPossibility?.let {
                        InfoRow(stringResource(StringKeyAshtamangala.RECOVERY_LIKELIHOOD), "$it%")
                    }
                }
                QueryCategory.TRAVEL -> {
                    special.travelSafety?.let {
                        InfoRow(stringResource(StringKeyAshtamangala.SAFETY_LEVEL), it.name.replace("_", " "))
                    }
                }
                QueryCategory.LITIGATION -> {
                    special.litigationSuccess?.let {
                        InfoRow(stringResource(StringKeyAshtamangala.SUCCESS_PROBABILITY), "$it%")
                    }
                }
                QueryCategory.RELATIONSHIP -> {
                    special.marriageTiming?.let {
                        InfoRow(stringResource(StringKeyAshtamangala.TIMING_INDICATION), it)
                    }
                }
                QueryCategory.HEALTH -> {
                    special.healthRecovery?.let {
                        InfoRow(stringResource(StringKeyAshtamangala.RECOVERY_OUTLOOK), it)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
    }
}

@Composable
private fun IndicationBadge(indication: IndicationStrength) {
    val (color, text) = when (indication) {
        IndicationStrength.VERY_FAVORABLE -> AppTheme.SuccessColor to stringResource(StringKeyAshtamangala.RESULT_VERY_FAVORABLE)
        IndicationStrength.FAVORABLE -> Color(0xFF4CAF50) to stringResource(StringKeyAshtamangala.RESULT_FAVORABLE)
        IndicationStrength.MIXED -> AppTheme.AccentGold to stringResource(StringKeyAshtamangala.RESULT_MIXED)
        IndicationStrength.CHALLENGING -> AppTheme.WarningColor to stringResource(StringKeyAshtamangala.RESULT_CHALLENGING)
        IndicationStrength.VERY_CHALLENGING -> AppTheme.ErrorColor to stringResource(StringKeyAshtamangala.RESULT_VERY_CHALLENGING)
        IndicationStrength.NEUTRAL -> AppTheme.TextMuted to stringResource(StringKeyAshtamangala.RESULT_NEUTRAL)
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

// ============================================
// ANALYSIS TAB
// ============================================

@Composable
private fun AnalysisTab(reading: AshtamangalaReading) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Detailed analysis
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(StringKeyAshtamangala.RESULT_TITLE),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = reading.interpretation.detailedAnalysis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Secondary influences
        if (reading.secondaryInfluences.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(StringKeyAshtamangala.RESULT_SECONDARY),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                        reading.secondaryInfluences.forEach { influence ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(stringResource(StringKeyUIExtra.BULLET), color = AppTheme.AccentPrimary)
                                Text(
                                    text = influence,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Chart validation if available
        reading.chartValidation?.let { validation ->
            item {
                ChartValidationCard(validation)
            }
        }

        // Cautions
        if (reading.interpretation.cautions.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppTheme.WarningColor.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Warning,
                                contentDescription = null,
                                tint = AppTheme.WarningColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(StringKeyAshtamangala.CAUTIONS),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.WarningColor
                            )
                        }
                        reading.interpretation.cautions.forEach { caution ->
                            Text(
                                text = "• $caution",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartValidationCard(validation: ChartValidation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (validation.isSupporting) Icons.Outlined.CheckCircle else Icons.Outlined.Info,
                    contentDescription = null,
                    tint = if (validation.isSupporting) AppTheme.SuccessColor else AppTheme.WarningColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(StringKeyAshtamangala.CHART_INTEGRATION),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
            }

            Text(
                text = validation.lagnaEffect,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
            Text(
                text = validation.moonEffect,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )

            if (validation.conflictAreas.isNotEmpty()) {
                validation.conflictAreas.forEach { conflict ->
                    Text(
                        text = "⚠ $conflict",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.WarningColor
                    )
                }
            }
        }
    }
}

// ============================================
// POSITIONS TAB
// ============================================

@Composable
private fun PositionsTab(reading: AshtamangalaReading) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(StringKeyAshtamangala.POS_ACTIVE),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
        }

        items(reading.positionAnalysis) { posResult ->
            PositionCard(posResult, posResult.position == reading.dominantPosition)
        }
    }
}

@Composable
private fun PositionCard(position: PositionResult, isDominant: Boolean) {
    val borderColor = if (isDominant) AppTheme.AccentGold else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isDominant) Modifier.border(2.dp, borderColor, RoundedCornerShape(12.dp))
                else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (position.isActive)
                AppTheme.AccentGold.copy(alpha = 0.1f)
            else
                AppTheme.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (position.isActive) AppTheme.AccentGold.copy(alpha = 0.2f)
                                else AppTheme.ChipBackground
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${position.position.number}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (position.isActive) AppTheme.AccentGold else AppTheme.TextMuted
                        )
                    }
                    Column {
                        Text(
                            text = position.deity,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.TextPrimary
                        )
                        Text(
                            text = position.direction,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTheme.TextMuted
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = if (position.isActive) AppTheme.SuccessColor.copy(alpha = 0.1f)
                        else AppTheme.ChipBackground,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (position.isActive) stringResource(StringKeyAshtamangala.ACTIVE) else stringResource(StringKeyAshtamangala.INACTIVE),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (position.isActive) AppTheme.SuccessColor else AppTheme.TextMuted
                        )
                    }
                    if (isDominant) {
                        Text(
                            text = stringResource(StringKeyAshtamangala.DOMINANT),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.AccentGold,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Strength bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(StringKeyAshtamangala.STRENGTH),
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextMuted
                )
                LinearProgressIndicator(
                    progress = { position.strength / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (position.strength >= 60) AppTheme.SuccessColor
                    else if (position.strength >= 40) AppTheme.AccentGold
                    else AppTheme.TextMuted,
                    trackColor = AppTheme.ChipBackground
                )
                Text(
                    text = "${position.strength}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.TextSecondary
                )
            }

            Text(
                text = position.effectOnQuery,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextSecondary
            )
        }
    }
}

// ============================================
// TIMING TAB
// ============================================

@Composable
private fun TimingTab(reading: AshtamangalaReading) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Timing prediction
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(StringKeyAshtamangala.TIME_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    Text(
                        text = reading.timingPrediction.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.TextSecondary
                    )

                    Divider(color = AppTheme.BorderColor)

                    reading.timingPrediction.bestDay?.let { day ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(StringKeyAshtamangala.TIME_BEST_DAY),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = day.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.SuccessColor
                            )
                        }
                    }

                    reading.timingPrediction.avoidDay?.let { day ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(StringKeyAshtamangala.TIME_AVOID_DAY),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = day.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.WarningColor
                            )
                        }
                    }

                    reading.timingPrediction.suggestedTithi?.let { tithi ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(StringKeyAshtamangala.TITHI_SUGGESTION),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                            Text(
                                text = tithi,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Remedies
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SelfImprovement,
                            contentDescription = null,
                            tint = AppTheme.AccentGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(StringKeyAshtamangala.REMEDY_TITLE),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppTheme.TextPrimary
                        )
                    }

                    reading.remedies.forEach { remedy ->
                        RemedyItem(remedy)
                    }
                }
            }
        }
    }
}

@Composable
private fun RemedyItem(remedy: Remedy) {
    val icon = when (remedy.type) {
        RemedyType.DEITY_WORSHIP -> Icons.Outlined.Temple
        RemedyType.MANTRA -> Icons.Outlined.RecordVoiceOver
        RemedyType.DIRECTION -> Icons.Outlined.Explore
        RemedyType.COLOR -> Icons.Outlined.Palette
        RemedyType.DAY -> Icons.Outlined.CalendarMonth
        RemedyType.DONATION -> Icons.Outlined.VolunteerActivism
        RemedyType.ACTION -> Icons.Outlined.TouchApp
    }

    Surface(
        color = AppTheme.ChipBackground,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppTheme.AccentPrimary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = remedy.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextPrimary
                )
                remedy.mantra?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.AccentGold,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ============================================
// INFO DIALOG
// ============================================

@Composable
private fun AshtamangalaInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(StringKeyAshtamangala.INFO_TITLE),
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = stringResource(StringKeyAshtamangala.INFO_DESC),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextSecondary,
                        lineHeight = 20.sp
                    )
                }

                item {
                    Text(
                        text = stringResource(StringKeyAshtamangala.THE_EIGHT_POSITIONS),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        AshtamangalaPosition.entries.forEach { pos ->
                            Row {
                                Text(
                                    text = "${pos.number}. ${pos.deity}: ",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = AppTheme.AccentPrimary
                                )
                                Text(
                                    text = pos.direction,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTheme.TextSecondary
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(StringKeyAshtamangala.INFO_VEDIC_REF),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppTheme.TextMuted
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKeyAshtamangala.INFO_CLOSE), color = AppTheme.AccentPrimary)
            }
        },
        containerColor = AppTheme.CardBackground
    )
}

// Missing icon fallbacks
private val Icons.Outlined.Temple: ImageVector
    get() = Icons.Outlined.TempleHindu

private val Icons.Outlined.TempleHindu: ImageVector
    get() = Icons.Outlined.Church // Fallback


package com.astro.storm.ui.screen.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Planet
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.DashaCalculator
import com.astro.storm.ephemeris.HoroscopeCalculator
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.theme.CinzelDecorativeFamily
import com.astro.storm.ui.theme.PoppinsFontFamily
import com.astro.storm.ui.theme.SpaceGroteskFamily
import com.astro.storm.ui.viewmodel.InsightsUiState
import com.astro.storm.ui.viewmodel.InsightsViewModel
import com.astro.storm.ui.viewmodel.InsightsData
import com.astro.storm.ui.viewmodel.InsightError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

enum class HoroscopePeriod(val titleKey: StringKey) {
    TODAY(StringKey.PERIOD_TODAY),
    TOMORROW(StringKey.PERIOD_TOMORROW),
    WEEKLY(StringKey.PERIOD_WEEKLY);

    fun getLocalizedTitle(language: Language): String = StringResources.get(titleKey, language)
}

private object InsightsFormatters {
    // Static formatters for default locale (English)
    val monthDay: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)
    val monthYear: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
    val fullDate: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)

    fun getDayMonth(language: Language): DateTimeFormatter {
        val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
        return DateTimeFormatter.ofPattern("EEEE, MMMM d", locale)
    }

    fun getMonthYear(language: Language): DateTimeFormatter {
        val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
        return DateTimeFormatter.ofPattern("MMM yyyy", locale)
    }

    fun getMonthDay(language: Language): DateTimeFormatter {
        val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
        return DateTimeFormatter.ofPattern("MMM d", locale)
    }
}

@Stable
private data class ChartIdentity(
    val name: String,
    val dateTimeHash: Int,
    val latitudeInt: Int,
    val longitudeInt: Int,
    val timezone: String
) {
    companion object {
        fun from(chart: VedicChart?): ChartIdentity? {
            if (chart == null) return null
            val birthData = chart.birthData
            return ChartIdentity(
                name = birthData.name,
                dateTimeHash = birthData.dateTime.hashCode(),
                latitudeInt = (birthData.latitude * 1000).toInt(),
                longitudeInt = (birthData.longitude * 1000).toInt(),
                timezone = birthData.timezone
            )
        }
    }
}

@Composable
fun InsightsTab(
    chart: VedicChart?,
    onCreateChart: () -> Unit = {},
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val todayDate = remember(chart) { LocalDate.now(resolveZoneId(chart?.birthData?.timezone)) }
    val chartIdentity = remember(chart) { ChartIdentity.from(chart) }

    LaunchedEffect(chartIdentity) {
        viewModel.loadInsights(chart)
    }

    val insightsState by viewModel.uiState.collectAsState()

    val onRetry = remember(chart) {
        { viewModel.loadInsights(chart) }
    }

    when (val state = insightsState) {
        is InsightsUiState.Loading -> InsightsLoadingSkeleton()
        is InsightsUiState.Error -> InsightsErrorState(
            messageKey = state.messageKey,
            onRetry = onRetry
        )
        is InsightsUiState.Success -> {
            var selectedPeriod by remember { mutableStateOf(HoroscopePeriod.TODAY) }
            InsightsContent(
                data = state.data,
                todayDate = todayDate,
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it },
                onRetryFailed = onRetry
            )
        }
        is InsightsUiState.Idle -> EmptyInsightsState(onCreateChart = onCreateChart)
    }
}

@Composable
private fun InsightsContent(
    data: InsightsData,
    todayDate: LocalDate,
    selectedPeriod: HoroscopePeriod,
    onPeriodSelected: (HoroscopePeriod) -> Unit,
    onRetryFailed: () -> Unit
) {
    val listState = rememberLazyListState()
    val language = LocalLanguage.current

    val hasAnyContent by remember(data) {
        derivedStateOf {
            data.dashaTimeline != null ||
                    data.todayHoroscope != null ||
                    data.tomorrowHoroscope != null ||
                    data.weeklyHoroscope != null
        }
    }

    val hasHoroscopeContent by remember(data) {
        derivedStateOf {
            data.todayHoroscope != null ||
                    data.tomorrowHoroscope != null ||
                    data.weeklyHoroscope != null
        }
    }

    if (!hasAnyContent && data.errors.isNotEmpty()) {
        InsightsErrorState(
            messageKey = data.errors.firstOrNull()?.messageKey ?: StringKey.ERROR_EPHEMERIS_DATA,
            onRetry = onRetryFailed
        )
        return
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        if (data.errors.isNotEmpty()) {
            item(key = "partial_error_banner") {
                PartialErrorBanner(
                    errors = data.errors,
                    onRetry = onRetryFailed
                )
            }
        }

        data.dashaTimeline?.let { timeline ->
            item(key = "dasha_current") {
                CurrentDashaCard(timeline, todayDate)
            }

            item(key = "dasha_timeline") {
                DashaTimelinePreview(timeline, todayDate)
            }
        }

        if (data.planetaryInfluences.isNotEmpty()) {
            item(key = "transits") {
                PlanetaryTransitsSection(data.planetaryInfluences)
            }
        }

        if (hasHoroscopeContent) {
            item(key = "section_divider") {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = AppTheme.DividerColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item(key = "period_selector") {
                PeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = onPeriodSelected,
                    todayAvailable = data.todayHoroscope != null,
                    tomorrowAvailable = data.tomorrowHoroscope != null,
                    weeklyAvailable = data.weeklyHoroscope != null
                )
            }

            when (selectedPeriod) {
                HoroscopePeriod.TODAY -> {
                    data.todayHoroscope?.let { horoscope ->
                        item(key = "today_header") {
                            DailyHoroscopeHeader(horoscope, isTomorrow = false)
                        }
                        item(key = "today_energy") {
                            EnergyCard(horoscope.overallEnergy)
                        }
                        item(key = "today_areas") {
                            LifeAreasSection(horoscope.lifeAreas)
                        }
                        item(key = "today_lucky") {
                            LuckyElementsCard(horoscope.luckyElements)
                        }
                        item(key = "today_recs") {
                            RecommendationsCard(horoscope.recommendations, horoscope.cautions)
                        }
                        item(key = "today_affirmation") {
                            AffirmationCard(horoscope.affirmationKey)
                        }
                    } ?: item(key = "today_unavailable") {
                        HoroscopeUnavailableCard(periodKey = StringKey.PERIOD_TODAY, onRetry = onRetryFailed)
                    }
                }
                HoroscopePeriod.TOMORROW -> {
                    data.tomorrowHoroscope?.let { horoscope ->
                        item(key = "tomorrow_header") {
                            DailyHoroscopeHeader(horoscope, isTomorrow = true)
                        }
                        item(key = "tomorrow_energy") {
                            EnergyCard(horoscope.overallEnergy)
                        }
                        item(key = "tomorrow_areas") {
                            LifeAreasSection(horoscope.lifeAreas)
                        }
                        item(key = "tomorrow_lucky") {
                            LuckyElementsCard(horoscope.luckyElements)
                        }
                    } ?: item(key = "tomorrow_unavailable") {
                        HoroscopeUnavailableCard(periodKey = StringKey.PERIOD_TOMORROW, onRetry = onRetryFailed)
                    }
                }
                HoroscopePeriod.WEEKLY -> {
                    data.weeklyHoroscope?.let { weekly ->
                        item(key = "weekly_overview") {
                            WeeklyOverviewHeader(weekly)
                        }
                        item(key = "weekly_chart") {
                            WeeklyEnergyChart(weekly.dailyHighlights)
                        }
                        item(key = "weekly_dates") {
                            KeyDatesSection(weekly.keyDates)
                        }
                        item(key = "weekly_predictions") {
                            WeeklyPredictionsSection(weekly.weeklyPredictions)
                        }
                        item(key = "weekly_advice") {
                            WeeklyAdviceCard(weekly.weeklyAdvice)
                        }
                    } ?: item(key = "weekly_unavailable") {
                        HoroscopeUnavailableCard(periodKey = StringKey.PERIOD_WEEKLY, onRetry = onRetryFailed)
                    }
                }
            }
        }
    }
}

@Composable
private fun PartialErrorBanner(
    errors: List<InsightError>,
    onRetry: () -> Unit
) {
    val language = LocalLanguage.current
    val errorCount = remember(errors) { errors.size }
    val localizedCount = remember(errorCount, language) {
        if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(errorCount.toString())
        else errorCount.toString()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.WarningColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.WarningColor.copy(alpha = 0.3f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = AppTheme.WarningColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(StringKey.ERROR_PARTIAL),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.WarningColor
                )
                Text(
                    text = stringResource(StringKey.ERROR_CALCULATIONS_FAILED, localizedCount),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = PoppinsFontFamily,
                    color = AppTheme.TextMuted
                )
            }

            TextButton(onClick = onRetry) {
                Text(
                    text = stringResource(StringKey.BTN_RETRY),
                    color = AppTheme.WarningColor,
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun HoroscopeUnavailableCard(
    periodKey: StringKey,
    onRetry: () -> Unit
) {
    val language = LocalLanguage.current
    val displayPeriod = remember(periodKey, language) { StringResources.get(periodKey, language) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppTheme.ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CloudOff,
                    contentDescription = null,
                    tint = AppTheme.TextMuted,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(StringKey.ERROR_HOROSCOPE_UNAVAILABLE, displayPeriod),
                style = MaterialTheme.typography.titleSmall,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(StringKey.ERROR_EPHEMERIS_DATA),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppTheme.AccentPrimary
                ),
                border = BorderStroke(1.dp, AppTheme.AccentPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKey.BTN_TRY_AGAIN),
                    fontFamily = SpaceGroteskFamily
                )
            }
        }
    }
}

@Composable
private fun InsightsErrorState(
    messageKey: StringKey,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppTheme.ErrorColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = AppTheme.ErrorColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(StringKey.ERROR_UNABLE_TO_LOAD),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = CinzelDecorativeFamily,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(messageKey),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppTheme.AccentPrimary
                ),
                border = BorderStroke(1.dp, AppTheme.AccentPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKey.BTN_TRY_AGAIN),
                    fontFamily = SpaceGroteskFamily,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun InsightsLoadingSkeleton() {
    val cardBackground = AppTheme.CardBackground
    val shimmerColors = remember(cardBackground) {
        listOf(
            cardBackground.copy(alpha = 0.9f),
            cardBackground.copy(alpha = 0.4f),
            cardBackground.copy(alpha = 0.9f)
        )
    }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = remember(translateAnim) {
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 500f, 0f),
            end = Offset(translateAnim, 0f)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShimmerCard(brush, 180.dp)
        ShimmerCard(brush, 140.dp)
        ShimmerCard(brush, 100.dp)
        ShimmerCard(brush, 48.dp)
        ShimmerCard(brush, 200.dp)
        ShimmerCard(brush, 120.dp)
    }
}

@Composable
private fun ShimmerCard(brush: Brush, height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(2.dp))
            .background(brush)
    )
}

@Composable
private fun PeriodSelector(
    selectedPeriod: HoroscopePeriod,
    onPeriodSelected: (HoroscopePeriod) -> Unit,
    todayAvailable: Boolean,
    tomorrowAvailable: Boolean,
    weeklyAvailable: Boolean
) {
    val availability = remember(todayAvailable, tomorrowAvailable, weeklyAvailable) {
        mapOf(
            HoroscopePeriod.TODAY to todayAvailable,
            HoroscopePeriod.TOMORROW to tomorrowAvailable,
            HoroscopePeriod.WEEKLY to weeklyAvailable
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HoroscopePeriod.entries.forEach { period ->
            val isAvailable = availability[period] ?: true
            val isSelected = period == selectedPeriod

            PeriodSelectorItem(
                period = period,
                isSelected = isSelected,
                isAvailable = isAvailable,
                onSelect = { onPeriodSelected(period) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PeriodSelectorItem(
    period: HoroscopePeriod,
    isSelected: Boolean,
    isAvailable: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected && isAvailable -> AppTheme.AccentPrimary
            isSelected && !isAvailable -> AppTheme.AccentPrimary.copy(alpha = 0.5f)
            else -> AppTheme.BorderColor
        },
        animationSpec = tween(250),
        label = "period_border_${period.name}"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected && isAvailable -> AppTheme.AccentPrimary.copy(alpha = 0.1f)
            else -> Color.Transparent
        },
        animationSpec = tween(250),
        label = "period_bg_${period.name}"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isSelected -> AppTheme.AccentPrimary
            !isAvailable -> AppTheme.TextMuted.copy(alpha = 0.5f)
            else -> AppTheme.TextSecondary
        },
        animationSpec = tween(250),
        label = "period_text_${period.name}"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onSelect() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = period.getLocalizedTitle(language).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
                letterSpacing = 1.5.sp
            )
            if (!isAvailable) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.CloudOff,
                    contentDescription = stringResource(StringKeyMatch.MISC_UNAVAILABLE),
                    tint = textColor,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun DailyHoroscopeHeader(
    horoscope: HoroscopeCalculator.DailyHoroscope,
    isTomorrow: Boolean
) {
    val language = LocalLanguage.current
    val formattedDate = remember(horoscope.date, language) {
        horoscope.date.format(InsightsFormatters.getDayMonth(language))
    }
    val themeDescription = stringResource(horoscope.themeDescriptionKey)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dateText = remember(formattedDate, language) {
                    if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(formattedDate)
                    else formattedDate
                }
                Text(
                    text = dateText.uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextMuted,
                    letterSpacing = 1.5.sp
                )
                if (isTomorrow) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .border(
                                width = 1.dp,
                                color = AppTheme.AccentPrimary.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(AppTheme.AccentPrimary.copy(alpha = 0.08f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(StringKey.BTN_PREVIEW).uppercase(),
                            fontFamily = SpaceGroteskFamily,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.AccentPrimary,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Theme title in Cinzel Decorative
            Text(
                text = stringResource(horoscope.themeKey),
                fontFamily = CinzelDecorativeFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                lineHeight = 28.sp
            )

            Spacer(Modifier.height(16.dp))

            // "The Oracle" drop cap effect: first letter large Cinzel, rest Cormorant Garamond
            if (themeDescription.isNotEmpty()) {
                val firstLetter = themeDescription.first().toString()
                val restOfText = themeDescription.drop(1)

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = CinzelDecorativeFamily,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.AccentGold
                            )
                        ) {
                            append(firstLetter)
                        }
                        withStyle(
                            SpanStyle(
                                fontFamily = PoppinsFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = AppTheme.TextSecondary
                            )
                        ) {
                            append(restOfText)
                        }
                    },
                    lineHeight = 28.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoChip(
                    icon = Icons.Outlined.NightlightRound,
                    label = stringResource(StringKey.TRANSITS_MOON_IN, horoscope.moonSign.getLocalizedName(language)),
                    modifier = Modifier.weight(1f)
                )
                InfoChip(
                    icon = Icons.Outlined.Schedule,
                    label = horoscope.activeDasha,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(
                width = 1.dp,
                color = AppTheme.BorderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .background(AppTheme.ChipBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontFamily = SpaceGroteskFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun EnergyCard(overallEnergy: Int) {
    val language = LocalLanguage.current
    val animatedEnergy by animateIntAsState(
        targetValue = overallEnergy,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "energy_anim"
    )

    val energyColor = getEnergyColor(overallEnergy)
    val energyDescription = remember(overallEnergy, language) { getEnergyDescription(overallEnergy, language) }

    val localizedScore = stringResource(StringKeyUIExtra.ENERGY_SCORE_FMT, overallEnergy)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(StringKey.INSIGHTS_OVERALL_ENERGY).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    repeat(10) { index ->
                        EnergyDot(
                            index = index,
                            isActive = index < animatedEnergy
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = localizedScore,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = energyColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = energyDescription,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = PoppinsFontFamily,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun EnergyDot(index: Int, isActive: Boolean) {
    val dotColor by animateColorAsState(
        targetValue = if (isActive) {
            when {
                index < 3 -> AppTheme.ErrorColor
                index < 6 -> AppTheme.WarningColor
                else -> AppTheme.SuccessColor
            }
        } else {
            AppTheme.ChipBackground
        },
        animationSpec = tween(300, delayMillis = index * 50),
        label = "dot_$index"
    )

    // Square indicators for the Neo-Vedic grid feel
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(dotColor)
    )
}

@Composable
private fun getEnergyColor(energy: Int): Color = when {
    energy < 4 -> AppTheme.ErrorColor
    energy < 7 -> AppTheme.WarningColor
    else -> AppTheme.SuccessColor
}

private fun getEnergyDescription(energy: Int, language: Language): String = when {
    energy >= 9 -> StringResources.get(StringKey.ENERGY_EXCEPTIONAL, language)
    energy >= 8 -> StringResources.get(StringKey.ENERGY_EXCELLENT, language)
    energy >= 7 -> StringResources.get(StringKey.ENERGY_STRONG, language)
    energy >= 6 -> StringResources.get(StringKey.ENERGY_FAVORABLE, language)
    energy >= 5 -> StringResources.get(StringKey.ENERGY_BALANCED, language)
    energy >= 4 -> StringResources.get(StringKey.ENERGY_MODERATE, language)
    energy >= 3 -> StringResources.get(StringKey.ENERGY_LOWER, language)
    energy >= 2 -> StringResources.get(StringKey.ENERGY_CHALLENGING, language)
    else -> StringResources.get(StringKey.ENERGY_REST, language)
}

@Composable
private fun LifeAreasSection(lifeAreas: List<HoroscopeCalculator.LifeAreaPrediction>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(StringKey.INSIGHTS_LIFE_AREAS).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        lifeAreas.forEachIndexed { index, prediction ->
            key(prediction.area.name) {
                LifeAreaCard(prediction)
                if (index < lifeAreas.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun LifeAreaCard(prediction: HoroscopeCalculator.LifeAreaPrediction) {
    var expanded by remember { mutableStateOf(false) }
    val areaConfig = getLifeAreaConfig(prediction.area)
    val interactionSource = remember { MutableInteractionSource() }

    // Get localized area name
    val localizedAreaName = stringResource(prediction.area.displayNameKey)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { expanded = !expanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(areaConfig.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = areaConfig.icon,
                        contentDescription = null,
                        tint = areaConfig.color,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = localizedAreaName,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    StarRating(rating = prediction.rating, color = areaConfig.color)
                }

                ExpandIcon(expanded = expanded)
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(200)) + expandVertically(tween(300)),
                exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = prediction.prediction,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 16.sp,
                        color = AppTheme.TextSecondary,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(2.dp))
                            .border(
                                width = 1.dp,
                                color = areaConfig.color.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(areaConfig.color.copy(alpha = 0.06f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = areaConfig.color,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = prediction.advice,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 14.sp,
                            color = AppTheme.TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StarRating(rating: Int, color: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (index < rating) color else AppTheme.TextSubtle,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun ExpandIcon(expanded: Boolean) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(300),
        label = "expand_rotation"
    )

    Icon(
        imageVector = Icons.Default.ExpandMore,
        contentDescription = if (expanded) stringResource(StringKeyMatch.MISC_COLLAPSE) else stringResource(StringKeyMatch.MISC_EXPAND),
        tint = AppTheme.TextMuted,
        modifier = Modifier
            .size(24.dp)
            .graphicsLayer { rotationZ = rotation }
    )
}

@Stable
private data class LifeAreaConfig(val color: Color, val icon: ImageVector)

@Composable
private fun getLifeAreaConfig(area: HoroscopeCalculator.LifeArea): LifeAreaConfig {
    return when (area) {
        HoroscopeCalculator.LifeArea.CAREER -> LifeAreaConfig(AppTheme.LifeAreaCareer, Icons.Outlined.Work)
        HoroscopeCalculator.LifeArea.LOVE -> LifeAreaConfig(AppTheme.LifeAreaLove, Icons.Outlined.Favorite)
        HoroscopeCalculator.LifeArea.HEALTH -> LifeAreaConfig(AppTheme.LifeAreaHealth, Icons.Outlined.FavoriteBorder)
        HoroscopeCalculator.LifeArea.FINANCE -> LifeAreaConfig(AppTheme.LifeAreaFinance, Icons.Outlined.AccountBalance)
        HoroscopeCalculator.LifeArea.FAMILY -> LifeAreaConfig(AppTheme.AccentTeal, Icons.Outlined.Home)
        HoroscopeCalculator.LifeArea.SPIRITUALITY -> LifeAreaConfig(AppTheme.LifeAreaSpiritual, Icons.Outlined.Star)
    }
}

@Composable
private fun LuckyElementsCard(lucky: HoroscopeCalculator.LuckyElements) {
    val language = LocalLanguage.current
    val colorValue = remember(lucky.color) { lucky.color.split(",").first().trim() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(StringKey.INSIGHTS_LUCKY_ELEMENTS).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LuckyElement(
                    Icons.Outlined.Numbers,
                    stringResource(StringKey.LUCKY_NUMBER),
                    lucky.number.toString().let {
                        if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(it) else it
                    }
                )
                LuckyElement(Icons.Outlined.Palette, stringResource(StringKey.LUCKY_COLOR), lucky.color)
                LuckyElement(Icons.Outlined.Explore, stringResource(StringKey.LUCKY_DIRECTION), lucky.direction)
                LuckyElement(Icons.Outlined.Diamond, stringResource(StringKey.LUCKY_GEMSTONE), lucky.gemstone)
            }
        }
    }
}

@Composable
private fun LuckyElement(icon: ImageVector, label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .border(
                width = 1.dp,
                color = AppTheme.BorderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .background(AppTheme.ChipBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.AccentGold,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label.uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextMuted,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontFamily = PoppinsFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun RecommendationsCard(recommendations: List<StringKey>, cautions: List<StringKey>) {
    if (recommendations.isEmpty() && cautions.isEmpty()) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            if (recommendations.isNotEmpty()) {
                Text(
                    text = stringResource(StringKey.INSIGHTS_RECOMMENDATIONS).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.SuccessColor,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                recommendations.forEach { recKey ->
                    key(recKey.name) {
                        BulletItem(
                            icon = Icons.Filled.CheckCircle,
                            textKey = recKey,
                            iconTint = AppTheme.SuccessColor
                        )
                    }
                }
            }

            if (cautions.isNotEmpty()) {
                if (recommendations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(
                    text = stringResource(StringKey.INSIGHTS_CAUTIONS).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.WarningColor,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                cautions.forEach { cautionKey ->
                    key(cautionKey.name) {
                        BulletItem(
                            icon = Icons.Filled.Warning,
                            textKey = cautionKey,
                            iconTint = AppTheme.WarningColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletItem(
    icon: ImageVector,
    textKey: StringKey,
    iconTint: Color
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(textKey),
            fontFamily = PoppinsFontFamily,
            fontSize = 16.sp,
            color = AppTheme.TextSecondary,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun AffirmationCard(affirmationKey: StringKey) {
    // Pull-quote style: left gold border accent, centered italic Cormorant Garamond
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left Vedic Gold accent border line
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .defaultMinSize(minHeight = 80.dp)
                    .background(AppTheme.AccentGold)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.FormatQuote,
                    contentDescription = null,
                    tint = AppTheme.AccentGold,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(affirmationKey),
                    fontFamily = PoppinsFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    color = AppTheme.TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(StringKey.INSIGHTS_TODAYS_AFFIRMATION).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentGold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
private fun WeeklyOverviewHeader(weekly: HoroscopeCalculator.WeeklyHoroscope) {
    val language = LocalLanguage.current
    val dateRange = remember(weekly.startDate, weekly.endDate, language) {
        val format = InsightsFormatters.getMonthDay(language)
        val range = "${weekly.startDate.format(format)} - ${weekly.endDate.format(format)}"
        if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(range)
        else range
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = dateRange.uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextMuted,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(weekly.weeklyThemeKey),
                fontFamily = CinzelDecorativeFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.TextPrimary,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = weekly.weeklyOverview,
                fontFamily = PoppinsFontFamily,
                fontSize = 16.sp,
                color = AppTheme.TextSecondary,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun WeeklyEnergyChart(dailyHighlights: List<HoroscopeCalculator.DailyHighlight>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(StringKey.INSIGHTS_WEEKLY_ENERGY).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dailyHighlights.forEach { highlight ->
                    key(highlight.dayOfWeek) {
                        DailyEnergyBar(highlight)
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyEnergyBar(highlight: HoroscopeCalculator.DailyHighlight) {
    val language = LocalLanguage.current
    val animatedHeight by animateFloatAsState(
        targetValue = highlight.energy / 10f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "energy_bar_${highlight.dayOfWeek.name}"
    )

    val barColor = getEnergyColor(highlight.energy)
    val dayAbbrev = remember(highlight.dayOfWeek, language) {
        val locale = if (language == Language.NEPALI) Locale("ne", "NP") else Locale.ENGLISH
        highlight.date.format(DateTimeFormatter.ofPattern("EEE", locale))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AppTheme.ChipBackground),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animatedHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dayAbbrev.uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextMuted,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun KeyDatesSection(keyDates: List<HoroscopeCalculator.KeyDate>) {
    if (keyDates.isEmpty()) return

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(StringKey.INSIGHTS_KEY_DATES).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        keyDates.forEachIndexed { index, keyDate ->
            key("${keyDate.date}_${keyDate.event}") {
                KeyDateCard(keyDate)
                if (index < keyDates.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun KeyDateCard(keyDate: HoroscopeCalculator.KeyDate) {
    val language = LocalLanguage.current
    val indicatorColor = if (keyDate.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor
    val dayOfMonth = remember(keyDate.date, language) {
        val day = keyDate.date.dayOfMonth.toString()
        if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(day)
        else day
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(indicatorColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayOfMonth,
                    fontFamily = CinzelDecorativeFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = indicatorColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = keyDate.event,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = keyDate.significance,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    color = AppTheme.TextMuted,
                    lineHeight = 18.sp
                )
            }

            Icon(
                imageVector = if (keyDate.isPositive) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                contentDescription = null,
                tint = indicatorColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun WeeklyPredictionsSection(predictions: Map<HoroscopeCalculator.LifeArea, String>) {
    if (predictions.isEmpty()) return

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(StringKey.INSIGHTS_WEEKLY_OVERVIEW).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val entries = remember(predictions) { predictions.entries.toList() }

        entries.forEachIndexed { index, (area, prediction) ->
            key(area.name) {
                WeeklyAreaCard(area, prediction)
                if (index < entries.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun WeeklyAreaCard(area: HoroscopeCalculator.LifeArea, prediction: String) {
    var expanded by remember { mutableStateOf(false) }
    val areaConfig = getLifeAreaConfig(area)
    val interactionSource = remember { MutableInteractionSource() }

    // Get localized area name
    val localizedAreaName = stringResource(area.displayNameKey)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { expanded = !expanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(areaConfig.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = areaConfig.icon,
                        contentDescription = null,
                        tint = areaConfig.color,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = localizedAreaName,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                ExpandIcon(expanded = expanded)
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(200)) + expandVertically(tween(300)),
                exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
            ) {
                Text(
                    text = prediction,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 16.sp,
                    color = AppTheme.TextSecondary,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun WeeklyAdviceCard(advice: String) {
    // Pull-quote style with left gold border
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left accent border line
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .defaultMinSize(minHeight = 60.dp)
                    .background(AppTheme.AccentPrimary)
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(StringKey.INSIGHTS_WEEKLY_ADVICE).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.AccentPrimary,
                        letterSpacing = 2.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = advice,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 16.sp,
                    color = AppTheme.TextPrimary,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
private fun CurrentDashaCard(
    timeline: DashaCalculator.DashaTimeline,
    todayDate: LocalDate
) {
    val language = LocalLanguage.current
    val currentMahadasha = timeline.currentMahadasha ?: return
    val currentAntardasha = timeline.currentAntardasha

    val mahadashaProgress = remember(currentMahadasha, todayDate) {
        calculateProgress(currentMahadasha.startDate.toLocalDate(), currentMahadasha.endDate.toLocalDate(), todayDate)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(StringKey.DASHA_CURRENT_PERIOD).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.TextPrimary,
                    letterSpacing = 2.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .border(
                            width = 1.dp,
                            color = AppTheme.SuccessColor.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(2.dp)
                        )
                        .background(AppTheme.SuccessColor.copy(alpha = 0.08f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(StringKey.DASHA_ACTIVE).uppercase(),
                        fontFamily = SpaceGroteskFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.SuccessColor,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DashaPeriodRow(
                labelKey = StringKey.DASHA_MAHADASHA,
                planet = currentMahadasha.planet,
                startDate = currentMahadasha.startDate.toLocalDate(),
                endDate = currentMahadasha.endDate.toLocalDate(),
                todayDate = todayDate,
                isPrimary = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            DashaProgressBar(
                progress = mahadashaProgress,
                color = getPlanetColor(currentMahadasha.planet)
            )

            currentAntardasha?.let { antardasha ->
                val antardashaProgress = remember(antardasha, todayDate) {
                    calculateProgress(antardasha.startDate.toLocalDate(), antardasha.endDate.toLocalDate(), todayDate)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AppTheme.DividerColor)
                Spacer(modifier = Modifier.height(16.dp))

                DashaPeriodRow(
                    labelKey = StringKey.DASHA_ANTARDASHA,
                    planet = antardasha.planet,
                    startDate = antardasha.startDate.toLocalDate(),
                    endDate = antardasha.endDate.toLocalDate(),
                    todayDate = todayDate,
                    isPrimary = false
                )

                Spacer(modifier = Modifier.height(8.dp))

                DashaProgressBar(
                    progress = antardashaProgress,
                    color = getPlanetColor(antardasha.planet),
                    height = 6
                )

                timeline.currentPratyantardasha?.let { pratyantardasha ->
    Spacer(modifier = Modifier.height(12.dp))
    PratyantardashaRow(pratyantardasha)
}
            }
        }
    }
}

@Composable
private fun PratyantardashaRow(pratyantardasha: DashaCalculator.Pratyantardasha) {
    val language = LocalLanguage.current
    val planetColor = getPlanetColor(pratyantardasha.planet)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(StringKey.DASHA_PRATYANTARDASHA).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextMuted,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(planetColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = pratyantardasha.planet.symbol,
                fontFamily = SpaceGroteskFamily,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = planetColor
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = pratyantardasha.planet.getLocalizedName(language),
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = planetColor
        )
    }
}

@Composable
private fun DashaPeriodRow(
    labelKey: StringKey,
    planet: Planet,
    startDate: LocalDate,
    endDate: LocalDate,
    todayDate: LocalDate,
    isPrimary: Boolean
) {
    val language = LocalLanguage.current
    val planetColor = getPlanetColor(planet)
    val label = stringResource(labelKey)

    val dateRange = remember(startDate, endDate, language) {
        val format = InsightsFormatters.getMonthYear(language)
        val range = "${startDate.format(format)} - ${endDate.format(format)}"
        if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(range)
        else range
    }

    val daysRemaining = remember(endDate, todayDate) {
        ChronoUnit.DAYS.between(todayDate, endDate)
    }

    val formattedDuration = remember(daysRemaining, language) {
        formatDuration(daysRemaining, language)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(if (isPrimary) 44.dp else 36.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(planetColor.copy(alpha = 0.15f))
                .border(width = 1.dp, color = planetColor, shape = RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = planet.symbol,
                style = if (isPrimary) MaterialTheme.typography.titleMedium
                else MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = planetColor
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(StringKey.DASHA_PERIOD_NAME, label, planet.getLocalizedName(language)),
                fontFamily = PoppinsFontFamily,
                fontSize = if (isPrimary) 16.sp else 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Text(
                text = dateRange,
                fontFamily = SpaceGroteskFamily,
                fontSize = 10.sp,
                color = AppTheme.TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        if (daysRemaining > 0) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formattedDuration,
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.AccentPrimary
                )
                Text(
                    text = stringResource(StringKey.DASHA_REMAINING).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextMuted,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun DashaProgressBar(
    progress: Float,
    color: Color,
    height: Int = 8
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "dasha_progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(1.dp))
            .background(AppTheme.ChipBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(1.dp))
                .background(color)
        )
    }
}

@Composable
private fun DashaTimelinePreview(
    timeline: DashaCalculator.DashaTimeline,
    todayDate: LocalDate
) {
    val currentMahadasha = timeline.currentMahadasha ?: return
    val currentAntardasha = timeline.currentAntardasha ?: return

    val upcomingAntardashas = remember(currentMahadasha, currentAntardasha) {
        val currentIndex = currentMahadasha.antardashas.indexOf(currentAntardasha)
        currentMahadasha.antardashas.drop(currentIndex + 1).take(3)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(StringKey.DASHA_UPCOMING).uppercase(),
                fontFamily = SpaceGroteskFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (upcomingAntardashas.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(2.dp))
                        .border(
                            width = 1.dp,
                            color = AppTheme.BorderColor,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .background(AppTheme.ChipBackground)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(StringKey.DASHA_LAST_IN_MAHADASHA),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 14.sp,
                        color = AppTheme.TextMuted
                    )
                }
            } else {
                upcomingAntardashas.forEachIndexed { index, antardasha ->
                    key("${antardasha.planet.name}_${antardasha.startDate}") {
                        UpcomingPeriodItem(
                            planet = antardasha.planet,
                            mahadashaPlanet = currentMahadasha.planet,
                            startDate = antardasha.startDate.toLocalDate(),
                            todayDate = todayDate,
                            isFirst = index == 0
                        )
                        if (index < upcomingAntardashas.lastIndex) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingPeriodItem(
    planet: Planet,
    mahadashaPlanet: Planet,
    startDate: LocalDate,
    todayDate: LocalDate,
    isFirst: Boolean
) {
    val language = LocalLanguage.current
    val planetColor = getPlanetColor(planet)

    val formattedDate = remember(startDate, language) {
        startDate.format(InsightsFormatters.getDayMonth(language))
    }

    val daysUntil = remember(startDate, todayDate) {
        ChronoUnit.DAYS.between(todayDate, startDate)
    }

    val durationText = formatDuration(daysUntil, language)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .then(
                if (isFirst) Modifier
                    .border(
                        width = 1.dp,
                        color = AppTheme.BorderColor,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .background(AppTheme.ChipBackground)
                else Modifier
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(planetColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = planet.symbol,
                fontFamily = SpaceGroteskFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = planetColor
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(StringKey.DASHA_COMBINED_NAME, mahadashaPlanet.getLocalizedName(language), planet.getLocalizedName(language)),
                fontFamily = PoppinsFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )
            Text(
                text = stringResource(StringKey.DASHA_STARTS, formattedDate),
                fontFamily = SpaceGroteskFamily,
                fontSize = 10.sp,
                color = AppTheme.TextMuted,
                letterSpacing = 0.5.sp
            )
        }

        if (daysUntil > 0) {
            Text(
                text = stringResource(StringKeyMatch.TIME_IN, durationText),
                fontFamily = SpaceGroteskFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (isFirst) AppTheme.AccentPrimary else AppTheme.TextMuted,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun PlanetaryTransitsSection(influences: List<HoroscopeCalculator.PlanetaryInfluence>) {
    val displayedInfluences = remember(influences) { influences.take(6) }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(StringKey.TRANSITS_CURRENT).uppercase(),
            fontFamily = SpaceGroteskFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(
                items = displayedInfluences,
                key = { it.planet.name }
            ) { influence ->
                TransitCard(influence)
            }
        }
    }
}

@Composable
private fun TransitCard(influence: HoroscopeCalculator.PlanetaryInfluence) {
    val language = LocalLanguage.current
    val planetColor = getPlanetColor(influence.planet)
    val trendColor = if (influence.isPositive) AppTheme.SuccessColor else AppTheme.WarningColor
    val strengthDots = remember(influence.strength) { (influence.strength / 2).coerceIn(0, 5) }

    Surface(
        modifier = Modifier.width(160.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, AppTheme.BorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(planetColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = influence.planet.symbol,
                        fontFamily = SpaceGroteskFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = planetColor
                    )
                }

                Icon(
                    imageVector = if (influence.isPositive) Icons.Filled.TrendingUp
                    else Icons.Filled.TrendingDown,
                    contentDescription = null,
                    tint = trendColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = influence.planet.getLocalizedName(language),
                fontFamily = PoppinsFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = influence.influence,
                fontFamily = PoppinsFontFamily,
                fontSize = 14.sp,
                color = AppTheme.TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Square indicators for the grid feel
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(
                                if (index < strengthDots) trendColor else AppTheme.ChipBackground
                            )
                    )
                    if (index < 4) Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

/**
 * Minimal Empty State UI for Insights Screen
 * Neo-Vedic "The Oracle" design with Cinzel header, Cormorant body, outlined gold button
 */
@Composable
private fun EmptyInsightsState(
    onCreateChart: () -> Unit = {}
) {
    val colors = AppTheme.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.ScreenBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Square icon container with border for Neo-Vedic grid feel
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(
                        width = 1.dp,
                        color = colors.BorderColor,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .background(colors.ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Insights,
                    contentDescription = null,
                    tint = colors.TextMuted,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(StringKey.SETTINGS_NO_PROFILE),
                fontFamily = CinzelDecorativeFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(StringKey.SETTINGS_TAP_TO_SELECT),
                fontFamily = PoppinsFontFamily,
                fontSize = 16.sp,
                color = colors.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Neo-Vedic outlined button with sharp 2dp corners and gold accent
            OutlinedButton(
                onClick = onCreateChart,
                modifier = Modifier
                    .height(52.dp)
                    .widthIn(min = 200.dp),
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, colors.AccentGold),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.AccentGold
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(StringKey.BTN_CREATE_CHART).uppercase(),
                    fontFamily = SpaceGroteskFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

private fun calculateProgress(startDate: LocalDate, endDate: LocalDate, todayDate: LocalDate): Float {
    if (todayDate.isBefore(startDate)) return 0f
    if (todayDate.isAfter(endDate)) return 1f

    val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toFloat()
    if (totalDays <= 0) return 1f

    val elapsedDays = ChronoUnit.DAYS.between(startDate, todayDate).toFloat()
    return (elapsedDays / totalDays).coerceIn(0f, 1f)
}

private fun formatDuration(days: Long, language: Language): String {
    if (days <= 0) {
        val zero = "0"
        val d = StringResources.get(StringKey.DAYS_SHORT, language)
        return if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(zero) + d else zero + d
    }

    val d = StringResources.get(StringKey.DAYS_SHORT, language)
    val w = StringResources.get(StringKey.WEEKS_SHORT, language)
    val m = StringResources.get(StringKey.MONTHS_SHORT, language)
    val y = StringResources.get(StringKey.YEARS_SHORT, language)

    fun num(n: Long): String = if (language == Language.NEPALI) com.astro.storm.core.common.BikramSambatConverter.toNepaliNumerals(n.toString()) else n.toString()

    return when {
        days < 7 -> "${num(days)}$d"
        days < 30 -> {
            val weeks = days / 7
            val remainingDays = days % 7
            if (remainingDays == 0L) "${num(weeks)}$w" else "${num(weeks)}$w ${num(remainingDays)}$d"
        }
        days < 365 -> {
            val months = days / 30
            val remainingDays = days % 30
            when {
                remainingDays == 0L -> "${num(months)}$m"
                remainingDays < 7 -> "${num(months)}$m ${num(remainingDays)}$d"
                else -> "${num(months)}$m ${num(remainingDays / 7)}$w"
            }
        }
        else -> {
            val years = days / 365
            val remainingDays = days % 365
            val months = remainingDays / 30
            if (months == 0L) "${num(years)}$y" else "${num(years)}$y ${num(months)}$m"
        }
    }
}

private fun resolveZoneId(timezone: String?): ZoneId {
    if (timezone.isNullOrBlank()) return ZoneId.systemDefault()
    return try {
        ZoneId.of(timezone)
    } catch (_: DateTimeException) {
        val numericHours = timezone.trim().toDoubleOrNull()
        if (numericHours != null) {
            val totalSeconds = (numericHours * 3600.0).roundToInt()
            ZoneOffset.ofTotalSeconds(totalSeconds.coerceIn(-18 * 3600, 18 * 3600))
        } else {
            ZoneId.systemDefault()
        }
    }
}

@Composable
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

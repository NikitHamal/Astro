package com.astro.storm.ui.screen.chartdetail.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Brightness2
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.core.common.Language
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.core.common.StringKeyDosha
import com.astro.storm.core.common.StringKeyPanchanga
import com.astro.storm.core.common.StringKeyNakshatra
import com.astro.storm.core.common.StringKeyMatch
import com.astro.storm.core.common.StringResources
import com.astro.storm.core.common.PanchangaDataProvider
import com.astro.storm.core.common.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.model.Nakshatra
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ephemeris.PanchangaCalculator
import com.astro.storm.ephemeris.panchanga.*
import com.astro.storm.ui.screen.chartdetail.ChartDetailColors
import java.util.Locale

@Composable
fun PanchangaTabContent(chart: VedicChart) {
    val context = LocalContext.current

    val panchanga = remember(chart) {
        PanchangaCalculator(context).use { calculator ->
            calculator.calculatePanchanga(
                dateTime = chart.birthData.dateTime,
                latitude = chart.birthData.latitude,
                longitude = chart.birthData.longitude,
                timezone = chart.birthData.timezone
            )
        }
    }

    var expandedCards by rememberSaveable { mutableStateOf(setOf<String>()) }

    fun toggleCard(cardKey: String, expand: Boolean) {
        expandedCards = if (expand) expandedCards + cardKey else expandedCards - cardKey
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "summary") {
            PanchangaSummaryCard(panchanga)
        }

        item(key = "tithi") {
            TithiCard(
                panchanga = panchanga,
                isExpanded = "tithi" in expandedCards,
                onToggleExpand = { toggleCard("tithi", it) }
            )
        }

        item(key = "nakshatra") {
            NakshatraCard(
                panchanga = panchanga,
                isExpanded = "nakshatra" in expandedCards,
                onToggleExpand = { toggleCard("nakshatra", it) }
            )
        }

        item(key = "yoga") {
            YogaCard(
                panchanga = panchanga,
                isExpanded = "yoga" in expandedCards,
                onToggleExpand = { toggleCard("yoga", it) }
            )
        }

        item(key = "karana") {
            KaranaCard(
                panchanga = panchanga,
                isExpanded = "karana" in expandedCards,
                onToggleExpand = { toggleCard("karana", it) }
            )
        }

        item(key = "vara") {
            VaraCard(
                panchanga = panchanga,
                isExpanded = "vara" in expandedCards,
                onToggleExpand = { toggleCard("vara", it) }
            )
        }

        item(key = "info") {
            PanchangaInfoCard(
                isExpanded = "info" in expandedCards,
                onToggleExpand = { toggleCard("info", it) }
            )
        }

        item(key = "spacer") {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PanchangaSummaryCard(panchanga: PanchangaData) {
    val language = LocalLanguage.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Panchanga summary for birth time" },
        shape = RoundedCornerShape(20.dp),
        color = ChartDetailColors.CardBackground,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    Icons.Outlined.WbSunny,
                    contentDescription = null,
                    tint = ChartDetailColors.AccentGold,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(StringKeyPanchanga.PANCHANGA_AT_BIRTH),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChartDetailColors.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyPanchanga.PANCHANGA_SANSKRIT),
                        fontSize = 12.sp,
                        color = ChartDetailColors.TextMuted
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PanchangaElement(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_TITHI),
                    sanskrit = stringResource(StringKeyPanchanga.PANCHANGA_LUNAR_DAY_SANSKRIT),
                    value = panchanga.tithi.tithi.getLocalizedName(language),
                    color = ChartDetailColors.AccentTeal
                )
                PanchangaElement(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_NAKSHATRA_LABEL),
                    sanskrit = stringResource(StringKeyPanchanga.PANCHANGA_LUNAR_MANSION_SANSKRIT),
                    value = panchanga.nakshatra.nakshatra.getLocalizedName(language),
                    color = ChartDetailColors.AccentPurple
                )
                PanchangaElement(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_YOGA),
                    sanskrit = stringResource(StringKeyPanchanga.PANCHANGA_LUNISOLAR_SANSKRIT),
                    value = panchanga.yoga.yoga.getLocalizedName(language),
                    color = ChartDetailColors.AccentGold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PanchangaElement(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_KARANA),
                    sanskrit = stringResource(StringKeyPanchanga.PANCHANGA_HALF_LUNAR_DAY_SANSKRIT),
                    value = panchanga.karana.karana.getLocalizedName(language),
                    color = ChartDetailColors.AccentBlue
                )
                PanchangaElement(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_VARA),
                    sanskrit = stringResource(StringKeyPanchanga.PANCHANGA_WEEKDAY_SANSKRIT),
                    value = panchanga.vara.getLocalizedName(language),
                    color = ChartDetailColors.AccentOrange
                )
            }

            HorizontalDivider(
                color = ChartDetailColors.DividerColor,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SunTimeIndicator(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_SUNRISE),
                    time = panchanga.sunrise,
                    icon = Icons.Outlined.WbSunny,
                    color = ChartDetailColors.AccentGold
                )
                MoonPhaseIndicator(
                    phase = panchanga.moonPhase,
                    paksha = panchanga.paksha.getLocalizedName(language)
                )
                SunTimeIndicator(
                    label = stringResource(StringKeyPanchanga.PANCHANGA_SUNSET),
                    time = panchanga.sunset,
                    icon = Icons.Outlined.WbTwilight,
                    color = ChartDetailColors.AccentOrange
                )
            }
        }
    }
}

@Composable
private fun SunTimeIndicator(
    label: String,
    time: String,
    icon: ImageVector,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = "$label at $time",
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = ChartDetailColors.TextMuted
        )
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun MoonPhaseIndicator(phase: Double, paksha: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            MoonPhaseCanvas(phase = phase)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = paksha,
            fontSize = 10.sp,
            color = ChartDetailColors.TextMuted
        )
        Text(
            text = "${String.format(Locale.US, "%.1f", phase)}%",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ChartDetailColors.AccentPurple
        )
    }
}

@Composable
private fun MoonPhaseCanvas(phase: Double) {
    val illuminatedColor = ChartDetailColors.AccentPurple
    val darkColor = ChartDetailColors.CardBackground

    Canvas(
        modifier = Modifier
            .size(40.dp)
            .semantics { contentDescription = "Moon phase ${phase.toInt()}% illuminated" }
    ) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(
            color = darkColor,
            radius = radius,
            center = center
        )

        drawCircle(
            color = illuminatedColor.copy(alpha = 0.3f),
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        val illuminationFraction = (phase / 100).toFloat().coerceIn(0f, 1f)
        val sweepAngle = 360f * illuminationFraction

        drawArc(
            color = illuminatedColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
    }
}

@Composable
private fun PanchangaElement(
    label: String,
    sanskrit: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = ChartDetailColors.TextMuted
        )
        Text(
            text = sanskrit,
            fontSize = 9.sp,
            color = ChartDetailColors.TextMuted.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = color.copy(alpha = 0.12f)
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun TithiCard(
    panchanga: PanchangaData,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val language = LocalLanguage.current
    val tithiData = PanchangaDataProvider.getTithiData(panchanga.tithi.number)

    ExpandableDetailCard(
        title = stringResource(StringKeyPanchanga.PANCHANGA_TITHI),
        subtitle = stringResource(StringKeyPanchanga.PANCHANGA_TITHI_SUBTITLE),
        value = panchanga.tithi.tithi.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.Brightness4,
        iconColor = ChartDetailColors.AccentTeal,
        qualityIndicator = tithiData.quality.toUiQuality()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_SANSKRIT_LABEL), panchanga.tithi.tithi.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NUMBER), "${panchanga.tithi.number} of 30", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_PAKSHA), panchanga.paksha.getLocalizedName(language), ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_DEITY), tithiData.getDeity(language), ChartDetailColors.AccentPurple)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_LORD), panchanga.tithi.lord.getLocalizedName(language), ChartDetailColors.AccentTeal)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NATURE), tithiData.getNature(language), getQualityColor(tithiData.quality.toUiQuality()))
            ProgressRow(stringResource(StringKeyPanchanga.PANCHANGA_PROGRESS), panchanga.tithi.progress, ChartDetailColors.AccentGold)

            DescriptionSection(
                title = stringResource(StringKeyPanchanga.PANCHANGA_SIGNIFICANCE),
                description = tithiData.getDescription(language)
            )

            if (tithiData.getActivities(language).isNotEmpty()) {
                ActivitiesSection(
                    favorable = tithiData.getActivities(language),
                    unfavorable = tithiData.getAvoid(language)
                )
            }
        }
    }
}

@Composable
private fun NakshatraCard(
    panchanga: PanchangaData,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val language = LocalLanguage.current
    val nakshatraData = PanchangaDataProvider.getNakshatraData(panchanga.nakshatra.nakshatra)

    ExpandableDetailCard(
        title = stringResource(StringKeyPanchanga.PANCHANGA_NAKSHATRA_LABEL),
        subtitle = stringResource(StringKeyPanchanga.PANCHANGA_NAKSHATRA_SUBTITLE),
        value = panchanga.nakshatra.nakshatra.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.Star,
        iconColor = ChartDetailColors.AccentPurple,
        qualityIndicator = nakshatraData.quality.toUiQuality()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_SANSKRIT_LABEL), nakshatraData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NUMBER), "${panchanga.nakshatra.number} of 27", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_PADA), "${panchanga.nakshatra.pada} of 4", ChartDetailColors.AccentTeal)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_RULER), panchanga.nakshatra.lord.getLocalizedName(language), ChartDetailColors.AccentGold)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_DEITY), nakshatraData.getDeity(language), ChartDetailColors.AccentPurple)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_SYMBOL), nakshatraData.getSymbol(language), ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_GANA), PanchangaDataProvider.getGanaName(nakshatraData.gana, language), getGanaColor(nakshatraData.gana))
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_GUNA), PanchangaDataProvider.getGunaName(nakshatraData.guna, language), ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_ANIMAL), nakshatraData.getAnimal(language), ChartDetailColors.TextSecondary)
            ProgressRow(stringResource(StringKeyPanchanga.PANCHANGA_PROGRESS), panchanga.nakshatra.progress, ChartDetailColors.AccentGold)

            DescriptionSection(
                title = stringResource(StringKeyPanchanga.PANCHANGA_CHARACTERISTICS),
                description = nakshatraData.getDescription(language)
            )
        }
    }
}

@Composable
private fun YogaCard(
    panchanga: PanchangaData,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val language = LocalLanguage.current
    val yogaData = PanchangaDataProvider.getYogaData(panchanga.yoga.yoga)

    ExpandableDetailCard(
        title = stringResource(StringKeyPanchanga.PANCHANGA_YOGA),
        subtitle = stringResource(StringKeyPanchanga.PANCHANGA_YOGA_SUBTITLE),
        value = panchanga.yoga.yoga.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.AutoAwesome,
        iconColor = ChartDetailColors.AccentGold,
        qualityIndicator = yogaData.quality.toUiQuality()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_SANSKRIT_LABEL), yogaData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NUMBER), "${panchanga.yoga.number} of 27", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_MEANING), yogaData.getMeaning(language), ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NATURE), panchanga.yoga.yoga.nature.getLocalizedName(language), getQualityColor(yogaData.quality.toUiQuality()))
            ProgressRow(stringResource(StringKeyPanchanga.PANCHANGA_PROGRESS), panchanga.yoga.progress, ChartDetailColors.AccentTeal)

            DescriptionSection(
                title = stringResource(StringKeyPanchanga.PANCHANGA_EFFECTS),
                description = yogaData.getDescription(language)
            )
        }
    }
}

@Composable
private fun KaranaCard(
    panchanga: PanchangaData,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val language = LocalLanguage.current
    val karanaData = PanchangaDataProvider.getKaranaData(panchanga.karana.karana)

    ExpandableDetailCard(
        title = stringResource(StringKeyPanchanga.PANCHANGA_KARANA),
        subtitle = stringResource(StringKeyPanchanga.PANCHANGA_KARANA_SUBTITLE),
        value = panchanga.karana.karana.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.Schedule,
        iconColor = ChartDetailColors.AccentBlue,
        qualityIndicator = karanaData.quality.toUiQuality()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_SANSKRIT_LABEL), karanaData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NUMBER), "${panchanga.karana.number} of 60", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_TYPE), karanaData.getType(language), ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_NATURE), panchanga.karana.karana.type.getLocalizedName(language), getQualityColor(karanaData.quality.toUiQuality()))
            ProgressRow(stringResource(StringKeyPanchanga.PANCHANGA_PROGRESS), panchanga.karana.progress, ChartDetailColors.AccentGold)

            DescriptionSection(
                title = stringResource(StringKeyPanchanga.PANCHANGA_SIGNIFICANCE),
                description = karanaData.getDescription(language)
            )
        }
    }
}

@Composable
private fun VaraCard(
    panchanga: PanchangaData,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val language = LocalLanguage.current
    val varaData = PanchangaDataProvider.getVaraData(panchanga.vara.number)

    ExpandableDetailCard(
        title = stringResource(StringKeyPanchanga.PANCHANGA_VARA),
        subtitle = stringResource(StringKeyPanchanga.PANCHANGA_VARA_SUBTITLE),
        value = panchanga.vara.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.CalendarMonth,
        iconColor = ChartDetailColors.AccentOrange,
        qualityIndicator = null
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_SANSKRIT_LABEL), varaData.rulerNe, ChartDetailColors.TextSecondary) // rulerNe is often the sanskrit name for vara
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_RULING_PLANET), panchanga.vara.lord.getLocalizedName(language), ChartDetailColors.getPlanetColor(panchanga.vara.lord))
            DetailRow(stringResource(StringKeyPanchanga.PANCHANGA_ELEMENT), stringResource(if (panchanga.vara == Vara.SUNDAY || panchanga.vara == Vara.TUESDAY) StringKeyNakshatra.NAKSHATRA_ELEMENT_FIRE else StringKeyNakshatra.NAKSHATRA_ELEMENT_EARTH), ChartDetailColors.TextSecondary)
            
            DescriptionSection(
                title = stringResource(StringKeyPanchanga.PANCHANGA_SIGNIFICANCE),
                description = varaData.getDescription(language)
            )

            if (varaData.getActivities(language).isNotEmpty()) {
                ActivitiesSection(
                    favorable = varaData.getActivities(language),
                    unfavorable = varaData.getAvoid(language)
                )
            }
        }
    }
}

private fun PanchangaDataProvider.Quality.toUiQuality(): Quality = when (this) {
    PanchangaDataProvider.Quality.EXCELLENT -> Quality.EXCELLENT
    PanchangaDataProvider.Quality.GOOD -> Quality.GOOD
    PanchangaDataProvider.Quality.NEUTRAL -> Quality.NEUTRAL
    PanchangaDataProvider.Quality.CHALLENGING -> Quality.CHALLENGING
}

@Composable
private fun ExpandableDetailCard(
    title: String,
    subtitle: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit,
    qualityIndicator: Quality?,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "expand_rotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand(!isExpanded) }
            .semantics { contentDescription = "$title: $value. Tap to ${if (isExpanded) "collapse" else "expand"}" },
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ChartDetailColors.TextPrimary
                            )
                            qualityIndicator?.let { quality ->
                                Spacer(modifier = Modifier.width(8.dp))
                                QualityBadge(quality)
                            }
                        }
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = ChartDetailColors.TextMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = iconColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = value,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = iconColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = ChartDetailColors.TextMuted,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotation)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(
                        color = ChartDetailColors.DividerColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    content()
                }
            }
        }
    }
}

@Composable
private fun QualityBadge(quality: Quality) {
    val (color, text) = when (quality) {
        Quality.EXCELLENT -> ChartDetailColors.SuccessColor to stringResource(StringKeyAnalysisPart1.QUALITY_EXCELLENT)
        Quality.GOOD -> ChartDetailColors.AccentTeal to stringResource(StringKeyAnalysisPart1.QUALITY_GOOD)
        Quality.NEUTRAL -> ChartDetailColors.TextMuted to stringResource(StringKeyAnalysisPart1.QUALITY_NEUTRAL)
        Quality.CHALLENGING -> ChartDetailColors.WarningColor to stringResource(StringKeyAnalysisPart1.QUALITY_CHALLENGING)
        Quality.INAUSPICIOUS -> ChartDetailColors.ErrorColor to stringResource(StringKeyAnalysisPart1.QUALITY_INAUSPICIOUS)
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = ChartDetailColors.TextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

@Composable
private fun ProgressRow(
    label: String,
    progress: Double,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = ChartDetailColors.TextMuted
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(2.dp),
                    color = color.copy(alpha = 0.2f)
                ) {}
                Surface(
                    modifier = Modifier
                        .fillMaxWidth((progress / 100).toFloat().coerceIn(0f, 1f))
                        .height(4.dp),
                    shape = RoundedCornerShape(2.dp),
                    color = color
                ) {}
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${String.format(Locale.US, "%.1f", progress)}%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
private fun DescriptionSection(
    title: String,
    description: String
) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        HorizontalDivider(
            color = ChartDetailColors.DividerColor,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = ChartDetailColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            fontSize = 13.sp,
            color = ChartDetailColors.TextPrimary,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ActivitiesSection(
    favorable: List<String>,
    unfavorable: List<String>
) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        if (favorable.isNotEmpty()) {
            Text(
                text = stringResource(StringKeyPanchanga.PANCHANGA_FAVORABLE_ACTIVITIES),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChartDetailColors.SuccessColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = favorable.joinToString(", "),
                fontSize = 12.sp,
                color = ChartDetailColors.TextSecondary,
                lineHeight = 18.sp
            )
        }

        if (unfavorable.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(StringKeyPanchanga.PANCHANGA_AVOID),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChartDetailColors.WarningColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = unfavorable.joinToString(", "),
                fontSize = 12.sp,
                color = ChartDetailColors.TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun PanchangaInfoCard(
    isExpanded: Boolean,
    onToggleExpand: (Boolean) -> Unit
) {
    val language = LocalLanguage.current
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "info_rotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand(!isExpanded) },
        shape = RoundedCornerShape(16.dp),
        color = ChartDetailColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = ChartDetailColors.AccentPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(StringKeyPanchanga.PANCHANGA_ABOUT),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChartDetailColors.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyPanchanga.PANCHANGA_ABOUT_SUBTITLE),
                            fontSize = 11.sp,
                            color = ChartDetailColors.TextMuted
                        )
                    }
                }
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ChartDetailColors.TextMuted,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(
                        color = ChartDetailColors.DividerColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = stringResource(StringKeyPanchanga.PANCHANGA_ABOUT_DESCRIPTION),
                        fontSize = 13.sp,
                        color = ChartDetailColors.TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val elements = listOf(
                        Triple(stringResource(StringKeyPanchanga.PANCHANGA_INFO_TITHI_TITLE), stringResource(StringKeyPanchanga.PANCHANGA_INFO_TITHI_LABEL), stringResource(StringKeyPanchanga.PANCHANGA_TITHI_DESC)),
                        Triple(stringResource(StringKeyPanchanga.PANCHANGA_INFO_NAKSHATRA_TITLE), stringResource(StringKeyPanchanga.PANCHANGA_INFO_NAKSHATRA_LABEL), stringResource(StringKeyPanchanga.PANCHANGA_NAKSHATRA_DESC)),
                        Triple(stringResource(StringKeyPanchanga.PANCHANGA_INFO_YOGA_TITLE), stringResource(StringKeyPanchanga.PANCHANGA_INFO_YOGA_LABEL), stringResource(StringKeyPanchanga.PANCHANGA_YOGA_DESC)),
                        Triple(stringResource(StringKeyPanchanga.PANCHANGA_INFO_KARANA_TITLE), stringResource(StringKeyPanchanga.PANCHANGA_INFO_KARANA_LABEL), stringResource(StringKeyPanchanga.PANCHANGA_KARANA_DESC)),
                        Triple(stringResource(StringKeyPanchanga.PANCHANGA_INFO_VARA_TITLE), stringResource(StringKeyPanchanga.PANCHANGA_INFO_VARA_LABEL), stringResource(StringKeyPanchanga.PANCHANGA_VARA_DESC))
                    )

                    elements.forEach { (name, subtitle, description) ->
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                fontSize = 14.sp,
                                color = ChartDetailColors.AccentGold,
                                modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                            )
                            Column {
                                Text(
                                    text = name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ChartDetailColors.AccentTeal
                                )
                                Text(
                                    text = subtitle,
                                    fontSize = 11.sp,
                                    color = ChartDetailColors.TextMuted
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = description,
                                    fontSize = 12.sp,
                                    color = ChartDetailColors.TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ChartDetailColors.AccentGold.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = stringResource(StringKeyPanchanga.PANCHANGA_BIRTH_INSIGHT),
                            fontSize = 12.sp,
                            color = ChartDetailColors.TextSecondary,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

private enum class Quality {
    EXCELLENT, GOOD, NEUTRAL, CHALLENGING, INAUSPICIOUS
}

@Composable
private fun getQualityColor(quality: Quality): Color {
    return when (quality) {
        Quality.EXCELLENT -> ChartDetailColors.SuccessColor
        Quality.GOOD -> ChartDetailColors.AccentTeal
        Quality.NEUTRAL -> ChartDetailColors.TextSecondary
        Quality.CHALLENGING -> ChartDetailColors.WarningColor
        Quality.INAUSPICIOUS -> ChartDetailColors.ErrorColor
    }
}

@Composable
private fun getGanaColor(gana: String): Color {
    return when (gana.lowercase()) {
        "deva", "देव" -> ChartDetailColors.AccentGold
        "manushya", "मनुष्य" -> ChartDetailColors.AccentTeal
        "rakshasa", "राक्षस" -> ChartDetailColors.AccentOrange
        else -> ChartDetailColors.TextSecondary
    }
}


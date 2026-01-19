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
import com.astro.storm.core.common.StringKeyMatch
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
                        text = stringResource(StringKeyAnalysis.PANCHANGA_AT_BIRTH),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChartDetailColors.TextPrimary
                    )
                    Text(
                        text = stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT),
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
                    label = stringResource(StringKeyMatch.PANCHANGA_TITHI),
                    sanskrit = stringResource(StringKeyAnalysis.PANCHANGA_LUNAR_DAY_SANSKRIT),
                    value = panchanga.tithi.tithi.getLocalizedName(language),
                    color = ChartDetailColors.AccentTeal
                )
                PanchangaElement(
                    label = stringResource(StringKeyMatch.PANCHANGA_NAKSHATRA_LABEL),
                    sanskrit = stringResource(StringKeyAnalysis.PANCHANGA_LUNAR_MANSION_SANSKRIT),
                    value = panchanga.nakshatra.nakshatra.getLocalizedName(language),
                    color = ChartDetailColors.AccentPurple
                )
                PanchangaElement(
                    label = stringResource(StringKeyMatch.PANCHANGA_YOGA),
                    sanskrit = stringResource(StringKeyAnalysis.PANCHANGA_LUNISOLAR_SANSKRIT),
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
                    label = stringResource(StringKeyMatch.PANCHANGA_KARANA),
                    sanskrit = stringResource(StringKeyAnalysis.PANCHANGA_HALF_LUNAR_DAY_SANSKRIT),
                    value = panchanga.karana.karana.getLocalizedName(language),
                    color = ChartDetailColors.AccentBlue
                )
                PanchangaElement(
                    label = stringResource(StringKeyMatch.PANCHANGA_VARA),
                    sanskrit = stringResource(StringKeyAnalysis.PANCHANGA_WEEKDAY_SANSKRIT),
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
                    label = stringResource(StringKeyMatch.PANCHANGA_SUNRISE),
                    time = panchanga.sunrise,
                    icon = Icons.Outlined.WbSunny,
                    color = ChartDetailColors.AccentGold
                )
                MoonPhaseIndicator(
                    phase = panchanga.moonPhase,
                    paksha = panchanga.paksha.getLocalizedName(language)
                )
                SunTimeIndicator(
                    label = stringResource(StringKeyMatch.PANCHANGA_SUNSET),
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
    val tithiData = getTithiData(panchanga.tithi.tithi.number, language)

    ExpandableDetailCard(
        title = stringResource(StringKeyMatch.PANCHANGA_TITHI),
        subtitle = stringResource(StringKeyAnalysis.PANCHANGA_TITHI_SUBTITLE),
        value = panchanga.tithi.tithi.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.Brightness4,
        iconColor = ChartDetailColors.AccentTeal,
        qualityIndicator = tithiData.quality
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT_LABEL), panchanga.tithi.tithi.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NUMBER), "${panchanga.tithi.number} of 30", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyMatch.PANCHANGA_PAKSHA), panchanga.paksha.getLocalizedName(language), ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_DEITY), tithiData.deity, ChartDetailColors.AccentPurple)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_LORD), panchanga.tithi.lord.getLocalizedName(language), ChartDetailColors.AccentTeal)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NATURE), tithiData.nature, getQualityColor(tithiData.quality))
            ProgressRow(stringResource(StringKeyAnalysis.PANCHANGA_PROGRESS), panchanga.tithi.progress, ChartDetailColors.AccentGold)

            DescriptionSection(
                title = stringResource(StringKeyAnalysis.PANCHANGA_SIGNIFICANCE),
                description = tithiData.description
            )

            if (tithiData.activities.isNotEmpty()) {
                ActivitiesSection(
                    favorable = tithiData.activities,
                    unfavorable = tithiData.avoid
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
    val nakshatraData = getNakshatraData(panchanga.nakshatra.nakshatra, language)

    ExpandableDetailCard(
        title = stringResource(StringKeyMatch.PANCHANGA_NAKSHATRA_LABEL),
        subtitle = stringResource(StringKeyAnalysis.PANCHANGA_NAKSHATRA_SUBTITLE),
        value = panchanga.nakshatra.nakshatra.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.Star,
        iconColor = ChartDetailColors.AccentPurple,
        qualityIndicator = nakshatraData.quality
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT_LABEL), nakshatraData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NUMBER), "${panchanga.nakshatra.number} of 27", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_PADA), "${panchanga.nakshatra.pada} of 4", ChartDetailColors.AccentTeal)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_RULER), panchanga.nakshatra.lord.getLocalizedName(language), ChartDetailColors.AccentGold)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_DEITY), nakshatraData.deity, ChartDetailColors.AccentPurple)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SYMBOL), nakshatraData.symbol, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_GANA), nakshatraData.gana, getGanaColor(nakshatraData.gana))
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_GUNA), nakshatraData.guna, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_ANIMAL), nakshatraData.animal, ChartDetailColors.TextSecondary)
            ProgressRow(stringResource(StringKeyAnalysis.PANCHANGA_PROGRESS), panchanga.nakshatra.progress, ChartDetailColors.AccentGold)

            DescriptionSection(
                title = stringResource(StringKeyAnalysis.PANCHANGA_CHARACTERISTICS),
                description = nakshatraData.description
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
    val yogaData = getYogaData(panchanga.yoga.yoga, language)

    ExpandableDetailCard(
        title = stringResource(StringKeyMatch.PANCHANGA_YOGA),
        subtitle = stringResource(StringKeyAnalysis.PANCHANGA_YOGA_SUBTITLE),
        value = panchanga.yoga.yoga.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.AutoAwesome,
        iconColor = ChartDetailColors.AccentGold,
        qualityIndicator = yogaData.quality
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT_LABEL), yogaData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NUMBER), "${panchanga.yoga.number} of 27", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_MEANING), yogaData.meaning, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NATURE), panchanga.yoga.yoga.nature.getLocalizedName(language), getQualityColor(yogaData.quality))
            ProgressRow(stringResource(StringKeyAnalysis.PANCHANGA_PROGRESS), panchanga.yoga.progress, ChartDetailColors.AccentTeal)

            DescriptionSection(
                title = stringResource(StringKeyAnalysis.PANCHANGA_EFFECTS),
                description = yogaData.description
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
    val karanaData = getKaranaData(panchanga.karana.karana, language)

    ExpandableDetailCard(
        title = stringResource(StringKeyMatch.PANCHANGA_KARANA),
        subtitle = stringResource(StringKeyAnalysis.PANCHANGA_KARANA_SUBTITLE),
        value = panchanga.karana.karana.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.Schedule,
        iconColor = ChartDetailColors.AccentBlue,
        qualityIndicator = karanaData.quality
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT_LABEL), karanaData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NUMBER), "${panchanga.karana.number} of 60", ChartDetailColors.TextPrimary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_TYPE), karanaData.type, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_NATURE), panchanga.karana.karana.type.getLocalizedName(language), getQualityColor(karanaData.quality))
            ProgressRow(stringResource(StringKeyAnalysis.PANCHANGA_PROGRESS), panchanga.karana.progress, ChartDetailColors.AccentGold)

            DescriptionSection(
                title = stringResource(StringKeyAnalysis.PANCHANGA_SIGNIFICANCE),
                description = karanaData.description
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
    val varaData = getVaraData(panchanga.vara, language)

    ExpandableDetailCard(
        title = stringResource(StringKeyMatch.PANCHANGA_VARA),
        subtitle = stringResource(StringKeyAnalysis.PANCHANGA_VARA_SUBTITLE),
        value = panchanga.vara.getLocalizedName(language),
        isExpanded = isExpanded,
        onToggleExpand = onToggleExpand,
        icon = Icons.Outlined.CalendarMonth,
        iconColor = ChartDetailColors.AccentOrange,
        qualityIndicator = null
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SANSKRIT_LABEL), varaData.sanskrit, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_RULING_PLANET), panchanga.vara.lord.getLocalizedName(language), ChartDetailColors.getPlanetColor(panchanga.vara.lord))
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_ELEMENT), varaData.element, ChartDetailColors.TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_DIRECTION), varaData.direction, ChartDetailColors.TextSecondary)

            DescriptionSection(
                title = stringResource(StringKeyAnalysis.PANCHANGA_SIGNIFICANCE),
                description = varaData.description
            )

            if (varaData.favorable.isNotEmpty()) {
                ActivitiesSection(
                    favorable = varaData.favorable,
                    unfavorable = varaData.unfavorable
                )
            }
        }
    }
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
        Quality.EXCELLENT -> ChartDetailColors.SuccessColor to stringResource(StringKeyAnalysis.QUALITY_EXCELLENT)
        Quality.GOOD -> ChartDetailColors.AccentTeal to stringResource(StringKeyAnalysis.QUALITY_GOOD)
        Quality.NEUTRAL -> ChartDetailColors.TextMuted to stringResource(StringKeyAnalysis.QUALITY_NEUTRAL)
        Quality.CHALLENGING -> ChartDetailColors.WarningColor to stringResource(StringKeyAnalysis.QUALITY_CHALLENGING)
        Quality.INAUSPICIOUS -> ChartDetailColors.ErrorColor to stringResource(StringKeyAnalysis.QUALITY_INAUSPICIOUS)
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
                text = stringResource(StringKeyAnalysis.PANCHANGA_FAVORABLE_ACTIVITIES),
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
                text = stringResource(StringKeyAnalysis.PANCHANGA_AVOID),
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
                            text = stringResource(StringKeyAnalysis.PANCHANGA_ABOUT),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChartDetailColors.TextPrimary
                        )
                        Text(
                            text = stringResource(StringKeyAnalysis.PANCHANGA_ABOUT_SUBTITLE),
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
                        text = stringResource(StringKeyAnalysis.PANCHANGA_ABOUT_DESCRIPTION),
                        fontSize = 13.sp,
                        color = ChartDetailColors.TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val elements = listOf(
                        Triple(stringResource(StringKeyAnalysis.PANCHANGA_INFO_TITHI_TITLE), stringResource(StringKeyAnalysis.PANCHANGA_INFO_TITHI_LABEL), stringResource(StringKeyAnalysis.PANCHANGA_TITHI_DESC)),
                        Triple(stringResource(StringKeyAnalysis.PANCHANGA_INFO_NAKSHATRA_TITLE), stringResource(StringKeyAnalysis.PANCHANGA_INFO_NAKSHATRA_LABEL), stringResource(StringKeyAnalysis.PANCHANGA_NAKSHATRA_DESC)),
                        Triple(stringResource(StringKeyAnalysis.PANCHANGA_INFO_YOGA_TITLE), stringResource(StringKeyAnalysis.PANCHANGA_INFO_YOGA_LABEL), stringResource(StringKeyAnalysis.PANCHANGA_YOGA_DESC)),
                        Triple(stringResource(StringKeyAnalysis.PANCHANGA_INFO_KARANA_TITLE), stringResource(StringKeyAnalysis.PANCHANGA_INFO_KARANA_LABEL), stringResource(StringKeyAnalysis.PANCHANGA_KARANA_DESC)),
                        Triple(stringResource(StringKeyAnalysis.PANCHANGA_INFO_VARA_TITLE), stringResource(StringKeyAnalysis.PANCHANGA_INFO_VARA_LABEL), stringResource(StringKeyAnalysis.PANCHANGA_VARA_DESC))
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
                            text = stringResource(StringKeyAnalysis.PANCHANGA_BIRTH_INSIGHT),
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
    return when (gana) {
        "Deva" -> ChartDetailColors.AccentGold
        "Manushya" -> ChartDetailColors.AccentTeal
        "Rakshasa" -> ChartDetailColors.AccentOrange
        else -> ChartDetailColors.TextSecondary
    }
}

private data class TithiData(
    val deity: String,
    val nature: String,
    val quality: Quality,
    val description: String,
    val activities: List<String>,
    val avoid: List<String>
)

import com.astro.storm.core.common.StringKeyPanchanga
import com.astro.storm.core.common.StringKeyNakshatra

private fun getTithiData(tithiNumber: Int, language: Language): TithiData {
    return when (tithiNumber) {
        1, 16 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_AGNI, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_NANDA, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_1, language),
            activities = listOf("New beginnings", "Starting ventures", "Foundation laying", "Travel"),
            avoid = listOf("Completing projects", "Endings")
        )
        2, 17 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_BRAHMA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_BHADRA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_2, language),
            activities = listOf("Creative work", "Naming ceremonies", "Marriage", "House warming"),
            avoid = listOf("Conflict", "Aggressive actions")
        )
        3, 18 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_GAURI, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_JAYA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_3, language),
            activities = listOf("Religious ceremonies", "Celebrations", "Victory rituals", "Arts"),
            avoid = listOf("Conflicts", "Harsh activities")
        )
        4, 19 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_GANESHA_YAMA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_RIKTA, language),
            quality = Quality.CHALLENGING,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_4, language),
            activities = listOf("Ganesha worship", "Removing obstacles", "Spiritual practices"),
            avoid = listOf("New beginnings", "Travel", "Important decisions")
        )
        5, 20 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_NAGAS, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_NANDA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_5, language),
            activities = listOf("Education", "Learning", "Writing", "Medicine", "Healing"),
            avoid = listOf("Destructive activities")
        )
        6, 21 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_KARTIKEYA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_BHADRA, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_6, language),
            activities = listOf("Medical treatments", "Surgery", "Overcoming obstacles", "Courage"),
            avoid = listOf("Timid actions", "Postponements")
        )
        7, 22 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_SURYA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_JAYA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_7, language),
            activities = listOf("Travel", "Pilgrimages", "Vehicle purchase", "Government work"),
            avoid = listOf("Night activities", "Moon-related work")
        )
        8, 23 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_SHIVA_RUDRA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_RIKTA, language),
            quality = Quality.NEUTRAL,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_8, language),
            activities = listOf("Shiva worship", "Fasting", "Spiritual practices", "Meditation"),
            avoid = listOf("New ventures", "Material pursuits", "Celebrations")
        )
        9, 24 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_DURGA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_NANDA, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_9, language),
            activities = listOf("Durga worship", "Protection rituals", "Overcoming enemies", "Strength"),
            avoid = listOf("Peaceful negotiations", "Gentle activities")
        )
        10, 25 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_YAMA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_BHADRA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_10, language),
            activities = listOf("Completing projects", "Victory celebrations", "Important tasks", "Success"),
            avoid = listOf("Beginning long-term projects")
        )
        11, 26 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_VISHNU, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_JAYA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_11, language),
            activities = listOf("Fasting", "Vishnu worship", "Spiritual practices", "Meditation", "Charity"),
            avoid = listOf("Material pursuits", "Eating grains", "Worldly pleasures")
        )
        12, 27 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_VISHNU, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_BHADRA, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_12, language),
            activities = listOf("Breaking fast", "Religious ceremonies", "Charity", "Feeding others"),
            avoid = listOf("Fasting continuation", "Heavy foods")
        )
        13, 28 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_KAMADEVA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_JAYA, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_13, language),
            activities = listOf("Shiva worship", "Romance", "Arts", "Music", "Celebrations"),
            avoid = listOf("Aggressive activities", "Conflicts")
        )
        14, 29 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_SHIVA_KALI, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_RIKTA, language),
            quality = Quality.CHALLENGING,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_14, language),
            activities = listOf("Tantric practices", "Shiva/Kali worship", "Removing negativity", "Spiritual austerities"),
            avoid = listOf("New beginnings", "Auspicious ceremonies", "Travel")
        )
        15 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_CHANDRA, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_PURNA, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_15, language),
            activities = listOf("All auspicious activities", "Celebrations", "Spiritual practices", "Charity", "Worship"),
            avoid = listOf("Surgery", "Activities requiring darkness")
        )
        30 -> TithiData(
            deity = StringResources.get(StringKeyPanchanga.TITHI_DEITY_PITRIS, language),
            nature = StringResources.get(StringKeyPanchanga.TITHI_NATURE_PURNA, language),
            quality = Quality.NEUTRAL,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_30, language),
            activities = listOf("Ancestral rites", "Kali worship", "Tantric practices", "Introspection", "Shadow work"),
            avoid = listOf("New beginnings", "Auspicious ceremonies", "Travel", "Important decisions")
        )
        else -> TithiData(
            deity = "Various",
            nature = "Mixed",
            quality = Quality.NEUTRAL,
            description = StringResources.get(StringKeyPanchanga.TITHI_DESC_MIXED, language),
            activities = listOf("General activities"),
            avoid = emptyList()
        )
    }
}

private data class NakshatraData(
    val sanskrit: String,
    val deity: String,
    val symbol: String,
    val gana: String,
    val guna: String,
    val animal: String,
    val quality: Quality,
    val description: String
)

private fun getNakshatraData(nakshatra: Nakshatra, language: Language): NakshatraData {
    return when (nakshatra) {
        Nakshatra.ASHWINI -> NakshatraData(
            sanskrit = "अश्विनी",
            deity = StringResources.get(StringKeyNakshatra.DEITY_ASHWINI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_ASHWINI, language),
            gana = "Deva",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_HORSE, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_ASHWINI, language)
        )
        Nakshatra.BHARANI -> NakshatraData(
            sanskrit = "भरणी",
            deity = StringResources.get(StringKeyNakshatra.DEITY_BHARANI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_BHARANI, language),
            gana = "Manushya",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_ELEPHANT, language),
            quality = Quality.NEUTRAL,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_BHARANI, language)
        )
        Nakshatra.KRITTIKA -> NakshatraData(
            sanskrit = "कृत्तिका",
            deity = StringResources.get(StringKeyNakshatra.DEITY_KRITTIKA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_KRITTIKA, language),
            gana = "Rakshasa",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_SHEEP, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_KRITTIKA, language)
        )
        Nakshatra.ROHINI -> NakshatraData(
            sanskrit = "रोहिणी",
            deity = StringResources.get(StringKeyNakshatra.DEITY_ROHINI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_ROHINI, language),
            gana = "Manushya",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_SERPENT, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_ROHINI, language)
        )
        Nakshatra.MRIGASHIRA -> NakshatraData(
            sanskrit = "मृगशिरा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_MRIGASHIRA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_MRIGASHIRA, language),
            gana = "Deva",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_SERPENT, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_MRIGASHIRA, language)
        )
        Nakshatra.ARDRA -> NakshatraData(
            sanskrit = "आर्द्रा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_ARDRA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_ARDRA, language),
            gana = "Manushya",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_DOG, language),
            quality = Quality.CHALLENGING,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_ARDRA, language)
        )
        Nakshatra.PUNARVASU -> NakshatraData(
            sanskrit = "पुनर्वसु",
            deity = StringResources.get(StringKeyNakshatra.DEITY_PUNARVASU, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_PUNARVASU, language),
            gana = "Deva",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_CAT, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_PUNARVASU, language)
        )
        Nakshatra.PUSHYA -> NakshatraData(
            sanskrit = "पुष्य",
            deity = StringResources.get(StringKeyNakshatra.DEITY_PUSHYA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_PUSHYA, language),
            gana = "Deva",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_SHEEP, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_PUSHYA, language)
        )
        Nakshatra.ASHLESHA -> NakshatraData(
            sanskrit = "आश्लेषा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_ASHLESHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_ASHLESHA, language),
            gana = "Rakshasa",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_CAT, language),
            quality = Quality.CHALLENGING,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_ASHLESHA, language)
        )
        Nakshatra.MAGHA -> NakshatraData(
            sanskrit = "मघा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_MAGHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_MAGHA, language),
            gana = "Rakshasa",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_RAT, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_MAGHA, language)
        )
        Nakshatra.PURVA_PHALGUNI -> NakshatraData(
            sanskrit = "पूर्वा फाल्गुनी",
            deity = StringResources.get(StringKeyNakshatra.DEITY_PURVA_PHALGUNI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_PURVA_PHALGUNI, language),
            gana = "Manushya",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_RAT, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_PURVA_PHALGUNI, language)
        )
        Nakshatra.UTTARA_PHALGUNI -> NakshatraData(
            sanskrit = "उत्तरा फाल्गुनी",
            deity = StringResources.get(StringKeyNakshatra.DEITY_UTTARA_PHALGUNI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_UTTARA_PHALGUNI, language),
            gana = "Manushya",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_COW, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_UTTARA_PHALGUNI, language)
        )
        Nakshatra.HASTA -> NakshatraData(
            sanskrit = "हस्त",
            deity = StringResources.get(StringKeyNakshatra.DEITY_HASTA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_HASTA, language),
            gana = "Deva",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_BUFFALO, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_HASTA, language)
        )
        Nakshatra.CHITRA -> NakshatraData(
            sanskrit = "चित्रा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_CHITRA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_CHITRA, language),
            gana = "Rakshasa",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_TIGER, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_CHITRA, language)
        )
        Nakshatra.SWATI -> NakshatraData(
            sanskrit = "स्वाति",
            deity = StringResources.get(StringKeyNakshatra.DEITY_SWATI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_SWATI, language),
            gana = "Deva",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_BUFFALO, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_SWATI, language)
        )
        Nakshatra.VISHAKHA -> NakshatraData(
            sanskrit = "विशाखा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_VISHAKHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_VISHAKHA, language),
            gana = "Rakshasa",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_TIGER, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_VISHAKHA, language)
        )
        Nakshatra.ANURADHA -> NakshatraData(
            sanskrit = "अनुराधा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_ANURADHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_ANURADHA, language),
            gana = "Deva",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_DEER, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_ANURADHA, language)
        )
        Nakshatra.JYESHTHA -> NakshatraData(
            sanskrit = "ज्येष्ठा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_JYESHTHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_JYESHTHA, language),
            gana = "Rakshasa",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_DEER, language),
            quality = Quality.NEUTRAL,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_JYESHTHA, language)
        )
        Nakshatra.MULA -> NakshatraData(
            sanskrit = "मूल",
            deity = StringResources.get(StringKeyNakshatra.DEITY_MULA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_MULA, language),
            gana = "Rakshasa",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_DOG, language),
            quality = Quality.CHALLENGING,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_MULA, language)
        )
        Nakshatra.PURVA_ASHADHA -> NakshatraData(
            sanskrit = "पूर्वाषाढ़ा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_PURVA_ASHADHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_PURVA_ASHADHA, language),
            gana = "Manushya",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_MONKEY, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_PURVA_ASHADHA, language)
        )
        Nakshatra.UTTARA_ASHADHA -> NakshatraData(
            sanskrit = "उत्तराषाढ़ा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_UTTARA_ASHADHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_UTTARA_ASHADHA, language),
            gana = "Manushya",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_MONGOOSE, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_UTTARA_ASHADHA, language)
        )
        Nakshatra.SHRAVANA -> NakshatraData(
            sanskrit = "श्रवण",
            deity = StringResources.get(StringKeyNakshatra.DEITY_SHRAVANA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_SHRAVANA, language),
            gana = "Deva",
            guna = "Rajas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_MONKEY, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_SHRAVANA, language)
        )
        Nakshatra.DHANISHTHA -> NakshatraData(
            sanskrit = "धनिष्ठा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_DHANISHTHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_DHANISHTHA, language),
            gana = "Rakshasa",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_LION, language),
            quality = Quality.GOOD,
            description = StringResources.get(StringKeyPanchanga.NA_DESC_DHANISHTHA, language)
        )
        Nakshatra.SHATABHISHA -> NakshatraData(
            sanskrit = "शतभिषा",
            deity = StringResources.get(StringKeyNakshatra.DEITY_SHATABHISHA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_SHATABHISHA, language),
            gana = "Rakshasa",
            guna = "Tamas",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_HORSE, language),
            quality = Quality.NEUTRAL,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_SHATABHISHA, language)
        )
        Nakshatra.PURVA_BHADRAPADA -> NakshatraData(
            sanskrit = "पूर्वा भाद्रपद",
            deity = StringResources.get(StringKeyNakshatra.DEITY_PURVA_BHADRAPADA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_PURVA_BHADRAPADA, language),
            gana = "Manushya",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_LION, language),
            quality = Quality.CHALLENGING,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_PURVA_BHADRAPADA, language)
        )
        Nakshatra.UTTARA_BHADRAPADA -> NakshatraData(
            sanskrit = "उत्तरा भाद्रपद",
            deity = StringResources.get(StringKeyNakshatra.DEITY_UTTARA_BHADRAPADA, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_UTTARA_BHADRAPADA, language),
            gana = "Manushya",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_COW, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_UTTARA_BHADRAPADA, language)
        )
        Nakshatra.REVATI -> NakshatraData(
            sanskrit = "रेवती",
            deity = StringResources.get(StringKeyNakshatra.DEITY_REVATI, language),
            symbol = StringResources.get(StringKeyNakshatra.SYMBOL_REVATI, language),
            gana = "Deva",
            guna = "Sattva",
            animal = StringResources.get(StringKeyNakshatra.ANIMAL_ELEPHANT, language),
            quality = Quality.EXCELLENT,
            description = StringResources.get(StringKeyPanchanga.NAK_DESC_REVATI, language)
        )
    }
}

private data class YogaData(
    val sanskrit: String,
    val meaning: String,
    val quality: Quality,
    val description: String
)

private fun getYogaData(yoga: Yoga, language: Language): YogaData {
    return when (yoga) {
        Yoga.VISHKUMBHA -> YogaData("विष्कुम्भ", StringResources.get(StringKeyPanchanga.YOGA_MEANING_SUPPORTING, language), Quality.CHALLENGING, StringResources.get(StringKeyPanchanga.YOGA_DESC_VISHKUMBHA, language))
        Yoga.PRITI -> YogaData("प्रीति", StringResources.get(StringKeyPanchanga.YOGA_MEANING_LOVE, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_PRITI, language))
        Yoga.AYUSHMAN -> YogaData("आयुष्मान", StringResources.get(StringKeyPanchanga.YOGA_MEANING_LONG_LIVED, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_AYUSHMAN, language))
        Yoga.SAUBHAGYA -> YogaData("सौभाग्य", StringResources.get(StringKeyPanchanga.YOGA_MEANING_GOOD_FORTUNE, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SAUBHAGYA, language))
        Yoga.SHOBHANA -> YogaData("शोभन", StringResources.get(StringKeyPanchanga.YOGA_MEANING_SPLENDOR, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SHOBHANA, language))
        Yoga.ATIGANDA -> YogaData("अतिगण्ड", StringResources.get(StringKeyPanchanga.YOGA_MEANING_GREAT_OBSTACLE, language), Quality.INAUSPICIOUS, StringResources.get(StringKeyPanchanga.YOGA_DESC_ATIGANDA, language))
        Yoga.SUKARMA -> YogaData("सुकर्म", StringResources.get(StringKeyPanchanga.YOGA_MEANING_GOOD_DEEDS, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SUKARMA, language))
        Yoga.DHRITI -> YogaData("धृति", StringResources.get(StringKeyPanchanga.YOGA_MEANING_STEADINESS, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.YOGA_DESC_DHRITI, language))
        Yoga.SHULA -> YogaData("शूल", StringResources.get(StringKeyPanchanga.YOGA_MEANING_SPEAR, language), Quality.CHALLENGING, StringResources.get(StringKeyPanchanga.YOGA_DESC_SHULA, language))
        Yoga.GANDA -> YogaData("गण्ड", StringResources.get(StringKeyPanchanga.YOGA_MEANING_OBSTACLE, language), Quality.CHALLENGING, StringResources.get(StringKeyPanchanga.YOGA_DESC_GANDA, language))
        Yoga.VRIDDHI -> YogaData("वृद्धि", StringResources.get(StringKeyPanchanga.YOGA_MEANING_GROWTH, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_VRIDDHI, language))
        Yoga.DHRUVA -> YogaData("ध्रुव", StringResources.get(StringKeyPanchanga.YOGA_MEANING_FIXED, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.YOGA_DESC_DHRUVA, language))
        Yoga.VYAGHATA -> YogaData("व्याघात", StringResources.get(StringKeyPanchanga.YOGA_MEANING_DESTRUCTION, language), Quality.INAUSPICIOUS, StringResources.get(StringKeyPanchanga.YOGA_DESC_VYAGHATA, language))
        Yoga.HARSHANA -> YogaData("हर्षण", StringResources.get(StringKeyPanchanga.YOGA_MEANING_JOY, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_HARSHANA, language))
        Yoga.VAJRA -> YogaData("वज्र", StringResources.get(StringKeyPanchanga.YOGA_MEANING_THUNDERBOLT, language), Quality.NEUTRAL, StringResources.get(StringKeyPanchanga.YOGA_DESC_VAJRA, language))
        Yoga.SIDDHI -> YogaData("सिद्धि", StringResources.get(StringKeyPanchanga.YOGA_MEANING_ACCOMPLISHMENT, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SIDDHI, language))
        Yoga.VYATIPATA -> YogaData("व्यतीपात", StringResources.get(StringKeyPanchanga.YOGA_MEANING_CALAMITY, language), Quality.INAUSPICIOUS, StringResources.get(StringKeyPanchanga.YOGA_DESC_VYATIPATA, language))
        Yoga.VARIYAN -> YogaData("वरीयान", StringResources.get(StringKeyPanchanga.YOGA_MEANING_EXCELLENT, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_VARIYAN, language))
        Yoga.PARIGHA -> YogaData("परिघ", StringResources.get(StringKeyPanchanga.YOGA_MEANING_OBSTRUCTION, language), Quality.CHALLENGING, StringResources.get(StringKeyPanchanga.YOGA_DESC_PARIGHA, language))
        Yoga.SHIVA -> YogaData("शिव", StringResources.get(StringKeyPanchanga.YOGA_MEANING_AUSPICIOUS, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SHIVA, language))
        Yoga.SIDDHA -> YogaData("सिद्ध", StringResources.get(StringKeyPanchanga.YOGA_MEANING_ACCOMPLISHED, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SIDDHA, language))
        Yoga.SADHYA -> YogaData("साध्य", StringResources.get(StringKeyPanchanga.YOGA_MEANING_ACHIEVABLE, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.YOGA_DESC_SADHYA, language))
        Yoga.SHUBHA -> YogaData("शुभ", StringResources.get(StringKeyPanchanga.YOGA_MEANING_AUSPICIOUS, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SHUBHA, language))
        Yoga.SHUKLA -> YogaData("शुक्ल", StringResources.get(StringKeyPanchanga.YOGA_MEANING_BRIGHT, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_SHUKLA, language))
        Yoga.BRAHMA -> YogaData("ब्रह्म", StringResources.get(StringKeyPanchanga.YOGA_MEANING_CREATOR, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_BRAHMA, language))
        Yoga.INDRA -> YogaData("इन्द्र", StringResources.get(StringKeyPanchanga.YOGA_MEANING_KING_OF_GODS, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.YOGA_DESC_INDRA, language))
        Yoga.VAIDHRITI -> YogaData("वैधृति", StringResources.get(StringKeyPanchanga.YOGA_MEANING_DISCORD, language), Quality.INAUSPICIOUS, StringResources.get(StringKeyPanchanga.YOGA_DESC_VAIDHRITI, language))
    }
}

private data class KaranaData(
    val sanskrit: String,
    val type: String,
    val quality: Quality,
    val description: String
)

private fun getKaranaData(karana: Karana, language: Language): KaranaData {
    return when (karana) {
        Karana.BAVA -> KaranaData("बव", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.KARANA_DESC_BAVA, language))
        Karana.BALAVA -> KaranaData("बालव", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.KARANA_DESC_BALAVA, language))
        Karana.KAULAVA -> KaranaData("कौलव", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.KARANA_DESC_KAULAVA, language))
        Karana.TAITILA -> KaranaData("तैतिल", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.KARANA_DESC_TAITILA, language))
        Karana.GARA -> KaranaData("गर", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.KARANA_DESC_GARA, language))
        Karana.VANIJA -> KaranaData("वणिज", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.EXCELLENT, StringResources.get(StringKeyPanchanga.KARANA_DESC_VANIJA, language))
        Karana.VISHTI -> KaranaData("विष्टि", StringResources.get(StringKeyPanchanga.KARANA_TYPE_MOVABLE, language), Quality.INAUSPICIOUS, StringResources.get(StringKeyPanchanga.KARANA_DESC_VISHTI, language))
        Karana.SHAKUNI -> KaranaData("शकुनि", StringResources.get(StringKeyPanchanga.KARANA_TYPE_FIXED, language), Quality.NEUTRAL, StringResources.get(StringKeyPanchanga.KARANA_DESC_SHAKUNI, language))
        Karana.CHATUSHPADA -> KaranaData("चतुष्पद", StringResources.get(StringKeyPanchanga.KARANA_TYPE_FIXED, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.KARANA_DESC_CHATUSHPADA, language))
        Karana.NAGA -> KaranaData("नाग", StringResources.get(StringKeyPanchanga.KARANA_TYPE_FIXED, language), Quality.NEUTRAL, StringResources.get(StringKeyPanchanga.KARANA_DESC_NAGA, language))
        Karana.KIMSTUGHNA -> KaranaData("किंस्तुघ्न", StringResources.get(StringKeyPanchanga.KARANA_TYPE_FIXED, language), Quality.GOOD, StringResources.get(StringKeyPanchanga.KARANA_DESC_KIMSTUGHNA, language))
    }
}

private data class VaraData(
    val sanskrit: String,
    val element: String,
    val direction: String,
    val description: String,
    val favorable: List<String>,
    val unfavorable: List<String>
)

private fun getVaraData(vara: Vara, language: Language): VaraData {
    return when (vara) {
        Vara.SUNDAY -> VaraData(
            sanskrit = "रविवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_FIRE, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_EAST, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_SUNDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_SUNDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_SUNDAY, language).split(", ")
        )
        Vara.MONDAY -> VaraData(
            sanskrit = "सोमवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_WATER, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_NORTHWEST, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_MONDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_MONDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_MONDAY, language).split(", ")
        )
        Vara.TUESDAY -> VaraData(
            sanskrit = "मंगलवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_FIRE, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_SOUTH, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_TUESDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_TUESDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_TUESDAY, language).split(", ")
        )
        Vara.WEDNESDAY -> VaraData(
            sanskrit = "बुधवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_EARTH, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_NORTH, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_WEDNESDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_WEDNESDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_WEDNESDAY, language).split(", ")
        )
        Vara.THURSDAY -> VaraData(
            sanskrit = "गुरुवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_ETHER, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_NORTHEAST, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_THURSDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_THURSDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_THURSDAY, language).split(", ")
        )
        Vara.FRIDAY -> VaraData(
            sanskrit = "शुक्रवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_WATER, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_SOUTHEAST, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_FRIDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_FRIDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_FRIDAY, language).split(", ")
        )
        Vara.SATURDAY -> VaraData(
            sanskrit = "शनिवार",
            element = StringResources.get(StringKeyDosha.NAKSHATRA_ELEMENT_AIR, language),
            direction = StringResources.get(StringKeyAnalysis.PRASHNA_DIR_WEST, language),
            description = StringResources.get(StringKeyPanchanga.VARA_DESC_SATURDAY, language),
            favorable = StringResources.get(StringKeyPanchanga.VARA_FAV_SATURDAY, language).split(", "),
            unfavorable = StringResources.get(StringKeyPanchanga.VARA_UNFAV_SATURDAY, language).split(", ")
        )
    }
}


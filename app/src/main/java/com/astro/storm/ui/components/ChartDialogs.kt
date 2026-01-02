package com.astro.storm.ui.components

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.LocalizationManager
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringKeyAnalysis
import com.astro.storm.data.localization.StringKeyMatch
import com.astro.storm.data.localization.StringKeyComponents
import com.astro.storm.data.localization.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.*
import com.astro.storm.ephemeris.DivisionalChartData
import com.astro.storm.ephemeris.PlanetaryShadbala
import com.astro.storm.ephemeris.RetrogradeCombustionCalculator
import com.astro.storm.ephemeris.ShadbalaCalculator
import com.astro.storm.ui.chart.ChartRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

// Color palette for dialogs
private val DialogBackground = Color(0xFF1A1A1A)
private val DialogSurface = Color(0xFF252525)
private val DialogSurfaceElevated = Color(0xFF2D2D2D)
private val AccentGold = Color(0xFFD4AF37)
private val AccentTeal = Color(0xFF4DB6AC)
private val AccentPurple = Color(0xFF9575CD)
private val AccentRose = Color(0xFFE57373)
private val AccentBlue = Color(0xFF64B5F6)
private val AccentGreen = Color(0xFF81C784)
private val AccentOrange = Color(0xFFFFB74D)
private val TextPrimary = Color(0xFFF5F5F5)
private val TextSecondary = Color(0xFFB0B0B0)
private val TextMuted = Color(0xFF757575)
private val DividerColor = Color(0xFF333333)

// Planet colors (including outer planets for complete coverage)
private val planetColors = mapOf(
    Planet.SUN to Color(0xFFD2691E),
    Planet.MOON to Color(0xFFDC143C),
    Planet.MARS to Color(0xFFDC143C),
    Planet.MERCURY to Color(0xFF228B22),
    Planet.JUPITER to Color(0xFFDAA520),
    Planet.VENUS to Color(0xFF9370DB),
    Planet.SATURN to Color(0xFF4169E1),
    Planet.RAHU to Color(0xFF8B0000),
    Planet.KETU to Color(0xFF8B0000),
    Planet.URANUS to Color(0xFF20B2AA),
    Planet.NEPTUNE to Color(0xFF4682B4),
    Planet.PLUTO to Color(0xFF800080)
)

/**
 * Full-screen chart dialog with zoom, pan, and download functionality
 */
@Composable
fun FullScreenChartDialog(
    chart: VedicChart,
    chartRenderer: ChartRenderer,
    chartTitle: String,
    divisionalChartData: DivisionalChartData? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var isDownloading by remember { mutableStateOf(false) }
    var downloadSuccess by remember { mutableStateOf<Boolean?>(null) }

    // Zoom and pan state
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DialogBackground)
        ) {
            // Chart canvas with zoom/pan
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .transformable(
                        state = rememberTransformableState { zoomChange, panChange, _ ->
                            scale = (scale * zoomChange).coerceIn(0.5f, 3f)
                            offsetX += panChange.x
                            offsetY += panChange.y
                        }
                    )
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                ) {
                    if (divisionalChartData != null) {
                        // Pass original chart for vargottama and combust status checking
                        chartRenderer.drawDivisionalChart(
                            drawScope = this,
                            planetPositions = divisionalChartData.planetPositions,
                            ascendantLongitude = divisionalChartData.ascendantLongitude,
                            size = size.minDimension,
                            chartTitle = chartTitle,
                            originalChart = chart
                        )
                    } else {
                        chartRenderer.drawNorthIndianChart(
                            drawScope = this,
                            chart = chart,
                            size = size.minDimension,
                            chartTitle = chartTitle
                        )
                    }
                }
            }

            // Top bar with title and close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DialogBackground.copy(alpha = 0.9f))
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chartTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(StringKeyAnalysis.DIALOG_CLOSE),
                        tint = TextPrimary
                    )
                }
            }

            // Bottom action bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DialogBackground.copy(alpha = 0.9f))
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset zoom button
                ActionButton(
                    icon = Icons.Default.CenterFocusStrong,
                    label = stringResource(StringKeyAnalysis.DIALOG_RESET),
                    onClick = {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    }
                )

                // Zoom in button
                ActionButton(
                    icon = Icons.Default.ZoomIn,
                    label = stringResource(StringKeyAnalysis.DIALOG_ZOOM_IN),
                    onClick = { scale = (scale * 1.2f).coerceAtMost(3f) }
                )

                // Zoom out button
                ActionButton(
                    icon = Icons.Default.ZoomOut,
                    label = stringResource(StringKeyAnalysis.DIALOG_ZOOM_OUT),
                    onClick = { scale = (scale / 1.2f).coerceAtLeast(0.5f) }
                )

                // Download button
                ActionButton(
                    icon = if (isDownloading) Icons.Default.HourglassEmpty else Icons.Default.Download,
                    label = if (isDownloading) stringResource(StringKeyAnalysis.DIALOG_SAVING) else stringResource(StringKeyAnalysis.DIALOG_DOWNLOAD),
                    onClick = {
                        if (!isDownloading) {
                            isDownloading = true
                            scope.launch {
                                val success = saveChartToGallery(
                                    context = context,
                                    chartRenderer = chartRenderer,
                                    chart = chart,
                                    divisionalChartData = divisionalChartData,
                                    chartTitle = chartTitle,
                                    density = density
                                )
                                downloadSuccess = success
                                isDownloading = false

                                withContext(Dispatchers.Main) {
                                    val locManager = LocalizationManager.getInstance(context)
                                    val message = if (success)
                                        locManager.getString(StringKeyAnalysis.DIALOG_CHART_SAVED)
                                    else
                                        locManager.getString(StringKeyAnalysis.DIALOG_SAVE_FAILED)
                                    Toast.makeText(
                                        context,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    enabled = !isDownloading
                )
            }

            // Zoom indicator
            AnimatedVisibility(
                visible = scale != 1f,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 72.dp, end = 16.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DialogSurface.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "${(scale * 100).toInt()}%",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (enabled) AccentGold else TextMuted,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (enabled) TextSecondary else TextMuted
        )
    }
}

/**
 * Save chart to device gallery
 */
private suspend fun saveChartToGallery(
    context: Context,
    chartRenderer: ChartRenderer,
    chart: VedicChart,
    divisionalChartData: DivisionalChartData?,
    chartTitle: String,
    density: androidx.compose.ui.unit.Density
): Boolean = withContext(Dispatchers.IO) {
    try {
        // Create high-resolution bitmap
        val size = 2048
        val bitmap = if (divisionalChartData != null) {
            chartRenderer.createDivisionalChartBitmap(
                planetPositions = divisionalChartData.planetPositions,
                ascendantLongitude = divisionalChartData.ascendantLongitude,
                chartTitle = chartTitle,
                width = size,
                height = size,
                density = density
            )
        } else {
            chartRenderer.createChartBitmap(chart, size, size, density)
        }

        // Save to gallery
        val filename = "AstroStorm_${chartTitle.replace(" ", "_")}_${System.currentTimeMillis()}.png"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AstroStorm")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return@withContext false

            context.contentResolver.openOutputStream(uri)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
        } else {
            val directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/AstroStorm"
            )
            if (!directory.exists()) directory.mkdirs()

            val file = java.io.File(directory, filename)
            java.io.FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            // Notify gallery
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DATA, file.absolutePath)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            }
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }

        bitmap.recycle()
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * Planet detail dialog with comprehensive information and predictions
 */
@Composable
fun PlanetDetailDialog(
    planetPosition: PlanetPosition,
    chart: VedicChart,
    onDismiss: () -> Unit
) {
    val planet = planetPosition.planet
    val shadbala = remember(chart) {
        ShadbalaCalculator.calculatePlanetShadbala(planetPosition, chart)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            color = DialogBackground
        ) {
            Column {
                // Header
                PlanetDialogHeader(planetPosition, onDismiss)

                // Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Position Details
                    item {
                        PlanetPositionCard(planetPosition)
                    }

                    // Strength Analysis (Shadbala)
                    item {
                        ShadbalaCard(shadbala)
                    }

                    // Significations
                    item {
                        SignificationsCard(planet)
                    }

                    // House Placement Interpretation
                    item {
                        HousePlacementCard(planetPosition)
                    }

                    // Status & Conditions
                    item {
                        PlanetStatusCard(planetPosition, chart)
                    }

                    // Predictions & Insights
                    item {
                        PredictionsCard(planetPosition, shadbala, chart)
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanetDialogHeader(
    planetPosition: PlanetPosition,
    onDismiss: () -> Unit
) {
    val language = LocalLanguage.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DialogSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            planetColors[planetPosition.planet] ?: AccentGold,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = planetPosition.planet.symbol,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = planetPosition.planet.getLocalizedName(language),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${planetPosition.sign.getLocalizedName(language)} • ${stringResource(StringKeyAnalysis.HOUSE)} ${planetPosition.house}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = stringResource(StringKeyAnalysis.DIALOG_CLOSE), tint = TextPrimary)
            }
        }
    }
}

@Composable
private fun PlanetPositionCard(position: PlanetPosition) {
    val language = LocalLanguage.current
    DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_POSITION_DETAILS), icon = Icons.Outlined.LocationOn) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_ZODIAC_SIGN), position.sign.getLocalizedName(language), AccentTeal)
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_DEGREE), formatDegree(position.longitude), TextPrimary)
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_HOUSE), "${stringResource(StringKeyAnalysis.HOUSE)} ${position.house}", AccentGold)
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_NAKSHATRA), "${position.nakshatra.getLocalizedName(language)} (${stringResource(StringKeyAnalysis.PANCHANGA_PADA)} ${position.nakshatraPada})", AccentPurple)
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_NAKSHATRA_LORD), position.nakshatra.ruler.getLocalizedName(language), TextSecondary)
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_NAKSHATRA_DEITY), position.nakshatra.deity, TextSecondary)
            if (position.isRetrograde) {
                DetailRow(stringResource(StringKeyAnalysis.DIALOG_MOTION), stringResource(StringKeyAnalysis.DIALOG_RETROGRADE), AccentOrange)
            }
        }
    }
}

@Composable
private fun ShadbalaCard(shadbala: PlanetaryShadbala) {
    val language = LocalLanguage.current
    DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_STRENGTH_ANALYSIS), icon = Icons.Outlined.TrendingUp) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Overall strength bar
            val strengthPercentage = (shadbala.percentageOfRequired / 150.0).coerceIn(0.0, 1.0).toFloat()
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(StringKeyAnalysis.DIALOG_OVERALL, String.format("%.2f", shadbala.totalRupas), String.format("%.2f", shadbala.requiredRupas)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = shadbala.strengthRating.getLocalizedName(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            shadbala.percentageOfRequired >= 100 -> AccentGreen
                            shadbala.percentageOfRequired >= 85 -> AccentOrange
                            else -> AccentRose
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { strengthPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        shadbala.percentageOfRequired >= 100 -> AccentGreen
                        shadbala.percentageOfRequired >= 85 -> AccentOrange
                        else -> AccentRose
                    },
                    trackColor = DividerColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(StringKeyAnalysis.DIALOG_PERCENT_OF_REQUIRED, String.format("%.1f", shadbala.percentageOfRequired)),
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            HorizontalDivider(color = DividerColor)

            // Breakdown
            Text(stringResource(StringKeyAnalysis.DIALOG_STRENGTH_BREAKDOWN), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)

            StrengthRow(stringResource(StringKeyAnalysis.DIALOG_STHANA_BALA), shadbala.sthanaBala.total, 180.0)
            StrengthRow(stringResource(StringKeyAnalysis.DIALOG_DIG_BALA), shadbala.digBala, 60.0)
            StrengthRow(stringResource(StringKeyAnalysis.DIALOG_KALA_BALA), shadbala.kalaBala.total, 180.0)
            StrengthRow(stringResource(StringKeyAnalysis.DIALOG_CHESTA_BALA), shadbala.chestaBala, 60.0)
            StrengthRow(stringResource(StringKeyAnalysis.DIALOG_NAISARGIKA_BALA), shadbala.naisargikaBala, 60.0)
            StrengthRow(stringResource(StringKeyAnalysis.DIALOG_DRIK_BALA), shadbala.drikBala, 60.0)
        }
    }
}

@Composable
private fun StrengthRow(label: String, value: Double, maxValue: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary, modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { (value / maxValue).coerceIn(0.0, 1.0).toFloat() },
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = AccentTeal,
                trackColor = DividerColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f", value),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun SignificationsCard(planet: Planet) {
    val significations = getPlanetSignifications(planet)

    DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_SIGNIFICATIONS), icon = Icons.Outlined.Info) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Nature
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_NATURE), significations.nature, when (significations.nature) {
                "Benefic" -> AccentGreen
                "Malefic" -> AccentRose
                else -> AccentOrange
            })

            // Element
            DetailRow(stringResource(StringKeyAnalysis.DIALOG_ELEMENT), significations.element, TextSecondary)

            // Represents
            Text(stringResource(StringKeyAnalysis.DIALOG_REPRESENTS), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
            significations.represents.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(AccentGold, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item, fontSize = 13.sp, color = TextPrimary)
                }
            }

            // Body Parts
            Text(stringResource(StringKeyAnalysis.DIALOG_BODY_PARTS), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
            Text(text = significations.bodyParts, fontSize = 13.sp, color = TextPrimary)

            // Professions
            Text(stringResource(StringKeyAnalysis.DIALOG_PROFESSIONS), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
            Text(text = significations.professions, fontSize = 13.sp, color = TextPrimary)
        }
    }
}

@Composable
private fun HousePlacementCard(position: PlanetPosition) {
    val interpretation = getHousePlacementInterpretation(position.planet, position.house)

    DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_HOUSE_PLACEMENT, position.house), icon = Icons.Outlined.Home) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = interpretation.houseName,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = AccentGold
            )
            Text(
                text = interpretation.houseSignification,
                fontSize = 13.sp,
                color = TextSecondary
            )
            HorizontalDivider(color = DividerColor)
            Text(
                text = interpretation.interpretation,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun PlanetStatusCard(position: PlanetPosition, chart: VedicChart) {
    val language = LocalLanguage.current
    val conditions = remember(chart) {
        RetrogradeCombustionCalculator.analyzePlanetaryConditions(chart)
    }
    val planetCondition = conditions.getCondition(position.planet)

    DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_STATUS_CONDITIONS), icon = Icons.Outlined.FactCheck) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Dignity
            val dignity = getDignity(position.planet, position.sign)
            StatusChip(
                label = stringResource(StringKeyAnalysis.DIALOG_DIGNITY),
                value = dignity.status,
                color = dignity.color
            )

            // Retrograde
            if (position.isRetrograde) {
                StatusChip(
                    label = stringResource(StringKeyAnalysis.DIALOG_MOTION),
                    value = stringResource(StringKeyAnalysis.DIALOG_RETROGRADE),
                    color = AccentOrange
                )
            }

            // Combustion
            planetCondition?.let { cond ->
                if (cond.combustionStatus != RetrogradeCombustionCalculator.CombustionStatus.NOT_COMBUST) {
                    StatusChip(
                        label = stringResource(StringKeyAnalysis.DIALOG_COMBUSTION),
                        value = cond.combustionStatus.getLocalizedName(language),
                        color = AccentRose
                    )
                }

                // Planetary War
                if (cond.isInPlanetaryWar) {
                    StatusChip(
                        label = stringResource(StringKeyAnalysis.DIALOG_PLANETARY_WAR),
                        value = stringResource(StringKeyAnalysis.DIALOG_AT_WAR_WITH, cond.warData?.loser?.getLocalizedName(language) ?: ""),
                        color = AccentPurple
                    )
                }
            }
        }
    }
}

@Composable
private fun PredictionsCard(
    position: PlanetPosition,
    shadbala: PlanetaryShadbala,
    chart: VedicChart
) {
    val predictions = getPlanetPredictions(position, shadbala, chart)

    DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_INSIGHTS_PREDICTIONS), icon = Icons.Outlined.AutoAwesome) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            predictions.forEach { prediction ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        when (prediction.type) {
                            PredictionType.POSITIVE -> Icons.Default.CheckCircle
                            PredictionType.NEGATIVE -> Icons.Default.Warning
                            PredictionType.NEUTRAL -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = when (prediction.type) {
                            PredictionType.POSITIVE -> AccentGreen
                            PredictionType.NEGATIVE -> AccentOrange
                            PredictionType.NEUTRAL -> AccentBlue
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = prediction.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = prediction.description,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = color.copy(alpha = 0.15f)
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun DialogCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = DialogSurface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }
            content()
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = valueColor)
    }
}

/**
 * Nakshatra detail dialog
 */
@Composable
fun NakshatraDetailDialog(
    nakshatra: Nakshatra,
    pada: Int,
    onDismiss: () -> Unit
) {
    val language = LocalLanguage.current
    val details = getNakshatraDetails(nakshatra)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = DialogBackground
        ) {
            Column {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DialogSurface
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
                                text = nakshatra.getLocalizedName(language),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${stringResource(StringKeyAnalysis.PANCHANGA_PADA)} $pada • ${nakshatra.ruler.getLocalizedName(language)}",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(StringKeyAnalysis.DIALOG_CLOSE), tint = TextPrimary)
                        }
                    }
                }

                // Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_BASIC_INFO), icon = Icons.Outlined.Info) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_NUMBER), "${nakshatra.number} / 27", TextPrimary)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_DEGREE_RANGE), "${String.format("%.2f", nakshatra.startDegree)}° - ${String.format("%.2f", nakshatra.endDegree)}°", AccentTeal)
                                DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_RULING_PLANET), nakshatra.ruler.getLocalizedName(language), AccentGold)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_NAKSHATRA_DEITY), nakshatra.deity, AccentPurple)
                            }
                        }
                    }

                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_NAKSHATRA_NATURE), icon = Icons.Outlined.Psychology) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_SYMBOL), details.symbol, TextPrimary)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_NATURE), details.nature, when(details.nature) {
                                    "Fixed (Dhruva)" -> AccentBlue
                                    "Movable (Chara)" -> AccentGreen
                                    "Sharp (Tikshna)" -> AccentRose
                                    else -> TextSecondary
                                })
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_GENDER), details.gender, TextSecondary)
                                DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_GANA), details.gana, TextSecondary)
                                DetailRow(stringResource(StringKeyAnalysis.PANCHANGA_GUNA), details.guna, TextSecondary)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_ELEMENT), details.element, TextSecondary)
                            }
                        }
                    }

                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_PADA_CHARACTERISTICS, pada), icon = Icons.Outlined.Star) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val padaSign = when(pada) {
                                    1 -> nakshatra.pada1Sign
                                    2 -> nakshatra.pada2Sign
                                    3 -> nakshatra.pada3Sign
                                    4 -> nakshatra.pada4Sign
                                    else -> nakshatra.pada1Sign
                                }
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_NAVAMSA_SIGN), padaSign.getLocalizedName(language), AccentTeal)
                                Text(
                                    text = getPadaDescription(nakshatra, pada),
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_GENERAL_CHARACTERISTICS), icon = Icons.Outlined.Description) {
                            Text(
                                text = details.characteristics,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_CAREER_INDICATIONS), icon = Icons.Outlined.Work) {
                            Text(
                                text = details.careers,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * House detail dialog
 */
@Composable
fun HouseDetailDialog(
    houseNumber: Int,
    houseCusp: Double,
    planetsInHouse: List<PlanetPosition>,
    chart: VedicChart,
    onDismiss: () -> Unit
) {
    val language = LocalLanguage.current
    val houseDetails = getHouseDetails(houseNumber)
    val sign = ZodiacSign.fromLongitude(houseCusp)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = DialogBackground
        ) {
            Column {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DialogSurface
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
                                text = "${stringResource(StringKeyAnalysis.HOUSE)} $houseNumber",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = houseDetails.name,
                                fontSize = 14.sp,
                                color = AccentGold
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(StringKeyAnalysis.DIALOG_CLOSE), tint = TextPrimary)
                        }
                    }
                }

                // Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_HOUSE_INFO), icon = Icons.Outlined.Home) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_ZODIAC_SIGN), sign.getLocalizedName(language), AccentTeal)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_CUSP_DEGREE), formatDegree(houseCusp), TextPrimary)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_SIGN_LORD), sign.ruler.getLocalizedName(language), AccentGold)
                                DetailRow(stringResource(StringKeyAnalysis.DIALOG_HOUSE_TYPE), houseDetails.type, TextSecondary)
                            }
                        }
                    }

                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_SIGNIFICATIONS), icon = Icons.Outlined.ListAlt) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                houseDetails.significations.forEach { signification ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(AccentGold, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = signification, fontSize = 13.sp, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    if (planetsInHouse.isNotEmpty()) {
                        item {
                            DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_PLANETS_IN_HOUSE), icon = Icons.Outlined.Star) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    planetsInHouse.forEach { planet ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(
                                                            planetColors[planet.planet] ?: AccentGold,
                                                            CircleShape
                                                        )
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = planet.planet.getLocalizedName(language),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = TextPrimary
                                                )
                                            }
                                            Text(
                                                text = formatDegreeInSign(planet.longitude),
                                                fontSize = 13.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_DETAILED_INTERPRETATION), icon = Icons.Outlined.Description) {
                            Text(
                                text = houseDetails.interpretation,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Shadbala summary dialog
 */
@Composable
fun ShadbalaDialog(
    chart: VedicChart,
    onDismiss: () -> Unit
) {
    val language = LocalLanguage.current
    val shadbalaAnalysis = remember(chart) {
        ShadbalaCalculator.calculateShadbala(chart)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            color = DialogBackground
        ) {
            Column {
                // Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DialogSurface
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
                                text = stringResource(StringKeyAnalysis.DIALOG_SHADBALA_ANALYSIS),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = stringResource(StringKeyAnalysis.DIALOG_SIXFOLD_STRENGTH),
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(StringKeyAnalysis.DIALOG_CLOSE), tint = TextPrimary)
                        }
                    }
                }

                // Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Overall summary
                    item {
                        DialogCard(title = stringResource(StringKeyAnalysis.DIALOG_OVERALL_SUMMARY), icon = Icons.Outlined.Analytics) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    SummaryBadge(
                                        label = stringResource(StringKeyAnalysis.DIALOG_CHART_STRENGTH),
                                        value = "${String.format("%.1f", shadbalaAnalysis.overallStrengthScore)}%",
                                        color = when {
                                            shadbalaAnalysis.overallStrengthScore >= 100 -> AccentGreen
                                            shadbalaAnalysis.overallStrengthScore >= 85 -> AccentOrange
                                            else -> AccentRose
                                        }
                                    )
                                    SummaryBadge(
                                        label = stringResource(StringKeyAnalysis.DIALOG_STRONGEST),
                                        value = shadbalaAnalysis.strongestPlanet.getLocalizedName(language),
                                        color = AccentGold
                                    )
                                    SummaryBadge(
                                        label = stringResource(StringKeyAnalysis.DIALOG_WEAKEST),
                                        value = shadbalaAnalysis.weakestPlanet.getLocalizedName(language),
                                        color = AccentPurple
                                    )
                                }
                            }
                        }
                    }

                    // Individual planet strengths
                    items(shadbalaAnalysis.getPlanetsByStrength()) { shadbala ->
                        PlanetStrengthCard(shadbala, language)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryBadge(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun PlanetStrengthCard(shadbala: PlanetaryShadbala, language: Language) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = DialogSurface
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
                            .size(40.dp)
                            .background(
                                planetColors[shadbala.planet] ?: AccentGold,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = shadbala.planet.symbol,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = shadbala.planet.getLocalizedName(language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = shadbala.strengthRating.getLocalizedName(language),
                            fontSize = 12.sp,
                            color = when {
                                shadbala.isStrong -> AccentGreen
                                shadbala.percentageOfRequired >= 85 -> AccentOrange
                                else -> AccentRose
                            }
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${String.format("%.2f", shadbala.totalRupas)} Rupas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Required: ${String.format("%.2f", shadbala.requiredRupas)}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            val progress = (shadbala.percentageOfRequired / 150.0).coerceIn(0.0, 1.0).toFloat()
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when {
                    shadbala.isStrong -> AccentGreen
                    shadbala.percentageOfRequired >= 85 -> AccentOrange
                    else -> AccentRose
                },
                trackColor = DividerColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${String.format("%.1f", shadbala.percentageOfRequired)}% of required",
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

// Helper functions
private fun formatDegree(degree: Double): String {
    val normalizedDegree = (degree % 360.0 + 360.0) % 360.0
    val deg = normalizedDegree.toInt()
    val min = ((normalizedDegree - deg) * 60).toInt()
    val sec = ((((normalizedDegree - deg) * 60) - min) * 60).toInt()
    return "$deg° $min' $sec\""
}

private fun formatDegreeInSign(longitude: Double): String {
    val degreeInSign = longitude % 30.0
    val deg = degreeInSign.toInt()
    val min = ((degreeInSign - deg) * 60).toInt()
    return "$deg° $min'"
}

// Data classes for interpretations
data class PlanetSignifications(
    val nature: String,
    val element: String,
    val represents: List<String>,
    val bodyParts: String,
    val professions: String
)

data class HousePlacementInterpretation(
    val houseName: String,
    val houseSignification: String,
    val interpretation: String
)

data class Dignity(
    val status: String,
    val color: Color
)

data class Prediction(
    val type: PredictionType,
    val title: String,
    val description: String
)

enum class PredictionType { POSITIVE, NEGATIVE, NEUTRAL }

data class NakshatraDetails(
    val symbol: String,
    val nature: String,
    val gender: String,
    val gana: String,
    val guna: String,
    val element: String,
    val characteristics: String,
    val careers: String
)

data class HouseDetails(
    val name: String,
    val type: String,
    val significations: List<String>,
    val interpretation: String
)

// Helper functions for getting interpretations (comprehensive data)
@Composable
private fun getPlanetSignifications(planet: Planet): PlanetSignifications {
    return when (planet) {
        Planet.SUN -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_SUN_NATURE),
            element = stringResource(StringKeyComponents.SIG_SUN_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_SUN_REP_1),
                stringResource(StringKeyComponents.SIG_SUN_REP_2),
                stringResource(StringKeyComponents.SIG_SUN_REP_3),
                stringResource(StringKeyComponents.SIG_SUN_REP_4),
                stringResource(StringKeyComponents.SIG_SUN_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_SUN_BODY),
            professions = stringResource(StringKeyComponents.SIG_SUN_PROF)
        )
        Planet.MOON -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_MOON_NATURE),
            element = stringResource(StringKeyComponents.SIG_MOON_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_MOON_REP_1),
                stringResource(StringKeyComponents.SIG_MOON_REP_2),
                stringResource(StringKeyComponents.SIG_MOON_REP_3),
                stringResource(StringKeyComponents.SIG_MOON_REP_4),
                stringResource(StringKeyComponents.SIG_MOON_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_MOON_BODY),
            professions = stringResource(StringKeyComponents.SIG_MOON_PROF)
        )
        Planet.MARS -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_MARS_NATURE),
            element = stringResource(StringKeyComponents.SIG_MARS_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_MARS_REP_1),
                stringResource(StringKeyComponents.SIG_MARS_REP_2),
                stringResource(StringKeyComponents.SIG_MARS_REP_3),
                stringResource(StringKeyComponents.SIG_MARS_REP_4),
                stringResource(StringKeyComponents.SIG_MARS_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_MARS_BODY),
            professions = stringResource(StringKeyComponents.SIG_MARS_PROF)
        )
        Planet.MERCURY -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_MERCURY_NATURE),
            element = stringResource(StringKeyComponents.SIG_MERCURY_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_MERCURY_REP_1),
                stringResource(StringKeyComponents.SIG_MERCURY_REP_2),
                stringResource(StringKeyComponents.SIG_MERCURY_REP_3),
                stringResource(StringKeyComponents.SIG_MERCURY_REP_4),
                stringResource(StringKeyComponents.SIG_MERCURY_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_MERCURY_BODY),
            professions = stringResource(StringKeyComponents.SIG_MERCURY_PROF)
        )
        Planet.JUPITER -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_JUPITER_NATURE),
            element = stringResource(StringKeyComponents.SIG_JUPITER_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_JUPITER_REP_1),
                stringResource(StringKeyComponents.SIG_JUPITER_REP_2),
                stringResource(StringKeyComponents.SIG_JUPITER_REP_3),
                stringResource(StringKeyComponents.SIG_JUPITER_REP_4),
                stringResource(StringKeyComponents.SIG_JUPITER_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_JUPITER_BODY),
            professions = stringResource(StringKeyComponents.SIG_JUPITER_PROF)
        )
        Planet.VENUS -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_VENUS_NATURE),
            element = stringResource(StringKeyComponents.SIG_VENUS_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_VENUS_REP_1),
                stringResource(StringKeyComponents.SIG_VENUS_REP_2),
                stringResource(StringKeyComponents.SIG_VENUS_REP_3),
                stringResource(StringKeyComponents.SIG_VENUS_REP_4),
                stringResource(StringKeyComponents.SIG_VENUS_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_VENUS_BODY),
            professions = stringResource(StringKeyComponents.SIG_VENUS_PROF)
        )
        Planet.SATURN -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_SATURN_NATURE),
            element = stringResource(StringKeyComponents.SIG_SATURN_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_SATURN_REP_1),
                stringResource(StringKeyComponents.SIG_SATURN_REP_2),
                stringResource(StringKeyComponents.SIG_SATURN_REP_3),
                stringResource(StringKeyComponents.SIG_SATURN_REP_4),
                stringResource(StringKeyComponents.SIG_SATURN_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_SATURN_BODY),
            professions = stringResource(StringKeyComponents.SIG_SATURN_PROF)
        )
        Planet.RAHU -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_RAHU_NATURE),
            element = stringResource(StringKeyComponents.SIG_RAHU_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_RAHU_REP_1),
                stringResource(StringKeyComponents.SIG_RAHU_REP_2),
                stringResource(StringKeyComponents.SIG_RAHU_REP_3),
                stringResource(StringKeyComponents.SIG_RAHU_REP_4),
                stringResource(StringKeyComponents.SIG_RAHU_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_RAHU_BODY),
            professions = stringResource(StringKeyComponents.SIG_RAHU_PROF)
        )
        Planet.KETU -> PlanetSignifications(
            nature = stringResource(StringKeyComponents.SIG_KETU_NATURE),
            element = stringResource(StringKeyComponents.SIG_KETU_ELEMENT),
            represents = listOf(
                stringResource(StringKeyComponents.SIG_KETU_REP_1),
                stringResource(StringKeyComponents.SIG_KETU_REP_2),
                stringResource(StringKeyComponents.SIG_KETU_REP_3),
                stringResource(StringKeyComponents.SIG_KETU_REP_4),
                stringResource(StringKeyComponents.SIG_KETU_REP_5)
            ),
            bodyParts = stringResource(StringKeyComponents.SIG_KETU_BODY),
            professions = stringResource(StringKeyComponents.SIG_KETU_PROF)
        )
        else -> PlanetSignifications("", "", emptyList(), "", "")
    }
}


@Composable
private fun getHousePlacementInterpretation(planet: Planet, house: Int): HousePlacementInterpretation {
    val houseDetails = getHouseDetails(house)

    val interpretation = when {
        planet == Planet.SUN && house == 1 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_1)
        planet == Planet.SUN && house == 10 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_10)
        planet == Planet.MOON && house == 4 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_MOON_4)
        planet == Planet.MOON && house == 1 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_MOON_1)
        planet == Planet.MARS && house == 10 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_MARS_10)
        planet == Planet.MARS && house == 1 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_MARS_1)
        planet == Planet.MERCURY && house == 1 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_MERCURY_1)
        planet == Planet.MERCURY && house == 5 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_MERCURY_5)
        planet == Planet.JUPITER && house == 1 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_JUPITER_1)
        planet == Planet.JUPITER && house == 9 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_JUPITER_9)
        planet == Planet.VENUS && house == 7 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_VENUS_7)
        planet == Planet.VENUS && house == 4 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_VENUS_4)
        planet == Planet.SATURN && house == 10 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_SATURN_10)
        planet == Planet.SATURN && house == 7 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_SATURN_7)
        planet == Planet.RAHU && house == 10 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_RAHU_10)
        planet == Planet.KETU && house == 12 -> stringResource(StringKeyComponents.HOUSE_PLACEMENT_KETU_12)
        else -> {
            val planetName = planet.localizedName()
            stringResource(StringKeyComponents.HOUSE_PLACEMENT_DEFAULT, planetName, house, houseDetails.name)
        }
    }

    return HousePlacementInterpretation(
        houseName = houseDetails.name,
        houseSignification = houseDetails.significations.take(3).joinToString(", "),
        interpretation = interpretation
    )
}

@Composable
private fun getDignity(planet: Planet, sign: ZodiacSign): Dignity {
    // Exaltation check
    val exalted = when (planet) {
        Planet.SUN -> sign == ZodiacSign.ARIES
        Planet.MOON -> sign == ZodiacSign.TAURUS
        Planet.MARS -> sign == ZodiacSign.CAPRICORN
        Planet.MERCURY -> sign == ZodiacSign.VIRGO
        Planet.JUPITER -> sign == ZodiacSign.CANCER
        Planet.VENUS -> sign == ZodiacSign.PISCES
        Planet.SATURN -> sign == ZodiacSign.LIBRA
        else -> false
    }
    if (exalted) return Dignity(stringResource(StringKeyMatch.PLANETARY_STATUS_EXALTED), AccentGreen)

    // Debilitation check
    val debilitated = when (planet) {
        Planet.SUN -> sign == ZodiacSign.LIBRA
        Planet.MOON -> sign == ZodiacSign.SCORPIO
        Planet.MARS -> sign == ZodiacSign.CANCER
        Planet.MERCURY -> sign == ZodiacSign.PISCES
        Planet.JUPITER -> sign == ZodiacSign.CAPRICORN
        Planet.VENUS -> sign == ZodiacSign.VIRGO
        Planet.SATURN -> sign == ZodiacSign.ARIES
        else -> false
    }
    if (debilitated) return Dignity(stringResource(StringKeyMatch.PLANETARY_STATUS_DEBILITATED), AccentRose)

    // Own sign check
    if (sign.ruler == planet) return Dignity(stringResource(StringKeyMatch.PLANETARY_STATUS_OWN_SIGN), AccentGold)

    // Moolatrikona check
    val moolatrikona = when (planet) {
        Planet.SUN -> sign == ZodiacSign.LEO
        Planet.MOON -> sign == ZodiacSign.TAURUS
        Planet.MARS -> sign == ZodiacSign.ARIES
        Planet.MERCURY -> sign == ZodiacSign.VIRGO
        Planet.JUPITER -> sign == ZodiacSign.SAGITTARIUS
        Planet.VENUS -> sign == ZodiacSign.LIBRA
        Planet.SATURN -> sign == ZodiacSign.AQUARIUS
        else -> false
    }
    if (moolatrikona) return Dignity(stringResource(StringKeyMatch.PLANETARY_STATUS_MOOLATRIKONA), AccentTeal)

    return Dignity(stringResource(StringKeyMatch.RELATION_NEUTRAL), TextSecondary)
}

@Composable
private fun getPlanetPredictions(
    position: PlanetPosition,
    shadbala: PlanetaryShadbala,
    chart: VedicChart
): List<Prediction> {
    val language = LocalLanguage.current
    val predictions = mutableListOf<Prediction>()
    val planet = position.planet
    val planetName = planet.getLocalizedName(language)

    // Strength-based predictions
    if (shadbala.isStrong) {
        predictions.add(Prediction(
            PredictionType.POSITIVE,
            stringResource(StringKeyAnalysis.PREDICTION_STRONG_PLANET, planetName),
            stringResource(StringKeyAnalysis.PREDICTION_STRONG_DESC)
        ))
    } else {
        predictions.add(Prediction(
            PredictionType.NEGATIVE,
            stringResource(StringKeyAnalysis.PREDICTION_WEAK_PLANET, planetName),
            stringResource(StringKeyAnalysis.PREDICTION_WEAK_DESC)
        ))
    }

    // Dignity-based predictions
    val dignity = getDignity(planet, position.sign)
    val exaltedStatus = stringResource(StringKeyMatch.PLANETARY_STATUS_EXALTED)
    val debilitatedStatus = stringResource(StringKeyMatch.PLANETARY_STATUS_DEBILITATED)
    val ownSignStatus = stringResource(StringKeyMatch.PLANETARY_STATUS_OWN_SIGN)

    when (dignity.status) {
        exaltedStatus -> predictions.add(Prediction(
            PredictionType.POSITIVE,
            stringResource(StringKeyAnalysis.PREDICTION_EXALTED),
            stringResource(StringKeyAnalysis.PREDICTION_EXALTED_DESC, planetName)
        ))
        debilitatedStatus -> predictions.add(Prediction(
            PredictionType.NEGATIVE,
            stringResource(StringKeyAnalysis.PREDICTION_DEBILITATED),
            stringResource(StringKeyAnalysis.PREDICTION_DEBILITATED_DESC, planetName)
        ))
        ownSignStatus -> predictions.add(Prediction(
            PredictionType.POSITIVE,
            stringResource(StringKeyAnalysis.PREDICTION_OWN_SIGN),
            stringResource(StringKeyAnalysis.PREDICTION_OWN_SIGN_DESC, planetName)
        ))
    }

    // Retrograde prediction
    if (position.isRetrograde) {
        predictions.add(Prediction(
            PredictionType.NEUTRAL,
            stringResource(StringKeyAnalysis.PREDICTION_RETROGRADE),
            stringResource(StringKeyAnalysis.PREDICTION_RETROGRADE_DESC)
        ))
    }

    // House-specific predictions
    when (position.house) {
        1, 5, 9 -> predictions.add(Prediction(
            PredictionType.POSITIVE,
            stringResource(StringKeyAnalysis.PREDICTION_TRIKONA),
            stringResource(StringKeyAnalysis.PREDICTION_TRIKONA_DESC, planetName, position.house)
        ))
        6, 8, 12 -> predictions.add(Prediction(
            PredictionType.NEUTRAL,
            stringResource(StringKeyAnalysis.PREDICTION_DUSTHANA),
            stringResource(StringKeyAnalysis.PREDICTION_DUSTHANA_DESC, planetName, position.house)
        ))
        4, 7, 10 -> predictions.add(Prediction(
            PredictionType.POSITIVE,
            stringResource(StringKeyAnalysis.PREDICTION_KENDRA),
            stringResource(StringKeyAnalysis.PREDICTION_KENDRA_DESC, planetName, position.house)
        ))
    }

    return predictions
}

@Composable
private fun getNakshatraDetails(nakshatra: Nakshatra): NakshatraDetails {
    return when (nakshatra) {
        Nakshatra.ASHWINI -> NakshatraDetails(
            symbol = stringResource(StringKeyComponents.NAK_ASHWINI_SYMBOL),
            nature = stringResource(StringKeyComponents.NAK_ASHWINI_NATURE),
            gender = stringResource(StringKeyComponents.NAK_ASHWINI_GENDER),
            gana = stringResource(StringKeyComponents.NAK_ASHWINI_GANA),
            guna = stringResource(StringKeyComponents.NAK_ASHWINI_GUNA),
            element = stringResource(StringKeyComponents.NAK_ASHWINI_ELEMENT),
            characteristics = stringResource(StringKeyComponents.NAK_ASHWINI_CHARS),
            careers = stringResource(StringKeyComponents.NAK_ASHWINI_CAREERS)
        )
        Nakshatra.BHARANI -> NakshatraDetails(
            symbol = stringResource(StringKeyComponents.NAK_BHARANI_SYMBOL),
            nature = stringResource(StringKeyComponents.NAK_BHARANI_NATURE),
            gender = stringResource(StringKeyComponents.NAK_BHARANI_GENDER),
            gana = stringResource(StringKeyComponents.NAK_BHARANI_GANA),
            guna = stringResource(StringKeyComponents.NAK_BHARANI_GUNA),
            element = stringResource(StringKeyComponents.NAK_BHARANI_ELEMENT),
            characteristics = stringResource(StringKeyComponents.NAK_BHARANI_CHARS),
            careers = stringResource(StringKeyComponents.NAK_BHARANI_CAREERS)
        )
        Nakshatra.ROHINI -> NakshatraDetails(
            symbol = stringResource(StringKeyComponents.NAK_ROHINI_SYMBOL),
            nature = stringResource(StringKeyComponents.NAK_ROHINI_NATURE),
            gender = stringResource(StringKeyComponents.NAK_ROHINI_GENDER),
            gana = stringResource(StringKeyComponents.NAK_ROHINI_GANA),
            guna = stringResource(StringKeyComponents.NAK_ROHINI_GUNA),
            element = stringResource(StringKeyComponents.NAK_ROHINI_ELEMENT),
            characteristics = stringResource(StringKeyComponents.NAK_ROHINI_CHARS),
            careers = stringResource(StringKeyComponents.NAK_ROHINI_CAREERS)
        )
        else -> NakshatraDetails(
            symbol = nakshatra.deity,
            nature = stringResource(StringKeyComponents.NAK_NATURE_MIXED),
            gender = stringResource(StringKeyComponents.NAK_GENDER_NEUTRAL),
            gana = stringResource(StringKeyComponents.NAK_GANA_MIXED),
            guna = stringResource(StringKeyComponents.NAK_GUNA_MIXED),
            element = stringResource(StringKeyComponents.NAK_ELEMENT_MIXED),
            characteristics = stringResource(StringKeyComponents.NAK_CHARS_DEFAULT, nakshatra.localizedName(), nakshatra.ruler.localizedName(), nakshatra.deity),
            careers = stringResource(StringKeyComponents.NAK_CAREERS_DEFAULT)
        )
    }
}

@Composable
private fun getPadaDescription(nakshatra: Nakshatra, pada: Int): String {
    val padaSigns = listOf(
        nakshatra.pada1Sign,
        nakshatra.pada2Sign,
        nakshatra.pada3Sign,
        nakshatra.pada4Sign
    )
    val padaSign = padaSigns[pada - 1]

    return stringResource(
        StringKeyComponents.PADA_DESC_TEMPLATE,
        pada,
        padaSign.localizedName(),
        padaSign.ruler.localizedName(),
        padaSign.element
    )
}

@Composable
private fun getHouseDetails(house: Int): HouseDetails {
    return when (house) {
        1 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_1_NAME),
            type = stringResource(StringKeyComponents.HOUSE_1_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_1_SIG_1),
                stringResource(StringKeyComponents.HOUSE_1_SIG_2),
                stringResource(StringKeyComponents.HOUSE_1_SIG_3),
                stringResource(StringKeyComponents.HOUSE_1_SIG_4),
                stringResource(StringKeyComponents.HOUSE_1_SIG_5),
                stringResource(StringKeyComponents.HOUSE_1_SIG_6),
                stringResource(StringKeyComponents.HOUSE_1_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_1_INTERP)
        )
        2 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_2_NAME),
            type = stringResource(StringKeyComponents.HOUSE_2_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_2_SIG_1),
                stringResource(StringKeyComponents.HOUSE_2_SIG_2),
                stringResource(StringKeyComponents.HOUSE_2_SIG_3),
                stringResource(StringKeyComponents.HOUSE_2_SIG_4),
                stringResource(StringKeyComponents.HOUSE_2_SIG_5),
                stringResource(StringKeyComponents.HOUSE_2_SIG_6),
                stringResource(StringKeyComponents.HOUSE_2_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_2_INTERP)
        )
        3 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_3_NAME),
            type = stringResource(StringKeyComponents.HOUSE_3_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_3_SIG_1),
                stringResource(StringKeyComponents.HOUSE_3_SIG_2),
                stringResource(StringKeyComponents.HOUSE_3_SIG_3),
                stringResource(StringKeyComponents.HOUSE_3_SIG_4),
                stringResource(StringKeyComponents.HOUSE_3_SIG_5),
                stringResource(StringKeyComponents.HOUSE_3_SIG_6),
                stringResource(StringKeyComponents.HOUSE_3_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_3_INTERP)
        )
        4 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_4_NAME),
            type = stringResource(StringKeyComponents.HOUSE_4_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_4_SIG_1),
                stringResource(StringKeyComponents.HOUSE_4_SIG_2),
                stringResource(StringKeyComponents.HOUSE_4_SIG_3),
                stringResource(StringKeyComponents.HOUSE_4_SIG_4),
                stringResource(StringKeyComponents.HOUSE_4_SIG_5),
                stringResource(StringKeyComponents.HOUSE_4_SIG_6),
                stringResource(StringKeyComponents.HOUSE_4_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_4_INTERP)
        )
        5 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_5_NAME),
            type = stringResource(StringKeyComponents.HOUSE_5_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_5_SIG_1),
                stringResource(StringKeyComponents.HOUSE_5_SIG_2),
                stringResource(StringKeyComponents.HOUSE_5_SIG_3),
                stringResource(StringKeyComponents.HOUSE_5_SIG_4),
                stringResource(StringKeyComponents.HOUSE_5_SIG_5),
                stringResource(StringKeyComponents.HOUSE_5_SIG_6),
                stringResource(StringKeyComponents.HOUSE_5_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_5_INTERP)
        )
        6 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_6_NAME),
            type = stringResource(StringKeyComponents.HOUSE_6_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_6_SIG_1),
                stringResource(StringKeyComponents.HOUSE_6_SIG_2),
                stringResource(StringKeyComponents.HOUSE_6_SIG_3),
                stringResource(StringKeyComponents.HOUSE_6_SIG_4),
                stringResource(StringKeyComponents.HOUSE_6_SIG_5),
                stringResource(StringKeyComponents.HOUSE_6_SIG_6),
                stringResource(StringKeyComponents.HOUSE_6_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_6_INTERP)
        )
        7 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_7_NAME),
            type = stringResource(StringKeyComponents.HOUSE_7_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_7_SIG_1),
                stringResource(StringKeyComponents.HOUSE_7_SIG_2),
                stringResource(StringKeyComponents.HOUSE_7_SIG_3),
                stringResource(StringKeyComponents.HOUSE_7_SIG_4),
                stringResource(StringKeyComponents.HOUSE_7_SIG_5),
                stringResource(StringKeyComponents.HOUSE_7_SIG_6),
                stringResource(StringKeyComponents.HOUSE_7_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_7_INTERP)
        )
        8 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_8_NAME),
            type = stringResource(StringKeyComponents.HOUSE_8_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_8_SIG_1),
                stringResource(StringKeyComponents.HOUSE_8_SIG_2),
                stringResource(StringKeyComponents.HOUSE_8_SIG_3),
                stringResource(StringKeyComponents.HOUSE_8_SIG_4),
                stringResource(StringKeyComponents.HOUSE_8_SIG_5),
                stringResource(StringKeyComponents.HOUSE_8_SIG_6),
                stringResource(StringKeyComponents.HOUSE_8_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_8_INTERP)
        )
        9 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_9_NAME),
            type = stringResource(StringKeyComponents.HOUSE_9_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_9_SIG_1),
                stringResource(StringKeyComponents.HOUSE_9_SIG_2),
                stringResource(StringKeyComponents.HOUSE_9_SIG_3),
                stringResource(StringKeyComponents.HOUSE_9_SIG_4),
                stringResource(StringKeyComponents.HOUSE_9_SIG_5),
                stringResource(StringKeyComponents.HOUSE_9_SIG_6),
                stringResource(StringKeyComponents.HOUSE_9_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_9_INTERP)
        )
        10 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_10_NAME),
            type = stringResource(StringKeyComponents.HOUSE_10_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_10_SIG_1),
                stringResource(StringKeyComponents.HOUSE_10_SIG_2),
                stringResource(StringKeyComponents.HOUSE_10_SIG_3),
                stringResource(StringKeyComponents.HOUSE_10_SIG_4),
                stringResource(StringKeyComponents.HOUSE_10_SIG_5),
                stringResource(StringKeyComponents.HOUSE_10_SIG_6),
                stringResource(StringKeyComponents.HOUSE_10_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_10_INTERP)
        )
        11 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_11_NAME),
            type = stringResource(StringKeyComponents.HOUSE_11_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_11_SIG_1),
                stringResource(StringKeyComponents.HOUSE_11_SIG_2),
                stringResource(StringKeyComponents.HOUSE_11_SIG_3),
                stringResource(StringKeyComponents.HOUSE_11_SIG_4),
                stringResource(StringKeyComponents.HOUSE_11_SIG_5),
                stringResource(StringKeyComponents.HOUSE_11_SIG_6),
                stringResource(StringKeyComponents.HOUSE_11_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_11_INTERP)
        )
        12 -> HouseDetails(
            name = stringResource(StringKeyComponents.HOUSE_12_NAME),
            type = stringResource(StringKeyComponents.HOUSE_12_TYPE),
            significations = listOf(
                stringResource(StringKeyComponents.HOUSE_12_SIG_1),
                stringResource(StringKeyComponents.HOUSE_12_SIG_2),
                stringResource(StringKeyComponents.HOUSE_12_SIG_3),
                stringResource(StringKeyComponents.HOUSE_12_SIG_4),
                stringResource(StringKeyComponents.HOUSE_12_SIG_5),
                stringResource(StringKeyComponents.HOUSE_12_SIG_6),
                stringResource(StringKeyComponents.HOUSE_12_SIG_7)
            ),
            interpretation = stringResource(StringKeyComponents.HOUSE_12_INTERP)
        )
        else -> HouseDetails("", "", emptyList(), "")
    }
}

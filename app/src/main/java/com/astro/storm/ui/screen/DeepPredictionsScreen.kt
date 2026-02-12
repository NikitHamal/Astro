package com.astro.storm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import com.astro.storm.ui.components.ScreenTopBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astro.storm.core.common.StringKey
import com.astro.storm.core.model.VedicChart
import com.astro.storm.ui.theme.*
import com.astro.storm.ui.viewmodel.DeepPredictionsUiState
import com.astro.storm.ui.viewmodel.DeepPredictionsViewModel
import com.astro.storm.data.localization.localizedName
import com.astro.storm.data.localization.formatLocalized
import com.astro.storm.data.localization.stringResource
import com.astro.storm.core.common.StringKeyUIExtra
import com.astro.storm.core.common.StringKeyAnalysis
import com.astro.storm.ui.components.deepanalysis.LocalizedParagraphText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepPredictionsScreen(
    chart: VedicChart,
    onBack: () -> Unit,
    viewModel: DeepPredictionsViewModel = hiltViewModel()
) {
    Scaffold(
        containerColor = Vellum,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daily Insight".uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateMuted,
                        fontFamily = SpaceGroteskFontFamily,
                        letterSpacing = 1.5.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, "Back", tint = CosmicIndigo)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Outlined.Share, "Share", tint = CosmicIndigo)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Vellum.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        DeepPredictionsBody(
            chart = chart,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DeepPredictionsBody(
    chart: VedicChart,
    viewModel: DeepPredictionsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(chart) {
        viewModel.calculatePredictions(chart)
    }

    Box(modifier = modifier.fillMaxSize()) {
        com.astro.storm.ui.screen.main.GrainTextureOverlay()

        when (val state = uiState) {
            is DeepPredictionsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VedicGold)
                }
            }
            is DeepPredictionsUiState.Success -> {
                OracleContent(state.predictions)
            }
            is DeepPredictionsUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = MarsRed)
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun OracleContent(predictions: com.astro.storm.ephemeris.deepanalysis.predictions.DeepPredictions) {
    val currentMahadasha = predictions.dashaAnalysis.currentMahadasha

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Block
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 40.dp)) {
                Icon(Icons.Outlined.Public, null, tint = VedicGold, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentMahadasha?.planet?.localizedName()?.uppercase()?.let { "$it DASHA" } ?: "CELESTIAL CYCLE",
                    style = MaterialTheme.typography.displaySmall,
                    color = CosmicIndigo,
                    fontFamily = CinzelDecorativeFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                currentMahadasha?.let { dasha ->
                    Text(
                        text = "The Teacher of Patience",
                        style = MaterialTheme.typography.headlineSmall,
                        color = VedicGold,
                        fontFamily = CormorantGaramondFontFamily,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.width(48.dp).height(1.dp).background(BorderSubtle))
                        Text(
                            text = "Cycle: ${dasha.startDate.formatLocalized(com.astro.storm.data.localization.DateFormat.YEAR_ONLY)} — ${dasha.endDate.formatLocalized(com.astro.storm.data.localization.DateFormat.YEAR_ONLY)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateMuted,
                            fontFamily = SpaceGroteskFontFamily,
                            letterSpacing = 1.5.sp
                        )
                        Box(Modifier.width(48.dp).height(1.dp).background(BorderSubtle))
                    }
                }
            }
        }

        // Article Body with Drop Cap
        item {
            currentMahadasha?.let { dasha ->
                val themeText = dasha.overallTheme.en
                DropCapParagraph(themeText)
            }
        }

        // Pull Quote
        item {
            currentMahadasha?.let { dasha ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(Modifier.align(Alignment.TopCenter), color = VedicGold.copy(alpha = 0.6f))
                    HorizontalDivider(Modifier.align(Alignment.BottomCenter), color = VedicGold.copy(alpha = 0.6f))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Message, null, tint = VedicGold, modifier = Modifier.size(24.dp).background(Vellum).padding(horizontal = 8.dp))
                        Text(
                            text = "\"${dasha.advice.en}\"",
                            style = MaterialTheme.typography.headlineSmall,
                            color = CosmicIndigo,
                            fontFamily = CormorantGaramondFontFamily,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )
                        Icon(Icons.Outlined.Star, null, tint = VedicGold, modifier = Modifier.size(24.dp).background(Vellum).padding(horizontal = 8.dp))
                    }
                }
            }
        }

        // Additional Content
        item {
            Text(
                text = predictions.overallPredictionSummary.en,
                style = MaterialTheme.typography.bodyLarge,
                color = CosmicIndigo,
                fontFamily = CormorantGaramondFontFamily,
                lineHeight = 28.sp,
                textAlign = TextAlign.Justify
            )
        }

        // End Ornament
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 48.dp, bottom = 64.dp).alpha(0.6f)) {
                Icon(Icons.Outlined.Star, null, tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "READING COMPLETE",
                    style = MaterialTheme.typography.labelSmall,
                    color = CosmicIndigo,
                    fontFamily = SpaceGroteskFontFamily,
                    letterSpacing = 2.sp
                )
            }
        }
    }

    // Floating Action Button
    Box(Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { /* Save */ },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = CosmicIndigo,
            contentColor = Vellum,
            shape = CircleShape
        ) {
            Icon(Icons.Outlined.Bookmark, null)
        }
    }
}

@Composable
fun DropCapParagraph(text: String) {
    if (text.isEmpty()) return
    val dropCap = text.take(1)
    val remainingText = text.drop(1)

    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = dropCap,
            style = MaterialTheme.typography.displayLarge,
            color = VedicGold,
            fontFamily = CinzelDecorativeFontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 50.sp,
            modifier = Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = remainingText,
            style = MaterialTheme.typography.bodyLarge,
            color = CosmicIndigo,
            fontFamily = CormorantGaramondFontFamily,
            lineHeight = 28.sp,
            textAlign = TextAlign.Justify
        )
    }
}

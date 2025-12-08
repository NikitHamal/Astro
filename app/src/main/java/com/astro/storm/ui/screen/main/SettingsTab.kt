package com.astro.storm.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.annotation.StringRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.R
import com.astro.storm.data.model.HouseSystem
import com.astro.storm.data.model.VedicChart
import com.astro.storm.data.repository.SavedChart
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.util.Language
import com.astro.storm.util.LocaleManager
import android.app.Activity

/**
 * Settings Tab - App Settings & Profile Management
 *
 * Provides access to:
 * - Profile management (edit, delete)
 * - Export options
 * - App preferences
 * - About section
 */
@Composable
fun SettingsTab(
    currentChart: VedicChart?,
    savedCharts: List<SavedChart>,
    onEditProfile: () -> Unit,
    onDeleteProfile: (Long) -> Unit,
    onExportChart: (ExportFormat) -> Unit,
    onManageProfiles: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var chartToDelete by remember { mutableStateOf<SavedChart?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Profile Section
        item {
            SettingsSectionHeader(title = stringResource(id = R.string.profile))
        }

        item {
            currentChart?.let { chart ->
                CurrentProfileCard(
                    chart = chart,
                    onExport = { showExportDialog = true }
                )
            } ?: run {
                EmptyProfileCard(onManageProfiles = onManageProfiles)
            }
        }

        item {
            if (currentChart != null) {
                SettingsItem(
                    icon = Icons.Outlined.Edit,
                    title = stringResource(id = R.string.edit_profile),
                    subtitle = stringResource(id = R.string.modify_birth_details),
                    onClick = onEditProfile
                )
            } else {
                SettingsItem(
                    icon = Icons.Outlined.People,
                    title = stringResource(id = R.string.manage_profiles),
                    subtitle = stringResource(id = R.string.no_profile_selected),
                    onClick = onManageProfiles
                )
            }
        }

        // Export Section
        if (currentChart != null) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsSectionHeader(title = stringResource(id = R.string.export))
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.PictureAsPdf,
                    title = stringResource(id = R.string.export_as_pdf),
                    subtitle = stringResource(id = R.string.complete_chart_report),
                    onClick = { onExportChart(ExportFormat.PDF) }
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Image,
                    title = stringResource(id = R.string.export_as_image),
                    subtitle = stringResource(id = R.string.high_quality_chart_image),
                    onClick = { onExportChart(ExportFormat.IMAGE) }
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.ContentCopy,
                    title = stringResource(id = R.string.copy_to_clipboard),
                    subtitle = stringResource(id = R.string.plain_text_format),
                    onClick = { onExportChart(ExportFormat.CLIPBOARD) }
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Code,
                    title = stringResource(id = R.string.export_as_json),
                    subtitle = stringResource(id = R.string.machine_readable_format),
                    onClick = { onExportChart(ExportFormat.JSON) }
                )
            }
        }

        // Preferences Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SettingsSectionHeader(title = stringResource(id = R.string.preferences))
        }

        item {
            HouseSystemSetting()
        }

        item {
            AyanamsaSetting()
        }

        item {
            LanguageSetting()
        }

        // About Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SettingsSectionHeader(title = stringResource(id = R.string.about))
        }

        item {
            SettingsItem(
                icon = Icons.Outlined.Info,
                title = stringResource(id = R.string.about_astrostorm),
                subtitle = stringResource(id = R.string.version_1_0_0),
                onClick = { /* Show about dialog */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Outlined.Science,
                title = stringResource(id = R.string.calculation_engine),
                subtitle = stringResource(id = R.string.swiss_ephemeris_jpl_mode),
                onClick = { /* Show calculation info */ }
            )
        }

        item {
            AboutCard()
        }
    }

    // Export Dialog
    if (showExportDialog && currentChart != null) {
        ExportOptionsDialog(
            onDismiss = { showExportDialog = false },
            onExport = { format ->
                onExportChart(format)
                showExportDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && chartToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = AppTheme.CardBackground,
            titleContentColor = AppTheme.TextPrimary,
            textContentColor = AppTheme.TextSecondary,
            title = { Text(stringResource(id = R.string.delete_profile)) },
            text = {
                Text(stringResource(id = R.string.are_you_sure_you_want_to_delete, chartToDelete?.name ?: ""))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        chartToDelete?.let { onDeleteProfile(it.id) }
                        showDeleteDialog = false
                        chartToDelete = null
                    }
                ) {
                    Text(stringResource(id = R.string.delete), color = AppTheme.ErrorColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(id = R.string.cancel), color = AppTheme.AccentPrimary)
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = AppTheme.AccentPrimary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
private fun CurrentProfileCard(
    chart: VedicChart,
    onExport: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AppTheme.AccentPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chart.birthData.name
                            .split(" ")
                            .take(2)
                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .joinToString(""),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.AccentPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chart.birthData.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = chart.birthData.dateTime.toLocalDate().toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                    Text(
                        text = chart.birthData.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = AppTheme.DividerColor)

            Spacer(modifier = Modifier.height(12.dp))

            // Chart details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ChartDetailItem(
                    label = stringResource(id = R.string.ascendant),
                    value = chart.planetPositions.find { it.planet.displayName == "Sun" }?.sign?.displayName
                        ?: com.astro.storm.data.model.ZodiacSign.fromLongitude(chart.ascendant).displayName
                )
                ChartDetailItem(
                    label = stringResource(id = R.string.moon_sign),
                    value = chart.planetPositions.find { it.planet == com.astro.storm.data.model.Planet.MOON }?.sign?.displayName
                        ?: "-"
                )
                ChartDetailItem(
                    label = stringResource(id = R.string.nakshatra),
                    value = chart.planetPositions.find { it.planet == com.astro.storm.data.model.Planet.MOON }?.nakshatra?.displayName?.take(8)
                        ?: "-"
                )
            }
        }
    }
}

@Composable
private fun ChartDetailItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppTheme.TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun EmptyProfileCard(onManageProfiles: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onManageProfiles() },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonAdd,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.no_profile_selected),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AppTheme.TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.tap_to_select_or_create_a_profile),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppTheme.ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppTheme.AccentPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.TextMuted
                )
            }

            trailing?.invoke() ?: Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AppTheme.TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun HouseSystemSetting() {
    var expanded by remember { mutableStateOf(false) }
    var selectedSystem by remember { mutableStateOf(HouseSystem.DEFAULT) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AppTheme.ChipBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.GridView,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.house_system),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = selectedSystem.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.AccentPrimary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            if (expanded) {
                HorizontalDivider(color = AppTheme.DividerColor)

                HouseSystem.entries.forEach { system ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedSystem = system
                                expanded = false
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = system == selectedSystem,
                            onClick = {
                                selectedSystem = system
                                expanded = false
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppTheme.AccentPrimary,
                                unselectedColor = AppTheme.TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = system.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (system == selectedSystem) AppTheme.TextPrimary else AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AyanamsaSetting() {
    val ayanamsaOptions = listOf("Lahiri", "Raman", "Krishnamurti", "True Chitrapaksha")
    var expanded by remember { mutableStateOf(false) }
    var selectedAyanamsa by remember { mutableStateOf(ayanamsaOptions[0]) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AppTheme.ChipBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.ayanamsa),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = selectedAyanamsa,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.AccentPrimary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            if (expanded) {
                HorizontalDivider(color = AppTheme.DividerColor)

                ayanamsaOptions.forEach { ayanamsa ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedAyanamsa = ayanamsa
                                expanded = false
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = ayanamsa == selectedAyanamsa,
                            onClick = {
                                selectedAyanamsa = ayanamsa
                                expanded = false
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppTheme.AccentPrimary,
                                unselectedColor = AppTheme.TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = ayanamsa,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (ayanamsa == selectedAyanamsa) AppTheme.TextPrimary else AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.astrostorm),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.AccentPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.ultra_precision_vedic_astrology),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.powered_by_swiss_ephemeris),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FeatureBadge(text = stringResource(id = R.string.lahiri_ayanamsa))
                FeatureBadge(text = stringResource(id = R.string.placidus_houses))
            }
        }
    }
}

@Composable
private fun FeatureBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(AppTheme.ChipBackground)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.TextMuted
        )
    }
}

@Composable
private fun ExportOptionsDialog(
    onDismiss: () -> Unit,
    onExport: (ExportFormat) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.CardBackground,
        titleContentColor = AppTheme.TextPrimary,
        title = {
            Text(
                text = stringResource(id = R.string.export_chart),
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                ExportFormat.entries.forEach { format ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onExport(format) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = format.icon,
                            contentDescription = null,
                            tint = AppTheme.AccentPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = stringResource(id = format.titleRes),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = AppTheme.TextPrimary
                            )
                            Text(
                                text = stringResource(id = format.descriptionRes),
                                style = MaterialTheme.typography.bodySmall,
                                color = AppTheme.TextMuted
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel), color = AppTheme.AccentPrimary)
            }
        }
    )
}

enum class ExportFormat(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val icon: ImageVector
) {
    PDF(R.string.pdf_report, R.string.complete_chart_analysis, Icons.Outlined.PictureAsPdf),
    IMAGE(R.string.chart_image, R.string.high_quality_png, Icons.Outlined.Image),
    JSON(R.string.json_data, R.string.machine_readable_format, Icons.Outlined.Code),
    CSV(R.string.csv_data, R.string.spreadsheet_format, Icons.Outlined.TableChart),
    CLIPBOARD(R.string.copy_text, R.string.plain_text_to_clipboard, Icons.Outlined.ContentCopy)
}

@Composable
private fun LanguageSetting() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(LocaleManager.getLanguage(context)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(12.dp)
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AppTheme.ChipBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = null,
                        tint = AppTheme.AccentPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.language),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.TextPrimary
                    )
                    Text(
                        text = selectedLanguage.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.AccentPrimary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AppTheme.TextMuted
                )
            }

            if (expanded) {
                HorizontalDivider(color = AppTheme.DividerColor)

                Language.entries.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLanguage = language
                                expanded = false
                                LocaleManager.setLocale(context, language)
                                (context as? Activity)?.recreate()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == selectedLanguage,
                            onClick = {
                                selectedLanguage = language
                                expanded = false
                                LocaleManager.setLocale(context, language)
                                (context as? Activity)?.recreate()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppTheme.AccentPrimary,
                                unselectedColor = AppTheme.TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = language.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (language == selectedLanguage) AppTheme.TextPrimary else AppTheme.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

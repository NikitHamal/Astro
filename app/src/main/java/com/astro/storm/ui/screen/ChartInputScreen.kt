package com.astro.storm.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.storm.data.localization.BikramSambatConverter
import com.astro.storm.data.localization.DateSystem
import com.astro.storm.data.localization.Language
import com.astro.storm.data.localization.LocalDateSystem
import com.astro.storm.data.localization.LocalLanguage
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.getLocalizedName
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.BirthData
import com.astro.storm.data.model.Gender
import com.astro.storm.ui.components.BSDatePickerDialog
import com.astro.storm.ui.components.LocationSearchField
import com.astro.storm.ui.viewmodel.ChartUiState
import com.astro.storm.ui.theme.AppTheme
import com.astro.storm.ui.viewmodel.ChartViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartInputScreen(
    viewModel: ChartViewModel,
    onNavigateBack: () -> Unit,
    onChartCalculated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val language = LocalLanguage.current
    val dateSystem = LocalDateSystem.current

    var name by rememberSaveable { mutableStateOf("") }
    var selectedGender by rememberSaveable { mutableStateOf(Gender.OTHER) }
    var locationLabel by rememberSaveable { mutableStateOf("") }
    var selectedDateMillis by rememberSaveable {
        mutableStateOf(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }
    var selectedHour by rememberSaveable { mutableStateOf(10) }
    var selectedMinute by rememberSaveable { mutableStateOf(0) }
    var latitude by rememberSaveable { mutableStateOf("") }
    var longitude by rememberSaveable { mutableStateOf("") }
    var altitude by rememberSaveable { mutableStateOf("") }
    var selectedTimezone by rememberSaveable { mutableStateOf(ZoneId.systemDefault().id) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showBSDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    // Allow users to select which date picker to use (independent of global setting)
    var useBSPicker by remember { mutableStateOf(dateSystem == DateSystem.BS) }
    var showTimezoneDropdown by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errorKey by remember { mutableStateOf<StringKey?>(null) }
    var chartCalculationInitiated by remember { mutableStateOf(false) }

    val selectedDate = remember(selectedDateMillis) {
        java.time.Instant.ofEpochMilli(selectedDateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    val selectedTime = remember(selectedHour, selectedMinute) {
        LocalTime.of(selectedHour, selectedMinute)
    }

    val latitudeFocusRequester = remember { FocusRequester() }
    val longitudeFocusRequester = remember { FocusRequester() }
    val altitudeFocusRequester = remember { FocusRequester() }

    val timezones = remember {
        val common = listOf(
            "Asia/Kathmandu", "Asia/Kolkata", "Asia/Dubai", "Asia/Singapore",
            "Asia/Tokyo", "Asia/Shanghai", "Asia/Hong_Kong", "Europe/London",
            "Europe/Paris", "Europe/Berlin", "America/New_York", "America/Los_Angeles",
            "America/Chicago", "America/Denver", "Australia/Sydney", "Pacific/Auckland"
        )
        val all = TimeZone.getAvailableIDs()
            .filter { it.contains("/") && !it.startsWith("Etc/") && !it.startsWith("SystemV/") }
            .sorted()
        (common + all).distinct()
    }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ChartUiState.Success -> {
                if (chartCalculationInitiated) {
                    viewModel.saveChart(state.chart)
                }
            }
            is ChartUiState.Saved -> {
                if (chartCalculationInitiated) {
                    chartCalculationInitiated = false
                    onChartCalculated()
                }
            }
            is ChartUiState.Error -> {
                errorMessage = state.message
                showErrorDialog = true
                chartCalculationInitiated = false
            }
            else -> {}
        }
    }

    val scrollState = rememberScrollState()
    val isCalculating = uiState is ChartUiState.Calculating

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            ChartInputHeader(onNavigateBack = onNavigateBack)

            Spacer(modifier = Modifier.height(28.dp))

            IdentitySection(
                name = name,
                onNameChange = { name = it },
                selectedGender = selectedGender,
                onGenderChange = { selectedGender = it },
                onFocusNext = { focusManager.clearFocus() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LocationSearchField(
                value = locationLabel,
                onValueChange = { locationLabel = it },
                onLocationSelected = { location, lat, lon ->
                    locationLabel = location
                    latitude = String.format(java.util.Locale.US, "%.6f", lat)
                    longitude = String.format(java.util.Locale.US, "%.6f", lon)
                },
                label = stringResource(StringKey.INPUT_LOCATION),
                placeholder = stringResource(StringKey.INPUT_SEARCH_LOCATION)
            )

            Spacer(modifier = Modifier.height(28.dp))

            DateTimeSection(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                selectedTimezone = selectedTimezone,
                timezones = timezones,
                showTimezoneDropdown = showTimezoneDropdown,
                useBSPicker = useBSPicker,
                language = language,
                onShowDatePicker = {
                    if (useBSPicker) {
                        showBSDatePicker = true
                    } else {
                        showDatePicker = true
                    }
                },
                onShowTimePicker = { showTimePicker = true },
                onTimezoneDropdownChange = { showTimezoneDropdown = it },
                onTimezoneSelected = {
                    selectedTimezone = it
                    showTimezoneDropdown = false
                },
                onToggleDateSystem = { useBSPicker = !useBSPicker }
            )

            Spacer(modifier = Modifier.height(28.dp))

            CoordinatesSection(
                latitude = latitude,
                longitude = longitude,
                altitude = altitude,
                onLatitudeChange = { latitude = it },
                onLongitudeChange = { longitude = it },
                onAltitudeChange = { altitude = it },
                latitudeFocusRequester = latitudeFocusRequester,
                longitudeFocusRequester = longitudeFocusRequester,
                altitudeFocusRequester = altitudeFocusRequester,
                onDone = { focusManager.clearFocus() }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Extract localized string outside the lambda (stringResource is @Composable)
            val unknownText = stringResource(StringKey.MISC_UNKNOWN)

            GenerateButton(
                isCalculating = isCalculating,
                onClick = {
                    val validationKey = validateInputLocalized(latitude, longitude)
                    if (validationKey != null) {
                        errorKey = validationKey
                        showErrorDialog = true
                        return@GenerateButton
                    }

                    val lat = latitude.toDouble()
                    val lon = longitude.toDouble()
                    val dateTime = LocalDateTime.of(selectedDate, selectedTime)

                    val birthData = BirthData(
                        name = name.ifBlank { unknownText },
                        dateTime = dateTime,
                        latitude = lat,
                        longitude = lon,
                        timezone = selectedTimezone,
                        location = locationLabel.ifBlank { unknownText },
                        gender = selectedGender
                    )

                    chartCalculationInitiated = true
                    viewModel.calculateChart(birthData)
                }
            )
        }
    }

    if (showDatePicker) {
        ChartDatePickerDialog(
            initialDateMillis = selectedDateMillis,
            onDismiss = { showDatePicker = false },
            onConfirm = { millis ->
                selectedDateMillis = millis
                showDatePicker = false
            }
        )
    }

    // BS Date Picker (when date system is BS)
    if (showBSDatePicker) {
        val currentBSDate = remember(selectedDate) {
            BikramSambatConverter.toBS(selectedDate) ?: BikramSambatConverter.today()
        }
        BSDatePickerDialog(
            initialDate = currentBSDate,
            onDismiss = { showBSDatePicker = false },
            onConfirm = { bsDate ->
                // Convert BS to AD and update selectedDateMillis
                BikramSambatConverter.toAD(bsDate)?.let { adDate ->
                    selectedDateMillis = adDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                }
                showBSDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        ChartTimePickerDialog(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                showTimePicker = false
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                errorKey = null
            },
            title = {
                Text(
                    stringResource(StringKey.ERROR_INPUT),
                    color = AppTheme.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    errorKey?.let { stringResource(it) } ?: errorMessage,
                    color = AppTheme.TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    errorKey = null
                }) {
                    Text(stringResource(StringKey.BTN_OK), color = AppTheme.AccentGold)
                }
            },
            containerColor = AppTheme.CardBackground,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

private fun validateInputLocalized(latitude: String, longitude: String): StringKey? {
    val lat = latitude.toDoubleOrNull()
    val lon = longitude.toDoubleOrNull()

    return when {
        lat == null || lon == null -> StringKey.ERROR_INVALID_COORDS
        lat < -90 || lat > 90 -> StringKey.ERROR_LATITUDE_RANGE
        lon < -180 || lon > 180 -> StringKey.ERROR_LONGITUDE_RANGE
        else -> null
    }
}

@Composable
private fun ChartInputHeader(onNavigateBack: () -> Unit) {
    val goBackText = stringResource(StringKey.BTN_BACK)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.semantics { contentDescription = goBackText }
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = null,
                tint = AppTheme.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(StringKey.INPUT_NEW_CHART),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.TextPrimary,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun IdentitySection(
    name: String,
    onNameChange: (String) -> Unit,
    selectedGender: Gender,
    onGenderChange: (Gender) -> Unit,
    onFocusNext: () -> Unit
) {
    val language = LocalLanguage.current
    Column {
        SectionTitle(stringResource(StringKey.INPUT_IDENTITY))
        Spacer(modifier = Modifier.height(12.dp))

        ChartOutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = stringResource(StringKey.INPUT_FULL_NAME),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onFocusNext() })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(StringKey.INPUT_GENDER),
            fontSize = 14.sp,
            color = AppTheme.TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Gender.entries.forEach { gender ->
                GenderChip(
                    text = gender.getLocalizedName(language),
                    isSelected = selectedGender == gender,
                    onClick = { onGenderChange(gender) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeSection(
    selectedDate: LocalDate,
    selectedTime: LocalTime,
    selectedTimezone: String,
    timezones: List<String>,
    showTimezoneDropdown: Boolean,
    useBSPicker: Boolean,
    language: Language,
    onShowDatePicker: () -> Unit,
    onShowTimePicker: () -> Unit,
    onTimezoneDropdownChange: (Boolean) -> Unit,
    onTimezoneSelected: (String) -> Unit,
    onToggleDateSystem: () -> Unit
) {
    // Format date based on selected picker type
    val dateDisplayText = remember(selectedDate, useBSPicker, language) {
        if (useBSPicker) {
            BikramSambatConverter.toBS(selectedDate)?.format(language)
                ?: selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } else {
            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }

    val selectDateText = stringResource(StringKey.INPUT_SELECT_DATE)
    val selectTimeText = stringResource(StringKey.INPUT_SELECT_TIME)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(stringResource(StringKey.INPUT_DATE_TIME))

            // Date System Toggle (AD / BS)
            DateSystemToggle(
                useBSPicker = useBSPicker,
                language = language,
                onToggle = onToggleDateSystem
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DateTimeChip(
                text = dateDisplayText,
                onClick = onShowDatePicker,
                modifier = Modifier.weight(1f),
                contentDescription = selectDateText
            )
            DateTimeChip(
                text = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                onClick = onShowTimePicker,
                modifier = Modifier.weight(0.7f),
                contentDescription = selectTimeText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = showTimezoneDropdown,
            onExpandedChange = onTimezoneDropdownChange
        ) {
            OutlinedTextField(
                value = selectedTimezone,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(stringResource(StringKey.INPUT_TIMEZONE), color = AppTheme.TextSecondary, fontSize = 14.sp)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTimezoneDropdown)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = chartTextFieldColors(),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )

            ExposedDropdownMenu(
                expanded = showTimezoneDropdown,
                onDismissRequest = { onTimezoneDropdownChange(false) },
                modifier = Modifier
                    .background(AppTheme.CardBackground)
                    .heightIn(max = 300.dp)
            ) {
                timezones.forEach { timezone ->
                    DropdownMenuItem(
                        text = {
                            Text(text = timezone, color = AppTheme.TextPrimary, fontSize = 14.sp)
                        },
                        onClick = { onTimezoneSelected(timezone) },
                        colors = MenuDefaults.itemColors(textColor = AppTheme.TextPrimary)
                    )
                }
            }
        }
    }
}

/**
 * Toggle component for switching between AD and BS date input
 */
@Composable
private fun DateSystemToggle(
    useBSPicker: Boolean,
    language: Language,
    onToggle: () -> Unit
) {
    val adLabel = when (language) {
        Language.ENGLISH -> "AD"
        Language.NEPALI -> "ई.सं."
    }
    val bsLabel = when (language) {
        Language.ENGLISH -> "BS"
        Language.NEPALI -> "वि.सं."
    }

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.CardBackground,
        border = BorderStroke(1.dp, AppTheme.BorderColor)
    ) {
        Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AD option
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (!useBSPicker) AppTheme.AccentGold else Color.Transparent,
                modifier = Modifier.padding(1.dp)
            ) {
                Text(
                    text = adLabel,
                    fontSize = 12.sp,
                    fontWeight = if (!useBSPicker) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (!useBSPicker) AppTheme.ButtonText else AppTheme.TextSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // BS option
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (useBSPicker) AppTheme.AccentGold else Color.Transparent,
                modifier = Modifier.padding(1.dp)
            ) {
                Text(
                    text = bsLabel,
                    fontSize = 12.sp,
                    fontWeight = if (useBSPicker) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (useBSPicker) AppTheme.ButtonText else AppTheme.TextSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CoordinatesSection(
    latitude: String,
    longitude: String,
    altitude: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onAltitudeChange: (String) -> Unit,
    latitudeFocusRequester: FocusRequester,
    longitudeFocusRequester: FocusRequester,
    altitudeFocusRequester: FocusRequester,
    onDone: () -> Unit
) {
    Column {
        SectionTitle(stringResource(StringKey.INPUT_COORDINATES))
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChartOutlinedTextField(
                value = latitude,
                onValueChange = onLatitudeChange,
                label = stringResource(StringKey.INPUT_LATITUDE),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(latitudeFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { longitudeFocusRequester.requestFocus() }
                )
            )

            ChartOutlinedTextField(
                value = longitude,
                onValueChange = onLongitudeChange,
                label = stringResource(StringKey.INPUT_LONGITUDE),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(longitudeFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { altitudeFocusRequester.requestFocus() }
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ChartOutlinedTextField(
            value = altitude,
            onValueChange = onAltitudeChange,
            label = stringResource(StringKey.INPUT_ALTITUDE),
            modifier = Modifier.focusRequester(altitudeFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() })
        )
    }
}

@Composable
private fun GenerateButton(
    isCalculating: Boolean,
    onClick: () -> Unit
) {
    val buttonContentDesc = stringResource(StringKey.BTN_GENERATE_SAVE)
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .semantics { contentDescription = buttonContentDesc },
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.AccentGold,
            contentColor = AppTheme.ButtonText,
            disabledContainerColor = AppTheme.AccentGold.copy(alpha = 0.5f),
            disabledContentColor = AppTheme.ButtonText.copy(alpha = 0.5f)
        ),
        enabled = !isCalculating
    ) {
        Crossfade(targetState = isCalculating, label = "button_content") { calculating ->
            if (calculating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AppTheme.ButtonText,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(StringKey.BTN_GENERATE_SAVE),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartDatePickerDialog(
    initialDateMillis: Long,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let(onConfirm)
                }
            ) {
                Text(stringResource(StringKey.BTN_OK), color = AppTheme.AccentGold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(StringKey.BTN_CANCEL), color = AppTheme.TextSecondary)
            }
        },
        colors = DatePickerDefaults.colors(containerColor = AppTheme.CardBackground)
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = AppTheme.CardBackground,
                titleContentColor = AppTheme.TextPrimary,
                headlineContentColor = AppTheme.TextPrimary,
                weekdayContentColor = AppTheme.TextSecondary,
                subheadContentColor = AppTheme.TextSecondary,
                yearContentColor = AppTheme.TextPrimary,
                currentYearContentColor = AppTheme.AccentGold,
                selectedYearContentColor = AppTheme.ButtonText,
                selectedYearContainerColor = AppTheme.AccentGold,
                dayContentColor = AppTheme.TextPrimary,
                selectedDayContentColor = AppTheme.ButtonText,
                selectedDayContainerColor = AppTheme.AccentGold,
                todayContentColor = AppTheme.AccentGold,
                todayDateBorderColor = AppTheme.AccentGold,
                navigationContentColor = AppTheme.TextSecondary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = AppTheme.CardBackground,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(StringKey.INPUT_SELECT_TIME),
                    color = AppTheme.TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        containerColor = AppTheme.CardBackground,
                        clockDialColor = AppTheme.ScreenBackground,
                        clockDialSelectedContentColor = AppTheme.ButtonText,
                        clockDialUnselectedContentColor = AppTheme.TextPrimary,
                        selectorColor = AppTheme.AccentGold,
                        periodSelectorBorderColor = AppTheme.BorderColor,
                        periodSelectorSelectedContainerColor = AppTheme.AccentGold,
                        periodSelectorUnselectedContainerColor = AppTheme.CardBackground,
                        periodSelectorSelectedContentColor = AppTheme.ButtonText,
                        periodSelectorUnselectedContentColor = AppTheme.TextSecondary,
                        timeSelectorSelectedContainerColor = AppTheme.AccentGold,
                        timeSelectorUnselectedContainerColor = AppTheme.CardBackground,
                        timeSelectorSelectedContentColor = AppTheme.ButtonText,
                        timeSelectorUnselectedContentColor = AppTheme.TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(StringKey.BTN_CANCEL), color = AppTheme.TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }
                    ) {
                        Text(stringResource(StringKey.BTN_OK), color = AppTheme.AccentGold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppTheme.TextPrimary,
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun ChartOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = AppTheme.TextSecondary, fontSize = 14.sp) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = chartTextFieldColors(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
    )
}

@Composable
private fun chartTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = AppTheme.TextPrimary,
    unfocusedTextColor = AppTheme.TextPrimary,
    focusedBorderColor = AppTheme.AccentGold,
    unfocusedBorderColor = AppTheme.BorderColor,
    focusedLabelColor = AppTheme.AccentGold,
    unfocusedLabelColor = AppTheme.TextSecondary,
    cursorColor = AppTheme.AccentGold,
    focusedTrailingIconColor = AppTheme.TextSecondary,
    unfocusedTrailingIconColor = AppTheme.TextSecondary
)

@Composable
private fun DateTimeChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(52.dp)
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else Modifier
            ),
        shape = RoundedCornerShape(26.dp),
        color = AppTheme.CardBackground,
        border = BorderStroke(1.dp, AppTheme.BorderColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = AppTheme.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GenderChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) AppTheme.AccentGold else AppTheme.CardBackground,
        border = BorderStroke(1.dp, if (isSelected) AppTheme.AccentGold else AppTheme.BorderColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = if (isSelected) AppTheme.ButtonText else AppTheme.TextPrimary,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
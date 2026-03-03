package com.astro.vajra.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astro.vajra.data.api.GeocodingService
import com.astro.vajra.core.common.StringKey
import com.astro.vajra.core.common.StringKeyUIExtra
import com.astro.vajra.data.localization.stringResource
import com.astro.vajra.ui.theme.LocalAppThemeColors
import com.astro.vajra.ui.components.common.NeoVedicTextField
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.Locale

/**
 * Error type enum for tracking error state without calling @Composable in LaunchedEffect
 */
enum class SearchErrorType {
    NONE,
    NETWORK,
    RATE_LIMIT,
    GENERIC
}

@OptIn(FlowPreview::class)
@Composable
fun LocationSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onLocationSelected: (location: String, latitude: Double, longitude: Double) -> Unit,
    onSearch: suspend (String) -> Result<List<GeocodingService.GeocodingResult>>,
    modifier: Modifier = Modifier,
    label: String = stringResource(StringKey.LOCATION_SEARCH),
    placeholder: String = stringResource(StringKey.LOCATION_PLACEHOLDER),
    searchState: LocationSearchState = rememberLocationSearchState()
) {
    val focusManager = LocalFocusManager.current
    var hasFocus by remember { mutableStateOf(false) }
    val colors = LocalAppThemeColors.current

    // Pre-fetch localized error strings for display
    val rateLimitText = stringResource(StringKey.ERROR_RATE_LIMIT)
    val searchFailedText = stringResource(StringKey.ERROR_SEARCH_FAILED)
    val clearSearchText = stringResource(StringKey.LOCATION_CLEAR)

    // Compute the display error message from error type
    val errorMessage = when (searchState.errorType) {
        SearchErrorType.NONE -> null
        SearchErrorType.NETWORK -> searchState.networkErrorMessage
        SearchErrorType.RATE_LIMIT -> rateLimitText
        SearchErrorType.GENERIC -> searchFailedText
    }

    // MutableStateFlow to track search queries with proper debouncing
    val searchQueryFlow = remember { MutableStateFlow("") }

    // Update the flow whenever value changes
    LaunchedEffect(value) {
        searchQueryFlow.value = value
    }

    // Single LaunchedEffect to handle debounced search
    LaunchedEffect(Unit) {
        searchQueryFlow
            .debounce(400)
            .distinctUntilChanged()
            .collectLatest { query ->
                if (query.length >= 3) {
                    searchState.search(query, onSearch)
                } else {
                    searchState.clear()
                }
            }
    }

    Column(modifier = modifier) {
        NeoVedicTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            placeholder = placeholder,
            leadingIcon = Icons.Outlined.Search,
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    AnimatedVisibility(visible = searchState.isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                            color = colors.AccentPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    AnimatedVisibility(visible = value.isNotEmpty() && !searchState.isSearching) {
                        IconButton(
                            onClick = {
                                onValueChange("")
                                searchState.clear()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = clearSearchText,
                                tint = colors.TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState: FocusState ->
                    hasFocus = focusState.isFocused
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            )
        )

        AnimatedVisibility(visible = errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = colors.ErrorColor,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S12,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        AnimatedVisibility(
            visible = hasFocus && searchState.results.isNotEmpty(),
            enter = fadeIn() + slideInVertically { -it / 4 },
            exit = fadeOut() + slideOutVertically { -it / 4 }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(12.dp),
                color = colors.CardBackground,
                shadowElevation = 8.dp,
                tonalElevation = 2.dp
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    itemsIndexed(
                        items = searchState.results,
                        key = { index, result -> "${result.latitude}_${result.longitude}_$index" }
                    ) { index, result ->
                        LocationResultItem(
                            result = result,
                            onClick = {
                                val formattedName = result.formattedShortName
                                onValueChange(formattedName)
                                onLocationSelected(formattedName, result.latitude, result.longitude)
                                searchState.clear()
                                focusManager.clearFocus()
                            },
                            colors = colors
                        )

                        if (index < searchState.results.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = colors.BorderColor.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationResultItem(
    result: GeocodingService.GeocodingResult,
    onClick: () -> Unit,
    colors: com.astro.vajra.ui.theme.AppThemeColors
) {
    val selectText = stringResource(StringKey.LOCATION_SELECT, result.formattedShortName)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .semantics { contentDescription = selectText },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.AccentPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = colors.AccentPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            val parts = result.displayName.split(",").map { it.trim() }
            val mainName = parts.firstOrNull() ?: result.displayName
            val comma = stringResource(StringKeyUIExtra.COMMA_SPACE)
            val details = if (parts.size > 1) {
                parts.drop(1).take(2).joinToString(comma)
            } else ""

            Text(
                text = mainName,
                color = colors.TextPrimary,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S15,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (details.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = details,
                    color = colors.TextSecondary,
                    fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S13,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = String.format(
                    Locale.US,
                    "%.4f%s, %.4f%s",
                    result.latitude,
                    stringResource(StringKeyUIExtra.DEGREE),
                    result.longitude,
                    stringResource(StringKeyUIExtra.DEGREE)
                ),
                color = colors.TextMuted,
                fontSize = com.astro.vajra.ui.theme.NeoVedicFontSizes.S11,
                letterSpacing = 0.3.sp
            )
        }
    }
}


@Stable
class LocationSearchState {
    var isSearching by mutableStateOf(false)
        private set
    var results by mutableStateOf<List<GeocodingService.GeocodingResult>>(emptyList())
        private set
    var errorType by mutableStateOf(SearchErrorType.NONE)
        private set
    var networkErrorMessage by mutableStateOf<String?>(null)
        private set

    suspend fun search(
        query: String,
        searcher: suspend (String) -> Result<List<GeocodingService.GeocodingResult>>
    ) {
        if (query.length < 3) {
            results = emptyList()
            return
        }

        isSearching = true
        errorType = SearchErrorType.NONE
        networkErrorMessage = null

        val result = searcher(query)
        result.onSuccess { searchResults ->
            results = searchResults
        }.onFailure { e ->
            when (e) {
                is GeocodingService.GeocodingError.NetworkError -> {
                    errorType = SearchErrorType.NETWORK
                    networkErrorMessage = e.message
                }
                is GeocodingService.GeocodingError.RateLimitExceeded -> {
                    errorType = SearchErrorType.RATE_LIMIT
                }
                else -> {
                    errorType = SearchErrorType.GENERIC
                }
            }
            results = emptyList()
        }

        isSearching = false
    }

    fun clear() {
        results = emptyList()
        errorType = SearchErrorType.NONE
        networkErrorMessage = null
        isSearching = false
    }
}

@Composable
fun rememberLocationSearchState(): LocationSearchState {
    return remember { LocationSearchState() }
}

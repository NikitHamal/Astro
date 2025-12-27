package com.astro.storm.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.astro.storm.data.localization.LocalLanguage
import androidx.compose.material3.Divider
import com.astro.storm.data.localization.StringKey
import com.astro.storm.data.localization.StringResources
import com.astro.storm.data.localization.stringResource
import com.astro.storm.data.model.VedicChart
import com.astro.storm.ui.screen.main.uimodel.FeatureCategory
import com.astro.storm.ui.screen.main.uimodel.InsightFeature
import com.astro.storm.ui.screen.main.components.CategoryFilterRow
import com.astro.storm.ui.screen.main.components.EmptyHomeState
import com.astro.storm.ui.screen.main.components.ExpandableCategorySection
import com.astro.storm.ui.screen.main.components.QuickAccessSection
import com.astro.storm.ui.theme.AppTheme

@Composable
fun HomeTab(
    chart: VedicChart?,
    onFeatureClick: (InsightFeature) -> Unit,
    onAddNewChart: () -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 100.dp)
) {
    if (chart == null) {
        EmptyHomeState(onCreateProfile = onAddNewChart, modifier = modifier)
        return
    }

    var selectedCategory by rememberSaveable { mutableStateOf(FeatureCategory.ALL) }
    val language = LocalLanguage.current

    val filteredFeatures = remember(selectedCategory) {
        InsightFeature.getByCategory(selectedCategory)
    }

    val groupedFeatures = remember(filteredFeatures, selectedCategory) {
        if (selectedCategory == FeatureCategory.ALL) {
            filteredFeatures.groupBy { it.category }
                .filterKeys { it != FeatureCategory.ALL }
                .toSortedMap(compareBy { it.ordinal })
        } else {
            mapOf(selectedCategory to filteredFeatures)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.ScreenBackground),
        contentPadding = contentPadding
    ) {
        item(key = "category_filter") {
            CategoryFilterRow(
                categories = FeatureCategory.entries,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (selectedCategory == FeatureCategory.ALL) {
            item(key = "quick_access") {
                QuickAccessSection(
                    features = InsightFeature.pinnedFeatures,
                    onFeatureClick = onFeatureClick,
                    language = language
                )
            }
            item {
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                    color = AppTheme.DividerColor.copy(alpha = 0.5f)
                )
            }
        }

        groupedFeatures.forEach { (category, features) ->
            item(key = "section_${category.name}") {
                ExpandableCategorySection(
                    category = category,
                    features = features,
                    onFeatureClick = onFeatureClick,
                    language = language,
                    initiallyExpanded = selectedCategory != FeatureCategory.ALL
                )
            }
        }

        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

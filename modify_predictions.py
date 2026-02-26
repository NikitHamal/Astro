import re

file_path = "app/src/main/java/com/astro/storm/ui/screen/PredictionsScreen.kt"
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Update EmptyPredictionsState
empty_old = """@Composable
private fun EmptyPredictionsState(modifier: Modifier = Modifier) {
    val language = currentLanguage()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.InsertChart,
                contentDescription = null,
                tint = AppTheme.TextSubtle,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                StringResources.get(StringKey.PREDICTIONS_NO_CHART_SELECTED, language),
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.TextMuted
            )
            Text(
                StringResources.get(StringKey.PREDICTIONS_SELECT_CHART_MESSAGE, language),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.TextSubtle
            )
        }
    }
}"""
empty_new = """@Composable
private fun EmptyPredictionsState(modifier: Modifier = Modifier) {
    val language = currentLanguage()

    NeoVedicEmptyState(
        title = StringResources.get(StringKey.PREDICTIONS_NO_CHART_SELECTED, language),
        subtitle = StringResources.get(StringKey.PREDICTIONS_SELECT_CHART_MESSAGE, language),
        icon = Icons.Outlined.InsertChart,
        modifier = modifier
    )
}"""
content = content.replace(empty_old, empty_new)

# Update LifePathCard surface
content = content.replace("""    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {""", """    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .vedicCornerMarkers(color = AppTheme.AccentPrimary),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.storm.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor),
        shadowElevation = 0.dp
    ) {""", 1) # Only for LifePathCard (first match)

# Update CurrentPeriodCard surface
content = content.replace("""    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {""", """    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .vedicCornerMarkers(color = AppTheme.AccentPrimary),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.storm.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor),
        shadowElevation = 0.dp
    ) {""", 1) # Next match is CurrentPeriodCard

# Update other cards with padding
content = content.replace("""    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {""", """    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.storm.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor),
        shadowElevation = 0.dp
    ) {""")

# Update LifeAreaDetailCard
content = content.replace("""    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {""", """    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable { expanded = !expanded },
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.storm.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor),
        shadowElevation = 0.dp
    ) {""")

# Update RemedialSuggestionsCard
content = content.replace("""    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.CardBackground),
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.ElementCornerRadius)
    ) {""", """    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppTheme.CardBackground,
        shape = RoundedCornerShape(com.astro.storm.ui.theme.NeoVedicTokens.CardCornerRadius),
        border = androidx.compose.foundation.BorderStroke(com.astro.storm.ui.theme.NeoVedicTokens.BorderWidth, AppTheme.BorderColor),
        shadowElevation = 0.dp
    ) {""")

# Typography replacements
def add_font_family(match):
    full_text = match.group(0)
    if 'fontFamily =' in full_text:
        return full_text
    # Determine the style type to insert correct font family
    style_type = match.group(1) # body, label, title, headline
    if style_type == 'label':
        font_family = 'SpaceGroteskFamily'
    elif style_type == 'body':
        font_family = 'PoppinsFontFamily'
    else: # title, headline
        font_family = 'CinzelDecorativeFamily'

    return full_text.replace('style =', f'fontFamily = {font_family},\n                    style =')

# Match 'style = MaterialTheme.typography.XXXX'
pattern = re.compile(r'Text\([^)]*style = MaterialTheme\.typography\.(body|label|title|headline)[^)]*\)')

content = pattern.sub(add_font_family, content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

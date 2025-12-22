package com.astro.storm.data.localization

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A sealed class representing a string that can be localized.
 * This allows ViewModels and other business logic to provide strings
 * without needing access to a `Context` or `@Composable` scope.
 */
sealed class LocalizableString {
    /**
     * A plain, non-localizable string.
     */
    data class Plain(val text: String) : LocalizableString()

    /**
     * A string resource that needs to be resolved.
     */
    data class Resource(val key: StringKeyInterface) : LocalizableString()

    /**
     * A string resource with format arguments.
     */
    data class ResourceWithArgs(val key: StringKeyInterface, val args: List<Any>) : LocalizableString()
}

/**
 * Resolves a [LocalizableString] to a displayable [String] within a Composable context.
 */
@Composable
fun LocalizableString.asString(): String {
    return when (this) {
        is LocalizableString.Plain -> this.text
        is LocalizableString.Resource -> stringResource(this.key)
        is LocalizableString.ResourceWithArgs -> stringResource(this.key, *this.args.toTypedArray())
    }
}

/**
 * Resolves a [LocalizableString] into its final, displayable string format outside of a Composable context.
 * This is essential for use in ViewModels, data layers, or any non-UI logic.
 *
 * @param language The target language for localization.
 * @return The final, localized string.
 */
fun LocalizableString.asPlainText(language: Language): String {
    return when (this) {
        is LocalizableString.Plain -> this.text
        is LocalizableString.Resource -> StringResources.get(this.key, language)
        is LocalizableString.ResourceWithArgs -> StringResources.get(this.key, language, *this.args.toTypedArray())
    }
}

package com.astro.storm.data.localization

import androidx.annotation.StringRes
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

sealed class LocalizableString {
    fun asString(context: Context): String {
        return when (this) {
            is Resource -> context.getString(resId)
            is ResourceWithArgs -> {
                val resolvedArgs = args.map {
                    when (it) {
                        is LocalizableString -> it.asString(context)
                        else -> it.toString()
                    }
                }.toTypedArray()
                context.getString(resId, *resolvedArgs)
            }
            is Plain -> text
        }
    }

    data class Resource(@StringRes val resId: Int) : LocalizableString()
    data class ResourceWithArgs(@StringRes val resId: Int, val args: List<Any>) :
        LocalizableString()

    data class Plain(val text: String) : LocalizableString()
}

@Composable
fun LocalizableString.asString(): String {
    return when (this) {
        is LocalizableString.Resource -> stringResource(id = resId)
        is LocalizableString.ResourceWithArgs -> {
            val resolvedArgs = args.map {
                when (it) {
                    is LocalizableString -> it.asString()
                    else -> it.toString()
                }
            }
            stringResource(id = resId, *resolvedArgs.toTypedArray())
        }

        is LocalizableString.Plain -> text
    }
}

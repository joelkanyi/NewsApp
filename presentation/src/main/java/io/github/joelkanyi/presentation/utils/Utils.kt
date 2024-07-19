package io.github.joelkanyi.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.paging.LoadState
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import io.github.joelkanyi.presentation.R
import retrofit2.HttpException
import java.io.IOException
import java.io.Reader
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.concurrent.TimeUnit

fun String.toISO3166Alpha2(): String {
    return when (this.lowercase()) {
        "kenya" -> "ke"
        "united states" -> "us"
        "united kingdom" -> "gb"
        "australia" -> "au"
        "canada" -> "ca"
        "india" -> "in"
        "germany" -> "de"
        "france" -> "fr"
        "italy" -> "it"
        "netherlands" -> "nl"
        "norway" -> "no"
        "sweden" -> "se"
        "china" -> "cn"
        "japan" -> "jp"
        "south korea" -> "kr"
        "russia" -> "ru"
        "brazil" -> "br"
        "argentina" -> "ar"
        "mexico" -> "mx"
        "south africa" -> "za"
        "nigeria" -> "ng"
        "egypt" -> "eg"
        "saudi arabia" -> "sa"
        "united arab emirates" -> "ae"
        "kuwait" -> "kw"
        else -> ""
    }
}

fun errorMessage(
    httpException: HttpException,
    context: Context,
): String {
    val errorResponse = convertErrorBody<ErrorResponse>(httpException)
    return errorResponse?.message
        ?: context.getString(R.string.an_unknown_error_occurred_please_try_again)
}

inline fun <reified T> convertErrorBody(throwable: HttpException): T? {
    return try {
        throwable.response()?.errorBody()?.charStream()?.use { it.readerToObject() }
    } catch (e: JsonParseException) {
        null
    }
}

inline fun <reified T> Reader.readerToObject(): T {
    val gson =
        GsonBuilder()
            .create()
    return gson.fromJson(this, T::class.java)
}

fun LoadState.Error.getPagingError(context: Context): String {
    return when (val err = this.error) {
        is HttpException -> {
            errorMessage(
                httpException = err,
                context = context
            )
        }

        is IOException -> {
            context.getString(R.string.a_network_error_occurred_please_check_your_connection_and_try_again)
        }

        else -> {
            context.getString(R.string.an_unknown_error_occurred_please_try_again)
        }
    }
}

@SuppressLint("NewApi")
fun String.toRelativeTime(): String {
    try {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val dateTime = ZonedDateTime.parse(this, formatter)
        val now = ZonedDateTime.now()

        val duration = ChronoUnit.SECONDS.between(dateTime, now)

        val years = TimeUnit.SECONDS.toDays(duration) / 365
        val months = TimeUnit.SECONDS.toDays(duration) / 30
        val weeks = TimeUnit.SECONDS.toDays(duration) / 7
        val days = TimeUnit.SECONDS.toDays(duration)
        val hours = TimeUnit.SECONDS.toHours(duration)
        val minutes = TimeUnit.SECONDS.toMinutes(duration)

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
            months > 0 -> "$months month${if (months > 1) "s" else ""} ago"
            weeks > 0 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "$duration second${if (duration > 1) "s" else ""} ago"
        }
    } catch (e: Exception) {
        return ""
    }
}

/** Given 2024-07-11T02:48:00Z, return Friday, 11 July 2024, 02:48 AM */
@SuppressLint("SimpleDateFormat")
fun String.toHumanReadableDateTIme(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = formatter.parse(this)
        val humanReadableDate = SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm a").format(date as Date)
        humanReadableDate
    } catch (e: Exception) {
        this
    }
}

fun Context.shareLink(url: String) {
    try {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(
            sendIntent,
            null
        )

        startActivity(shareIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Int.getThemeName(context: Context): String {
    return when (this) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> context.getString(R.string.use_system_settings)
        AppCompatDelegate.MODE_NIGHT_NO -> context.getString(R.string.light_mode)
        AppCompatDelegate.MODE_NIGHT_YES -> context.getString(R.string.dark_mode)
        12 -> context.getString(R.string.material_you)
        else -> context.getString(R.string.use_system_settings)
    }
}

fun Int.getLanguageName(context: Context): String {
    return when (this) {
        LANGUAGE_SYSTEM_DEFAULT -> context.getString(R.string.follow_system)
        1 -> context.getString(R.string.la_en)
        2 -> context.getString(R.string.la_sw)
        else -> context.getString(R.string.follow_system)
    }
}

const val LANGUAGE_SYSTEM_DEFAULT = 0

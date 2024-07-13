package io.github.joelkanyi.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.paging.LoadState
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
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

fun errorMessage(httpException: HttpException): String {
    val errorResponse = convertErrorBody<ErrorResponse>(httpException)
    return errorResponse?.message ?: "An unknown error occurred, please try again"
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

fun LoadState.Error.getPagingError(): String {
    return when (val err = this.error) {
        is HttpException -> {
            errorMessage(err)
        }

        is IOException -> {
            "A network error occurred, please check your connection and try again"
        }

        else -> {
            "An unknown error occurred, please try again"
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

/**
 * Given 2024-07-11T02:48:00Z, return Friday, 11 July 2024, 02:48 AM
 */
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

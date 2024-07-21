/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.utils

import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    const val PAGE_SIZE = 10
    const val BASE_URL = "https://newsapi.org/v2/"
    const val DATABASE_NAME = "news.db"
    internal const val NEWS_TABLE_NAME = "news"
    const val NEWS_APP_PREFERENCES = "news_app_preferences"
    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
    val LANGUAGE_KEY = intPreferencesKey(name = "language_key")
}

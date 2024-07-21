/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.github.joelkanyi.data.utils.Constants
import io.github.joelkanyi.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferenceRepository {
    override suspend fun saveTheme(themeValue: Int) {
        dataStore.edit { preferences ->
            preferences[Constants.THEME_OPTIONS] = themeValue
        }
    }

    override fun getTheme(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[Constants.THEME_OPTIONS] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    override suspend fun setLanguage(languageNumber: Int) {
        dataStore.edit { preferences ->
            preferences[Constants.LANGUAGE_KEY] = languageNumber
        }
    }

    override fun getLanguage(): Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[Constants.LANGUAGE_KEY] ?: LANGUAGE_SYSTEM_DEFAULT
        }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        const val LANGUAGE_SYSTEM_DEFAULT = 0
    }
}

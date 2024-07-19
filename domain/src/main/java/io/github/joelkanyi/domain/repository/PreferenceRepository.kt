package io.github.joelkanyi.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    suspend fun saveTheme(themeValue: Int)
    fun getTheme(): Flow<Int>

    suspend fun setLanguage(languageNumber: Int)
    fun getLanguage(): Flow<Int>
}

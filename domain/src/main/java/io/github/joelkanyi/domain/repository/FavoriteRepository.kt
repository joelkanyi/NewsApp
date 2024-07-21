/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.domain.repository

import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addFavorite(news: News)

    suspend fun removeFavorite(news: News)

    fun getFavorites(): Flow<List<News>>

    fun isFavorite(news: News): Flow<Boolean>
}

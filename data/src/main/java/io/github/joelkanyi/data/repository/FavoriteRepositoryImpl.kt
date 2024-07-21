/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.repository

import io.github.joelkanyi.data.cache.NewsDao
import io.github.joelkanyi.data.cache.NewsEntity.Companion.toEntity
import io.github.joelkanyi.data.cache.NewsEntity.Companion.toNews
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao
) : FavoriteRepository {
    override suspend fun addFavorite(news: News) {
        newsDao.insert(news.toEntity())
    }

    override suspend fun removeFavorite(news: News) {
        newsDao.delete(news.toEntity())
    }

    override fun getFavorites(): Flow<List<News>> = newsDao.getAll().map { newsList ->
        newsList.map {
            it.toNews()
        }
    }

    override fun isFavorite(news: News): Flow<Boolean> = newsDao.isFavorite(news.title)
}

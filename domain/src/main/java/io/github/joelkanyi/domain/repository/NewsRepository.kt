/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.domain.repository

import androidx.paging.PagingData
import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(
        country: String?,
        category: String?,
        searchQuery: String?
    ): Flow<PagingData<News>>
}

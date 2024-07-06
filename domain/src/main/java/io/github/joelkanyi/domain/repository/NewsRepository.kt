package io.github.joelkanyi.domain.repository

import androidx.paging.PagingData
import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(
        country: String?,
        category: String?,
    ): Flow<PagingData<News>>
}

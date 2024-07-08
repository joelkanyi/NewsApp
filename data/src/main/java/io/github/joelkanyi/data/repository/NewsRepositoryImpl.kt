package io.github.joelkanyi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.joelkanyi.data.network.NewsApi
import io.github.joelkanyi.data.paging.NewsPagingSource
import io.github.joelkanyi.data.utils.Constants.PAGE_SIZE
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
) : NewsRepository {
    override suspend fun getNews(
        country: String?,
        category: String?,
        searchQuery: String?,
    ): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApi = newsApi,
                    country = country,
                    category = category,
                    searchQuery = searchQuery,
                )
            }
        ).flow
    }
}
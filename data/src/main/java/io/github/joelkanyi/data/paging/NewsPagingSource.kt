package io.github.joelkanyi.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.joelkanyi.data.network.NewsApi
import io.github.joelkanyi.domain.model.News

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val country: String?,
    private val category: String?,
) : PagingSource<Int, News>() {
    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        TODO("Not yet implemented")
    }
}
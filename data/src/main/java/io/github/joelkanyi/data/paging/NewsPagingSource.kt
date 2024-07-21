/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.joelkanyi.data.network.NewsApi
import io.github.joelkanyi.data.network.dto.NewsResponseDto.Companion.toNews
import io.github.joelkanyi.domain.model.News

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val country: String?,
    private val category: String?,
    private val searchQuery: String?
) : PagingSource<Int, News>() {
    override fun getRefreshKey(state: PagingState<Int, News>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE_INDEX
            val response =
                newsApi.fetchNews(
                    country = country,
                    category = category,
                    searchQuery = searchQuery,
                    pageSize = params.loadSize,
                    page = nextPage
                )

            LoadResult.Page(
                data = response.articles.map { it.toNews() },
                prevKey = if (nextPage == FIRST_PAGE_INDEX) null else nextPage - 1,
                nextKey = if (response.articles.isEmpty()) null else nextPage + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        const val FIRST_PAGE_INDEX = 0
    }
}

package io.github.joelkanyi.data.paging

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import io.github.joelkanyi.data.network.FakeNewsApi
import io.github.joelkanyi.data.utils.NewsFactory
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NewsPagingSourceTest {
    private val newsFactory = NewsFactory()
    private val fakeNews = listOf(
        newsFactory.createNews(),
        newsFactory.createNews(),
        newsFactory.createNews(),
    )


    private val fakeNewsApi = FakeNewsApi().apply {
        addNews(fakeNews)
    }

    @Test
    fun `load returns LoadResult Page`() = runTest {
        val pagingSource = NewsPagingSource(
            newsApi = fakeNewsApi,
            country = null,
            category = null,
            searchQuery = null
        )

        val expected = PagingSource.LoadResult.Page(
            data = fakeNews,
            prevKey = null,
            nextKey = 1
        )

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `load returns LoadResult Error`() = runTest {
        fakeNewsApi.shouldThrowException = true

        val pagingSource = NewsPagingSource(
            newsApi = fakeNewsApi,
            country = null,
            category = null,
            searchQuery = null
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        assertThat(result is PagingSource.LoadResult.Error).isTrue()
    }
}
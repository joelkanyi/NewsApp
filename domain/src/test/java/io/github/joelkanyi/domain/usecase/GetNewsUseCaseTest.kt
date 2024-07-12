package io.github.joelkanyi.domain.usecase

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.repository.NewsRepository
import io.github.joelkanyi.domain.usecase.news.GetNewsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNewsUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: NewsRepository

    private lateinit var getNewsUseCase: GetNewsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getNewsUseCase = GetNewsUseCase(repository)
    }

    @Test
    fun `get news from repository`() =
        runBlocking {
            // Given
            val news = sampleNews()
            coEvery { repository.getNews(any(), any(), any()) } returns flowOf(news)

            // When
            val result = getNewsUseCase(null, null).first()

            // Then
            assertThat(result).isEqualTo(news)
            coVerify { repository.getNews(any(), any(), any()) }
        }

    private fun sampleNews(): PagingData<News> {
        return PagingData.from(
            listOf(
                News(
                    title = "title",
                    description = "description",
                    url = "url",
                    imageUrl = "urlToImage",
                    publishedAt = "publishedAt",
                    content = "content",
                    source = "source",
                    author = "author",
                ),
            ),
        )
    }
}

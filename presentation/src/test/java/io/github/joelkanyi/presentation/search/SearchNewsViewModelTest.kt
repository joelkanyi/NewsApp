package io.github.joelkanyi.presentation.search

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.usecase.SearchNewsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    private lateinit var searchNewsUseCase: SearchNewsUseCase

    private lateinit var viewModel: SearchNewsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchNewsViewModel(searchNewsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test updateSearchValue should update search value`() = runTest {
        // Given
        val expectedString = "searchQuery"

        // When
        viewModel.updateSearchValue(expectedString)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchValue).isEqualTo(expectedString)
        }
    }

    @Test
    fun `test initial ui state should have default values`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchValue).isEmpty()
            assertThat(state.news).isNull()
        }
    }

    @Test
    fun `test getNews should update news with debounce`() = runTest {
        val news = sampleNews()
        coEvery { searchNewsUseCase.invoke(any()) } returns flowOf(news)

        viewModel.getNews("searchQuery")

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.news).isNotNull()
        }
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
                    author = "author"
                )
            )
        )
    }
}
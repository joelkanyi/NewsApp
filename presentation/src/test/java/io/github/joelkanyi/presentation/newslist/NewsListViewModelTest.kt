/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.newslist

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.usecase.news.GetNewsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: NewsListViewModel

    @RelaxedMockK
    private lateinit var getNewsUseCase: GetNewsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = NewsListViewModel(getNewsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @Test
    fun `test selectCategory updates state with value`() =
        runTest {
            val expectedCategory = "business"

            viewModel.selectCategory("business")

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.selectedCategory).isEqualTo(expectedCategory)
            }
        }

    @Test
    fun `test selectCountry updates state with value`() =
        runTest {
            val expectedCountry = "kenya"

            viewModel.selectCountry("kenya")

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.selectedCountry).isEqualTo(expectedCountry)
            }
        }

    @Test
    fun `test setCountriesDialogState updates state with value`() =
        runTest {
            viewModel.setCountriesDialogState(true)

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.showCountryDialog).isEqualTo(true)
            }
        }

    @Test
    fun `test initial state has default values`() =
        runTest {
            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.selectedCategory).isEqualTo("All News")
                assertThat(state.showCountryDialog).isFalse()
                assertThat(state.selectedCountry).isEqualTo(countries.first())
                // assertThat(state.news).isNull()
            }
        }

    @Test
    fun `test getNews updates state with news`() =
        runTest {
            val news = sampleNews()
            coEvery { getNewsUseCase.invoke(any(), any()) } returns flowOf(news)

            viewModel.getNews("US", "Business")

            viewModel.uiState.test {
                val uiState = awaitItem()
                assertThat(uiState.news).isNotNull()
            }
        }

    private fun sampleNews(): PagingData<News> = PagingData.from(
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

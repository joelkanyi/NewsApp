package io.github.joelkanyi.presentation.newsdetails

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.joelkanyi.domain.usecase.favorite.AddFavoriteUseCase
import io.github.joelkanyi.domain.usecase.favorite.IsFavoriteUseCase
import io.github.joelkanyi.domain.usecase.favorite.RemoveFavoriteUseCase
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.model.NewsUiModel.Companion.toNews
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: NewsDetailsViewModel

    @RelaxedMockK
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @RelaxedMockK
    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @RelaxedMockK
    private lateinit var isFavoriteUseCase: IsFavoriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = NewsDetailsViewModel(
            addFavoriteUseCase = addFavoriteUseCase,
            removeFavoriteUseCase = removeFavoriteUseCase,
            isFavoriteUseCase = isFavoriteUseCase
        )
    }

    @Test
    fun test_addFavorite_AddsNewsToFavorites() = runTest {
        val news = NewsUiModel(
            title = "title",
            description = "description",
            url = "url",
            imageUrl = "imageUrl",
            source = "source",
            publishedAt = "publishedAt",
            content = "content",
            author = "author"
        )

        viewModel.addFavorite(news)

        coVerify { addFavoriteUseCase(news.toNews()) }
    }

    @Test
    fun test_removeFavorite_RemovesNewsFromFavorites() = runTest {
        val news = NewsUiModel(
            title = "title",
            description = "description",
            url = "url",
            imageUrl = "imageUrl",
            source = "source",
            publishedAt = "publishedAt",
            content = "content",
            author = "author"
        )

        viewModel.removeFavorite(news)

        coVerify { removeFavoriteUseCase(news.toNews()) }
    }

    @Test
    fun test_isFavorite_ReturnsTrueIfNewsIsFavorite() = runTest {
        val news = NewsUiModel(
            title = "title",
            description = "description",
            url = "url",
            imageUrl = "imageUrl",
            source = "source",
            publishedAt = "publishedAt",
            content = "content",
            author = "author"
        )

        coEvery { isFavoriteUseCase(news.toNews()) } returns flowOf(true)

        val isFavorite = viewModel.isFavorite(news)

        isFavorite.test {
            val value = awaitItem()
            assertThat(value).isTrue()
            awaitComplete()
        }
    }
}

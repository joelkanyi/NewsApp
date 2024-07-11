package io.github.joelkanyi.presentation.newslist

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.joelkanyi.domain.usecase.GetNewsUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class NewsListViewModelTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var viewModel: NewsListViewModel

    @RelaxedMockK
    private lateinit var getNewsUseCase: GetNewsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = NewsListViewModel(getNewsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `test select category`() = runTest {
        val expectedCategory = "business"

        viewModel.selectCategory("business")

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedCategory).isEqualTo(expectedCategory)
        }
    }

    @Test
    fun `test select country`() = runTest {
        val expectedCountry = "kenya"

        viewModel.selectCountry("kenya")

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedCountry).isEqualTo(expectedCountry)
        }
    }

    @Test
    fun `test set countries dialog state`() = runTest {
        viewModel.setCountriesDialogState(true)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showCountryDialog).isEqualTo(true)
        }
    }

    @Test
    fun `test set filters bottom sheet state`() = runTest {
        viewModel.setFiltersBottomSheetState(false)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showNewsFilters).isEqualTo(false)
        }
    }

    @Test
    fun `test initial ui state`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedCategory).isNull()
            assertThat(state.showNewsFilters).isFalse()
            assertThat(state.showCountryDialog).isFalse()
            assertThat(state.selectedCountry).isEqualTo(newsCountries.first())
        }
    }
}


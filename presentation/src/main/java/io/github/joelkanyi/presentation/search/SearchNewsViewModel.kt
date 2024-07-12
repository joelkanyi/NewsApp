package io.github.joelkanyi.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joelkanyi.domain.usecase.SearchNewsUseCase
import io.github.joelkanyi.presentation.model.NewsUiModel.Companion.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel
@Inject
constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchNewsUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun getNews(searchQuery: String) {
        searchJob?.cancel()
        searchJob =
            viewModelScope.launch {
                delay(DEBOUNCE_PERIOD)
                _uiState.update {
                    it.copy(
                        news = searchNewsUseCase(searchQuery)
                            .map { pagingData ->
                                pagingData.map { news ->
                                    news.toUiModel()
                                }
                            }.cachedIn(viewModelScope),
                    )
                }
            }
    }

    fun updateSearchValue(value: String) {
        _uiState.update {
            it.copy(searchValue = value)
        }
    }

    companion object {
        const val DEBOUNCE_PERIOD = 500L
    }
}

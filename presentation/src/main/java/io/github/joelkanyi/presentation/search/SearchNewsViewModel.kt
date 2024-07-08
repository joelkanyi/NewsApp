package io.github.joelkanyi.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joelkanyi.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchNewsUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun getNews(searchQuery: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    news = getNewsUseCase(
                        country = null,
                        category = null,
                        searchQuery = searchQuery,
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }

    fun updateSearchValue(value: String) {
        _uiState.update {
            it.copy(searchValue = value)
        }
    }
}
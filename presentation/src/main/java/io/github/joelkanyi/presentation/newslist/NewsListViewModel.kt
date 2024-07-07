package io.github.joelkanyi.presentation.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joelkanyi.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsListUiState())
    val uiState = _uiState.asStateFlow()

    fun getNews(
        country: String? = null,
        category: String? = null,
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    news = getNewsUseCase(
                        country = country,
                        category = category
                    ).cachedIn(viewModelScope)
                )
            }
        }
    }

    init {
        getNews(country = "us")
    }
}
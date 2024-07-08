package io.github.joelkanyi.presentation.search

import androidx.paging.PagingData
import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchNewsUiState(
    val searchValue: String = "",
    val news: Flow<PagingData<News>> = emptyFlow(),
)
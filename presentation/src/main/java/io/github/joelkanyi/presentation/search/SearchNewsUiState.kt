package io.github.joelkanyi.presentation.search

import androidx.paging.PagingData
import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow

data class SearchNewsUiState(
    val searchValue: String = "",
    val news: Flow<PagingData<News>>? = null,
)

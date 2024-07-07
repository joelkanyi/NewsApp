package io.github.joelkanyi.presentation.newslist

import androidx.paging.PagingData
import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class NewsListUiState(
    val news: Flow<PagingData<News>> = emptyFlow(),
)

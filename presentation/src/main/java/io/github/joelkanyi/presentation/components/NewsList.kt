/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import io.github.joelkanyi.designsystem.components.EmptyStateComponent
import io.github.joelkanyi.designsystem.components.ErrorStateComponent
import io.github.joelkanyi.designsystem.components.LoadingStateComponent
import io.github.joelkanyi.presentation.R
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.utils.getPagingError

@Composable
fun NewsList(
    onClickNews: (NewsUiModel) -> Unit,
    newsPaging: LazyPagingItems<NewsUiModel>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(newsPaging.itemCount) {
            val news = newsPaging[it]
            if (news != null) {
                NewsItem(
                    news = news,
                    onClick = {
                        onClickNews(news)
                    }
                )
            }
        }
        newsPaging.loadState.let { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingStateComponent()
                        }
                    }
                }

                loadState.refresh is LoadState.NotLoading && newsPaging.itemCount < 1 -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyStateComponent(
                                message = stringResource(R.string.no_news_available)
                            )
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ErrorStateComponent(
                                message = error.getPagingError(context)
                            )
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier =
                                Modifier
                                    .size(16.dp)
                                    .align(Alignment.Center),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

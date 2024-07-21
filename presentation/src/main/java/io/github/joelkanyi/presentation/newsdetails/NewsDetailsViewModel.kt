/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.newsdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.joelkanyi.domain.usecase.favorite.AddFavoriteUseCase
import io.github.joelkanyi.domain.usecase.favorite.IsFavoriteUseCase
import io.github.joelkanyi.domain.usecase.favorite.RemoveFavoriteUseCase
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.model.NewsUiModel.Companion.toNews
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {
    fun addFavorite(news: NewsUiModel) {
        viewModelScope.launch {
            addFavoriteUseCase(news.toNews())
        }
    }

    fun removeFavorite(news: NewsUiModel) {
        viewModelScope.launch {
            removeFavoriteUseCase(news.toNews())
        }
    }

    fun isFavorite(news: NewsUiModel) = isFavoriteUseCase(news.toNews())
}

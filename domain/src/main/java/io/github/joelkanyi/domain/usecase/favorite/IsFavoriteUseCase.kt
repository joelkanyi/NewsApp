package io.github.joelkanyi.domain.usecase.favorite

import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.repository.FavoriteRepository
import javax.inject.Inject

class IsFavoriteUseCase
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) {
        operator fun invoke(news: News) = repository.isFavorite(news)
    }
